import os
import sys

# Constantes para los generadores
PRIMERA_LINEA = "Primero viene la cantidad (n) de timestamps para ambos, luego n líneas que son un timestamp aproximado cada uno separado por una coma (',') del error, y luego n lineas de las transacciones del sospechoso"
TAMANIOS_PRUEBAS = [5, 505, 1005, 1505, 2005, 2505, 3005, 3505, 4005, 4505, 5005, 5505, 6005, 6505, 7005, 7505, 8005, 8505, 9005, 9505, 10005, 10505, 11005, 11505, 12005, 12505, 13005, 13505, 14005, 14505, 15005, 15505, 16005, 16505, 17005, 17505, 18005, 18505, 19005, 19505]
NOMBRE_ARCHIVO_NO_ES = "{n}-no-es.txt"
NOMBRE_ARCHIVO_ES = "{n}-es.txt"
FORMATO_INTERVALO = "{ti},{error}\n"
FORMATO_TIMESTAMP = "{timestamp}\n"

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