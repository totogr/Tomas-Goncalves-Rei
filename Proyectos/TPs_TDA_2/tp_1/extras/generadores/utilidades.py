import os
import sys
import random

# Constantes para los generadores

# (cantidad_de_batallas, rango_tiempo, rango_peso)

SETS_PRUEBAS = [(10,(1, 50), (100,100)),
(20,(1, 50), (100,100)),
(30,(1, 50), (100,100)),
(40,(1, 50), (100,100)),
(50,(1, 50), (100,100)),
(60,(1, 50), (100,100)),
(70,(1, 50), (100,100)),
(80,(1, 50), (100,100)),
(90,(1, 50), (100,100)),
(100,(1, 50), (100,100))]

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

def guardar_archivo(ruta, guerras):
    """
    Guarda un set de guerras en un archivo.
    Cada linea tiene formato: tiempo,peso
    """
    with open(ruta, "w") as f:
        f.write("T_i,B_i\n")
        for tiempo, peso in guerras:
            f.write(f"{tiempo},{peso}\n")
