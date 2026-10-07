# Preguntas estilo Parcial

## Clase 1 - Introducción a Redes

#### ¿Cómo podemos tomar la tecnología desde el presente para vaticinar los próximos 50 años tal como lo hizo Martin Cooper?

Para vaticinar los próximos 50 años siguiendo la lógica de **Martin Cooper** expuesta en la Clase 1, debemos observar las tendencias tecnológicas actuales y proyectarlas hacia una **integración total con el ser humano**, bajo la premisa de que *"el futuro hay que crearlo"*.

Nos podríamos basar en estos pilares:

- **De la portabilidad a la incrustación biológica:** Cooper vaticinó que la tecnología inalámbrica avanzaría desde los teléfonos móviles hasta **dispositivos empotrados en el cuerpo humano**. Para predecir el futuro, debemos imaginar cómo los smartphones y wearables de hoy dejarán de ser externos para volverse parte de nuestra biología.
- **Solución de problemas actuales mediante fuentes naturales:** Ante la frustración por el consumo de energía en dispositivos complejos, Cooper propone utilizar al **cuerpo humano como fuente de energía constante** para alimentar dicha tecnología. Vaticinar el futuro implica buscar soluciones disruptivas a las limitaciones presentes (como las baterías) en recursos que ya poseemos.
- **La identidad vinculada a la red:** Se proyecta un mundo donde la conexión es tan vital que el número de teléfono o la identidad digital se vuelve **parte intrínseca de la persona desde el nacimiento**, permitiendo incluso acciones mediante el pensamiento.
- **Expansión a industrias críticas:** El vaticinio no se limita a las telecomunicaciones, sino que utiliza la base tecnológica actual para revolucionar sectores como **el cuidado de la salud y la energía**, permitiendo diagnósticos y sanaciones instantáneas e inalámbricas.
- **Superar el conservadurismo humano:** Cooper advierte que el principal obstáculo para que estas predicciones se cumplan no es la tecnología en sí, sino la **resistencia al cambio de la gente**. Por lo tanto, para vaticinar el futuro debemos considerar no solo lo que es técnicamente posible, sino cómo evolucionará la aceptación social de estas innovaciones.

En resumen, el método de Cooper consiste en tomar las telecomunicaciones y la tecnología como el motor de la economía y la sociedad, proyectando su evolución a través de fases (como las eras del contenido, contexto y contactos) hasta llegar a una etapa donde la tecnología y el ser humano sean uno solo.

## Clase 2 - Transmisión de Datos

#### Dé un ejemplo práctico de código de Hamming.

Basado en lo estudiado en la **clase 2 de Transmisión de datos**, el código de Hamming se utiliza para detectar y corregir errores en la transmisión de bits. 

A continuación, se desarrolla un ejemplo práctico para proteger un código binario de **4 bits (M=4)** con la posibilidad de que se invierta un bit durante la transmisión. Para corregir un bit erróneo, se requiere que el código tenga una **distancia mínima de** d=3.
##### 1. Determinación de bits de redundancia (K)

Para proteger 4 bits de datos, se deben agregar bits de redundancia K que cumplan la ecuación $2^K≥M+K+1$. En este caso, el menor valor que cumple la ecuación es $K=3$, por lo que la palabra final tendrá 7 bits en total.

##### 2. Estructura de la palabra de Hamming

Los bits de redundancia ($K_i$​) se ubican en las posiciones que son potencias de 2 (1,2,4), mientras que los datos originales ($M_i$​) ocupan el resto de los lugares:

- **Posición 1:** $K1​$
- **Posición 2:** $K2​$
- **Posición 3:** $M3$​ (Dato A)
- **Posición 4:** $K4​$
- **Posición 5:** $M5​$ (Dato B)
- **Posición 6:** $M6​$ (Dato C)
- **Posición 7:** $M7$​ (Dato D)

##### 3. Lógica de paridad

Cada bit de datos interviene en la generación de los bits K tales que la suma de los subíndices de los K sea igual al subíndice del dato. Por ejemplo, M6​ (posición 6) afecta a K4​ y K2​ porque 4+2=6. Así se definen las paridades parciales:

- $K1$​ controla a $M3​,M5​,M7$​.
- $K2​$ controla a $M3​,M6​,M7​$.
- $K4​$ controla a $M5​,M6​,M7$​.

##### 4. Ejemplo de detección y corrección

Supongamos que se transmite la palabra `1101001` y se recibe el código `1101011`. Al recibirlo, se realizan las operaciones de paridad (usando la función lógica **XOR**) para verificar si hubo cambios:

1. $P1​=K1​⊕M3​⊕M5​⊕M7​=0.$
2. $P2​=K2​⊕M3​⊕M6​⊕M7​=1.$
3. $P4​=K4​⊕M5​⊕M6​⊕M7​=1.$

Al armar la palabra con los resultados de las paridades (P4​,P2​,P1​), obtenemos el binario 110, que equivale al número **6** en el sistema decimal. Esto indica que **el bit invertido se encuentra en la posición 6** (el dato M6​), permitiendo su corrección inmediata.

## Clase 3 - Modelo de Capas (OSI vs. TCP/IP)

#### Explicar el modelo OSI y diferenciarlo con TCP/IP.

**Las 7 Capas del Modelo OSI**

1. **Capa 1 - Física**: Se encarga de la **transmisión binaria** (ceros y unos) a través de medios físicos como cables de cobre (RJ45), fibra óptica o radiofrecuencias.
2. **Capa 2 - Enlace de Datos**: Proporciona el direccionamiento físico mediante **direcciones MAC**, gestiona el acceso al medio y realiza la detección de errores. Los switches operan en esta capa.
3. **Capa 3 - Red**: Determina la **mejor ruta** y el direccionamiento lógico a través de **direcciones IP** para el enrutamiento de paquetes entre sistemas finales.
4. **Capa 4 - Transporte**: Garantiza la **entrega extremo a extremo** de los datos y el control de flujo. Utiliza protocolos como **TCP** (confiable, con acuse de recibo) y **UDP** (no confiable, más rápido).
5. **Capa 5 - Sesión**: Establece, administra y termina las conexiones o **diálogos entre aplicaciones** de distintos hosts.
6. **Capa 6 - Presentación**: Estandariza el **formato de los datos** (como JPG, ASCII o GIF), además de encargarse de la compresión y el cifrado para que sean legibles por la capa de aplicación.
7. **Capa 7 - Aplicación**: Es la interfaz que identifica los servicios de red para las aplicaciones del usuario, como **HTTP** para navegación, **SMTP** para correos o **FTP** para archivos.

**Diferencias entre el Modelo OSI y el Modelo TCP/IP**

Aunque ambos modelos buscan organizar las funciones de red, presentan diferencias estructurales y prácticas fundamentales:

- **Naturaleza del modelo**: El modelo **OSI es teórico y experimental**, diseñado para la estandarización, mientras que el modelo **TCP/IP es el modelo práctico y real** utilizado actualmente en Internet.
- **Cantidad de capas**: OSI posee **7 capas**, mientras que TCP/IP se simplifica en **4 capas**.
- **Mapeo de capas**:
    - La capa de **Aplicación de TCP/IP** agrupa las funciones de las tres capas superiores de OSI (Aplicación, Presentación y Sesión).
    - La capa de **Acceso a la Red de TCP/IP** combina las capas de Enlace de Datos y Física de OSI, abarcando desde el cable físico hasta la dirección MAC de la tarjeta de red.
    - La capa de **Internet** en TCP/IP equivale directamente a la capa de **Red** en OSI.
    - La capa de **Transporte** mantiene la misma funcionalidad en ambos modelos.
- **Gestión**: El modelo TCP/IP simplifica la gestión operativa al abreviar los procesos que ocurren en los extremos del modelo.

#### ¿Cuál es el ancho de banda de STM-4?

El ancho de banda de una trama **STM-4** es de **622.08 Mbps**.

Este valor se obtiene de la siguiente manera:

- **Multiplexación:** Un nivel STM-4 se forma a partir de la multiplexación a nivel de byte de **cuatro estructuras STM-1** (de 155.52 Mbps cada una).
- **Equivalencia:** En la jerarquía americana (**SONET**), el ancho de banda del STM-4 (europeo) es equivalente al de un enlace **OC-12**.
- **Cálculo técnico:** Matemáticamente, responde a la fórmula: 4×8000×(270 columnas×9 filas×8 bits)=622 Mbps.

#### Multiple Choice Trama E1

**La trama E1 multiplexacion TDM:**

1. **Es una tecnología de multiplexado por division de frecuencia** 
2. **Cuenta con la capacidad de multiplexar 32 intervalos de tiempo entregando un máximo de 2MB.**
3. **Cuenta con 16 canales para multiplexar voz.**
4. **Entrega un ancho de banda máximo de 34Mbps.**

La respuesta correcta es la **b**.

La **trama E1** basada en multiplexación por división de tiempo (TDM) presenta las siguientes características:

- **Capacidad y ancho de banda**: Se forma concatenando **32 canales** de voz digitalizados a 64 Kbps cada uno, lo que arroja una trama total de 2048 Kbps, equivalente a **2 Mbps**.
- **Tecnología**: Es una técnica de **división de tiempo (TDM)**, no de frecuencia (FDM). En TDM, el canal se divide en intervalos de tiempo o "ranuras" fijas asignadas a cada señal.
- **Diferencia con otras tramas**: Una trama de **34 Mbps** corresponde a una **E3**, no a una E1. Asimismo, la jerarquía americana (SONET) utiliza tramas de 24 canales (T1) que entregan 1.5 Mbps, a diferencia de los 32 canales de la norma europea E1.
## Clase 4 - Factor Tecnológico de la Integración

#### Desarrolle los 4 hitos del Factor Tecnológico de la Integración.

En base al material de la clase 4, el **Factor Tecnológico de la Integración** se desarrolla a través de los siguientes **4 hitos o pilares fundamentales**:

- Escabilidad
- Convergencia
- Seguridad
- Disponibilidad

Cada una se detalla de la siguiente manera:

1. **Redes Escalables**: Actualmente, la prioridad principal es satisfacer las demandas de capacidad y ancho de banda que surgen del incremento masivo de dispositivos conectados. Esto requiere de infraestructuras más robustas, como centros de datos, salas de cómputo y salas de telecomunicaciones (_Telco Rooms_), que demandan soluciones de conectividad eficientes.
2. **Convergencia**: Los fabricantes líderes a nivel mundial proveen soluciones integrales que operan en las distintas capas del **Modelo OSI**. Esta convergencia permite ofrecer desde la infraestructura física hasta las aplicaciones, integrando en una misma solución elementos como _switches_, enrutamiento (_routing_) y tecnologías inalámbricas.
3. **Seguridad de la información**: Este hito se centra en soluciones que garantizan la **autentificación segura** de cada dispositivo. Además, implica la administración de políticas de seguridad mediante la encriptación de datos sensibles para asegurar conexiones totalmente protegidas.
4. **Disponibilidad de red**: Se refiere a la capacidad de las redes para mantenerse operativas con niveles de disponibilidad superiores al **99.9%**. Esto se logra mediante opciones flexibles de implementación, soporte técnico constante y una alta resiliencia de la infraestructura ante fallos.

## Clase 5 - Capa 1 - Cobre

#### ¿En qué consiste la diafonía? Explicar también sus dos tipos.

La **diafonía** (también conocida como _cross talk_) es un fenómeno de interferencia que ocurre en los cables de cobre cuando la señal eléctrica que viaja por un par de hilos interfiere en otro par cercano, generando ruido y degradando la comunicación. Este efecto se produce porque, al circular corriente por un par, se genera un **campo electromagnético** que se acopla a los pares vecinos, siendo un problema más propenso en señales de alta frecuencia.

Existen dos tipos principales de diafonía según el punto donde se produzca la interferencia:

- **Diafonía en el extremo cercano (NEXT):** Es la interferencia que se genera **cerca del transmisor**, afectando los datos de transmisión al filtrarse hacia los pares cercanos en ese mismo extremo.
- **Diafonía en el extremo lejano (FEXT):** Es la interferencia que se produce en el **extremo del receptor**, impactando la calidad de la señal que llega al otro lado del cable.

Para minimizar estos efectos, los cables de red utilizan el método constructivo de **par trenzado**, donde los hilos se entrelazan helicoidalmente para lograr un desacoplamiento electromagnético. Además, factores como una **mayor longitud del cable** o una baja calidad en el blindaje aumentan significativamente la probabilidad de sufrir este tipo de interferencias.

#### ¿Cuál es la distancia máxima de transmisión para un cable de par trenzado de cobre?

La distancia máxima de transmisión para un cable de par trenzado de cobre varía según el estándar y la tecnología utilizada, aunque existe un valor típico general:

- **Distancia Estándar (Canal Típico):** La distancia máxima normalizada por el estándar ANSI/TIA 568 para un canal de cobre es de **100 metros**. Esta distancia se compone generalmente de **90 metros de cableado horizontal** y **10 metros adicionales** para los cables de conexión (_patch cords_) en los extremos.
- **Variaciones según la Categoría:**
    - **Categoría 6:** Soporta 1 Gbps hasta los 100 metros, pero si se utiliza para **10 Gbps**, su distancia se reduce a aproximadamente **55 metros**.
    - **Categoría 6A y 7:** Están diseñadas para alcanzar los **100 metros** manteniendo velocidades de 10 Gbps.
    - **Categoría 8:** Debido a su alta frecuencia (2000 MHz), puede llegar hasta **40Gbps** pero tiene una limitación mucho mayor, permitiendo vínculos de solo **30 metros**.
- **Soluciones de Distancia Extendida:** Existen tecnologías y cables de cobre homologados que permiten superar el límite de los 100 metros, sacrificando ancho de banda a medida que aumenta la longitud:
    - Hasta **150 metros**: Puede alcanzar velocidades de hasta 1 Gbps (de tipo **6**)
    - Hasta **200 metros**: La velocidad cae a **100 Mbps** (como los de tipo **5e**)
    - Hasta **250 metros**: Es el límite máximo mencionado para estas soluciones especiales, entregando una velocidad de **10 Mbps** y soporte para PoE de hasta 90W, ideal para cámaras de seguridad (como los de tipo **5**).

En resumen, mientras que el **estándar general es de 100 metros**, es posible alcanzar hasta **250 metros** con cables especiales, aunque reduciendo significativamente la tasa de transferencia de datos. Para distancias superiores (como 500 metros), las fuentes indican que es obligatorio el uso de **fibra óptica**.

#### Nombrar y explicar los tipos de cable de par trenzado. Explicar, además, los diferentes tipos de blindaje.

La nomenclatura estándar utiliza siglas para indicar el blindaje externo (antes de la barra) y el blindaje de los pares individuales (después de la barra) bajo el formato **XX/YTP**:

- **UTP (Unshielded Twisted Pair - U/UTP):** Es el cable **sin blindaje**. Es la opción más económica y común en instalaciones residenciales y oficinas con ambientes controlados y sin grandes emisiones electromagnéticas.
- **STP (Shielded Twisted Pair):** Posee **blindaje por cada par** de hilos. Ofrece una alta protección contra la diafonía entre pares y el ruido externo, siendo ideal para entornos con interferencias pesadas.
- **FTP (Foiled Twisted Pair - F/UTP):** Cuenta con un **blindaje general** en forma de lámina metálica que envuelve a los cuatro pares, pero los pares individuales no tienen protección propia. Se recomienda para interferencias ligeras o moderadas, como en hospitales.
- **S/FTP (Shielded Foiled Twisted Pair):** Es un cable de **doble blindaje**. Cada par está envuelto en una lámina de aluminio (foil) y, adicionalmente, todo el conjunto está protegido por una malla metálica externa. Es la solución para ambientes industriales críticos (fábricas, centros de datos).
- **SF/UTP (Shielded Foiled Unshielded Twisted Pair):** El conjunto total de cables tiene una **doble capa de blindaje externo** (lámina más malla), pero los pares internos no están blindados. Bloquea bien las interferencias externas, pero no tanto la diafonía interna.

**Tipos de Blindaje**

Constructivamente, las fuentes distinguen dos elementos principales utilizados para proteger la señal:

1. **Lámina o Foil (F):** Es una fina película metálica, similar al **papel de aluminio**, que envuelve los pares para aislarlos de la interferencia que pueda generar el par contiguo o fuentes externas.
2. **Malla o Braid (S):** Es una **malla metálica entrelazada**, similar a la que se encuentra en los cables coaxiales, que ofrece una protección más robusta y mecánica.
3. **Guía separadora (Cruceta):** Aunque no es un blindaje metálico, los cables de categorías superiores (como Cat6) incluyen un separador plástico interno en forma de cruz que mantiene los pares distanciados físicamente para **reducir la diafonía**.

Es importante destacar que cuando se utilizan cables blindados, es obligatorio conectar el sistema a una **puesta a tierra** a través de conectores metálicos (_jacks_ o _plugs_ blindados) para evitar desbalances o microcortes en la transmisión. El conector más famoso y utilizado es el **RJ45**.

## Clase 6 - Capa 1 - Fibra Óptica

#### ¿Qué características tienen los cables de fibra óptica respecto a su flamabilidad?

Las características de los cables de fibra óptica respecto a su **flamabilidad** se definen por su comportamiento frente a eventos de sobrecalentamiento o incendio, centrándose en la propagación de la llama y la emisión de gases.

Los estándares (como TIA y ANSI) clasifican los cables principalmente en tres categorías según el área de instalación y su composición:

- **Cables Plenum (CMP):**
    - **Ubicación:** Se instalan en los espacios entre el cielorraso y la losa (áreas plenum), donde hay circulación de aire y, por ende, más oxígeno para alimentar un incendio.
    - **Comportamiento:** Son los más rigurosos; al contacto con el fuego **no crean llamas**, generan muy poco humo y su forro simplemente se derrite. Están construidos para que el fuego no se propague a través de la cubierta.
- **Cables Riser (CMR):**
    - **Ubicación:** Diseñados para montantes verticales (el hueco que viaja de piso a piso en un edificio).
    - **Comportamiento:** Son retardantes al fuego, pero menos exigentes que los Plenum. Pueden llegar a crear una flama, pero están diseñados para **apagarse antes de llegar al metro y medio** de distancia. Son más económicos, aunque generan más humo que los CMP.
- **Cables Libres de Halógenos (LSZH):**
    - **Toxicidad:** Se especifican para evitar que, al quemarse la cubierta, se desprendan gases halógenos (como flúor, cloro, bromo, yodo o astato) que son **tóxicos para la salud**.
    - **Uso:** Son muy utilizados actualmente en pliegos de especificaciones para centros de datos para reducir la cantidad de humo tóxico en caso de siniestro.

En resumen, la elección del cable depende de si la prioridad es evitar la propagación del fuego en áreas con mucho aire (Plenum), proteger las vías verticales del edificio (Riser) o garantizar la seguridad humana mediante la baja toxicidad del humo (Libre de halógenos).

#### Comparar cables OM4 y OM5

La fibra óptica **OM5** representa la evolución más reciente de la fibra multimodo (estandarizada en 2018), diseñada para superar las capacidades de la **OM4** en términos de escalabilidad y alcance en centros de datos modernos.

1. Similitudes Físicas

A nivel constructivo, ambas fibras comparten parámetros fundamentales:

- **Diámetro del núcleo:** Ambas poseen un núcleo de **50 micrones** (μm) y un revestimiento (_cladding_) de 125 μm.
- **Ancho de banda modal a 850nm:** Ambas entregan un ancho de banda efectivo de **4700 MHz*km** cuando operan en la longitud de onda estándar de 850nm.

2. Diferencias Tecnológicas (Capacidad de Multiplexación)

La diferencia principal radica en la ventana de operación y la tecnología de transmisión:

- **OM4 (y OM3):** Están optimizadas para operar en una **única longitud de onda** (generalmente 850nm).
- **OM5 (Wideband Multimode Fiber - WBMMF):** Está diseñada para soportar **SWDM** (_Shortwave Wavelength Division Multiplexing_). Esto le permite multiplexar **4 longitudes de onda** (850nm, 880nm, 910nm y 940nm) por un mismo hilo de fibra.
    - Esto permite reducir la cantidad de hilos físicos necesarios para alcanzar altas velocidades como **400G u 800G** en el futuro.

3. Comparativa de Alcance (Distancia)

La fibra OM5 ofrece una mayor tolerancia a distancias largas en aplicaciones de alta velocidad:

|Aplicación|Alcance OM4|Alcance OM5|
|---|---|---|
|**40G-BiDi**|150 metros|**200 metros**|
|**40G-SWDM4**|350 metros|**440 metros**|
|**100G-SWDM4**|100 metros|**150 metros**|
|**100G-BiDi**|100 metros|**150 metros**|

4. Recomendación de Uso

- **OM4:** Sigue siendo ampliamente utilizada y es suficiente para la mayoría de las aplicaciones de 10G y 40G en distancias estándar.
- **OM5:** Se especifica para proyectos nuevos de centros de datos que requieren **escalabilidad futura** y máxima eficiencia. Si la diferencia de costo entre ambas es pequeña (alrededor del 10%), el material recomienda diseñar con OM5 debido a sus funcionalidades avanzadas y mejor soporte en distancias críticas.

#### Se tiene que conectar dos sedes A y B separadas entre ellas a 8km, definir qué tipo de fibra usar y justificar.

Para conectar dos sedes separadas por una distancia de **8 km**, se debe utilizar **fibra óptica monomodo (Single-mode)**, específicamente de categoría **OS2**.

**Justificación técnica según las fuentes:**

- **Superación del límite de la fibra multimodo:** La fibra multimodo (OM3, OM4, OM5) está diseñada para distancias cortas dentro de edificios o campus. El alcance máximo mencionado para la fibra multimodo más avanzada (OM4) es de solo **550 metros**. Por lo tanto, es físicamente imposible cubrir 8 km con tecnología multimodo estándar.
- **Capacidad de larga distancia (Long Reach):** La fibra monomodo es la solución estándar para "Long Reach" (LR). Según los cuadros técnicos de conectividad, las interfaces monomodo tipo **LR** cubren rangos de **2 a 10 km**, lo que incluye perfectamente el requerimiento de 8 km. Para distancias aún mayores, existen módulos como el ZX que alcanzan los 40 km.
- **Propiedades físicas del núcleo:** A diferencia de la multimodo, que tiene un núcleo de 50 micrones donde la luz rebota en varios modos, la fibra monomodo tiene un núcleo mucho más pequeño de **9 micrones**. Esto permite que la información viaje como un **único haz de luz láser** continuo y uniforme, minimizando la degradación de la señal en trayectos largos.
- **Uso en redes WAN y transporte:** Para interconectar sitios geográficamente dispersos (redes WAN) o para el transporte entre localidades lejanas, el medio de transmisión por excelencia es la fibra monomodo debido a su alta eficiencia y fiabilidad.

En conclusión, la **fibra monomodo OS2** es la única opción viable para esta distancia, garantizando que la señal llegue con la integridad necesaria entre la sede A y la sede B.

#### ¿Qué es la fibra óptica OM5? ¿Para qué sirve?

La fibra óptica **OM5**, también conocida como **Wideband Multimode Fiber (WBMMF)**, es la evolución más reciente de la fibra multimodo, estandarizada en 2018 para superar las limitaciones de sus predecesoras.

**¿Qué es la fibra óptica OM5?**

Es un tipo de fibra multimodo diseñada específicamente para soportar la tecnología **SWDM** (_Shortwave Wavelength Division Multiplexing_ o multiplexación por división de longitud de onda de onda corta). A diferencia de las fibras OM3 y OM4, que operan en una única longitud de onda (850 nm), la OM5 está optimizada para transmitir **cuatro longitudes de onda** simultáneamente (850, 880, 910 y 940 nm) a través del mismo hilo físico.

**¿Para qué sirve?**

Su propósito principal es maximizar la eficiencia y escalabilidad en infraestructuras críticas como los centros de datos modernos:

- **Escalabilidad a altas velocidades:** Permite alcanzar velocidades de **400G y hasta 800G** en el futuro, reduciendo drásticamente la cantidad de hilos de fibra necesarios gracias a la multiplexación.
- **Mayor alcance en distancia:** Ofrece una mejor tolerancia a la distancia en aplicaciones de alta velocidad. Por ejemplo, en aplicaciones de **100G SWDM4**, la fibra OM5 alcanza los **150 metros**, mientras que la OM4 se limita a 100 metros.
- **Optimización de costos y espacio:** Al poder enviar más información por un solo hilo (4 "landas" o longitudes de onda en lugar de una), se requiere menos infraestructura física y conectividad paralela compleja.
- **Diseño de centros de datos de vanguardia:** Se especifica en proyectos nuevos donde la diferencia de costo respecto a la OM4 es pequeña (cercana al 10%), garantizando que la instalación esté preparada para futuras demandas de tráfico.

## Clase 7 - Centros de datos

#### Explicar qué define el estándar ANSI/TIA-942.

El estándar **ANSI/TIA-942** es la normativa que regula la infraestructura de telecomunicaciones para centros de datos.

A continuación, se detallan los aspectos principales que define este estándar:

1. Alcance y Propósito

- **Regulación Integral:** Define las pautas para el diseño e implementación de centros de datos, abarcando desde la infraestructura de cableado hasta aspectos de construcción y protección.
- **Calidad y Confiabilidad:** Impulsa el uso de elementos de alta calidad y marcas homologadas para garantizar la estabilidad del servicio.
- **Interoperabilidad:** Unifica criterios para que equipos de diferentes fabricantes puedan trabajar juntos de manera eficiente.

2. Áreas Funcionales y Distribución Física

El estándar establece una arquitectura de bloques para organizar el flujo de datos y la conectividad interna:

- **ER (Entrance Room):** Cuarto de entrada donde llegan los servicios del proveedor.
- **MDA (Main Distribution Area):** Área de distribución principal, el punto central del sistema de cableado.
- **IDA (Intermediate Distribution Area):** Área de distribución intermedia (opcional).
- **ZDA (Zone Distribution Area):** Punto de consolidación de zona.
- **EDA (Equipment Distribution Area):** Área donde se encuentran los servidores y sistemas de almacenamiento.

3. Requerimientos de Infraestructura y Diseño

- **Sistemas de Refrigeración:** Define que el diseño debe contar con sistemas de enfriamiento adecuados, incluyendo la implementación de **pasillos fríos y pasillos calientes** para optimizar la disipación del calor.
- **Seguridad y Sustentabilidad:** Establece normas estrictas de seguridad física y, recientemente, ha integrado criterios de sustentabilidad para un uso eficiente de la energía.
- **Escalabilidad:** El diseño debe ser lo suficientemente flexible para permitir el crecimiento futuro y la actualización de servicios sin interrumpir la operación.

4. Aplicabilidad

Este estándar es obligatorio para cualquier tipo de centro de datos que busque ser certificado, sin importar si es de tipo **Empresarial (Enterprise)** o de **Nube (Cloud)**. En la región, se utiliza el estándar americano ANSI/TIA-942, mientras que en Europa se utiliza su equivalente, el EN-50600.

#### Plasmar, en un dibujo, cómo está formado un DC. Indicar sus diferentes áreas.

La estructura de un Centro de Datos se organiza en bloques funcionales que permiten administrar el cableado y garantizar la escalabilidad y redundancia. El recorrido lógico y físico es el siguiente:

1. **Cámara Cero**: Ubicada en la vía pública, es el punto donde llegan las fibras de los distintos proveedores de servicios (Internet/Carrier).
2. **Acometida**: Es la entrada física del cableado desde la calle hacia la fachada del edificio.
3. **ER (Entrance Room - Cuarto de Entrada)**: Es el espacio donde termina el cableado del proveedor y se encuentra la **ENI (External Network Interface)**. Es la interfaz entre la red externa y la infraestructura interna del DC.
4. **MDA (Main Distribution Area - Área de Distribución Principal)**: Es el corazón del Centro de Datos. Alberga el **MD (Main Distributor)**, que es el punto central de distribución del sistema de cableado estructurado y donde suelen ubicarse los switches de **Core**.
5. **IDA (Intermediate Distribution Area - Área de Distribución Intermedia)**: Es un área opcional utilizada en centros de datos de gran porte para segmentar la distribución entre el MDA y las zonas finales.
6. **ZDA (Zone Distribution Area - Área de Distribución de Zona)**: Funciona como un punto de consolidación intermedio que facilita los cambios y movimientos de cables hacia las áreas de equipos.
7. **EDA (Equipment Distribution Area - Área de Distribución de Equipamiento)**: Es el área final donde se ubican los **racks** que contienen los servidores, sistemas de almacenamiento (_storage_) y equipamiento activo de telecomunicaciones.

Consideraciones de Diseño Críticas

- **Redundancia**: Los estándares sugieren el uso de **Caminos A y B** (rutas redundantes) para que, si falla un proveedor o un tramo de cableado, el DC siga operativo.
- **Gestión Térmica**: Los racks en el área EDA deben organizarse en **pasillos fríos y pasillos calientes** para optimizar la refrigeración y evitar que el aire caliente de un servidor alimente la entrada de otro.
- **Elementos Pasivos**: Se utilizan canalizaciones especiales como las **guías de fibra (Fiber Raceway)** amarillas para proteger la integridad de los cables ópticos y conectores de alta densidad tipo **MPO**.

#### Explicar los distintos tipos de "tier" de DC definidos por el Uptime Institute.

El **Uptime Institute** es un organismo internacional que certifica las capacidades de diseño, construcción y operación de los Centros de Datos a nivel mundial mediante una clasificación por niveles denominados **"Tiers"**.

A continuación, se explican los cuatro tipos de Tiers según el material de la clase:

**Tier I: Capacidades Básicas**

Es el nivel más sencillo y precario.

- **Mantenimiento:** Requiere paradas totales de todo el sitio para realizar mantenimientos programados o reparaciones.
- **Resiliencia:** No posee componentes redundantes. Cualquier falla en la distribución de energía afecta directamente a los servicios.

**Tier II: Componentes Redundantes**

Introduce redundancia en los componentes de capacidad, pero mantiene una infraestructura de distribución simplificada.

- **Componentes:** Requiere generadores y sistemas de energía ininterrumpida (**UPS**) redundantes (típicamente configuración **N+1**).
- **Mantenimiento:** Al igual que el Tier I, todavía requiere paradas completas del sitio para tareas de mantenimiento o reparación.
- **Vías de distribución:** Posee una **única vía de distribución** de energía no redundante, por lo que fallas en este tramo afectarán los servicios.

**Tier III: Mantenimiento Simultáneo**

Es un nivel robusto donde cada componente tiene una alternativa, permitiendo que el DC sea **concurridamente mantenible**.

- **Operación sin interrupciones:** Cualquier componente o ruta de distribución de energía puede ser removido o reemplazado de forma planificada sin afectar las operaciones.
- **Infraestructura:** Cuenta con múltiples proveedores de telecomunicaciones, dos cuartos de entrada de servicio, y rutas y áreas redundantes.
- **Energía:** Al menos redundancia N+1 y capacidad de combustible para operar el generador por al menos **72 horas**.
- **Seguridad:** Incluye muros exteriores sin ventanas, acceso controlado y seguridad perimetral con CCTV.

**Tier IV: Tolerante a Fallas**

Es el nivel de máxima calidad y disponibilidad, diseñado para resistir cualquier tipo de evento.

- **Tolerancia total:** Una falla en un equipo individual o una interrupción en cualquier ruta de distribución de energía **no afecta las operaciones**.
- **Doble suministro:** A diferencia del Tier III, requiere un **segundo proveedor de energía de red** totalmente independiente.
- **Situación en Argentina:** Alcanzar el nivel Tier IV es extremadamente complejo (o casi imposible) en el país debido a que la distribución eléctrica suele estar monopolizada por una sola empresa en cada zona (como Edenor o Edesur), lo que impide tener el doble proveedor de red eléctrica externa requerido.

#### Comparar tipos de DCs según alcance y ejemplos de c/u

Los Centros de Datos se clasifican principalmente según su **modelo de gestión**, su **tamaño (escala)** y su **proximidad al usuario (alcance)**.

A continuación, se presenta una comparación detallada de los tipos de DCs, su alcance y ejemplos representativos:

1. Centros de Datos de Nube (Cloud Computing o Hyperscale)

- **Alcance:** Son los de mayor porte a nivel mundial (**Core**). Albergan recursos de infraestructura de TI para el **uso compartido de millones de clientes** a través de conexiones masivas a Internet.
- **Escala:** Se consideran de **hiperescala** cuando su consumo de energía es de **100 MW en adelante**. Están diseñados para máxima estabilidad, almacenamiento masivo y alta densidad de servidores.
- **Ejemplos:** Google, Amazon (AWS), Microsoft (Azure), Meta, Oracle y SAP.

2. Centros de Datos Empresariales (Enterprise)

- **Alcance:** Instalaciones donde **toda la infraestructura y los datos pertenecen y se alojan en la propia empresa**. Se eligen cuando la corporación requiere un control total sobre la seguridad de su información.
- **Escala:** Típicamente son centros de datos **medianos o grandes**.
- **Ejemplos:** Bancos (tanto públicos como privados), procesadoras de pago, aseguradoras, empresas de energía o petroleras y organismos gubernamentales.

3. Centros de Datos Gestionados (Telco o Collocation)

- **Alcance:** Instalaciones desplegadas por **operadores de telecomunicaciones** que alquilan espacio físico (racks), energía y refrigeración a terceros. Es un modelo común para Pymes o empresas medianas que no desean invertir en su propio edificio de datos, pagando solo por el espacio y servicio utilizado.
- **Escala:** Suelen ser centros de datos **medianos** (entre 50 y 100 MW).
- **Ejemplos en Argentina:** Telecom (Pacheco), Telefónica, Claro (barrio Belgrano), Arsat (Benavides), e IPLAN.

4. Centros de Datos de Borde (Edge Data Center)

- **Alcance:** Es un concepto que emergió con fuerza tras la pandemia para **acercar las aplicaciones al usuario final**. Su objetivo principal es **reducir la latencia** en sectores críticos como el bancario o el retail, mejorando la experiencia del cliente al procesar datos "en el borde" de la red.
- **Escala:** Son los más pequeños (**Far Edge**), a menudo consistiendo en **un solo gabinete** o rack instalado en lugares de cercanía. Se clasifican como **pequeños** cuando su consumo es de hasta **20 MW**.
- **Ejemplos:** Sistemas de gestión en sucursales de retail o bancos que necesitan respuesta inmediata cerca del punto de venta.

Resumen Comparativo de Escalas

|Tipo de DC|Escala (Nivel)|Consumo Energético|Latencia|
|---|---|---|---|
|**Edge / Far Edge**|Pequeño|Hasta 20 MW|**Mínima** (Más cerca del usuario)|
|**Enterprise / Telco**|Mediano|50 a 100 MW|Media|
|**Cloud / Core**|Grande / Hiperescala|Más de 100 MW|Mayor (Por distancia física)|
## Clase 8 - Capa 2 

#### ¿Cuál es la diferencia entre protocolos FDM y TDM?

1. Mecanismo de División

- **TDM (Time Division Multiplexing):** El canal de transmisión se divide en **intervalos de tiempo** o "ranuras" (_slots_) muy pequeños. Cada señal tiene asignado un momento específico para transmitir su información.
- **FDM (Frequency Division Multiplexing):** El espectro de frecuencias del canal se divide en **varias bandas de frecuencia** mediante el uso de filtros.

2. Uso del Ancho de Banda y el Tiempo

- **TDM:** Cada usuario utiliza la **totalidad del ancho de banda** disponible, pero solo durante su intervalo de tiempo asignado.
- **FDM:** Los usuarios transmiten **al mismo tiempo**, pero cada uno tiene la **posesión exclusiva de su propia banda** de frecuencias (modulando su señal en una portadora distinta).

3. Características Técnicas y Ejemplos

- **TDM:** El dato está multiplexado en el tiempo. Puede ser **sincrónico** (intervalos fijos y predefinidos) o **asíncrono** (asignación dinámica según disponibilidad de datos). Fue la base de las redes digitales de los años 90 (como las tramas E1 de 2 Mbps).
- **FDM:** Para evitar interferencias entre canales, las frecuencias deben estar separadas por **bandas de guarda** (_guard bands_). El ejemplo clásico es la **radio FM**, donde distintas emisoras viajan por el aire simultáneamente pero en diferentes bandas (como 88.1 MHz o 101.5 MHz).

En resumen, mientras que **TDM reparte el tiempo** de uso del canal, **FDM reparte el espectro de frecuencias** del mismo.

#### Explicar el concepto de QoS (Quality of Service). ¿Cómo obtengo QoS en capa 2?

El concepto de **QoS (Quality of Service)** o Calidad de Servicio se refiere a la capacidad de una red para brindar un trato preferencial o **priorización al tráfico** de datos según su tipo y sensibilidad. Este mecanismo permite que el comportamiento de la red sea predecible, manteniendo bajo control parámetros críticos como la **latencia**, el **jitter** (variación en la demora de entrega de paquetes) y la **pérdida de paquetes**.

A diferencia del modelo original de las redes IP, que son de "Mejor Esfuerzo" (_Best Effort_) y no garantizan la entrega ni el tiempo de llegada, el QoS permite segmentar el tráfico para que servicios sensibles (como la voz o el video en tiempo real) no se vean afectados por tráficos más pesados pero menos críticos (como la descarga de archivos).

**¿Cómo se obtiene QoS en Capa 2?**

Existen protocolos específicos de la capa de enlace de datos diseñados para gestionar la calidad del servicio:

1. **Redes ATM (Asynchronous Transfer Mode):** Fue la tecnología pionera en manejar QoS de forma nativa en capa 2. ATM utiliza una estructura de **celdas fijas de 53 bytes** y define cuatro tipos o "sabores" de tráfico para garantizar el servicio:
    - **CBR (Constant Bit Rate):** Garantiza un ancho de banda fijo y constante (el más caro y confiable).
    - **VBR (Variable Bit Rate):** Garantiza una velocidad media o sostenible (SCR), permitiendo picos de tráfico.
    - **ABR (Available Bit Rate):** Asegura una velocidad mínima garantizada.
    - **UBR (Unspecified Bit Rate):** Es el más económico y no garantiza nada; el tráfico se asigna a la capacidad sobrante.
    - **Bit CLP (Cell Loss Priority):** En la cabecera de la celda ATM existe un bit que indica la prioridad de pérdida; si hay congestión, las celdas con este bit activo son las primeras en descartarse.
2. **Redes MPLS (Multiprotocol Label Switching):** Aunque actúa entre las capas 2 y 3, MPLS es fundamental para el transporte eficiente con QoS.
    - **Campo Exp (Experimental):** La etiqueta (label) de MPLS incluye un campo de **3 bits dedicado específicamente a QoS** para clasificar y priorizar el paquete mientras viaja por el núcleo de la red.
    - Permite manejar tráfico de alta prioridad (voz y video) heredando la capacidad de gestión que antes era exclusiva de ATM.
3. **Frame Relay:** Al igual que ATM, permite niveles de gestión de calidad acorde al servicio contratado (CBR, VBR, etc.), aunque suele utilizarse para velocidades menores.
4. **Control de Flujo:** En términos generales, la capa 2 obtiene QoS mediante mecanismos de **control de flujo** que gestionan la velocidad de envío y la **entrega ordenada de tramas** para evitar saturaciones en el enlace físico.

#### Explicar las diferencias entre TDM (Time Division Multiplexing) y Frame Relay.

Las diferencias entre **TDM** (Time Division Multiplexing) y **Frame Relay** se centran en su arquitectura, el tipo de tráfico que manejan y la forma en que gestionan los datos:

1. Mecanismo de funcionamiento

- **TDM:** Es un método de multiplexación que divide el canal de comunicación en **intervalos de tiempo (slots) fijos y predefinidos**. Cada señal tiene asignado su propio momento para transmitir, utilizando la totalidad del ancho de banda solo durante ese instante.
- **Frame Relay:** Es una red basada en la **conmutación de paquetes**. A diferencia de TDM, que reserva tiempo, Frame Relay transmite **tramas de longitud variable** a través de la red WAN.

2. Tipo de tráfico y eficiencia

- **TDM:** Está diseñado para la transmisión de **datos de tipo continuo**, siendo la base de la telefonía digital tradicional (como las líneas E1 de 2 Mbps). Es muy fiable pero menos flexible, ya que si un canal no tiene datos para enviar, su espacio de tiempo suele desperdiciarse en la versión sincrónica.
- **Frame Relay:** Está optimizado para **tráfico de ráfagas** (_burst traffic_), donde los datos no son constantes sino que ocurren en picos. Fue el reemplazo evolutivo del protocolo X.25 para conectar sucursales bancarias y cajeros automáticos, ofreciendo una gestión de tráfico mucho más eficiente.

3. Gestión de la Calidad de Servicio (QoS)

- **TDM:** Ofrece una capacidad dedicada y determinística por su naturaleza de división de tiempo, lo que garantizaba gran fiabilidad como red de acceso.
- **Frame Relay:** Permite manejar **distintas clases de servicio** (como CBR o VBR) según lo contratado con el proveedor. Esto permite priorizar tráficos críticos sobre los que no lo son, aunque generalmente se utilizaba para velocidades menores (64 o 128 Kbps) en comparación con otras tecnologías como ATM.

4. Relación jerárquica

- **Frame Relay se montaba sobre el mismo tipo de red digital soporte** que los nodos TDM. Mientras que TDM actuaba como la tecnología de base para el transporte, Frame Relay era un servicio de nivel superior que ofrecía mayor capilaridad y capacidad de gerenciamiento lógico mediante **circuitos virtuales permanentes (PVC)**.

#### Si tengo que transmitir servicios de voz de una sucursal a otra sobre ATM, ¿qué tipo de servicio debería contratar?

Si necesitas transmitir servicios de voz entre sucursales utilizando una red **ATM (Asynchronous Transfer Mode)**, el servicio que deberías contratar es **CBR (Constant Bit Rate)**.

Los motivos técnicos para esta elección son los siguientes:

- **Garantía de Ancho de Banda:** El servicio **CBR** es el de mayor categoría y costo, ya que garantiza de forma permanente el ancho de banda total contratado directamente para el usuario (por ejemplo, un vínculo de 2 Mbps "limpios").
- **Soporte para Tiempo Real:** La voz es un tráfico de tipo **Real Time** y extremadamente sensible al **jitter** (variación en la demora de entrega de paquetes). CBR proporciona una tasa de bits constante que permite reconstruir el canal de voz en el receptor de manera fluida, evitando interrupciones.
- **Priorización de Tráfico:** En una red ATM, las celdas que transportan voz bajo un perfil CBR tienen **priorización sobre otros tipos de tráfico** (como los datos en servicios UBR o ABR), asegurando que los paquetes de voz no se desordenen ni se pierdan en caso de congestión en la red del proveedor.
- **Capa de Adaptación:** Para este tipo de servicios constantes y de tiempo real, ATM utiliza la capa de adaptación **AAL1**, diseñada específicamente para servicios CBR.

No tendría sentido intentar pasar servicios de voz sobre un servicio de tipo **UBR (Unspecified Bit Rate)**, ya que este es el más económico y no ofrece ninguna garantía de entrega o velocidad, relegando ese tráfico al final de la cola.

#### Explicar el concepto de Jitter.

- **Definición:** El Jitter es la **variación o demora en el tiempo de entrega** de los paquetes de datos a través de una red. Técnicamente, representa la diferencia de tiempo entre el momento en que se transmite una señal y el momento en que se recibe en el destino.
- **Unidad de medida:** Se calcula y expresa habitualmente en **milisegundos (ms)**.
- **Impacto en la comunicación:** Esta variación supone una interrupción en la secuencia ordinaria de envío de paquetes. Si el Jitter es muy alto, el receptor pierde la capacidad de concatenar correctamente los fragmentos de datos, lo que genera retrasos o interrupciones perceptibles.
- **Sensibilidad según el tráfico:** Es un parámetro crítico para los servicios en **tiempo real (Real Time)**, como la **Telefonía IP (VoIP)** y el **Video IP**. En el caso de la voz, un Jitter excesivo impide reconstruir el audio de forma fluida para el oyente.
- **Relación con QoS:** El manejo del Jitter es fundamental en las redes que miden la **Calidad de Servicio (QoS)**. Protocolos como **ATM**, que fueron pioneros en QoS, fueron diseñados específicamente para mitigar este efecto y permitir que el tráfico de voz y video viaje de forma estable.

En resumen, mientras que el ancho de banda define "cuántos" datos pasan, el **Jitter mide la "constancia" o regularidad** con la que llegan esos datos, siendo vital para que las aplicaciones interactivas funcionen correctamente.

#### ¿Qué es un SLA (Service Level Agreement)?

Un **SLA (Service Level Agreement)**, o Acuerdo de Nivel de Servicio, es un compromiso formal entre un proveedor de servicios y un cliente que define los **niveles de servicio esperados** y garantiza parámetros específicos de rendimiento y disponibilidad.

De acuerdo con las fuentes, sus principales características son:

- **Parámetros técnicos garantizados:** En contratos de servicios críticos, como el tránsito IP, el SLA asegura niveles específicos de:
    - **Disponibilidad** (Availability): Por ejemplo, soluciones con disponibilidades mayores al **99.9%**.
    - **Latencia**: El tiempo de retardo en la red.
    - **Jitter**: La variación en la demora de entrega de los paquetes.
    - **Pérdida de paquetes**: El porcentaje de datos que no llegan a su destino.
- **Relación con QoS (Calidad de Servicio):** Un SLA alto solo es posible cuando la infraestructura física y los protocolos pueden soportar mecanismos de **Quality of Service**, permitiendo que el tráfico sensible (como voz o video) funcione correctamente sin interrupciones.
- **Enfoque en el negocio:** En modelos modernos como **Network as a Service (NaaS)**, el SLA es parte fundamental del "Modelado de un Servicio", donde se definen las condiciones de funcionamiento y las dependencias antes de ejecutar la automatización de la red.

En resumen, el SLA es la herramienta contractual que permite a las empresas asegurar que la red que contratan cumple con las exigencias técnicas necesarias para su operación diaria.

#### ¿Cómo funciona MPLS en la capa 2 bajo ATM y qué ventajas presenta ante hacerlo por routeo en IP para la capa 3?

¿Cómo funciona MPLS en Capa 2 bajo ATM?

MPLS (_Multiprotocol Label Switching_) actúa como una tecnología de transporte que permite realizar ruteo sobre una red que, por naturaleza, es de Capa 2. Su funcionamiento se basa en el **etiquetado de datos**:

1. **Ingreso a la red (Etiquetado):** Cuando un paquete de datos llega al borde de la red, un router denominado **LER (Label Edge Router)** le asigna una etiqueta (_tag_).
2. **Tránsito en el núcleo (Conmutación):** Una vez dentro de la red ATM, los nodos internos llamados **LSR (Label Switch Router)** no examinan la dirección IP de destino. En su lugar, simplemente leen la etiqueta y reenvían el tráfico basándose en ella hacia el siguiente nodo.
3. **Jerarquía IP sobre Capa 2:** En la jerga técnica, se dice que montar MPLS sobre ATM le otorga a esta red una **"jerarquía IP"**, ya que permite agregar la inteligencia del ruteo y la determinación del mejor camino a un protocolo de enlace de datos.
4. **Salida de la red:** Al llegar al borde opuesto, la etiqueta se retira y el paquete continúa su ruteo tradicional por IP.

**Ventajas de MPLS frente al ruteo IP (Capa 3)**

El ruteo por etiquetas de MPLS presenta beneficios significativos sobre el ruteo convencional basado únicamente en direcciones IP:

- **Mayor Velocidad y Eficiencia:** Es mucho más ágil y óptimo conmutar por etiquetas que procesar cada paquete examinando la dirección IP completa en cada salto. Al no tener que mirar las tablas de ruteo IP en el núcleo de la red, el transporte es más rápido.
- **Gestión de QoS (Calidad de Servicio):** MPLS hereda y potencia la capacidad nativa de ATM para manejar QoS. Esto permite priorizar tráficos sensibles (como voz y video) de manera mucho más efectiva que las redes IP originales, que operaban bajo el concepto de "Mejor Esfuerzo" (_Best Effort_).
- **Escalabilidad:** A diferencia de los vínculos fijos como los PVC de ATM o las líneas dedicadas (_leased lines_), MPLS permite escalar los servicios fácilmente según las necesidades del cliente.
- **Soporte Multiprotocolo:** Una ventaja clave es que MPLS es versátil y puede montarse sobre distintas tecnologías como **IPv4, IPv6, ATM o Frame Relay**, permitiendo que un proveedor use una misma infraestructura física para múltiples tipos de servicios.
- **Creación de VPNs:** Facilita la creación de **Redes Privadas Virtuales (VPNs)** de forma eficiente, permitiendo que las empresas conecten sus sucursales de forma segura sobre una infraestructura pública compartida.

En resumen, mientras que el ruteo IP tradicional busca el destino paquete por paquete en cada router, **MPLS establece un camino predefinido (LSP)** mediante etiquetas, combinando la velocidad de la Capa 2 con la inteligencia de ruteo de la Capa 3.
#### Desarrollar tecnología de transmisión ATM, ventajas, y para qué tipo de servicios es aplicable.

La tecnología **ATM (Asynchronous Transfer Mode)** se define y desarrolla de la siguiente manera:

Desarrollo de la Tecnología ATM

- **Estructura de Celdas Fijas:** A diferencia de otros protocolos que usan tramas de longitud variable, ATM utiliza **celdas pequeñas y fijas de 53 bytes**. Estas se componen de **48 bytes de carga útil** (_payload_) y **5 bytes de encabezado** (_overhead_).
- **Identificación de Circuitos:** El encabezado de la celda incluye identificadores lógicos conocidos como **VPI** (Identificador de Ruta Virtual) y **VCI** (Identificador de Canal Virtual), que permiten direccionar el tráfico dentro de la red.
- **Orientada a Conexión:** Es una tecnología que establece vínculos lógicos permanentes, denominados **PVC** (_Permanent Virtual Circuits_), sobre una infraestructura física.
- **Capas de Adaptación (AAL):** Para transportar diferentes tipos de tráfico (como IP sobre ATM), requiere subcapas de adaptación, siendo la **AAL5** la más común para datos y la **AAL1** para servicios constantes de tiempo real.

Ventajas de ATM

- **Pionera en QoS (Quality of Service):** Fue la primera red de capa 2 diseñada para segmentar y **priorizar el tráfico**, permitiendo que la voz, el video y los datos convivan en una misma red sin interferirse.
- **Velocidad y Dedicación:** A diferencia de tecnologías como Frame Relay, ATM manejaba velocidades superiores (típicamente **2 Mbps "limpios"** o dedicados).
- **Bajo Jitter y Retardo:** Al tener celdas de tamaño constante, el receptor puede concatenar la información de forma predecible, lo cual es vital para reconstruir señales de voz en el destino.
- **Capilaridad:** Debido a la gran inversión realizada por las operadoras en los años 90 y 2000, ATM posee una amplia presencia geográfica, lo que permitió usarla como **red de acceso** para llegar a lugares donde el ruteo IP masivo aún no llegaba.

Tipos de Servicios Aplicables

ATM define diferentes "sabores" o categorías de servicio según la necesidad del cliente:

1. **CBR (Constant Bit Rate):** Ideal para servicios de **voz y video en tiempo real**. Garantiza un ancho de banda fijo y constante, siendo el servicio de mayor categoría.
2. **VBR (Variable Bit Rate):** Aplicable a **datos transaccionales** o aplicaciones que no son en tiempo real pero que pueden tener ráfagas de tráfico.
3. **ABR (Available Bit Rate):** Garantiza una **velocidad mínima** y utiliza la capacidad disponible de la red.
4. **UBR (Unspecified Bit Rate):** Es el servicio más económico; no ofrece garantías y se le asigna la **capacidad sobrante** de la red. No es apto para voz o video crítico.

En resumen, ATM es una tecnología **multiservicio** que actualmente se utiliza concatenada con troncales IP, funcionando principalmente como un robusto protocolo de acceso en la capa 2.

#### Desarrollar acerca del estándar Ethernet 802.3 detallando la capa en la que trabaja y sus velocidades de transmisión.

1. Definición del Estándar

**Ethernet 802.3** es un estándar de red de área local (**LAN**) desarrollado por el **IEEE** (Institute of Electrical and Electronics Engineers). Es el tipo de red más utilizado en la actualidad en entornos empresariales, hogares y centros de datos. Su función principal es permitir que los dispositivos se comuniquen entre sí mediante el uso de **direcciones MAC** y la estructuración de los datos en **tramas**.

2. Capa en la que trabaja

Este estándar opera fundamentalmente en la **Capa 2 (Capa de Enlace de Datos)** del modelo OSI.

- **Funciones de Capa 2:** En este nivel, el protocolo se encarga del direccionamiento físico (MAC), la detección de errores (mediante mecanismos como el CRC), el control de flujo y la entrega ordenada de las tramas de un nodo a otro.
- **Relación con la Capa 1:** Aunque el protocolo es de Capa 2, define especificaciones que se ejecutan sobre la **Capa Física (Capa 1)**, utilizando medios de transmisión como cables de cobre (par trenzado) o fibra óptica.

3. Velocidades de Transmisión y Estándares

El estándar ha evolucionado para soportar diversas velocidades y medios físicos. Los ejemplos más comunes citados en las fuentes son:

|Estándar|Velocidad de Transmisión|Nombre Comercial|Medio Típico|
|---|---|---|---|
|**10BASE-T**|**10 Mbps**|Ethernet|Cobre (Par trenzado)|
|**100BASE-TX**|**100 Mbps**|Fast Ethernet|Cobre (Par trenzado)|
|**1000BASE-T/SX**|**1 Gbps**|Gigabit Ethernet|Cobre / Fibra Óptica|
|**10GBASE-T**|**10 Gbps**|10 Gigabit Ethernet|Cobre / Fibra Óptica|
**Evolución hacia Altas Velocidades:** Las fuentes mencionan un "roadmap" de crecimiento donde las interfaces han escalado a velocidades de **40 Gbps, 100 Gbps, 400 Gbps** e incluso **800 Gbps** en centros de datos modernos y redes de operadores. Se proyecta que para el futuro cercano se alcancen velocidades de **1.6 Tbps y hasta 6.4 Tbps**.

4. Características Adicionales

- **Red Multiservicio:** Ethernet es capaz de transportar tráfico en tiempo real, como **Telefonía IP y Video IP**, aunque este tráfico es sensible al **Jitter** (variación en la demora de entrega de paquetes).
- **Power Over Ethernet (PoE):** Bajo variaciones del estándar (como 802.3af/at/bt), Ethernet permite enviar energía eléctrica y datos por un solo cable, alimentando dispositivos como cámaras IP o puntos de acceso.
- **Distancias:** En conexiones de cobre, el estándar típicamente garantiza un alcance de **100 metros**, aunque existen soluciones homologadas que permiten extender esta distancia hasta los 250 metros sacrificando ancho de banda.

#### Explicar el uso de redes Metro, tipos de servicio, acceso y VPNs.

Las **redes Metro** (o MAN - _Metropolitan Area Network_) se definen como arquitecturas de **Capa 2** diseñadas para la interconexión de redes LAN extendidas en un ámbito geográfico metropolitano. Su uso principal radica en la **interconexión de sucursales** de empresas u organismos (como bancos) dentro de áreas urbanas densas, permitiendo que una casa central se vincule con múltiples puntos de forma eficiente. Estas redes son de carácter **multiservicio**, lo que significa que pueden transportar simultáneamente tráfico de voz, datos y video, siendo capaces de manejar aplicaciones en tiempo real sensibles al _jitter_ (variación de latencia).

Tipos de Servicio y Calidad

En cuanto a los tipos de servicio, las redes Metro ofrecen diferentes niveles de rendimiento según las necesidades del cliente:

- **Garantía de Servicio (SLA):** Permiten establecer acuerdos de nivel de servicio para asegurar una **alta disponibilidad** y manejo de tráfico prioritario.
- **Calidad de Servicio (QoS):** A diferencia de Internet público, las redes Metro y las líneas dedicadas permiten priorizar el tráfico para garantizar que aplicaciones críticas no sufran degradación.
- **Categorías Heredadas:** Aunque se migra hacia IP, persisten conceptos de servicios como **CBR** (tasa de bits constante) o **VBR** (tasa de bits variable) provenientes de tecnologías como ATM y Frame Relay.
- **Comparativa de Coste/Rendimiento:** Mientras que una línea dedicada (_Leased Line_) ofrece la menor latencia al mayor costo, la **Metro Ethernet** se posiciona como una solución de costo medio con baja latencia y alta escalabilidad.

Redes de Acceso

La jerarquía de red se divide fundamentalmente entre la **red de acceso** (la "última milla" que llega al usuario) y la **red de transporte** (el núcleo del operador). Los métodos de acceso varían según el perfil del usuario:

- **Residencial:** Utiliza mayormente tecnologías como **ADSL** (sobre par de cobre), **Cablemódem** (sobre cable coaxial/HFC) y, cada vez más, **FTTH** (fibra hasta el hogar).
- **Corporativo:** Las empresas suelen optar por accesos dedicados a través de **redes Metro (LAN to LAN)**, SDH o enlaces directos de fibra óptica para evitar compartir el medio con otros usuarios.
- **Inalámbrico (RAM):** En lugares donde no llega el cable físico, se utilizan radioenlaces o la red de acceso por radio (RAN) para depositar el tráfico en el _backbone_ IP del operador.
- **Redes Metro como acceso:** Es común que una conexión Metro Ethernet sirva como el puente de acceso hacia una red de transporte más robusta como **MPLS**.

Redes Privadas Virtuales (VPN)

El concepto de **VPN (Virtual Private Network)** consiste en utilizar la infraestructura de una red pública (como Internet o el transporte de un proveedor) para montar una **red privada propia**.

- **VPN MPLS:** Es el estándar comercial para empresas, donde el proveedor utiliza su red física para crear "nubes" virtuales aisladas para cada cliente.
- **Layer 2 VPN (L2VPN):** Funciona como una extensión de la LAN del cliente de forma transparente (como un cable largo entre sitios), a menudo configurado mediante un **pseudo wire**.
- **Layer 3 VPN (L3VPN):** Utiliza enrutamiento IP y se basa en el uso de **VRF (Virtual Routing and Forwarding)** para que cada cliente tenga su propia tabla de rutas independiente dentro de los routers del proveedor.
- **Seguridad:** Las VPN permiten crear **túneles encriptados** para que usuarios móviles o sucursales remotas accedan a recursos internos de forma segura a través de redes inseguras.
## Clase 9 - Capa 3

#### Dar un ejemplo de ruteo en una red con BGP.

El ruteo mediante el protocolo **BGP (Border Gateway Protocol)** es la base del funcionamiento global de Internet, ya que permite la comunicación entre diferentes **Sistemas Autónomos (AS)**.

A continuación, se presenta un ejemplo de cómo funciona este ruteo en una red:

Ejemplo de ruteo BGP: Interconexión de Telecentro (AS 27747)

Imaginemos un escenario de **Tránsito IP** o **Peering IP** donde una red local necesita enviar datos a un destino internacional:

1. **Identificación por AS:** Cada entidad (como un proveedor de internet o una gran empresa) tiene un "documento de identidad" único llamado **ASN (Autonomous System Number)**. Por ejemplo, **Telecentro** es el **AS 27747**.
2. **Anuncio de Rutas (Advertising):** El router de borde de Telecentro utiliza BGP para "anunciar" su bloque de direcciones IP públicas a sus pares (_peers_).
3. **Establecimiento de Sesiones:** Telecentro establece sesiones BGP con otros sistemas autónomos de mayor jerarquía o socios, como **Telecom Italia Sparkle (AS 6762)** o **Telefónica de Argentina (AS 22927)**.
4. **Intercambio Dinámico:** A través de estas sesiones, los routers intercambian información sobre qué redes conocen. Si Telecentro quiere enviar un paquete a un servidor de Google, su router consulta la tabla de ruteo actualizada por BGP y decide cuál es el mejor camino (por ejemplo, a través del **AS 6762**).
5. **Actualización Automática:** Si una de las rutas internacionales se cae (por ejemplo, un corte de fibra submarina en Las Toninas), BGP detecta la falla y **actualiza automáticamente las tablas de ruteo** para redirigir el tráfico por un camino alternativo disponible, garantizando la escalabilidad y resiliencia de la red.

En resumen, el ruteo con BGP no se configura "a mano" en cada salto, sino que los routers de diferentes empresas "conversan" entre sí para informarse mutuamente sobre cómo llegar a cada rincón del mundo.

#### Explicar cómo se diferencia IP de los protocolos de capa 2, y dónde se separan la capa 2 y 3 entre sí.

La diferencia entre el protocolo IP y los de capa 2, así como su punto de separación, se explica a través de su alcance, direccionamiento y funciones dentro del modelo de capas.

Diferencias entre IP (Capa 3) y Protocolos de Capa 2

1. **Naturaleza del Direccionamiento:**
    - **Capa 2 (Enlace de Datos):** Utiliza un **direccionamiento físico**, conocido como **dirección MAC**. Estas direcciones son grabadas por el fabricante en la tarjeta de red y son únicas para el hardware.
    - **Capa 3 (Red - IP):** Utiliza un **direccionamiento lógico (dirección IP)**. A diferencia de la MAC, la dirección IP se configura por software y permite identificar un dispositivo de manera única en una red global o Internet.
2. **Alcance y Propósito:**
    - **Capa 2:** Se encarga del enlace de datos entre dispositivos dentro de una **misma red local (LAN)** o entre dos nodos conectados directamente. Su función es asegurar que los datos viajen por el enlace físico sin errores.
    - **Capa 3:** Proporciona conectividad y **selección de ruta (ruteo)** entre sistemas finales que pueden estar en **redes diferentes y geográficamente separadas**. Mientras que la capa 2 entrega "tramas" de nodo a nodo, la capa 3 entrega "paquetes" de extremo a extremo.
3. **Garantía y Conexión:**
    - **Capa 2:** Protocolos como ATM o X.25 pueden estar orientados a conexión y poseer mecanismos de recuperación de errores y control de flujo.
    - **Capa 3 (IP):** Es un protocolo **no orientado a conexión** y **no confiable**. IP no garantiza que el paquete llegue ni asegura el orden o la integridad; estas funciones se delegan a la capa superior (TCP) si es necesario.

¿Dónde se separan la Capa 2 y la Capa 3?

La separación entre estas capas se manifiesta en el proceso de **encapsulamiento** y en el equipamiento utilizado:

- **El punto de interfaz (Encapsulamiento):** La capa 3 recibe los datos de la capa de transporte y los empaqueta en un **datagrama IP**. El límite se encuentra cuando ese datagrama IP se entrega a la capa 2 para ser introducido dentro de una **trama** (como Ethernet, ATM o POS) para su transmisión física. Cada encabezado de capa 2 "envuelve" al paquete de capa 3.
- **Separación de funciones en hardware:** Tradicionalmente, la separación física ocurría entre el **Switch** (Capa 2), que decide basándose en direcciones MAC, y el **Router** (Capa 3), que toma decisiones basándose en tablas de ruteo y direcciones IP.
- **Identificación en la red:** Un dispositivo "entra" en la capa 3 en el momento en que se le asigna una dirección lógica en su interfaz. Sin una dirección IP configurada, el equipo opera puramente en los niveles de acceso al medio y direccionamiento físico de la capa 2.

En resumen, la capa 2 es el transporte dentro del barrio (la LAN), mientras que la capa 3 (IP) es el sistema de correo que permite que los paquetes viajen entre diferentes ciudades y países ruteando la información.

#### Definir las clases de direccionamiento IP y en qué rango está comprendida cada una. Dado un bloque de direccionamiento definido como /29, cuántas direcciones serían reutilizables?

Clases de Direccionamiento IP y Rangos

El protocolo IPv4 se ha estructurado tradicionalmente en las siguientes clases (según los estándares citados en las fuentes como el RFC 791):

- **Clase A:** Diseñada para redes muy grandes. El primer octeto identifica la red.
    - **Rango:** 0.0.0.0 a 127.255.255.255.
- **Clase B:** Para redes de tamaño mediano. Los dos primeros octetos identifican la red.
    - **Rango:** 128.0.0.0 a 191.255.255.255.
- **Clase C:** Para redes pequeñas (LAN típicas). Los tres primeros octetos identifican la red y solo el último octeto queda para hosts.
    - **Rango:** 192.0.0.0 a 223.255.255.255.
- **Clase D:** Reservada para grupos de **Multicast**.
    - **Rango:** 224.0.0.0 a 239.255.255.255.
- **Clase E:** Reservada para uso experimental o futuro.
    - **Rango:** 240.0.0.0 a 255.255.255.255.

Cálculo para un bloque /29

Para determinar cuántas direcciones son reutilizables (útiles para asignar a dispositivos) en un bloque con máscara **/29**, se aplica la lógica de reserva de direcciones explicada en la clase:

1. **Total de direcciones:** Un bloque /29 deja 3 bits para los hosts (32−29=3). El número total de combinaciones posibles es $2^3=8$ direcciones totales.
2. **Direcciones reservadas:** Según las fuentes, en cualquier red se deben reservar obligatoriamente dos direcciones que no pueden ser asignadas a equipos:
    - La **primera dirección** del bloque, que se reserva para identificar a la **red** misma.
    - La **última dirección** del bloque, que se reserva para el **broadcast** (difusión a todos los equipos).
3. **Direcciones reutilizables:** Restando las dos direcciones reservadas al total:
    - 8(totales)−2(reservadas)=6 direcciones reutilizables.

En resumen, un bloque **/29** entrega **6 direcciones IP** que efectivamente pueden ser configuradas en interfaces de routers, servidores o terminales.

## Clase 10 - Capa 4 (TCP/UDP)

#### Desarrollar las distintas vulnerabilidades de UDP, y comparar cómo funciona vs. TCP.

El protocolo **UDP (User Datagram Protocol)** se define como un protocolo de la capa de transporte diseñado para el envío simple y eficiente de datos sin establecer una conexión previa ni realizar controles complejos de errores.

A continuación, se desarrollan sus vulnerabilidades y la comparación con TCP:

**Vulnerabilidades de UDP**

Debido a su naturaleza sin conexión y falta de verificación, UDP presenta varias debilidades que son aprovechadas para ataques de denegación de servicio (DoS):

- **Ataques de Inundación UDP (UDP Flood):** Consisten en el envío masivo de datagramas para saturar el ancho de banda y agotar los recursos de procesamiento (CPU y memoria) de routers, switches o servidores, provocando la caída de la red.
- **Desbordamiento de Búfer (Buffer Overflow):** El procesamiento de datagramas maliciosos puede generar errores de memoria en el dispositivo receptor. En ciertas implementaciones, esto permite al atacante la **ejecución remota de código** para tomar control de funciones del equipo.
- **Ataques de Reflexión:** El atacante falsifica la dirección IP de origen (pone la IP de la víctima) y envía datagramas a servidores legítimos. Estos servidores responden a la IP de la víctima, amplificando el tráfico recibido por esta y provocando una denegación de servicio por "camuflaje".
- **Ataques de Bucle (Loop DoS):** Se crean bucles de tráfico entre dispositivos mal configurados, haciendo que los paquetes reboten infinitamente entre ellos hasta agotar los recursos de la red.

**Comparación: UDP vs. TCP**

La principal diferencia radica en que **TCP es orientado a la conexión y confiable**, mientras que **UDP prioriza la velocidad y la baja latencia**.

|Factor|TCP|UDP|
|---|---|---|
|**Tipo de Conexión**|Requiere un **handshake** (establecimiento de conexión) antes de transmitir.|No necesita conexión previa; envía los datos directamente.|
|**Secuencia de datos**|Puede **secuenciar y reordenar** los paquetes en el destino para que lleguen íntegros.|No puede secuenciar; los datos llegan en el orden en que la red los entrega.|
|**Retransmisión**|Si un paquete no llega, lo **vuelve a enviar** automáticamente.|No existe retransmisión. Los datos perdidos simplemente se descartan.|
|**Garantía de entrega**|**Garantizada**. Acusa recibo (ACK) de la información.|**No garantizada**. Es un modelo de "mejor esfuerzo" (_best-effort_).|
|**Velocidad y Latencia**|**Más lento** debido a la sobrecarga de la cabecera y la gestión de la conexión.|**Más rápido y dinámico** (baja latencia), ideal para aplicaciones en tiempo real.|
|**Casos de Uso**|Navegación Web (HTTP), transferencia de archivos (FTP), correo (SMTP).|Voz sobre IP (VoIP), juegos en línea, streaming y DNS.|

En resumen, mientras que TCP asegura que la "entrega sea correcta y completa" (como un sistema de correo certificado), UDP se asegura de que el transporte sea lo más rápido posible sin importar si se pierden fragmentos en el camino.

#### Desarrollar las medidas de protección esenciales de vulnerabilidades UDP.

Las medidas de protección para mitigar las vulnerabilidades del protocolo UDP se centran en el filtrado, la restricción de tráfico y el monitoreo constante.

A continuación se desarrollan las protecciones esenciales detalladas en las fuentes:

1. **Firewalls e IPS (Sistemas de Prevención de Intrusiones)**

Constituyen la primera línea de defensa perimetral para bloquear tráfico no deseado o sospechoso.

- **Cierre de puertos:** Es fundamental configurar políticas que limiten el acceso únicamente a los **puertos UDP que se utilizan** para servicios legítimos. Dejar puertos abiertos innecesarios se compara con dejar ventanas abiertas en una casa que pueden ser aprovechadas para ataques maliciosos.
- **Políticas de seguridad:** La configuración de estos equipos debe basarse en políticas estrictas definidas por los especialistas en seguridad de la organización.

2. **Rate Limiting (Limitación de Tasa)**

Esta técnica consiste en aplicar una **restricción a la cantidad de paquetes** recibidos de un emisor específico en un intervalo de tiempo determinado.

- **Propósito:** Su objetivo principal es reducir la posibilidad de saturar el procesamiento y la memoria de los routers y switches ante ataques de inundación (UDP Flood).
- **Control de frecuencia:** Permite "cortarle la frecuencia" a un emisor que está liquidando a paquetes la red, evitando que los recursos del equipo lleguen al máximo y causen una caída del servicio.

3. **Validación de Datos**

Los servidores deben implementar mecanismos de validación rigurosa para cada datagrama entrante.

- **Verificación de formato:** Se debe asegurar que los datos cumplan estrictamente con el formato esperado por la aplicación y que no contengan **código malicioso** que pueda provocar errores de memoria o ejecuciones remotas.

4. **Monitoreo y Detección en Tiempo Real**

El uso de herramientas de monitoreo de red permite identificar patrones de tráfico anómalos antes de que el servicio colapse.

- **Protocolos de monitoreo:** Se utilizan herramientas como **SNMP** (Simple Network Management Protocol) y **Netflow** para observar el comportamiento de la red de forma constante.
- **Sistemas Anti-DDoS:** Existen plataformas especializadas de proveedores como **Arbor o Radware** que detectan automáticamente los ataques en curso, permitiendo a los administradores (o de forma automatizada) "apagar" una interfaz atacada en tiempo real para proteger el resto de la red.
- **Centros de Operación:** Habitualmente este monitoreo se realiza en un **SOC** (_Security Operation Center_), donde operadores vigilan los parámetros de tráfico y disparan protocolos de acción ante irregularidades.

5. **Evolución hacia Protocolos más Seguros**

Las fuentes mencionan que la seguridad de UDP también está evolucionando mediante el desarrollo de nuevos protocolos que añaden capas de protección sobre la base de UDP:

- **QUIC (Google):** Combina UDP con cifrado **TLS 1.3** para reducir latencia y evitar ataques simples.
- **DTLS (Datagram Transport Layer Security):** Es una versión de TLS adaptada específicamente para UDP, proporcionando una capa extra de seguridad para aplicaciones de baja latencia como la comunicación en tiempo real.

## Clase 11 - NFV

#### ¿Qué es NFV (Network Functions Virtualization)?

La **NFV (Network Functions Virtualization)** o Virtualización de Funciones de Red es una tecnología que permite que las funciones de red (como firewalls, routers o balanceadores de carga), que antes requerían hardware propietario y específico de un fabricante, ahora se ejecuten mediante **software en servidores estándar**.

A continuación se detallan los puntos fundamentales para entender qué es y cómo funciona:

1. El cambio de paradigma

NFV propone pasar de un modelo basado en dispositivos físicos dedicados (donde para cada función se necesitaba un equipo _ad-hoc_) a un modelo basado en **software montado sobre hardware estándar** (conocido como COTS - _Commercial Off-The-Shelf_). Esto genera un **desacople entre el hardware y las funciones de red**, permitiendo que la inteligencia de la red resida en el software.

2. Funciones de Red Virtualizadas (VNFs)

En este entorno, las aplicaciones de red se denominan **VNFs (Virtual Network Functions)**. Estas son esencialmente máquinas virtuales o contenedores que emulan el comportamiento del hardware propietario. Algunos ejemplos comunes de VNFs son:

- **Virtual Router (vRouter):** Realiza funciones de ruteo sin necesidad de un equipo físico dedicado.
- **Virtual EPC (vEPC):** Virtualiza el núcleo de paquetes de las redes móviles (4G/LTE), permitiendo que las telcos operen su "core" de forma virtualizada.
- **vBNG (Broadband Network Gateway):** Una aplicación de software para gestionar accesos de ancho de banda, asignación de IPs y políticas de QoS.
- **vFirewalls y balanceadores de carga:** Servicios de seguridad y tráfico ejecutados como máquinas virtuales.

3. Orquestación y Control (MANO)

Debido a que una red de gran porte puede tener miles de VNFs conviviendo en distintos entornos (nube, borde, etc.), la gestión manual es inviable. NFV introduce un marco de gestión llamado **MANO (Management and Orchestration)**, que incluye al **NFVO (NFV Orchestrator)**, encargado de administrar el ciclo de vida de los servicios de red y los recursos de hardware de forma dinámica y automática.

4. Ventajas Principales

- **Flexibilidad y Escalabilidad (Scaling):** Permite levantar o dar de baja servicios prácticamente desde un escritorio, adaptándose dinámicamente a la demanda sin necesidad de cuadrillas físicas para cambiar equipos.
- **Reducción de Costos:** Al utilizar hardware genérico y económico en lugar de equipos de red especializados (que pueden costar cientos de miles de dólares), se reducen drásticamente los gastos de capital (CAPEX) y operativos (OPEX).
- **Agilidad en el Despliegue:** Facilita la implementación de servicios complejos como las redes **5G e IoT**, que serían inviables de gestionar bajo el modelo de hardware antiguo.
- **Alta Disponibilidad:** Permite correr varias VNFs en simultáneo para tener redundancia y mejorar los niveles de servicio (**SLA**).

En resumen, NFV transforma el **Datacenter en el nuevo "Core" u oficina central** del operador de red, permitiendo que la infraestructura de telecomunicaciones sea tan ágil y flexible como una plataforma de computación en la nube.

#### Utilice un canal de voz y comprima 6 a 1.

Si se utiliza un canal de voz estándar y se aplica una **compresión de 6 a 1**, el resultado es el siguiente:

- **Capacidad por canal:** Se pueden colocar **seis canales de voz** dentro de una única trama de **64 Kbps**.
- **Fundamento técnico:** Originalmente, un canal de voz digitalizado sin compresión consume **64 Kbps** (resultado de una frecuencia de muestreo de 8 kHz codificada con 8 bits).
- **Aplicación práctica:** Esta técnica de compresión es utilizada por las compañías (especialmente las celulares) para maximizar el uso del ancho de banda disponible y sostener el servicio con múltiples señales simultáneas sobre un mismo recurso físico.

Como ejemplo adicional mencionado en las fuentes, si se tuviera que transportar un total de **60 canales de voz** con esta misma compresión, se requerirían **10 tramas de 64 Kbps**.

1. El fundamento del canal de 64 Kbps

Para entender por qué se pueden agrupar canales, primero se debe comprender por qué la unidad básica es de 64 Kbps:

- **Frecuencia de muestreo:** La voz humana tiene un ancho de banda de 0 a 4 kHz. Según el teorema de muestreo, para digitalizarla se requiere el doble de su frecuencia máxima, es decir, **8 kHz** (8000 muestras por segundo).
- **Codificación:** Cada una de esas muestras se codifica con **8 bits**.
- **Resultado:** 8000 muestras/seg × 8 bits/muestra = **64.000 bps (64 Kbps)**. Esta es la trama de voz estándar "limpia" o sin compresión.

2. ¿Por qué funciona la compresión 6 a 1?

La compresión permite reducir la tasa de bits que requiere cada señal individual sin perder la legibilidad de la comunicación.

- **Maximización de recursos:** En lugar de dedicar un slot físico completo de 64 Kbps a un solo usuario, las compañías (especialmente las celulares) comprimen la señal para que ocupe menos espacio.
- **Cálculo lógico:** Al aplicar una relación de 6 a 1, cada canal de voz pasa a requerir aproximadamente **10.66 Kbps**. Por lo tanto, dentro del "espacio" físico de una trama estándar de 64 Kbps, ahora es posible transportar **seis conversaciones simultáneas** en lugar de una sola.

3. Aplicación en escalas mayores (Ejemplo de los 60 canales)

El ejemplo mencionado sobre los 60 canales de voz demuestra la escalabilidad de esta lógica dentro de las jerarquías de transmisión:

- Si 1 trama de 64 Kbps transporta 6 canales (gracias a la compresión 6:1), para transportar **60 canales** solo se necesitan **10 tramas de 64 Kbps** (60÷6=10).
- Esto es sumamente eficiente si se compara con el modelo tradicional: sin compresión, para 60 canales de voz se necesitarían **60 tramas de 64 Kbps**, lo cual equivaldría casi a un vínculo **E1 completo** (que posee 30/32 canales de 64 Kbps).

En conclusión, esto funciona porque la tecnología permite **subdividir el ancho de banda físico** de 64 Kbps en sub-canales lógicos más pequeños mediante software y procesamiento digital, optimizando el uso de la infraestructura existente.

#### Explicar los conceptos de NFV y sus avances en Edge Computing.

La tecnología **NFV (Virtualización de Funciones de Red)** representa un cambio de paradigma en las telecomunicaciones, permitiendo que funciones de red que antes requerían hardware propietario y específico (como routers, firewalls o balanceadores de carga) ahora se ejecuten como **software sobre servidores estándar** conocidos como COTS (_Commercial Off-The-Shelf_). El concepto central es el **desacople entre el hardware y el software**, lo que permite a los operadores e industrias ganar flexibilidad, reducir costos de infraestructura y escalar servicios de forma casi instantánea mediante orquestación.

Dentro de la arquitectura NFV, los componentes clave son las **VNF (Virtual Network Functions)**, que son las aplicaciones de red virtualizadas, y el marco de gestión **MANO** (_Management and Orchestration_). Este último incluye el orquestador (**NFVO**) para la gestión de servicios de red, el gestor de funciones (**VNFM**) para el ciclo de vida de las aplicaciones y el gestor de infraestructura (**VIM**) para administrar recursos como Kubernetes o OpenStack.

**Avances en Edge Computing mediante NFV**

El **Edge Computing** (computación en el borde) es una respuesta a la necesidad de procesar datos lo más cerca posible de su origen para garantizar **baja latencia y respuestas en tiempo real**. NFV es el habilitador tecnológico que permite llevar esta inteligencia al extremo de la red:

- **Despliegue de funciones en el borde:** Gracias a la virtualización, funciones críticas como firewalls (**vFirewall**) o ruteadores (**vRouter**) pueden desplegarse en pequeños nodos de cómputo o "Data Centers de Borde" situados cerca de sensores de **IoT** o cámaras de seguridad industrial.
- **Optimización para 5G y IoT:** Las redes 5G requieren una infraestructura extremadamente ágil que las redes tradicionales no pueden proveer; NFV permite crear **rebanadas de red (slicing)** específicas para aplicaciones de misión crítica en el borde, optimizando el ancho de banda al no tener que enviar todo el tráfico al núcleo centralizado.
- **Avances en la Radio de Acceso (v-RAN):** Un avance significativo es la virtualización de la red de acceso inalámbrico, donde la unidad de banda base se desagrega en funciones de software como la **vBBU y vDU**, que se ejecutan en servidores genéricos en el borde para minimizar la latencia de la interfaz aérea.
- **Fog Computing:** NFV también facilita la implementación de la **computación en la niebla (Fog)**, una capa intermedia entre el borde puro y la nube central que utiliza gateways locales para agregar y analizar información de múltiples sensores antes de su transmisión final, mejorando la resiliencia del sistema.