import random
import os

RANGO_MAX_TIMESTAMP = 2000
PRIMERA_LINEA = "Primero viene la cantidad (n) de timestamps para ambos, luego n líneas que son un timestamp aproximado cada uno separado por una coma (',') del error, y luego n lineas de las transacciones del sospechoso"

from utilidades import (
    leer_directorio,
    TAMANIOS_PRUEBAS,
    PRIMERA_LINEA,
    NOMBRE_ARCHIVO_ES,
    FORMATO_INTERVALO,
    FORMATO_TIMESTAMP
)


def crear_archivos_pruebas():
    for tamanio in TAMANIOS_PRUEBAS:
        timestamps, intervalos = generador_pruebas(tamanio)
        generar_archivo(timestamps, intervalos)

#Pruebas con muchos solapamientos
def generador_pruebas(cantidad_timestamps):
    timestamps = [] 
    intervalos = [] # acordarse lo de margen de error -> // t y e

    for i in range(cantidad_timestamps):
        # El proximo timestamp estara entre tiempo y tiempo + RANGO_MAX_TIMESTAMP
        si = random.randint(0, RANGO_MAX_TIMESTAMP)
        timestamps.append(si)
        
        # Calculo el intervalo correspondiente
        desde_intervalo = random.randint(0, si)
        hasta_intervalo = random.randint(si, RANGO_MAX_TIMESTAMP)
        mitad = (desde_intervalo + hasta_intervalo) // 2
        intervalo = (mitad, hasta_intervalo-mitad) # intevalo = (tiempo,error)
        intervalos.append(intervalo)
    
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