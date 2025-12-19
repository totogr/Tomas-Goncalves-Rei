import random
import os
import sys

from utilidades import (
    CANTIDAD_REPETICIONES_MUY_BAJA,
    CANTIDAD_REPETICIONES_BAJA,
    CANTIDAD_REPETICIONES_ALTA,
    SETS_PRUEBAS,
    leer_directorio,
    parsear_palabras,
    NOMBRE_ARCHIVO_ES,
)

def crear_archivos_pruebas():
    palabras = parsear_palabras(sys.argv[1])
    for cantidad_palabras,cantidad_mensajes in SETS_PRUEBAS:
        mensajesCodificados = generador_mensajes(palabras,cantidad_palabras,cantidad_mensajes)
        generar_archivo(mensajesCodificados)

# Generador de mensajes que se pueden desencriptar
# Muchas veces la misma palabra (seguida)
# Se puede variar cantidad maxima de REPETICIONES con CONSTANTES
def generador_mensajes(palabras,cantidad_palabras,cantidad_mensajes):
    palabras = list(palabras)
    mensajesCodificados = []

    for i in range(cantidad_mensajes):
        mensajeActual = []

        cantidad_restante = cantidad_palabras
        for i in range(cantidad_palabras):

            if cantidad_restante == 0:
                break

            indice = random.randint(0,len(palabras)-1)
            palabra_random = palabras[indice]

            repetir_palabra = random.randint(0,CANTIDAD_REPETICIONES_MUY_BAJA)

            if repetir_palabra%2 == 0:
                for i in range(repetir_palabra):
                    mensajeActual.append(palabra_random)
                    cantidad_restante-=1
                    if cantidad_restante == 0:
                        break   
            else:
                mensajeActual.append(palabra_random)

        mensajesCodificados.append("".join(mensajeActual))

    return mensajesCodificados

def generar_archivo(mensajesCodificados):
    n = len(mensajesCodificados)
    nombre_archivo = NOMBRE_ARCHIVO_ES.format(n=n)
    carpeta = leer_directorio(2)
    ruta = os.path.join(os.getcwd(), carpeta, nombre_archivo)

    with open(ruta, "w") as archivo:
        
        for mensaje in mensajesCodificados:
            archivo.write(f"{mensaje}\n")


crear_archivos_pruebas()

#Utilizar con :
# python3  generador_es_1.py  <direccion archivo palabras>  <direccion directorio de mensajes>