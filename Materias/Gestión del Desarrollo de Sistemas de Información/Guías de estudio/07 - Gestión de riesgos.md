# 07 · Gestión de riesgos

> 🧩 **Guía de estudio para llegar a la clase con el tema masticado.**
> Basada en el cronograma de la cátedra (clase 7) y el libro base ***Artful Making***. Lectura previa: documento de gestión de riesgos (campus).

---

## 🎯 En una frase

Gestionar riesgos es hacer **explícito lo que puede salir mal** —anotarlo, priorizarlo y planear cómo mitigarlo— usando herramientas como la **planilla de riesgos**, **MoSCoW** para priorizar y **buffering** para dejar colchón ante la incertidumbre.

---

## 🧭 ¿Por qué importa / dónde encaja?

Todo proyecto de software vive rodeado de incertidumbre; ignorarla no la hace desaparecer. Esta clase le da **método**: en vez de cruzar los dedos, listás los riesgos y los atacás por prioridad. Cierra el **release plan** de cada equipo. Acá va la **entrega v2**.

---

## 💡 La idea con una analogía

Es como **planear un viaje largo en auto**: no salís esperando que nada falle. Anotás los riesgos (se pincha una goma, hay ruta cortada), su **probabilidad e impacto**, y llevás **colchón**: rueda de auxilio, tiempo de sobra, rutas alternativas. El **buffering** es justamente ese colchón; **MoSCoW** es decidir qué llevar sí o sí (Must) y qué es opcional (Won't).

---

## 🗺️ El ciclo de gestión de riesgos

```mermaid
flowchart LR
    A["Identificar<br/>riesgos"] --> B["Evaluar<br/>probabilidad × impacto"]
    B --> C["Priorizar<br/>(MoSCoW)"]
    C --> D["Mitigar / planear<br/>respuesta + buffer"]
    D --> E["Monitorear<br/>y actualizar"]
    E --> A
```

---

## 📊 Conceptos clave

| Herramienta | Qué es |
| --- | --- |
| **Planilla de riesgos** | Lista con cada riesgo, su **probabilidad**, **impacto** y **plan de respuesta** |
| **Probabilidad × impacto** | Cómo se prioriza un riesgo: no todos merecen la misma atención |
| **MoSCoW** | Priorización: **Must** (imprescindible), **Should** (importante), **Could** (deseable), **Won't** (esta vez no) |
| **Buffering** | Reservar **colchón** (de tiempo, alcance o recursos) para absorber lo imprevisto |
| **Mitigación** | Acción para **reducir** la probabilidad o el impacto de un riesgo |

> 🔑 Conexión con *Artful Making*: si el **cambio es barato** y trabajás en ciclos cortos, muchos riesgos se **descubren y corrigen temprano**, cuando cuesta poco.

### La matriz probabilidad × impacto (con qué priorizás)

![Matriz de riesgos con zonas verde/amarillo/naranja/rojo y ejemplos ubicados](assets/07-matriz-riesgos.svg)

> Sirve como semáforo: los que caen en **rojo** exigen plan B o evitarlos antes del sprint; los verdes se aceptan y listo.

---

## 📄 Lo que dice el documento de riesgos de la cátedra

- **Definición:** "proceso sistemático de **identificar, evaluar, priorizar y controlar** amenazas que podrían afectar el éxito del producto".
- **Tipos de amenaza:** factibilidad técnica, atractivo para los usuarios, sostenibilidad del negocio y calidad.
- **Siempre** se gestionan riesgos, con distinto detalle. Aparecen al **priorizar el story map**.
- **Los releases frecuentes son un mitigador:** validan hipótesis temprano y reducen la exposición acumulada.
- **Exposición = probabilidad × impacto.**
  - El documento usa una matriz con valores 1, 3 y 5 (de 1 a 25) y sugiere **priorizar exposición > 10**.
  - Ejemplo: "dependencia de un proveedor externo": media (3) × alto (5) = **15**.
- **Mecanismos:**
  - **mitigación** (antes de que ocurra);
  - **transferencia** (a un tercero);
  - **contingencia** (plan para cuando ocurra; por ejemplo, rollback);
  - **evitar**;
  - **aceptación** (monitorear, si la exposición es baja).
- **Ejemplo del documento:** integración con otras aplicaciones de la empresa.
  - **Mitigar** con releases modulares.
  - **Transferir** al soporte de un experto.
  - **Contingencia:** rollback.
- **Recomendaciones:** foco en los más importantes, herramientas simples, integrarlo a la planificación ("¿qué riesgos ven en este release?").

> ⚠️ **El documento tiene dos inconsistencias:** habla de una matriz "5×5" pero la tabla es 3×3, y da dos umbrales distintos (> 20 "alto" y > 10 "priorizar"). En un examen, explicitá la escala que uses.

Detalle en el [Resumen para la Evaluación 1](Resumen%20para%20la%20Evaluaci%C3%B3n%201.md#8-gestión-de-riesgos-).

---

## ❓ Preguntas para autoevaluarte

1. ¿Qué campos tiene una **planilla de riesgos**?
2. ¿Cómo se **prioriza** un riesgo? (pista: dos factores)
3. Explicá **MoSCoW** con un ejemplo de tu TP.
4. ¿Qué es el **buffering** y para qué sirve?
5. ¿Cómo ayuda trabajar en **ciclos cortos** a gestionar el riesgo?

---

## 📌 Qué prestar atención en la clase

- Cómo se **arma y prioriza** una planilla de riesgos (probabilidad × impacto).
- **MoSCoW** aplicado tanto a riesgos como a alcance (vuelve en gestión de proyectos, clase 10).
- El concepto de **buffer** y por qué no es "planificar de más".
- Preparar la **entrega v2** (mapa de historias + release plan con riesgos).

---

<sub>⚙️ Guía basada en el cronograma de la cátedra y *Artful Making* (Austin & Devin).</sub>
