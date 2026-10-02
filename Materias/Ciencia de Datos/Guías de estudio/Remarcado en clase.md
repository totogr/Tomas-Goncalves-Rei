# 🎯 Remarcado en clase — Ciencia de Datos

> Lo que el profe **marcó como pregunta de examen**, lo que dijo que había que **dominar** y los ejemplos que usó para explicar, sacado de las transcripciones de las teóricas.
> Solo está lo que es **correcto y aporta**: si en la grabación algo se explicó mal o quedó confuso, acá figura la versión correcta (ver la sección [Ojo al repasar la grabación](#-ojo-al-repasar-la-grabación)).
> Es la **base para armar los resúmenes del parcial**, junto con las guías de cada tema.

**Leyenda:** 🔴 = el profe dijo explícitamente "pregunta de examen / de parcial" · 🟠 = dijo que hay que dominarlo o lo repitió varias veces · 🟢 = ejemplo o detalle que usó para explicar y conviene saber.

---

## 📅 Clases cubiertas

| Fecha | Tema | Guías relacionadas |
| --- | --- | --- |
| 18/08 | Intro a la materia · Visualización y falacias | [01](01%20-%20Introducci%C3%B3n%20a%20la%20materia.md) · [02](02%20-%20Visualizaci%C3%B3n%20de%20datos.md) |
| 25/08 | Intro a la ciencia de datos · Regresión lineal · Métricas · Clustering | [03](03%20-%20Introducci%C3%B3n%20a%20la%20ciencia%20de%20datos.md) · [04](04%20-%20M%C3%A9tricas.md) · [08](08%20-%20Clasificaci%C3%B3n%20y%20regresi%C3%B3n%20cl%C3%A1sicos%20%28K-NN%2C%20SVM%2C%20lineal%2C%20log%C3%ADstica%29.md) · [13](13%20-%20M%C3%A9todos%20de%20agrupamiento%20%28Clustering%29.md) |
| 01/09 | Descenso por gradiente · Umbral · ROC · Repaso de métricas | [04](04%20-%20M%C3%A9tricas.md) · [08](08%20-%20Clasificaci%C3%B3n%20y%20regresi%C3%B3n%20cl%C3%A1sicos%20%28K-NN%2C%20SVM%2C%20lineal%2C%20log%C3%ADstica%29.md) |
| 15/09 | Árboles (ID3, Gini, C4.5) · Random Forest · Intro a ensambles | [06](06%20-%20%C3%81rboles%20-%20ID3%2C%20C4.5%20y%20Random%20Forest.md) · [09](09%20-%20Ensamble%20de%20modelos%20%28AdaBoost%2C%20Gradient%20Boosting%2C%20XGBoost%29.md) |
| 29/09 | Redes neuronales · Regularización · Optimizadores · Repaso KNN/SVM | [10](10%20-%20Redes%20neuronales%20%28perceptr%C3%B3n%2C%20MLP%2C%20backpropagation%2C%20SOM%29.md) · [08](08%20-%20Clasificaci%C3%B3n%20y%20regresi%C3%B3n%20cl%C3%A1sicos%20%28K-NN%2C%20SVM%2C%20lineal%2C%20log%C3%ADstica%29.md) |

_Faltan transcripciones del 08/09 y del 22/09 — cuando lleguen, se suman acá._

---

## 📝 Cómo se aprueba (clase 18/08)

- **2 TPs grupales obligatorios**: el TP1 se **presenta en forma presencial**; el TP2 es una **competencia privada en Kaggle**.
- **Parcial presencial y escrito, en hoja** (no es digital), con **2 recuperatorios**.
- **Promoción** (evitás el final escrito con un **coloquio oral grupal** sobre temas de la segunda parte): aprobar los dos TPs y el **parcial con 7 o más en la primera oportunidad**.
- El profe insistió en que **todo lo que se evalúa es contenido dado en clase**.
- Bibliografía principal: *Hands-On Machine Learning* (Géron) — en castellano *Aprende Machine Learning con Scikit-Learn, Keras y TensorFlow* —, y *Dive into Deep Learning* para la última parte (Transformers y LLMs).

---

## 🔴 Preguntas que el profe marcó como de examen

### 1. ¿Para qué graficamos / visualizamos datos? (18/08)
Tres respuestas:
1. **Entender** los datos de forma eficiente.
2. **Encontrar patrones o relaciones** entre variables.
3. **Comunicar** de forma concisa y clara lo que vemos a otra persona.

Además, la visualización es **parte del análisis**: permite chequear supuestos de un método, ver outliers, ver si hay linealidad y comparar lo predicho contra lo observado (residuos). Ejemplo canónico: **Datasaurus** (misma media y desvío, gráficos completamente distintos).

### 2. ¿La regresión logística es un método de regresión o de clasificación? (25/08)
**De clasificación.** Toma la idea de la regresión (ajustar una curva) pero la usa para **partir el espacio en dos**: usa la **sigmoide** `1 / (1 + e^(−x))`, que devuelve algo parecido a una **probabilidad** entre 0 y 1; con un umbral se decide la clase. Entrenarla = encontrar los parámetros (β₀, β₁) que mejor ubican la sigmoide sobre los datos.

> 🟢 Ejemplo de clase: préstamo hipotecario según **puntaje crediticio**. Debajo de ~500 nunca se otorga, entre 500 y 800 es "50/50", arriba de 800 casi siempre se otorga → la sigmoide modela esa transición.

### 3. Regla del codo, Silhouette y estadístico de Hopkins (25/08)
"Acuérdense, esto es pregunta de examen."

| Técnica | Qué responde | Idea |
| --- | --- | --- |
| **Hopkins** | ¿Tiene sentido hacer clustering? (¿hay tendencia a agruparse?) | Compara distancias al vecino más cercano en los datos reales vs. en un conjunto **uniforme generado al azar** en el mismo rango. ≈ 0.5 → datos aleatorios; **> 0.75 → hay tendencia a clusters** (con 90% de confianza). |
| **Regla del codo** | ¿Cuántos clusters (K)? | Graficar la distancia promedio de los puntos a su centroide para K = 1..10; elegir el K donde la caída deja de ser abrupta (el "codo"). **Más rápida** de calcular. |
| **Silhouette** | ¿Cuántos clusters (K)? / ¿qué puntos están mal asignados? | Por punto: `s = (b − a) / max(a, b)`, con *a* = distancia promedio a los de su cluster y *b* = distancia promedio a los del cluster vecino más cercano. Va de −1 a 1; **negativo = punto mal asignado**. **Más costosa** pero más precisa. |

**Cuándo usar cada una** (lo respondió a una pregunta): son **complementarias**. Primero **Hopkins** para decidir si vale la pena hacer clustering; si da que sí, **codo o Silhouette** para elegir K.

### 4. Leer una matriz de confusión: ¿modelo bueno o malo? (01/09)
"Este tipo de preguntas a veces tomamos en los exámenes." Tenés que poder mirar una matriz y decir si el modelo clasifica bien:
- Un **buen modelo** tiene los valores **altos en la diagonal** (TP y TN, los aciertos) y valores **bajos fuera de la diagonal** (FP y FN).
- Si los **FN** son más altos que los **TP**, el modelo no está detectando la clase positiva → está mal entrenado.
- Vale igual para la **matriz multiclase** (N×N): los aciertos están en la diagonal; todo lo demás es error. Para una clase dada, su **columna** (fuera de la diagonal) son sus falsos negativos y su **fila** son sus falsos positivos (si las filas son lo predicho).

### 5. El perceptrón y las compuertas lógicas (29/09)
"Presten atención, esto es pregunta de examen."
- Un **perceptrón** (una sola neurona) **puede modelar AND** — y también **OR** — porque son **linealmente separables**: alcanza con **una recta** que parta el plano.
- **No puede modelar XOR**: los puntos de cada clase están en diagonales opuestas y **ninguna recta** los separa; harían falta dos.
- La solución es **agregar capas** (neuronas cuya salida es la entrada de otras) → **perceptrón multicapa (MLP)**, que puede partir el espacio de forma arbitrariamente compleja.
- El modelo entrenado **es solo el vector/matriz de pesos** (w₁, w₂, b); predecir es un producto escalar + la función de activación.

### 6. Métodos de regularización en redes neuronales (29/09)
"Pregunta de examen: ¿qué métodos de regularización tenemos para una red neuronal?"

| Método | Qué hace |
| --- | --- |
| **L1** | Suma a la función de pérdida `λ · Σ |wᵢ|` → penaliza pesos grandes |
| **L2** | Suma a la función de pérdida `λ · Σ wᵢ²` → penaliza pesos grandes |
| **Dropout** | Apaga neuronas **al azar durante el entrenamiento** → la red no depende de pocas neuronas, se vuelve más robusta |
| **Early stopping** | Corta el entrenamiento cuando el error en **validación/test empieza a subir** aunque en train siga bajando |
| **Data augmentation** | Genera más datos a partir de los existentes (ej. rotar imágenes, agregar ruido, cambiar iluminación) |

La idea de fondo: el **overfitting** aparece cuando el modelo es muy complejo para los datos que hay, o hay pocos datos para el modelo. Se combate **simplificando/penalizando el modelo** (L1, L2, dropout, early stopping) o **sumando datos** (data augmentation).

---

## 🟠 Lo que dijo que hay que dominar

### Los tres tipos de problemas (25/08 y 01/09)
"Estos conceptos son bastante fundamentales de la materia y los tienen que dominar bien."
- El **tipo de la variable dependiente** define el problema: cualitativa → **clasificación**; cuantitativa (rango continuo) → **regresión**; sin variable a predecir → **clustering**.
- **Supervisados** (clasificación y regresión): el dataset viene **etiquetado** con la salida esperada. **No supervisados** (clustering, SOM, reducción de dimensionalidad): sin etiqueta.
- En clasificación las **clases son finitas y conocidas de antemano**: el modelo nunca va a devolver una clase con la que no se entrenó (si MNIST tiene dígitos 0–9, un garabato igual va a salir como algún dígito).

### Descenso por gradiente (01/09)
Es el método general con el que se entrena casi todo, incluidas las redes neuronales (backpropagation lo usa adentro).
1. Arrancar con **parámetros al azar**.
2. Calcular la **función de pérdida** (ej. suma de residuos al cuadrado).
3. Calcular la **derivada/gradiente** en el punto actual: indica hacia dónde **crece** el error.
4. Moverse en el **sentido opuesto**, un paso de tamaño `learning rate × gradiente`.
5. Repetir hasta que el gradiente ≈ 0, el error deje de bajar, o se llegue a un máximo de pasos.

> 🟢 Ejemplo de clase: encontrar la ordenada al origen de una recta. Arranca en 0, la derivada da −5.7; con learning rate 0.1 el paso es 0.57 → nueva ordenada = 0.57. Se repite hasta llegar al mínimo (≈ 1).

Por qué existe si para regresión lineal ya hay fórmula cerrada (mínimos cuadrados): la solución analítica requiere **invertir una matriz**, que es muy caro con muchas variables; y en redes neuronales **no hay fórmula analítica**, así que no queda otra.

### Precisión vs. recall y el umbral (25/08 y 01/09)
- **Precisión** = TP / (TP + FP): "de lo que dije positivo, ¿cuánto lo era?".
- **Recall** = TP / (TP + FN): "de todo lo positivo, ¿cuánto detecté?".
- **Subir el umbral** → el modelo solo dice "positivo" cuando está muy seguro → **sube la precisión, baja el recall**. **Bajarlo** → al revés. "Sube una métrica, baja la otra; es muy difícil mantener las dos arriba."
- El **punto donde se cruzan** las curvas de precisión y recall en función del umbral es un buen umbral si querés equilibrar ambas.
- **Cuándo priorizar cada una:** detección de **tumores** → **recall** (que no se escape ninguno, aunque traiga falsos positivos que después descarta un médico); reconocer **caras para un álbum de fotos** → **precisión** (solo etiquetar si está seguro).
- **F1** = `2·P·R / (P + R)` combina las dos con el mismo peso; sirve para comparar modelos cuando uno tiene mejor precisión y otro mejor recall.
- **AUC-ROC**: clasificador perfecto = 1, azaroso = 0.5.

### Clases desbalanceadas (25/08)
- Si en un millón de transacciones solo 1000 son fraude, un modelo que **siempre dice "genuina"** tiene **accuracy ≈ 99.9%** y no sirve para nada.
- Solución: **balancear el entrenamiento**:
  - **Undersampling**: sacar ejemplos de la clase mayoritaria.
  - **Oversampling**: agregar ejemplos de la clase minoritaria (no siempre se puede inventar datos; en imágenes sí, rotándolas o agregándoles ruido).

### Train / validación / test y over/underfitting (25/08)
- Siempre se **parte el dataset**: típicamente **80/20**, **75/25** o **2/3–1/3** (train/test). Variante: **50% train, 25% validación, 25% test**.
- **Cross-validation** con 5 folds: en cada ronda 4/5 entrena y 1/5 valida, rotando el fold.
- Las métricas se miden **siempre sobre validación o test**, nunca sobre lo que el modelo ya vio.
- **Overfitting**: "se aprendió los datos de memoria, hasta el ruido" → bien en train, mal en test. Analogía del profe: **estudiar de memoria parciales viejos** y desaprobar cuando las preguntas cambian.
- **Underfitting**: el modelo es **demasiado simple** para la complejidad de los datos (ej. una recta para datos curvos) → mal en todos lados.

### Sesgo y varianza (15/09)
- **Sesgo alto** → modelo muy simple → **underfitting** → errores en train, validación y test.
- **Varianza alta** → pequeños cambios en el dataset generan grandes cambios en la salida → **overfitting** → bien en train, mal en test.
- Objetivo: **bajo sesgo y baja varianza**.

---

## 🟢 Detalles y ejemplos por tema

### Ciencia de datos en general (18/08 y 25/08)
- **Data engineer vs. data scientist**: el ingeniero **mantiene y extrae** los datos (arquitecturas big data, bases no relacionales, distribuidas); el científico viene después y **los analiza y construye modelos**.
- **Minería de datos** = encontrar **patrones ocultos** sin una pregunta previa. Ejemplo clásico: **pañales y cerveza** (en un supermercado, quien compraba pañales solía comprar cerveza → pusieron la cerveza al lado).
- **ML vs. programación tradicional** (ejemplo del **filtro de spam**): en vez de escribir reglas a mano, se entrena un modelo con mails **etiquetados**; si aparece spam nuevo, se **reentrena** en vez de reescribir reglas.
- **Origen de los modelos**: algunos vienen de la **matemática** previa a la computación (regresión lineal — mínimos cuadrados, Gauss 1805 —, análisis discriminante, PCA); otros **mezclan** matemática e informática (ID3, K-Means, Naive Bayes); y otros son **propios de la informática** (redes neuronales, SVM).
- **¿Por qué "regresión"?** Por **Francis Galton** (fines del siglo XIX): observó que padres muy altos tienen hijos algo más bajos y viceversa — la "**regresión a la media**".

### Visualización (18/08)
- **Histograma**: depende mucho de la **cantidad de bins** — pocos esconden la forma (pueden ocultar una distribución bimodal), demasiados la rompen. Bins con **anchos redondos** (2, 5, 10) se leen mejor.
- **Arrancar los ejes en 0** siempre que se pueda.
- **Density plot**: pierde la **cantidad absoluta** (no sabés si se encuestaron 100 o 5000 personas) y por suavizar puede mostrar valores imposibles (ej. salarios negativos).
- **Box plot**: mediana, Q1, Q3, rango intercuartílico (50% central), mínimo/máximo y outliers por fuera. **Q2 = mediana** (no la media).
- **Scatter plot** con una variable **discreta** queda confuso → mejor un **box plot por categoría**.
- **Heatmap**: el **color** funciona como una dimensión extra.
- **Falacias**: **paradoja de Simpson** (cálculos renales: una técnica parecía mejor en el total, pero al separar por tamaño de piedra era al revés, porque los casos fáciles iban a una y los difíciles a la otra) y **sesgo de supervivencia** (aviones de la Segunda Guerra: hay que reforzar donde **no** hay impactos, porque los que recibieron ahí no volvieron). Para evitar conclusiones sesgadas: **asignación aleatoria / experimentos A-B** (ej. el botón de donar de Wikipedia: 50% del tráfico a cada color).

### Estadística básica (18/08 y 25/08)
- **Media = promedio** (son lo mismo). **Mediana** = valor que parte el conjunto en dos mitades.
- **Varianza muestral** se divide por **n − 1** porque casi nunca tenemos la población completa, sino una muestra.
- **Pearson** = covarianza / (desvío_x · desvío_y); solo mide relación **lineal**.
- **Correlación no implica causalidad**: puede ser casualidad o una **tercera variable** (incluso fuera del dataset) que mueve a ambas.

### Outliers (25/08)
Antes de sacar un outlier, preguntarse:
1. ¿Es un **dato genuino** o un **error de carga** (ej. 18 m de altura por una coma olvidada, o 1,80 m con 2 años de edad)?
2. ¿Me interesan esos casos? A veces **son justo lo que quiero detectar** (ej. un fraude es casi un outlier entre millones de transacciones).
3. ¿Mi modelo los **tolera**? Algunos son muy sensibles (KNN, el clasificador de margen máximo), otros los ignoran mejor (ensambles de bagging).

### Métricas de regresión (25/08)
- **Residuo** = valor real − valor predicho.
- No alcanza con **sumar** los errores: un dataset más grande suma más error aunque cada error sea chico → hay que **promediar**.
- Se **elevan al cuadrado** para que errores positivos y negativos no se cancelen.
- **MSE** (error cuadrático medio), **RMSE** (raíz del MSE, en las mismas unidades que la variable), **MAE** (error absoluto medio). Para **comparar modelos o épocas** alcanza con el MSE: sacar la raíz no cambia cuál es mejor.

### Árboles (15/09)
- **ID3** = *Iterative Dichotomiser 3* (Ross Quinlan). Cada nodo interno pregunta por un atributo, cada rama es un valor y cada hoja es una clase.
- **Entropía**: 0 si la muestra es homogénea; con 2 clases, máxima (= 1) cuando es 50/50. Ejemplo: un dado tiene entropía log₂6 ≈ 2.58.
- **Gini**: `1 − Σ pᵢ²`. Scikit-learn la usa **por defecto** porque es **más barata de calcular** que la entropía y lleva a árboles muy parecidos. Se puede elegir entropía con el hiperparámetro `criterion`.
- Con **nulos**: se omiten en el cálculo; ojo con un atributo que parece clasificar perfecto pero tiene muchísimos nulos.
- **C4.5** mejora ID3: soporta **valores faltantes**, **atributos numéricos** (busca un umbral donde cambia la clase y elige el de mayor ganancia) y **poda** (saca nodos de abajo hacia arriba mientras el error en test no empeore).

### Random Forest y ensambles (15/09)
- **"Muchos estimadores mediocres promediados pueden ser muy buenos"** — la **sabiduría de las multitudes** (Galton, 1906: el promedio de las estimaciones de muchas personas sobre el peso de una vaca fue muy preciso).
- **Bagging**: subconjuntos al azar (≈ 2/3 de las filas, ≈ √columnas) → un árbol por subconjunto → **votación**. Reduce la **varianza** y es **robusto a outliers** (no aparecen en todos los subconjuntos). Puede mejorar con **voto ponderado** (cada modelo vota con su nivel de confianza).
- **Cuándo Random Forest no sirve**: si hay **un atributo predictor muy fuerte**, todos los árboles lo eligen como raíz y quedan casi iguales → votan lo mismo. Ahí alcanza con un árbol común.
- **Boosting**: modelos en **secuencia**, cada uno da **más peso a los errores** del anterior; al final, voto ponderado. Casos de éxito: **AdaBoost, Gradient Boosting, XGBoost**.
- **XGBoost superó a Random Forest** en desempeño, pero es más costoso de entrenar y **puede sobreajustar**.

### Redes neuronales (29/09)
- La **función escalón no se usa** para entrenar porque **no es derivable** → no se puede aplicar descenso por gradiente. Se usan funciones derivables como la **sigmoide** o la **ReLU**.
- **Backpropagation** (lo que hay que saber): es el **descenso por gradiente aplicado a todos los pesos** de la red, actualizando **primero los de la última capa y después hacia atrás**, porque el único valor esperado conocido es el de la salida. Es la única forma de entrenar estas redes, y es costosa (cada "parámetro" de un LLM es un peso entrenado así).
- **SOM / Kohonen**: red **no supervisada**, parecida a K-Means. Cada neurona de salida tiene pesos; gana la de **menor distancia euclídea** a la entrada y actualiza a sus **vecinas dentro de un radio** que se va achicando. Ejemplo: mapa de países agrupados por sus características.
- **Arquitectura**: la capa de **entrada** = cantidad de features (MNIST 28×28 = **784**; Iris = **4**); la de **salida** = cantidad de clases (MNIST **10**; Iris **3**). Las **ocultas** se eligen por **prueba y error** (práctica habitual: forma de pirámide, ej. 300 → 200 → 100).
- **Learning rate** muy grande → **oscila** sin converger; muy chico → tarda muchísimo.
- **Épocas** = ciclos de entrenamiento sobre todo el dataset. **Batch size** = cuántos ejemplos toma en cada paso de actualización.
- **Optimizadores** (mejoras de backpropagation para converger más rápido): SGD (base) → **Momentum** → **Nesterov** → AdaGrad → **RMSProp** → **Adam** (Momentum + RMSProp, el más usado) → Nadam. Detalle en la guía [10](10%20-%20Redes%20neuronales%20%28perceptr%C3%B3n%2C%20MLP%2C%20backpropagation%2C%20SOM%29.md).
- La **biología de la neurona** (axón, dendritas, neurotransmisores) la contó como contexto: **no entra en el examen**.

### KNN y SVM — repaso (29/09)
- **KNN**: hiperparámetros **K** (por defecto 5 en scikit-learn) y **distancia** (en general euclídea). Sensible a **clases desbalanceadas** y a **outliers**.
- **SVM** en tres niveles: **clasificador de margen máximo** (muy sensible a outliers) → **clasificador de margen blando** (tolera algunos errores dentro del margen) → **SVM con kernel** (lleva los datos a una dimensión mayor donde sí son linealmente separables, ej. agregando x²).
- **Kernel polinómico** (grado 2, 3…) y **kernel radial (RBF)**: trabaja en dimensiones infinitas y se comporta parecido a un KNN.
- **Kernel trick**: no transforma realmente los datos; calcula las **relaciones par a par** entre observaciones como si estuvieran en el espacio mayor → mucho más barato.

---

## ⚠️ Ojo al repasar la grabación

Algunas cosas se dijeron mal al pasar o la transcripción automática las deformó. Para el examen, quedate con esta versión:

| Si en la grabación escuchás… | Lo correcto es… |
| --- | --- |
| Softmax "para n clases simultáneas" (varias etiquetas a la vez) | **Softmax** = clasificación **multiclase excluyente** (una sola clase por ejemplo; las probabilidades suman 1). Para **multi-label** (varias clases a la vez) va **sigmoide en cada neurona** de salida. |
| Para regresión "conviene sigmoide" en la salida | En regresión la salida es **lineal** (o ReLU si el valor no puede ser negativo). La sigmoide limita la salida a (0, 1). |
| "La peor precisión posible en un clasificador binario es 0.5" | El **0.5** es la referencia de un clasificador al azar para la **accuracy** (con clases balanceadas) y para el **AUC**. La precisión puede ser menor. |
| Los árboles de scikit-learn "son C4.5" | Scikit-learn implementa **CART** (árboles binarios, Gini por defecto), que comparte la base de C4.5 pero no es igual. |
| "El MAE es más costoso y el MSE más barato" | Lo importante no es el costo: **MSE castiga más los errores grandes** (por el cuadrado) y es derivable en todos lados; **MAE es más robusto a outliers**. |
| MNIST: "0 es blanco y 255 negro" | En MNIST, **0 = negro (fondo)** y **255 = blanco (trazo)** — el propio profe lo corrigió en clase. |
| "De eso se trata KNN" al explicar el kernel | Estaba hablando de **SVM** (llevar los datos a una dimensión mayor). |

---

<sub>⚙️ Armado a partir de las transcripciones de las teóricas del 18/08, 25/08, 01/09, 15/09 y 29/09 (Rodríguez). Las transcripciones originales no se suben al repo (traen datos de los asistentes y errores del reconocimiento de voz).</sub>
