# 📝 Evaluaciones anteriores resueltas — GDSI

> La **Evaluación I del 27/10/2025** (misma cátedra: Gabardini y Fontdevila), transcripta y resuelta con respuestas modelo.
> La consigna sale de fotos y de un examen resuelto que circulan entre alumnos ([fuentes en Material extra](../Material%20extra/README.md)). Acá está solo el **texto de la consigna**, sin los nombres de quienes la rindieron.
>
> Para estudiar la teoría: [Resumen para la Evaluación 1](Resumen%20para%20la%20Evaluaci%C3%B3n%201.md) · [Resumen simple](Resumen%20simple.md).

---

## Cómo es la evaluación

| Pregunta | Tipo | Tema | Puntos |
| --- | --- | --- | --- |
| 1 | Múltiple choice | *Artful Making*: las cuatro cualidades | 1 |
| 2 | Múltiple choice | Example Mapping: para qué están las preguntas (tarjetas rojas) | 1 |
| 3 | Desarrollo corto | Review vs. Retrospective, con un ejemplo de cada una | 2 |
| 4 | Caso | Ciclo de producto de Jeff Patton (Sense, Focus, Discovery, Delivery) + actividades ligadas a riesgos | 2 |
| 5 | Caso | Explicar qué es un experimento con un ejemplo del caso | 1 |
| 6 | Caso | Armar y **dibujar** un story map con dos roles | 3 |
| | | | **10** |

**Lo que se desprende:**
- **El 60% de la nota sale del caso:** hay que **aplicar** las herramientas a una situación concreta, no solo definirlas. La consigna pide "**explicitá cualquier supuesto que tomes**".
- **El story map vale 3 puntos:** practicalo dibujado a mano.
- **Los riesgos aparecen dentro de otras preguntas.** La 4b pide "vincular cada actividad a un **riesgo potencial** (de negocio, atractivo o técnico)".
- **El tiempo es justo:** un alumno escribió en el examen que sintió que el tiempo era poco. Llevá las estructuras de respuesta ya pensadas.
- **No hubo preguntas de métricas, riesgos como tema propio, Scrum en detalle ni BDD** más allá de la tarjeta roja. Igual entran: en 2026 el cronograma dice que la evaluación cubre **las clases 2 a 8**, que incluyen mejora y métricas.

---

## La consigna

**1.** En el marco de *Artful Making*, los pilares incluyen colaboración, release, ensemble y play. ¿Cuál es el propósito principal de "ensemble"? (1 punto)

- a. Facilitar la liberación rápida de versiones para feedback.
- b. Fomentar el trabajo en equipo como un conjunto unificado.
- c. Reducir los costos de iteración mediante exploración creativa.
- d. Enfocarse en el juego como mecanismo para innovación sin estructura.

**2.** En el contexto de Example Mapping, ¿cuál es el rol de las "preguntas" en el mapa? (1 punto)

- a. Servir como ejemplos concretos para validar reglas.
- b. Definir las reglas estrictas que guían el comportamiento del sistema.
- c. Identificar incertidumbres o aspectos no claros en la historia o reglas, a clarificar.
- d. Representar la historia principal que el usuario quiere lograr.

**3.** Describí brevemente cómo se diferencian las reuniones de Review y Retrospective en Scrum, y da un ejemplo de qué se podría discutir en cada una. (2 puntos)

**Situación para las preguntas 4 a 6** (*"Explicitá cualquier supuesto que tomes"*):

> Estás desarrollando la aplicación **MidFood**, cuyo objetivo es facilitar la comunicación entre servicios de pedidos de comida (Rappi, Pedidos Ya, Uber Eats, etc.) y las aplicaciones que usa cada restaurante para gestionar pedidos y facturación (CRM).
>
> Buscamos que sea altamente automatizada para disminuir costos de personal. Sin embargo, si se pierden pedidos, los servicios de pedidos penalizan fuertemente a los restaurantes. Por lo tanto MidFood debe ser robusta: habrá alertas y personas controlando que los pedidos se tomen correctamente y que se carguen en el CRM. Habrá funcionalidad para resolver contingencias, tanto cargando pedidos en MidFood desde servicios de comida como cargando a mano en el CRM con información de MidFood.
>
> Considerá que algunos servicios de pedidos ofrecerán API, otros no y tendremos que hacer web scraping. Igualmente, algunas aplicaciones de restaurantes tendrán API, otras tendremos que hacer RPA.
>
> - *Web scraping:* proceso de usar bots para extraer contenido y datos de un sitio web, como código HTML y datos de bases de datos.
> - *RPA:* tecnología que usa robots de software para automatizar tareas repetitivas imitando acciones humanas en interfaces de usuario.
>
> El enunciado trae un diagrama:
> - En el centro está **MidFood**.
> - A la izquierda, MidFood se conecta con el **CRM** del restaurante a través de un **Adaptador CRM–MidFood**.
> - A la derecha, MidFood se conecta con dos servicios de pedidos (**Velocci** y **ComidaYA**), cada uno vía **API / scraping**.
> - Los dos lados están marcados como zona de **contingencia**.
> - Hay dos roles: **Gestor de servicio** y **Cargador de pedidos**.

**4.** Estás en un equipo desarrollando MidFood, y han identificado una oportunidad de mercado. Discutí los próximos pasos basándote en la mirada de producto de Jeff Patton (2 puntos):
- a. Describí brevemente el ciclo de mejora de producto (Sense, Focus, Discovery, Delivery) aplicado a este escenario.
- b. Proponé al menos una actividad específica para Sense y Focus, explicando cómo ayuda a mitigar riesgos o avanzar en el desarrollo.

*Importante: basate en los temas vistos en la materia. En el punto b, asegurate de vincular cada actividad a un riesgo potencial (por ejemplo, de negocio, atractivo o técnico).*

**5.** Experimentos: explicá el concepto a una persona que no lo conoce, usando un ejemplo de MidFood. (1 punto)

**6.** Creá un story map básico para MidFood. Considerá los roles de Gestor y Cargador de pedidos. (3 puntos)
- b. Definí las columnas principales (objetivos o actividades secuenciales del usuario).
- c. Para cada columna, proponé al menos 2–3 historias de usuario relevantes, priorizándolas por valor (por ejemplo, MVP vs. futuras iteraciones).

*Importante: mantené la respuesta estructurada y concisa; dibujá el story map.*

---

## Respuestas modelo

### 1 → **b**

*Ensemble* es "la cualidad del trabajo de un grupo dedicado a colaborar, en el que cada miembro **cede soberanía sobre su trabajo** y así crean algo que ninguno podría haber hecho solo: **un todo mayor que la suma de sus partes**" (Austin y Devin, cap. 1).

Por qué las otras opciones están mal:
- **a** confunde *release* (un modo de control) con "liberar versiones".
- **c** describe la iteración barata, la base de todo el libro, pero no *ensemble*.
- **d** malinterpreta *play*, que no es "juego sin estructura" sino la interacción con el público y entre el grupo.

### 2 → **c**

Las tarjetas **rojas** registran lo que **nadie pudo responder** en la conversación. Se anotan y se sigue, en vez de trabar la reunión. Alguien se hace responsable de responderlas antes de que la historia entre al sprint.

Las otras opciones describen otras tarjetas:
- **a** = ejemplos (verdes);
- **b** = reglas (azules);
- **d** = historia (amarilla).

### 3 · Review vs. Retrospective

| | **Sprint Review** | **Sprint Retrospective** |
| --- | --- | --- |
| Qué se inspecciona | **El producto**: el incremento del sprint (el *qué*) | **El proceso**: cómo trabajó el equipo (el *cómo*) |
| Quiénes | Equipo Scrum + **stakeholders** | Solo el **equipo Scrum** |
| Para qué | Recibir feedback y **adaptar el Product Backlog** | Detectar causas y **comprometer mejoras** para el próximo sprint |
| Ejemplo (MidFood) | Se muestra la recepción automática de pedidos de ComidaYA. El gestor de un restaurante pide que las alertas lleguen también por WhatsApp → se agrega al backlog | Dos historias quedaron sin terminar porque el ambiente de prueba del scraping se caía. Se analiza con 5 porqués y el equipo se compromete a automatizar su recreación |

> ⚠️ **El corrector marcó un error típico.** El alumno escribió que los stakeholders "son quienes usan el producto" y le anotaron "**no necesariamente**". Stakeholder = **interesado** (cliente, sponsor, gerencia, usuarios…), no solo usuario.

### 4 · Ciclo de producto de Patton aplicado a MidFood

**a. El ciclo** (los equipos de producto lo recorren continuamente):

1. **Sense (percibir/escuchar):** mirar el contexto para detectar oportunidades:
   - qué problemas tienen los restaurantes (pedidos perdidos, penalizaciones, gente cargando pedidos a mano);
   - qué ofrecen los servicios (quién tiene API y quién no);
   - qué hace la competencia.
2. **Focus (enfocar):** elegir **a qué oportunidad apuntar** y qué **outcome** buscamos.
   - Ejemplo de outcome: "los restaurantes no pierden pedidos y reducen la carga manual".
   - Se arma la visión (Product Vision Board) y la estrategia: con qué servicios y CRMs arrancar.
3. **Discovery (descubrir):** experimentar con ideas de solución para descartar las malas antes de construir: prototipos, Wizard of Oz, example maps de las historias.
4. **Delivery (entregar):** construir en incrementos lo que se validó. Por ejemplo, primero la integración con un servicio con API y un CRM con API. Después se vuelve a **Sense** con lo que se observa en uso.

**b. Actividades ligadas a un riesgo** (el corrector pidió **especificar el riesgo**, no nombrarlo en general):

| Etapa | Actividad | Riesgo que ataca |
| --- | --- | --- |
| **Sense** | **Observación contextual** en 3 restaurantes durante el pico de la noche: cómo reciben los pedidos de cada app, cuántos se pierden y cuánto tiempo pasan cargándolos en el CRM | **Atractivo/valor:** ¿el problema de los pedidos perdidos es real y suficientemente doloroso como para pagar por MidFood? |
| **Sense** | **Entrevistas** (*Mom Test*) a dueños: "¿cuándo fue la última vez que los penalizaron? ¿cuánto les costó?" | **Negocio:** si las penalizaciones son chicas, el ahorro no justifica el precio |
| **Focus** | **Product Vision Board** + elegir el segmento inicial (restaurantes con un CRM que tenga API y los dos servicios más usados) | **Negocio:** dispersarse en todas las integraciones a la vez. **Técnico:** empezar por los casos sin API (scraping/RPA), que son los más frágiles |
| **Focus** | Inventario de integraciones: qué servicios tienen API y cuáles requieren scraping o RPA | **Técnico:** el scraping se rompe cuando el sitio cambia. Saberlo temprano define dónde poner alertas y contingencias |

### 5 · Qué es un experimento, explicado con MidFood

> **Respuesta modelo:**
> - **Qué es:** un experimento es una **prueba chica y barata** para averiguar si una idea es cierta **antes** de invertir en construirla.
> - **Cómo se arma:** se parte de un supuesto riesgoso, se escribe como hipótesis con un **criterio de éxito decidido de antemano**, se prueba con usuarios reales y se mira **qué hacen**, no solo qué opinan.
> - **Ejemplo con MidFood:**
>   - **Supuesto:** "los restaurantes van a pagar para no cargar pedidos a mano".
>   - **Experimento (Wizard of Oz):** antes de programar las integraciones, durante dos semanas una persona del equipo copia **a mano** los pedidos de Pedidos Ya al CRM de 5 restaurantes. Para ellos, el servicio parece automático.
>   - **Medición:** pedidos perdidos antes y durante la prueba, y cuántos restaurantes aceptan **pagar** para seguir.
>   - **Criterio de éxito:** si al menos 3 de 5 aceptan pagar, la idea tiene valor y avanzamos.
>   - **Qué gana:** se aprende mucho gastando muy poco.

### 6 · Story map de MidFood

**Supuestos explícitos:**
- MidFood **no factura**: la facturación queda en el CRM del restaurante (en el examen resuelto que circula, el corrector anotó al margen "MidFood no factura").
- El **Gestor de servicio** configura y supervisa.
- El **Cargador de pedidos** resuelve las contingencias a mano.

**Backbone y historias** (de izquierda a derecha, las actividades en orden; hacia abajo, menos prioridad):

| | 1 · Conectar el restaurante | 2 · Recibir pedidos | 3 · Pasar el pedido al CRM | 4 · Controlar | 5 · Resolver contingencias |
| --- | --- | --- | --- | --- | --- |
| **Release 1 — walking skeleton (MVP)** | Como gestor, quiero conectar **un** servicio con API (ComidaYA) a mi restaurante | Como gestor, quiero ver los pedidos que entran desde ComidaYA | Como sistema, quiero cargar el pedido en un CRM que tenga API | Como gestor, quiero una **alerta** cuando un pedido no llegó al CRM en 2 minutos | Como cargador, quiero ver los datos de un pedido fallido para **cargarlo a mano** en el CRM |
| **Release 2** | Como gestor, quiero conectar un servicio **sin API** (Velocci, vía scraping) | Como gestor, quiero ver el estado de cada pedido (recibido / en CRM / fallido) | Como sistema, quiero cargar pedidos en un CRM **sin API** (vía RPA) | Como gestor, quiero ver cuántos pedidos se perdieron en el día | Como cargador, quiero cargar en MidFood un pedido que vi en la app del servicio y no entró |
| **Release 3+** | Como gestor, quiero conectar varios restaurantes de una cadena | Como gestor, quiero filtrar los pedidos por servicio | Como sistema, quiero reintentar automáticamente las cargas fallidas | Como gestor, quiero alertas por WhatsApp | Como cargador, quiero marcar un pedido como resuelto y dejar una nota |

**Cómo se lee:**
- La **primera fila** es el **walking skeleton**: con un solo servicio y un solo CRM, el recorrido completo ya funciona, incluida la red de seguridad (alerta + carga manual), que el enunciado pide porque perder pedidos se penaliza.
- Las filas siguientes son **releases** que agregan los casos más difíciles (scraping y RPA) y mejoras.
- Se entregan **franjas horizontales**, no columnas completas.

> ✍️ **En el examen, dibujalo:** una fila de tarjetas arriba con las actividades y debajo de cada una las historias, con una **línea horizontal** separando cada release. Escribí los supuestos arriba del dibujo.

---

<sub>⚙️ Consigna transcripta de las imágenes del examen del 27/10/2025 que circulan entre alumnos (sin nombres). Las respuestas modelo son propias, basadas en el material de la cátedra: *Artful Making* (cap. 1, "The Four Qualities of Artful Making"), slides de la clase 4, Jeff Patton (*The game has changed*) y la guía de Example Mapping.</sub>
