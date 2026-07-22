# Sprint 2

## Objetivos
- Consolidar el acceso a la plataforma, garantizando que un usuario pueda registrarse e iniciar sesión de forma fluida.
- Permitir a los usuarios con perfil "Cliente" visualizar el listado de profesionales disponibles en la plataforma.
- Habilitar la exploración detallada de los servicios ofrecidos y los horarios de disponibilidad de cada profesional.
- Implementar el flujo básico completo de reserva, de principio a fin, para que el cliente pueda agendar un turno exitosamente.
- Mantener el alcance enfocado en la funcionalidad principal, excluyendo explícitamente en esta fase el sistema de envío de notificaciones.

## Roles asignados
| Rol | Responsable |
|-----|-------------|
| Product Owner | Tomás Goncalves Rei |
| Scrum Master | Ignacio Mahmoud Abalos |
| QA | Camila Miranda Vandevalle |
| Desarrollo | Valentín Abaca - José Evaristo Tissera - Nicolás Agustín Rossi |

## Entregables
- Modelo de base de datos definido (ver [../../03-arquitectura/modelo-de-datos.md](../../03-arquitectura/modelo-de-datos.md)).
- Endpoints iniciales definidos con sus parámetros.

## Resultados obtenidos
- Flujo de reserva operativo: Se alcanzó el objetivo principal del sprint. El cliente ahora puede recorrer todo el circuito básico: ver profesionales, consultar sus servicios y horarios, y concretar la reserva de un turno.

- Interfaz de usuario (UI) preliminar: Si bien el flujo es completamente funcional a nivel de lógica (backend), la interfaz gráfica desarrollada es de carácter básico o "maqueta". Se priorizó la funcionalidad por sobre la estética, por lo que el diseño y la experiencia de usuario (UX) requerirán un trabajo de refinamiento en próximos sprints.

- Identificación de deuda técnica: Se cumplieron los objetivos, pero se detectaron algunos bugs remanentes y comportamientos no deseados en el frontend durante la integración. Estos fueron documentados y cargados al backlog del producto para su pronta resolución.

## Evidencias generadas
- Revisiones del PO / SM / QA: ver carpeta [evidencias/](evidencias/).

---
