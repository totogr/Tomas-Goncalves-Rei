import copy
from grafo import Grafo

# (★★) Implementar por backtracking un algoritmo que, dado un grafo no dirigido y un numero n menor a
# ∣V∣, devuelva si es posible obtener un subconjunto de n vertices tal que ningun par de vertices sea adyacente entre si.

# Para este ejercicio de Independent Set vamos a probar combinaciones de vertices y verificar si el conjunto actual es valido, 
# hasta poder tener n vertices.
# Podas:
# - Si ya tengo n vertices validos, corto y devuelvo True
# - Si el conjunto actual ya tiene un vertice adyacente al nuevo, descarto esa rama
# - Si con los vertices que me quedan no llego a n, corto y devuelvo False

def independent_set(grafo, n):
    vertices = grafo.obtener_vertices()
    return backtracking_IS(grafo, vertices, n, 0, [])

def es_compatible(grafo, v, conjunto_actual):
    for w in conjunto_actual:
        if grafo.estan_unidos(v,w):
            return False
    return True

def backtracking_IS(grafo, vertices, n, indice, conjunto_actual):
    # Caso base, se logro formar el conjunto de tamaño n
    if len(conjunto_actual) == n:
        return True
    
    # Si ya no quedan tantos vertices como para completar el conjunto
    if indice >= len(vertices):
        return False
    

    for i in range(indice, len(vertices)):
        v = vertices[i]

        # Si v no es compatible, lo salteo
        if not es_compatible(grafo, v, conjunto_actual):
            continue

        conjunto_actual.append(v)
        if backtracking_IS(grafo, vertices, n, i + 1, conjunto_actual):
            return True
        conjunto_actual.pop()

    return False


# (★★) Implementar un algoritmo que reciba un grafo y un número n que, utilizando backtracking, indique si es posible 
# pintar cada vértice con n colores de tal forma que no hayan dos vértices adyacentes con el mismo color.

# Para este ejercicio de Coloreo vamos a ir pintando vertices, verificando que sean validos, hasta que podamos colorear todo el grafo
# Podas:
# - Si ya pinte todos los vertices, corto y devuelvo True
# - Si algun vecino del vertices actual ya tiene ese color, paso al siguiente color.

def coloreo(grafo, n):
    vertices = grafo.obtener_vertices()
    colores = {}
    return backtracking_coloreo(grafo, vertices, n, 0, colores)

def color_valido(grafo, v, color, colores):
    for w in grafo.adyacentes(v):
        if w in colores and colores[w] == color:
            return False
    return True 

def backtracking_coloreo(grafo, vertices, n, indice, colores):
    # Caso base, se logro pintar todos los vertices
    if indice == len(vertices):
        return True
    
    v = vertices[indice]

    for color in range(1, n+1):
        # Si v puede tener el color porque ninguno de sus adyacentes lo tiene, sigo
        if color_valido(grafo, v, color, colores):
            colores[v] = color
            if backtracking_coloreo(grafo, vertices, n, indice + 1, colores):
                return True
            del colores[v]
    return False


# (★) Dado un tablero de ajedrez n×n, implementar un algoritmo por backtracking que ubique 
# (si es posible) a n reinas de tal manera que ninguna pueda comerse con ninguna.

# Para este problema de N reinas, vamos fila por fila y vamos a ingresar la reina en una posicion de una fila y validar que no
# se cruce con otra, hasta poder ubicar n reinas.
# Podas:
# - Si la reina colocada esta amenazada por las anteriores, la sacamos
# - Si completamos n filas, encontramos la solucion

def n_reinas(n):
    return backtracking_n_reinas(n, 0, [], set(), set(), set())

def backtracking_n_reinas(n, fila, solucion, col_ocupadas, diag1_ocupadas, diag2_ocupadas):
    if fila == n:
        return solucion.copy()
    
    for col in range(n):
        diag1 = fila - col
        diag2 = fila + col

        # Si la reina que colocamos choca con otra, la salteamos
        if (col in col_ocupadas) or (diag1 in diag1_ocupadas) or (diag2 in diag2_ocupadas):
            continue

        solucion.append((fila,col))
        col_ocupadas.add(col)
        diag1_ocupadas.add(diag1)
        diag2_ocupadas.add(diag2)

        resultado = backtracking_n_reinas(n, fila + 1, solucion, col_ocupadas, diag1_ocupadas, diag2_ocupadas)
        if resultado != []:
            return resultado
        
        solucion.pop()
        col_ocupadas.remove(col)
        diag1_ocupadas.remove(diag1)
        diag2_ocupadas.remove(diag2)

    return []

# (★★) Implementar un algoritmo que dado un Grafo no dirigido nos devuelva un conjunto de vértices 
# que representen un máximo Independent Set del mismo.

# Para este problema de maximo IS, vamos a recorrer vertice por vertice y ver si lo agregamos o no al conjunto, si lo incluimos
# no incluimos sus vecinos
# Podas:
# - Podamos por invalides de adyacencia
# - Si el tamaño del conjunto actual + los vertices restantes no puede ser mayor a la mejor solucion, lo descartamos
# - Si ya recorrimos todos los vertices comparamos la solucion parcial con la final

def maximo_IS(grafo):
    vertices = grafo.obtener_vertices()
    return list(backtracking_max_IS(grafo, vertices, 0, set(), set()))

def es_IS(grafo, v, sol_parcial):
    for w in sol_parcial:
        if grafo.estan_unidos(v,w):
            return False
    return True

def backtracking_max_IS(grafo, vertices, indice, sol_parcial, sol_final):
    if (len(sol_parcial) + (len(vertices) - indice)) <= len(sol_final):
        return sol_final
    
    if indice == len(vertices):
        if len(sol_parcial) > len(sol_final):
            return set(sol_parcial)
        return sol_final

    v = vertices[indice]

    if es_IS(grafo, v, sol_parcial):
        sol_parcial.add(v)
        sol_final = backtracking_max_IS(grafo, vertices, indice + 1, sol_parcial, sol_final)

    sol_parcial.discard(v)
    return backtracking_max_IS(grafo, vertices, indice + 1, sol_parcial, sol_final)

# (★★) Un camino hamiltoniano, es un camino de un grafo, que visita todos los vértices del grafo una sola vez. 
# Implementar un algoritmo por backtracking que encuentre un camino hamiltoniano de un grafo dado.

# Para este ejrcicio, recorremos los vertices y buscamos un conjunto en el cual se pasen por todos los vertices
# una vez a partir de un vertice y devolvemos el conjunto.
# Podas:
# - Se guardan los vertices visitados para no agregarlos al camino innecesariamentte
# - Si el largo del camino es igual a la cantidad de vertices cortamos

def camino_hamiltoniano(grafo):
    camino = []
    vertices = grafo.obtener_vertices()
    for v in vertices:
        if backtracking_camino_h(grafo, v, vertices, set(), camino):
            return camino
    return None

def backtracking_camino_h(grafo, v, vertices, visitados, camino):
    visitados.add(v)
    camino.append(v)
    
    if len(vertices) == len(camino):
        return True        

    for w in grafo.adyacentes(v):
        if w not in visitados:

            if backtracking_camino_h(grafo, w, vertices, visitados, camino):
                return True
                     
    visitados.discard(v)
    camino.pop()
    
    return False


# (★) Dada una matriz de 9x9, implementar un algoritmo por backtracking que llene la matriz con números 
#  del 1 al 9, dadas las condiciones del Sudoku (si es posible). Las condiciones son: (i) Las celdas están 
#  dispuestas en 9 subgrupos de 3x3. (ii) Cada columna y cada fila no puede repetir número. (iii) Cada 
#  subgrupo de 3x3 no puede repetir número.

# Para este ejercicio, recorremos el tablero celda por celda, probando todos los valores posibles (1 al 9), validandolo
# y en caso que no lo sea retrocede y prueba otra combinacion.
# Podas:
# - Si el numero ya esta en la fila, no se prueba
# - Si el numero ya esta en la columna, no se prueba
# - Si el numero ya esta en el subcuadro de 3x3 correspondiente, no se prueba
# - Si la celda ya tiene un valor fijo, no se cambia

def sudoku(tablero):
    return backtracking_sudoku(tablero)

def encontrar_celda_vacia(tablero):
    for fila in range(9):
        for col in range(9):
            if tablero[fila][col] == 0:
                return fila, col
    return None, None

def es_valido(tablero, fila, col, num):
    # verificar fila
    if num in tablero[fila]:
        return False
    # Verificar columna
    for i in range(9):
        if tablero[i][col] == num:
            return False
    # Verificar recuadro de 3x3
    inicio_fila = 3 * (fila // 3)
    inicio_col = 3 * (col // 3)
    for i in range(inicio_fila, inicio_fila + 3):
        for j in range(inicio_col, inicio_col + 3):
            if tablero[i][j] == num:
                return False
    return True

def backtracking_sudoku(tablero):
    fila, col = encontrar_celda_vacia(tablero)
    # Si completamos todas las filas se termino el sudoku
    if fila is None:
        return copy.deepcopy(tablero)
    for i in range(1,10):
        if es_valido(tablero, fila, col, i):
            tablero[fila][col] = i
            resultado = backtracking_sudoku(tablero)
            if resultado:
                return resultado
            tablero[fila][col] = 0
    return None


# (★) Implementar un algoritmo de backtracking que, dado una pieza de caballo en un tablero de ajedrez, 
#  determine los movimientos a realizar para que el caballo logre pasar por todos los casilleros del tablero 
#  una única vez. Recordar que el caballo mueve en forma de L (dos casilleros en una dirección, y un casillero 
#  en forma perpendicular).

# Para este ejercicio, exploramos todas las posibles secuencias de movimientos del caballo en el tablero,
# intentando moverlo a una celda valida hasta completar el recorrido, sino retrocede y pruebas otros movimientos
# Podas:
# - Si un movimiento lo hace salirse del tablero, lo descarta
# - Evita visitar celdas repetidas

def caballo_ajedrez(n):
    tablero = [[-1 for x in range(n)] for x in range(n)]
    tablero[0][0] = 0
    movimientos = [
        (-2, -1), (-2, 1), (-1, -2), (-1, 2),
        (1, -2),  (1, 2),  (2, -1),  (2, 1)
    ]
    return backtracking_caballo_ajedrez(tablero, 0, 0, 1, movimientos, n)

def caballo_es_valido(tablero, nueva_fila, nueva_col, n):
    return ((0 <= nueva_fila < n) and (0 <= nueva_col < n) and (tablero[nueva_fila][nueva_col] == -1))

def backtracking_caballo_ajedrez(tablero, fila, col, paso, movimientos, n):
    if paso == n*n:
        return True
    for dx, dy in movimientos:
        nueva_fila = fila + dx
        nueva_col = col + dy
        if caballo_es_valido(tablero, nueva_fila, nueva_col, n):
            tablero[fila][col] = paso
            if backtracking_caballo_ajedrez(tablero, nueva_fila, nueva_col, paso + 1, movimientos, n):
                return True
            tablero[fila][col] = -1
    return False


# (★★★) Implementar un algoritmo de backtracking que, dados dos grafos, determine si existe un Isomorfismo entre ambos.

# Para este ejercicio, vamos a construir paso a paso una asignacion de vertices de g1 a g2, asegurando que las conexiones
# de las aristas se mantengan. 
# Podas:
# - Verificamos mismo numero de vertices
# - Verificamos grados de vertices de los grafos
# - En cada paso, evitamos vertices de g2 que ya se agisnaron
# - Validamos que al avanzar, la asignacion parcial respete las aristas

def isomorfismo(g1, g2):
    vertices_g1 = g1.obtener_vertices()
    vertices_g2 = g2.obtener_vertices()
    if len(vertices_g1) != len(vertices_g2):
        return False
    grados_g1 = []
    grados_g2 = []
    for v in vertices_g1:
        grados_g1.append(len(g1.adyacentes(v)))
    for w in vertices_g2:
        grados_g2.append(len(g2.adyacentes(w)))
    grados_g1.sort()
    grados_g2.sort()
    if grados_g1 != grados_g2:
        return False
    return backtracking_isomorfismo(g1, g2, {}, set(), vertices_g1, vertices_g2) 

def isomorfismo_valido(g1, g2, asignaciones):
    for v1 in asignaciones:
        for w1 in g1.adyacentes(v1):
            if w1 in asignaciones:
                v2 = asignaciones[v1]
                w2 = asignaciones[w1]
                if not g2.estan_unidos(v2,w2):
                    return False
    return True

def backtracking_isomorfismo(g1, g2, asignaciones, usados, vertices_g1, vertices_g2):
    if len(asignaciones) == len(vertices_g1):
        return True
    v1 = vertices_g1[len(asignaciones)]
    for v2 in vertices_g2:
        if v2 in usados:
            continue
        asignaciones[v1] = v2
        usados.add(v2)
        if isomorfismo_valido(g1, g2, asignaciones):
            if backtracking_isomorfismo(g1, g2, asignaciones, usados, vertices_g1, vertices_g2):
                return True       
        del asignaciones[v1]
        usados.remove(v2)
    return False


# (★) Se tiene una lista de materias que deben ser cursadas en el mismo cuatrimestre, cada materia está 
#  representada con una lista de cursos/horarios posibles a cursar (solo debe elegirse un horario por cada curso). 
#  Cada materia puede tener varios cursos. Implementar un algoritmo de backtracking que devuelva un listado con 
#  todas las combinaciones posibles que permitan asistir a un curso de cada materia sin que se solapen los horarios. 
#  Considerar que existe una función son_compatibles(curso_1, curso_2) que dados dos cursos devuelve un valor booleano 
#  que indica si se pueden cursar al mismo tiempo.

# Para este ejercicio, recorreremos las materias agregando un curso y probando si esa asignacion es valida con la que ya
# teniamos, y asi consiguiendo agregar 1 curso de cada materia.
# Podas:
# - Cuando vamos a agreagr un curso, validamos que sea compatible con los otros ya elegidos hasta el momento.

def horarios_posibles(materias):
    return backtracking_horarios_posibles(materias, 0, [], [])

def cursos_son_validos(curso, sol_parcial):
    for i in sol_parcial:
        if not son_compatibles(i, curso): # type: ignore
            return False
    return True

def backtracking_horarios_posibles(materias, indice, sol_parcial, soluciones):
    if indice == len(materias):
        soluciones.append(sol_parcial.copy())
        return soluciones
    materia = materias[indice]
    for curso in materia:
        if cursos_son_validos(curso, sol_parcial):
            sol_parcial.append(curso)
            backtracking_horarios_posibles(materias, indice + 1, sol_parcial, soluciones)
            sol_parcial.pop()
    return soluciones


# (★) Implementar un algoritmo tipo Backtracking que reciba una cantidad de dados n 
# y una suma s. La función debe devolver todas las tiradas posibles de n dados cuya suma es s

# Para este ejercicio de Sumset Sum, vamos a mostrar todas las combinaciones posibles con n dados 
# tal que la suma de ellos sea s
# Podas:
# - Si encuentro una suma valida la devuelvo
# - Si ya no puedo llegar a la suma final, corto

def sumbet_sum_dados(n, s):
    return backtracking_ssd(n, s, 0, [])

def backtracking_ssd(cant_dados, suma_total, suma_actual, resultado):
    if cant_dados == 0:
        if suma_actual == suma_total:
            return [resultado.copy()]
        
    if suma_actual + cant_dados > suma_total or suma_actual + 6 * cant_dados < suma_total:
        return []
    
    soluciones = []
    for i in range(1,7):
        resultado.append(i)
        soluciones += backtracking_ssd(cant_dados - 1, suma_total, suma_actual + i, resultado)
        resultado.pop()

    return soluciones

# (★) Escribir un algoritmo que, utilizando backtracking, dada una lista de enteros positivos 
# L y un entero n devuelva todos los subconjuntos de L que suman exactamente n.

# Para este ejercicio, haremos masomenos lo mismo que el anterior, mostraremos todas las posibles combinaciones
# de elementos en L tal que sumen n
# Podas:
# - Si la suma ya paso el valor n, corta
# - Si ya se recorrieron todos los elementos de la lista y no se llego a n, corta

def sumbet_sum(L, n):
    L.sort()
    return backtracking_ss(L, n, 0, [], [])

def backtracking_ss(L, n, indice, sol_parcial, solucion):
    if sum(sol_parcial) == n:
        solucion.append(sol_parcial.copy())
        return solucion    
    if sum(sol_parcial) > n or indice >= len(L):
        return solucion
    
    numero = L[indice]
    sol_parcial.append(numero)
    backtracking_ss(L, n, indice+1, sol_parcial, solucion) 
    sol_parcial.pop()
    backtracking_ss(L, n, indice+1, sol_parcial, solucion)
    return solucion


# (★) Modificar el algoritmo anterior para que, dada una lista de enteros positivos L y un entero n,
#  devuelva un subconjunto de L que sume exactamente n, o, en caso de no existir, que devuelva el 
#  subconjunto de suma máxima sin superar el valor de n.

# Para este ejercicio, haremos masomenos lo mismo que el anterior, mostraremos la primer combinacion
# de elementos en L tal que sumen n o, en caso de no haber, la combinacion maxima de valores sumados de L
# sin superar a n
# Podas:
# - Si la suma parcial ya dio n, la devolvemos
# - Si la suma parcial ya paso el valor n o el indice supero la cantidad de numeros, devolvemos la suma maxima
# - Si la suma actual + el numero ya superan a n, sigo con otro numero

def sumbet_sum_modificado(L, n):
    L.sort()
    return backtracking_ss(L, n, 0, [], [], [0])

def backtracking_ss(L, n, indice, sol_parcial, solucion, mejor_suma):
    suma_actual = sum(sol_parcial)
    if suma_actual == n:
        return sol_parcial.copy()
    if suma_actual > n or indice >= len(L):
        if suma_actual > mejor_suma[0]:
            solucion.clear()
            solucion.extend(sol_parcial)
            mejor_suma[0] = suma_actual
        return solucion.copy()
    
    numero = L[indice]
    if suma_actual + numero <= n:
        sol_parcial.append(numero)
        resultado = backtracking_ss(L, n, indice + 1, sol_parcial, solucion, mejor_suma)
        if resultado is not None and sum(resultado) == n:
            return resultado
        sol_parcial.pop()
    return backtracking_ss(L, n, indice + 1, sol_parcial, solucion, mejor_suma)

print(sumbet_sum_modificado([3, 5, 6], 13))


# (★★) Un Vertex Cover de un Grafo G es un conjunto de vértices del grafo en el cual todas las aristas 
# del grafo tienen al menos uno de sus extremos en dicho conjunto. Por ejemplo, el conjunto de todos los vértices 
# del grafo siempre será un Vertex Cover. Implementar un algoritmo que dado un Grafo no dirigido nos devuelva un 
# conjunto de vértices que representen un mínimo Vertex Cover del mismo (es decir, que sea el conjunto de tamaño mínimo).

# Para este ejercicio recorreremos todas las combinaciones posibles de vertices tal que esa combinacion cubra a todas
# las aristas y sea la minima encontrada.
# Podas:
# - Si el conjunto actual ya tiene mas vertices que la mejor solucion parcial, cortamos
# - Si ya cubrimos todas las aristas no seguimos

def vertex_cover_minimo(grafo):
    aristas = obtener_aristas(grafo)
    vertices = grafo.obtener_vertices()
    mejor_sol = [set(vertices)]  # arranca con todos los vértices (siempre válido)
    return backtracking_vc(grafo, aristas, vertices, 0, set(), mejor_sol)

def backtracking_vc(grafo, aristas, vertices, indice, actual, mejor_sol):
    # Si ya es peor que la mejor, corto
    if len(actual) >= len(mejor_sol[0]):
        return mejor_sol[0]

    # Si ya cubrí todas las aristas, actualizo la mejor solución
    if cubre_todas(actual, aristas):
        mejor_sol[0] = actual.copy()
        return mejor_sol[0]
    if indice >= len(vertices):
        return mejor_sol[0]

    v = vertices[indice]

    actual.add(v)
    backtracking_vc(grafo, aristas, vertices, indice + 1, actual, mejor_sol)
    actual.remove(v)
    backtracking_vc(grafo, aristas, vertices, indice + 1, actual, mejor_sol)

    return mejor_sol[0]

def obtener_aristas(grafo):
    aristas = set()
    for v in grafo:
        for w in grafo.adyacentes(v):
            if (w, v) not in aristas:
                aristas.add((v, w))
    return list(aristas)

def cubre_todas(conjunto, aristas):
    for u, v in aristas:
        if u not in conjunto and v not in conjunto:
            return False
    return True

