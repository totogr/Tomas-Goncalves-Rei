
import random
import time
from matplotlib import pyplot as plt
import numpy as np
import generador_1

import sys
import os

# agrega la raíz del proyecto al sys.path
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '../..')))

import tp1



random.seed(12345)

def generar_mediciones(n, iteraciones, max_tiempo, max_peso):
    tiempos = []
    for _ in range(iteraciones):
        max_tiempo_t = (1, max_tiempo)
        max_peso_t = (1, max_peso)

        guerras = generador_1.generar_guerras(n, max_tiempo_t, max_peso_t)
        inicio = time.perf_counter()
        tp1.algoritmo(guerras)
        fin = time.perf_counter()
        tiempos.append(fin - inicio)
    return sum(tiempos) / iteraciones



def obtener_ajuste_lineal(x, y):
    x = np.array(x)
    y = np.array(y)
    a, b = np.polyfit(x, y, 1)
    ajuste_lineal = a * x + b
    errores_lineales = y - ajuste_lineal

    return ajuste_lineal, errores_lineales


def graficar_mediciones(tamaños,tiempos, ajuste_lineal, escala=False):
    plt.figure(figsize=(20, 12))
    plt.plot(tamaños, tiempos, '-o', label='Mediciones Obtenidas', color='black')
    plt.plot(tamaños, ajuste_lineal, '--', label='Ajuste Lineal', color='red')
    plt.legend()
    if not escala:
        plt.xlabel("Tamaño de muestra (n)")
    else:
        plt.xlabel("Tamaño de muestra escala (n log n)")
    plt.ylabel("Tiempo (s)")
    plt.title("Tiempo de ejecucion segun tamaño de muestra")
    plt.show()

def graficar_errores(tamaños, errores_lineales, error_escala):
    plt.figure(figsize=(20, 12))
    plt.plot(tamaños, errores_lineales, '-o', label='Errores Lineales', color='blue')
    plt.plot(tamaños, error_escala, '-o', label='Errores Escala n log n', color='green')
    plt.axhline(0, color='red', linestyle='--')
    plt.legend()
    plt.xlabel("Tamaño de muestra (n)")
    plt.ylabel("Error")
    plt.title("Errores del ajuste lineal")
    plt.show()

def graficar_comparativa_tiempos(tamaños, tiempos_estandar, tiempos_peso_grande, tiempos_tiempo_grande):
    plt.figure(figsize=(20, 12))
    plt.plot(tamaños, tiempos_estandar, '-o', label='Tiempos Estándar', color='black')
    plt.plot(tamaños, tiempos_peso_grande, '-o', label='Tiempos Peso Grande', color='blue')
    plt.plot(tamaños, tiempos_tiempo_grande, '-o', label='Tiempos Tiempo Grande', color='green')
    plt.legend()
    plt.xlabel("Tamaño de muestra (n)")
    plt.ylabel("Tiempo (s)")
    plt.title("Comparativa de Tiempos de Ejecución")
    plt.show()

def graficar_prueba_cociente(tamaños, prueba_cociente):
    plt.figure(figsize=(20, 12))
    plt.plot(tamaños, prueba_cociente, '-o', label='Prueba del Cociente', color='purple')
    plt.legend()
    plt.xlabel("Tamaño de muestra (n)")
    plt.ylabel("Cociente")
    plt.title("Prueba del Cociente")
    plt.show()

def tiempos_estandar(tamaños, iteraciones):
    tiempos = []
    max_tiempo = 50
    for n in tamaños:
        max_peso = n // 2
        tiempo_promedio = generar_mediciones(n, iteraciones, max_tiempo, max_peso)
        tiempos.append(tiempo_promedio)
    return tiempos

def tiempos_tiempo_grande(tamaños, iteraciones):
    tiempos = []
    for n in tamaños:
        max_tiempo = n
        max_peso = n // 2
        tiempo_promedio = generar_mediciones(n, iteraciones, max_tiempo, max_peso)
        tiempos.append(tiempo_promedio)
    return tiempos

def tiempos_peso_grande(tamaños, iteraciones):
    tiempos = []
    max_tiempo = 50
    for n in tamaños:
        max_peso = n * 2
        tiempo_promedio = generar_mediciones(n, iteraciones, max_tiempo, max_peso)
        tiempos.append(tiempo_promedio)
    return tiempos


def main():
    tamaños = [2500,5000,10000,25000, 50000,75000,100000, 125000, 150000, 175000, 200000, 225000, 250000]
    tamaños_escala_logaritmica = [t * np.log2(t) for t in tamaños]
    iteraciones = 20
    tiempos = tiempos_estandar(tamaños, iteraciones)
    ajuste, error = obtener_ajuste_lineal(tamaños, tiempos)
    ajuste_escala, error_escala = obtener_ajuste_lineal(tamaños_escala_logaritmica, tiempos)
    tamaños_chicos = [t//10 for t in tamaños]
    tiempos_chicos = tiempos_estandar(tamaños_chicos, iteraciones)
    prueba_cociente = [tiempos_chicos[i] / (tamaños_chicos[i] * np.log2(tamaños_chicos[i]) ) for i in range(len(tamaños_chicos))]



    tiempos_peso_g = tiempos_peso_grande(tamaños, iteraciones)
    tiempos_tiempo_g = tiempos_tiempo_grande(tamaños, iteraciones)
    graficar_mediciones(tamaños, tiempos, ajuste)
    graficar_mediciones(tamaños_escala_logaritmica, tiempos, ajuste_escala, escala=True)
    graficar_errores(tamaños, error, error_escala)
    graficar_comparativa_tiempos(tamaños, tiempos, tiempos_peso_g, tiempos_tiempo_g)
    graficar_prueba_cociente(tamaños_chicos, prueba_cociente)






if __name__ == "__main__":
    main()