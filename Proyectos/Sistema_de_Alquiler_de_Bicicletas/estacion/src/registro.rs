//! Registro autoritativo de alquileres abiertos que mantiene el líder.
//!
//! Es la "fuente de verdad" del sistema sobre qué alquileres están en curso. Cada
//! estación, además, guarda los suyos en `alquileres_propios` (eso permite
//! reconstruir este registro tras una elección, en la Etapa 5).

use std::collections::HashMap;

use comun::{Alquiler, BiciId, EstadoAlquiler, RentalId, Timestamp};

#[derive(Default)]
pub struct Registro {
    alquileres: HashMap<RentalId, Alquiler>,
}

impl Registro {
    pub fn new() -> Self {
        Self::default()
    }

    /// Incorpora (o reemplaza) un alquiler al registro.
    pub fn agregar(&mut self, alquiler: Alquiler) {
        self.alquileres.insert(alquiler.rental_id.clone(), alquiler);
    }

    /// Busca el alquiler **activo** de una bicicleta (lo usa la devolución para
    /// armar los datos de cobro).
    pub fn buscar_por_bici(&self, bici_id: BiciId) -> Option<&Alquiler> {
        self.alquileres
            .values()
            .find(|a| a.bici_id == bici_id && a.estado == EstadoAlquiler::Activo)
    }

    /// Busca el alquiler **activo** de un usuario (lo usa la denuncia de robo,
    /// que llega sin el id del alquiler: el usuario solo dice "me robaron").
    pub fn buscar_por_usuario(&self, usuario_id: &comun::UsuarioId) -> Option<&Alquiler> {
        self.alquileres
            .values()
            .find(|a| &a.usuario_id == usuario_id && a.estado == EstadoAlquiler::Activo)
    }

    /// Marca un alquiler como cerrado. Devuelve `true` si existía.
    pub fn cerrar(&mut self, rental_id: &RentalId) -> bool {
        match self.alquileres.get_mut(rental_id) {
            Some(a) => {
                a.estado = EstadoAlquiler::Cerrado;
                true
            }
            None => false,
        }
    }

    /// Marca un alquiler como robado (denuncia). Devuelve `true` si existía.
    pub fn marcar_robado(&mut self, rental_id: &RentalId) -> bool {
        match self.alquileres.get_mut(rental_id) {
            Some(a) => {
                a.estado = EstadoAlquiler::Robado;
                true
            }
            None => false,
        }
    }

    /// ¿El registro ya conoce este alquiler (activo o cerrado)? Evita que un
    /// `IngresoTardio` re-abra un alquiler que el líder ya cerró.
    pub fn contiene(&self, rental_id: &RentalId) -> bool {
        self.alquileres.contains_key(rental_id)
    }

    /// Busca un alquiler por su id, en cualquier estado. Lo usa la detección de
    /// robo por inactividad: cuando vence el timer chequea si el alquiler sigue
    /// activo (si ya se devolvió/robó, el timer no hace nada).
    pub fn buscar(&self, rental_id: &RentalId) -> Option<&Alquiler> {
        self.alquileres.get(rental_id)
    }

    /// `(rental_id, inicio)` de cada alquiler activo. Lo usa el líder para
    /// (re)programar los timers de robo por inactividad tras reconstruir el
    /// registro (elección) o reiniciar.
    pub fn activos_para_timer(&self) -> Vec<(RentalId, Timestamp)> {
        self.alquileres
            .values()
            .filter(|a| a.estado == EstadoAlquiler::Activo)
            .map(|a| (a.rental_id.clone(), a.inicio))
            .collect()
    }

    /// ¿El registro ya tiene este alquiler y NO está activo (cerrado o robado)?
    /// Un `AlquilerAbierto` tardío (p. ej. de la regularización de un alquiler que
    /// se cerró offline) no debe re-abrirlo.
    pub fn esta_cerrado(&self, rental_id: &RentalId) -> bool {
        self.alquileres
            .get(rental_id)
            .is_some_and(|a| a.estado != EstadoAlquiler::Activo)
    }

    /// Cantidad de alquileres activos (para diagnóstico/consulta).
    pub fn activos(&self) -> usize {
        self.alquileres
            .values()
            .filter(|a| a.estado == EstadoAlquiler::Activo)
            .count()
    }
}

#[cfg(test)]
mod tests {
    use super::*;
    use comun::{EstacionId, Timestamp, UsuarioId};

    fn alquiler(rental: &str, bici: u32) -> Alquiler {
        Alquiler {
            rental_id: RentalId(rental.to_string()),
            bici_id: BiciId(bici),
            usuario_id: UsuarioId("alice".to_string()),
            estacion_origen: EstacionId(1),
            inicio: Timestamp(0),
            fin: None,
            preauth_id: Some("P-1".to_string()),
            estado: EstadoAlquiler::Activo,
        }
    }

    #[test]
    fn agrega_busca_por_bici_y_cierra() {
        let mut reg = Registro::new();
        reg.agregar(alquiler("R1", 42));
        reg.agregar(alquiler("R2", 7));
        assert_eq!(reg.activos(), 2);

        // Busca por bici_id el alquiler activo.
        let encontrado = reg.buscar_por_bici(BiciId(42)).expect("debería estar");
        assert_eq!(encontrado.rental_id, RentalId("R1".to_string()));

        // Cierra R1: deja de aparecer como activo.
        assert!(reg.cerrar(&RentalId("R1".to_string())));
        assert_eq!(reg.activos(), 1);
        assert!(reg.buscar_por_bici(BiciId(42)).is_none());

        // Cerrar algo inexistente devuelve false.
        assert!(!reg.cerrar(&RentalId("R9".to_string())));
    }
}
