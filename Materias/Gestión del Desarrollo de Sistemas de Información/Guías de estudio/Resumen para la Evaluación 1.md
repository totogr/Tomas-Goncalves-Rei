# 📘 Resumen para la Evaluación 1 — Producto (GDSI)

> **Evaluación 1: lunes 26/10, presencial** (clase 9, "Integración de Producto"). Recuperatorio: **lunes 16/11 a las 17:00**.
> Entra **todo el bloque de Producto**: las clases 2 a 8 del cronograma, que son las **guías 02 a 09**. La guía 01 (modelos de gestión y *Artful Making*) es el marco que conviene tener claro para justificar las respuestas.
> Ese mismo día se entrega la **v3 del TP** (plan de mejora).
>
> **Se complementa con:**
> - 🟢 [**Resumen simple**](Resumen%20simple.md): la misma materia contada sin tecnicismos, para entender las ideas antes de memorizar.
> - 📚 Las [guías por clase](README.md): para profundizar cuando algo no cierra.

**Leyenda:**
- 🔴 = tema central del bloque (aparece en el título de la clase o en una entrega del TP);
- 🟠 = importante, muy probable que lo pidan aplicado;
- ✍️ = caso para resolver por escrito (con respuesta modelo).

> ⚠️ **Sobre el formato:** no tenemos evaluaciones anteriores de esta cátedra. El cronograma dice que la clase 9 es de "consultas y **práctica de aplicación**", así que lo más probable es que pidan **aplicar** las herramientas a un caso (armar un example map, elegir un experimento, priorizar riesgos), no solo definir. Por eso este resumen tiene una sección de [casos resueltos](#10-casos-para-practicar-).

---

## 🗓️ Qué entra y cómo estudiarlo

| Clase | Fecha | Tema | Sección | Guía |
| --- | --- | --- | --- | --- |
| 1 | 24/8 | Modelos de gestión · *Artful Making* (marco) | [1](#1-el-marco-artful-making-y-los-modelos-de-gestión) | [01](01%20-%20Introducci%C3%B3n%20-%20Modelos%20de%20gesti%C3%B3n%20del%20desarrollo%20de%20software.md) |
| 2 | 31/8 | Descubrimiento de producto 🔴 | [2](#2-descubrimiento-de-producto-) | [02](02%20-%20Descubrimiento%20de%20producto.md) |
| 3 | 7/9 | Métodos empíricos 🔴 | [3](#3-experimentos-y-métodos-empíricos-) | [03](03%20-%20M%C3%A9todos%20emp%C3%ADricos.md) |
| 4 | 14/9 | Construcción con Scrum + BDD 🔴 | [4](#4-scrum-) · [5](#5-bdd-example-mapping-y-slicing-) | [04](04%20-%20Construcci%C3%B3n%20con%20Scrum.md) |
| 5 | 21/9 | Revisión de avance con Scrum | [6](#6-revisión-de-avance) | [05](05%20-%20Revisi%C3%B3n%20de%20avance%20con%20Scrum.md) |
| 6 | 28/9 | Planificación y release planning 🔴 | [7](#7-planificación-story-map-y-release-plan-) | [06](06%20-%20Planificaci%C3%B3n%20y%20release%20planning.md) |
| 7 | 5/10 | Gestión de riesgos 🔴 | [8](#8-gestión-de-riesgos-) | [07](07%20-%20Gesti%C3%B3n%20de%20riesgos.md) |
| — | 12/10 | Equipos, personas y comunicación (feriado, material en el campus) | [9.1](#91-equipos-personas-y-comunicación) | [08](08%20-%20Equipos%2C%20personas%20y%20comunicaci%C3%B3n.md) |
| 8 | 19/10 | Mejora y métricas 🔴 | [9.2](#92-mejora-y-métricas) | [09](09%20-%20Mejora%20y%20m%C3%A9tricas.md) |

### Plan de estudio: del miércoles 14/10 al lunes 26/10

> Contexto: el parcial de **Ciencia de Datos pasó al jueves 29/10** (el 15/10 hubo paro) y el de **Redes** es el **miércoles 4/11**. GDSI es el primero, pero es el más liviano en cuentas: alcanza con **45–60 minutos** los días que le tocan.

| Fecha | GDSI |
| --- | --- |
| **Mié 14/10** | Leé el [Resumen simple](Resumen%20simple.md) completo, de corrido, sin tomar notas |
| **Jue 15/10** | Secciones **1–3** (marco, descubrimiento, experimentos y métodos empíricos) |
| **Vie 16/10** | Secciones **4–5** (Scrum y BDD). Armá de memoria un **example map** de "cambio de contraseña" |
| **Dom 18/10** | Secciones **6–8** (revisión, story map, release plan y riesgos). Hacé los ✍️ de [velocidad](#cuentas-que-pueden-aparecer-) y de [exposición al riesgo](#8-gestión-de-riesgos-) |
| **Lun 19/10** | Clase de **mejora y métricas** → sección **9** a la noche |
| **Jue 22/10** | [Casos para practicar](#10-casos-para-practicar-) 1 a 4 **sin mirar la respuesta** |
| **Vie 23/10** | Casos 5 a 8 + aplicá cada herramienta a **tu TP** (es lo que mejor conocés) |
| **Dom 25/10** | Repaso final: [tablas de comparación](#11-no-confundir-tablas-de-comparación), [Ojo con esto](#12-ojo-con-esto-errores-típicos) y [preguntas para practicar](#13-preguntas-para-practicar-sin-mirar) |
| **Lun 26/10** | 🎯 **Evaluación 1** + entrega v3 del TP |

> Los días que faltan (sáb 17, mar 20, mié 21, sáb 24) quedan para Ciencia de Datos. El plan completo de CdD está en su [Resumen para el parcial](../../Ciencia%20de%20Datos/Gu%C3%ADas%20de%20estudio/Resumen%20para%20el%20parcial.md).

---

## 🧵 Las 7 ideas que atraviesan todo el bloque

Si en una respuesta no sabés cómo justificar, casi siempre la justificación es una de estas:

1. **El desarrollo de producto es incierto.** No sabés del todo qué hay que construir hasta que lo probás. Por eso se **descubre** y se **experimenta**, en vez de planificar todo de entrada.
2. **Hacer que el cambio sea barato** (*Artful Making*). Si iterar cuesta poco, conviene probar, equivocarse y ajustar muchas veces.
3. **Aprender antes de construir.** Un prototipo, una entrevista o un example map salen mucho más baratos que programar lo equivocado.
4. **Comportamiento > opinión.** Lo que la gente **hace** (paga, se registra, vuelve) vale más que lo que **dice** que haría.
5. **Hacer explícito lo implícito.** Supuestos → lista de riesgos; acuerdos → ejemplos concretos; dudas → tarjetas rojas. Lo que no está escrito no se puede revisar.
6. **Entregar de a rebanadas con valor.** Cortes verticales y walking skeleton: cada entrega sirve para algo y genera feedback.
7. **Inspeccionar y adaptar.** Review (¿construimos lo correcto?), retro (¿trabajamos bien?), métricas (¿mejoramos de verdad?).

---

## 1. El marco: *Artful Making* y los modelos de gestión

### *Artful Making* (Austin & Devin) 🟠

El libro compara dos formas de "hacer":

| | **Industrial making** 🏭 | **Artful making** 🎭 |
| --- | --- | --- |
| Cómo se trabaja | Se especifica todo y después se ejecuta el plan | Se **ensaya**: se prueba, se mira el resultado y se ajusta |
| El resultado | Se conoce de antemano | **Emerge** durante el trabajo |
| La variación | Es un error a eliminar | Es una **fuente de ideas** |
| Conviene cuando… | Cambiar es **caro** (fabricar, construir un puente) | Cambiar (reconfigurar, iterar) es **barato** |

- **La idea clave:** cuando el **costo de iterar es bajo**, conviene trabajar como una compañía de teatro: ensayar muchas veces y converger en algo mejor de lo que se podría haber especificado al principio. El software tiene costo de iteración bajo, y por eso le sirve este enfoque.
- **Las cuatro cualidades** del trabajo "artístico":
  - **Release (liberar):** el líder suelta parte del control y deja que el equipo explore.
  - **Collaboration (colaborar):** las ideas de unos se combinan con las de otros y salen cosas nuevas.
  - **Ensemble (elenco):** el grupo funciona como una unidad, más que la suma de sus integrantes.
  - **Play (jugar):** explorar, probar variantes e interactuar con el público (el usuario) para mejorar la obra.
- **Cómo se conecta con cada tema:** experimentos y prototipos = ensayos baratos; Scrum = iteraciones cortas con función frente al público (review); equipos autoorganizados = *release* + *ensemble*; retrospectivas = el elenco revisa cómo trabajó.

### Producto, proyecto y proceso

| Enfoque | Pregunta que responde | Bloque |
| --- | --- | --- |
| **Producto** | ¿Estamos construyendo **lo correcto**? ¿Le sirve al usuario? | Evaluación 1 |
| **Proyecto** | ¿Lo llevamos a término en alcance, tiempo, costo y calidad? | Evaluación 2 |
| **Proceso** | ¿Cómo mejoramos **la forma de trabajar**? | Evaluación 2 |

### Modelos de gestión del desarrollo

| Modelo | En una línea |
| --- | --- |
| **En etapas (cascada)** | Fases en secuencia con plan cerrado; sufre cuando los requisitos cambian |
| **Iterativo** | Repite el ciclo y **refina** lo mismo en cada vuelta |
| **Incremental** | **Suma pedazos** que ya funcionan |
| **Ágil** | Iterativo + incremental + adaptarse al cambio con feedback frecuente |
| **Flujo continuo** | El trabajo pasa de a un ítem, sin lotes fijos (Kanban) |

> 🔑 **Iterativo ≠ incremental.** Iterativo es *mejorar lo mismo* (el boceto se vuelve dibujo); incremental es *agregar partes* (primero la rueda, después el chasis). Scrum hace las dos cosas.

---

## 2. Descubrimiento de producto 🔴

**Qué es:** el trabajo de averiguar **qué** construir antes de construirlo: entender necesidades reales y **validar ideas con experimentos baratos**, reduciendo la incertidumbre.

### Output, outcome e impacto (Jeff Patton, *The game has changed*)

| | Qué es | Ejemplo |
| --- | --- | --- |
| **Output** | Lo que el equipo **produce** (features, pantallas) | "Hicimos el carrito de compras" |
| **Outcome** | El **cambio de comportamiento** en los usuarios | "La gente compra sin llamar por teléfono" |
| **Impacto** | El **beneficio** para la organización | "Bajaron los costos del call center" |

> 🔑 El objetivo no es producir más (output), sino lograr **el mayor outcome e impacto con el menor output posible**. Medir solo "cuántas features entregamos" es medir lo equivocado.

### Herramientas

| Herramienta | Para qué | Qué contiene |
| --- | --- | --- |
| **Product Vision Board** 🟠 | Poner en una hoja la visión del producto y acordarla | **Visión** (el porqué) · **grupo objetivo** (para quién) · **necesidades** (qué problema resuelve) · **producto** (qué es y qué lo hace distinto) · **objetivos de negocio** (qué gana la organización) |
| **Lista de riesgos / supuestos** 🔴 | Hacer explícito lo que **creemos** pero no sabemos | Cada supuesto, qué tan peligroso es si es falso y qué tan seguro estamos. Se atacan primero los **más peligrosos y menos validados** |
| **Hipótesis** | Convertir un supuesto en algo que se pueda probar | "Creemos que **[usuarios]** van a **[comportamiento]** porque **[motivo]**. Lo sabremos si **[métrica]** llega a **[umbral]**" |
| **Diseño de experimento** | Probar la hipótesis con el **mínimo esfuerzo** | Hipótesis · método · métrica · **criterio de éxito definido antes** · duración |
| **Prototipo rápido** | Aprender sin desarrollar todo | De baja fidelidad (papel) a alta (clickeable). Se elige la **mínima fidelidad** que responde la pregunta |
| **Story Map** | Ver el producto completo como un recorrido | Ver la [sección 7](#7-planificación-story-map-y-release-plan-) |

### Los riesgos de un producto (qué puede salir mal)

| Riesgo | Pregunta | Se ataca con… |
| --- | --- | --- |
| **Valor / deseabilidad** | ¿Lo quieren? ¿Resuelve un problema real? | Entrevistas, observación, landing, fake door |
| **Usabilidad** | ¿Pueden usarlo sin trabarse? | Test de usabilidad con prototipo |
| **Factibilidad técnica** | ¿Lo podemos construir? | Spike o prueba de concepto técnica |
| **Viabilidad de negocio** | ¿Cierra el negocio? ¿Pagarían? | Pre-venta, concierge que cobra, buy-a-feature |

> 🔑 **Criterio de éxito antes del experimento.** Si lo definís después de ver los datos, cualquier resultado "confirma" tu idea.

---

## 3. Experimentos y métodos empíricos 🔴

### Los cuatro métodos empíricos de la clase 3

| Método | Pregunta que responde | Cómo | Cuidado con… |
| --- | --- | --- | --- |
| **Test de usabilidad** | ¿Pueden **usarlo**? | Un usuario real hace **tareas realistas** mientras lo observás | Ayudarlo o explicarle (invalida el test) |
| **Entrevista** | ¿Qué **necesita** y por qué? | Preguntás por **hechos pasados concretos** | Preguntar opiniones o vender tu idea |
| **Fake door / smoke test** | ¿Hay **interés real**? | Ofrecés algo que todavía no existe y medís cuántos lo eligen | No avisar después ("todavía no está") → frustra al usuario |
| **Observación contextual** | ¿Cómo lo hace **en su entorno**? | Lo mirás trabajar donde trabaja, como aprendiz | Interrumpir o "corregirlo" |

### *The Mom Test* (Rob Fitzpatrick) 🟠

Una entrevista está bien hecha si **ni tu mamá podría mentirte** con sus respuestas. Tres reglas:

1. Hablá de **su vida**, no de tu idea.
2. Preguntá por **cosas concretas del pasado**, no por opiniones ni por el futuro ("¿lo usarías?").
3. **Hablá menos y escuchá más.**

| ❌ Mala pregunta | ✅ Buena pregunta |
| --- | --- |
| "¿Te parece buena idea una app para X?" | "¿Cómo resolviste X la última vez?" |
| "¿Pagarías por esto?" | "¿Gastaste plata o tiempo en resolverlo? ¿Cuánto?" |
| "¿Usarías esta función?" | "¿Cuándo fue la última vez que te pasó? ¿Qué hiciste?" |

> Los cumplidos ("¡qué buena idea!") **no son datos**. Un dato es un hecho o un compromiso (tiempo, plata, reputación).

### Test de usabilidad (Kate Moran, NN/g) 🟠

- **3 componentes:** **facilitador** (da tareas y observa, sin inducir) · **tareas** (realistas, sin revelar la solución) · **participante** (representa al usuario real, no es del equipo).
- **5 usuarios** encuentran la mayoría de los problemas (alrededor del 85%, Nielsen). Conviene testear con 5, corregir y volver a testear.
- **Think-aloud:** el participante piensa en voz alta mientras usa el producto.
- **Formativo** (cualitativo, para **encontrar** problemas) vs. **sumativo** (cuantitativo, para **medir**: tasa de éxito, tiempo por tarea).
- **Moderado** (alguien guía, presencial o remoto) vs. **no moderado** (la plataforma graba sola).

### Observación contextual (contextual inquiry)

- Se va **al lugar** donde el usuario hace la tarea.
- Relación **maestro–aprendiz**: el usuario es el experto y vos aprendés de él.
- Muestra lo que el usuario **no cuenta** porque lo hace en automático: atajos, papelitos pegados en el monitor, planillas paralelas.

### El catálogo de experimentos (lo esencial)

| Experimento | Qué es | Riesgo que ataca |
| --- | --- | --- |
| **Landing page / smoke test** | Página que describe el producto con un botón de registro o pre-compra | Deseabilidad (y negocio, si hay pre-compra) |
| **Fake door** | Un botón de una **función** que no existe; se cuentan los clics | Deseabilidad de una función puntual |
| **Concierge MVP** | El servicio se da **a mano** y el cliente **sabe** que hay personas detrás | Valor del **resultado** |
| **Wizard of Oz** | Parece automático, pero **una persona lo hace en secreto** | La **experiencia** completa, sin construir el motor |
| **Prototipo en papel / clickeable** | Pantallas de mentira para recorrer el flujo | Usabilidad |
| **A/B test** | Dos versiones a grupos distintos, se mide cuál rinde más | Optimización (ya con producto) |
| **MVP de una sola funcionalidad** | Solo la función central, funcionando de verdad | Valor principal, con evidencia fuerte |

**Fuerza de la evidencia** (de menor a mayor):

> encuesta / entrevista → prototipo → landing / fake door → concierge / Wizard of Oz → pre-venta → MVP o A/B en producción

> 🔑 Sube cuando el usuario **hace algo que le cuesta** (deja su mail, paga, vuelve), y baja cuando solo **opina**.

---

## 4. Scrum 🔴

**Qué es:** un **marco de trabajo** (no una metodología completa) para desarrollar productos en **iteraciones cortas** (sprints), basado en el **empirismo**.

### Las 3 responsabilidades (como las presenta la cátedra)

| Responsabilidad | Características (slides de la clase 4) |
| --- | --- |
| **Responsable de Producto** (Product Owner) | Tiene la **visión** · responde por el **retorno de la inversión** · es **experto del negocio** y **líder del diseño del producto** · está **cerca del equipo**. Ordena el Product Backlog. Es **una persona**, no un comité |
| **Desarrolladores** (Developers) | **Autoorganizados** · **multifuncionales** · **responsabilidad solidaria** (el equipo responde en conjunto) · "**habilidades y no roles**" · **no distribuidos** · **3 a 10 miembros** |
| **Facilitador** (Scrum Master) | **Líder servicial** · controla dejando hacer y **promueve la autonomía** · **no es parte del equipo de desarrollo** · "debe tender a ser **invisible**" |

> ℹ️ El *Scrum Guide 2020* dice que el **Scrum Team completo** (PO + SM + Developers) es "típicamente de 10 personas o menos". El "3 a 10 desarrolladores" es la formulación de la cátedra: si te preguntan, respondé con la de las slides.

### Los 5 eventos

| Evento | Para qué | Timebox (sprint de 1 mes) |
| --- | --- | --- |
| **Sprint** | El contenedor de todo; largo fijo | ≤ 1 mes |
| **Sprint Planning** | Qué se hace y cómo; se define el **Sprint Goal** | 8 h |
| **Daily Scrum** | Inspeccionar el avance hacia el Sprint Goal y reorganizarse | 15 min |
| **Sprint Review** | Mostrar el incremento a los interesados y **adaptar el backlog** | 4 h |
| **Retrospectiva** | Revisar **cómo trabajamos** y elegir mejoras | 3 h |

### Los 3 artefactos y sus compromisos

| Artefacto | Su compromiso | Cómo lo dice la cátedra |
| --- | --- | --- |
| **Product Backlog** | **Product Goal** (objetivo de producto) | "El Product Backlog **concretiza** el objetivo de producto" |
| **Sprint Backlog** | **Sprint Goal** (objetivo del sprint) | "El Sprint Backlog **concretiza** el objetivo del sprint" |
| **Incremento** | **Definition of Done** (definición de terminado) | "El incremento **cumple** la definición de terminado" |

- **Pilares del empirismo:** **transparencia · inspección · adaptación**.
- **Valores:** **compromiso · foco · apertura · respeto · coraje**.
- **Tablero:** hace visible el Sprint Backlog (qué está por hacer, en curso y terminado). No es un artefacto oficial de Scrum, pero es la herramienta de **transparencia** del día a día.

---

## 5. BDD, Example Mapping y slicing 🔴

### BDD (Behavior-Driven Development)

> **Definición de la cátedra:** *"Guiar el desarrollo incremental mediante la promoción de **conversaciones** sobre el comportamiento esperado del sistema y la producción de **especificaciones ejecutables**."*

- Es una **aproximación ágil**: una práctica que acompaña todo el desarrollo, no una herramienta.
- El comportamiento se define **en colaboración** entre negocio y desarrollo, en un **lenguaje compartido**.
- Las especificaciones se pueden **automatizar como tests**.

**Las 3 actividades:**

| Actividad | Qué se hace | Qué produce |
| --- | --- | --- |
| **Discovery** | Conversación estructurada para generar **entendimiento compartido** | Reglas, ejemplos y preguntas (example map) |
| **Formulation** | Los ejemplos se escriben como **escenarios** | Escenarios *Given / When / Then* |
| **Automation** | Los escenarios se convierten en **tests automáticos** | Feedback automático del comportamiento |

**El flujo de 8 pasos (slides de la clase 4):**

1. **Elegir la historia** (priorizar la funcionalidad).
2. **Taller de requerimientos** (descubrir detalles mediante ejemplos).
3. **Formular** (convertir los ejemplos en escenarios).
4. **Revisar** (feedback del negocio).
5. **Automatizar** (codear las pruebas automáticas).
6. **Implementar** (codear con TDD).
7. **Tests complementarios** (carga, integración, seguridad).
8. **Release** (producto potencialmente entregable).

> ⚠️ **Product Discovery ≠ BDD Discovery.** El primero decide **qué producto** hacer (clases 2–3). El segundo define **en detalle cada funcionalidad** ya elegida (clase 4).

### Example Mapping (Matt Wynne) 🔴

| Tarjeta | Qué va | Ejemplo ("cambio de contraseña") |
| --- | --- | --- |
| 🟨 **Amarilla** | La **historia** | "Como usuario quiero cambiar mi contraseña para mantener mi cuenta segura" |
| 🟦 **Azul** | Las **reglas** (criterios de aceptación) | "La nueva contraseña debe ser distinta de la anterior" |
| 🟩 **Verde** | **Ejemplos concretos** de cada regla | "Ingresa *miperro123*, que es la actual → se rechaza" |
| 🟥 **Roja** | **Preguntas** que nadie puede responder en la reunión | "¿Se cierra la sesión en los otros dispositivos?" |

**Cómo leer un example map terminado:**
- **Muchas azules** → la historia es grande: conviene **partirla**.
- **Muchas rojas** → falta conocimiento: todavía **no está lista** para el sprint.
- **Una regla sin ejemplos** → no se entendió: pedí un ejemplo.
- **Pocas tarjetas y ninguna roja** → la historia está lista.

**Cómo es la conversación (slides y *BDD Discovery*):**
- **Three Amigos:** negocio + desarrollo + testing (perspectivas **diversas**; pueden ser más de tres personas).
- **Corta y frecuente:** bloques de **30 minutos**, **1 historia por sesión**, varias veces por semana, en los espacios de **refinamiento**.
- **Antes** de que la historia entre al Sprint Backlog. **No** en la Sprint Planning.
- **Foco progresivo:** se registra lo aprendido mientras se avanza y se cortan las discusiones que no avanzan (pasan a tarjeta roja).
- **Consenso:** el resultado es correcto y suficiente para todos, **no hay conocimiento privado**, y **alguien se hace responsable** de responder las preguntas pendientes.

**Formulation** (cómo queda un ejemplo escrito como escenario):

```gherkin
Escenario: la nueva contraseña es igual a la actual
  Dado que el usuario tiene la contraseña "miperro123"
  Cuando intenta cambiarla por "miperro123"
  Entonces el sistema rechaza el cambio
  Y le muestra "La nueva contraseña debe ser distinta de la actual"
```

### Slicing (cómo partir funcionalidades) 🟠

| | **Vertical** ✅ | **Horizontal** ❌ |
| --- | --- | --- |
| Cómo se corta | Una rebanada **fina que atraviesa todas las capas** (pantalla + lógica + datos) | Por capa técnica: "primero toda la base de datos, después toda la interfaz" |
| ¿Entrega valor sola? | **Sí**: el usuario la puede usar | No, hasta que se juntan todas las capas |
| ¿Da feedback? | Sí, en cada sprint | Recién al final |

**Formas útiles de partir una historia grande:** por **regla** (cada tarjeta azul puede ser una historia), por **camino feliz primero y casos de error después**, por **tipo de usuario**, por **variante de datos** (un medio de pago primero, después los demás) o **manual primero, automático después**.

> 🔑 Una buena historia cumple **INVEST**: **I**ndependiente, **N**egociable, **V**aliosa, **E**stimable, pequeña (***S**mall*) y **T**esteable (testeable = tiene ejemplos).

---

## 6. Revisión de avance

- **Qué es:** contrastar **lo construido** con **lo esperado**, usando el **mapa de ejemplos** como vara.
- Cada ejemplo verde es un **caso de prueba**: si el sistema hace lo que dice el ejemplo, la regla está cumplida. Así no se discute "a ojo" si algo está terminado.
- Es lo que se hace en la **Sprint Review**: se inspecciona el incremento con los interesados y se **adapta el Product Backlog** (qué sigue).

| | **Sprint Review** | **Retrospectiva** |
| --- | --- | --- |
| Mira… | **El producto** (¿construimos lo correcto?) | **El proceso** (¿trabajamos bien?) |
| Participan | Equipo Scrum + **interesados** | Solo el **equipo Scrum** |
| Resultado | Backlog actualizado | **Acciones de mejora** concretas |

| | **Criterios de aceptación** | **Definition of Done** |
| --- | --- | --- |
| Se aplica a… | **Una historia** en particular | **Todas** las historias |
| Ejemplo | "La contraseña nueva debe ser distinta de la anterior" | "Tiene tests automáticos, pasó revisión de código y está desplegado en el entorno de prueba" |

---

## 7. Planificación: story map y release plan 🔴

### Por qué un mapa y no una lista

Un **backlog plano** (una lista larga ordenada por prioridad) **pierde el contexto**: no muestra el recorrido del usuario, es fácil que falten cosas y no se ve qué sería un primer release usable. Patton lo compara con una bolsa de abono sin contexto.

### El Story Map (Jeff Patton)

- **Eje horizontal — backbone (columna vertebral):** las **actividades del usuario** en el orden en que las hace. Contado de izquierda a derecha, es la **historia del sistema**.
- **Eje vertical — prioridad:** debajo de cada actividad, las historias que la implementan, **lo más importante arriba**.
- **Walking skeleton:** la primera franja, con **una historia de cada actividad** → el producto más flaco que **funciona de punta a punta**.
- **Release slice:** cada **corte horizontal** del mapa es una entrega. Se entregan **franjas**, no columnas completas.

> 🔑 Si terminás toda una columna antes de empezar la siguiente, llegás a la fecha con medio producto que no se puede usar. Con franjas, desde la primera entrega el usuario puede **hacer el recorrido completo**, aunque sea en su versión básica.

### Release plan

| Paso | Qué se hace |
| --- | --- |
| 1 | Armar el story map y **cortar franjas** (releases) con un objetivo cada una |
| 2 | **Estimar** el tamaño de las historias (puntos o tallas, relativo) |
| 3 | Usar la **velocidad** del equipo para saber cuántos sprints entran |
| 4 | **Calendarizar**: asignar las franjas a sprints y fechas |
| 5 | Dejar **colchón** (buffer) y **reordenar** cuando cambia lo que se sabe |

### Cuentas que pueden aparecer ✍️

> **Enunciado:** el release 1 suma **84 puntos**. La velocidad promedio del equipo es de **20 puntos por sprint**, con sprints de 2 semanas. ¿Cuándo termina? ¿Y con un colchón del 20%?
>
> **Resolución:**
> - Sin colchón: 84 / 20 = 4,2 → **5 sprints** (se redondea para arriba) = **10 semanas**.
> - Con colchón del 20%: 84 × 1,2 = 100,8 → 100,8 / 20 = 5,04 → **6 sprints** = **12 semanas**.
> - Otra forma de verlo: si la fecha es fija en 5 sprints, el colchón va en **alcance**: comprometés los **Must** (por ejemplo, 70 puntos) y dejás los **Could** como lo que se cae si hace falta.

> ⚠️ La velocidad es de **un equipo** y sirve para **proyectar**, no para comparar equipos ni para medir productividad.

---

## 8. Gestión de riesgos 🔴

**Riesgo:** un evento **incierto** que, si ocurre, afecta al proyecto o al producto. Un **problema** es un riesgo que **ya ocurrió**.

### La planilla de riesgos

| Campo | Qué va |
| --- | --- |
| **Descripción** | Escrita como **causa → consecuencia**: "Si el proveedor de pagos no da acceso al sandbox a tiempo, no podemos probar el checkout" |
| **Probabilidad** | Qué tan probable es (alta/media/baja o de 1 a 5) |
| **Impacto** | Cuánto daño hace si ocurre |
| **Exposición** | **Probabilidad × impacto** → define la prioridad |
| **Respuesta** | Evitar, mitigar, transferir o aceptar (ver abajo) |
| **Disparador** | La señal temprana de que está por ocurrir |
| **Responsable** | Quién lo sigue |

### Las 4 respuestas a un riesgo

| Respuesta | Qué es | Ejemplo |
| --- | --- | --- |
| **Evitar** | Cambiar el plan para que el riesgo **no pueda ocurrir** | No usar una tecnología que nadie conoce |
| **Mitigar** | **Bajar** la probabilidad o el impacto | Hacer un spike técnico al principio; capacitar |
| **Transferir** | Pasárselo a **otro** | Contratar un servicio con SLA; un seguro |
| **Aceptar** | Asumirlo, con o sin **plan de contingencia** | Riesgos de exposición baja |

### ✍️ Ejemplo de priorización

| Riesgo | P (1–5) | I (1–5) | Exposición | Prioridad |
| --- | --- | --- | --- | --- |
| R1 · Los usuarios no entienden el flujo de pago | 4 | 4 | **16** | 1ª → test de usabilidad ya |
| R2 · La API externa cambia de versión | 2 | 5 | **10** | 2ª → aislarla detrás de un adaptador |
| R3 · Un integrante deja la materia | 2 | 3 | **6** | 3ª → no tener conocimiento en una sola persona |
| R4 · Cambia el diseño gráfico | 3 | 1 | **3** | Aceptar |

> 🔑 **Se ordena por exposición, no por miedo.** Un riesgo de impacto altísimo pero muy improbable puede quedar detrás de uno moderado que es casi seguro.

### MoSCoW y buffering

- **MoSCoW** prioriza **alcance**:
  - **Must:** sin esto el release no sirve.
  - **Should:** importante, pero hay un rodeo posible.
  - **Could:** deseable, es lo primero que se cae.
  - **Won't (this time):** acordado que **esta vez no** entra.
- **Buffering:** reservar un **colchón explícito** para lo imprevisto:
  - de **tiempo** (sprints de margen);
  - de **alcance** (los *Could* funcionan como colchón: si algo se complica, se caen ellos y los *Must* llegan);
  - de **recursos**.
- **Relación entre los dos:** MoSCoW **crea** el buffer de alcance. Si el 100% del release es *Must*, no hay de dónde recortar cuando aparece un riesgo.

> 🔑 Un buffer **no es inflar cada estimación** "por las dudas" (eso se consume solo, por la ley de Parkinson). Es un colchón **explícito y compartido**, que se gestiona y se mira cuánto se consumió.

**Conexión con *Artful Making*:** con iteraciones cortas, los riesgos **aparecen temprano**, cuando corregirlos todavía es barato. Los experimentos de la clase 2 **son** gestión de riesgos de producto.

---

## 9. Equipos, mejora y métricas

### 9.1 Equipos, personas y comunicación

**Team Topologies — 4 tipos de equipo:**

| Tipo | Qué hace |
| --- | --- |
| **Stream-aligned** | Alineado a un **flujo de valor** (una parte del producto, un tipo de cliente). Es el equipo principal; los demás existen para ayudarlo |
| **Platform** | Da **servicios internos** (infraestructura, despliegue) para que los otros equipos vayan rápido |
| **Enabling** | **Ayuda** temporalmente a otros equipos a adquirir una capacidad (mentoría) |
| **Complicated-subsystem** | Se ocupa de una pieza que requiere **conocimiento muy especializado** |

**3 modos de interacción:**
- **Colaboración:** trabajar juntos un tiempo, para descubrir algo nuevo.
- **X-as-a-Service:** un equipo **consume** lo que el otro ofrece, con mínima coordinación.
- **Facilitación:** un equipo **ayuda** a otro a aprender.

**Conceptos de personas y comunicación:**

| Concepto | Idea |
| --- | --- |
| **Ley de Conway** | El sistema **copia la estructura de comunicación** de la organización que lo construye. Por eso se diseñan los equipos según la arquitectura que se busca (*maniobra inversa de Conway*) |
| **Carga cognitiva** | Un equipo tiene un límite de cosas que puede entender y mantener a la vez. Si se lo supera, baja la calidad y la velocidad |
| **Canales de comunicación** | Con *n* personas hay **n(n−1)/2** canales. 5 personas → 10; 10 personas → 45. Por eso los equipos chicos se comunican mejor |
| **Conflictos** | Son inevitables y, bien llevados, útiles (sobre **ideas**, no sobre **personas**). Se gestionan, no se esconden |
| **Perfiles** | Se busca gente con **habilidades** más que con roles fijos (perfil "en T": profundo en algo, con nociones de lo demás) |

### 9.2 Mejora y métricas

**Retrospectiva:** el equipo revisa **cómo trabajó** y sale con **pocas acciones concretas** (con responsable), no con una lista de quejas. Una estructura habitual tiene 5 momentos:
1. Preparar el ambiente.
2. Juntar datos (qué pasó).
3. Entender por qué pasó.
4. Decidir qué hacer.
5. Cerrar.

Algunos formatos para juntar datos:
- **Empezar / dejar de / seguir haciendo** (*start / stop / continue*).
- **Qué salió bien / qué salió mal / qué aprendimos**.

**GQM (Goal – Question – Metric):** se empieza por el **objetivo**, después las **preguntas** que dirían si se cumple, y recién al final las **métricas**.

> **Ejemplo:**
> - **Goal:** "reducir los errores que llegan al usuario".
> - **Questions:** "¿cuántos errores se escapan por release?", "¿en qué parte del sistema aparecen?".
> - **Metrics:** "defectos reportados por usuarios por release" y "defectos por módulo".

**Métricas DORA (Nicole Forsgren, *Accelerate*):**

| Métrica | Mide | Lado de la balanza |
| --- | --- | --- |
| **Frecuencia de despliegue** | Cada cuánto se sube a producción | Velocidad |
| **Lead time de cambios** | Cuánto tarda un cambio desde el commit hasta producción | Velocidad |
| **Tasa de fallas por cambio** | Qué % de despliegues genera un problema | Estabilidad |
| **Tiempo de recuperación** | Cuánto se tarda en restaurar el servicio tras una falla | Estabilidad |

> 🔑 El hallazgo de DORA: los equipos de **alto rendimiento** son **más rápidos y más estables a la vez**; no hay que elegir entre una cosa y la otra. Por eso se miden **juntas**: mirar solo la velocidad empuja a romper cosas.

**Cuidados con las métricas:**
- **Ley de Goodhart:** cuando una medida se convierte en objetivo, deja de ser una buena medida (si se premia "cantidad de tests", aparecen tests vacíos).
- **Métricas de vanidad:** números que suben siempre y no ayudan a decidir (usuarios registrados totales, líneas de código).
- **Métrica vs. indicador:** la métrica es el número; el indicador es la métrica comparada contra un **objetivo o umbral** que te dice si estás bien o mal.

---

## 10. Casos para practicar ✍️

> Hacelos por escrito **antes** de mirar la respuesta. Están pensados como la "práctica de aplicación" que anuncia el cronograma.

**Caso 1 — Elegir un experimento.** Un equipo quiere lanzar una app para compartir auto entre vecinos. No sabe si a la gente le interesa. ¿Qué experimento hacés primero y qué medís?

> **Respuesta modelo:**
> - El riesgo principal es de **deseabilidad** (¿lo quieren?), no técnico.
> - Primero, **entrevistas** estilo *Mom Test* sobre cómo se mueven hoy y cuánto gastan.
> - Después, una **landing page** con registro anticipado en un barrio puntual.
> - **Criterio de éxito definido antes:** por ejemplo, "el 10% de los visitantes deja su mail".
> - No conviene construir la app: es caro y no responde la pregunta.

**Caso 2 — Corregir una entrevista.** Un compañero pregunta: *"¿No te parece que sería genial una app que te recuerde tomar agua? ¿La usarías?"*. ¿Qué está mal y cómo la reformulás?

> **Respuesta modelo:**
> - Viola las reglas de *The Mom Test*: **presenta la idea**, pide **opinión** y pregunta por el **futuro** ("¿la usarías?"). La respuesta va a ser un "sí" por cortesía.
> - **Mejor:** "¿Te pasó en la última semana olvidarte de tomar agua? ¿Qué hiciste? ¿Probaste algo para acordarte?".

**Caso 3 — Concierge o Wizard of Oz.** Una startup recomienda outfits con "inteligencia artificial", pero al principio dos estilistas eligen la ropa a mano sin que el cliente lo sepa. ¿Qué experimento es?

> **Respuesta modelo:**
> - **Wizard of Oz:** el cliente cree que es automático y hay personas en secreto.
> - Si el cliente **supiera** que lo eligen estilistas, sería **concierge**.
> - Sirve para probar si la **experiencia** tiene valor antes de construir el algoritmo.

**Caso 4 — Example map.** Historia: *"Como cliente quiero retirar mi pedido en el local"*. Armá un example map con al menos 2 reglas, ejemplos y una pregunta.

> **Respuesta modelo:**
> - 🟨 **Historia:** retirar el pedido en el local.
> - 🟦 **Regla 1:** solo se puede retirar si el pedido está "listo".
>   - 🟩 El pedido 123 está listo y el cliente muestra el código → lo retira.
>   - 🟩 El pedido 124 está "en preparación" → se le avisa que espere.
> - 🟦 **Regla 2:** lo retira el titular o alguien con el código de retiro.
>   - 🟩 Va un amigo con el código → lo retira.
>   - 🟩 Va un amigo sin código → no lo retira.
> - 🟥 **Pregunta:** ¿cuántos días se guarda un pedido sin retirar?

**Caso 5 — Slicing.** La historia "pagar la compra" es enorme (tarjeta, transferencia, cuotas, cupones). ¿Cómo la partís?

> **Respuesta modelo:**
> - Cortes **verticales**, cada uno usable por sí mismo:
>   1. Pagar con **un solo medio** (tarjeta en un pago) de punta a punta.
>   2. Sumar **transferencia**.
>   3. Sumar **cuotas**.
>   4. Sumar **cupones**.
> - Todo lo que no sea el primer corte puede ir como *Should* o *Could*.
> - **No** se corta "primero la base de datos de pagos, después la pantalla" (eso es horizontal).

**Caso 6 — Story map y release.** Una app de turnos médicos tiene estas actividades: buscar profesional → elegir turno → confirmar → recordatorio → cancelar. ¿Qué es el walking skeleton?

> **Respuesta modelo:**
> - Una historia mínima **por cada actividad**:
>   - buscar por especialidad (sin filtros);
>   - ver los turnos libres de un día;
>   - confirmar con nombre y DNI;
>   - recordatorio por mail;
>   - cancelar desde el link del mail.
> - Es pobre, pero **funciona de punta a punta**.
> - Los filtros, el pago o el recordatorio por WhatsApp van en franjas posteriores.

**Caso 7 — Riesgos.** El equipo depende de una API de mapas que nunca usó y el cliente pide la demo en 3 semanas. Escribí el riesgo en la planilla y proponé una respuesta.

> **Respuesta modelo:**
> - **Descripción:** "Si la API de mapas no soporta lo que necesitamos o tiene límites de uso, la demo no muestra el recorrido".
> - **Evaluación:** probabilidad media (3), impacto alto (5) → **exposición 15**.
> - **Respuesta — mitigar:** un **spike** de 1–2 días en el primer sprint para probar la API.
> - **Contingencia:** si falla, usar un mapa estático en la demo.
> - **Disparador:** el spike no logra mostrar un recorrido al día 2.

**Caso 8 — Métricas.** El gerente quiere medir al equipo por "cantidad de historias cerradas por sprint". ¿Qué le respondés usando GQM y DORA?

> **Respuesta modelo:**
> - **Primero el objetivo (GQM):** ¿qué quiere lograr? Si es "entregar valor más rápido sin romper nada", las preguntas son "¿cuánto tarda un cambio en llegar al usuario?" y "¿cuántas veces rompemos producción?".
> - **De ahí salen las métricas:** lead time y tasa de fallas (**DORA**), medidas **juntas**.
> - **El problema de "historias cerradas":** por **Goodhart**, invita a partir historias artificialmente o a cerrarlas sin terminar. Además es **output**, no **outcome**.

---

## 11. No confundir (tablas de comparación)

| Esto | vs. | Aquello | La diferencia |
| --- | --- | --- | --- |
| **Product Discovery** | | **BDD Discovery** | **Qué producto** hacer vs. el **detalle** de una funcionalidad ya elegida |
| **Concierge** | | **Wizard of Oz** | El cliente **sabe** que hay personas vs. **cree** que es automático |
| **Fake door** | | **Smoke test / landing** | Una **función** puntual dentro de un producto vs. el **producto entero** antes de existir |
| **Sprint Review** | | **Retrospectiva** | **Producto** con interesados vs. **proceso**, solo el equipo |
| **Criterios de aceptación** | | **Definition of Done** | Para **una** historia vs. para **todas** |
| **Iterativo** | | **Incremental** | **Refinar** lo mismo vs. **sumar** partes |
| **Backlog plano** | | **Story map** | Lista sin contexto vs. mapa 2D con recorrido + prioridad |
| **Slicing vertical** | | **Slicing horizontal** | Rebanada con valor de punta a punta vs. una capa técnica sin valor sola |
| **Output** | | **Outcome** | Lo que **producimos** vs. el **cambio** que logramos en el usuario |
| **Riesgo** | | **Problema** | Puede pasar vs. **ya pasó** |
| **Mitigar** | | **Contingencia** | Actuar **antes** para bajar P o I vs. plan B para **cuando ocurra** |
| **Test formativo** | | **Test sumativo** | **Encontrar** problemas vs. **medir** con números |
| **Métrica** | | **Indicador** | El número vs. el número **comparado contra un objetivo** |

---

## 12. Ojo con esto (errores típicos)

| ❌ Error | ✅ Lo correcto |
| --- | --- |
| "BDD es escribir Given/When/Then" o "BDD es Cucumber" | BDD es **conversación y descubrimiento**. Given/When/Then es solo la **formulación**, y la herramienta es la **automatización** |
| "El example map se hace en la Sprint Planning" | Se hace **antes**, en el **refinamiento** (30 min, 1 historia). La historia ya llega lista a la planning |
| "Si la gente dice que lo usaría, la idea está validada" | Lo dicho es **opinión**. Hace falta **comportamiento** (registrarse, pagar, volver) |
| "El Scrum Master es el jefe del equipo" | Es un **líder servicial** que facilita; el equipo se **autoorganiza** |
| "El Product Owner puede ser un comité" | Es **una persona** (puede representar a muchos interesados) |
| "La retro es para revisar el producto" | La retro mira el **proceso**; el producto se revisa en la **review** |
| "Story map = backlog dibujado en columnas" | Además de las columnas, la **posición vertical es prioridad** y se libera por **franjas horizontales** |
| "Walking skeleton = la funcionalidad más importante terminada" | Es una historia mínima de **cada** actividad: el recorrido completo, en su versión más flaca |
| "Buffer = sumarle 20% a cada tarea" | Es un colchón **explícito, compartido y gestionado** (de tiempo o de alcance), no un relleno escondido |
| "Todo es Must" | Si todo es Must, no hay prioridad ni colchón de alcance |
| "Velocidad alta = equipo mejor" | La velocidad es **propia** de cada equipo; sirve para **proyectar**, no para comparar |
| "Medimos todo lo que podamos" | **GQM**: primero el objetivo; medir sin objetivo genera métricas de vanidad |
| "Elegir entre velocidad o estabilidad" | DORA muestra que los mejores equipos tienen **las dos** |
| "Se testea con 30 usuarios para que sea serio" | Para encontrar problemas alcanzan **5 por ronda**; conviene hacer varias rondas |
| "Fijamos el criterio de éxito cuando veamos los resultados" | Se fija **antes**; si no, todo "confirma" la hipótesis |

---

## 13. Preguntas para practicar (sin mirar)

**Marco**
1. ¿Por qué, según *Artful Making*, el software se gestiona mejor como una obra de teatro que como una fábrica? ¿Qué papel juega el costo de iterar?
2. Diferenciá un modelo **iterativo** de uno **incremental** con un ejemplo.

**Descubrimiento y experimentos**

3. ¿Qué es un **Product Vision Board** y qué secciones tiene?
4. ¿Qué es la **lista de riesgos** del producto y en qué orden se atacan los supuestos?
5. Escribí una **hipótesis** con su criterio de éxito para un producto que conozcas.
6. Ordená por fuerza de evidencia: encuesta, landing con pre-venta, entrevista, A/B test en producción.
7. Diferenciá **output, outcome e impacto**.

**Métodos empíricos**

8. Nombrá los **4 métodos empíricos** y qué pregunta responde cada uno.
9. Explicá las reglas de *The Mom Test* y reformulá una mala pregunta.
10. ¿Cuáles son los **3 componentes** de un test de usabilidad? ¿Por qué alcanzan 5 usuarios?

**Scrum y BDD**

11. Describí las **3 responsabilidades** de Scrum con las características que dio la cátedra.
12. Nombrá los **5 eventos**, los **3 artefactos** y el **compromiso** de cada artefacto.
13. Definí BDD y explicá sus **3 actividades**.
14. Explicá los **4 colores** de Example Mapping. ¿Qué indica un mapa con muchas tarjetas rojas?
15. ¿Cuándo y cómo se hace el BDD Discovery dentro de Scrum?
16. ¿Por qué se prefiere el slicing **vertical**?

**Planificación y riesgos**

17. ¿Qué problema del backlog plano resuelve el **story map**? ¿Qué es el **walking skeleton**?
18. Con 120 puntos y una velocidad de 25 por sprint, ¿cuántos sprints necesitás? ¿Y con 20% de buffer?
19. ¿Qué campos tiene una **planilla de riesgos** y cómo se prioriza?
20. Explicá las **4 respuestas** a un riesgo con un ejemplo de cada una.
21. ¿Cómo se relacionan **MoSCoW** y **buffering**?

**Equipos y métricas**

22. Nombrá los **4 tipos de equipo** y los **3 modos de interacción** de Team Topologies.
23. ¿Qué dice la **Ley de Conway** y qué es la maniobra inversa?
24. ¿Qué diferencia hay entre la **review** y la **retrospectiva**?
25. Explicá **GQM** con un ejemplo.
26. Nombrá las **4 métricas DORA** y explicá por qué se miran juntas.

---

<sub>⚙️ Resumen armado con las guías 01–09, las slides de la clase 4 de la cátedra (*Desarrollo de Productos*), el material *Tipos de Experimentos de Producto*, el sample de *The BDD Books — Discovery*, el *Scrum Guide 2020*, Kate Moran (NN/g), *The Mom Test*, Jeff Patton y *Artful Making* (Austin & Devin). Las clases 5 a 8 no tienen slides publicadas en el repo: esas secciones salen del cronograma y la bibliografía citada. Cuando llegue material de esas clases, se ajusta acá.</sub>
