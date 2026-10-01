# 10 · Redes neuronales (perceptrón, MLP, backpropagation, SOM)

> 🧩 **Guía de estudio para llegar a la clase con el tema masticado.**
> Fundamentada en las PPTs *Redes Neuronales*, *Backpropagation* e *Implementación de Redes Neuronales* de la cátedra (Rodríguez) y en los notebooks `redes_neuronales_introducción.ipynb`, `redes_neuronales_keras_clasificacion.ipynb`, `redes_neuronales_keras_regresion.ipynb` y `redes_neuronales_keras_cross_validation.ipynb`.

---

## 🎯 En una frase

Una **red neuronal** es un modelo inspirado en el cerebro: **neuronas** que combinan entradas con **pesos** más una **función de activación**; el **perceptrón** es la neurona básica, el **perceptrón multicapa (MLP)** apila varias capas para resolver problemas no lineales, y **backpropagation** es el algoritmo que ajusta los pesos propagando el error hacia atrás.

---

## 🧭 ¿Por qué importa / dónde encaja?

Es el salto de los modelos "clásicos" (clase 08) a los que dominan la IA moderna. Reutiliza todo lo anterior:
- El **gradient descent** de la clase 08 es el motor.
- Las **métricas** de la clase 04 evalúan.
- Es la antesala del **deep learning** (clase 12).

---

## 💡 La idea con una analogía

Una neurona es como una **decisión con votos ponderados**: cada entrada tiene un **peso** (cuánto le importa), sumás todo y si supera un **umbral** (bias), "te activás" (decís que sí). Un **MLP** es un **comité de comités**: capas donde la salida de unas neuronas alimenta a las siguientes, permitiendo aprender patrones cada vez más complejos. **Backpropagation** es el "feedback": cuando el resultado final está mal, el error se **reparte hacia atrás** ajustando cada peso según cuánto contribuyó.

---

## 🧠 El Perceptrón Simple

**Estructura.** Una sola neurona con N entradas, cada una multiplicada por un peso, sumadas junto con un bias, y pasadas por una función de activación.

```
      x₁ ──w₁──┐
      x₂ ──w₂──┼──[ Σ ]──→ activación(suma) ──→ y
      ...      │
      xₙ ──wₙ──┘
       b (bias) ↑
```

**Fórmula:**

$$y = \text{activación}(w_1 x_1 + w_2 x_2 + \dots + w_n x_n + b)$$

Con la **función escalón de Heaviside** como activación (la original):
- Si `w₁·x₁ + w₂·x₂ + b ≥ 0` → **y = 1**
- Si `w₁·x₁ + w₂·x₂ + b < 0` → **y = 0**

El bias `b` cumple el rol de **umbral**: es el valor que la suma ponderada tiene que superar para activarse.

### Entrenamiento del perceptrón — ejemplo AND paso a paso

Tengo un dataset etiquetado para la función AND (dos entradas, salida esperada):

| x₁ | x₂ | esperado |
| --- | --- | --- |
| 0 | 0 | 0 |
| 0 | 1 | 0 |
| 1 | 0 | 0 |
| 1 | 1 | 1 |

**Paso 1 — Inicializar pesos aleatorios:** `w₁ = 0.3`, `w₂ = 0.2`, `b = -1`.

**Paso 2 — Primera iteración** con la entrada `(1, 1)`:
- Suma: `0.3·1 + 0.2·1 − 1 = -0.5` → escalón → **0** (esperaba 1).
- **Error = esperado − predicho = 1 − 0 = 1.**

**Paso 3 — Actualizar los pesos** con la regla:

$$w_i \leftarrow w_i + \alpha \cdot \text{error} \cdot x_i$$

Con `α = 0.2` (tasa de aprendizaje):
- `w₁ = 0.3 + 0.2·1·1 = 0.5`
- `w₂ = 0.2 + 0.2·1·1 = 0.4`

**Paso 4 — Repetir** hasta que el error sea 0 para todas las entradas. En este ejemplo, en pocas iteraciones el perceptrón converge a `w₁ = 0.7, w₂ = 0.6, b = -1`.

**Paso 5 — Modelo entrenado.** Se almacena como una **matriz de números flotantes** — los pesos y el bias son todo lo que la red "sabe".

> 💡 **La tasa de aprendizaje α** (entre 0 y 1, típico 0.5) controla cuánto se mueven los pesos en cada paso. Igual que en gradient descent: muy chica = aprende lentísimo; muy grande = oscila sin converger.

### Limitación del perceptrón simple

**Solo resuelve problemas linealmente separables.** El contraejemplo clásico es **XOR**:

| x₁ | x₂ | XOR |
| --- | --- | --- |
| 0 | 0 | 0 |
| 0 | 1 | 1 |
| 1 | 0 | 1 |
| 1 | 1 | 0 |

**No hay una recta** que separe los 1 de los 0 en el plano `(x₁, x₂)`. Por eso surge el **perceptrón multicapa**.

---

## 🧠 Funciones de activación

| Nombre | Fórmula | Rango de salida | Dónde se usa |
| --- | --- | --- | --- |
| **Escalón (Heaviside)** | 1 si x ≥ 0, else 0 | {0, 1} | Perceptrón clásico; obsoleta en MLP (no derivable) |
| **Sigmoide** | `1 / (1 + e⁻ˣ)` | (0, 1) | Clasificación binaria en la salida; capas ocultas antiguas |
| **Tanh** | `tanh(x)` | (−1, 1) | Capas ocultas; centrada en cero (converge mejor que sigmoide) |
| **ReLU** | `max(0, x)` | [0, ∞) | **Default** en capas ocultas modernas; rápida y evita saturación |
| **Lineal** | `x` | (−∞, ∞) | Capa de salida en problemas de **regresión** |
| **Softmax** | `eˣⁱ / Σ eˣʲ` | (0, 1) y suma 1 | Capa de salida en **clasificación multiclase** |

> 🔑 **Softmax = exponencial normalizada.** Convierte las salidas de la última capa en un **vector de probabilidades** que suma 1. La clase predicha es el `argmax`.

**Regla rápida para la última capa:**

| Problema | Activación de salida |
| --- | --- |
| Regresión | **Lineal** |
| Clasificación binaria | **Sigmoide** |
| Clasificación multiclase excluyente | **Softmax** |
| Clasificación N etiquetas simultáneas (multi-label) | **Sigmoide** en cada neurona |

---

## 🧠 Perceptrón Multicapa (MLP)

Varias neuronas organizadas en **capas totalmente conectadas** (fully connected):

```mermaid
flowchart LR
    I1["x1"] --> H1["h1"]
    I1 --> H2["h2"]
    I2["x2"] --> H1
    I2 --> H2
    H1 --> O1["o1"]
    H1 --> O2["o2"]
    H2 --> O1
    H2 --> O2
    O1 --> Y1["y1"]
    O2 --> Y2["y2"]
```

- **Capa de entrada:** las features del dataset.
- **Capa(s) oculta(s):** las neuronas que combinan información — acá vive la "inteligencia" aprendida.
- **Capa de salida:** una o varias neuronas según el problema (regresión / clasificación).
- **Pesos y biases:** se inicializan **aleatorios** y se van ajustando con backpropagation.

**El forward pass** (hacia adelante):
1. La entrada se multiplica por los pesos de la primera capa, se suma el bias, se aplica la activación → salida de la capa oculta.
2. Esa salida alimenta a la siguiente capa, con sus propios pesos y biases.
3. Al final, la capa de salida produce la predicción.

---

## 🔄 Backpropagation paso a paso

**Idea.** Después del forward pass, calculo el **error** de la salida y lo **propago hacia atrás** por la red, actualizando los pesos usando la **regla de la cadena** del cálculo.

### Los pasos (nivel detalle de la PPT)

**1. Forward pass.** Calcular la salida de cada neurona.

Para una neurona `h` con entradas `x₁, x₂` y pesos `w₁, w₂, b`:

$$\text{net}_h = w_1 x_1 + w_2 x_2 + b, \qquad \text{out}_h = \sigma(\text{net}_h)$$

donde `σ` es la activación (sigmoide en el ejemplo canónico).

**2. Calcular el error total.** Error cuadrático medio en cada neurona de salida:

$$E_{\text{total}} = \sum_i \tfrac{1}{2}(\text{objetivo}_i - \text{salida}_i)^2$$

**3. Backpropagation en la última capa.** Queremos saber **cuánto cambia el error si cambio el peso `w₅`** (regla de la cadena):

$$\frac{\partial E_{\text{total}}}{\partial w_5} = \frac{\partial E_{\text{total}}}{\partial \text{out}_{o_1}} \cdot \frac{\partial \text{out}_{o_1}}{\partial \text{net}_{o_1}} \cdot \frac{\partial \text{net}_{o_1}}{\partial w_5}$$

Tres piezas:
- Cuánto contribuye `o₁` al error total.
- Cuánto cambia `o₁` al cambiar su entrada neta (derivada de la activación).
- Cuánto cambia la entrada neta al cambiar `w₅`.

**4. Actualizar el peso** con learning rate `η`:

$$w_5' = w_5 - \eta \cdot \frac{\partial E_{\text{total}}}{\partial w_5}$$

Típicamente `η = 0.5` en el ejemplo didáctico; en producción se usan valores mucho más chicos (`0.01` o `0.001`).

**5. Propagar hacia la capa oculta.** Para los pesos de la primera capa (`w₁..w₄`), la regla de la cadena se encadena un paso más, usando lo ya calculado en la capa de salida (por eso se llama **back**propagation — se reutilizan derivadas de atrás hacia adelante).

**6. Repetir** forward + error + backprop para cada ejemplo (o mini-batch) hasta converger o alcanzar el máximo de **épocas**.

> 🔑 **¿Qué es una época?** Una pasada completa por **todo el dataset**. Entrenar una red son típicamente decenas o cientos de épocas.

---

## 🔀 SOM — Self-Organizing Maps (Kohonen)

Las redes neuronales no son todas supervisadas. Las **SOM** (de Teuvo Kohonen) son redes **no supervisadas** que aprenden a mapear datos de muchas dimensiones a una **grilla 2D** conservando la **topología** (puntos parecidos quedan cerca en la grilla).

| Característica | SOM | MLP |
| --- | --- | --- |
| Supervisión | **No supervisado** | Supervisado |
| Objetivo | Agrupar / visualizar | Predecir |
| Salida | Mapa 2D | Clase o valor |
| Backprop | **No usa** (regla propia de Kohonen) | Sí |

**Usos típicos:** visualizar datasets de alta dimensión en 2D, clustering avanzado con topología, análisis de clientes.

---

## 🛠️ Implementación con Keras (del notebook)

El esqueleto típico:

```python
from keras.models import Sequential
from keras.layers import Dense

model = Sequential()
model.add(Dense(16, activation='relu', input_shape=(n_features,)))  # oculta
model.add(Dense(8, activation='relu'))                               # oculta
model.add(Dense(n_clases, activation='softmax'))                     # salida

model.compile(optimizer='adam', loss='categorical_crossentropy', metrics=['accuracy'])
model.fit(X_train, y_train, epochs=50, batch_size=32, validation_split=0.2)
```

**Hiperparámetros clave:**

| Hiperparámetro | Qué controla |
| --- | --- |
| **Nº de capas ocultas** | "Profundidad" — más capas, más capacidad (y más riesgo de overfit) |
| **Nº de neuronas por capa** | "Ancho" — más neuronas, más capacidad |
| **Función de activación** | Default `relu` para capas ocultas |
| **Learning rate (η)** | Qué tanto se mueven los pesos por paso |
| **Épocas** | Cuántas pasadas al dataset completo |
| **Batch size** | Cuántos ejemplos ve antes de actualizar pesos (SGD con mini-batches) |
| **Optimizador** | `adam`, `sgd`, `rmsprop`… |
| **Función de pérdida** | `mse` (regresión), `binary_crossentropy` (binaria), `categorical_crossentropy` (multiclase) |

**Evaluación con cross-validation:** el notebook `redes_neuronales_keras_cross_validation.ipynb` arma la red con `KerasClassifier` + `KFold` + `cross_val_score` para tener una estimación honesta.

---

## ❓ Preguntas para autoevaluarte

1. ¿Qué hace una **neurona** con sus entradas? (pesos, suma, bias, activación)
2. ¿Qué rol cumple el **bias**? ¿Qué pasaría sin él?
3. En el perceptrón simple, ¿cómo se **actualiza un peso** después de equivocarse?
4. ¿Qué significa la **tasa de aprendizaje α**? ¿Qué pasa si es muy grande o muy chica?
5. ¿Por qué el perceptrón simple **no puede resolver XOR**? ¿Qué se agrega para resolverlo?
6. Diferenciá **sigmoide, tanh, ReLU y Softmax** — ¿cuándo usarías cada una?
7. ¿Qué activación va en la última capa si el problema es: regresión / clasificación binaria / clasificación multiclase?
8. ¿Qué es una **época** de entrenamiento? ¿Y un **batch**?
9. Explicá **backpropagation** en una frase — y después con tus palabras el rol de la **regla de la cadena**.
10. ¿Qué diferencia a una **red SOM (Kohonen)** de un MLP?
11. Si tu MLP overfitea, ¿qué hiperparámetros podrías tocar para regularizar?
12. ¿Qué guarda un modelo entrenado al final del día?

---

## 📌 Qué prestar atención en la clase

- La conexión **gradient descent (clase 08) → backpropagation** — es el mismo motor con regla de la cadena encima.
- El ejemplo del **XOR** como motivación para pasar del perceptrón simple al MLP.
- Las **funciones de activación** y en especial cuándo usar Softmax vs Sigmoide en la salida.
- La **regla de actualización** `w ← w + α · error · x` del perceptrón — la base conceptual de todo lo demás.
- El rol del **learning rate** (ya lo venís viendo desde la clase 08: aparece en gradient descent, en gradient boosting y acá).
- Las **SOM** como caso **no supervisado** — rompe el patrón de que "redes neuronales = supervisado".

---

<sub>⚙️ Guía basada en las PPTs *Redes Neuronales*, *Backpropagation* e *Implementación de Redes Neuronales* de la cátedra (Rodríguez), y en los notebooks `redes_neuronales_introducción.ipynb`, `redes_neuronales_keras_clasificacion.ipynb`, `redes_neuronales_keras_regresion.ipynb` y `redes_neuronales_keras_cross_validation.ipynb`.</sub>
