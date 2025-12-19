import os
import sys
from archivos import archivos
from desencriptacion import cadena_es_mensaje

def generar_resultados_pruebas_nuestras():
    """
    Lee de los argumentos 2 y 3 de entrada estandar las rutas de las carpetas de
    palabras y textos. Lee del argumento 4 la ruta de salida

    Genera las pruebas por cada archivo de palabras para todos los textos de la carpeta especificada
    y los escribe en la ruta de salida
    """
    # Leer datos de entrada de los argumentos
    ruta_carpeta_palabras = archivos.leer_argumento(2, archivos.TIPO_CARPETA)
    ruta_carpeta_textos = archivos.leer_argumento(3, archivos.TIPO_CARPETA)
    
    # Leer y comprobar archivo de salida
    ruta_salida = sys.argv[4]

    archivos.crear_resultados_pruebas(ruta_carpeta_palabras, ruta_carpeta_textos, ruta_salida, cadena_es_mensaje)


def comprobar_cadenas_stdin():
    """
    Lee una linea desde stdin y ejecuta cadena_es_mensaje
    con esa linea hasta recibir un EOF
    """
    ruta_palabras = archivos.leer_argumento(1, archivos.TIPO_ARCHIVO)
    palabras = archivos.parsear_palabras(ruta_palabras)

    termino = False
    texto = input()
    while not termino:
        resultado = cadena_es_mensaje(palabras, texto)
        print(obtener_resultados(resultado))
        
        try:
            texto = input()
        except EOFError:
            termino = True

def obtener_resultados(resultado):
    es_mensaje, mensaje = resultado
    if es_mensaje:
        return f"{mensaje}"
    else:
        return "No es un mensaje"   
    
def pruebas():
    """
    Prueba el algoritmo con un set de pruebas.
    
    - Ejecución común: Lee del argumento 1 la ruta de las palabras, y de stdin las lineas a descifrar
    - Argumento -l: Genera resultados a partir de pruebas a partir de la carpeta de palabras y la carpeta
    de textos enviadas en los arg1 y arg2, y los escribe en el archivo arg3
    """
    if len(sys.argv) < 3:
        raise Exception("Argumentos insuficientes")

    argumento = sys.argv[1]

    if argumento == "-l":
        generar_resultados_pruebas_nuestras()
    else:
        comprobar_cadenas_stdin()

if __name__ == "__main__":
    pruebas()

# Uso: 
# Ejecutar una prueba: python3 pruebas.py ruta_archivo_palabras < posibles_textos 
# Ejecutar multiples pruebas: python3 pruebas.py -l carpeta_palabras carpeta_textos ruta_salida