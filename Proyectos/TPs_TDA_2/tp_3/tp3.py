#!/usr/bin/env python3
import sys
import os

sys.path.append(os.path.abspath(os.path.dirname(__file__)))

from archivos.parseo import parsear_archivo_tribu
from backtracking.backtracking import tribu_del_agua_backtracking
from programacion_lineal.PL import resolver_programacion_lineal
from aproximacion.aproximacion import aprox

def ejecutar_aproximacion(ruta_archivo, k, maestros):
	print(f"\nEjecutando algoritmo de Aproximacion para {ruta_archivo}")
	print(f"K = {k}, Maestros = {len(maestros)}\n")

	valor_aprox, grupos = aprox(k, maestros)

	for i, grupo in enumerate(grupos, start=1):
		print(f"Grupo {i}: {', '.join(grupo)}")

	print(f"\nCoeficiente = {valor_aprox}\n")

def ejecutar_backtracking(ruta_archivo, k, maestros):
	print(f"\nEjecutando algoritmo de Backtracking para {ruta_archivo}")
	print(f"K = {k}, Maestros = {len(maestros)}\n")

	mejor_valor, grupos = tribu_del_agua_backtracking(k, maestros)

	for i, grupo in enumerate(grupos, start=1):
		suma = sum(next(h for n, h in maestros if n == nombre) for nombre in grupo)
		print(f"Grupo {i}: {', '.join(grupo)}")

	print(f"\nCoeficiente = {mejor_valor}\n")

def ejecutar_programacion_lineal(ruta_archivo, k, maestros):
	print(f"\nEjecutando algoritmo de Programacion Lineal para {ruta_archivo}")
	print(f"K = {k}, Maestros = {len(maestros)}\n")

	valor_optimo, grupos = resolver_programacion_lineal(k, maestros)

	for i, grupo in enumerate(grupos, start=1):
		suma = sum(next(h for n, h in maestros if n == nombre) for nombre in grupo)
		print(f"Grupo {i}: {', '.join(grupo)}")

	print(f"\nValor optimo (PL) = {valor_optimo}\n")

def main():
	if len(sys.argv) < 4:
		print("Uso: ./tp3 ruta/a/archivo.txt <K> <algoritmo>")
		print("Algoritmos disponibles: backtracking | programacion_lineal")
		sys.exit(1)

	ruta_archivo = sys.argv[1]
	k_param = sys.argv[2]
	algoritmo = sys.argv[3].lower()

	# Validar K
	try:
		k = int(k_param)
	except ValueError:
		print("Error: <K> debe ser un numero entero.")
		sys.exit(1)

	# Validar archivo
	if not os.path.exists(ruta_archivo):
		print(f"Error: no se encontro el archivo {ruta_archivo}")
		sys.exit(1)

	# Leer archivo de entrada
	k_archivo, maestros = parsear_archivo_tribu(ruta_archivo)

	if k == 0:
		k = k_archivo

	# Seleccionar algoritmo
	if algoritmo == "backtracking":
		ejecutar_backtracking(ruta_archivo, k, maestros)
	elif algoritmo == "programacion_lineal":
		ejecutar_programacion_lineal(ruta_archivo, k, maestros)
	elif algoritmo == "aproximacion":
		ejecutar_aproximacion(ruta_archivo, k, maestros)
	else:
		print(f"Algoritmo '{algoritmo}' no reconocido.")
		print("Opciones validas: backtracking | programacion_lineal")
		sys.exit(1)

if __name__ == "__main__":
	main()