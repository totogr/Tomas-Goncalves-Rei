## Clase 11 - Machine Learning II

### Videos teóricos
#### 1. Creación de Nuevos Features

La creación de características (features) busca generar información que no existe explícitamente en el dataset original para mejorar el poder predictivo del modelo.

- **Tipos de Features:**
    - **Temporales (Lagged Features):** Valores de una variable en periodos de tiempo anteriores (ej. el precio de un ítem hace una semana o un mes).
    - **Estadísticos:** Promedios, medianas, desviaciones estándar, máximos y mínimos de otros features.
    - **Basados en K-NN:** El promedio de alguna métrica para los vecinos más cercanos de una observación.
    - **De Texto:** Extraer información binaria (si contiene una palabra) o usar técnicas como TF-IDF.
#### 2. Transformación de Features

Modificar features existentes para que se adapten mejor a las necesidades del algoritmo.

- **Logaritmos:** Útil para convertir distribuciones exponenciales en normales. Es vital para redes neuronales pero **irrelevante para modelos basados en árboles**, ya que no cambia el orden de los datos.
- **Normalización y Escalamiento:** Restar el promedio y dividir por la desviación estándar. Es obligatorio para redes neuronales pero innecesario para árboles.
- **Binning:** Convertir variables numéricas en categóricas mediante "baquets" o rangos (ej. edad en grupos: joven, adulto, etc.). Esto limita y simplifica los puntos de split para los árboles de decisión.

#### 3. Codificación de Variables Categóricas

Muchos modelos (como XGBoost) no aceptan variables categóricas y requieren su conversión a números.

- **One-Hot Encoding (OHE):** Crea una columna por cada valor posible. Es muy popular pero **ineficiente si hay miles de categorías**, ya que genera demasiadas columnas.
- **Label Encoding (Arbitrario):** Reemplazar cada categoría por un número (1, 2, 3...). Es **el peor método**, ya que introduce un orden artificial (ej. decir que "París < Roma") que confunde a los modelos.
- **Binary Encoding:** Convierte las categorías en números binarios y asigna cada bit a una columna. Requiere mucho menos espacio que OHE (logaritmo de la cantidad de valores).
- **Mean Encoding (Target Encoding):** Reemplaza la categoría por el promedio del target para esa categoría.
    - **Ejemplo:** Si para la ciudad "Moscú" el target es 1 en el 33% de los casos, "Moscú" se convierte en 0.33.
    - **Riesgo:** Genera **Overfitting** masivo por filtración de etiquetas (data leakage).
    - **Remedios:** Usar validación cruzada (Leave-one-out), agregar ruido o usar el promedio acumulado en el tiempo (expanding mean).

#### 4. Interacción entre Features

Los modelos basados en árboles analizan cada columna de forma individual y les cuesta detectar relaciones entre ellas (como ratios).

- **Numéricas:** Crear nuevas variables mediante operaciones matemáticas (`+`, `-`, `*`, `/`).
- **Categóricas:** Concatenar dos variables (ej. "Origen" y "Destino") y luego encodearlas.
- **Intervalos de Confianza:** Es importante considerar que un ratio de 0.5 basado en 2 casos no es igual a un 0.5 basado en 500 casos. Se sugiere usar fórmulas como la de Wilson para determinar el piso y techo del ratio.
- **Truco de Hojas de Árbol:** Usar el índice de la hoja de un árbol pequeño como un feature que ya captura múltiples interacciones.

#### 5. Caso de Estudio: Predicción de Ventas en Supermercados

Se analizó una competencia para predecir ventas mensuales en Rusia.

- **Puntos Clave:**
    - **Datos Faltantes:** Si un producto no se vendió un día, no figura en el registro. Es crucial **completar la matriz con ceros** para informar al modelo qué no se vendió.
    - **Validación Temporal:** En series de tiempo, **no se puede hacer un split 80/20 al azar**. Se debe entrenar con el pasado y validar con el futuro.
    - **Features Creados:** Ranking de sucursales, precios promedio por categoría y meses anteriores (lags) para capturar la estacionalidad (ej. ventas navideñas en diciembre).

---
### Resumen de la Clase Práctica de Feature Engineering (Titanic)

En esta clase se aplicaron los conceptos anteriores al dataset del **Titanic** para predecir la supervivencia de los pasajeros.

Puntos Importantes resaltados:

1. **Dependencia del Problema:** El Feature Engineering no está escrito en piedra; lo que funciona para un modelo o dataset puede no funcionar para otro.
2. **Limpieza de Datos Inicial:** Se eliminaron columnas como `Name` y `PassengerId` porque son identificadores únicos que no aportan patrones estadísticos y pueden causar que el modelo "memorice" en lugar de aprender.
3. **Manejo de Nulos en Edad (*Age*)**:
    - No se recomienda simplemente usar el promedio global porque altera la distribución (infla el medio).
    - Se optó por **promedios con criterio**: calcular la media de edad según el sexo (los hombres solían tener promedios distintos a las mujeres por los roles en el barco).
4. **Tratamiento de la Cabina (*Cabin*)**:
    - Como faltaban muchísimos datos (6/8 partes del dataset), se creó un feature booleano `Has Cabin` para indicar si el dato existía.
    - Se extrajo la **letra de la cabina** (que indica la cubierta y cercanía a botes) y se le aplicó un **Target Encoder**.
5. **One-Hot Encoding y *drop_first***: Al encodear el sexo o el puerto, se usó `drop_first=True`. Esto evita la redundancia de columnas (si no es hombre, es mujer) y ahorra recursos de procesamiento.
6. **La Regla de Oro: Fit vs. Transform:**
    - **Fit:** El encoder aprende las categorías y estadísticas únicamente del set de **entrenamiento**.
    - **Transform:** Se aplica esa lógica aprendida tanto al set de entrenamiento como al de **test**.
    - **Error Fatal:** Hacer `fit_transform` en el set de test. Esto causa **Data Leakage**, invalidando los resultados del modelo al darle información del futuro o del set que debe ser oculto.

Métricas y Evaluación:

- El profesor enfatizó que la **Precisión** no lo es todo. Se discutió el **Recall** (importante para no omitir casos positivos, como en medicina) y el **F1-Score**, que es un balance más robusto, especialmente en datasets desbalanceados.
- Se utilizó la **Curva ROC** para ver cómo evoluciona el ratio de verdaderos positivos frente a falsos positivos según el umbral (threshold) de decisión.
- **Feature Importance:** Al final, el modelo mostró que las variables más importantes fueron **Sex** (por la regla de "mujeres y niños primero") y la **Clase** del pasajero. Sorprendentemente, la cantidad de hijos no tuvo gran relevancia en este modelo.