//! Flujo de **detección de robos**: el usuario denuncia que le robaron la bici
//! mientras tenía un alquiler abierto. El robo es una tercera forma de cerrar el
//! alquiler: cobra la reposición (el monto reservado completo) y saca la bici de
//! circulación. La denuncia se puede hacer en cualquier estación.
//!
//! Módulo hijo de `estacion`: usa los privados del actor `Estacion`.

use super::*;

/// Datos del alquiler robado que el cierre necesita (los obtiene del registro del
/// líder o del alquiler propio local).
pub(super) struct DatosRobo {
    rental_id: RentalId,
    bici_id: BiciId,
    /// `None` si el alquiler es offline sin regularizar (todavía sin preauth): la
    /// reposición no se puede cobrar hasta que se regularice.
    preauth_id: Option<String>,
    estacion_origen: EstacionId,
}

impl DatosRobo {
    fn de_alquiler(a: &Alquiler) -> Self {
        DatosRobo {
            rental_id: a.rental_id.clone(),
            bici_id: a.bici_id,
            preauth_id: a.preauth_id.clone(),
            estacion_origen: a.estacion_origen,
        }
    }
}

impl Estacion {
    /// Busca el alquiler activo del usuario en lo que esta estación conoce: su
    /// registro (si es líder) o sus alquileres propios (incluye el offline sin
    /// regularizar). `None` si no lo tiene localmente (habrá que ir al líder).
    fn buscar_robo_local(&self, usuario_id: &UsuarioId) -> Option<DatosRobo> {
        if let RolEstacion::Lider { registro, .. } = &self.rol {
            if let Some(a) = registro.buscar_por_usuario(usuario_id) {
                return Some(DatosRobo::de_alquiler(a));
            }
        }
        self.alquileres_propios
            .values()
            .find(|a| &a.usuario_id == usuario_id && a.estado == EstadoAlquiler::Activo)
            .map(DatosRobo::de_alquiler)
    }

    /// Verifica, al vencer su timer, si un alquiler superó la duración máxima sin
    /// devolverse: si sigue activo en el registro del líder, lo cierra como Robado
    /// por inactividad (mismo flujo que una denuncia: marca robado, saca la bici de
    /// circulación, avisa al origen y cobra la reposición —reintentada si la
    /// pasarela está caída—). Si ya se devolvió/robó, o yo no soy el líder, no hace
    /// nada (el plazo se recalcula desde el inicio, así que un timer viejo de un
    /// alquiler ya cerrado es inofensivo).
    pub(super) fn verificar_robo_por_timeout(
        &mut self,
        rental_id: RentalId,
        ctx: &mut Context<Self>,
    ) {
        let datos = {
            let RolEstacion::Lider { registro, .. } = &self.rol else {
                return; // solo el líder detecta el robo por inactividad
            };
            match registro.buscar(&rental_id) {
                Some(a) if a.estado == EstadoAlquiler::Activo => DatosRobo::de_alquiler(a),
                // El timer venció pero el alquiler ya no está activo (se devolvió o
                // ya se cerró por robo) o el registro ya no lo tiene: nada que hacer.
                ya_no_activo => {
                    println!(
                        "[{}] (líder) venció el timer de inactividad de {rental_id:?}, pero {}: \
                         no lo marco robado",
                        self.id,
                        match ya_no_activo {
                            Some(_) => "ya está devuelto/robado",
                            None => "ya no está en el registro",
                        }
                    );
                    return;
                }
            }
        };
        println!(
            "[{}] (líder) {rental_id:?} (bici {}) superó la duración máxima sin devolverse: \
             lo marco ROBADO por inactividad",
            self.id, datos.bici_id
        );
        ctx.address().do_send(ProcesarRobo {
            rental_id: datos.rental_id,
            bici_id: datos.bici_id,
            preauth_id: datos.preauth_id,
            estacion_origen: datos.estacion_origen,
            ya_cerrado: false,
        });
    }

    /// Dispara el cierre del robo en background y devuelve la respuesta al usuario.
    fn registrar_robo(
        &mut self,
        datos: DatosRobo,
        ctx: &mut Context<Self>,
    ) -> MensajeEstacionAUsuario {
        let bici_id = datos.bici_id;
        ctx.address().do_send(ProcesarRobo {
            rental_id: datos.rental_id,
            bici_id,
            preauth_id: datos.preauth_id,
            estacion_origen: datos.estacion_origen,
            ya_cerrado: false,
        });
        MensajeEstacionAUsuario::RoboRegistrado { bici_id }
    }

    /// Cierra un alquiler como robado: lo marca en el líder (en proceso si soy
    /// líder; por red, con cola de diferidos, si no), saca la bici de circulación,
    /// marca mi copia local y avisa al origen.
    fn cerrar_robo(
        &mut self,
        rental_id: &RentalId,
        bici_id: BiciId,
        estacion_origen: EstacionId,
        ctx: &mut Context<Self>,
    ) {
        // Mi copia local del alquiler queda robada (siempre).
        if let Some(a) = self.alquileres_propios.get_mut(rental_id) {
            a.estado = EstadoAlquiler::Robado;
        }
        // Si es un alquiler offline sin regularizar (hay un pago pendiente), el
        // líder todavía no lo conoce: anotamos el robo en el pago y la
        // regularización lo liquidará (preauth + reposición + cierre). No
        // encolamos nada al líder ahora (evita re-abrirlo al regularizar).
        if self.anotar_cierre_offline(bici_id, CierreOffline::Robado) {
            println!(
                "[{}] robo: {rental_id:?} (bici {bici_id}) es un alquiler offline sin regularizar; \
                 el cierre y la reposición se liquidan al regularizar",
                self.id
            );
            return;
        }
        if matches!(self.rol, RolEstacion::Lider { .. }) {
            if let RolEstacion::Lider { registro, .. } = &mut self.rol {
                registro.marcar_robado(rental_id);
            }
            self.bicis_robadas.insert(bici_id);
            println!(
                "[{}] robo: cierro {rental_id:?} como Robado y saco la bici {bici_id} de circulación",
                self.id
            );
        } else {
            let n = self.proximo();
            let event_id = EventId(format!("E-{}-{}", self.id.0, n));
            println!(
                "[{}] robo: reporto el cierre de {rental_id:?} (bici {bici_id}) al líder {}",
                self.id, self.lider_id
            );
            self.enviar_evento_al_lider(
                MensajeEntreEstacionesTCP::RoboProcesado {
                    event_id,
                    rental_id: rental_id.clone(),
                    bici_id,
                },
                ctx,
            );
        }
        // Avisar al origen (si no soy yo, va por red; si soy yo, ya lo marqué arriba).
        if estacion_origen != self.id {
            if let Some(destino) = self.estaciones.get(&estacion_origen).copied() {
                if let (Some(comunicador), Ok(bytes)) = (
                    &self.comunicador,
                    comun::serializacion::a_bytes(&MensajeEntreEstacionesTCP::CierreRobo {
                        rental_id: rental_id.clone(),
                    }),
                ) {
                    comunicador.do_send(EnviarTcp {
                        destino,
                        datos: bytes,
                    });
                }
            }
        }
        self.persistir();
    }
}

impl Handler<DenunciaRobo> for Estacion {
    type Result = ResponseActFuture<Self, MensajeEstacionAUsuario>;

    fn handle(&mut self, msg: DenunciaRobo, ctx: &mut Self::Context) -> Self::Result {
        let usuario_id = msg.usuario_id;
        // 1) ¿Lo tengo local? (registro si soy líder, o un alquiler propio).
        if let Some(datos) = self.buscar_robo_local(&usuario_id) {
            let respuesta = self.registrar_robo(datos, ctx);
            return Box::pin(async move { respuesta }.into_actor(self));
        }
        // 2) Soy el líder y no lo tengo: el usuario no tiene alquiler activo.
        if matches!(self.rol, RolEstacion::Lider { .. }) {
            return Box::pin(async { MensajeEstacionAUsuario::SinAlquilerActivo }.into_actor(self));
        }
        // 3) Soy follower: le pregunto al líder por red.
        let comunicador = self.comunicador.clone();
        let lider = self.lider;
        let n = self.proximo();
        let event_id = EventId(format!("E-{}-{}", self.id.0, n));
        Box::pin(
            async move { consultar_lider_robo(&comunicador, lider, event_id, usuario_id).await }
                .into_actor(self)
                .map(|resultado, actor, ctx| match resultado {
                    Ok(datos) => actor.registrar_robo(datos, ctx),
                    // El líder respondió que el usuario no tiene alquiler activo.
                    Err(false) => MensajeEstacionAUsuario::SinAlquilerActivo,
                    // El líder es inalcanzable: no se pudo verificar la denuncia.
                    Err(true) => MensajeEstacionAUsuario::RoboNoProcesado,
                }),
        )
    }
}

impl Handler<ProcesarRobo> for Estacion {
    type Result = ResponseActFuture<Self, ()>;

    fn handle(&mut self, msg: ProcesarRobo, ctx: &mut Self::Context) -> Self::Result {
        // Primera pasada: cerrar el alquiler como robado y sacar la bici de
        // circulación. No depende de la pasarela (en los reintentos del cobro se
        // saltea, `ya_cerrado = true`).
        if !msg.ya_cerrado {
            self.cerrar_robo(&msg.rental_id, msg.bici_id, msg.estacion_origen, ctx);
        }
        // Cobrar la reposición (monto reservado). Sin preauth (alquiler offline sin
        // regularizar) no se puede cobrar todavía: queda anotado.
        let Some(preauth_id) = msg.preauth_id.clone() else {
            println!(
                "[{}] robo de la bici {}: sin preauth (alquiler offline), la reposición \
                 queda pendiente de regularización",
                self.id, msg.bici_id
            );
            return Box::pin(async {}.into_actor(self).map(|_, _, _| ()));
        };
        let comunicador = self.comunicador.clone();
        let pasarela = self.pasarela;
        let mi_id = self.id;
        let bici_id = msg.bici_id;
        let rental_id = msg.rental_id.clone();
        let estacion_origen = msg.estacion_origen;
        Box::pin(
            async move {
                let cobro = MensajeEstacionAPasarela::CobrarRobo {
                    preauth_id: preauth_id.clone(),
                };
                consultar_pasarela(&comunicador, pasarela, &cobro).await
            }
            .into_actor(self)
            .map(move |resp, actor, ctx| match resp {
                Some(MensajePasarelaAEstacion::RoboCobrado { monto, .. }) => {
                    println!("[{mi_id}] robo de la bici {bici_id}: cobré la reposición ${monto}");
                }
                // La preauth ya no era cobrable (anulada): se audita.
                Some(_) => {
                    actor.cobros_fallidos += 1;
                    println!(
                        "[{mi_id}] robo de la bici {bici_id}: la preauth ya no era cobrable \
                         (auditado, sin cobro)"
                    );
                }
                // Pasarela inalcanzable: reintentar SOLO el cobro (el cierre ya está).
                None => {
                    println!(
                        "[{mi_id}] robo de la bici {bici_id}: pasarela inalcanzable, \
                         reintento la reposición al volver"
                    );
                    let proximo = ProcesarRobo {
                        rental_id,
                        bici_id,
                        preauth_id: msg.preauth_id,
                        estacion_origen,
                        ya_cerrado: true,
                    };
                    ctx.run_later(ESPERA_REINTENTO, move |_, ctx| {
                        ctx.address().do_send(proximo);
                    });
                }
            }),
        )
    }
}

/// Consulta al líder (por red) el alquiler activo de un usuario para la denuncia.
/// `Ok(datos)` si lo tiene; `Err(false)` si el líder dice que no hay alquiler;
/// `Err(true)` si el líder es inalcanzable (no se pudo verificar).
async fn consultar_lider_robo(
    comunicador: &Option<Addr<Comunicador>>,
    lider: SocketAddr,
    event_id: EventId,
    usuario_id: UsuarioId,
) -> Result<DatosRobo, bool> {
    let Some(comunicador) = comunicador.as_ref() else {
        return Err(true);
    };
    let pedido = MensajeEntreEstacionesTCP::BuscarAlquilerDeUsuario {
        event_id,
        usuario_id,
    };
    let Ok(bytes) = comun::serializacion::a_bytes(&pedido) else {
        return Err(true);
    };
    let respuesta = comunicador
        .send(ConsultarTcp {
            destino: lider,
            datos: bytes,
        })
        .await
        .ok()
        .flatten();
    let Some(respuesta) = respuesta else {
        return Err(true); // líder inalcanzable
    };
    match comun::serializacion::desde_bytes::<MensajeEntreEstacionesTCP>(&respuesta).ok() {
        Some(MensajeEntreEstacionesTCP::AlquilerDeUsuario {
            rental_id,
            bici_id,
            preauth_id,
            estacion_origen,
            ..
        }) => Ok(DatosRobo {
            rental_id,
            bici_id,
            preauth_id: Some(preauth_id),
            estacion_origen,
        }),
        Some(MensajeEntreEstacionesTCP::UsuarioSinAlquiler { .. }) => Err(false),
        _ => Err(true),
    }
}
