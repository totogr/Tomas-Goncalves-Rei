# Glosario de siglas de Redes, agrupadas por tema

> **Para qué es este archivo:** para ubicar rápido **a qué tema pertenece cada sigla** y qué quiere decir. Está ordenado por tema, en el mismo orden que las clases. Dentro de cada tema, cada sigla tiene su nombre completo y una línea que explica para qué sirve.
>
> - Si tenés una sigla suelta y no sabés de qué tema es, buscala en el [índice alfabético](#índice-alfabético) del final: te dice a qué sección ir.
> - 🔴 = sigla que aparece seguido en parciales.
> - Al final de cada tema hay un **truco** para acordarte.
> - Ojo con las [siglas que significan dos cosas distintas](#siglas-que-significan-dos-cosas-distintas) (FTP, STP, ER, CSS…).

---

## 1. Organismos y estándares

Quién escribe las reglas. Aparecen al lado de un número de norma (IEEE 802.3, TIA-942, RFC 791…).

| Sigla | Qué significa | Para qué sirve |
| --- | --- | --- |
| **ISO** | International Organization for Standardization | Organismo internacional de normas. Definió el **modelo OSI** |
| **IEC** | International Electrotechnical Commission | Normas eléctricas y electrónicas; firma normas junto con ISO (ISO/IEC) |
| **IEEE** | Institute of Electrical and Electronics Engineers | Hace las normas **802**: 802.3 = Ethernet, 802.11 = Wi-Fi, 802.3af/at/bt = PoE |
| **IETF** | Internet Engineering Task Force | Define los protocolos de Internet (IP, TCP, HTTP…) en documentos **RFC** |
| **RFC** | Request for Comments | El documento donde se publica un protocolo de Internet (ej.: **RFC 791 = IPv4**) |
| **ANSI** | American National Standards Institute | Organismo de normas de EE. UU.; aparece en ANSI/TIA |
| **TIA** | Telecommunications Industry Association | Normas de **cableado**: **TIA-568** (cableado estructurado) y **TIA-942** (data center) |
| **EN-50600** | Norma europea | El equivalente europeo de la TIA-942 para data centers |
| **ODVA** | Open DeviceNet Vendors Association | Asociación industrial dueña de **EtherNet/IP** y **CIP** (ver [IoT](#15-iot-y-redes-industriales)) |

> 💡 **Truco:** **IEEE** = Ethernet y Wi-Fi (los "802"). **IETF** = Internet (los "RFC"). **TIA** = cables y edificios.

---

## 2. Conceptos generales y tipos de red

Clase 01. Cómo se clasifican las redes y quién te da el servicio.

| Sigla | Qué significa | Para qué sirve |
| --- | --- | --- |
| 🔴 **LAN** | Local Area Network | Red **local**: una casa, una oficina, un piso |
| **MAN** | Metropolitan Area Network | Red de **ciudad** (ej.: Metro Ethernet) |
| 🔴 **WAN** | Wide Area Network | Red de **larga distancia**: une sedes en distintas ciudades o países |
| **ISP** | Internet Service Provider | La empresa que te da Internet (Telecom, Telecentro, IPLAN…) |
| **PSTN** | Public Switched Telephone Network | La red de **telefonía fija tradicional** (conmutación de circuitos) |
| **ISDN** | Integrated Services Digital Network (RDSI) | Telefonía **digital** de los 80/90 que llevaba voz y datos |
| **ARPANET** | Advanced Research Projects Agency Network | La red militar y académica de EE. UU. que dio origen a Internet (adopta TCP/IP el 1/1/1983) |
| **WWW** | World Wide Web | La **web**: páginas enlazadas que viajan por HTTP. No es lo mismo que Internet |
| **DSL / ADSL** | (Asymmetric) Digital Subscriber Line | Internet por el **par de cobre del teléfono**. "Asimétrico": bajás más rápido de lo que subís |
| **FTTH** | Fiber To The Home | **Fibra hasta la casa** del cliente |
| **SLA** | Service Level Agreement | El **contrato de nivel de servicio**: qué disponibilidad y tiempos te garantiza el proveedor |
| **BW** | Bandwidth | **Ancho de banda** (cuántos bits por segundo entran por el enlace) |
| **NOC / SOC** | Network / Security Operations Center | Centro que **monitorea la red** (NOC) o la **seguridad** (SOC) las 24 h |

> 💡 **Truco:** **L**ocal · **M**etropolitana · **W**ide (amplia), de chica a grande: **L < M < W**.

---

## 3. Transmisión de datos y multiplexación

Clase 02. Cómo una señal se vuelve bits, cómo se detectan errores y cómo varios canales comparten un mismo medio.

| Sigla | Qué significa | Para qué sirve |
| --- | --- | --- |
| **A/D** | Analógico / Digital | La **conversión** de una señal continua (voz) a bits: muestreo, cuantificación y codificación |
| **FM** | Frecuencia de muestreo | Cuántas muestras por segundo se toman al digitalizar (en otros contextos, FM es *modulación de frecuencia*, como la radio) |
| **MHz** | Megahertz | Millones de ciclos por segundo. En cables mide el **ancho de banda** de la categoría (Cat 6 = 250 MHz) |
| 🔴 **CRC** | Cyclic Redundancy Check | **Detecta errores**: un cálculo sobre los datos que el receptor repite y compara |
| **FCS** | Frame Check Sequence | El campo de **4 bytes** al final de la trama Ethernet que lleva el CRC |
| **K1, K2, K4 / M3, M5…** | (No son siglas) | En el **código de Hamming**: **K** = bits de **control** (posiciones 1, 2, 4…) y **M** = bits del **mensaje** |
| **MUX / DEMUX** | Multiplexor / Demultiplexor | Junta varios canales en un medio / los vuelve a separar del otro lado |
| 🔴 **TDM** | Time Division Multiplexing | Multiplexación **por tiempo**: cada canal usa **todo** el ancho de banda en su turno |
| **STDM** | Synchronous TDM | TDM con **ranuras fijas**: si un canal no tiene nada que mandar, su ranura va vacía |
| **ATDM** | Asynchronous (estadístico) TDM | TDM con ranuras **asignadas según quién tenga datos**: aprovecha mejor el enlace |
| 🔴 **FDM** | Frequency Division Multiplexing | Multiplexación **por frecuencia**: cada canal tiene su banda, todos a la vez (radio FM, TV) |
| 🔴 **WDM** | Wavelength Division Multiplexing | La FDM de la **fibra**: cada canal es un **"color" de luz** (longitud de onda, λ) distinto |

> 💡 **Truco:** **T**DM = **T**urnos (tiempo). **F**DM = **F**recuencias (bandas). **W**DM = colores de luz (**W**avelength).

---

## 4. Jerarquías digitales: E1, T1 y SDH

Clase 02/07. Cómo se agrupan canales de voz de 64 kbps en "caños" cada vez más grandes. Son los **números para saber de memoria**.

| Sigla | Qué significa | Para qué sirve |
| --- | --- | --- |
| 🔴 **E1** | Portadora europea nivel 1 | **2,048 Mbps** = **32 canales × 64 kbps**. Es la que se usa en Argentina |
| **T1 / DS1** | Portadora de EE. UU. nivel 1 (Digital Signal 1) | **1,544 Mbps** = **24 canales** × 64 kbps + entramado |
| 🔴 **E3** | Portadora europea nivel 3 | **34 Mbps** (16 E1). Error típico: decir que 34 Mbps es un E1 |
| **T3 / DS3** | Portadora de EE. UU. nivel 3 | **44,7 Mbps** |
| 🔴 **SDH** | Synchronous Digital Hierarchy | La jerarquía **sincrónica** (europea) que transporta todo lo anterior sobre fibra. Para la cátedra es **capa 1** |
| **SONET** | Synchronous Optical Network | La versión **norteamericana** de SDH |
| 🔴 **STM-N** | Synchronous Transport Module, nivel N | Los escalones de SDH: **STM-1 = 155 Mbps**, STM-4 = 622 Mbps, STM-16 = 2,5 Gbps, STM-64 = 10 Gbps, STM-256 = 40 Gbps. Cada uno es **×4** del anterior |
| **OC-N** | Optical Carrier, nivel N | Los escalones de SONET: **OC-3 = STM-1**, OC-12 = STM-4, OC-48 = STM-16, OC-192, OC-768 |
| **OH** | Overhead | La parte de la trama SDH que **no es carga útil**: gestión, alarmas, sincronización |
| **POS** | Packet over SONET/SDH | Mandar **paquetes IP directo sobre SDH**, sin pasar por ATM |

> 💡 **Truco:** **E**1 = **E**uropa = **32** canales (2 Mbps). **T**1 = EE. UU. = **24** canales (1,5 Mbps). En SDH, STM-**1** → **155** y de ahí todo **× 4**; en SONET el número de **OC** es **3 veces** el de STM (OC-**3** = STM-**1**).

---

## 5. Modelo de capas

Clase 03. El mapa donde se ubica todo lo demás.

| Sigla | Qué significa | Para qué sirve |
| --- | --- | --- |
| 🔴 **OSI** | Open Systems Interconnection | El modelo **de 7 capas** de ISO: física, enlace, red, transporte, sesión, presentación, aplicación. Es **de referencia** (teórico) |
| 🔴 **TCP/IP** | Transmission Control Protocol / Internet Protocol | El modelo **práctico de 4 capas** con el que funciona Internet: acceso a la red, Internet, transporte, aplicación |
| **L1, L2, L3…** | Layer 1, 2, 3… | Forma corta de decir "capa 1, capa 2…" (ej.: **switch L2**, **switch L3**) |
| **PDU** | Protocol Data Unit | Cómo se llama el "paquete" en cada capa: **bits** (1), **trama** (2), **paquete** (3), **segmento** (4), **datos** (5–7) |

> 💡 **Truco para las 7 capas de abajo hacia arriba:** **F**ísica, **E**nlace, **R**ed, **T**ransporte, **S**esión, **P**resentación, **A**plicación → "**F**ui **E**n **R**emis **T**odo **S**ábado **P**or **A**hí".

---

## 6. Cobre: cables, blindajes y PoE

Clase 04. El cableado de par trenzado y sus normas.

| Sigla | Qué significa | Para qué sirve |
| --- | --- | --- |
| **Cat** | Categoría | El nivel del cable: Cat 5e (100 MHz, 1 Gbps), **Cat 6** (250 MHz), **Cat 6A** (500 MHz, **10 Gbps a 100 m**), Cat 7, Cat 8 |
| 🔴 **UTP** | Unshielded Twisted Pair | Par trenzado **sin blindaje**: el más común y barato |
| **FTP** | Foiled Twisted Pair | Par trenzado con **lámina** (*foil*) metálica que envuelve todo. ⚠️ No confundir con el protocolo FTP |
| **STP** | Shielded Twisted Pair | Par trenzado **blindado**. ⚠️ No confundir con el protocolo Spanning Tree |
| **XX/YTP** | Formato de la norma | Lo que va **antes de la barra** es el blindaje **general**; lo de **después**, el de **cada par**. **U** = sin blindaje, **F** = lámina, **S** = malla |
| **U/UTP** | — | Sin blindaje general y sin blindaje por par (el UTP común) |
| **F/UTP** | — | **Lámina general**, pares sin blindaje |
| **S/FTP** | — | **Malla general** + **lámina en cada par**: el más protegido (Cat 7) |
| **SF/UTP** | — | **Malla + lámina general**, pares sin blindaje |
| **RJ45** | Registered Jack 45 | El **conector** de 8 pines del cable de red |
| **GG45 / TERA** | (Nombres comerciales) | Conectores para **Cat 7 / 7A**, que no usan RJ45 |
| **NEXT** | Near-End Crosstalk | **Diafonía** (un par "contagia" al otro) medida **cerca del transmisor** |
| **FEXT** | Far-End Crosstalk | Diafonía medida en el **otro extremo** |
| **EMI** | Electromagnetic Interference | **Interferencia electromagnética**: el ruido de motores o cables de energía. La fibra es inmune |
| 🔴 **PoE** | Power over Ethernet | Llevar **energía por el mismo cable** de datos: **15 W** (802.3af), **30 W** (802.3at, PoE+), **60 / 90 W** (802.3bt) |
| **PTZ** | Pan-Tilt-Zoom | Cámara **motorizada** (gira, se inclina y hace zoom): por eso pide PoE+ de 30 W |
| **AP** | Access Point | **Punto de acceso** Wi-Fi |
| 🔴 **CMP** | Communications Plenum | Cable para el **plenum** (entre el cielorraso y la losa, por donde circula aire): **no propaga llama** y hace poco humo |
| **CMR** | Communications Riser | Cable para **montantes** verticales entre pisos: se apaga solo, más barato que CMP |
| **LSZH** | Low Smoke Zero Halogen | **Poco humo y sin halógenos**: no larga gases tóxicos al quemarse |
| **AIM** | Automated Infrastructure Management | Patch panels "inteligentes" que **reportan en tiempo real qué está enchufado** en cada puerto y avisan si alguien desconecta algo (empresas, campus, DC) |
| **MDF / IDF** | Main / Intermediate Distribution Frame | El **rack principal** del edificio y los **racks de cada piso** del cableado estructurado |

> 💡 **Truco:** en **CMP** la **P** es de **P**lenum (techo); en **CMR** la **R** es de **R**iser (sube entre pisos). En el blindaje, **U** = *Unshielded* (nada), **F** = *Foil* (papel de aluminio), **S** = *Screen* (malla).

---

## 7. Fibra óptica

Clase 05. Tipos de fibra, alcances, conectores y medición.

| Sigla | Qué significa | Para qué sirve |
| --- | --- | --- |
| 🔴 **OS1 / OS2** | Optical Single-mode | Fibras **monomodo** (núcleo de **~9 µm**): para **kilómetros**. **OS2** es la de exterior y la que se elige para **unir sedes** |
| 🔴 **OM1 … OM5** | Optical Multimode | Fibras **multimodo** (núcleo de **50 µm**; OM1 de 62,5 µm): para **distancias cortas** (edificio, DC). Cuanto más alto el número, más velocidad y alcance |
| 🔴 **OM5 / WBMMF** | Wideband Multimode Fiber | Multimodo **de banda ancha**: igual a OM4 por dentro, pero preparada para **SWDM** (4 colores por hilo) |
| 🔴 **SWDM** | Short Wavelength Division Multiplexing | WDM en **multimodo** con **4 longitudes de onda** cortas. **SWDM4** = 4 λ. Menos fibras y preparada para 400G/800G |
| **λ** | Lambda | **Longitud de onda** (el "color" de la luz): 850 nm en multimodo, 1310/1550 nm en monomodo |
| 🔴 **SR** | Short Reach | Óptica de **corto alcance**, multimodo (~100 m). **SR4** = 4 fibras en paralelo |
| 🔴 **LR** | Long Reach | Óptica de **largo alcance**, monomodo (**10 km**). **LR4** = 4 λ en un par de fibras |
| **ER / ZR** | Extended Reach | Alcance **extendido**: ER ~40 km, ZR ~80 km. ⚠️ No confundir con la sala ER del data center |
| **NRZ** | Non-Return to Zero | Modulación de **2 niveles** de luz: **1 bit** por símbolo |
| **PAM4** | Pulse Amplitude Modulation, 4 niveles | **4 niveles**: **2 bits** por símbolo. Duplica la velocidad sin cambiar la fibra |
| **LC** | Lucent Connector | El conector **chico** más usado en DC (dúplex) |
| **SC** | Subscriber Connector | Conector **cuadrado** de encastre (*push-pull*) |
| **ST** | Straight Tip | Conector con anclaje de **bayoneta** (girás y traba) |
| **FC** | Ferrule Connector | Conector **a rosca**, para ambientes con vibración |
| 🔴 **MPO** | Multi-fiber Push On | Conector **multifibra** (12–24 fibras) para 40G/100G/400G |
| **PC / UPC / APC** | (Ultra / Angled) Physical Contact | El **pulido** de la punta: **APC** está en ángulo (8°, conector **verde**) y refleja menos luz |
| **OTDR** | Optical Time-Domain Reflectometer | Manda pulsos y **grafica la fibra**: dice **dónde** está un corte o empalme |
| **OPM** | Optical Power Meter | Medidor de **potencia**: con una fuente de luz mide **cuánto** se pierde |
| **EDFA** | Erbium-Doped Fiber Amplifier | **Amplificador óptico** (fibra con erbio) de los cables **submarinos** y de larga distancia |
| **PFCS** | Powered Fiber Cable System | Cable híbrido de **fibra + cobre para energía**: lleva datos y electricidad lejos (como un PoE de fibra) |

> 💡 **Truco:** **OS** = **S**ingle (monomodo, **lejos**). **OM** = **M**ulti (multimodo, **cerca**). **SR** = *Short* (corto, multimodo); **LR** = *Long* (largo, monomodo). Para medir: **OPM** = *cuánto* se pierde; **OTDR** = *dónde*.

---

## 8. Data center

Clase 06. Normas, zonas del DC, energía y transceivers.

| Sigla | Qué significa | Para qué sirve |
| --- | --- | --- |
| 🔴 **DC** | Data Center | **Centro de datos** |
| **TIA-942** | Norma de la TIA | **Cableado e infraestructura** del data center (en Europa, **EN-50600**) |
| 🔴 **ER** | Entrance Room | **Sala de entrada**: donde llegan las fibras de los **proveedores** (después de la cámara 0 y la acometida) |
| 🔴 **MDA** | Main Distribution Area | **Distribución principal**: el corazón del cableado (core) |
| **IDA** | Intermediate Distribution Area | Distribución **intermedia** (DC grandes) |
| **HDA** | Horizontal Distribution Area | Distribución **horizontal** hacia las filas de racks. La norma la pone después de la IDA |
| **ZDA** | Zone Distribution Area | Punto de distribución **de zona** (opcional), cerca de los equipos |
| 🔴 **EDA** | Equipment Distribution Area | Donde están los **equipos**: servidores y storage |
| **UPS** | Uninterruptible Power Supply | **Batería** que mantiene la energía sin corte hasta que arranca el generador |
| **N+1** | (Notación de redundancia) | Hay **uno más** de lo necesario: si uno falla, sigue andando |
| **EPO** | Emergency Power Off | **Botón de corte de energía de emergencia** (Tier II en adelante) |
| **CCTV** | Closed-Circuit Television | **Cámaras de seguridad**; un Tier III/IV pide CCTV perimetral |
| **ToR** | Top of Rack | Switch **arriba de cada rack**, al que se conectan los servidores |
| 🔴 **SFP** | Small Form-factor Pluggable | **Transceiver enchufable** del switch: **SFP** 1G, **SFP+** 10G, **SFP28** 25G |
| **QSFP** | Quad SFP | **4 canales** en un módulo: **QSFP+** 40G, **QSFP28** 100G, **QSFP-DD** (*Double Density*) 400G |
| **OSFP** | Octal SFP | **8 canales**: 400G–800G (hyperscalers) |
| **MTDC** | Multi-Tenant Data Center | DC grande donde **varias empresas** alquilan racks |
| **Core / Edge DC** | — | **Core:** los gigantes de la nube. **Edge:** DC chico **cerca del usuario**, con baja latencia |

> 💡 **Truco para el recorrido de la fibra:** **E**R → **M**DA → **I**DA → (HDA) → **Z**DA → **E**DA = "**E**ntra, **M**anda, **I**ntermedia, **Z**ona, **E**quipos". Empieza en la puerta (**E**ntrance) y termina en los equipos (**E**quipment).

---

## 9. Redes legacy, capa 2 y MPLS

Clase 07. Las redes de antes de IP (X.25, Frame Relay, ATM), Ethernet y lo que usan hoy los operadores.

| Sigla | Qué significa | Para qué sirve |
| --- | --- | --- |
| **X.25** | (Norma de la UIT) | Red de **paquetes** muy **confiable pero lenta** (corrige errores en cada salto). Cajeros y posnets |
| 🔴 **PVC** | Permanent Virtual Circuit | **Circuito virtual permanente**: un camino fijo dentro de la red conmutada. El que se usaba comercialmente |
| **SVC** | Switched Virtual Circuit | Circuito virtual que se **arma a demanda** en cada sesión |
| **LAPB** | Link Access Procedure, Balanced | El protocolo de **capa 2** de X.25 |
| **FR** | Frame Relay | Sucesor de X.25: tramas **variables**, **sin retransmisión**, más eficiente |
| 🔴 **ATM** | Asynchronous Transfer Mode | Red de **celdas fijas de 53 bytes** (48 de datos + 5 de cabecera). **Pionera en QoS** |
| **VPI / VCI** | Virtual Path / Channel Identifier | Los campos de la celda ATM que dicen **por qué camino y canal** va |
| **CLP** | Cell Loss Priority | Bit de la celda ATM: indica si se puede **descartar primero** cuando hay congestión |
| **AAL** | ATM Adaptation Layer | Adapta los datos a celdas: **AAL1** para tráfico constante (voz), **AAL5** para datos |
| 🔴 **QoS** | Quality of Service | **Calidad de servicio**: darle prioridad o garantías a cierto tráfico (voz, video). La ofrecen ATM, Frame Relay y MPLS; IP sola no |
| 🔴 **CBR** | Constant Bit Rate | Clase ATM: velocidad **constante garantizada** → **voz** y video en tiempo real |
| 🔴 **VBR** | Variable Bit Rate | Velocidad **media sostenida (SCR)** con picos → video comprimido, streaming |
| **SCR** | Sustainable Cell Rate | La velocidad **media** que garantiza VBR |
| 🔴 **ABR** | Available Bit Rate | Un **mínimo** garantizado; sube si hay capacidad libre → datos |
| 🔴 **UBR** | Unspecified Bit Rate | **Sin garantía**: usa lo que sobra → mail, descargas, backup |
| 🔴 **MAC** | Media Access Control | La **dirección física** de la placa de red: **48 bits** = 24 del fabricante + 24 del dispositivo |
| **OUI** | Organizationally Unique Identifier | Los **primeros 24 bits** de la MAC: identifican al **fabricante** |
| **CSMA/CD** | Carrier Sense Multiple Access / Collision Detection | Escucha antes de hablar y **detecta choques**: Ethernet **cableado** |
| **CSMA/CA** | … / Collision Avoidance | Escucha antes de hablar y **evita choques**: **Wi-Fi** |
| **PPP** | Point-to-Point Protocol | Protocolo de capa 2 para enlaces **punto a punto** |
| **HDLC** | High-level Data Link Control | Protocolo de capa 2 de enlaces seriales (WAN) |
| **STP** | Spanning Tree Protocol | **Evita bucles** en una red de switches bloqueando los caminos redundantes. ⚠️ No confundir con el cable STP |
| **VLAN** | Virtual LAN | Dividir **un switch** en varias redes **lógicas** separadas |
| **SNA** | Systems Network Architecture | La arquitectura de red de **IBM** para mainframes (las redes "paralelas" de antes) |
| 🔴 **MPLS** | Multiprotocol Label Switching | La red WAN de los operadores: le pega una **etiqueta** al paquete y conmuta mirando solo eso. **Capa 2,5**, con QoS |
| **LER / PE** | Label Edge Router / Provider Edge | Router de **borde** del operador: **pone y saca** la etiqueta |
| **LSR** | Label Switching Router | Router del **núcleo**: conmuta mirando **solo la etiqueta** |
| **LSP** | Label Switched Path | El **camino** que siguen los paquetes etiquetados dentro de la red MPLS |
| **PHP** | Penultimate Hop Popping | El **anteúltimo** router saca la etiqueta para alivianar al último. ⚠️ No confundir con el lenguaje web |
| 🔴 **VPN** | Virtual Private Network | **Red privada virtual**: tráfico separado y seguro sobre una red compartida |
| **L2VPN / L3VPN** | Layer 2 / Layer 3 VPN | VPN MPLS que une sedes **como si fuera un mismo switch** (L2) o **a nivel de rutas IP** (L3) |

> 💡 **Truco para ATM:** **C**BR = **C**onstante (voz) · **V**BR = **V**ariable (video comprimido) · **A**BR = lo **A**vailable (disponible) · **U**BR = **U**nspecified (sin garantía, lo que sobra). Van de **más** garantía a **menos**.

---

## 10. Capa 3: IP

Clase 08 y 11. El protocolo de red, su cabecera y lo que lo rodea.

| Sigla | Qué significa | Para qué sirve |
| --- | --- | --- |
| 🔴 **IP** | Internet Protocol | **Direccionamiento lógico** y **ruteo** de paquetes. **IPv4** (32 bits, ~4.300 millones) e **IPv6** (128 bits) |
| 🔴 **ICMP** | Internet Control Message Protocol | Mensajes de **control y error** de capa 3: lo usan `ping` y `traceroute` |
| 🔴 **TTL** | Time To Live | Campo que cada router **resta en 1**; al llegar a 0 se descarta el paquete → **evita bucles** |
| **MTU** | Maximum Transmission Unit | El **tamaño máximo** de paquete en un enlace (Ethernet: **1500 bytes**). Si se pasa, se **fragmenta** |
| **IHL** | Internet Header Length | Campo con el **largo de la cabecera** IP (20 bytes sin opciones) |
| **ToS / DSCP** | Type of Service / Differentiated Services Code Point | Campo de **prioridad** del paquete IP (para QoS) |
| 🔴 **NAT** | Network Address Translation | Muchas **IPs privadas** salen a Internet por **una pública** (lo hace tu router) |
| **CIDR** | Classless Inter-Domain Routing | La notación **/24, /16**: cuántos bits de la IP son la red |
| **ARP** | Address Resolution Protocol | Averigua **qué MAC** tiene una IP en la red local (une capa 3 con capa 2) |
| **IPX** | Internetwork Packet Exchange | Protocolo de red de **Novell**, competidor viejo de IP (ejemplo de capa 3 en OSI) |

> 💡 **Truco:** **TTL** = "tiempo de vida" → cada salto le quita uno, como las vidas de un videojuego. **MTU** = la altura máxima del camión que pasa por el puente.

---

## 11. Enrutamiento: sistemas autónomos, BGP y OSPF

Clase 08b/09. Cómo eligen los routers el camino, adentro de una red y entre redes.

| Sigla | Qué significa | Para qué sirve |
| --- | --- | --- |
| 🔴 **AS** | Autonomous System | **Sistema autónomo**: el conjunto de redes de una misma organización con una política de ruteo (un ISP, Google…) |
| 🔴 **ASN** | Autonomous System Number | El **número** del AS (ej.: **AS15169** = Google, **AS27747** = Telecentro) |
| 🔴 **IGP** | Interior Gateway Protocol | Protocolos de ruteo **dentro** de un AS: OSPF, RIP, EIGRP |
| 🔴 **EGP** | Exterior Gateway Protocol | Protocolos de ruteo **entre** AS: BGP |
| 🔴 **OSPF** | Open Shortest Path First | IGP de **estado de enlace**: cada router conoce el mapa y calcula el **camino más corto** (algoritmo **SPF**, Dijkstra) |
| **RIP** | Routing Information Protocol | IGP viejo de **vector distancia**: cuenta **saltos** (máximo 15) |
| **EIGRP** | Enhanced Interior Gateway Routing Protocol | IGP de **Cisco** |
| 🔴 **BGP** | Border Gateway Protocol | El protocolo que **une los AS** de Internet. Elige por **políticas**, no por el camino más corto |
| **eBGP / iBGP** | External / Internal BGP | **eBGP:** entre AS distintos. **iBGP:** reparte esas rutas **dentro** del mismo AS |
| **IXP** | Internet Exchange Point | **Punto de intercambio** donde muchos AS se conectan entre sí (en Argentina, los de CABASE) |
| **RIR** | Regional Internet Registry | El registro que **asigna IPs y ASN** por región: **LACNIC** (Latinoamérica, el nuestro), **ARIN** (Norteamérica), **RIPE NCC** (Europa), **APNIC** (Asia-Pacífico), **AfriNIC** (África) |
| **CDN** | Content Delivery Network | Red de servidores repartidos que dejan el contenido **cerca del usuario** (Netflix, YouTube) |
| **CAIDA** | Center for Applied Internet Data Analysis | Hace el ranking **AS Rank** de los sistemas autónomos |

> 💡 **Truco:** **I**GP = **I**nterior (OSPF, adentro de casa). **E**GP = **E**xterior (BGP, entre países). "**B**GP es la **B**order (frontera)".

---

## 12. Virtualización: NFV, SDN y 5G

Clase 10. Las funciones de red dejan de ser cajas y pasan a ser software.

| Sigla | Qué significa | Para qué sirve |
| --- | --- | --- |
| 🔴 **NFV** | Network Functions Virtualization | Las funciones de red (router, firewall) pasan de **equipos propietarios** a **software** sobre hardware genérico |
| 🔴 **VNF** | Virtual Network Function | Cada función **virtualizada**: vRouter, vFirewall, vLoad Balancer… |
| **COTS** | Commercial Off-The-Shelf | **Hardware estándar** (servidores comunes) donde corren las VNF |
| **VM** | Virtual Machine | **Máquina virtual**: una computadora simulada por software |
| **NFVI** | NFV Infrastructure | El **hardware + la capa de virtualización** donde corren las VNF |
| **MANO** | Management and Orchestration | El marco que **administra** NFV. Tiene tres partes: NFVO, VNFM y VIM |
| **NFVO** | NFV Orchestrator | **Orquesta** los servicios completos |
| **VNFM** | VNF Manager | Maneja el **ciclo de vida** de cada VNF |
| **VIM** | Virtualized Infrastructure Manager | Maneja la **infraestructura** (OpenStack, Kubernetes) |
| 🔴 **SDN** | Software-Defined Networking | Red **programable**: el control se separa de los equipos y se maneja desde software |
| **SD-WAN** | Software-Defined WAN | **WAN definida por software**: une sedes eligiendo el mejor enlace en cada momento |
| **OSS / BSS** | Operations / Business Support Systems | Los sistemas del operador para **operar la red** (OSS) y para **facturar y atender clientes** (BSS) |
| **CAPEX / OPEX** | Capital / Operational Expenditure | Gasto en **compra de equipos** (CAPEX) y gasto **operativo mensual** (OPEX). NFV baja los dos |
| **EPC** | Evolved Packet Core | El **núcleo** de la red móvil 4G (vEPC = virtualizado) |
| **IMS** | IP Multimedia Subsystem | La plataforma del operador para **voz y video sobre IP** |
| **RAN** | Radio Access Network | La parte **de radio** de la red móvil: antenas y estaciones base |
| **vRAN** | Virtualized RAN | RAN **virtualizada**: el procesamiento de la antena corre en servidores |
| **MEC** | Multi-access Edge Computing | **Cómputo en el borde** de la red móvil, cerca del usuario (baja latencia) |

> 💡 **Truco:** **NFV** = virtualizo **la función** (el *qué*). **SDN** = programo **el control** (el *cómo* se maneja la red). Se complementan.

---

## 13. Capa 4: transporte

Clase 11. TCP, UDP y los flags del encabezado.

| Sigla | Qué significa | Para qué sirve |
| --- | --- | --- |
| 🔴 **TCP** | Transmission Control Protocol | Transporte **confiable y orientado a conexión**: handshake, ACK, retransmisión, orden, control de flujo y congestión |
| 🔴 **UDP** | User Datagram Protocol | Transporte **sin conexión**, rápido y sin garantías: voz, video, DNS, juegos |
| **QUIC** | (Nombre propio, nació como *Quick UDP Internet Connections*) | Transporte moderno **sobre UDP** con TLS incorporado. Es la base de **HTTP/3** |
| 🔴 **SYN** | Synchronize | Flag para **abrir** la conexión (1.er paso del handshake: SYN → SYN-ACK → ACK) |
| 🔴 **ACK** | Acknowledgment | **Acuse de recibo**: "me llegó hasta acá" |
| **FIN** | Finish | **Cierra** la conexión de forma ordenada |
| **RST** | Reset | **Corta** la conexión de golpe |
| **PSH** | Push | "Pasale los datos **ya** a la aplicación" |
| **URG** | Urgent | Hay datos **urgentes** |
| **ECN (ECE / CWR)** | Explicit Congestion Notification (ECN-Echo / Congestion Window Reduced) | Flags para **avisar congestión** sin perder paquetes |
| **SACK** | Selective Acknowledgment | Confirma **bloques sueltos** recibidos, para retransmitir solo lo que faltó |
| **MSS** | Maximum Segment Size | Los **datos máximos** que entran en un segmento TCP |
| **DTLS** | Datagram TLS | **TLS para UDP** |

> 💡 **Truco:** **TCP** = carta **certificada** (llega, en orden, con acuse). **UDP** = **volante** tirado por debajo de la puerta (rápido, sin garantía).

---

## 14. Capas 5, 6 y 7: sesión, presentación y aplicación

Clase 12. Las siglas más numerosas: agrupadas por capa.

### Capa 5 · sesión

| Sigla | Qué significa | Para qué sirve |
| --- | --- | --- |
| **NetBIOS** | Network Basic Input/Output System | Sesiones entre equipos de una red Windows |
| **SMB / CIFS** | Server Message Block / Common Internet File System | **Compartir archivos e impresoras** en Windows (445/TCP) |
| **RPC** | Remote Procedure Call | **Ejecutar una función en otra máquina** como si fuera local |
| **NFS** | Network File System | Compartir archivos en red (mundo Unix/Linux) |
| **SIP** | Session Initiation Protocol | **Arma, mantiene y corta** llamadas de **VoIP** (5060/UDP) |
| **PPTP** | Point-to-Point Tunneling Protocol | Túnel VPN **viejo e inseguro** |
| **L2TP** | Layer 2 Tunneling Protocol | **Túnel** VPN sobre **UDP 1701**. No cifra solo: se combina con IPsec |
| **SCP** | Session Control Protocol | Protocolo de sesión de **DECnet**. ⚠️ No confundir con SCP = copia segura por SSH |

### Capa 6 · presentación (formatos, cifrado y compresión)

| Sigla | Qué significa | Para qué sirve |
| --- | --- | --- |
| **ASCII** | American Standard Code for Information Interchange | Codificación de texto de **7 bits** (128 caracteres) |
| **EBCDIC** | Extended Binary Coded Decimal Interchange Code | Codificación de texto de los **mainframes IBM** |
| **UTF-8 / UTF-16** | Unicode Transformation Format | Codificación **Unicode**: todos los idiomas y los emojis |
| **JPEG / PNG / GIF / TIFF / BMP** | Joint Photographic Experts Group / Portable Network Graphics / Graphics Interchange Format / Tagged Image File Format / Bitmap | Formatos de **imagen**. JPEG comprime **con pérdida**; PNG **sin pérdida** |
| **MPEG / MP3 / MP4 / AVI** | Moving Picture Experts Group / Audio Video Interleave | Formatos de **audio y video** |
| **MIDI** | Musical Instrument Digital Interface | Formato de **instrucciones musicales** (no es audio grabado) |
| **MIME** | Multipurpose Internet Mail Extensions | Indica **qué tipo de archivo** viaja (adjuntos de mail, respuestas HTTP) |
| 🔴 **TLS / SSL** | Transport Layer Security / Secure Sockets Layer | **Cifra** la comunicación (la "S" de HTTPS). SSL es el nombre viejo |
| **AES / DES** | Advanced / Data Encryption Standard | Cifrado **simétrico** (una sola clave). DES es viejo e inseguro; AES es el actual |
| **RSA / ECC** | Rivest-Shamir-Adleman / Elliptic Curve Cryptography | Cifrado **asimétrico** (clave pública y privada) |
| **MD5 / SHA-256** | Message Digest 5 / Secure Hash Algorithm | Funciones **hash**: la "huella digital" de un dato para verificar que no cambió. MD5 ya no es seguro |
| **LZ77** | Lempel-Ziv 1977 | Algoritmo de **compresión sin pérdida** (base de ZIP) |

### Capa 7 · aplicación

| Sigla | Qué significa | Para qué sirve |
| --- | --- | --- |
| 🔴 **HTTP / HTTPS** | Hypertext Transfer Protocol (Secure) | La **web**: 80/TCP y 443/TCP (con TLS). **HTTP/2** multiplexa; **HTTP/3** va sobre QUIC |
| 🔴 **FTP** | File Transfer Protocol | **Transferir archivos**: 21/TCP (control) y 20/TCP (datos). ⚠️ No confundir con el cable FTP |
| **FTPS / SFTP** | FTP over SSL / SSH File Transfer Protocol | FTP **seguro**: FTPS usa TLS (990); SFTP va por SSH (22) |
| **TFTP** | Trivial FTP | FTP **mínimo sobre UDP**, sin autenticación (para cargar configuraciones en equipos) |
| **SCP** | Secure Copy Protocol | **Copiar archivos** por SSH |
| 🔴 **SSH** | Secure Shell | **Acceso remoto seguro** a otra máquina (22/TCP) |
| **TELNET** | Teletype Network | Acceso remoto **sin cifrar** (23/TCP): obsoleto |
| **RDP** | Remote Desktop Protocol | **Escritorio remoto** de Windows (3389/TCP) |
| 🔴 **SMTP** | Simple Mail Transfer Protocol | **Enviar** correo (25/TCP; 587/465 con cifrado). SMTPS / STARTTLS = con TLS |
| 🔴 **POP3** | Post Office Protocol 3 | **Bajar** el correo al equipo y borrarlo del servidor (110/TCP) |
| 🔴 **IMAP** | Internet Message Access Protocol | **Leer** el correo **dejándolo en el servidor**, sincronizado en varios dispositivos (143/TCP) |
| 🔴 **DNS** | Domain Name System | Traduce **nombres a IPs** (53/UDP) |
| **TLD** | Top-Level Domain | La **terminación** del dominio: .com, .ar, .org |
| **A / AAAA / CNAME / MX / NS / TXT** | Tipos de registro DNS | **A** = IPv4 · **AAAA** = IPv6 · **CNAME** = alias · **MX** = servidor de correo · **NS** = servidor autoritativo · **TXT** = texto libre |
| **DNSSEC** | DNS Security Extensions | **Firma** las respuestas DNS para que no las falsifiquen |
| 🔴 **DHCP** | Dynamic Host Configuration Protocol | **Asigna automáticamente** IP, máscara, gateway y DNS (67/68 UDP). Pasos **DORA** |
| **DORA** | Discover · Offer · Request · Acknowledge | Los **4 pasos** de DHCP: busco, me ofrecen, pido, confirman |
| **SNMP** | Simple Network Management Protocol | **Monitorear** equipos de red (161/UDP consultas, 162/UDP alertas o *traps*) |
| **NTP** | Network Time Protocol | **Sincronizar la hora** de los equipos (123/UDP) |
| **LDAP** | Lightweight Directory Access Protocol | **Directorio de usuarios** y autenticación (389/TCP); LDAPS = con TLS |
| **RTP / RTCP / SRTP** | Real-time Transport Protocol / RTP Control Protocol / Secure RTP | Transporta **voz y video en tiempo real** (sobre UDP), lo controla y lo cifra |
| **STUN** | Session Traversal Utilities for NAT | Ayuda a la VoIP a **atravesar el NAT** (3478/UDP) |
| **REST / SOAP** | Representational State Transfer / Simple Object Access Protocol | Estilos de **API web**: REST (liviano, JSON) y SOAP (más rígido, XML) |
| **JSON / XML** | JavaScript Object Notation / eXtensible Markup Language | Formatos de **texto para intercambiar datos** |
| **HTML / CSS** | HyperText Markup Language / Cascading Style Sheets | El **contenido** y el **estilo** de una página web |
| **URL** | Uniform Resource Locator | La **dirección** de un recurso web (https://…) |

> 💡 **Truco para el correo:** **SMTP** = **S**ale (envío). **POP3** = **P**op, lo saco del buzón y me lo llevo. **IMAP** = lo **M**iro en el servidor (queda allá).

---

## 15. IoT y redes industriales

Clase 12 BIS (entra en el **final**).

| Sigla | Qué significa | Para qué sirve |
| --- | --- | --- |
| 🔴 **IoT** | Internet of Things | **Internet de las cosas**: objetos con sensores conectados a una red |
| 🔴 **IIoT** | Industrial IoT | IoT **industrial**: además de medir, **controla procesos** (exige disponibilidad y determinismo) |
| 🔴 **IT / OT** | Information / Operational Technology | **IT** = la red de la empresa (oficinas, sistemas). **OT** = la red de la **planta**. La idea clave es su **convergencia** |
| 🔴 **PLC** | Programmable Logic Controller | **Controlador programable**: el "cerebro" que lee sensores y mueve actuadores |
| **PAC** | Programmable Automation Controller | Un PLC con más capacidad de cómputo |
| **DCS** | Distributed Control System | **Sistema de control distribuido** de plantas grandes (petroquímicas) |
| **RTU** | Remote Terminal Unit | **Unidad remota** que manda datos al centro de control. También es el modo **serie** de Modbus |
| **HMI** | Human-Machine Interface | La **pantalla** donde el operador ve y maneja el proceso |
| 🔴 **SCADA** | Supervisory Control And Data Acquisition | El sistema que **supervisa toda la planta** y deja intervenir a una persona |
| **MES** | Manufacturing Execution System | Gestiona la **producción** en planta (qué se fabrica, calidad) |
| **ERP** | Enterprise Resource Planning | El sistema de **gestión de la empresa** (compras, stock, finanzas): planifica a largo plazo |
| **CMMS** | Computerized Maintenance Management System | Sistema de gestión del **mantenimiento** |
| **PID** | Proporcional-Integral-Derivativo | Tipo de **controlador** clásico de lazos analógicos |
| **KPI / MTBF / MTTR** | Key Performance Indicator / Mean Time Between Failures / Mean Time To Repair | **Indicadores**: tiempo medio **entre fallas** y tiempo medio **de reparación** |
| **HART** | Highway Addressable Remote Transducer | Mete una señal **digital (FSK)** sobre el lazo analógico **4–20 mA** sin alterarlo |
| **FSK** | Frequency Shift Keying | Modulación que representa los bits **cambiando la frecuencia** |
| **RS-232 / RS-485** | Recommended Standard | **Puertos serie**: RS-232 punto a punto y corto; RS-485 multipunto y más largo |
| **Modbus (RTU / TCP)** | — | Protocolo industrial **maestro-esclavo**: RTU por serie, TCP por Ethernet |
| **PROFIBUS / PROFINET** | Process Field Bus / Process Field Net | Protocolos industriales de Siemens: **PROFIBUS** por serie, **PROFINET** por Ethernet |
| **RT / IRT** | Real Time / Isochronous Real Time | Modos de PROFINET: **IRT** es **sincronizado**, con ciclos **< 1 ms** (control de movimiento) |
| **MRP** | Media Redundancy Protocol | **Redundancia en anillo**: si se corta un tramo, el tráfico va por el otro lado |
| **EtherNet/IP · CIP** | EtherNet/Industrial Protocol · Common Industrial Protocol | Protocolo industrial de **ODVA/Rockwell** que lleva **CIP** sobre Ethernet. ⚠️ La "IP" es *Industrial Protocol*, no Internet Protocol |
| 🔴 **OPC UA** | Open Platform Communications Unified Architecture | Estándar para que **máquinas de distintas marcas** intercambien datos de forma segura |
| 🔴 **MQTT** | Message Queuing Telemetry Transport | Protocolo **liviano publicar/suscribir** con un **broker** en el medio: ideal para sensores |
| **LPWAN** | Low Power Wide Area Network | Redes de **largo alcance y bajo consumo**: LoRaWAN, Sigfox, NB-IoT, LTE-M |
| **LoRa / LoRaWAN** | Long Range (Wide Area Network) | **LoRa** = la **modulación de radio** (capa física). **LoRaWAN** = el **protocolo y la red** que la usan |
| **CSS** | Chirp Spread Spectrum | La técnica de radio que usa LoRa. ⚠️ No confundir con CSS de la web |
| **NB-IoT / LTE-M** | Narrowband IoT / LTE for Machines | IoT sobre la **red celular** del operador |
| **NTN** | Non-Terrestrial Network | Conectividad **satelital** para zonas sin infraestructura |
| **OTA** | Over-The-Air | **Actualizar** el software de un dispositivo **a distancia** |
| **MU-MIMO** | Multi-User Multiple Input Multiple Output | Wi-Fi que atiende a **varios usuarios a la vez** con varias antenas |
| **OFDMA** | Orthogonal Frequency Division Multiple Access | Wi-Fi 6: divide el canal en **subportadoras** para varios usuarios a la vez |

> 💡 **Truco para la pirámide industrial (de abajo hacia arriba):** sensor → **PLC** (decide) → **HMI/SCADA** (supervisa) → **MES** (produce) → **ERP** (planifica). Cuanto más arriba, más IT y menos OT.

---

## Siglas que significan dos cosas distintas

Las que más confunden en un choice:

| Sigla | Significado 1 | Significado 2 |
| --- | --- | --- |
| **FTP** | Cable **Foiled Twisted Pair** (cobre, [sección 6](#6-cobre-cables-blindajes-y-poe)) | **File Transfer Protocol** (capa 7, [sección 14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación)) |
| **STP** | Cable **Shielded Twisted Pair** (cobre) | **Spanning Tree Protocol** (capa 2, evita bucles) |
| **ER** | **Entrance Room** (sala de entrada del DC) | **Extended Reach** (óptica de ~40 km) |
| **SCP** | **Session Control Protocol** (DECnet, capa 5) | **Secure Copy** (copia de archivos por SSH) |
| **CSS** | **Chirp Spread Spectrum** (radio de LoRa) | **Cascading Style Sheets** (estilo de páginas web) |
| **FM** | **Frecuencia de muestreo** (digitalización) | **Modulación de frecuencia** (radio FM) |
| **PC** | **Personal Computer** | **Physical Contact** (pulido de conector de fibra) |
| **PHP** | **Penultimate Hop Popping** (MPLS) | El lenguaje de programación web |
| **IP** | **Internet Protocol** (capa 3) | **Industrial Protocol** en EtherNet/IP |
| **RTU** | **Remote Terminal Unit** (equipo industrial) | El modo **serie** de Modbus (Modbus RTU) |

---

## Índice alfabético

Cada sigla, con el número de la sección donde está explicada.

- **A:** **A** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **A/D** → [3](#3-transmisión-de-datos-y-multiplexación) · **AAAA** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **AAL** → [9](#9-redes-legacy-capa-2-y-mpls) · **ABR** → [9](#9-redes-legacy-capa-2-y-mpls) · **ACK** → [13](#13-capa-4-transporte) · **ADSL** → [2](#2-conceptos-generales-y-tipos-de-red) · **AES** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **AIM** → [6](#6-cobre-cables-blindajes-y-poe) · **ANSI** → [1](#1-organismos-y-estándares) · **AP** → [6](#6-cobre-cables-blindajes-y-poe) · **APC** → [7](#7-fibra-óptica) · **ARP** → [10](#10-capa-3-ip) · **ARPANET** → [2](#2-conceptos-generales-y-tipos-de-red) · **AS** → [11](#11-enrutamiento-sistemas-autónomos-bgp-y-ospf) · **ASCII** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **ASN** → [11](#11-enrutamiento-sistemas-autónomos-bgp-y-ospf) · **ATDM** → [3](#3-transmisión-de-datos-y-multiplexación) · **ATM** → [9](#9-redes-legacy-capa-2-y-mpls) · **AVI** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación)
- **B:** **BGP** → [11](#11-enrutamiento-sistemas-autónomos-bgp-y-ospf) · **BMP** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **BSS** → [12](#12-virtualización-nfv-sdn-y-5g) · **BW** → [2](#2-conceptos-generales-y-tipos-de-red)
- **C:** **CAIDA** → [11](#11-enrutamiento-sistemas-autónomos-bgp-y-ospf) · **CAPEX** → [12](#12-virtualización-nfv-sdn-y-5g) · **Cat** → [6](#6-cobre-cables-blindajes-y-poe) · **CBR** → [9](#9-redes-legacy-capa-2-y-mpls) · **CCTV** → [8](#8-data-center) · **CDN** → [11](#11-enrutamiento-sistemas-autónomos-bgp-y-ospf) · **CIDR** → [10](#10-capa-3-ip) · **CIFS** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **CIP** → [15](#15-iot-y-redes-industriales) · **CLP** → [9](#9-redes-legacy-capa-2-y-mpls) · **CMMS** → [15](#15-iot-y-redes-industriales) · **CMP** → [6](#6-cobre-cables-blindajes-y-poe) · **CMR** → [6](#6-cobre-cables-blindajes-y-poe) · **CNAME** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **Core** → [8](#8-data-center) · **COTS** → [12](#12-virtualización-nfv-sdn-y-5g) · **CRC** → [3](#3-transmisión-de-datos-y-multiplexación) · **CSMA/CA** → [9](#9-redes-legacy-capa-2-y-mpls) · **CSMA/CD** → [9](#9-redes-legacy-capa-2-y-mpls) · **CSS** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación), [15](#15-iot-y-redes-industriales) · **CWR** → [13](#13-capa-4-transporte)
- **D:** **DC** → [8](#8-data-center) · **DCS** → [15](#15-iot-y-redes-industriales) · **DEMUX** → [3](#3-transmisión-de-datos-y-multiplexación) · **DES** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **DHCP** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **DNS** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **DNSSEC** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **DORA** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **DS1** → [4](#4-jerarquías-digitales-e1-t1-y-sdh) · **DS3** → [4](#4-jerarquías-digitales-e1-t1-y-sdh) · **DSCP** → [10](#10-capa-3-ip) · **DSL** → [2](#2-conceptos-generales-y-tipos-de-red) · **DTLS** → [13](#13-capa-4-transporte)
- **E:** **E1** → [4](#4-jerarquías-digitales-e1-t1-y-sdh) · **E3** → [4](#4-jerarquías-digitales-e1-t1-y-sdh) · **EBCDIC** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **eBGP** → [11](#11-enrutamiento-sistemas-autónomos-bgp-y-ospf) · **ECC** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **ECE** → [13](#13-capa-4-transporte) · **ECN** → [13](#13-capa-4-transporte) · **EDA** → [8](#8-data-center) · **EDFA** → [7](#7-fibra-óptica) · **Edge DC** → [8](#8-data-center) · **EGP** → [11](#11-enrutamiento-sistemas-autónomos-bgp-y-ospf) · **EIGRP** → [11](#11-enrutamiento-sistemas-autónomos-bgp-y-ospf) · **EMI** → [6](#6-cobre-cables-blindajes-y-poe) · **EN-50600** → [1](#1-organismos-y-estándares) · **EPC** → [12](#12-virtualización-nfv-sdn-y-5g) · **EPO** → [8](#8-data-center) · **ER** → [7](#7-fibra-óptica), [8](#8-data-center) · **ERP** → [15](#15-iot-y-redes-industriales) · **EtherNet/IP** → [15](#15-iot-y-redes-industriales)
- **F:** **F/UTP** → [6](#6-cobre-cables-blindajes-y-poe) · **FC** → [7](#7-fibra-óptica) · **FCS** → [3](#3-transmisión-de-datos-y-multiplexación) · **FDM** → [3](#3-transmisión-de-datos-y-multiplexación) · **FEXT** → [6](#6-cobre-cables-blindajes-y-poe) · **FIN** → [13](#13-capa-4-transporte) · **FM** → [3](#3-transmisión-de-datos-y-multiplexación) · **FR** → [9](#9-redes-legacy-capa-2-y-mpls) · **FSK** → [15](#15-iot-y-redes-industriales) · **FTP** → [6](#6-cobre-cables-blindajes-y-poe), [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **FTPS** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **FTTH** → [2](#2-conceptos-generales-y-tipos-de-red)
- **G:** **GG45** → [6](#6-cobre-cables-blindajes-y-poe) · **GIF** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación)
- **H:** **HART** → [15](#15-iot-y-redes-industriales) · **HDA** → [8](#8-data-center) · **HDLC** → [9](#9-redes-legacy-capa-2-y-mpls) · **HMI** → [15](#15-iot-y-redes-industriales) · **HTML** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **HTTP** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **HTTPS** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación)
- **I:** **iBGP** → [11](#11-enrutamiento-sistemas-autónomos-bgp-y-ospf) · **ICMP** → [10](#10-capa-3-ip) · **IDA** → [8](#8-data-center) · **IDF** → [6](#6-cobre-cables-blindajes-y-poe) · **IEC** → [1](#1-organismos-y-estándares) · **IEEE** → [1](#1-organismos-y-estándares) · **IETF** → [1](#1-organismos-y-estándares) · **IGP** → [11](#11-enrutamiento-sistemas-autónomos-bgp-y-ospf) · **IHL** → [10](#10-capa-3-ip) · **IIoT** → [15](#15-iot-y-redes-industriales) · **IMAP** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **IMS** → [12](#12-virtualización-nfv-sdn-y-5g) · **IoT** → [15](#15-iot-y-redes-industriales) · **IP** → [10](#10-capa-3-ip) · **IPX** → [10](#10-capa-3-ip) · **IRT** → [15](#15-iot-y-redes-industriales) · **ISDN** → [2](#2-conceptos-generales-y-tipos-de-red) · **ISO** → [1](#1-organismos-y-estándares) · **ISP** → [2](#2-conceptos-generales-y-tipos-de-red) · **IT** → [15](#15-iot-y-redes-industriales) · **IXP** → [11](#11-enrutamiento-sistemas-autónomos-bgp-y-ospf)
- **J:** **JPEG** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **JSON** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación)
- **K:** **KPI** → [15](#15-iot-y-redes-industriales)
- **L:** **L1, L2, L3** → [5](#5-modelo-de-capas) · **L2TP** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **L2VPN** → [9](#9-redes-legacy-capa-2-y-mpls) · **L3VPN** → [9](#9-redes-legacy-capa-2-y-mpls) · **LAN** → [2](#2-conceptos-generales-y-tipos-de-red) · **LAPB** → [9](#9-redes-legacy-capa-2-y-mpls) · **LC** → [7](#7-fibra-óptica) · **LDAP** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **LER** → [9](#9-redes-legacy-capa-2-y-mpls) · **LoRa** → [15](#15-iot-y-redes-industriales) · **LoRaWAN** → [15](#15-iot-y-redes-industriales) · **LPWAN** → [15](#15-iot-y-redes-industriales) · **LR** → [7](#7-fibra-óptica) · **LSP** → [9](#9-redes-legacy-capa-2-y-mpls) · **LSR** → [9](#9-redes-legacy-capa-2-y-mpls) · **LSZH** → [6](#6-cobre-cables-blindajes-y-poe) · **LTE-M** → [15](#15-iot-y-redes-industriales) · **LZ77** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación)
- **M:** **MAC** → [9](#9-redes-legacy-capa-2-y-mpls) · **MAN** → [2](#2-conceptos-generales-y-tipos-de-red) · **MANO** → [12](#12-virtualización-nfv-sdn-y-5g) · **MD5** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **MDA** → [8](#8-data-center) · **MDF** → [6](#6-cobre-cables-blindajes-y-poe) · **MEC** → [12](#12-virtualización-nfv-sdn-y-5g) · **MES** → [15](#15-iot-y-redes-industriales) · **MHz** → [3](#3-transmisión-de-datos-y-multiplexación) · **MIDI** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **MIME** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **Modbus** → [15](#15-iot-y-redes-industriales) · **MP3** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **MP4** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **MPEG** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **MPLS** → [9](#9-redes-legacy-capa-2-y-mpls) · **MPO** → [7](#7-fibra-óptica) · **MQTT** → [15](#15-iot-y-redes-industriales) · **MRP** → [15](#15-iot-y-redes-industriales) · **MSS** → [13](#13-capa-4-transporte) · **MTBF** → [15](#15-iot-y-redes-industriales) · **MTDC** → [8](#8-data-center) · **MTTR** → [15](#15-iot-y-redes-industriales) · **MTU** → [10](#10-capa-3-ip) · **MU-MIMO** → [15](#15-iot-y-redes-industriales) · **MUX** → [3](#3-transmisión-de-datos-y-multiplexación) · **MX** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación)
- **N:** **N+1** → [8](#8-data-center) · **NAT** → [10](#10-capa-3-ip) · **NB-IoT** → [15](#15-iot-y-redes-industriales) · **NetBIOS** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **NEXT** → [6](#6-cobre-cables-blindajes-y-poe) · **NFS** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **NFV** → [12](#12-virtualización-nfv-sdn-y-5g) · **NFVI** → [12](#12-virtualización-nfv-sdn-y-5g) · **NFVO** → [12](#12-virtualización-nfv-sdn-y-5g) · **NOC** → [2](#2-conceptos-generales-y-tipos-de-red) · **NRZ** → [7](#7-fibra-óptica) · **NS** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **NTN** → [15](#15-iot-y-redes-industriales) · **NTP** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación)
- **O:** **OC-N** → [4](#4-jerarquías-digitales-e1-t1-y-sdh) · **ODVA** → [1](#1-organismos-y-estándares) · **OFDMA** → [15](#15-iot-y-redes-industriales) · **OH** → [4](#4-jerarquías-digitales-e1-t1-y-sdh) · **OM1** → [7](#7-fibra-óptica) · **OM5** → [7](#7-fibra-óptica) · **OPC UA** → [15](#15-iot-y-redes-industriales) · **OPEX** → [12](#12-virtualización-nfv-sdn-y-5g) · **OPM** → [7](#7-fibra-óptica) · **OS1** → [7](#7-fibra-óptica) · **OS2** → [7](#7-fibra-óptica) · **OSFP** → [8](#8-data-center) · **OSI** → [5](#5-modelo-de-capas) · **OSPF** → [11](#11-enrutamiento-sistemas-autónomos-bgp-y-ospf) · **OSS** → [12](#12-virtualización-nfv-sdn-y-5g) · **OT** → [15](#15-iot-y-redes-industriales) · **OTA** → [15](#15-iot-y-redes-industriales) · **OTDR** → [7](#7-fibra-óptica) · **OUI** → [9](#9-redes-legacy-capa-2-y-mpls)
- **P:** **PAC** → [15](#15-iot-y-redes-industriales) · **PAM4** → [7](#7-fibra-óptica) · **PC** → [7](#7-fibra-óptica) · **PDU** → [5](#5-modelo-de-capas) · **PE** → [9](#9-redes-legacy-capa-2-y-mpls) · **PFCS** → [7](#7-fibra-óptica) · **PHP** → [9](#9-redes-legacy-capa-2-y-mpls) · **PID** → [15](#15-iot-y-redes-industriales) · **PLC** → [15](#15-iot-y-redes-industriales) · **PNG** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **PoE** → [6](#6-cobre-cables-blindajes-y-poe) · **POP3** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **POS** → [4](#4-jerarquías-digitales-e1-t1-y-sdh) · **PPP** → [9](#9-redes-legacy-capa-2-y-mpls) · **PPTP** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **PROFIBUS** → [15](#15-iot-y-redes-industriales) · **PROFINET** → [15](#15-iot-y-redes-industriales) · **PSH** → [13](#13-capa-4-transporte) · **PSTN** → [2](#2-conceptos-generales-y-tipos-de-red) · **PTZ** → [6](#6-cobre-cables-blindajes-y-poe) · **PVC** → [9](#9-redes-legacy-capa-2-y-mpls)
- **Q:** **QoS** → [9](#9-redes-legacy-capa-2-y-mpls) · **QSFP** → [8](#8-data-center) · **QUIC** → [13](#13-capa-4-transporte)
- **R:** **RAN** → [12](#12-virtualización-nfv-sdn-y-5g) · **RDP** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **REST** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **RFC** → [1](#1-organismos-y-estándares) · **RIP** → [11](#11-enrutamiento-sistemas-autónomos-bgp-y-ospf) · **RIR** → [11](#11-enrutamiento-sistemas-autónomos-bgp-y-ospf) · **RJ45** → [6](#6-cobre-cables-blindajes-y-poe) · **RPC** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **RS-232** → [15](#15-iot-y-redes-industriales) · **RS-485** → [15](#15-iot-y-redes-industriales) · **RSA** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **RST** → [13](#13-capa-4-transporte) · **RT** → [15](#15-iot-y-redes-industriales) · **RTCP** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **RTP** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **RTU** → [15](#15-iot-y-redes-industriales)
- **S:** **S/FTP** → [6](#6-cobre-cables-blindajes-y-poe) · **SACK** → [13](#13-capa-4-transporte) · **SC** → [7](#7-fibra-óptica) · **SCADA** → [15](#15-iot-y-redes-industriales) · **SCP** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **SCR** → [9](#9-redes-legacy-capa-2-y-mpls) · **SD-WAN** → [12](#12-virtualización-nfv-sdn-y-5g) · **SDH** → [4](#4-jerarquías-digitales-e1-t1-y-sdh) · **SDN** → [12](#12-virtualización-nfv-sdn-y-5g) · **SF/UTP** → [6](#6-cobre-cables-blindajes-y-poe) · **SFP** → [8](#8-data-center) · **SFTP** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **SHA-256** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **SIP** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **SLA** → [2](#2-conceptos-generales-y-tipos-de-red) · **SMB** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **SMTP** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **SNA** → [9](#9-redes-legacy-capa-2-y-mpls) · **SNMP** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **SOAP** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **SOC** → [2](#2-conceptos-generales-y-tipos-de-red) · **SONET** → [4](#4-jerarquías-digitales-e1-t1-y-sdh) · **SR** → [7](#7-fibra-óptica) · **SRTP** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **SSH** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **SSL** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **ST** → [7](#7-fibra-óptica) · **STDM** → [3](#3-transmisión-de-datos-y-multiplexación) · **STM-N** → [4](#4-jerarquías-digitales-e1-t1-y-sdh) · **STP** → [6](#6-cobre-cables-blindajes-y-poe), [9](#9-redes-legacy-capa-2-y-mpls) · **STUN** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **SVC** → [9](#9-redes-legacy-capa-2-y-mpls) · **SWDM** → [7](#7-fibra-óptica) · **SYN** → [13](#13-capa-4-transporte)
- **T:** **T1** → [4](#4-jerarquías-digitales-e1-t1-y-sdh) · **T3** → [4](#4-jerarquías-digitales-e1-t1-y-sdh) · **TCP** → [13](#13-capa-4-transporte) · **TCP/IP** → [5](#5-modelo-de-capas) · **TDM** → [3](#3-transmisión-de-datos-y-multiplexación) · **TELNET** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **TERA** → [6](#6-cobre-cables-blindajes-y-poe) · **TFTP** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **TIA** → [1](#1-organismos-y-estándares) · **TIA-942** → [8](#8-data-center) · **TIFF** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **TLD** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **TLS** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **ToR** → [8](#8-data-center) · **ToS** → [10](#10-capa-3-ip) · **TTL** → [10](#10-capa-3-ip) · **TXT** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación)
- **U:** **U/UTP** → [6](#6-cobre-cables-blindajes-y-poe) · **UBR** → [9](#9-redes-legacy-capa-2-y-mpls) · **UDP** → [13](#13-capa-4-transporte) · **UPC** → [7](#7-fibra-óptica) · **UPS** → [8](#8-data-center) · **URG** → [13](#13-capa-4-transporte) · **URL** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **UTF-16** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **UTF-8** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **UTP** → [6](#6-cobre-cables-blindajes-y-poe)
- **V:** **VBR** → [9](#9-redes-legacy-capa-2-y-mpls) · **VCI** → [9](#9-redes-legacy-capa-2-y-mpls) · **VIM** → [12](#12-virtualización-nfv-sdn-y-5g) · **VLAN** → [9](#9-redes-legacy-capa-2-y-mpls) · **VM** → [12](#12-virtualización-nfv-sdn-y-5g) · **VNF** → [12](#12-virtualización-nfv-sdn-y-5g) · **VNFM** → [12](#12-virtualización-nfv-sdn-y-5g) · **VPI** → [9](#9-redes-legacy-capa-2-y-mpls) · **VPN** → [9](#9-redes-legacy-capa-2-y-mpls) · **vRAN** → [12](#12-virtualización-nfv-sdn-y-5g)
- **W:** **WAN** → [2](#2-conceptos-generales-y-tipos-de-red) · **WBMMF** → [7](#7-fibra-óptica) · **WDM** → [3](#3-transmisión-de-datos-y-multiplexación) · **WWW** → [2](#2-conceptos-generales-y-tipos-de-red)
- **X:** **X.25** → [9](#9-redes-legacy-capa-2-y-mpls) · **XML** → [14](#14-capas-5-6-y-7-sesión-presentación-y-aplicación) · **XX/YTP** → [6](#6-cobre-cables-blindajes-y-poe)
- **Z:** **ZDA** → [8](#8-data-center) · **ZR** → [7](#7-fibra-óptica)
- **λ:** **λ** → [7](#7-fibra-óptica)
