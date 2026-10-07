# 📝 Parciales anteriores resueltos — Redes

> Las consignas de **5 modelos de parcial** de la cátedra (Volpi / Giorgi / Llasat) más las preguntas de parcial que juntaron otros alumnos, **agrupadas por tema**, con la respuesta correcta revisada contra las PPTs.
> Fuentes originales en [`Material extra/`](../Material%20extra/). Para estudiar la teoría, usá el [Resumen para el parcial](Resumen%20para%20el%20parcial.md).

---

## 0. Cómo es el parcial

- **Formato:** unas **6 consignas**:
  - **3 o 4 para desarrollar**: "explicar", "comparar", "dar un ejemplo";
  - **2 múltiple choice** con números: SDH, E1, TDM, OM5.
- **Se repite muchísimo.** Los mismos temas aparecen en casi todos los modelos.

| Tema | Apariciones | Cómo lo preguntan |
| --- | --- | --- |
| **Fibra óptica: cuál elegir / OM4 vs. OM5** | **5 de 5** | "Conectar dos sedes a 8 km (o 12 km): ¿qué fibra?", "Choice OM5", "Comparar OM4 y OM5" |
| **Modelo OSI vs. TCP/IP** | **4 de 5** | "Explicar el modelo OSI y compararlo con TCP/IP" |
| **Data centers y Tiers** | **4 de 5** | "Definir qué es un DC y sus Tiers", "Clasificación según tolerancia a fallas, con ejemplo" |
| **SDH / TDM / E1 (múltiple choice)** | **4 de 5** | Ancho de banda de **STM-1** o **STM-4**, qué es la **trama E1**, máximo de TDM, "Explicar SDH y POS" |
| **ATM** | **3 de 5** | "Definir red ATM, ventajas y para qué servicios se pensó" |
| **Hamming** | **2 de 5** | "Explicar Hamming y un ejemplo práctico" |
| TDM vs. FDM (vs. ATM) | 1 de 5 | "Comparar TDM y ATM. Explicar TDM y FDM" |
| MPLS | 1 de 5 | "MPLS" |
| VPN IP vs. tránsito IP | 1 de 5 | "Comparar VPN IP y tránsito IP" |
| Par trenzado y Ethernet | 1 de 5 | "Tipos de cable de par trenzado y algo de Ethernet" |

> 🔑 **Conclusión:**
> - Si dominás **fibra (elegir y OM4/OM5)**, **OSI vs. TCP/IP**, **Tiers de DC**, la **tabla SDH/E1**, **ATM** y **Hamming**, tenés cubierto lo que salió en casi todos los parciales.
> - **No se tomaron** capas 5–7 ni TCP/UDP en detalle, pero este cuatrimestre la cátedra dijo que **entra hasta la clase #11 (capas 5, 6 y 7)**, así que pueden aparecer.

---

## 1. Fibra óptica

**Se tienen que conectar dos sedes A y B separadas por 8 km (o 12 km). ¿Qué tipo de fibra usar? Justificar y dar un ejemplo.** *(modelos 1, 2, 3)*

**Fibra monomodo (OS2).**
- **Por qué no multimodo:**
  - La luz viaja en **muchos modos** que rebotan dentro del núcleo (50 µm) y llegan desfasados: eso es la **dispersión modal**.
  - Según la tabla de la cátedra, la multimodo alcanza **de 70 a 440 m**, según la velocidad y si es OM3, OM4 u OM5.
  - Con kilómetros es imposible.
- **Por qué monomodo:**
  - El núcleo es de **~9 µm** y lleva **un solo haz de láser**, sin dispersión modal.
  - Las ópticas **LR** (*long reach*) cubren hasta **10 km**, y las **ER/ZR**, 40 km o más.
  - Es la fibra de las **redes WAN, backbones y enlaces entre edificios**.
- **Ejemplo:** interconectar dos sucursales de un banco en distintos barrios, o el troncal de un operador (FTTH, anillos SDH).
- **Para sumar puntos, completá el diseño:**
  - conectores **LC** si hay alta densidad en el rack;
  - pulido **APC** (verde, −60 dB) si la red es crítica o de video, y **UPC** para datos comunes;
  - cable **riser (CMR)** si sube por una montante, **plenum (CMP)** si pasa entre el plafón y la losa, y **libre de halógenos (LSZH)** en ambientes cerrados con gente.

**Comparar OM4 y OM5. / ¿Qué es la OM5 y para qué sirve?** *(modelos 2 y 5)*

| | OM4 | OM5 |
| --- | --- | --- |
| Tipo | Multimodo | Multimodo **de banda ancha** (WBMMF, 2016–2018) |
| Núcleo / revestimiento | 50 / 125 µm | 50 / 125 µm (**igual**) |
| Ancho de banda modal a 850 nm | 4700 MHz·km | 4700 MHz·km (**igual**) |
| Longitudes de onda | **Una** (850 nm) | **Cuatro** con **SWDM** (850–940 nm) por el mismo hilo |
| Alcance 40G-SWDM4 / 100G-SWDM4 | 350 m / 100 m | **440 m / 150 m** |
| Para qué | DC actuales, 10G/40G | DC nuevos: **menos fibras en paralelo**, escalable a **400G/800G** |

Conclusión: **físicamente son iguales**; la diferencia es que la OM5 está optimizada para **multiplexar por longitud de onda (SWDM)**. En un DC nuevo, si la diferencia de costo es chica, conviene OM5.

**Choice OM5** *(modelo 2)* → la opción correcta es la que diga: "multimodo basada en **SWDM**, reduce la cantidad de fibras en paralelo y **escala hacia 400G/800G**".

**Diferencias entre OS2 y OM3/OM4** *(final 06/07/26)*
- **OS2** es **monomodo**: núcleo de 9 µm, un solo haz, **kilómetros**.
- **OM3/OM4** son **multimodo**: núcleo de 50 µm, varios modos, **cientos de metros**, dentro del DC. OM4 tiene más ancho de banda modal que OM3 (4700 vs. 2000 MHz·km), así que llega más lejos a la misma velocidad.

---

## 2. Modelo OSI vs. TCP/IP

**Explicar (sintéticamente) el modelo OSI y compararlo con TCP/IP.** *(modelos 1, 2, 3, 4)*

| # | Capa OSI | Qué hace | Unidad | Ejemplos | TCP/IP |
| --- | --- | --- | --- | --- | --- |
| 7 | **Aplicación** | Servicios de red para las aplicaciones del usuario | Datos | HTTP, FTP, SMTP, DNS | **Aplicación** (une 5, 6 y 7) |
| 6 | **Presentación** | Formato, codificación, **cifrado** y **compresión** | Datos | ASCII, JPEG, MPEG, TLS | ↑ |
| 5 | **Sesión** | Establece, mantiene, **sincroniza** y cierra el diálogo | Datos | NetBIOS, RPC, SIP | ↑ |
| 4 | **Transporte** | Entrega **extremo a extremo**, control de flujo y errores | **Segmento** | **TCP** (confiable), **UDP** (rápido) | **Transporte** |
| 3 | **Red** | **Direccionamiento lógico (IP)** y **ruteo** entre redes distintas | **Paquete** | IP, ICMP, OSPF, BGP | **Internet** |
| 2 | **Enlace de datos** | **Direccionamiento físico (MAC)**, acceso al medio, detección de errores (CRC) | **Trama** | Ethernet 802.3, PPP, ATM | **Acceso a la red** (une 1 y 2) |
| 1 | **Física** | Transmisión de **bits** por el medio (niveles de tensión, conectores) | **Bits** | Cobre, fibra, radio, SDH | ↑ |

**Diferencias:**
- **OSI** es un modelo **teórico/de referencia** de la ISO, con **7 capas**, pensado para **estandarizar**.
- **TCP/IP** es el modelo **práctico, el que realmente usa Internet**, con **4 capas**:
  - **Aplicación** (OSI 5 + 6 + 7);
  - **Transporte** (igual a OSI 4);
  - **Internet** (OSI 3);
  - **Acceso a la red** (OSI 1 + 2).
- En ambos hay **encapsulamiento**: cada capa agrega su cabecera al bajar (datos → segmento → paquete → trama → bits) y se quita al subir.

---

## 3. Data centers

**Definir qué es un data center y sus Tiers. / Explicar los DC y cómo se clasifican según la tolerancia a fallos, con un ejemplo. / Comparar los Tiers.** *(modelos 1, 2, 3, 4)*

**Definición:** un **centro de datos** es una instalación física que **alberga infraestructura informática** (servidores, almacenamiento, red) para **crear, ejecutar y entregar aplicaciones y servicios** y **almacenar sus datos**. Necesita energía estable, refrigeración y operación 24/7. Hoy es el **núcleo** de las redes, porque ahí se virtualizan las funciones de red.

**Clasificación por tipo (con ejemplos):**

| Tipo | Qué es | Ejemplos |
| --- | --- | --- |
| **Enterprise** | Infraestructura **propia** de una empresa, que quiere control total de sus datos | Bancos, aseguradoras, petroleras, gobierno |
| **Cloud / hiperescala** | Recursos **compartidos por millones** de clientes vía Internet | Google, AWS, Azure, Meta |
| **Telco / colocation** | Un operador **alquila** espacio, racks, energía y refrigeración | Telecom, Telefónica, Claro, ARSAT, IPLAN |
| **Edge** | DC **chico, cerca del usuario**, para **baja latencia** | Sucursales de bancos y retail |

**Escala por potencia:**
- **pequeños:** hasta 20 MW;
- **medianos:** 50–100 MW;
- **grandes / hiperescala:** más de 100 MW.

**Tiers del Uptime Institute (tolerancia a fallas):**

| Tier | Nombre | Redundancia | ¿Mantenimiento sin cortar? | ¿Una falla corta el servicio? |
| --- | --- | --- | --- | --- |
| **I** | Capacidades básicas | **Ninguna** | ❌ Hay que **parar todo el sitio** | ✅ Sí |
| **II** | Componentes redundantes | **UPS y generador N+1**, pero **una sola vía** de distribución | ❌ Hay que parar todo el sitio | ✅ Sí, si falla la vía de distribución |
| **III** | **Mantenimiento simultáneo** | Componentes N+1 y **dos vías** (una activa y otra alterna); combustible para **72 h**; **dos proveedores** de telecomunicaciones | ✅ **Sí** | ⚠️ Sigue expuesto a la falla de un equipo o a un error humano |
| **IV** | **Tolerante a fallas** | **Todo duplicado y activo-activo**, incluso **dos proveedores de energía** | ✅ Sí | ❌ **No**: una falla individual no afecta la operación |

> 💡 Dato que suma: en Argentina es casi imposible certificar **Tier IV**, porque la distribución eléctrica de cada zona la tiene **una sola empresa** (Edenor o Edesur) y no hay **doble proveedor** de energía.

**Norma:** **ANSI/TIA-942** (América) o **EN-50600** (Europa). Define:
- el **cableado**;
- las **áreas** (cuarto de entrada ER → MDA → IDA → ZDA → EDA, donde van los racks de servidores);
- la **refrigeración** con **pasillos fríos y calientes**;
- la **redundancia**, con caminos A y B.

---

## 4. SDH, TDM y E1 (múltiple choice)

**¿Cuánto es el ancho de banda de STM-4?** *(modelo 1)* → **622 Mbps** (= 4 × STM-1; equivale a OC-12 de SONET).

**¿Cuál es el ancho de banda máximo de la trama STM-1 de la jerarquía SDH?** *(modelo 3)* → **155 Mbps** (8000 × 270 columnas × 9 filas × 8 bits).

**La trama E1 de multiplexación TDM…** *(modelo 3)*

| Opción | ¿Correcta? | Por qué |
| --- | --- | --- |
| a) Es una tecnología de multiplexado por división de frecuencia | ❌ | Es por división de **tiempo** (TDM) |
| **b) Cuenta con la capacidad de multiplexar 32 intervalos de tiempo, entregando un máximo de 2 Mb** | ✅ | **32 × 64 kbps = 2048 kbps ≈ 2 Mbps** |
| c) Cuenta con 16 canales para multiplexar voz | ❌ | Son **32** intervalos (30 de voz + 2 de señalización y sincronismo) |
| d) Entrega un ancho de banda máximo de 34 Mbps | ❌ | 34 Mbps es la **E3** |

**Máximo de ancho de banda en TDM** *(modelos 1 y 2)* → la consigna es ambigua. Conviene responder con los dos datos:
- En TDM cada usuario usa **el 100% del ancho de banda del canal**, pero solo durante su ranura de tiempo.
- La jerarquía sincrónica llega hasta **STM-256 = 40 Gbps**.

**Explicar SDH y POS.** *(modelo 4)*
- **SDH** (*Synchronous Digital Hierarchy*) es la jerarquía **europea** de transmisión **sincrónica** sobre fibra (capa 1). Su contraparte americana es **SONET**.
  - Los datos se encapsulan en **contenedores**, se les agregan **cabeceras de control** y se multiplexan en tramas **STM-1 (155 Mbps)**.
  - Los niveles superiores multiplexan **a nivel de byte** varios STM-1: STM-4 (622 Mbps), STM-16 (2,5 Gbps), STM-64 (10 Gbps), STM-256 (40 Gbps).
  - Se usa en **redes de transporte** de operadores, en **anillos de fibra**.
- **POS** (*Packet over SONET/SDH*) transporta **paquetes IP directamente sobre tramas SDH/SONET**, encapsulados con PPP/HDLC.
  - Evita pasar por **celdas ATM** y su overhead.
  - Ejemplo: un router con una interfaz STM-1 POS conectado al anillo SDH del operador.

---

## 5. ATM y capa 2

**Definir red ATM, sus ventajas y para qué servicios se pensó. / Desarrollar la tecnología ATM.** *(modelos 1, 3, 4)*

**ATM** (*Asynchronous Transfer Mode*) es una tecnología **legacy de capa 2**, **orientada a conexión** con circuitos virtuales permanentes (**PVC**). Transmite en **celdas de tamaño fijo de 53 bytes**: **48 de datos** y **5 de cabecera**.

**Ventajas:**
- Fue **pionera en QoS**: permitió transportar **voz, video y datos** en la misma red, priorizando el tráfico.
- El tamaño **fijo** de la celda hace el retardo **predecible**, con **bajo jitter**.
- Tiene mucha **capilaridad**: los operadores invirtieron mucho en ATM, así que todavía se usa como **red de acceso** de capa 2.

**Servicios para los que se pensó** (las categorías de QoS):

| Servicio | Garantía | Uso |
| --- | --- | --- |
| **CBR** (Constant Bit Rate) | Ancho de banda **fijo y constante** (el más caro) | **Voz y video en tiempo real** |
| **VBR** (Variable Bit Rate) | Velocidad **media sostenible**, con picos | Video comprimido, datos transaccionales |
| **ABR** (Available Bit Rate) | Un **mínimo garantizado**; usa lo que sobre | Transferencia de datos |
| **UBR** (Unspecified Bit Rate) | **Ninguna** (best effort; el más barato) | Mail, backups |

> **Si me piden transmitir voz entre sucursales sobre ATM, ¿qué servicio contrato?** → **CBR**: la voz es tiempo real y es muy sensible al **jitter**.

**Comparar TDM y ATM. Explicar TDM y FDM.** *(modelo 4)*
- **TDM:** reparte el **tiempo**. Cada canal tiene una **ranura fija** y usa todo el ancho de banda en su turno. En el TDM **sincrónico**, si un canal no tiene datos, su ranura **se desperdicia**.
- **FDM:** reparte la **frecuencia**. Cada canal tiene su **banda** y todos transmiten **a la vez**, separados por **bandas de guarda**. Ejemplo: la radio FM.
- **TDM vs. ATM:**
  - TDM es **rígido**: un slot fijo por canal, aunque no tenga datos.
  - ATM envía **celdas** según la demanda, con **categorías de QoS** (CBR, VBR, ABR, UBR) → aprovecha mejor el enlace sin perder garantías.

**Explicar los protocolos de capa 2: X.25, Frame Relay, ATM** *(Notion y finales)*

| | X.25 | Frame Relay | ATM |
| --- | --- | --- | --- |
| Unidad | Paquetes | **Tramas variables** | **Celdas fijas de 53 bytes** |
| Errores | **Corrige y retransmite** en cada salto (lento) | No retransmite (más eficiente) | No retransmite |
| Velocidad | ~64 kbps | 64–128 kbps (hasta 2 Mbps) | 2 Mbps y más |
| QoS | No | Clases según el contrato | **Nativa** (CBR, VBR, ABR, UBR) |
| Uso | Cajeros, puntos de venta | Reemplazo de X.25 (sucursales) | Voz, video y datos; hoy como acceso |

---

## 6. Hamming

**Explicar Hamming y dar un ejemplo práctico.** *(modelos 4 y 5)*

**Qué es:** un código que **detecta y corrige** el error de **un bit**, sin pedir retransmisión. Agrega **bits de redundancia (K)** a los M bits de datos para que el código tenga **distancia 3**. Regla: para corregir n bits hace falta **d = 2n + 1**.

**Pasos:**
1. **K** es el menor número que cumple **2^K ≥ M + K + 1**. Con M = 4 → **K = 3**, y la palabra tiene 7 bits.
2. Los K van en las posiciones **potencia de 2**: `K1 K2 M3 K4 M5 M6 M7`.
3. Cada K es el XOR de los datos cuya posición "lo contiene":
   - **K1 = M3 ⊕ M5 ⊕ M7**;
   - **K2 = M3 ⊕ M6 ⊕ M7**;
   - **K4 = M5 ⊕ M6 ⊕ M7**.
4. El receptor recalcula **P1, P2 y P4**, y el número **(P4 P2 P1)** da la **posición del error**.

**Ejemplo (el de la PPT):** se transmite `1101001` y llega `1101011`.
- P1 = K1 ⊕ M3 ⊕ M5 ⊕ M7 = 1 ⊕ 0 ⊕ 0 ⊕ 1 = **0**.
- P2 = K2 ⊕ M3 ⊕ M6 ⊕ M7 = 1 ⊕ 0 ⊕ 1 ⊕ 1 = **1**.
- P4 = K4 ⊕ M5 ⊕ M6 ⊕ M7 = 1 ⊕ 0 ⊕ 1 ⊕ 1 = **1**.
- (P4 P2 P1) = 110 = **6** → el bit errado es **M6**; se invierte y se recupera `1101001`.

> ⚠️ El ejemplo del docx ("1011 → 1010011") **no sigue la convención de la cátedra**. Con K1 K2 M3 K4 M5 M6 M7, el dato 1011 se codifica **0110011** (ver la [guía 02](02%20-%20Conceptos%20de%20la%20transmisi%C3%B3n%20de%20datos.md)).

---

## 7. Otros temas que salieron

**MPLS (describir funcionalidades y para qué servicios se usa).** *(modelo 2 y Notion)*
- **Multiprotocol Label Switching** es de **capa 2,5**: la **etiqueta** se inserta **entre la cabecera de capa 2 y la de capa 3**.
- **Cómo funciona:**
  - El router de borde (**LER / PE**) clasifica el paquete y le **pone una etiqueta**.
  - Los routers del núcleo (**LSR / P**) **conmutan mirando solo la etiqueta**, sin leer la IP, siguiendo un camino predefinido (**LSP**).
  - Al salir, se le quita la etiqueta.
- **Ventajas:**
  - **velocidad** (conmutar por etiqueta es más rápido que rutear por IP);
  - **QoS** (la etiqueta tiene 3 bits para prioridad);
  - **escalabilidad**;
  - es **multiprotocolo** (IPv4, IPv6, ATM, Frame Relay).
- **Servicios:** **VPN corporativas** entre sucursales (cada cliente aislado), transporte de **voz y video con prioridad** y acceso a data centers. Es la evolución del modelo IP/ATM.

**Comparar VPN IP y tránsito IP.** *(modelo 5)*

| | VPN IP | Tránsito IP |
| --- | --- | --- |
| Qué es | Una **red privada** montada sobre la infraestructura de un proveedor o de Internet, con **túneles** (cifrados con IPSec o aislados con MPLS) | Un **servicio pago** por el que un carrier más grande te da **salida a toda Internet** |
| Direcciones | **Privadas**, no ruteables en Internet | **Públicas**: anunciás tu bloque y el carrier te da **todas las rutas** de Internet por **BGP** |
| Para qué | Unir **sucursales** entre sí de forma segura | Que una red (ISP chico, empresa grande) llegue a **cualquier destino** de Internet |

> Relacionado (final): **peering** es un intercambio **directo y sin pago** entre dos redes de tamaño parecido, normalmente en un **IXP**; cada una solo accede a las redes de la otra. **Tránsito** es **pago** y da acceso a **todo** Internet.

**Tipos de cable de par trenzado (y algo de Ethernet).** *(modelo 5)*
- **Tipos según el blindaje** (formato XX/YTP: lo que va antes de la barra es el blindaje **general**; lo de después, el de **cada par**):
  - **U/UTP:** sin blindaje; oficinas.
  - **F/UTP (FTP):** lámina general; interferencia moderada.
  - **STP:** blindaje por par.
  - **S/FTP:** lámina por par + malla general; industria y DC.
  - **SF/UTP:** malla + lámina general, pares sin blindar.
  - Los blindados **requieren puesta a tierra**.
- **Categorías:**
  - Cat 5e: 100 MHz, 1 Gbps.
  - **Cat 6:** 250 MHz, 1 Gbps (10 Gbps hasta ~55 m).
  - **Cat 6A:** 500 MHz, **10 Gbps a 100 m**.
  - Cat 8: 2000 MHz, 40 Gbps, **30 m**.
- **Canal Ethernet:** **100 m** (90 m horizontales + patch cords).
- **Ethernet 802.3:**
  - estándar IEEE de **LAN**;
  - trabaja en **capas 1 y 2** (MAC, tramas de 64 a 1518 bytes, CRC);
  - velocidades: 10 Mbps (10BASE-T) → 100 Mbps (Fast Ethernet) → 1 Gbps → 10 Gbps → 40/100/400/800G en DC;
  - admite **PoE**.

---

## 8. Preguntas de la lista de la Notion y de otros cuatrimestres (mismo temario)

Para practicar: cada una tiene la respuesta en la guía indicada.

| Pregunta | Dónde está la respuesta |
| --- | --- |
| Explicar el protocolo **BGP** y compararlo con **OSPF** | [Guía 09](09%20-%20Enrutamiento%20est%C3%A1tico%20y%20din%C3%A1mico%20%28BGP%20y%20OSPF%29.md) |
| Dar un **ejemplo de ruteo con BGP** | [Guía 09](09%20-%20Enrutamiento%20est%C3%A1tico%20y%20din%C3%A1mico%20%28BGP%20y%20OSPF%29.md): esquema de routing |
| ¿Qué es un **AS** y qué tipos hay? | [Guía 09](09%20-%20Enrutamiento%20est%C3%A1tico%20y%20din%C3%A1mico%20%28BGP%20y%20OSPF%29.md) |
| **Clases de IP** y sus rangos. Un bloque **/29**, ¿cuántas IP utilizables tiene? → **2³ − 2 = 6** | [Guía 11](11%20-%20Datagrama%20IP%2C%20TCP%20y%20UDP.md) |
| ¿Cómo se diferencia **IP de la capa 2** y dónde se separan? | [Guía 08](08%20-%20Protocolo%20de%20capa%203%20-%20Internet%20Protocol%20%28IP%29.md) |
| **Vulnerabilidades de UDP** y medidas de protección; **TCP vs. UDP** | [Guía 11](11%20-%20Datagrama%20IP%2C%20TCP%20y%20UDP.md) |
| **QoS**: ¿qué es y cómo se obtiene en capa 2? **Jitter**, **SLA** | [Guía 07](07%20-%20Redes%20legacy%20y%20protocolos%20de%20capa%202.md) y el resumen |
| **Redes Metro**, tipos de servicio, acceso y VPN | [Guía 07](07%20-%20Redes%20legacy%20y%20protocolos%20de%20capa%202.md) |
| **Diafonía**: qué es, **NEXT** y **FEXT** | [Guía 04](04%20-%20Infraestructura%20de%20redes%20-%20Tecnolog%C3%ADas%20de%20cobre.md) |
| ¿Cuál es la **distancia máxima** del cobre? | [Guía 04](04%20-%20Infraestructura%20de%20redes%20-%20Tecnolog%C3%ADas%20de%20cobre.md): 100 m, extendible hasta 250 m |
| **Flamabilidad** de los cables (riser, plenum, LSZH) | [Guía 05](05%20-%20Infraestructura%20de%20redes%20-%20Fibra%20%C3%B3ptica.md) |
| Norma **ANSI/TIA-942**; **dibujar un DC** con sus áreas | [Guía 06](06%20-%20Redes%20en%20el%20centro%20de%20datos.md) |
| **Los 4 hitos del factor tecnológico** | [Guía 10](10%20-%20NFV%20-%20Virtualizaci%C3%B3n%20de%20funciones%20de%20red.md) |
| **NFV**, la virtualización 2.0 y el **edge computing** | [Guía 10](10%20-%20NFV%20-%20Virtualizaci%C3%B3n%20de%20funciones%20de%20red.md) |
| **Martin Cooper**: ¿cómo vaticinar los próximos 50 años? | [Guía 01](01%20-%20Introducci%C3%B3n%20a%20las%20telecomunicaciones%20y%20redes.md) |
| **Comprimir un canal de voz 6 a 1** → entran **6 conversaciones** en un canal de 64 kbps (~10,7 kbps cada una); 60 canales comprimidos ocupan **10** canales de 64 kbps | [Resumen](Resumen%20para%20el%20parcial.md), sección 2 |

**Solo de final** (no entran en el parcial: son de las clases de después): RAN y V-RAN, Wi-Fi 4/5/6/7, beamforming, MU-MIMO y OFDMA, MPLS a fondo (L2VPN vs. L3VPN, operaciones *swap* y *pop*), IoT (LoRa).

---

## 9. Múltiple choice para practicar (armados con el estilo de la cátedra)

1. Un STM-16 tiene un ancho de banda de: a) 622 Mbps · b) 2,5 Gbps · c) 10 Gbps · d) 155 Mbps
2. La trama T1 de la jerarquía americana: a) tiene 32 canales · b) entrega 2 Mbps · c) tiene 24 canales y entrega ≈ 1,5 Mbps · d) es de FDM
3. Una celda ATM mide: a) 48 bytes · b) 53 bytes (48 de datos + 5 de cabecera) · c) 64 bytes · d) es de tamaño variable
4. Para voz sobre ATM conviene contratar: a) UBR · b) ABR · c) VBR · d) CBR
5. La fibra OM5: a) es monomodo · b) tiene núcleo de 9 µm · c) usa SWDM para multiplexar 4 longitudes de onda por hilo · d) llega a 40 km
6. Un Tier III: a) no tiene redundancia · b) permite mantenimiento sin interrumpir la operación · c) tiene dos proveedores de energía · d) es tolerante a cualquier falla
7. El cable Cat 6A: a) trabaja a 250 MHz · b) da 10 Gbps a 100 m · c) llega solo a 30 m · d) no usa RJ45
8. Un bloque /29 tiene: a) 8 IP utilizables · b) 6 IP utilizables · c) 29 IP · d) 30 IP
9. Para corregir 1 bit con Hamming la distancia del código debe ser: a) 1 · b) 2 · c) 3 · d) 4
10. En TDM sincrónico, si un canal no tiene datos: a) su ranura la usa otro · b) su ranura se desperdicia · c) se cambia de frecuencia · d) se descarta la trama

<details>
<summary>Respuestas</summary>

1 **b** (16 × 155 Mbps) · 2 **c** · 3 **b** · 4 **d** · 5 **c** · 6 **b** (los dos proveedores de energía son de Tier IV) · 7 **b** · 8 **b** (2³ − 2) · 9 **c** (d = 2n + 1) · 10 **b** (en el asincrónico la usa otro)

</details>

---

<sub>⚙️ Armado con las consignas de 5 modelos de parcial, las preguntas de parcial de la Notion "Apuntes de Redes" y del repo de P. Ciliberto (1C 2026), y la recopilación de finales 2026. Las respuestas se revisaron contra las PPTs de la cátedra (Volpi / Giorgi / Llasat). Originales en `Material extra/`.</sub>
