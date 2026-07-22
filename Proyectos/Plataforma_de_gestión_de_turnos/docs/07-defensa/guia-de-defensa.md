# Guía de defensa — preguntas y respuestas

Banco de preguntas probables con respuestas de referencia, organizado por los temas que la cátedra evalúa en la instancia individual: **producto, requerimientos, diseño y código, arquitectura, desarrollo, testing, decisiones tomadas, manejo de errores, seguridad, escalabilidad y forma de trabajo**.

> Recomendación: que **cada integrante** pueda responder al menos a nivel general sobre todos los temas, y en profundidad sobre las partes en las que trabajó. Los roles por sprint están en [../02-sprints/README.md](../02-sprints/README.md).

---

## 1. Producto y problema

**¿Qué problema resuelve el producto?**
La fricción de gestionar turnos de forma manual (WhatsApp, agenda en papel) que sufren profesionales y negocios de servicios: superposición de horarios, dobles reservas, cancelaciones de último momento sin penalización y ausencias que dejan lugares vacíos. La plataforma centraliza la agenda, publica disponibilidad y permite reservar/cancelar/reprogramar, reduciendo esa fricción tanto para el profesional como para el cliente final.

**¿Quiénes son los usuarios? ¿Por qué les importa?**
Dos roles: el **profesional/negocio** (peluquerías, entrenadores, salud, complejos deportivos) que administra su agenda, y el **cliente final** que reserva. Está documentado en las user personas y mapas de empatía ([../01-discovery/](../01-discovery/)). Un hallazgo clave del relevamiento es minimizar la fricción del cliente: reservar desde el navegador **sin instalar ninguna app**.

**¿Cuál es la propuesta de valor / diferencial?**
Centralización de la agenda + reducción de pérdidas por ausencias (marcado de inasistencias, bloqueo de clientes problemáticos, lista de espera que reocupa slots liberados) + recordatorios automáticos opt-in.

---

## 2. Requerimientos

**¿Cómo relevaron y validaron los requerimientos?**
Mediante el proceso de discovery: entrevistas, encuestas, user personas, mapas de empatía, customer journey e hipótesis con sus validaciones (todo en [../01-discovery/](../01-discovery/)).

**¿Cuáles son los requerimientos funcionales principales?**
Registro/login de clientes y profesionales; configuración de perfil, servicios y horarios de trabajo del profesional; publicación de disponibilidad; reserva, cancelación y reprogramación de turnos; lista de espera; bloqueo de clientes; recordatorios por email; reseñas; estadísticas para el profesional.

**¿Y los no funcionales?**
Seguridad (auth + control de acceso por rol), usabilidad (sin instalar apps), confiabilidad (sin dobles reservas), configurabilidad (mismo artefacto en dev/prod), mantenibilidad y escalabilidad. Ver [atributos de calidad](../03-arquitectura/atributos-de-calidad.md), con su forma de medirlos.

---

## 3. Arquitectura

**Describí la arquitectura.**
Tres componentes desplegados en contenedores Docker, orquestados con Docker Compose y expuestos tras un **ingress** (reverse proxy) bajo un único dominio:
- **Frontend** SPA en React 19 + TypeScript (Vite).
- **Backend** API REST en Java 21 + Spring Boot.
- **Base de datos** PostgreSQL.

El documento completo está en [arquitectura 4+1](../03-arquitectura/arquitectura-4+1.md) (vistas lógica, de procesos, de desarrollo, física y escenarios) y el modelo de datos en [modelo-de-datos.md](../03-arquitectura/modelo-de-datos.md).

**¿Por qué backend y frontend separados (no un monolito con vistas)?**
Para desacoplar la experiencia del cliente (SPA liviana en el navegador) de la lógica de negocio, permitir evolucionarlos por separado y poder escalar el backend de forma independiente.

**¿El backend es stateless? ¿Por qué importa?**
Sí: no guarda estado de sesión en memoria; cada request se autentica por su JWT. Eso permite **escalar horizontalmente** (varias instancias detrás del ingress) sin sesiones pegajosas.

---

## 4. Diseño y código

**¿Cómo está organizado el código?**
Backend **por feature/dominio** (`appointment`, `professional`, `client`, `service`, `schedule`, `review`, `blockedclient`, `password_reset`, `user`) y, dentro de cada uno, **capas**: Entidad (JPA) → Repository (Spring Data) → Service (negocio) → RestController (HTTP), con DTOs para desacoplar la API del modelo interno. Detalle en [calidad-clean-code-solid.md](../05-ingenieria-y-calidad/calidad-clean-code-solid.md).

**¿Qué principios SOLID aplicaron? Dame un ejemplo.**
- **SRP:** clases con una sola responsabilidad — `AvailabilityService` calcula disponibilidad, `JwtService` maneja tokens, `AppointmentStatusUpdater` actualiza estados, `EmailService` encapsula el proveedor de email.
- **DIP:** inyección de dependencias de Spring; los servicios dependen de interfaces de repositorio, no de implementaciones concretas.
- **OCP/ISP:** repositorios de Spring Data y servicios acotados por responsabilidad.

**¿Qué patrones de diseño usaron y dónde aportan valor?**
- **DTO** — separa el contrato de la API del modelo de dominio.
- **Repository** — abstrae el acceso a datos (Spring Data JPA).
- **Dependency Injection** — desacopla la construcción de objetos (Spring).
- **Strategy/encapsulación de proveedor** — `EmailService` encapsula a Resend; cambiar de proveedor solo afecta esa clase (ADR-12).
- **Publish/Subscribe (eventos de aplicación)** — al cancelarse un turno se publica `AppointmentCancelledEvent` y un listener dispara la promoción de la lista de espera, desacoplando ambos casos de uso.
- **Argument Resolver** — `@CurrentClientId` / `@CurrentProfessionalId` inyectan el id del usuario autenticado en los controladores a partir del JWT, sin repetir la extracción.

**¿Cómo evitan código duplicado, números y strings mágicos?**
Lógica común en utilidades/servicios (`BookingBlockUtils`), configuración externalizada (no hardcodeada) y roles modelados con el enum `UserRole`. *(Mejora pendiente identificada: los estados de turno —`CONFIRMED`, `CANCELLED`, etc.— hoy son `String`; el siguiente paso de calidad es modelarlos como enum/máquina de estados explícita.)*

---

## 5. Desarrollo

**¿Cómo desarrollaron frontend y backend?**
Frontend: React + TypeScript con Vite; routing con `wouter`, estado de servidor con TanStack Query, formularios con TanStack Form y validación de contratos con **Zod**. Backend: Spring Boot con Spring Data JPA, Spring Security, JWT (jjwt), Liquibase (migraciones) y springdoc-openapi (Swagger). Justificación de cada elección en [decisiones-de-diseno.md](../03-arquitectura/decisiones-de-diseno.md).

**¿Cómo se comunican front y back?**
Vía API REST sobre HTTP/JSON. El frontend valida cada respuesta con un esquema Zod, de modo que un cambio de contrato del backend falla de forma temprana y explícita.

**¿Cómo manejan la disponibilidad de turnos?**
Se **calcula on-demand** (`AvailabilityService`) combinando horarios de trabajo (`Schedule`), bloqueos de fecha (`ScheduleBlock`) y turnos ya reservados. No se materializan slots en la base, lo que evita inconsistencias ante cambios de horario o reservas (ADR-11).

**¿Cómo funcionan los recordatorios y la lista de espera?**
Un job `@Scheduled` (`AppointmentReminderScheduler`) corre cada 15 min, busca turnos dentro de las próximas 24 h y envía email a quienes tienen el opt-in (`receivesReminders`); marca `reminderSent` para no duplicar (ADR-10). La lista de espera se promueve por eventos al liberarse un slot.

---

## 6. Testing

**¿Qué estrategia de testing siguieron?**
Dos niveles: **unitarios** de la lógica de negocio (JUnit 5 + Mockito) e **integración** de la capa web (Spring Boot Test + Spring Security Test, con H2 en memoria). En el frontend, tests de componentes con Vitest + Testing Library. Todo corre en el **pipeline de CI** en cada Merge Request. Detalle y casos de prueba en [testing.md](../05-ingenieria-y-calidad/testing.md).

**¿Cuántos tests y qué cobertura tienen?**
255 tests en backend y 28 en frontend, todos en verde. Cobertura backend (JaCoCo): **82,6 % de instrucciones** y **67,6 % de ramas**. Las clases `*BranchesTest` apuntan explícitamente a casos de borde, no solo al camino feliz.

**¿Qué caso difícil testearon?**
La **no superposición / doble reserva**: que reservar un slot ocupado devuelva `SLOT_TAKEN`, y los bordes de capacidad por servicio, turno en el pasado, horario bloqueado y reprogramación a un slot lleno.

**¿Tienen tests end-to-end?**
No automatizados; la validación de los flujos completos se hace de forma manual y se demuestra en vivo en la demo. Es una mejora futura identificada.

---

## 7. Decisiones tomadas (ADRs)

Las decisiones relevantes están registradas como ADRs en [decisiones-de-diseno.md](../03-arquitectura/decisiones-de-diseno.md). Las más preguntables:

- **ADR-01** — clientes y profesionales en **tablas separadas**, con unicidad de email cruzada entre ambas.
- **ADR-05 / ADR-07** — **JWT** para el access token (stateless, corta duración) + **refresh token opaco persistido** en base, para poder invalidar sesiones del lado del servidor.
- **ADR-08** — **login unificado**: un único `AuthService.login` busca en profesionales y luego en clientes (interfaz `UserCredentials`), conciliando tablas separadas con una experiencia de login única.
- **ADR-11** — disponibilidad **calculada on-demand**, sin persistir slots.
- **ADR-12** — `EmailService` que **encapsula el proveedor** (Resend) y centraliza plantillas.

**Para cada decisión, sepan explicar: contexto, alternativas evaluadas y por qué eligieron la que eligieron** (todas las ADRs siguen ese formato).

---

## 8. Manejo de errores

**¿Cómo manejan los errores?**
Documentado en detalle en [manejo-de-errores.md](../05-ingenieria-y-calidad/manejo-de-errores.md). En síntesis:
- La lógica de negocio lanza **excepciones de dominio** (`BadRequestException`, `ItemNotFoundException`, `ConflictException`, `SlotTakenException`, `ForbiddenException`, etc.).
- Un **`@RestControllerAdvice` global** las traduce a un cuerpo uniforme `{ code, message }` con el HTTP status correcto, y captura también errores del framework (validación, JSON mal formado, parámetros faltantes) y un catch-all que loguea el detalle y responde un 500 genérico **sin filtrar internals**.
- El frontend centraliza la interpretación de respuestas (`useHandleResponse`) y muestra los mensajes con `ErrorContainer`.

**¿Qué pasa si dos personas reservan el mismo turno a la vez?**
La verificación de ocupación usa un **bloqueo pesimista** (`findActiveAppointmentsForUpdate`) dentro de una transacción `@Transactional`: la segunda reserva ve a la primera y recibe `SLOT_TAKEN`. Si algo falla a mitad, la transacción hace rollback y no quedan estados parciales.

---

## 9. Seguridad

**¿Cómo autentican y autorizan?**
Autenticación **stateless con JWT** (filtro propio `JwtAuthFilter`). Autorización por **rol** (`CLIENT` / `PROFESSIONAL`) y verificación de **pertenencia del recurso** (p. ej. un cliente solo ve/cancela sus propios turnos → si no, `403 FORBIDDEN`).

**¿Cómo guardan las contraseñas?**
Hasheadas con **BCrypt** (salt automático + costo ajustable), nunca en texto plano (ADR-09).

**¿Cómo está configurada la seguridad HTTP?**
`SecurityConfig`: sesiones **STATELESS**, CSRF deshabilitado (no se usan cookies; el token va en `Authorization`), **CORS restringido y parametrizado** (lista blanca de orígenes vía `CORS_ALLOWED_ORIGINS`, no `*`), lista blanca de endpoints públicos y `anyRequest().denyAll()` para todo lo no contemplado.

**¿Y los secretos (claves, credenciales)?**
No están en el código: se inyectan por variables de entorno/configuración. Ver [gestion-de-secretos.md](../05-ingenieria-y-calidad/gestion-de-secretos.md) y [configuracion-parametrizacion.md](../05-ingenieria-y-calidad/configuracion-parametrizacion.md). La API key de email, por ejemplo, se inyecta por config y el servicio falla al arrancar si falta.

**¿Cómo recuperan contraseña de forma segura?**
Token de reseteo con expiración (`PasswordResetToken`) enviado por email; no se revela si un email existe o no.

---

## 10. Escalabilidad y mantenibilidad

**¿Cómo escala el sistema?**
El backend es **stateless** y está contenerizado → se pueden levantar varias instancias detrás del ingress sin estado de sesión compartido. La base de datos es independiente. El cálculo de disponibilidad está acotado por profesional/servicio/fecha, evitando consultas costosas.

**¿Qué harían si crece mucho la carga?**
Escalar horizontalmente el backend; agregar índices/optimizar las queries de disponibilidad y agenda (ya se trabajó en la optimización de queries en la fase 3 de auditoría); cachear lecturas frecuentes; mover el envío de emails (ya es asíncrono) a una cola si hiciera falta.

**¿Cómo aseguran la mantenibilidad?**
Organización por feature + capas, Clean Code y SOLID, bajo acoplamiento/alta cohesión, tests automatizados en CI, linter en frontend y revisiones de código por Pull Request. Métrica: cobertura (JaCoCo/Vitest) + estado verde en CI.

---

## 11. Forma de trabajo y responsabilidades (Sprints)

**¿Cómo se organizaron como equipo?**
4 sprints con marco ágil (Scrum). En cada sprint rotaron los roles de **Product Owner**, **Scrum Master** y **QA**, además de las tareas de desarrollo (tabla de roles por sprint en [../02-sprints/README.md](../02-sprints/README.md)). Cada integrante debería poder contar **qué rol tuvo en cada sprint y qué construyó**.

**¿Cómo controlaron el versionado y la integración?**
Estrategia de ramas + Pull Requests con revisión de código y pipeline de CI/CD que corre build y tests en cada MR. Detalle en [control-de-versiones.md](../05-ingenieria-y-calidad/control-de-versiones.md).

---

## Apéndice — preguntas "trampa" frecuentes

- **¿Por qué Java/Spring?** Requisito de cátedra para el backend; ecosistema maduro para REST, seguridad, JPA y testing (ADR-02).
- **¿Por qué tablas separadas y no una tabla `users` con rol?** Para mantener el modelo simple y desacoplado por rol; el costo es verificar unicidad de email cruzada (ADR-01).
- **¿Por qué el refresh token no es un JWT?** Para poder **invalidar sesiones** del lado del servidor; un JWT stateless no se puede revocar antes de su expiración (ADR-07).
- **¿Por qué calculan disponibilidad en vez de guardarla?** Para no tener que sincronizar una tabla de slots ante cada cambio de horario/bloqueo/reserva y evitar inconsistencias (ADR-11).
- **¿Qué mejorarían si tuvieran más tiempo?** Modelar los estados de turno como enum/máquina de estados, agregar tests E2E automatizados, y centralizar la paleta de estilos del frontend en design tokens.
