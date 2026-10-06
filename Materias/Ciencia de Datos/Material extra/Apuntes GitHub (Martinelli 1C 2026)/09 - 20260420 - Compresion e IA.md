## Clase 9 - Compresión e IA

### Teoría de la información

La entropía de Shannon nos da una noción del **desorden de las probabilidades**.  

```
Mejor cantidad de bits: -log_2(P(a))
```

``` 
Entropía: - Σ P(a) * log_2(P(a))
``` 

- El **ajuste de Laplace** se usa para calcular la probabilidad de eventos que nunca sucedieron.
- Los bits de un mensaje permiten saber **cuál es la mejor cantidad posible de bits** a gastar en el mensaje.
- La entropía de Shannon nos indica qué tan impredecible es un fenómeno.
- La entropía cruzada nos da una medida de **qué tan distintas son 2 distribuciones** de probabilidad.
- La divergencia de Kullback-Leibler nos da otra métrica de lo mismo.

### Compresión sin pérdida

#### Algoritmo de Huffman

- Crear encodings para cada caracter dependiendo de sus frecuencias. Se usan árboles binarios. 

#### Compresión aritmética

- Se usan bits partidos (con coma), para definir caracteres y combinaciones específicas.
- Mapear un archivo a un **intervalo**, y luego representar a ese intervalo con un **número**.
- Este sí llega a la optimalidad de la entropía de Shannon.
- Se asignan subintervalos en el intervalo 0 a 1, proporcionales a las probabilidades de cada caracter.
- Cuando aparece un caracter, me quedo con su subintervalo. Ahora el nuevo intervalo es ese, y se **distribuyen nuevamente la proporción de cada caracter con su subintervalo**.
- Hago eso con todos los caracteres.

### Compresiones LZ

Buscan repeticiones y patrones en los archivos.

- **LZ77**: se estipula una longitud mínima de posibilidad de repeticion de caracteres, y se reemplazan por un par `(distancia, longitud)`.
- **LZ78**: crea nuevos códigos para patrones de caracteres luego de los primeros 256 símbolos (del 0 al 255). 

### Complejidad de Kolmogorov

- `x`: string
- `K(x)`: cantidad mínima de bits para generar `x` (complejidad)

Decimos que `x` es un **archivo aleatorio** si y sólo si `K(x) = |x|`. En este caso la complejidad del *string* es igual a la longitud del mismo.

#### Propiedades

1. `K(x) >= 0`
2. `K(x) <= |x|`
3. `K(x) <= K(x) + K(y)`
4. `K(xy) >= K(x), K(xy) >= K(y)`
5. `K(xy) = K(yx)`
6. `K(xx) = K(x)`
7. `K(xy) + K(z) <= K(xz) + K(yz)`

#### Distancia de Kolmogorov

`KD(x,y) = K(xy) - min{K(x),K(y)}`

- En el mejor caso, la distancia nos da **0**.
- En el peor caso, nos da el máximo entre ambas complejidades
##### Distancia de Kolmogorov Normalizada:

`NKD(x,y) = (K(xy) - min{K(x),K(y)}) / max{K(x),K(y)}`

Nos puede llegar a servir para **encontrar cosas parecidas**.
##### Distancia de Comrpesión:

Ya que la distancia anterior es "intractable" (imposible de calcular), usamos una aproximación.

`NCD(x,y) = (C(xy) - min{C(x),C(y)}) / max{C(x),C(y)}`

`C`: obtenemos una complejidad (compresor) conocida, que pensamos que es la mejor.

### Inducción de Solomonoff

En la práctica suele usarse **inducción para Machine Learning**, ya que se puede aprender de los datos ya obtenidos.

**Epicúreo**: "si más de una teoría es consistente con los datos, quedate con todas".

**Navaja de Ockam**: "quedate con la teoría más simple que sea consistente con las observaciones".

```
Teorema de Bayes:
P(H|E) = (P(E|H)*P(H)) / P(E)
```

¿En qué consiste esta inducción?

1. **Recolectamos los datos** y ponemos en formato: entrada -> salida
2. **Iteramos todos los programas**: si terminan, miramos si su salida dada la entrada es lo que queríamos para ese dato.
3. **Actualizamos la probabilidad** de que cada programa sea el que explica el fenómeno.
4. **Repetimos**.

```
P(H|E) = (P(E|H)*P(H)) / P(E)
```

- `P(H|E)`: probabilidad de que el programa H genere los datos E.
- `P(E|H)`: probabilidad de ver la evidencia E dado que corremos el programa H.
- `P(H)`: probabilidad de que el programa H sea correcto.
- `P(E)`: probabilidad de que exista la evidencia E.

Tenemos matemáticamente que:

```
P(H0) = 2^(-K(H0))

P(E) = P(H0) + P(H1) + P(H2) + ... / P(E|Hi) = 1
Se consideran solo los programas H que generan esa eviencia E.
```
