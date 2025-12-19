import os
import sys
import random

# Constantes para los generadores

# Formatos de generacion:
# (n, rango_x, rango_f)
SETS_PRUEBAS = [
    (10, (100, 1000), (10, 2000)),
    (20, (100, 1000), (10, 2000)),
    (30, (100, 1000), (10, 2000)),
    (40, (100, 1000), (10, 2000)),
    (50, (100, 1000), (10, 2000)),
    (60, (100, 1000), (10, 2000)),
    (70, (100, 1000), (10, 2000)),
    (80, (100, 1000), (10, 2000)),
    (90, (100, 1000), (10, 2000)),
    (100, (100, 1000), (10, 2000)),
]

# Errores
FILE_NOT_FOUND = "ERROR: El archivo no existe"
ARGUMENTOS_INSUFICIENTES = "Se necesita un argumento {indice} en entrada estandar"
NOT_A_DIRECTORY = "La carpeta especificada no existe"

def leer_directorio(indice):
    if indice >= len(sys.argv):
        raise Exception(ARGUMENTOS_INSUFICIENTES.format(indice=indice))
    
    carpeta = sys.argv[indice]
    if not os.path.exists(carpeta):
        os.makedirs(carpeta)
    
    return carpeta

def guardar_archivo(ruta, n, x, f):
    """
    Guarda una instancia del problema actual (formato del TP).
    """
    with open(ruta, "w") as archivo:
        archivo.write("# La primera linea indica la cantidad de minutos a considerar (n). Luego vienen n lineas que corresponden a los x_i, y luego los n valores que corresponden a la funcion f(.)\n")
        archivo.write(f"{n}\n")
        for xi in x:
            archivo.write(f"{xi}\n")
        for fi in f:
            archivo.write(f"{fi}\n")