//! Flujo de **devolución** (CU2): cierre del alquiler tras recibir la bici.
//!
//! Vive en un módulo hijo de `estacion` para poder acceder a los campos privados
//! del actor `Estacion`. El cobro corre fuera del actor
//! (`resolver_cobro_devolucion`) y su resultado se aplica dentro
//! (`aplicar_salida_devolucion`).

use super::*;

/// Datos que el destino necesita para cobrar y cerrar una devolución (los obtiene
/// del registro del líder).
pub(super) struct DatosCobro {
    pub(super) rental_id: RentalId,
    pub(super) preauth_id: String,
    pub(super) t0: Timestamp,
    pub(super) estacion_origen: EstacionId,
}

/// Resultado de pedirle al líder los datos de cobro de una devolución. Distingue
/// "el líder, alcanzable, dice que no lo conoce" de "no pude hablar con el líder":
/// solo el primero es candidato a huérfana; el segundo hay que reintentarlo.
enum DatosLider {
    /// El líder respondió con los datos: se puede cobrar y cerrar.
    Datos(DatosCobro),
    /// El líder respondió `NoRegistradoAun`: con la red sana, el alquiler no
    /// existe en el registro → candidato a huérfana (protocolo 8.2.1).
    NoRegistrado,
    /// No hubo respuesta del líder (estación offline, líder caído o elección en
    /// curso): NO se puede concluir que sea huérfana; hay que esperar y reintentar.
    Inalcanzable,
}

/// Resultado del trabajo async de una devolución; el `.map` lo traduce a acción.
enum SalidaDevolucion {
    /// El líder, alcanzable, no tiene el alquiler (`NoRegistradoAun`): reintentar;
    /// tras N → protocolo de huérfanas (8.2.1).
    SinDatos,
    /// El líder no está alcanzable (offline / caído / elección en curso): NO se
    /// puede saber si el alquiler existe, así que NO se declara huérfana. Se
    /// reintenta hasta poder consultarlo (igual que `CobroPendiente` con la
    /// pasarela). El alquiler queda abierto mientras tanto.
    LiderPendiente,
    /// El alquiler existe pero la pasarela no está alcanzable para cobrar:
    /// reintentar el cobro hasta que vuelva (NO es una huérfana). El alquiler
    /// queda abierto hasta cobrarse.
    CobroPendiente,
    /// Cobro confirmado: cerrar con el `monto` cobrado.
    Cerrar {
        datos: DatosCobro,
        monto: f64,
        event_id: EventId,
    },
    /// La preauth ya no es cobrable (anulada por timeout, o perdida porque la
    /// pasarela se reinició sin estado): se cierra la devolución SIN cobrar y se
    /// audita como cobro fallido. La bici ya está físicamente de vuelta.
    CerrarSinCobro {
        datos: DatosCobro,
        event_id: EventId,
    },
}

impl Handler<ProcesarDevolucion> for Estacion {
    type Result = ResponseActFuture<Self, ()>;

    fn handle(&mut self, msg: ProcesarDevolucion, ctx: &mut Self::Context) -> Self::Result {
        // Caso E: si la bici corresponde a un alquiler offline sin regularizar (hay
        // un pago pendiente), el líder todavía no lo conoce. No consultamos al líder
        // ni declaramos huérfana: anotamos la devolución en el pago y la
        // regularización lo liquidará (preauth + cobro del uso + cierre).
        if !msg.ya_reprocesada
            && self.anotar_cierre_offline(msg.bici_id, CierreOffline::Devuelto { t1: msg.t1 })
        {
            println!(
                "[{}] devolución (bici {}): alquiler offline sin regularizar; el cierre y el cobro \
                 se liquidan al regularizar",
                self.id, msg.bici_id
            );
            return Box::pin(async {}.into_actor(self).map(|_, _, _| ()));
        }

        // Aislada de la red: no puedo consultar al líder ni cobrar, y si soy líder
        // mi propio registro puede estar viejo (mientras estuve offline pudieron
        // elegir otro). No declaro huérfana: espero a reconectar y reintento. Esto
        // cubre tanto al follower como al (ex-)líder aislado; el caso online con el
        // líder caído lo resuelve `LiderPendiente` más abajo.
        if self.esta_desconectado() {
            println!(
                "[{}] devolución (bici {}): estación aislada, no la declaro huérfana; \
                 reintento al reconectar",
                self.id, msg.bici_id
            );
            let proximo = ProcesarDevolucion {
                bici_id: msg.bici_id,
                t1: msg.t1,
                ya_reprocesada: msg.ya_reprocesada,
                intento: 0,
            };
            ctx.run_later(ESPERA_REINTENTO, move |_, ctx| {
                ctx.address().do_send(proximo);
            });
            return Box::pin(async {}.into_actor(self).map(|_, _, _| ()));
        }

        // Datos de cobro: si soy el líder los busco en mi registro (en proceso);
        // si soy follower los consulto por red (más abajo).
        let es_lider = matches!(self.rol, RolEstacion::Lider { .. });
        let datos_si_lider = if let RolEstacion::Lider { registro, .. } = &self.rol {
            registro.buscar_por_bici(msg.bici_id).and_then(|a| {
                a.preauth_id.clone().map(|preauth_id| DatosCobro {
                    rental_id: a.rental_id.clone(),
                    preauth_id,
                    t0: a.inicio,
                    estacion_origen: a.estacion_origen,
                })
            })
        } else {
            None
        };
        let comunicador = self.comunicador.clone();
        let lider = self.lider;
        let pasarela = self.pasarela;
        let mi_id = self.id;
        let bici_id = msg.bici_id;
        let t1 = msg.t1;
        let ya_reprocesada = msg.ya_reprocesada;
        let intento = msg.intento;
        let n = self.proximo();
        let event_id = EventId(format!("E-{}-{}", self.id.0, n));

        Box::pin(
            resolver_cobro_devolucion(
                es_lider,
                datos_si_lider,
                comunicador,
                lider,
                pasarela,
                mi_id,
                bici_id,
                t1,
                event_id,
            )
            .into_actor(self)
            .map(move |salida, actor, ctx| {
                actor.aplicar_salida_devolucion(salida, ctx, bici_id, t1, ya_reprocesada, intento);
            }),
        )
    }
}

/// Resuelve el cobro de una devolución (fuera del actor): obtiene los datos de
/// cobro —del registro si soy líder, o consultando al líder por red—, cobra a la
/// pasarela y devuelve qué hacer con el alquiler.
#[allow(clippy::too_many_arguments)]
async fn resolver_cobro_devolucion(
    es_lider: bool,
    datos_si_lider: Option<DatosCobro>,
    comunicador: Option<Addr<Comunicador>>,
    lider: SocketAddr,
    pasarela: SocketAddr,
    mi_id: EstacionId,
    bici_id: BiciId,
    t1: Timestamp,
    event_id: EventId,
) -> SalidaDevolucion {
    // UN intento por mensaje: si falla, el reintento se re-despacha como mensaje
    // nuevo (y lee el líder vigente en ESE momento; si hubo elección, el nuevo).
    let datos = if es_lider {
        // Soy el líder: la fuente de verdad. Si no lo tengo en el registro, es un
        // candidato genuino a huérfana (no hay "inalcanzable" contra uno mismo).
        match datos_si_lider {
            Some(d) => d,
            None => return SalidaDevolucion::SinDatos,
        }
    } else {
        match consultar_lider_devolucion(&comunicador, lider, event_id.clone(), bici_id, mi_id, t1)
            .await
        {
            DatosLider::Datos(d) => d,
            // El líder, alcanzable, niega el alquiler → camino de huérfanas.
            DatosLider::NoRegistrado => return SalidaDevolucion::SinDatos,
            // No alcanzamos al líder → esperar y reintentar (NO huérfana).
            DatosLider::Inalcanzable => return SalidaDevolucion::LiderPendiente,
        }
    };
    // Cobro a la pasarela (proporcional a t1 - t0).
    let cobro = MensajeEstacionAPasarela::ProcesarCobro {
        preauth_id: datos.preauth_id.clone(),
        t0: datos.t0,
        t1,
    };
    match consultar_pasarela(&comunicador, pasarela, &cobro).await {
        Some(MensajePasarelaAEstacion::CobroConfirmado { monto, .. }) => SalidaDevolucion::Cerrar {
            datos,
            monto,
            event_id,
        },
        // La preauth ya no es cobrable (anulada por timeout, o perdida si la
        // pasarela se reinició sin estado): se cierra sin cobrar.
        Some(_) => SalidaDevolucion::CerrarSinCobro { datos, event_id },
        // Pasarela inalcanzable: NO cerramos el alquiler; reintentamos el cobro
        // (cuando la pasarela vuelva, se cobra y recién ahí se cierra).
        None => SalidaDevolucion::CobroPendiente,
    }
}

impl Estacion {
    /// Aplica el resultado de una devolución (dentro del actor): cierra (cobrado o
    /// sin cobro, con su auditoría), o reprograma el reintento (cobro pendiente, o
    /// sin datos → eventual huérfana).
    fn aplicar_salida_devolucion(
        &mut self,
        salida: SalidaDevolucion,
        ctx: &mut Context<Self>,
        bici_id: BiciId,
        t1: Timestamp,
        ya_reprocesada: bool,
        intento: u32,
    ) {
        let (datos, monto, event_id, cobrado) = match salida {
            SalidaDevolucion::Cerrar {
                datos,
                monto,
                event_id,
            } => (datos, monto, event_id, true),
            // La preauth ya no es cobrable: cerrar la devolución sin cobro y
            // auditarlo (la bici ya volvió; no se puede cobrar este alquiler).
            SalidaDevolucion::CerrarSinCobro { datos, event_id } => {
                self.cobros_fallidos += 1;
                (datos, 0.0, event_id, false)
            }
            // El alquiler existe pero la pasarela no está: reintentar el cobro
            // (sin contar contra el límite de huérfanas; el alquiler sigue abierto).
            SalidaDevolucion::CobroPendiente => {
                println!(
                    "[{}] devolución (bici {bici_id}): pasarela inalcanzable, reintento el cobro al volver",
                    self.id
                );
                let proximo = ProcesarDevolucion {
                    bici_id,
                    t1,
                    ya_reprocesada,
                    intento: 0,
                };
                ctx.run_later(ESPERA_REINTENTO, move |_, ctx| {
                    ctx.address().do_send(proximo);
                });
                return;
            }
            // No alcanzamos al líder (offline / caído / elección): NO declaramos
            // huérfana. Reintentamos con `intento: 0`, así no cuenta contra el tope
            // de huérfanas; cuando la red vuelva (o se elija líder), el reintento
            // obtiene una respuesta definitiva. Mismo criterio que `CobroPendiente`.
            SalidaDevolucion::LiderPendiente => {
                println!(
                    "[{}] devolución (bici {bici_id}): líder inalcanzable, no la declaro huérfana; \
                     reintento cuando vuelva la red",
                    self.id
                );
                let proximo = ProcesarDevolucion {
                    bici_id,
                    t1,
                    ya_reprocesada,
                    intento: 0,
                };
                ctx.run_later(ESPERA_REINTENTO, move |_, ctx| {
                    ctx.address().do_send(proximo);
                });
                return;
            }
            SalidaDevolucion::SinDatos => {
                // ¿Quedan reintentos? Se reprograma con espera creciente.
                if intento + 1 < REINTENTOS_NOTIFICACION {
                    let proximo = ProcesarDevolucion {
                        bici_id,
                        t1,
                        ya_reprocesada,
                        intento: intento + 1,
                    };
                    ctx.run_later(ESPERA_REINTENTO * (intento + 1), move |_, ctx| {
                        ctx.address().do_send(proximo);
                    });
                    return;
                }
                // Agotados los reintentos: protocolo de huérfanas (8.2.1). Si esto ya
                // era el reproceso post-recuperación, se confirma huérfana directo.
                if ya_reprocesada {
                    self.confirmar_huerfana(bici_id);
                } else {
                    self.buscar_alquiler_huerfano(bici_id, t1, ctx);
                }
                return;
            }
        };
        let tiempo = datos.t0.minutos_hasta(t1);
        if cobrado {
            println!(
                "[{}] devolución: cobré ${monto} a la pasarela (preauth {}, {tiempo} min); \
                 cierro {:?} en el líder y aviso al origen {}",
                self.id, datos.preauth_id, datos.rental_id, datos.estacion_origen
            );
        } else {
            println!(
                "[{}] devolución: la preauth {} ya no es cobrable (anulada o perdida); \
                 cierro {:?} SIN COBRO (auditado) y aviso al origen {}",
                self.id, datos.preauth_id, datos.rental_id, datos.estacion_origen
            );
        }

        // Cerrar en el líder (en proceso si soy líder; por red si no, con cola de
        // diferidos si no está disponible).
        if let RolEstacion::Lider { registro, .. } = &mut self.rol {
            registro.cerrar(&datos.rental_id);
        } else {
            self.enviar_evento_al_lider(
                MensajeEntreEstacionesTCP::DevolucionProcesada {
                    event_id,
                    rental_id: datos.rental_id.clone(),
                    monto_cobrado: monto,
                    tiempo_uso_minutos: tiempo,
                },
                ctx,
            );
        }

        // Cerrar en el origen (en proceso si el origen soy yo; por red si no).
        if datos.estacion_origen == self.id {
            if let Some(alquiler) = self.alquileres_propios.get_mut(&datos.rental_id) {
                alquiler.estado = EstadoAlquiler::Cerrado;
                self.persistir();
            }
        } else if let Some(destino) = self.estaciones.get(&datos.estacion_origen).copied() {
            if let (Some(comunicador), Ok(bytes)) = (
                &self.comunicador,
                comun::serializacion::a_bytes(&MensajeEntreEstacionesTCP::CierreAlquiler {
                    rental_id: datos.rental_id.clone(),
                    t1,
                    monto_cobrado: monto,
                }),
            ) {
                comunicador.do_send(EnviarTcp {
                    destino,
                    datos: bytes,
                });
            }
        }
    }
}

/// Consulta al líder (por red) los datos de cobro de una bici que se está
/// devolviendo. Distingue la negación explícita del líder (`NoRegistradoAun` →
/// `NoRegistrado`) de la falta de respuesta (offline / líder caído / elección →
/// `Inalcanzable`): solo la primera habilita el protocolo de huérfanas.
async fn consultar_lider_devolucion(
    comunicador: &Option<Addr<Comunicador>>,
    lider: SocketAddr,
    event_id: EventId,
    bici_id: comun::BiciId,
    estacion_destino: EstacionId,
    t1: Timestamp,
) -> DatosLider {
    let Some(comunicador) = comunicador.as_ref() else {
        return DatosLider::Inalcanzable;
    };
    let notif = MensajeEntreEstacionesTCP::NotificarDevolucion {
        event_id,
        bici_id,
        estacion_destino,
        t1,
    };
    let Ok(bytes) = comun::serializacion::a_bytes(&notif) else {
        return DatosLider::Inalcanzable;
    };
    // Sin respuesta (None / error de envío): el líder no está alcanzable.
    let respuesta = comunicador
        .send(ConsultarTcp {
            destino: lider,
            datos: bytes,
        })
        .await
        .ok()
        .flatten();
    let Some(payload) = respuesta else {
        return DatosLider::Inalcanzable;
    };
    match comun::serializacion::desde_bytes::<MensajeEntreEstacionesTCP>(&payload) {
        Ok(MensajeEntreEstacionesTCP::DatosParaCobro {
            rental_id,
            preauth_id,
            t0,
            estacion_origen,
            ..
        }) => DatosLider::Datos(DatosCobro {
            rental_id,
            preauth_id,
            t0,
            estacion_origen,
        }),
        // El líder, vivo, niega conocer el alquiler.
        Ok(MensajeEntreEstacionesTCP::NoRegistradoAun { .. }) => DatosLider::NoRegistrado,
        // Respuesta inesperada o ilegible: no podemos concluir que sea huérfana,
        // así que la tratamos como inalcanzable y reintentamos (conservador: no
        // declaramos huérfana por un mensaje raro).
        _ => DatosLider::Inalcanzable,
    }
}
