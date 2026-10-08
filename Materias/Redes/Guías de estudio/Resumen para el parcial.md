# 📘 Resumen para el parcial — Redes

> **Primer parcial: miércoles 4/11.** Entra **de la clase 1 a la #11** (capas 5, 6 y 7), es decir, las guías **01 a 12**. Es escrito.
> Este resumen junta en un solo lugar **todo lo que entra**, priorizado según lo que se tomó en **5 modelos de parcial** de la cátedra.
>
> **Se complementa con:**
> - 📝 [**Parciales anteriores resueltos**](Parciales%20anteriores%20resueltos.md): cada consigna real con su respuesta modelo, y múltiple choice para practicar.
> - 📚 Las [guías por tema](README.md): para profundizar cuando algo no cierra.

**Leyenda:**
- 🔴 = salió en **casi todos** los parciales;
- 🟠 = salió alguna vez o es muy probable;
- ✍️ = ejercicio para hacer a mano.

---

## 🗓️ Qué entra y cómo estudiarlo

| Clase | Tema | Sección | Guía | Peso en parciales |
| --- | --- | --- | --- | --- |
| 1 | Introducción a las telecomunicaciones | [1](#1-introducción-telecomunicaciones-redes-y-vpn) | [01](01%20-%20Introducci%C3%B3n%20a%20las%20telecomunicaciones%20y%20redes.md) | 🟠 Cooper, acceso vs. transporte |
| 2 | Transmisión de datos | [2](#2-transmisión-de-datos-sdh-tdm-y-hamming) | [02](02%20-%20Conceptos%20de%20la%20transmisi%C3%B3n%20de%20datos.md) | 🔴 **SDH/E1 (choice)** · 🟠 Hamming |
| 3 | Modelo de capas | [3](#3-modelo-osi-vs-tcpip) | [03](03%20-%20Redes%20de%20datos%20y%20modelo%20de%20capas%20%28OSI%29.md) | 🔴 **OSI vs. TCP/IP** |
| 4–5 | Cobre | [4](#4-capa-1-cobre) | [04](04%20-%20Infraestructura%20de%20redes%20-%20Tecnolog%C3%ADas%20de%20cobre.md) | 🟠 Tipos de par trenzado, Cat 6 vs. 6A |
| 5 | Fibra óptica | [5](#5-capa-1-fibra-óptica) | [05](05%20-%20Infraestructura%20de%20redes%20-%20Fibra%20%C3%B3ptica.md) | 🔴 **Qué fibra elijo · OM4 vs. OM5** |
| 6 | Data center | [6](#6-data-centers) | [06](06%20-%20Redes%20en%20el%20centro%20de%20datos.md) | 🔴 **Tipos y Tiers** |
| 7 | Legacy y capa 2 | [7](#7-redes-legacy-y-capa-2) | [07](07%20-%20Redes%20legacy%20y%20protocolos%20de%20capa%202.md) | 🔴 **ATM** · 🟠 TDM/FDM, MPLS, QoS |
| 8–9 | IP, sistemas autónomos, BGP/OSPF | [8](#8-capa-3-ip-ruteo-bgp-y-ospf) | [08](08%20-%20Protocolo%20de%20capa%203%20-%20Internet%20Protocol%20%28IP%29.md) · [09](09%20-%20Enrutamiento%20est%C3%A1tico%20y%20din%C3%A1mico%20%28BGP%20y%20OSPF%29.md) | 🟠 VPN IP vs. tránsito, peering, /29 |
| — | NFV / factor tecnológico | [9](#9-nfv-y-factor-tecnológico) | [10](10%20-%20NFV%20-%20Virtualizaci%C3%B3n%20de%20funciones%20de%20red.md) | 🟠 4 pilares, NFV |
| 10 | Capa 4: TCP y UDP | [10](#10-capa-4-tcp-y-udp) | [11](11%20-%20Datagrama%20IP%2C%20TCP%20y%20UDP.md) | 🟠 TCP vs. UDP, seguridad de UDP |
| 11 | Capas 5, 6 y 7 | [11](#11-capas-5-6-y-7) | [12](12%20-%20Capas%205%2C%206%20y%207%20-%20Sesi%C3%B3n%2C%20Presentaci%C3%B3n%20y%20Aplicaci%C3%B3n.md) | 🟠 Nuevo: no salió en parciales viejos |

**No entra:** MPLS a fondo (L2VPN/L3VPN, PHP), RAN y V-RAN, Wi-Fi, IoT/LoRa. Son de las clases posteriores y se toman en el **final**.

### Plan de estudio: del viernes 16/10 al miércoles 4/11

> Hasta el **jueves 15/10** la prioridad es el **parcial de Ciencia de Datos**. Redes arranca el viernes 16, con ritmo tranquilo: unos **40–60 minutos** los días de estudio.

| Fecha | Qué hacer |
| --- | --- |
| **Vie 16 – Dom 18/10** | Secciones **1–3** (intro, transmisión de datos, OSI vs. TCP/IP). Empezá a memorizar la [tabla de números](#-números-para-saber-de-memoria). Hacé el ✍️ de **Hamming a mano** |
| **Lun 19 – Jue 22/10** | Semana de la **exposición del TP1 de CdD**: solo un repaso corto de la tabla de números y de **OSI vs. TCP/IP** escrito de memoria |
| **Vie 23 – Sáb 24/10** | Secciones **4–5** (cobre y fibra). Escribí sin mirar "**conectar dos sedes a 8 km**" y "**OM4 vs. OM5**" |
| **Dom 25 – Lun 26/10** | Secciones **6–7** (data centers y legacy). Escribí de memoria la **tabla de Tiers** y la de **ATM (CBR/VBR/ABR/UBR)** |
| **Mar 27 – Mié 28/10** | Secciones **8–9** (IP, /29, BGP/OSPF, peering vs. tránsito, NFV y los 4 pilares) |
| **Jue 29 – Vie 30/10** | Secciones **10–11** (TCP/UDP, puertos, TLS, capa 5 vs. capa 4) |
| **Sáb 31/10** | **Simulacro 1:** un modelo de [Parciales anteriores](Parciales%20anteriores%20resueltos.md) completo **sin mirar**, con reloj. Corregí y anotá qué falló |
| **Dom 1/11** | Repasá lo que falló en el simulacro, con la guía de ese tema |
| **Lun 2/11** | **Simulacro 2:** otro modelo + los **10 múltiple choice** + las [preguntas para practicar](#14-preguntas-para-practicar-sin-mirar) 🔴 |
| **Mar 3/11** | Repaso liviano: [6 preguntas que más se repiten](#-las-6-preguntas-que-más-se-repiten), [¿qué elijo?](#12-qué-elijo-preguntas-de-diseño) y [Ojo con esto](#13-ojo-con-esto-errores-típicos) |
| **Mié 4/11** | 🎯 **Parcial.** A la mañana, solo la tabla de números |

> 💡 Las clases de Redes posteriores a la #11 (MPLS a fondo, RAN, Wi-Fi) **no entran en este parcial**: alcanza con ir a cursarlas.

---

## 🔴 Las 6 preguntas que más se repiten

1. **¿Qué fibra usás para unir dos sedes a 8 km (o 12 km)? Justificá.** → **Monomodo OS2**.
   - La multimodo sufre **dispersión modal** y llega a cientos de metros.
   - La monomodo (núcleo de 9 µm, un solo haz) cubre **kilómetros**: LR hasta 10 km.
2. **Modelo OSI vs. TCP/IP.** → 7 capas (teórico, ISO) contra 4 (práctico, Internet).
   - 5 + 6 + 7 → Aplicación;
   - 4 → Transporte;
   - 3 → Internet;
   - 1 + 2 → Acceso a la red.
3. **Data center: definición, tipos y Tiers.** → Enterprise, Cloud, Colocation y Edge. **Tier I** básico, **II** componentes redundantes, **III** mantenimiento simultáneo, **IV** tolerante a fallas.
4. **Choice de SDH/E1.** → **E1 = 32 × 64 kbps = 2 Mbps**, **E3 = 34 Mbps**, **STM-1 = 155 Mbps**, **STM-4 = 622 Mbps**.
5. **ATM: qué es, ventajas, servicios.** → Celdas de **53 bytes** (48 + 5), **pionera en QoS**, servicios **CBR / VBR / ABR / UBR**.
6. **OM4 vs. OM5.** → Iguales físicamente (50 µm). **OM5 = SWDM** (4 longitudes de onda por hilo) → menos fibras, más alcance y preparada para 400G/800G.

---

## 1. Introducción: telecomunicaciones, redes y VPN

- **Martin Cooper** inventó el **celular** en **1973** (Nueva York).
  - **Visión:** dispositivos **incrustados en el cuerpo**, alimentados por el cuerpo, para **diagnosticar y curar** a distancia.
  - **El freno, según él:** la **resistencia de la gente al cambio**.
  - **Lema:** *"el futuro hay que crearlo"*.
- **Integración:**
  - **Antes:** redes **paralelas** (SNA para datos, LAN to LAN, telefonía PSTN/ISDN) → **mínimo uso de los recursos**.
  - **Hoy:** **voz y datos sobre una red IP/MPLS** única, con QoS.
- **VPN:**
  - **intranet:** sucursales;
  - **extranet:** partners;
  - **acceso remoto:** usuarios móviles, con túneles encriptados sobre una red pública.
- 🟠 **Red de acceso vs. red de transporte:**
  - **Acceso** (última milla):
    - **hogares:** ADSL, cablemódem, FTTH;
    - **empresas:** accesos **dedicados** (Metro LAN to LAN, SDH, ATM) o **VPN IP**.
  - **Transporte:** el **backbone** de gran capacidad (fibra, **cables submarinos** con amplificadores EDFA, satélite).
- **LAN vs. WAN:**
  - **LAN:** área reducida, con Ethernet o Wi-Fi.
  - **WAN:** une LAN dispersas. Puede ser de circuitos conmutados, paquetes conmutados u MPLS.

---

## 2. Transmisión de datos: SDH, TDM y Hamming

- **Analógica vs. digital:**
  - **Analógica:** continua; infinitos valores entre un mínimo y un máximo.
  - **Digital:** **dos niveles** (0 y 1).
- **Nyquist:** **FM ≥ 2 × FS**, es decir, muestrear al menos al doble de la frecuencia máxima. La voz (4 kHz) se muestrea a **8 kHz**.
- **Conversión A/D:** el **muestreador** toma muestras a FM (de infinitos puntos a finitos) y el **conversor** pasa cada muestra a binario con una tabla.
- **Canal de voz** = 8000 muestras/s × 8 bits = **64 kbps**.
  - 🟠 Con **compresión 6 a 1**, entran **6 conversaciones** en un canal de 64 kbps (~10,7 kbps cada una).
  - Así, **60 canales** comprimidos ocupan **10 canales** de 64 kbps.

### SDH / SONET 🔴

| SDH (Europa / Argentina) | Velocidad | SONET (EE. UU.) |
| --- | --- | --- |
| **E1** | **2 Mbps** = 32 × 64 kbps | **T1**: 1,5 Mbps = 24 × 64 kbps |
| **E3** | **34 Mbps** | T3: 44,7 Mbps |
| **STM-1** | **155 Mbps** = 8000 × 270 × 9 × 8 | OC-3 |
| **STM-4** | **622 Mbps** | OC-12 |
| **STM-16** | **2,5 Gbps** | OC-48 |
| **STM-64** | **10 Gbps** | OC-192 |
| **STM-256** | **40 Gbps** | OC-768 |

- **STM-N = N × STM-1**, multiplexando **a nivel de byte**.
- Los datos van en **contenedores** con **cabeceras de control**.
- SDH/SONET es **capa 1** (la "autopista" óptica de los operadores, en anillos).
- **POS** (*Packet over SONET/SDH*): IP **directo sobre SDH**, sin celdas ATM.

### Multiplexación 🟠

| | Reparte | Cómo | Ejemplo |
| --- | --- | --- | --- |
| **TDM** | **Tiempo** | Cada canal usa **todo** el ancho de banda **en su ranura** | E1 |
| **FDM** | **Frecuencia** | Cada canal tiene **su banda**, todos **a la vez**, con **bandas de guarda** | Radio FM |
| **WDM** | **Longitud de onda** | Un "color" de luz por canal en la misma fibra | SWDM en OM5 |

- **TDM sincrónico:** ranuras **fijas**; si un canal no tiene datos, la ranura **se desperdicia**.
- **TDM asincrónico (estadístico):** asignación **dinámica**.
- **MUX:** 2ⁿ entradas → 1 salida, con n controles. **DEMUX:** 1 entrada → 2ⁿ salidas.

### Hamming 🟠 ✍️
- **Distancia del código (d):**
  - con **d = 2** se **detecta** 1 error (es lo que logra el bit de paridad);
  - con **d = 3** se **corrige** 1 error;
  - en general, **d = 2n + 1** corrige n errores.
- **Pasos:**
  1. **K bits de redundancia:** el menor K con **2^K ≥ M + K + 1** (M = 4 → K = 3, palabra de 7 bits).
  2. **Orden:** `K1 K2 M3 K4 M5 M6 M7` (los K en las posiciones potencia de 2).
  3. **Cálculo:** K1 = M3⊕M5⊕M7 · K2 = M3⊕M6⊕M7 · K4 = M5⊕M6⊕M7.
  4. **En el receptor:** se recalculan P1, P2 y P4. **(P4 P2 P1) en binario = posición del error.**
- **Ejemplo de la PPT:** se envía `1101001` y llega `1101011` → P1 = 0, P2 = 1, P4 = 1 → **110 = 6** → se corrige M6.

> ✍️ **Practicá:** codificá **1011** → `0110011`. Si llega `0110111` → (P4 P2 P1) = 101 = **5**. La resolución completa está en la [guía 02](02%20-%20Conceptos%20de%20la%20transmisi%C3%B3n%20de%20datos.md).

---

## 3. Modelo OSI vs. TCP/IP

| # | OSI | Función | Unidad | Protocolos | TCP/IP |
| --- | --- | --- | --- | --- | --- |
| 7 | Aplicación | Servicios de red para las aplicaciones | Datos | HTTP, FTP, SMTP, DNS | **Aplicación** |
| 6 | Presentación | Formato, cifrado, compresión | Datos | ASCII, JPEG, MPEG, TLS | ↑ |
| 5 | Sesión | Abrir, mantener, sincronizar y cerrar el diálogo | Datos | NetBIOS, RPC, SIP | ↑ |
| 4 | Transporte | Extremo a extremo, flujo y errores | **Segmento** | TCP, UDP | **Transporte** |
| 3 | Red | **IP lógica** + **ruteo** | **Paquete** | IP, ICMP, OSPF, BGP | **Internet** |
| 2 | Enlace | **MAC**, acceso al medio, **CRC** | **Trama** | Ethernet, PPP, ATM | **Acceso a la red** |
| 1 | Física | **Bits** por el medio | **Bits** | Cobre, fibra, radio, SDH | ↑ |

- **OSI** = **teórico, de referencia (ISO)**, 7 capas, para estandarizar.
- **TCP/IP** = **práctico, el de Internet**, 4 capas.
- **Encapsulamiento:** al bajar, cada capa agrega su cabecera (datos → segmento → paquete → trama → bits). Al subir, en el receptor, se quitan.
- Regla mnemotécnica de abajo hacia arriba: **F**ísica, **E**nlace, **R**ed, **T**ransporte, **Se**sión, **P**resentación, **A**plicación.

---

## 4. Capa 1: cobre

- **Ventajas:** barato, flexible, larga vida útil. **Desventajas:** se degrada con la **distancia** y tiene **más errores** que la fibra.
- **Diafonía (crosstalk):** interferencia entre pares del mismo cable, por el campo electromagnético.
  - **NEXT:** en el extremo **cercano** (el transmisor).
  - **FEXT:** en el extremo **lejano** (el receptor).
  - Se reduce con el **trenzado** y con la **cruceta** separadora.
- **Blindaje (XX/YTP: antes de la barra, el general; después, el de cada par):**
  - **U/UTP:** sin blindaje; oficina.
  - **F/UTP:** lámina general.
  - **S/FTP:** lámina por par + malla general; industria y DC.
  - **SF/UTP:** malla + lámina general, pares sin blindar.
  - Los blindados llevan **puesta a tierra**.

| Categoría | MHz | Velocidad | Distancia |
| --- | --- | --- | --- |
| Cat 5e | 100 | 1 Gbps | 100 m |
| **Cat 6** | **250** | 1 Gbps (10G hasta **~55 m**) | 100 m |
| **Cat 6A** | **500** | **10 Gbps** | **100 m** |
| Cat 7 | 600 | 10 Gbps (sin RJ45) | 100 m |
| Cat 8 | 2000 | 25–40 Gbps | **30 m** |

- **Canal Ethernet = 100 m** (90 m horizontales + patch cords).
- **Distancias extendidas** de hasta **250 m**: a más distancia, menos velocidad, pero se mantiene PoE.
- **PoE:**
  - **15 W** (802.3af, Type 1);
  - **30 W** (802.3at, Type 2, PoE+);
  - **60 W** (802.3bt, Type 3);
  - **90 W** (802.3bt, Type 4).
- **Racks:** **19"** de ancho; **1U = 1,75" ≈ 4,45 cm**; típico de DC: **42U**.

---

## 5. Capa 1: fibra óptica

| | **Monomodo (OS2)** | **Multimodo (OM3 / OM4 / OM5)** |
| --- | --- | --- |
| Núcleo | **~9 µm** | **50 µm** (OM1: 62,5 µm) |
| Luz | **Un solo haz** de láser | **Varios modos** que rebotan (**dispersión modal**) |
| Alcance | **Kilómetros**: LR hasta 10 km, ER/ZR 40 km o más | **Cientos de metros**: de 70 a 440 m según la velocidad |
| Uso | WAN, entre edificios y sedes, operadores, submarinos | Dentro del DC y del edificio |

- **OM4 vs. OM5** 🔴:
  - **Iguales:** 50 µm y **4700 MHz·km** a 850 nm.
  - **Diferencia:** la **OM5 usa SWDM**, con **4 longitudes de onda por hilo**. Así necesita **menos fibras en paralelo**, llega **más lejos** (40G-SWDM4: 440 vs. 350 m; 100G-SWDM4: 150 vs. 100 m) y queda preparada para **400G/800G**.
- **Conectores:**
  - **LC:** el más usado; alta densidad.
  - **SC:** push-pull.
  - **ST:** bayoneta.
  - **FC:** rosca; ambientes con **vibración**.
  - **MPO:** **multifibra** (12–24 fibras); 40G a 400G.
- **Pulido de la férula:**
  - **PC:** −30 a −40 dB.
  - **UPC:** −40 a −55 dB.
  - **APC:** **8°, verde, −60 dB** (el mejor).
- **Flamabilidad:**
  - **Riser (CMR):** montante vertical. Puede prender, pero se apaga antes de 1,5 m.
  - **Plenum (CMP):** entre el plafón y la losa, donde circula aire. **No genera llama** y hace muy poco humo.
  - **LSZH:** libre de halógenos, para que el humo **no sea tóxico**.
- **Calidad del enlace:**
  - **Limpieza:** la suciedad del conector es la **falla más común**. Se inspecciona con microscopio (norma IEC 61300-3-35).
  - **Medición:** potencia con **OPM + fuente**; el **OTDR** localiza empalmes y cortes.
- **NRZ → PAM4:** de 2 niveles (1 bit por símbolo) a 4 niveles (2 bits): **duplica** la tasa sin cambiar la fibra.

---

## 6. Data centers

- **Qué es:** una instalación que **aloja infraestructura informática** (servidores, almacenamiento, red) para **ejecutar y entregar aplicaciones y almacenar datos**, con energía, refrigeración y seguridad 24/7.
- **Tipos:**
  - **Enterprise:** propio (bancos, petroleras).
  - **Cloud / hiperescala:** compartido (Google, AWS, Azure).
  - **Colocation / telco:** alquila espacio (Telecom, Claro, ARSAT).
  - **Edge:** chico y cerca del usuario, para **baja latencia**.
- **Escala:** pequeño hasta 20 MW · mediano 50–100 MW · grande más de 100 MW.
- **Norma ANSI/TIA-942** (Europa: EN-50600). Áreas: **ER** (entrada de proveedores) → **MDA** (distribución principal, core) → IDA → **ZDA** → **EDA** (racks de servidores).
  - **Pasillos fríos y calientes**.
  - **Caminos A y B** redundantes.

| Tier | Nombre | Clave |
| --- | --- | --- |
| **I** | Capacidades básicas | Sin redundancia; el mantenimiento **para todo el sitio** |
| **II** | Componentes redundantes | UPS y generador **N+1**, pero **una sola vía** de distribución; también hay que parar para mantener |
| **III** | **Mantenimiento simultáneo** | **Dos vías** (activa + alterna): se mantiene **sin cortar**; combustible para **72 h**; **2 proveedores de telecomunicaciones** |
| **IV** | **Tolerante a fallas** | Todo duplicado y **activo-activo**, **2 proveedores de energía**. Casi imposible en Argentina (un solo distribuidor eléctrico por zona) |

- **Spine-leaf:**
  - **Modelo viejo:** core–agregación–acceso, pensado para tráfico norte-sur.
  - **Spine-leaf:** cada leaf se conecta con **todos** los spines → **menor latencia** y mejor manejo del tráfico **este-oeste** (entre servidores).
- **Transceptores:**
  - SFP (1G) · SFP+ (10G) · SFP28 (25G) · QSFP28 (100G) · QSFP-DD (400G).
  - Sufijos: **SR** = multimodo, corto alcance; **LR** = monomodo, 10 km.

---

## 7. Redes legacy y capa 2

| | X.25 | **TDM** | Frame Relay | **ATM** 🔴 |
| --- | --- | --- | --- | --- |
| Idea | Paquetes con **corrección y retransmisión** | **Ranuras de tiempo** fijas | **Tramas variables** | **Celdas fijas de 53 bytes** (48 + 5) |
| Fuerte | Muy confiable | Determinístico | Eficiente para ráfagas | **QoS nativa** |
| Débil | Lento (~64 kbps) | Desperdicia ranuras | Velocidades bajas | Overhead al llevar IP |
| Uso | Cajeros, puntos de venta | Telefonía (E1/T1) | Reemplazo de X.25 | Voz + video + datos; hoy como acceso |

**ATM:**
- **Orientado a conexión**, con circuitos virtuales permanentes (**PVC**), identificados por **VPI/VCI**.
- Capas de adaptación: **AAL1** para tráfico constante y **AAL5** para datos.

| Servicio ATM | Garantiza | Para |
| --- | --- | --- |
| **CBR** | Ancho de banda **constante** (el más caro) | **Voz y video en tiempo real** → *"voz entre sucursales sobre ATM: contratá CBR"* |
| **VBR** | Velocidad media sostenible, con picos | Video comprimido, datos transaccionales |
| **ABR** | Un **mínimo** garantizado + lo que sobre | Datos |
| **UBR** | **Nada** (best effort; el más barato) | Mail, backups |

- **Capa 2:**
  - **MAC** (6 bytes, la asigna el fabricante);
  - detección de errores con **CRC**;
  - control de acceso al medio: **CSMA/CD** en Ethernet, **CSMA/CA** en Wi-Fi;
  - **STP** evita bucles.
- **Trama Ethernet 802.3:** de **64 a 1518 bytes**. El **switch** manda la trama solo al puerto de la MAC destino; el **hub** la repite a todos.
- **Ethernet 802.3:** de 10 Mbps (10BASE-T) a 100 Mbps, 1 Gbps, 10 Gbps y 40/100/400/800G. Trabaja en **capas 1 y 2**.
- 🟠 **QoS:** priorizar tráfico para controlar **latencia, jitter y pérdida**. En capa 2 se logra con:
  - **ATM** (categorías de servicio y bit CLP);
  - **MPLS** (3 bits de prioridad en la etiqueta);
  - **Frame Relay** (clases según el contrato).
  - **Jitter:** la **variación** de la demora entre paquetes, en ms. Es crítico para VoIP y video.
  - **SLA:** contrato que **garantiza** disponibilidad (ej. > 99,9%), latencia, jitter y pérdida.
- **Metro Ethernet:** **capa 2 extendida** a nivel ciudad, que une LAN de sucursales. Es **multiservicio**, cuida el jitter y suele usarse como **acceso a la red MPLS** del operador.
- 🟠 **MPLS:** **capa 2,5**. El router de borde (**LER**) **etiqueta** el paquete y los del núcleo (**LSR**) conmutan **solo por la etiqueta** a lo largo de un camino (**LSP**).
  - **Ventajas:** velocidad, **QoS**, escalabilidad, multiprotocolo y **VPN MPLS**.
  - Reemplazó al modelo **IP sobre ATM**, que tenía mucho overhead y dos redes separadas.

---

## 8. Capa 3: IP, ruteo, BGP y OSPF

- **IP:**
  - direccionamiento **lógico** de 32 bits y **ruteo** entre redes distintas;
  - es **no orientado a conexión** y **no confiable** (best effort): la confiabilidad se la da **TCP**;
  - va dentro de una **trama** de capa 2 (Ethernet, ATM, POS).
- **Clases:** A (0–127) · B (128–191) · C (192–223) · D (224–239, multicast) · E (240–255, experimental).
- **Máscara:** con un **AND** bit a bit entre la IP y la máscara se obtiene la **red**.
  - Si el destino está en la misma red, la entrega es **directa**; si no, va al **gateway**.
- ✍️ **IP utilizables en un /n = 2^(32−n) − 2**, porque se restan la de red y la de broadcast. **/29 → 8 − 2 = 6.** /24 → 254.
- **Pública vs. privada:**
  - **Pública:** única en el mundo, visible en Internet.
  - **Privada:** solo dentro de la LAN; sale a Internet con **NAT**.
- **Estático vs. dinámico:**
  - **Estático:** a mano; simple, pero no se adapta a fallas.
  - **Dinámico:** con protocolos que actualizan las tablas solos.
- **Sistema autónomo (AS):** conjunto de redes de **una sola entidad** con su propia política de ruteo, identificado por un **ASN** (ej.: Telecentro es el AS 27747).

| | **OSPF** | **BGP** |
| --- | --- | --- |
| Ámbito | **Dentro** de un AS (IGP) | **Entre** AS (EGP): **el protocolo de Internet** |
| Cómo decide | **Camino más corto** (estado de enlace, Dijkstra) | Por **políticas** y atributos (path vector) |
| Analogía | El GPS de tu ciudad | Los acuerdos de frontera entre países |

| | **Peering** | **Tránsito IP** | **VPN IP** |
| --- | --- | --- | --- |
| Qué es | Intercambio **directo** entre dos redes, en un **IXP** | Servicio **pago** a un carrier más grande | Red **privada** sobre una infraestructura compartida |
| Costo | **Sin pago** (tráfico balanceado) | Pago | Pago |
| Alcance | Solo las redes del otro | **Toda Internet** (rutas completas por BGP) | Solo las sedes propias (IP **privadas**) |

- **ICMP:** los mensajes de control y error de la capa 3 (`ping`, `traceroute`).

---

## 9. NFV y factor tecnológico

- 🟠 **Los 4 pilares del factor tecnológico** ("redes escalables"):
  1. **Escalabilidad / capacidad:** más dispositivos y más servicios.
  2. **Convergencia:** soluciones completas que abarcan varias capas.
  3. **Seguridad:** autenticación, políticas, encriptación.
  4. **Disponibilidad:** **> 99,99%**, con resiliencia.
- **Drivers de la red:** **voice driven** (TDM) → **data driven** (IP) → **deterministic driven** (plataformas de software, con QoS predecible).
- **Evolución en 8 pasos:**
  1. Hardware dedicado.
  2. Cliente-servidor.
  3. Consolidación IP (multiservicio).
  4. Virtualización de servidores (VM).
  5. Data centers y redes virtuales.
  6. Separación de los planos de control y de datos.
  7. **SDN**.
  8. **NFV**.
- **NFV:** las funciones de red dejan de ser **cajas propietarias** ("una función = un equipo") y pasan a ser **software (VNF)** sobre **hardware estándar (COTS)**, con una capa de VM o contenedores (VMware, OpenStack, Kubernetes).
  - **Ejemplos de VNF:** vRouter, vFirewall, vLoad Balancer, vEPC/v5G.
  - **Ventajas:** despliegue rápido, escalabilidad, automatización, **menor costo** (de CAPEX a OPEX).
- **VM vs. contenedor:**
  - La **VM** incluye un SO completo: más pesada y más aislada.
  - El **contenedor** comparte el SO del host: es liviano y portable.
- **Del cloud al edge:**
  - Procesar **cerca del usuario** (edge) baja la **latencia** y ahorra ancho de banda.
  - Cerca de la **nube**: más escala.
  - **Fog:** el intermedio.

---

## 10. Capa 4: TCP y UDP

| | **TCP** | **UDP** |
| --- | --- | --- |
| Conexión | **Orientado a conexión**: handshake **SYN → SYN-ACK → ACK** | **Sin conexión** |
| Confiabilidad | **ACK, retransmisión, orden** (números de secuencia) | **Ninguna** (best effort) |
| Flujo / congestión | **Sí**: ventana deslizante y control de congestión | No |
| Cabecera | **20–60 bytes** | **8 bytes** |
| Velocidad | Más lento | **Más rápido**, baja latencia |
| Usos | Web (HTTP), FTP, SMTP, SSH | **DNS**, DHCP, **VoIP**, streaming, juegos, **QUIC** |

- **Socket** = IP + puerto. **Puertos bien conocidos:** del 0 al 1023.
- 🟠 **Vulnerabilidades de UDP:**
  - **UDP flood:** saturar al destino.
  - **Reflexión / amplificación:** se falsifica la IP de la víctima, y las respuestas (ej. DNS) le llegan agrandadas.
  - **Buffer overflow**.
  - **Loop DoS**.
- **Protecciones:**
  - **firewall/IPS** y cerrar los puertos que no se usan;
  - **rate limiting**;
  - validar los datos que llegan;
  - **monitoreo** con SNMP o NetFlow en el **NOC/SOC**, más soluciones anti-DDoS;
  - protocolos nuevos: **QUIC** (UDP + TLS 1.3, la base de HTTP/3) y **DTLS** (TLS para UDP).

---

## 11. Capas 5, 6 y 7

- **Capa 5 (Sesión):** **establece, mantiene, controla el diálogo, sincroniza, recupera y finaliza** sesiones.
  - **Puntos de control:** si se corta, **retoma desde el último**, sin reenviar todo.
  - **Primitivas:** ESTABLECER, UTILIZAR, SINCRONIZAR, FINALIZAR.
  - **Protocolos:** NetBIOS/SMB, RPC, PPTP/L2TP, SQL Net, NFS, SIP.
  - **Capa 4 vs. capa 5:** la 4 asegura que **los datos lleguen**; la 5 **organiza el diálogo** en el tiempo.
- **Capa 6 (Presentación):** traduce formatos (EBCDIC ↔ ASCII, UTF-8), **cifra**, **comprime** y estructura los datos (ASN.1, JSON, XML).
  - **Cifrado:**
    - **simétrico** (AES): misma clave para cifrar y descifrar;
    - **asimétrico** (RSA): clave pública y privada;
    - **hash** (SHA-256): **no es cifrado**, garantiza la **integridad**.
  - **Compresión:** **sin pérdida** (gzip) o **con pérdida** (JPEG, MP3).
  - **TLS:**
    - **Handshake:** Client Hello → Server Hello + certificado → verificación → intercambio de claves → clave de sesión → datos cifrados.
    - **Garantiza:** confidencialidad, integridad y autenticación.
- **Capa 7 (Aplicación):** los servicios que usan los programas.

| Protocolo | Puerto | | Protocolo | Puerto |
| --- | --- | --- | --- | --- |
| HTTP / HTTPS | 80 / 443 TCP | | DNS | **53 UDP** |
| FTP | 20 (datos) / 21 (control) TCP | | DHCP | **67/68 UDP** |
| SSH | 22 TCP | | SNMP | **161/162 UDP** |
| SMTP | 25 TCP (587/465 con cifrado) | | NTP | **123 UDP** |
| POP3 / IMAP | 110 / 143 TCP | | Telnet | 23 TCP (inseguro) |

- **POP3 descarga y borra** del servidor; **IMAP sincroniza** y deja los mails en el servidor. **SMTP envía**.
- **Las capas se mezclan:** en **HTTPS** (HTTP → TLS → TCP → IP) cada protocolo cumple el rol de una capa. **QUIC** junta transporte, conexión, TLS y multiplexación **sobre UDP**.

---

## 🔢 Números para saber de memoria

| Dato | Valor |
| --- | --- |
| Canal de voz | **64 kbps** (8 kHz × 8 bits) |
| E1 / T1 / E3 | **2 Mbps** (32 canales) / **1,5 Mbps** (24 canales) / **34 Mbps** |
| STM-1 / 4 / 16 / 64 / 256 | **155 Mbps / 622 Mbps / 2,5 Gbps / 10 Gbps / 40 Gbps** |
| Celda ATM | **53 bytes** = 48 de datos + 5 de cabecera |
| Trama Ethernet | **64 a 1518 bytes** · MAC de **6 bytes** |
| Canal de cobre | **100 m** (90 m + patch cords) · extendido hasta **250 m** · Cat 8: **30 m** |
| Cat 6 / 6A | **250 MHz** (10G hasta 55 m) / **500 MHz** (10G a 100 m) |
| PoE | **15 / 30 / 60 / 90 W** |
| Núcleo de la fibra | Monomodo **9 µm** · multimodo **50 µm** (OM1: 62,5) |
| OM4 / OM5 | **4700 MHz·km** a 850 nm · OM5 = **SWDM, 4 λ** |
| Pulido APC | **8°**, verde, **−60 dB** |
| Rack | **19"** · 1U = **1,75"** (4,45 cm) · **42U** |
| Escala de un DC | < 20 MW · 50–100 MW · > 100 MW |
| Tier III | Combustible para **72 h** |
| Cabeceras | IP **20 bytes** · TCP **20–60** · UDP **8** |
| /29 | **6** IP utilizables |
| Hamming (4 bits) | **K = 3** → palabra de 7 bits · corregir 1 bit → **d = 3** |

---

## 12. ¿Qué elijo? (preguntas de diseño)

| Si el enunciado dice… | Respuesta |
| --- | --- |
| Unir dos **sedes a varios km** | **Monomodo OS2** (LR) |
| Enlaces **dentro del DC**, pensando en **400G/800G** | **Multimodo OM5** (SWDM) |
| Poco espacio en el rack | Conector **LC** (o **MPO** para 40G–400G en paralelo) |
| Ambiente **industrial con vibración** | Conector **FC** (a rosca) |
| El cable pasa **entre el plafón y la losa** (con aire acondicionado) | **Plenum (CMP)** |
| El cable **sube entre pisos** | **Riser (CMR)** |
| Hospital o túnel (importa el **humo tóxico**) | **LSZH** (libre de halógenos) |
| Cableado de una **oficina nueva** | **Cat 6A** (10 Gbps a 100 m; vida útil de 20–25 años) |
| Fábrica con **mucho ruido** eléctrico | **S/FTP** con puesta a tierra |
| Alimentar una cámara o un AP por el mismo cable | **PoE** (30 W si es una cámara PTZ) |
| **Voz** sobre ATM | **CBR** |
| Un DC que **no puede parar** para mantenimiento | Mínimo **Tier III**; si tampoco puede caerse ante una falla, **Tier IV** |
| Aplicación en **tiempo real** (VoIP, juegos) | **UDP** |
| Necesito que **llegue todo y en orden** | **TCP** |
| Salir a **toda Internet** | **Tránsito IP** |
| Unir **sucursales** de forma privada | **VPN** (IP o MPLS) |

---

## 13. Ojo con esto (errores típicos)

| Error común | Lo correcto |
| --- | --- |
| E1 tiene 16 canales o es de FDM | **32 intervalos** de 64 kbps, por **TDM** = 2 Mbps |
| 34 Mbps es un E1 | Es un **E3** |
| STM-4 = 4 × E1 | STM-4 = **4 × STM-1 = 622 Mbps** |
| La multimodo sirve para unir sedes a 8 km | **No**: la dispersión modal la limita a **cientos de metros** → **monomodo** |
| OM5 es monomodo o tiene otro núcleo | Es **multimodo de 50 µm**, igual que OM4; la diferencia es **SWDM** |
| Tier II permite mantenimiento sin cortar | Eso es **Tier III**. El II tiene componentes redundantes pero **una sola vía** |
| Tier III soporta cualquier falla | Eso es **Tier IV** (tolerante a fallas) |
| ATM usa tramas variables | **Celdas fijas de 53 bytes**. Las tramas variables son de **Frame Relay** |
| Para voz alcanza con UBR | **CBR**: UBR no garantiza nada |
| SDH es de capa 2 o 3 | Es **capa 1** (transporte físico) |
| MPLS es capa 3 | Es **capa 2,5**: la etiqueta va entre la cabecera de capa 2 y la de capa 3 |
| OSI es el modelo que usa Internet | Internet usa **TCP/IP**. OSI es el de **referencia** |
| TCP/IP tiene 5 capas | En la cátedra tiene **4**: Aplicación, Transporte, Internet, Acceso a la red |
| Con paridad se corrige el error | La paridad solo **detecta** (d = 2). Para **corregir** hace falta d = 3 → Hamming |
| Peering = tránsito | El peering es **directo y sin pago**; el tránsito es **pago** y da **toda Internet** |
| IP es confiable | IP es **best effort**; la confiabilidad la pone **TCP** |
| Un hash es un cifrado | El hash **no se descifra**: sirve para la **integridad** |
| La capa 5 es la que asegura la entrega | La entrega la asegura la **capa 4**; la 5 organiza el **diálogo** |

---

## 14. Preguntas para practicar (sin mirar)

1. 🔴 Se deben conectar dos sedes a **12 km**. ¿Qué fibra usás? Justificá y dá un ejemplo. Agregá conector y tipo de cable.
2. 🔴 Explicá el **modelo OSI** y compáralo con **TCP/IP**.
3. 🔴 Definí qué es un **data center**, sus **tipos** con ejemplos y sus **Tiers**. ¿Por qué es difícil tener un Tier IV en Argentina?
4. 🔴 ¿Cuánto es un **STM-1**? ¿Y un **STM-4**? ¿De dónde sale el **E1** de 2 Mbps?
5. 🔴 Desarrollá **ATM**: estructura, ventajas y servicios. ¿Qué servicio contratás para **voz**?
6. 🔴 Compará **OM4** y **OM5**.
7. 🟠 Explicá **Hamming** con un ejemplo: codificá **0110** y mostrá cómo se corrige un error en la posición 3.
8. 🟠 Diferenciá **TDM, FDM y WDM**. ¿TDM sincrónico vs. asincrónico?
9. 🟠 Explicá **SDH** y **POS**.
10. 🟠 Compará **VPN IP** y **tránsito IP**. ¿Y el **peering**?
11. 🟠 Tipos de **par trenzado** según el blindaje. **Cat 6 vs. 6A**.
12. 🟠 ¿Qué es la **diafonía**? **NEXT** vs. **FEXT**.
13. 🟠 Compará **X.25, Frame Relay y ATM**.
14. 🟠 ¿Qué es **QoS** y cómo se obtiene en capa 2? ¿Qué es el **jitter**? ¿Y un **SLA**?
15. 🟠 Explicá **MPLS** y sus ventajas frente al ruteo IP.
16. 🟠 Explicá **BGP** y compáralo con **OSPF**. ¿Qué es un **AS**?
17. 🟠 Clases de IP. ¿Cuántas IP utilizables hay en un **/28**?
18. 🟠 Compará **TCP y UDP**. Nombrá vulnerabilidades de UDP y cómo protegerse.
19. 🟠 Desarrollá los **4 pilares** del factor tecnológico.
20. 🟠 ¿Qué es **NFV**? Compará el modelo **purpose-built** con el **software-based**.
21. ¿Qué hacen las **capas 5, 6 y 7**? ¿Qué diferencia a la capa 5 de la capa 4?
22. Contá el **handshake de TLS**. ¿Qué garantiza?
23. ¿Qué puerto y transporte usan DNS, DHCP, HTTPS, SSH y SNMP?
24. ¿Qué imaginó **Martin Cooper**?

<details>
<summary>Resolución del 7 (Hamming de 0110)</summary>

- **Datos:** M3 = 0, M5 = 1, M6 = 1, M7 = 0.
- **Bits de redundancia:**
  - K1 = M3 ⊕ M5 ⊕ M7 = 0 ⊕ 1 ⊕ 0 = **1**;
  - K2 = M3 ⊕ M6 ⊕ M7 = 0 ⊕ 1 ⊕ 0 = **1**;
  - K4 = M5 ⊕ M6 ⊕ M7 = 1 ⊕ 1 ⊕ 0 = **0**.
- **Palabra:** `1100110`.
- **Llega `1110110`** (con la posición 3 invertida):
  - P1 = 1 ⊕ 1 ⊕ 1 ⊕ 0 = **1**;
  - P2 = 1 ⊕ 1 ⊕ 1 ⊕ 0 = **1**;
  - P4 = 0 ⊕ 1 ⊕ 1 ⊕ 0 = **0**.
- **(P4 P2 P1) = 011 = 3** → se invierte M3 y se recupera `1100110`.

</details>

<details>
<summary>Resolución del 17</summary>

Un /28 deja 4 bits para hosts → 2⁴ = 16 direcciones → **14 utilizables**.

</details>

---

<sub>⚙️ Resumen armado con las guías 01–12 (basadas en las PPTs de la cátedra Volpi / Giorgi / Llasat) y priorizado según 5 modelos de parcial y las preguntas recopiladas en `Material extra/` (revisadas contra las PPTs).</sub>
