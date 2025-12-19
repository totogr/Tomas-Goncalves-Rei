import os
import sys

# Constantes para el parseo de archivos de prueba
ARCHIVO_RESULTADOS = ["Resultados Esperados.txt"]
SOLO_RATAS = [ARCHIVO_RESULTADOS[0], "no-es"]
SEPARADOR_TI_EI = ","

# Errores
FILE_NOT_FOUND = "ERROR: El archivo no existe"
ARGUMENTOS_INSUFICIENTES = "Se necesita un argumento {indice} en entrada estandar"
NOT_A_DIRECTORY = "La carpeta especificada no existe"

# Constantes para resultados de mediciones 
HEADER_RESULTADOS = "Tamaño de entrada -- Tiempo de ejecucion promedio\n\n"
SALIDA_MEDICION = "        {valor}              {tiempo} seg \n"

def parseo(ruta, solo_ratas=False, dicc=True):	
	"""
	Parsea los archivos de prueba de la ruta especificada y excluye ARCHIVOS_RESULTADOS del parseo.
	
	Recibe:
	- La ruta de la cual se deben leer las pruebas
	- Un booleano solo_ratas:
		-  solo_ratas=True --> Solo se parsean las pruebas en las que el sospechoso es la rata
		-  solo_ratas=False --> Se parsean todos los archivos
	- Un booleano dicc:
		-  dicc=True --> Devuelve dicc del formato tamaño: (intervalos, timestamps, nombres)
		-  dicc=False --> Devuelve lista de lista de intervalos, lista de listas de timestamps, y lista de nombres de archivos.
	"""
	filtros = ARCHIVO_RESULTADOS
	if solo_ratas:
		filtros = SOLO_RATAS

	nombres_archivos = []
	intervalos_totales = []
	timestamps_totales = []
	
	for nombre_archivo in os.listdir(ruta):
		# Saltearse el archivo si esta dentro de los no deseados
		paso_filtros = comprobar_filtros(nombre_archivo, filtros)
		if not paso_filtros:
			continue
			
		nombres_archivos.append(nombre_archivo)
		ruta_archivo = os.path.join(ruta, nombre_archivo)

		intervalos, timestamps = parsear_archivo(ruta_archivo)
		intervalos_totales.append(intervalos)
		timestamps_totales.append(timestamps)
	
	if not dicc:
		return intervalos_totales, timestamps_totales, nombres_archivos
	
	dicc_pruebas = crear_dicc_pruebas(intervalos_totales, timestamps_totales, nombres_archivos)
	return dicc_pruebas


def parsear_archivo(ruta_archivo):
	"""
	Parsea un archivo de prueba
	Recibe la ruta del archivo de pruebas proporcionado por la catedra
	Devuelve dos listas:
		- La primera tiene los intervalos posibles del sospechoso
		- La segunda tiene los timestamps confirmados de la posible rata
	"""
	intervalos = []
	timestamps = []
	
	with open(ruta_archivo, "r") as archivo:
			linea = archivo.readline() #Evitar primera linea
			
			# Obtener cantidad de entradas (n)
			linea = archivo.readline()
			linea = linea.strip()
			n = int(linea)
			
			# Obtener intervalos posibles
			for i in range(n):
				linea = archivo.readline()
				linea = linea.strip()
			
				# Separo la entrada ti,ei
				tiempo, margen_error = linea.split(SEPARADOR_TI_EI)
				tiempo, margen_error = int(tiempo), int(margen_error)
				intervalos.append((tiempo-margen_error,tiempo+margen_error))
			
			# Obtener timestamps del sospechoso
			for i in range(n):
				linea = archivo.readline()
				linea = linea.strip()
				timestamps.append(int(linea))

	return intervalos, timestamps

def leer_nombre():
	"""
	Lee de entrada estandar el nombre del archivo a probar
	Si el archivo no existe lanza un error FileNotFoundError
	"""
	nombre = sys.argv[1]
	if not os.path.exists(nombre):
		raise FileNotFoundError(FILE_NOT_FOUND)

	return nombre

def comprobar_filtros(cadena, filtros):
	"Comprueba si la cadena pasa con todos los filtros o no"
	for filtro in filtros:
		if filtro in cadena:
			return False
		
	return True

def crear_dicc_pruebas(intervalos, timestamps, nombres):
	"""
	Recibe una lista de intervalos y uno de timestamps
	Devuelve un dicc del formato tamaño:(intervalos, timestamps, nombre)
	"""
	dicc_pruebas = {}

	for i in range(len(intervalos)):
		tam_intervalo = len(intervalos[i])
		dicc_pruebas[tam_intervalo] = (
			intervalos[i],
			timestamps[i],
			nombres[i]
		)
	
	return dicc_pruebas

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

def crear_archivo_resultados(resultados, ruta):
    """
	Recibe los resultados de una prueba y los escribe en la ruta especificada.
	"""
    with open(ruta, "w") as archivo:
        archivo.write(HEADER_RESULTADOS)
        for valor, tiempo in resultados:
            archivo.write(SALIDA_MEDICION.format(valor=valor, tiempo=tiempo))