## Clase 12 - Machine Learning III

### Videos teóricos

#### 1. Árboles de Decisión: Fundamentos

Los árboles de decisión son modelos de **aprendizaje supervisado** que segmentan el conjunto de entrenamiento en regiones más pequeñas (hojas o nodos terminales) basándose en reglas lógicas. Su objetivo es que las observaciones dentro de una hoja sean lo más parecidas posible entre sí.

- **Ventajas:** Son modelos de "caja blanca", fáciles de interpretar, no requieren normalización de datos y sirven tanto para clasificación como para regresión.
- **Algoritmos Clásicos:**
    - **ID3 (1979):** Utiliza la **ganancia de información** (basada en la reducción de la entropía) para elegir el mejor atributo en cada paso. Originalmente solo admitía atributos categóricos.
    - **C4.5 (1993):** Mejora al ID3 permitiendo **atributos numéricos** (mediante la evaluación de puntos de corte ordenados) y manejando datos faltantes. `C5.0` es una mejora de este algoritmo.
- **Ejemplo Teórico:** Para decidir si contratar a un candidato, el árbol puede evaluar la "presencia", "estudios" y "experiencia". Si la presencia es "buena", el modelo puede decidir directamente la contratación.

#### 2. Técnicas de Ensambles

Un ensamble combina múltiples modelos para obtener un clasificador más robusto y preciso que cualquiera de sus componentes individuales.

- **Bagging (Bootstrap Aggregating):** Reduce la varianza entrenando múltiples modelos en paralelo sobre diferentes muestras del set de datos generadas con **Bootstrap** (muestreo con reposición).
- **Boosting:** Los modelos se entrenan de forma **secuencial**, donde cada nuevo árbol intenta corregir los errores cometidos por los anteriores.
- **Combinación de Resultados:** Los resultados se pueden combinar mediante **voto de la mayoría** (clasificación), **promedio** (regresión), o técnicas más complejas como **Stacking/Blending**, donde un modelo adicional aprende a combinar las salidas de los anteriores.

#### 3. RandomForest

Es una variante de Bagging sobre árboles de decisión que busca reducir la correlación entre ellos.

- **Mecánica:** En cada partición de un árbol, solo se permite elegir entre un **subconjunto aleatorio de variables (*m* de *p*)**. Esto evita que variables muy dominantes hagan que todos los árboles sean iguales.
- **Importancia de Variables:** Permite calcular qué tan útil es cada variable basándose en qué tan arriba y con qué frecuencia aparece en los árboles.

#### 4. XGBoost (Extreme Gradient Boosting)

Es una implementación optimizada de boosting que se enfoca en la velocidad y el rendimiento.

- **Funcionamiento:** En lugar de ajustar directamente a los residuos como el boosting tradicional, se ajusta a los **gradientes de una función de pérdida**.
- **Regularización:** Incluye parámetros para controlar la complejidad del modelo (como el número de hojas o pesos de las mismas) para evitar el sobreajuste (overfitting).
- **Hiperparámetros clave:** Tasa de aprendizaje (learning rate o lambda), profundidad máxima de los árboles y porcentaje de columnas/muestras por árbol.

--------------------------------------------------------------------------------

### Resumen de la Clase Práctica de Árboles (11-05-2026)

#### Conceptos Clave de Construcción

- **Construcción Top-Down y Greedy:** Los árboles se construyen desde la raíz hacia abajo. En cada paso, el algoritmo elige la mejor partición inmediata sin considerar si esa elección afectará negativamente los pasos futuros (enfoque codicioso o _greedy_).
- **Puntos de Corte:** Para variables continuas, se consideran puntos intermedios entre valores consecutivos del set de entrenamiento para decidir dónde dividir el espacio.

#### Métricas de Evaluación de Particiones

Para decidir qué división es "mejor", se utilizan métricas específicas:

- **Regresión:** Se busca minimizar la **Suma de los Residuos al Cuadrado (RSS)**.
- **Clasificación:** Se utiliza el **Índice de Gini** (métrica de impureza) o la **Entropía Cruzada**.

#### Control del Overfitting y Criterios de Parada

Dado que un árbol puede crecer hasta que cada hoja tenga una sola observación (causando sobreajuste), se aplican criterios de corte:

1. **Profundidad máxima:** Limitar cuántos niveles puede tener el árbol.
2. **Pureza de las hojas:** Detenerse cuando todas las observaciones en una hoja sean de la misma clase.
3. **Cantidad mínima de observaciones:** No permitir divisiones si una hoja tiene menos de n datos.

#### Implementación Práctica Destacada

- **Dataset:** Se utilizó el conjunto de datos de cáncer de mama (Breast Cancer) para clasificación binaria.
- **Ensambles con otros modelos:** Se demostró que técnicas como **Bagging** pueden aplicarse no solo a árboles, sino también a otros estimadores como **KNN** (vecinos más cercanos).
- **Validación Cruzada (K-Fold):** Se explicó cómo dividir el set de entrenamiento en k partes para evaluar diferentes combinaciones de hiperparámetros de manera robusta.
- **Error Común:** El profesor hizo énfasis en que para calcular el **área bajo la curva ROC**, se deben pasar **probabilidades** y no valores binarios (0 o 1), ya que la curva se construye variando los puntos de corte de probabilidad.
- **Importancia de Variables (Feature Importance):** Es fundamental graficar la importancia de las variables para entender si el modelo está capturando patrones lógicos o si hay algún error en los datos. En el ejemplo del cáncer, variables como el "peor perímetro" o "peor radio" resultaron ser las más críticas.
