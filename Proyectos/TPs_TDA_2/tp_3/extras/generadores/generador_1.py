import os
import random
from .utilidades import SETS_PRUEBAS, leer_directorio, guardar_archivo_tribu

def generar_maestros(n, rango_habilidades):
	"""
	Genera una lista de maestros con nombres ficticios y habilidades aleatorias.
	"""
	nombres_base = [
		"Hama", "Sura", "Eska", "Desna", "Unalaq", "Katara", "Yue", "Pakku",
		"Kya", "Kuruk", "Ming-Hua", "Senna", "La", "Tui", "Amon", "Sangok",
		"Huu", "Rafa", "Tho", "Misu"
	]
	maestros = []
	for i in range(n):
		nombre = nombres_base[i % len(nombres_base)]
		if i >= len(nombres_base):
			nombre += f"_{i}"
		habilidad = random.randint(*rango_habilidades)
		maestros.append((nombre, habilidad))
	return maestros

def crear_sets_prueba():
	"""
	Genera varios archivos de prueba con formato (k, maestros).
	"""
	carpeta = leer_directorio(1)
	for n, k, rango in SETS_PRUEBAS:
		maestros = generar_maestros(n, rango)
		nombre_archivo = f"{n}_{k}.txt"
		ruta = os.path.join(carpeta, nombre_archivo)
		guardar_archivo_tribu(ruta, k, maestros)

if __name__ == "__main__":
	"""
	Ejemplo de uso:
		python generador_1.py grupos_y_maestros/pruebas_auto
	"""
	crear_sets_prueba()