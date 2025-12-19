import os
import sys

# Errores
FILE_NOT_FOUND = "ERROR: El archivo no existe"
ARGUMENTOS_INSUFICIENTES = "Se necesita un argumento {indice} en entrada estandar"
NOT_A_DIRECTORY = "La carpeta especificada no existe"

# Archivo de resultados (para mediciones de tiempo)
HEADER_RESULTADOS = "Tamano de entrada -- Tiempo de ejecucion promedio\n\n"
SALIDA_MEDICION = "        {valor}              {tiempo} seg \n"


def parseo(ruta, dicc=True):
    """
    Parsea todos los archivos de un directorio.

    Si dicc=True devuelve un diccionario:
        { tamano: (instancia, nombre_archivo) }
        donde instancia = (n, x, f)
    Si dicc=False devuelve:
        lista de instancias, lista de nombres de archivos
    """

    if not os.path.exists(ruta):
        raise NotADirectoryError(f"Directorio no encontrado: {ruta}")

    nombres_archivos = []
    instancias_totales = []

    for nombre_archivo in os.listdir(ruta):
        if nombre_archivo == "Resultados Esperados.txt":
            continue
        ruta_archivo = os.path.join(ruta, nombre_archivo)
        if not os.path.isfile(ruta_archivo):
            continue
        try:
            instancia = parsear_archivo(ruta_archivo)
            nombres_archivos.append(nombre_archivo)
            instancias_totales.append(instancia)
        except Exception as e:
            print(f"Error al parsear {nombre_archivo}: {e}")
            continue

    if not dicc:
        return instancias_totales, nombres_archivos

    dicc_pruebas = {}
    for i in range(len(instancias_totales)):
        n, _, _ = instancias_totales[i]
        dicc_pruebas[n] = (instancias_totales[i], nombres_archivos[i])
    return dicc_pruebas


def parsear_archivo(ruta):
    """
    Parsea un archivo de instancia.
    Formato esperado:
        # comentario
        n
        x_1
        x_2
        ...
        x_n
        f(1)
        f(2)
        ...
        f(n)

    Devuelve una tupla (n, x, f) donde:
        n = cantidad de minutos
        x = lista 1-indexada de llegadas
        f = lista 1-indexada de recarga acumulada
    """
    if not os.path.exists(ruta):
        raise FileNotFoundError(FILE_NOT_FOUND)

    with open(ruta, "r") as f:
        lineas = [line.strip() for line in f if line.strip()]

    if lineas[0].startswith("#"):
        lineas = lineas[1:]

    n = int(lineas[0])
    x_vals = list(map(int, lineas[1:1+n]))
    f_vals = list(map(int, lineas[1+n:1+2*n]))

    x = [None] + x_vals
    f = [None] + f_vals
    return n, x, f

def leer_archivo():
    """
    Lee de entrada estandar el nombre del archivo a probar
    """
    if len(sys.argv) < 2:
        raise FileNotFoundError(FILE_NOT_FOUND)

    nombre = sys.argv[1]
    if not os.path.exists(nombre):
        raise FileNotFoundError(FILE_NOT_FOUND)

    return nombre


def leer_directorio(indice):
    """
    Lee el argumento <indice> de la entrada estandar y comprueba que sea una carpeta.
    """
    if indice >= len(sys.argv):
        raise Exception(ARGUMENTOS_INSUFICIENTES.format(indice=indice))

    carpeta = sys.argv[indice]
    if not os.path.exists(carpeta):
        raise NotADirectoryError(NOT_A_DIRECTORY)

    return carpeta

def crear_archivo_resultados(resultados, ruta):
    """
    Recibe los resultados de una prueba y los escribe en la ruta especificada.
    """
    with open(ruta, "w") as archivo:
        archivo.write(HEADER_RESULTADOS)
        for valor, tiempo in resultados:
            archivo.write(SALIDA_MEDICION.format(valor=valor, tiempo=tiempo))
