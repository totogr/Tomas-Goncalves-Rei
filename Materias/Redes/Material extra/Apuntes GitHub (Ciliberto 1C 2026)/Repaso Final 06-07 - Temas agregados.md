# Preguntas tipo Final para temas agregados

## Clase 12 - MPLS

#### Explicar MPLS y los componentes del proceso de transporte de datos por etiquetado.

**¿Qué es MPLS?**

**MPLS** (_Multiprotocol Label Switching_) es una tecnología de transporte de datos diseñada para combinar la **velocidad** del reenvío basado en etiquetas (propio de la Capa 2) con la **capacidad de decisión** avanzada del enrutamiento IP (Capa 3).

Funciona de forma **híbrida entre las capas 2 y 3 del modelo OSI**. Su principal diferencia con el ruteo tradicional es que los routers no necesitan examinar la cabecera IP completa en cada salto para decidir el destino; en su lugar, introducen una **etiqueta corta** que identifica una ruta preestablecida, haciendo el proceso mucho más ágil y eficiente.

**Componentes del Proceso de Etiquetado**

Para que el transporte funcione, intervienen elementos lógicos y dispositivos físicos específicos:

**Elementos Lógicos**

- **Label (Etiqueta):** Es un número corto que determina el camino exacto que seguirá el paquete a través de la red.
- **FEC (Forwarding Equivalence Class):** Es un grupo de paquetes que se tratan de manera idéntica. Todos los paquetes que pertenecen a una misma FEC recibirán la misma etiqueta al ingresar a la red.
- **Cabecera MPLS (Shim Header):** Tiene un tamaño fijo de **4 bytes (32 bits)** e incluye el campo de etiqueta (20 bits), bits de QoS (3 bits), un bit de pila (_stack_) y el tiempo de vida (TTL).

**Componentes de Red (Routers)**

- **CE (Customer Edge):** El router situado en las instalaciones del cliente que envía el tráfico original al proveedor.
- **LER (Label Edge Router) / PE (Provider Edge):** Es el router de borde del proveedor. Tiene dos funciones críticas:
    - **Label Push:** Al recibir un paquete IP, le asigna una etiqueta y lo "empuja" dentro de la red MPLS.
    - **Label Pop:** Al salir el paquete de la red, le quita la etiqueta para entregarlo como un paquete IP original al cliente.
- **LSR (Label Switch Router) / P (Provider):** Son los routers del núcleo de la red. No miran la dirección IP; solo leen la etiqueta entrante, consultan su tabla de reenvío y realizan un **Label Swap** (intercambian la etiqueta vieja por una nueva para el siguiente salto).

**Resumen del Flujo de Transporte**

1. El paquete sale del **CE** del cliente.
2. El **PE (LER)** de entrada asigna la etiqueta según la **FEC** correspondiente (**Push**).
3. Los **LSR** internos conmutan el paquete basándose solo en etiquetas (**Swap**).
4. Antes del último salto, se realiza el **PHP** (_Penultimate Hop Popping_) donde el penúltimo router quita la etiqueta para aliviar la carga del router de salida.
5. El **PE (LER)** de salida entrega el paquete final al **CE** de destino.

## Clase 13 - RAN

#### ¿Cómo se estructurarían los protocolos de RAN según el Modelo OSI?

La arquitectura de los protocolos de una RAN se estructura concentrándose mayoritariamente en las **tres capas inferiores** del modelo OSI.

1. Capa Física (Capa 1)

- **Función:** Se encarga de la transmisión y recepción de flujos de bits brutos a través del espectro radioeléctrico.
- **Implementación:** Materializa la **interfaz aérea** (_wireless_) entre el equipo del usuario (UE) y la red.
- **Procesos técnicos:** Aquí se ejecutan la codificación de canal y la modulación de la señal utilizando esquemas como **QPSK** o 16-QAM.

2. Capa de Enlace de Datos (Capa 2)

Esta capa asegura una transmisión confiable sobre el canal físico y se divide en tres subniveles críticos:

- **MAC (Medium Access Control):** Gestiona la asignación de recursos de radio, organiza el acceso al medio compartido y aplica políticas de **Calidad de Servicio (QoS)**.
- **RLC (Radio Link Control):** Su tarea principal es la segmentación y reensamblado de las unidades de datos, además del manejo de errores mediante retransmisiones automáticas.
- **PDCP (Packet Data Convergence Protocol):** Se ubica en el nivel superior de esta capa y realiza la compresión de cabeceras, el **cifrado** para la confidencialidad y la verificación de integridad (seguridad).

3. Capa de Red (Capa 3)

- **Protocolo principal:** **RRC (Radio Resource Control)**.
- **Función:** Es responsable de manejar la **señalización** entre el dispositivo y la red.
- **Responsabilidades:** Coordina el establecimiento, mantenimiento y liberación de conexiones, además de la gestión de la red y el control del tráfico.

#### Explicar conceptos de RAN y v-RAN en el modelo PSK.

En el contexto de las comunicaciones inalámbricas, la relación entre la modulación **PSK** y las arquitecturas **RAN / v-RAN** es fundamental, ya que define cómo se transforma la información digital en señales que pueden viajar por el aire.

A continuación, se explica el funcionamiento de estos conceptos integrados:

1. La Modulación PSK en la RAN

La **RAN (Radio Access Network)** es la red de acceso inalámbrico que conecta los dispositivos finales con el núcleo del operador. Dentro de esta red, la modulación **PSK (Phase Shift Keying)** cumple un rol crítico en la **Capa Física (Capa 1)**.

- **Mecanismo Técnico:** La PSK es una modulación angular que consiste en variar la **fase de la onda portadora** entre un número determinado de valores discretos. A diferencia de la modulación analógica, en PSK la señal moduladora es **digital**, lo que significa que tiene un número de estados limitado.
- **Representación de datos:** La información se codifica mediante cambios de fase. Por ejemplo, en el esquema de la materia, a una entrada binaria "00" le corresponde un desfase de -135º en la señal.
- **QPSK (Quadrature PSK):** Es la variante más mencionada en clase, la cual utiliza **cuatro estados** (00, 01, 10, 11). Su función principal, además de transmitir datos, es optimizar la **detección y corrección de errores** en la interfaz aérea.

2. Ejecución en la RAN Convencional

En una arquitectura tradicional (D-RAN), estas funciones de procesamiento digital y modulación (como PSK) son ejecutadas por la **BBU (Baseband Unit)**.

- La BBU procesa los bits, aplica la codificación y modulación, y envía la señal a la **RRU (Remote Radio Unit)**, que se encarga de convertir esos datos digitales al dominio analógico para emitirlos por la antena.
- En este modelo, la capacidad de procesar PSK está limitada por el **hardware propietario** (equipos cerrados de fabricantes como Nokia o Ericsson).

3. El modelo v-RAN (RAN Virtualizada)

La **v-RAN** representa la evolución donde las funciones de la red de acceso se separan del hardware físico específico.

- **Desacople de software y hardware:** Las funciones de procesamiento de banda base (donde reside la lógica de la modulación PSK/QPSK) dejan de estar en una "caja" cerrada y pasan a ejecutarse como **instancias de software** sobre servidores estándar denominados **COTS** (_Commercial Off-The-Shelf_).
- **Arquitectura Desagregada:** La BBU se divide en bloques virtuales:
    - **vBBU (Virtual Baseband Unit):** Es el núcleo que realiza la **modulación (incluyendo PSK)**, codificación y sincronización en servidores genéricos.
    - **vDU (Virtual Distributed Unit):** Maneja la capa física y parte de la capa MAC cerca de la antena para minimizar la latencia.
    - **vCU (Virtual Centralized Unit):** Gestiona protocolos de capas superiores (RLC, PDCP, RRC) y seguridad en centros de datos.

Resumen de la integración

Mientras que **PSK** es la técnica matemática y física para enviar ceros y unos a través de cambios de fase en el aire, la **v-RAN** es la arquitectura moderna que permite que ese proceso de modulación sea gestionado de forma **flexible, escalable y por software**. Esto es esencial para el **5G**, ya que permite crear "rebanadas" de red (slicing) y optimizar recursos dinámicamente según la demanda.

## Clase 14 - WiFi

#### ¿Cuál es el mejor metodo/diseño para una red wifi que va a tener una concurrencia de $n$ cantidad de usuarios?

Para diseñar una red Wi-Fi profesional con una alta concurrencia de usuarios, el mejor método según el estándar **TIA TSB-162A** y lo explicado en clase no es basarse solo en la cobertura (señal), sino priorizar la **capacidad de la red**,.

1. Metodología de Celdas (TIA TSB-162A)

El diseño profesional abandona la idea de "una antena para todo el lugar" y divide el espacio en **celdas de diseño**.

- **Tamaño de celda:** Se deben proyectar celdas de **18,3 m²** (equivalente a 60 pies cuadrados).
- **Ubicación del Access Point (AP):** El equipo debe colocarse lo más céntrico posible en la celda.
- **Radio de cobertura:** El radio máximo de cobertura desde la boca de red hasta el AP debe ser de **13 metros**.

2. Factor de Ocupación (Cálculo de WAPs)

La cantidad de puntos de acceso inalámbricos (WAP) necesarios se determina según el **factor de ocupación de personas**. El estándar establece la siguiente relación:

| Ocupación (n personas) | Número de WAP necesarios |
| ---------------------- | ------------------------ |
| 1 a 25                 | 1                        |
| 26 a 50                | 2                        |
| 51 a 75                | 3                        |
| 76 a 100               | 4                        |
| 101 a 125              | 5                        |
| 126 a 200              | 9                        |
| 201 a 300              | 14                       |
| 301 a 400              | 18                       |
| **Hasta 500**          | **21**                   |

3. Infraestructura de Cableado

Para que la red inalámbrica no se vea limitada por el cableado físico, se recomienda,,:

- Llevar **dos cables Categoría 6A** (capaces de manejar hasta 10 Gbps cada uno) a cada punto de acceso, de longitud máxima de **80 metros**.
- Esto garantiza el ancho de banda necesario para las nuevas generaciones de Wi-Fi y ofrece **redundancia física y lógica**.

4. Tecnologías y Herramientas Críticas

- **Hardware:** Utilizar equipos que soporten **Wi-Fi 6, 6E o 7**, ya que incluyen tecnologías como **MU-MIMO** (para manejar múltiples dispositivos a la vez) y **OFDMA** (que reduce la latencia en entornos densos).
- **Beamforming:** Es vital que los AP manejen esta técnica para focalizar la señal hacia los dispositivos activos, mejorando la relación señal-ruido (**SNR**) sin generar interferencias innecesarias en otras direcciones.
- **Planificación:** El diseño debe validarse mediante el uso de **mapas de calor** (_heatmaps_) y relevamientos de sitio (_site surveys_) tanto predictivos como físicos para evitar el solapamiento de canales y zonas muertas.

#### ¿Cuántos cables *Cat 6a* se deben llevar a cada AP?

Según las recomendaciones de diseño y las buenas prácticas establecidas en el estándar **TIA TSB-162A**, se deben llevar **2 (dos) cables Categoría 6A** a cada punto de acceso inalámbrico (AP o WAP).

- **Redundancia y Resiliencia:** El estándar sugiere que estos dos cables sigan **caminos distintos** desde el cuarto de telecomunicaciones hasta el punto de acceso. De esta forma, si ocurre un evento físico que afecte a uno de los cables, el AP puede seguir operando a través del otro.
- **Capacidad de Transmisión:** Cada cable Categoría 6A tiene una capacidad máxima de transmisión de **10 Gbps**. Contar con dos enlaces permite no solo la redundancia física, sino también una redundancia lógica para soportar el alto tráfico de las nuevas generaciones de Wi-Fi.
- **Especificaciones del Enlace:**
    - La distancia del cableado horizontal (desde el panel de parcheo hasta la boca de red o TO) no debe superar los **80 metros**.
    - Desde la boca de red (TO) hasta el AP, el cable de conexión debe tener un radio máximo de **13 metros**.
    - El diseño se basa en celdas de aproximadamente **18,3 m²** (60 pies cuadrados), colocando el AP en la posición más céntrica posible dentro de cada celda.

#### Explicar brevemente la evolución de WiFi y comparar WiFi 4, 5 y 6.

**Resumen de la Evolución**

- **Wi-Fi 1, 2 y 3:** Arranca a finales de 1998 con velocidades modestas de **11 Mbps** en la banda de 2.4 GHz (802.11b) y evoluciona hasta los **54 Mbps** incorporando la banda de 5 GHz.
- **Wi-Fi 4 y 5:** Wi-Fi 4 (2009) marcó un hito al ser **dual band** y elevar la velocidad a 450 Mbps. Wi-Fi 5 (2014) llevó esto a los gigabits (**7 Gbps**) operando exclusivamente en 5 GHz para alta velocidad.
- **Wi-Fi 6, 6E y 7:** Lanzado hacia 2019, Wi-Fi 6 se enfoca en la eficiencia en entornos densos. Wi-Fi 7 es la última evolución, alcanzando hasta **45 Gbps** con tecnologías avanzadas de modulación.

**Comparativa: Wi-Fi 4 vs. Wi-Fi 5 vs. Wi-Fi 6**

| Característica                 | Wi-Fi 4 (802.11n)                           | Wi-Fi 5 (802.11ac)                          | Wi-Fi 6 (802.11ax)                                            |
| ------------------------------ | ------------------------------------------- | ------------------------------------------- | ------------------------------------------------------------- |
| **Año de Lanzamiento**         | 2009                                        | 2014                                        | 2019                                                          |
| **Bandas de Frecuencia**       | 2.4 GHz y 5 GHz (**Dual Band**)             | 5 GHz (principalmente para alta velocidad)  | 2.4 GHz y 5 GHz                                               |
| **Velocidad Máxima**           | Hasta **450 Mbps**                          | Hasta **7 Gbps**                            | Hasta **9.6 Gbps**                                            |
| **Velocidad por antena (4x4)** | **600 Mbps**                                | **1560 Mbps**                               | **2400 Mbps**                                                 |
| **Tecnologías Clave**          | MIMO básico                                 | **MU-MIMO** y **Beamforming** estandarizado | **MU-MIMO**, **OFDMA** y mejor gestión de dispositivos densos |
| **Enfoque Principal**          | Aumento de velocidad 10x respecto a Wi-Fi 3 | Alta capacidad para streaming y video       | Eficiencia, baja latencia y alta densidad de usuarios (IoT)   |

**Diferencias técnicas destacadas:**

1. **Canales y modulación:** Wi-Fi 6 utiliza **OFDMA**, que permite dividir un canal en subcanales más pequeños para atender a múltiples dispositivos simultáneamente, reduciendo la latencia, algo que Wi-Fi 4 y 5 no hacían de forma tan eficiente.
2. **Configuración de antenas:** Mientras que en Wi-Fi 4 una configuración 1x1 entregaba solo 75 Mbps, en Wi-Fi 6 esa misma antena entrega **600 Mbps**.
3. **Entorno de uso:** Wi-Fi 4 y 5 se diseñaron pensando en la cobertura y la velocidad pura. Wi-Fi 6 responde a la tendencia **BYOD** (_Bring Your Own Device_), donde cada usuario tiene múltiples dispositivos (móvil, tablet, laptop) conectados a la misma antena.