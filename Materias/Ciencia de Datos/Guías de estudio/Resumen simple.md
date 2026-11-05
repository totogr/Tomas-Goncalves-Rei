# 🟢 Resumen simple — Ciencia de Datos

> **Para qué es este archivo:** para **entender** los temas antes de meterte con las cuentas. Casi no hay fórmulas: cada tema tiene la idea en palabras simples, las definiciones que hay que saber y un ejemplo cotidiano.
> Leelo **primero**, de corrido. Cuando las ideas cierren, pasá al [Resumen para el parcial](Resumen%20para%20el%20parcial.md), que tiene las fórmulas, los ejercicios a mano y los errores típicos.
>
> **Parcial:** fue el jueves 29/10 y entraron las partes 1 a 11. **Recuperatorio: jueves 19/11.**
> La parte 12 (lenguaje natural) se dio antes del parcial: **preguntá si entra en el recuperatorio**.
> Las partes 13 a 16 (embeddings, redes profundas, recurrentes e IA generativa) son de la segunda mitad del cuatrimestre.

---

## La materia en un párrafo

La ciencia de datos usa **datos del pasado** para **entender** qué pasa y **predecir** lo que va a pasar. El camino siempre es parecido:
1. Mirar los datos (graficarlos).
2. Limpiarlos y prepararlos.
3. Elegir un **modelo** que aprenda de ellos.
4. **Medir** si funciona con datos que nunca vio.

Casi todo lo que se ve en la materia son distintas formas de hacer el paso 3 y de controlar el paso 4.

---

## 1. Lo básico: qué es aprender de los datos

**La idea:** en lugar de escribirle a la computadora las reglas ("si el mail dice *gratis* es spam"), se le muestran **muchos ejemplos** y ella encuentra las reglas sola. Eso es **machine learning**.

**Definiciones:**
- **Variable (feature):** una columna del dataset (edad, precio, color).
- **Variable a predecir (target, variable dependiente):** la columna que queremos adivinar.
- **Modelo:** la "receta" que aprende la relación entre las variables y el target.
- **Entrenar:** dejar que el modelo aprenda mirando los ejemplos.
- **Parámetros:** lo que el modelo **aprende solo** (los pesos de una recta).
- **Hiperparámetros:** lo que **elegís vos** antes de entrenar (cuántos vecinos mirar, cuántos árboles usar).

**Los tipos de problema:**

| Tipo | Qué hace | Ejemplo |
| --- | --- | --- |
| **Clasificación** (supervisado) | Predice una **categoría** | ¿Este mail es spam o no? |
| **Regresión** (supervisado) | Predice un **número** | ¿Cuánto va a valer esta casa? |
| **Clustering** (no supervisado) | Arma **grupos** sin que nadie le diga cuáles son | Separar clientes en perfiles parecidos |

> 💡 **Supervisado** = los ejemplos vienen con la respuesta correcta (alguien los "etiquetó"). **No supervisado** = no hay respuesta, el modelo busca estructura por su cuenta.

**Estadística mínima:**
- **Media:** el promedio. Se deja llevar por los valores extremos.
- **Mediana:** el valor del medio. No le afectan los extremos.
- **Desvío estándar:** qué tan **desparramados** están los datos alrededor de la media.
- **Correlación:** si dos variables suben o bajan juntas. **Correlación no es causa:** en verano suben los helados y los ahogados, pero el helado no ahoga a nadie (las dos cosas las causa el calor).

---

## 2. Graficar los datos

**La idea:** antes de cualquier modelo, hay que **mirar** los datos. Los números resumen solos pueden engañar: hay conjuntos de datos con el mismo promedio y el mismo desvío que, graficados, son completamente distintos (el *Datasaurus*).

**¿Para qué graficamos?** (pregunta de examen) Para **entender** los datos, **encontrar patrones** y **comunicar** lo que vemos.

**Qué gráfico usar:**
- **Histograma:** cómo se reparten los valores de un número (edades, alturas).
- **Gráfico de barras:** cuántos hay de cada categoría.
- **Diagrama de dispersión (scatter):** si dos números se relacionan.
- **Box plot (diagrama de caja):** dónde está la mitad central de los datos y cuáles son los valores raros.
- **Gráfico de líneas:** cómo cambia algo en el tiempo.

**Dos trampas famosas:**
- **Paradoja de Simpson:** una tendencia que aparece en el total **se da vuelta** al separar por grupos. Siempre hay que mirar los subgrupos.
- **Sesgo de supervivencia:** analizar solo lo que "sobrevivió". En la Segunda Guerra se estudiaban los agujeros de los aviones que **volvían**; había que reforzar justamente donde **no** tenían agujeros, porque los que recibían tiros ahí no volvían.

---

## 3. Cómo saber si un modelo funciona

**La idea:** un modelo sirve si acierta con **datos nuevos**, no con los que ya vio. Es como un alumno: si memorizó los parciales viejos, saca 10 en esos, pero no sabe resolver uno nuevo.

**Definiciones:**
- **Train / test:** se separan los datos. Con una parte se entrena; con la otra, que el modelo **nunca vio**, se mide.
- **Cross-validation:** se parte el dataset en varios pedazos y se repite el entrenamiento rotando cuál pedazo se usa para medir. Da una medida más confiable.
- **Overfitting (sobreajuste):** el modelo **memorizó** los datos de entrenamiento, ruido incluido. Anda bárbaro en train y mal en test.
- **Underfitting (subajuste):** el modelo es **demasiado simple** y no aprende ni lo básico. Anda mal en los dos.

**La matriz de confusión** (pregunta de examen): una tabla que cruza lo que **era** con lo que el modelo **dijo**.
- **Verdaderos positivos y negativos:** aciertos.
- **Falso positivo:** una **falsa alarma** (dijo "tumor" y no había).
- **Falso negativo:** **se le escapó** (dijo "sano" y había tumor).

Un modelo es **bueno** si los aciertos (la diagonal) son grandes y los errores (fuera de la diagonal) son chicos.

**Las métricas, en palabras:**

| Métrica | Pregunta que responde |
| --- | --- |
| **Accuracy (exactitud)** | ¿Qué porcentaje acertó en total? |
| **Precisión** | Cuando dijo "sí", ¿cuántas veces era sí? |
| **Recall (sensibilidad)** | De todos los "sí" que había, ¿cuántos encontró? |
| **F1** | Un equilibrio entre precisión y recall |

> 💡 **Cuál importa depende del caso.** Detectar tumores → **recall**: que no se escape ninguno, aunque haya falsas alarmas. Etiquetar fotos para un álbum → **precisión**: mejor no etiquetar que etiquetar mal.
>
> ⚠️ **Accuracy engaña con clases desbalanceadas.** Si hay 1 fraude cada 1.000 operaciones, un modelo que dice siempre "no es fraude" tiene 99,9% de accuracy y **no sirve para nada**.

**En regresión** se mide **cuánto se equivoca en promedio** el número predicho respecto del real (MAE, MSE, RMSE).

---

## 4. Preparar los datos (feature engineering)

**La idea:** los datos reales vienen sucios: con huecos, errores, valores raros y columnas en escalas distintas. Prepararlos bien suele importar más que el modelo elegido.

**Datos faltantes** (cuando una celda está vacía):
- **Faltan al azar:** se pueden borrar esas filas sin problema.
- **Faltan por algo que explican otras columnas:** se pueden **rellenar** (imputar) usando esas columnas.
- **Faltan por el propio valor** (los que más ganan no dicen su sueldo): borrarlos **sesga** el análisis. Hay que entender por qué faltan.

**Outliers (valores atípicos):** valores muy alejados del resto.
- Antes de borrarlos, hay que preguntarse si son un **error de carga** o algo **real e importante**. Un fraude es justamente un valor raro.
- Hay valores que son normales en cada columna por separado pero raros **en la combinación**: un nene de 4 años no es raro, una persona de 1,90 m tampoco, pero un nene de 4 años de 1,90 m sí.

**Escalar (normalizar):** llevar las columnas a escalas parecidas.
- Si una columna es la edad (0 a 100) y otra el sueldo (0 a 1.000.000), el sueldo **domina** cualquier cálculo de distancia.
- **Hace falta** en los modelos que miden distancias (KNN, SVM, K-Means) y en las redes neuronales.
- **No hace falta** en los árboles.

**Categorías a números:** los modelos solo entienden números.
- **One-hot encoding:** una columna de "sí/no" por cada categoría.
- Con **200 categorías** se generan 200 columnas casi vacías, y eso trae problemas. Conviene agrupar las categorías poco frecuentes en "Otros".

---

## 5. Regresión lineal, descenso por gradiente y regresión logística

**Regresión lineal:** busca la **recta que mejor pasa** entre los puntos, es decir, la que comete el menor error total. Sirve para predecir números (precio según metros cuadrados).

**Descenso por gradiente:** la forma de encontrar los mejores parámetros **paso a paso**.
- **Ejemplo cotidiano:** estás en una montaña con niebla y querés bajar al valle. No ves el camino, pero sentís hacia dónde baja el terreno bajo tus pies. Das un paso en esa dirección, volvés a sentir, y así hasta llegar abajo.
- El **valle** es el punto de **menor error**.
- El **learning rate** es el **largo de cada paso**:
  - si es muy largo, saltás de un lado al otro del valle sin llegar;
  - si es muy corto, tardás una eternidad.

**Regresión logística:** a pesar del nombre, es un método de **clasificación** (pregunta de examen).
- Usa una curva en forma de "S" (la **sigmoide**) que devuelve un número entre 0 y 1, que se lee como **probabilidad**.
- Con un corte (por ejemplo 0,5) decide la clase: arriba del corte es "sí", abajo es "no".

---

## 6. Clustering: armar grupos (K-Means)

**La idea:** sin que nadie le diga las respuestas, el modelo **agrupa** los datos que se parecen.

**Cómo funciona K-Means:**
1. Elegís cuántos grupos querés (**K**).
2. Ponés K "centros" al azar.
3. Cada punto se va con el centro **más cercano**.
4. Cada centro se mueve al **medio** de sus puntos.
5. Se repiten los pasos 3 y 4 hasta que nada cambie.

**Tres herramientas** (pregunta de examen):
- **Hopkins:** ¿hay grupos **de verdad** o los datos están desparramados al azar? Se mira **primero**.
- **Método del codo:** ¿cuántos grupos conviene armar? Se grafica el error para distintos K y se elige donde la curva "dobla", como un codo. Es rápido.
- **Silhouette:** ¿cuántos grupos?, y además ¿qué puntos quedaron **mal asignados**? Es más preciso pero más lento.

**Limitaciones:**
- Hay que elegir K a mano.
- Encuentra grupos **redondeados**; falla con formas raras (medias lunas).
- El resultado depende del arranque al azar.
- Los grupos **no traen nombre**: interpretarlos es trabajo tuyo.

---

## 7. Árboles de decisión

**La idea:** un árbol hace **preguntas de sí o no**, una detrás de otra, hasta llegar a una respuesta. Funciona como el juego de "adiviná el personaje": "¿es hombre?", "¿tiene anteojos?"…

**Definiciones:**
- **Nodo:** una pregunta ("¿la edad es mayor a 30?").
- **Hoja:** la respuesta final (la clase).
- **Cómo elige la pregunta:** en cada paso elige la que **mejor separa** los datos, la que deja grupos más "puros" (todos de la misma clase).
- **Entropía y Gini:** dos formas de medir qué tan **mezclado** está un grupo. Cero quiere decir que está puro.
- **Poda:** cortar ramas que no mejoran el resultado con datos nuevos. Evita que el árbol memorice (overfitting).

**ID3, C4.5 y CART** son versiones del mismo algoritmo:
- **ID3:** la original, solo con categorías.
- **C4.5:** agrega números, datos faltantes y poda.
- **CART:** la que usa scikit-learn.

---

## 8. Ensambles: muchos modelos juntos

**La idea:** muchos modelos "regulares" que se equivocan **en cosas distintas**, combinados, pueden ser muy buenos.
- **Ejemplo cotidiano:** en una feria de 1906, cientos de personas intentaron adivinar el peso de una vaca. Casi nadie acertó, pero el **promedio** de todas las respuestas fue casi exacto.

**Dos estrategias:**

| | **Bagging** | **Boosting** |
| --- | --- | --- |
| Cómo | Muchos modelos **en paralelo**, cada uno con una parte distinta de los datos, y después **votan** | Modelos **en fila**: cada uno se concentra en **los errores del anterior** |
| Arregla | El overfitting (modelos que memorizan) | El underfitting (modelos demasiado simples) |
| Ejemplo | **Random Forest** | **AdaBoost, Gradient Boosting, XGBoost** |

- **Random Forest:**
  - muchos árboles, cada uno con **filas y columnas elegidas al azar**;
  - al final votan;
  - no sirve de mucho si hay **una variable que domina** todo, porque todos los árboles terminan iguales.
- **AdaBoost:**
  - árboles mínimos (una sola pregunta);
  - los ejemplos mal clasificados **ganan importancia** para el siguiente;
  - los árboles que aciertan más **pesan más** en la votación.
- **Gradient Boosting:**
  - el primer modelo predice el promedio;
  - cada árbol siguiente aprende **lo que le faltó** al anterior (el error) y lo va corrigiendo de a poco.
- **XGBoost:** Gradient Boosting **mejorado**: más rápido y con frenos para no memorizar.
- **Ensambles híbridos:** mezclan modelos **de distinto tipo**:
  - **votación:** cada modelo vota;
  - **stacking:** un modelo aprende a combinar a los demás;
  - **cascada:** cada modelo resuelve lo que el anterior no pudo resolver con seguridad.

---

## 9. KNN y SVM

**KNN (k vecinos más cercanos):**
- **La idea:** "decime con quién andás y te diré quién sos". Para clasificar un punto nuevo, mira a sus **k vecinos más cercanos** y elige la clase que más se repite entre ellos.
- **k chico:** se deja engañar por un vecino raro (overfitting).
- **k grande:** todo se vuelve un promedio y pierde detalle (underfitting).
- Hay que **normalizar**, porque trabaja con distancias.
- Aprende al instante (solo guarda los datos), pero **predice lento**: tiene que medir la distancia a todos.

**SVM (máquinas de vectores de soporte):**
- **La idea:** trazar la línea que separa las dos clases dejando la **calle más ancha posible** entre ellas.
- **Vectores de soporte:** los puntos que quedan **en el borde de la calle**. Son los únicos que definen la línea; el resto no importa.
- **Margen blando:** permite que algunos puntos queden mal ubicados, para no depender de un dato raro.
- **Kernel:** si los datos no se pueden separar con una línea recta, se los "levanta" a más dimensiones, donde sí se pueden separar.
  - **Ejemplo cotidiano:** puntos rojos en el centro de una mesa y azules alrededor; si levantás los del centro, una hoja horizontal los separa.

---

## 10. Redes neuronales

**La idea:** muchas "neuronas" simples conectadas en capas. Cada neurona:
1. recibe números;
2. los multiplica por unos **pesos** (cuánta importancia le da a cada uno) y los suma;
3. decide qué pasar a la siguiente capa.

**Definiciones:**
- **Perceptrón:** una sola neurona. Puede separar los datos con **una línea recta**.
  - Aprende **AND** y **OR**, pero **no XOR**: ninguna recta separa los casos de XOR (pregunta de examen).
  - Para eso hacen falta **más capas** (red multicapa, MLP).
- **Función de activación:** la regla con la que la neurona decide qué valor pasar. Las más usadas:
  - **ReLU** en las capas del medio;
  - **sigmoide** para un "sí/no" a la salida;
  - **softmax** para elegir **una entre varias** clases.
- **Backpropagation:** cómo aprende la red.
  1. Se mira el **error a la salida**.
  2. Se reparte "la culpa" hacia atrás, capa por capa.
  3. Se ajusta cada peso un poquito.

  Es el descenso por gradiente aplicado a toda la red.
- **Época:** una pasada completa por todos los datos de entrenamiento.
- **Regularización** (pregunta de examen): técnicas para que la red **no memorice**:
  - **L1 y L2:** castigan los pesos grandes.
  - **Dropout:** apaga neuronas al azar mientras entrena, para que no dependa de unas pocas.
  - **Early stopping:** corta el entrenamiento cuando empieza a empeorar con datos nuevos.
  - **Data augmentation:** fabrica más datos (rotar o agregar ruido a imágenes).
- **SOM (mapa de Kohonen):** una red **no supervisada** que acomoda datos complejos en una **grilla 2D**, dejando cerca lo parecido.

---

## 11. Reducción de la dimensionalidad

**La idea:** cuando hay **muchísimas columnas**, se buscan unas **pocas columnas nuevas** que resuman casi toda la información.
- **Ejemplo cotidiano:** un **planisferio** pasa la Tierra (3D) a un papel (2D); se pierde algo (Groenlandia parece gigante), pero sirve muchísimo.
- **El mapa del subte:** no respeta las distancias reales, pero conserva lo importante (el orden de las estaciones y las combinaciones).

**¿Para qué sirve?** (pregunta de examen) Sobre todo para **visualizar** datos de muchas dimensiones y para **acelerar el entrenamiento**. Además reduce el ruido y ayuda a presentar resultados.

**Las cuatro técnicas, en palabras:**

| Técnica | Qué intenta conservar | Cuándo usarla |
| --- | --- | --- |
| **PCA** | Hacia dónde están **más desparramados** los datos | Ver la **dispersión** y **qué variables** la explican. Es rápida |
| **MDS** | Las **distancias** entre cada par de puntos | Cuando importan las distancias, aunque sean de un tipo raro |
| **ISOMAP** | La forma de una **superficie curva** (como un rollo enrollado) | Cuando los datos viven sobre una forma curva |
| **t-SNE** | Los **grupos** (quién es vecino de quién) | Para **ver clusters** en 2D. Cada corrida da distinto |

**PCA un poco más de cerca:**
- Busca la dirección en la que los datos están **más estirados** (componente 1), después la perpendicular más estirada (componente 2), y así.
- **Ejemplo cotidiano:** sacarle una foto a un objeto desde el ángulo en que **más se nota su forma**.
- **Scree plot:** un gráfico de barras que muestra **cuánta información** guarda cada componente.
  - Si las dos primeras suman mucho (por ejemplo, 95%), un gráfico 2D representa bien los datos.
  - Si el gráfico es plano, se pierde mucho.
- **¿Cuándo usar PCA?** (pregunta de examen) Para ver si hay **agrupamientos** y **cuán dispersos** están los datos y **sobre qué variables**. Tiene sentido cuando las variables están **relacionadas entre sí**.

---

## 12. Procesamiento de lenguaje natural

> Se dio el 13/10 (teórica) y el 22/10 (práctica), antes del parcial. **Preguntá si entra en el recuperatorio.** Detalle en la [guía 11](11%20-%20Procesamiento%20de%20lenguaje%20natural.md).

**La idea:** enseñarle a la computadora a trabajar con **texto**. Por ejemplo:
- clasificar mails;
- detectar si una reseña es positiva o negativa;
- sacar nombres y fechas de un documento.

**Los pasos típicos:**
1. **Tokenizar:** partir el texto en palabras.
2. **Limpiar:**
   - pasar a minúsculas y sacar tildes;
   - sacar las **stop words**, palabras muy comunes que no aportan ("el", "de", "y");
   - opcionalmente, llevar cada palabra a su raíz (**stemming**, más bruto) o a su forma de diccionario (**lema**: "corrían" → "correr").
3. **Vectorizar:** convertir el texto en números.
   - **Bolsa de palabras:** cuenta cuántas veces aparece cada palabra, sin importar el orden.
   - **TF-IDF:** da más peso a las palabras **frecuentes en ese texto pero raras en el resto**. Una palabra que está en todos los textos pesa cero.
4. **Clasificar.**

**Naive Bayes (pregunta muy probable):**
- **Qué hace:** para cada clase (positiva, negativa) aprende **qué tan probable es cada palabra**. Ante un texto nuevo, multiplica las probabilidades de sus palabras y elige la clase que da más alto.
- **Por qué "ingenuo":** supone dos cosas falsas. Que el **orden no importa** y que las palabras **no dependen unas de otras** ("Buenos" casi siempre va con "Aires"). Igual funciona muy bien.
- **El suavizado de Laplace:** si una palabra nunca apareció en una clase, su probabilidad sería cero y anularía todo el producto. Por eso se **suma 1 a cada conteo**.

**Análisis de sentimientos:**
- **Qué detecta:** la **actitud** de quien escribe hacia algo: quién opina, sobre qué y si es positivo o negativo.
- **Trucos clásicos:**
  - marcar las palabras **negadas** ("no me NO_gustó NO_nada");
  - usar **todas** las palabras, no solo los adjetivos;
  - usar **diccionarios** de palabras positivas y negativas (lexicones como SentiWordNet).
- **Lo que le cuesta:** la ironía y las expectativas frustradas ("tenía todo para ser excelente, pero es aburridísima").

**Extracción de información:** sacar **datos ordenados** de un texto libre.
- **NER:** encontrar las **entidades**: personas, lugares, fechas, empresas.
- **Relaciones:** "Borges **nació en** Buenos Aires" → (Borges, nació en, Buenos Aires). Se pueden buscar con reglas, con ejemplos etiquetados o arrancando de unos pocos pares conocidos y aprendiendo los patrones solo.

---

## 13. Embeddings: palabras con significado

> Clase práctica del 5/11. Detalle en la [guía 14](14%20-%20Embeddings%20%28Word2Vec%29.md).

**La idea:** representar cada palabra con **una lista corta de números** (por ejemplo, 300) que capture su significado. Las palabras parecidas quedan **cerca** entre sí.

- **El ejemplo de la clase:** si describís a una persona con números (cuán extrovertida, cuán ordenada…), para encontrar a alguien parecido buscás a quien tenga **números parecidos**.
- **Por qué no alcanza con numerarlas:** con el "one-hot" (un 1 en la posición de la palabra y ceros en el resto), todas las palabras quedan **igual de lejos**. "Rey" está tan lejos de "reina" como de "burlete".
- **De dónde sale el significado:** de **con quién anda la palabra**. Las que aparecen en los mismos contextos significan cosas parecidas. Por eso alcanza con **mucho texto común**, sin etiquetar nada.
- **Word2Vec:** aprende los vectores tratando de **adivinar las palabras vecinas** (skip-gram) o **la palabra del medio a partir de las vecinas** (CBOW).
- **Lo sorprendente:** se puede hacer **álgebra**: **rey − hombre + mujer ≈ reina**, **Madrid − España + Francia ≈ París**.

---

## 14. Redes profundas: muchas capas

> Teórica del 3/11. Detalle en la [guía 12](12%20-%20Introducci%C3%B3n%20al%20aprendizaje%20profundo.md).

**La idea:** redes con **muchas capas** donde cada capa aprende algo más abstracto que la anterior: bordes → formas → caras.

- **Por qué tardaron tanto:** el ajuste de los pesos (el "reto" por el error) **se pierde** antes de llegar a las primeras capas, que entonces no aprenden. Ese es el **desvanecimiento del gradiente**. Además, entrenar llevaba meses: las **GPU** lo bajaron a días.
- **Cada red, para qué:**
  - **Convolucionales (CNN):** para **imágenes**. Pasan "filtros" que buscan patrones, achican la imagen y al final clasifican.
  - **Recurrentes:** para **secuencias** (texto, voz, series de tiempo).
  - **Autoencoders:** aprenden a **copiar** su entrada pasando por un "cuello de botella". Sirven para comprimir y para limpiar ruido.
  - **RBM y DBN:** aprenden patrones **sin etiquetas**, capa por capa.

---

## 15. Redes recurrentes: redes con memoria

> Semana del 10/11. Detalle en la [guía 15](15%20-%20Redes%20recurrentes%20%28RNN%2C%20LSTM%2C%20GRU%29.md).

**La idea:** una neurona que, además del dato nuevo, recibe **lo que ella misma dijo en el paso anterior**. Así "recuerda".

- **Ejemplo cotidiano:** entendés cada palabra de una novela por lo que ya leíste.
- **Sirven para:**
  - predecir el **próximo valor** de una serie (la temperatura de mañana);
  - leer una crítica y decir si es **positiva o negativa**;
  - ponerle **título** a una foto.
- **El problema:** tienen **memoria corta**. Las **LSTM** y **GRU** agregan "puertas" que deciden qué **olvidar** y qué **guardar**, como tomar notas al margen.
- **Robo-Shakespeare:** una red que leyó todo Shakespeare y aprende a **adivinar la próxima letra**. Repitiéndolo, **escribe** texto nuevo.

---

## 16. IA generativa: GAN y Transformers

> Detalle en la [guía 16](16%20-%20IA%20generativa%20-%20GAN%2C%20Deep%20Fakes%20y%20Transformers.md).

**La idea:** modelos que no clasifican, sino que **crean** datos nuevos: imágenes, texto, voz.

- **GAN:** un **falsificador** contra un **perito**. Una red inventa imágenes y otra trata de descubrir cuáles son falsas. Compitiendo, las dos mejoran hasta que las falsas parecen reales (como las caras de *thispersondoesnotexist.com*).
- **Deep fakes:** se entrena una red que "comprime y reconstruye" la cara de una persona. Después se reconstruye **con la cara de otra**: queda el gesto de la primera con la cara de la segunda.
- **Transformers (la "T" de ChatGPT):**
  - **Qué hacen:** adivinan **la próxima palabra**, la agregan y repiten. Así escriben.
  - **La atención:** cada palabra **mira a las demás** para entender su sentido. "Banco" se entiende distinto con "plaza" que con "plata".
  - **La posición:** le agregan a cada palabra **en qué lugar** de la frase está, porque si no, "el gato comió al ratón" sería igual que "el ratón comió al gato".
- **La temperatura:** un control de **creatividad**. Baja elige siempre lo más probable; alta se arriesga más.

---

## 🧠 Todo junto, en una tabla

| Tema | La pregunta que responde | La idea en una frase |
| --- | --- | --- |
| Graficar | ¿Qué hay en los datos? | Mirar antes de modelar; los promedios solos engañan |
| Evaluar | ¿El modelo sirve? | Medir con datos que no vio y elegir la métrica según el caso |
| Preparar datos | ¿Están listos? | Huecos, valores raros, escalas y categorías |
| Regresión lineal | ¿Cuánto va a valer? | La recta que menos se equivoca |
| Descenso por gradiente | ¿Cómo encuentra los parámetros? | Bajar la montaña del error paso a paso |
| Regresión logística | ¿Sí o no? | Una curva en S que da una probabilidad |
| K-Means | ¿Qué grupos hay? | Centros que se mueven hasta acomodarse |
| Árboles | ¿Qué decide? | Preguntas de sí o no en cadena |
| Ensambles | ¿Cómo mejoro? | Muchos modelos que se corrigen o votan |
| KNN | ¿A quién se parece? | Decime con quién andás… |
| SVM | ¿Dónde está la frontera? | La calle más ancha entre las clases |
| Redes neuronales | ¿Y si el problema es complejo? | Capas de neuronas que ajustan sus pesos hacia atrás |
| Reducción de dimensiones | ¿Cómo lo veo en 2D? | Pocas columnas nuevas que resumen todo |
| Lenguaje natural | ¿Qué dice este texto? | Contar palabras y multiplicar probabilidades (Naive Bayes) |
| Embeddings | ¿Qué significa esta palabra? | Números que dejan cerca a las palabras que se usan parecido |
| Redes profundas | ¿Y con imágenes o voz? | Muchas capas, cada una más abstracta |
| Recurrentes | ¿Qué viene después? | Neuronas con memoria del paso anterior |
| IA generativa | ¿Puede crear algo nuevo? | Falsificador contra perito (GAN) y adivinar la próxima palabra mirando el contexto (Transformers) |

---

<sub>⚙️ Versión simplificada de las [guías de la materia](README.md) y del [Resumen para el parcial](Resumen%20para%20el%20parcial.md). Si algo de acá no alcanza para responder una pregunta, la versión completa está ahí.</sub>
