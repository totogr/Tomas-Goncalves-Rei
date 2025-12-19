import random
import os
import sys

from utilidades import (
    CANTIDAD_ERRORES_GRANDE,
    CANTIDAD_ERRORES_PEQUEÑA,
    CANTIDAD_ERRORES_MUY_PEQUEÑA,
    SETS_PRUEBAS,
    leer_directorio,
    parsear_palabras,
    NOMBRE_ARCHIVO_NO_ES,
)



def crear_archivos_pruebas():
    palabras = parsear_palabras(sys.argv[1])
    for cantidad_palabras,cantidad_mensajes in SETS_PRUEBAS:
        mensajesCodificados = generador_mensajes(palabras,cantidad_palabras,cantidad_mensajes)
        generar_archivo(mensajesCodificados)



# Generador de mensajes que NO se pueden desencriptar 
# Cantidad errores se puede variar con uso de CONSTANTES
def generador_mensajes(palabras,cantidad_palabras,cantidad_mensajes):
    palabras = list(palabras)
    mensajesCodificados = []
    multiplicacion_errores = random.randint(1,8)
    cantidad_errores = multiplicacion_errores * CANTIDAD_ERRORES_GRANDE

    for i in range(cantidad_mensajes):
        mensajeActual = []

        for i in range(cantidad_palabras):

            indice = random.randint(0,len(palabras)-1)
            palabra_random = palabras[indice]

            hay_error = random.randint(0,14)

            if hay_error%2 == 0 and cantidad_errores>0:
                cantidad_errores-=1
                if hay_error == 0:
                    palabra_random = palabra_random[1:]
                elif hay_error == 2:
                    palabra_random = palabra_random[:len(palabra_random)-1]
                elif hay_error == 4:
                    palabra_random = palabra_random[::2]
                elif hay_error == 6 :
                    palabra_random = palabra_random[::-1]
                elif hay_error == 8 :
                    palabra_random = palabra_random[2:]
                elif hay_error == 10 :
                    letraAleatoria = random.randint(0,len(palabra_random)-1)
                    pR = [palabra_random,palabra_random[letraAleatoria]]
                    palabra_random = "".join(pR)
                elif hay_error == 12:
                    letraAleatoria = random.randint(0,len(palabra_random)-1)
                    pR = [palabra_random[letraAleatoria],palabra_random]
                    palabra_random = "".join(pR)
                else:
                    letraAleatoria1 = random.randint(0,len(palabra_random)-1)
                    letraAleatoria2 = random.randint(0,len(palabra_random)-1)
                    letraAleatoria3 = random.randint(0,len(palabra_random)-1)
                    pR = [palabra_random,palabra_random[letraAleatoria1],palabra_random[letraAleatoria2],palabra_random[letraAleatoria3]]
                    palabra_random = "".join(pR)

            mensajeActual.append(palabra_random)

        mensajesCodificados.append("".join(mensajeActual))

    return mensajesCodificados




def generar_archivo(mensajesCodificados):
    n = len(mensajesCodificados)
    nombre_archivo = NOMBRE_ARCHIVO_NO_ES.format(n=n)
    carpeta = leer_directorio(2)
    ruta = os.path.join(os.getcwd(), carpeta, nombre_archivo)

    with open(ruta, "w") as archivo:
        
        for mensaje in mensajesCodificados:
            archivo.write(f"{mensaje}\n")


crear_archivos_pruebas()

#Utilizar con :
# python3  generador_es_1.py  <direccion archivo palabras>  <direccion directorio de mensajes>