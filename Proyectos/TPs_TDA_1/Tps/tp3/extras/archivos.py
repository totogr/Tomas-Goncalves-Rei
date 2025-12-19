import os
import sys
from .recursos.grafo import Grafo

# Mensajes de error
MSJ_FILE_NOT_FOUND = "ERROR: El archivo no existe"
MSJ_NOT_A_DIR = "ERROR: La carpeta especificada no existe"
ARGUMENTOS_INSUFICIENTES = "Se necesita un argumento {indice} en entrada estandar"
NOT_NUMBER = "ERROR: El argumento debe ser un numero"

# Constantes de los sets de prueba
SEPARADOR_VERTICE = "," # Cadena que separa los vertices en cada linea del archivo

# Encabezado y formato para archivos de medición
HEADER_RESULTADOS = "Tamanio de entrada -- Tiempo de ejecucion promedio\n\n"
SALIDA_MEDICION = "        {valor}              {tiempo} seg \n"

# Tipos de entradas
TIPO_CARPETA = 0
TIPO_ARCHIVO = 1
TIPO_NUMERO = 2

def crear_archivo_mediciones(resultados: list[tuple[int, float]], ruta: str):
    """
    Recibe una lista de tuplas (tamaño, tiempo) y escribe los resultados en la ruta especificada.
    """
    with open(ruta, "w") as archivo:
        archivo.write(HEADER_RESULTADOS)
        for tamanio, tiempo in resultados:
            archivo.write(SALIDA_MEDICION.format(valor=tamanio, tiempo=tiempo))

def crear_grafos_desde_carpeta(ruta_carpeta:str) -> dict[str, Grafo]:
    """
    Recibe una carpeta con archivos correspondientes a grafos de prueba
    especificados en el formato indicado por la catedra

    Devuelve una diccionario del formato {nombre_archivo:str: grafo_correspondiente:Grafo, ...}
    """
    comprobar_carpeta(os.path.dirname(ruta_carpeta))
    grafos = {}

    for nombre in os.listdir(ruta_carpeta):
        if nombre == "Resultados_Esperados.txt":
            continue
        ruta_archivo = os.path.join(os.getcwd(), ruta_carpeta, nombre)
        grafo_archivo = crear_grafo_desde_archivo(ruta_archivo)
        grafos[nombre] = grafo_archivo
    
    return grafos

def crear_grafo_desde_archivo(ruta_archivo:str) -> Grafo:
    """
    Recibe la ruta de un archivo existente con el formato indicado por la catedra
    
    Devuelve un grafo creado a partir de los vertices y aristas descriptos 
    en el archivo
    """
    comprobar_archivo(ruta_archivo)
    
    grafo = Grafo(es_dirigido=False)
    archivo = open(ruta_archivo, "r")
    archivo.readline() #Ignoro la primera linea
    
    for linea in archivo:
        linea = linea.strip()
        origen, dest = linea.split(SEPARADOR_VERTICE)
        
        if origen not in grafo:
            grafo.agregar_vertice(origen)
        if dest not in grafo:
            grafo.agregar_vertice(dest)

        grafo.agregar_arista(origen, dest)

    archivo.close()
    return grafo

def comprobar_carpeta(ruta_carpeta):
    """
    Comprueba que la ruta recibida por parametro corresponde a una carpeta
	Si la carpeta no existe lanza un error NotADirectoryError
    """
    if not os.path.exists(ruta_carpeta):
        raise NotADirectoryError(MSJ_NOT_A_DIR)
    
    return

def comprobar_archivo(ruta_archivo:str):
    """
	Comprueba que la ruta recibida por parametro corresponde a un archivo
	Si el archivo no existe lanza un error FileNotFoundError
	"""
    if not os.path.exists(ruta_archivo):
        raise FileNotFoundError(MSJ_FILE_NOT_FOUND)
    
    return

def leer_entrada_usuario(mensaje, mensaje_error,entradas_posibles):
    """
    Lee de entrada estandar mostrando antes el mensaje recibido por parametro,
    hasta que el usuario ingrese uno de los valores especificados en entradas_posibles.
    Si el mensaje es invalido, se le muestra al usuario el mensaje especificado en mensaje_error
    """
    entrada = input(mensaje)
    while entrada not in entradas_posibles:
        print(mensaje_error)
        entrada = input(mensaje)
    
    return entrada

def leer_argumento(indice, tipo):
    """
    Lee el argumento <indice> de la entrada estandar.
	Si el argumento es del tipo especificado, devuelve su valor. De lo contrario, lanza un error
    """

    if indice >= len(sys.argv):
        raise Exception(ARGUMENTOS_INSUFICIENTES.format(indice=indice))
    
    entrada = sys.argv[indice]

    if tipo == TIPO_ARCHIVO:
        comprobar_archivo(entrada)
    elif tipo == TIPO_CARPETA:
        comprobar_carpeta(entrada)
    elif tipo == TIPO_NUMERO:
        if not entrada.isdigit():
            raise Exception(NOT_NUMBER)
        return int(entrada)
    
    return entrada
        