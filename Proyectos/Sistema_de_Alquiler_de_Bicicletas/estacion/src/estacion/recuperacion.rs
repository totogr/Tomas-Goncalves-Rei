//! **Recuperación y regularización** del actor `Estacion`: bicis huérfanas
//! (8.2.1), reintento de commits decididos sin confirmar (Caso C) y
//! regularización de alquileres offline cuando vuelve la pasarela (Caso E).
//!
//! Módulo hijo de `estacion`: agrupa estos métodos del actor `Estacion`.

use super::*;

impl Estacion {
    /// Recuperación de bicis huérfanas (8.2.1): el líder no encontró el alquiler
    /// de `bici_id`, así que le preguntamos a cada estación si el alquiler
    /// activo es suyo (`BuscarAlquilerPropio`). Si aparece, se re-reporta al
    /// líder y la devolución se reprocesa; si nadie lo tiene, la bici queda
    /// confirmada huérfana.
    pub(super) fn buscar_alquiler_huerfano(
        &mut self,
        bici_id: BiciId,
        t1: Timestamp,
        ctx: &mut Context<Self>,
    ) {
        // Primero en casa: puede ser un alquiler propio que el líder perdió.
        let propio = self
            .alquileres_propios
            .values()
            .find(|a| a.bici_id == bici_id && a.estado == EstadoAlquiler::Activo)
            .cloned();
        if let Some(alquiler) = propio {
            self.recuperar_huerfana(alquiler, bici_id, t1, ctx);
            return;
        }
        let n = self.proximo();
        let event_id = EventId(format!("E-{}-{}", self.id.0, n));
        let otras: Vec<SocketAddr> = self
            .estaciones
            .iter()
            .filter(|(id, _)| **id != self.id)
            .map(|(_, addr)| *addr)
            .collect();
        let comunicador = self.comunicador.clone();
        println!(
            "[{}] {} no tiene alquiler en el líder: busco a su dueño en las demás estaciones",
            self.id, bici_id
        );
        let fut = async move {
            let (Some(comunicador), Ok(bytes)) = (
                comunicador,
                comun::serializacion::a_bytes(&MensajeEntreEstacionesTCP::BuscarAlquilerPropio {
                    event_id,
                    bici_id,
                }),
            ) else {
                return None;
            };
            for destino in otras {
                let respuesta = comunicador
                    .send(ConsultarTcp {
                        destino,
                        datos: bytes.clone(),
                    })
                    .await;
                if let Ok(Some(datos)) = respuesta {
                    if let Ok(MensajeEntreEstacionesTCP::AlquilerEncontrado { alquiler, .. }) =
                        comun::serializacion::desde_bytes(&datos)
                    {
                        return Some(alquiler);
                    }
                }
            }
            None
        }
        .into_actor(self)
        .map(move |encontrado, actor, ctx| match encontrado {
            Some(alquiler) => actor.recuperar_huerfana(alquiler, bici_id, t1, ctx),
            None => actor.confirmar_huerfana(bici_id),
        });
        ctx.spawn(fut);
    }

    /// Resultado 1 de la 8.2.1 (recuperación automática): el alquiler apareció
    /// en alguna estación. Se re-reporta al líder (evento nuevo, con cola y
    /// dedup) y la devolución se reprocesa una única vez.
    fn recuperar_huerfana(
        &mut self,
        alquiler: Alquiler,
        bici_id: BiciId,
        t1: Timestamp,
        ctx: &mut Context<Self>,
    ) {
        // Un alquiler offline sin regularizar no tiene preauth: no se puede
        // cobrar ni reportar todavía. Queda huérfana (en la práctica la
        // regularización lo resuelve antes de que la bici se devuelva).
        let Some(preauth_id) = alquiler.preauth_id.clone() else {
            self.confirmar_huerfana(bici_id);
            return;
        };
        println!(
            "[{}] alquiler de {} recuperado (origen {}): re-reporto al líder y reproceso",
            self.id, bici_id, alquiler.estacion_origen
        );
        let n = self.proximo();
        let evento = MensajeEntreEstacionesTCP::AlquilerAbierto {
            event_id: EventId(format!("E-{}-{}", self.id.0, n)),
            rental_id: alquiler.rental_id.clone(),
            bici_id: alquiler.bici_id,
            usuario_id: alquiler.usuario_id.clone(),
            estacion_origen: alquiler.estacion_origen,
            t0: alquiler.inicio,
            preauth_id,
        };
        if let RolEstacion::Lider { registro, .. } = &mut self.rol {
            registro.agregar(alquiler);
        } else {
            self.enviar_evento_al_lider(evento, ctx);
        }
        ctx.address().do_send(ProcesarDevolucion {
            bici_id,
            t1,
            ya_reprocesada: true,
            intento: 0,
        });
    }

    /// Resultado 2 de la 8.2.1 (huérfana confirmada): nadie reconoce el
    /// alquiler. La bici queda asegurada en el slot, disponible para alquilarse,
    /// y no se cobra nada (no hay pre-autorización asociada). Queda en el log
    /// para auditoría.
    pub(super) fn confirmar_huerfana(&mut self, bici_id: BiciId) {
        self.huerfanas_confirmadas += 1;
        println!(
            "[{}] AUDITORÍA: {} confirmada huérfana (sin alquiler en ninguna estación); \
             queda disponible, sin cobro",
            self.id, bici_id
        );
    }

    /// Reintenta los commits decididos que la pasarela todavía no confirmó
    /// (Caso C). El re-Commit es seguro: la pasarela es idempotente. Si la
    /// pasarela responde `PreauthAnulada` (la anuló por su propio timeout), no
    /// queda nada que completar y la constancia se descarta.
    pub(super) fn reintentar_commits(&mut self, ctx: &mut Context<Self>) {
        if self.commits_pendientes.is_empty() {
            return;
        }
        let pendientes: Vec<(TransaccionId, String)> = self
            .commits_pendientes
            .iter()
            .map(|(t, p)| (t.clone(), p.clone()))
            .collect();
        println!(
            "[{}] reintento {} commits pendientes contra la pasarela",
            self.id,
            pendientes.len()
        );
        let comunicador = self.comunicador.clone();
        let pasarela = self.pasarela;
        let fut = async move {
            let mut resueltos = Vec::new();
            for (tx_id, preauth_id) in pendientes {
                let commit = MensajeEstacionAPasarela::CommitPreauth {
                    tx_id: tx_id.clone(),
                    preauth_id: preauth_id.clone(),
                };
                match comun::tiempo::con_timeout(
                    TIMEOUT_PREPARE,
                    consultar_pasarela(&comunicador, pasarela, &commit),
                )
                .await
                .flatten()
                {
                    // Confirmada: la preauth quedó Activa en la pasarela.
                    Some(MensajePasarelaAEstacion::PreauthConfirmada { .. }) => {
                        resueltos.push((tx_id, preauth_id, true));
                    }
                    // Anulada: la preauth ya no es cobrable (se anuló por timeout
                    // antes de que llegara el Commit). El alquiler queda sin cobro.
                    Some(MensajePasarelaAEstacion::PreauthAnulada { .. }) => {
                        resueltos.push((tx_id, preauth_id, false));
                    }
                    _ => {} // sin respuesta: sigue pendiente, se reintenta
                }
            }
            resueltos
        }
        .into_actor(self)
        .map(|resueltos, actor, _ctx| {
            if !resueltos.is_empty() {
                for (tx_id, preauth_id, confirmada) in resueltos {
                    actor.commits_pendientes.remove(&tx_id);
                    if confirmada {
                        println!(
                            "[{}] commit OK: la preauth {preauth_id} quedó ACTIVA en la pasarela",
                            actor.id
                        );
                    } else {
                        println!(
                            "[{}] AVISO: la preauth {preauth_id} estaba anulada al commitear; \
                             este alquiler no se va a poder cobrar",
                            actor.id
                        );
                    }
                }
                actor.persistir();
            }
        });
        ctx.spawn(fut);
    }

    /// Si hay un pago pendiente (alquiler offline sin regularizar) para esta bici,
    /// le anota cómo se cerró offline (devuelto/robado): la regularización, tras
    /// conseguir la preauth y reportar el alquiler, disparará ese cierre en vez de
    /// dejarlo abierto. Devuelve `true` si existía el pago (entonces el cierre se
    /// difiere y no hay que avisar al líder ahora, que todavía no lo conoce).
    pub(super) fn anotar_cierre_offline(&mut self, bici_id: BiciId, cierre: CierreOffline) -> bool {
        if let Some(pago) = self
            .pagos_pendientes
            .iter_mut()
            .find(|p| p.bici_id == bici_id)
        {
            pago.cierre = Some(cierre);
            self.persistir();
            true
        } else {
            false
        }
    }

    /// Regularización del Caso E: cuando la pasarela vuelve a estar alcanzable,
    /// cada `PagoPendiente` consigue su preauth diferida. El Commit va por la
    /// maquinaria de commits pendientes (write-ahead + reintentos, Caso C), y
    /// recién con la preauth puesta el alquiler se reporta al líder. Si la
    /// pasarela rechaza la tarjeta, el alquiler queda como CobroFallido
    /// (auditoría): no se va a cobrar.
    pub(super) fn regularizar_pagos(&mut self, ctx: &mut Context<Self>) {
        if self.pagos_pendientes.is_empty() {
            return;
        }
        let comunicador = self.comunicador.clone();
        let pasarela = self.pasarela;
        let pendientes = self.pagos_pendientes.clone();
        let mi_id = self.id;
        let base = self.proximo();
        let fut = async move {
            let mut resultados = Vec::new();
            // Sin la pasarela de vuelta no hay nada que hacer todavía.
            if !consultar_alcanzable(&comunicador, pasarela).await {
                return resultados;
            }
            for (i, pago) in pendientes.into_iter().enumerate() {
                let tx_id = TransaccionId(format!("T-{}-reg{}-{}", mi_id.0, base, i));
                let prepare = MensajeEstacionAPasarela::PreparePreauth {
                    tx_id: tx_id.clone(),
                    usuario_id: pago.usuario_id.clone(),
                    tarjeta: pago.tarjeta.clone(),
                    monto_propuesto: MONTO_RESERVA,
                };
                match comun::tiempo::con_timeout(
                    TIMEOUT_PREPARE,
                    consultar_pasarela(&comunicador, pasarela, &prepare),
                )
                .await
                .flatten()
                {
                    Some(MensajePasarelaAEstacion::Voto {
                        resultado: VotoResultado::Yes,
                        preauth_id: Some(preauth_id),
                        ..
                    }) => resultados.push((pago, tx_id, Ok(preauth_id))),
                    Some(MensajePasarelaAEstacion::Voto {
                        resultado: VotoResultado::No { motivo },
                        ..
                    }) => resultados.push((pago, tx_id, Err(motivo))),
                    // Sin respuesta: el pago sigue pendiente, se reintenta.
                    _ => {}
                }
            }
            resultados
        }
        .into_actor(self)
        .map(|resultados, actor, ctx| {
            let mut alguno_regularizado = false;
            for (pago, tx_id, resultado) in resultados {
                actor
                    .pagos_pendientes
                    .retain(|p| p.rental_id != pago.rental_id);
                match resultado {
                    Ok(preauth_id) => {
                        println!(
                            "[{}] pago regularizado para {}: preauth {} obtenida",
                            actor.id, pago.bici_id, preauth_id
                        );
                        // El Commit va por la maquinaria del Caso C (write-ahead
                        // + reintento); la pasarela cobra igual sobre Preparada.
                        actor.commits_pendientes.insert(tx_id, preauth_id.clone());
                        alguno_regularizado = true;
                        if let Some(alquiler) = actor.alquileres_propios.get_mut(&pago.rental_id) {
                            alquiler.preauth_id = Some(preauth_id.clone());
                            let alquiler = alquiler.clone();
                            // Recién ahora se reporta al líder (regla 7.1.1).
                            // (Si el líder soy yo, reportar lo agrega directo
                            // a mi registro; si no, viaja con la cola.)
                            actor.reportar_alquiler(&alquiler, ctx);
                        }
                        // Si la bici ya se había devuelto o denunciado robada offline
                        // (antes de regularizar), ahora que el alquiler está reportado
                        // y con preauth disparamos su cierre: cobra (uso o reposición)
                        // y cierra. Va DESPUÉS del reporte, así no hay race (primero se
                        // abre, después se cierra).
                        match &pago.cierre {
                            Some(CierreOffline::Devuelto { t1 }) => {
                                println!(
                                    "[{}] liquidación: la bici {} ya se había devuelto offline; cierro y cobro el uso",
                                    actor.id, pago.bici_id
                                );
                                ctx.address().do_send(ProcesarDevolucion {
                                    bici_id: pago.bici_id,
                                    t1: *t1,
                                    ya_reprocesada: false,
                                    intento: 0,
                                });
                            }
                            Some(CierreOffline::Robado) => {
                                println!(
                                    "[{}] liquidación: la bici {} ya se había denunciado robada offline; cierro y cobro la reposición",
                                    actor.id, pago.bici_id
                                );
                                ctx.address().do_send(ProcesarRobo {
                                    rental_id: pago.rental_id.clone(),
                                    bici_id: pago.bici_id,
                                    preauth_id: Some(preauth_id),
                                    estacion_origen: actor.id,
                                    ya_cerrado: false,
                                });
                            }
                            None => {}
                        }
                    }
                    Err(motivo) => {
                        actor.cobros_fallidos += 1;
                        println!(
                            "[{}] AUDITORÍA: regularización de {} rechazada ({motivo}): \
                             CobroFallido, este alquiler no se cobrará",
                            actor.id, pago.bici_id
                        );
                    }
                }
            }
            actor.persistir();
            // Acabamos de meter el Commit de la preauth recién obtenida en
            // `commits_pendientes`. En modo event-driven (producción) nadie más lo
            // dispararía hasta el próximo `PeerConectado`, así que lo mandamos ya:
            // sin esto, la pasarela anularía la preauth por timeout (quedó Preparada
            // pero nunca Commiteada).
            if alguno_regularizado {
                actor.reintentar_commits(ctx);
            }
        });
        ctx.spawn(fut);
    }
}
