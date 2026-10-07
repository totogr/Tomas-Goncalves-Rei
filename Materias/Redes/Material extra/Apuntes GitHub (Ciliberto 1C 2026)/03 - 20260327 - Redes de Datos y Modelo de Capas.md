## Clase 3 - Redes de Datos y Modelo de Capas

**Red de datos**: 
- Sistema informático.
- Implica la existencia de **múltiples medios**.
- Como finalidad, **conectar dos o más dispositivos** (móviles, computadoras, routers, aplicaciones).
- Permitir la transmisión e intercambio de información y recursos.

### Clasificación de redes de datos

**Redes de Area Local (LAN)**: conecta a usuarios y aplicaciones en una proximidad geográfica cercana (mismo edificio u oficina).

**Redes de Area Extendida (WAN)**: conecta a usuarios y aplicaciones en ubicaciones geográficamente dispersas (extremo a extremo en cualquier parte del mundo).

### Modelo de Capas OSI

**Modelo Open Systems Communication (OSI)**: permite que diversos sistemas de comunicación se conecten usando protocolos estándar.

- Los estándares permiten que **los equipos puedan comunicarse entre sí**.
- Es un lenguaje universal para la conexión de las redes de equipos.
- Consiste en dividir un sistema de comunicación en **7 capas abstractas**, cada una apilada sobre la inferior.

#### Capa 7 - APLICACIÓN

- Identifica el partícipe de la comunicación.
- Proporciona funciones para determinados servicios de aplicación, como **transferencia de archivos** y **terminales virtuales**.

Ejemplos de aplicaciones basadas en TCP/IP:

- **FTP** - File Transfer Protocol
- **TFTP** - Trivial File Transfer Protocol
- **SMTP** - Simple Transfer Mail Protocol
- **SNMP** - Simple Network Management Protocol
- **HTTP** - Hypertext Transfer Protocol
- **DHCP** - Dynamic Host Configuration Protocol 

#### Capa 6 - PRESENTACIÓN

- Proporciona servicios **convirtiendo los distintos formatos** de datos, video, sonido y gráficos en un **formato apropiado** para la transmisión.
- Responsable de la compresión, descompresión, cifrado y descifrado de los datos.
- Los protocolos suelen estar incorporados en los de capa de aplicación ya existentes:
	- **Texto**: ASCII, EBCDIC
	- **Gráficos**: TIFF, JPEG, GIF, PICT
	- **Sonido**: MIDI, MPEG, QuickTime

#### Capa 5 - SESIÓN

- Controla el **diálogo** entre dispositivos o hosts. 
- Establece, administra y termina sesiones entre aplicaciones.

Protocolos conocidos:
- Sistema de archivos en Red (NFS)
- Lenguaje de Consulta Estructurado (SQL)
- Llamada de Procedimiento Remoto (RPC)
- Sistema X-Window
- Protocolo de session AppleTalk (ASP)
- Protocolo de control de session DNA (SCP)

#### Capa 4 - TRANSPORTE

- Encargada de la **entrega extremo a extremo** de la información.
- Se incluye **recuperación de errores** y **control de flujo**.
- Pueden ser:
	- **No confiables**: no tienen ninguna responsabilidad para establecer conexiones, acuses de recibo y control de flujo (ej: UDP, lo emplean TFTP, SNMP, DNS, NFS).
	- **Confiables**: responsables a la hora de establecer conexiones (ej: TCP, lo emplean TELNET, FTP, HTTP).

#### Capa 3 - RED

- Proporciona **conectividad y selección de ruta** entre dos sistemas finales (origen y destino).
- Los sistemas pueden estar ubicados geográficamente en **distintas redes**.
- Establece la dirección de origen y destino, las cuales no cambian a lo largo de la ruta (para TCP/IP, son las **direcciones IP**).

Ejemplos de protocolos:
- Protocolo de Internet (IP)
- Protocolo de Intercambio de Paquetes (IPX)

#### Capa 2 - ENLACE DE DATOS

- Proporciona un tráfico confiable por un **enlace físico**. 
- Se ocupa del **direccionamiento físico** de la topología de red.
- Maneja la **entrega ordenada de tramas** y el control de flujo de información.
- Las tramas se entregan **de un nodo a otro**: host-to-host, host-to-switch, switch-to-router.
- Switches, tarjetas de red, etc (cualquiera que maneje MAC Address).

Ejemplos de protocolos:
- Ethernet
- IEEE 802.3
- Token Ring
- Control de Enlace de Datos de alto nivel (HDLC)
- Protocolo punto a punto (PPP)

#### Capa 1 - FÍSICA

- Define especificaciones eléctricas, mecánicas, procedimentales y funcionales para **activar, mantener y desactivar el enlace físico** entre sistemas finales.
- Se manejan los medios como **cables**.
- **Características definidas**: niveles de tensión, duración de cambios de tensión, velocidades de datos físicos, distancias máximas de transmisión, conexiones físicas.

Estándares de esta capa:
- 10BASE-T (10Mbps)
- 100BASE-TX (100Mbps)
- V35
- RS232

### SDH (Syncrhonous Digital Hierarchy)

- La trama SDH va encapsulada en un **contenedor**. 
- Luego se añaden **cabeceras de control** que identifican su contenido.
- Se realiza un proceso de **multiplexación**: los niveles superiores se forman multiplexando a nivel de *byte* varias estructuras STM-1.
- Así se generan los niveles STM-4, STM-16, STM-64 y STM-256.
- La jerarquía está **basada en modelos europeos**. 
- Si se asocia con los modelos vistos, llega **hasta las capas 1 y 2 del modelo OSI**. Se transmite mediante **fibra óptica** (medios físicos).
- Disponibiliza una autopista para que la información viaje.

En la jerarquía americana SONET (sincrónica), se soportan varios servicios de transmisión de datos. Se tiene:
- **T1** o **DS1**: 1.536 Mbps
- **T3** o **DS3**: 44.736 Mbps
- **OC-3**: 155.52 Mbps
- **OC-12**: 622.08 Mbps
- **OC-48**: 2.488 Gbps

### Capas vs. Modelo TCP/IP

El modelo TCP/IP tiene únicamente **4 capas**:
- **Acceso a la red** (capas 1 y 2 de OSI)
- **Internet** (capa 3 de OSI)
- **Transporte** (capa 4 de OSI)
- **Aplicación** (capas 5, 6 y 7 de OSI)

El protocolo **IP** (Internet Protocol) no se orienta a la conexión, y se utiliza tanto por el origen como por el destino, para la **comunicación de datos** a través de una red de paquetes conmutados.

- Ambos modelos (OSI y TCP/IP) buscan organizar las funciones de una red.
- El modelo OSI es **teórico** (y experimental).
- El modelo TCP/IP se usa en la práctica en Internet (es más real).