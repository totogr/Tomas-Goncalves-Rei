# 02 · Visualización de datos (y falacias con los datos)

> 🧩 **Guía de estudio para llegar a la clase con el tema masticado.**
> Basada en las slides *Visualización de Datos* y *Falacias con los datos* (cátedra) y el temario del plan de estudios.

---

## 🎯 En una frase

Graficar no es "decorar": es **entender** los datos y **comunicarlos** sin mentir; para eso hay que elegir **el gráfico correcto según el tipo de dato** y estar alerta a las **falacias** (Simpson, sesgo de supervivencia) que hacen que los datos "digan" cosas falsas.

---

## 🧭 ¿Por qué importa / dónde encaja?

Es el primer paso de **todo análisis** (análisis descriptivo / EDA). Antes de modelar hay que **mirar** los datos: ¿hay outliers?, ¿hay relación entre variables?, ¿la distribución es rara? Y en la parte de falacias, aprendés a **desconfiar** de conclusiones apuradas — una habilidad que te salva en el TP y en la vida profesional.

---

## 💡 La idea con una analogía

El **Datasaurus** lo dice todo: varios conjuntos de datos con **la misma media y el mismo desvío estándar**… pero uno dibuja un dinosaurio y otro una estrella. Si solo mirás los números resumen, **no ves nada**; recién al graficar aparece la verdad. Moraleja: **los estadísticos solos engañan, el gráfico revela**.

---

## 🗺️ Qué gráfico usar según el dato

```mermaid
flowchart TD
    A["¿Qué querés mostrar?"] --> B["Distribución continua<br/>histograma · density · box plot · violin"]
    A --> C["Distribución discreta<br/>bar plot · stacked bar · treemap"]
    A --> D["Relación entre variables<br/>scatter · regression plot · heatmap"]
    A --> E["Evolución en el tiempo<br/>line plot"]
```

> ❌ **Error clásico:** confundir el **soporte** del dato (continuo vs. discreto) y elegir el gráfico equivocado.

### Cómo se ven los principales gráficos

![Panel con histograma, box plot, scatter y line plot](assets/02-tipos-de-graficos.svg)

---

## 📊 Conceptos clave

### Números resumen (para leer una distribución)

| Medida | Qué es |
| --- | --- |
| **Media** | El promedio |
| **Mediana** | El valor del medio de la población ordenada |
| **Cuartiles (Q1, Q2, Q3)** | Cortan la población en 25% / 50% / 75% |
| **Rango intercuartílico (IQR)** | El rango entre Q1 y Q3 (el 50% central) |

### Anatomía de un box plot (te lo van a pedir leer varias veces)

![Box plot con mediana, cuartiles, IQR, bigotes y outliers](assets/02-anatomia-boxplot.svg)

> Reglas: **IQR = Q3 − Q1**. Los **bigotes** llegan hasta Q1 − 1.5·IQR y Q3 + 1.5·IQR. **Todo lo que cae afuera** de eso se dibuja como punto → outlier.

### Los gráficos y para qué sirven

| Gráfico | Para qué | Ojo con… |
| --- | --- | --- |
| **Histograma** | Distribución continua por "bins" | Elegir bien la **cantidad de bins** y usar baseline en 0 |
| **Density plot** | Suaviza la distribución | Puede sugerir valores imposibles (ej. densidad < 0) |
| **Box plot** | Muestra cuartiles, IQR y **outliers** de un vistazo | — |
| **Bar plot / Stacked / Treemap** | Datos **discretos** / categorías | — |
| **Scatter plot** | Relación entre **dos variables** continuas | Ver si hay correlación |
| **Heatmap** | Dos ejes discretos + un valor numérico ("profundidad") | — |
| **Line plot** | Series de **tiempo** | — |
| **Violin plot** | Box plot + densidad (forma de la distribución) | — |

### ⚠️ Falacias con los datos (la parte más "peligrosa")

| Falacia | Qué es | Cómo evitarla |
| --- | --- | --- |
| **Paradoja de Simpson** | Una tendencia se **invierte** al separar los datos en grupos (ej. cirugía vs. método parece peor, pero le tocan los casos difíciles) | Segmentar bien; **asignación al azar** de los casos a cada grupo (experimento controlado) |
| **A/B testing (mal hecho)** | Un cambio "mejora" un 5%… ¿fue el cambio u otra cosa? | Dividir el tráfico **50/50** al mismo tiempo (grupo A vs. B) |
| **Sesgo de supervivencia** | Analizás solo lo que "sobrevivió" (los aviones que **volvieron**) y sacás la conclusión al revés | Preguntarte siempre: **¿cuál es el origen de mis datos?** |

> 🔑 Correlación **no** implica causalidad (ver [scatter plot] y los ejemplos de *spurious correlations*).

> 💡 En la grabación, a la asignación aleatoria se la llama "validación cruzada". Ojo: **validación cruzada** (*cross-validation*) es otra cosa — la técnica de rotar folds para evaluar un modelo (guía 04). Acá lo correcto es **asignación aleatoria / experimento A-B**.

---

## 🎯 Remarcado en clase (teórica 18/08)

> 🔴 **Pregunta de examen:** *"¿Por qué / para qué graficamos los datos?"*
> 1. Para **entender** los datos de forma eficiente.
> 2. Para **encontrar patrones o relaciones** entre variables.
> 3. Para **comunicar** de forma concisa y clara lo que vemos.
>
> Otras formas de resumir datos son el **análisis descriptivo** y la **agregación**; la visualización es además **parte del análisis**: sirve para chequear los supuestos de un método, detectar outliers, ver si hay linealidad y comparar lo predicho contra lo observado (residuos).

**Detalles que remarcó al explicar cada gráfico:**
- **Histograma ≠ bar plot:** el histograma es para un soporte **continuo** (sueldos, tiempo); para valores **discretos** (meses, cantidad de aumentos) va un **bar plot** o una torta.
- **Bins:** pocos bins esconden la forma (ej. una distribución **bimodal** parece una escalera); demasiados la rompen en ruido. Conviene un ancho **redondo** (2, 5, 10) para poder leer los cortes mentalmente.
- **Formas típicas** de un histograma: simétrica unimodal (campana de Gauss), sesgada a un lado, uniforme, bimodal, multimodal.
- **Empezar los ejes en 0:** recortar el eje no ahorra nada y hace el gráfico más difícil de interpretar (o engañoso).
- **Density plot:** muestra proporciones (el área total es 1), así que **perdés la cantidad absoluta** (¿100 o 5000 encuestados?). Al estar interpolado puede dibujar valores imposibles (salarios negativos).
- **Box plot:** **Q2 = mediana** (no la media). El rango intercuartílico concentra el 50% central.
- **Scatter plot con una variable discreta** (ej. años de experiencia enteros): los puntos se apilan en columnas y se lee mal → mejor un **box plot por categoría**.
- **Torta vs. barras:** la torta muestra bien la **completitud**; si querés agregar una variable más (ej. separar por profesión), barras agrupadas o **apiladas** funcionan mejor.
- **Heatmap:** el **color** agrega una dimensión (ej. profesión × años de experiencia, color = salario).
- **Violin plot:** como un box plot, pero mostrando la **densidad** a los costados.

### Las dos falacias en imagen

**Paradoja de Simpson**

![La tendencia global sube pero por grupo baja](assets/02-paradoja-simpson.svg)

**Sesgo de supervivencia** (los aviones de la 2ª Guerra Mundial: Abraham Wald)

![Silueta de avión con impactos donde volvieron y zonas sin impactos a reforzar](assets/02-sesgo-supervivencia.svg)

---

## ❓ Preguntas para autoevaluarte

1. ¿Qué demuestra el **Datasaurus** sobre confiar solo en media y desvío?
2. ¿Qué gráfico usarías para ver **outliers** de un vistazo? ¿Y para relación entre dos variables continuas?
3. ¿Qué es el **rango intercuartílico**? ¿Qué porcentaje de la población cubre?
4. Explicá la **paradoja de Simpson** con el ejemplo de la cirugía.
5. ¿Qué es el **sesgo de supervivencia**? ¿Cómo lo evitás?
6. ¿Cómo se hace bien un **A/B test**?
7. 🔴 ¿Para qué graficamos los datos? (tres respuestas)
8. ¿Qué pasa con un histograma si usás muy pocos bins? ¿Y demasiados?
9. ¿Por qué un **density plot** puede ser mala idea para mostrar cantidad de encuestados?
10. ¿Cuándo usás histograma y cuándo bar plot?

---

## 📌 Qué prestar atención en la clase

- El mapeo **tipo de dato → gráfico correcto** (continuo/discreto/relación/tiempo).
- Cómo leer un **box plot** (Q1, mediana, Q3, IQR, outliers) — se usa todo el tiempo.
- Las **tres falacias**: son ideas conceptuales que suelen tomarse y te hacen mejor analista.
- El mensaje de fondo: **graficá antes de concluir**; los números resumen engañan.

---

<sub>⚙️ Guía basada en las slides *Visualización de Datos* y *Falacias con los datos* de la cátedra.</sub>
