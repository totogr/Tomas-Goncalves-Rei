//! Manejo de tiempo serializable entre procesos, y el helper de timeouts que
//! usa el 2PC.

use std::future::Future;
use std::pin::Pin;
use std::task::{Context, Poll};
use std::time::{Duration, SystemTime, UNIX_EPOCH};

use serde::{Deserialize, Serialize};

/// Instante en milisegundos desde el epoch UNIX.
///
/// Usamos esto en lugar de `std::time::Instant` porque `Instant` es opaco y
/// local a cada proceso: no se puede serializar ni comparar entre máquinas.
/// Como el monto a cobrar depende de `t1 - t0` y esos timestamps viajan por la
/// red (estación origen → líder → pasarela), necesitamos un valor absoluto.
#[derive(Debug, Clone, Copy, PartialEq, Eq, PartialOrd, Ord, Hash, Serialize, Deserialize)]
pub struct Timestamp(pub u64);

impl Timestamp {
    /// Timestamp del momento actual.
    pub fn ahora() -> Self {
        let millis = SystemTime::now()
            .duration_since(UNIX_EPOCH)
            .expect("el reloj del sistema está antes del epoch UNIX")
            .as_millis() as u64;
        Timestamp(millis)
    }

    /// Minutos transcurridos entre `self` y un instante posterior, redondeados
    /// hacia abajo. Si `posterior` es anterior, devuelve 0.
    pub fn minutos_hasta(self, posterior: Timestamp) -> u32 {
        (posterior.0.saturating_sub(self.0) / 60_000) as u32
    }
}

/// Corre `futuro` con un límite de tiempo: `Some(resultado)` si terminó antes,
/// `None` si venció el plazo. Lo usa el coordinador del 2PC para tratar a un
/// participante que no contesta como un voto No implícito (Caso A).
///
/// No hay un `timeout` en `actix::clock`, así que el futuro combinado se arma a
/// mano: en cada poll prueba primero el futuro real y después el timer (sin
/// crates extra y sin `unsafe`: ambos van en `Box::pin`).
pub fn con_timeout<F: Future>(limite: Duration, futuro: F) -> ConTimeout<F> {
    ConTimeout {
        futuro: Box::pin(futuro),
        timer: Box::pin(actix::clock::sleep(limite)),
    }
}

pub struct ConTimeout<F> {
    futuro: Pin<Box<F>>,
    timer: Pin<Box<actix::clock::Sleep>>,
}

impl<F: Future> Future for ConTimeout<F> {
    type Output = Option<F::Output>;

    fn poll(self: Pin<&mut Self>, cx: &mut Context<'_>) -> Poll<Self::Output> {
        // `ConTimeout` es Unpin (sus campos ya están pineados en el heap).
        let this = self.get_mut();
        if let Poll::Ready(valor) = this.futuro.as_mut().poll(cx) {
            return Poll::Ready(Some(valor));
        }
        match this.timer.as_mut().poll(cx) {
            Poll::Ready(()) => Poll::Ready(None),
            Poll::Pending => Poll::Pending,
        }
    }
}

/// Corre dos futuros **concurrentemente** y devuelve sus dos resultados cuando
/// AMBOS terminaron. Lo usa la fase Prepare del 2PC para pedirle el voto al slot
/// y a la pasarela en paralelo (en lugar de uno y después el otro), de modo que
/// la espera sea `max(t_slot, t_pasarela)` y no la suma.
///
/// Mismo enfoque que `con_timeout`: el combinador se arma a mano (en cada poll se
/// avanza el futuro que falte), sin crates async extra y sin `unsafe` (ambos van
/// en `Box::pin`).
pub fn join2<A, B>(a: A, b: B) -> Join2<A, B>
where
    A: Future,
    B: Future,
{
    Join2 {
        a: Box::pin(a),
        b: Box::pin(b),
        salida_a: None,
        salida_b: None,
    }
}

pub struct Join2<A: Future, B: Future> {
    a: Pin<Box<A>>,
    b: Pin<Box<B>>,
    // Guardan el resultado de cada lado en cuanto termina (el otro puede tardar).
    salida_a: Option<A::Output>,
    salida_b: Option<B::Output>,
}

impl<A, B> Future for Join2<A, B>
where
    A: Future,
    B: Future,
    // Los resultados se guardan por valor en el combinador: deben ser `Unpin`
    // (lo son: votos, tuplas y demás tipos chicos). Los futuros en sí pueden no
    // serlo —por eso van en `Box::pin`—.
    A::Output: Unpin,
    B::Output: Unpin,
{
    type Output = (A::Output, B::Output);

    fn poll(self: Pin<&mut Self>, cx: &mut Context<'_>) -> Poll<Self::Output> {
        let this = self.get_mut();
        // Avanzar cada lado que todavía no terminó (no se vuelve a pollear uno
        // ya completado: su `Future` no debe pollearse después de `Ready`).
        if this.salida_a.is_none() {
            if let Poll::Ready(valor) = this.a.as_mut().poll(cx) {
                this.salida_a = Some(valor);
            }
        }
        if this.salida_b.is_none() {
            if let Poll::Ready(valor) = this.b.as_mut().poll(cx) {
                this.salida_b = Some(valor);
            }
        }
        // Listo solo cuando los dos votaron.
        match (this.salida_a.take(), this.salida_b.take()) {
            (Some(a), Some(b)) => Poll::Ready((a, b)),
            (a, b) => {
                // Reponer lo que ya estaba listo y seguir esperando al otro.
                this.salida_a = a;
                this.salida_b = b;
                Poll::Pending
            }
        }
    }
}

#[cfg(test)]
mod tests {
    use super::*;
    use actix::System;
    use std::time::Instant;

    #[test]
    fn join2_devuelve_los_dos_resultados() {
        System::new().block_on(async {
            // Uno termina al instante; el otro después de una espera. El join
            // devuelve los dos resultados, sin importar el orden en que terminan.
            let a = async { 1u32 };
            let b = async {
                actix::clock::sleep(Duration::from_millis(50)).await;
                "dos"
            };
            let (ra, rb) = join2(a, b).await;
            assert_eq!(ra, 1);
            assert_eq!(rb, "dos");
        });
    }

    #[test]
    fn join2_corre_los_dos_en_paralelo() {
        System::new().block_on(async {
            // Dos esperas de 200ms cada una. En paralelo, el total ronda los
            // 200ms; si fuera secuencial, rondaría los 400ms.
            let inicio = Instant::now();
            let a = async {
                actix::clock::sleep(Duration::from_millis(200)).await;
                'a'
            };
            let b = async {
                actix::clock::sleep(Duration::from_millis(200)).await;
                'b'
            };
            let (ra, rb) = join2(a, b).await;
            let transcurrido = inicio.elapsed();
            assert_eq!((ra, rb), ('a', 'b'));
            assert!(
                transcurrido < Duration::from_millis(350),
                "el join debería correr en paralelo (~200ms), tardó {transcurrido:?}"
            );
        });
    }
}
