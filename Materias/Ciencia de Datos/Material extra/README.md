# Material extra — Ciencia de Datos

Material **de terceros**, ya publicado por sus autores, que se guarda acá para tenerlo a mano y usarlo como fuente al armar las guías y el [Resumen para el parcial](../Gu%C3%ADas%20de%20estudio/Resumen%20para%20el%20parcial.md). **No es material propio**: los créditos son de quienes lo armaron.

> ⚠️ Las resoluciones y apuntes de otros alumnos **pueden tener errores**. Lo que se pasa a nuestras guías se revisa antes; si algo de acá contradice al resumen, vale el resumen (y si no, avisame y lo corregimos).

## Contenido

| Carpeta | Qué hay | Fuente |
| --- | --- | --- |
| `Parciales anteriores/` | Parciales de la **cátedra Rodríguez** (2022-06, 2022-10, 2023-10, recuperatorio 2023-11, 2025-10-16, recuperatorio 2025-11-20), un enunciado sin fecha y ejemplos de preguntas tipo parcial. Algunos con respuestas o resolución | Notion público de un alumno de la cátedra Rodríguez (2C 2025): [apuntes de Ciencia de Datos](https://scratched-lantern-d4a.notion.site/apuntes-de-Ciencia-de-Datos-257716c1daf48005b682f0ec543b4129) |
| `Finales anteriores/` | Final del 13-12-2022 | Mismo Notion |
| `Resúmenes de otros/` | `ResumenCiencia.pdf`: resumen de toda la materia | Mismo Notion |
| `Apuntes Notion (Rodríguez 2C 2025)/` | El texto completo del Notion: apuntes por clase, 60 preguntas tipo parcial con respuestas, material para el integrador y resúmenes por unidad | Mismo Notion |
| `Apuntes GitHub (Martinelli 1C 2026)/` | Apuntes por clase de **otra cátedra** (Martinelli): Pandas, Spark, ML, clustering, reducción de dimensiones, NLP, IA | [pedrociliberto/CienciaDeDatos-2026C1](https://github.com/pedrociliberto/CienciaDeDatos-2026C1/tree/main/cdd-apuntes) |

## Errores conocidos en este material

| Dónde | Qué dice | Lo correcto |
| --- | --- | --- |
| Notion — funciones de activación | Sigmoide para regresión | En regresión la salida es **lineal** |
| Notion — funciones de activación | Softmax para "N clases simultáneas" | Softmax = **multiclase excluyente**; para multi-label, **sigmoide por neurona** |
| Notion — regularización | El dropout "hace que la red dependa de unas pocas neuronas" | Es al revés: **evita** que dependa de pocas neuronas |
