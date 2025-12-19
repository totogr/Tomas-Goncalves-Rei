import os
import sys
import random
from utilidades import SETS_PRUEBAS, leer_directorio, guardar_archivo

def crear_sets_prueba():
    """
    Genera sets de prueba con pares (tiempo, peso)
    """
    carpeta = leer_directorio(1)    
    
    for n, rango_x, rango_f in SETS_PRUEBAS:
        x = generar_lista(n, rango_x)
        f = generar_funcion_f(n, rango_f)
        
        # Nombre del archivo: {n}.txt
        archivo = os.path.join(carpeta, f"{n}.txt")
        guardar_archivo(archivo, n, x, f)

def generar_funcion_f(n, rango):
    """
    Genera una funcion f con n valores en el rango dado incremental sin repetidos.
    """
    f = set()
    for _ in range(n):
        val = random.randint(rango[0], rango[1])
        while val in f:
            val = random.randint(rango[0], rango[1])
        f.add(val)
    f = list(f)
    f.sort()
    return f

def generar_lista(n, rango):
    """
    Genera una lista de n valores enteros aleatorios en el rango dado.
    """
    return [random.randint(rango[0], rango[1]) for _ in range(n)]

def generar_datos_prueba(n, rango_x, rango_f):
    """
    Genera datos de prueba segun n y los rangos dados.
    rango_x y rango_f son tuplas (min, max)
    """
    x = generar_lista(n, rango_x)
    f = generar_funcion_f(n, rango_f)

    x = [None] + x
    f = [None] + f
    
    return x, f

if __name__ == "__main__":
    crear_sets_prueba()
