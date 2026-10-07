## Clase 6 - Centro de Datos

**Definición**: 
- Sala física, edificio o instalación. 
- Alberga infraestructura informática para **crear, ejecutar y entregar aplicaciones y servicios**.
- Almacenan y gestionan los datos asociados a las aplicaciones y servicios entregados.

### Tipos de Centros de Datos

- **Empresariales (Enterprise)**: toda la infraestructura y datos **se alojan en las instalaciones**. Empresas optan por tener sus propios centros de datos para sentir más control y seguridad.
	- Bancos, procesadoras de pago, aseguradoras, petroleras, energéticas, Retail, gobiernos.
- **Nube (Cloud Computing o Hyperscale)**: recursos de infraestructura para **uso compartido** de múltiples clientes, a través de **una o más conexiones a Internet**.
	- Google, Amazon, Microsoft, Meta, Oracle, SAP.
- **Gestionados (Telco o Collocation)**: infraestructura desplegada por Operadores de Telecomunicaciones. Permiten alojar servidores, aplicaciones y equipamiento a otras empresas.
	- Telecom, Telefonica, Claro, Arsat, Telecentro.

**Datacenter Edge**: se requiere gestión en el borde de la red (más cerca del usuario), para mejorar el servicio a los clientes.

### Estándares

- Basados en experiencias colectivas.
- Conocimiento de profesionales en campo.
- Consensos de la industria.

Ventajas:
- Camino hacia el uso de **elementos de alta calidad y confiabilidad** (reconocidos en el mercado).
- Mejoran **inter-operabilidad** entre fabricantes.
- Unifican criterios.
- Impulsan mejores prácticas.

Algunos estándares:
- **ANSI/TIA-942** (estándar de cableado)
- ISO/IEC JTC 1/SC 25
- IEC TC86 Fibre Optics
- ANSI/TIA TR-42
- ICEA Telecommunications fibre & Cable Technical Advisory Committee
- Telecordia
- IEEE 802.3
- FIber Channel T11
### Elementos y áreas de distribución

- TIA-942 para América del Norte
- EN-50600 para Europa

Se tienen en cuenta:
- Diseño
- Sistemas de refrigeración
- Seguridad
- Sustentabilidad

![[DC Areas de distribucion.png]]

- Se tiene un cuarto de entrada (ER), con acceso al área de distribución principal (MDA).
- Hay un área intermedia de distribución (IDA), y luego una de zona (ZDA). 
- Se pasan por todas ellas para llegar a los sectores de almacenamiento, servidores u otros equipamientos (EDA).

![[DC Arquitectura modelo.png]]

- **Foto Izquierda**: DataCenter típico, cámara 0 (donde llegan todos los operadores). Trae la red del operador, llegan las fibras (quizás de forma subterránea), y se entra a la **acometida** del edificio (dentro de la fachada). De ahí se entra al ER.
- **Foto Derecha**: en un mismo recinto se tienen distintas áreas de distribuciones (principales, intermedias y de zona).
#### Esquema de distribución típico

![[DC Esquema de distribucion tipico.png]]

- Hay dos caminos: hay 2 proveedores de servicios distintos. Cada uno viene por su propio camino: nos da **redundancia**. 
- Este DataCenter tiene la conectividad con mayor tolerancia a fallos, ya que tiene más de un camino posible.

> [!NOTE]
> Cuantos más proveedores tiene el DataCenter, más capacidad de resiliencia tiene, es decir, de **adaptarse a fallos críticos**. También le dan mayor conectividad.
#### Diagrama de infraestructura

![[DC Diagrama de infraestructura.png]]

- Aparecen los tipos de gabinetes
- Las guías amarillas son canalizaciones de fibra óptica.
- Se generan ángulos para no lastimar las microfibras.
- Los rojos se conectan con el proveedor A, los azules con el proveedor B.
- El Cross-connect hace de cruce entre la distribución de fibra interna y externa.
### Aspectos de Diseño

- **Uso eficiente de energía**: tanta energía de red como sea posible usar para **alimentar el equipamiento**, en lugar de desperdiciarse por hardware ineficiente.
- **Escabilidad**: construcción flexible para ser **actualizada** en soporte de nuevos clientes y salas de datos. 
- **Cadena de suministro**: entrega de servicios optimizada para **agilizar las instalaciones en campo**.

### Escalas de un Data Center

![[DC Escalas.png]]

- Los de **menor latencia** son los DataCenters más pequeños (los de la izquierda). Posibilidad de estar más cerca del usuario final. Consumo de energía bajo, cobertura limitada.
- Los que van creciendo (pequeños pero no tanto), tienen hasta 2 racks.
- Los más grandes (hiperescala), están más alejados. Tienen más estabilidad y capacidad.
### Clasificación según Tiers

**Uptime Institute** certifica DataCenters mediante las **habilidades de diseño, construcción y oprtaciones** de los centros de datos.
#### Tier I (Capacidades básicas)

- Paradas de todo el sitio para mantenimientos/reparaciones. 
- Fallas de distribución de energía: afectan a los servicios.
#### Tier II (Capacidades redundantes)

- Paradas de todo el sitio para mantenimientos/reparaciones.
- Fallas de capacidad (servicios): podrían afectar o no a servicios.
- Fallas de distribución de energía: afectan a los servicios.

**Componentes redundantes**:
- Única vía de distribución no redundante.
- Infraestructura susceptible a interrupciones planeadas o no planeadas.
- Requiere Generador y UPS redundantes.

**Protección mínima para eventos críticos**:
- Puertas de seguridad.
- Redundancia de fuentes y procesadores para equipos críticos de telecomunicaciones.
- Capacidad de enfriamento combinada, temperatura y humedad todo el tiempo.

**Componentes eléctricos**:
- UPS redundante N+1. Un generador redundante.
- Emergency Power Off System (EPO).

#### Tier III (Mantenimiento simultáneo)

- Todos los componentes de capacidad y rutas de distribución **se pueden eliminar sin afectar a las operaciones** (de forma planificada para mantenimiento/reemplazo).
- Sitio aún expuesto a fallas del equipo u erorres del operador.

**Componentes redundantes**:
- Vías de distribución redundantes (una activa y otras pasivas).
- Posibilidad de remover componentes en eventos planeados **sin generar interrupciones de sistema**.

**Protección**:
- Susceptible a actividades no planeadas.
- Riesgo de interrupción durante mantenimientos.
- Acceso controlado, muros exteriores sin ventanas.
- Seguridad perimetral con CCTV.
- 2 proveedores de telecomunicaiones.
- 2 cuartos de entrada de servicio.
- Rutas y áreas redundantes.

**Componentes eléctricos**:
- Redundancia N+1 en generador, UPS y sistema de distribución.
- 2 vías de distribución (activa y alterna).
- Sistema de puesta a tierra y de protección de iluminación.
- Sistema de control y monitoreo.
- Generador con combustible para poder operar por 72 horas.

#### Tier IV (Tolerante a fallas)

- Como el Tier III, pero con otro proveedor de energía adicional.
- Fallas individuales o interrupciones de rutas de distribución **no afectan las operaciones**.
- Se puede mantener simultáneamente.
- En Argentina hay problema con ellos: no suele haber 2 proveedores en el mismo lugar (donde está EDENOR, no está EDESUR).

### Conectividad de FO - Aplicaciones

![[DC Conectividad de FO aplicaciones.png]]

- **Aplicación**: ancho de banda y cómo se entrega en un SFP (Small Form-Factor Puggable), el cual se conecta a un switch (o al router).
- Hay de 25Gb hasta 400GB. 
- Si es monomodo, se usa para distancias largas. Si es multimodo, son para pequeñas distancias.
- Aplicación paralela: cables MPO y MTP.
### Arquitecturas de Conectividad

![[DC Switching vs Spine Leaf.png]]
#### Modelo Tradicional de Switching

- Tráfico de Norte a Sur.
- Predominaba hace muchos años atrás.
- Tiene 3 niveles: Core, Aggregation y Access.
- Hay 2 switches de Core (ocupan todo un rack).
- Los switches de Acceso conectan a los dispositivos finales.

**Ventajas**:
- Diseño previsible y escalable
**Desventajas**:
- Poco eficiente para aplicaciones de **baja latencia** o virtualizadas.

#### Modelo Spine & Leaf

- Tráfico de Este a Oeste.
- También tienen switches de Core.
- La **capa Spine** interconecta todos los Leaf, formando una malla sin conexiones directas entre dispositivos de la misma capa.
- La **capa Leaf** se conecta a servidores, almacenamiento (storage) y dispositivos finales.

**Ventajas**:
- Mejoras de diseño.
- Mejor latencia.
- Más resiliente.
- Más ancho de banda.