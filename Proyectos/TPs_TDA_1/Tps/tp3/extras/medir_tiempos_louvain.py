import os
import sys
from archivos import crear_grafos_desde_carpeta, crear_archivo_mediciones
from tiempos_y_complejidad.mediciones import time_algorithm
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))
from solucion_louvain import algoritmo_louvain_para_mediciones

# Constante de error por si no se pasan los argumentos requeridos
ERROR_ARGUMENTOS = "Uso: python medir_tiempos_louvain.py <carpeta_grafos> <archivo_salida>"

def obtener_argumentos():
    if len(sys.argv) != 3:
        print(ERROR_ARGUMENTOS)
        sys.exit(1)

    ruta_carpeta = sys.argv[1]
    archivo_salida = sys.argv[2]

    return ruta_carpeta, archivo_salida

def extraer_tamaño_desde_nombre(nombre_archivo):
    return int(nombre_archivo.split("_")[0].split("-")[1])

def medir_tiempo_de_algoritmo(grafo):
    resultados = time_algorithm(
        algorithm=algoritmo_louvain_para_mediciones,
        sizes=[1],
        get_args=lambda x: (grafo,)
    )
    return resultados[1]

def procesar_grafos_y_medir_tiempos(ruta_carpeta):
    grafos = crear_grafos_desde_carpeta(ruta_carpeta)
    resultados = []

    for nombre_archivo in sorted(grafos.keys(), key=extraer_tamaño_desde_nombre):

        print(f"Procesando archivo: {nombre_archivo}")  # 👈 Print agregado

        grafo = grafos[nombre_archivo]
        tamaño = extraer_tamaño_desde_nombre(nombre_archivo)
        tiempo = medir_tiempo_de_algoritmo(grafo)
        resultados.append((tamaño, tiempo))

    return resultados

def main():
    ruta_carpeta, archivo_salida = obtener_argumentos()
    resultados = procesar_grafos_y_medir_tiempos(ruta_carpeta)
    crear_archivo_mediciones(resultados, archivo_salida)

if __name__ == "__main__":
    main()