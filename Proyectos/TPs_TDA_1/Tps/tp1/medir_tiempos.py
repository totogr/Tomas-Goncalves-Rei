from detector_rata_greedy import verificar_sospechoso
from extras.tiempos_y_complejidad.mediciones import time_algorithm
from archivos.archivos import parseo, leer_directorio, crear_archivo_resultados
import os
import sys

NOMBRE_ARCHIVO = "resultados_tiempos_solapamientos_totales.txt"

# Errores
ERROR_ARGUMENTOS = "No se especifico la carpeta de salida. Uso: medir_tiempos.py <carpeta_pruebas> <carpeta_salida>"

def hacer_medicion():
    carpeta = leer_directorio(1)
    ruta_pruebas = os.path.join(os.getcwd(), carpeta)
    dicc_pruebas = parseo(ruta_pruebas)
    
    resultados = time_algorithm(
        algorithm=verificar_sospechoso,
        sizes=dicc_pruebas.keys(),
        get_args=lambda x: (dicc_pruebas[x][0], dicc_pruebas[x][1])
    )

    resultados = resultados.items()
    resultados = sorted(resultados, key=lambda x: x[0])

    return resultados

def medir_tiempos():
    """
    Mide los tiempos del algoritmo con el set de pruebas especificado
    Recibe de entrada estandar:
    1. Carpeta del set de pruebas
    2. Carpeta de salida de los resultados
    Los resultados se guardan en la carpeta recibida como segundo parametro, con el nombre especificado
    en la constante NOMBRE_ARCHIVO (resultados_tiempos.txt por defecto).
    """
    if len(sys.argv) != 3:
        raise Exception(ERROR_ARGUMENTOS)
    carpeta = leer_directorio(2)
    ruta = os.path.join(os.getcwd(), carpeta, NOMBRE_ARCHIVO) 

    resultados = hacer_medicion()
    crear_archivo_resultados(resultados, ruta)

medir_tiempos()

# Uso: python3 medir_tiempos.py <carpeta_pruebas> <carpeta_salida>