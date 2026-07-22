# Calidad: Clean Code y SOLID

Este documento describe las buenas prácticas de ingeniería aplicadas en el código y cómo se evitaron los problemas frecuentes que evalúa la cátedra.

## Organización y separación de responsabilidades

- **Backend organizado por feature/dominio** (`appointment`, `professional`, `client`, `service`, `schedule`, `review`, etc.) y, dentro de cada uno, **separación en capas**:
  - **Modelo / Entidad** — representación del dominio (JPA).
  - **Repository** — acceso a datos (Spring Data JPA).
  - **Service** — lógica de negocio.
  - **RestController** — exposición HTTP (API).
  - **DTOs** — objetos de transferencia, desacoplando la API del modelo interno.
- Esto da **baja cohesión entre módulos y alta cohesión dentro de cada uno**, y mantiene la **lógica de negocio separada de la presentación (controllers) y de la infraestructura**.

## Principios SOLID

- **S — Responsabilidad única:** cada clase tiene un rol claro (p. ej. `AvailabilityService` calcula disponibilidad, `AppointmentStatusUpdater` gestiona transiciones de estado, `JwtService` maneja tokens).
- **O / L / I:** uso de interfaces de repositorio de Spring Data; servicios acotados por responsabilidad.
- **D — Inversión de dependencias:** inyección de dependencias de Spring (los servicios dependen de abstracciones de repositorio, no de implementaciones concretas).

## Manejo de errores

- **Excepciones de negocio propias** en `common/exception/` (`BadRequestException`, `ConflictException`, `ForbiddenException`,`ItemNotFoundException`, `SlotTakenException`, `UnauthorizedException`).
- **Manejo centralizado** con `GlobalControllerExceptionHandler`, que traduce cada excepción al código HTTP correspondiente (400/401/403/404/409). Evita repetir manejo de errores en cada controlador.

## Problemas frecuentes evitados

| Problema a evitar | Cómo se aborda |
|-------------------|----------------|
| Código duplicado | Lógica compartida en servicios/utilidades (`BookingBlockUtils`), DTOs reutilizables. |
| Números y strings mágicos | Roles modelados con el enum `UserRole`; configuración externalizada. *(Mejora pendiente: los estados de turno son hoy `String` literales; se planea modelarlos como enum/máquina de estados.)* |
| Datos hardcodeados | Parametrización vía `.env` / `application.properties` (ver [configuracion-parametrizacion.md](configuracion-parametrizacion.md)). |
| Métodos/clases gigantes | Separación por responsabilidad; servicios acotados por caso de uso. |
| Lógica de negocio mezclada con presentación/infraestructura | Capas separadas (controller ≠ service ≠ repository). |
| Dependencias innecesarias entre módulos | Organización por feature; comunicación a través de servicios y DTOs. |

## Convenciones de código

- **Nombres claros y consistentes** para clases, métodos y variables.
- **Consistencia de idioma:** el código y los identificadores siguen la convención del template (inglés); la documentación está en español.
- **Linter en frontend:** ESLint + reglas de React Hooks; ordenamiento de imports con prettier-plugin-sort-imports.

---
