## Clase 2 - Transmisión de datos

### Sistemas de comunicación

**Señal analógica**: 
- Variables eléctricas continuas en el tiempo
- Pueden ser en forma de una corriente, una tensión o una carga eléctrica.
- Varían en forma continua entre un límite inferior y un límite superior.
- Para señales periódicas podemos definir ciertos parámetros como el período T. 

**Señal digital**:
- Son variables eléctricas con dos niveles (0 o 1). 
- Su variación en el tiempo contiene la información a transferir acorde a una codificación a utilizar.

#### Teorema de NYQUIST

- Frecuencia mínima de muestreo para **reconstruir una señal analógica** en base a su muestreo.
- FM: frecuencia muestreo
- FS: frecuencia señal

`NYQUIST --> FM >= 2 * FS`

#### SDH (Synchronous Digital Hierarchy)

- Es un paquete de información, conjunto de bits.
- La trama SDH va encapsulada en una estructura llamada **contenedor**. 
- Luego se añaden **cabeceras de control** para identificar su contenido.
#### Jerarquía de transmisión

- Basándonos en un canal de Voz digitalizado a **8Khz** (doble de la frecuencia máxima de la voz) con 8 bits nos arroja una trama de 64Kbps.
- Si concatenamos 32 canales de voz de 64 Kbps nos da una trama de 2048 = 2Mbps (SDH).
- Tramas SONET de 24 canales implica 1536 = 1.5Mbps

**Estructura**:

- Se representa como una bloque de 9 filas x 270 columnas (compuesto por **bytes** - 8 bits).
- Cada trama dura 125 microsegundos, lo que significa que transmiten **8000 tramas por segundo**.
- De las 270 columnas:
	- Primeras 9 - **sobrecarga (overhead)**: información de gestión, control y mantenimiento (regeneración y multiplexación).
	- Las otras 261 - **carga útil (payload)**: transportan los datos encapsulados en contenedores virtuales.

**Tramas**:

Trama 2Mbps = E1
Trama 34 Mbps = E3

- **STM-1** = 8000 * (270 columnas * 9 filas * 8 bits) = 155 Mbps.
- **STM-4** = 4 * 8000 * (270 columnas * 9 filas * 8 bits) = 622 Mbps.
- **STM-16** = 16 * 8000 * (270 columnas * 9 filas * 8 bits) = 2.5 Gbps.
- **STM-64** = 64 * 8000 * (270 columnas * 9 filas * 8 bits) = 10 Gbps.
- **STM-256** = 256 * 8000 * (270 columnas * 9 filas * 8 bits) = 40 Gbps.

Los niveles superiores a STM-1 se logran **multiplexando a nivel de byte** varias estructuras STM-1.

**SONET**:

Jerarquía americana SONET: estándar de red óptica sincrónica ampliamente adoptada en América del Norte, diseñada para soportar varios servicios de transmisión de datos. 

En esta jerarquía tenemos:

- **T1 o DS1**: 1.536 Mbps
- **T3 o DS3**: 44.736 Mbps
- **OC-3**: 155.52Mbps (= STM-1)
- **OC-12**: 622.08Mbps (= STM-4)
- **OC-48**: 2.488 Gbps (= STM-16)
#### Teorema de Hamming

- Corregir en caso de tener errores de transmisión.
- Agregando un **bit de paridad** podemos detectar si hay un error en el bit. Sirve para detectar solo un bit cambiado.
- Para corregir N bits es necesario una distancia **`d = 2n+1`**. Por ejemplo, con d=3 puedo corregir hasta 1 bit.

**Ejemplo**: binario con *n = 4*

1. Construimos el **Transmisor (TX)**.

`A B C D`: tira de bits original 
	
- **M** bits a proteger del código: 4 bits
- Posibilidad de inversión de 1 bit (errado). Es decir, asumimos un bit errado en Hamming.
- Se agregan **K** bits tal que: `2^K >= M + K + 1`
- La ecuación se cumple para K = 3, entonces necesito 3 bits de redundancia.
- Los lugares de los K_i deben ser potencias de 2.

Por lo tanto irán en el orden: `K1 K2 M3 K4 M5 M6 M7`
Nos alcanzará con 7 bits, 4 de ellos la información real, y 3 de ellos de redundancia.

- A ese resultado se le llama **Palabra de Hamming**.
- M_i son los datos originales, K_i son la redundancia que me permite **aumentar la distancia del código detector-corrector**.
- Los K_i se generan por cada región del código. Cada bit M_i interviene en generar los K_i tal que las sumas de los subíndices de K_i sea igual al subíndice de M_i.

M3 genera K1 y K2
M5 genera K1 y K4
M7 genera K1, K2, K4

 - Ahora vemos qué M_i intervienen en cada uno de los K

Para K1 intervienen M3, M5 y M7
Para K2 intervienen M3, M6 y M7
Para K4 intervienen M5, M6 y M7

Los K_i se arman en base a sus generadores:
- `K1 = M3 XOR M5 XOR X7`
- `K2 = M3 XOR M6 XOR X7`
- `K4 = M5 XOR M6 XOR X7`

Calculo los `XOR` con la cantidad de 1s:
- A cantidad **par** de 1s, va un **0**.
- A cantidad **impar** de 1s, va un **1**.

**HASTA AQUÍ SE HA CONSTRUÍDO EL *TRANSMISOR* (TX)**. 

2. Leemos información en el **Receptor (RX)**.

Solo sabemos lo que recibimos; no conocemos el mensaje original.

**Ejemplo**: recibimos `1 1 0 1 0 1 1`

Detectamos el error mediante la **paridad de los subcódigos** (P_i = bits de paridad):

- `P1 = K1 XOR M3 XOR M5 XOR M7 = 1 xor 0 xor 0 xor 1 = 0` --> K1 dio igual al XOR entre sus M generadores , por lo que el bit de paridad da 0.
- `P2 = K2 XOR M3 XOR M6 XOR M7 = 1 xor 0 xor 1 xor 1 = 1` --> K2 **no** dio igual al XOR entre sus M generadores , por lo que el bit de paridad da 1 e indica que hay un error en algún lado.
- `P4 = K4 XOR M5 XOR M6 XOR M7 = 1 xor 0 xor 1 xor 1 = 1` --> K4 **no** dio igual al XOR entre sus M generadores , por lo que el bit de paridad da 1 e indica que hay un error en algún lado.

Para encontrar el subíndice del error en la información inicial, armamos el conjunto de bits de paridad en orden descendiente:

`(P4, P2, P1) = (1, 1, 0)`

El número armado por los bits de paridad nos da el subíndice: 

`1 1 0 = 6` -> el bit erróneo es **M6**.

#### Multiplexación

**Multiplexor**: circuito combinacional con múltiples **entradas de Datos**, otra cantidad de **entradas de Control**, y **una salida**.

- Según la combinación lógica de las entradas de Control,  **solo una de las entradas va dirigida hacia la salida**.
- `#entradas-Datos = 2 ^ #entradas-Control`

#### TDM (Time Division Multiplexing)

- Compartir un mismo canal de transmisión entre varios usuarios. 
- Se separan los canales en slots/ranuras de tiempo.
- Todos usan el mismo ancho de banda disponible.
- Los receptores pueden reconstruir la información en base a los intervalos de tiempo de cada usuario.
- **TDM sincrónico**: intervalos de tiempo predefinidos y asignados de manera fija. Cada canal tiene un tiempo específico para enviar sus datos.
- **TDM asincrónico**: intervalos no predefinidos, se asignan dinámicamente según la disponibilidad de datos. Si no hay para enviar, el intervalo se utiliza para otro canal.

#### FDM (Frequency Division Multiplexing)

- Mediante filtros en el espectro de frecuencias, se transmite un usuario distinto en cada rango. 
- Se lo hace a cada señal, para que sean transmitidas simultáneamente a través del mismo canal. 
- El ancho de banda es propio para cada usuario.
- Ejemplo: **radio FM** (como las frecuencias 88.1MHz, 101.5MHz, etc).

#### WDM (Wavelength Division Multiplexing)

- Se dividen las distintas longitudes de onda que hay en la señal. 