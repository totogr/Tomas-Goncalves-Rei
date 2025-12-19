
import random
import time
from matplotlib import pyplot as plt
import numpy as np
import generador_1

import sys
import os

# agrega la raíz del proyecto al sys.path
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '../..')))

import tp2



random.seed(12345)

def generar_mediciones(n, iteraciones, rango_x, rango_f):
    tiempos = []
    for _ in range(iteraciones):

        x, f = generador_1.generar_datos_prueba(n, rango_x, rango_f)
        inicio = time.perf_counter()
        tp2.algoritmo(n, x, f)
        fin = time.perf_counter()
        tiempos.append(fin - inicio)
    return sum(tiempos) / iteraciones

def generar_mediciones_especial(n, iteraciones, max_x, max_f):
    tiempos = []
    for _ in range(iteraciones):

        x = [None] + [max_x] * n
        f = [None] + [max_f] * n
        inicio = time.perf_counter()
        tp2.algoritmo(n, x, f)
        fin = time.perf_counter()
        tiempos.append(fin - inicio)
    return sum(tiempos) / iteraciones

def generar_mediciones_algoritmo_especial(n, iteraciones, max_x, max_f):
    tiempos = []
    for _ in range(iteraciones):

        x = [None] + [max_x] * n
        f = [None] + [max_f] * n
        inicio = time.perf_counter()
        algoritmo_especial(n, x, f)
        fin = time.perf_counter()
        tiempos.append(fin - inicio)
    return sum(tiempos) / iteraciones

def obtener_ajuste_cuadratico(x, y):
    x = np.array(x)
    y = np.array(y)
    a, b, c = np.polyfit(x, y, 2)
    ajuste_cuadratico = a * x**2 + b * x + c
    errores_cuadraticos = y - ajuste_cuadratico

    return ajuste_cuadratico, errores_cuadraticos

def obtener_ajuste_lineal(x, y):
    x = np.array(x)
    y = np.array(y)
    a, b = np.polyfit(x, y, 1)
    ajuste_lineal = a * x + b
    errores_lineales = y - ajuste_lineal

    return ajuste_lineal, errores_lineales


def graficar_mediciones(tamaños,tiempos, ajuste_lineal, ajuste_cuadratico):
    plt.figure(figsize=(20, 12))
    plt.plot(tamaños, tiempos, '-o', label='Mediciones Obtenidas', color='black')
    plt.plot(tamaños, ajuste_lineal, '--', label='Ajuste Lineal', color='red')
    plt.plot(tamaños, ajuste_cuadratico, '--', label='Ajuste Cuadratico', color='blue')
    plt.legend()
    plt.xlabel("Tamaño de muestra (n)")
    plt.ylabel("Tiempo (s)")
    plt.title("Tiempo de ejecucion segun tamaño de muestra")
    plt.show()

def graficar_errores(tamaños, errores_lineales, error_cuadratico):
    plt.figure(figsize=(20, 12))
    plt.plot(tamaños, errores_lineales, '-o', label='Errores Lineales', color='red')
    plt.plot(tamaños, error_cuadratico, '-o', label='Errores Cuadraticos', color='blue')
    plt.axhline(0, color='red', linestyle='--')
    plt.legend()
    plt.xlabel("Tamaño de muestra (n)")
    plt.ylabel("Error")
    plt.title("Errores de Ajuste Lineal y Cuadratico")
    plt.show()

def graficar_comparativa_tiempos(tamaños, tiempos_estandar, tiempos_x_grande, tiempos_f_grande):
    plt.figure(figsize=(20, 12))
    plt.plot(tamaños, tiempos_estandar, '-o', label='Tiempos Estándar', color='black')
    plt.plot(tamaños, tiempos_x_grande, '-o', label='Tiempos X Grande', color='blue')
    plt.plot(tamaños, tiempos_f_grande, '-o', label='Tiempos F Grande', color='green')
    plt.legend()
    plt.xlabel("Tamaño de muestra (n)")
    plt.ylabel("Tiempo (s)")
    plt.title("Comparativa de Tiempos de Ejecución")
    plt.show()

def graficar_tiempos_caso_especial(tamaños, tiempos, tiempo_especial):
    plt.figure(figsize=(20, 12))
    plt.plot(tamaños, tiempos, '-o', label='Tiempos Estándar', color='black')
    plt.plot(tamaños, tiempo_especial, '-o', label='Tiempos Caso Especial', color='purple')
    plt.legend()
    plt.xlabel("Tamaño de muestra (n)")
    plt.ylabel("Tiempo (s)")
    plt.title("Tiempos de Ejecución Caso Especial vs Estandar")
    plt.show()

def graficar_caso_especial_aislado(tamaños, tiempo_especial_algoritmo):
    plt.figure(figsize=(20, 12))
    plt.plot(tamaños, tiempo_especial_algoritmo, '-o', label='Tiempos Caso Especial Algoritmo Especial', color='orange')
    plt.legend()
    plt.xlabel("Tamaño de muestra (n)")
    plt.ylabel("Tiempo (s)")
    plt.title("Tiempos de Ejecución Caso Especial Algoritmo Especial")
    plt.show()

def tiempos_estandar(tamaños, iteraciones):
    tiempos = []
    for n in tamaños:
        max_x, max_f = n, n
        tiempo_promedio = generar_mediciones(n, iteraciones, (1, max_x), (1, max_f))
        tiempos.append(tiempo_promedio)
    return tiempos

def tiempos_x_grande(tamaños, iteraciones):
    tiempos = []
    for n in tamaños:
        max_x = n * 2
        max_f = n
        tiempo_promedio = generar_mediciones(n, iteraciones, (n, max_x), (1, max_f))
        tiempos.append(tiempo_promedio)
    return tiempos

def tiempos_f_grande(tamaños, iteraciones):
    tiempos = []
    for n in tamaños:
        max_x = n
        max_f = n * 2
        tiempo_promedio = generar_mediciones(n, iteraciones, (1,max_x), (n,max_f))
        tiempos.append(tiempo_promedio)
    return tiempos

def caso_especial(tamaños, iteraciones):
    tiempos = []
    for n in tamaños:
        max_x = 1
        max_f = n * 10
        tiempo_promedio = generar_mediciones_especial(n, iteraciones, max_x, max_f)
        tiempos.append(tiempo_promedio)
    return tiempos

def caso_especial_algoritmo_especial(tamaños, iteraciones):
    tiempos = []
    for n in tamaños:
        max_x = 1
        max_f = n * 10
        tiempo_promedio = generar_mediciones_algoritmo_especial(n, iteraciones, max_x, max_f)
        tiempos.append(tiempo_promedio)
    return tiempos


def main():
    tamaños = [100, 200, 300, 400, 500, 600, 700, 800, 900, 1000]
    iteraciones = 8
    tiempos = tiempos_estandar(tamaños, iteraciones)
    print("estandar terminado")
    ajuste_lineal, error_lineal = obtener_ajuste_lineal(tamaños, tiempos)
    ajuste_cuadratico, error_cuadratico = obtener_ajuste_cuadratico(tamaños, tiempos)
    tiempos_x_g = tiempos_x_grande(tamaños, iteraciones)
    print("x grande terminado")
    tiempos_f_g = tiempos_f_grande(tamaños, iteraciones)
    print("f grande terminado")
    tiempos_caso_especial = caso_especial(tamaños, iteraciones)
    print("caso especial terminado")
    tiempos_caso_especial_algoritmo = caso_especial_algoritmo_especial(tamaños, iteraciones)
    print("caso especial algoritmo especial terminado")



    graficar_mediciones(tamaños, tiempos, ajuste_lineal, ajuste_cuadratico)
    graficar_errores(tamaños, error_lineal, error_cuadratico)
    graficar_comparativa_tiempos(tamaños, tiempos, tiempos_x_g, tiempos_f_g)
    graficar_tiempos_caso_especial(tamaños, tiempos, tiempos_caso_especial)
    graficar_tiempos_caso_especial(tamaños, tiempos, tiempos_caso_especial_algoritmo)
    graficar_caso_especial_aislado(tamaños, tiempos_caso_especial_algoritmo)





def algoritmo_especial(n, x, f):
    """
    Caso especial donde sabemos que f(1) >= X_i para todo i.
    """
    eliminados = [0] * (n + 1)
    for i in range(1, n + 1):
        eliminados_sin_atacar = eliminados[i-1]
        eliminados_atacando = x[i] + eliminados[i-1]
        eliminados[i] = max(eliminados_sin_atacar, eliminados_atacando)
    
    return eliminados





if __name__ == "__main__":
    main()