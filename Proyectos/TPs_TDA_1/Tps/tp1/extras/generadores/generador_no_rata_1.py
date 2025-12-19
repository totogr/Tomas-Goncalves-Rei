import random
import os
from utilidades import (
    leer_directorio,
    TAMANIOS_PRUEBAS,
    PRIMERA_LINEA,
    NOMBRE_ARCHIVO_NO_ES,
    FORMATO_INTERVALO,
    FORMATO_TIMESTAMP
)

RANGO_MAX_TIEMPO = 2000
RANGO_ENTRE_TIEMPOS = 100
RANGO_MAX_MARGEN_ERROR = 50

#Pruebas negativas
def generador_pruebas(cantidad_timestamps):
    timestamps = [] 
    intervalos = [] 
    tiempo = 1
    for i in range(cantidad_timestamps):
        if tiempo>RANGO_MAX_TIEMPO:
            tiempo=1

        ti = random.randint(tiempo, tiempo + RANGO_ENTRE_TIEMPOS)
        if ti>RANGO_MAX_TIEMPO:
            ti = RANGO_MAX_TIEMPO - random.randint(1,RANGO_ENTRE_TIEMPOS)
        tiempo+=ti
        ei = random.randint(1, RANGO_MAX_MARGEN_ERROR)
        if ti+ei>RANGO_MAX_TIEMPO:
            ei= ti + random.randint(RANGO_MAX_TIEMPO-ti,ti)
        if ti-ei<=0:
            ei= ti -1
        
        intervalo = (ti, ei) # intevalo = (tiempo,error)
        intervalos.append(intervalo)
        
        if i % 2 != 0: # si i no es par genero un error
            azar = random.randint(1,RANGO_MAX_TIEMPO)
            if azar>ti:
                si = random.randint(min(ti+ei+1,RANGO_MAX_TIEMPO),RANGO_MAX_TIEMPO)
            else:
                si = random.randint(0,max(ti-ei-1,0))
        else:
            si = random.randint(ti-ei,ti+ei)
        timestamps.append(si)
    
    return sorted(timestamps), intervalos

def generar_archivo(timestamps, intervalos):
    n = len(intervalos)
    nombre_archivo = NOMBRE_ARCHIVO_NO_ES.format(n=n)
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

def crear_archivos_pruebas():
    for tamanio in TAMANIOS_PRUEBAS:
        timestamps, intervalos = generador_pruebas(tamanio)
        generar_archivo(timestamps, intervalos)

crear_archivos_pruebas()