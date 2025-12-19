import os
import sys

from archivos import crear_grafos_desde_carpeta, crear_archivo_mediciones
from tiempos_y_complejidad.mediciones import time_algorithm
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))

from solucion_pl import clusterizar
from solucion_greedy import minimizar_distancia_maxima_greedy
from solucion_bt import minimizar_distancia_maxima

ERROR_ARGUMENTOS = (
    "Uso: python medir_tiempos.py <carpeta_grafos> <archivo_salida> <algoritmo>\n"
    "algoritmo: 'pl' para clusterizar, 'greedy' para minimizar_distancia_maxima_greedy, 'bt' para minimizar_distancia_maxima (backtracking)"
)

def parsear_argumentos():
    if len(sys.argv) != 4:
        print(ERROR_ARGUMENTOS)
        sys.exit(1)
    return sys.argv[1], sys.argv[2], sys.argv[3]

def extraer_tamaño_desde_nombre(nombre_archivo):
    return int(nombre_archivo.split("_")[0].split("-")[1])


def solo_ejecutar_clusterizar(g, k):
    clusterizar(g, k)
    return 0

def solo_ejecutar_minimizar_greedy(g, k):
    minimizar_distancia_maxima_greedy(g, k)
    return 0

def solo_ejecutar_minimizar_bt(g, k):
    minimizar_distancia_maxima(g, k)
    return 0

def medir_tiempo(algorithm, grafo, k=3):
    resultados = time_algorithm(
        algorithm=algorithm,
        sizes=[1],
        get_args=lambda x: (grafo, k)
    )
    return resultados[1]

def generar_resultados(carpeta_grafos, algoritmo):
    grafos = crear_grafos_desde_carpeta(carpeta_grafos)
    resultados = []

    if algoritmo == 'pl':
        func = solo_ejecutar_clusterizar
    elif algoritmo == 'greedy':
        func = solo_ejecutar_minimizar_greedy
    elif algoritmo == 'bt':
        func = solo_ejecutar_minimizar_bt
    else:
        print("Algoritmo no reconocido:", algoritmo)
        sys.exit(1)

    for nombre_archivo in sorted(grafos.keys(), key=extraer_tamaño_desde_nombre):

        print(f"Procesando archivo: {nombre_archivo}")

        grafo = grafos[nombre_archivo]
        tamaño = extraer_tamaño_desde_nombre(nombre_archivo)
        if tamaño is None:
            continue

        tiempo = medir_tiempo(func, grafo)
        resultados.append((tamaño, tiempo))

    return resultados

def main():
    ruta_carpeta, archivo_salida, algoritmo = parsear_argumentos()
    resultados = generar_resultados(ruta_carpeta, algoritmo)
    crear_archivo_mediciones(resultados, archivo_salida)

if __name__ == "__main__":
    main()
