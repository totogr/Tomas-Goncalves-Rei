import os
import sys
import random
from utilidades import SETS_PRUEBAS, leer_directorio, guardar_archivo

def crear_sets_prueba():
    """
    Genera sets de prueba con pares (tiempo, peso)
    """
    carpeta = leer_directorio(1)    
    
    for cantidad_guerras, max_tiempo, max_peso in SETS_PRUEBAS:
        guerras = generar_guerras(cantidad_guerras, max_tiempo, max_peso)
        
        # Nombre del archivo: {cantidad_guerras}.txt
        archivo = os.path.join(carpeta, f"{cantidad_guerras}.txt")
        guardar_archivo(archivo, guerras)

def generar_guerras(cantidad, max_tiempo, max_peso):
    """
    Genera 'cantidad' de pares (tiempo, peso)
    tiempo aleatorio entre max_tiempo
    peso aleatorio entre max_peso    
    """
    guerras = []
    for _ in range(cantidad):
        tiempo = random.randint(max_tiempo[0], max_tiempo[1])
        peso = random.randint(max_peso[0], max_peso[1])
        guerras.append((tiempo, peso))
    return guerras

if __name__ == "__main__":
    crear_sets_prueba()
