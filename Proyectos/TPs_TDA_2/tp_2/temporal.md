Para demostrar, empı́ricamente, la complejidad planteada teóricamente tomamos mediciones
de tiempo de ejecución del algoritmo con distintos tamaños de muestra.

Para demostrar que el algoritmo planteado es efectivamente O(n2) realizamos ajustes por cuadrados minimos a las mediciones obtenidas, como podemos ver en la figura 1 el ajuste cuadratico ajusta casi a la perfeccion a los resultados obtenidos en comparativa con el ajuste lineal que se puede ver no ajusta con gran precision. Para reafirmar esto podemos ver en la figura 2 la comparativa de errores absolutos tanto del ajuste lineal como el cuadratico, podemos ver los errores cuadraticos practicamente nulos, muy cercanos al 0 mientras que los errores lineales van de un rango a , b reafirmando la complejidad teorica previamente planteada.


Variabilidad de los datos.

variamos los datos y tomamos mediciones en busca de optimizaciones temporales del algoritmo. Tomamos dos variaciones, dejamos F fijo y aumentamos los valores de X y el caso contrario dejando X fijo y aumentando los valores de F, como se puede ver en la figura no hay variacion en los tiempos obtenidos, por lo tanto notamos que la variabilidad de los datos no afectan a la complejidad ni a los tiempos medidos.

A pesar de esto encontramos un caso donde el algoritmo podria funcionar en tiempo lineal, si f(1) >= k_i para todo i podriamos tomar la decision de atacar siempre y no hace falta verificar lo que sucede anteriormente ya que atacando siempre obtenemos la mayor cantidad de soldados abatidos, sin embargo habria que modificar el algoritmo, ya que como vemos en la figura midiendo los tiempos de este caso con el algoritmo original no hay una mejora, y no funcionaria en el resto de los casos por lo que decidimos no tomar este caso como una mejora posible.