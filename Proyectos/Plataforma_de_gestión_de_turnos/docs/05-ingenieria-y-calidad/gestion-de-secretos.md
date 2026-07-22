# Gestión de secretos y credenciales

> **Resumen:** en este proyecto las credenciales (archivos `.env`, `.env.prod`, el secreto de firma JWT y las API keys de Resend) se versionan **deliberadamente** dentro del repositorio. Este documento explica esa decisión, su alcance y qué se haría distinto en un entorno productivo real.

## Contexto

El sistema de turnos es un Trabajo Práctico de la materia **Ingeniería de Software I (FIUBA)**. El despliegue se realiza de forma automática: lo que se mergea a la rama `master` se publica en el entorno provisto por la cátedra (`https://grupo-09.tp1.ingsoft1.fiuba.ar`) a través del pipeline de GitLab CI. No existe un sistema de gestión de secretos (Vault, AWS Secrets Manager, GitLab CI/CD variables protegidas, etc.) montado para el TP, y la infraestructura es efímera y de uso exclusivamente académico.

Por ese motivo, y para que cualquier integrante o corrector pueda **clonar el repositorio y levantar el sistema completo sin pasos de configuración manual**, se decidió versionar los valores de configuración sensibles junto al código.

## Qué credenciales están versionadas y por qué

| Archivo / propiedad | Contenido | Motivo de versionarlo |
|---|---|---|
| `.env` | Puertos, password de BD de desarrollo, API key de Resend (dev), URL de frontend | Levantar el stack local con `docker-compose` sin configuración previa |
| `.env.prod` | Equivalente para el entorno de la cátedra | Que el deploy automático desde `master` tenga todo lo necesario |
| `backend/.../application.properties` → `jwt.access.secret` | Secreto de firma de los JWT | Reproducibilidad de tokens entre entornos de prueba |
| `resend.api-key` (vía `.env`) | API key del proveedor de emails (Resend) | Que las notificaciones por email funcionen end-to-end en la demo |

## Alcance y riesgo asumido

La decisión es **consciente y acotada al ámbito académico**:

- **No hay datos personales reales ni sensibles**: las cuentas y turnos son de prueba.
- El entorno productivo es **provisto y aislado por la cátedra**, no expone activos de valor.
- Las API keys de Resend están limitadas al dominio de pruebas `onboarding@resend.dev` y a cuotas gratuitas; su eventual abuso no genera costos ni impacto sobre terceros.
- El secreto JWT solo protege sesiones de usuarios de prueba dentro del mismo entorno efímero.

El riesgo (que un tercero con acceso al repositorio pudiera firmar tokens o usar la API de Resend) se considera *aceptable dado el contexto*, ya que no hay información ni recursos críticos detrás de esas credenciales.

## Qué se haría distinto en un entorno productivo real

En un proyecto productivo, **ninguna** de estas credenciales se versionaría. El enfoque correcto sería:

1. **Secretos fuera del repositorio**: `.env*` listados en `.gitignore`, provistos en tiempo de despliegue mediante variables de entorno o un gestor de secretos (Vault, AWS/GCP Secrets Manager, GitLab CI/CD *masked & protected variables*).
2. **Secreto JWT robusto y rotable**: generado aleatoriamente (≥ 256 bits), inyectado por variable de entorno y rotado periódicamente, nunca con un valor por defecto débil.
3. **API keys con rotación y mínimo privilegio**, almacenadas en el gestor de secretos y revocables ante una filtración.
4. **Escaneo de secretos en CI** (p. ej. `gitleaks`/`trufflehog`) para impedir commits accidentales de credenciales.

> Si este proyecto evolucionara más allá del ámbito de la materia, la primera tarea de *hardening* sería migrar a este esquema y **rotar todas las credenciales** que hoy figuran en el historial de git, asumiéndolas comprometidas.
