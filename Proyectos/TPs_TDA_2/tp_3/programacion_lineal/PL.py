import pulp
import sys
import os

sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))
from archivos.parseo import parsear_archivo_tribu

def resolver_programacion_lineal(k, maestros):
	"""
	Modelo de Programacion Lineal para la Tribu del Agua.
	Minimiza la diferencia entre la suma maxima y minima de habilidades entre grupos.
	"""

	n = len(maestros)
	habilidades = [h for _, h in maestros]

	# Crear modelo
	modelo = pulp.LpProblem("Tribu_del_Agua_PL", pulp.LpMinimize)

	# Variables binarias x[i][j] = 1 si el maestro i va al grupo j
	x = [[pulp.LpVariable(f"x_{i}_{j}", cat="Binary") for j in range(k)] for i in range(n)]

	# Variables auxiliares
	S = [pulp.LpVariable(f"S_{j}", lowBound=0) for j in range(k)] # Suma de habilidades del grupo j
	M = pulp.LpVariable("M", lowBound=0)						  # Suma maxima de un grupo
	m = pulp.LpVariable("m", lowBound=0)						  # Suma minima de un grupo

	# Restricciones: cada maestro en un solo grupo
	for i in range(n):
		modelo += pulp.lpSum(x[i][j] for j in range(k)) == 1, f"Asignacion_maestro_{i}"

	# Restricciones: suma de cada grupo
	for j in range(k):
		modelo += S[j] == pulp.lpSum(habilidades[i] * x[i][j] for i in range(n)), f"Suma_grupo_{j}"

	# Relacionar sumas con M y m
	for j in range(k):
		modelo += S[j] <= M, f"Cota_superior_{j}"
		modelo += S[j] >= m, f"Cota_inferior_{j}"

	# Objetivo: minimizar la diferencia M - m
	modelo += M - m

	# Resolver
	modelo.solve(pulp.PULP_CBC_CMD(msg=False, timeLimit=300))

	# Extraer resultados
	grupos = [[] for _ in range(k)]
	for i in range(n):
		for j in range(k):
			if pulp.value(x[i][j]) == 1:
				grupos[j].append(maestros[i][0])

	valor_optimo = pulp.value(M - m)
	return valor_optimo, grupos

def main():
	if len(sys.argv) < 2:
		print("Uso: python3 programacion_lineal.py <archivo_entrada>")
		sys.exit(1)

	ruta_archivo = sys.argv[1]
	k, maestros = parsear_archivo_tribu(ruta_archivo)

	valor, grupos = resolver_programacion_lineal(k, maestros)

	for i, grupo in enumerate(grupos, start=1):
		suma = sum(next(h for n, h in maestros if n == nombre) for nombre in grupo)
		print(f"Grupo {i}: {', '.join(grupo)}")
	print(f"\nValor optimo (M - m) = {valor}\n")

if __name__ == "__main__":
	main()