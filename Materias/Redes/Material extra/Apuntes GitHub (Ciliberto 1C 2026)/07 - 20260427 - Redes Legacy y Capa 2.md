## Redes Legacy y Protocolos Capa 2

### Redes X25 - Legacy

- Protocolo de red de **conmutación de paquetes**.
- Se verifica que el paquete llega al otro extremo, usando *acknowledge* para asegurar que ha llegado.
- Se orientaba al uso de redes de telefonía existentes para la transmisión de datos.
- La conmutación de paquetes se realizaba a través de circuitos virtuales.
- Usaba 3 niveles (como modelo OSI): Capa física, enlace y red.
	- **Tamaño de paquete**: Variable (128 / 1024 bytes).
	- Poseía **corrección de errores**.
	- Protocolo basado en **retransmisiones**: gran confiabilidad sobre redes físicas poco confiables.
- **Velocidad típica**: 64kbps, heredada del canal de voz sin comprensión
- Se canalizaba a través de las tramas E1.
#### Niveles

- **Físico (Layer 1)**: Conexión física con la red (como RS-232, V.35).
- **Enlace (Layer 2)**: Utiliza LAPB (Link Access Procedure Balanced) para el control de errores y flujo.
- **Red (Layer 3)**: Entrega de paquetes y mantenimiento de circuitos virtuales.
#### Tipos de circuitos lógicos

Estos servicios luego se heredan en las redes ATM.

- **PVC (Permanent Virtual Circuit)**: Conexión fija, línea dedicada sobre una red conmutada. Comercializados en Argentina. Íntimamente ligado a ATM.
- **SVC (Switched Virtual Circuit)**: Se establecía temporalmente el circuito conmutado para dar servicio. Los operadores no ofrecían el servicio, en general se comercializaban los PVC. No se llegan a implementar masivamente en Argentina.

X25 generaba **mucha sobrecarga de red**, y no era óptima para transmisión de datos Real Time.
- Uso en cajeros, puntos de venta, etc.
#### TDM

- TDM: *Time Division Multiple Access*
- Trama de transmisión de datos: adoptamos la jerarquía Europea (en Argentina). Esas son las tramas **E**.
- Método de **multiplexación por división de tiempo**. A diferencia de FDM, que transmite la señal en bandas de frecuencia.
- Formaba parte de las Redes Digitales Soporte de los Carriers en la década del 90 hasta mediados del 2000.
- Siguió usándose como red de acceso ante la evolución de las redes IP por su gran capilaridad y expansión geográfica.
- **Características**:
	- De tipo multiplexado. 
	- Tiempo fijo en cada ciclo (para cada canal). 
	- Usado en telefonía digital (T1, E1), y transmisión de datos continuos.
- Gran fiabilidad como redes de acceso a futuras redes IP/MPLS.
### Redes FR - Frame Relay

- Red basada en **conmutación de paquetes**.
- Transmisión de tramas en redes WAN, sobre un soporte físico más confiable que X25. 
- No está orientado a la retransmisión.
- Más eficiente que X25, con tramas de longitud variable.
- Usado como reemplazo de X25 para **tráfico de ráfagas** (cajeros automáticos).
- Ya no son tan comerciales, pueden existir en ciertas geografías (ancho de banda pequeño).
### Redes ATM – Asynchronous Transfer Mode

- Se siguen usando concatenadas con IP, ambas permiten QoS (priorizar el tipo de tráfico).
- ATM es el protocolo de acceso de capa 2.
- Red pensada para la **voz** (basada en ese negocio).
- Celdas fijas de 53 bytes (48 payload + 5 overhead).

![[Capa 2 cuadro comparativo.png]]

El tráfico ATM **garantiza QoS** de distintas formas: 

- **CBR - Velocidad de bits constante**: se especifica una velocidad máxima, garantizando 100% lo contratado. 
- **VBR - Velocidad binaria variable**: se especifica una velocidad binaria media o sostenible (**SCR**, lo que sí se garantiza, que puede alcanzar un determinado nivel. Se puede usar tipos dentro del medio: una parte sería el valor medio, y por encima de eso se podría acceder a más en base al tráfico actual (aunque esto último no se garantiza).
- **ABR - Velocidad binaria disponible**: se especifica una velocidad mínima garantizada.
- **UBR - Velocidad binaria no especificada**: el tráfico se asigna a toda la capacidad de transmisión restante. Es el servicio ATM más económico. No garantizan la velocidad contratada.

![[Capa 2 atm.png]]

### Redes de Capa 2

- Redes de **enlace de datos**. Se basan en la capa 2 del Modelo OSI.
- Se encarga del enlace de datos **entre dispositivos en una misma red local (LAN)**.
- Establece **conexión confiable entre dos nodos** conectados directamente
- Controla **cómo se transmiten** los datos y asegurando que **lleguen sin errores**.

#### Funciones principales

- **Direccionamiento físico** (**MAC**, *Media Access Control*): para identificar dispositivos en la red.
- Cada dispositivo tiene una dirección MAC única.
- **Detección de errores**: *CRC* (Cyclic Redundancy Check) para verificar la integridad de los datos.
- Control de flujo, **gestión de velocidad** de envío de datos.
- Control de **acceso al medio** (MAC): quién puede usar el canal de comunicación en un momento dado.
- Protocolos como CSMA/CD (Ethernet) o CSMA/CA (Wi-Fi)

#### Protocolos comunes

- **Ethernet** (IEEE 802.3)
- **Wi-Fi** (IEEE 802.11)
- **PPP** (Point-to-Point Protocol)
- **HDLC** (High-Level Data Link Control): proporciona conexión confiable punto a punto entre dos nodos. Se usa principalmente en enlaces seriales sincrónicos (enlaces WAN). Usa pares de cobre alámbricos. Escala hasta 8MB.
- **STP** (Spanning Tree Protocol): evita bucles en redes con switches. 
	- Cuando se conectan switches con múltiples enlaces redundantes, pueden producirse bucles de red. 
	- Se repiten tramas de broadcast indefinidamente (el rendimiento de red cae a cero,  *tormentas de broadcast*). 
	- Detecta caminos redundantes y bloquea selectivamente algunos puertos, creando un **árbol lógico sin bucles** (llamado "*spanning tree*").

> [!NOTE]
> *Ejemplo: se tienen 3 computadoras conectadas a un switch Ethernet. Cuando una PC envía datos a otra, el switch revisa la dirección MAC de destino y los envía solo a ese puerto. Esto evita colisiones y mejora el rendimiento comparado con los hubs (capa 1).*
#### Ethernet 802.3

- Estándar de **red local** (LAN) desarrollado por IEEE.
- Trabaja en la capa 2 (enlace de datos) del modelo OSI.
- Tipo más usado en empresas, hogares y centros de datos.
- Los dispositivos se comunican entre sí **usando direcciones MAC y tramas**, para enviar datos a través de cables físicos.
- Usar las tecnologías físicas para mayor manejo de ancho de banda.
##### Trama Ethernet

Paquete que se envía por la red. Incluye: 

- Dirección MAC **destino** (6 bytes)
- Dirección MAC **origen** (6 bytes)
- **Tipo** o **longitud** (2 bytes)
- **Datos** (payload)
- FCS/CRC (verificación de errores – 4 bytes)
- **Tamaño total**: mínimo 64 bytes, máximo 1518 bytes.
- Se usa con **cables UTP** (como Cat 5e, Cat 6, 6A) **o fibra óptica** en distancias largas.

![[Capa 2 ethernet 802.3.png]]

#### Redes Metro Ethernet

- Permite la interconexión de **redes LAN extendidas** (Red Ethernet Metropolitana - ***MAN***).
- Multiservicio, tráfico en tiempo real para Telefonía IP y Video IP (aún siendo sensible al Jitter).
- **Jitter**: variación o demora en la entrega de paquetes de datos. Demora entre el **momento en que se transmite y se recibe una señal**.
- El retraso es una interrupción en la secuencia ordinaria de envío de paquetes de datos. Se mide en **milisegundos (ms)**. 
- Pueden utilizar **líneas de cobre** como medio de acceso y la **fibra óptica** para transporte de larga distancia.
- Cobre: alta disponibilidad (no se rompen las líneas, y si se rompe parcialmente, sigue transmitiendo). Reduce el ancho de banda de forma proporcional.
- Ambas se complementan de forma ideal en el **ámbito metropolitano**, ofreciendo cobertura total.

![[Capa 2 redes metro.png]]

#### Redes Metro como acceso a Redes MPLS

- El acceso a la red MPLS se realiza **mediante un enlace Metro Ethernet** dedicado.

> [!NOTE]
> *Ejemplo: Una empresa se conecta al proveedor de servicios mediante una conexión Metro Ethernet. Esa conexión Ethernet **forma parte de la infraestructura** del carrier que soporta MPLS.*

- Tráfico de cliente: etiquetado **dentro de la red MPLS** del carrier (datos entre sucursales, Data Centers, etc).
- Las empresas suelen usar **VPNs MPLS** para separar su tráfico del de otros clientes.

![[Capa 2 redes metro MPLS.png]]

#### Redes MPLS

- MPLS: *Multiprotocol Label Switching*
- Dirige paquetes usando **etiquetas (labels)** en lugar de direcciones IP completas.
- Usada por **Service Providers como transporte WAN** sin rutear con IP.
- Mucho más eficiente y ágil en cuanto a transporte y gestión de datos.
- Dar **QoS**: para esa celda/trama, ponerle una **etiqueta (*tag*)** al ingreso de la red.
##### Ventajas

- **Velocidad**: más rápida que el enrutamiento IP tradicional.
- Calidad de servicio (**QoS**): prioriza tráfico (voz, video, datos críticos).
- **Escalabilidad**: fácil de ampliar para muchas sucursales o clientes.
- Soporte **multiprotocolo**: funciona con IPv4, IPv6, ATM, Frame Relay, etc.
- **VPNs MPLS** (*Virtual Private Network*): crear redes privadas (virtuales y seguras) sobre la red del proveedor.
- Aplicaciones comunes VPNs corporativas (sucursales conectadas por red privada).
- Tráfico de voz y video con **alta prioridad**.
##### Componentes

![[Capa 2 MPLS componentes.png]]