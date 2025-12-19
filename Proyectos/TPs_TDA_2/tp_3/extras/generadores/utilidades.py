import os
import sys

# Cada tupla = (n_maestros, k_grupos, rango_habilidad)
SETS_PRUEBAS = [
	(6, 3, (50, 500)),
	(10, 4, (100, 1000)),
	(12, 5, (200, 1200)),
	(15, 5, (300, 1500)),
	(20, 6, (400, 2000)),
]

FILE_NOT_FOUND = "ERROR: El archivo no existe"
ARGUMENTOS_INSUFICIENTES = "Se necesita un argumento {indice} en entrada estandar"
NOT_A_DIRECTORY = "La carpeta especificada no existe"

def leer_directorio(indice):
	"""
	Lee el argumento <indice> de la entrada estandar.
	Si la carpeta no existe, la crea.
	"""
	if indice >= len(sys.argv):
		raise Exception(ARGUMENTOS_INSUFICIENTES.format(indice=indice))
	
	carpeta = sys.argv[indice]
	if not os.path.exists(carpeta):
		os.makedirs(carpeta)
	
	return carpeta

def guardar_archivo_tribu(ruta, k, maestros):
	"""
	Guarda una instancia del problema de la Tribu del Agua.
	Formato compatible con parseo.py
	"""
	with open(ruta, "w", encoding="utf-8") as archivo:
		archivo.write("# La primera linea indica la cantidad de grupos a formar, las siguientes son de la forma 'nombre maestro, habilidad'\n")
		archivo.write(f"{k}\n")
		for nombre, habilidad in maestros:
			archivo.write(f"{nombre}, {habilidad}\n")