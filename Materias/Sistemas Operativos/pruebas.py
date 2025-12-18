def karatsuba(x, y):
    # Si los números son pequeños, se multiplican directamente
    if x < 10 or y < 10:
        return x * y

    # Calculamos la longitud de los números
    n = max(len(str(x)), len(str(y)))
    m = n // 2  # Mitad de la longitud

    # Dividimos x e y en dos partes
    x_H, x_L = divmod(x, 10**m)
    y_H, y_L = divmod(y, 10**m)

    # Aplicamos la recursión
    A = karatsuba(x_H, y_H)
    B = karatsuba(x_L, y_L)
    C = karatsuba(x_H + x_L, y_H + y_L) - A - B

    # Reconstruimos el resultado
    return A * 10**(2*m) + C * 10**m + B

    # Prueba con números grandes
    num1 = 25
    num2 = 20

    resultado = karatsuba(num1, num2)
    print(resultado)

    # Verificación con multiplicación estándar
    assert resultado == num1 * num2, "Error en la multiplicación"

def mochila(W,elementos):
    n = len(elementos)

    if n == 0 or W == 0:
        return 0
    if n == 1 and elementos[0] <= W:
        return elementos[0] 
    
    matriz = [[0 for j in range(W + 1)] for i in range(n + 1)]

    for i in range(1, n + 1):
        elemento = elementos[i-1]
        for j in range(1, W + 1):
            if elemento[1] > j:
                matriz[i][j] = matriz[i-1][j]
            else:
                matriz[i][j] = max(matriz[i-1][j], matriz[i-1][j - elemento[1]] + elemento[0])

    return matriz[len(elementos)][W]


def charlas(charlas, k):
    charlas.sort(key=lambda x: x[1])

    disponible = [0] * k # tiempo disponible de cada equipo
    asignaciones = []

    for charla in charlas:
        for equipo in range(k):
            if disponible[equipo] <= charla[0]:
                disponible[equipo] = charla[1]
                asignaciones.append((charla, equipo))
                break

    return asignaciones
            
def charlas(charlas, k):
    asignacion = {i:[] for i in range(k)}
    mejor_asignacion = {i:[] for i in range(k)}
    return charlas_rec(charlas, k, 0, asignacion, mejor_asignacion)

def charlas_rec(charlas, k, indice, asignacion, mejor_asignacion):
    cantidad_actual = sum(len(v) for v in asignacion.values())
    cantidad_restante = len(charlas) - indice
    cantidad_maxima_posible = cantidad_actual + cantidad_restante

    if cantidad_maxima_posible <= sum(len(v) for v in mejor_asignacion.values()):
        return mejor_asignacion
    
    if indice >= len(charlas):
        if (sum(len(v) for v in asignacion.values())) > (sum(len(v) for v in mejor_asignacion.values())):
            for k in asignacion:
                mejor_asignacion[k] = list(asignacion[k])
        return mejor_asignacion

    for i in range(k):
        asignacion[i].append(charlas[indice])

        if es_compatible(asignacion[i]):
            mejor_asignacion = charlas_rec(charlas, k, indice + 1, asignacion,mejor_asignacion)
                        
        asignacion[i].pop()
    
    
    mejor_asignacion = charlas_rec(charlas, k, indice + 1, asignacion,mejor_asignacion)

    return mejor_asignacion

def es_compatible(asignacion_equipo):
    ultima_asignacion = asignacion_equipo[-1]
    hora_inicio = ultima_asignacion[0]

    for i in range(len(asignacion_equipo) - 1):
        actual = asignacion_equipo[i]
        hora_fin = actual[1]

        if hora_fin > hora_inicio:
            return False
        
    return True


def resolver_texto(texto, palabras):
    n = len(texto)

    dp = [False] * (n + 1)
    dp[0] = True

    for i in range(1, n + 1):
        for j in range(i):
            if dp[j] == True and texto[j:i] in palabras:
                dp[i] = True

    
    return dp[n] 

def parentesis(cadena):
    largo = 0
    pila = []

    for parentesis in cadena:
        if parentesis == "(":
            pila.append(parentesis)
        elif parentesis == ")":
            if len(pila) > 0:
                pila.pop()
                largo += 2
            else:
                break

    return largo

def elemento_mas_cerca(enteros, elemento, k):
    inicio = 0
    fin = len(enteros) - 1
    
    while fin >= inicio:

        medio = (inicio + fin) // 2

        if enteros[medio] < elemento:
            inicio = medio + 1
        else:
            fin = medio - 1

def dyc_1(arr):
    return dyc_1_rec(arr, 0, len(arr) - 1)

def dyc_1_rec(arr, inicio, fin):
        
    mitad = (inicio + fin) // 2

    if arr[mitad] < arr[mitad - 1] or arr[mitad] > arr[mitad + 1]:
        return arr[mitad]

    izquierda = dyc_1_rec(arr[:mitad])
    derecha = dyc_1_rec(arr[mitad:])

    if izquierda is None:
        return derecha
    else:
        return izquierda
    

# Dado un Grafo dirigido, acíclico y pesado, y dos vértices s y t, implementar un algoritmo que, por programación
# dinámica, permita encontrar el camino de peso máximo. Indicar y justificar la complejidad del algoritmo implementado.
# Escribir y describir la ecuación de recurrencia de la solución, y la complejidad del algoritmo implementado. Implementar
# el algoritmo de reconstrucción de la solución, indicando su complejidad.

# Ec de recurrencia: En cada vertice guardo cual es el camino donde voy obteniendo el peso maximo, y para cada nuevo vertice elijo si
# eliguiendolo obtengo el peso maximo o no

# dp[v]=max(u,v)∈E (dp[u]+peso(u,v))

def encontrar_camino_maximo(grafo, s, t):
    V = len(grafo)  # cantidad de nodos
    mejorPeso = [-float('inf')] * V  # inicializamos todo en -infinito
    padre = [-1] * V                 # para reconstruir el camino
    mejorPeso[s] = 0                 # el inicio tiene peso 0

    # 1. Hacer orden topológico (los nodos en orden sin ciclos)
    orden = orden_topologico(grafo)

    # 2. Para cada nodo en el orden
    for u in orden:
        for (v, peso) in grafo[u]:
            if mejorPeso[u] + peso > mejorPeso[v]:
                mejorPeso[v] = mejorPeso[u] + peso
                padre[v] = u

    # 3. Reconstruimos el camino
    camino = []
    actual = t
    if mejorPeso[t] == -float('inf'):
        return None, []  # No hay camino

    while actual != -1:
        camino.append(actual)
        actual = padre[actual]

    camino.reverse()
    return mejorPeso[t], camino


def mochila_back(elementos, W, K):
    n = len(elementos)
    valor_actual = 0
    return backtracking(n, elementos, 0, W, K,  valor_actual, 0)

def backtracking(n, elementos, indice, W, K,  valor_actual, cant_elementos):
    if W < 0:
        return -1
    
    if (n - indice) + cant_elementos < K:
        return -1
    
    if indice == n:
        if cant_elementos >= K:
            return valor_actual
        else:
            return -1

    elemento = elementos[indice]

    tomarlo = backtracking(n, elementos, indice + 1, W - elemento[1], K,  valor_actual + elemento[0], cant_elementos + 1)

    no_tomarlo = backtracking(n, elementos, indice + 1, W, K,  valor_actual, cant_elementos)

    return max(tomarlo, no_tomarlo)

elementos = [(60, 10), (100, 20), (120, 30)]

W = 50
K = 2

print(mochila_back(elementos, W, K))