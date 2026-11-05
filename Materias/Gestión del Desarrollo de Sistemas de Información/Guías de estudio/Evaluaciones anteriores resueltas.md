# 📝 Evaluaciones anteriores resueltas — GDSI

> Cuatro evaluaciones de la misma cátedra (Gabardini y Fontdevila), transcriptas y resueltas con respuestas modelo:
> 1. [**Evaluación 1 del 26/10/2026**](#evaluación-1-del-26102026-2c-2026--caso-clubplay) (**esta cursada**, caso ClubPlay). Es la más parecida a lo que puede venir en el **recuperatorio del 16/11**.
> 2. [**Evaluación I del 27/10/2025**](#evaluación-i-del-27102025-2c-2025--caso-midfood) (caso MidFood).
> 3. [**Evaluación I del 18/5/2026**](#evaluación-i-del-1852026-1c-2026--caso-reciclaplus) (1C 2026, caso ReciclaPlus), con **lo que marcó el corrector**.
> 4. [**Simulación de Evaluación I**](#simulación-de-evaluación-i) (sin caso: riesgos y actividades de un grupo que arranca).
>
> Las consignas salen de fotos, enunciados y exámenes corregidos que circulan entre alumnos ([fuentes en Material extra](../Material%20extra/README.md)). Acá está solo el **texto de la consigna**, sin los nombres de quienes las rindieron.
>
> Para estudiar la teoría: [Resumen para la Evaluación 1](Resumen%20para%20la%20Evaluaci%C3%B3n%201.md) · [Resumen simple](Resumen%20simple.md).

---

## 🔁 Lo que se repite

| Tema | 2C 2025 | 1C 2026 | Simulación | **2C 2026** |
| --- | --- | --- | --- | --- |
| **Story map dibujado** con dos o más roles y la línea de corte de la primera release | 30% | 30% | — | **20%** (+ 10% de release plan con fecha fija) |
| **Riesgos** y **actividades o experimentos** que los atacan | dentro del ciclo de Patton (20%) | dentro del ciclo de Patton (20%) + múltiple choice | 50% | **20%** (criticar un plan de validación) |
| **Example Mapping** | múltiple choice | múltiple choice | múltiple choice | **20%** (armar uno completo) |
| **Experimentos**: qué son o cómo leer un resultado | explicarlo (10%) | ubicarlos en el ciclo de Patton | — | **10%** (leer un *fake door*) |
| **Review vs. Retrospective** | 20% | 20% | — | — |
| **Múltiple choice de *Artful Making*** | *ensemble* | *release* y *play* | costo de iteración | — |
| **Partir historias** en cortes verticales | — | — | — | **10%** |
| **Métricas** de resultado (no de vanidad) | — | — | — | **10%** |

> 🔑 **La de esta cursada no preguntó definiciones de memoria:** todo fue **aplicar al caso**, y cada opción del múltiple choice pedía justificar en dos o tres líneas.
>
> **Para el recuperatorio, practicá:**
> - un **story map dibujado** con su línea de corte;
> - un **example map** con reglas, ejemplos y preguntas;
> - una tabla **riesgo → experimento**;
> - y tené a mano **slicing vertical**, **alcance variable con fecha fija** y **outcome vs. output**.

---

## Evaluación 1 del 26/10/2026 (2C 2026) · caso ClubPlay

Es la Evaluación 1 de esta cursada. Seis preguntas, **todas sobre el mismo caso**.

| Pregunta | Tipo | Tema | Peso |
| --- | --- | --- | --- |
| 1 | Múltiple choice justificado | Leer el resultado de un *fake door* | 10% |
| 2 | Múltiple choice justificado | Partir una historia en historias usables | 10% |
| 3 | Desarrollo | Criticar un plan de validación: riesgo → cómo evaluarlo mejor | 20% |
| 4 | Caso | Story map con línea de corte (a, 20%) + plan con fecha fija (b, 10%) | 30% |
| 5 | Caso | Example Mapping de una historia (a, 15%) + qué hacer con una historia llena de preguntas (b, 5%) | 20% |
| 6 | Desarrollo | Por qué "3.000 descargas" no alcanza y qué medir | 10% |

### La consigna

**Caso común a todas las preguntas:**

> **ClubPlay** es una idea de app para clubes de barrio que alquilan canchas de fútbol 5.
>
> **Cómo es hoy:**
> - Los jugadores reservan por WhatsApp o llamando al club.
> - El encargado anota en un cuaderno o en una planilla, pide una seña por transferencia y muchas veces no se entera de que el grupo canceló hasta que no aparece nadie.
> - Del lado de los jugadores, es común que a último momento falten uno o dos para completar el equipo, y el partido se suspende o se juega con menos.
>
> **La propuesta:**
> - El jugador ve los horarios libres de los clubes cercanos, reserva y paga la seña desde la app.
> - Puede publicar "**nos falta uno**" para que otros jugadores de la zona se sumen.
> - El encargado del club ve su grilla de reservas al día y cobra las señas sin tener que perseguir a nadie.
>
> **Hasta ahora**, el equipo tiene un **prototipo clickable** y habló con los encargados de **dos clubes**, que se mostraron interesados.

**1.** (10%) El equipo publicó una landing de ClubPlay con un botón "Encontrá los jugadores que te faltan". Al hacer clic aparece un cartel de "Próximamente". De 400 visitantes, 70 hicieron clic. ¿Cuál es la conclusión más defendible? Marcá una opción y justificá en dos o tres líneas.
- a. Quedó validado que los jugadores se suman a partidos con desconocidos, así que se puede empezar a construir esa funcionalidad.
- b. Hay una señal de interés en la propuesta tal como se presentó, pero no dice si alguien efectivamente va a ir a jugar con desconocidos ni si los partidos se completan.
- c. Como menos del 20% hizo clic, la funcionalidad no resulta atractiva y conviene descartarla.
- d. El resultado muestra que la pantalla es fácil de usar, porque los visitantes encontraron el botón y lo usaron.

**2.** (10%) La historia "Como jugador quiero reservar una cancha y pagar la seña" no entra en una entrega, así que el equipo la va a dividir en historias más chicas que se terminen una después de otra. ¿Con cuál de estas propuestas, al terminar cada historia, queda algo que se puede poner en las manos de un usuario? Marcá una opción y justificá en dos o tres líneas.
- a. Primero las pantallas de reserva y de pago; después los servicios de reservas y de pagos; por último la base de datos y la conexión con la pasarela de pago.
- b. Primero la versión completa para el jugador; después la versión completa para el encargado del club.
- c. Primero reservar un horario libre en un club, pagando la seña en el club como hoy; después pagar la seña desde la app; por último cancelar una reserva y recuperar la seña.
- d. Primero reservar y pagar con clubes y horarios de prueba cargados por el equipo; después dar de alta los clubes reales con sus horarios.

**3.** (20%) Para decidir si seguir adelante, el equipo arma este plan para las próximas dos semanas:
1. Una encuesta online a 200 jugadores con dos preguntas: "¿Usarías una app para reservar cancha y pagar la seña?" y "Del 1 al 5, ¿cuánto te molesta que falte gente para jugar?".
2. Mostrarles el prototipo a los encargados de los dos clubes interesados y preguntarles qué les parece.
3. Si los resultados dan bien, empezar a desarrollar la app completa para esos dos clubes.

¿Qué opinás del plan? Para cada punto que te preocupe, completá: "*Veo este posible riesgo: … Creo que lo podríamos evaluar mejor si hacemos: …*". Usá y nombrá las herramientas y conceptos de la materia que apliques.

**4.** (30%) Release plan de ClubPlay.
- a. (20%) Construí un story map de ClubPlay con:
  - las actividades del recorrido de uso;
  - 2 o 3 historias por actividad (considerá al menos al Jugador y al Encargado del club);
  - la línea de corte de la primera release, justificada.

  Dibujá el story map.
- b. (10%) El dueño avisa: "*La temporada de verano arranca el 1/12. Si para esa fecha no está la primera release, los clubes arman la grilla de verano por WhatsApp como siempre y perdemos el año*". Quedan **3 sprints de dos semanas**. El equipo calcula que, entre construir, probar con un club piloto y ajustar lo que surja de esa prueba, la primera release tal como la cortaste lleva **entre 3 y 5 sprints**. ¿Cómo armarías el plan?

**5.** (20%) Tomá la historia "Como jugador quiero cancelar mi reserva y recuperar la seña".
- a. (15%) Construí su Example Mapping: al menos 3 reglas, 1 o 2 ejemplos concretos por regla y al menos 1 pregunta abierta.
- b. (5%) En otra historia, el equipo terminó la sesión de Example Mapping con **3 reglas y 6 preguntas sin responder**. El Product Owner propone meterla igual en el próximo sprint "para no frenar". ¿Qué opinás?

**6.** (10%) Tres meses después del lanzamiento en cinco clubes, el equipo presenta en la Review: "*Vamos muy bien: ya tenemos 3.000 descargas*". ¿Qué opinás de esa medida para saber si a ClubPlay le está yendo bien? Proponé qué medirías vos y explicá cómo llegaste a esas medidas.

### Respuestas modelo

> Son respuestas propias, armadas con el material de la cátedra. No hay corrección oficial de esta evaluación.

**1 → b.**
- **Qué mide un *fake door*:** el **interés** en la promesa tal como se presentó. El 17,5% (70 de 400) es una señal.
- **Qué no mide:** un clic es barato y **no es el comportamiento** que importa: ir a jugar con desconocidos y que el partido se complete.
- **Por qué no las otras:**
  - **a** sobreinterpreta el resultado: un clic no valida el comportamiento real.
  - **c** inventa un umbral **después** de ver el dato. El criterio de éxito se fija **antes** del experimento.
  - **d** confunde interés con **usabilidad**, que se mide con tareas en un test de usabilidad.
- **Siguiente paso:** un experimento que mida conducta. Por ejemplo, un **concierge**: el equipo arma a mano, por WhatsApp, los partidos a los que les falta uno en un club, y cuenta cuántos se completan.

**2 → c.**
- Es un **corte vertical**: cada historia deja algo que un usuario real puede usar.
  1. Reservar pagando la seña en el club, como hoy, ya sirve.
  2. Después se suma pagar la seña desde la app.
  3. Por último, cancelar y recuperar la seña.
- **Por qué no las otras:**
  - **a** es un corte **horizontal** por capas técnicas: hasta la última nada funciona.
  - **b** parte por rol: cada mitad sigue siendo enorme, y la versión del jugador sin el encargado no sirve, porque alguien tiene que cargar y ver la grilla.
  - **d** deja una demo con **datos de prueba**: nadie puede reservar una cancha real hasta la segunda historia.

**3. Qué me preocupa del plan:**

| Punto del plan | Veo este posible riesgo… | Creo que lo podríamos evaluar mejor si hacemos… |
| --- | --- | --- |
| 1 · Encuesta "¿usarías…?" | **Valor/atractivo.** Pregunta por **opiniones y conducta futura**, y la gente contesta que sí por cortesía. Además mezcla **dos problemas** (reservar y que falte gente) con riesgos distintos | **Entrevistas de problema (*Mom Test*)** sobre hechos pasados: "¿cuándo fue la última vez que se suspendió un partido? ¿qué hicieron?". Para medir conducta, el ***fake door*** de la pregunta 1 o una **landing con reserva real** |
| 1 · Encuesta | **El otro lado:** "nos falta uno" necesita **muchos jugadores en la zona** para funcionar. Si no hay masa crítica, nadie se suma | **Concierge** en un barrio: un grupo de WhatsApp de la zona que el equipo maneja a mano. Se mide cuántos pedidos de "falta uno" se completan |
| 2 · Mostrar el prototipo y preguntar "qué les parece" | **Usabilidad**, con **sesgo**: son solo dos encargados, ya entusiasmados (los primeros de la curva de adopción, no representativos), y se les pide **opinión** | **Test de usabilidad** con **tareas** ("cargá los horarios del sábado", "registrá que el grupo de las 20 canceló"): facilitador que no induce, unos **5 participantes** y encargados de **otros clubes**, no solo los dos interesados |
| 2 · Prototipo | **Negocio:** nadie preguntó **quién paga** ni cuánto (comisión por seña o abono del club) | **Preventa** o compromiso: proponerles a los clubes un **piloto pago** o una comisión, y ver si aceptan. También sirve un **Buy-a-Feature** con encargados para priorizar |
| 3 · "Si da bien, la app completa" | **No hay criterio de éxito:** "dar bien" no está definido, y cualquier resultado se puede leer como bueno | Escribir cada experimento como **hipótesis con criterio de éxito decidido antes**. Por ejemplo: "si 6 de 10 jugadores contaron un partido suspendido en el último mes, seguimos" |
| 3 · App completa | **Riesgo de producto y de plazos:** construir todo de una vez es lo contrario a entregar de a poco. Se aprende tarde y caro | **Story map** con un **walking skeleton** / release mínima para **un club piloto**, entregando incrementos y midiendo |
| 3 · Pagos | **Técnico:** cobrar señas y **devolverlas** desde la app (pasarela de pago) | Un **spike** corto con la pasarela antes de comprometer la funcionalidad |

**4a. Story map.**

**Supuestos:**
- La primera release es para **un club piloto**.
- El club tiene MercadoPago para cobrar señas.

| | 1 · Publicar la grilla (Encargado) | 2 · Buscar un horario (Jugador) | 3 · Reservar y señar (Jugador) | 4 · Gestionar el día (Encargado) | 5 · Cancelar (Jugador) | 6 · Completar el equipo (Jugador) |
| --- | --- | --- | --- | --- | --- | --- |
| **Release 1** | Como encargado, quiero cargar mis canchas y horarios de la semana | Como jugador, quiero ver los horarios libres **de mi club** | Como jugador, quiero reservar un horario y **pagar la seña con un link de pago** | Como encargado, quiero ver la **grilla del día** con quién reservó y si señó | Como jugador, quiero cancelar mi reserva **y que el club se entere** | — |
| *línea de corte* | | | | | | |
| **Release 2** | Como encargado, quiero bloquear horarios (torneos, lluvia) | Como jugador, quiero ver los horarios de **los clubes cercanos** | Como jugador, quiero reservar un **turno fijo semanal** | Como encargado, quiero ver las señas cobradas del mes | Como jugador, quiero **recuperar la seña** según la política del club | Como jugador, quiero publicar "**nos falta uno**" |
| **Release 3+** | Como encargado, quiero poner precios distintos por franja | Como jugador, quiero filtrar por día, hora y precio | Como jugador, quiero dividir la seña con mis compañeros | Como encargado, quiero ver qué horarios quedan vacíos para hacer promociones | Como jugador, quiero transferir mi reserva a otro grupo | Como jugador, quiero sumarme a un partido que publicó otro / calificar a los jugadores |

**Por qué se corta ahí:**
- La release 1 es el **corte horizontal más fino que cierra el flujo de punta a punta**: el encargado publica, el jugador reserva y seña, el encargado ve la grilla y se entera de las cancelaciones.
- Ataca el dolor **más claro y más validado**: el de los encargados (la seña sin perseguir a nadie y saber quién canceló). Es lo que los clubes necesitan para armar la **grilla de verano**.
- "**Nos falta uno**" queda afuera por tres razones:
  - solo tiene una señal de interés (el *fake door*), no de conducta;
  - necesita masa crítica de jugadores en la zona;
  - con un solo club no tiene sentido.

  Se valida en paralelo con un concierge.

**4b. Plan con fecha fija.**
- **La fecha no se mueve:** si llega tarde, se pierde el año. Entonces, según el **triángulo de hierro**, fijamos **tiempo** (3 sprints) y **equipo**, y lo que se ajusta es el **alcance**. **No la calidad.**
- **Sumar gente ahora** no ayuda: entrenar a los nuevos le saca tiempo al equipo.
- **Estimar en rango:** 3 sprints es el **mejor caso** de un rango de 3 a 5. Prometer la release entera sería apostar al escenario optimista. Se lo digo así al dueño, y acordamos qué es lo **imprescindible** para el 1/12.
- **Recortar la release a lo mínimo para el verano:**
  - grilla del encargado;
  - reserva del jugador;
  - aviso de cancelación;
  - seña **pagada en el club como hoy**, o por **link de pago** sin integración (la división de la pregunta 2).

  La seña integrada y la devolución automática pasan a después del 1/12.
- **Plan por sprint:**

  | Sprint | Qué se hace |
  | --- | --- |
  | **1** | **Walking skeleton** de punta a punta (cargar la grilla, reservar, ver la grilla) en el **club piloto** desde el final del sprint. El piloto no queda para el final: es lo que más aprendizaje y más incertidumbre trae |
  | **2** | Ajustes del piloto + aviso de cancelación + link de pago. Si hay tiempo, segundo club |
  | **3** | **Reservado para ajustes** y para abrir a los demás clubes. Nada nuevo grande |

- **Seguimiento:** **burn-up** sprint a sprint, y en cada **Review** el PO decide qué queda afuera si no llegamos.
- **Contingencia:** si al final del sprint 2 la reserva desde la app no está estable, los clubes usan **solo la grilla en la app** y siguen tomando reservas por WhatsApp. Igual se evita el cuaderno y no se pierde la temporada.

**5a. Example Mapping de "Como jugador quiero cancelar mi reserva y recuperar la seña".**

Supuesto: la política la define el club; para el ejemplo, 24 h.

| 🟦 Regla | 🟩 Ejemplos |
| --- | --- |
| **Cancelando con 24 h o más de anticipación, se devuelve el 100% de la seña** | Lucas reservó el sábado a las 20:00 y cancela el viernes a las 18:00 (26 h antes) → recupera los $10.000 · Cancela el viernes a las 20:00, **justo 24 h antes** → recupera el 100% |
| **Cancelando con menos de 24 h, no se devuelve la seña** | Lucas cancela el sábado a las 15:00 para las 20:00 → no recupera nada, pero la cancha igual se libera |
| **Al cancelar, el horario vuelve a quedar libre y el encargado recibe un aviso** | Lucas cancela el turno del sábado 20:00 → aparece libre en la grilla y al encargado le llega la notificación |
| **Solo quien hizo la reserva puede cancelarla** | Martín se sumó al partido de Lucas e intenta cancelar la reserva → no puede |
| **La seña se devuelve por el mismo medio con el que se pagó** | Lucas pagó con MercadoPago → la devolución vuelve a su cuenta de MercadoPago |

🟥 **Preguntas abiertas:**
- Si **el club** cancela (lluvia, cancha rota), ¿se devuelve el 100% siempre?
- ¿El plazo de 24 h lo define cada club o es igual para todos?
- ¿Quién paga la comisión de la pasarela cuando se devuelve la seña?
- Si el grupo quiere **cambiar de horario** en vez de cancelar, ¿es una cancelación?

**5b. Meterla igual "para no frenar": no conviene.**
- **Qué indican 6 preguntas contra 3 reglas:** la historia **todavía no se entiende**. Es la señal de que no está lista: es grande o tiene mucha incertidumbre.
- **Qué pasa si entra igual:** el equipo se frena **adentro** del sprint esperando respuestas, o construye suponiendo y después **rehace**. Las tarjetas "vuelven para atrás" y la historia no termina.
- **Qué propongo:**
  - que alguien (el PO) se haga **responsable de responder las preguntas** antes del sprint;
  - **partir** la historia, tomando ahora solo la parte cuyas reglas ya están claras;
  - si alguna pregunta es técnica, un **spike**.
- **Así no se frena:** entra otra historia que ya esté lista. "No frenar" no es lo mismo que empezar algo que no se entiende.

**6. "3.000 descargas" no dice si ClubPlay funciona.**
- **Es una métrica de vanidad:**
  - siempre sube;
  - no dice si la app se **usa** ni si cambió algo;
  - no ayuda a decidir.
- **Es *output*, no *outcome*:** no muestra un cambio de comportamiento de los usuarios.
- **Ejemplo:** 3.000 personas pudieron descargarla una vez y no volver nunca, y los clubes seguir tomando reservas por WhatsApp.

**Cómo llego a qué medir (GQM: objetivo → preguntas → métricas):**

| Objetivo (*outcome* buscado) | Pregunta | Métrica |
| --- | --- | --- |
| Los clubes gestionan sus reservas con ClubPlay | ¿Los clubes la usan en lugar de WhatsApp? | **% de las reservas** de los 5 clubes hechas por la app, mes a mes |
| | ¿Siguen con nosotros? | Clubes activos (o que pagan) al tercer mes |
| Los encargados no pierden plata por cancelaciones | ¿Bajaron los turnos perdidos? | **Turnos perdidos sin aviso** por club y por mes, **antes vs. después** |
| | ¿Cobran la seña sin perseguir? | % de reservas con seña cobrada |
| Los jugadores vuelven a usarla | ¿Reservan más de una vez? | **Jugadores que reservan 2 o más veces por mes** (retención) |
| Los partidos se completan | ¿Funciona "nos falta uno"? | % de publicaciones "nos falta uno" que se completan |

> 🔑 Para cada métrica conviene tener la **línea de base** (cómo era antes de ClubPlay) y mirarlas **juntas**: una sola se puede inflar.

---

## Evaluación I del 27/10/2025 (2C 2025) · caso MidFood

### Cómo es la evaluación (2C 2025)

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

### La consigna

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

### Respuestas modelo

#### 1 → **b**

*Ensemble* es "la cualidad del trabajo de un grupo dedicado a colaborar, en el que cada miembro **cede soberanía sobre su trabajo** y así crean algo que ninguno podría haber hecho solo: **un todo mayor que la suma de sus partes**" (Austin y Devin, cap. 1).

Por qué las otras opciones están mal:
- **a** confunde *release* (un modo de control) con "liberar versiones".
- **c** describe la iteración barata, la base de todo el libro, pero no *ensemble*.
- **d** malinterpreta *play*, que no es "juego sin estructura" sino la interacción con el público y entre el grupo.

#### 2 → **c**

Las tarjetas **rojas** registran lo que **nadie pudo responder** en la conversación. Se anotan y se sigue, en vez de trabar la reunión. Alguien se hace responsable de responderlas antes de que la historia entre al sprint.

Las otras opciones describen otras tarjetas:
- **a** = ejemplos (verdes);
- **b** = reglas (azules);
- **d** = historia (amarilla).

#### 3 · Review vs. Retrospective

| | **Sprint Review** | **Sprint Retrospective** |
| --- | --- | --- |
| Qué se inspecciona | **El producto**: el incremento del sprint (el *qué*) | **El proceso**: cómo trabajó el equipo (el *cómo*) |
| Quiénes | Equipo Scrum + **stakeholders** | Solo el **equipo Scrum** |
| Para qué | Recibir feedback y **adaptar el Product Backlog** | Detectar causas y **comprometer mejoras** para el próximo sprint |
| Ejemplo (MidFood) | Se muestra la recepción automática de pedidos de ComidaYA. El gestor de un restaurante pide que las alertas lleguen también por WhatsApp → se agrega al backlog | Dos historias quedaron sin terminar porque el ambiente de prueba del scraping se caía. Se analiza con 5 porqués y el equipo se compromete a automatizar su recreación |

> ⚠️ **El corrector marcó un error típico.** El alumno escribió que los stakeholders "son quienes usan el producto" y le anotaron "**no necesariamente**". Stakeholder = **interesado** (cliente, sponsor, gerencia, usuarios…), no solo usuario.

#### 4 · Ciclo de producto de Patton aplicado a MidFood

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

#### 5 · Qué es un experimento, explicado con MidFood

> **Respuesta modelo:**
> - **Qué es:** un experimento es una **prueba chica y barata** para averiguar si una idea es cierta **antes** de invertir en construirla.
> - **Cómo se arma:** se parte de un supuesto riesgoso, se escribe como hipótesis con un **criterio de éxito decidido de antemano**, se prueba con usuarios reales y se mira **qué hacen**, no solo qué opinan.
> - **Ejemplo con MidFood:**
>   - **Supuesto:** "los restaurantes van a pagar para no cargar pedidos a mano".
>   - **Experimento (Wizard of Oz):** antes de programar las integraciones, durante dos semanas una persona del equipo copia **a mano** los pedidos de Pedidos Ya al CRM de 5 restaurantes. Para ellos, el servicio parece automático.
>   - **Medición:** pedidos perdidos antes y durante la prueba, y cuántos restaurantes aceptan **pagar** para seguir.
>   - **Criterio de éxito:** si al menos 3 de 5 aceptan pagar, la idea tiene valor y avanzamos.
>   - **Qué gana:** se aprende mucho gastando muy poco.

#### 6 · Story map de MidFood

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

## Evaluación I del 18/5/2026 (1C 2026) · caso ReciclaPlus

Aprobación (nota 4) con el **60%**. El encabezado pide: "**Explicitá tus hipótesis**" y "**Validá que lo que escribís cumpla lo pedido en el enunciado**".

> ℹ️ El texto está **verificado contra el enunciado digital** del examen. Su tabla de puntajes del encabezado no coincide con la de las preguntas (dice 3 = 30%, 4.a = 30% y 4.b = 20%). Acá se usan los pesos **de cada pregunta** (1, 2 y 3: 10% cada una; 4: 20%; 5: 20%; 6: 30%), que son los que usó el corrector.

| Pregunta | Tipo | Tema | Peso |
| --- | --- | --- | --- |
| 1 | Múltiple choice | *Artful Making*: *release* y *play* | 10% |
| 2 | Múltiple choice | Example Mapping: regla vs. ejemplo | 10% |
| 3 | Múltiple choice | Gestión adaptativa de riesgos | 10% |
| 4 | Caso | Ubicar 4 experimentos en el ciclo de Patton (a) y decir qué riesgo atacan (b) | 20% |
| 5 | Caso | Review vs. Retrospective: propósito (a) y un tema concreto para cada una (b) | 20% |
| 6 | Caso | Story map con al menos dos roles, MVP y justificación del recorte | 30% |

### La consigna

**1.** En el marco de *Artful Making*, ¿qué relación describe correctamente "release" y "play"?
- a. "Release" implica liberar versiones al usuario y "play" es la celebración del equipo al terminar una iteración.
- b. "Release" es soltar el control para permitir la autonomía y la exploración, y "play" es el espacio seguro para experimentar y la obra que producimos.
- c. "Release" se refiere a la documentación liberada al cliente y "play" a los juegos de roles entre desarrolladores.
- d. Ambos son sinónimos: refieren a la flexibilidad del equipo frente al cambio.

**2.** En Example Mapping, ¿cuál es la diferencia principal entre una "regla" y un "ejemplo"?
- a. La regla es un caso concreto y el ejemplo es la generalización abstracta.
- b. La regla expresa un comportamiento esperado del sistema; el ejemplo es una instancia concreta que la ilustra o valida.
- c. La regla se escribe en lenguaje técnico y el ejemplo en lenguaje de negocio.
- d. No hay diferencia, son dos formas de nombrar lo mismo en distintos contextos.

**3.** En el contexto de gestión adaptativa de riesgos, ¿cuál de las siguientes afirmaciones es la más adecuada?
- a. Los riesgos se identifican al inicio del proyecto en una matriz y se revisan al cierre del mismo.
- b. La gestión de riesgos se delega a un rol especializado externo al equipo de desarrollo.
- c. La identificación y mitigación de riesgos es continua: se revisan en planificación, dailies y retrospectivas, y se mitigan tempranamente.
- d. Los riesgos técnicos se priorizan siempre por encima de los de negocio o atractivo.

**Situación para las preguntas 4 a 6** (*"Explicitá cualquier supuesto que tomes"*):

> Estás desarrollando **ReciclaPlus**, una plataforma digital que busca conectar a **vecinos** de barrios urbanos con **cooperativas de clasificadores** de residuos reciclables y con **empresas que compran materiales** (papel, plástico, vidrio, metal).
>
> La idea es que los vecinos puedan **agendar retiros a domicilio** de materiales reciclables clasificados. Las cooperativas reciben las solicitudes en una app móvil, optimizan recorridos y coordinan a los clasificadores que las visitan.
>
> Para una **2da etapa**, también podremos incorporar a las **empresas compradoras**: ven el stock disponible por cooperativa y hacen pedidos. El **gobierno municipal** apoya el proyecto con subsidios por kilo reciclado y exige reportes periódicos del impacto ambiental (kilos retirados, hogares activos, cooperativas operando).
>
> Las principales dificultades detectadas son:
> - Los vecinos no siempre clasifican correctamente y a veces dejan materiales no reciclables.
> - Las cooperativas tienen baja alfabetización digital y poca infraestructura tecnológica.
> - La logística de retiros es compleja por la dispersión geográfica.
> - No es claro aún si los vecinos sostendrán el hábito en el tiempo.

**4. Ciclo de mejora de producto (Jeff Patton)** (20%):
- a. Para cada uno de estos cuatro experimentos, indicá en qué etapa del ciclo (Sense, Focus, Discovery o Delivery) lo usarías y justificá. Si alguno podría ir en más de una etapa, indicá la principal y por qué.
  - **Observación etnográfica:** acompañar a un grupo de vecinos durante su rutina diaria para observar cómo manejan sus residuos hoy, sin intervenir.
  - **Buy-a-Feature:** en un taller con cooperativas, les das un presupuesto ficticio para "comprar" funcionalidades de una lista, y ves cuáles eligen primero.
  - **Wizard of Oz MVP:** la app le muestra a las cooperativas una ruta optimizada de retiros, pero detrás un operador la arma manualmente con Google Maps.
  - **A/B Testing:** en producción, dos versiones de la pantalla de agendamiento (una con recordatorio el día anterior, otra sin él) para ver cuál genera más confirmaciones.
- b. Para el experimento que ubicaste en Sense y el que ubicaste en Discovery, indicá a qué tipo de riesgo (negocio, atractivo, técnico o de usabilidad) apunta a mitigar y por qué.

**5. Ceremonias de Scrum: Review y Retrospectiva** (20%):
- a. Diferenciá brevemente el propósito de la Review y la Retrospectiva. ¿Qué se busca mejorar en cada una?
- b. El equipo terminó el sprint donde lanzó el agendamiento de retiros en un barrio piloto. Proponé un tema concreto para discutir en la **Review** y uno para la **Retrospectiva**. Justificá por qué cada tema corresponde a esa ceremonia y no a la otra.

**6. Story Map** (30%). Diseñá un story map para ReciclaPlus. Considerá al menos dos roles entre *Vecino*, *Clasificador (cooperativa)* y *Empresa compradora* (podés sumar un tercero, por ejemplo el municipio).
1. Definí las columnas principales (objetivos o actividades secuenciales del usuario a lo largo del recorrido de uso del producto).
2. Para cada columna, proponé al menos 2–3 historias de usuario, indicando a qué rol corresponden.
3. Priorizá visualmente: arriba lo que entraría en un MVP, abajo lo que dejarías para iteraciones futuras.
4. Justificá brevemente el criterio del recorte del MVP.

*Importante: mantené la respuesta estructurada y concisa; dibujá el story map.*

### Respuestas modelo

**1 → b.** Es la que acepta la cátedra. Ojo con cómo lo dice: "soltar el control para permitir la autonomía y la exploración". El libro lo matiza: *release* **sigue siendo control**, pero fija los límites y deja variar adentro. Las dos ideas son compatibles. La trampa es la **a**: *release* **no es** "liberar versiones".

**2 → b.** La **regla** (tarjeta azul) es el comportamiento esperado, general. El **ejemplo** (verde) es un caso concreto que la ilustra y sirve de prueba. La **a** los da vuelta.

**3 → c.** La gestión adaptativa revisa los riesgos **todo el tiempo** (planning, daily, retro) y los ataca **temprano**. La **a** es la gestión tradicional (matriz al inicio, revisión al cierre).

**4a. Dónde va cada experimento** (las anotaciones son del corrector):

| Experimento | Etapa | Por qué |
| --- | --- | --- |
| Observación etnográfica | **Sense** ✓ | Todavía no hay solución: se mira cómo es hoy el problema para encontrar oportunidades |
| Buy-a-Feature | **Focus** ✓ | Ya hay una lista de ideas y hay que **priorizar**: elegir en qué enfocarse según lo que las cooperativas valoran |
| Wizard of Oz MVP | **Discovery** (corrector: "para nosotros, Discovery") | Prueba si la ruta optimizada **sirve y se usa** antes de construir el optimizador. Es un experimento para validar una solución |
| A/B Testing | **Delivery** (corrector: "para nosotros, Delivery") | Corre **en producción**, sobre algo ya construido: optimiza lo entregado |

> ⚠️ El alumno puso A/B en Discovery y Wizard of Oz en Delivery. Se lo aceptaron a medias. **La regla para la cátedra:** si el experimento corre sobre el producto **ya entregado y en producción**, es **Delivery**. Si prueba una idea **antes** de construirla de verdad, es **Discovery**.

**4b. Qué riesgo atacan:**
- **Observación etnográfica (Sense) → usabilidad y atractivo.**
  - **Usabilidad:** muestra **por qué** los vecinos clasifican mal (no saben qué va en cada bolsa, no tienen lugar para separar). Es la primera dificultad del enunciado.
  - **Atractivo:** muestra si el problema les importa lo suficiente como para **sostener el hábito** (la cuarta dificultad).
- **Wizard of Oz (Discovery) → atractivo/valor para las cooperativas, y usabilidad.**
  - **Atractivo/valor:** ¿usan la ruta? ¿les ahorra tiempo de verdad?
  - **Usabilidad:** con baja alfabetización digital, ¿la entienden?
  - **Técnico:** **posterga** el riesgo técnico (construir el optimizador) hasta saber si vale la pena.

> ⚠️ **Lo que marcó el corrector:** "no me cierra, nombró sólo los recordatorios". Hay que conectar el riesgo con **las dificultades del enunciado** y explicar **qué se aprende**, no solo nombrar el tipo de riesgo.

**5a.**
- La **Review** busca mejorar **el producto**:
  - se inspecciona el incremento con los **stakeholders** (el *qué*);
  - se recibe feedback y se adapta el backlog.
- La **Retrospectiva** busca mejorar **el proceso**:
  - el equipo solo revisa **cómo trabajó** (el *cómo*: colaboración, herramientas, acuerdos);
  - se compromete con acciones concretas.

> ⚠️ El alumno escribió que la retro busca "mejorar la calidad y efectividad del equipo a la hora de desarrollar el producto" y le anotaron "**confuso, puede ser ambos**". Decilo sin vueltas: **Review = producto, Retro = proceso**.

**5b.**
- **Review:** los resultados del piloto.
  - **Qué se mira:**
    - cuántos vecinos agendaron;
    - cuántos retiros se concretaron;
    - qué encontró la cooperativa en las bolsas (por ejemplo, 30% mal clasificado).
  - **Qué se decide:** con la cooperativa y el municipio presentes, sumar al backlog una **guía visual de qué se recicla** antes de agendar.
  - **Por qué es Review:** es sobre **el incremento** y necesita la voz de los **stakeholders** para decidir qué sigue.
- **Retrospectiva:** cómo trabajó el equipo.
  - **Qué pasó:** la historia de agendamiento quedó **trabada tres días** esperando que la cooperativa validara las franjas horarias, y el despliegue al piloto fue manual y se rompió dos veces.
  - **Acciones:**
    - acordar un contacto fijo en la cooperativa;
    - automatizar el despliegue.
  - **Por qué es Retro:** es sobre **cómo trabajamos**, no sobre qué hace el producto.

> ⚠️ **Lo que marcó el corrector:**
> - En la Review, el alumno propuso "discutir de qué forma se consideró la manera en que la gente clasifica sus residuos" (diseño interno del equipo). Le anotaron que ahí iría el **feedback de los stakeholders** (la letra de la corrección se lee a medias).
> - En la Retro propuso analizar si los vecinos clasifican bien. Eso es **producto**, no proceso: "**NO cierra**".

**6. Story map.**

**Supuestos:**
- MVP en **un barrio piloto con una cooperativa**.
- Empresas compradoras y municipio quedan para la 2da etapa, como dice el enunciado.

| | 1 · Sumarse (Vecino) | 2 · Preparar el material (Vecino) | 3 · Agendar el retiro (Vecino) | 4 · Organizar los retiros (Cooperativa) | 5 · Retirar (Clasificador) | 6 · Ver el resultado (Vecino / Cooperativa) |
| --- | --- | --- | --- | --- | --- | --- |
| **MVP** | Como vecino, quiero registrarme con mi dirección | Como vecino, quiero ver **con fotos** qué se recicla y qué no | Como vecino, quiero elegir día y franja horaria para el retiro | Como cooperativa, quiero ver la **lista de retiros del día** en una pantalla simple | Como clasificador, quiero **marcar un retiro como hecho** o como "material no reciclable" | Como vecino, quiero recibir la confirmación de que retiraron mi material |
| **Iteración 2** | Como vecino, quiero que me avisen si mi zona tiene cobertura | Como vecino, quiero sacar una foto de la bolsa y que me digan si está bien | Como vecino, quiero un **recordatorio** el día anterior | Como cooperativa, quiero ver los retiros en un **mapa con la ruta sugerida** | Como clasificador, quiero ver la dirección y las indicaciones del vecino | Como vecino, quiero ver cuántos kilos reciclé en el mes |
| **2da etapa** | — | — | Como vecino, quiero agendar retiros recurrentes | Como municipio, quiero un **reporte de impacto** (kilos, hogares, cooperativas) | Como cooperativa, quiero registrar los kilos por material | Como **empresa**, quiero ver el stock por cooperativa y hacer un pedido |

**Criterio del recorte del MVP:**
- La primera fila es el **corte horizontal más fino que cierra el flujo de valor completo**: el vecino agenda, la cooperativa lo ve, el clasificador retira y el vecino recibe la confirmación.
- Suma lo mínimo para las dos dificultades más riesgosas: la **guía con fotos** (vecinos que clasifican mal) y una **pantalla simple** (cooperativas con baja alfabetización digital).
- La ruta optimizada queda para después, validada primero con el Wizard of Oz.

> ⚠️ **Lo que marcó el corrector** en el story map del alumno:
> - **"No cierra el flujo con esta única actividad":** puso **una sola** actividad ("Agendar retiro") arriba. La fila de arriba (backbone) tiene que mostrar **todo el recorrido** en varias actividades.
> - **"No parecen de MVP":** puso historias como "editar residuo", "editar clasificación" y "eliminar residuo". Las variantes de editar y borrar casi nunca son MVP.
> - **Hizo un mapa por rol** (uno del vecino y otro del clasificador, en hojas distintas). La anotación del corrector se lee a medias, pero apunta a que **un rol solo no cierra el flujo de valor**: el vecino agenda y el clasificador retira. Lo más seguro es **un solo mapa** que recorra todo el flujo, con el rol indicado en cada actividad o historia.
> - Lo que sí valoró: "**bien que el MVP es corte horizontal**". También anotó "Patton: columnas = actividades".

---

## Simulación de Evaluación I

Sin caso: un grupo que recién arranca. Total: 10 puntos.

### La consigna

**1.** Elegí un tema de la materia y explicalo a una persona que no lo conoce. (2 puntos)

**2.** Estás en un grupo con otras dos personas que identificaron una problemática que puede ser valioso resolver con una aplicación, y están discutiendo cuáles serían los próximos pasos. (5 puntos)
- a. Planteá una lista de riesgos incluyendo cuestiones de **negocio (N)**, **atractivo/interés (A)** y **técnico (T)**.
- b. Diseñá tres actividades iniciales que el grupo podría llevar a cabo en pos de desarrollar el producto exitoso, especificando cuál es el/los riesgos que trata.

*Importante: proponé los riesgos y actividades basándote en los temas vistos en la materia. En el punto b, una actividad por cada "cuestión" (N / A / T).*

**3.** En el marco de *Artful Making*, el costo de iteración es: (1 punto)
- a. la suma de costo de exploración y el costo de reconfiguración.
- b. el costo de las actividades de gestión más el costo técnico de evolución.
- c. el presupuesto de cada sprint.
- d. el costo promedio extra insumido por iterar en lugar de hacer cascada.

**4.** Diseñando un Story Map, ¿qué definimos como columnas? (1 punto)
- a. Los pasos que realizamos como equipo para construir el producto.
- b. La secuencia de pasos del sistema, considerando los actores internos y externos (subsistemas, sistemas externos y usuarios).
- c. Los pasos por los que pasa un usuario en su interacción con el sistema para lograr sus objetivos.
- d. Las historias principales relacionadas a un tema en particular.

**5.** En el contexto de un Mapa de Ejemplos, el uso correcto sería: (1 punto)
- a. la historia es 'Controlar acceso', la regla es 'Limitar acceso por hora' y el ejemplo es 'No puede ingresar a las 6:00'.
- b. la regla es 'No permitir acceso tardío' y el ejemplo es 'El usuario ingresa a las 9:00'.
- c. la historia es 'Controlar acceso por hora' y la regla es 'No se puede ingresar después de las 20:00' y el ejemplo es 'Diego no logra ingresar a las 20:01'.

### Respuestas modelo

**1. Explicar un tema.**
- **Elegí uno que sepas aplicar:** experimentos, story map o el ciclo de Patton.
- **Estructura que funciona:**
  1. qué problema resuelve;
  2. qué es, en una frase sin jerga;
  3. un ejemplo concreto;
  4. un error común.
- **Ejemplo:** la respuesta modelo de la [pregunta 5 de 2025](#5--qué-es-un-experimento-explicado-con-midfood) (experimentos explicados con MidFood).

**2a. Riesgos.** Suponé una app concreta y decilo. Por ejemplo: una app para que los vecinos de un edificio **compartan herramientas**.

| Tipo | Riesgo concreto |
| --- | --- |
| **N** (negocio) | Nadie pagaría por la app y los vecinos lo resuelven gratis por WhatsApp. ¿De dónde salen los ingresos? |
| **N** | Una app grande de alquileres o de vecinos agrega lo mismo |
| **A** (atractivo) | El problema no duele lo suficiente: pedir prestado un taladro pasa dos veces por año |
| **A** | Desconfianza: a la gente le da miedo prestar cosas a desconocidos |
| **T** (técnico) | Verificar la identidad de los vecinos y que vivan en el edificio |
| **T** | Ninguno de los tres sabe desarrollar apps móviles |

**2b. Una actividad por cuestión:**

| Riesgo | Actividad | Qué se aprende y criterio de éxito |
| --- | --- | --- |
| **A**: ¿el problema existe y les importa? | **Entrevistas de problema (*Mom Test*)** a 10 vecinos: "¿cuándo fue la última vez que necesitaste una herramienta? ¿qué hiciste?" | Si menos de 4 de 10 tuvieron el problema en el último mes, se replantea la idea |
| **N**: ¿alguien pagaría? | **Concierge** o **landing con preventa**: el grupo arma a mano un grupo de préstamo en un edificio, con un abono simbólico | Cuántos pagan o dejan el mail para la versión paga |
| **T**: ¿podemos verificar a los vecinos? | **Spike / prueba de concepto** de una semana con validación por dirección y foto del DNI | Si sale en una semana y sin costo por usuario, se avanza. Si no, se busca otra forma de generar confianza |

> 🔑 Lo que valora la cátedra (como en las otras dos evaluaciones):
> - riesgos **concretos** del caso, no genéricos;
> - cada actividad **atada a su riesgo**;
> - experimentos **baratos antes** de construir;
> - y, si podés, un **criterio de éxito** decidido de antemano.

**3 → a.** Costo de iteración = costo de **exploración** + costo de **reconfiguración** (*Artful Making*).

**4 → c.** Las columnas (backbone) son las **actividades del usuario** en orden, a lo largo de su recorrido. Esto lo remarcó el corrector de 1C 2026: "Patton: columnas = actividades". La **b** describe un diagrama del sistema, no un story map.

**5 → c.**
- **Historia:** "Controlar acceso por hora".
- **Regla:** "No se puede ingresar después de las 20:00". Es precisa: tiene el **límite** explícito.
- **Ejemplo:** "Diego no logra ingresar a las 20:01". Es **concreto**: un personaje, un valor **justo en el borde** y el resultado esperado.
- **Por qué no las otras:**
  - en la **a**, la regla no dice cuál es el límite y el ejemplo no tiene personaje;
  - en la **b**, falta la historia, la regla es vaga ("tardío") y el ejemplo **no dice qué pasa**.

---

<sub>⚙️ Consignas transcriptas de exámenes que circulan entre alumnos (sin nombres): la Evaluación 1 del 26/10/2026 (enunciados), la Evaluación I del 27/10/2025, la del 18/5/2026 (con las correcciones del docente) y una Simulación de Evaluación I. Las respuestas modelo son propias, basadas en el material de la cátedra: *Artful Making* (cap. 1, "The Four Qualities of Artful Making"), slides de la clase 4, Jeff Patton (*The game has changed*) y la guía de Example Mapping.</sub>
