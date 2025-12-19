import os
import sys

# Constantes para los generadores
SETS_PRUEBAS= [(5, 20), (25, 20), (45, 20), (65, 20), (85, 20),(100,20), (505, 15), (1005, 5), (2005, 5), (5005, 5), (10005, 5)]
NOMBRE_ARCHIVO_NO_ES = "{n}-no-es.txt"
NOMBRE_ARCHIVO_ES = "{n}-es.txt"
CANTIDAD_ERRORES_GRANDE = 15
CANTIDAD_ERRORES_PEQUEÑA = 10
CANTIDAD_ERRORES_MUY_PEQUEÑA = 2
CANTIDAD_REPETICIONES_MUY_BAJA=4
CANTIDAD_REPETICIONES_BAJA=8
CANTIDAD_REPETICIONES_ALTA=14

# Errores
FILE_NOT_FOUND = "ERROR: El archivo no existe"
ARGUMENTOS_INSUFICIENTES = "Se necesita un argumento {indice} en entrada estandar"
NOT_A_DIRECTORY = "La carpeta especificada no existe"


def leer_directorio(indice):
	"""
	Lee el argumento <indice> de la entrada estandar y comprueba que sea una carpeta.
	Si lo es, devuelve True.
	
	"""
	if indice >= len(sys.argv):
		raise Exception(ARGUMENTOS_INSUFICIENTES.format(indice=indice))
	
	carpeta = sys.argv[indice]
	if not os.path.exists(carpeta):
		raise NotADirectoryError(NOT_A_DIRECTORY)
	
	return carpeta


def parsear_archivo(ruta_textos):
	"""
	Documentar
	"""
	comprobar_archivo(ruta_textos)

	lineas = []
	with open(ruta_textos) as archivo:
		for linea in archivo:
			linea = linea.strip()
			lineas.append(linea)
	
	return lineas


def parsear_palabras(ruta_palabras):
	"""
	Documentar
	"""
	return parsear_archivo(ruta_palabras)


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