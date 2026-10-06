## Clase 17 - Reducción de Dimensiones

### Herramientas y Algoritmos

#### 1. SVD (Descomposición en Valores Singulares)

Es una técnica de álgebra lineal que descompone una matriz original en matrices de menor dimensión ($U$, $\Sigma$ y $V$) mediante una diagonalización.

- **Funcionamiento:** Se basa en obtener los **autovalores** de la matriz, ordenarlos de forma decreciente y quedarse con los $k$ más grandes.
- **Concepto de Energía:** Permite medir cuánta información se retiene. Por ejemplo, es posible reducir una imagen a una fracción de sus dimensiones originales manteniendo casi el 100% de su "energía" o nitidez.

#### 2. PCA (Análisis de Componentes Principales)

Conceptualmente busca lo mismo que SVD pero con un enfoque estadístico.

- **Funcionamiento:** Intenta encontrar los componentes que **maximicen la varianza global** de los datos para poder diferenciar mejor los puntos en un espacio reducido.
- **Ventaja:** Permite usar métodos como `fit_transform` y `transform`, lo que significa que una vez entrenado con un set de datos, puede transformar puntos nuevos rápidamente.

#### 3. MDS (Escalamiento Multidimensional)

A diferencia de los anteriores, no trabaja directamente con los valores de los puntos, sino con sus **distancias**.

- **Funcionamiento:** Construye una matriz de distancias entre todos los puntos y busca posicionarlos en un nuevo espacio de baja dimensión (2D o 3D) de forma que se respeten esas distancias originales.
- **Limitación:** No soporta un método `transform` sencillo; si llega un dato nuevo, generalmente hay que recalcular todo el MDS.

#### 4. Isomap

Es un algoritmo que combina la teoría de grafos con MDS.

- **Funcionamiento:** Construye un grafo conectando cada punto con sus **k-vecinos más cercanos**. Luego, calcula la distancia entre cualquier par de puntos siguiendo los caminos del grafo y aplica MDS sobre esa nueva matriz de distancias.
- **Debilidad:** Respeta muy bien las distancias locales (puntos cercanos), pero puede distorsionar significativamente la estructura global de los datos.

#### 5. t-SNE

Se utiliza principalmente para representar datos en 2 o 3 dimensiones, a menudo aplicándose después de un PCA para ganar velocidad.

- **Funcionamiento:** Calcula la **probabilidad** de que un punto sea vecino de otro. Utiliza la divergencia de Kullback-Leibler para minimizar la diferencia entre las probabilidades en el espacio original y el reducido.
- **Resultado:** Es muy bueno manteniendo grupos o clústers cercanos, pero al igual que Isomap, tiende a fallar en mantener una visión global coherente.

#### 6. UMAP

Una técnica más moderna basada en geometría y topología.

- **Funcionamiento:** Similar a t-SNE en su capacidad de mantener estructuras locales, pero es más eficiente y, a diferencia de t-SNE o Isomap, **soporta la transformación de nuevos puntos** sin recalcular todo.
- **Observación:** Aunque es potente, a veces puede "desarmar" la estructura global (como se vio en el ejemplo de la vaca o el mamut en la clase), separando partes que deberían estar unidas.

#### 7. PaCMAP

Presentado como una mejora reciente a UMAP para resolver problemas de estructura global.

- **Funcionamiento:** Clasifica los puntos en tres tipos: vecinos cercanos, de proximidad media y lejanos. Utiliza una función de pérdida que considera estos tres niveles simultáneamente.
- **Ventaja:** Logra mantener la relación entre puntos cercanos y lejanos mucho mejor que UMAP, preservando la integridad global del objeto (por ejemplo, manteniendo la cabeza de la vaca unida al cuerpo).