# Uso de IA durante el proyecto

> La cátedra **fomenta** el uso de IA y quiere entender cómo se incorporó al proceso de ingeniería. No busca que se oculte. Este documento responde, punto por punto, las preguntas planteadas.
>

## Herramientas utilizadas

El uso de IA se concentró fundamentalmente en la **etapa de desarrollo de código**. En las
etapas previas (investigación, planteamiento del problema y entendimiento del usuario) el
trabajo fue principalmente manual: se realizó de forma práctica, relevando y deduciendo a
partir de la información que nos brindaban las personas encuestadas y entrevistadas.

Recién una vez que tuvimos una **base, un plan y la investigación** de lo que queríamos
resolver con la plataforma, incorporamos la IA como asistente para construir el software.

- **Asistentes de código:** utilizados para construir y revisar el código del proyecto.

## Agentes / asistentes empleados

**Claude Code (modelo Opus 4.7):** principal asistente utilizado, siempre en tareas
  relacionadas con el código: generación, revisión del mismo y detección de errores o
  problemas que iban surgiendo durante el desarrollo.

## Integración al proceso de trabajo

El flujo de trabajo combinó diseño humano y asistencia de IA:

1. **Diseño en equipo:** primero discutimos y diseñamos en grupo cómo queríamos que fuera la
   plataforma, definiendo su estructura y su flujo de uso.
2. **Construcción asistida:** luego le pasamos a la IA todas las instrucciones para que
   fuera construyendo la aplicación a partir de ese diseño.
3. **Iteración y ajuste:** a medida que la IA generaba el código, íbamos revisando y
   corrigiendo aquello que no nos convencía o que queríamos resolver de otra manera.

De esta forma, la IA se integró principalmente en las etapas de **codificación, testing y
revisión de código**, mientras que el **relevamiento, el diseño de la solución y la
definición de los endpoints** se mantuvieron como trabajo manual del equipo.

## Vibe Coding

Gran parte del desarrollo se realizó de forma exploratoria, guiando a la IA con
lenguaje natural: a partir del diseño acordado en grupo le dábamos instrucciones y ella
construía la aplicación, mientras nosotros ajustábamos sobre la marcha lo que no nos
gustaba o queríamos diferente. Esto nos permitió avanzar rápido en la construcción, siempre
sobre la base de un plan previo definido por el equipo.

## Specification Driven Development (SDD)

Trabajamos con un enfoque cercano al SDD en un punto clave: **los endpoints y el flujo de
uso de la aplicación se especificaron primero de forma manual** (diseñados en grupo) y
recién después se implementaron con asistencia de IA. Es decir, la especificación de la
interfaz/flujo precedió a la generación del código, que se construyó para cumplir con esa
definición previa.

## Desarrollo del frontend

El frontend fue **construido con asistencia de IA** a partir del diseño y el flujo de uso
que habíamos definido en equipo. Le indicábamos cómo queríamos que fueran las pantallas y
la interacción, y luego íbamos ajustando los detalles de la interfaz que no nos
convencían.

## Desarrollo del backend

El backend también fue **construido con asistencia de IA**, sobre la base de los
**endpoints definidos manualmente por el grupo**. Le indicamos explícitamente a la IA que
siguiera los principios **SOLID** y de **Clean Code**, de modo de obtener un código
ordenado, entendible y mantenible por los desarrolladores.

## Tareas asistidas por IA vs. tareas manuales
Resumen de qué se hizo con asistencia de IA y qué se realizó manualmente.

| Tarea / área | Asistida por IA | Manual | Observaciones |
|--------------|:---------------:|:------:|---------------|
| Relevamiento / discovery | | X | Trabajo práctico de campo: deducción a partir de encuestas y entrevistas. |
| Entendimiento del usuario | | X | Realizado de forma natural por el equipo. |
| Diseño de la solución y flujo de uso | | X | Discutido y definido en grupo antes de codificar. |
| Definición de endpoints | | X | Diseñados manualmente por el equipo. |
| Endpoints backend (implementación) | X | | Implementados por la IA sobre los endpoints ya definidos. |
| Lógica de negocio | X | | Generada por IA siguiendo SOLID y Clean Code, con revisión del equipo. |
| Frontend (componentes/UI) | X | | Construido por IA a partir del diseño acordado, con ajustes manuales. |
| Testing | X | | Tests generados con asistencia de IA. |
| Revisión de código / detección de errores | X | | Claude Code (Opus 4.7) usado para revisar y encontrar problemas. |
