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
DISTANCIA_ENTRE_INTERVALOS = 5
LONGITUD_MAXIMA_INTERVALOS = 20

def crear_archivos_pruebas():
    for tamanio in TAMANIOS_PRUEBAS:
        timestamps, intervalos = generador_pruebas(tamanio)
        generar_archivo(timestamps, intervalos)

#Pruebas con INTERVALOS SIN SOLAPAMIENTOS TOTALES
def generador_pruebas(cantidad_timestamps):
    timestamps = [] 
    intervalos = [] 
    desde = 1
    for i in range(cantidad_timestamps):
        ti = random.randint(desde,desde+LONGITUD_MAXIMA_INTERVALOS)
        mitad = (desde + ti ) // 2
        intervalo = (mitad,ti-mitad)
        intervalos.append(intervalo)

        si = random.randint(desde,ti)
        timestamps.append(si) 

        desde = DISTANCIA_ENTRE_INTERVALOS + ti
        
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
# python3  generador_rata_4.py <direccion directorio>