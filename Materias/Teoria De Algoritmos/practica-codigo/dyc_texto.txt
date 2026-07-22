# Teorema Maestro: 
# A : Número de llamadas recursivas.
# B : Proporción del tamaño del problema en cada llamada.
# O(n^C) : Costo de dividir y combinar.

# T(n)=aT(n/b)+f(n)
# Si logb(A) < C -> T(n) = O(n^c)
# Si logb(A) = C -> T(n) = O(n^c * logb(A)) = O(n^c * log n)
# Si logb(A) > C -> T(n) = O(n^logb(A))



# (★) Implementar, por división y conquista, una función que dado un arreglo sin elementos repetidos y casi ordenado 
# (todos los elementos se encuentran ordenados, salvo uno), obtenga el elemento fuera de lugar. Indicar y justificar su complejidad temporal.

# Ya que tenemos un arreglo casi ordenado, significa que hay un solo punto donde se rompe el orden creciente de los elementos, 
# por lo que usaremos busqueda binaria para encontrar esa posicion
# Como idea general, si estuviera el arreglo ordenado siempre tendriamos que arr[i] < arr[i+1], por lo que la posicion erronea
# sera cuando se de que arr[i] > arr[i+1]

def encontrar_fuera_de_lugar(arr):
    n = len(arr)
    if n == 1:
        return None
    if n == 2:
        if arr[0] > arr[1]:
            return arr[0]
        else:
            return None
    
    mitad = n//2

    if arr[mitad-1] > arr[mitad]:
        return arr[mitad-1]
    if arr[mitad+1] < arr[mitad]:
        return arr[mitad]

    izq = arr[:mitad]
    der = arr[mitad:]

    elemento_izq = encontrar_fuera_de_lugar(izq)
    elemento_der = encontrar_fuera_de_lugar(der)

    if elemento_izq != None:
        return elemento_izq
    elif elemento_der != None:
        return elemento_der
    return None

# Complejidad temporal, en cada llamado a la funcion se realiza lo siguiente:
# - Se divide el arreglo a la mitad 
# - Dentro se llama a dos funciones de recurrencia
# - Se aplican comparaciones constantes
# Ecuacion de recurrencia = T(n)= 2T(n/2)+O(1)
# Por teorema maestro nos queda el caso de logb(A) > C -> T(n) = O(n^logb(A)), por lo que O(n^logb(A)) <=> O(n) por 
# lo que la complejidad temporal es O(n)


# (★) Se tiene un arreglo en el que se registran los resultados de tests automáticos de una porción de código. Este código se 
# encontraba funcionando pero, debido a unos cambios que se están realizando, en algún momento dejó de funcionar. Se registra 
# un 1 si pasa los tests, 0 en caso contrario. De esta manera, el arreglo tendrá la forma [1, 1, 1, ..., 0, 0, ...] (es decir, 
# unos seguidos de ceros). Se pide: 
# a. una función de orden O(logn) que, por división y conquista, encuentre el índice del primer 0, de forma que se pueda reconocer 
# rápidamente en qué modificación del código se dejó de pasar los tests. Si no hay ningún 0 (solo hay unos), debe devolver -1. 
# b. demostrar con el Teorema Maestro que la función es, en efecto, O(logn).

# Para este problema, usaremos la busqueda binaria para poder encontrar el primer 0 en el arreglo, en donde nuestra idea 
# principal sera que si arr[i] = 0 con arr[i-1] = 1 y arr[i+1] = 0, entonces i es el indice del primer 0.

def encontrar_primer_cero(arr):
    inicio = 0
    fin = len(arr) - 1
    while inicio <= fin:
        mitad = (inicio+fin) // 2
        if arr[mitad] == 0:
            if arr[mitad-1] == 1 and arr[mitad+1] == 0:
                return mitad
            else:
                fin = mitad - 1
        else:
            inicio = mitad + 1
    return -1 

# Complejidad temporal, en cada llamado a la funcion se realiza lo siguiente:
# - Se divide el arreglo a la mitad
# - Solamente se busca por una de las mitades del arreglo
# - Se aplican comparaciones constantes
# Ecuacion de recurrencia = T(n)= T(n/2)+O(1)
# Por teorema maestro nos queda el caso de logb(A) = C -> T(n) = O(n^c * log n), por lo que O(n^c * log n) <=> O(log n) por 
# lo que la complejidad temporal es O(log n)


# (★) Implementar un algoritmo que, por división y conquista, permita obtener la parte entera de la raíz cuadrada de un número 
# n, en tiempo O(logn). Por ejemplo, para n=10 debe devolver 3, y para n=25 debe devolver 5. Justificar la complejidad del algoritmo.

# Para este ejercicio, utilizaremos la busqueda binaria para encontrar la parte entera de la raiz cuadrada del numero n. Esto lo 
# haremos planteando lo siguiente, agarramos la mitad del numero como maximo posible, ya que si fuera uno mayor su multiplicacion
# superaria al mismo. Y luego realizamos la busqueda binaria con la idea principal de que si arr[i] ^ 2 = n, ya encontramos el numero, 
# sino, si arr[i-1] ^ 2 < n y arr[i+1] ^ 2 > n, entonces arr[i] = n.

def raiz_cuadrada(n):
    if n == 1 or n == 0:
        return n
    medio = n // 2
    inicio = 0
    fin = medio
    while inicio <= fin:
        mitad = (inicio + fin) // 2
        if (mitad ** 2 == n):
            return mitad
        if mitad ** 2 < n:
            inicio = mitad + 1
        else:
            fin = mitad - 1
    return mitad

# Complejidad temporal, en cada llamado a la funcion se realiza lo siguiente:
# - Se divide el numero a la mitad
# - Se busca recurrentemente dividiendo el numero a la mitad
# - Se aplican comparaciones constantes
# Ecuacion de recurrencia = T(n)= T(n/2)+O(1)
# Por teorema maestro nos queda el caso de logb(A) = C -> T(n) = O(n^c * log n), por lo que O(n^c * log n) <=> O(log n) por 
# lo que la complejidad temporal es O(log n)


# (★) Se tiene un arreglo de N >= 3 elementos en forma de pico, esto es: estrictamente creciente hasta una determinada posición p, 
# y estrictamente decreciente a partir de ella (con 0 < p < N−1). Por ejemplo, en el arreglo [1, 2, 3, 1, 0, -2] la posición del pico es 
# p=2. Se pide: 
# a. Implementar un algoritmo de división y conquista de complejidad O(logn) que encuentre la posición p del pico. 
# b. Justificar la complejidad del algoritmo mediante el teorema maestro.

# Para este ejercicio utilizaremos busqueda binaria de la siguiente forma, dividimos el arreglo de numeros a la mitad y planteamos que,
# si arr[i-1] < arr[i] y arr[i+1] < arr[i] significa que encontramos el pico en la posicion i. Si arr[i] > arr[i-1] buscamos por la 
# derecha y si arr[i] < arr[i-1] buscamos por la izquierda.

def encontrar_pico(arr):
    inicio = 0
    fin = len(arr) - 1
    while inicio <= fin:
        mitad = (inicio + fin) // 2
        if arr[mitad] > arr[mitad-1] and arr[mitad] > arr[mitad+1]:
            return mitad
        elif arr[mitad] > arr[mitad-1]:
            inicio = mitad + 1
        else:
            fin = mitad - 1
    return mitad

# Complejidad temporal, en cada llamado a la funcion se realiza lo siguiente:
# - Se divide el arreglo a la mitad
# - Solamente se busca por una de las mitades del arreglo
# - Se aplican comparaciones constantes
# Ecuacion de recurrencia = T(n)= T(n/2)+O(1)
# Por teorema maestro nos queda el caso de logb(A) = C -> T(n) = O(n^c * log n), por lo que O(n^c * log n) <=> O(log n) por 
# lo que la complejidad temporal es O(log n)


# (★) Implementar Merge Sort. Justificar la complejidad del algoritmo mediante el teorema maestro.

def merge_sort(arr):
    if len(arr) <= 1:
        return arr
    mitad = len(arr) // 2
    izq = merge_sort(arr[:mitad])
    der = merge_sort(arr[mitad:])
    return merge(izq,der)

def merge(izq, der):
    resultado = []
    i = j = 0
    while i < len(izq) and j < len(der):
        if izq[i] <= der[j]:
            resultado.append(izq[i])
            i += 1
        else:
            resultado.append(der[j])
            j += 1
    return resultado

# Complejidad temporal, en cada llamado a la funcion se realiza lo siguiente:
# - Se divide el arreglo a la mitad
# - Se recorren las dos mitades con la misma funcion recurrente
# - Se llama a una funcion de merge que es O(n) que ordena la parte izq y der dividida antes
# Ecuacion de recurrencia = T(n)= 2T(n/2)+O(n)
# Por teorema maestro nos queda el caso de logb(A) = C -> T(n) = O(n^c * log n), por lo que O(n^c * log n) <=> O(n log n) por 
# lo que la complejidad temporal es O(n log n)


# (★) Implementar un algoritmo de multiplicación de dos números grandes de longitud n, por división y conquista, con un orden de 
# complejidad mejor que O(n^2). Justificar la complejidad del algoritmo mediante el teorema maestro.

# Para este problema usaremos el algoritmo de Karatsuba, el cual dice lo siguiente:
# Dados dos numeros a y b, se calcula la mitad de la longitud n del numero mas largo, luego calculamos:
# a = a1 * 10m^2 + a2 
# b = b1 * 10m^2 + b2 
# con 𝑚 = [𝑛/2]
# Y luego se calcula recursivamente:
# A = a1 * b1
# B = a2 * b2
# C = (a1 + a2)(b1 + b2) - A - B
# Y como resultado final queda:  a*b = A * 10^2m + C * 10^m + B

def multiplicar(a,b):
    if a <= 10 or b <= 10:
        return a*b
    longitud = max(len(str(a)), len(str(b)))
    mitad = longitud // 2
    a1, a2 = divmod(a, 10**mitad)
    b1, b2 = divmod(b, 10**mitad)
    A = multiplicar(a1, b1)
    B = multiplicar(a2, b2)
    C = multiplicar((a1+a2), (b1+b2)) - A - B
    return ((A * 10**(2*mitad)) + (C * 10**mitad) + B)

# Complejidad temporal, en cada llamado a la funcion se realiza lo siguiente:
# - Se busca la mitad de la longitud del numero mas grande
# - Se realizan 3 llamadas recursivas sobre esos numeros de tamaño n/2
# - Se realizan calculo de suma, resta y potencia que es O(n)
# Ecuacion de recurrencia = T(n)= 3T(n/2)+O(n)
# Por teorema maestro nos queda el caso de logb(A) > C -> T(n) = O(n^logb(A)), por lo que O(n^logb(A)) <=> O(n^log2 (3)) por 
# lo que la complejidad temporal es O(n^(1.58)), mejor que O(n^2)


# (★★) Implementar un algoritmo que dados n puntos en un plano, busque la pareja que se encuentre más cercana, por división y conquista, 
# con un orden de complejidad mejor que O(n^2). Justificar la complejidad del algoritmo mediante el teorema maestro. Se puede asumir 
# que ningún par de puntos tienen la misma coordenada x o y.

# Para este ejercicio utilizaremos el algoritmo de Closest Pair of Points que tiene complejidad O(n log n) y hace lo siguiente:
# Ordena los puntos por coordenada x, divide el conjunto en dos mitades de igual tamaño y recursivamente encuentra la distancia minima 
# en cada mitad, osea dos subproblemas. Luego combina estos resultados, verificando si hay un par de puntos, uno en cada mitad, que este mas cerca
# que el minimo de las mitades. Esto se hace considerando solo los puntos dentro de una franja vertica de ancho 2 * δ, donde δ es 
# la mejor distancia encontrada en las mitades.

def closest_pair(puntos):
    px = sorted(puntos, key=lambda p: p[0])
    py = sorted(puntos, key=lambda p: p[1])
    return closest_pairs_rec(px, py)

def dist(p1, p2):
    return ((p1[0] - p2[0]) ** 2 + (p1[1] - p2[1]) ** 2) ** 0.5   

def closest_pairs_rec(px, py):
    n = len(px)
    if n <= 3:
        return min(((p1, p2) for i, p1 in enumerate(px) for p2 in px[i+1:]),
                key=lambda pair: dist(pair[0], pair[1]))
    mitad = n // 2
    Qx = px[:mitad]
    Rx = px[mitad:]
    linea_divisoria = px[mitad][0] 
    Qy = []
    Ry = []
    for p in py:
        if p[0] <= linea_divisoria:
            Qy.append(p)
        else:
            Ry.append(p)
    q0, q1 = closest_pairs_rec(Qx, Qy)
    r0, r1 = closest_pairs_rec(Rx, Ry)
    dist_q = dist(q0, q1)
    dist_r = dist(r0, r1)
    d = min(dist_q, dist_r)
    mejor_par = (q0, q1) if dist_q < dist_r else (r0, r1)
    S = []
    for p in py:
        if abs(p[0] - linea_divisoria) < d:
            S.append(p)
    for i in range(len(S)):
        for j in range(i + 1, min(i + 7, len(S))): 
            d_s = dist(S[i], S[j])
            if d_s < d:
                d = d_s
                mejor_par = (S[i], S[j])
    return mejor_par

# Complejidad temporal, en cada llamado a la funcion se realiza lo siguiente:
# - Se divide en dos subproblemas de tamaño n/2
# - El paso de combinación toma O(n) (con el truco de ordenar por y)
# Ecuacion de recurrencia = T(n)= 2T(n/2)+O(n)
# Por teorema maestro nos queda el caso de logb(A) = C -> T(n) = O(n^c * log n), por lo que O(n^c * log n) <=> O(n log n) por 
# lo que la complejidad temporal es O(n log n)


# (★) Dados un conjunto de n elementos, y 2 arreglos de longitud n, con dichos elementos. El arreglo A está completamente 
# ordenado de menor a mayor. El arreglo B se encuentra desordenado. Indicar, por división y conquista, la cantidad de 
# inversiones necesarias al arreglo B para que quede ordenado de menor a mayor, con un orden de complejidad mejor que 
# O(n^2). Justificar la complejidad del algoritmo mediante el teorema maestro.

def inversiones_para_ordenar(A, B):
    if len(B) <= 1:
        return 0
    mitad = len(B) // 2
    inversiones_izq = inversiones_para_ordenar(A, B[:mitad])
    inversiones_der = inversiones_para_ordenar(A, B[mitad:])
    inversiones_merge = merge(B[:mitad], B[mitad:])
    return inversiones_izq + inversiones_der + inversiones_merge

def merge(izq, der):
    resultado = []
    inversiones = 0
    i = j = 0
    while i < len(izq) and j < len(der):
        if izq[i] <= der[j]:
            resultado.append(izq[i])
            i += 1
        else:
            resultado.append(der[j])
            inversiones += len(izq) - i
            j += 1
    resultado.extend(izq[i:])
    resultado.extend(der[j:])
    return inversiones

# Complejidad temporal, en cada llamado a la funcion se realiza lo siguiente:
# - Se divide el arreglo a la mitad
# - Se recorren las dos mitades con la misma funcion recurrente
# - Se llama a una funcion de merge que es O(n) que ordena la parte izq y der dividida antes
# Ecuacion de recurrencia = T(n)= 2T(n/2)+O(n)
# Por teorema maestro nos queda el caso de logb(A) = C -> T(n) = O(n^c * log n), por lo que O(n^c * log n) <=> O(n log n) por 
# lo que la complejidad temporal es O(n log n)


# (★★★) Implementar una función, que utilice división y conquista, de complejidad O(nlogn) que dado un arreglo de n números enteros 
# devuelva true o false según si existe algún elemento que aparezca más de la mitad de las veces. Justificar la complejidad de la solución. 
# Ejemplos:
# [1, 2, 1, 2, 3] -> false
# [1, 1, 2, 3] -> false
# [1, 2, 3, 1, 1, 1] -> true
# [1] -> true
# Aclaración: Este ejercicio puede resolverse, casi trivialmente, ordenando el arreglo con un algoritmo eficiente, o incluso se puede 
# realizar más rápido utilizando una tabla de hash. Para cumplir con la consigna, resolver sin ordenar el arreglo ni con tabla de hash, 
# sino puramente por división y conquista.

def mas_veces_que_la_mitad(arr):
    candidato = mayoritario(arr)
    if candidato is None:
        return False
    apariciones = contar_apariciones(candidato, arr)
    return apariciones > (len(arr)//2)

def contar_apariciones(n, arr):
    apariciones = sum(1 for i in arr if i == n)
    return apariciones

def mayoritario(arr):
    n = len(arr)
    if n == 1:
        return arr[0]
    mitad = n // 2
    izq = mayoritario(arr[:mitad])
    der = mayoritario(arr[mitad:])
    if izq == der:
        return izq
    contador_izq = contar_apariciones(izq, arr) if izq is not None else 0
    contador_der = contar_apariciones(der, arr) if der is not None else 0
    if contador_izq > n // 2:
        return izq
    elif contador_der > n // 2:
        return der
    else: 
        return None
    
# Complejidad temporal, en cada llamado a la funcion se realiza lo siguiente:
# - Se divide el arreglo a la mitad
# - Se recorren las dos mitades con la misma funcion recurrente
# - Se llama a una funcion de contar apariciones que es O(n) que recorre el arreglo 
# Ecuacion de recurrencia = T(n)= 2T(n/2)+O(n)
# Por teorema maestro nos queda el caso de logb(A) = C -> T(n) = O(n^c * log n), por lo que O(n^c * log n) <=> O(n log n) por 
# lo que la complejidad temporal es O(n log n).


# (★★★★) Resolver el ejercicio anterior, por división y conquista, en complejidad O(n), dada la misma aclaración. 
# Justificar la complejidad de la solución.

# Para este ejercicio utilizaremos el algoritmo de Boyer-Moore, en el cual la idea es recorrer el arreglo para encontrar a un
# candidato, y luego se lo recorre denuevo para confirmar que lo sea, lo cual hace que sea O(n). El pensamiento de division
# y conquista en este ejercicio esta implicito, descartando pares de elementos distintos.

def mas_veces_que_la_mitad_O_n(arr):
    candidato = boyer_moore(arr)
    if candidato is None:
        return False
    apariciones = contar_apariciones(candidato, arr)
    return apariciones > (len(arr)//2)

def boyer_moore(arr):
    contador = 0
    candidato = None
    for numero in arr:
        if contador == 0:
            candidato = numero
            contador = 1
        elif numero == candidato:
            contador += 1
        else:
            contador -= 1
    return candidato

# Complejidad temporal, en cada llamado a la funcion se realiza lo siguiente:
# - Se recorre el arreglo una vez para encontrar al candidato
# - Se recorre denuevo para contra sus apariciones
# En este ejercicio no aplica la recurrencia por lo que no usamos teorema maestro, al recorrer el arreglo dos veces
# nuestra complejidad temporal sera O(2n), por lo que es O(n).


# (★★★★) Implementar una función, que utilice división y conquista, de complejidad O(n) que dado un arreglo de n números enteros devuelva
# true o false según si existe algún elemento que aparezca más de dos tercios de las veces. Justificar la complejidad de la solución.

# En este ejercicio, usamos una generalizacion del algoritmo de Boyer Moore, en donde vamos a conseguir un candidato esperado
# y devolveremos si este aparece mas de 2/3 de veces.

def dos_tercios_de_veces_O_n(arr):
    candidato = boyer_moore_dos_tercios(arr)
    if candidato is None:
        return False
    apariciones = contar_apariciones(candidato, arr)
    return apariciones > (2 * len(arr)) // 3

def boyer_moore_dos_tercios(arr):
    contador = 0
    candidato = None
    for numero in arr:
        if contador == 0:
            candidato = numero
            contador = 1
        elif numero == candidato:
            contador += 1
        else:
            contador -= 1
    return candidato

# Complejidad temporal, en cada llamado a la funcion se realiza lo siguiente:
# - Se recorre el arreglo una vez para encontrar al candidato
# - Se recorre denuevo para contra sus apariciones
# En este ejercicio no aplica la recurrencia por lo que no usamos teorema maestro, al recorrer el arreglo dos veces
# nuestra complejidad temporal sera O(2n), por lo que es O(n).


# (★★★) Tenemos un arreglo de tamaño 2n de la forma {C1, C2, C3, … Cn, D1, D2, D3, … Dn}, tal que la cantidad total de elementos 
# del arreglo es potencia de 2 (por ende, n también lo es). Implementar un algoritmo de División y Conquista que modifique el arreglo 
# de tal forma que quede con la forma {C1, D1, C2, D2, C3, D3, …, Cn, Dn}, sin utilizar espacio adicional (obviando el utilizado 
# por la recursividad). Indicar y justificar su complejidad temporal.

# Para este ejercicio de entrelazamiento (interleaving) lo plantearemos de la siguiente forma:
# Nosotros tenemos el arreglo {C1, C2, C3, … Cn, D1, D2, D3, … Dn} y queremos obtener el arreglo {C1, D1, C2, D2, C3, D3, …, Cn, Dn},
# por lo que dividiremos el arreglo en 4 partes iguales; A = {C1, .. , Cn/2} , B = {Cn/2+1, .. , Cn} , C = {D1, .. , Dn/2} , D = {Dn/2+1, .. , Dn}
# y luego haremos un swap recursivo de segmentos internos reemplazando B con C.

def intercalar(arr, inicio, fin):
    longitud = fin - inicio + 1
    if longitud <= 2:
        return
    mitad = longitud // 2
    cuarto = mitad // 2
    for i in range(cuarto):
        arr[inicio + cuarto + i], arr[inicio + mitad + i] = arr[inicio + mitad + i], arr[inicio + cuarto + i]
    intercalar(arr, inicio, inicio + mitad - 1)
    intercalar(arr, inicio + mitad, fin)

# Complejidad temporal, en cada llamado a la funcion se realiza lo siguiente:
# - Se divide el arreglo en mitades
# - Se llama 2 veces a la funcion recursiva
# - Se aplican comparaciones y operaciones lineales
# Ecuacion de recurrencia = T(n)= 2T(n/2)+O(n)
# Por teorema maestro nos queda el caso de logb(A) = C -> T(n) = O(n^c * log n), por lo que O(n^c * log n) <=> O(n log n) por 
# lo que la complejidad temporal es O(n log n).


# (★★★) Dado un arreglo de n enteros (no olvidar que pueden haber números negativos), encontrar el subarreglo 
# contiguo de máxima suma, utilizando División y Conquista. Indicar y justificar la complejidad del algoritmo. 
# Ejemplos:
# [5, 3, 2, 4, -1] →  [5, 3, 2, 4]
# [5, 3, -5, 4, -1] →  [5, 3]
# [5, -4, 2, 4, -1] → [5, -4, 2, 4]
# [5, -4, 2, 4] → [5, -4, 2, 4]

# Para este ejercicio del Maximo Subarreglo Contiguo, usaremos el algoritmo de Kadane, en donde pensaremos lo siguiente:
# Dividimos el arreglo en dos mitades para luego calcular el maximo entre, la solucion esta en la izquierda, en la 
# derecha o esta en el cruce de ambas partes.

def max_subarreglo(arr):
    suma, inicio, fin = encontrar_subarreglo_max(arr, 0, len(arr) - 1)
    return arr[inicio:fin+1]

def encontrar_subarreglo_max(arr, inicio, fin):
    if inicio == fin:
        return arr[inicio], inicio, fin
    mitad = (inicio + fin) // 2
    suma_izq, inicio_izq, fin_izq = encontrar_subarreglo_max(arr, 0, mitad)
    suma_der, inicio_der, fin_der = encontrar_subarreglo_max(arr, mitad + 1, fin)
    suma_medio, inicio_medio, fin_medio = encontrar_subarreglo_max_cruzado(arr, inicio, mitad, fin)
    if suma_izq >= suma_der and suma_izq >= suma_medio:
        return suma_izq, inicio_izq, fin_izq
    elif suma_der >= suma_izq and suma_der >= suma_medio:
        return suma_der, inicio_der, fin_der
    else:
        return suma_medio, inicio_medio, fin_medio
    
def encontrar_subarreglo_max_cruzado(arr, inicio, mitad, fin):
    suma_izq = float('-inf')
    suma = 0
    max_izq = mitad
    for i in range(mitad, inicio - 1, -1):
        suma += arr[i]
        if suma > suma_izq:
            suma_izq = suma
            max_izq = i

    suma_der = float('-inf')
    suma = 0
    max_der = mitad + 1
    for j in range(mitad + 1, fin + 1):
        suma += arr[j]
        if suma > suma_der:
            suma_der = suma
            max_der = j
    return suma_izq + suma_der, max_izq, max_der

# Complejidad temporal, en cada llamado a la funcion se realiza lo siguiente:
# - Se divide el arreglo a la mitad
# - Se recorren las dos mitades con la misma funcion recurrente
# - Se llama a una funcion de maximo arreglo entre la izquierda y la derecha que es O(n)
# Ecuacion de recurrencia = T(n)= 2T(n/2)+O(n)
# Por teorema maestro nos queda el caso de logb(A) = C -> T(n) = O(n^c * log n), por lo que O(n^c * log n) <=> O(n log n) por 
# lo que la complejidad temporal es O(n log n)


# Dado un arreglo de enteros ordenado, un elemento y un valor entero k, implementar una función que, usando división
# y conquista, encuentre los k valores del arreglo más cercanos al elemento en cuestión (que bien podría estar en el
# arreglo, o no). La complejidad de la función implementada debe ser menor a O(n), suponiendo que k < n. Justificar
# adecuadamente la complejidad del algoritmo implementado.


def k_cercanos_a_elem(arr, elemento, k):
    inicio = 0
    fin = len(arr) - 1
    while inicio < fin:
        mitad = (inicio + fin) // 2
        if arr[mitad] < elemento:
            inicio = mitad + 1
        else: 
            fin = mitad
    # en izquierda va a quedar el indice del primer valor >= al elemento buscado
    i = inicio - 1
    j = inicio
    resultado = []
    for x in range(k):
        # si no hay mas valores a la izquierda, cargo los de la derecha
        if i < 0:
            resultado.append(arr[j])
            j += 1
        # si no hay mas valores a la derecha, cargo los de izquierda
        elif j >= len(arr):
            resultado.append(arr[i])
            i -= 1
        # si el modulo del valor izquierdo - elemento es menor al modulo del valor derecho - elemento,
        # lo agregamos por estar mas cerca
        elif abs(arr[i] - elemento) <= abs(arr[j] - elemento):
            resultado.append(arr[i])
            i -= 1
        # si el modulo del valor derecho - elemento es menor al modulo del valor izquierdo - elemento,
        # lo agregamos por estar mas cerca
        else:
            resultado.append(arr[j])
            j += 1
    return resultado

# Complejidad temporal, en cada llamado a la funcion se realiza lo siguiente:
# - Se realiza una busqueda binaria del elemento en cuestion lo cual es O(log n)
# - Se utilizan dos punteros para encontrar los k valores mas cercanos al elemento objetivo, lo cual es O(k)
# Como no hay recurrencia no se aplica la Ecuacion de recurrencia. Al utilizar los punteros para recorrer el arreglo
# buscando los k valores mas cercanos, como k < n, esto es mas eficiente que recorrer el arreglo entero, por lo que 
# nuestra complejidad total sera O(log n + k) lo cual es mejor a O(n) cuando k < n.