//! Flujo de **alquiler** (CU1): el coordinador del 2PC (slot + pasarela) y el
//! handler de las operaciones del usuario (`SolicitudUsuario`).
//!
//! Vive en un módulo hijo de `estacion` para acceder a los privados de `Estacion`.
//! Las fases del 2PC corren fuera del actor; el handler aplica el resultado dentro.

use super::*;

/// Contexto inmutable de una operación (sin `&self`), para el trabajo async. Lo
/// construye `Estacion::contexto` y lo consume `procesar_operacion`.
pub(super) struct ContextoOperacion {
    pub(super) tx_id: TransaccionId,
    pub(super) rental_id: RentalId,
    /// Id de evento para la consulta al líder (p. ej. "¿esta bici está robada?").
    pub(super) event_id: EventId,
    pub(super) slots: Vec<Addr<Slot>>,
    pub(super) estacion_origen: EstacionId,
    pub(super) pasarela: SocketAddr,
    /// Dirección del líder (para la consulta de bici robada si soy follower).
    pub(super) lider: SocketAddr,
    pub(super) comunicador: Option<Addr<Comunicador>>,
    /// `Addr` de la propia estación: el 2PC le avisa el commit decidido (para
    /// que lo persista ANTES de mandarlo) y la confirmación de la pasarela.
    pub(super) yo: Addr<Estacion>,
    /// Si la estación está en modo offline: el alquiler entra directo al Caso E
    /// (sin intentar la pasarela), y la devolución cierra/cobra cuando vuelva.
    pub(super) desconectado: bool,
    /// Si soy el líder: tengo localmente la verdad sobre las bicis robadas.
    pub(super) es_lider: bool,
    /// Bicis robadas conocidas por el líder (solo se consulta si `es_lider`).
    pub(super) bicis_robadas: HashSet<BiciId>,
}

/// Lógica del alquiler/devolución, sin estado de la estación. Devuelve la
/// respuesta para el usuario, el alquiler a registrar (si lo hubo) y el pago
/// pendiente de regularización (si el alquiler salió offline, Caso E).
pub(super) async fn procesar_operacion(
    operacion: MensajeUsuarioAEstacion,
    ctx: ContextoOperacion,
) -> (
    MensajeEstacionAUsuario,
    Option<Alquiler>,
    Option<PagoPendiente>,
) {
    let ContextoOperacion {
        tx_id,
        rental_id,
        event_id,
        slots,
        estacion_origen,
        pasarela,
        lider,
        comunicador,
        yo,
        desconectado,
        es_lider,
        bicis_robadas,
    } = ctx;

    match operacion {
        MensajeUsuarioAEstacion::SolicitudAlquiler {
            usuario_id,
            slot_id,
            tarjeta,
        } => {
            let Some(slot) = slots.get(slot_id as usize).cloned() else {
                return (
                    MensajeEstacionAUsuario::AlquilerRechazado {
                        motivo: format!("no existe el slot {slot_id}"),
                    },
                    None,
                    None,
                );
            };

            // Recursos compartidos por las tres fases del 2PC.
            let fases = Fases2PC {
                slot: &slot,
                comunicador: &comunicador,
                pasarela,
                tx_id: &tx_id,
                estacion_origen,
            };

            // FASE PREPARE: votos del slot y (salvo Caso E) de la pasarela.
            let prep = prepare_alquiler(&fases, &usuario_id, &tarjeta, slot_id, desconectado).await;

            // FASE DECISIÓN.
            if prep.voto_slot == Voto::Si && prep.pasarela_ok {
                let bici = commit_alquiler(&fases, &yo, &prep.preauth_id).await;
                match bici {
                    Some(bici_id) => {
                        let inicio = Timestamp::ahora();
                        // Caso E: el pago queda pendiente, con los datos para la
                        // preauth diferida (incluida la tarjeta).
                        let pago = prep.modo_offline.then(|| {
                            println!(
                                "[{estacion_origen}] alquiler de {bici_id} resuelto OFFLINE \
                                 (Caso E): preauth pendiente, sin reporte al líder"
                            );
                            PagoPendiente {
                                rental_id: rental_id.clone(),
                                bici_id,
                                usuario_id: usuario_id.clone(),
                                tarjeta: tarjeta.clone(),
                                t0: inicio,
                                cierre: None,
                            }
                        });
                        let alquiler = Alquiler {
                            rental_id: rental_id.clone(),
                            bici_id,
                            usuario_id,
                            estacion_origen,
                            inicio,
                            fin: None,
                            preauth_id: prep.preauth_id.clone(),
                            estado: EstadoAlquiler::Activo,
                        };
                        (
                            MensajeEstacionAUsuario::AlquilerConfirmado {
                                rental_id,
                                bici_id,
                                preauth_id: prep.preauth_id,
                            },
                            Some(alquiler),
                            pago,
                        )
                    }
                    None => (
                        MensajeEstacionAUsuario::AlquilerRechazado {
                            motivo: "el slot no liberó la bici".to_string(),
                        },
                        None,
                        None,
                    ),
                }
            } else {
                abortar_alquiler(&fases, prep.voto_slot, prep.preauth_id).await;
                // Solo se rechaza por el slot (sin bici) o por un voto No real de la
                // pasarela (tarjeta). Un timeout/inalcanzable resuelve offline (Caso E).
                let motivo = if prep.voto_slot != Voto::Si {
                    "el slot no tenía una bici disponible".to_string()
                } else {
                    "la pasarela rechazó el pago".to_string()
                };
                (
                    MensajeEstacionAUsuario::AlquilerRechazado { motivo },
                    None,
                    None,
                )
            }
        }

        MensajeUsuarioAEstacion::SolicitudDevolucion {
            bici_id, slot_id, ..
        } => {
            // ¿La bici figura como robada? Si soy líder lo sé directo; si soy
            // follower le pregunto al líder. Si no se puede verificar (offline o
            // líder inalcanzable) sigue el camino normal (best-effort, sin aviso).
            let robada = if es_lider {
                bicis_robadas.contains(&bici_id)
            } else if !desconectado {
                consultar_bici_robada(&comunicador, lider, event_id, bici_id).await
            } else {
                false
            };
            if robada {
                println!(
                    "[{estacion_origen}] devolución de la bici {bici_id}: figura como ROBADA; \
                     no la acepto a circulación y aviso a la policía"
                );
                return (
                    MensajeEstacionAUsuario::DevolucionBiciRobada { bici_id },
                    None,
                    None,
                );
            }
            let Some(slot) = slots.get(slot_id as usize).cloned() else {
                return (
                    MensajeEstacionAUsuario::DevolucionRechazada {
                        motivo: format!("no existe el slot {slot_id}"),
                    },
                    None,
                    None,
                );
            };
            match slot.send(AceptarBici { bici_id }).await {
                Ok(true) => {
                    println!(
                        "[{estacion_origen}] devolución: bici {bici_id} asegurada en slot {slot_id}; \
                         arranca el cierre en background (cobro + cierre al origen)"
                    );
                    (
                        MensajeEstacionAUsuario::DevolucionAceptada { bici_id },
                        None,
                        None,
                    )
                }
                _ => (
                    MensajeEstacionAUsuario::DevolucionRechazada {
                        motivo: format!("el slot {slot_id} está ocupado"),
                    },
                    None,
                    None,
                ),
            }
        }

        MensajeUsuarioAEstacion::DenunciarRobo { usuario_id } => {
            // La denuncia se resuelve dentro del actor (necesita el registro / la
            // consulta al líder): se la mandamos a la propia estación y devolvemos
            // su respuesta. El cierre y el cobro de la reposición van en background.
            let respuesta = yo
                .send(DenunciaRobo { usuario_id })
                .await
                .unwrap_or(MensajeEstacionAUsuario::RoboNoProcesado);
            (respuesta, None, None)
        }
    }
}

/// Recursos e identificadores compartidos por las fases del 2PC de alquiler
/// (`prepare_alquiler`, `commit_alquiler`, `abortar_alquiler`). Es `Copy` —todos
/// sus campos lo son (referencias y tipos chicos)— así que se pasa por valor.
#[derive(Clone, Copy)]
struct Fases2PC<'a> {
    slot: &'a Addr<Slot>,
    comunicador: &'a Option<Addr<Comunicador>>,
    pasarela: SocketAddr,
    tx_id: &'a TransaccionId,
    estacion_origen: EstacionId,
}

/// Resultado de la fase Prepare del 2PC de alquiler.
struct PreparacionAlquiler {
    voto_slot: Voto,
    /// `true` si el lado pasarela aprueba (votó Sí, o se omitió por modo offline).
    pasarela_ok: bool,
    /// Pre-autorización obtenida (`None` en modo offline; se difiere).
    preauth_id: Option<String>,
    /// `true` si el alquiler se resuelve offline (Caso E): sin preauth, sin líder.
    modo_offline: bool,
}

/// FASE PREPARE del 2PC de alquiler: pide el voto al `Slot` y —salvo Caso E— a la
/// pasarela, **en paralelo** (los dos participantes votan a la vez, no uno y
/// después el otro). Si la pasarela no responde dentro del plazo, cae a modo
/// offline en vez de abortar (prioriza la disponibilidad; sección 7.1.1 del README).
async fn prepare_alquiler(
    fases: &Fases2PC<'_>,
    usuario_id: &UsuarioId,
    tarjeta: &DatosTarjeta,
    slot_id: u32,
    desconectado: bool,
) -> PreparacionAlquiler {
    let Fases2PC {
        slot,
        comunicador,
        pasarela,
        tx_id,
        estacion_origen,
    } = *fases;
    println!(
        "[{estacion_origen}] 2PC alquiler {tx_id:?}: PREPARE → slot {slot_id} + pasarela (en paralelo)"
    );

    // Lado SLOT (local): reserva de la bici.
    let fut_slot = slot.send(PrepareLiberacion {
        tx_id: tx_id.clone(),
    });

    // Lado PASARELA (remoto): decide el modo offline y —si corresponde— pide la
    // preauth. Devuelve (pasarela_ok, preauth_id, modo_offline).
    let fut_pasarela = async {
        // Caso E: si la estación está desconectada por consola, va directo a
        // offline (ni consulta la pasarela). Si no, se fija si el Comunicador la
        // marcó inalcanzable.
        if desconectado || !consultar_alcanzable(comunicador, pasarela).await {
            return (true, None, true);
        }
        let prepare = MensajeEstacionAPasarela::PreparePreauth {
            tx_id: tx_id.clone(),
            usuario_id: usuario_id.clone(),
            tarjeta: tarjeta.clone(),
            monto_propuesto: MONTO_RESERVA,
        };
        match comun::tiempo::con_timeout(
            TIMEOUT_PREPARE,
            consultar_pasarela(comunicador, pasarela, &prepare),
        )
        .await
        .flatten()
        {
            Some(MensajePasarelaAEstacion::Voto {
                resultado: VotoResultado::Yes,
                preauth_id: Some(id),
                ..
            }) => (true, Some(id), false),
            // Votó No de verdad (tarjeta inválida / sin fondos): se aborta.
            Some(_) => (false, None, false),
            // No respondió en el plazo (timeout / inalcanzable): offline (Caso E).
            None => (true, None, true),
        }
    };

    // Los dos Prepare corren a la vez; se espera a que voten ambos.
    let (resultado_slot, (pasarela_ok, preauth_id, modo_offline)) =
        comun::tiempo::join2(fut_slot, fut_pasarela).await;
    let voto_slot = resultado_slot.unwrap_or(Voto::No);

    println!("[{estacion_origen}] 2PC {tx_id:?}: voto del slot {slot_id} = {voto_slot:?}");
    if modo_offline {
        println!(
            "[{estacion_origen}] 2PC {tx_id:?}: pasarela inalcanzable → resuelvo OFFLINE (Caso E)"
        );
    } else {
        let voto_pasarela = match &preauth_id {
            Some(id) => format!("Sí (preauth {id})"),
            None => "No (tarjeta rechazada)".to_string(),
        };
        println!("[{estacion_origen}] 2PC {tx_id:?}: voto de la pasarela = {voto_pasarela}");
    }
    PreparacionAlquiler {
        voto_slot,
        pasarela_ok,
        preauth_id,
        modo_offline,
    }
}

/// FASE COMMIT del 2PC de alquiler: libera la bici del `Slot` y, si hubo preauth,
/// commitea en la pasarela con la maquinaria del Caso C (constancia persistida
/// ANTES de mandar el Commit + reintento idempotente). Devuelve la bici liberada.
async fn commit_alquiler(
    fases: &Fases2PC<'_>,
    yo: &Addr<Estacion>,
    preauth_id: &Option<String>,
) -> Option<BiciId> {
    let Fases2PC {
        slot,
        comunicador,
        pasarela,
        tx_id,
        estacion_origen,
    } = *fases;
    println!("[{estacion_origen}] 2PC {tx_id:?}: DECISIÓN = COMMIT");
    let bici = slot
        .send(CommitLiberacion {
            tx_id: tx_id.clone(),
        })
        .await
        .ok()
        .flatten();
    // El lado pasarela solo existe si hubo preauth (offline no commitea allá).
    let Some(preauth) = preauth_id else {
        return bici;
    };
    // Caso C: constancia persistida de la decisión ANTES de mandar el Commit.
    let _ = yo
        .send(RegistrarCommitPendiente {
            tx_id: tx_id.clone(),
            preauth_id: preauth.clone(),
        })
        .await;
    let commit = MensajeEstacionAPasarela::CommitPreauth {
        tx_id: tx_id.clone(),
        preauth_id: preauth.clone(),
    };
    // Con plazo: una pasarela colgada acá no debe dejar al usuario esperando
    // (el reintento periódico se encarga).
    let respuesta_commit = comun::tiempo::con_timeout(
        TIMEOUT_PREPARE,
        consultar_pasarela(comunicador, pasarela, &commit),
    )
    .await
    .flatten();
    if matches!(
        respuesta_commit,
        Some(MensajePasarelaAEstacion::PreauthConfirmada { .. })
    ) {
        println!("[{estacion_origen}] 2PC {tx_id:?}: COMMIT → pasarela confirmó preauth {preauth}");
        yo.do_send(CommitConfirmado {
            tx_id: tx_id.clone(),
        });
    } else {
        println!(
            "[{estacion_origen}] 2PC {tx_id:?}: COMMIT a la pasarela sin confirmar (lo completa el reintento)"
        );
    }
    bici
}

/// FASE ABORT del 2PC de alquiler: suelta la reserva del `Slot` (si votó Sí) y
/// anula la pre-autorización en la pasarela (si la hubo).
async fn abortar_alquiler(fases: &Fases2PC<'_>, voto_slot: Voto, preauth_id: Option<String>) {
    let Fases2PC {
        slot,
        comunicador,
        pasarela,
        tx_id,
        estacion_origen,
    } = *fases;
    println!("[{estacion_origen}] 2PC {tx_id:?}: DECISIÓN = ABORT");
    if voto_slot == Voto::Si {
        let _ = slot
            .send(AbortLiberacion {
                tx_id: tx_id.clone(),
            })
            .await;
    }
    if let Some(id) = preauth_id {
        let abort = MensajeEstacionAPasarela::AbortPreauth {
            tx_id: tx_id.clone(),
            preauth_id: id,
        };
        let _ = comun::tiempo::con_timeout(
            TIMEOUT_PREPARE,
            consultar_pasarela(comunicador, pasarela, &abort),
        )
        .await;
    }
}

impl Handler<SolicitudUsuario> for Estacion {
    type Result = ResponseActFuture<Self, MensajeEstacionAUsuario>;

    fn handle(&mut self, msg: SolicitudUsuario, _ctx: &mut Self::Context) -> Self::Result {
        let ctx = self.contexto(_ctx);
        Box::pin(
            async move { procesar_operacion(msg.0, ctx).await }
                .into_actor(self)
                .map(|(respuesta, alquiler, pago), actor, ctx| {
                    if let Some(a) = alquiler {
                        actor.reportar_alquiler(&a, ctx);
                        actor.alquileres_propios.insert(a.rental_id.clone(), a);
                        if let Some(pago) = pago {
                            actor.pagos_pendientes.push(pago);
                        }
                        actor.persistir();
                    }
                    if let MensajeEstacionAUsuario::DevolucionAceptada { bici_id } = &respuesta {
                        ctx.address().do_send(ProcesarDevolucion {
                            bici_id: *bici_id,
                            t1: Timestamp::ahora(),
                            ya_reprocesada: false,
                            intento: 0,
                        });
                    }
                    respuesta
                }),
        )
    }
}
