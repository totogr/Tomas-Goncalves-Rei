//! Actor coordinador de la estación.
//!
//! Es el **coordinador del 2PC** del alquiler: en la fase Prepare le pregunta al
//! `Slot` (local) y a la `Pasarela` (remota); si los dos votan que sí, commitea
//! ambos y registra el alquiler con la pre-autorización real; si alguno vota no
//! (o la pasarela no responde), aborta ambos. La devolución sigue siendo local.
//!
//! Toda la red pasa por el `Comunicador`: para hablarle a la pasarela, la estación
//! le manda `ConsultarTcp` a su Comunicador (no toca sockets ella misma).

use std::collections::{HashMap, HashSet};
use std::net::SocketAddr;
use std::path::PathBuf;
use std::sync::atomic::{AtomicBool, Ordering};
use std::sync::Arc;

use actix::prelude::*;
use comun::comunicador::{
    Comunicador, ConfigurarPeerPersistente, ConsultarAlcanzable, ConsultarTcp, EnviarTcp,
    EnviarTcpConfirmado, EnviarUdp, PaqueteRecibido, PeerConectado, PeerDesconectado, Responder,
    RolPeer, Transporte,
};
use comun::mensajes::estacion_estacion::{MensajeEntreEstacionesTCP, MensajeEntreEstacionesUDP};
use comun::mensajes::estacion_pasarela::{
    MensajeEstacionAPasarela, MensajePasarelaAEstacion, VotoResultado,
};
use comun::mensajes::usuario_estacion::{
    MensajeEstacionAUsuario, MensajeEstacionAUsuarioConsulta, MensajeUsuario,
    MensajeUsuarioAEstacion, MensajeUsuarioAEstacionConsulta,
};
use comun::{
    Alquiler, BiciId, DatosTarjeta, EstacionId, EstadoAlquiler, EventId, InfoEstacion, RentalId,
    Timestamp, TransaccionId, UsuarioId,
};
use serde::{Deserialize, Serialize};

/// Cada cuánto cada estación le manda su estado al líder por UDP.
const INTERVALO_GOSSIP: std::time::Duration = std::time::Duration::from_secs(3);

/// Cada cuánto un follower sondea al líder para verificar que siga vivo.
const INTERVALO_VIGILANCIA: std::time::Duration = std::time::Duration::from_secs(2);

/// Sondeos fallidos consecutivos para dar al líder por caído e iniciar elección.
const UMBRAL_FALLOS_LIDER: u8 = 2;

/// Intervalos de vigilancia con una elección sin resolver antes de reiniciarla
/// (cubre el caso de un mensaje del Ring perdido por una caída en cadena).
const UMBRAL_ELECCION_TRABADA: u8 = 5;

/// Cada cuánto se supervisa una elección en curso (modo persistente). Solo
/// chequea estado interno —no abre conexiones—, así que no es busy-wait de red:
/// reinicia una elección que no convergió (p. ej. un mensaje del Ring perdido).
const INTERVALO_SUPERVISION_ELECCION: std::time::Duration = std::time::Duration::from_secs(2);

/// Cuánto espera el coordinador del 2PC el voto de la pasarela antes de tomarlo
/// como No implícito (Caso A de la sección 7.1.1 del README).
const TIMEOUT_PREPARE: std::time::Duration = std::time::Duration::from_secs(3);

/// Intentos de `NotificarDevolucion` al líder antes de dar a la bici por
/// huérfana (el líder pudo responder `NoRegistradoAun` si el reporte del origen
/// todavía no le llegó, o puede haber una elección en curso).
const REINTENTOS_NOTIFICACION: u32 = 5;

/// Cuánto espera el que reenvía un mensaje del Ring el ACK del siguiente nodo.
/// Un nodo COLGADO acepta la conexión (lo hace el kernel) pero nunca contesta:
/// sin este ACK el anillo lo daría por entregado y la elección se trabaría.
const TIMEOUT_ACK_RING: std::time::Duration = std::time::Duration::from_secs(2);

/// Espera base entre reintentos de `NotificarDevolucion` (crece linealmente).
const ESPERA_REINTENTO: std::time::Duration = std::time::Duration::from_millis(500);

/// Cada cuánto se reintentan los commits decididos que la pasarela todavía no
/// confirmó (Caso C: el Commit se perdió o la pasarela estaba caída).
const INTERVALO_REINTENTO_COMMITS: std::time::Duration = std::time::Duration::from_secs(5);

/// Cada cuánto se intenta regularizar los pagos pendientes (alquileres que
/// salieron offline, Caso E) contra la pasarela.
const INTERVALO_REGULARIZACION: std::time::Duration = std::time::Duration::from_secs(5);

/// Cuánto puede durar un alquiler abierto antes de que el líder lo dé por robo:
/// al cumplirse, si la bici no se devolvió, se marca Robado y se cobra la
/// reposición (detección de robo por inactividad). 24 horas.
const DURACION_MAXIMA_ALQUILER: std::time::Duration = std::time::Duration::from_secs(24 * 60 * 60);

use crate::eleccion::{AccionRing, Eleccion, EstadoLider};
use crate::mensajes::{
    AbortLiberacion, AceptarBici, CommitConfirmado, CommitLiberacion, ConsultarEstado,
    DenunciaRobo, MostrarGossip, PrepareLiberacion, ProcesarDevolucion, ProcesarRobo,
    RedescubrirLider, RegistrarCommitPendiente, RegistrarComunicador, SolicitudUsuario, Voto,
};
// Mensajes de diagnóstico: solo existen (y se usan) en los tests.
#[cfg(test)]
use crate::mensajes::{
    ConsultarCache, ConsultarCobrosFallidos, ConsultarCommitsPendientes, ConsultarHuerfanas,
    ConsultarLider, ConsultarPagosPendientes, ConsultarPendientes, ConsultarPropiosActivos,
    ConsultarRegistro, ConsultarRobadas, InfoLider,
};
use crate::registro::Registro;
use crate::slot::Slot;

// Flujos en módulos hijos (acceden a los privados de `Estacion`).
mod alquiler;
mod devolucion;
mod recuperacion;
mod robo;
use alquiler::{procesar_operacion, ContextoOperacion};

/// Rol de la estación: una es el líder (mantiene el registro autoritativo y la
/// cache de estados para las consultas), el resto son followers.
enum RolEstacion {
    Lider {
        registro: Registro,
        /// Estado agregado de cada estación, alimentado por el gossip UDP
        /// (`EstadoEstacion`). Sirve para responder consultas de disponibilidad.
        cache: HashMap<EstacionId, InfoEstacion>,
        /// Eventos ya procesados (Caso D): un reintento con el mismo `event_id`
        /// no se vuelve a aplicar. Importa con la cola de diferidos: un evento
        /// puede llegar dos veces si el ACK de TCP se perdió y se reenvió.
        eventos: HashSet<EventId>,
    },
    Follower,
}

impl RolEstacion {
    /// Rol líder recién asumido: registro, cache y dedup arrancan vacíos (el
    /// registro se puebla con la reconstrucción).
    fn lider_nuevo() -> Self {
        RolEstacion::Lider {
            registro: Registro::new(),
            cache: HashMap::new(),
            eventos: HashSet::new(),
        }
    }
}

/// Monto que se pre-autoriza (reserva) al iniciar un alquiler. Provisorio fijo.
const MONTO_RESERVA: f64 = 1000.0;

pub struct Estacion {
    id: EstacionId,
    ubicacion: (f64, f64),
    slots: Vec<Addr<Slot>>,
    pasarela: SocketAddr,
    /// Dirección del líder (a quién reportar los alquileres si soy follower).
    lider: SocketAddr,
    /// Id del líder, para responder el discovery (`PreguntarLider`).
    lider_id: EstacionId,
    /// Direcciones de todas las estaciones, para alcanzar al origen en la devolución.
    estaciones: HashMap<EstacionId, SocketAddr>,
    rol: RolEstacion,
    /// Estado del algoritmo de elección de líder (Ring): anillo, `term` y a quién
    /// reconoce como líder.
    eleccion: Eleccion,
    /// `Addr` del Comunicador propio (se cablea al arrancar con `RegistrarComunicador`).
    comunicador: Option<Addr<Comunicador>>,
    alquileres_propios: HashMap<RentalId, Alquiler>,
    /// Contador para generar ids únicos de transacción, alquiler y evento.
    contador: u64,
    /// Último líder visto por `aplicar_liderazgo` (detecta el cambio para la
    /// reincorporación tardía).
    lider_id_anterior: EstacionId,
    /// Sondeos al líder fallidos consecutivos (la vigilancia los cuenta).
    fallos_lider: u8,
    /// Intervalos de vigilancia transcurridos con la elección sin resolver.
    intervalos_en_eleccion: u8,
    /// Cola de diferidos: eventos al líder que no se pudieron entregar. Se
    /// reintentan cuando el líder vuelve a responder (o tras una elección).
    eventos_pendientes: Vec<MensajeEntreEstacionesTCP>,
    /// Commits del 2PC decididos pero todavía sin confirmación de la pasarela
    /// (Caso C). Se reintentan periódicamente y sobreviven reinicios.
    commits_pendientes: HashMap<TransaccionId, String>,
    /// Archivo de persistencia (si está, el estado sobrevive un reinicio).
    archivo: Option<PathBuf>,
    /// Cada cuánto reintentar los commits pendientes (acortable en tests).
    intervalo_reintento_commits: std::time::Duration,
    /// Bicis confirmadas huérfanas (auditoría, sección 8.2.1).
    huerfanas_confirmadas: usize,
    /// Bicis denunciadas como robadas, fuera de circulación (auditoría). El
    /// líder es la fuente de verdad; se persiste para sobrevivir reinicios.
    bicis_robadas: HashSet<BiciId>,
    /// Alquileres offline a la espera de su preauth diferida (Caso E).
    pagos_pendientes: Vec<PagoPendiente>,
    /// Cada cuánto intentar la regularización (acortable en tests).
    intervalo_regularizacion: std::time::Duration,
    /// Regularizaciones rechazadas por la pasarela (auditoría: CobroFallido).
    cobros_fallidos: usize,
    /// Si está en `true`, la estación imprime el gossip UDP que envía (y, si es
    /// líder, el que recibe). Lo togglea el comando `logs` de la consola.
    mostrar_gossip: bool,
    /// Flag de corte de red, COMPARTIDO con el Comunicador (mismo `Arc`). Cuando
    /// está activo, la estación sigue atendiendo al usuario local pero descarta
    /// el tráfico inter-estación/gossip y resuelve los alquileres offline.
    desconectado: Arc<AtomicBool>,
    /// Si está en `true` (lo activa `main`), la estación mantiene conexiones TCP
    /// persistentes hacia el líder y la pasarela, y reacciona a su salud
    /// (event-driven) en vez de sondear/reintentar por `run_interval`. Los tests
    /// unitarios lo dejan en `false` (caminos efímeros, más simples de mockear).
    usa_peers_persistentes: bool,
    /// Cuánto puede durar un alquiler abierto antes de marcarse Robado por
    /// inactividad (acortable en tests). Solo el líder arma estos timers.
    duracion_maxima_alquiler: std::time::Duration,
}

/// Alquiler completado localmente sin pasar por la pasarela (Caso E). Se
/// persiste y se procesa cuando se restaura el acceso a la pasarela: primero la
/// preauth diferida, recién después el reporte al líder. El cobro NO ocurre acá
/// (sucede al devolverse la bici, por el flujo normal de CU2).
#[derive(Clone, Serialize, Deserialize)]
struct PagoPendiente {
    rental_id: RentalId,
    bici_id: BiciId,
    usuario_id: UsuarioId,
    tarjeta: DatosTarjeta,
    t0: Timestamp,
    /// Si la bici se devolvió o se denunció robada **antes** de regularizar (todo
    /// offline en la misma estación): la regularización, tras conseguir la preauth
    /// y reportar el alquiler, dispara este cierre en vez de dejarlo abierto.
    #[serde(default)]
    cierre: Option<CierreOffline>,
}

/// Cómo se cerró offline un alquiler que todavía no tenía preauth (se liquida al
/// regularizar).
#[derive(Clone, Serialize, Deserialize)]
enum CierreOffline {
    /// La bici se devolvió a un slot en `t1` (se cobra el uso al regularizar).
    Devuelto { t1: Timestamp },
    /// La bici se denunció robada (se cobra la reposición al regularizar).
    Robado,
}

/// Lo que la estación guarda en disco: sus alquileres, los commits decididos
/// sin confirmar, la cola de eventos diferidos al líder y los pagos pendientes
/// de regularización. El rol y la elección NO se persisten: al reiniciar se
/// rearma por config + discovery/elección.
#[derive(Serialize, Deserialize)]
struct EstadoEnDisco {
    alquileres_propios: HashMap<RentalId, Alquiler>,
    commits_pendientes: Vec<(TransaccionId, String)>,
    eventos_pendientes: Vec<MensajeEntreEstacionesTCP>,
    /// `default` para poder leer archivos guardados antes de que existiera.
    #[serde(default)]
    pagos_pendientes: Vec<PagoPendiente>,
    /// `default` para compatibilidad con estados guardados antes de los robos.
    #[serde(default)]
    bicis_robadas: HashSet<BiciId>,
}

impl Estacion {
    pub fn new(
        id: EstacionId,
        ubicacion: (f64, f64),
        slots: Vec<Addr<Slot>>,
        pasarela: SocketAddr,
        lider: (EstacionId, SocketAddr),
        es_lider: bool,
        estaciones: HashMap<EstacionId, SocketAddr>,
    ) -> Self {
        let (lider_id, lider) = lider;
        let eleccion = Eleccion::new(id, estaciones.keys().copied()).con_lider_inicial(lider_id);
        Self {
            id,
            ubicacion,
            slots,
            pasarela,
            lider,
            lider_id,
            estaciones,
            rol: if es_lider {
                RolEstacion::lider_nuevo()
            } else {
                RolEstacion::Follower
            },
            eleccion,
            comunicador: None,
            alquileres_propios: HashMap::new(),
            contador: 0,
            lider_id_anterior: lider_id,
            fallos_lider: 0,
            intervalos_en_eleccion: 0,
            eventos_pendientes: Vec::new(),
            commits_pendientes: HashMap::new(),
            archivo: None,
            intervalo_reintento_commits: INTERVALO_REINTENTO_COMMITS,
            huerfanas_confirmadas: 0,
            bicis_robadas: HashSet::new(),
            pagos_pendientes: Vec::new(),
            intervalo_regularizacion: INTERVALO_REGULARIZACION,
            cobros_fallidos: 0,
            mostrar_gossip: false,
            desconectado: Arc::new(AtomicBool::new(false)),
            usa_peers_persistentes: false,
            duracion_maxima_alquiler: DURACION_MAXIMA_ALQUILER,
        }
    }

    /// Comparte el flag de corte de red con el Comunicador (mismo `Arc`), para
    /// que `desconectar`/`conectar` por consola afecten a ambos a la vez.
    pub fn con_flag_desconexion(mut self, desconectado: Arc<AtomicBool>) -> Self {
        self.desconectado = desconectado;
        self
    }

    /// Activa el modo de conexiones persistentes + event-driven (lo usa `main`).
    pub fn con_peers_persistentes(mut self) -> Self {
        self.usa_peers_persistentes = true;
        self
    }

    fn esta_desconectado(&self) -> bool {
        self.desconectado.load(Ordering::Relaxed)
    }

    /// Acorta el intervalo de regularización (para tests).
    #[cfg(test)]
    pub fn con_intervalo_de_regularizacion(mut self, intervalo: std::time::Duration) -> Self {
        self.intervalo_regularizacion = intervalo;
        self
    }

    /// Acorta la duración máxima del alquiler (para tests del robo por inactividad).
    #[cfg(test)]
    pub fn con_duracion_maxima_alquiler(mut self, duracion: std::time::Duration) -> Self {
        self.duracion_maxima_alquiler = duracion;
        self
    }

    /// Activa la persistencia en `ruta`: si ya hay un estado guardado lo carga
    /// (recuperación tras reinicio) y, si esta estación arranca como líder por
    /// config, repuebla su registro con sus propios alquileres activos.
    pub fn con_persistencia(mut self, ruta: PathBuf) -> Self {
        if let Some(estado) = comun::persistencia::cargar::<EstadoEnDisco>(&ruta) {
            println!(
                "[{}] estado recuperado de {:?}: {} alquileres, {} commits pendientes, {} eventos diferidos",
                self.id,
                ruta,
                estado.alquileres_propios.len(),
                estado.commits_pendientes.len(),
                estado.eventos_pendientes.len()
            );
            self.alquileres_propios = estado.alquileres_propios;
            self.commits_pendientes = estado.commits_pendientes.into_iter().collect();
            self.eventos_pendientes = estado.eventos_pendientes;
            self.pagos_pendientes = estado.pagos_pendientes;
            self.bicis_robadas = estado.bicis_robadas;
            if let RolEstacion::Lider { registro, .. } = &mut self.rol {
                for alquiler in self
                    .alquileres_propios
                    .values()
                    .filter(|a| a.estado == EstadoAlquiler::Activo)
                {
                    registro.agregar(alquiler.clone());
                }
            }
        }
        self.archivo = Some(ruta);
        self
    }

    /// Acorta el intervalo de reintento de commits (para tests).
    #[cfg(test)]
    pub fn con_intervalo_de_reintento(mut self, intervalo: std::time::Duration) -> Self {
        self.intervalo_reintento_commits = intervalo;
        self
    }

    /// Vuelca el estado a disco (si la persistencia está activa).
    fn persistir(&self) {
        let Some(ruta) = &self.archivo else { return };
        let estado = EstadoEnDisco {
            alquileres_propios: self.alquileres_propios.clone(),
            commits_pendientes: self
                .commits_pendientes
                .iter()
                .map(|(t, p)| (t.clone(), p.clone()))
                .collect(),
            eventos_pendientes: self.eventos_pendientes.clone(),
            pagos_pendientes: self.pagos_pendientes.clone(),
            bicis_robadas: self.bicis_robadas.clone(),
        };
        if let Err(e) = comun::persistencia::guardar(ruta, &estado) {
            eprintln!("[{}] no pude persistir el estado en {ruta:?}: {e}", self.id);
        }
    }

    fn proximo(&mut self) -> u64 {
        self.contador += 1;
        self.contador
    }

    /// Arma el snapshot de estado (contando bicis disponibles en los slots) y se
    /// lo manda al líder por UDP. El conteo es asincrónico (consulta a cada slot),
    /// así que se resuelve con un futuro spawneado en el contexto del actor.
    fn enviar_estado(&mut self, ctx: &mut Context<Self>) {
        let slots = self.slots.clone();
        let id = self.id;
        let ubicacion = self.ubicacion;
        let lider = self.lider;
        let total = self.slots.len() as u32;
        let fut = async move {
            let mut disponibles = 0u32;
            for slot in &slots {
                if let Ok(estado) = slot.send(ConsultarEstado).await {
                    if estado.ocupado {
                        disponibles += 1;
                    }
                }
            }
            disponibles
        }
        .into_actor(self)
        .map(move |disponibles, act, _ctx| {
            let estado = MensajeEntreEstacionesUDP::EstadoEstacion {
                estacion_id: id,
                ubicacion,
                bicis_disponibles: disponibles,
                slots_libres: total - disponibles,
                timestamp: Timestamp::ahora(),
            };
            if let (Some(comunicador), Ok(bytes)) =
                (&act.comunicador, comun::serializacion::a_bytes(&estado))
            {
                if act.mostrar_gossip {
                    println!(
                        "[{}] → gossip UDP al líder {}: {} bicis, {} slots libres",
                        id,
                        lider,
                        disponibles,
                        total - disponibles
                    );
                }
                comunicador.do_send(EnviarUdp {
                    destino: lider,
                    datos: bytes,
                });
            }
        });
        ctx.spawn(fut);
    }

    /// Reporta un alquiler recién abierto al líder. Si esta estación ES el líder,
    /// lo registra directo; si es follower, se lo manda por el Comunicador (con
    /// cola de diferidos si el líder no está disponible).
    fn reportar_alquiler(&mut self, alquiler: &Alquiler, ctx: &mut Context<Self>) {
        // Regla de la 7.1.1: un alquiler sin preauth (modo offline) NO se
        // reporta; primero se regulariza contra la pasarela.
        let Some(preauth_id) = alquiler.preauth_id.clone() else {
            println!(
                "[{}] alquiler de {} sin preauth: pendiente de regularización",
                self.id, alquiler.bici_id
            );
            return;
        };
        let n = self.proximo();
        let abierto = MensajeEntreEstacionesTCP::AlquilerAbierto {
            event_id: EventId(format!("E-{}-{}", self.id.0, n)),
            rental_id: alquiler.rental_id.clone(),
            bici_id: alquiler.bici_id,
            usuario_id: alquiler.usuario_id.clone(),
            estacion_origen: alquiler.estacion_origen,
            t0: alquiler.inicio,
            preauth_id,
        };
        let soy_lider = match &mut self.rol {
            RolEstacion::Lider { registro, .. } => {
                println!(
                    "[{}] (líder) registro mi propio alquiler {:?} (bici {})",
                    self.id, alquiler.rental_id, alquiler.bici_id
                );
                registro.agregar(alquiler.clone());
                true
            }
            RolEstacion::Follower => {
                println!(
                    "[{}] reporto AlquilerAbierto {:?} al líder {}",
                    self.id, alquiler.rental_id, self.lider_id
                );
                self.enviar_evento_al_lider(abierto, ctx);
                false
            }
        };
        // Arranca el reloj del robo por inactividad (solo lo hace el líder).
        if soy_lider {
            self.programar_robo_por_timeout(alquiler.rental_id.clone(), alquiler.inicio, ctx);
        }
    }

    /// Manda un evento al líder con confirmación de entrega: si no se pudo (el
    /// líder está caído o en elección), el evento queda en la cola de diferidos
    /// y se reintenta cuando el líder vuelva a estar disponible. El `event_id`
    /// del evento hace que un doble envío sea inofensivo (el líder deduplica).
    fn enviar_evento_al_lider(
        &mut self,
        evento: MensajeEntreEstacionesTCP,
        ctx: &mut Context<Self>,
    ) {
        let (Some(comunicador), Ok(bytes)) = (
            self.comunicador.clone(),
            comun::serializacion::a_bytes(&evento),
        ) else {
            self.eventos_pendientes.push(evento);
            self.persistir();
            return;
        };
        let destino = self.lider;
        let fut = async move {
            comunicador
                .send(EnviarTcpConfirmado {
                    destino,
                    datos: bytes,
                })
                .await
                .unwrap_or(false)
        }
        .into_actor(self)
        .map(move |entregado, actor, _ctx| {
            if !entregado {
                println!(
                    "[{}] el líder no recibe eventos: lo dejo en la cola de diferidos",
                    actor.id
                );
                actor.eventos_pendientes.push(evento);
                actor.persistir();
            }
        });
        ctx.spawn(fut);
    }

    /// Reintenta los eventos diferidos. Si mientras tanto esta estación pasó a
    /// ser el líder, se los aplica a sí misma (directo al registro).
    fn descargar_pendientes(&mut self, ctx: &mut Context<Self>) {
        if self.eventos_pendientes.is_empty() {
            return;
        }
        let pendientes = std::mem::take(&mut self.eventos_pendientes);
        self.persistir();
        println!(
            "[{}] reintento {} eventos diferidos al líder",
            self.id,
            pendientes.len()
        );
        for evento in pendientes {
            if matches!(self.rol, RolEstacion::Lider { .. }) {
                self.manejar_entre_estaciones(evento, None, ctx);
            } else {
                self.enviar_evento_al_lider(evento, ctx);
            }
        }
    }

    /// Responde una consulta del usuario (CU3): discovery del líder o
    /// disponibilidad. La disponibilidad solo la puede contestar el líder (es el
    /// único con la cache); un follower devuelve la lista vacía.
    fn manejar_consulta(
        &self,
        consulta: MensajeUsuarioAEstacionConsulta,
    ) -> MensajeEstacionAUsuarioConsulta {
        match consulta {
            MensajeUsuarioAEstacionConsulta::PreguntarLider => {
                match self.eleccion.lider_conocido() {
                    EstadoLider::Conocido(_) => MensajeEstacionAUsuarioConsulta::RespuestaLider {
                        lider_id: self.lider_id,
                        lider_addr: self.lider,
                        term: self.eleccion.term(),
                    },
                    EstadoLider::EnEleccion => MensajeEstacionAUsuarioConsulta::EnEleccion,
                    EstadoLider::Desconocido => MensajeEstacionAUsuarioConsulta::LiderDesconocido,
                }
            }
            MensajeUsuarioAEstacionConsulta::ConsultaDisponibilidad {
                ubicacion,
                radio_max_km,
                ..
            } => {
                let estaciones = match &self.rol {
                    RolEstacion::Lider { cache, .. } => cache
                        .values()
                        .filter(|info| info.bicis_disponibles > 0)
                        .filter(|info| distancia_km(ubicacion, info.ubicacion) <= radio_max_km)
                        .cloned()
                        .collect(),
                    RolEstacion::Follower => Vec::new(),
                };
                MensajeEstacionAUsuarioConsulta::RespuestaDisponibilidad { estaciones }
            }
        }
    }

    /// Maneja un mensaje que llegó de otra estación. Son sincrónicos (consultas al
    /// registro local). El líder responde `NotificarDevolucion` con los datos de
    /// cobro y cierra el alquiler con `DevolucionProcesada`.
    fn manejar_entre_estaciones(
        &mut self,
        msg: MensajeEntreEstacionesTCP,
        responder: Option<Responder>,
        ctx: &mut Context<Self>,
    ) {
        match msg {
            MensajeEntreEstacionesTCP::AlquilerAbierto {
                event_id,
                rental_id,
                bici_id,
                usuario_id,
                estacion_origen,
                t0,
                preauth_id,
            } => {
                let mut a_programar = None;
                if let RolEstacion::Lider {
                    registro, eventos, ..
                } = &mut self.rol
                {
                    // Caso D: un evento repetido (reintento) no se vuelve a aplicar.
                    if eventos.insert(event_id) {
                        // No re-abrir un alquiler ya cerrado/robado: puede llegar un
                        // AlquilerAbierto tardío de la regularización de un alquiler
                        // que se cerró offline (el cierre ya llegó antes).
                        if registro.esta_cerrado(&rental_id) {
                            println!(
                                "[{}] (líder) ignoro AlquilerAbierto {rental_id:?}: ya está cerrado/robado",
                                self.id
                            );
                        } else {
                            println!(
                                "[{}] (líder) registro AlquilerAbierto {rental_id:?} (bici {bici_id}) de la estación {estacion_origen}",
                                self.id
                            );
                            a_programar = Some((rental_id.clone(), t0));
                            registro.agregar(Alquiler {
                                rental_id,
                                bici_id,
                                usuario_id,
                                estacion_origen,
                                inicio: t0,
                                fin: None,
                                preauth_id: Some(preauth_id),
                                estado: EstadoAlquiler::Activo,
                            });
                        }
                    }
                }
                // Arranca el reloj del robo por inactividad para el alquiler nuevo.
                if let Some((rental_id, inicio)) = a_programar {
                    self.programar_robo_por_timeout(rental_id, inicio, ctx);
                }
            }
            MensajeEntreEstacionesTCP::NotificarDevolucion {
                event_id, bici_id, ..
            } => {
                let respuesta = match &self.rol {
                    RolEstacion::Lider { registro, .. } => {
                        // El registro solo guarda alquileres con preauth (regla
                        // de la 7.1.1); el and_then es defensivo.
                        match registro
                            .buscar_por_bici(bici_id)
                            .and_then(|a| a.preauth_id.clone().map(|p| (a, p)))
                        {
                            Some((a, preauth_id)) => MensajeEntreEstacionesTCP::DatosParaCobro {
                                event_id,
                                rental_id: a.rental_id.clone(),
                                preauth_id,
                                t0: a.inicio,
                                estacion_origen: a.estacion_origen,
                            },
                            None => MensajeEntreEstacionesTCP::NoRegistradoAun { event_id },
                        }
                    }
                    RolEstacion::Follower => {
                        MensajeEntreEstacionesTCP::NoRegistradoAun { event_id }
                    }
                };
                if let (Some(r), Ok(bytes)) = (responder, comun::serializacion::a_bytes(&respuesta))
                {
                    r.responder(bytes);
                }
            }
            MensajeEntreEstacionesTCP::DevolucionProcesada {
                event_id,
                rental_id,
                monto_cobrado,
                tiempo_uso_minutos,
            } => {
                let mi_id = self.id;
                if let RolEstacion::Lider {
                    registro, eventos, ..
                } = &mut self.rol
                {
                    // Caso D: dedup por event_id, igual que AlquilerAbierto.
                    if eventos.insert(event_id) {
                        registro.cerrar(&rental_id);
                        println!(
                            "[{mi_id}] (líder) recibí el cierre de la devolución {rental_id:?} \
                             (cobrado ${monto_cobrado}, {tiempo_uso_minutos} min): lo cierro en el registro"
                        );
                    }
                }
            }
            MensajeEntreEstacionesTCP::CierreAlquiler {
                rental_id,
                monto_cobrado,
                ..
            } => {
                // Soy la estación de origen: marco mi alquiler como cerrado.
                if let Some(alquiler) = self.alquileres_propios.get_mut(&rental_id) {
                    alquiler.estado = EstadoAlquiler::Cerrado;
                    self.persistir();
                    println!(
                        "[{}] (origen) recibí el cierre de la devolución {rental_id:?} \
                         (cobrado ${monto_cobrado}): cierro mi alquiler",
                        self.id
                    );
                }
            }

            // --- Robos: búsqueda por usuario y cierre por robo ---
            MensajeEntreEstacionesTCP::BuscarAlquilerDeUsuario {
                event_id,
                usuario_id,
            } => {
                // Soy el líder: busco el alquiler activo del usuario para la denuncia.
                let respuesta = match &self.rol {
                    RolEstacion::Lider { registro, .. } => {
                        match registro.buscar_por_usuario(&usuario_id) {
                            Some(a) => match a.preauth_id.clone() {
                                Some(preauth_id) => MensajeEntreEstacionesTCP::AlquilerDeUsuario {
                                    event_id,
                                    rental_id: a.rental_id.clone(),
                                    bici_id: a.bici_id,
                                    preauth_id,
                                    t0: a.inicio,
                                    estacion_origen: a.estacion_origen,
                                },
                                None => MensajeEntreEstacionesTCP::UsuarioSinAlquiler { event_id },
                            },
                            None => MensajeEntreEstacionesTCP::UsuarioSinAlquiler { event_id },
                        }
                    }
                    RolEstacion::Follower => {
                        MensajeEntreEstacionesTCP::UsuarioSinAlquiler { event_id }
                    }
                };
                if let (Some(r), Ok(bytes)) = (responder, comun::serializacion::a_bytes(&respuesta))
                {
                    r.responder(bytes);
                }
            }
            MensajeEntreEstacionesTCP::RoboProcesado {
                event_id,
                rental_id,
                bici_id,
            } => {
                // Soy el líder: cierro el alquiler como robado y saco la bici de
                // circulación (dedup por event_id, igual que el resto de eventos).
                let mi_id = self.id;
                let mut procesado = false;
                if let RolEstacion::Lider {
                    registro, eventos, ..
                } = &mut self.rol
                {
                    if eventos.insert(event_id) {
                        registro.marcar_robado(&rental_id);
                        procesado = true;
                    }
                }
                if procesado {
                    self.bicis_robadas.insert(bici_id);
                    self.persistir();
                    println!(
                        "[{mi_id}] (líder) recibí la denuncia de robo de la bici {bici_id}: \
                         cierro {rental_id:?} como Robado y la saco de circulación"
                    );
                }
            }
            MensajeEntreEstacionesTCP::CierreRobo { rental_id } => {
                // Soy la estación de origen: marco mi alquiler como robado.
                if let Some(alquiler) = self.alquileres_propios.get_mut(&rental_id) {
                    alquiler.estado = EstadoAlquiler::Robado;
                    self.persistir();
                    println!(
                        "[{}] (origen) recibí el cierre por robo de {rental_id:?}: \
                         marco mi alquiler como robado",
                        self.id
                    );
                }
            }
            MensajeEntreEstacionesTCP::ConsultarBiciRobada { event_id, bici_id } => {
                // Soy el líder (la fuente de verdad de las robadas): respondo si la
                // bici figura como robada. Un follower no la conoce → responde que no.
                let robada = matches!(self.rol, RolEstacion::Lider { .. })
                    && self.bicis_robadas.contains(&bici_id);
                if let (Some(r), Ok(bytes)) = (
                    responder,
                    comun::serializacion::a_bytes(
                        &MensajeEntreEstacionesTCP::RespuestaBiciRobada { event_id, robada },
                    ),
                ) {
                    r.responder(bytes);
                }
            }

            // --- CU4: reconstrucción del registro tras una elección ---
            MensajeEntreEstacionesTCP::SolicitarAlquileresAbiertos { .. } => {
                // El nuevo líder está reconstruyendo su registro: le mando mis
                // alquileres propios que siguen activos.
                let alquileres: Vec<Alquiler> = self
                    .alquileres_propios
                    .values()
                    .filter(|a| a.estado == EstadoAlquiler::Activo)
                    // Sin preauth no entra al registro del líder (regla 7.1.1).
                    .filter(|a| a.preauth_id.is_some())
                    .cloned()
                    .collect();
                let respuesta = MensajeEntreEstacionesTCP::RespuestaAlquileres { alquileres };
                if let (Some(r), Ok(bytes)) = (responder, comun::serializacion::a_bytes(&respuesta))
                {
                    r.responder(bytes);
                }
            }

            // --- CU2 (recuperación): manejo de bicis huérfanas (8.2.1) ---
            MensajeEntreEstacionesTCP::BuscarAlquilerPropio { event_id, bici_id } => {
                // Otra estación busca al dueño de una bici sin alquiler en el
                // líder: si el alquiler activo es mío, se lo paso.
                let respuesta = match self
                    .alquileres_propios
                    .values()
                    .find(|a| a.bici_id == bici_id && a.estado == EstadoAlquiler::Activo)
                {
                    Some(a) => MensajeEntreEstacionesTCP::AlquilerEncontrado {
                        event_id,
                        alquiler: a.clone(),
                    },
                    None => MensajeEntreEstacionesTCP::NoLoTengo { event_id, bici_id },
                };
                if let (Some(r), Ok(bytes)) = (responder, comun::serializacion::a_bytes(&respuesta))
                {
                    r.responder(bytes);
                }
            }

            // --- CU4: reincorporación tardía ---
            MensajeEntreEstacionesTCP::IngresoTardio { alquileres } => {
                let mut a_programar = Vec::new();
                if let RolEstacion::Lider { registro, .. } = &mut self.rol {
                    for alquiler in alquileres {
                        // Solo lo que el registro no conoce: si el líder ya lo
                        // tiene (incluso cerrado), la versión del líder manda.
                        if !registro.contiene(&alquiler.rental_id) {
                            if alquiler.estado == EstadoAlquiler::Activo {
                                a_programar.push((alquiler.rental_id.clone(), alquiler.inicio));
                            }
                            registro.agregar(alquiler);
                        }
                    }
                }
                // El líder también vigila por inactividad los alquileres que le
                // llegan tarde (p. ej. de una estación origen que estuvo offline).
                for (rental_id, inicio) in a_programar {
                    self.programar_robo_por_timeout(rental_id, inicio, ctx);
                }
            }

            // --- Ring de elección (CU4) ---
            // Los mensajes del anillo se ACKean: el que reenvía solo da por
            // entregado si recibe respuesta (un nodo colgado acepta la conexión
            // pero no contesta, y sin ACK la elección se trabaría en él).
            MensajeEntreEstacionesTCP::Election { ids, iniciador } => {
                ack_ring(responder);
                let accion = self.eleccion.recibir_election(ids, iniciador);
                self.procesar_accion_ring(accion, ctx);
            }
            MensajeEntreEstacionesTCP::Coordinator { lider, term } => {
                ack_ring(responder);
                let accion = self.eleccion.recibir_coordinator(lider, term);
                self.procesar_accion_ring(accion, ctx);
            }
            // Anti split-brain: una estación que arranca pregunta quién es el líder
            // vigente; respondemos lo que reconocemos (o None si estamos en elección).
            MensajeEntreEstacionesTCP::QuienEsLider => {
                let lider_id = match self.eleccion.lider_conocido() {
                    EstadoLider::Conocido(id) => Some(id),
                    EstadoLider::EnEleccion | EstadoLider::Desconocido => None,
                };
                if let (Some(r), Ok(bytes)) = (
                    responder,
                    comun::serializacion::a_bytes(&MensajeEntreEstacionesTCP::LiderActual {
                        lider_id,
                        term: self.eleccion.term(),
                    }),
                ) {
                    r.responder(bytes);
                }
            }
            // Manejo de bicis huérfanas: Etapa 6.
            _ => {}
        }
    }

    /// Aplica el resultado de un paso del Ring: primero el cambio de rol/líder que
    /// dictaminó la elección, después el reenvío del mensaje por el anillo.
    fn procesar_accion_ring(&mut self, accion: AccionRing, ctx: &mut Context<Self>) {
        self.aplicar_liderazgo(ctx);
        self.ejecutar_accion_ring(accion, ctx);
    }

    /// Reenvía un mensaje del Ring probando los `destinos` en orden de anillo
    /// hasta que uno acepte la conexión (así se saltean los nodos caídos). Si
    /// ninguno es alcanzable, el anillo se reduce a esta estación: el mensaje
    /// "da la vuelta" entregándose a sí misma, lo que cierra la elección si era
    /// propia (y se descarta si no lo era).
    fn ejecutar_accion_ring(&mut self, accion: AccionRing, ctx: &mut Context<Self>) {
        let (destinos, mensaje) = match accion {
            AccionRing::Ignorar => return,
            AccionRing::Reenviar { destinos, mensaje }
            | AccionRing::AsumirYReenviar { destinos, mensaje } => (destinos, mensaje),
        };
        if destinos.is_empty() {
            return;
        }
        let (Some(comunicador), Ok(bytes)) = (
            self.comunicador.clone(),
            comun::serializacion::a_bytes(&mensaje),
        ) else {
            return;
        };
        let direcciones: Vec<(EstacionId, SocketAddr)> = destinos
            .iter()
            .filter_map(|id| self.estaciones.get(id).map(|addr| (*id, *addr)))
            .collect();
        let yo = ctx.address();
        let mi_id = self.id;
        let fut = async move {
            for (id, addr) in direcciones {
                // Entrega con ACK y plazo corto: cubre tanto al nodo caído
                // (conexión rechazada) como al colgado (acepta y no contesta).
                let entregado = comun::tiempo::con_timeout(
                    TIMEOUT_ACK_RING,
                    comunicador.send(ConsultarTcp {
                        destino: addr,
                        datos: bytes.clone(),
                    }),
                )
                .await
                .and_then(|r| r.ok())
                .flatten()
                .is_some();
                if entregado {
                    return;
                }
                println!("[{mi_id}] {id} no responde, salteo al siguiente del anillo");
            }
            // Nadie alcanzable: me lo entrego a mí misma (vuelta completa).
            yo.do_send(PaqueteRecibido {
                transporte: Transporte::Tcp,
                datos: bytes,
                responder: None,
            });
        };
        ctx.spawn(fut.into_actor(self).map(|_, _, _| ()));
    }

    /// (Re)configura las conexiones persistentes hacia los peers estables: la
    /// pasarela (siempre) y el líder (solo si soy follower; no me conecto a mí
    /// mismo). Es idempotente en el Comunicador, así que se puede llamar al
    /// arrancar y en cada cambio de líder. No hace nada fuera del modo persistente.
    fn configurar_peers_persistentes(&self) {
        if !self.usa_peers_persistentes {
            return;
        }
        let Some(comunicador) = &self.comunicador else {
            return;
        };
        comunicador.do_send(ConfigurarPeerPersistente {
            rol: RolPeer::Pasarela,
            addr: self.pasarela,
        });
        if self.lider_id != self.id {
            comunicador.do_send(ConfigurarPeerPersistente {
                rol: RolPeer::Lider,
                addr: self.lider,
            });
        }
    }

    /// Sincroniza el rol y el puntero al líder con lo que dictaminó el Ring. Si
    /// esta estación pasa a ser líder, reconstruye el registro pidiéndole a cada
    /// estación sus alquileres abiertos; si deja de serlo, vuelve a follower.
    fn aplicar_liderazgo(&mut self, ctx: &mut Context<Self>) {
        let EstadoLider::Conocido(lider_id) = self.eleccion.lider_conocido() else {
            return;
        };
        self.fallos_lider = 0;
        self.intervalos_en_eleccion = 0;
        self.lider_id = lider_id;
        if let Some(addr) = self.estaciones.get(&lider_id).copied() {
            self.lider = addr;
        }
        let lider_cambio = lider_id != self.lider_id_anterior;
        self.lider_id_anterior = lider_id;
        let soy_lider = lider_id == self.id;
        let era_lider = matches!(self.rol, RolEstacion::Lider { .. });
        if soy_lider && !era_lider {
            println!(
                "[{}] asumo como líder (term {})",
                self.id,
                self.eleccion.term()
            );
            self.rol = RolEstacion::lider_nuevo();
            self.reconstruir_registro(ctx);
        } else if !soy_lider && era_lider {
            println!("[{}] dejo de ser líder: ahora lidera {}", self.id, lider_id);
            self.rol = RolEstacion::Follower;
        } else if !soy_lider && lider_cambio {
            println!(
                "[{}] reconozco al líder {} (term {})",
                self.id,
                lider_id,
                self.eleccion.term()
            );
        }
        // Re-apuntar la conexión persistente al líder vigente (si soy follower).
        if lider_cambio {
            self.configurar_peers_persistentes();
        }
        // Con líder conocido (sea quien sea), los eventos diferidos se reintentan.
        self.descargar_pendientes(ctx);
        // Reincorporación tardía (CU4): si soy follower con alquileres activos
        // y acabo de enterarme de un líder nuevo, se los mando por las dudas
        // (pude haber estado caída durante su reconstrucción). El líder solo
        // incorpora los que no conoce, así no re-abre nada.
        if lider_cambio && !soy_lider {
            let alquileres: Vec<Alquiler> = self
                .alquileres_propios
                .values()
                .filter(|a| a.estado == EstadoAlquiler::Activo)
                .filter(|a| a.preauth_id.is_some())
                .cloned()
                .collect();
            if !alquileres.is_empty() {
                self.enviar_evento_al_lider(
                    MensajeEntreEstacionesTCP::IngresoTardio { alquileres },
                    ctx,
                );
            }
        }
    }

    /// Reconstruye el registro del líder recién electo: arranca con los alquileres
    /// propios y le pide los suyos a cada una de las otras estaciones
    /// (`SolicitarAlquileresAbiertos`). Si alguna no responde, se continúa con
    /// datos parciales (como pide el enunciado); sus alquileres se recuperan por
    /// la vía de las bicis huérfanas (Etapa 6) o cuando se reincorpore.
    fn reconstruir_registro(&mut self, ctx: &mut Context<Self>) {
        let propios: Vec<Alquiler> = self
            .alquileres_propios
            .values()
            .filter(|a| a.estado == EstadoAlquiler::Activo)
            .cloned()
            .collect();
        if let RolEstacion::Lider { registro, .. } = &mut self.rol {
            for alquiler in propios {
                registro.agregar(alquiler);
            }
        }
        let term = self.eleccion.term();
        let comunicador = self.comunicador.clone();
        let destinos: Vec<SocketAddr> = self
            .estaciones
            .iter()
            .filter(|(id, _)| **id != self.id)
            .map(|(_, addr)| *addr)
            .collect();
        let fut = async move {
            let mut recuperados = Vec::new();
            let (Some(comunicador), Ok(bytes)) = (
                comunicador,
                comun::serializacion::a_bytes(
                    &MensajeEntreEstacionesTCP::SolicitarAlquileresAbiertos { term },
                ),
            ) else {
                return recuperados;
            };
            for destino in destinos {
                let respuesta = comunicador
                    .send(ConsultarTcp {
                        destino,
                        datos: bytes.clone(),
                    })
                    .await;
                if let Ok(Some(datos)) = respuesta {
                    if let Ok(MensajeEntreEstacionesTCP::RespuestaAlquileres { alquileres }) =
                        comun::serializacion::desde_bytes(&datos)
                    {
                        recuperados.extend(alquileres);
                    }
                }
            }
            recuperados
        }
        .into_actor(self)
        .map(|alquileres, actor, ctx| {
            if let RolEstacion::Lider { registro, .. } = &mut actor.rol {
                for alquiler in alquileres {
                    registro.agregar(alquiler);
                }
                println!(
                    "[{}] registro reconstruido: {} alquileres activos",
                    actor.id,
                    registro.activos()
                );
            }
            // Re-programar los timers de robo por inactividad desde el inicio de
            // cada alquiler: así la detección sobrevive a la caída del líder
            // anterior (el nuevo líder retoma el plazo restante).
            actor.reprogramar_timers_robo(ctx);
        });
        ctx.spawn(fut);
    }

    /// Discovery del líder al arrancar (anti split-brain, CU4): pregunta a cada
    /// vecina quién es el líder vigente y, si alguna reporta uno con `term` no más
    /// viejo que el propio, lo adopta — en vez de quedarse con el de la config, que
    /// pudo haber cambiado mientras esta estación estaba caída. Si nadie responde
    /// (cold start), conserva el bootstrap de la config. Lo dispara el cableado del
    /// Comunicador, una vez que la estación ya puede hablar por la red.
    fn descubrir_lider_al_arrancar(&mut self, ctx: &mut Context<Self>) {
        let comunicador = self.comunicador.clone();
        let destinos: Vec<SocketAddr> = self
            .estaciones
            .iter()
            .filter(|(id, _)| **id != self.id)
            .map(|(_, addr)| *addr)
            .collect();
        if destinos.is_empty() {
            return; // anillo de un solo nodo: no hay a quién preguntarle
        }
        let fut = async move {
            let mut mejor: Option<(EstacionId, u64)> = None;
            let (Some(comunicador), Ok(bytes)) = (
                comunicador,
                comun::serializacion::a_bytes(&MensajeEntreEstacionesTCP::QuienEsLider),
            ) else {
                return mejor;
            };
            for destino in destinos {
                if let Ok(Some(datos)) = comunicador
                    .send(ConsultarTcp {
                        destino,
                        datos: bytes.clone(),
                    })
                    .await
                {
                    if let Ok(MensajeEntreEstacionesTCP::LiderActual {
                        lider_id: Some(id),
                        term,
                    }) = comun::serializacion::desde_bytes(&datos)
                    {
                        // Nos quedamos con el de term más alto (el más reciente).
                        if mejor.is_none_or(|(_, t)| term > t) {
                            mejor = Some((id, term));
                        }
                    }
                }
            }
            mejor
        }
        .into_actor(self)
        .map(|mejor, actor, ctx| {
            if let Some((lider, term)) = mejor {
                if actor.eleccion.adoptar(lider, term) {
                    println!(
                        "[{}] discovery al arrancar: el líder vigente es {} (term {})",
                        actor.id, lider, term
                    );
                    actor.aplicar_liderazgo(ctx);
                }
            }
        });
        ctx.spawn(fut);
    }

    /// Vigilancia del líder (corre cada `INTERVALO_VIGILANCIA`): un follower
    /// sondea al líder con un request-response; tras `UMBRAL_FALLOS_LIDER` fallos
    /// consecutivos lo da por caído e inicia una elección. Cubre tanto al líder
    /// caído (conexión rechazada, falla al instante) como al colgado (el sondeo
    /// vence por el timeout de lectura del Comunicador).
    fn vigilar_lider(&mut self, ctx: &mut Context<Self>) {
        match self.eleccion.lider_conocido() {
            EstadoLider::Conocido(lider_id) if lider_id == self.id => return, // el líder soy yo
            EstadoLider::Conocido(_) => {}
            EstadoLider::EnEleccion => {
                // Una elección puede quedar trabada si el que tenía que reenviar
                // se cayó con el mensaje encima: tras unos intervalos sin
                // resolverse, la reiniciamos.
                self.intervalos_en_eleccion += 1;
                if self.intervalos_en_eleccion >= UMBRAL_ELECCION_TRABADA {
                    self.intervalos_en_eleccion = 0;
                    let accion = self.eleccion.iniciar();
                    self.procesar_accion_ring(accion, ctx);
                }
                return;
            }
            EstadoLider::Desconocido => {
                // No hay líder que vigilar: directamente elegimos uno.
                let accion = self.eleccion.iniciar();
                self.procesar_accion_ring(accion, ctx);
                return;
            }
        }
        let comunicador = self.comunicador.clone();
        let lider = self.lider;
        let fut = async move { sondear_lider(&comunicador, lider).await }
            .into_actor(self)
            .map(|vivo, actor, ctx| {
                if vivo {
                    actor.fallos_lider = 0;
                    // El líder responde: si quedaron eventos diferidos (p.ej. de
                    // un corte transitorio), este es el momento de reenviarlos.
                    actor.descargar_pendientes(ctx);
                    return;
                }
                actor.fallos_lider += 1;
                if actor.fallos_lider >= UMBRAL_FALLOS_LIDER {
                    actor.fallos_lider = 0;
                    println!(
                        "[{}] el líder {} no responde: inicio elección",
                        actor.id, actor.lider_id
                    );
                    let accion = actor.eleccion.iniciar();
                    actor.procesar_accion_ring(accion, ctx);
                }
            });
        ctx.spawn(fut);
    }

    /// Supervisión de elección (modo persistente): no sondea al líder (de eso se
    /// encarga la salud de la conexión persistente), solo destraba una elección
    /// que no converge y arranca una si quedó sin líder conocido. Es chequeo de
    /// estado interno: no abre conexiones.
    fn supervisar_eleccion(&mut self, ctx: &mut Context<Self>) {
        match self.eleccion.lider_conocido() {
            EstadoLider::EnEleccion => {
                self.intervalos_en_eleccion += 1;
                if self.intervalos_en_eleccion >= UMBRAL_ELECCION_TRABADA {
                    self.intervalos_en_eleccion = 0;
                    let accion = self.eleccion.iniciar();
                    self.procesar_accion_ring(accion, ctx);
                }
            }
            EstadoLider::Desconocido if !self.esta_desconectado() => {
                let accion = self.eleccion.iniciar();
                self.procesar_accion_ring(accion, ctx);
            }
            _ => self.intervalos_en_eleccion = 0,
        }
    }

    /// Programa el "aviso de robo" por inactividad de un alquiler: al cumplirse
    /// `duracion_maxima_alquiler` **desde su inicio**, si sigue activo, se marca
    /// Robado y se cobra la reposición. El plazo se calcula desde `inicio` (no
    /// desde ahora), así sobrevive a reconstrucciones del registro y reinicios
    /// del líder: un alquiler ya vencido dispara enseguida (restante 0). Solo
    /// tiene sentido en el líder (es la fuente de verdad de los alquileres).
    fn programar_robo_por_timeout(
        &self,
        rental_id: RentalId,
        inicio: Timestamp,
        ctx: &mut Context<Self>,
    ) {
        let transcurrido =
            std::time::Duration::from_millis(Timestamp::ahora().0.saturating_sub(inicio.0));
        let restante = self.duracion_maxima_alquiler.saturating_sub(transcurrido);
        println!(
            "[{}] (líder) aviso de robo por inactividad de {rental_id:?} programado en {restante:?}",
            self.id
        );
        ctx.run_later(restante, move |act, ctx| {
            act.verificar_robo_por_timeout(rental_id, ctx);
        });
    }

    /// (Re)programa los timers de robo por inactividad de todos los alquileres
    /// activos del registro. Lo llama el líder al arrancar (recuperó su registro
    /// de disco) y tras reconstruirlo en una elección: cada timer se recalcula
    /// desde el `inicio` del alquiler, de modo que la detección sobreviva a la
    /// caída del líder anterior.
    fn reprogramar_timers_robo(&mut self, ctx: &mut Context<Self>) {
        let activos = match &self.rol {
            RolEstacion::Lider { registro, .. } => registro.activos_para_timer(),
            RolEstacion::Follower => return,
        };
        if !activos.is_empty() {
            println!(
                "[{}] (líder) re-programo {} timers de robo por inactividad",
                self.id,
                activos.len()
            );
        }
        for (rental_id, inicio) in activos {
            self.programar_robo_por_timeout(rental_id, inicio, ctx);
        }
    }

    /// Datos que necesita `procesar_operacion`, capturados antes del trabajo async.
    fn contexto(&mut self, ctx: &mut Context<Self>) -> ContextoOperacion {
        let n = self.proximo();
        ContextoOperacion {
            tx_id: TransaccionId(format!("T-{}-{}", self.id.0, n)),
            rental_id: RentalId(format!("R-{}-{}", self.id.0, n)),
            event_id: EventId(format!("E-{}-{}", self.id.0, n)),
            slots: self.slots.clone(),
            estacion_origen: self.id,
            pasarela: self.pasarela,
            lider: self.lider,
            comunicador: self.comunicador.clone(),
            yo: ctx.address(),
            desconectado: self.esta_desconectado(),
            es_lider: matches!(self.rol, RolEstacion::Lider { .. }),
            bicis_robadas: self.bicis_robadas.clone(),
        }
    }
}

/// Contesta el ACK de un mensaje del Ring (si vino con canal de respuesta).
fn ack_ring(responder: Option<Responder>) {
    if let (Some(r), Ok(bytes)) = (
        responder,
        comun::serializacion::a_bytes(&MensajeEntreEstacionesTCP::EventoProcesadoAck {
            event_id: EventId("ring-ack".to_string()),
        }),
    ) {
        r.responder(bytes);
    }
}

/// Distancia aproximada en km entre dos puntos `(lat, lon)` en grados, con la
/// fórmula equirectangular (suficiente para distancias urbanas y mucho más barata
/// que la de Haversine). No usamos crates externas: solo `f64` de la std.
fn distancia_km(a: (f64, f64), b: (f64, f64)) -> f64 {
    const R_TIERRA_KM: f64 = 6371.0;
    let (lat1, lon1) = (a.0.to_radians(), a.1.to_radians());
    let (lat2, lon2) = (b.0.to_radians(), b.1.to_radians());
    let x = (lon2 - lon1) * ((lat1 + lat2) / 2.0).cos();
    let y = lat2 - lat1;
    R_TIERRA_KM * (x * x + y * y).sqrt()
}

impl Actor for Estacion {
    type Context = Context<Self>;

    fn started(&mut self, ctx: &mut Self::Context) {
        println!(
            "[{}] actor Estacion iniciado (ubicacion {:?}, {} slots, pasarela {})",
            self.id,
            self.ubicacion,
            self.slots.len(),
            self.pasarela
        );
        // Gossip: cada estación (incluido el líder, a sí mismo) le manda su estado
        // al líder por UDP. Así el líder arma su cache para responder consultas.
        ctx.run_interval(INTERVALO_GOSSIP, |act, ctx| act.enviar_estado(ctx));

        // Si arranco como líder con un registro recuperado de disco (persistencia),
        // re-programo los timers de robo por inactividad desde el inicio de cada
        // alquiler activo, así un reinicio del líder no pierde la detección.
        self.reprogramar_timers_robo(ctx);

        if self.usa_peers_persistentes {
            // Modo producción: la salud de las conexiones persistentes dispara
            // el reenvío de colas y la detección de caída del líder (event-driven).
            // Solo queda una supervisión de elección trabada (chequeo de estado
            // interno, no toca la red): si una elección no converge, se reinicia.
            ctx.run_interval(INTERVALO_SUPERVISION_ELECCION, |act, ctx| {
                act.supervisar_eleccion(ctx)
            });
        } else {
            // Modo test: sin conexiones persistentes, se sondea/reintenta por
            // polling (más simple de mockear en los tests unitarios).
            ctx.run_interval(INTERVALO_VIGILANCIA, |act, ctx| act.vigilar_lider(ctx));
            ctx.run_interval(self.intervalo_reintento_commits, |act, ctx| {
                act.reintentar_commits(ctx)
            });
            ctx.run_interval(self.intervalo_regularizacion, |act, ctx| {
                act.regularizar_pagos(ctx)
            });
        }
    }
}

impl Handler<RegistrarComunicador> for Estacion {
    type Result = ();

    fn handle(&mut self, msg: RegistrarComunicador, ctx: &mut Self::Context) {
        self.comunicador = Some(msg.0);
        // En modo persistente, abrimos las conexiones estables (pasarela y, si
        // soy follower, líder). Su salud disparará el reenvío de colas.
        self.configurar_peers_persistentes();
        // Ya podemos hablar por la red: preguntamos a las vecinas quién es el líder
        // vigente, para no quedarnos con el de la config si quedó viejo mientras
        // estábamos caídas (anti split-brain al reiniciar).
        self.descubrir_lider_al_arrancar(ctx);
    }
}

impl Handler<PeerConectado> for Estacion {
    type Result = ();

    fn handle(&mut self, msg: PeerConectado, ctx: &mut Self::Context) {
        match msg.rol {
            RolPeer::Pasarela => {
                // La pasarela volvió: regularizamos pagos offline y reintentamos
                // los commits decididos sin confirmar (Caso C/E), event-driven.
                self.reintentar_commits(ctx);
                self.regularizar_pagos(ctx);
            }
            RolPeer::Lider => {
                // El líder está accesible: drenamos los eventos diferidos.
                self.fallos_lider = 0;
                self.descargar_pendientes(ctx);
            }
        }
    }
}

impl Handler<PeerDesconectado> for Estacion {
    type Result = ();

    fn handle(&mut self, msg: PeerDesconectado, ctx: &mut Self::Context) {
        // La caída del líder dispara una elección (salvo que sea por estar
        // nosotras offline: ahí quedamos aisladas a propósito, sin elegir).
        if msg.rol == RolPeer::Lider && !self.esta_desconectado() {
            if let EstadoLider::Conocido(lider_id) = self.eleccion.lider_conocido() {
                if lider_id != self.id {
                    println!(
                        "[{}] se cayó la conexión con el líder {}: inicio elección",
                        self.id, lider_id
                    );
                    let accion = self.eleccion.iniciar();
                    self.procesar_accion_ring(accion, ctx);
                }
            }
        }
    }
}

impl Handler<RedescubrirLider> for Estacion {
    type Result = ();

    fn handle(&mut self, _msg: RedescubrirLider, ctx: &mut Self::Context) {
        // La estación recuperó la red (comando `conectar`): puede que se haya
        // elegido otro líder mientras estaba aislada. Reusamos el mismo discovery
        // del arranque, que pregunta a las vecinas y adopta al de term mayor; si
        // esta estación era líder y hay uno más nuevo, `aplicar_liderazgo` la baja
        // a follower. Así un ex-líder que reconecta no queda como segundo líder.
        self.descubrir_lider_al_arrancar(ctx);
    }
}

impl Handler<MostrarGossip> for Estacion {
    type Result = ();

    fn handle(&mut self, _msg: MostrarGossip, _ctx: &mut Self::Context) {
        self.mostrar_gossip = !self.mostrar_gossip;
        println!(
            "[{}] logs de gossip UDP {}",
            self.id,
            if self.mostrar_gossip {
                "ACTIVADOS"
            } else {
                "DESACTIVADOS"
            }
        );
    }
}

impl Handler<RegistrarCommitPendiente> for Estacion {
    type Result = ();

    fn handle(&mut self, msg: RegistrarCommitPendiente, _ctx: &mut Self::Context) {
        self.commits_pendientes.insert(msg.tx_id, msg.preauth_id);
        // La respuesta de este handler ES la garantía: el 2PC espera este send
        // antes de mandar el Commit, así que al persistir acá la constancia
        // queda en disco antes de que el Commit salga a la red.
        self.persistir();
    }
}

impl Handler<CommitConfirmado> for Estacion {
    type Result = ();

    fn handle(&mut self, msg: CommitConfirmado, _ctx: &mut Self::Context) {
        if self.commits_pendientes.remove(&msg.tx_id).is_some() {
            self.persistir();
        }
    }
}

/// ¿El Comunicador tiene a `destino` por alcanzable? (sin Comunicador: sí).
async fn consultar_alcanzable(
    comunicador: &Option<Addr<Comunicador>>,
    destino: SocketAddr,
) -> bool {
    match comunicador {
        Some(c) => c
            .send(ConsultarAlcanzable { destino })
            .await
            .unwrap_or(true),
        None => true,
    }
}

/// Sondea al líder con un `PreguntarLider` (request-response): `true` si contestó
/// algo, `false` si no se pudo conectar o no respondió. Reusa el mensaje de
/// discovery: no hace falta un "ping" propio en el protocolo.
async fn sondear_lider(comunicador: &Option<Addr<Comunicador>>, lider: SocketAddr) -> bool {
    let Some(comunicador) = comunicador else {
        // Sin red cableada (tests unitarios) no se vigila a nadie.
        return true;
    };
    let consulta = MensajeUsuario::Consulta(MensajeUsuarioAEstacionConsulta::PreguntarLider);
    let Ok(bytes) = comun::serializacion::a_bytes(&consulta) else {
        return true;
    };
    matches!(
        comunicador
            .send(ConsultarTcp {
                destino: lider,
                datos: bytes,
            })
            .await,
        Ok(Some(_))
    )
}

/// Le pregunta al líder si una bici figura como robada (request-response), antes
/// de aceptarla en una devolución. Es best-effort: si no hay Comunicador, el líder
/// es inalcanzable o no responde, devuelve `false` (no se pudo verificar → la
/// devolución sigue su camino normal, sin aviso de robo).
async fn consultar_bici_robada(
    comunicador: &Option<Addr<Comunicador>>,
    lider: SocketAddr,
    event_id: EventId,
    bici_id: BiciId,
) -> bool {
    let Some(comunicador) = comunicador.as_ref() else {
        return false;
    };
    let pedido = MensajeEntreEstacionesTCP::ConsultarBiciRobada { event_id, bici_id };
    let Ok(bytes) = comun::serializacion::a_bytes(&pedido) else {
        return false;
    };
    let respuesta = comunicador
        .send(ConsultarTcp {
            destino: lider,
            datos: bytes,
        })
        .await
        .ok()
        .flatten();
    matches!(
        respuesta
            .and_then(|r| comun::serializacion::desde_bytes::<MensajeEntreEstacionesTCP>(&r).ok()),
        Some(MensajeEntreEstacionesTCP::RespuestaBiciRobada { robada: true, .. })
    )
}

/// Le pide al Comunicador que haga el request-response con la pasarela y devuelve
/// la respuesta deserializada (o `None` si no hay Comunicador o no se pudo).
async fn consultar_pasarela(
    comunicador: &Option<Addr<Comunicador>>,
    pasarela: SocketAddr,
    pedido: &MensajeEstacionAPasarela,
) -> Option<MensajePasarelaAEstacion> {
    let comunicador = comunicador.as_ref()?;
    let bytes = comun::serializacion::a_bytes(pedido).ok()?;
    let respuesta = comunicador
        .send(ConsultarTcp {
            destino: pasarela,
            datos: bytes,
        })
        .await
        .ok()
        .flatten()?;
    comun::serializacion::desde_bytes(&respuesta).ok()
}

impl Handler<PaqueteRecibido> for Estacion {
    type Result = ResponseActFuture<Self, ()>;

    fn handle(&mut self, msg: PaqueteRecibido, ctx: &mut Self::Context) -> Self::Result {
        // En modo offline la estación queda aislada de la red pero sigue
        // atendiendo al usuario local: se saltea el tráfico inter-estación y el
        // gossip (caen al `match` final, fallan como `MensajeUsuario` y se
        // descartan), y solo se procesan los mensajes del usuario.
        let offline = self.esta_desconectado();

        // ¿Es un mensaje entre estaciones (reporte/devolución al líder, Ring, etc.)?
        if !offline {
            if let Ok(entre_estaciones) =
                comun::serializacion::desde_bytes::<MensajeEntreEstacionesTCP>(&msg.datos)
            {
                self.manejar_entre_estaciones(entre_estaciones, msg.responder, ctx);
                return Box::pin(async {}.into_actor(self).map(|_, _, _| ()));
            }
        }

        // ¿Es gossip de estado (UDP)? Solo le interesa al líder, que actualiza su cache.
        if !offline {
            if let Ok(MensajeEntreEstacionesUDP::EstadoEstacion {
                estacion_id,
                ubicacion,
                bicis_disponibles,
                slots_libres,
                timestamp,
            }) = comun::serializacion::desde_bytes::<MensajeEntreEstacionesUDP>(&msg.datos)
            {
                if self.mostrar_gossip {
                    println!(
                        "[{}] ← gossip UDP de {}: {} bicis, {} slots libres",
                        self.id, estacion_id, bicis_disponibles, slots_libres
                    );
                }
                if let RolEstacion::Lider { cache, .. } = &mut self.rol {
                    cache.insert(
                        estacion_id,
                        InfoEstacion {
                            estacion_id,
                            ubicacion,
                            bicis_disponibles,
                            slots_libres,
                            last_seen: timestamp,
                        },
                    );
                }
                return Box::pin(async {}.into_actor(self).map(|_, _, _| ()));
            }
        }

        let pedido: Option<MensajeUsuario> = comun::serializacion::desde_bytes(&msg.datos).ok();

        match (pedido, msg.responder) {
            (Some(MensajeUsuario::Consulta(consulta)), Some(responder)) => {
                let respuesta = self.manejar_consulta(consulta);
                if let Ok(bytes) = comun::serializacion::a_bytes(&respuesta) {
                    responder.responder(bytes);
                }
                Box::pin(async {}.into_actor(self).map(|_, _, _| ()))
            }
            (Some(MensajeUsuario::Operacion(operacion)), Some(responder)) => {
                let ctx = self.contexto(ctx);
                Box::pin(
                    async move {
                        let (respuesta, alquiler, pago) = procesar_operacion(operacion, ctx).await;
                        (respuesta, alquiler, pago, responder)
                    }
                    .into_actor(self)
                    .map(
                        |(respuesta, alquiler, pago, responder), actor, ctx| {
                            if let Some(a) = alquiler {
                                actor.reportar_alquiler(&a, ctx);
                                actor.alquileres_propios.insert(a.rental_id.clone(), a);
                                if let Some(pago) = pago {
                                    actor.pagos_pendientes.push(pago);
                                }
                                actor.persistir();
                            }
                            if let MensajeEstacionAUsuario::DevolucionAceptada { bici_id } =
                                &respuesta
                            {
                                ctx.address().do_send(ProcesarDevolucion {
                                    bici_id: *bici_id,
                                    t1: Timestamp::ahora(),
                                    ya_reprocesada: false,
                                    intento: 0,
                                });
                            }
                            if let Ok(bytes) = comun::serializacion::a_bytes(&respuesta) {
                                responder.responder(bytes);
                            }
                        },
                    ),
                )
            }
            _ => Box::pin(async {}.into_actor(self).map(|_, _, _| ())),
        }
    }
}

/// Handlers de los mensajes de **diagnóstico** (solo test): viven acá, junto al
/// resto del código de test y fuera del flujo de producción. Un `impl Handler`
/// para `Estacion` en un submódulo hijo igual accede a sus campos privados.
#[cfg(test)]
mod handlers_diagnostico {
    use super::*;

    impl Handler<ConsultarRegistro> for Estacion {
        type Result = usize;

        fn handle(&mut self, _msg: ConsultarRegistro, _ctx: &mut Self::Context) -> usize {
            match &self.rol {
                RolEstacion::Lider { registro, .. } => registro.activos(),
                RolEstacion::Follower => 0,
            }
        }
    }

    impl Handler<ConsultarCache> for Estacion {
        type Result = usize;

        fn handle(&mut self, _msg: ConsultarCache, _ctx: &mut Self::Context) -> usize {
            match &self.rol {
                RolEstacion::Lider { cache, .. } => cache.len(),
                RolEstacion::Follower => 0,
            }
        }
    }

    impl Handler<ConsultarPendientes> for Estacion {
        type Result = usize;

        fn handle(&mut self, _msg: ConsultarPendientes, _ctx: &mut Self::Context) -> usize {
            self.eventos_pendientes.len()
        }
    }

    impl Handler<ConsultarHuerfanas> for Estacion {
        type Result = usize;

        fn handle(&mut self, _msg: ConsultarHuerfanas, _ctx: &mut Self::Context) -> usize {
            self.huerfanas_confirmadas
        }
    }

    impl Handler<ConsultarPropiosActivos> for Estacion {
        type Result = usize;

        fn handle(&mut self, _msg: ConsultarPropiosActivos, _ctx: &mut Self::Context) -> usize {
            self.alquileres_propios
                .values()
                .filter(|a| a.estado == EstadoAlquiler::Activo)
                .count()
        }
    }

    impl Handler<ConsultarRobadas> for Estacion {
        type Result = usize;

        fn handle(&mut self, _msg: ConsultarRobadas, _ctx: &mut Self::Context) -> usize {
            self.bicis_robadas.len()
        }
    }

    impl Handler<ConsultarPagosPendientes> for Estacion {
        type Result = usize;

        fn handle(&mut self, _msg: ConsultarPagosPendientes, _ctx: &mut Self::Context) -> usize {
            self.pagos_pendientes.len()
        }
    }

    impl Handler<ConsultarCobrosFallidos> for Estacion {
        type Result = usize;

        fn handle(&mut self, _msg: ConsultarCobrosFallidos, _ctx: &mut Self::Context) -> usize {
            self.cobros_fallidos
        }
    }

    impl Handler<ConsultarCommitsPendientes> for Estacion {
        type Result = usize;

        fn handle(&mut self, _msg: ConsultarCommitsPendientes, _ctx: &mut Self::Context) -> usize {
            self.commits_pendientes.len()
        }
    }

    impl Handler<ConsultarLider> for Estacion {
        type Result = MessageResult<ConsultarLider>;

        fn handle(&mut self, _msg: ConsultarLider, _ctx: &mut Self::Context) -> Self::Result {
            let lider_id = match self.eleccion.lider_conocido() {
                EstadoLider::Conocido(id) => Some(id),
                _ => None,
            };
            MessageResult(InfoLider {
                lider_id,
                term: self.eleccion.term(),
                soy_lider: matches!(self.rol, RolEstacion::Lider { .. }),
            })
        }
    }
}

#[cfg(test)]
mod tests {
    use super::*;
    use comun::comunicador::Transporte;
    use comun::framing::{enmarcar, Desenmarcador};
    use comun::{BiciId, DatosTarjeta, UsuarioId};

    fn paquete_tcp(
        msg: &MensajeEntreEstacionesTCP,
        responder: Option<Responder>,
    ) -> PaqueteRecibido {
        PaqueteRecibido {
            transporte: Transporte::Tcp,
            datos: comun::serializacion::a_bytes(msg).unwrap(),
            responder,
        }
    }

    fn paquete_udp(msg: &MensajeEntreEstacionesUDP) -> PaqueteRecibido {
        PaqueteRecibido {
            transporte: Transporte::Udp,
            datos: comun::serializacion::a_bytes(msg).unwrap(),
            responder: None,
        }
    }
    use std::io::{Read, Write};
    use std::net::TcpListener;

    fn tarjeta() -> DatosTarjeta {
        DatosTarjeta {
            numero: "4111111111111111".to_string(),
            titular: "Alice".to_string(),
            vencimiento: "12/29".to_string(),
            cvv: "123".to_string(),
        }
    }

    fn alquilar(slot_id: u32) -> SolicitudUsuario {
        SolicitudUsuario(MensajeUsuarioAEstacion::SolicitudAlquiler {
            usuario_id: UsuarioId("alice".to_string()),
            slot_id,
            tarjeta: tarjeta(),
        })
    }

    fn denunciar_robo() -> SolicitudUsuario {
        SolicitudUsuario(MensajeUsuarioAEstacion::DenunciarRobo {
            usuario_id: UsuarioId("alice".to_string()),
        })
    }

    /// Crea la estación (como líder) con su Comunicador cableado (igual que en `main`).
    async fn arrancar(slots: Vec<Addr<Slot>>, pasarela: SocketAddr) -> Addr<Estacion> {
        // Como es líder, registra sus propios alquileres; la dirección de líder no se usa.
        let lider = "127.0.0.1:9".parse().unwrap();
        let estacion = Estacion::new(
            EstacionId(1),
            (0.0, 0.0),
            slots,
            pasarela,
            (EstacionId(1), lider),
            true,
            HashMap::new(),
        )
        .start();
        let comunicador = Comunicador::new(
            "127.0.0.1:0".parse().unwrap(),
            "127.0.0.1:0".parse().unwrap(),
            estacion.clone().recipient(),
        )
        .start();
        estacion
            .send(RegistrarComunicador(comunicador))
            .await
            .unwrap();
        estacion
    }

    /// Vecina de mentira que, ante un `QuienEsLider`, contesta que el líder vigente
    /// es la estación `lider` en `term`. La usa el test del discovery al arrancar.
    fn vecina_que_conoce_lider(lider: EstacionId, term: u64) -> SocketAddr {
        let listener = TcpListener::bind("127.0.0.1:0").unwrap();
        let addr = listener.local_addr().unwrap();
        std::thread::spawn(move || {
            for conexion in listener.incoming() {
                let Ok(mut stream) = conexion else { continue };
                let mut desen = Desenmarcador::new();
                let mut buf = [0u8; 4096];
                loop {
                    let n = match stream.read(&mut buf) {
                        Ok(0) | Err(_) => break,
                        Ok(n) => n,
                    };
                    desen.alimentar(&buf[..n]);
                    if let Some(payload) = desen.siguiente_payload() {
                        if let Ok(MensajeEntreEstacionesTCP::QuienEsLider) =
                            comun::serializacion::desde_bytes(&payload)
                        {
                            let resp = MensajeEntreEstacionesTCP::LiderActual {
                                lider_id: Some(lider),
                                term,
                            };
                            let _ = stream.write_all(&enmarcar(&resp).unwrap());
                        }
                    }
                }
            }
        });
        addr
    }

    #[test]
    fn responde_quien_es_lider_con_su_vista() {
        System::new().block_on(async {
            let estacion = arrancar(vec![], pasarela_mock(true)).await; // líder id 1
            let (responder, rx) = Responder::canal();
            estacion
                .send(paquete_tcp(
                    &MensajeEntreEstacionesTCP::QuienEsLider,
                    Some(responder),
                ))
                .await
                .unwrap();
            let datos = rx
                .recv_timeout(std::time::Duration::from_secs(2))
                .expect("debería responder LiderActual");
            let resp: MensajeEntreEstacionesTCP =
                comun::serializacion::desde_bytes(&datos).unwrap();
            assert_eq!(
                resp,
                MensajeEntreEstacionesTCP::LiderActual {
                    lider_id: Some(EstacionId(1)),
                    term: 0,
                }
            );
        });
    }

    #[test]
    fn un_ex_lider_que_reinicia_se_descubre_follower() {
        System::new().block_on(async {
            // La 1 arranca como líder por config, pero una vecina viva reporta que
            // el líder vigente es la 3 (term 1, electa mientras la 1 estuvo caída).
            let vecina = vecina_que_conoce_lider(EstacionId(3), 1);
            let estaciones: HashMap<EstacionId, SocketAddr> =
                [(EstacionId(3), vecina)].into_iter().collect();
            let estacion = Estacion::new(
                EstacionId(1),
                (0.0, 0.0),
                vec![],
                pasarela_mock(true),
                (EstacionId(1), "127.0.0.1:9".parse().unwrap()),
                true, // es_lider por config (el bootstrap viejo)
                estaciones,
            )
            .start();
            let comunicador = Comunicador::new(
                "127.0.0.1:0".parse().unwrap(),
                "127.0.0.1:0".parse().unwrap(),
                estacion.clone().recipient(),
            )
            .start();
            estacion
                .send(RegistrarComunicador(comunicador))
                .await
                .unwrap();

            // Tras el discovery deja de creerse líder y reconoce a la 3 (term 1).
            // (Asertamos rápido, antes de que la vigilancia —cada 2s— intervenga.)
            let mut info = None;
            for _ in 0..18 {
                actix::clock::sleep(std::time::Duration::from_millis(100)).await;
                let i = estacion.send(ConsultarLider).await.unwrap();
                if i.lider_id == Some(EstacionId(3)) {
                    info = Some(i);
                    break;
                }
            }
            let info = info.expect("la ex-líder debería adoptar a la 3 como líder");
            assert!(!info.soy_lider, "ya no se cree líder");
            assert_eq!(info.term, 1);
        });
    }

    /// Pasarela de mentira: escucha en un puerto efímero y responde a cada pedido
    /// con un voto fijo (Yes/No) y las confirmaciones de commit/abort.
    fn pasarela_mock(vota_si: bool) -> SocketAddr {
        pasarela_mock_en("127.0.0.1:0".parse().unwrap(), vota_si)
    }

    /// Igual que `pasarela_mock` pero en una dirección fija: permite arrancar
    /// el test con la pasarela "caída" y levantarla a mitad de camino.
    fn pasarela_mock_en(addr: SocketAddr, vota_si: bool) -> SocketAddr {
        let listener = TcpListener::bind(addr).unwrap();
        let addr = listener.local_addr().unwrap();
        std::thread::spawn(move || {
            for conexion in listener.incoming() {
                let Ok(mut stream) = conexion else { continue };
                let mut desen = Desenmarcador::new();
                let mut buf = [0u8; 4096];
                loop {
                    let n = match stream.read(&mut buf) {
                        Ok(0) | Err(_) => break,
                        Ok(n) => n,
                    };
                    desen.alimentar(&buf[..n]);
                    if let Some(payload) = desen.siguiente_payload() {
                        let pedido: MensajeEstacionAPasarela =
                            comun::serializacion::desde_bytes(&payload).unwrap();
                        let resp = responder_mock(pedido, vota_si);
                        let _ = stream.write_all(&enmarcar(&resp).unwrap());
                        break;
                    }
                }
            }
        });
        addr
    }

    /// Pasarela de mentira que vota Sí y, además, vuelca al canal cada pedido que
    /// recibe. Sirve para verificar qué le llegó (p. ej. que tras un rechazo del
    /// slot se haya mandado el `AbortPreauth` de la preauth creada en paralelo).
    fn pasarela_mock_que_registra() -> (
        SocketAddr,
        std::sync::mpsc::Receiver<MensajeEstacionAPasarela>,
    ) {
        let listener = TcpListener::bind("127.0.0.1:0").unwrap();
        let addr = listener.local_addr().unwrap();
        let (tx, rx) = std::sync::mpsc::channel();
        std::thread::spawn(move || {
            for conexion in listener.incoming() {
                let Ok(mut stream) = conexion else { continue };
                let mut desen = Desenmarcador::new();
                let mut buf = [0u8; 4096];
                loop {
                    let n = match stream.read(&mut buf) {
                        Ok(0) | Err(_) => break,
                        Ok(n) => n,
                    };
                    desen.alimentar(&buf[..n]);
                    if let Some(payload) = desen.siguiente_payload() {
                        let pedido: MensajeEstacionAPasarela =
                            comun::serializacion::desde_bytes(&payload).unwrap();
                        let _ = tx.send(pedido.clone());
                        let resp = responder_mock(pedido, true);
                        let _ = stream.write_all(&enmarcar(&resp).unwrap());
                        break;
                    }
                }
            }
        });
        (addr, rx)
    }

    fn responder_mock(pedido: MensajeEstacionAPasarela, vota_si: bool) -> MensajePasarelaAEstacion {
        match pedido {
            MensajeEstacionAPasarela::PreparePreauth { tx_id, .. } => {
                if vota_si {
                    MensajePasarelaAEstacion::Voto {
                        tx_id,
                        resultado: VotoResultado::Yes,
                        preauth_id: Some("P-mock".to_string()),
                    }
                } else {
                    MensajePasarelaAEstacion::Voto {
                        tx_id,
                        resultado: VotoResultado::No {
                            motivo: "tarjeta rechazada".to_string(),
                        },
                        preauth_id: None,
                    }
                }
            }
            MensajeEstacionAPasarela::CommitPreauth { preauth_id, .. } => {
                MensajePasarelaAEstacion::PreauthConfirmada { preauth_id }
            }
            MensajeEstacionAPasarela::AbortPreauth { preauth_id, .. } => {
                MensajePasarelaAEstacion::PreauthAnulada { preauth_id }
            }
            MensajeEstacionAPasarela::ProcesarCobro { preauth_id, .. } => {
                MensajePasarelaAEstacion::CobroConfirmado {
                    preauth_id,
                    monto: 0.0,
                }
            }
            MensajeEstacionAPasarela::CobrarRobo { preauth_id } => {
                MensajePasarelaAEstacion::RoboCobrado {
                    preauth_id,
                    monto: 1000.0,
                }
            }
        }
    }

    #[test]
    fn alquiler_con_ambos_votos_si_confirma_con_preauth_real() {
        System::new().block_on(async {
            let pasarela = pasarela_mock(true);
            let s0 = Slot::con_bici(0, BiciId(42)).start();
            let estacion = arrancar(vec![s0.clone()], pasarela).await;

            let resp = estacion.send(alquilar(0)).await.unwrap();
            match resp {
                MensajeEstacionAUsuario::AlquilerConfirmado { preauth_id, .. } => {
                    assert_eq!(
                        preauth_id.as_deref(),
                        Some("P-mock"),
                        "la preauth la da la pasarela"
                    );
                }
                otro => panic!("esperaba AlquilerConfirmado, fue {otro:?}"),
            }
            assert!(
                !s0.send(ConsultarEstado).await.unwrap().ocupado,
                "el slot quedó vacío"
            );
        });
    }

    #[test]
    fn el_lider_registra_el_alquiler() {
        System::new().block_on(async {
            let pasarela = pasarela_mock(true);
            let s0 = Slot::con_bici(0, BiciId(42)).start();
            let estacion = arrancar(vec![s0], pasarela).await;

            assert_eq!(estacion.send(ConsultarRegistro).await.unwrap(), 0);
            let _ = estacion.send(alquilar(0)).await.unwrap();
            // El alquiler exitoso se reportó al registro (esta estación es el líder).
            assert_eq!(estacion.send(ConsultarRegistro).await.unwrap(), 1);
        });
    }

    #[test]
    fn denuncia_con_alquiler_activo_lo_cierra_como_robado_y_cobra() {
        System::new().block_on(async {
            let pasarela = pasarela_mock(true);
            let s0 = Slot::con_bici(0, BiciId(42)).start();
            let estacion = arrancar(vec![s0], pasarela).await; // líder y origen

            // El usuario alquila: registro en 1.
            let _ = estacion.send(alquilar(0)).await.unwrap();
            assert_eq!(estacion.send(ConsultarRegistro).await.unwrap(), 1);

            // Denuncia el robo: se registra al toque.
            let r = estacion.send(denunciar_robo()).await.unwrap();
            assert!(
                matches!(
                    r,
                    MensajeEstacionAUsuario::RoboRegistrado {
                        bici_id: BiciId(42)
                    }
                ),
                "esperaba RoboRegistrado, fue {r:?}"
            );

            // En background: el alquiler queda Robado (sale del registro activo) y
            // la bici queda fuera de circulación.
            let mut listo = false;
            for _ in 0..40 {
                actix::clock::sleep(std::time::Duration::from_millis(50)).await;
                let activos = estacion.send(ConsultarRegistro).await.unwrap();
                let robadas = estacion.send(ConsultarRobadas).await.unwrap();
                if activos == 0 && robadas == 1 {
                    listo = true;
                    break;
                }
            }
            assert!(
                listo,
                "el alquiler debe cerrarse como Robado y la bici salir de circulación"
            );
        });
    }

    #[test]
    fn denuncia_sin_alquiler_activo_responde_sin_alquiler() {
        System::new().block_on(async {
            let pasarela = pasarela_mock(true);
            let estacion = arrancar(vec![Slot::nuevo(0).start()], pasarela).await; // líder

            // El usuario nunca alquiló: no hay bici que denunciar.
            let r = estacion.send(denunciar_robo()).await.unwrap();
            assert!(
                matches!(r, MensajeEstacionAUsuario::SinAlquilerActivo),
                "esperaba SinAlquilerActivo, fue {r:?}"
            );
            assert_eq!(estacion.send(ConsultarRobadas).await.unwrap(), 0);
        });
    }

    /// Crea la estación líder con su Comunicador cableado y una duración máxima de
    /// alquiler corta (para ejercitar el robo por inactividad sin esperar 24h).
    async fn arrancar_con_duracion(
        slots: Vec<Addr<Slot>>,
        pasarela: SocketAddr,
        duracion: std::time::Duration,
    ) -> Addr<Estacion> {
        let lider = "127.0.0.1:9".parse().unwrap();
        let estacion = Estacion::new(
            EstacionId(1),
            (0.0, 0.0),
            slots,
            pasarela,
            (EstacionId(1), lider),
            true,
            HashMap::new(),
        )
        .con_duracion_maxima_alquiler(duracion)
        .start();
        let comunicador = Comunicador::new(
            "127.0.0.1:0".parse().unwrap(),
            "127.0.0.1:0".parse().unwrap(),
            estacion.clone().recipient(),
        )
        .start();
        estacion
            .send(RegistrarComunicador(comunicador))
            .await
            .unwrap();
        estacion
    }

    fn devolver(bici_id: u32, slot_id: u32) -> SolicitudUsuario {
        SolicitudUsuario(MensajeUsuarioAEstacion::SolicitudDevolucion {
            usuario_id: UsuarioId("alice".to_string()),
            bici_id: BiciId(bici_id),
            rental_id: RentalId("ignorado".to_string()),
            slot_id,
        })
    }

    #[test]
    fn alquiler_no_devuelto_se_marca_robado_por_timeout() {
        System::new().block_on(async {
            let pasarela = pasarela_mock(true);
            let s0 = Slot::con_bici(0, BiciId(42)).start();
            // Líder y origen, con una "duración máxima" cortísima (300ms).
            let estacion =
                arrancar_con_duracion(vec![s0], pasarela, std::time::Duration::from_millis(300))
                    .await;

            let _ = estacion.send(alquilar(0)).await.unwrap();
            assert_eq!(estacion.send(ConsultarRegistro).await.unwrap(), 1);

            // Pasan las "24h" (300ms) sin devolución: el alquiler se marca Robado y
            // la bici sale de circulación (cobro de la reposición incluido).
            let mut listo = false;
            for _ in 0..60 {
                actix::clock::sleep(std::time::Duration::from_millis(50)).await;
                let activos = estacion.send(ConsultarRegistro).await.unwrap();
                let robadas = estacion.send(ConsultarRobadas).await.unwrap();
                if activos == 0 && robadas == 1 {
                    listo = true;
                    break;
                }
            }
            assert!(
                listo,
                "el alquiler no devuelto debe marcarse Robado por inactividad"
            );
        });
    }

    #[test]
    fn alquiler_devuelto_antes_del_timeout_no_se_marca_robado() {
        System::new().block_on(async {
            let pasarela = pasarela_mock(true);
            let s0 = Slot::con_bici(0, BiciId(42)).start();
            // Duración del alquiler corta para no esperar 24h, pero MUY holgada
            // frente al tiempo de la devolución (que cierra en pocos ms): así la
            // devolución gana la carrera al timer con un margen amplio.
            let estacion = arrancar_con_duracion(
                vec![s0.clone()],
                pasarela,
                std::time::Duration::from_millis(500),
            )
            .await;

            // Alquilar (la bici 42 sale del slot 0) y devolver enseguida.
            let _ = estacion.send(alquilar(0)).await.unwrap();
            let resp = estacion.send(devolver(42, 0)).await.unwrap();
            assert!(matches!(
                resp,
                MensajeEstacionAUsuario::DevolucionAceptada { .. }
            ));

            // Confirmar que cerró la DEVOLUCIÓN (no un robo): la bici volvió al
            // slot 0. (`ConsultarRegistro == 0` no alcanza: tanto un cierre por
            // devolución como uno por robo sacan el alquiler de los activos.)
            let mut devuelta = false;
            for _ in 0..40 {
                if estacion.send(ConsultarRegistro).await.unwrap() == 0
                    && s0.send(ConsultarEstado).await.unwrap().ocupado
                {
                    devuelta = true;
                    break;
                }
                actix::clock::sleep(std::time::Duration::from_millis(10)).await;
            }
            assert!(
                devuelta,
                "la devolución debería cerrar el alquiler y dejar la bici en el slot"
            );
            // Todavía nada robado (el timer aún no venció).
            assert_eq!(estacion.send(ConsultarRobadas).await.unwrap(), 0);

            // Dejar vencer el timer de robo por inactividad (500ms): como el
            // alquiler ya está cerrado, `verificar_robo_por_timeout` no hace nada.
            actix::clock::sleep(std::time::Duration::from_millis(700)).await;
            assert_eq!(
                estacion.send(ConsultarRobadas).await.unwrap(),
                0,
                "un alquiler ya devuelto no debe marcarse robado al vencer el timer"
            );
            assert!(
                s0.send(ConsultarEstado).await.unwrap().ocupado,
                "la bici devuelta sigue en el slot (ningún robo la sacó de circulación)"
            );
        });
    }

    #[test]
    fn devolver_una_bici_robada_avisa_a_la_policia_y_no_la_acepta() {
        System::new().block_on(async {
            let pasarela = pasarela_mock(true);
            let s0 = Slot::con_bici(0, BiciId(42)).start();
            let estacion = arrancar(vec![s0.clone()], pasarela).await; // líder y origen

            // Alquila (la bici 42 sale del slot 0) y denuncia el robo.
            let _ = estacion.send(alquilar(0)).await.unwrap();
            let _ = estacion.send(denunciar_robo()).await.unwrap();
            // Esperar a que la bici quede fuera de circulación (robadas == 1).
            let mut robada = false;
            for _ in 0..40 {
                if estacion.send(ConsultarRobadas).await.unwrap() == 1 {
                    robada = true;
                    break;
                }
                actix::clock::sleep(std::time::Duration::from_millis(50)).await;
            }
            assert!(robada, "la denuncia debería sacar la bici de circulación");

            // Intentar devolver la bici robada en el slot 0 (vacío): se rechaza con
            // el aviso de policía y el slot NO se ocupa (no vuelve a circulación).
            let resp = estacion.send(devolver(42, 0)).await.unwrap();
            assert!(
                matches!(
                    resp,
                    MensajeEstacionAUsuario::DevolucionBiciRobada {
                        bici_id: BiciId(42)
                    }
                ),
                "esperaba DevolucionBiciRobada, fue {resp:?}"
            );
            assert!(
                !s0.send(ConsultarEstado).await.unwrap().ocupado,
                "una bici robada no debe re-aceptarse en el slot"
            );
        });
    }

    #[test]
    fn slot_no_con_pasarela_si_anula_la_preauth() {
        System::new().block_on(async {
            // Como el Prepare es concurrente, la pasarela vota (y crea la preauth)
            // aunque el slot vaya a votar No. Slot vacío → vota No → el alquiler se
            // rechaza y la preauth creada en paralelo DEBE anularse (AbortPreauth).
            let (pasarela, pedidos) = pasarela_mock_que_registra();
            let estacion = arrancar(vec![Slot::nuevo(0).start()], pasarela).await;

            let resp = estacion.send(alquilar(0)).await.unwrap();
            assert!(
                matches!(resp, MensajeEstacionAUsuario::AlquilerRechazado { .. }),
                "sin bici en el slot, el alquiler se rechaza: {resp:?}"
            );

            // La pasarela debió recibir el Prepare (preauth creada en paralelo) y,
            // tras el rechazo del slot, el Abort que la libera.
            let mut vio_prepare = false;
            let mut vio_abort = false;
            for _ in 0..10 {
                match pedidos.recv_timeout(std::time::Duration::from_secs(2)) {
                    Ok(MensajeEstacionAPasarela::PreparePreauth { .. }) => vio_prepare = true,
                    Ok(MensajeEstacionAPasarela::AbortPreauth { .. }) => {
                        vio_abort = true;
                        break;
                    }
                    Ok(_) => {}
                    Err(_) => break,
                }
            }
            assert!(
                vio_prepare,
                "la pasarela debió recibir el Prepare (vota en paralelo al slot)"
            );
            assert!(
                vio_abort,
                "al rechazarse por el slot, la preauth creada debe anularse"
            );
        });
    }

    #[test]
    fn devolucion_completa_cobra_y_cierra_el_registro() {
        System::new().block_on(async {
            let pasarela = pasarela_mock(true);
            let s0 = Slot::con_bici(0, BiciId(42)).start();
            let estacion = arrancar(vec![s0], pasarela).await; // líder y origen

            // Alquilar: registro queda en 1, slot 0 vacío.
            let _ = estacion.send(alquilar(0)).await.unwrap();
            assert_eq!(estacion.send(ConsultarRegistro).await.unwrap(), 1);

            // Devolver la bici en el slot 0 (ahora vacío) → DevolucionAceptada + background.
            let resp = estacion
                .send(SolicitudUsuario(
                    MensajeUsuarioAEstacion::SolicitudDevolucion {
                        usuario_id: UsuarioId("alice".to_string()),
                        bici_id: BiciId(42),
                        rental_id: RentalId("ignorado".to_string()),
                        slot_id: 0,
                    },
                ))
                .await
                .unwrap();
            assert!(matches!(
                resp,
                MensajeEstacionAUsuario::DevolucionAceptada { .. }
            ));

            // El cierre (ProcesarDevolucion) corre en background: el cobro pasa
            // por el Comunicador (asincrónico), así que esperamos a que el
            // registro refleje el cierre en vez de asumir el orden.
            let mut activos = usize::MAX;
            for _ in 0..50 {
                activos = estacion.send(ConsultarRegistro).await.unwrap();
                if activos == 0 {
                    break;
                }
                actix::clock::sleep(std::time::Duration::from_millis(100)).await;
            }
            assert_eq!(activos, 0, "la devolución debería cerrar el alquiler");
        });
    }

    #[test]
    fn la_devolucion_no_cierra_sin_cobrar_y_reintenta_al_volver_la_pasarela() {
        System::new().block_on(async {
            // Pasarela muerta (nadie escucha todavía).
            let pasarela: SocketAddr = "127.0.0.1:18930".parse().unwrap();
            let estacion = arrancar(vec![], pasarela).await; // líder

            // Registramos un alquiler abierto (como si lo hubiera reportado el origen).
            estacion
                .send(paquete_tcp(&alquiler_abierto("E1", "R1", 7), None))
                .await
                .unwrap();
            assert_eq!(estacion.send(ConsultarRegistro).await.unwrap(), 1);

            // Llega la bici en devolución, pero la pasarela está caída: el primer
            // intento de cobro falla. El alquiler NO debe cerrarse (se reintenta).
            estacion
                .send(ProcesarDevolucion {
                    bici_id: BiciId(7),
                    t1: Timestamp(120_000),
                    ya_reprocesada: false,
                    intento: 0,
                })
                .await
                .unwrap();
            assert_eq!(
                estacion.send(ConsultarRegistro).await.unwrap(),
                1,
                "sin cobrar (pasarela caída), la devolución no debe cerrar el alquiler"
            );

            // Vuelve la pasarela: el reintento cobra y recién entonces cierra.
            let _viva = pasarela_mock_en(pasarela, true);
            let mut activos = usize::MAX;
            for _ in 0..50 {
                activos = estacion.send(ConsultarRegistro).await.unwrap();
                if activos == 0 {
                    break;
                }
                actix::clock::sleep(std::time::Duration::from_millis(100)).await;
            }
            assert_eq!(
                activos, 0,
                "tras volver la pasarela, el reintento cobra y cierra el alquiler"
            );
        });
    }

    #[test]
    fn el_lider_responde_datos_para_cobro_y_cierra_con_devolucion_procesada() {
        System::new().block_on(async {
            let pasarela = pasarela_mock(true);
            let s0 = Slot::con_bici(0, BiciId(42)).start();
            let estacion = arrancar(vec![s0], pasarela).await; // es líder
            let _ = estacion.send(alquilar(0)).await.unwrap(); // registra la bici 42
            assert_eq!(estacion.send(ConsultarRegistro).await.unwrap(), 1);

            // La destino notifica la devolución → el líder responde DatosParaCobro.
            let (responder, rx) = Responder::canal();
            let notif = MensajeEntreEstacionesTCP::NotificarDevolucion {
                event_id: EventId("E1".to_string()),
                bici_id: BiciId(42),
                estacion_destino: EstacionId(2),
                t1: Timestamp(60_000),
            };
            estacion
                .send(paquete_tcp(&notif, Some(responder)))
                .await
                .unwrap();
            let reply: MensajeEntreEstacionesTCP =
                comun::serializacion::desde_bytes(&rx.recv().unwrap()).unwrap();
            let rental = match reply {
                MensajeEntreEstacionesTCP::DatosParaCobro {
                    rental_id,
                    preauth_id,
                    ..
                } => {
                    assert_eq!(preauth_id, "P-mock");
                    rental_id
                }
                otro => panic!("esperaba DatosParaCobro, fue {otro:?}"),
            };

            // La destino confirma el cobro → el líder cierra el alquiler.
            let procesada = MensajeEntreEstacionesTCP::DevolucionProcesada {
                event_id: EventId("E1".to_string()),
                rental_id: rental,
                monto_cobrado: 70.0,
                tiempo_uso_minutos: 1,
            };
            estacion.send(paquete_tcp(&procesada, None)).await.unwrap();
            assert_eq!(estacion.send(ConsultarRegistro).await.unwrap(), 0);
        });
    }

    #[test]
    fn notificar_devolucion_de_bici_desconocida_responde_no_registrado() {
        System::new().block_on(async {
            let pasarela = pasarela_mock(true);
            let estacion = arrancar(vec![], pasarela).await; // líder sin alquileres
            let (responder, rx) = Responder::canal();
            let notif = MensajeEntreEstacionesTCP::NotificarDevolucion {
                event_id: EventId("E1".to_string()),
                bici_id: BiciId(999),
                estacion_destino: EstacionId(2),
                t1: Timestamp(0),
            };
            estacion
                .send(paquete_tcp(&notif, Some(responder)))
                .await
                .unwrap();
            let reply: MensajeEntreEstacionesTCP =
                comun::serializacion::desde_bytes(&rx.recv().unwrap()).unwrap();
            assert!(matches!(
                reply,
                MensajeEntreEstacionesTCP::NoRegistradoAun { .. }
            ));
        });
    }

    /// Pasarela "colgada": acepta la conexión, lee el pedido y nunca contesta.
    /// Mantiene el stream vivo para que el timeout sea de verdad por espera (no
    /// por conexión cerrada).
    fn pasarela_muda() -> SocketAddr {
        let listener = TcpListener::bind("127.0.0.1:0").unwrap();
        let addr = listener.local_addr().unwrap();
        std::thread::spawn(move || {
            let mut conexiones = Vec::new();
            for conexion in listener.incoming() {
                let Ok(mut stream) = conexion else { continue };
                let mut buf = [0u8; 4096];
                let _ = stream.read(&mut buf);
                conexiones.push(stream); // la dejamos abierta, sin responder
            }
        });
        addr
    }

    #[test]
    fn timeout_de_la_pasarela_resuelve_offline_y_entrega_la_bici() {
        System::new().block_on(async {
            // La pasarela está colgada y no vota dentro del plazo (TIMEOUT_PREPARE
            // = 3s). En vez de rechazar, el alquiler se resuelve OFFLINE (Caso E):
            // la bici se entrega y la preauth queda pendiente de regularización.
            let pasarela = pasarela_muda();
            let s0 = Slot::con_bici(0, BiciId(42)).start();
            let estacion = arrancar(vec![s0.clone()], pasarela).await;

            let resp = estacion.send(alquilar(0)).await.unwrap();
            match resp {
                MensajeEstacionAUsuario::AlquilerConfirmado { preauth_id, .. } => {
                    assert_eq!(preauth_id, None, "offline: la preauth queda pendiente");
                }
                otro => panic!("esperaba AlquilerConfirmado offline, fue {otro:?}"),
            }
            // El slot entregó la bici (el alquiler se resolvió solo con el slot).
            let estado = s0.send(ConsultarEstado).await.unwrap();
            assert_eq!(estado.bici_id, None, "la bici se entregó al usuario");
            // Regla 7.1.1: sin preauth NO se reporta al líder; queda un pago pendiente.
            assert_eq!(estacion.send(ConsultarRegistro).await.unwrap(), 0);
            assert_eq!(estacion.send(ConsultarPagosPendientes).await.unwrap(), 1);
        });
    }

    #[test]
    fn pasarela_vota_no_aborta_y_el_slot_no_pierde_la_bici() {
        System::new().block_on(async {
            let pasarela = pasarela_mock(false);
            let s0 = Slot::con_bici(0, BiciId(42)).start();
            let estacion = arrancar(vec![s0.clone()], pasarela).await;

            let resp = estacion.send(alquilar(0)).await.unwrap();
            assert!(matches!(
                resp,
                MensajeEstacionAUsuario::AlquilerRechazado { .. }
            ));
            // Tras el abort, el slot sigue con su bici (la reserva se liberó).
            let estado = s0.send(ConsultarEstado).await.unwrap();
            assert_eq!(estado.bici_id, Some(BiciId(42)), "la bici sigue en el slot");
        });
    }

    #[test]
    fn devolucion_a_slot_ocupado_se_rechaza() {
        System::new().block_on(async {
            let pasarela = pasarela_mock(true);
            let ocupado = Slot::con_bici(0, BiciId(10)).start();
            let estacion = arrancar(vec![ocupado.clone()], pasarela).await;

            let resp = estacion
                .send(SolicitudUsuario(
                    MensajeUsuarioAEstacion::SolicitudDevolucion {
                        usuario_id: UsuarioId("alice".to_string()),
                        bici_id: BiciId(99),
                        rental_id: RentalId("R1".to_string()),
                        slot_id: 0,
                    },
                ))
                .await
                .unwrap();
            assert!(matches!(
                resp,
                MensajeEstacionAUsuario::DevolucionRechazada { .. }
            ));
            assert_eq!(
                ocupado.send(ConsultarEstado).await.unwrap().bici_id,
                Some(BiciId(10))
            );
        });
    }

    #[test]
    fn el_lider_guarda_el_gossip_en_su_cache() {
        System::new().block_on(async {
            let pasarela = pasarela_mock(true);
            let estacion = arrancar(vec![Slot::nuevo(0).start()], pasarela).await;

            // Arranca con la cache vacía.
            assert_eq!(estacion.send(ConsultarCache).await.unwrap(), 0);

            // Llega gossip de otra estación → el líder lo guarda.
            let gossip = MensajeEntreEstacionesUDP::EstadoEstacion {
                estacion_id: EstacionId(2),
                ubicacion: (-34.61, -58.41),
                bicis_disponibles: 4,
                slots_libres: 6,
                timestamp: Timestamp(123),
            };
            estacion.send(paquete_udp(&gossip)).await.unwrap();
            assert_eq!(estacion.send(ConsultarCache).await.unwrap(), 1);

            // Otro snapshot de la MISMA estación no agrega una entrada (es un upsert).
            estacion.send(paquete_udp(&gossip)).await.unwrap();
            assert_eq!(estacion.send(ConsultarCache).await.unwrap(), 1);
        });
    }

    /// Gossip de una estación con `bicis` bicis en `ubicacion`.
    fn gossip(id: u32, ubicacion: (f64, f64), bicis: u32) -> PaqueteRecibido {
        paquete_udp(&MensajeEntreEstacionesUDP::EstadoEstacion {
            estacion_id: EstacionId(id),
            ubicacion,
            bicis_disponibles: bicis,
            slots_libres: 10 - bicis,
            timestamp: Timestamp(0),
        })
    }

    /// Manda una consulta a la estación (vía `PaqueteRecibido` con responder) y
    /// devuelve la respuesta tipada.
    async fn consultar(
        estacion: &Addr<Estacion>,
        consulta: MensajeUsuarioAEstacionConsulta,
    ) -> MensajeEstacionAUsuarioConsulta {
        let (resp, rx) = Responder::canal();
        let datos = comun::serializacion::a_bytes(&MensajeUsuario::Consulta(consulta)).unwrap();
        estacion
            .send(PaqueteRecibido {
                transporte: Transporte::Tcp,
                datos,
                responder: Some(resp),
            })
            .await
            .unwrap();
        comun::serializacion::desde_bytes(&rx.recv().unwrap()).unwrap()
    }

    #[test]
    fn discovery_devuelve_al_lider() {
        System::new().block_on(async {
            let pasarela = pasarela_mock(true);
            let estacion = arrancar(vec![Slot::nuevo(0).start()], pasarela).await;

            let resp = consultar(&estacion, MensajeUsuarioAEstacionConsulta::PreguntarLider).await;
            match resp {
                MensajeEstacionAUsuarioConsulta::RespuestaLider { lider_id, .. } => {
                    assert_eq!(lider_id, EstacionId(1));
                }
                otra => panic!("esperaba RespuestaLider, fue {otra:?}"),
            }
        });
    }

    #[test]
    fn consulta_de_disponibilidad_filtra_por_proximidad_y_por_bicis() {
        System::new().block_on(async {
            let pasarela = pasarela_mock(true);
            let estacion = arrancar(vec![Slot::nuevo(0).start()], pasarela).await;

            // Cargamos la cache con tres estaciones cerca de Buenos Aires:
            //  - 2: cerca, con bicis  → debe aparecer
            //  - 3: cerca, sin bicis  → se filtra (no sirve para alquilar)
            //  - 4: lejos (La Plata), con bicis → se filtra por distancia
            estacion.send(gossip(2, (-34.60, -58.40), 5)).await.unwrap();
            estacion.send(gossip(3, (-34.61, -58.41), 0)).await.unwrap();
            estacion.send(gossip(4, (-34.92, -57.95), 5)).await.unwrap();

            let resp = consultar(
                &estacion,
                MensajeUsuarioAEstacionConsulta::ConsultaDisponibilidad {
                    usuario_id: UsuarioId("alice".to_string()),
                    ubicacion: (-34.60, -58.40),
                    radio_max_km: 5.0,
                },
            )
            .await;
            match resp {
                MensajeEstacionAUsuarioConsulta::RespuestaDisponibilidad { estaciones } => {
                    let ids: Vec<u32> = estaciones.iter().map(|e| e.estacion_id.0).collect();
                    assert_eq!(ids, vec![2], "solo la 2 está cerca y con bicis");
                }
                otra => panic!("esperaba RespuestaDisponibilidad, fue {otra:?}"),
            }
        });
    }

    #[test]
    fn pasarela_inalcanzable_resuelve_el_alquiler_offline_sin_reportar() {
        System::new().block_on(async {
            // Pasarela muerta en un puerto fijo (nadie escucha).
            let pasarela: SocketAddr = "127.0.0.1:18910".parse().unwrap();
            let s0 = Slot::con_bici(0, BiciId(41)).start();
            let s1 = Slot::con_bici(1, BiciId(42)).start();
            let estacion = arrancar(vec![s0, s1], pasarela).await; // líder

            // Primer intento: la pasarela no responde (conexión rechazada). En vez
            // de rechazar, el alquiler se resuelve OFFLINE (Caso E) en este intento.
            let r1 = estacion.send(alquilar(0)).await.unwrap();
            assert!(
                matches!(
                    r1,
                    MensajeEstacionAUsuario::AlquilerConfirmado {
                        preauth_id: None,
                        ..
                    }
                ),
                "esperaba AlquilerConfirmado offline, fue {r1:?}"
            );
            actix::clock::sleep(std::time::Duration::from_millis(300)).await;

            // Segundo intento: la pasarela ya quedó marcada inalcanzable, así que
            // el precheck manda directo a offline (sin esperar el timeout).
            let r2 = estacion.send(alquilar(1)).await.unwrap();
            assert!(
                matches!(
                    r2,
                    MensajeEstacionAUsuario::AlquilerConfirmado {
                        preauth_id: None,
                        ..
                    }
                ),
                "esperaba AlquilerConfirmado offline, fue {r2:?}"
            );
            // Regla 7.1.1: sin preauth NO se reporta (ni al propio registro, aunque
            // esta estación sea el líder); quedan los dos pagos pendientes.
            assert_eq!(estacion.send(ConsultarRegistro).await.unwrap(), 0);
            assert_eq!(estacion.send(ConsultarPagosPendientes).await.unwrap(), 2);
        });
    }

    /// Estación líder con la pasarela en `pasarela` y los intervalos de
    /// reintento/regularización acortados, para los tests del Caso E.
    async fn arrancar_con_intervalos_cortos(pasarela: SocketAddr) -> Addr<Estacion> {
        let lider = "127.0.0.1:9".parse().unwrap();
        let s0 = Slot::con_bici(0, BiciId(41)).start();
        let s1 = Slot::con_bici(1, BiciId(42)).start();
        let estacion = Estacion::new(
            EstacionId(1),
            (0.0, 0.0),
            vec![s0, s1],
            pasarela,
            (EstacionId(1), lider),
            true,
            HashMap::new(),
        )
        .con_intervalo_de_reintento(std::time::Duration::from_millis(300))
        .con_intervalo_de_regularizacion(std::time::Duration::from_millis(300))
        .start();
        let comunicador = Comunicador::new(
            "127.0.0.1:0".parse().unwrap(),
            "127.0.0.1:0".parse().unwrap(),
            estacion.clone().recipient(),
        )
        .start();
        estacion
            .send(RegistrarComunicador(comunicador))
            .await
            .unwrap();
        estacion
    }

    /// Deja a la estación en modo offline con un alquiler pendiente de pago: la
    /// pasarela no responde, así que el alquiler se resuelve por el Caso E
    /// (offline), sin reportar al líder, y queda un pago pendiente de regularizar.
    async fn alquilar_offline(estacion: &Addr<Estacion>) {
        let r = estacion.send(alquilar(0)).await.unwrap();
        assert!(
            matches!(
                r,
                MensajeEstacionAUsuario::AlquilerConfirmado {
                    preauth_id: None,
                    ..
                }
            ),
            "esperaba AlquilerConfirmado offline, fue {r:?}"
        );
        assert_eq!(estacion.send(ConsultarPagosPendientes).await.unwrap(), 1);
        assert_eq!(
            estacion.send(ConsultarRegistro).await.unwrap(),
            0,
            "offline: nada en el registro"
        );
    }

    #[test]
    fn alquiler_offline_se_regulariza_al_volver_la_pasarela() {
        System::new().block_on(async {
            let pasarela: SocketAddr = "127.0.0.1:18920".parse().unwrap();
            let estacion = arrancar_con_intervalos_cortos(pasarela).await;
            alquilar_offline(&estacion).await;

            // Vuelve la pasarela: el re-sondeo la desmarca y la regularización
            // consigue la preauth, reporta al líder y commitea.
            let _viva = pasarela_mock_en(pasarela, true);
            let mut listo = false;
            for _ in 0..60 {
                actix::clock::sleep(std::time::Duration::from_millis(300)).await;
                let pagos = estacion.send(ConsultarPagosPendientes).await.unwrap();
                let registro = estacion.send(ConsultarRegistro).await.unwrap();
                let commits = estacion.send(ConsultarCommitsPendientes).await.unwrap();
                if pagos == 0 && registro == 1 && commits == 0 {
                    listo = true;
                    break;
                }
            }
            assert!(
                listo,
                "la regularización debería poner la preauth, reportar y commitear"
            );
            assert_eq!(estacion.send(ConsultarCobrosFallidos).await.unwrap(), 0);
        });
    }

    #[test]
    fn regularizacion_rechazada_queda_como_cobro_fallido() {
        System::new().block_on(async {
            let pasarela: SocketAddr = "127.0.0.1:18921".parse().unwrap();
            let estacion = arrancar_con_intervalos_cortos(pasarela).await;
            alquilar_offline(&estacion).await;

            // La pasarela vuelve... pero rechaza la tarjeta: CobroFallido.
            let _viva = pasarela_mock_en(pasarela, false);
            let mut fallidos = 0;
            for _ in 0..60 {
                actix::clock::sleep(std::time::Duration::from_millis(300)).await;
                fallidos = estacion.send(ConsultarCobrosFallidos).await.unwrap();
                if fallidos == 1 {
                    break;
                }
            }
            assert_eq!(fallidos, 1, "la regularización rechazada queda auditada");
            assert_eq!(estacion.send(ConsultarPagosPendientes).await.unwrap(), 0);
            assert_eq!(
                estacion.send(ConsultarRegistro).await.unwrap(),
                0,
                "sin preauth no hay reporte al líder"
            );
        });
    }

    #[test]
    fn la_regularizacion_dispara_el_commit_en_el_acto() {
        System::new().block_on(async {
            // Pasarela muerta al principio: el alquiler entra offline (Caso E).
            let pasarela: SocketAddr = "127.0.0.1:18922".parse().unwrap();
            // Reintento de commits "apagado" (1h): si el Commit igual sale, es
            // porque la regularización lo disparó ella misma (camino event-driven
            // de producción), y no un `run_interval`. Sin el fix, la preauth queda
            // sólo Preparada y la pasarela la anularía por timeout.
            let lider = "127.0.0.1:9".parse().unwrap();
            let s0 = Slot::con_bici(0, BiciId(41)).start();
            let s1 = Slot::con_bici(1, BiciId(42)).start();
            let estacion = Estacion::new(
                EstacionId(1),
                (0.0, 0.0),
                vec![s0, s1],
                pasarela,
                (EstacionId(1), lider),
                true,
                HashMap::new(),
            )
            .con_intervalo_de_reintento(std::time::Duration::from_secs(3600))
            .con_intervalo_de_regularizacion(std::time::Duration::from_millis(300))
            .start();
            let comunicador = Comunicador::new(
                "127.0.0.1:0".parse().unwrap(),
                "127.0.0.1:0".parse().unwrap(),
                estacion.clone().recipient(),
            )
            .start();
            estacion
                .send(RegistrarComunicador(comunicador))
                .await
                .unwrap();

            alquilar_offline(&estacion).await;

            // Vuelve la pasarela: la regularización consigue la preauth y debe
            // commitearla en el mismo acto (el reintento periódico está en 1h).
            let _viva = pasarela_mock_en(pasarela, true);
            let mut listo = false;
            for _ in 0..60 {
                actix::clock::sleep(std::time::Duration::from_millis(300)).await;
                let pagos = estacion.send(ConsultarPagosPendientes).await.unwrap();
                let commits = estacion.send(ConsultarCommitsPendientes).await.unwrap();
                if pagos == 0 && commits == 0 {
                    listo = true;
                    break;
                }
            }
            assert!(
                listo,
                "la regularización debe disparar el Commit en el acto, sin esperar otro ciclo"
            );
            assert_eq!(estacion.send(ConsultarRegistro).await.unwrap(), 1);
        });
    }

    fn alquiler_abierto(event: &str, rental: &str, bici: u32) -> MensajeEntreEstacionesTCP {
        MensajeEntreEstacionesTCP::AlquilerAbierto {
            event_id: EventId(event.to_string()),
            rental_id: RentalId(rental.to_string()),
            bici_id: BiciId(bici),
            usuario_id: UsuarioId("alice".to_string()),
            estacion_origen: EstacionId(2),
            // Recién iniciado: así el timer de robo por inactividad (24h) no se
            // dispara durante el test (un t0 antiguo lo haría vencer enseguida).
            t0: Timestamp::ahora(),
            preauth_id: "P-1".to_string(),
        }
    }

    #[test]
    fn el_lider_ignora_un_evento_duplicado() {
        System::new().block_on(async {
            let pasarela = pasarela_mock(true);
            let estacion = arrancar(vec![], pasarela).await; // líder

            // Primer reporte: se registra.
            estacion
                .send(paquete_tcp(&alquiler_abierto("E1", "R1", 1), None))
                .await
                .unwrap();
            assert_eq!(estacion.send(ConsultarRegistro).await.unwrap(), 1);

            // El MISMO event_id otra vez (reintento de la cola de diferidos):
            // no se aplica, aunque el contenido difiera.
            estacion
                .send(paquete_tcp(&alquiler_abierto("E1", "R2", 2), None))
                .await
                .unwrap();
            assert_eq!(estacion.send(ConsultarRegistro).await.unwrap(), 1);

            // Un evento nuevo sí entra.
            estacion
                .send(paquete_tcp(&alquiler_abierto("E2", "R2", 2), None))
                .await
                .unwrap();
            assert_eq!(estacion.send(ConsultarRegistro).await.unwrap(), 2);
        });
    }

    #[test]
    fn el_ingreso_tardio_solo_suma_lo_que_el_lider_no_conoce() {
        System::new().block_on(async {
            let pasarela = pasarela_mock(true);
            let estacion = arrancar(vec![Slot::con_bici(0, BiciId(42)).start()], pasarela).await;

            // El líder ya tiene un alquiler (R-1-1) y lo cierra.
            let _ = estacion.send(alquilar(0)).await.unwrap();
            let cierre = MensajeEntreEstacionesTCP::DevolucionProcesada {
                event_id: EventId("E-cierre".to_string()),
                rental_id: RentalId("R-1-1".to_string()),
                monto_cobrado: 70.0,
                tiempo_uso_minutos: 1,
            };
            estacion.send(paquete_tcp(&cierre, None)).await.unwrap();
            assert_eq!(estacion.send(ConsultarRegistro).await.unwrap(), 0);

            // Una estación que se reincorpora tarde manda su versión (vieja,
            // todavía Activa) de R-1-1 + un alquiler que el líder no conocía.
            let alquiler = |rental: &str, bici: u32| Alquiler {
                rental_id: RentalId(rental.to_string()),
                bici_id: BiciId(bici),
                usuario_id: UsuarioId("bob".to_string()),
                estacion_origen: EstacionId(7),
                // Reciente: el timer de robo por inactividad (24h) no se dispara
                // durante el test.
                inicio: Timestamp::ahora(),
                fin: None,
                preauth_id: Some("P-tardio".to_string()),
                estado: EstadoAlquiler::Activo,
            };
            let tardio = MensajeEntreEstacionesTCP::IngresoTardio {
                alquileres: vec![alquiler("R-1-1", 42), alquiler("R-tardio", 77)],
            };
            estacion.send(paquete_tcp(&tardio, None)).await.unwrap();

            // Solo el desconocido entra; el cerrado NO se re-abre.
            assert_eq!(
                estacion.send(ConsultarRegistro).await.unwrap(),
                1,
                "R-tardio entra, R-1-1 sigue cerrado"
            );
        });
    }

    #[test]
    fn los_eventos_al_lider_caido_se_difieren_y_se_aplican_al_asumir() {
        System::new().block_on(async {
            let pasarela = pasarela_mock(true);
            // Follower con el líder (9) muerto desde el arranque.
            let muerto: SocketAddr = "127.0.0.1:19039".parse().unwrap();
            let estaciones: HashMap<EstacionId, SocketAddr> =
                [(EstacionId(9), muerto)].into_iter().collect();
            let s0 = Slot::con_bici(0, BiciId(42)).start();
            let estacion = Estacion::new(
                EstacionId(1),
                (0.0, 0.0),
                vec![s0],
                pasarela,
                (EstacionId(9), muerto),
                false,
                estaciones,
            )
            .start();
            let comunicador = Comunicador::new(
                "127.0.0.1:0".parse().unwrap(),
                "127.0.0.1:0".parse().unwrap(),
                estacion.clone().recipient(),
            )
            .start();
            estacion
                .send(RegistrarComunicador(comunicador))
                .await
                .unwrap();

            // El alquiler sale bien (la pasarela está viva), pero el reporte al
            // líder muerto falla y queda diferido.
            let resp = estacion.send(alquilar(0)).await.unwrap();
            assert!(matches!(
                resp,
                MensajeEstacionAUsuario::AlquilerConfirmado { .. }
            ));
            actix::clock::sleep(std::time::Duration::from_millis(600)).await;
            assert_eq!(
                estacion.send(ConsultarPendientes).await.unwrap(),
                1,
                "el AlquilerAbierto quedó en la cola de diferidos"
            );

            // Llega un Coordinator que me nombra líder: descargo la cola sobre
            // mi propio registro.
            estacion
                .send(paquete_tcp(
                    &MensajeEntreEstacionesTCP::Coordinator {
                        lider: EstacionId(1),
                        term: 1,
                    },
                    None,
                ))
                .await
                .unwrap();
            actix::clock::sleep(std::time::Duration::from_millis(600)).await;
            assert_eq!(estacion.send(ConsultarPendientes).await.unwrap(), 0);
            assert_eq!(estacion.send(ConsultarRegistro).await.unwrap(), 1);
        });
    }

    /// Líder de mentira con guion: al primer `NotificarDevolucion` responde
    /// `NoRegistradoAun` (el reporte del origen "todavía no llegó"), al segundo
    /// entrega los datos de cobro. Todo evento sin respuesta (p.ej.
    /// `DevolucionProcesada`) se vuelca al canal para que el test lo verifique.
    /// También contesta el sondeo de la vigilancia para no disparar elecciones.
    fn lider_mock() -> (
        SocketAddr,
        std::sync::mpsc::Receiver<MensajeEntreEstacionesTCP>,
    ) {
        let listener = TcpListener::bind("127.0.0.1:0").unwrap();
        let addr = listener.local_addr().unwrap();
        let (tx, rx) = std::sync::mpsc::channel();
        std::thread::spawn(move || {
            let mut notificaciones = 0u32;
            for conexion in listener.incoming() {
                let Ok(mut stream) = conexion else { continue };
                let mut desen = Desenmarcador::new();
                let mut buf = [0u8; 4096];
                loop {
                    let n = match stream.read(&mut buf) {
                        Ok(0) | Err(_) => break,
                        Ok(n) => n,
                    };
                    desen.alimentar(&buf[..n]);
                    while let Some(payload) = desen.siguiente_payload() {
                        if let Ok(msg) =
                            comun::serializacion::desde_bytes::<MensajeEntreEstacionesTCP>(&payload)
                        {
                            match msg {
                                MensajeEntreEstacionesTCP::NotificarDevolucion {
                                    event_id, ..
                                } => {
                                    notificaciones += 1;
                                    let resp = if notificaciones == 1 {
                                        MensajeEntreEstacionesTCP::NoRegistradoAun { event_id }
                                    } else {
                                        MensajeEntreEstacionesTCP::DatosParaCobro {
                                            event_id,
                                            rental_id: RentalId("R-lider".to_string()),
                                            preauth_id: "P-mock".to_string(),
                                            t0: Timestamp(0),
                                            estacion_origen: EstacionId(2),
                                        }
                                    };
                                    let _ = stream.write_all(&enmarcar(&resp).unwrap());
                                }
                                // El discovery al arrancar pregunta esto: el mock
                                // responde que el líder es la 9 (no lo vuelca al canal).
                                MensajeEntreEstacionesTCP::QuienEsLider => {
                                    let resp = MensajeEntreEstacionesTCP::LiderActual {
                                        lider_id: None,
                                        term: 0,
                                    };
                                    let _ = stream.write_all(&enmarcar(&resp).unwrap());
                                }
                                // Pre-chequeo de la devolución: ninguna bici está robada.
                                MensajeEntreEstacionesTCP::ConsultarBiciRobada {
                                    event_id, ..
                                } => {
                                    let resp = MensajeEntreEstacionesTCP::RespuestaBiciRobada {
                                        event_id,
                                        robada: false,
                                    };
                                    let _ = stream.write_all(&enmarcar(&resp).unwrap());
                                }
                                otro => {
                                    let _ = tx.send(otro);
                                }
                            }
                        } else if comun::serializacion::desde_bytes::<MensajeUsuario>(&payload)
                            .is_ok()
                        {
                            let resp = MensajeEstacionAUsuarioConsulta::RespuestaLider {
                                lider_id: EstacionId(9),
                                lider_addr: addr,
                                term: 0,
                            };
                            let _ = stream.write_all(&enmarcar(&resp).unwrap());
                        }
                    }
                }
            }
        });
        (addr, rx)
    }

    /// Líder de mentira para robos: responde el alquiler activo del usuario y
    /// vuelca al canal lo que recibe (p. ej. el `RoboProcesado` del follower).
    fn lider_mock_robo() -> (
        SocketAddr,
        std::sync::mpsc::Receiver<MensajeEntreEstacionesTCP>,
    ) {
        let listener = TcpListener::bind("127.0.0.1:0").unwrap();
        let addr = listener.local_addr().unwrap();
        let (tx, rx) = std::sync::mpsc::channel();
        std::thread::spawn(move || {
            for conexion in listener.incoming() {
                let Ok(mut stream) = conexion else { continue };
                let mut desen = Desenmarcador::new();
                let mut buf = [0u8; 4096];
                loop {
                    let n = match stream.read(&mut buf) {
                        Ok(0) | Err(_) => break,
                        Ok(n) => n,
                    };
                    desen.alimentar(&buf[..n]);
                    while let Some(payload) = desen.siguiente_payload() {
                        if let Ok(msg) =
                            comun::serializacion::desde_bytes::<MensajeEntreEstacionesTCP>(&payload)
                        {
                            match msg {
                                MensajeEntreEstacionesTCP::BuscarAlquilerDeUsuario {
                                    event_id,
                                    ..
                                } => {
                                    let resp = MensajeEntreEstacionesTCP::AlquilerDeUsuario {
                                        event_id,
                                        rental_id: RentalId("R-lider".to_string()),
                                        bici_id: BiciId(77),
                                        preauth_id: "P-mock".to_string(),
                                        t0: Timestamp(0),
                                        estacion_origen: EstacionId(2),
                                    };
                                    let _ = stream.write_all(&enmarcar(&resp).unwrap());
                                }
                                MensajeEntreEstacionesTCP::QuienEsLider => {
                                    let resp = MensajeEntreEstacionesTCP::LiderActual {
                                        lider_id: None,
                                        term: 0,
                                    };
                                    let _ = stream.write_all(&enmarcar(&resp).unwrap());
                                }
                                otro => {
                                    let _ = tx.send(otro);
                                }
                            }
                        } else if comun::serializacion::desde_bytes::<MensajeUsuario>(&payload)
                            .is_ok()
                        {
                            // Sondeo de la vigilancia: respondemos para no disparar elección.
                            let resp = MensajeEstacionAUsuarioConsulta::RespuestaLider {
                                lider_id: EstacionId(9),
                                lider_addr: addr,
                                term: 0,
                            };
                            let _ = stream.write_all(&enmarcar(&resp).unwrap());
                        }
                    }
                }
            }
        });
        (addr, rx)
    }

    #[test]
    fn denuncia_en_otra_estacion_consulta_al_lider_y_reporta_el_robo() {
        System::new().block_on(async {
            let pasarela = pasarela_mock(true);
            let (lider_addr, eventos) = lider_mock_robo();
            let estaciones: HashMap<EstacionId, SocketAddr> =
                [(EstacionId(9), lider_addr)].into_iter().collect();
            let estacion = Estacion::new(
                EstacionId(2),
                (0.0, 0.0),
                vec![Slot::nuevo(0).start()],
                pasarela,
                (EstacionId(9), lider_addr),
                false, // follower
                estaciones,
            )
            .start();
            let comunicador = Comunicador::new(
                "127.0.0.1:0".parse().unwrap(),
                "127.0.0.1:0".parse().unwrap(),
                estacion.clone().recipient(),
            )
            .start();
            estacion
                .send(RegistrarComunicador(comunicador))
                .await
                .unwrap();

            // El usuario denuncia en esta estación (follower): consulta al líder,
            // que confirma el alquiler → se registra el robo.
            let r = estacion.send(denunciar_robo()).await.unwrap();
            assert!(
                matches!(
                    r,
                    MensajeEstacionAUsuario::RoboRegistrado {
                        bici_id: BiciId(77)
                    }
                ),
                "esperaba RoboRegistrado, fue {r:?}"
            );

            // El follower le reporta el cierre por robo al líder (RoboProcesado).
            let mut vio_robo = false;
            for _ in 0..40 {
                if let Ok(MensajeEntreEstacionesTCP::RoboProcesado {
                    bici_id: BiciId(77),
                    ..
                }) = eventos.try_recv()
                {
                    vio_robo = true;
                    break;
                }
                actix::clock::sleep(std::time::Duration::from_millis(50)).await;
            }
            assert!(vio_robo, "el follower debe reportarle el robo al líder");
        });
    }

    #[test]
    fn la_devolucion_reintenta_cuando_el_lider_responde_no_registrado_aun() {
        System::new().block_on(async {
            let pasarela = pasarela_mock(true);
            let (lider_addr, eventos) = lider_mock();
            let estaciones: HashMap<EstacionId, SocketAddr> =
                [(EstacionId(9), lider_addr)].into_iter().collect();
            let s0 = Slot::nuevo(0).start(); // vacío: puede aceptar la bici
            let estacion = Estacion::new(
                EstacionId(2),
                (0.0, 0.0),
                vec![s0],
                pasarela,
                (EstacionId(9), lider_addr),
                false,
                estaciones,
            )
            .start();
            let comunicador = Comunicador::new(
                "127.0.0.1:0".parse().unwrap(),
                "127.0.0.1:0".parse().unwrap(),
                estacion.clone().recipient(),
            )
            .start();
            estacion
                .send(RegistrarComunicador(comunicador))
                .await
                .unwrap();

            // El usuario devuelve la bici: aceptada al toque, cierre en background.
            let resp = estacion
                .send(SolicitudUsuario(
                    MensajeUsuarioAEstacion::SolicitudDevolucion {
                        usuario_id: UsuarioId("alice".to_string()),
                        bici_id: BiciId(42),
                        rental_id: RentalId("R-lider".to_string()),
                        slot_id: 0,
                    },
                ))
                .await
                .unwrap();
            assert!(matches!(
                resp,
                MensajeEstacionAUsuario::DevolucionAceptada { .. }
            ));

            // El primer Notificar recibe NoRegistradoAun; el reintento obtiene
            // los datos, cobra y cierra con DevolucionProcesada hacia el líder.
            let mut recibido = None;
            for _ in 0..100 {
                if let Ok(msg) = eventos.try_recv() {
                    recibido = Some(msg);
                    break;
                }
                actix::clock::sleep(std::time::Duration::from_millis(100)).await;
            }
            match recibido {
                Some(MensajeEntreEstacionesTCP::DevolucionProcesada { rental_id, .. }) => {
                    assert_eq!(rental_id, RentalId("R-lider".to_string()));
                }
                otro => panic!("esperaba DevolucionProcesada tras el reintento, fue {otro:?}"),
            }
        });
    }

    #[test]
    fn los_alquileres_propios_sobreviven_un_reinicio() {
        System::new().block_on(async {
            let ruta = std::env::temp_dir().join("tp-bicis-test-estacion-reinicio.json");
            let _ = std::fs::remove_file(&ruta);
            let pasarela = pasarela_mock(true);
            let lider = "127.0.0.1:9".parse().unwrap();

            // "Primera corrida": la estación (líder por config) alquila y persiste.
            let s0 = Slot::con_bici(0, BiciId(42)).start();
            let e1 = Estacion::new(
                EstacionId(1),
                (0.0, 0.0),
                vec![s0],
                pasarela,
                (EstacionId(1), lider),
                true,
                HashMap::new(),
            )
            .con_persistencia(ruta.clone())
            .start();
            let comunicador = Comunicador::new(
                "127.0.0.1:0".parse().unwrap(),
                "127.0.0.1:0".parse().unwrap(),
                e1.clone().recipient(),
            )
            .start();
            e1.send(RegistrarComunicador(comunicador)).await.unwrap();
            let resp = e1.send(alquilar(0)).await.unwrap();
            assert!(matches!(
                resp,
                MensajeEstacionAUsuario::AlquilerConfirmado { .. }
            ));
            assert_eq!(e1.send(ConsultarRegistro).await.unwrap(), 1);

            // "Reinicio": otra instancia con el mismo archivo recupera sus
            // alquileres y, como arranca de líder, repuebla su registro.
            let e2 = Estacion::new(
                EstacionId(1),
                (0.0, 0.0),
                vec![],
                pasarela,
                (EstacionId(1), lider),
                true,
                HashMap::new(),
            )
            .con_persistencia(ruta.clone())
            .start();
            assert_eq!(
                e2.send(ConsultarRegistro).await.unwrap(),
                1,
                "el alquiler activo sobrevive el reinicio"
            );

            let _ = std::fs::remove_file(&ruta);
        });
    }

    /// Pasarela con guion para el Caso C: vota Sí al Prepare, "pierde" el
    /// primer Commit (cierra la conexión sin responder) y confirma el segundo.
    fn pasarela_mock_commit_perdido() -> SocketAddr {
        let listener = TcpListener::bind("127.0.0.1:0").unwrap();
        let addr = listener.local_addr().unwrap();
        std::thread::spawn(move || {
            let mut commits = 0u32;
            for conexion in listener.incoming() {
                let Ok(mut stream) = conexion else { continue };
                let mut desen = Desenmarcador::new();
                let mut buf = [0u8; 4096];
                loop {
                    let n = match stream.read(&mut buf) {
                        Ok(0) | Err(_) => break,
                        Ok(n) => n,
                    };
                    desen.alimentar(&buf[..n]);
                    if let Some(payload) = desen.siguiente_payload() {
                        let pedido: MensajeEstacionAPasarela =
                            comun::serializacion::desde_bytes(&payload).unwrap();
                        if matches!(pedido, MensajeEstacionAPasarela::CommitPreauth { .. }) {
                            commits += 1;
                            if commits == 1 {
                                break; // primer Commit: se "pierde" (sin respuesta)
                            }
                        }
                        let resp = responder_mock(pedido, true);
                        let _ = stream.write_all(&enmarcar(&resp).unwrap());
                        break;
                    }
                }
            }
        });
        addr
    }

    #[test]
    fn un_commit_perdido_se_completa_con_el_reintento() {
        System::new().block_on(async {
            let pasarela = pasarela_mock_commit_perdido();
            let lider = "127.0.0.1:9".parse().unwrap();
            let s0 = Slot::con_bici(0, BiciId(42)).start();
            let estacion = Estacion::new(
                EstacionId(1),
                (0.0, 0.0),
                vec![s0],
                pasarela,
                (EstacionId(1), lider),
                true,
                HashMap::new(),
            )
            .con_intervalo_de_reintento(std::time::Duration::from_millis(300))
            .start();
            let comunicador = Comunicador::new(
                "127.0.0.1:0".parse().unwrap(),
                "127.0.0.1:0".parse().unwrap(),
                estacion.clone().recipient(),
            )
            .start();
            estacion
                .send(RegistrarComunicador(comunicador))
                .await
                .unwrap();

            // El alquiler se confirma igual (la decisión ya era COMMIT), pero
            // la constancia queda pendiente porque la pasarela no respondió.
            let resp = estacion.send(alquilar(0)).await.unwrap();
            assert!(matches!(
                resp,
                MensajeEstacionAUsuario::AlquilerConfirmado { .. }
            ));
            assert_eq!(
                estacion.send(ConsultarCommitsPendientes).await.unwrap(),
                1,
                "el commit sin confirmar queda registrado"
            );

            // El reintento periódico completa el Commit (la pasarela responde
            // al segundo intento) y la constancia se borra.
            let mut pendientes = usize::MAX;
            for _ in 0..30 {
                actix::clock::sleep(std::time::Duration::from_millis(200)).await;
                pendientes = estacion.send(ConsultarCommitsPendientes).await.unwrap();
                if pendientes == 0 {
                    break;
                }
            }
            assert_eq!(pendientes, 0, "el reintento debe completar el commit");
        });
    }

    #[test]
    fn responde_si_tiene_el_alquiler_de_la_bici_buscada() {
        System::new().block_on(async {
            let pasarela = pasarela_mock(true);
            let s0 = Slot::con_bici(0, BiciId(42)).start();
            let estacion = arrancar(vec![s0], pasarela).await;
            let _ = estacion.send(alquilar(0)).await.unwrap(); // propio: bici 42

            // Bici alquilada acá: AlquilerEncontrado con el alquiler completo.
            let (responder, rx) = Responder::canal();
            let buscar = MensajeEntreEstacionesTCP::BuscarAlquilerPropio {
                event_id: EventId("E-b1".to_string()),
                bici_id: BiciId(42),
            };
            estacion
                .send(paquete_tcp(&buscar, Some(responder)))
                .await
                .unwrap();
            let reply: MensajeEntreEstacionesTCP =
                comun::serializacion::desde_bytes(&rx.recv().unwrap()).unwrap();
            match reply {
                MensajeEntreEstacionesTCP::AlquilerEncontrado { alquiler, .. } => {
                    assert_eq!(alquiler.bici_id, BiciId(42));
                    assert_eq!(alquiler.preauth_id.as_deref(), Some("P-mock"));
                }
                otro => panic!("esperaba AlquilerEncontrado, fue {otro:?}"),
            }

            // Bici desconocida: NoLoTengo.
            let (responder, rx) = Responder::canal();
            let buscar = MensajeEntreEstacionesTCP::BuscarAlquilerPropio {
                event_id: EventId("E-b2".to_string()),
                bici_id: BiciId(99),
            };
            estacion
                .send(paquete_tcp(&buscar, Some(responder)))
                .await
                .unwrap();
            let reply: MensajeEntreEstacionesTCP =
                comun::serializacion::desde_bytes(&rx.recv().unwrap()).unwrap();
            assert!(matches!(reply, MensajeEntreEstacionesTCP::NoLoTengo { .. }));
        });
    }

    #[test]
    fn bici_sin_alquiler_en_ningun_lado_se_confirma_huerfana() {
        System::new().block_on(async {
            let pasarela = pasarela_mock(true);
            // La estación ES el líder (autoridad alcanzable) y no hay otras: una
            // bici que no figura en SU registro ni en ninguna otra estación se
            // confirma huérfana. La huérfana requiere una autoridad que NIEGUE el
            // alquiler; una estación aislada no la declara, sino que espera (eso lo
            // cubre `devolucion_en_estacion_aislada_no_declara_huerfana`).
            let yo: SocketAddr = "127.0.0.1:19049".parse().unwrap();
            let estaciones: HashMap<EstacionId, SocketAddr> =
                [(EstacionId(2), yo)].into_iter().collect();
            let s0 = Slot::nuevo(0).start();
            let estacion = Estacion::new(
                EstacionId(2),
                (0.0, 0.0),
                vec![s0],
                pasarela,
                (EstacionId(2), yo),
                true, // soy el líder: la fuente de verdad del registro
                estaciones,
            )
            .start();
            let comunicador = Comunicador::new(
                "127.0.0.1:0".parse().unwrap(),
                "127.0.0.1:0".parse().unwrap(),
                estacion.clone().recipient(),
            )
            .start();
            estacion
                .send(RegistrarComunicador(comunicador))
                .await
                .unwrap();

            // Llega una bici que nadie alquiló.
            let resp = estacion
                .send(SolicitudUsuario(
                    MensajeUsuarioAEstacion::SolicitudDevolucion {
                        usuario_id: UsuarioId("alice".to_string()),
                        bici_id: BiciId(99),
                        rental_id: RentalId("R-fantasma".to_string()),
                        slot_id: 0,
                    },
                ))
                .await
                .unwrap();
            assert!(matches!(
                resp,
                MensajeEstacionAUsuario::DevolucionAceptada { .. }
            ));

            // Soy el líder y no la tengo + búsqueda sin candidatos → huérfana.
            let mut huerfanas = 0;
            for _ in 0..60 {
                actix::clock::sleep(std::time::Duration::from_millis(250)).await;
                huerfanas = estacion.send(ConsultarHuerfanas).await.unwrap();
                if huerfanas == 1 {
                    break;
                }
            }
            assert_eq!(huerfanas, 1, "la bici debería confirmarse huérfana");
        });
    }

    #[test]
    fn devolucion_en_estacion_aislada_no_declara_huerfana() {
        System::new().block_on(async {
            // Estación aislada (offline) que recibe una devolución cuyo alquiler no
            // conoce localmente: NO debe declararla huérfana, porque estando aislada
            // no puede consultar al líder (ni confiar en su propio registro). Tiene
            // que esperar a reconectar y reintentar. Antes de este fix, agotaba los
            // reintentos contra el líder inalcanzable y la confirmaba huérfana.
            let pasarela: SocketAddr = "127.0.0.1:18934".parse().unwrap();
            let lider = "127.0.0.1:9".parse().unwrap();
            let flag = Arc::new(AtomicBool::new(true)); // arranca offline
            let s0 = Slot::nuevo(0).start();
            let estacion = Estacion::new(
                EstacionId(2),
                (0.0, 0.0),
                vec![s0],
                pasarela,
                (EstacionId(9), lider),
                false, // follower
                HashMap::new(),
            )
            .con_flag_desconexion(Arc::clone(&flag))
            .start();
            let comunicador = Comunicador::con_flag(
                "127.0.0.1:0".parse().unwrap(),
                "127.0.0.1:0".parse().unwrap(),
                estacion.clone().recipient(),
                Arc::clone(&flag),
            )
            .start();
            estacion
                .send(RegistrarComunicador(comunicador))
                .await
                .unwrap();

            // Llega una bici al slot 0 (vacío): se acepta localmente aunque aislada.
            let resp = estacion
                .send(SolicitudUsuario(
                    MensajeUsuarioAEstacion::SolicitudDevolucion {
                        usuario_id: UsuarioId("alice".to_string()),
                        bici_id: BiciId(99),
                        rental_id: RentalId("R-fantasma".to_string()),
                        slot_id: 0,
                    },
                ))
                .await
                .unwrap();
            assert!(matches!(
                resp,
                MensajeEstacionAUsuario::DevolucionAceptada { .. }
            ));

            // Esperamos MÁS que el viejo umbral de reintentos (5 * 500ms = 2.5s):
            // si fuese a declararla huérfana, ya habría pasado. Debe seguir en 0.
            for _ in 0..14 {
                actix::clock::sleep(std::time::Duration::from_millis(250)).await;
                assert_eq!(
                    estacion.send(ConsultarHuerfanas).await.unwrap(),
                    0,
                    "una estación aislada no debe declarar huérfana: tiene que esperar a reconectar"
                );
            }
        });
    }

    /// Líder con memoria para el camino feliz de la 8.2.1: arranca sin saber
    /// nada (responde `NoRegistradoAun`), aprende el alquiler cuando alguien le
    /// manda `AlquilerAbierto`, y a partir de ahí sirve `DatosParaCobro`. El
    /// `DevolucionProcesada` final se vuelca al canal.
    fn lider_mock_recuperable() -> (
        SocketAddr,
        std::sync::mpsc::Receiver<MensajeEntreEstacionesTCP>,
    ) {
        use std::sync::{Arc, Mutex};
        /// Lo que el líder de mentira recuerda de un `AlquilerAbierto`.
        type AlquilerGuardado = (BiciId, RentalId, String, Timestamp, EstacionId);
        let listener = TcpListener::bind("127.0.0.1:0").unwrap();
        let addr = listener.local_addr().unwrap();
        let (tx, rx) = std::sync::mpsc::channel();
        let registro: Arc<Mutex<Option<AlquilerGuardado>>> = Arc::new(Mutex::new(None));
        std::thread::spawn(move || {
            for conexion in listener.incoming() {
                let Ok(mut stream) = conexion else { continue };
                let registro = Arc::clone(&registro);
                let tx = tx.clone();
                let mi_addr = addr;
                std::thread::spawn(move || {
                    let mut desen = Desenmarcador::new();
                    let mut buf = [0u8; 4096];
                    loop {
                        let n = match stream.read(&mut buf) {
                            Ok(0) | Err(_) => break,
                            Ok(n) => n,
                        };
                        desen.alimentar(&buf[..n]);
                        while let Some(payload) = desen.siguiente_payload() {
                            if let Ok(msg) = comun::serializacion::desde_bytes::<
                                MensajeEntreEstacionesTCP,
                            >(&payload)
                            {
                                match msg {
                                    MensajeEntreEstacionesTCP::AlquilerAbierto {
                                        bici_id,
                                        rental_id,
                                        preauth_id,
                                        t0,
                                        estacion_origen,
                                        ..
                                    } => {
                                        *registro.lock().unwrap() = Some((
                                            bici_id,
                                            rental_id,
                                            preauth_id,
                                            t0,
                                            estacion_origen,
                                        ));
                                    }
                                    MensajeEntreEstacionesTCP::NotificarDevolucion {
                                        event_id,
                                        bici_id,
                                        ..
                                    } => {
                                        let guardado = registro.lock().unwrap().clone();
                                        let resp = match guardado {
                                            Some((bici, rental, preauth, t0, origen))
                                                if bici == bici_id =>
                                            {
                                                MensajeEntreEstacionesTCP::DatosParaCobro {
                                                    event_id,
                                                    rental_id: rental,
                                                    preauth_id: preauth,
                                                    t0,
                                                    estacion_origen: origen,
                                                }
                                            }
                                            _ => MensajeEntreEstacionesTCP::NoRegistradoAun {
                                                event_id,
                                            },
                                        };
                                        let _ = stream.write_all(&enmarcar(&resp).unwrap());
                                    }
                                    // El discovery al arrancar pregunta esto: el mock
                                    // responde que el líder es la 9 (no al canal).
                                    MensajeEntreEstacionesTCP::QuienEsLider => {
                                        let resp = MensajeEntreEstacionesTCP::LiderActual {
                                            lider_id: None,
                                            term: 0,
                                        };
                                        let _ = stream.write_all(&enmarcar(&resp).unwrap());
                                    }
                                    // Pre-chequeo de la devolución: nada robado.
                                    MensajeEntreEstacionesTCP::ConsultarBiciRobada {
                                        event_id,
                                        ..
                                    } => {
                                        let resp = MensajeEntreEstacionesTCP::RespuestaBiciRobada {
                                            event_id,
                                            robada: false,
                                        };
                                        let _ = stream.write_all(&enmarcar(&resp).unwrap());
                                    }
                                    otro => {
                                        let _ = tx.send(otro);
                                    }
                                }
                            } else if comun::serializacion::desde_bytes::<MensajeUsuario>(&payload)
                                .is_ok()
                            {
                                let resp = MensajeEstacionAUsuarioConsulta::RespuestaLider {
                                    lider_id: EstacionId(9),
                                    lider_addr: mi_addr,
                                    term: 0,
                                };
                                let _ = stream.write_all(&enmarcar(&resp).unwrap());
                            }
                        }
                    }
                });
            }
        });
        (addr, rx)
    }

    /// Camino feliz de la 8.2.1: la bici llega a B, el líder no la conoce, B la
    /// encuentra en A (la estación de origen), re-reporta el alquiler y la
    /// devolución termina cobrando y cerrando con normalidad.
    #[test]
    fn bici_huerfana_se_recupera_de_la_estacion_de_origen() {
        System::new().block_on(async {
            let pasarela = pasarela_mock(true);
            let (lider_addr, eventos) = lider_mock_recuperable();
            let addr_a: SocketAddr = "127.0.0.1:19051".parse().unwrap();
            let muerto: SocketAddr = "127.0.0.1:19059".parse().unwrap();

            // A (origen): su "líder" está muerto, así que su AlquilerAbierto
            // queda en la cola de diferidos y el líder real nunca se entera.
            let estaciones_a: HashMap<EstacionId, SocketAddr> =
                [(EstacionId(9), muerto)].into_iter().collect();
            let s_a = Slot::con_bici(0, BiciId(42)).start();
            let a = Estacion::new(
                EstacionId(1),
                (0.0, 0.0),
                vec![s_a],
                pasarela,
                (EstacionId(9), muerto),
                false,
                estaciones_a,
            )
            .start();
            let com_a = Comunicador::new(addr_a, addr_a, a.clone().recipient()).start();
            a.send(RegistrarComunicador(com_a)).await.unwrap();
            let resp = a.send(alquilar(0)).await.unwrap();
            assert!(matches!(
                resp,
                MensajeEstacionAUsuario::AlquilerConfirmado { .. }
            ));

            // B (destino): su líder es el mock, que no conoce el alquiler.
            // Conoce a A, así que la búsqueda de huérfanas la va a encontrar.
            let estaciones_b: HashMap<EstacionId, SocketAddr> =
                [(EstacionId(1), addr_a), (EstacionId(9), lider_addr)]
                    .into_iter()
                    .collect();
            let s_b = Slot::nuevo(0).start();
            let b = Estacion::new(
                EstacionId(2),
                (0.0, 0.0),
                vec![s_b],
                pasarela,
                (EstacionId(9), lider_addr),
                false,
                estaciones_b,
            )
            .start();
            let com_b = Comunicador::new(
                "127.0.0.1:0".parse().unwrap(),
                "127.0.0.1:0".parse().unwrap(),
                b.clone().recipient(),
            )
            .start();
            b.send(RegistrarComunicador(com_b)).await.unwrap();

            // El usuario devuelve la bici 42 en B.
            let resp = b
                .send(SolicitudUsuario(
                    MensajeUsuarioAEstacion::SolicitudDevolucion {
                        usuario_id: UsuarioId("alice".to_string()),
                        bici_id: BiciId(42),
                        rental_id: RentalId("ignorado".to_string()),
                        slot_id: 0,
                    },
                ))
                .await
                .unwrap();
            assert!(matches!(
                resp,
                MensajeEstacionAUsuario::DevolucionAceptada { .. }
            ));

            // Retries → NoRegistradoAun → búsqueda → A lo tiene → re-reporte →
            // reproceso → cobro → DevolucionProcesada en el líder.
            let mut recibido = None;
            for _ in 0..100 {
                if let Ok(msg) = eventos.try_recv() {
                    if matches!(msg, MensajeEntreEstacionesTCP::DevolucionProcesada { .. }) {
                        recibido = Some(msg);
                        break;
                    }
                }
                actix::clock::sleep(std::time::Duration::from_millis(200)).await;
            }
            assert!(
                recibido.is_some(),
                "la devolución debería completarse tras recuperar la huérfana"
            );
            // Y B no la contó como huérfana confirmada (se recuperó).
            assert_eq!(b.send(ConsultarHuerfanas).await.unwrap(), 0);
        });
    }

    /// Escenario combinado 1: la devolución arranca con el líder CAÍDO y una
    /// elección en el medio. La bici se alquila en la 1 (su reporte queda
    /// diferido), se devuelve en la 2 mientras no hay líder, y el sistema
    /// tiene que converger igual: elección, reconstrucción/flush, reintentos
    /// que le hablan al líder nuevo, cobro y cierre — sin huérfanas falsas.
    #[test]
    fn la_devolucion_sobrevive_a_la_caida_del_lider() {
        System::new().block_on(async {
            let pasarela = pasarela_mock(true);
            let puerto = |id: u32| 19060 + id as u16;
            let direccion = |id: u32| SocketAddr::from(([127, 0, 0, 1], puerto(id)));
            let estaciones: HashMap<EstacionId, SocketAddr> = [1, 2, 3, 9]
                .into_iter()
                .map(|id| (EstacionId(id), direccion(id)))
                .collect();

            let mut actores = Vec::new();
            for id in [1u32, 2, 3] {
                let slots = if id == 1 {
                    vec![Slot::con_bici(0, BiciId(41)).start()]
                } else {
                    vec![Slot::nuevo(0).start()]
                };
                let estacion = Estacion::new(
                    EstacionId(id),
                    (0.0, 0.0),
                    slots,
                    pasarela,
                    (EstacionId(9), direccion(9)), // líder por config: muerto
                    false,
                    estaciones.clone(),
                )
                .start();
                let comunicador =
                    Comunicador::new(direccion(id), direccion(id), estacion.clone().recipient())
                        .start();
                estacion
                    .send(RegistrarComunicador(comunicador))
                    .await
                    .unwrap();
                actores.push(estacion);
            }

            // Alquiler en la 1 (reporte diferido: el líder está muerto)...
            let resp = actores[0].send(alquilar(0)).await.unwrap();
            assert!(matches!(
                resp,
                MensajeEstacionAUsuario::AlquilerConfirmado { .. }
            ));
            // ...y devolución inmediata en la 2, ANTES de que haya elección.
            let resp = actores[1]
                .send(SolicitudUsuario(
                    MensajeUsuarioAEstacion::SolicitudDevolucion {
                        usuario_id: UsuarioId("alice".to_string()),
                        bici_id: BiciId(41),
                        rental_id: RentalId("ignorado".to_string()),
                        slot_id: 0,
                    },
                ))
                .await
                .unwrap();
            assert!(matches!(
                resp,
                MensajeEstacionAUsuario::DevolucionAceptada { .. }
            ));

            // Convergencia: el alquiler termina CERRADO en el origen (le llegó
            // el CierreAlquiler), el líder electo no tiene activos, y la 2
            // nunca confirmó una huérfana falsa.
            let mut convergio = false;
            for _ in 0..60 {
                actix::clock::sleep(std::time::Duration::from_millis(500)).await;
                let propios_activos = actores[0].send(ConsultarPropiosActivos).await.unwrap();
                let lider = actores[2].send(ConsultarLider).await.unwrap();
                if propios_activos == 0 && lider.lider_id == Some(EstacionId(3)) {
                    convergio = true;
                    break;
                }
            }
            assert!(
                convergio,
                "la devolución debería completarse pese a la caída del líder"
            );
            assert_eq!(
                actores[1].send(ConsultarHuerfanas).await.unwrap(),
                0,
                "el alquiler existía: no es una huérfana"
            );
            assert_eq!(
                actores[2].send(ConsultarRegistro).await.unwrap(),
                0,
                "el líder nuevo no debe tener alquileres activos"
            );
        });
    }

    /// Escenario combinado 2: el líder no está caído sino COLGADO (acepta
    /// conexiones pero nunca contesta: el kernel completa el handshake aunque
    /// el proceso no lea). La vigilancia lo detecta por el timeout de lectura
    /// y el anillo lo saltea gracias al ACK (sin ACK, el Election "entregado"
    /// al colgado se perdería y la elección quedaría trabada para siempre).
    #[test]
    fn lider_colgado_dispara_eleccion_y_el_anillo_lo_saltea() {
        System::new().block_on(async {
            let pasarela = pasarela_mock(true);
            let puerto = |id: u32| 19070 + id as u16;
            let direccion = |id: u32| SocketAddr::from(([127, 0, 0, 1], puerto(id)));
            // El "líder" 9: un listener que jamás procesa nada.
            let _colgado = TcpListener::bind(direccion(9)).unwrap();

            let estaciones: HashMap<EstacionId, SocketAddr> = [1, 2, 9]
                .into_iter()
                .map(|id| (EstacionId(id), direccion(id)))
                .collect();
            let mut actores = Vec::new();
            for id in [1u32, 2] {
                let estacion = Estacion::new(
                    EstacionId(id),
                    (0.0, 0.0),
                    vec![Slot::nuevo(0).start()],
                    pasarela,
                    (EstacionId(9), direccion(9)),
                    false,
                    estaciones.clone(),
                )
                .start();
                let comunicador =
                    Comunicador::new(direccion(id), direccion(id), estacion.clone().recipient())
                        .start();
                estacion
                    .send(RegistrarComunicador(comunicador))
                    .await
                    .unwrap();
                actores.push(estacion);
            }

            // Los sondeos vencen por timeout de lectura (5s c/u), la elección
            // arranca y el anillo saltea al colgado: gana la 2 (mayor id vivo).
            let mut convergio = false;
            for _ in 0..60 {
                actix::clock::sleep(std::time::Duration::from_millis(500)).await;
                let info1 = actores[0].send(ConsultarLider).await.unwrap();
                let info2 = actores[1].send(ConsultarLider).await.unwrap();
                if info1.lider_id == Some(EstacionId(2))
                    && info2.lider_id == Some(EstacionId(2))
                    && info2.soy_lider
                {
                    convergio = true;
                    break;
                }
            }
            assert!(
                convergio,
                "ambas estaciones deberían reconocer a la 2 como líder pese al colgado"
            );
        });
    }

    /// Escenario completo de la Etapa 5: el líder (9) está caído desde el
    /// arranque. Las estaciones vivas (1, 2, 3) lo detectan por el sondeo,
    /// corren el Ring salteando al muerto (el sucesor de la 3 es la 9), gana la
    /// 3 (mayor id vivo), y el nuevo líder reconstruye su registro con el
    /// alquiler que la 1 tenía abierto.
    #[test]
    fn caida_del_lider_dispara_eleccion_y_reconstruye_el_registro() {
        System::new().block_on(async {
            let pasarela = pasarela_mock(true);
            // Puertos fijos del rango de tests: las estaciones se tienen que
            // conocer entre sí ANTES de arrancar (igual que con la config real).
            let puerto = |id: u32| 19020 + id as u16;
            let direccion = |id: u32| SocketAddr::from(([127, 0, 0, 1], puerto(id)));
            let estaciones: HashMap<EstacionId, SocketAddr> = [1, 2, 3, 9]
                .into_iter()
                .map(|id| (EstacionId(id), direccion(id)))
                .collect();

            let mut actores = Vec::new();
            for id in [1u32, 2, 3] {
                let slots = vec![Slot::con_bici(0, BiciId(40 + id)).start()];
                let estacion = Estacion::new(
                    EstacionId(id),
                    (0.0, 0.0),
                    slots,
                    pasarela,
                    (EstacionId(9), direccion(9)), // líder por config: muerto
                    false,
                    estaciones.clone(),
                )
                .start();
                let comunicador =
                    Comunicador::new(direccion(id), direccion(id), estacion.clone().recipient())
                        .start();
                estacion
                    .send(RegistrarComunicador(comunicador))
                    .await
                    .unwrap();
                actores.push(estacion);
            }

            // La 1 alquila: le queda un alquiler propio activo (el reporte al
            // líder muerto se pierde; lo recupera la reconstrucción).
            let resp = actores[0].send(alquilar(0)).await.unwrap();
            assert!(matches!(
                resp,
                MensajeEstacionAUsuario::AlquilerConfirmado { .. }
            ));

            // Vigilancia (2s) × umbral (2 fallos) ≈ 4s hasta la elección; después
            // circula el Ring y se reconstruye el registro. Esperamos hasta 15s.
            let mut convergio = false;
            for _ in 0..30 {
                actix::clock::sleep(std::time::Duration::from_millis(500)).await;
                let info = actores[2].send(ConsultarLider).await.unwrap();
                if info.soy_lider
                    && info.lider_id == Some(EstacionId(3))
                    && actores[2].send(ConsultarRegistro).await.unwrap() == 1
                {
                    convergio = true;
                    break;
                }
            }
            assert!(
                convergio,
                "la 3 debería asumir como líder con el registro reconstruido (1 alquiler)"
            );

            // Las demás reconocen a la 3 como líder, con term post-elección.
            for (i, actor) in actores.iter().enumerate().take(2) {
                let info = actor.send(ConsultarLider).await.unwrap();
                assert_eq!(
                    info.lider_id,
                    Some(EstacionId(3)),
                    "la estación {} debería reconocer a la 3",
                    i + 1
                );
                assert!(info.term >= 1, "el term debe haber avanzado");
                assert!(!info.soy_lider);
            }
        });
    }

    // --- Modo offline: la estación aislada sigue atendiendo al usuario local ---

    /// Arranca una estación (líder, con bicis en slots 0 y 1) compartiendo un flag
    /// de corte de red con su Comunicador, ya en estado `desconectado`. Devuelve
    /// la estación y el flag, para poder "reconectar" luego (`store(false)`).
    async fn arrancar_desconectada(pasarela: SocketAddr) -> (Addr<Estacion>, Arc<AtomicBool>) {
        let lider = "127.0.0.1:9".parse().unwrap();
        let flag = Arc::new(AtomicBool::new(true));
        let s0 = Slot::con_bici(0, BiciId(41)).start();
        let s1 = Slot::nuevo(1).start();
        let estacion = Estacion::new(
            EstacionId(1),
            (0.0, 0.0),
            vec![s0, s1],
            pasarela,
            (EstacionId(1), lider),
            true,
            HashMap::new(),
        )
        .con_intervalo_de_regularizacion(std::time::Duration::from_millis(300))
        .con_intervalo_de_reintento(std::time::Duration::from_millis(300))
        .con_flag_desconexion(Arc::clone(&flag))
        .start();
        let comunicador = Comunicador::con_flag(
            "127.0.0.1:0".parse().unwrap(),
            "127.0.0.1:0".parse().unwrap(),
            estacion.clone().recipient(),
            Arc::clone(&flag),
        )
        .start();
        estacion
            .send(RegistrarComunicador(comunicador))
            .await
            .unwrap();
        (estacion, flag)
    }

    #[test]
    fn estacion_desconectada_alquila_localmente() {
        System::new().block_on(async {
            // La pasarela ni siquiera existe: offline no debe intentar contactarla.
            let pasarela: SocketAddr = "127.0.0.1:18930".parse().unwrap();
            let (estacion, _flag) = arrancar_desconectada(pasarela).await;

            let r = estacion.send(alquilar(0)).await.unwrap();
            assert!(
                matches!(
                    r,
                    MensajeEstacionAUsuario::AlquilerConfirmado {
                        preauth_id: None,
                        ..
                    }
                ),
                "desconectada, el alquiler sale offline (Caso E): {r:?}"
            );
            assert_eq!(estacion.send(ConsultarPagosPendientes).await.unwrap(), 1);
            assert_eq!(
                estacion.send(ConsultarRegistro).await.unwrap(),
                0,
                "offline: no se reporta al líder"
            );
        });
    }

    #[test]
    fn estacion_desconectada_devuelve_localmente() {
        System::new().block_on(async {
            let pasarela: SocketAddr = "127.0.0.1:18931".parse().unwrap();
            let (estacion, _flag) = arrancar_desconectada(pasarela).await;

            // El slot 1 está vacío: aceptar una bici devuelta ahí debe andar offline.
            let devolucion = SolicitudUsuario(MensajeUsuarioAEstacion::SolicitudDevolucion {
                usuario_id: UsuarioId("alice".to_string()),
                bici_id: BiciId(77),
                rental_id: RentalId("R-x".to_string()),
                slot_id: 1,
            });
            let r = estacion.send(devolucion).await.unwrap();
            assert!(
                matches!(r, MensajeEstacionAUsuario::DevolucionAceptada { .. }),
                "desconectada, la devolución local se acepta: {r:?}"
            );
        });
    }

    #[test]
    fn estacion_desconectada_descarta_trafico_de_red() {
        System::new().block_on(async {
            let pasarela: SocketAddr = "127.0.0.1:18932".parse().unwrap();
            let (estacion, _flag) = arrancar_desconectada(pasarela).await;

            // Un evento inter-estación (reporte al líder) debe descartarse: la
            // estación está aislada de la red aunque atienda al usuario.
            estacion
                .send(paquete_tcp(&alquiler_abierto("E-1", "R-1", 9), None))
                .await
                .unwrap();
            assert_eq!(
                estacion.send(ConsultarRegistro).await.unwrap(),
                0,
                "offline: el tráfico de red se descarta, no toca el registro"
            );
        });
    }

    #[test]
    fn al_reconectar_se_regulariza_el_pago_offline() {
        System::new().block_on(async {
            let pasarela: SocketAddr = "127.0.0.1:18933".parse().unwrap();
            let (estacion, flag) = arrancar_desconectada(pasarela).await;

            // Alquiler offline por estar desconectada: queda un pago pendiente.
            let r = estacion.send(alquilar(0)).await.unwrap();
            assert!(matches!(
                r,
                MensajeEstacionAUsuario::AlquilerConfirmado {
                    preauth_id: None,
                    ..
                }
            ));
            assert_eq!(estacion.send(ConsultarPagosPendientes).await.unwrap(), 1);

            // "conectar": baja el flag (compartido) y la pasarela está viva.
            let _viva = pasarela_mock_en(pasarela, true);
            flag.store(false, Ordering::Relaxed);

            let mut listo = false;
            for _ in 0..60 {
                actix::clock::sleep(std::time::Duration::from_millis(300)).await;
                let pagos = estacion.send(ConsultarPagosPendientes).await.unwrap();
                let registro = estacion.send(ConsultarRegistro).await.unwrap();
                if pagos == 0 && registro == 1 {
                    listo = true;
                    break;
                }
            }
            assert!(
                listo,
                "al reconectar, el pago offline se regulariza y reporta"
            );
        });
    }

    #[test]
    fn robo_offline_local_se_liquida_al_reconectar_sin_re_abrir() {
        System::new().block_on(async {
            let pasarela: SocketAddr = "127.0.0.1:18934".parse().unwrap();
            let (estacion, flag) = arrancar_desconectada(pasarela).await;

            // Alquiler offline (pago pendiente) y denuncia de robo, todo offline.
            let _ = estacion.send(alquilar(0)).await.unwrap();
            assert_eq!(estacion.send(ConsultarPagosPendientes).await.unwrap(), 1);
            let r = estacion.send(denunciar_robo()).await.unwrap();
            assert!(
                matches!(r, MensajeEstacionAUsuario::RoboRegistrado { .. }),
                "la denuncia offline se acepta: {r:?}"
            );

            // Reconectar: la regularización consigue la preauth, reporta el alquiler
            // y LIQUIDA el robo (no lo deja abierto): registro sin activos, bici
            // robada contabilizada, sin pagos pendientes.
            let _viva = pasarela_mock_en(pasarela, true);
            flag.store(false, Ordering::Relaxed);
            let mut listo = false;
            for _ in 0..60 {
                actix::clock::sleep(std::time::Duration::from_millis(300)).await;
                let pagos = estacion.send(ConsultarPagosPendientes).await.unwrap();
                let activos = estacion.send(ConsultarRegistro).await.unwrap();
                let robadas = estacion.send(ConsultarRobadas).await.unwrap();
                if pagos == 0 && activos == 0 && robadas == 1 {
                    listo = true;
                    break;
                }
            }
            assert!(
                listo,
                "el robo offline debe liquidarse cerrado (no re-abierto) al reconectar"
            );
        });
    }

    #[test]
    fn devolucion_offline_local_se_liquida_al_reconectar_sin_re_abrir() {
        System::new().block_on(async {
            let pasarela: SocketAddr = "127.0.0.1:18935".parse().unwrap();
            let (estacion, flag) = arrancar_desconectada(pasarela).await;

            // Alquiler offline (pago pendiente) y devolución en un slot vacío, todo offline.
            let r = estacion.send(alquilar(0)).await.unwrap();
            let rental_id = match r {
                MensajeEstacionAUsuario::AlquilerConfirmado { rental_id, .. } => rental_id,
                otro => panic!("esperaba AlquilerConfirmado, fue {otro:?}"),
            };
            assert_eq!(estacion.send(ConsultarPagosPendientes).await.unwrap(), 1);
            let dev = SolicitudUsuario(MensajeUsuarioAEstacion::SolicitudDevolucion {
                usuario_id: UsuarioId("alice".to_string()),
                bici_id: BiciId(41),
                rental_id,
                slot_id: 1, // slot vacío
            });
            assert!(matches!(
                estacion.send(dev).await.unwrap(),
                MensajeEstacionAUsuario::DevolucionAceptada { .. }
            ));

            // Reconectar: regulariza, reporta y LIQUIDA la devolución (cobra y
            // cierra), sin re-abrir el alquiler: registro sin activos, sin pagos,
            // sin cobros fallidos.
            let _viva = pasarela_mock_en(pasarela, true);
            flag.store(false, Ordering::Relaxed);
            let mut listo = false;
            for _ in 0..60 {
                actix::clock::sleep(std::time::Duration::from_millis(300)).await;
                let pagos = estacion.send(ConsultarPagosPendientes).await.unwrap();
                let activos = estacion.send(ConsultarRegistro).await.unwrap();
                let fallidos = estacion.send(ConsultarCobrosFallidos).await.unwrap();
                if pagos == 0 && activos == 0 && fallidos == 0 {
                    listo = true;
                    break;
                }
            }
            assert!(
                listo,
                "la devolución offline debe liquidarse cerrada (no re-abierta) al reconectar"
            );
        });
    }
}
