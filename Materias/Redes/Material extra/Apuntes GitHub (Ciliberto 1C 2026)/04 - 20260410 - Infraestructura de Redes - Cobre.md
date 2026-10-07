## Clase 4 - Infraestructura de Redes - Cobre

### Cable de par trenzado

- Medio de transmisión **alámbrica**.
- Utiliza pares de cables de cobre, trenzados entre sí.
- Son para transmitir diversas señales eléctricas.
- Su diseño ayuda a reducir interferencia electromagnética (EMI) y minimizar/reducir la diafonía (*crosstalk*).
- Con esto se logra transmisión más clara y fiable de las señales.
- La capacidad de transporte (ancho de banda) es **inversamente proporcional** a la distancia.

**Ventajas**:
- Cable económico en comparación a otros medios.
- Económico de **fabricar e instalar** (materiales frecuentes como el cobre).
- **Fácil de manejar**: flexible, fácil de doblar y pasar por espacios reducidos.
- **Alta vida útil** gracias a los conductores trenzados.
- Menos posibilidad de daño superficial.
- Previene interferencias: los hilos se cruzan para reducir ruido de diafonía.

**Desventajas**:
- Limitaciones para transmisión de señales **a grandes distancias** (sin degradar la señal).
- Limitaciones a distancias elevadas **sin regeneración** (a diferencia de la fibra óptica).
- Mayor tasa de error respecto a otros cables.
- Ancho de banda disminuye con la distancia.

### Diafonía (*crosstalk*)

La diafonía en un **cable de cobre (como un `Cat 6`)** ocurre cuando la señal eléctrica de un par de hilos **interviene en otro par cercano**, generando ruido y degradando la comunicación.
- En cables de red (`Cat 6`) hay 4 pares de cobre trenzados. Cada par transporta señales.
- Cuando circula corriente por un par, se genera un campo electromagnético.

En altas frecuencias, se produce la **diafonía**:
- En el **extremo cercano** (NEXT): interferencia cerca del transmisor, afectando datos del TX y filtrándose a los pares cercanos.
- En el **extremo lejano** (FEXT): interferencia en el receptor, afectando la calidad de señal recibida en el otro extremo del cable.

**Consideraciones**:
- **Longitud**: cuanto más largo el cable, mayor probabilidad de diafonía.
- **Frecuencia**: señales de alta frecuencia son más propensas a interferencias.
- **Calidad**: menor calidad de cables (o sin blindaje) son más vulnerables.
- **Distancia**: distancias más cortas reducen probabilidad de interferencias.
- **Categorías**: a categorías más altas de cable, mejora el problema de la diafonía.

### Solución típica: Ethernet con par trenzado 

![[Ethernet con par trenzado.png]]

- Es un recinto llamado **Telco Room (TR)**.
- Aparecen Patch Panels, Patchcords, FacePlates y Jacks.
- Se maneja cableado horizontal.

¿Cómo llegamos a ella?
#### Conectividad Ethernet

- **Canal típico**: máximo 100m, pensando en un `Cat 6` (dependiendo de su categoría; en `Cat 8` se llega hasta 30m).
- Existen otras soluciones en cobre que permiten extender más allá esta distancia.

![[Ethernet conectividad.png]]

##### Usos de conectividad

- **Automation**: tendencia de unificación de redes (corporativa con industrial) permitiendo tener un **único tipo de servicio** para abastecer automatismo y granjas de servidores.
- **Automotive**: dentro de los vehículos se encuentran pequeños Datacenters (autos que se manejan solos). Hay baterías, cableados, sensores... con sistemas de cómputo.
- **Enterprise**: dejar Ethernet en puestos de trabajo, en Datacenters y servidores. 
- **Cloud Providers**: que ofrecen servicios a Pymes, etc.

![[Ethernet usos.png]]

##### Categorías de conectividad

En la imagen se muestran los **anchos de banda** (Mbps/Gbps) y las **frecuencias** (MHz) de cada categoría:

- Los `Cat 5` (*Fast Ethernet*) ya no se usan; los `5e` sí pero ya están viéndose opacados por los `6` (*Gigabit*) ya que se pueden resolver los mismos problemas con **similares costos y mayores ventajas**.
- `Cat 6` a `Cat 7`: hay una brecha significante en el ancho de banda, pero el tema de la **distancia de uso (en metros)** es vital: pierde mucha efectividad. A la hora de implementarlos en empresas, se terminan decidiendo por soluciones más baratas (`6` o `6a`). `6a` puede valer un 70% más caro que el `6`.
- Los `Cat 8` ya **pierden muchísima distancia** y no suelen valer la pena (prácticamente no se usan en el mercado, aunque existen fabricantes).

![[Ethernet categorias.png]]

- Cuanta más alta la categoría, más velocidad y ancho de banda, pero posiblemente **menos distancia máxima**.
- Cuanta más frecuencia, más interferencia.

##### Panel de distribución (Patch Panel)

![[Ethernet Copper patch panel.png]]

- **Copper Patch Panel**
- Puerto vacío para conectores
- De 24 o 48 puertos
- Puede venir plano o angulado según preferencias de cliente.

#####  FACEPLATES

- Universal opening
- Color options
- Label/no-label options
- 1/2/4/6 ports available

![[Ethernet Faceplates.png]]

##### SURFACE MOUNT

- 1/2 port versions
- Material: UL 94 V-0 compliant
- Color options
- Plenum rated boxes are available

![[Ethernet Surface mount.png]]

##### PATCH CORDS

- UTP & STP
- Transparent slim-line boot design
- Supports highest density apps
- Available in aditional jacket colors

Tipos de conectores: 
- RJ45 (más común): ficha estandar para redes Ethernet.
- `Cat 7` y `Cat 8`: a veces usan GG45 o TERA, conectores propietarios (también compatibles con RJ45)
- Angulados o planos: para espacios reducidos o *racks*.
- Las de abajo son **blindadas**.

![[Ethernet Patch cords.png]]

**Tipos de cable**:

| Tipo de cable | Blindaje                            | Es ideal para...                    |
| ------------- | ----------------------------------- | ----------------------------------- |
| UTP           | Sin blindaje                        | Ambientes sin interferencia EMI     |
| FTP           | Lámina que envuelve todos los pares | Interferencia ligera o moderada     |
| STP           | Blindaje por cada par               | Alta protección ante interferencias |
| S/FTP         | Blindaje por par + malla general    | Ambientes industriales o críticos   |

##### RACKS

Tamaños:
- **Altura**: se mide en **U** (unidades de rack)
- `1U = 1.75 pulgadas (4.45 cm)`
- **Tamaños comunes**: 6U, 12U, 24U, 42U (en data centers)
- **Profundidad**: entre 300 y 1200 mm (se usan mayores para servidores)
- **Ancho estándar**: 19 pulgadas (48.26 cm) para mayoría de equipos de red y servidores

Tipos: 
- **Rack de pared** (*wall-mount*): pequeños, fijos a la pared. Espacios reducidos.
- **Rack abierto** (*open frame*): sin puertas ni paneles laterales. Fácil acceso, buena ventilación.
- **Rack cerrado** (*closed cabinet*): con puertas y paneles laterales. Mayor seguridad, control de temperatura.

![[Ethernet Racks.png]]
### Cables de Par Trenzado típicos

- **UTP (Unshielded Twisted Pair)**: sin blindaje, más barato, más susceptible a interferencias. Es el típico a comprar.
- **STP (Shielded Twisted Pair)**: con blindaje por par, mejor protección.
- **FTP (Foiled Twisted Pair)**: blindaje general para todos los pares, aluminio.
- **S/FTP o SF/UTP**: blindaje combinado, alta protección, uso en ambientes con mucha interferencia electromagnética.

**Casos de uso**:
- Doméstico / video vigilancia: `Cat 5e`.
- Oficinas: `Cat 6` (UTP).
- Edificios inteligentes: `Cat 6` o `Cat 6a`.
- Redes empresariales / servidores: `Cat 7` o `Cat 8` con blindaje (su uso nunca terminó de instalarse en la industria).

#### S/FTP (Shielded/Foiled Twisted Pair)

- Cada par de hilos está **protegido con una lámina metálica individual** (FTP).
- Todo el conjunto se envuelve con una **malla metálica general** (S).
- Protección doble: entornos industriales, contra las EMI y diafonía entre pares.
- Ideal para oficinas con muchos cables eléctricos, Datacenters, etc.

#### SF/UTP (Shielded Foil/Unshielded Twisted Pair)

- Todo el conjunto tiene una **doble capa de blindaje externo** (lámina metálica F y malla S).
- Los pares internos **NO están blindados (UTP)**.
- Protección media-alta: bloquea interferencias externas pero no tanto entre pares.
- Uso específico para ambientes con EMIs pero que no tengan tanta interferencia entre pares individuales (es menos exigente que la anterior).
### Power Over Ethernet - POE

Permite enviar **energía eléctrica** y **datos** por un solo cable Ethernet.

- **15W (Type 1**): pequeños clientes, control de acceso biométrico, 802.11n Wireless.
- **30W (Type 2)**: lectores de tarjetas, cámaras, sistemas de alarmas, iluminación.
- **60W (Type 3)**: controles de acceso, laptops, lectoras POS, cámaras, 802.11ac Wireless. 
- **90W (Type 4)**: computadoras de escritorio, televisiones, conferencias de video, High Power Wireless.

### Distancias extendidas en cobre

- Las soluciones de transmisión de datos en cobra manejan **distancias máximas de 100 metros**, incluyendo Patchcords.
- Existen actualmente **tipos de cable que pueden usar hasta 250 metros**, evitando necesidad de utilizar soluciones de fibra óptica (FO) que son más costosas. Sin embargo, va a bajar el ancho de banda total. 
- Se puede extender hasta 250 mts, a **10 Mbps con POE de 90W**. Es ideal para conexión de cámaras remotas en campo.