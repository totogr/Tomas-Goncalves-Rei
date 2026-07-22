//! REPL del usuario: lee comandos por consola y los traduce en pedidos a las
//! estaciones por TCP (request-response, con framing). Actualiza el estado del
//! usuario con cada respuesta.

use std::error::Error;
use std::io::{BufRead, Read, Write};
use std::net::{SocketAddr, TcpStream};

use comun::framing::{enmarcar, Desenmarcador};
use comun::mensajes::usuario_estacion::{
    MensajeEstacionAUsuario, MensajeEstacionAUsuarioConsulta, MensajeUsuario,
    MensajeUsuarioAEstacion, MensajeUsuarioAEstacionConsulta,
};
use comun::serializacion::desde_bytes;
use comun::{Config, EstacionId};

use crate::usuario::{EstadoUsuario, ModoConectividad, Usuario};

pub fn correr(mut usuario: Usuario, config: Config) {
    ayuda();
    let stdin = std::io::stdin();
    for linea in stdin.lock().lines() {
        let Ok(linea) = linea else { break };
        let partes: Vec<&str> = linea.split_whitespace().collect();
        match partes.as_slice() {
            ["alquilar", est, slot] => alquilar(&mut usuario, &config, est, slot),
            ["devolver", est, slot] => devolver(&mut usuario, &config, est, slot),
            ["denunciar", "robo", est] => denunciar_robo_en(&mut usuario, &config, est),
            ["denunciar", "robo"] => denunciar_robo(&mut usuario, &config),
            ["consultar", lat, lon, radio] => consultar(&usuario, &config, lat, lon, radio),
            ["desconectar"] => {
                usuario.cambiar_modo(ModoConectividad::SoloLocal);
                println!("modo SoloLocal: podés alquilar y devolver, pero no consultar");
            }
            ["conectar"] => {
                usuario.cambiar_modo(ModoConectividad::Conectado);
                println!("conectividad restablecida");
            }
            ["estado"] => println!("estado: {:?} (modo {:?})", usuario.estado(), usuario.modo()),
            ["ayuda"] => ayuda(),
            ["salir"] | ["exit"] => break,
            [] => {}
            _ => println!("comando desconocido (probá 'ayuda')"),
        }
    }
}

fn ayuda() {
    println!(
        "comandos: alquilar <estacion> <slot> | devolver <estacion> <slot> | \
         denunciar robo [<estacion>] | consultar <lat> <lon> <radio_km> | desconectar | \
         conectar | estado | ayuda | salir"
    );
}

/// Denuncia de robo en una estación concreta (la que el usuario elige, como en
/// `alquilar`/`devolver`). La estación busca el alquiler activo del usuario.
fn denunciar_robo_en(usuario: &mut Usuario, config: &Config, est: &str) {
    let Ok(est_id) = est.parse::<u32>() else {
        println!("uso: denunciar robo <estacion> (número), o 'denunciar robo' para probar todas");
        return;
    };
    let Some(addr) = direccion(config, est_id) else {
        println!("no conozco la estación {est_id}");
        return;
    };
    let pedido = MensajeUsuario::Operacion(MensajeUsuarioAEstacion::DenunciarRobo {
        usuario_id: usuario.id().clone(),
    });
    match enviar(addr, &pedido) {
        Ok(respuesta @ MensajeEstacionAUsuario::RoboRegistrado { .. }) => {
            usuario.aplicar(&respuesta);
            println!("denuncia registrada en la estación {est_id}: {respuesta:?}");
        }
        Ok(MensajeEstacionAUsuario::SinAlquilerActivo) => {
            println!("no tenés ninguna bici alquilada para denunciar como robada");
        }
        Ok(MensajeEstacionAUsuario::RoboNoProcesado) => println!(
            "la estación {est_id} no pudo verificar tu alquiler (sin conexión al líder); \
             probá otra estación o 'denunciar robo' para recorrerlas todas"
        ),
        Ok(otra) => println!("respuesta inesperada a la denuncia: {otra:?}"),
        Err(e) => println!("error hablando con la estación {est_id}: {e}"),
    }
}

/// Denuncia de robo sin elegir estación: prueba las de la config en orden hasta
/// que una dé una respuesta definitiva (registrado / sin alquiler). Una estación
/// que no pueda verificar (sin conexión al líder) o esté caída se saltea.
fn denunciar_robo(usuario: &mut Usuario, config: &Config) {
    let pedido = MensajeUsuario::Operacion(MensajeUsuarioAEstacion::DenunciarRobo {
        usuario_id: usuario.id().clone(),
    });
    let direcciones: Vec<SocketAddr> = config
        .estaciones
        .iter()
        .map(|e| SocketAddr::from(([127, 0, 0, 1], e.puerto)))
        .collect();
    for addr in direcciones {
        match enviar(addr, &pedido) {
            Ok(respuesta @ MensajeEstacionAUsuario::RoboRegistrado { .. }) => {
                usuario.aplicar(&respuesta);
                println!("denuncia registrada: {respuesta:?}");
                return;
            }
            Ok(MensajeEstacionAUsuario::SinAlquilerActivo) => {
                println!("no tenés ninguna bici alquilada para denunciar como robada");
                return;
            }
            // No pudo verificar (sin conexión al líder) o se cayó: probamos otra.
            Ok(MensajeEstacionAUsuario::RoboNoProcesado) | Err(_) => continue,
            Ok(otra) => {
                println!("respuesta inesperada a la denuncia: {otra:?}");
                return;
            }
        }
    }
    println!("no pude procesar la denuncia (ninguna estación pudo verificar tu alquiler)");
}

fn alquilar(usuario: &mut Usuario, config: &Config, est: &str, slot: &str) {
    // Una bici por usuario: si ya tiene una, no puede alquilar otra.
    if let EstadoUsuario::ConBici { bici_id, .. } = usuario.estado() {
        println!("ya tenés la bici {bici_id:?}; devolvela antes de alquilar otra");
        return;
    }
    let (Ok(est_id), Ok(slot_id)) = (est.parse::<u32>(), slot.parse::<u32>()) else {
        println!("uso: alquilar <estacion> <slot> (números)");
        return;
    };
    let Some(addr) = direccion(config, est_id) else {
        println!("no conozco la estación {est_id}");
        return;
    };
    let pedido = MensajeUsuario::Operacion(MensajeUsuarioAEstacion::SolicitudAlquiler {
        usuario_id: usuario.id().clone(),
        slot_id,
        tarjeta: usuario.tarjeta().clone(),
    });
    responder(usuario, addr, &pedido);
}

fn devolver(usuario: &mut Usuario, config: &Config, est: &str, slot: &str) {
    let (Ok(est_id), Ok(slot_id)) = (est.parse::<u32>(), slot.parse::<u32>()) else {
        println!("uso: devolver <estacion> <slot> (números)");
        return;
    };
    let (bici_id, rental_id) = match usuario.estado() {
        EstadoUsuario::ConBici { bici_id, rental_id } => (*bici_id, rental_id.clone()),
        EstadoUsuario::SinBici => {
            println!("no tenés ninguna bici para devolver");
            return;
        }
    };
    let Some(addr) = direccion(config, est_id) else {
        println!("no conozco la estación {est_id}");
        return;
    };
    let pedido = MensajeUsuario::Operacion(MensajeUsuarioAEstacion::SolicitudDevolucion {
        usuario_id: usuario.id().clone(),
        bici_id,
        rental_id,
        slot_id,
    });
    responder(usuario, addr, &pedido);
}

/// Manda el pedido, aplica la respuesta al estado y la imprime.
fn responder(usuario: &mut Usuario, addr: SocketAddr, pedido: &MensajeUsuario) {
    match enviar(addr, pedido) {
        Ok(respuesta) => {
            usuario.aplicar(&respuesta);
            if let MensajeEstacionAUsuario::DevolucionBiciRobada { bici_id } = &respuesta {
                println!(
                    "La bici {bici_id} figura como ROBADA: no se acepta la devolución. \
                     Se dará aviso a la policía."
                );
            } else {
                println!("respuesta: {respuesta:?}");
            }
        }
        Err(e) => println!("error hablando con la estación: {e}"),
    }
}

/// CU3: el usuario pregunta por estaciones con bici cerca de un punto. Primero
/// hace discovery del líder (le pregunta a cualquier estación conocida) y después
/// le manda la consulta de disponibilidad al líder.
fn consultar(usuario: &Usuario, config: &Config, lat: &str, lon: &str, radio: &str) {
    // En SoloLocal no hay cómo llegar al líder: la consulta global no está
    // disponible (alquilar y devolver contra una estación cercana, sí).
    if usuario.modo() == ModoConectividad::SoloLocal {
        println!("sin señal (SoloLocal): la consulta de disponibilidad no está disponible");
        return;
    }
    let (Ok(lat), Ok(lon), Ok(radio)) =
        (lat.parse::<f64>(), lon.parse::<f64>(), radio.parse::<f64>())
    else {
        println!("uso: consultar <lat> <lon> <radio_km> (números)");
        return;
    };
    // Discovery: preguntamos por el líder a CADA estación de la config hasta que
    // una responda, no solo a la primera. Así, si la primera está caída, el
    // descubrimiento sigue con las demás (antes era un punto único de falla).
    let direcciones: Vec<SocketAddr> = config
        .estaciones
        .iter()
        .map(|e| SocketAddr::from(([127, 0, 0, 1], e.puerto)))
        .collect();
    let lider_addr = match descubrir_lider(&direcciones, |addr| {
        enviar_consulta(
            addr,
            &MensajeUsuario::Consulta(MensajeUsuarioAEstacionConsulta::PreguntarLider),
        )
    }) {
        Ok(addr) => addr,
        Err(motivo) => {
            println!("{motivo}");
            return;
        }
    };
    // Consulta de disponibilidad al líder.
    let consulta =
        MensajeUsuario::Consulta(MensajeUsuarioAEstacionConsulta::ConsultaDisponibilidad {
            usuario_id: usuario.id().clone(),
            ubicacion: (lat, lon),
            radio_max_km: radio,
        });
    match enviar_consulta(lider_addr, &consulta) {
        Ok(MensajeEstacionAUsuarioConsulta::RespuestaDisponibilidad { estaciones }) => {
            if estaciones.is_empty() {
                println!("no hay estaciones con bici dentro de {radio} km");
            } else {
                println!("estaciones con bici cerca:");
                for info in estaciones {
                    println!(
                        "  estación {} en {:?}: {} bicis, {} slots libres",
                        info.estacion_id.0,
                        info.ubicacion,
                        info.bicis_disponibles,
                        info.slots_libres
                    );
                }
            }
        }
        Ok(otra) => println!("respuesta inesperada a la consulta: {otra:?}"),
        // El discovery devolvió un líder que justo cayó (o hay una elección en
        // curso y nos dieron una dirección que ya no escucha): no mostramos el
        // error crudo de socket, sino el mismo mensaje que cuando nadie conoce
        // al líder todavía.
        Err(_) => println!(
            "el líder no está disponible (puede haber una elección en curso); \
             probá de nuevo en unos segundos"
        ),
    }
}

/// Recorre las direcciones preguntando por el líder hasta que una estación
/// responda quién es. No depende de una sola estación: si la primera está caída,
/// prueba con la siguiente; si está viva pero todavía no conoce al líder (hay una
/// elección en curso), sigue con las demás por si alguna sí lo sabe. Devuelve la
/// dirección del líder, o un mensaje explicando por qué no se pudo descubrir.
///
/// `preguntar` está parametrizada para poder testear el recorrido sin red real.
fn descubrir_lider<F>(direcciones: &[SocketAddr], mut preguntar: F) -> Result<SocketAddr, String>
where
    F: FnMut(SocketAddr) -> Result<MensajeEstacionAUsuarioConsulta, Box<dyn Error>>,
{
    if direcciones.is_empty() {
        return Err("no hay estaciones en la config".to_string());
    }
    let mut vio_estacion_viva = false;
    for &addr in direcciones {
        match preguntar(addr) {
            Ok(MensajeEstacionAUsuarioConsulta::RespuestaLider { lider_addr, .. }) => {
                return Ok(lider_addr);
            }
            // Viva, pero sin líder confirmado (elección en curso / no lo sabe aún):
            // seguimos preguntando, alguna otra puede conocerlo.
            Ok(_) => vio_estacion_viva = true,
            // Caída o sin responder: probamos la siguiente.
            Err(_) => {}
        }
    }
    if vio_estacion_viva {
        Err(
            "hay una elección en curso o ninguna estación conoce al líder todavía; \
             probá de nuevo en unos segundos"
                .to_string(),
        )
    } else {
        Err("no pude contactar ninguna estación de la config".to_string())
    }
}

fn direccion(config: &Config, est_id: u32) -> Option<SocketAddr> {
    let est = config.estacion(EstacionId(est_id))?;
    Some(SocketAddr::from(([127, 0, 0, 1], est.puerto)))
}

/// Manda una operación y espera la respuesta de operación.
fn enviar(
    addr: SocketAddr,
    pedido: &MensajeUsuario,
) -> Result<MensajeEstacionAUsuario, Box<dyn Error>> {
    let payload = intercambiar(addr, pedido)?;
    Ok(desde_bytes(&payload)?)
}

/// Manda una consulta y espera la respuesta de consulta.
fn enviar_consulta(
    addr: SocketAddr,
    pedido: &MensajeUsuario,
) -> Result<MensajeEstacionAUsuarioConsulta, Box<dyn Error>> {
    let payload = intercambiar(addr, pedido)?;
    Ok(desde_bytes(&payload)?)
}

/// Cliente TCP request-response: conecta, manda el pedido enmarcado y devuelve el
/// payload (sin deserializar) de la respuesta que llega por la misma conexión.
fn intercambiar(addr: SocketAddr, pedido: &MensajeUsuario) -> Result<Vec<u8>, Box<dyn Error>> {
    let mut stream = TcpStream::connect(addr)?;
    stream.write_all(&enmarcar(pedido)?)?;

    let mut desenmarcador = Desenmarcador::new();
    let mut buf = [0u8; 4096];
    loop {
        let n = stream.read(&mut buf)?;
        if n == 0 {
            return Err("la estación cerró la conexión sin responder".into());
        }
        desenmarcador.alimentar(&buf[..n]);
        if let Some(payload) = desenmarcador.siguiente_payload() {
            return Ok(payload);
        }
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    fn addr(p: u16) -> SocketAddr {
        SocketAddr::from(([127, 0, 0, 1], p))
    }

    fn respuesta_lider(lider: u16) -> MensajeEstacionAUsuarioConsulta {
        MensajeEstacionAUsuarioConsulta::RespuestaLider {
            lider_id: EstacionId(2),
            lider_addr: addr(lider),
            term: 1,
        }
    }

    #[test]
    fn descubre_al_lider_salteando_la_primera_caida() {
        let dirs = vec![addr(8001), addr(8002), addr(8003)];
        // La 8001 está caída (Err); la 8002 responde quién es el líder.
        let lider = descubrir_lider(&dirs, |a| {
            if a == addr(8001) {
                Err("connection refused".into())
            } else {
                Ok(respuesta_lider(9000))
            }
        });
        assert_eq!(
            lider,
            Ok(addr(9000)),
            "debe saltear la caída y encontrar al líder"
        );
    }

    #[test]
    fn si_ninguna_responde_avisa_que_no_se_pudo_contactar() {
        let dirs = vec![addr(8001), addr(8002)];
        let r = descubrir_lider(&dirs, |_| Err("down".into()));
        assert!(r.is_err(), "ninguna viva → error");
        assert!(r.unwrap_err().contains("no pude contactar"));
    }

    #[test]
    fn si_estan_vivas_pero_sin_lider_pide_reintentar() {
        let dirs = vec![addr(8001), addr(8002)];
        // Ambas vivas pero en elección: ninguna conoce al líder todavía.
        let r = descubrir_lider(&dirs, |_| Ok(MensajeEstacionAUsuarioConsulta::EnEleccion));
        assert!(r.is_err());
        assert!(r.unwrap_err().contains("elección en curso"));
    }

    #[test]
    fn una_estacion_viva_que_conoce_al_lider_despues_de_una_en_eleccion() {
        let dirs = vec![addr(8001), addr(8002)];
        // La 8001 está en elección; la 8002 sí conoce al líder.
        let lider = descubrir_lider(&dirs, |a| {
            if a == addr(8001) {
                Ok(MensajeEstacionAUsuarioConsulta::EnEleccion)
            } else {
                Ok(respuesta_lider(9000))
            }
        });
        assert_eq!(lider, Ok(addr(9000)));
    }
}
