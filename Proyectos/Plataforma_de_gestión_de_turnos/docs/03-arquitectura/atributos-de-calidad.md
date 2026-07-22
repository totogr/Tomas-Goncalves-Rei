# Atributos de calidad

Listado de atributos de calidad seleccionados para el sistema, con su **forma de medirlos y evaluarlos**.

| # | Atributo | Cómo se aborda en el sistema | Forma de medir / evaluar |
|---|----------|------------------------------|--------------------------|
| 1 | **Seguridad** | Autenticación JWT (access + refresh), contraseñas hasheadas, control de acceso por rol (CLIENT/PROFESSIONAL), validación de inputs. | Revisión de que todo endpoint protegido exige token válido; tests de seguridad (403/401); ausencia de credenciales hardcodeadas en código (ver parametrización). |
| 2 | **Mantenibilidad** | Organización por feature + capas, Clean Code, SOLID, bajo acoplamiento. | Cobertura de tests (JaCoCo backend, Vitest frontend); análisis de linter (ESLint); revisión de PRs. |
| 3 | **Confiabilidad / Correctitud** | Validación de superposición de turnos, máquina de estados de turnos, manejo centralizado de excepciones. | Suite de tests unitarios y de integración en verde en CI; casos de borde cubiertos (ver [../05-ingenieria-y-calidad/testing.md](../05-ingenieria-y-calidad/testing.md)). |
| 4 | **Usabilidad** | Reserva sin instalar apps, minimizando la fricción del cliente final (hallazgo del relevamiento). | Recorrido de los flujos principales en la demo; feedback de usuarios de prueba. |
| 5 | **Configurabilidad / Portabilidad** | Toda variable que cambia entre ambientes está parametrizada (`.env`, `.env.prod`, `application.properties`). | Despliegue del mismo artefacto en dev y prod cambiando solo configuración; revisión de que no hay valores de ambiente en el código. |
| 6 | **Escalabilidad** | Backend stateless (JWT) y contenerizado; base de datos independiente. | Posibilidad de escalar horizontalmente el backend sin estado de sesión en memoria. |
| 7 | **Disponibilidad** | Despliegue contenerizado con ingress y CI/CD automatizado. | Healthcheck (Spring Actuator, `management.server.port=8081`); estado del ambiente desplegado. |
| 8 | **Performance** | Cálculo de disponibilidad acotado por servicio/fecha; consultas vía JPA. | Tiempo de respuesta de los endpoints críticos (disponibilidad, agenda) en escenarios representativos. |

---
