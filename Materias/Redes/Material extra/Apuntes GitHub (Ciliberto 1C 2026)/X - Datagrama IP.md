### Datagrama IP

- **Definición de Internet**: Es un conjunto de redes o sistemas autónomos conectados entre sí que comparten una pila de protocolos comunes y utilizan el protocolo IP.
- **Características de IP**: Es una red de datagramas no orientada a conexión que ofrece un servicio de "mejor esfuerzo" (best effort).
- **Fiabilidad**: La entrega no está garantizada; en caso de congestión, los routers pueden descartar paquetes sin previo aviso.
- **Estructura**: El datagrama IP consta de una cabecera (parte fija de 20 bytes y opcional de hasta 40 bytes) y el texto o datos. La cabecera siempre es múltiplo de 4 bytes para optimizar el procesamiento.
### Direccionamiento IPv4

- **IPv4**: 4 octetos.
- **Requisitos de Configuración**: Para configurar TCP/IP se requiere una dirección IP, una máscara de subred y una puerta de enlace (gateway).
- **Dirección IP**: Es un número de 32 bits que identifica de forma única a un host en la red. Se expresa comúnmente en formato decimal punteado (ej. 192.168.123.132).
- **Clases de Direcciones**:
    - **Clase A**: Para grandes redes (primer octeto 0-127).
    - **Clase B**: Para redes moderadas (primer octeto 128-191).
    - **Clase C**: Para redes pequeñas (primer octeto 192-223).
    - **Clase D**: Para Multicast (224-239).
    - **Clase E**: Experimental (240-255).
### Máscara de Red y Enrutamiento

- **Máscara de Subred**: Se utiliza para diferenciar la **porción de red y la de host** dentro de una dirección IP mediante una operación lógica AND bit a bit.
- **Gateway Predeterminado**: Es la dirección de la interfaz del router local que permite al host comunicarse con redes remotas.
- **Lógica de Entrega**: Si el destino está en la misma red, la entrega es directa; si está en otra red, el paquete se envía al router (R2 en el ejemplo) para su salida.

### Protocolo HTTP (Capa de Aplicación)

- **Definición**: Protocolo de solicitud-respuesta que define la comunicación entre clientes web (navegadores/apps) y servidores.
- **Proceso de 4 pasos**:
    1. **Navegación**: El usuario inicia la solicitud mediante una URL.
    2. **Solicitud**: El cliente envía un mensaje HTTP indicando la versión y encabezados.
    3. **Respuesta**: El servidor devuelve un código (ej. 200 OK, 404 Not Found) y el contenido (usualmente HTML).
    4. **Representación**: El navegador muestra la página al usuario.

### Protocolo SMTP (Capa de Aplicación)

- **Definición**: Estándar para la transferencia de correo electrónico entre servidores. Es un protocolo de entrega, no de recuperación.
- **Funcionamiento**: Utiliza TCP para transporte e inicia la comunicación con el comando "Hello" (HELO/EHLO).
	- **Apertura de la conexión**: usando TCP, comienza con el comando "Hello".
	- **Transferencia de los datos**: contenido real del correo (encabezado con destino y asunto, cuerpo, etc).
	- **Agente de transferencia de correo** (MTA): este programa comprueba el dominio de la dirección de correo del destinatario, para ver si difiere o no de la del remitente.
	- **Cierre de la conexión**: cliente avisa finalización de transmisión al servidor, y el servidor cierra la conexión. No se recibirán más datos de correo a menos que se vuela a abrir.

#### Comandos

Comandos predefinidos basados en texto: qué debe hacer el cliente o el servidor para gestionar los datos. 
- **HELO/EHLO**: inician la conexión SMTP entre cliente y servidor. `EHLO` es para un tipo especializado de SMTP.
- **MAIL FROM**: indica al servidor quién envía el correo. 
- **RCPT TO**: enumera los destinatarios del correo, se puede enviar varias veces si hay varios destinatarios.
#### Concepto de Puertos

- Los puertos actúan como "números de puerta" o departamentos dentro de una dirección IP.
- Permiten que el sistema operativo entregue los datos a la aplicación correcta (ej. puerto 80 para web, puerto 25 para correo). 
- Los firewalls suelen bloquear puertos innecesarios para mejorar la seguridad.
- **Puertos SMTP**:
    - **25**: Tradicional entre servidores, hoy frecuentemente bloqueado por spam.
    - **465**: Antiguo para SSL, actualmente obsoleto.
    - **587**: Puerto por defecto actual con encriptación TLS.
    - **2525**: Alternativo si los anteriores están bloqueados.