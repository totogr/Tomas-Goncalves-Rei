# Decisiones de diseño (ADRs)

Registro de las decisiones de diseño y arquitectura más relevantes tomadas durante el proyecto, con sus alternativas y la justificación.

---

## ADR-01 · Modelo de usuarios: clientes y profesionales en tablas separadas

**Contexto.** Se necesitaba representar dos tipos de usuario (clientes y profesionales) que comparten campos base pero tienen atributos y comportamientos distintos. Un mismo usuario no puede ser cliente y profesional a la vez.

**Opciones evaluadas:**

1. **Dos tablas separadas (`clients` y `professionals`).** Cada tipo vive en su propia tabla con sus campos específicos. Punto a resolver: un mismo email no puede existir en ambas tablas simultáneamente.
2. **Una tabla unificada (`users`) con un campo `role`.** Punto a resolver: clientes y profesionales con el mismo email tendrían que diferenciarse por nombre de usuario, lo que complica la autenticación.
3. **Tabla base + tablas derivadas.** Una tabla `users` con los campos comunes y dos tablas derivadas (`clients` y `professionals`) con los campos específicos de cada rol.

**Decisión adoptada: Opción 1 — dos tablas completamente separadas.**

El criterio de unicidad de email se aplica **entre ambas tablas**: al registrarse, se verifica que el email no exista ni en `clients` ni en `professionals`. Si ya existe en cualquiera de las dos, el registro se rechaza con un error **409 Conflict**.

**Consecuencias.** Modelo simple y desacoplado por rol; la unicidad de email requiere una verificación explícita cruzada entre tablas en el registro.

---

## ADR-02 · Backend en Java / Spring Boot

**Decisión.** API REST en **Java 21 + Spring Boot** (requisito de la cátedra: backend en Java). Se utilizan Spring Data JPA, Spring Security, Liquibase (migraciones), JWT (jjwt) y springdoc-openapi (documentación interactiva).

---

## ADR-03 · Frontend en React + TypeScript

**Decisión.** Aplicación web SPA en **React 19 + TypeScript** con Vite. Routing con `wouter`, manejo de estado de servidor con TanStack Query, formularios con TanStack Form y validación con Zod.

**Justificación.** Minimizar la fricción del cliente final (reservar desde el navegador sin instalar apps), uno de los hallazgos clave del relevamiento.

---

## ADR-04 · PostgreSQL como base de datos

**Decisión.** **PostgreSQL** en producción/desarrollo; **H2** en memoria para los tests (aislamiento y velocidad). Esquema versionado con Liquibase.

---

## ADR-05 · Autenticación con JWT (access + refresh token)

**Decisión.** Autenticación stateless mediante **JWT**: un *access token* de corta duración y un *refresh token* de larga duración (endpoint `PUT /sessions` para refrescar). El control de acceso se hace por rol (`CLIENT` / `PROFESSIONAL`).

---

## ADR-06 · Despliegue con Docker Compose + ingress

**Decisión.** Cada subproyecto (backend, frontend, ingress) se empaqueta en su propio contenedor Docker y se orquesta con **Docker Compose**. Un **ingress** (reverse proxy) expone todos los servicios bajo un único dominio HTTP. El despliegue a la nube se automatiza con el pipeline de GitLab CI/CD.

---

## ADR-07 · Refresh token persistido en base de datos (token opaco)

**Contexto.** La autenticación usa JWT para el *access token* (stateless, de corta duración). Faltaba decidir cómo manejar el *refresh token*.

**Decisión.** El refresh token **no es un JWT**, sino un **token opaco persistido** en la tabla `RefreshToken` (con `userId`, `userType` y `expiresAt`). Al refrescar, se valida contra la base de datos (`RefreshTokenService.verify(...)`) y se emite un nuevo par de tokens.

**Justificación.** A diferencia de un refresh stateless, persistirlo permite **invalidar sesiones** y controlar su expiración del lado del servidor, a costa de una consulta a la base en cada refresh.

---

## ADR-08 · Autenticación unificada para clientes y profesionales

**Contexto.** Clientes y profesionales viven en tablas separadas (ver ADR-01), pero ambos deben poder iniciar sesión.

**Decisión.** Un **único punto de autenticación** (`AuthService.login`) que busca las credenciales primero en `professionals` y luego en `clients`, abstraído mediante la interfaz `UserCredentials` (`username` / `password`). El rol resultante (`CLIENT` / `PROFESSIONAL`) se incluye en el token emitido.

**Justificación.** Concilia el modelo de tablas separadas con una experiencia de login única, evitando endpoints duplicados y manteniendo la lógica de autenticación en un solo lugar.

---

## ADR-09 · Hashing de contraseñas con BCrypt

**Decisión.** Las contraseñas se almacenan hasheadas con **BCrypt** (`PasswordEncoder = BCryptPasswordEncoder`), nunca en texto plano.

**Justificación.** BCrypt incorpora *salt* automático y un costo computacional ajustable, lo que lo hace resistente a ataques de fuerza bruta y de tablas precalculadas (rainbow tables).

---

## ADR-10 · Recordatorios mediante job programado (polling) con opt-in del cliente

**Contexto.** Se necesitaba enviar recordatorios de turnos próximos sin intervención manual del profesional.

**Decisión.** Un job programado (`AppointmentReminderScheduler`) se ejecuta periódicamente (`@Scheduled`, cada 15 minutos), busca los turnos dentro de las próximas 24 horas y envía un email de recordatorio. El envío respeta el flag `receivesReminders` del cliente (**opt-in**) y marca cada turno con `reminderSent` para no duplicar avisos.

**Alternativas.** Un planificador de tareas externo (cron del sistema) o un esquema basado en eventos/colas.

**Justificación.** El polling interno es simple, no requiere infraestructura adicional y se integra de forma nativa con Spring. El flag `reminderSent` garantiza idempotencia.

**Consecuencias.** La granularidad del recordatorio depende del intervalo del job (15 min).

---

## ADR-11 · Cálculo de disponibilidad dinámico (on-demand)

**Contexto.** Para reservar un turno se necesita conocer los horarios libres de un profesional en una fecha y servicio determinados.

**Decisión.** La disponibilidad se **calcula al momento** (`AvailabilityService`), combinando los horarios de trabajo (`Schedule`), los bloqueos de fechas (`ScheduleBlock`) y los turnos ya reservados. **No se materializan ni almacenan slots** en la base de datos.

**Alternativas.** Pre-generar y persistir todos los slots disponibles.

**Justificación.** Calcular on-demand evita mantener sincronizada una tabla de slots ante cada cambio de horario, bloqueo o reserva, y reduce el riesgo de inconsistencias. El cálculo queda acotado a un profesional/servicio/fecha, por lo que su costo es bajo.

---

## ADR-12 · Servicio de email desacoplado con Resend

**Contexto.** El sistema envía emails transaccionales (recuperación de contraseña y recordatorios de turno).

**Decisión.** Un único `EmailService` **encapsula el proveedor de email (Resend)** y centraliza las plantillas HTML. El resto del sistema invoca métodos de alto nivel (`sendResetEmail`, `sendAppointmentReminderEmail`) sin conocer el proveedor. La API key se inyecta por configuración (`resend.api-key`) y el servicio falla al arrancar si no está definida.

**Justificación.** Desacopla la lógica de negocio del proveedor concreto: cambiar de proveedor de email solo afecta a esta clase. Centralizar las plantillas evita duplicación.

---

## ADR-13 · Sesiones stateless, CSRF deshabilitado y CORS abierto

**Contexto.** Al ser una API REST consumida por una SPA y autenticada con JWT, se debió configurar la seguridad HTTP (`SecurityConfig`).

**Decisión.**
- **Sesiones STATELESS** (sin estado de sesión en el servidor): cada request se autentica por su JWT a través de un filtro propio (`JwtAuthFilter`).
- **CSRF deshabilitado**, dado que no se usan cookies de sesión (el token viaja en el header `Authorization`, no en una cookie).
- **CORS restringido y parametrizado:** los orígenes permitidos se inyectan por configuración (`application.cors.allowed-origins`, variable `CORS_ALLOWED_ORIGINS`) y por defecto se limitan al frontend de desarrollo y al dominio productivo del grupo —**no** se usa `*`—. Solo se habilitan los métodos y headers que la SPA necesita.
- Lista blanca de endpoints públicos explícitos (signup, login/refresh, Swagger, healthcheck); **todo el resto requiere autenticación** y cualquier ruta no contemplada se rechaza (`anyRequest().denyAll()`).

**Justificación.** Es la configuración habitual para una API stateless con JWT consumida por una SPA. Parametrizar los orígenes CORS evita exponer la API a cualquier dominio y permite cambiar la lista entre ambientes sin tocar el código (ver [parametrización](../05-ingenieria-y-calidad/configuracion-parametrizacion.md)).
