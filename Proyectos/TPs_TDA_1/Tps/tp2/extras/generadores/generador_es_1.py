import random
import os
import sys

from utilidades import (
    SETS_PRUEBAS,
    leer_directorio,
    parsear_palabras,
    NOMBRE_ARCHIVO_ES,
)

def crear_archivos_pruebas():
    palabras = parsear_palabras(sys.argv[1])
    for cantidad_palabras,cantidad_mensajes in SETS_PRUEBAS:
        mensajesCodificados = generador_mensajes(palabras,cantidad_palabras,cantidad_mensajes)
        generar_archivo(mensajesCodificados, cantidad_palabras)

# Generador de mensajes que se pueden desencriptar
def generador_mensajes(palabras,cantidad_palabras,cantidad_mensajes):
    palabras = list(palabras)
    mensajesCodificados = []

    for i in range(cantidad_mensajes):
        mensajeActual = []

        for i in range(cantidad_palabras):

            indice = random.randint(0,len(palabras)-1)
            palabra_random = palabras[indice]
            mensajeActual.append(palabra_random)

        mensajesCodificados.append("".join(mensajeActual))

    return mensajesCodificados

def generar_archivo(mensajesCodificados, cantidad_palabras):
    nombre_archivo = NOMBRE_ARCHIVO_ES.format(n=cantidad_palabras)
    carpeta = leer_directorio(2)
    ruta = os.path.join(os.getcwd(), carpeta, nombre_archivo)

    with open(ruta, "w") as archivo:
        
        for mensaje in mensajesCodificados:
            archivo.write(f"{mensaje}\n")


crear_archivos_pruebas()

#Utilizar con :
# python3  generador_es_1.py  <direccion archivo palabras>  <direccion directorio de mensajes>