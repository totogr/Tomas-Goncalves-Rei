## Clase 5 - Infraestructura de Redes - Fibra Óptica

### Fibras Single vs. Multimode

Se separan por el **modo de transmisión de luz**.

**Recubrimiento**: *cladding*.

- **Multimodo**: núcleo de 50 micrones. Cladding más chico (porque el core es más grande). La luz se va refractando mediante diversos modos, en el vidrio del láser.
- **Monomodo**: núcleo de 9 micrones. Mayor superficie de cladding. Es solo un haz de luz recto (no tienen varios modos). Es para distancias más largas.

![[Fibra Multimodo vs Monomodo.png]]
### Características según flamabilidad

- **Áreas Plenum**: entre el plafón y la loza, donde conviven sistemas de iluminación, aire acondicionado, etc.
	- No crean llamas al contacto con el fuego.
	- Generan muy poco humo.
	- El forro del cable se derrite cuando hay fuego.
- **Áreas Riser**: áreas verticales, los cables viajan de piso a piso.
	- Deben ser retardantes al fuego.
	- Generan más humo que los Plenum.
	- Llegan a crear flama, pero se apagan antes de 1,5 m.
	- Son más económicos.

Ambos cables **generan un poco de humo**. El humo puede ser tóxico (los forros son de flúor, cloro, bromo, yodo y astato, que son **halógenos**)

Otros cables son libres de halógenos (hoy muy utilizados), los cuales reducen la cantidad de humo tóxico.

### Conectores de F.O.

- **Férula**: más importante. Sujeta, protege y alínea la fibra de vidrio. Son de cerámica y plástico o metal de alta calidad.
- **Mecanismo de acoplamiento**: mantiene el conector en su lugar cuando está conectado a otro dispositivo.
- **Cuerpo**: sostiene la férula, el mecanismo de acoplamiento y la bota. Es de plástico o metal.

#### Pulido de férulas

**Pérdida de retorno**: cantidad de energía perdida cuando la luz vuelve de la fibra a la fuente (discontinuidad entre férulas). Se mide en **decibeles**. **Mayor valor es mejor**.

Las férulas se pulen de diferentes maneras, clasificando a los conectores como:
- **PC (Physical Contact**): pulidos con ligera curvatura, eliminando espacio de aire entre las férulas. 
	- Pérdida de retorno: entre -30 dB y -40 dB.
- **UPC (Ultra Physical Contact)**: también tienen una curvatura, pero esta es mucho más pronunciada. Ideales para transmitir señales de TV y data.
	- Pérdida de retorno: entre -40 dB y -55 dB, 
- **APC (Angled Physical Contact)**: ángulo de 8°, conexiones sean mucho más unidas. 
	- Pérdida de retorno: -60dB.

#### Tipos de conectores

- **Conectores pre-pulidos**: 
	- Tienen un empalme de fibra pequeño pegado en su interior.
	- Vienen pulidos de fábrica. 
	- Se unen a la fibra con un empalme mecánico cubierto con un gel que tiene un índice de refracción parecido al de la fibra de vidrio encargado de reducir la pérdida. 
	- Fáciles de instalar.
	- Pérdida de inserción típica: entre 0.5dB y 0.7dB. 
- **Conectores monomodo y multimodo**: 
	- Conectores monomodo (UPC): azules.
	- Conectores multimodo: beige.
	- APC (monomodo): verdes.

Los conectores cumplen con los **estándares de la TIA *Fiber Optic Connector Intermateability Standards (FOCIS)***.

**Pérdida de inserción**: señal que se pierde al atravesar un componente (atenuación). Es la reducción de potencia desde la entrada hasta la salida. **Menor valor es mejor**.

- **SC (Standard Connector)**: creado a mediados de los 80 por Nippon Telegraph and Telephone.
	- No fue muy usado en sus inicios (muy costoso). 
	- **Pérdida de inserción**: 0.25dB. 
	- Calificados para soportar 1000 ciclos de conexión y desconexión.
	- Alinean las fibras con precisión debido a sus férulas de cerámica, que funcionan con un sistema push y pull. 
	- Inicialmente usados en sistemas de Gigabit Ethernet, pero reemplazados por conectores de menor tamaño. 
	- Actualmente usados en versiones **monomodo** y **multimodo** en sistemas de TV por cable y telefonía.
- **LC (Lucent Connector)**: creado por Lucent Technologies
	- Más chico que el SC: en un espacio de SC entra un LC duplex (dos espacios).
	- Extensamente usado en aplicaciones monomodo.
	- Excelente rendimiento, terminado de manera sencilla. 
	- Férulas de 1.25mm, de mecanismo de push y pull. 
	- **Pérdida de inserción**: 0.10dB. 
	- Tamaño pequeño: reduce necesidad espacio en un 50% en contraste con conectores SC y ST
	- Usados en sistema de alta densidad (paneles y racks, FTTH, distribución en edificios, LANs, redes de procesamiento de data y sistemas de TV por cable). 
	- Pueden ser utilizados con cables **monomodo** y **multimodo**. 
	- **FOCIS-10**: TIA-604-10.
- **ST (Straight Tip)**: creado por AT&T.
	- Utilizados en sistemas de redes. 
	- **Pérdida por inserción**: 0.25dB. 
	- Sostienen la fibra con una férula de 2.5mm, se mantiene con un sistema de anclaje por bayoneta.
	- Usados en **aplicaciones de larga y corta distancia** (campus o redes corporativas y en aplicaciones militares). 
	- Conectado y desconectado de manera fácil debido a su flexibilidad.
	- **FOCIS-2**: TIA-604-2
- **FC (Ferrule Connector)**: diseñado por Nippon Telegraph and Telephone. 
	- Férula de cerámica de 2.5mm, se mantiene en su lugar con un sistema de rosca. 
	- Disponibles para fibra multimodo y monomodo
	- Mayormente usados en aplicaciones monomodo y en redes de alta velocidad. 
	- Principal uso en **entornos de alta vibración** debido a su sistema de rosca. 
	- **Pérdida por inserción**: 0.3dB.
- **MTRJ (Mechanical Transfer-Registered Jack)**: 
	- Conector dúplex: sostiene dos fibras al mismo tiempo. 
	- Cuerpo y férulas hechos de **polímero**.
	- Versiones hembra y macho.
	- Mayormente utilizados con fibra **multimodo**.
	- Nació pero duró muy poco en el mercado.
- **MPO (Multi-fiber Push-on)**: 
	- Conector **multi-fibra**: puede sostener desde 8 hasta 24 fibras en una sola férula rectangular. 
	- Usados para construir redes de Ethernet de transmisión paralela de 40G, 100G, 400G.
	- Férulas macho: 2 pines, conectores hembra: 2 agujeros.
	- Pérdida por inserción: 0.25dB.
	- Aplican tanto para monomodo como para multimodo.

#### Velocidades (anchos de banda)

- Hay diferentes conectores: difieren las aplicaciones (velocidades y *lanes*) que la interfaz va a mandar en paralelo.
- Ejemplo: **800G, para MPO8 O MPO12**. Se pueden mandar en distintos *lanes*, o *longitudes de onda* (por ejemplo, 200G en 4 lanes). 800G de transmisión dividido en 4 hilos (lo mismo para la recepción). Para MPO12 sobran 4 hilos, para MPO8 se usan todos (4 lanes por transmisión, 4 por recepción). 
- Ejemplo: **800G, para MPO16**. Se usan los 16 hilos: 8x100G para transmisión, y otros 8x100G de recepción. Es el que mejor se adapta a las interfaces: no se dejan hilos libres, y el costo debería ser mucho menor a un MPO24, y parecido al de MPO12.

### OMX

TIA adoptó la nomenclatura de la fibra de la norma internacional ISO/IEC 11801.

- Fibra **multimodo**: prefijo **`OM`**. 
	- Líneas **viejas** de multimodo: OM1 (hasta 2001/2) (62,5 micrones de núcleo).
	- Ya se especifican fibras OM3/OM4 en la actualidad (50 micrones de núcleo).
- Fibra **monomodo**: prefijo **`OS`**.

Con esto se debería **minimizar la confusión** asociada con asistencia brindada a sitios remotos, si hay errores con las aplicaciones. 

Cada OM tiene un requisito de **ancho de banda modal (MBW)** mínimo.

![[Fibra OMX.png]]

Cada tipo de fibra es **la evolución de la anterior** (la de arriba).
#### OM5 

**OM5 WBMMF (Wideband Multimode Fibre)**: cada hilo multiplexado por longitud de onda, reduciendo cantidad de fibras en paralelo (para escalar a 800G).

- **Mayor alcance** que OM4 (para las 4 longitudes de onda). Soporta hasta 150m, mientras que OM4 soportaba 100m.
- **Soporte de aplicaciones**: optimización en el tráfico que puede soportar. Eficiencia de vínculos de fibra (tecnologías modernas de modulación).
- Ejemplo -- Centro de datos (construir de cero): si la diferencia económica entre OM4 Y OM5 es solo del 10%, **conviene ya aplicar OM5 en la construcción**, sobre todo para tolerancia grandes distancias. 
- **Aplicación**: los switches ya traen puertos MPO para conectar los cables.

### Tendido de cables de F.O. por montante

- Cables tendidos en bundles uniformes a lo largo de todas las canastas.
- Asegurados con velcro, sin cruces entre cables (ni entre grupos de cables).

Es importante **inspeccionar las fibras antes de conectarlas**:
- Contaminación en las **caras de conectores**: peor performance de red.
- **Partículas de polvo** o sólidos: daño a los conectores.

### Consideraciones de seguridad

- Radiación emitida por fibras monomodo: daños en la retina del ojo.
- No se persive dolor físico a este efecto.
- El iris del ojo **no se contraerá automáticamente** como cuando se ve una luz brillante.
- Daño **permanente o temporario** dependiendo la potencia de salida del láser y la distancia del extremo de la fibra al ojo.
- Los sistemas de sonda de video cuentan con protección de daño de retina.
- **No se recomienda usar alcohol** para quitar suciedad: sigue dejando partículas de suciedad que degradan.
- **No remover** protectores de polvo de los adaptadores. 

### Protocolo de aceptación de pérdidas de F.O.

![[Fibra Protocolo de aceptacion.png]]

Se usa por una empresa grande que debe **regular la correctitud y aceptación de las instalaciones**.

- Especifica los **valores máximos en decibeles** que deben aceptarse para las mediciones de pérdidas.