import random
import os
from utilidades import (
    leer_directorio,
    TAMANIOS_PRUEBAS,
    PRIMERA_LINEA,
    NOMBRE_ARCHIVO_ES,
    FORMATO_INTERVALO,
    FORMATO_TIMESTAMP
)

INICIO_INTERVALOS = 1
FIN_INTERVALOS = 2000

def crear_archivos_pruebas():
    for tamanio in TAMANIOS_PRUEBAS:
        timestamps, intervalos = generador_pruebas(tamanio)
        generar_archivo(timestamps, intervalos)

#Pruebas con INTERVALOS CON SOLAPAMIENTOS TOTALES
def generador_pruebas(cantidad_timestamps):
    timestamps = [] 
    intervalos = [] 
    for i in range(cantidad_timestamps):
        mitad = (FIN_INTERVALOS + INICIO_INTERVALOS) // 2 #para definir margen de error
        intervalo = (mitad, FIN_INTERVALOS-mitad)
        intervalos.append(intervalo)

        si = random.randint(INICIO_INTERVALOS,FIN_INTERVALOS)
        timestamps.append(si) 
        
    return sorted(timestamps), intervalos

def generar_archivo(timestamps, intervalos):
    n = len(intervalos)
    nombre_archivo = NOMBRE_ARCHIVO_ES.format(n=n)
    carpeta = leer_directorio(1)
    ruta = os.path.join(os.getcwd(), carpeta, nombre_archivo)

    with open(ruta, "w") as archivo:
        # Escribir linea con instrucciones para leer
        archivo.write(f"{PRIMERA_LINEA}\n")
        
        # Escribo la cantidad n de timestamps
        archivo.write(f"{n}\n")
        
        for intervalo, error in intervalos:
            archivo.write(FORMATO_INTERVALO.format(ti=intervalo, error=error))
        for timestamp in timestamps:
            archivo.write(FORMATO_TIMESTAMP.format(timestamp=timestamp))

crear_archivos_pruebas()

#Utilizar con :
# python3  generador_rata_3.py <direccion directorio>