# Guion de la demo

Recorrido sugerido para la demo final sobre el **ambiente desplegado en la nube** (`https://grupo-09.tp1.ingsoft1.fiuba.ar`), mostrando escenarios realistas con **datos precargados**.

## Preparación

- **Datos precargados:** ejecutar [`docs/demo-seed.sql`](../demo-seed.sql) en la base (Adminer/Postgres) antes de la defensa. Deja 6 profesionales, 16 clientes, servicios, horarios, turnos pasados (para estadísticas y reseñas) y turnos próximos (para la agenda).
- **Contraseña de todos los usuarios:** `12345678`.
- **Fechas del seed:** turnos pasados del 2 al 9/6/2026; turnos próximos del 15 al 21/6/2026 (semana de entrega).
- Tener abiertas dos sesiones/navegadores (uno como **cliente**, otro como **profesional**) para mostrar ambos lados sin re-loguear.

### Usuarios clave del seed

| Rol | Usuario | Para mostrar |
|-----|---------|--------------|
| Profesional | `lucia@demo.com` (Peluquería, slots de 30 min) | Agenda, servicios, estadísticas, reseñas |
| Profesional | `martin@demo.com` (Barbería, slots de 15 min) | Intervalos de slot distintos |
| Profesional | `javier@demo.com` (Kinesiología) | Servicio grupal (capacidad 3) y lista de espera |
| Cliente | `ana@demo.com` | Reserva, cancelación, reprogramación |
| Cliente | `daniel@demo.com` (2 ausencias) | Cliente con inasistencias / bloqueo |

> El servicio 9 es un **taller grupal con `max_capacity = 3`**, ideal para mostrar capacidad y lista de espera.

## Recorrido sugerido (flujos principales)

1. **Login unificado.** Iniciar sesión como cliente (`ana@demo.com`). Comentar que el mismo login sirve para clientes y profesionales (ADR-08) y que la sesión usa JWT + refresh token.

2. **Descubrir y reservar (lado cliente).**
   - Listar profesionales, abrir uno (p. ej. Lucía), elegir un servicio.
   - Ver la **disponibilidad calculada on-demand** (respeta horarios, bloqueos y turnos ya tomados).
   - Reservar un slot futuro → confirmación + email de aviso. Mostrar que reservar un slot ocupado da error (no hay dobles reservas).

3. **Gestionar la reserva (lado cliente).**
   - Ver "Mis turnos" (próximos / pasados / cancelados).
   - **Reprogramar** un turno (cancela y redirige a elegir nuevo horario con el servicio preseleccionado).
   - **Cancelar** un turno y mostrar el cambio de estado.

4. **Lista de espera.**
   - Con el servicio grupal de Javier lleno, anotarse en la **lista de espera**.
   - Liberar un cupo (cancelar desde otra sesión) y mostrar que el primero en la lista es **notificado/promovido** automáticamente (evento + scheduler).

5. **Lado profesional.**
   - Login como `lucia@demo.com`. Mostrar la **agenda** con los turnos próximos del seed.
   - Configuración: **servicios**, **horarios de trabajo**, **bloqueo de fechas/horas**.
   - **Marcar inasistencia** de un turno pasado (incrementa el contador de ausencias del cliente).
   - **Bloquear un cliente** problemático (Daniel, con 2 ausencias) y mostrar que no puede reservarle.

6. **Estadísticas y reseñas (lado profesional).**
   - Mostrar las **estadísticas** (turnos por día, servicios más pedidos, clientes frecuentes) alimentadas por los turnos pasados del seed.
   - Mostrar **reseñas** dejadas por clientes.

7. **Recordatorios (mencionar, no necesariamente en vivo).**
   - Explicar el job programado que envía recordatorios 24 h antes a quienes tienen el opt-in.

## Escenarios de error a mostrar (si preguntan)

- Reservar un slot ocupado → `SLOT_TAKEN` (sin doble reserva).
- Reservar en el pasado → error de validación.
- Intentar cancelar/ver un turno ajeno → `403` (control de pertenencia).
- Acceder a un endpoint protegido sin sesión → `401`.

## Mensajes a transmitir durante la demo

- **No hay dobles reservas** ni superposiciones (correctitud + concurrencia).
- La **disponibilidad es siempre consistente** porque se calcula, no se almacena.
- El sistema **reduce pérdidas**: inasistencias, bloqueo de clientes y lista de espera que reocupa cupos.
- Todo el manejo de errores devuelve mensajes claros al usuario, sin romper la UI.
