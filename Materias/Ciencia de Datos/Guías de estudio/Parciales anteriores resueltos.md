# 📝 Parciales anteriores — resueltos y corregidos

> Todas las preguntas de los parciales de la **cátedra Rodríguez** que conseguimos (2022 a 2025), agrupadas por tema y con **la respuesta correcta**. Los PDFs originales están en [`Material extra/Parciales anteriores`](../Material%20extra/Parciales%20anteriores/).
> Las resoluciones que circulan tienen errores. Acá cada respuesta está revisada, y al final hay una tabla con [los errores que encontramos](#-errores-en-las-resoluciones-que-circulan).
> Va en conjunto con el [Resumen para el parcial](Resumen%20para%20el%20parcial.md).

**Parciales usados:** 29/06/2022 (V/F) · 27/10/2022 · 26/10/2023 · recuperatorio 23/11/2023 · 16/10/2025 · recuperatorio 20/11/2025 · un enunciado sin fecha (02-E) · ejemplos de preguntas tipo parcial.

---

## 📊 Qué toman: frecuencia por tema

| Tema | Veces que apareció | Formato típico |
| --- | --- | --- |
| **Métricas y matriz de confusión** | **7 de 8** | Calcular accuracy / precision / recall / F1 de dos modelos y elegir uno · cuándo maximizar precision o recall |
| **Preguntas sobre el TP1** | **6 de 8** | Outliers que detectaron, técnica de preprocesamiento, cuántos clusters eligieron, qué afectó las métricas |
| **Redes neuronales** | **6 de 8** | Perceptrón (limitación, calcular salidas), backpropagation, optimizadores, regularización, activaciones, SOM |
| **Bagging vs. boosting / ensambles** | **5 de 8** | Diferencias, cómo se ve en Random Forest y XGBoost, voting vs. stacking, homogéneo vs. híbrido |
| **Reducción de dimensionalidad** | **4 de 8** (los 3 últimos años) | Qué técnica usar según el caso (PCA, MDS, ISOMAP, t-SNE), para qué sirve el scree plot, ventajas |
| **Overfitting, underfitting, sesgo y varianza** | **4 de 8** | Explicar, cómo detectarlos, cómo evitarlos, relación con la complejidad |
| **Tipos de problema y de variables** | **4 de 8** | Clasificación vs. regresión vs. agrupamiento, con ejemplos y algoritmos |
| **Outliers** | **4 de 8** | Univariados vs. multivariados, métodos de cada uno, qué hacer con ellos |
| **Árboles de decisión** | **3 de 8** | Poda, atributos numéricos, Gini/entropía, ID3 vs. C4.5, hiperparámetros con GridSearchCV |
| **Feature engineering** | **3 de 8** | Faltantes, normalización, one-hot con muchas categorías, transformación log |
| **KNN y SVM** | 1 de 8 | Clase según K; kernels |
| **K-Means** | 1 de 8 (+ el TP) | V/F básicos |

> 🔑 **Conclusión:** las métricas con cuentas, las preguntas del TP1, redes, ensambles y reducción de dimensionalidad aparecen casi siempre. Desde 2023 el formato es **desarrollo corto + alguna cuenta** (los resultados se pueden dejar como fracción).

---

## 1. Preguntas para desarrollar

### Tipos de problema y variables

**¿Cuál es la diferencia entre clasificación, regresión y agrupamiento? Dé un ejemplo de cada uno y qué algoritmos usaría.** *(2022-10, 2023-10, 2025-10)*
- **Clasificación** (supervisado): predecir una **categoría** de un conjunto **finito y conocido de antemano**. Ej.: si un paciente está sano o enfermo. Algoritmos: regresión logística, árboles, Random Forest, XGBoost, KNN, SVM, redes. Métricas: accuracy, precision, recall, F1, AUC.
- **Regresión** (supervisado): predecir un **valor numérico continuo**. Ej.: el precio de una propiedad. Algoritmos: regresión lineal, árboles y Random Forest de regresión, XGBoost, KNN, redes. Métricas: MSE, RMSE, MAE (y R²).
- **Agrupamiento** (no supervisado): **no hay variable a predecir**; se buscan grupos de observaciones parecidas, sin saber de antemano qué significan. Ej.: segmentar clientes o canciones. Algoritmos: K-Means, SOM, jerárquico, DBSCAN.

**Explique la diferencia entre variables cuantitativas y cualitativas y cómo se relacionan con los tipos de problema.** *(2025-11)*
- **Cuantitativas:** números con los que tiene sentido operar; **discretas** (cantidad de hijos) o **continuas** (precio, altura).
- **Cualitativas:** categorías; **nominales** sin orden (país, color) u **ordinales** con orden (bajo/medio/alto).
- El tipo de la **variable objetivo** define el problema: cualitativa → **clasificación**; cuantitativa → **regresión**; sin variable objetivo → **agrupamiento** (que puede usar variables de los dos tipos, codificando las cualitativas).

### Métricas

**Diferencia entre precision y recall, cómo se calculan y en qué problemas conviene maximizar cada una.** *(2023-10, 2025-10)*
- **Precision = TP / (TP + FP)**: de lo que el modelo dijo positivo, cuánto lo era. Parte de **lo que dice el modelo**.
- **Recall = TP / (TP + FN)**: de todos los positivos reales, cuántos detectó. Parte de **la realidad**.
- **Maximizar recall** cuando un **falso negativo** es caro: detección de enfermedades o tumores, fraude (que no se escape ninguno).
- **Maximizar precision** cuando un **falso positivo** es caro: filtro de spam (no mandar a spam un mail importante), etiquetar caras en un álbum, apuestas.
- Ejemplo numérico (2025-10): el modelo marca 100 mails como spam y 80 lo son → precision = 0,8; si había 200 spams reales y detectó 80 → recall = 0,4.

**¿Son adecuadas para un problema de regresión?** *(2023-10)*
No: precision y recall necesitan clases (aciertos y errores por clase). En regresión se mide **la distancia** entre lo real y lo predicho: **MSE, RMSE, MAE**.

**¿Qué rol cumple la matriz de confusión?** *(2025-10)*
Cruza las clases reales con las predichas y muestra **TP, TN, FP, FN**. De ahí salen accuracy, precision, recall, F1 y especificidad, y permite ver **qué tipo de error** comete el modelo (muchos FN o muchos FP). Un buen modelo tiene la **diagonal alta** y lo de afuera bajo.

### Overfitting, underfitting, sesgo y varianza

**Explique overfitting y underfitting, cómo detectarlos y cómo evitarlos.** *(2023-10, ejemplos)*

| | Qué es | Cómo se detecta | Cómo se evita |
| --- | --- | --- | --- |
| **Overfitting** | El modelo **memorizó** los datos de entrenamiento, incluido el ruido | Métricas **muy buenas en train y mucho peores en validación/test** | Modelo más simple, regularización (poda, L1/L2, dropout, early stopping), más datos, cross-validation para elegir hiperparámetros |
| **Underfitting** | El modelo es **demasiado simple** y no capta los patrones | Métricas **malas en train y en test** | Modelo más complejo, menos regularización, mejores features o más datos |

**Explique el error de sesgo y el overfitting y cómo se relacionan con la complejidad del modelo.** *(2023-11)*
- **Sesgo (bias):** diferencia entre lo que predice el modelo en promedio y el valor real. Los modelos **menos complejos** tienen **más sesgo** → underfitting.
- **Overfitting:** aparece con modelos **más complejos**, que tienen **más varianza** (pequeños cambios en el train cambian mucho el modelo).
- Al subir la complejidad el sesgo baja y la varianza sube; el error de generalización es mínimo en el medio.

### Outliers y feature engineering

**Explique qué es un outlier, la diferencia entre univariados y multivariados y métodos para detectarlos.** *(2023-10)*
- **Outlier:** observación muy alejada del resto. Puede ser un error (de carga, de medición) o un **caso genuino** que interesa (fraude). Hay que **analizar su origen antes de decidir** qué hacer.
- **Univariado:** raro en **una variable** sola (una persona de 300 años). Métodos: **IQR / box plot**, **Z-score**, **Z-score modificado (MAD)**.
- **Multivariado:** cada valor es normal por separado, pero **la combinación** es rara (un nene de 4 años que mide 1,90 m). Métodos: **Mahalanobis**, **LOF**, **Isolation Forest**, scatter plot, clustering.

**Un alumno quiere aplicar one-hot encoding a una variable con 200 categorías. ¿Qué problemas trae y qué alternativas hay?** *(2025-10)*
- **Problemas:** 200 columnas nuevas, casi todas en cero (**matriz dispersa**); más memoria y tiempo; **maldición de la dimensionalidad** y más riesgo de **overfitting** (categorías con muy pocos casos); modelos menos interpretables.
- **Alternativas:** **agrupar** las categorías poco frecuentes en "Otros" o en grupos con sentido; usar otro encoding (**ordinal/label**, **target/mean encoding**, **frecuencia**); **embeddings**.

**El histograma de precios tiene sesgo positivo fuerte. ¿Qué transformación aplicaría?** *(2025-11)*
**Logaritmo** (log o log(1 + x)): comprime la cola larga de la derecha, reduce la asimetría y estabiliza la varianza. Alternativas más suaves: raíz cuadrada; más general: Box-Cox.

### Ensambles

**Diferencias entre bagging y boosting, y cómo se reflejan en Random Forest y XGBoost.** *(2022-10, 2023-10, 2025-10)*

| | Bagging | Boosting |
| --- | --- | --- |
| Entrenamiento | Modelos **independientes, en paralelo** | Modelos **en secuencia**: cada uno corrige los errores del anterior |
| Datos | Cada modelo con una muestra **bootstrap** (con reemplazo) | Más peso a las instancias **mal clasificadas** |
| Decisión | **Votación** (clasificación) o **promedio** (regresión) | **Votación ponderada** / suma de modelos |
| Reduce | **Varianza** | **Sesgo** |
| Ejemplo | **Random Forest:** muchos árboles completos, cada uno con bootstrap de filas y un subconjunto aleatorio de atributos en cada corte | **XGBoost:** cada árbol se ajusta a los **residuos** del ensamble, con learning rate y regularización (λ, γ) |

**Diferencias entre stacking y voting; entre ensambles híbridos y homogéneos.** *(2023-10, 2025-10)*
- **Voting:** varios modelos (pueden ser distintos) y se decide por **mayoría** (hard) o promediando **probabilidades** (soft).
- **Stacking:** las predicciones de los modelos base son la entrada de un **modelo meta** que aprende a combinarlas.
- **Homogéneo:** todos los modelos del mismo tipo (Random Forest, AdaBoost, XGBoost). **Híbrido/heterogéneo:** modelos de distinto tipo (RF + SVM + KNN) combinados con voting, stacking o cascading. Desventaja de los ensambles: más costo computacional y menos interpretabilidad.

### Árboles

**¿Para qué se usa la poda en C4.5?** *(2023-10)*
Para **evitar el overfitting**. Después de construir el árbol completo, se eliminan nodos desde las hojas hacia arriba si el error en un conjunto de prueba **no empeora**.

**¿Puede un árbol manejar atributos numéricos? ¿Cómo?** *(2023-10)*
Sí (C4.5): ordena los valores del atributo A, busca dónde cambia la clase, prueba esos umbrales **C** y se queda con el de **mayor ganancia de información**. Así crea el booleano **A < C**.

**¿Para qué se usa la impureza de Gini? ¿Qué es un nodo puro?** *(2023-10)*
Para elegir el atributo de la **raíz** y de cada **división**: se elige el de **menor Gini ponderada**. Un nodo **puro** tiene todos sus ejemplos de la **misma clase** (Gini = 0); uno impuro mezcla clases.

### Reducción de dimensionalidad

**¿Qué ventajas tiene reducir la dimensionalidad? Nombre tres técnicas.** *(2025-10)*
Reduce el **ruido** y la **colinealidad**, baja el **costo computacional**, disminuye el riesgo de **overfitting** (maldición de la dimensionalidad) y permite **visualizar** en 2D/3D. Técnicas: **PCA, MDS, ISOMAP, t-SNE**.

**¿Para qué sirve el scree plot en PCA?** *(2023-10)*
Grafica la **varianza explicada por cada componente principal**, de mayor a menor. Sirve para **elegir cuántos componentes conservar**: donde la curva hace el **codo**, o al llegar a un porcentaje de varianza acumulada (ej. 90%).

**¿Qué técnica usaría si…** *(2022-10, 2023-10, 2025-11)*

| Caso | Técnica |
| --- | --- |
| Se sospecha que los datos están sobre una **variedad** (superficie curva de menor dimensión) | **ISOMAP** (usa distancias **geodésicas**) |
| Se quiere **preservar las distancias** entre puntos, incluso con métricas **no euclídeas** | **MDS** |
| Se quiere entender **cuán dispersos** están los datos y **sobre qué ejes/variables** | **PCA** |
| Se quiere proyectar a 2D **manteniendo los clusters** | **t-SNE** |

### Redes neuronales

**¿Cuál es la limitación principal del perceptrón simple?** *(2025-10)*
Solo resuelve problemas **linealmente separables** (su frontera es un hiperplano). No puede con **XOR**. Se soluciona agregando capas: **perceptrón multicapa**.

**¿Para qué se usa backpropagation? ¿Qué recurso matemático utiliza?** *(2025-10, ejemplos)*
Para **entrenar redes neuronales**: ajusta los pesos para minimizar la función de pérdida. Usa la **regla de la cadena** para calcular el **gradiente** de la pérdida respecto de cada peso, de la capa de salida hacia atrás, y actualiza los pesos con **descenso por gradiente**. Necesita activaciones **derivables**.

**¿Qué es un optimizador? Nombre tres.** *(2025-10)*
Es la regla concreta con la que se actualizan los pesos a partir del gradiente (una variante del descenso por gradiente que converge más rápido o más estable). Ejemplos: **SGD, Momentum, Nesterov, AdaGrad, RMSProp, Adam, Nadam, AdaMax**.

**¿Para qué utilizaría una red SOM?** *(2025-10)*
Es una red **no supervisada** que proyecta datos de muchas dimensiones a un **mapa 2D** conservando la topología (lo parecido queda cerca). Sirve para **clustering** y **visualización exploratoria** (ej. segmentar clientes). No se entrena con backpropagation.

**Explique en qué consiste el early stopping.** *(2023-11)*
Es una técnica de **regularización**: se monitorea el error en **validación** durante el entrenamiento y se **corta cuando empieza a subir** (aunque el de train siga bajando), quedándose con los pesos del mejor punto. Evita el overfitting.

**Mencione dos funciones de activación y dos técnicas contra el overfitting.** *(2025-11)*
- Activaciones: **ReLU** = max(0, x) (default en capas ocultas) y **sigmoide** = 1 / (1 + e⁻ˣ) (salida binaria). Otras: tanh, softmax, lineal. Conviene saber **graficarlas**: ReLU es 0 para x < 0 y la identidad para x ≥ 0; la sigmoide es una "S" entre 0 y 1 que pasa por 0,5 en x = 0.
- Técnicas: **L1/L2** (penalizan pesos grandes), **dropout** (apaga neuronas al azar al entrenar), **early stopping**, **data augmentation**.

### Sobre el TP1

Aparece **en casi todos los parciales**: "describí una técnica de preprocesamiento aplicada en el TP1 con un ejemplo", "¿qué técnicas usaron para detectar outliers multivariados?", "¿cuántos clusters eligieron con K-Means y qué representan?", "¿qué características del dataset afectaron las métricas y cómo lo mitigaron?". Las respuestas sobre nuestro TP están en un archivo aparte, que se guarda solo en local (`TP1 (privado)/`).

---

## 2. Verdadero o falso

### 29/06/2022

| Afirmación | | Por qué |
| --- | --- | --- |
| La razón de los faltantes siempre es ajena a los datos | **F** | En **MNAR** depende del propio valor faltante |
| Min-max lleva los valores a [0, 1] | **V** | |
| Z-score lleva los valores a [0, 1] | **F** | Deja **media 0 y desvío 1**, sin acotar |
| Imputar con media/mediana atenúa la varianza | **V** | Repite el mismo valor muchas veces |
| La varianza es cuánto varía la predicción según los datos de **test** | **F** | Según los datos de **entrenamiento** |
| Bias muy alto = no se ajustó lo suficiente a train | **V** | |
| Varianza baja: pequeños cambios en train → pequeños cambios en la estimación | **V** | |
| Bias alto → error alto **solo en test** | **F** | En underfitting el error es alto en **train y test** |
| Se elige el atributo que más **aumenta** la impureza | **F** | El que más la **reduce** |
| ID3 es superior a C4.5 porque maneja numéricos | **F** | Al revés: C4.5 agrega numéricos, faltantes y poda |
| Gini es numéricamente similar a la entropía | **V** | |
| Un árbol muy profundo **evita** el overfitting | **F** | Lo **produce**; por eso se poda |
| XGBoost solo sirve para clasificación | **F** | También regresión |
| En Random Forest la votación reduce la varianza | **V** | |
| XGBoost maneja mejor el overfitting con regularizaciones | **V** | λ, γ, learning rate |
| Random Forest es útil para seleccionar features | **V** | Da la importancia de cada variable |
| Las redes se optimizan con descenso por gradiente | **V** | |
| Las redes son sensibles a la escala de los datos | **V** | Hay que normalizar |
| Un perceptrón simple **no** puede modelar NAND | **F** | NAND es **linealmente separable** (sí puede) |
| Las redes muy complejas pueden **sub**ajustar y se arregla con early stopping | **F** | Las muy complejas **sobre**ajustan |
| Outliers: eliminarlos siempre | **F** | |
| Outliers: tratarlos como faltantes y corregir su valor | **F** (según la clave) | Como regla general es falso; si se comprueba que es un **error de carga**, sí se puede corregir |
| Outliers: no hay que tocarlos nunca | **F** | |
| Outliers: analizar su origen antes de decidir | **V** | |
| Los árboles solo sirven para clasificación | **F** | También regresión |
| En los árboles buscamos nodos con la **mayor** entropía | **F** | Con la **menor** (nodos puros) |
| Contra el overfitting: frenar el crecimiento antes (pre-poda) | **V** | |
| Contra el overfitting: crecer entero y después podar | **V** | |

### 27/10/2022

| Afirmación | | Por qué |
| --- | --- | --- |
| SVM busca el clasificador en un espacio de dimensión **más chica** | **F** | En una dimensión **mayor** |
| Con uno de sus kernels SVM trabaja en dimensiones infinitas | **V** | Kernel **radial (RBF)** |
| SVM no transforma realmente los datos (kernel trick) | **V** | |
| El kernel polinómico necesita **d**, "el grado más pequeño" del polinomio | **F** | **d** es **el** grado del polinomio, no el más chico |
| K-Means es no supervisado porque parte de datos **etiquetados** | **F** | Parte de datos **sin** etiquetar |
| K = cantidad de clusters | **V** | |
| K = cantidad de centroides | **V** | Un centroide por cluster |
| K-Means es supervisado porque parte de datos etiquetados | **F** | |
| AdaBoost se entrena con bagging | **F** | Es **boosting** |
| AdaBoost usa árboles completos y Random Forest tocones | **F** | Al revés |
| En Random Forest cada árbol vota según su peso | **F** | Todos valen igual; el voto ponderado es de **AdaBoost** |
| AdaBoost usa bootstrap aggregating | **F** | Eso es **bagging** (Random Forest) |
| El perceptrón simple **no** divide el espacio linealmente | **F** | Justamente lo divide con un hiperplano |
| Las SOM necesitan datos balanceados y **etiquetados** | **F** | Son **no supervisadas** |
| Backpropagation necesita activaciones derivables | **V** | |
| Backpropagation es una **alternativa** al descenso por gradiente | **F** | **Usa** descenso por gradiente |
| Dropout es regularización y evita el sobreajuste | **V** | |
| Habitualmente Adam es más rápido que RMSProp y Momentum | **V** | Combina los dos |
| L1/L2 evitan entrenar con backpropagation | **F** | Se suman a la pérdida; se sigue entrenando igual |
| Softmax da la salida como probabilidad de cada clase | **V** | |
| F1 train 0,75 y test 0,31 → generaliza muy bien | **F** | |
| … → podría estar sobreajustando | **V** | Gran brecha train/test |
| … → hay un problema en los datos de test | **F** | El problema es el modelo |
| … → podría estar subajustando | **F** | En train anda bien |

### Enunciado sin fecha (02-E)

| Afirmación | | Por qué |
| --- | --- | --- |
| El box plot detecta outliers multivariados | **F** | Es **univariado** |
| Isolation Forest es un método **basado en densidad** | **F** | Se basa en **árboles de aislamiento** (el de densidad es **LOF**); sí detecta outliers multivariados |
| La única forma de tratar outliers es borrar los extremos | **F** | |
| Con z-score se eliminan los valores **dentro** de [−3, 3] | **F** | Se marcan los de **afuera** |
| Pearson mide la relación sea o no lineal | **F** | Solo **lineal** |
| La regresión logística resuelve clasificación binaria | **V** | |
| La regresión lineal **simple** usa más de una variable predictora | **F** | Esa es la **múltiple** |
| Bagging y boosting: entrenan varios modelos y toman una única decisión | **V** | Es lo que comparten |
| … cada modelo se entrena en paralelo | **F** | Solo bagging |
| … cada modelo es influido por los otros | **F** | Solo boosting |
| … usan datasets generados con bootstrap | **F** | Solo bagging |

---

## 3. Ejercicios con cuentas

> 💡 **Atajo para el F1 en fracción:** F1 = **2·TP / (2·TP + FP + FN)**. Da lo mismo que 2·P·R/(P+R) y evita calcular P y R por separado.
> ⚠️ En todas las matrices de estos parciales las **filas son la clase real** (True) y las columnas lo predicho (Predicted).

### Dos modelos para detectar enfermos (recuperatorio 23/11/2023)
Modelo A: TN = 13, FP = 12, FN = 16, TP = 22. Modelo B: TN = 15, FP = 10, FN = 11, TP = 27. Total = 63.
- **F1 A** = 44 / (44 + 12 + 16) = 44/72 = **11/18 ≈ 0,611**
- **F1 B** = 54 / (54 + 10 + 11) = 54/75 = **18/25 = 0,72**
- **Accuracy A** = 35/63 = **5/9 ≈ 0,556** · **Accuracy B** = 42/63 = **2/3 ≈ 0,667** → **B**, mejor en las dos métricas.

### Accuracy y recall (recuperatorio 20/11/2025)
Modelo A: TN = 10, FP = 8, FN = 9, TP = 17 (total 44: 26 enfermos, 18 sanos). Modelo B: TP = 8, FP = 9 → con los mismos datos, FN = 26 − 8 = 18 y TN = 18 − 9 = 9.
- **A:** accuracy = 27/44 ≈ 0,614 · recall = 17/26 ≈ 0,654
- **B:** accuracy = 17/44 ≈ 0,386 · recall = 8/26 ≈ 0,308 → **A** en las dos.

### Accuracy (02-E)
Modelo A: TN = 135, FP = 36, FN = 38, TP = 111. Modelo B: TP = 49, TN = 130, FP = 41, FN = 100. Total = 320.
- **A** = 246/320 ≈ **0,769** · **B** = 179/320 ≈ **0,559** → **A**.

### Matriz 3×3 (27/10/2022)

```
              Predicho
              0    1    2
Real   0     13    1    1
       1      0   15    3
       2      4    1   13
```
- **Precision clase 0** = 13 / (13 + 0 + 4) = **13/17** (columna del 0)
- **Recall clase 1** = 15 / (0 + 15 + 3) = **15/18 = 5/6** (fila del 1)
- **Accuracy** = (13 + 15 + 13) / 51 = **41/51**

### Salida de un perceptrón (27/10/2022)
w₁ = 0,7 · w₂ = 0,8 · b = −0,5 · escalón: 1 si la suma ≥ 0.

| x₁, x₂ | Suma = 0,7·x₁ + 0,8·x₂ − 0,5 | Salida |
| --- | --- | --- |
| 0, 0 | −0,5 | **0** |
| 1, 1 | 1,0 | **1** |
| 0,5 · 0,5 | 0,25 | **1** |
| −0,25 · 1 | 0,125 | **1** |

### K-fold cross validation (27/10/2022)
250 filas, 80% train (200) y 20% test (50), k = 5.
- ¿Cuántas veces se entrena? **5** (una por fold).
- ¿Con cuántos registros cada vez? 4/5 de 200 = **160**.
- ¿Cuántos tiene cada validación? 1/5 de 200 = **40**.
- ¿Y con 70/30? Igual **5 veces**: la cantidad depende de k, no de la partición (cambian los tamaños: 140 para entrenar y 35 para validar).

### GridSearchCV (27/10/2022)

```python
params_grid = {'criterion': ['gini', 'entropy'],
               'ccp_alpha': [0.001, 0.0173, 0.0336, 0.05],
               'max_depth': [2, 4, 5]}
gridcv = GridSearchCV(estimator=arbol, param_grid=params_grid, scoring='accuracy', cv=10)
```
- **Juegos de parámetros:** 2 × 4 × 3 = **24** (prueba todas las combinaciones).
- **Veces que se evalúa cada uno:** **10** (cv = 10) → 240 entrenamientos, más el reentrenamiento final del mejor.
- **Ejemplos:** `{'criterion': 'gini', 'ccp_alpha': 0.001, 'max_depth': 2}` · `{'criterion': 'entropy', 'ccp_alpha': 0.05, 'max_depth': 5}`.
- **`ccp_alpha`:** parámetro de **poda por costo-complejidad**. Cuanto más grande, **más se poda** (árbol más simple); con 0 no se poda. Se usa contra el **overfitting**.
- **`max_depth`:** **profundidad máxima** del árbol (cuántas preguntas encadenadas puede hacer). Limitarla evita el **overfitting**; demasiado baja causa **underfitting**.

### KNN según K (27/10/2022)
Punto negro junto a una cruz y a cuatro círculos, con más cruces a la izquierda: **K = 1 → cruz** (el más cercano) · **K = 4 → círculo** (mayoría de círculos) · **K = 8 → cruz** (al ampliar entran más cruces). La idea: **la clase puede cambiar con K**.

### Correlación de Pearson (recuperatorio 23/11/2023)
Asociar gráficos con r = 0,80, r = −0,86 y r = 0,35: **0,80** → nube creciente bastante ajustada (**positiva fuerte**); **−0,86** → nube decreciente ajustada (**negativa fuerte**); **0,35** → nube muy dispersa con leve tendencia creciente (**positiva débil**).

---

## ⚠️ Errores en las resoluciones que circulan

| Dónde | Qué dice | Lo correcto |
| --- | --- | --- |
| Resolución 26/10/2023, ej. 7 | Precision para **diagnóstico médico** | Es defendible solo si se justifica el costo del falso positivo, pero el ejemplo del profe para medicina es **recall** (que no se escape ningún enfermo). Para precision usá **spam** o el álbum de fotos |
| Resolución 23/11/2023, ej. 2 | r = 0,35 = "ausencia de correlación" | Es correlación **positiva débil**, no ausencia (eso sería r ≈ 0) |
| Resolución 23/11/2023, ej. 5 | Calcula precision y recall pero **no el F1** que se pedía | F1 A = 11/18 y F1 B = 18/25 (ver arriba) |
| Examen corregido 27/10/2022 | Respuestas marcadas con ✗ | Las respuestas correctas están en las tablas de V/F y en los ejercicios de arriba |
| Notion (preguntas tipo parcial) | Sigmoide para regresión; Softmax para "N clases simultáneas"; el dropout "hace que la red dependa de unas pocas neuronas" | Regresión → salida **lineal**; Softmax → multiclase **excluyente**; dropout **evita** depender de pocas neuronas |

---

<sub>⚙️ Armado a partir de los parciales de la cátedra Rodríguez guardados en `Material extra/Parciales anteriores/` (fuente: Notion público de un alumno, 2C 2025). Las respuestas se revisaron contra las guías y las teóricas.</sub>
