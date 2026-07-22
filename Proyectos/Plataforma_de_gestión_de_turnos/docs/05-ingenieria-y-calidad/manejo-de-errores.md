# Manejo de errores

Documenta la estrategia de manejo de errores de punta a punta: cómo se modelan, se traducen a respuestas HTTP en el backend y cómo el frontend las presenta al usuario.

## Principios

1. **Errores como excepciones de dominio.** La lógica de negocio no devuelve códigos ni `null` para señalar errores: lanza excepciones con un significado claro. La traducción a HTTP es responsabilidad de una única capa.
2. **Traducción centralizada (un solo lugar).** Un `@RestControllerAdvice` global mapea cada tipo de excepción al código HTTP y a un cuerpo de error uniforme. Los controladores quedan limpios, sin `try/catch` repetidos.
3. **Contrato de error uniforme.** Toda respuesta de error tiene la misma forma: `{ "code": ..., "message": ... }`. El `code` es estable y apto para que el frontend reaccione; el `message` está redactado en español para mostrarse al usuario.
4. **No filtrar detalles internos.** Las excepciones inesperadas se loguean en el servidor pero al cliente se le devuelve un mensaje genérico (sin stack traces ni detalles de infraestructura).

## Backend

### Jerarquía de excepciones de dominio

Excepciones propias en `common/exception/`, cada una asociada a un código HTTP:

| Excepción | HTTP | `code` | Uso típico |
|-----------|------|--------|------------|
| `BadRequestException` | 400 | `BAD_REQUEST` | Datos inválidos por reglas de negocio (turno en el pasado, servicio inactivo). |
| `ItemNotFoundException` | 404 | `NOT_FOUND` | Entidad inexistente (turno, servicio, profesional, cliente). |
| `ForbiddenException` | 403 | `FORBIDDEN` | Recurso que no pertenece al usuario autenticado. |
| `UnauthorizedException` | 401 | `UNAUTHORIZED` | Credenciales inválidas / sesión no válida. |
| `ConflictException` | 409 | `CONFLICT` | Conflicto de estado (email duplicado, slot lleno). |
| `SlotTakenException` | 409 | `SLOT_TAKEN` | Slot de turno ya ocupado al reservar. |
| `UnprocessableEntityException` | 422 | `UNPROCESSABLE_ENTITY` | Operación válida en forma pero no aplicable al estado actual (reprogramar un turno cancelado). |

### Traducción centralizada

`config/GlobalControllerExceptionHandler` (`@RestControllerAdvice`) concentra el mapeo. Además de las excepciones de dominio, captura los errores que el propio framework genera, para que **nunca** se filtre una respuesta de error con formato distinto:

- `MethodArgumentNotValidException` → 400 `VALIDATION_ERROR` (falla la validación de un DTO con Bean Validation; se devuelve el primer mensaje de validación).
- `HttpMessageNotReadableException` → 400 (body JSON ausente o mal formado).
- `MissingServletRequestParameterException` → 400 (falta un query param requerido).
- `MethodArgumentTypeMismatchException` → 400 (tipo de parámetro inválido).
- `AccessDeniedException` (Spring Security) → 403.
- `Exception` (catch-all) → 500 `INTERNAL_ERROR`: se **loguea** el detalle real con `log.error(...)` y se responde un mensaje genérico al cliente.

Todas las respuestas comparten el record `ErrorResponse(String code, String message)`.

### Validación de entrada

- **Sintáctica / de formato:** anotaciones de Bean Validation (`@NotNull`, `@Email`, `@Size`, etc.) sobre los DTOs, activadas con `@Valid` en los controladores. Las violaciones las captura el handler global como `VALIDATION_ERROR`.
- **De negocio:** se valida dentro de los servicios y se lanza la excepción de dominio correspondiente (p. ej. en `BookingService`: turno en el pasado → `BadRequestException("SLOT_IN_PAST")`, slot sin capacidad → `SlotTakenException`).

### Consistencia transaccional

Las operaciones que modifican varias entidades o publican eventos están anotadas con `@Transactional` (p. ej. `createBooking`, `cancelBooking`, `rescheduleBooking`, `markAbsent`). Si se lanza una excepción a mitad de la operación, la transacción hace **rollback** y no quedan estados parciales en la base.

### Concurrencia (doble reserva)

El caso crítico de correctitud es evitar que dos clientes reserven el mismo slot en simultáneo. La verificación de ocupación se hace con `findActiveAppointmentsForUpdate(...)` (bloqueo pesimista sobre las filas candidatas) dentro de la transacción, de modo que la segunda reserva concurrente vea la primera y reciba `SLOT_TAKEN` en lugar de generar una doble reserva.

## Frontend

### Traducción de respuestas HTTP

El frontend centraliza el manejo de respuestas en `useHandleResponse` (`services/TokenContext`), que usan todos los servicios (`fetch` + TanStack Query). Allí se decide, según el status:

- **2xx:** se parsea el body con el esquema **Zod** correspondiente (validación del contrato también del lado del cliente; si el backend cambiara la forma del JSON, falla de manera explícita y temprana).
- **401:** dispara el flujo de refresh del token / cierre de sesión.
- **Otros errores:** se propaga el `message` del cuerpo `ErrorResponse` para mostrarlo al usuario.

### Presentación al usuario

- Estados de carga/error expuestos por TanStack Query (`isLoading`, `isError`, `isPending`) — la UI muestra spinners y estados vacíos en lugar de romperse.
- Errores de formulario y de operación renderizados con el componente `ErrorContainer`, con estilo visual diferenciado (rojo) y mensajes en español.
- Acciones con efecto (reservar, cancelar, reprogramar) deshabilitan el botón mientras la mutación está en curso (`disabled={isLoading}`) para evitar envíos duplicados.

## Resumen de flujo

```
Usuario → UI (botón) → servicio fetch (frontend)
   → API REST (controlador) → servicio de negocio
        └─ lanza excepción de dominio
   → @RestControllerAdvice traduce a { code, message } + HTTP status
→ useHandleResponse interpreta el status
→ ErrorContainer muestra el message al usuario
```

Este esquema mantiene la lógica de negocio desacoplada del protocolo HTTP, garantiza un contrato de error uniforme y evita exponer detalles internos.
