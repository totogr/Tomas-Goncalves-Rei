import sys
import os
import heapq

sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))

from archivos.parseo import parsear_archivo_tribu


def backtrack_recursivo(indice, k, n, habilidades, nombres, cargas, valor_actual, asignacion_actual, mejor, suma_restante):
    """
    Funcion recursiva que explora todas las asignaciones posibles de maestros a grupos
    minimizando la suma de cuadrados de las sumas por grupo.
    Usa poda y ruptura de simetrias.
    """

    mejor_valor, mejor_asignacion = mejor

    # Caso base: todos los maestros fueron asignados
    if indice == n:
        if valor_actual < mejor_valor:
            return valor_actual, asignacion_actual[:]
        return mejor

    habilidad = habilidades[indice]
    usado_vacio = False
    usados_anteriores = set()

    grupos_ordenados = sorted(range(k), key=lambda g: cargas[g])

    for g in grupos_ordenados:
        vacio = (cargas[g] == 0)
        if cargas[g] in usados_anteriores:
            continue
        if vacio and usado_vacio:
            continue

        # Cota inferior: si incluso distribuyendo el resto perfectamente no mejora el mejor valor, podar
        resto = suma_restante[indice + 1]
        if valor_actual + (resto * resto) / k >= mejor_valor:
            continue

        incremento = 2 * cargas[g] * habilidad + habilidad * habilidad
        nuevo_valor = valor_actual + incremento

        if nuevo_valor >= mejor_valor:
            continue

        # Asignar maestro al grupo g
        cargas[g] += habilidad
        asignacion_actual[indice] = g
        if vacio:
            usado_vacio = True

        mejor = backtrack_recursivo(indice + 1, k, n, habilidades, nombres, cargas, nuevo_valor, asignacion_actual, mejor, suma_restante)

        # Deshacer asignacion
        cargas[g] -= habilidad
        asignacion_actual[indice] = -1
        if vacio:
            usado_vacio = False

        mejor_valor, _ = mejor
        usados_anteriores.add(cargas[g])

    return mejor


def tribu_del_agua_backtracking(k, maestros):
    """
    Funcion principal que prepara los datos y llama al backtracking.
    Devuelve el valor minimo y los grupos optimos.
    """

    # Ordenamos de mayor a menor habilidad
    maestros = sorted(maestros, key=lambda x: x[1], reverse=True)
    n = len(maestros)
    habilidades = [m[1] for m in maestros]
    nombres = [m[0] for m in maestros]

    cargas = [0] * k
    asignacion_actual = [-1] * n

    cargas_greedy = [0] * k
    asignacion_greedy = [-1] * n
    
    # igual q en aprox para usar de cota superior
    min_heap = [(0, i) for i in range(k)]
    heapq.heapify(min_heap)
    
    for i, habilidad in enumerate(habilidades):
        carga_actual, g_idx = heapq.heappop(min_heap)
        
        carga_nueva = carga_actual + habilidad
        cargas_greedy[g_idx] = carga_nueva
        asignacion_greedy[i] = g_idx
        
        heapq.heappush(min_heap, (carga_nueva, g_idx))
        
    mejor_valor = sum(c**2 for c in cargas_greedy)
    mejor_asignacion = asignacion_greedy[:]


    # Precomputar sumas restantes (suma_restante[i] = suma de habilidades desde i hasta fin)
    suma_restante = [0] * (n + 1)
    for i in range(n - 1, -1, -1):
        suma_restante[i] = suma_restante[i + 1] + habilidades[i]

    mejor_valor, mejor_asignacion = backtrack_recursivo(0, k, n, habilidades, nombres, cargas, 0, asignacion_actual, (mejor_valor, mejor_asignacion), suma_restante)

    # Construir los grupos finales
    grupos_finales = [[] for _ in range(k)]
    for i, g in enumerate(mejor_asignacion):
        grupos_finales[g].append(nombres[i])

    return mejor_valor, grupos_finales


def main():
    if len(sys.argv) < 2:
        print("Uso: python3 backtracking.py <archivo_entrada>")
        sys.exit(1)

    ruta_archivo = sys.argv[1]
    k, maestros = parsear_archivo_tribu(ruta_archivo)

    mejor_valor, grupos = tribu_del_agua_backtracking(k, maestros)

    for i, grupo in enumerate(grupos, start=1):
        suma = sum(next(h for n, h in maestros if n == nombre) for nombre in grupo)
        print(f"Grupo {i}: {', '.join(grupo)}")
    print(f"Coeficiente = {mejor_valor}\n")

if __name__ == "__main__":
    main()