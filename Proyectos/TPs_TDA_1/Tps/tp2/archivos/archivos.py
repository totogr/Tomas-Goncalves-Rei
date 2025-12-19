import os
import sys

# Errores
FILE_NOT_FOUND = "ERROR: El archivo no existe"
ARGUMENTOS_INSUFICIENTES = "Se necesita un argumento {indice} en entrada estandar"
NOT_A_DIRECTORY = "La carpeta especificada no existe"

#Tipos de directorios
TIPO_CARPETA = 0
TIPO_ARCHIVO = 1

HEADER_RESULTADOS = "Tamanio de entrada -- Tiempo de ejecucion promedio\n\n"
SALIDA_MEDICION = "        {valor}              {tiempo} seg \n"
HEADER_RESULTADO_PRUEBA = "Palabras: {archivo_palabras}, entrada: {archivo_textos}\n"
	

def parsear_carpeta_palabras(ruta_carpeta_palabras):
	"""
	Recibe la ruta a una carpeta con palabras
	Devuelve un diccionario del formato {nombre_archivo: [palabras_archivo]}
	"""
	comprobar_carpeta(ruta_carpeta_palabras)
	
	archivos = {}
	for nombre_archivo in os.listdir(ruta_carpeta_palabras):
		ruta_archivo = os.path.join(ruta_carpeta_palabras, nombre_archivo)
		archivos[nombre_archivo] = parsear_palabras(ruta_archivo)
	
	return archivos

def parsear_carpeta_textos(ruta_carpeta_textos):
	"""
	Recibe la ruta a una carpeta de textos
	Devuelve un diccionario del formato {nombre_archivo: [textos_archivo]}
	"""
	comprobar_carpeta(ruta_carpeta_textos)
	
	archivos = {}
	for nombre_archivo in os.listdir(ruta_carpeta_textos):
		ruta_archivo = os.path.join(ruta_carpeta_textos, nombre_archivo)
		archivos[nombre_archivo] = parsear_textos(ruta_archivo)
	
	return archivos

def parsear_palabras(ruta_palabras):
	"""
	Recibe la ruta de un archivo de palabras
	Devuelve una lista con las palabras que contiene cada linea del archivo
	"""
	return parsear_archivo(ruta_palabras)

def parsear_textos(ruta_textos):
	"""
	Recibe la ruta de un archivo de textos
	Devuelve una lista con los textos que contiene cada linea del archivo 
	"""
	return parsear_archivo(ruta_textos)


def parsear_archivo(ruta_textos):
	"""
	Recibe una ruta de un archivo
	Devuelve una lista que contiene cada linea del archivo
	"""
	comprobar_archivo(ruta_textos)

	lineas = []
	with open(ruta_textos) as archivo:
		for linea in archivo:
			linea = linea.strip()
			lineas.append(linea)
	
	return lineas

def comprobar_filtros(cadena, filtros):
	"Comprueba si la cadena pasa con todos los filtros o no"
	for filtro in filtros:
		if filtro in cadena:
			return False
		
	return True

def leer_argumento(indice, tipo):
	"""
	Lee el argumento <indice> de la entrada estandar.
	Si el argumento es del tipo especificado, devuelve su valor. De lo contrario, lanza un error
	
	"""
	if indice >= len(sys.argv):
		raise Exception(ARGUMENTOS_INSUFICIENTES.format(indice=indice))
	
	ruta = sys.argv[indice]
	
	if tipo == TIPO_ARCHIVO:
		comprobar_archivo(ruta)
	elif tipo == TIPO_CARPETA:
		comprobar_carpeta(ruta)
	
	return ruta

def comprobar_archivo(ruta):
	"""
	Lee de entrada estandar el nombre del archivo a probar
	Si el archivo no existe lanza un error FileNotFoundError
	"""
	if not os.path.exists(ruta):
		raise FileNotFoundError(FILE_NOT_FOUND)

	return

def comprobar_carpeta(ruta):
	if not os.path.exists(ruta):
		raise NotADirectoryError(NOT_A_DIRECTORY)
	
	return

def crear_archivo_mediciones(resultados, ruta):
	"""
	Recibe los resultados de una prueba de tiempos y los escribe en la ruta especificada.
	"""
	with open(ruta, "w") as archivo:
		archivo.write(HEADER_RESULTADOS)

		for tamanio,tiempo in resultados:
			archivo.write(SALIDA_MEDICION.format(valor=tamanio, tiempo=tiempo))

def crear_resultados_pruebas(ruta_carpeta_palabras, ruta_carpeta_textos, ruta_salida, algoritmo):
	"""
	Recibe una ruta con archivos de palabras, una ruta con archivos de textos a descifrar,
	una ruta de salida de los resultados y un algoritmo a ejecutar.

	Ejecuta el algoritmo para cada archivo de palabras y de textos con el algoritmo recibido
	y escribe cada resultado en la ruta de salida especificada.
	"""
	comprobar_carpeta(os.path.dirname(ruta_salida))

    # Comprobar que las rutas de los datos de entrada son validas
	comprobar_carpeta(ruta_carpeta_palabras)
	comprobar_carpeta(ruta_carpeta_textos)
	
	archivo = open(ruta_salida, "w")

	for archivo_palabras in os.listdir(ruta_carpeta_palabras):
		for archivo_posibles_textos in os.listdir(ruta_carpeta_textos):
			ruta_palabras = os.path.join(os.getcwd(), ruta_carpeta_palabras, archivo_palabras)
			ruta_textos = os.path.join(os.getcwd(), ruta_carpeta_textos, archivo_posibles_textos)
			palabras = parsear_palabras(ruta_palabras)
			textos = parsear_textos(ruta_textos)

			archivo.write(HEADER_RESULTADO_PRUEBA.format(archivo_palabras=archivo_palabras, archivo_textos=archivo_posibles_textos))
			for texto in textos:
				resultado = algoritmo(palabras, texto)
				archivo.write(obtener_resultados(resultado))
				archivo.write("\n")
			archivo.write("\n")
		archivo.write("\n")

	archivo.close()	

def obtener_resultados(resultado):
    es_mensaje, mensaje = resultado
    if es_mensaje:
        return f"{mensaje}"
    else:
        return "No es un mensaje"
	