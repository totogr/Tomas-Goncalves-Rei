# 🟢 Resumen simple — Redes

> **Para qué es este archivo:** para **entender** cómo funciona una red antes de memorizar números y siglas. Cada tema está contado en palabras simples, con la idea central, las definiciones que hay que saber y un ejemplo de la vida cotidiana.
> Leelo **primero**, de corrido. Cuando las ideas cierren, pasá al [Resumen para el parcial](Resumen%20para%20el%20parcial.md), que tiene los números, las tablas completas, los ejercicios y lo que más se tomó.
>
> **Primer parcial: miércoles 4/11.** Entran las partes 1 a 11 (guías 01 a 12).

---

## La materia en un párrafo

Una red es la forma de llevar **información** (voz, video, datos) de un lugar a otro. Para que eso funcione hacen falta varias cosas:
- un **medio físico** por donde viajar (cables de cobre, fibra óptica, aire);
- **reglas** para ordenar el tráfico (protocolos);
- **lugares** donde se guardan y procesan los datos (data centers).

Como es un problema enorme, se lo divide en **capas**, donde cada una resuelve una parte. Casi todo lo que se ve en la materia es entender **qué hace cada capa** y **con qué tecnología** lo hace.

---

## 1. Telecomunicaciones y redes: el panorama

**La idea:** antes había **una red distinta para cada cosa** (una para el teléfono, otra para los datos), y se desperdiciaban recursos. Hoy **todo viaja por una misma red** basada en IP: la voz, el video y los datos.

**Definiciones:**
- **LAN (red local):** la red de un lugar chico (una casa, una oficina). Usa cables Ethernet o Wi-Fi.
- **WAN (red extendida):** une redes que están lejos entre sí (las sucursales de un banco).
- **Red de acceso ("última milla"):** el tramo que llega hasta tu casa o tu empresa.
- **Red de transporte (backbone):** las "autopistas" de gran capacidad que unen ciudades y países (fibra, cables submarinos, satélites).
- **VPN (red privada virtual):** una red **privada** armada **sobre una red pública**, con un "túnel" cifrado. Ejemplos: unir sucursales, conectar a un socio o trabajar desde tu casa como si estuvieras en la oficina.

> 💡 **Ejemplo cotidiano:** la red de acceso es la **calle de tu barrio**; la de transporte, la **autopista**.

**Martin Cooper:** inventó el **teléfono celular** en 1973. Imaginaba aparatos integrados al cuerpo que ayudaran a diagnosticar y curar, y decía que el mayor freno es que **la gente se resiste al cambio**.

---

## 2. Cómo viaja la información

**La idea:** la voz es una onda **continua** (analógica), pero las redes trabajan con **ceros y unos** (digital). Hay que convertirla, juntar muchas comunicaciones en un mismo cable y protegerlas de errores.

**Analógico y digital:**
- **Analógico:** una señal que varía de forma continua, como una rampa.
- **Digital:** solo **dos valores** (0 y 1), como una escalera de dos escalones.
- **Digitalizar la voz:** se le toman "fotos" muchas veces por segundo (muestras) y cada foto se escribe en ceros y unos.
  - La regla (**Nyquist**): hay que muestrear al menos al **doble** de la frecuencia más alta de la señal.
  - Así sale el **canal de voz digital** básico: **64 kbps**.

**Multiplexar = compartir un mismo medio entre muchas comunicaciones:**

| Forma | Cómo se reparte | Ejemplo cotidiano |
| --- | --- | --- |
| **Por tiempo (TDM)** | Cada comunicación usa el cable **por turnos** muy cortos | Una ronda de mate: cada uno toma en su turno |
| **Por frecuencia (FDM)** | Cada comunicación usa **su propia banda**, todas a la vez | Las radios de FM: cada una en su frecuencia |
| **Por color de luz (WDM)** | En una fibra, cada comunicación usa **un color** distinto | Varios rayos de colores por el mismo tubo |

**SDH:** el sistema que usan las empresas de telecomunicaciones para **juntar muchos canales de voz** en "bloques" cada vez más grandes. El primero es el **E1**, que junta 32 canales de voz (unos 2 Mbps).

**Hamming (detectar y corregir errores):**
- **La idea:** al mensaje se le agregan unos **bits de control**. Si en el camino un bit se da vuelta, el receptor puede **detectar en qué posición** está el error y **corregirlo** solo, sin pedir que lo manden de nuevo.
- **Ejemplo cotidiano:** el dígito verificador de un CBU o de una tarjeta, pero más astuto, porque además de avisar que hay un error dice **dónde** está.

---

## 3. El modelo de capas (OSI y TCP/IP)

**La idea:** mandar un dato por una red es tan complejo que se divide el trabajo en **capas**. Cada capa hace **una sola cosa** y le pasa el resultado a la de al lado.

**Ejemplo cotidiano — mandar una carta:**
1. Escribís la carta.
2. La metés en un sobre con la dirección.
3. El correo la clasifica y decide por qué ruta va.
4. Un camión la lleva.

En la otra punta se hace todo al revés. A nadie le importa cómo trabaja el camión: cada etapa hace su parte.

**Las 7 capas del modelo OSI, de abajo hacia arriba:**

| # | Capa | Qué hace, en una frase | Ejemplo |
| --- | --- | --- | --- |
| 1 | **Física** | Mueve **bits** por el cable, la fibra o el aire | Cable de cobre, fibra |
| 2 | **Enlace** | Lleva los datos **dentro de la red local**, entre dos equipos vecinos | Ethernet, direcciones MAC |
| 3 | **Red** | Elige el **camino** entre redes distintas (el "correo") | IP, routers |
| 4 | **Transporte** | Se asegura de que los datos lleguen **completos y en orden** (si hace falta) | TCP, UDP |
| 5 | **Sesión** | **Organiza la conversación**: abrirla, mantenerla, retomarla si se corta | Llamada de VoIP |
| 6 | **Presentación** | **Traduce** el formato, **cifra** y **comprime** | Cifrado, JPEG |
| 7 | **Aplicación** | Los servicios que usan los programas | Web, mail |

**OSI vs. TCP/IP** (pregunta que se repite):
- **OSI** es el modelo **teórico**, de referencia, con **7 capas**. Sirve para explicar y estandarizar.
- **TCP/IP** es el modelo **real**, el que usa Internet, con **4 capas**:
  - **Aplicación** (junta las capas 5, 6 y 7 de OSI);
  - **Transporte**;
  - **Internet** (la red);
  - **Acceso a la red** (junta física y enlace).

**Encapsulamiento:** al bajar por las capas, cada una le agrega **su propia "etiqueta"** al dato, como un sobre dentro de otro sobre. Al llegar, se van abriendo los sobres en orden inverso.

---

## 4. El cable de cobre

**La idea:** el cable de red común (UTP) tiene **pares de cables trenzados**. Es barato y fácil de instalar, pero la señal **se debilita con la distancia**: llega hasta unos **100 metros**.

**Definiciones:**
- **Trenzado:** los cables van enrollados de a pares para que **no se interfieran** entre sí.
- **Diafonía (crosstalk):** cuando un par "se cuela" en el de al lado. Es como escuchar la conversación de la mesa vecina.
- **Blindaje:** una lámina o una malla metálica que protege al cable del ruido externo. Se usa en fábricas y data centers.
- **Categorías (Cat 5e, 6, 6A, 8):** cuanto más alta la categoría, **más velocidad** soporta el cable. Por ejemplo, el **Cat 6A** llega a 10 Gbps a 100 m.
- **PoE (energía por Ethernet):** el mismo cable de datos lleva **electricidad** para alimentar cámaras, teléfonos o antenas Wi-Fi.
- **Rack:** el "mueble" estándar donde se montan los equipos de red, medido en unidades (U).

---

## 5. La fibra óptica

**La idea:** en vez de electricidad, se mandan **pulsos de luz** por un hilo de vidrio finísimo. Llega **mucho más lejos**, es **mucho más rápida** y no le afecta el ruido eléctrico.

**Los dos tipos, la pregunta que más se repite:**

| | **Monomodo** | **Multimodo** |
| --- | --- | --- |
| Cómo viaja la luz | **Un solo haz**, derecho por el centro | **Muchos haces** que rebotan |
| Hasta dónde llega | **Kilómetros** | **Cientos de metros** |
| Dónde se usa | Entre edificios, ciudades, sedes | Dentro de un edificio o de un data center |

> 💡 **Ejemplo cotidiano:** en la multimodo, los haces que rebotan recorren caminos de distinto largo y **llegan desparramados**: es como un grupo que sale junto pero cada uno toma un atajo distinto. Con la distancia, la señal se vuelve borrosa (**dispersión modal**). Por eso, para unir **dos sedes a varios kilómetros**, la respuesta es siempre **monomodo**.

**OM4 vs. OM5:** son dos multimodo **físicamente iguales**. La diferencia es que la **OM5** puede mandar **varios colores de luz a la vez** por el mismo hilo (SWDM). Así necesita **menos fibras** y está preparada para velocidades futuras.

**Otros conceptos:**
- **Conectores:** las "fichas" de la fibra. El más común es el **LC** (chico); el **MPO** junta muchas fibras en una sola ficha.
- **Suciedad:** la causa más común de fallas en la fibra es un **conector sucio**. Por eso se inspecciona y se limpia.
- **Cables según dónde pasan:** hay cables especiales que **no propagan el fuego** o que **no largan humo tóxico** (por ejemplo, para el espacio entre el techo y el cielorraso, por donde circula aire).

---

## 6. Data centers

**La idea:** un **data center** es un edificio preparado para tener **muchísimos servidores funcionando las 24 horas**, con energía de respaldo, refrigeración, seguridad y conexión a Internet.

**Tipos:**
- **Empresarial:** de una sola empresa (un banco).
- **Cloud (nube):** de un gran proveedor que alquila capacidad (Google, Amazon, Microsoft).
- **Colocation:** te alquilan el **espacio** y vos ponés tus equipos.
- **Edge:** data centers **chicos y cerca del usuario**, para que todo responda más rápido.

**Los Tiers (niveles de confiabilidad),** otra pregunta que se repite:
- **Tier I — básico:** sin respaldo. Para hacer mantenimiento hay que **apagar todo**.
- **Tier II — componentes redundantes:** hay equipos de repuesto (UPS, generador), pero **un solo camino** de energía. Para mantener, también hay que cortar.
- **Tier III — mantenimiento simultáneo:** hay **dos caminos**, así que se puede arreglar uno **sin cortar el servicio**.
- **Tier IV — tolerante a fallas:** **todo duplicado y funcionando a la vez**. Aunque falle cualquier cosa, el servicio sigue.

> 💡 **Ejemplo cotidiano:**
> - **Tier I:** una casa con una sola instalación eléctrica.
> - **Tier II:** la misma casa con un generador de repuesto.
> - **Tier III:** una casa con dos instalaciones independientes, donde podés arreglar una mientras usás la otra.
> - **Tier IV:** dos instalaciones funcionando siempre a la vez.

**Spine-leaf:** la forma moderna de conectar los equipos dentro de un data center.
- Cada switch "hoja" (**leaf**) se conecta con **todos** los switches "columna" (**spine**).
- Así, cualquier servidor llega a cualquier otro en pocos saltos y con la **misma demora**.
- Sirve porque hoy la mayor parte del tráfico va **entre servidores** del mismo data center.

---

## 7. Redes viejas (legacy) y la capa 2

**La idea:** antes de que todo fuera IP hubo otras tecnologías. Algunas todavía se usan y se toman en el parcial.

| Tecnología | En una frase |
| --- | --- |
| **X.25** | Muy confiable pero lenta; revisaba y corregía todo en cada tramo. Se usaba en cajeros |
| **TDM** | Turnos fijos de tiempo, ideal para telefonía. Si alguien no habla, su turno se desperdicia |
| **Frame Relay** | Más rápida que X.25, con tramas de tamaño variable |
| **ATM** | Manda todo en **celdas chiquitas del mismo tamaño** (53 bytes). Fue pionera en **calidad de servicio** |

**ATM y sus "planes"** (pregunta que se repite): según cuánto garantiza la red.
- **CBR:** velocidad **constante** garantizada. Para **voz y video en vivo**. Es el más caro.
- **VBR:** una velocidad media, con picos.
- **ABR:** un **mínimo** garantizado, más lo que sobre.
- **UBR:** **nada garantizado** ("lo que se pueda"). Para mail y backups. Es el más barato.

**La capa 2 hoy:**
- **Dirección MAC:** el "documento de identidad" de cada placa de red. Viene de fábrica.
- **Switch:** manda cada dato **solo** al equipo que corresponde. El **hub**, más viejo, se lo mandaba a todos.
- **Ethernet:** el estándar de las redes locales.

**Calidad de servicio (QoS):** darle **prioridad** a lo que no puede esperar (una llamada) sobre lo que sí puede (un mail).
- Se mide con la **latencia** (cuánto tarda), el **jitter** (cuánto **varía** esa demora) y la **pérdida** de datos.
- El jitter es lo que hace que una videollamada se entrecorte.

**MPLS:** en vez de mirar la dirección completa en cada router, al entrar a la red del operador se le pone al paquete una **etiqueta**, y adentro se lo mueve solo mirando la etiqueta. Es más rápido y permite priorizar tráfico.
- **Ejemplo cotidiano:** el pase de abordar de un aeropuerto. Ya no revisan tu pasaporte en cada puerta, solo el pase.

---

## 8. La capa 3: IP y el ruteo

**La idea:** la **dirección IP** es la "dirección postal" de cada equipo en una red, y los **routers** son los carteros que deciden **por dónde mandar cada paquete** para que llegue a destino, aunque cruce muchas redes.

**Definiciones:**
- **Dirección IP:** identifica a un equipo **dentro de una red**. A diferencia de la MAC, **cambia** según dónde estés conectado.
- **Máscara:** indica qué parte de la IP es la **red** (el barrio) y qué parte es el **equipo** (la casa).
- **IP pública:** única en todo Internet.
- **IP privada:** solo vale dentro de tu red local (la de tu casa).
- **NAT:** el router traduce las IP privadas a una pública para salir a Internet. Es como el conmutador de una empresa: muchos internos y una sola línea hacia afuera.
- **IP no es confiable:** manda los paquetes y **no garantiza** que lleguen. Eso lo resuelve la capa 4 (TCP).
- **Ruteo estático:** las rutas se cargan **a mano**. Es simple, pero si se cae un camino, no se adapta.
- **Ruteo dinámico:** los routers **se pasan información entre ellos** y actualizan las rutas solos.
- **Sistema autónomo (AS):** el conjunto de redes de **una misma organización** (un proveedor de Internet, por ejemplo).

**Los dos protocolos de ruteo:**
- **OSPF:** se usa **dentro** de una organización. Busca el **camino más corto**. Es como el GPS de tu ciudad.
- **BGP:** se usa **entre** organizaciones, y es el que hace funcionar Internet. Decide por **acuerdos y políticas** comerciales, no solo por distancia. Es como los acuerdos entre países para cruzar fronteras.

**Peering vs. tránsito:**
- **Peering:** dos redes intercambian tráfico **directamente y sin pagarse**, porque a las dos les conviene.
- **Tránsito:** una red **le paga** a otra más grande para llegar a **todo Internet**.

---

## 9. Virtualización de redes (NFV) y la evolución tecnológica

**La idea:** antes, cada función de la red (router, firewall) era **un aparato físico** distinto y cerrado. Con **NFV** esas funciones pasan a ser **programas** que corren en servidores comunes.

**Ejemplo cotidiano:** antes tenías una cámara de fotos, un GPS, un reproductor de música y una linterna. Hoy son **aplicaciones en un mismo celular**. NFV hace lo mismo con los equipos de red.

**Definiciones:**
- **VNF:** una función de red hecha software (router virtual, firewall virtual).
- **Ventajas:**
  - se instala **más rápido**;
  - **crece** fácil cuando hace falta;
  - se **automatiza**;
  - **cuesta menos** (en vez de comprar aparatos, se paga por uso).
- **Máquina virtual vs. contenedor:**
  - la **máquina virtual** simula una computadora entera, con su propio sistema operativo: es más pesada;
  - el **contenedor** comparte el sistema operativo: es más liviano y arranca más rápido.
- **SDN:** separar la **"cabeza"** de la red (quién decide por dónde va el tráfico) del **"cuerpo"** (los equipos que lo mueven), para manejar toda la red desde un software central.
- **Edge vs. cloud:** procesar **cerca del usuario** (edge) baja la demora; en la **nube** hay más capacidad.
- **Los 4 pilares de una red moderna:**
  - que **crezca** (escalabilidad);
  - que **integre** todo (convergencia);
  - que sea **segura**;
  - que **no se caiga** (disponibilidad).

---

## 10. La capa 4: TCP y UDP

**La idea:** la capa 4 decide **cómo** se entregan los datos entre los dos programas que se comunican. Hay dos formas, según qué importa más: que **llegue todo** o que **llegue rápido**.

| | **TCP** | **UDP** |
| --- | --- | --- |
| En una frase | **Carta certificada**: con acuse de recibo | **Volante por debajo de la puerta**: se tira y listo |
| ¿Garantiza que llegue? | **Sí**: si algo se pierde, lo vuelve a mandar | **No** |
| ¿En orden? | Sí | No |
| Velocidad | Más lento | **Más rápido** |
| Se usa para… | Páginas web, mail, descargas | Llamadas, videojuegos, streaming, DNS |

**Definiciones:**
- **Handshake (apretón de manos):** antes de mandar datos, TCP **saluda en tres pasos** ("¿hablamos?" → "dale, ¿hablamos?" → "dale"). Recién ahí arranca la conversación.
- **Puerto:** el número que dice **a qué programa** va el dato dentro de una computadora. Si la IP es la dirección del edificio, el puerto es el **número de departamento**.
- **¿Por qué UDP para una llamada?** Porque es mejor perder un pedacito de audio que esperar a que se reenvíe y escuchar todo con demora.
- **Riesgos de UDP:** como no verifica quién manda, se lo usa para **ataques de saturación** (inundar a la víctima con respuestas que no pidió). Se protege con firewalls, límites de tráfico y monitoreo.

---

## 11. Las capas 5, 6 y 7

**Capa 5 — Sesión:** **organiza la conversación**.
- La abre, la mantiene, controla quién habla y la cierra.
- Pone **puntos de control**: si una descarga larga se corta, **retoma desde el último punto** en lugar de empezar de cero.
- **Diferencia con la capa 4:** la 4 se ocupa de que **los datos lleguen**; la 5, de **ordenar el diálogo**.

**Capa 6 — Presentación:** el **traductor**.
- Hace que los dos lados **entiendan el mismo formato**.
- Además **cifra** (para que nadie más pueda leer) y **comprime** (para que pese menos).
- **Cifrado simétrico:** una **misma clave** para cerrar y abrir, como la llave de tu casa.
- **Cifrado asimétrico:** una clave **pública**, que cualquiera usa para cerrar, y una **privada**, que solo vos tenés para abrir. Es como un buzón: cualquiera puede meter una carta, pero solo el dueño lo abre.
- **Hash:** una "huella digital" del dato. No sirve para descifrar; sirve para comprobar que **nadie lo modificó**.
- **TLS:** lo que pone el **candadito** en el navegador (HTTPS). Garantiza que nadie lea lo que mandás, que no lo modifique y que el sitio sea quien dice ser.

**Capa 7 — Aplicación:** los servicios que usamos todos los días:
- **HTTP/HTTPS:** páginas web.
- **DNS:** traduce nombres (google.com) a direcciones IP. Es la **guía telefónica** de Internet.
- **DHCP:** le da una IP automáticamente a tu equipo cuando se conecta.
- **SMTP:** **envía** mails.
- **POP3:** **descarga** los mails y los borra del servidor.
- **IMAP:** **sincroniza** los mails y los deja en el servidor (por eso los ves igual en el celular y en la compu).
- **FTP:** transfiere archivos.
- **SSH:** acceso remoto **seguro** a otra computadora.

---

## 🧠 Todo junto, en una tabla

| Tema | La pregunta que responde | La idea en una frase |
| --- | --- | --- |
| Panorama | ¿Qué es una red y de qué partes tiene? | Acceso (calle) + transporte (autopista); hoy todo va por IP |
| Transmisión | ¿Cómo viaja la información? | Se digitaliza, se comparte el medio y se protege de errores |
| Capas | ¿Cómo se organiza el trabajo? | Cada capa hace una sola cosa; OSI es el teórico y TCP/IP el real |
| Cobre | ¿Hasta dónde llega el cable común? | Unos 100 m; el trenzado y el blindaje lo protegen del ruido |
| Fibra | ¿Cuál fibra uso? | Monomodo para lejos, multimodo para cerca |
| Data center | ¿Dónde viven los servidores? | Cuanto más alto el Tier, menos se corta |
| Legacy y capa 2 | ¿Qué había antes y qué hace la red local? | ATM inventó la calidad de servicio; el switch entrega por MAC |
| IP y ruteo | ¿Cómo llega un paquete a destino? | La IP es la dirección; los routers eligen el camino (OSPF adentro, BGP entre redes) |
| NFV | ¿Por qué todo se vuelve software? | Funciones de red como apps sobre servidores comunes |
| TCP y UDP | ¿Llega todo o llega rápido? | TCP es la carta certificada; UDP, el volante |
| Capas 5, 6 y 7 | ¿Qué pasa "arriba"? | Organizar la charla, traducir y cifrar, y los servicios que usamos |

---

<sub>⚙️ Versión simplificada de las [guías de la materia](README.md) y del [Resumen para el parcial](Resumen%20para%20el%20parcial.md). Si algo de acá no alcanza para responder una pregunta, la versión completa está ahí.</sub>
