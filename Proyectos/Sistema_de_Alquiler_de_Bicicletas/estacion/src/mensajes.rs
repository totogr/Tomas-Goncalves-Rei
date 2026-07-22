//! Mensajes internos entre los actores de la estación (vía `Addr`, en memoria).
//! No viajan por la red, así que no están en la crate `comun`.
//!
//! Los mensajes de *diagnóstico* (consultas del estado interno usadas solo por
//! los tests) viven al final, gateados con `#[cfg(test)]`: no forman parte del
//! protocolo de producción.

use actix::prelude::*;
use comun::comunicador::Comunicador;
use comun::mensajes::usuario_estacion::{MensajeEstacionAUsuario, MensajeUsuarioAEstacion};
use comun::{BiciId, EstacionId, RentalId, Timestamp, TransaccionId, UsuarioId};

/// Mensaje que la estación destino se manda a sí misma para correr el cierre de
/// la devolución **en background** (consultar al líder, cobrar, cerrar), después
/// de haberle respondido `DevolucionAceptada` al usuario.
#[derive(Message)]
#[rtype(result = "()")]
pub struct ProcesarDevolucion {
    pub bici_id: BiciId,
    pub t1: Timestamp,
    /// `true` si este es el reintento posterior a una recuperación de huérfana:
    /// si vuelve a fallar, la bici se confirma huérfana (no se busca de nuevo).
    pub ya_reprocesada: bool,
    /// Número de intento (arranca en 0). Cada reintento es un mensaje nuevo en
    /// vez de un loop: así toma el líder VIGENTE en ese momento (si hubo una
    /// elección en el medio, el reintento ya le habla al líder nuevo).
    pub intento: u32,
}

/// Denuncia de robo: la estación busca el alquiler activo del usuario (local o
/// consultando al líder) y responde al usuario. Es un mensaje a sí misma desde
/// `procesar_operacion`, para resolverlo dentro del actor (con acceso al registro).
#[derive(Message)]
#[rtype(result = "MensajeEstacionAUsuario")]
pub struct DenunciaRobo {
    pub usuario_id: UsuarioId,
}

/// Cierre de un robo **en background** (después de responderle al usuario):
/// cierra el alquiler como robado (en el líder y el origen), saca la bici de
/// circulación y cobra la reposición. `ya_cerrado` distingue la primera pasada
/// del reintento del cobro (si la pasarela estaba caída) para no re-cerrar.
#[derive(Message)]
#[rtype(result = "()")]
pub struct ProcesarRobo {
    pub rental_id: RentalId,
    pub bici_id: BiciId,
    /// `None` si el alquiler es offline sin regularizar (todavía sin preauth):
    /// la reposición no se puede cobrar hasta que se regularice.
    pub preauth_id: Option<String>,
    pub estacion_origen: EstacionId,
    pub ya_cerrado: bool,
}

/// Un pedido de usuario (alquiler/devolución) que la estación procesa localmente:
/// rutea al slot que corresponde y coordina el 2PC. Envuelve el mensaje de red
/// para poder enviarlo como mensaje de actor. La respuesta es la misma que iría
/// de vuelta al usuario.
#[derive(Message)]
#[rtype(result = "MensajeEstacionAUsuario")]
pub struct SolicitudUsuario(pub MensajeUsuarioAEstacion);

/// Le da a la `Estacion` la `Addr` de su `Comunicador` (se cablea al arrancar,
/// porque el Comunicador se crea después de la Estacion). Así la estación le pide
/// al Comunicador que hable con la pasarela, sin tocar sockets ella misma.
#[derive(Message)]
#[rtype(result = "()")]
pub struct RegistrarComunicador(pub Addr<Comunicador>);

/// Toggle del detalle del gossip UDP (lo dispara el comando `logs` de la consola).
/// Cada vez que llega, invierte si la estación imprime el gossip que envía (y, si
/// es líder, el que recibe). Apagado por defecto, para no ahogar la consola.
#[derive(Message)]
#[rtype(result = "()")]
pub struct MostrarGossip;

/// Le pide a la estación re-ejecutar el discovery del líder. Lo dispara el comando
/// `conectar` de la consola: cuando la estación recupera la red puede que se haya
/// elegido otro líder mientras estaba aislada, así que vuelve a preguntar a las
/// vecinas (`QuienEsLider`) y, si alguna reconoce un líder con `term` mayor, se
/// baja a follower. Es el anti split-brain al reconectar (no solo al reiniciar).
#[derive(Message)]
#[rtype(result = "()")]
pub struct RedescubrirLider;

/// El 2PC decidió COMMIT: se deja constancia (persistida) ANTES de mandarle el
/// `CommitPreauth` a la pasarela, así un crash en el medio se completa al
/// reiniciar (Caso C). Se responde recién después de persistir.
#[derive(Message)]
#[rtype(result = "()")]
pub struct RegistrarCommitPendiente {
    pub tx_id: TransaccionId,
    pub preauth_id: String,
}

/// La pasarela confirmó el Commit: la constancia se borra.
#[derive(Message)]
#[rtype(result = "()")]
pub struct CommitConfirmado {
    pub tx_id: TransaccionId,
}

/// Fase Prepare del 2PC sobre el `Slot`: pide reservar la bici para una
/// transacción. Responde con el voto.
#[derive(Message)]
#[rtype(result = "Voto")]
pub struct PrepareLiberacion {
    pub tx_id: TransaccionId,
}

/// Voto de un participante del 2PC.
#[derive(Debug, Clone, Copy, PartialEq, Eq)]
pub enum Voto {
    Si,
    No,
}

/// Fase Commit: libera la bici reservada por la transacción. Responde con la
/// bici liberada (o `None` si la reserva no coincide).
#[derive(Message)]
#[rtype(result = "Option<BiciId>")]
pub struct CommitLiberacion {
    pub tx_id: TransaccionId,
}

/// Aborta la reserva de la transacción (no toca la bici).
#[derive(Message)]
#[rtype(result = "()")]
pub struct AbortLiberacion {
    pub tx_id: TransaccionId,
}

/// Pide al slot asegurar una bici que llega. Responde `true` si la aseguró
/// (estaba vacío), `false` si ya estaba ocupado.
#[derive(Message)]
#[rtype(result = "bool")]
pub struct AceptarBici {
    pub bici_id: BiciId,
}

/// Consulta el estado actual del slot.
#[derive(Message)]
#[rtype(result = "EstadoSlot")]
pub struct ConsultarEstado;

#[derive(Debug, Clone, Copy, PartialEq, Eq)]
pub struct EstadoSlot {
    pub ocupado: bool,
    pub bici_id: Option<BiciId>,
}

// La re-exportación va ANTES del módulo (que queda como último ítem del archivo)
// para no dejar ítems después de un módulo de test (lint `items_after_test_module`).
#[cfg(test)]
pub(crate) use diagnostico::*;

/// Mensajes de **diagnóstico**: consultas del estado interno del actor que solo
/// usan los tests (para verificar de punta a punta el registro, la cache, las
/// colas, etc.). No son parte del protocolo de producción, por eso se compilan
/// únicamente en `test`.
#[cfg(test)]
mod diagnostico {
    use super::*;

    /// Cuántos alquileres activos tiene el registro del líder (0 si no es líder).
    #[derive(Message)]
    #[rtype(result = "usize")]
    pub struct ConsultarRegistro;

    /// Cuántas estaciones tiene el líder en su cache de estados (0 si no es líder).
    #[derive(Message)]
    #[rtype(result = "usize")]
    pub struct ConsultarCache;

    /// A quién reconoce esta estación como líder, en qué term, y si es ella misma.
    #[derive(Message)]
    #[rtype(result = "InfoLider")]
    pub struct ConsultarLider;

    /// Cuántos eventos al líder esperan en la cola de diferidos.
    #[derive(Message)]
    #[rtype(result = "usize")]
    pub struct ConsultarPendientes;

    /// Cuántos commits decididos siguen sin confirmación de la pasarela.
    #[derive(Message)]
    #[rtype(result = "usize")]
    pub struct ConsultarCommitsPendientes;

    /// Cuántas bicis quedaron confirmadas como huérfanas (llegaron a un slot sin
    /// alquiler asociado en ninguna estación).
    #[derive(Message)]
    #[rtype(result = "usize")]
    pub struct ConsultarHuerfanas;

    /// Cuántos alquileres propios siguen activos (para verificar que un cierre
    /// llegó hasta el origen).
    #[derive(Message)]
    #[rtype(result = "usize")]
    pub struct ConsultarPropiosActivos;

    /// Cuántas bicis quedaron denunciadas como robadas (fuera de circulación).
    #[derive(Message)]
    #[rtype(result = "usize")]
    pub struct ConsultarRobadas;

    /// Cuántos alquileres offline esperan regularización (preauth diferida).
    #[derive(Message)]
    #[rtype(result = "usize")]
    pub struct ConsultarPagosPendientes;

    /// Cuántas regularizaciones fracasaron (la pasarela rechazó la tarjeta).
    #[derive(Message)]
    #[rtype(result = "usize")]
    pub struct ConsultarCobrosFallidos;

    #[derive(Debug, Clone, Copy, PartialEq, Eq)]
    pub struct InfoLider {
        /// `None` si hay una elección en curso o todavía no se conoce al líder.
        pub lider_id: Option<comun::EstacionId>,
        pub term: u64,
        pub soy_lider: bool,
    }
}
