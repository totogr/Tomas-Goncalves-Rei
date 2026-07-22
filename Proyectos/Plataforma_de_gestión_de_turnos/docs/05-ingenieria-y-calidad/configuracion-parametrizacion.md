# Configuración y parametrización

Todo valor que razonablemente puede variar entre ambientes o despliegues está **parametrizado** fuera del código fuente.

## Mecanismos de configuración

| Archivo | Ámbito | Contenido |
|---------|--------|-----------|
| `.env` | Desarrollo | Puertos, URL externa, ruta de volumen, credenciales de DB, claves de servicios. |
| `.env.prod` | Producción | Mismos parámetros con valores del ambiente desplegado en la nube. |
| `backend/src/main/resources/application.properties` | Backend | Configuración de Spring (datasource, JPA, Liquibase, JWT, email, puertos). |
| `docker-compose.yml` | Orquestación | Toma variables de `.env` para configurar los contenedores. |

## Parámetros externalizados (ejemplos)

- **URLs de servicios / frontend:** `EXTERNAL_URL`, `FRONTEND_URL`, `application.frontend.url`.
- **Base de datos:** `DB_PORT`, `DB_APP_PASSWORD`, `spring.datasource.url/username/password`.
- **Puertos:** `INGRESS_PORT`, `DB_PORT`, `server.port`, `management.server.port`.
- **Credenciales / claves:** `RESEND_API_KEY` (servicio de email), `jwt.access.secret`.
- **Límites y umbrales de negocio:** expiración de tokens (`jwt.access.expiration`, `jwt.refresh.expiration`), expiración del token de reseteo de contraseña (`RESET_TOKEN_EXPIRY_MIN` → `application.security.resetToken.expiration-minutes`).
- **Email:** `resend.api-key`, `resend.from`.
- **Volúmenes:** `VOLUME_DIR`.

El backend usa **placeholders con valor por defecto** (p. ej. `resend.api-key=${RESEND_API_KEY:}`, `application.frontend.url=${FRONTEND_URL:http://localhost:5173}`), de modo que el mismo artefacto funciona en distintos ambientes cambiando solo variables de entorno.
