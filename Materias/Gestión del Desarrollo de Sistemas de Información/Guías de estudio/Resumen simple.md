# 🟢 Resumen simple — Gestión del Desarrollo de Sistemas Informáticos

> **Para qué es este archivo:** para **entender** la materia, no para memorizarla. Cada tema está contado en palabras simples, con una idea central, las definiciones que hay que saber y un ejemplo de la vida cotidiana.
> Leelo **primero**, de corrido. Cuando las ideas cierren, pasá al [Resumen para la Evaluación 1](Resumen%20para%20la%20Evaluaci%C3%B3n%201.md), que tiene el detalle, las tablas completas y los casos para practicar.
>
> **Evaluación 1 (lunes 26/10):** partes 1 a 9 (Producto). **Evaluación 2 (lunes 30/11):** partes 10 a 13 (Proyectos y Procesos).

---

## La materia en un párrafo

Hacer software es un trabajo **incierto**: casi nunca se sabe del todo qué hay que construir hasta que se empieza a construir y alguien lo usa. La materia enseña a **gestionar esa incertidumbre** en tres planos:
- el **producto**: ¿estamos haciendo lo correcto?;
- el **proyecto**: ¿llegamos con lo acordado, a tiempo y con la plata que hay?;
- el **proceso**: ¿cómo trabajamos cada vez mejor?

La respuesta de fondo, que viene del libro *Artful Making*, es siempre la misma: **probar en chiquito, mirar qué pasó y ajustar**, en lugar de planificar todo al principio y rezar.

---

# Bloque 1 · Producto (Evaluación 1)

## 1. El punto de partida: fábrica o teatro

**La idea:** hay dos maneras de hacer cosas.
- **Como una fábrica:** primero se diseña todo con precisión y después se ejecuta el plan sin desviarse. Sirve cuando cambiar es caro (construir un puente).
- **Como una compañía de teatro:** se ensaya, se prueba una escena de varias formas, se ve qué funciona y se ajusta. Sirve cuando cambiar es barato.

El software se parece al teatro: cambiar una pantalla o una regla cuesta poco, así que conviene **ensayar muchas veces** en vez de pretender acertar de entrada. Eso propone *Artful Making* (Austin y Devin).

**Las cuatro cualidades del "trabajo de artista"** según el libro:
- **Liberar:** el jefe suelta el control y deja explorar.
- **Colaborar:** las ideas de uno se apoyan en las de otro.
- **Elenco:** el grupo funciona como una unidad.
- **Jugar:** probar variantes y mostrarlas al público (el usuario).

**Los modelos de gestión, de más rígido a más flexible:**
- **En etapas (cascada):** primero todo el análisis, después todo el diseño, después todo el código. Si a mitad de camino cambia algo, duele mucho.
- **Iterativo:** se hace una versión y se la **mejora** en cada vuelta (como un dibujo que empieza en boceto).
- **Incremental:** se entrega **de a partes** que ya funcionan (primero la rueda, después el chasis).
- **Ágil:** combina las dos anteriores y se adapta al cambio con feedback frecuente.
- **Flujo continuo:** el trabajo entra y sale de a una tarea, sin "paquetes" (Kanban).

> 💡 **Para recordar:** iterativo = *mejorar lo mismo*; incremental = *agregar partes*.

---

## 2. Descubrir qué construir

**La idea:** el error más caro no es programar mal, es programar **algo que nadie necesita**. Por eso, antes de construir, se **descubre**: se averigua qué necesita la gente y se prueban las ideas de la forma más barata posible.

**Ejemplo cotidiano:** si vas a cocinar para alguien que no conocés, no preparás un banquete de cinco platos apostando a que le guste. Le das a probar algo chico, mirás su cara y ajustás.

**Definiciones que hay que saber:**
- **Product Vision Board:** una hoja que resume el producto: **por qué** existe (la visión), **para quién** es, **qué problema** le resuelve, **qué es** el producto y **qué gana** la organización.
- **Supuesto:** algo que **creemos** pero no comprobamos ("la gente va a pagar por esto").
- **Lista de riesgos:** los supuestos anotados, para atacar primero los más **peligrosos** (los que, si son falsos, hunden el producto).
- **Hipótesis:** un supuesto escrito para poder probarlo, con un número que diga si se cumplió ("si el 10% deja su mail, hay interés").
- **Experimento:** una prueba barata y rápida para confirmar o descartar una hipótesis.
- **Prototipo:** una versión "de mentira" del producto (dibujos, pantallas sin programar) para aprender sin construir.

**Output, outcome e impacto** (Jeff Patton):
- **Output** es lo que fabricamos (una función nueva).
- **Outcome** es lo que **cambia en la gente** (ahora compran sin llamar).
- **Impacto** es lo que gana la organización (bajan los costos).

El objetivo es lograr el mayor cambio con lo **mínimo** que haya que construir.

> 💡 **Para recordar:** primero aprender, después construir.

---

## 3. Aprender del usuario real

**La idea:** la gente es amable y te dice lo que querés escuchar. Por eso se **observa lo que hace**, no lo que dice que haría.

**Las cuatro formas de aprender del usuario:**

| Método | En palabras simples | Responde… |
| --- | --- | --- |
| **Test de usabilidad** | Le das el producto (o el prototipo), le pedís que haga algo y mirás dónde se traba | ¿Lo **puede usar**? |
| **Entrevista** | Le preguntás cómo resolvió el problema la última vez | ¿Qué **necesita**? |
| **Fake door / smoke test** | Mostrás un botón o una página de algo que todavía no existe y contás cuántos lo eligen | ¿Le **interesa** de verdad? |
| **Observación contextual** | Lo mirás trabajar en su lugar, como un aprendiz | ¿Cómo lo **hace en la realidad**? |

**El "Mom Test":** una buena entrevista es la que ni tu mamá podría responder con una mentira piadosa. Para lograrlo:
- se pregunta por **hechos pasados** ("¿qué hiciste la última vez?");
- no se pregunta por **opiniones** ("¿te gusta?") ni por el **futuro** ("¿lo usarías?").

**Dos experimentos que se confunden:**
- **Concierge:** hacés el servicio a mano y el cliente **sabe** que lo hace una persona.
- **Wizard of Oz** (el Mago de Oz): parece automático, pero **detrás hay una persona** haciéndolo en secreto.

**Test de usabilidad, en simple:**
- con **5 personas** ya aparecen casi todos los problemas;
- se les pide que **piensen en voz alta**;
- el que lo conduce **no ayuda**: si ayuda, el test ya no mide nada.

> 💡 **Para recordar:** lo que la gente **hace** vale más que lo que **dice**.

---

## 4. Scrum: construir de a sprints

**La idea:** en vez de construir todo de una, se trabaja en **ciclos cortos y fijos** (sprints, de 1 a 4 semanas). Al final de cada uno hay algo **terminado que se puede mostrar**, se recibe feedback y se decide qué sigue.

**Ejemplo cotidiano:** una serie que se filma por episodios. Cada episodio sale completo, el público opina y el siguiente se ajusta.

**Las tres responsabilidades:**
- **Responsable de Producto (Product Owner):** decide **qué** se hace y en qué orden. Es **una sola persona**, conoce el negocio y tiene la visión.
- **Desarrolladores:** **construyen**. Se organizan solos, saben hacer de todo un poco y responden **como equipo** (3 a 10 personas, según la cátedra).
- **Facilitador (Scrum Master):** **ayuda** a que el equipo trabaje bien y saca obstáculos del camino. No es el jefe; idealmente, casi no se nota.

**Los eventos (las reuniones):**
- **Planificación del sprint:** qué vamos a hacer y para qué.
- **Daily:** 15 minutos por día para ver cómo vamos.
- **Review:** mostramos lo hecho a los interesados.
- **Retrospectiva:** charlamos sobre **cómo trabajamos** y qué mejorar.

**Los artefactos (las "listas"):**
- **Product Backlog:** **todo** lo que podría hacerse en el producto, ordenado.
- **Sprint Backlog:** lo que se eligió para **este** sprint.
- **Incremento:** lo **terminado** en el sprint. "Terminado" significa que cumple la **Definition of Done**, una lista de condiciones que vale para todas las tareas.

**Los tres pilares:** **transparencia** (todo a la vista), **inspección** (mirar seguido cómo va) y **adaptación** (corregir).

> 💡 **Para recordar:** ciclos cortos + algo terminado al final de cada uno + mirar y corregir.

---

## 5. BDD y el mapa de ejemplos: ponerse de acuerdo con ejemplos

**La idea:** cuando alguien dice "quiero poder cambiar mi contraseña", cada persona se imagina algo distinto. **BDD** propone resolverlo **conversando con ejemplos concretos** antes de programar. Después esos ejemplos se convierten en **pruebas automáticas**.

**Definiciones:**
- **BDD (desarrollo guiado por comportamiento):** definir **cómo se tiene que comportar** el sistema mediante conversaciones y ejemplos, que después se automatizan como pruebas.
- **Sus 3 pasos:**
  - **descubrir** (conversar con ejemplos);
  - **formular** (escribirlos como escenarios: "dado… cuando… entonces…");
  - **automatizar** (convertirlos en pruebas).
- **Three Amigos:** la charla junta tres miradas: **negocio** (qué hace falta), **desarrollo** (cómo se construye) y **testing** (qué puede fallar).

**El mapa de ejemplos (Example Mapping)**, con tarjetas de colores:
- 🟨 **Amarilla:** la historia ("cambiar la contraseña").
- 🟦 **Azul:** las **reglas** ("la nueva tiene que ser distinta de la anterior").
- 🟩 **Verde:** **ejemplos** concretos de cada regla ("pone la misma → se rechaza").
- 🟥 **Roja:** las **dudas** que nadie pudo responder.

**Cómo se lee el mapa:**
- Si quedaron **muchas rojas**, falta información: la historia no está lista para programar.
- Si hay **muchas azules**, la historia es grande y conviene partirla.

**Cuándo se hace:** en reuniones **cortas** (30 minutos, una historia por vez), **antes** de que la historia entre al sprint. No en la planificación del sprint.

**Slicing (cómo partir el trabajo):** se corta en **rebanadas verticales**: cada una atraviesa todo (pantalla, lógica y datos) y ya sirve para algo. Lo contrario es cortar por capas ("primero toda la base de datos"): no se puede usar nada hasta el final.

> 💡 **Para recordar:** acordar con ejemplos, antes de programar.
>
> ⚠️ **Dos "descubrimientos" distintos:** el *descubrimiento de producto* decide **qué producto** hacer; el *de BDD* decide **cómo se comporta** una funcionalidad ya elegida.

---

## 6. Revisar el avance

**La idea:** al terminar un sprint se compara **lo que se construyó** con **lo que se esperaba**, y la vara son los ejemplos del mapa. Si el sistema hace lo que dice cada ejemplo, está bien. Así no se discute "a ojo" si algo está terminado.

**Dos reuniones que se confunden:**
- **Review:** se mira el **producto**, con los interesados. Pregunta: ¿construimos lo correcto?
- **Retrospectiva:** se mira **cómo trabajamos**, solo el equipo. Pregunta: ¿cómo trabajamos mejor?

**Otras dos cosas que se confunden:**
- **Criterios de aceptación:** las condiciones de **una** historia.
- **Definition of Done:** las condiciones que tienen que cumplir **todas** (tiene pruebas, está revisado, está publicado).

---

## 7. Planificar: el mapa de historias y el plan de entregas

**La idea:** una lista larga de tareas no muestra el "dibujo completo". El **story map** acomoda el trabajo en **dos dimensiones**:
- **de izquierda a derecha:** los pasos que hace el usuario (buscar → elegir → pagar → recibir);
- **de arriba hacia abajo:** lo **más importante arriba**.

**Ejemplo cotidiano:** no es una lista de compras desordenada, sino la lista **organizada por comidas**. De un vistazo se ve qué hace falta sí o sí para tener al menos un desayuno completo.

**Definiciones:**
- **Backbone (columna vertebral):** la fila de arriba, con los grandes pasos del usuario.
- **Walking skeleton (esqueleto que camina):** la versión **más flaca que funciona de punta a punta**: una tarea mínima de cada paso.
- **Release (entrega):** un corte **horizontal** del mapa. Se entrega una **franja completa**, no una columna.
- **Release plan:** qué franja sale en qué fecha.
- **Velocidad:** cuánto trabajo termina el equipo, en promedio, por sprint. Sirve para estimar cuántos sprints lleva algo.

> 💡 **Para recordar:** entregar el recorrido completo en versión pobre, y después mejorarlo.

---

## 8. Riesgos: anticipar lo que puede salir mal

**La idea:** en todo proyecto hay cosas que pueden salir mal. Ignorarlas no las hace desaparecer. Gestionar riesgos es **anotarlas, ordenarlas por importancia y decidir qué hacer con cada una**.

**Ejemplo cotidiano:** un viaje largo en auto. Pensás qué puede fallar (una goma, una ruta cortada), qué tan probable es y cuánto te complicaría, y llevás **rueda de auxilio y tiempo de sobra**.

**Definiciones:**
- **Riesgo:** algo que **puede** pasar y afectaría el proyecto. Si ya pasó, es un **problema**.
- **Planilla de riesgos:** la lista de riesgos con su probabilidad, su impacto y qué se va a hacer con cada uno.
- **Exposición:** probabilidad × impacto. Dice **cuál atender primero**.
- **Qué se puede hacer con un riesgo:**
  - **evitarlo** (cambiar el plan);
  - **mitigarlo** (achicarlo);
  - **transferirlo** (pasárselo a otro, como un seguro);
  - **aceptarlo** (asumirlo, con un plan B por si pasa).
- **MoSCoW:** separar lo que se hace en cuatro grupos:
  - **Must:** sí o sí;
  - **Should:** debería;
  - **Could:** si sobra tiempo;
  - **Won't:** esta vez no.
- **Buffer (colchón):** un margen **explícito** de tiempo o de alcance para absorber imprevistos. Los *Could* funcionan como colchón: si algo se complica, son lo primero que se cae.

> 💡 **Para recordar:** si todo es "Must", no hay de dónde recortar cuando algo sale mal.

---

## 9. Equipos, mejora y métricas

### Equipos y comunicación

**La idea:** el software lo hacen **personas**. Cómo se organizan y cómo se comunican se termina notando en el sistema que construyen.

- **Ley de Conway:** el sistema termina **copiando cómo se comunica la organización**. Si tres equipos casi no se hablan, el sistema va a tener tres partes que se integran mal.
- **Team Topologies**, los cuatro tipos de equipo:
  - **Alineado al flujo:** el equipo principal, dueño de una parte del producto.
  - **Plataforma:** da herramientas internas a los demás.
  - **Habilitador:** enseña y ayuda a otros equipos.
  - **Subsistema complicado:** se ocupa de una pieza muy especializada.
- **Carga cognitiva:** un equipo puede entender y mantener una cantidad limitada de cosas. Si se lo sobrecarga, empeora todo.
- **Equipos chicos:** cuanta más gente, más canales de comunicación hay que mantener (con 10 personas ya hay 45). Por eso se comunican mejor.
- **Conflictos:** son normales. Se discuten **ideas**, no personas.

### Mejora y métricas

**La idea:** para mejorar hay que **mirar datos**, no solo sensaciones. Pero hay que medir lo que importa.

- **Retrospectiva:** el equipo se pregunta qué funcionó, qué no y qué va a cambiar. Termina con **pocas acciones concretas**, no con una lista de quejas.
- **GQM (objetivo → pregunta → métrica):** primero se decide **qué se quiere lograr**, después qué preguntas lo responderían, y recién ahí **qué medir**.
- **Métricas DORA:** cuatro números para saber si un equipo entrega bien software:
  - dos de **velocidad**: cada cuánto publica y cuánto tarda un cambio en llegar al usuario;
  - dos de **estabilidad**: cuántas veces rompe algo y cuánto tarda en arreglarlo.

  Se miran **juntas**: los mejores equipos son rápidos **y** estables.
- **Ley de Goodhart:** cuando un número se vuelve el objetivo, la gente aprende a "inflarlo" y deja de medir lo que importaba.

> 💡 **Para recordar:** primero el objetivo, después el número.

---

# Bloque 2 · Proyectos y Procesos (Evaluación 2)

## 10. Gestionar un proyecto

**La idea:** un **proyecto** tiene principio y fin, y hay que acordar desde el arranque **para qué es, qué incluye y quién decide**.

- **Chartering / project charter:** el "acta de nacimiento" del proyecto. Un documento corto que fija:
  - el objetivo;
  - qué entra y qué no;
  - quiénes participan;
  - cómo se sabe si salió bien.
- **Stakeholders (interesados):** todas las personas a las que el proyecto afecta o que pueden afectarlo. No todos pesan igual: se ordenan según **cuánto poder** y **cuánto interés** tienen.
- **Alcance:** lo que el proyecto incluye. El peligro es que crezca sin control (*scope creep*).
- **WBS:** partir el trabajo grande en piezas cada vez más chicas, hasta que sean manejables.
- **Las 4 restricciones:** alcance, tiempo, costo y calidad. Si tocás una, se mueven las otras.

## 11. Estimar y hacer seguimiento

**La idea:** estimar es adivinar el futuro con poca información, así que conviene hacerlo de forma **honesta**.

- **Estimación relativa:** en vez de decir "esto lleva 13,5 horas", se dice "esto es el **doble** que aquello". Es más fácil y suele equivocarse menos. Se usan **puntos** o **talles de remera** (S, M, L).
- **Story points:** una unidad **relativa** de tamaño. No son horas.
- **#NoEstimates:** no estimar cada tarea, sino **medir cuánto tardan de verdad** y usar ese dato para proyectar.
- **Seguimiento por producto:** el avance se mide por lo **terminado y funcionando**, no por las tareas "empezadas".

## 12. Kanban: que el trabajo fluya

**La idea:** en vez de trabajar por sprints, las tareas **fluyen** de a una por un tablero: por hacer → haciendo → revisión → hecho.

**Ejemplo cotidiano:** la cocina de un restaurante. Si entran infinitos pedidos a la vez, la cocina colapsa y no sale ningún plato. Si se limita a "máximo 3 platos en preparación", salen más rápido.

- **WIP (trabajo en curso):** cuántas tareas hay empezadas al mismo tiempo. Kanban le pone un **límite**.
- **Sistema pull:** se toma trabajo nuevo **cuando hay lugar**, no cuando alguien lo empuja.
- **Diagrama de flujo acumulado (CFD):** un gráfico de bandas que muestra cuántas tareas hay en cada estado a lo largo del tiempo. Si una banda **se ensancha**, ahí hay un cuello de botella.
- **Lead time:** cuánto tarda una tarea desde que se pide hasta que se termina.
- **Kanban vs. Scrum:** Scrum trabaja por **ciclos fijos**; Kanban, por **flujo continuo** con límite de WIP. Se pueden combinar.

> 💡 **Para recordar:** empezar menos cosas para terminar más.

## 13. Mejorar como hábito: Toyota Kata

**La idea:** mejorar no es un evento aislado, es un **hábito** que se practica. La Toyota Kata propone avanzar **paso a paso hacia una meta**, probando cosas chicas.

**Ejemplo cotidiano:** escalar una montaña con niebla. Se ve la cima, pero no el camino entero. Entonces uno se fija el próximo refugio, prueba una ruta, ve qué pasa y ajusta.

- **Los 4 pasos:**
  1. **Visión:** adónde se quiere llegar a largo plazo.
  2. **Situación actual:** dónde estamos hoy.
  3. **Condición objetivo:** el próximo escalón alcanzable.
  4. **Experimentos:** probar, mirar el resultado, aprender y repetir (**PDCA**: planificar, hacer, verificar, actuar).
- **Indicadores:** números para saber si el proceso va bien.
- **Flujo de fondos:** controlar la plata que entra y sale del proyecto.
- **Auditoría:** alguien independiente revisa que las cosas se hagan como corresponde.
- **Gestión de contrataciones:** manejar proveedores y contratos con terceros.

---

## 🧠 Todo junto, en una tabla

| Tema | La pregunta que responde | La idea en una frase |
| --- | --- | --- |
| Artful Making | ¿Cómo encarar algo incierto? | Ensayar barato muchas veces en vez de planificar todo |
| Descubrimiento | ¿Qué construimos? | Probar las ideas antes de construirlas |
| Métodos empíricos | ¿Qué necesita el usuario de verdad? | Mirar lo que hace, no lo que dice |
| Scrum | ¿Cómo construimos? | Ciclos cortos con algo terminado al final de cada uno |
| BDD y mapa de ejemplos | ¿Qué tiene que hacer exactamente esta función? | Acordar con ejemplos concretos antes de programar |
| Revisión de avance | ¿Lo que hicimos está bien? | Compararlo con los ejemplos acordados |
| Story map y release plan | ¿Qué entregamos primero? | El recorrido completo en versión mínima, después mejorarlo |
| Riesgos | ¿Qué puede salir mal? | Anotarlo, ordenarlo y tener colchón |
| Equipos | ¿Cómo nos organizamos? | La estructura del equipo se nota en el sistema |
| Métricas | ¿Mejoramos de verdad? | Primero el objetivo, después el número |
| Proyectos | ¿Para qué es y qué incluye? | Acordarlo por escrito al principio |
| Estimaciones | ¿Cuánto falta? | Comparar tamaños en vez de inventar horas |
| Kanban | ¿Cómo hacemos que fluya? | Limitar lo que está empezado |
| Toyota Kata | ¿Cómo mejoramos siempre? | Pasos chicos hacia una meta, aprendiendo en cada uno |

---

<sub>⚙️ Versión simplificada de las [guías de la materia](README.md) y del [Resumen para la Evaluación 1](Resumen%20para%20la%20Evaluaci%C3%B3n%201.md). Si algo de acá no alcanza para responder una pregunta, la versión completa está ahí.</sub>
