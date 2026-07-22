# Requerimientos

Requerimientos derivados del relevamiento y de la propuesta de solución. Los marcados como *(MVP)* corresponden a los criterios de producto de la primera versión; los marcados como *(futuro)* fueron planteados como evolución del producto.

## Requerimientos funcionales

### Gestión de usuarios y autenticación
- RF-01. Un cliente puede registrarse con email y contraseña.
- RF-02. Un profesional puede registrarse con email y contraseña.
- RF-03. El sistema autentica usuarios y emite tokens de acceso y de refresco (JWT).
- RF-04. Un profesional puede completar/editar su perfil (especialidad, dirección, barrio, ciudad).
- RF-05. Recuperación de contraseña vía email.

### Configuración del negocio (profesional)
- RF-06. Definir servicios con nombre, duración, precio, cupo máximo y estado activo/inactivo. *(MVP)*
- RF-07. Definir horarios laborales semanales por día y rango horario. *(MVP)*
- RF-08. Configurar el intervalo entre turnos. *(MVP)*
- RF-09. Bloquear días/fechas específicas (vacaciones, feriados). 
- RF-10. Gestionar empleados y los servicios que cada uno puede ofrecer.
- RF-11. Configurar la política de cancelación (horas mínimas, bloqueo por inasistencia, límite de ausencias).

### Reserva y gestión de turnos
- RF-12. Un cliente puede ver el listado de profesionales y el perfil de cada uno. *(MVP)*
- RF-13. Un cliente puede consultar la disponibilidad de un servicio en una fecha. *(MVP)*
- RF-14. Un cliente puede reservar un turno. *(MVP)*
- RF-15. El sistema evita la superposición de turnos (doble reserva). *(MVP)*
- RF-16. Un cliente puede ver sus turnos (próximos, pasados, cancelados). *(MVP)*
- RF-17. Un cliente puede cancelar/reprogramar un turno. *(MVP)*
- RF-18. Un profesional puede ver su agenda semanal.
- RF-19. Un profesional puede marcar el estado de un turno (confirmado, completado, ausente, cancelado).
- RF-20. Lista de espera automática para reasignar turnos liberados. *(MVP)*
- RF-21. Bloqueo de clientes por inasistencias reiteradas.

### Reseñas y métricas
- RF-22. Un cliente puede dejar una reseña (puntaje 1–5 y comentario) sobre un turno completado.
- RF-23. Un profesional puede ver estadísticas: resumen del período, turnos por día, distribución por servicio y clientes frecuentes.

### Comunicación
- RF-24. Recordatorios automáticos por email. 
- RF-25. Notificaciones automáticas al profesional.
- RF-26. Integración con WhatsApp. *(futuro)*
- RF-27. Cobro de señas. *(futuro)*

## Requerimientos no funcionales

- RNF-01. **Accesibilidad para el cliente final:** reservar sin necesidad de descargar una app, minimizando la fricción (la barrera de adopción detectada provino mayormente de los clientes, no del profesional).
- RNF-02. **Seguridad:** autenticación basada en JWT; contraseñas hasheadas; control de acceso por rol (CLIENT / PROFESSIONAL).
- RNF-03. **Configurabilidad:** el producto debe ser lo más genérico posible para que cada profesional personalice su negocio (servicios, horarios, empleados).
- RNF-04. **Mantenibilidad y calidad de código:** ver
  [../05-ingenieria-y-calidad/calidad-clean-code-solid.md](../05-ingenieria-y-calidad/calidad-clean-code-solid.md).
- RNF-05. **Parametrización:** todo valor que varíe entre ambientes debe ser configurable (ver [../05-ingenieria-y-calidad/configuracion-parametrizacion.md](../05-ingenieria-y-calidad/configuracion-parametrizacion.md)).
- RNF-06. **Despliegue en la nube** mediante contenedores Docker y pipeline de CI/CD.

> Los atributos de calidad y su forma de medición están en [../03-arquitectura/atributos-de-calidad.md](../03-arquitectura/atributos-de-calidad.md).

---
