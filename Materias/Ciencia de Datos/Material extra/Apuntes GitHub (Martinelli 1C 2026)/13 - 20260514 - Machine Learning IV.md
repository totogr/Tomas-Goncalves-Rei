## Clase 13 - Machine Learning IV

### 1. Neuronas Artificiales y el Teorema de Aproximación Universal

Una **neurona artificial** se define matemáticamente como una función de la forma $f(w⋅x+b)$, donde $w$ representa los **pesos** (weights) y $b$ el **sesgo** (bias).

- **Neuronas Lineales:** Si la función de activación es lineal ($f(x)=x$), la red neuronal, sin importar cuántas capas tenga, siempre se comportará como un modelo lineal simple. Esto sucede porque la composición de funciones lineales es otra función lineal, lo que no permite aprovechar la profundidad de la red.
- **Neuronas No Lineales:** Para resolver problemas complejos, se introducen funciones no lineales como la función **escalón**, la **sigmoide** (entre 0 y 1), la **tangente hiperbólica** (entre -1 y 1) o la **ReLU** ($0$ para negativos, $x$ para positivos).
- **Teorema de Aproximación Universal:** Este teorema establece que una red neuronal con **una sola capa oculta de tamaño infinito** puede aproximar cualquier función continua. El uso de funciones no lineales permite "suavizar" las salidas y crear curvas complejas mediante la combinación de múltiples neuronas.

### 2. Fundamentos de Deep Learning

Aunque una capa infinita puede aproximar cualquier función, el **Deep Learning** propone apilar múltiples capas (hacer la red "profunda") por razones de eficiencia.

- **Ventajas de la profundidad:** Apilar capas permite una suerte de **compresión de información**. Las primeras capas aprenden características simples y las capas posteriores las combinan en conceptos más complejos. Esto resulta más "barato" en términos de parámetros que usar una sola capa gigante.
- **Arquitecturas de ejemplo:**
    - **Clasificación Binaria:** Suele utilizar una función **sigmoide** al final para devolver una probabilidad entre 0 y 1.
    - **Regresión:** Es común usar la función **ReLU** o una activación lineal al final para permitir valores arbitrariamente altos o negativos.

### 3. Entrenamiento: Back Propagation y Descenso por el Gradiente

El entrenamiento consiste en ajustar los **parámetros** (pesos y sesgos) para minimizar una función de error o pérdida (Loss).

- **Parámetros vs. Hiperparámetros:** Los parámetros son los números que el algoritmo ajusta (w, b). Los **hiperparámetros** son definidos por el usuario: cantidad de capas, neuronas, funciones de activación, la tasa de aprendizaje (**learning rate**) y el tamaño del lote (**batch size**).
- **Métricas de Error:** Se utilizan funciones como el Error Cuadrático Medio (MSE) para regresión o la **Entropía Cruzada** para clasificación.
- **Backpropagation:** Es el proceso de calcular el gradiente de la función de pérdida respecto a cada parámetro usando la **regla de la cadena**. Se llama así porque el error se propaga "hacia atrás" desde la salida hacia la entrada para saber cuánto debe cambiar cada peso.
- **Stochastic Gradient Descent (SGD):** En lugar de usar todo el dataset a la vez, se entrena por pequeños grupos llamados **batches**. Esto añade azar al proceso, lo que ayuda a evitar mínimos locales y regulariza el entrenamiento.

### 4. Redes Neuronales Convolucionales (CNN)

Las CNN están diseñadas principalmente para trabajar con **imágenes**, aunque pueden aplicarse a texto.

- **Problema de Representación:** Antes de las CNN, la extracción de características de una imagen era artesanal (p. ej., calcular el color promedio). Las CNN resuelven esto aprendiendo **automáticamente** los filtros necesarios.
- **Funcionamiento:**
    - **Convolución:** Se aplica un filtro (**kernel**) que se desplaza por la imagen realizando multiplicaciones. Los valores del filtro son los parámetros que la red aprende para detectar bordes, texturas o formas.
    - **Max Pooling:** Reduce el tamaño de las matrices quedándose con el valor máximo de una zona. Esto otorga **invarianza a rotaciones y movimientos**, haciendo que la red sea más robusta.
- **Transfer Learning:** Es posible usar redes ya entrenadas con millones de imágenes (como VGG) y adaptarlas a un problema específico (p. ej., distinguir entre un mouse y un teclado) con pocos datos propios.
- **Aplicación en Texto:** Mediante **embeddings** (vectores que representan palabras), se pueden aplicar convoluciones 1D para tareas como análisis de sentimiento, siendo modelos muy rápidos y efectivos.

### 5. Regularización y Técnicas Avanzadas

La regularización busca evitar el **overfitting** (sobreajuste), que ocurre cuando la red memoriza los datos de entrenamiento pero no generaliza bien.

- **L1 (Lasso) y L2 (Ridge):** Añaden una penalización a la función de pérdida basada en la magnitud de los pesos. La L1 tiende a llevar pesos a cero (economiza features), mientras que la L2 penaliza más los pesos muy altos, suavizando la función.
- **Dropout:** Una técnica donde, con cierta probabilidad, se "apagan" neuronas al azar durante el entrenamiento. Esto obliga a la red a no confiar en una sola conexión y a crear redundancia, entorpeciendo el entrenamiento para que sea más estable.
- **Early Stopping:** Consiste en monitorear el error en un set de validación y detener el entrenamiento cuando este error empieza a subir, indicando que el modelo comenzó a sobreajustar.
- **Autoencoders:** Redes compuestas por un **encoder** (comprime la información) y un **decoder** (intenta reconstruir la entrada original). Se usan para resumir features o comprimir datos con pérdida.
- **Softmax:** Una función de activación para problemas **multiclase** que garantiza que todas las probabilidades de salida sumen 1.

### 6. Aplicaciones y Frameworks

El Deep Learning tiene un dominio fuerte en áreas de **computación cognitiva**.

- **Ejemplos de Aplicación:** Reconocimiento de dígitos (MNIST), detección de tumores en imágenes médicas, traducción de textos, chatbots y **modelos generativos** como las GANs o DALL-E (generación de imágenes a partir de texto).
- **Frameworks:** Los más utilizados hoy en día son **PyTorch** y **TensorFlow**. **Keras** se destaca como una interfaz de alto nivel que permite construir redes profundas de manera muy sencilla, simplemente instanciando capas en orden.

### KERAS (clase práctica)

```python
import keras
import tensorflow as tf
import numpy as np
```

- Para que un modelo sea reproducible, nos aseguramos de **agregar el `seed`** (para que se persistan los mismos datos). Esto vale para todo (`np` y `tf`).

```python
def f(x):
	return 2*x + 4
# Esta función solo necesitaría 1 neurona.
```

- **El Peso ($w$ o Weight):** La red buscará que este valor sea lo más cercano posible a **2**. Es la pendiente de la recta.
- **El Sesgo ($b$ o Bias):** La red buscará que este valor sea lo más cercano posible a **4**. Es el punto donde la recta corta el eje Y.
#### 1. Definición del Modelo (`Sequential`)

```python
model = Sequential([
	Input(shape=(1,))
	Dense(1),
])
```

En el primer bloque, estás construyendo la estructura de la red:

- **`Sequential`**: Es un contenedor de Keras que apila capas de forma lineal (una después de otra).
- **`Input(shape=(1,))`**: Indica que tu red recibirá **un solo número** como entrada. Por ejemplo, si quieres predecir el precio de una casa basándote solo en los metros cuadrados, la entrada es ese único valor.
- **`Dense(1)`**: Esta es la "neurona". Una capa `Dense` significa que está totalmente conectada. Al poner un `(1)`, le dices que solo tiene una neurona de salida.

**¿Qué está calculando realmente?**
 
A nivel matemático, esta neurona está intentando aprender la fórmula de una línea recta: $$y = wx + b$$Donde $w$ es el **peso** (weight) y $b$ es el **sesgo** (bias). Por eso, en el resumen ves que hay **2 parámetros**: uno para $w$ y otro para $b$.

#### 2. Resumen del Modelo (`model.summary()`)

```python
model.summary()
```

- **Total params: 2**: son los dos valores que la red debe "adivinar" mediante el entrenamiento.
- **Output Shape (None, 1)**: El `None` significa que el modelo puede recibir cualquier cantidad de ejemplos (batch size), y el `1` es el resultado que arroja.

#### 3. Compilación del Modelo (`model.compile`)

```python
model.compile(
	optimizer=Adam(learning_rate=0.05),
	loss="mean_squared_error",
)
```

Acá es donde se configura "cómo va a aprender" la red antes de mostrarle los datos reales:

- **`optimizer=Adam(learning_rate=0.05)`**: El optimizador es el algoritmo que ajusta los pesos para reducir el error. **Adam** es de los más usados porque es muy eficiente. El `learning_rate` (tasa de aprendizaje) es qué tan grandes son los "pasos" que da la red para corregirse; $0.05$ es un paso relativamente rápido.
- **`loss="mean_squared_error"`**: La función de pérdida (Loss) es cómo la red mide qué tan mal lo está haciendo. El **Error Cuadrático Medio** calcula la diferencia entre lo que la red predijo y el valor real, lo eleva al cuadrado y saca el promedio. El objetivo del entrenamiento es que este número llegue lo más cerca posible a **cero**.

#### 4. El entrenamiento (`model.fit`)

```python
hist = model.fit(x, y, epochs=200, batch_size?16, verbose=0)
```

Esta es la fase donde la red "estudia":

- **`epochs=200`**: Le pediste a la red que recorra todo tu set de datos **200 veces**. Cada vez que termina una vuelta (época), la red ajusta sus pesos internos para intentar equivocarse menos en la siguiente.
- **`batch_size=16`**: En lugar de mirar todos los datos a la vez, la red toma grupos de 16 ejemplos para actualizar sus parámetros. Esto ayuda a que el entrenamiento sea más estable y eficiente en memoria.    
- **`hist = ...`**: Se guarda para poder graficarlo después.

#### 5. Interpretación de la Gráfica de Pérdida (`Loss`)

La gráfica muestra la relación entre las épocas (eje X) y el error (eje Y).

- **El descenso inicial:** En las primeras 25 épocas, el optimizador (Adam) encontró rápidamente la dirección correcta para ajustar los pesos. El error bajó de casi 175 a casi 0.
- **La "meseta" (asíntota):** Después de la época 50, la línea se vuelve plana y se pega al eje X. Esto indica que el modelo ha **convergido**. Es decir, ya aprendió los patrones de los datos y por más que lo dejemos entrenando mil épocas más, ya encontró los mejores valores para $w$ (peso) y $b$ (sesgo).

![[ML loss-epoch.png]]

**¿Qué sigue ahora?** Normalmente, después de ver que la pérdida es mínima, el siguiente paso es la **predicción**. Podrías probar algo como:

```python
# Si 'x' era 10, y el modelo aprendió y=2x, esto debería dar cerca de 20
print(model.predict([10])) 
```

---

