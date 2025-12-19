from desencriptacion import cadena_es_mensaje
from extras.tiempos_y_complejidad.mediciones import time_algorithm
from archivos.archivos import parsear_carpeta_textos, parsear_palabras, leer_argumento, crear_archivo_mediciones, TIPO_CARPETA, TIPO_ARCHIVO
import os
import sys

# Errores
ERROR_ARGUMENTOS = "No se especifico el archivo de salida. Uso: medir_tiempos.py <carpeta_pruebas> <archivo_salida>"

def hacer_medicion(carpeta_textos, archivo_palabras):
    """
    Mide los tiempos del algoritmo para diferentes tamaños según los textos
    y palabras recibidos.

    Devuelve una lista de listas donde cada sublista tiene el formato [tamanio:tiempo]
    """
    textos = parsear_carpeta_textos(carpeta_textos)
    palabras = parsear_palabras(archivo_palabras)
    
    dicc_pruebas = crear_dicc_pruebas(textos, palabras)

    resultados = time_algorithm(
            algorithm=cadena_es_mensaje,
            sizes=dicc_pruebas.keys(),
            get_args=lambda x: [dicc_pruebas[x][0], dicc_pruebas[x][1]]
        )

    return sorted(resultados.items(), key=lambda x: int(x[0]))
    

def medir_tiempos():
    """
    Mide los tiempos del algoritmo con el set de pruebas especificado
    Recibe de entrada estandar:
    1. Carpeta del set de pruebas
    2. Carpeta de salida de los resultados
    Los resultados se guardan en la carpeta recibida como segundo parametro, con el nombre especificado
    en la constante NOMBRE_ARCHIVO (resultados_tiempos.txt por defecto).
    """
    if len(sys.argv) != 4:
        raise Exception(ERROR_ARGUMENTOS)

    carpeta_textos = leer_argumento(1, TIPO_CARPETA)
    archivo_palabras = leer_argumento(2, TIPO_ARCHIVO)
    archivo_salida = sys.argv[3]    

    os.makedirs(os.path.dirname(archivo_salida), exist_ok=True)

    resultados = hacer_medicion(carpeta_textos, archivo_palabras)
    crear_archivo_mediciones(resultados, archivo_salida)

def crear_dicc_pruebas(textos, palabras):
    """
    Recibe una diccionario del formato {nombre_archivo:textos_archivo} 
    y una lista de palabras.
    Crea un diccionario del formato {tamanio_archivo:[arg1, arg2]},
    donde arg1 y arg2 son los argumentos para la llamada al algoritmo con
    ese tamaño (en este caso, las palabaras y los textos de ese archivo)
    """
    dicc_pruebas = {}
    for nombre_archivo in textos:
        texto_archivo = textos[nombre_archivo][0]
        tamanio = int(nombre_archivo.split("-")[0])
        dicc_pruebas[tamanio] = [palabras, texto_archivo]
    
    return dicc_pruebas
    

if __name__ == "__main__":
    medir_tiempos()

# Uso: python3 medir_tiempos.py <carpeta_textos> <archivo_palabras> <archivo_salida>