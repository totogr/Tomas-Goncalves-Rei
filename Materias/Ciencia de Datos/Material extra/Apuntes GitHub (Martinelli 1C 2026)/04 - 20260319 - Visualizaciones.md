# Clase 4 - Visualizaciones

Una visualización exitosa tiene 4 elementos:
- **Datos**: información usada
- **Objetivo o función**: ¿para qué es útil?
- **Metáfora visual**: belleza, estructura, apariencia, elementos metafóricos
- **Historia o concepto**: ¿qué tan interesante es? ¿Qué cuenta?

## Números útiles

- **Media**: promedio.
- **Mediana**: valor que está en la mitad de la población (la divide a la mitad en cantidad).
- **Cuartil**: valores límite que dejan al 25% de población entre ellos.
- **Rango intercuartílico**: rango entre el cuartil 1 y el cuartil 3.
## Plots

- Objetivos y datos bien definidos.
- Pueden o no contar una historia.
- Sus metáforas visuales son muy simples.
- Útiles en visualizaciones científicas y de análisis exploratorio.

### Distribución continua

La variable que se quiere ver es **continua**, o al menos usualmente no puede dividirse en partes.

#### Histograma

- En el eje X va el **soporte continuo** (la variable continua en cuestión).
- En el eje Y va **la cantidad** (o similar). 

Se pueden controlar los **bins**, es decir, la resolución del histograma. Cuantos más bins, más fluido se podrá visualizar.

![[histograma.png]]

#### Density plot

- En el eje X va el **soporte continuo**.
- En el eje Y va la **densidad** (de 0 a 1).

Es poco interpretable, ya que no conocemos las cantidades, pero suma prolijidad. Podemos **quitar la densidad** y solo mostrar la curva.

![[density-plot.png]]

También deberíamos sacarle el espacio de los **negativos** (suavización de curva) si es que no tiene sentido.

#### Box plot

Exhibe los cuartiles en un gráfico. 

![[box-plot.png]]

- La línea central es el cuartil 2 (**mediana**). 
- Los extremos del *box* son los cuartiles 1 y 3 (limitan el **rango intercuartílico**).
- Los "bigotes" externos son el **mínimo** y **máximo**.
- El resto que se sale de los valores comunes son los **outliers**.

#### Violin plot

Density plot espejado que suaviza los bordes para lograr densidad. No sabe que no tiene sentido agregar valores menores a 0.

![[violin-plot.png]]

### Distribución discreta

Ver cómo se distribuye una variable que toma **pocos y finitos valores**.

#### Bar plot

- **Eje X**: una barra para cada cada categoría.
- **Eje Y**: cantidades.

- Se pueden agregar los valores exactos por encima de cada barra.
- Se pueden usar divisores en las barras, por colores, para agregar más de una variable discreta al análisis.

![[bar-plot.png]]

#### Stacked bar plot

- **Eje X**: variable discreta
- **Eje Y**: varias barras que van sumando la cantidad total de distintas opciones de resultados.

![[stacked-bar-plot.png]]
#### Treemap

- Distintas **áreas que ocupan un rectángulo**.
- Se pueden usar para definir porcentajes de ocupación de distintas opciones para una variable discreta.
- En cada sub-rectángulo se suele poder ampliar y seguir viendo *treemaps* internos.

![[treemap.png]]

### Plots de relación

Mostrar relación entre **dos o más variables**.

#### Scatter plot

- Dos variables, una en cada eje.
- Se coloca un punto por cada par encontrado, y se busca una correlación.
- Ambos ejes son bien continuos o al menos tienen buena granularidad

![[scatter-plot.png]]

#### Regression plot

Igual a un Scatter Plot, pero también nos permite dibujar una **regresión** en el gráfico, para ver qué tendencia tienen los puntos.

![[regression-plot.png]]

#### Heatmap

- **Eje X**: variable discreta
- **Eje Y**: otra variable discreta

Se definen cuadrados en el Plot, y se dibuja con un **color** (siguiendo una escala determinada) **según la cantidad correspondiente** a ese lugar.

![[heatmap.png]]
### Series de tiempo

En general aparecerá **el tiempo en el eje X**.  

#### Lineplot

- Dibuja una línea uniendo los datos a través del tiempo. 
- **Extiende la línea** para hacerla continua (aunque no haya datos en medio).

![[lineplot.png]]

#### Lollipop

- Se usa para series de tiempo que son discretas (cada instante temporal no tiene continuidad).
- Resuelve el problema anterior de la extensión de la línea continua.

![[lollipop-plot.png]]

### Otros plots

- Radar count
- Word cloud
- Sankey diagram

### Cosas que se corrigen en las visualizaciones

- Debe explicarse por sí mismo
- Interesante y claro
- Elegir buenos colores