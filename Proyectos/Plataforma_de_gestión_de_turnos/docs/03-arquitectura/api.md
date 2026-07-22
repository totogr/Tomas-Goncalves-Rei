# API — Endpoints

Documentación de la API REST del backend. La API expone documentación interactiva (Swagger UI / OpenAPI).

> **Base de autenticación:** salvo los endpoints de registro/login, todos requieren un `Bearer token` (JWT) en el header `Authorization`.

---

## Autenticación

| Método | Endpoint | Auth | Descripción |
|--------|----------|------|-------------|
| POST | `/sessions` | No | Login. Devuelve `accessToken`, `refreshToken`, `role` (`CLIENT`/`PROFESSIONAL`) e `id`. |
| PUT | `/sessions` | No | Refresh del token a partir del `refreshToken`. |
| POST | `/sessions/forgot-password` | No | Solicita el envío de un email para recuperar la contraseña. Devuelve 200 siempre que el email exista. |
| POST | `/sessions/reset-password` | No | Restablece la contraseña usando un token válido (mínimo 8 caracteres). 400 si el token es inválido o expirado. |

---

## Clientes

| Método | Endpoint | Auth | Descripción |
|--------|----------|------|-------------|
| POST | `/clients/signup` | No | Registro de cliente (password ≥ 8 caracteres). 409 si el email ya existe. |
| GET | `/clients` | Sí | Listar todos los clientes. Solo accesible para profesionales. |
| PATCH | `/clients/me/preferences` | Sí | Actualizar preferencias de notificaciones del cliente logueado (ej: activar/desactivar emails recordatorios). |

---

## Profesionales

| Método | Endpoint | Auth | Descripción |
|--------|----------|------|-------------|
| POST | `/professionals/signup` | No | Registro de profesional. 409 si el email ya existe. |
| GET | `/professionals` | Sí | Listar profesionales (`specialty` y `rating` pueden ser null). |
| GET | `/professionals/{id}` | Sí | Perfil del profesional (incluye ubicación y servicios). 404 si no existe. |
| PATCH | `/professionals/{id}/profile` | Sí | Completar perfil post-registro (specialty, address, neighborhood, city). 204 No Content. 403 si no es el dueño del perfil. |
| GET | `/professionals/{id}/services/{serviceId}/availability?date=YYYY-MM-DD` | Sí | Slots disponibles para un servicio en una fecha determinada. |

---

## Clientes bloqueados

| Método | Endpoint | Auth | Descripción |
|--------|----------|------|-------------|
| GET | `/professionals/{professionalId}/blocked-clients` | Sí | Obtener los IDs de clientes bloqueados por el profesional logueado. |
| POST | `/professionals/{professionalId}/blocked-clients` | Sí | Bloquear un cliente. Devuelve el registro con `id`, `professionalId`, `clientId` y `blockedAt`. |
| DELETE | `/professionals/{professionalId}/blocked-clients/{clientId}` | Sí | Desbloquear un cliente. |

---

## Turnos — cliente

| Método | Endpoint | Auth | Descripción |
|--------|----------|------|-------------|
| POST | `/appointments?clientId={id}` | Sí | Crear un turno. Requiere `professional_id`, `service_id`, `employee_id`, `date` y `time`. |
| GET | `/bookings/me?status=upcoming\|past\|cancelled&clientId={id}` | Sí | Mis turnos (filtrable por estado). |
| GET | `/bookings/{id}?clientId={id}` | Sí | Detalle de un turno. 403 si no pertenece al cliente; 404 si no existe. |
| PATCH | `/bookings/{id}/cancel?clientId={id}` | Sí | Cancelar un turno (setea `cancelled_by = client`). |
| PATCH | `/bookings/{id}/reschedule?clientId={id}` | Sí | Reprogramar un turno existente con nueva `date` y `time`. |
| POST | `/bookings/{id}/review` | Sí | Dejar reseña (score 1–5). Solo para turnos completados sin reseña previa. 409 si ya tiene reseña. |

---

## Turnos — profesional

| Método | Endpoint | Auth | Descripción |
|--------|----------|------|-------------|
| GET | `/pro/bookings?status=all&profId={id}` | Sí | Listar turnos del profesional (filtrable por estado). Incluye `cancelled_by`, `marked_absent_at` y datos del cliente. |
| PATCH | `/pro/bookings/{id}/cancel?profId={id}` | Sí | Cancelar un turno desde el lado del profesional (setea `cancelled_by = professional`). |
| PATCH | `/pro/bookings/{id}/absent?profId={id}` | Sí | Marcar un cliente como ausente en un turno. |

---

## Horarios

| Método | Endpoint | Auth | Descripción |
|--------|----------|------|-------------|
| GET | `/schedule` | Sí | Obtener los horarios laborales del profesional logueado (devuelve los 7 días con sus rangos y el intervalo de slots en minutos). |
| PUT | `/schedule` | Sí | Guardar horarios laborales. Recibe estructura de días con rangos `from`/`to` y `slotIntervalMinutes`. |
| GET | `/schedule/blocks` | Sí | Listar bloques de horario inhabilitados del profesional logueado. |
| POST | `/schedule/blocks` | Sí | Crear un bloque de horario inhabilitado para una fecha y rango horario. Devuelve el bloque creado y la cantidad de turnos cancelados como consecuencia. |
| DELETE | `/schedule/blocks/{id}` | Sí | Eliminar un bloque de horario inhabilitado. |

---

## Servicios

| Método | Endpoint | Auth | Descripción |
|--------|----------|------|-------------|
| GET | `/services` | Sí | Listar servicios del profesional logueado. |
| POST | `/services` | Sí | Crear un nuevo servicio (`name`, `duration_minutes`, `price`, `max_capacity`). |
| PUT | `/services/{id}` | Sí | Actualizar un servicio existente. Permite modificar todos sus campos incluyendo `active`. |
| DELETE | `/services/{id}` | Sí | Eliminar un servicio. |

---

## Lista de espera

| Método | Endpoint | Auth | Descripción |
|--------|----------|------|-------------|
| GET | `/waitlist?professionalId={id}&serviceId={id}&slotStart={datetime}` | Sí | Listar entradas de la lista de espera para un slot específico. Devuelve posición de cada cliente. |
| POST | `/waitlist?professionalId={id}&serviceId={id}&slotStart={datetime}&clientId={id}` | Sí | Inscribir un cliente en la lista de espera para un slot. |
| DELETE | `/waitlist?professionalId={id}&serviceId={id}&slotStart={datetime}&clientId={id}` | Sí | Eliminar a un cliente de la lista de espera. |
| POST | `/waitlist/confirm?professionalId={id}&serviceId={id}&slotStart={datetime}&clientId={id}` | Sí | Confirmar el turno para un cliente promovido desde la lista de espera. |
| GET | `/waitlist/me?professionalId={id}&serviceId={id}&slotStart={datetime}&clientId={id}` | Sí | Obtener la entrada del cliente logueado en la lista de espera para un slot. |
| GET | `/waitlist/me/all?clientId={id}` | Sí | Listar todas las listas de espera en las que está inscripto el cliente logueado. |
| GET | `/waitlist/promotions/professional?profId={id}` | Sí | Ver promociones de lista de espera pendientes de confirmación desde el lado del profesional. Incluye `expiresAt`. |

---

## Estadísticas

| Método | Endpoint | Auth | Descripción |
|--------|----------|------|-------------|
| GET | `/pro/stats?period=30d` | Sí | Resumen del período: total de turnos, tasa de cancelación, rating promedio, ingresos estimados, turnos por día, servicios más usados y clientes frecuentes. |

---

## Códigos de error estándar

| HTTP | Descripción |
|------|-------------|
| 400 | Campos faltantes o inválidos |
| 401 | Token ausente, expirado o credenciales incorrectas |
| 403 | Sin permisos para este recurso |
| 404 | Recurso no encontrado |
| 409 | Conflicto (email duplicado, reseña ya existente, etc.) |

> La documentación interactiva (Swagger UI) se sirve desde el backend en ejecución. Configuración en `backend/.../config/OpenApiConfiguration.java`.