# 03 · Introducción a la ciencia de datos

> 🧩 **Guía de estudio para llegar a la clase con el tema masticado.**
> Basada en la slide *Introducción a la ciencia de datos 01* (Dr. Ing. Juan M. Rodríguez) y el temario del plan de estudios.

---

## 🎯 En una frase

La ciencia de datos busca **aprender patrones a partir de datos** para describir lo que pasó o predecir lo que va a pasar; todo empieza por entender **qué tipo de variables** tenés, porque eso define **qué tipo de problema** estás resolviendo y **qué modelo** te conviene.

---

## 🧭 ¿Por qué importa / dónde encaja?

Esta clase es **la brújula conceptual** de la materia. Antes de tocar cualquier algoritmo (árboles, K-NN, SVM, redes neuronales…) necesitás saber leer un dataset: qué son las variables, si el problema es de clasificación / regresión / agrupamiento, y si tus datos están "sanos". Es el paso que decide *todo lo que viene después*.

```
Visualización de datos (clase previa)
        │
        ▼
  👉 Introducción a la ciencia de datos (esta clase)
     · tipos de variables · tipos de problemas · outliers · correlación
        │
        ▼
Modelos: regresión → árboles → K-NN/SVM → ensambles → redes neuronales
```

---

## 💡 La idea con una analogía

Pensá en un **médico frente a un paciente**:

- Primero **mira los síntomas** (las *variables*: fiebre sí/no, edad, presión…).
- Según qué quiere responder, el problema cambia: *"¿qué enfermedad tiene?"* (elegir de una lista → **clasificación**), *"¿cuántos días de recuperación?"* (un número → **regresión**), *"¿qué pacientes se parecen entre sí?"* (sin respuesta previa → **agrupamiento**).
- Y antes de confiar en los datos, **descarta mediciones raras** (un termómetro que marcó 50°C → *outlier*).

La ciencia de datos hace exactamente eso, pero con algoritmos y a escala.

---

## 🗺️ El árbol de decisión del tipo de problema

```mermaid
flowchart TD
    A["¿Tengo una variable dependiente<br/>(una 'respuesta' a predecir)?"] -->|No| G["🟣 AGRUPAMIENTO<br/>(clustering)<br/>agrupar por similitud"]
    A -->|Sí| B["¿De qué tipo es esa variable?"]
    B -->|Cualitativa<br/>categorías| C["🔵 CLASIFICACIÓN<br/>ej: spam / no spam"]
    B -->|Cuantitativa<br/>un número| D["🟢 REGRESIÓN<br/>ej: precio de una casa"]
```

> 🔑 **La regla de oro de la clase:** el **tipo de la variable dependiente** decide el tipo de problema. Cualitativa → clasificación · Cuantitativa → regresión · Sin variable dependiente → agrupamiento.

---

## 📊 Conceptos clave

### Tipos de variables

| | | Subtipo | Ejemplo |
| --- | --- | --- | --- |
| **Cualitativas** | (texto / categorías) | **Nominales** (sin orden) | países, colores |
| | | **Ordinales** (con orden) | poco / mucho / muchísimo |
| **Cuantitativas** | (números) | **Discreta** (contable) | cantidad de hijos |
| | | **Continua** (medible) | altura, temperatura |

También se distinguen por su rol:
- **Independientes** = las **entradas** (lo que uso para predecir).
- **Dependiente** = la **salida** (lo que quiero predecir).

### Herramientas para entender los datos

| Concepto | Qué mide / para qué | Ojo con… |
| --- | --- | --- |
| **Outlier** (valor atípico) | Un dato que se aleja mucho del resto | Puede ser un error o un caso real importante |
| **Varianza** | Cuánto se dispersan los datos respecto de la media | Se divide por *n-1* (no *n*) al estimar sobre una muestra |
| **Desvío estándar** | La dispersión, en las mismas unidades que los datos | Bajo = datos agrupados cerca de la media |
| **Covarianza** | Si dos variables varían juntas respecto de sus medias | Es la base para calcular la correlación |
| **Correlación de Pearson (r)** | Cuán relacionadas están **linealmente** dos variables | r=0 sin correlación · r=1 perfecta positiva · r=-1 perfecta negativa |

### ⚠️ La trampa más importante

> **Correlación NO implica causalidad.**
> Que dos variables se muevan juntas no significa que una cause la otra. Puede haber una **tercera variable** que empuja a ambas, o ser puro **azar** (hay ejemplos famosos de correlaciones absurdas, como consumo de queso vs. accidentes). Tenerlo grabado: es la fuente de error más común al interpretar datos.

---

## 🎯 Remarcado en clase (teóricas 25/08 y 01/09)

> 🟠 El profe dijo que estos conceptos son **fundamentales** y que hay que **dominarlos bien**: tipos de problema, supervisado vs. no supervisado, y cómo se entrena y evalúa un modelo.

### Supervisado vs. no supervisado

| | Supervisado | No supervisado |
| --- | --- | --- |
| **Dataset** | **Etiquetado**: cada fila trae la salida esperada (la "etiqueta") | Sin etiqueta |
| **Problemas** | **Clasificación** y **regresión** | **Clustering**, reducción de dimensionalidad, asociación |
| **Modelos de la materia** | Regresión lineal/logística, árboles, KNN, SVM, ensambles, MLP | K-Means, redes SOM, PCA |
| **Métricas** | Claras (precisión, recall, MSE…) — se necesitan para entrenar y comparar | Dependen del problema |

**En clasificación las clases son finitas y conocidas de antemano.** El modelo solo puede devolver clases con las que se entrenó: si entrenaste MNIST con los dígitos 0–9, un garabato o una letra **igual va a salir como algún dígito**. Para detectar "no es un dígito" habría que sumar esa clase y entrenarla con ejemplos.

### ¿De dónde vienen los modelos?

| Origen | Ejemplos |
| --- | --- |
| **Matemática previa a la computación** (cálculos a mano, algunos de los siglos XVIII–XIX) | Regresión lineal (mínimos cuadrados, Gauss 1805), análisis discriminante, PCA |
| **Mezcla de matemática e informática** | ID3, K-Means, Naive Bayes |
| **Propios de la informática** | Redes neuronales, SVM |

> 🟢 El dataset **Iris** (Ronald Fisher, 1936) es de la era pre-informática y viene precargado en casi todas las bibliotecas; **MNIST** (dígitos manuscritos de 28×28 píxeles en escala de grises) es el otro dataset "de prueba" que se repite en toda la materia.

### ¿Qué hago con un outlier?

Antes de borrarlo, tres preguntas:
1. **¿Es genuino o un error?** Un error de carga (una persona de 18 m por una coma olvidada) o una **combinación imposible** (1,80 m de altura con 2 años de edad) se corrige o se saca. Un caso **real pero excepcional** (alguien que gana muchísimo) es genuino.
2. **¿Me interesa?** Si quiero predecir el caso general, puedo sacarlo del entrenamiento aunque sea genuino. Pero a veces **el outlier es justo lo que busco**: un **fraude** es casi un outlier entre millones de transacciones.
3. **¿Mi modelo lo tolera?** Hay modelos muy sensibles (KNN, el clasificador de margen máximo de SVM) y otros que los toleran mejor (los ensambles de bagging).

### Variables: cómo conviene representarlas

- Que una variable **nominal** se codifique con números (Argentina = 1, Uruguay = 2) **no la vuelve cuantitativa**: no tiene sentido sumar ni promediar esos números.
- Una variable que en el fondo es discreta (el tiempo medido en milisegundos) se puede **modelar como continua** si eso ayuda a resolver el problema: lo que importa es cómo te conviene representarla para el modelo (las redes neuronales, por ejemplo, trabajan muy bien con valores continuos).
- Dos columnas pueden **depender** entre sí (latitud/longitud y código postal): en ese caso conviene quedarse con una o combinarlas.

### Pearson y desvío estándar, sin mezclarlos

- **Pearson** = covarianza(X, Y) / (desvío X · desvío Y). Mide **solo relación lineal**; dos variables podrían estar relacionadas de forma no lineal y dar r ≈ 0.
- El **desvío estándar** de una sola variable dice qué tan **concentrados** están los datos cerca de la media (bajo) o qué tan **desparramados** (alto). Es un dato aparte de la correlación: Pearson solo lo usa para normalizar.

---

## ❓ Preguntas para autoevaluarte

1. Si mi variable dependiente es "categoría de producto", ¿es un problema de clasificación o de regresión? ¿Y si es "precio en pesos"?
2. ¿Qué diferencia hay entre una variable **nominal** y una **ordinal**? Dame un ejemplo de cada una.
3. ¿Por qué al estimar la varianza de una muestra se divide por *n-1* y no por *n*?
4. Si dos variables tienen r = 0.95, ¿puedo afirmar que una causa la otra? ¿Por qué?
5. ¿Qué es un outlier y por qué no siempre conviene eliminarlo?
6. ¿En qué se diferencian covarianza y correlación de Pearson?
7. ¿Qué diferencia hay entre un modelo **supervisado** y uno **no supervisado**? Dame dos ejemplos de cada uno.
8. Si entrenás un clasificador de dígitos y le pasás una letra, ¿qué devuelve? ¿Por qué?
9. Te aparece un outlier en el dataset de fraudes: ¿lo sacás? ¿Qué preguntas te hacés antes?
10. Si codifico "país" como 1, 2, 3…, ¿se vuelve una variable cuantitativa?

---

## 📌 Qué prestar atención en la clase

- El **mapa "tipo de variable → tipo de problema"**: es la base para elegir modelos toda la cursada.
- La **fórmula de la varianza** y por qué el *n-1* (el profe suele detenerse acá).
- Los **ejemplos de correlaciones sin sentido**: entender *por qué* fallan, no solo reírse del gráfico.
- Anotá qué **tratamiento** se le da a los outliers (detectar, corregir, eliminar o dejar) — se retoma en limpieza de datos.

---

<sub>⚙️ Guía basada en la slide *Introducción a la ciencia de datos 01* (Dr. Ing. Juan M. Rodríguez) y las teóricas del 25/08 y 01/09.</sub>
