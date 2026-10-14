# 11 · Estimaciones, seguimiento y control

> 🧩 **Guía de estudio para llegar a la clase con el tema masticado.**
> Basada en el cronograma de la cátedra (clase 11) y el libro base ***Artful Making***. Lectura previa: estimaciones — tipos y herramientas (campus).

---

## 🎯 En una frase

Estimar es **anticipar esfuerzo o tiempo** bajo incertidumbre: se puede hacer con **estimación relativa** (comparar tareas entre sí, sin horas exactas), llevarlo al extremo con **#NoEstimates** (medir flujo real en vez de estimar), y hacer **seguimiento y control** con indicadores de gestión y calidad.

---

## 🧭 ¿Por qué importa / dónde encaja?

Estimar mal es una de las causas clásicas de proyectos fallidos. Esta clase muestra enfoques **ágiles** de estimación (menos precisión falsa, más comparación) y cómo **seguir el avance** con datos. Acá va la **entrega final del TP**. Según el cronograma v0.5, el 9/11 es **feriado** (el material queda en el campus) y el **recuperatorio de la Evaluación 1** pasó al 16/11.

---

## 💡 La idea con una analogía

Estimar en horas exactas es como decir *"esta mochila pesa 4,3 kg"* a ojo: casi seguro te equivocás. La **estimación relativa** es más honesta: *"esta mochila pesa como el doble que aquella"*. No necesitás la balanza (horas), solo **comparar**. Por eso se usan **story points** o tallas de remera (S/M/L): el equipo acuerda tamaños relativos, que resultan **más confiables** que adivinar horas.

---

## 🗺️ Enfoques de estimación

```mermaid
flowchart TD
    A["Estimar el trabajo"] --> B["Estimación relativa<br/>comparar tareas<br/>(story points, tallas)"]
    A --> C["Estimación absoluta<br/>horas/días<br/>(precisión ilusoria)"]
    A --> D["#NoEstimates<br/>no estimar: medir el<br/>flujo real y proyectar"]
```

---

## 📊 Conceptos clave

| Concepto | Qué es |
| --- | --- |
| **Estimación relativa** | Comparar tareas entre sí (una es "el doble" de otra) en vez de dar horas. Más confiable |
| **Story points** | Unidad **relativa** de esfuerzo/complejidad (no son horas) |
| **Velocidad (velocity)** | Cuántos puntos completa el equipo por sprint → sirve para **proyectar** |
| **#NoEstimates** | Corriente que propone **no estimar**: medir cuánto tarda de verdad cada ítem y usar ese dato |
| **Seguimiento por producto** | Controlar el avance por **lo entregado y funcionando**, no por tareas marcadas |
| **Indicadores de gestión y calidad** | Métricas para saber si el proyecto va bien (avance, defectos, etc.) |

> 🔑 La trampa a evitar: la **precisión ilusoria**. Una estimación en horas parece exacta pero suele fallar; la relativa asume la incertidumbre en lugar de esconderla.

---

## 📒 Lo que dio la cátedra (2C 2025 y 1C 2026, apuntes de alumnos)

- **Estimación vs. presupuesto:**
  - la **estimación** es un **pronóstico**, idealmente un **rango** ("entre 3 y 5 días");
  - el **presupuesto** es un **compromiso** entre partes y suma un margen por riesgo ("lo tengo en 7 días").
- **Un buen estimador es insesgado:** tiene la misma probabilidad de errar por exceso que por defecto. Atar bonos a cumplir estimaciones las sesga hacia arriba.
- **Enfoques:**
  - **predictivo:** situaciones conocidas;
  - **adaptativo:** alta incertidumbre, lo común en software;
  - **estadístico:** distribuciones conocidas.
- **Qué se estima:** duración, costo, esfuerzo y el objeto (proyecto, hito, requerimiento, tarea).
- **Técnicas:**
  - **análoga:** comparar con un proyecto parecido; poca información, al inicio;
  - **paramétrica:** escalar un dato histórico con parámetros;
  - **PERT (3 puntos):** optimista, más probable y pesimista;
  - **Delphi:** rondas anónimas de expertos hasta el consenso;
  - **planning poker:** estimación relativa en equipo, votando a la vez para evitar sesgos;
  - **story points**;
  - **Monte Carlo:** simular miles de veces con datos históricos y dar **percentiles**; el **P85** es el estándar para comprometerse;
  - **#NoEstimates:** tareas chicas y de tamaño parecido, y proyectar con **throughput y lead time** reales.
- **Takt time:** tiempo promedio entre dos entregas sucesivas. Tiempo total ≈ N ítems × takt, pero conviene usar su **distribución**, no solo el promedio.
- **Curva en Z:** la entrega no es lineal.
  - **Primer 20%:** lento (aprendizaje, ambientes).
  - **60% del medio:** ritmo sostenido.
  - **Último 20%:** lento (defectos, cierre).
- **Seguimiento:**
  - **producto:** satisfacción, uso por característica, ganancias;
  - **proceso:** avance y las métricas DORA;
  - **equipo:** satisfacción, rotación.
  - **Indicadores "de futuro":** tiempo de revisión de código, estabilidad de requisitos, tamaño de los PR, tasa de retrabajo.

> ⚠️ La prelectura de estimaciones trae una **tabla comparativa generada por IA** (época, enfoque, precisión y esfuerzo de cada técnica). La cátedra pide **"desafiar cada afirmación"**. Por ejemplo, poner la precisión de Monte Carlo como "muy alta" depende por completo de la calidad de los datos históricos.

---

## ❓ Preguntas para autoevaluarte

1. ¿Qué diferencia hay entre estimación **relativa** y **absoluta**? ¿Por qué la relativa suele ser mejor?
2. ¿Qué son los **story points** y por qué **no** son horas?
3. ¿Para qué sirve la **velocidad** del equipo?
4. ¿Qué propone **#NoEstimates**?
5. ¿Por qué conviene hacer **seguimiento por producto entregado** y no por tareas?

---

## 📌 Qué prestar atención en la clase

- La lógica de la **estimación relativa** (story points, comparación).
- El concepto de **velocidad** para proyectar entregas.
- El enfoque **#NoEstimates** y cuándo tiene sentido.
- ⚠️ El 9/11 es **feriado**: no hay clase, pero sí material y la **guía de prácticos 5**. Ese día vence la **entrega final del TP** (videos).

---

<sub>⚙️ Guía basada en el cronograma de la cátedra y *Artful Making* (Austin & Devin).</sub>
