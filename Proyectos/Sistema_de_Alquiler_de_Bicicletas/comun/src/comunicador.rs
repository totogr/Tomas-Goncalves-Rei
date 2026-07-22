//! Actor de red, compartido por las aplicaciones que hablan por socket
//! (estación y pasarela). Aísla la lógica de red del actor de negocio:
//!
//! - Escucha TCP y UDP en threads dedicados (`std::net`), desenmarca los frames
//!   TCP y reenvía cada payload recibido al actor de negocio vía un `Recipient`.
//! - Envía por TCP (con framing) y por UDP (datagrama suelto) a pedido.
//!
//! Es agnóstico del tipo de mensaje: trabaja con bytes. Quien recibe el
//! `PaqueteRecibido` lo deserializa al tipo que corresponda.

use std::collections::HashMap;
use std::io::{Read, Write};
use std::net::{SocketAddr, TcpListener, TcpStream, UdpSocket};
use std::sync::atomic::{AtomicBool, Ordering};
use std::sync::mpsc::{self, Receiver, RecvTimeoutError, Sender};
use std::sync::Arc;
use std::thread;
use std::time::Duration;

use actix::prelude::*;

use crate::framing::{enmarcar_payload, Desenmarcador};

/// Transporte por el que llegó un paquete.
#[derive(Debug, Clone, Copy, PartialEq, Eq)]
pub enum Transporte {
    Tcp,
    Udp,
}

/// Permite responder por la misma conexión TCP por la que llegó un pedido
/// (patrón request-response). Para UDP y para mensajes sin respuesta es `None`.
pub struct Responder {
    tx: Sender<Vec<u8>>,
}

impl Responder {
    /// Envía la respuesta (bytes ya serializados) de vuelta por la conexión.
    pub fn responder(self, datos: Vec<u8>) {
        let _ = self.tx.send(datos);
    }

    /// Crea un `Responder` suelto junto con el extremo receptor. Útil para tests:
    /// permite simular un pedido con respuesta y leer lo que el actor contesta.
    pub fn canal() -> (Responder, std::sync::mpsc::Receiver<Vec<u8>>) {
        let (tx, rx) = mpsc::channel();
        (Responder { tx }, rx)
    }
}

/// Un payload (bytes ya desenmarcados) recibido de la red. El Comunicador se lo
/// reenvía al actor de negocio, que lo deserializa al tipo que espera. Si vino
/// por TCP, trae un `Responder` para contestar por la misma conexión.
#[derive(Message)]
#[rtype(result = "()")]
pub struct PaqueteRecibido {
    pub transporte: Transporte,
    pub datos: Vec<u8>,
    pub responder: Option<Responder>,
}

/// Pide enviar un payload por TCP (con framing) a un destino.
#[derive(Message)]
#[rtype(result = "()")]
pub struct EnviarTcp {
    pub destino: SocketAddr,
    pub datos: Vec<u8>,
}

/// Pide enviar un payload por UDP (un datagrama, sin framing) a un destino.
#[derive(Message)]
#[rtype(result = "()")]
pub struct EnviarUdp {
    pub destino: SocketAddr,
    pub datos: Vec<u8>,
}

/// Pide un request-response por TCP: conecta a `destino`, manda el payload
/// (enmarcado) y devuelve la respuesta, o `None` si no se pudo. Lo usa la estación
/// para hablarle a la pasarela en el 2PC, sin tocar sockets ella misma.
#[derive(Message)]
#[rtype(result = "Option<Vec<u8>>")]
pub struct ConsultarTcp {
    pub destino: SocketAddr,
    pub datos: Vec<u8>,
}

/// Como `EnviarTcp`, pero responde si la conexión y el envío salieron bien. Lo
/// usa el Ring de elección para cerrar el anillo ante caídas: si el siguiente
/// nodo no acepta la conexión, el que reenvía prueba con el que le sigue.
#[derive(Message)]
#[rtype(result = "bool")]
pub struct EnviarTcpConfirmado {
    pub destino: SocketAddr,
    pub datos: Vec<u8>,
}

/// ¿La última operación TCP hacia `destino` anduvo? Una dirección que nunca se
/// intentó se asume alcanzable. Lo usa la estación para decidir el modo
/// desconectado (Caso E) sin pagar un timeout en el momento del alquiler.
#[derive(Message)]
#[rtype(result = "bool")]
pub struct ConsultarAlcanzable {
    pub destino: SocketAddr,
}

/// Actualiza la marca de alcanzabilidad de una dirección. Lo usan los propios
/// handlers del Comunicador (tras cada operación saliente) y el re-sondeo.
#[derive(Message)]
#[rtype(result = "()")]
pub struct MarcarAlcanzable {
    pub destino: SocketAddr,
    pub alcanzable: bool,
}

/// Simula un corte de red (lo dispara la consola del proceso): mientras está
/// desconectado, lo SALIENTE por este Comunicador se descarta. El tráfico
/// entrante lo filtra cada receptor según el flag compartido (la estación deja
/// pasar al usuario local; la pasarela descarta todo). Al reconectar, el
/// re-sondeo va desmarcando lo que volvió.
#[derive(Message)]
#[rtype(result = "()")]
pub struct SimularConectividad {
    pub conectado: bool,
}

/// Cada cuánto el Comunicador re-sondea (conexión corta) las direcciones que
/// tiene marcadas como inalcanzables, para detectar que volvieron.
const INTERVALO_RESONDEO: Duration = Duration::from_secs(3);

/// Peer estable hacia el que se mantiene una conexión TCP persistente. El líder
/// puede cambiar (re-elección) y la pasarela es fija.
#[derive(Debug, Clone, Copy, PartialEq, Eq, Hash)]
pub enum RolPeer {
    Lider,
    Pasarela,
}

/// Avisa al Comunicador que mantenga una conexión persistente hacia `addr` con
/// el rol dado. Si el rol ya tenía una conexión a otra dirección (p. ej. cambió
/// el líder), la reemplaza.
#[derive(Message)]
#[rtype(result = "()")]
pub struct ConfigurarPeerPersistente {
    pub rol: RolPeer,
    pub addr: SocketAddr,
}

/// El Comunicador avisa que una conexión persistente quedó (re)establecida. La
/// estación lo usa como señal event-driven para drenar sus colas diferidas.
#[derive(Message)]
#[rtype(result = "()")]
pub struct PeerConectado {
    pub rol: RolPeer,
    pub addr: SocketAddr,
}

/// El Comunicador avisa que una conexión persistente se cayó. La estación lo usa
/// para detectar la caída del líder (dispara elección) sin sondeo periódico.
#[derive(Message)]
#[rtype(result = "()")]
pub struct PeerDesconectado {
    pub rol: RolPeer,
    pub addr: SocketAddr,
}

/// Lo que el Comunicador encola al thread de una conexión persistente.
enum MandatoSalida {
    /// Fire-and-forget: escribir el frame y seguir.
    Enviar(Vec<u8>),
    /// Request-response serializado: escribir el frame y devolver la respuesta
    /// (o `None` si falló) por este canal. Como el thread sirve la cola de a uno,
    /// nunca hay dos consultas en vuelo sobre el mismo socket.
    Consultar {
        datos: Vec<u8>,
        responder: Sender<Option<Vec<u8>>>,
    },
}

/// Estado vivo de una conexión persistente hacia un peer estable.
struct ConexionPersistente {
    addr: SocketAddr,
    tx_salida: Sender<MandatoSalida>,
    /// Al ponerse en `true`, el thread de la conexión termina (se usa al
    /// re-apuntar el líder o al bajar el actor).
    cierre: Arc<AtomicBool>,
}

pub struct Comunicador {
    addr_tcp: SocketAddr,
    addr_udp: SocketAddr,
    destino: Recipient<PaqueteRecibido>,
    /// Socket UDP para enviar (clon del que usa el thread de recepción). Se
    /// inicializa en `started`.
    socket_udp: Option<UdpSocket>,
    /// Servicios alcanzables, por dirección: el resultado de la última
    /// operación TCP saliente hacia cada destino.
    alcanzables: HashMap<SocketAddr, bool>,
    /// Corte de red simulado (compartido con los threads de escucha).
    desconectado: Arc<AtomicBool>,
    /// Conexiones TCP persistentes hacia los peers estables (líder, pasarela).
    peers_persistentes: HashMap<RolPeer, ConexionPersistente>,
    /// Adónde avisar los cambios de salud de las conexiones persistentes (la
    /// estación). La pasarela no lo necesita (no abre conexiones salientes).
    salud_conectado: Option<Recipient<PeerConectado>>,
    salud_desconectado: Option<Recipient<PeerDesconectado>>,
}

impl Comunicador {
    pub fn new(
        addr_tcp: SocketAddr,
        addr_udp: SocketAddr,
        destino: Recipient<PaqueteRecibido>,
    ) -> Self {
        Self::con_flag(
            addr_tcp,
            addr_udp,
            destino,
            Arc::new(AtomicBool::new(false)),
        )
    }

    /// Igual que `new`, pero comparte el flag de corte de red con el actor de
    /// negocio (la estación lo lee para decidir el modo offline y para descartar
    /// el tráfico inter-proceso sin afectar al usuario local).
    pub fn con_flag(
        addr_tcp: SocketAddr,
        addr_udp: SocketAddr,
        destino: Recipient<PaqueteRecibido>,
        desconectado: Arc<AtomicBool>,
    ) -> Self {
        Self {
            addr_tcp,
            addr_udp,
            destino,
            socket_udp: None,
            alcanzables: HashMap::new(),
            desconectado,
            peers_persistentes: HashMap::new(),
            salud_conectado: None,
            salud_desconectado: None,
        }
    }

    /// Registra a quién avisar los cambios de salud de las conexiones
    /// persistentes (la estación, que reacciona drenando sus colas / eligiendo
    /// líder). Sin esto, las conexiones persistentes igual funcionan, pero nadie
    /// se entera de las (re)conexiones.
    pub fn con_salud(
        mut self,
        conectado: Recipient<PeerConectado>,
        desconectado: Recipient<PeerDesconectado>,
    ) -> Self {
        self.salud_conectado = Some(conectado);
        self.salud_desconectado = Some(desconectado);
        self
    }

    fn esta_desconectado(&self) -> bool {
        self.desconectado.load(Ordering::Relaxed)
    }

    /// Si hay una conexión persistente hacia `addr`, devuelve un clon de su canal
    /// de salida (para encolarle un mandato).
    fn tx_persistente_para(&self, addr: SocketAddr) -> Option<Sender<MandatoSalida>> {
        self.peers_persistentes
            .values()
            .find(|c| c.addr == addr)
            .map(|c| c.tx_salida.clone())
    }

    /// Re-sondea (conexión corta, en un thread) las direcciones marcadas como
    /// inalcanzables; si alguna volvió, se desmarca. Así el Caso E termina solo
    /// cuando la pasarela reaparece.
    fn resondear(&self, ctx: &mut Context<Self>) {
        // Con el corte simulado activo no hay nada que re-sondear.
        if self.esta_desconectado() {
            return;
        }
        let caidas: Vec<SocketAddr> = self
            .alcanzables
            .iter()
            .filter(|(_, ok)| !**ok)
            .map(|(addr, _)| *addr)
            // Los peers con conexión persistente manejan su propia salud (con el
            // ping/pong del heartbeat): el re-sondeo no debe marcarlos alcanzables
            // por un simple connect, porque un peer `desconectar`-ado igual acepta
            // TCP y volveríamos a la oscilación.
            .filter(|addr| !self.peers_persistentes.values().any(|c| c.addr == *addr))
            .collect();
        if caidas.is_empty() {
            return;
        }
        let yo = ctx.address();
        thread::spawn(move || {
            for destino in caidas {
                if TcpStream::connect_timeout(&destino, Duration::from_millis(500)).is_ok() {
                    yo.do_send(MarcarAlcanzable {
                        destino,
                        alcanzable: true,
                    });
                }
            }
        });
    }
}

impl Actor for Comunicador {
    type Context = Context<Self>;

    fn started(&mut self, ctx: &mut Self::Context) {
        escuchar_tcp(
            self.addr_tcp,
            self.destino.clone(),
            Arc::clone(&self.desconectado),
        );
        self.socket_udp = Some(escuchar_udp(self.addr_udp, self.destino.clone()));
        ctx.run_interval(INTERVALO_RESONDEO, |act, ctx| act.resondear(ctx));
    }
}

impl Handler<EnviarTcp> for Comunicador {
    type Result = ();

    fn handle(&mut self, msg: EnviarTcp, _ctx: &mut Self::Context) {
        if self.esta_desconectado() {
            return; // corte simulado: el envío se pierde
        }
        // El envío es bloqueante; lo hacemos en un thread para no frenar al actor.
        thread::spawn(move || enviar_tcp(msg.destino, &msg.datos));
    }
}

impl Handler<EnviarUdp> for Comunicador {
    type Result = ();

    fn handle(&mut self, msg: EnviarUdp, _ctx: &mut Self::Context) {
        if self.esta_desconectado() {
            return; // corte simulado: el datagrama se pierde
        }
        if let Some(socket) = &self.socket_udp {
            let _ = socket.send_to(&msg.datos, msg.destino);
        }
    }
}

impl Handler<ConsultarTcp> for Comunicador {
    type Result = ResponseFuture<Option<Vec<u8>>>;

    fn handle(&mut self, msg: ConsultarTcp, ctx: &mut Self::Context) -> Self::Result {
        // El round-trip bloqueante se hace en un thread y acá solo se espera el
        // resultado, sin frenar al actor. Importa de verdad: si este handler
        // bloqueara el arbiter, dos procesos que se consultan mutuamente (p.ej.
        // el líder reconstruyendo el registro mientras un follower lo sondea)
        // quedarían esperándose para siempre.
        if self.esta_desconectado() {
            // Corte simulado: la consulta falla y el destino queda marcado.
            self.alcanzables.insert(msg.destino, false);
            return Box::pin(async { None });
        }
        // Si el destino tiene una conexión persistente (líder/pasarela), la
        // consulta va por ahí (serializada con el resto de su tráfico) en vez de
        // abrir una conexión nueva.
        if let Some(tx) = self.tx_persistente_para(msg.destino) {
            let (resp_tx, resp_rx) = mpsc::channel();
            if tx
                .send(MandatoSalida::Consultar {
                    datos: msg.datos,
                    responder: resp_tx,
                })
                .is_err()
            {
                return Box::pin(async { None });
            }
            let yo = ctx.address();
            let destino = msg.destino;
            return Box::pin(async move {
                // Esperar la respuesta del thread de la conexión sin frenar el arbiter.
                let resultado = en_thread(move || resp_rx.recv().ok().flatten())
                    .await
                    .flatten();
                yo.do_send(MarcarAlcanzable {
                    destino,
                    alcanzable: resultado.is_some(),
                });
                resultado
            });
        }
        let yo = ctx.address();
        Box::pin(async move {
            let destino = msg.destino;
            let resultado = en_thread(move || solicitar_tcp(msg.destino, &msg.datos).ok())
                .await
                .flatten();
            yo.do_send(MarcarAlcanzable {
                destino,
                alcanzable: resultado.is_some(),
            });
            resultado
        })
    }
}

impl Handler<EnviarTcpConfirmado> for Comunicador {
    type Result = ResponseFuture<bool>;

    fn handle(&mut self, msg: EnviarTcpConfirmado, ctx: &mut Self::Context) -> Self::Result {
        // Mismo esquema que `ConsultarTcp`: el envío (con sus reintentos de
        // conexión) corre en un thread para no frenar al actor.
        if self.esta_desconectado() {
            self.alcanzables.insert(msg.destino, false);
            return Box::pin(async { false });
        }
        // Si el destino tiene conexión persistente (el líder), el envío va por
        // ahí: se encola y se da por entregado mientras la conexión esté viva. La
        // garantía de entrega real la respalda la cola de diferidos de la estación.
        if let Some(tx) = self.tx_persistente_para(msg.destino) {
            let entregado = tx.send(MandatoSalida::Enviar(msg.datos)).is_ok();
            return Box::pin(async move { entregado });
        }
        let yo = ctx.address();
        Box::pin(async move {
            let destino = msg.destino;
            let entregado = en_thread(move || enviar_tcp_confirmado(msg.destino, &msg.datos))
                .await
                .unwrap_or(false);
            yo.do_send(MarcarAlcanzable {
                destino,
                alcanzable: entregado,
            });
            entregado
        })
    }
}

impl Handler<SimularConectividad> for Comunicador {
    type Result = ();

    fn handle(&mut self, msg: SimularConectividad, _ctx: &mut Self::Context) {
        self.desconectado.store(!msg.conectado, Ordering::Relaxed);
        if msg.conectado {
            println!("[comunicador] red RESTABLECIDA (simulación)");
        } else {
            println!(
                "[comunicador] red CORTADA (simulación): se corta el tráfico de red \
                 (el usuario local sigue siendo atendido)"
            );
        }
    }
}

impl Handler<ConsultarAlcanzable> for Comunicador {
    type Result = bool;

    fn handle(&mut self, msg: ConsultarAlcanzable, _ctx: &mut Self::Context) -> bool {
        *self.alcanzables.get(&msg.destino).unwrap_or(&true)
    }
}

impl Handler<MarcarAlcanzable> for Comunicador {
    type Result = ();

    fn handle(&mut self, msg: MarcarAlcanzable, _ctx: &mut Self::Context) {
        let anterior = self.alcanzables.insert(msg.destino, msg.alcanzable);
        if anterior == Some(!msg.alcanzable) {
            println!(
                "[comunicador] {} pasó a {}",
                msg.destino,
                if msg.alcanzable {
                    "alcanzable"
                } else {
                    "INALCANZABLE"
                }
            );
        }
    }
}

impl Handler<ConfigurarPeerPersistente> for Comunicador {
    type Result = ();

    fn handle(&mut self, msg: ConfigurarPeerPersistente, ctx: &mut Self::Context) {
        // Idempotente: si el rol ya apunta a esa misma dirección, no hago nada.
        if let Some(actual) = self.peers_persistentes.get(&msg.rol) {
            if actual.addr == msg.addr {
                return;
            }
            // Cambió la dirección (p. ej. nuevo líder): cierro el thread viejo.
            actual.cierre.store(true, Ordering::Relaxed);
        }
        let (tx_salida, rx_salida) = mpsc::channel();
        let cierre = Arc::new(AtomicBool::new(false));
        let rol = msg.rol;
        let addr = msg.addr;
        let cierre_thread = Arc::clone(&cierre);
        let desconectado = Arc::clone(&self.desconectado);
        let salud_conectado = self.salud_conectado.clone();
        let salud_desconectado = self.salud_desconectado.clone();
        // El thread refleja la salud de la conexión en el mapa de alcanzables, así
        // `consultar_alcanzable` (que usa el alquiler/regularización) queda al día
        // sin depender del re-sondeo.
        let marcar = ctx.address().recipient::<MarcarAlcanzable>();
        thread::spawn(move || {
            correr_conexion_persistente(ConexionCfg {
                rol,
                addr,
                rx_salida,
                cierre: cierre_thread,
                desconectado,
                salud_conectado,
                salud_desconectado,
                marcar,
            });
        });
        self.peers_persistentes.insert(
            msg.rol,
            ConexionPersistente {
                addr: msg.addr,
                tx_salida,
                cierre,
            },
        );
    }
}

/// Corre `trabajo` (bloqueante) en un thread y devuelve un futuro await-able con
/// su resultado, sin bloquear el thread del actor. El puente es un canal de la
/// std consultado con `try_recv` + sleeps cortos (sin crates async extra).
/// Devuelve `None` si el thread murió sin responder.
fn en_thread<T, F>(trabajo: F) -> impl std::future::Future<Output = Option<T>>
where
    T: Send + 'static,
    F: FnOnce() -> T + Send + 'static,
{
    let (tx, rx) = mpsc::channel();
    thread::spawn(move || {
        let _ = tx.send(trabajo());
    });
    async move {
        loop {
            match rx.try_recv() {
                Ok(resultado) => return Some(resultado),
                Err(mpsc::TryRecvError::Disconnected) => return None,
                Err(mpsc::TryRecvError::Empty) => {
                    actix::clock::sleep(Duration::from_millis(10)).await;
                }
            }
        }
    }
}

/// Levanta el listener TCP en un thread; por cada conexión, otro thread lee y
/// desenmarca, reenviando cada payload al destino. Comparte el flag de corte de
/// red para decidir si contesta los heartbeats (ver `leer_stream_tcp`).
fn escuchar_tcp(
    addr: SocketAddr,
    destino: Recipient<PaqueteRecibido>,
    desconectado: Arc<AtomicBool>,
) {
    let listener =
        TcpListener::bind(addr).unwrap_or_else(|e| panic!("no pude bindear TCP en {addr}: {e}"));
    thread::spawn(move || {
        for conexion in listener.incoming() {
            match conexion {
                Ok(stream) => {
                    let destino = destino.clone();
                    let desconectado = Arc::clone(&desconectado);
                    thread::spawn(move || leer_stream_tcp(stream, destino, desconectado));
                }
                Err(_) => continue,
            }
        }
    });
}

fn leer_stream_tcp(
    mut stream: TcpStream,
    destino: Recipient<PaqueteRecibido>,
    desconectado: Arc<AtomicBool>,
) {
    let mut desenmarcador = Desenmarcador::new();
    let mut buf = [0u8; 4096];
    loop {
        match stream.read(&mut buf) {
            Ok(0) => break, // el otro lado cerró
            Ok(n) => {
                desenmarcador.alimentar(&buf[..n]);
                while let Some(payload) = desenmarcador.siguiente_payload() {
                    // Heartbeat de una conexión persistente: si estoy conectado,
                    // contesto el pong (el peer sabe que sigo vivo y atendiendo).
                    // Si estoy offline, lo ignoro a propósito: el peer no recibe
                    // pong y me da por caído, así un `desconectar` por consola se
                    // detecta igual que una caída real (ctrl-c). No molesto al actor.
                    if payload == PING_HEARTBEAT {
                        if !desconectado.load(Ordering::Relaxed) {
                            if let Ok(frame) = enmarcar_payload(PONG_HEARTBEAT) {
                                let _ = stream.write_all(&frame);
                            }
                        }
                        continue;
                    }
                    // El resto del corte de red lo decide cada receptor (la estación
                    // deja pasar al usuario local; la pasarela descarta todo): el
                    // Comunicador siempre entrega el payload al actor.
                    // Por cada pedido, le damos al actor un canal para responder.
                    let (tx, rx) = mpsc::channel();
                    destino.do_send(PaqueteRecibido {
                        transporte: Transporte::Tcp,
                        datos: payload,
                        responder: Some(Responder { tx }),
                    });
                    // Esperamos la respuesta y la devolvemos por la misma conexión.
                    // Si el actor no responde (mensaje fire-and-forget), suelta el
                    // `Responder` y `recv` corta sin escribir nada.
                    if let Ok(respuesta) = rx.recv() {
                        if let Ok(frame) = enmarcar_payload(&respuesta) {
                            let _ = stream.write_all(&frame);
                        }
                    }
                }
            }
            Err(_) => break,
        }
    }
}

/// Bindea el socket UDP, deja un thread escuchando datagramas y devuelve un clon
/// del socket para poder enviar.
fn escuchar_udp(addr: SocketAddr, destino: Recipient<PaqueteRecibido>) -> UdpSocket {
    let socket =
        UdpSocket::bind(addr).unwrap_or_else(|e| panic!("no pude bindear UDP en {addr}: {e}"));
    let socket_recv = socket.try_clone().expect("clonar socket UDP");
    thread::spawn(move || {
        let mut buf = [0u8; 65_535];
        while let Ok((n, _)) = socket_recv.recv_from(&mut buf) {
            // El corte de red lo decide el receptor, no el Comunicador: el
            // datagrama siempre se entrega (la estación descarta el gossip si
            // está offline).
            destino.do_send(PaqueteRecibido {
                transporte: Transporte::Udp,
                datos: buf[..n].to_vec(),
                responder: None,
            });
        }
    });
    socket
}

/// Conecta y envía un payload enmarcado por TCP. Reintenta unos instantes para
/// tolerar que el destino todavía no haya terminado de levantar su listener.
fn enviar_tcp(destino: SocketAddr, datos: &[u8]) {
    let frame = match enmarcar_payload(datos) {
        Ok(f) => f,
        Err(_) => return,
    };
    for _ in 0..10 {
        if let Ok(mut stream) = TcpStream::connect(destino) {
            let _ = stream.write_all(&frame);
            return;
        }
        thread::sleep(Duration::from_millis(50));
    }
    eprintln!("[comunicador] no pude conectar a {destino} para enviar por TCP");
}

/// Conecta y envía un payload enmarcado, informando si se pudo entregar. Menos
/// reintentos que `enviar_tcp`: acá el que llama quiere un veredicto rápido para
/// probar con otro destino (el par de reintentos tolera un listener que recién
/// está levantando).
fn enviar_tcp_confirmado(destino: SocketAddr, datos: &[u8]) -> bool {
    let Ok(frame) = enmarcar_payload(datos) else {
        return false;
    };
    for intento in 0..3 {
        if intento > 0 {
            thread::sleep(Duration::from_millis(50));
        }
        if let Ok(mut stream) = TcpStream::connect(destino) {
            return stream.write_all(&frame).is_ok();
        }
    }
    false
}

/// Cuánto espera `solicitar_tcp` la respuesta antes de dar la consulta por
/// fallida. Sin esto, un proceso colgado (vivo pero que no responde) dejaría la
/// consulta esperando para siempre; con esto, el que consulta recibe `None` y
/// reacciona (p.ej. la vigilancia del líder lo da por caído).
const TIMEOUT_LECTURA_TCP: Duration = Duration::from_secs(5);

/// Cliente TCP request-response: conecta, manda un payload enmarcado y devuelve
/// la respuesta (también enmarcada) por la misma conexión. Es **bloqueante**
/// (corre en un thread, ver `en_thread`) pero acotado: la lectura vence a los
/// `TIMEOUT_LECTURA_TCP`. Interno del Comunicador: lo usa `ConsultarTcp`.
fn solicitar_tcp(destino: SocketAddr, datos: &[u8]) -> std::io::Result<Vec<u8>> {
    let frame = enmarcar_payload(datos)
        .map_err(|e| std::io::Error::new(std::io::ErrorKind::InvalidData, e.to_string()))?;
    let mut stream = TcpStream::connect(destino)?;
    stream.set_read_timeout(Some(TIMEOUT_LECTURA_TCP))?;
    stream.write_all(&frame)?;

    let mut desenmarcador = Desenmarcador::new();
    let mut buf = [0u8; 4096];
    loop {
        let n = stream.read(&mut buf)?;
        if n == 0 {
            return Err(std::io::Error::new(
                std::io::ErrorKind::UnexpectedEof,
                "la conexión se cerró sin respuesta",
            ));
        }
        desenmarcador.alimentar(&buf[..n]);
        if let Some(payload) = desenmarcador.siguiente_payload() {
            return Ok(payload);
        }
    }
}

/// Lee una respuesta enmarcada de un stream ya abierto (para el request-response
/// sobre una conexión persistente). Acotado por el `set_read_timeout` del stream.
fn leer_respuesta(stream: &mut TcpStream) -> std::io::Result<Vec<u8>> {
    let mut desenmarcador = Desenmarcador::new();
    let mut buf = [0u8; 4096];
    loop {
        let n = stream.read(&mut buf)?;
        if n == 0 {
            return Err(std::io::Error::new(
                std::io::ErrorKind::UnexpectedEof,
                "la conexión persistente se cerró sin respuesta",
            ));
        }
        desenmarcador.alimentar(&buf[..n]);
        if let Some(payload) = desenmarcador.siguiente_payload() {
            return Ok(payload);
        }
    }
}

/// Cada cuánto el thread de una conexión persistente despierta a chequear si lo
/// mandaron cerrar o si el corte de red se activó (sin un mandato que servir).
const ESPERA_MANDATO: Duration = Duration::from_millis(200);
/// Tope del backoff entre reintentos de (re)conexión persistente.
const BACKOFF_MAX: Duration = Duration::from_secs(2);
/// Cuántas esperas sin tráfico antes de mandar un heartbeat por la conexión
/// persistente (≈ `PINGS_SIN_TRAFICO * ESPERA_MANDATO`).
const PINGS_SIN_TRAFICO: u32 = 10;
/// Payload del heartbeat (ping): no deserializa a ningún mensaje de negocio, el
/// receptor lo intercepta y contesta con un `PONG_HEARTBEAT` (salvo que esté
/// offline, en cuyo caso lo ignora a propósito para que el peer lo dé por caído).
const PING_HEARTBEAT: &[u8] = b"\x00hb";
/// Respuesta al ping: el emisor solo necesita recibir *algo* para saber que el
/// peer sigue vivo y atendiendo (no se compara el contenido).
const PONG_HEARTBEAT: &[u8] = b"\x00pong";
/// Cuánto espera el ping su pong antes de dar la conexión por caída. Corto (a
/// diferencia del timeout de las consultas de negocio): un peer vivo contesta el
/// pong al instante, así que si no llega es que se cayó o se desconectó.
const TIMEOUT_PONG: Duration = Duration::from_secs(1);

/// Parámetros del thread de una conexión persistente (agrupados para no pasar
/// una ristra de argumentos sueltos).
struct ConexionCfg {
    rol: RolPeer,
    addr: SocketAddr,
    rx_salida: Receiver<MandatoSalida>,
    cierre: Arc<AtomicBool>,
    desconectado: Arc<AtomicBool>,
    salud_conectado: Option<Recipient<PeerConectado>>,
    salud_desconectado: Option<Recipient<PeerDesconectado>>,
    marcar: Recipient<MarcarAlcanzable>,
}

/// Thread que mantiene una conexión TCP persistente hacia un peer estable. Sirve
/// los mandatos encolados (envíos y consultas) por la misma conexión y, cuando se
/// cae, reconecta con backoff. Avisa cada (re)conexión y cada caída por los
/// `Recipient` de salud. Termina cuando `cierre` se activa.
fn correr_conexion_persistente(cfg: ConexionCfg) {
    let ConexionCfg {
        rol,
        addr,
        rx_salida,
        cierre,
        desconectado,
        salud_conectado,
        salud_desconectado,
        marcar,
    } = cfg;
    let mut backoff = Duration::from_millis(100);
    loop {
        if cierre.load(Ordering::Relaxed) {
            return;
        }
        // En modo offline no se intenta conectar (la estación queda aislada).
        if desconectado.load(Ordering::Relaxed) {
            thread::sleep(ESPERA_MANDATO);
            continue;
        }
        match TcpStream::connect_timeout(&addr, Duration::from_millis(500)) {
            Ok(mut stream) => {
                let _ = stream.set_read_timeout(Some(TIMEOUT_LECTURA_TCP));
                // Probar liveness ANTES de declarar la conexión: un peer que se
                // `desconectar`-ó sigue aceptando TCP pero no contesta el pong. Si
                // anunciáramos `PeerConectado` apenas conecta, contra un peer mudo
                // quedaríamos oscilando conectado/caído (reconecta → no pong →
                // cae → reconecta...). Con el ping previo, solo anunciamos cuando
                // el peer realmente atiende.
                if !ping_pong(&mut stream) {
                    thread::sleep(backoff);
                    backoff = (backoff * 2).min(BACKOFF_MAX);
                    continue;
                }
                backoff = Duration::from_millis(100);
                // Marcar alcanzable ANTES de avisar la (re)conexión, para que la
                // regularización que dispara `PeerConectado` vea la pasarela viva.
                marcar.do_send(MarcarAlcanzable {
                    destino: addr,
                    alcanzable: true,
                });
                if let Some(r) = &salud_conectado {
                    r.do_send(PeerConectado { rol, addr });
                }
                // Servir mandatos hasta que la conexión se rompa o nos cierren.
                let se_rompio = servir_conexion(&mut stream, &rx_salida, &cierre, &desconectado);
                if se_rompio {
                    marcar.do_send(MarcarAlcanzable {
                        destino: addr,
                        alcanzable: false,
                    });
                }
                if let Some(r) = &salud_desconectado {
                    r.do_send(PeerDesconectado { rol, addr });
                }
                if !se_rompio {
                    // Salida por cierre o corte (no por caída de red): no spinear.
                    continue;
                }
            }
            Err(_) => {
                thread::sleep(backoff);
                backoff = (backoff * 2).min(BACKOFF_MAX);
            }
        }
    }
}

/// Sirve la cola de mandatos sobre una conexión abierta. Devuelve `true` si la
/// conexión se rompió (hay que reconectar) o `false` si salimos por cierre/corte.
fn servir_conexion(
    stream: &mut TcpStream,
    rx_salida: &Receiver<MandatoSalida>,
    cierre: &Arc<AtomicBool>,
    desconectado: &Arc<AtomicBool>,
) -> bool {
    let mut sin_trafico = 0u32;
    loop {
        if cierre.load(Ordering::Relaxed) || desconectado.load(Ordering::Relaxed) {
            return false;
        }
        match rx_salida.recv_timeout(ESPERA_MANDATO) {
            Ok(MandatoSalida::Enviar(datos)) => {
                sin_trafico = 0;
                let Ok(frame) = enmarcar_payload(&datos) else {
                    continue;
                };
                if stream.write_all(&frame).is_err() {
                    return true;
                }
            }
            Ok(MandatoSalida::Consultar { datos, responder }) => {
                sin_trafico = 0;
                let frame = match enmarcar_payload(&datos) {
                    Ok(f) => f,
                    Err(_) => {
                        let _ = responder.send(None);
                        continue;
                    }
                };
                if stream.write_all(&frame).is_err() {
                    let _ = responder.send(None);
                    return true;
                }
                match leer_respuesta(stream) {
                    Ok(payload) => {
                        let _ = responder.send(Some(payload));
                    }
                    Err(_) => {
                        let _ = responder.send(None);
                        return true;
                    }
                }
            }
            // Sin mandatos: cada tanto mandamos un heartbeat para detectar que el
            // peer dejó de atender aunque no haya tráfico. Es request-response: si
            // el peer no contesta el pong dentro del timeout, la conexión se da por
            // caída (`PeerDesconectado`). Así detectamos tanto la caída real (ctrl-c,
            // que además rompe el socket) como el `desconectar` por consola (el
            // socket sigue abierto pero el peer no contesta).
            Err(RecvTimeoutError::Timeout) => {
                sin_trafico += 1;
                if sin_trafico >= PINGS_SIN_TRAFICO {
                    sin_trafico = 0;
                    if !ping_pong(stream) {
                        return true;
                    }
                }
            }
            // El Comunicador soltó el extremo emisor (re-apuntado/baja): cerramos.
            Err(RecvTimeoutError::Disconnected) => return false,
        }
    }
}

/// Manda un ping por la conexión persistente y espera el pong con un timeout
/// corto. `true` si el peer contestó (sigue vivo y atendiendo); `false` si el
/// write falló (socket roto) o no llegó el pong a tiempo (peer caído o
/// `desconectar`-ado). Restaura el timeout de lectura de negocio al salir.
fn ping_pong(stream: &mut TcpStream) -> bool {
    let Ok(frame) = enmarcar_payload(PING_HEARTBEAT) else {
        return false;
    };
    if stream.write_all(&frame).is_err() {
        return false;
    }
    let _ = stream.set_read_timeout(Some(TIMEOUT_PONG));
    let vivo = leer_respuesta(stream).is_ok();
    // Volver al timeout de las consultas de negocio (más holgado que el del pong).
    let _ = stream.set_read_timeout(Some(TIMEOUT_LECTURA_TCP));
    vivo
}

#[cfg(test)]
mod tests {
    use super::*;
    use std::sync::mpsc::{self, Sender};

    /// Actor de prueba que vuelca a un canal todo lo que recibe.
    struct Sonda {
        tx: Sender<(Transporte, Vec<u8>)>,
    }

    impl Actor for Sonda {
        type Context = Context<Self>;
    }

    impl Handler<PaqueteRecibido> for Sonda {
        type Result = ();

        fn handle(&mut self, msg: PaqueteRecibido, _ctx: &mut Self::Context) {
            let _ = self.tx.send((msg.transporte, msg.datos));
        }
    }

    /// Actor de prueba que vuelca a sendos canales la salud de las conexiones
    /// persistentes (qué rol (re)conectó y qué rol se cayó).
    struct SondaSalud {
        conectado: Sender<RolPeer>,
        desconectado: Sender<RolPeer>,
    }

    impl Actor for SondaSalud {
        type Context = Context<Self>;
    }

    impl Handler<PeerConectado> for SondaSalud {
        type Result = ();

        fn handle(&mut self, msg: PeerConectado, _ctx: &mut Self::Context) {
            let _ = self.conectado.send(msg.rol);
        }
    }

    impl Handler<PeerDesconectado> for SondaSalud {
        type Result = ();

        fn handle(&mut self, msg: PeerDesconectado, _ctx: &mut Self::Context) {
            let _ = self.desconectado.send(msg.rol);
        }
    }

    /// Actor de prueba que contesta cada pedido TCP con un eco (simula un peer
    /// que responde un request-response, p. ej. la pasarela al `CommitPreauth`).
    struct Eco;

    impl Actor for Eco {
        type Context = Context<Self>;
    }

    impl Handler<PaqueteRecibido> for Eco {
        type Result = ();

        fn handle(&mut self, msg: PaqueteRecibido, _ctx: &mut Self::Context) {
            if let Some(responder) = msg.responder {
                responder.responder(msg.datos);
            }
        }
    }

    #[test]
    fn el_request_response_sobre_la_conexion_persistente_devuelve_la_respuesta() {
        System::new().block_on(async {
            // Peer B: un Comunicador real que ecoa cada pedido (como la pasarela
            // respondiendo un Commit por la conexión persistente).
            let addr_b: SocketAddr = "127.0.0.1:18951".parse().unwrap();
            let _b = Comunicador::new(
                addr_b,
                "127.0.0.1:0".parse().unwrap(),
                Eco.start().recipient(),
            )
            .start();

            // Cliente A: mantiene una conexión persistente hacia B.
            let (tx_a, _rx_a) = mpsc::channel();
            let a = Comunicador::new(
                "127.0.0.1:0".parse().unwrap(),
                "127.0.0.1:0".parse().unwrap(),
                Sonda { tx: tx_a }.start().recipient(),
            )
            .start();
            a.send(ConfigurarPeerPersistente {
                rol: RolPeer::Pasarela,
                addr: addr_b,
            })
            .await
            .unwrap();
            // Dar tiempo a que la conexión persistente se establezca (con su ping).
            actix::clock::sleep(Duration::from_millis(300)).await;

            // La consulta debe rutear por la conexión persistente y volver con el eco.
            let respuesta = a
                .send(ConsultarTcp {
                    destino: addr_b,
                    datos: b"commit-por-persistente".to_vec(),
                })
                .await
                .unwrap();
            assert_eq!(
                respuesta.as_deref(),
                Some(&b"commit-por-persistente"[..]),
                "la consulta por la conexión persistente debe devolver la respuesta del peer"
            );
        });
    }

    #[test]
    fn marca_inalcanzable_tras_fallar_y_se_recupera_con_el_resondeo() {
        System::new().block_on(async {
            let (tx, _rx) = mpsc::channel();
            let comunicador = Comunicador::new(
                "127.0.0.1:0".parse().unwrap(),
                "127.0.0.1:0".parse().unwrap(),
                Sonda { tx }.start().recipient(),
            )
            .start();
            let muerto: SocketAddr = "127.0.0.1:18900".parse().unwrap();

            // Una dirección que nunca se intentó se asume alcanzable.
            assert!(comunicador
                .send(ConsultarAlcanzable { destino: muerto })
                .await
                .unwrap());

            // Un envío fallido la marca inalcanzable.
            let entregado = comunicador
                .send(EnviarTcpConfirmado {
                    destino: muerto,
                    datos: b"hola".to_vec(),
                })
                .await
                .unwrap();
            assert!(!entregado);
            actix::clock::sleep(Duration::from_millis(200)).await;
            assert!(
                !comunicador
                    .send(ConsultarAlcanzable { destino: muerto })
                    .await
                    .unwrap(),
                "tras el fallo queda marcada inalcanzable"
            );

            // El servicio "vuelve": el re-sondeo periódico la desmarca solo.
            let _listener = TcpListener::bind(muerto).unwrap();
            let mut alcanzable = false;
            for _ in 0..30 {
                actix::clock::sleep(Duration::from_millis(300)).await;
                alcanzable = comunicador
                    .send(ConsultarAlcanzable { destino: muerto })
                    .await
                    .unwrap();
                if alcanzable {
                    break;
                }
            }
            assert!(alcanzable, "el re-sondeo debería detectar que volvió");
        });
    }

    #[test]
    fn el_peer_que_se_desconecta_deja_de_contestar_el_heartbeat_y_se_lo_da_por_caido() {
        System::new().block_on(async {
            // Peer B: un Comunicador real escuchando, con su propio flag de corte.
            let (tx_b, _rx_b) = mpsc::channel();
            let flag_b = Arc::new(AtomicBool::new(false));
            let addr_b: SocketAddr = "127.0.0.1:18950".parse().unwrap();
            let _b = Comunicador::con_flag(
                addr_b,
                "127.0.0.1:0".parse().unwrap(),
                Sonda { tx: tx_b }.start().recipient(),
                Arc::clone(&flag_b),
            )
            .start();

            // Cliente A: mantiene una conexión persistente hacia B y reporta su salud.
            let (tx_conn, rx_conn) = mpsc::channel();
            let (tx_disc, rx_disc) = mpsc::channel();
            let (tx_a, _rx_a) = mpsc::channel();
            let salud = SondaSalud {
                conectado: tx_conn,
                desconectado: tx_disc,
            }
            .start();
            let a = Comunicador::new(
                "127.0.0.1:0".parse().unwrap(),
                "127.0.0.1:0".parse().unwrap(),
                Sonda { tx: tx_a }.start().recipient(),
            )
            .con_salud(salud.clone().recipient(), salud.recipient())
            .start();
            a.send(ConfigurarPeerPersistente {
                rol: RolPeer::Pasarela,
                addr: addr_b,
            })
            .await
            .unwrap();

            // B está vivo: A reporta la (re)conexión.
            let mut conecto = false;
            for _ in 0..30 {
                actix::clock::sleep(Duration::from_millis(100)).await;
                if rx_conn.try_recv().is_ok() {
                    conecto = true;
                    break;
                }
            }
            assert!(conecto, "con B vivo, A debería reportar PeerConectado");

            // B se "desconecta" (deja de contestar): no rompe el socket, pero deja
            // de responder el pong. A debe darlo por caído por el heartbeat.
            flag_b.store(true, Ordering::Relaxed);
            let mut se_cayo = false;
            for _ in 0..80 {
                actix::clock::sleep(Duration::from_millis(100)).await;
                if rx_disc.try_recv().is_ok() {
                    se_cayo = true;
                    break;
                }
            }
            assert!(
                se_cayo,
                "B dejó de contestar el heartbeat: A debería reportar PeerDesconectado"
            );
            // Y queda marcado inalcanzable (lo usa el alquiler/regularización).
            assert!(
                !a.send(ConsultarAlcanzable { destino: addr_b })
                    .await
                    .unwrap(),
                "el peer caído queda marcado inalcanzable"
            );
        });
    }

    #[test]
    fn el_corte_simulado_descarta_lo_saliente_pero_entrega_lo_entrante() {
        System::new().block_on(async {
            let (tx_a, rx_a) = mpsc::channel::<(Transporte, Vec<u8>)>();
            let (tx_b, _rx_b) = mpsc::channel::<(Transporte, Vec<u8>)>();
            let addr_a: SocketAddr = "127.0.0.1:18902".parse().unwrap();
            let addr_b: SocketAddr = "127.0.0.1:18903".parse().unwrap();
            let a =
                Comunicador::new(addr_a, addr_a, Sonda { tx: tx_a }.start().recipient()).start();
            let b =
                Comunicador::new(addr_b, addr_b, Sonda { tx: tx_b }.start().recipient()).start();
            actix::clock::sleep(Duration::from_millis(300)).await;

            // A se queda sin red: lo que intenta mandar falla al instante.
            a.send(SimularConectividad { conectado: false })
                .await
                .unwrap();
            let entregado = a
                .send(EnviarTcpConfirmado {
                    destino: addr_b,
                    datos: b"no-deberia-salir".to_vec(),
                })
                .await
                .unwrap();
            assert!(!entregado, "sin red no se entrega nada saliente");

            // Pero lo que le MANDAN al Comunicador de A sí llega al actor: el
            // Comunicador es agnóstico y entrega siempre; filtrar el tráfico de
            // red durante el corte es responsabilidad del receptor (la estación).
            let entregado = b
                .send(EnviarTcpConfirmado {
                    destino: addr_a,
                    datos: b"entrante-durante-corte".to_vec(),
                })
                .await
                .unwrap();
            assert!(entregado, "B sí tiene red y A acepta la conexión");
            let mut recibido = None;
            for _ in 0..20 {
                if let Ok(p) = rx_a.try_recv() {
                    recibido = Some(p);
                    break;
                }
                actix::clock::sleep(Duration::from_millis(100)).await;
            }
            let (_, datos) =
                recibido.expect("el Comunicador entrega el entrante aunque esté cortado");
            assert_eq!(datos, b"entrante-durante-corte");

            // Vuelve la red: lo saliente fluye de nuevo.
            a.send(SimularConectividad { conectado: true })
                .await
                .unwrap();
            let entregado = a
                .send(EnviarTcpConfirmado {
                    destino: addr_b,
                    datos: b"ahora-si".to_vec(),
                })
                .await
                .unwrap();
            assert!(entregado, "con la red de vuelta, lo saliente se entrega");
        });
    }

    #[test]
    fn intercambio_tcp_y_udp_entre_dos_comunicadores() {
        let (tx, rx) = mpsc::channel::<(Transporte, Vec<u8>)>();

        let b: SocketAddr = "127.0.0.1:18841".parse().unwrap();
        let a: SocketAddr = "127.0.0.1:18843".parse().unwrap();

        // Corremos el sistema de actores en un thread aparte para que el thread
        // del test quede libre para esperar en el canal.
        thread::spawn(move || {
            let system = System::new();
            system.block_on(async move {
                let sonda = Sonda { tx }.start();
                let _comun_b = Comunicador::new(b, b, sonda.clone().recipient()).start();
                let comun_a = Comunicador::new(a, a, sonda.recipient()).start();

                // Esperamos (sin bloquear el arbiter) a que ambos bindeen.
                actix::clock::sleep(Duration::from_millis(300)).await;

                comun_a.do_send(EnviarTcp {
                    destino: b,
                    datos: b"hola-por-tcp".to_vec(),
                });
                comun_a.do_send(EnviarUdp {
                    destino: b,
                    datos: b"hola-por-udp".to_vec(),
                });
            });
            let _ = system.run();
        });

        let mut recibidos = Vec::new();
        for _ in 0..2 {
            let paquete = rx
                .recv_timeout(Duration::from_secs(5))
                .expect("debería llegar un paquete");
            recibidos.push(paquete);
        }

        assert!(
            recibidos
                .iter()
                .any(|(t, d)| *t == Transporte::Tcp && d == b"hola-por-tcp"),
            "B debería recibir el mensaje por TCP idéntico"
        );
        assert!(
            recibidos
                .iter()
                .any(|(t, d)| *t == Transporte::Udp && d == b"hola-por-udp"),
            "B debería recibir el datagrama UDP"
        );
    }
}
