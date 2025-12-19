from mediciones import time_algorithm
import os
import sys

sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), "..")))

from archivos.parseo import parseo, leer_directorio, crear_archivo_resultados
from tp1 import algoritmo

NOMBRE_ARCHIVO = "resultados.txt"

# Errores
ERROR_ARGUMENTOS = "No se especifico la carpeta de salida. Uso: medir_tiempos.py <carpeta_pruebas> <carpeta_salida>"

def hacer_medicion():
    """
    Mide los tiempos del algoritmo con el set de pruebas especificado
    Devuelve una lista de tuplas (tamano, tiempo_promedio)
    """

    carpeta = leer_directorio(1)
    ruta_pruebas = os.path.join(os.getcwd(), carpeta)
    dicc_pruebas = parseo(ruta_pruebas)
    
    resultados = time_algorithm(
        algorithm=algoritmo,
        sizes=list(dicc_pruebas.keys()),
        get_args=lambda x: (dicc_pruebas[x][0],)
    )

    resultados = sorted(resultados.items(), key=lambda x: x[0])

    return resultados

def medir_tiempos():
    """
    Mide los tiempos del algoritmo con el set de pruebas especificado
    Recibe de entrada estandar:
    1. Carpeta del set de pruebas
    2. Carpeta de salida de los resultados
    Los resultados se guardan en la carpeta recibida como segundo parametro, con el nombre especificado
    en la constante NOMBRE_ARCHIVO.
    """
    if len(sys.argv) != 3:
        raise Exception(ERROR_ARGUMENTOS)
    carpeta = leer_directorio(2)
    ruta = os.path.join(os.getcwd(), carpeta, NOMBRE_ARCHIVO) 

    resultados = hacer_medicion()
    crear_archivo_resultados(resultados, ruta)

if __name__ == "__main__":
    medir_tiempos()

# Uso: python3 medir_tiempos.py <carpeta_pruebas> <carpeta_salida>