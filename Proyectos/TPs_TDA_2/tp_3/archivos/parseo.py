import os
import sys

# Errores
FILE_NOT_FOUND = "ERROR: El archivo no existe"
ARGUMENTOS_INSUFICIENTES = "Se necesita un argumento {indice} en entrada estandar"
NOT_A_DIRECTORY = "La carpeta especificada no existe"

# Archivo de resultados (para mediciones de tiempo)
HEADER_RESULTADOS = "Tamano de entrada -- Tiempo de ejecucion promedio\n\n"
SALIDA_MEDICION = "        {valor}              {tiempo} seg \n"


def parseo(ruta, dicc=True):
	"""
	Parsea todos los archivos de un directorio.

	Si dicc=True devuelve un diccionario:
		{ tamano: (instancia, nombre_archivo) }
		donde instancia = (k, lista_maestros)
	Si dicc=False devuelve:
		lista de instancias, lista de nombres de archivos
	"""

	if not os.path.exists(ruta):
		raise NotADirectoryError(f"Directorio no encontrado: {ruta}")

	nombres_archivos = []
	instancias_totales = []

	for nombre_archivo in os.listdir(ruta):
		if nombre_archivo == "Resultados Esperados.txt":
			continue
		ruta_archivo = os.path.join(ruta, nombre_archivo)
		if not os.path.isfile(ruta_archivo):
			continue
		try:
			instancia = parsear_archivo_tribu(ruta_archivo)
			nombres_archivos.append(nombre_archivo)
			instancias_totales.append(instancia)
		except Exception as e:
			print(f"Error al parsear {nombre_archivo}: {e}")
			continue

	if not dicc:
		return instancias_totales, nombres_archivos

	dicc_pruebas = {}
	for i in range(len(instancias_totales)):
		k, maestros = instancias_totales[i]
		dicc_pruebas[len(maestros)] = (instancias_totales[i], nombres_archivos[i])
	return dicc_pruebas


def parsear_archivo_tribu(ruta):
	"""
	Parsea un archivo con el formato de la Tribu del Agua.
	
	Formato esperado:
		# comentario (opcional)
		k
		nombre_maestro, habilidad
		nombre_maestro, habilidad
		...

	Devuelve una tupla (k, maestros)
	donde maestros = [(nombre, habilidad), ...]
	"""

	if not os.path.exists(ruta):
		raise FileNotFoundError(FILE_NOT_FOUND)

	with open(ruta, "r", encoding="utf-8") as f:
		lineas = [line.strip() for line in f if line.strip()]

	if lineas[0].startswith("#"):
		lineas = lineas[1:]

	# Primera línea = cantidad de grupos
	k = int(lineas[0])

	maestros = []
	for linea in lineas[1:]:
		if "," not in linea:
			continue
		partes = linea.split(",")
		nombre = partes[0].strip()
		habilidad = int(partes[1].strip())
		maestros.append((nombre, habilidad))

	return k, maestros


def leer_archivo():
	"""
	Lee de entrada estandar el nombre del archivo a probar
	"""
	if len(sys.argv) < 2:
		raise FileNotFoundError(FILE_NOT_FOUND)

	nombre = sys.argv[1]
	if not os.path.exists(nombre):
		raise FileNotFoundError(FILE_NOT_FOUND)

	return nombre


def leer_directorio(indice):
	"""
	Lee el argumento <indice> de la entrada estandar y comprueba que sea una carpeta.
	"""
	if indice >= len(sys.argv):
		raise Exception(ARGUMENTOS_INSUFICIENTES.format(indice=indice))

	carpeta = sys.argv[indice]
	if not os.path.exists(carpeta):
		raise NotADirectoryError(NOT_A_DIRECTORY)

	return carpeta


def crear_archivo_resultados(resultados, ruta):
	"""
	Recibe los resultados de una prueba y los escribe en la ruta especificada.
	"""
	with open(ruta, "w") as archivo:
		archivo.write(HEADER_RESULTADOS)
		for valor, tiempo in resultados:
			archivo.write(SALIDA_MEDICION.format(valor=valor, tiempo=tiempo))
