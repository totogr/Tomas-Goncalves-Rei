# Testing

## Estrategia general

El testing se organiza en dos niveles principales:

- **Backend (Java):** tests unitarios de la lógica de negocio (servicios) y tests de integración de la capa web (controladores REST). Frameworks: **JUnit 5**, **Mockito**, **Spring Boot Test** y **Spring Security Test**; base de datos **H2** en memoria para aislar los tests. Cobertura medida con **JaCoCo**.
- **Frontend (TypeScript/React):** tests de componentes con **Vitest** + **Testing Library** (entorno `happy-dom`).

Los tests se ejecutan automáticamente en el **pipeline de CI** (`.gitlab-ci.yml`) en cada Merge Request.

## Cómo ejecutar

```bash
# Backend — corre los tests y genera el reporte de cobertura JaCoCo
cd backend && ./mvnw test
# Reporte: backend/target/site/jacoco/index.html

# Frontend
cd frontend && npm test
```

## Tests unitarios (backend)

Cubren la lógica de negocio de cada módulo. Clases de test (en `backend/src/test/.../turnos/`):

- `appointment/` — `AvailabilityServiceTest`, `AvailabilityServiceBranchesTest`,
  `BookingServiceTest`, `BookingServiceBranchesTest`.
- `professional/` — `ProfessionalServiceTest`, `ProfessionalServiceLoginTest`,
  `StatsServiceTest`.
- `client/` — `ClientServiceTest`.
- `service/` — `ServiceServiceTest`.
- `schedule/` — `ScheduleServiceTest`, `ScheduleBlockServiceTest`.
- `review/` — `ReviewServiceTest`.
- `blockedclient/` — `BlockedClientServiceTest`.
- `user/` — `AuthServiceTest`.
- `config/email/` — `PasswordResetEmailServiceTest`.

> Nota: las clases `*BranchesTest` apuntan explícitamente a cubrir **ramas y casos de borde** (no solo el camino feliz).

## Tests de integración (backend)

Validan la capa web (endpoints) con el contexto de Spring y seguridad:

- `appointment/BookingRestControllerTest`
- `professional/ProfessionalRestControllerTest`
- `client/ClientRestControllerTest`
- `service/ServiceRestControllerTest`
- `schedule/ScheduleRestControllerTest`
- `blockedclient/BlockedClientRestControllerTest`
- `TurnosApplicationTests` (carga del contexto de la aplicación).

## Tests de frontend

Tests de componentes (Vitest + Testing Library):

- `components/BookingSuccess/BookingSuccess.test.tsx`
- `components/ProfessionalList/ProfessionalList.test.tsx`
- `components/ProfessionalServiceList/ProfessionalServiceList.test.tsx`

## Casos de prueba definidos

Los escenarios principales cubiertos por la suite (camino feliz + casos de borde y error):

| # | Caso de prueba | Resultado esperado | Dónde se verifica |
|---|----------------|--------------------|-------------------|
| 1 | Reservar un turno en un slot libre | Turno creado en estado `CONFIRMED`, emails de aviso enviados | `BookingServiceTest`, `BookingRestControllerTest` |
| 2 | Reservar un slot ya ocupado (sin capacidad) | Error `409 SLOT_TAKEN`, no se crea el turno | `BookingServiceTest` / `*BranchesTest` |
| 3 | Reservar en el pasado o en un horario bloqueado | Error `400 SLOT_IN_PAST` / `409 SLOT_TAKEN` | `BookingServiceBranchesTest` |
| 4 | Reservar un servicio inactivo | Error `400 SERVICE_INACTIVE` | `BookingServiceBranchesTest` |
| 5 | Reservar siendo un cliente bloqueado por el profesional | El profesional aparece como no encontrado (`404`) | `BookingServiceTest` + `BlockedClientServiceTest` |
| 6 | Cancelar un turno propio futuro | Turno en `CANCELLED`, evento de cancelación publicado (waitlist) | `BookingServiceTest` |
| 7 | Cancelar un turno ajeno o ya pasado | Error `403 FORBIDDEN` / `400 BAD_REQUEST` | `BookingServiceBranchesTest` |
| 8 | Reprogramar un turno a un slot libre / ocupado | Reprogramado / Error `409 SLOT_FULL` o `422` | `BookingServiceBranchesTest` |
| 9 | Marcar inasistencia de un cliente | Incrementa `absenceCount`; doble marca rechazada | `BookingServiceTest` |
| 10 | Cálculo de disponibilidad (horarios, bloqueos, ocupación) | Slots libres correctos según `Schedule` + `ScheduleBlock` + turnos | `AvailabilityServiceTest`, `AvailabilityServiceBranchesTest` |
| 11 | Login con credenciales válidas / inválidas | Par de tokens emitido / `401 UNAUTHORIZED` | `AuthServiceTest` |
| 12 | Refresh de sesión con token válido / inválido | Nuevo par de tokens / error | `AuthServiceTest`, `RefreshTokenServiceTest` |
| 13 | Registro con email ya existente (cliente o profesional) | Error `409 CONFLICT` | `ProfessionalServiceTest`, `ClientServiceTest` |
| 14 | Promoción de lista de espera al liberarse un slot | El primero en la waitlist es notificado | `WaitListServiceTest` |
| 15 | Acceso a endpoint protegido sin token / con rol incorrecto | `401` / `403` | tests de `*RestControllerTest` con Spring Security Test |

## Evidencias y resultados obtenidos

- **Total de tests:** **255 tests en el backend** (unitarios + integración) y **28 tests en el frontend**, todos en verde.
- **Cobertura backend (JaCoCo):** **82,6 % de instrucciones** y **67,6 % de ramas** (medido sobre el último build; reporte en `backend/target/site/jacoco/index.html`).
- **Estado en CI:** la suite completa se ejecuta en cada Merge Request del pipeline de GitLab CI y debe estar en verde para poder mergear.
- **Evidencias de ejecución:** capturas del reporte de cobertura y de la corrida en CI en [../06-evidencias-testing/](../06-evidencias-testing/).

> Reproducción rápida: `cd backend && ./mvnw test` (255 tests) y `cd frontend && npm test` (28 tests).
