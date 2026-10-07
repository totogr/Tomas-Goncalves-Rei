# Material extra — Redes

Material de **terceros** (otros alumnos de la cátedra Volpi / Giorgi / Llasat), público en su origen y guardado acá para tenerlo a mano. Lo que sirve se vuelca, **revisado contra las PPTs de la cátedra**, en las [guías de estudio](../Gu%C3%ADas%20de%20estudio/), el [Resumen para el parcial](../Gu%C3%ADas%20de%20estudio/Resumen%20para%20el%20parcial.md) y los [Parciales anteriores resueltos](../Gu%C3%ADas%20de%20estudio/Parciales%20anteriores%20resueltos.md).

> ⚠️ Son apuntes de alumnos y respuestas armadas en parte con IA: tienen errores (ver abajo). Ante una duda, manda la PPT de la cátedra.

## Contenido

| Carpeta / archivo | Qué es | Fuente |
| --- | --- | --- |
| [`Parciales y finales anteriores/Consignas de parciales (modelos 1 a 5, con respuestas).docx`](Parciales%20y%20finales%20anteriores/) | **Consignas de 5 parciales** (incluye los múltiple choice de E1, STM-1, STM-4, TDM y OM5) con respuestas | Compartido por compañeros |
| [`Parciales y finales anteriores/Recopilación de finales y resúmenes (1C 2026).pdf`](Parciales%20y%20finales%20anteriores/) | 161 páginas: resúmenes por clase, **finales del 01/07/26 y 06/07/26 resueltos**, banco de 26 preguntas de final con resolución | Compartido por compañeros |
| [`Apuntes GitHub (Ciliberto 1C 2026)/`](Apuntes%20GitHub%20%28Ciliberto%201C%202026%29/) | Apuntes por clase (en Markdown) + **`Repaso Parcial 29-05.md`** (preguntas de parcial con respuesta, clases 1 a 11) + `Repaso Final 06-07` | [pedrociliberto/Redes-2026C1](https://github.com/pedrociliberto/Redes-2026C1) |
| [`Apuntes GitHub (González Alejo 1C 2025)/`](Apuntes%20GitHub%20%28Gonz%C3%A1lez%20Alejo%201C%202025%29/) | Apuntes por clase en PDF (1C 2025) | [c-gonzalez-a/Redes](https://github.com/c-gonzalez-a/Redes) |
| [`Apuntes Notion/`](Apuntes%20Notion/) | Texto completo de la Notion: preguntas y respuestas por clase, **"Práctica para el parcial"** (7 secciones + anexo con preguntas de parcial reales) y "Práctica para el integrador" | [Notion "Apuntes de Redes"](https://scratched-lantern-d4a.notion.site/Apuntes-de-Redes-253716c1daf480359ca4fbc57e326473) |

**No se guardó:**
- **La sección "Cátedra Hamelin" de la Notion:** es **otra cátedra**, con otro temario (Wireshark, subnetting, fragmentación).
- **Las diapositivas del 1C 2026:** ya tenemos las de la cursada actual.
- **El TP de Wi-Fi 6:** es tema del integrador, no del parcial.

## Ojo: cosas que estas fuentes dicen mal o distinto

| Dice | Lo correcto (según la cátedra) |
| --- | --- |
| Hamming de "1011" → "1010011" (docx, modelo 5) | Con la convención de la cátedra (K1 K2 M3 K4 M5 M6 M7), 1011 se codifica **0110011**. El ejemplo de la PPT: se transmite **1101001**, se recibe **1101011** → error en la posición **6** |
| Alcance de la multimodo "400–500 m" (docx) o "550 m" (Notion) | Depende de la velocidad y del tipo de OM: en la tabla de la PPT (40G a 400G) va de **70 a 440 m**. Para el parcial: multimodo = **cientos de metros**, monomodo = **kilómetros** |
| La disponibilidad del pilar "Disponibilidad de red" es "mayor a 99,9%" (apuntes 1C 2026) | En la PPT 2026 dice **mayor a 99,99%** |
| Ubica MPLS como protocolo de capa 2 | Es de **capa 2,5**: la etiqueta va **entre** la cabecera de capa 2 y la de capa 3 |
| Notion: PoE "Tipo 4" de 90–100 W | En la PPT son **90 W** (802.3bt Type 4 / Power over HDBaseT). La escala completa: **15 W** (Type 1, 802.3af) · **30 W** (Type 2, 802.3at, PoE+) · **60 W** (Type 3, 802.3bt) · **90 W** (Type 4) |
| T1 = 1,536 Mbps (PPT 02) | 24 × 64 kbps = 1,536 Mbps es la **carga útil**; la línea T1 completa son **1,544 Mbps** (con los bits de trama). Para el parcial alcanza con "≈ 1,5 Mbps" |
