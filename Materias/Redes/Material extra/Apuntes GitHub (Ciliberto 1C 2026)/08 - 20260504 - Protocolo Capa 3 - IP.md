## Clase 8 - Protocolo Capa 3 - IP

### Definición IP

**IP** (Internet Protocol): protocolo de capa 3 (Red) del modelo OSI.

- **Direccionar y enrutar paquetes** de datos entre dispositivos en redes IP (como Internet).
- RFC 791 (IPv4), RFC 8200 (IPv6).
- Define:
	- cómo debe estructurarse un paquete;
	- cómo se enruta;
	- cómo se entregan los datos de un punto a otro (sin establecer conexión previa).
- Va estableciendo la conexión en el camino (no es un protocolo orientado a conexión).

#### Funciones clave

##### 1. **Direccionamiento**

- Asigna una **IP única** a cada dispositivo en la red.
- Se combinan con una **máscara de subred** (qué parte es la red y cuál el host).
- Ejemplo: IP 192.168.1.10 con máscara 255.255.255.0 → red 192.168.1.0. El último octeto de la máscara se reserva para definir el host.

##### 2. **Encapsulamiento**

- Recibe datos desde la capa de transporte (TCP, UDP).
- Los empaqueta en un datagrama IP.
- Envía el datagrama a la capa 2 de enlace (como Ethernet).

##### 3. **Fragmentación**

- Puede dividir el paquete en **fragmentos** si es muy grande para el MTU del enlace (Ethernet tiene 1500 bytes).
- Cada fragmento tiene su propio **encabezado**.

##### 4. **Ruteo (Routing)**

- Envía los paquetes por múltiples redes.
- Toma decisiones basadas en **tablas de enrutamiento**.
- Usa protocolos como OSPF, BGP, EIGRP, RIP (capa 3 con IP).

##### 5. **Entrega sin conexión ni garantía**

- Es **no confiable** y **sin conexión**: no garantiza que el paquete llegue ni el orden o integridad.
- Se junta con **TCP** cuando es necesario para garantizar entrega.

#### Tipos de redes IP

- **Pública**: se asigna a un dispositivo para que pueda ser identificado en Internet. 
	- Única a nivel mundial.
	- Permite que cualquier equipo o servidor que la tenga **pueda ser localizado** desde cualquier parte del mundo.
- **Privada**: se utiliza dentro de una red LAN (oficinas, escuelas, etc).
	- No es válida para comunicarse directo en Internet.
	- Está reservada para entornos privados.
	- Se aplica a nivel WAN con soluciones de VPN IP.

![[IP VPN.png]]

- Pasar de una red pública a una privada de forma **tunelizada**.
### Encapsulamiento

- El protocolo IP puede ser **transportado sobre capas inferiores**. 
- Esto ofrece eficiencia, confiabilidad y gestión de la red vs. escabilidad.

Ejemplos: 

- **Ethernet**: Trama (802.3) - LAN, WAN
- **MPLS**: Label Stack antes de IP - WAN, core IP
- **ATM**: Celdas fijas de 53 bytes - Redes legacy de carriers
- **POS**: IP directo sobre HDLC o PPP sobre SONET - Redes de backbone óptico

#### 1. **IP/MPLS**

-  MPLS es una tecnología de conmutación de etiquetas que actúa **entre las capas 2 y 3** del modelo OSI. 
- No reemplaza al protocolo IP, sino que lo encapsula añadiendo un encabezado especial de **etiqueta (label)**.
- Permite un reenvío más **eficiente** dentro del núcleo de la red.
- Encabezado MPLS:
	- **Label** (20 bits): identificador de ruta
	- **Exp** (3 bits): para QoS
	- **S** (1 bit): último label en la pila
	- **TTL** (8 bits): similar a TTL de IP
- **Rendimiento**: reenviar paquetes con mayor rapidez, sin examinar dirección IP.
- **Escabilidad**: admite múltiples etiquetas (Label Stack), facilitando VPNs, ingeniería de tráfico.

![[IP sobre MPLS.png]]
#### 2. **IP/ATM**

- ATM es una tecnología orientada a conexión que divide los datos en **celdas fijas de 53 bytes**. 
- Fue ampliamente utilizada en redes de operadores y acceso DSL. 
- Para transportar IP sobre ATM, se requiere una **capa de adaptación**, usualmente **AAL5** (ATM Adaptation Layer 5).
- Existen distintos Adaptation Layer: AAL1 para CBR, AAL2 para VBR, y AAL5 para IP sobre ATM.

![[IP sobre ATM.png]]

Cada celda ATM (parte inferior de la imagen) contiene:

- **Encabezado (5 bytes)**: identificadores de circuito virtual.
- **Carga útil (48 bytes)**: fragmento AAL5.

PDU (Protocol Data Unit) de AAL5 **empaqueta un datagrama IP** y se divide en **múltiples celdas**.

**Características**:

- **Eficiencia fija**: tamaño constante de celdas optimizado para tráfico de voz/datos.
- **Fragmentación a nivel de enlace**: los paquetes IP grandes se dividen en varias celdas y se reensamblan en el destino.
- **Complejidad de gestión**: requiere mantenimiento de circuitos virtuales (VCs) y adaptación de protocolos.

#### 3. **IP/POS** (Packet over SONET/SDH)

- Técnica utilizada para transportar paquetes IP directamente sobre **redes ópticas de alta capacidad**. 
- Utiliza una capa de enlace mínima (típicamente PPP o HDLC)
- Transmite el tráfico directamente sobre la capa física SONET/ SDH.

Partes: 

- **SONET/SDH**: sincronización, multiplexación, y framing (nivel óptico).
- **PPP (Point-to-Point Protocol)**: encapsula el paquete IP y añade funcionalidades (autenticación, compresión, negociación de protocolos). También se emplea HDLC como mecanismo de framing básico.

Características:

- **Alta eficiencia**: se reduce el overhead al mínimo necesario.
- **Sin direccionamiento MAC**: es una conexión punto a punto, no se requiere este direccionamiento.
- **Fiabilidad**: inherente a la arquitectura SONET/SDH y su capacidad de recuperación ante fallas.

Aplicaciones:

- Enlaces backbone de operadores y proveedores de Internet (OC-3, OC-12, OC-48). 
- Transmisión a través de **infraestructura óptica** con mínima latencia.
- Ambientes donde se requiere **gran ancho de banda** con bajo retardo.

### Peering y Tránsito IP

#### Peering IP

- Acuerdo entre dos redes para **intercambiar tráfico directamente**, sin necesidad de pagarle a un tercero por el transporte de datos. 
- Relación entre iguales, cada parte se beneficia al reducir costos y mejorar el rendimiento.

Beneficios y características:

- Reducir costos, mejorar la latencia y descongestionar enlaces internacionales.
- Pueden hacer peering público (compartido entre muchos) o peering privado.
- Intercambio de datos **sin costo monetario directo** entre dos redes.
- Se establece por medio de enlaces físicos (como cables de fibra óptica) en puntos de intercambio de Internet (IXP).
- Mejora la latencia y la eficiencia, ya que el tráfico toma rutas más cortas.
- Normalmente se utiliza entre redes de tamaño similar.

#### Tránsito IP

- Provisión de **acceso a toda la red global** mediante un proveedor de Internet de mayor jerarquía, que transporta el tráfico de una red (cliente) hacia el resto de Internet.
- Cuando una red no puede llegar directamente a todos los destinos en Internet, necesita **contratar tránsito IP a un proveedor** que sí tenga conexiones globales.

Características técnicas:

- Utiliza **BGP** (protocolo de enrutamiento) para el intercambio de rutas entre el proveedor y el cliente.
- La red cliente anuncia su bloque de direcciones IP públicas al proveedor.
- El proveedor anuncia todas las rutas de Internet al cliente.
- Se cobra normalmente en función del **ancho de banda contratado**, medido en Mbps o Gbps.
- Los contratos incluyen parámetros como **SLA** (Service Level Agreement) que garantizan niveles de disponibilidad, latencia, jitter, y pérdida de paquetes.

### Sistemas Autónomos

- Conjunto de redes cuya administración está realizada por una **misma entidad**.
- Posee **reglas propias** para decidir cómo manejar y enrutar el tráfico.
- Posee **independencia operativa**, administra sus propias rutas y direcciones IP. 
- Puede conectarse con otros AS por medio de Peering o Tránsito IP.
- Permite gestionar redes complejas y conectividad internacional .

Tipos de AS:

- **Proveedor de servicios (ISP)**: conectividad a otros AS o usuarios finales (Telecom Argentina, Movistar, IPLAN).
- **Contenido/empresa**: grandes organizaciones con mucho tráfico (Google, Netflix).
- **Académicos/institucionales**: universidades o redes científicas.

#### Factores predominantes para visibilidad global de AS

1. **Infraestructura global**:
	- Empresas con gran capilaridad de red, centros de datos distribuídos y puntos de presencia (PoPs) tienen mayor capacidad para garantizar servicios de baja latencia y alta disponibilidad.
2. **Número de Sistemas Autónomos (AS)**:
	- Cada empresa opera uno o más AS, y su calidad puede medirse por la cantidad de prefijos IP anunciados, el número de peers y la estabilidad de sus rutas.
3. **Participación en puntos de intercambio (IXP)**:
	- Una empresa que participa activamente en IXPs puede reducir los tiempos de tránsito entre redes, mejorando la calidad del servicio.
4. **Métricas de BGP Visibility**: 
	- Herramientas como "CAIDA AS Rank" o "BGPStream" permiten medir la visibilidad global de un AS observando desde cuántos puntos se puede ver su actividad BGP.
5. **Tiempo de actividad (Uptime)**: 
	- SLA del 99.99% o superior, indica una infraestructura resiliente.

#### Tipos de Routing IP

- **Enrutamiento estático**: las rutas se configuran manualmente por un administrador de red. Es simple y funciona bien en redes pequeñas, pero no se actualiza automáticamente si hay cambios.
- **Enrutamiento dinámico**: los routers se comunican entre sí para intercambiar información sobre las rutas y actualizar sus tablas automáticamente. Se adaptan mejor a cambios o fallos en la red, siendo ideal para redes grandes o complejas.