## Clase 9 - Protocolo Capa 4 - TCP-UDP

### Protocolo UDP (User Datagram Protocol)

- **Definición**: Protocolo de la capa de transporte diseñado para enviar datagramas de forma simple y eficiente sin conexión previa. 
- **Encabezado**: Incluye puerto de origen, puerto de destino, longitud y checksum (todos de 16 bits).
- **Puertos**: Rango de 0 a 65535, divididos en **bien conocidos** (servicios de sistema, 0-1023), **registrados** (aplicaciones específicas, 1024-49151) y **dinámicos** (uso temporal, 49152-65535).
- **Ventajas**: Baja latencia (sin handshake), menor sobrecarga y simplicidad de implementación.
- **Desventajas**: No garantiza la entrega, no ordena paquetes y carece de control de congestión.
- **Casos de uso**: VoIP, juegos en línea, streaming de video y DNS.

Notas en clase:

- Protocolo importante de la **Capa 4 del modelo OSI (Transporte)**. 
- Eficiente y rápido: no hay controles de conexión.
- Es un protocolo de 16 bits. 
- Los puertos van del 0 al 65535. 

Proceso de comunicación:

1. **Preparación inicial**: aplicación emisora crea un socket UDP.
2. **Encapsulación del mensaje**: los datos de la aplicación se encapsulan en un datagrama UDP.
3. **Envío al nivel de red**: datagrama UDP se pasa al protocolo IP.
4. **Transporte por la red**: el paquete viaja por la red usando el enrutamiento IP.
5. **Recepción en el destino**: el host destino recibe el paquete IP.
6. **Entrega a la aplicación**: UDP identifica la aplicación destino usando el puerto.

#### Comparativa: UDP vs. TCP

- **Conexión**: UDP no requiere conexión; TCP requiere una conexión establecida antes de transmitir.
- **Orden y Secuencia**: TCP puede secuenciar y ordenar datos; UDP no.
- **Retransmisión**: TCP retransmite paquetes perdidos; UDP no puede recuperarlos.
- **Velocidad**: UDP es rápido pero puede entregar datos incompletos; TCP es más lento pero garantiza la entrega completa.

| **Característica** | **TCP (Transmission Control Protocol)**                     | **UDP (User Datagram Protocol)**                                             |
| ------------------ | ----------------------------------------------------------- | ---------------------------------------------------------------------------- |
| **Conexión**       | Orientado a conexión (Handshake). Requiere conexión previa. | No orientado a conexión. No la necesita previamente (solo origen y destino). |
| **Fiabilidad**     | Alta (Garantiza la entrega)                                 | Baja (Mejor esfuerzo)                                                        |
| **Orden**          | Entrega los datos en orden, y puede reordenar.              | No garantiza el orden ni secuenciar los datos.                               |
| **Velocidad**      | Más lento (por la sobrecarga), pero entrega completa.       | Muy rápido, con riesgo de datos incompletos.                                 |
| **Retransmisión**  | Sí, si se pierden paquetes.                                 | No retransmite (descarta).                                                   |
| **Uso común**      | Web (HTTP), Email, Transferencia de archivos                | Streaming, Juegos, VoIP, DNS                                                 |
#### Vulnerabilidades comunes en UDP

- **Ataques de Inundación**: envío masivo de datagramas UDP para saturar el ancho de banda y agotar los recursos del servidor, causando denegación de servicio (DoS).
- **Desbordamiento de Búfer**: procesamiento de datagramas maliciosos genera errores de memoria, permitiendo la ejecución remota de código en ciertas implementaciones.
- **Ataques de Reflexión**: el atacante falsifica la IP de la víctima, enviando datagramas a servidores legítimos que responden a la víctima, amplificando el tráfico y provocando DoS.
- **Ataques Loop DoS**: creación de bucles de tráfico entre dispositivos mal configurados, agotando sus recursos y causando interrupciones en la red.

#### Medidas de protección esenciales

- **Firewalls e IPS**: bloquean tráfico no deseado y sospechoso. Es crucial configurar reglas para **limitar el acceso a puertos UDP** no utilizados, fortaleciendo la seguridad perimetral.
- **Rate Limiting**: restringe la cantidad de paquetes recibidos de una fuente en un tiempo dado, reduciendo posibilidad de sobrecarga y ataques de inundación.
- **Validación de Datos**: validar datagramas entrantes, para que cumplan con formatos esperados y no contengan datos maliciosos.
- **Monitoreo y Detección**: identifican patrones de tráfico anómalos, alertando sobre posibles ataques en curso.
#### UDP en redes modernas

**Innovación**: 
- **QUIC - Velocidad y Seguridad**: protocolo de Google que combina UDP con fiabilidad y cifrado TLS 1.3, reduciendo latencia y ataques comunes. Es la base de HTTP/3.
- **DTLS - TLS sobre UDP**: versión de TLS adaptada para UDP, proporcionando seguridad a aplicaciones de baja latencia (comunicación en tiempo real).
- **RTP**: usado en streaming de audio y video, aprovechando velocidad UDP para transmisiones en tiempo real (telefonía IP y videoconferencias).

**Crecimiento a futuro**:
- Aplicaciones en tiempo real
- Demanda de conexión rápida con UDP
- Extensión de capacidades (seguridad y fiabilidad)
- Flexibilidad y velocidad de diseño de aplicaciones
- Centro de la evolución de Internet (QUIC y HTTP/3)