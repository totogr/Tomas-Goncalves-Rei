import os
import sys

sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))

from backtracking.backtracking import tribu_del_agua_backtracking
from programacion_lineal.PL import resolver_programacion_lineal
from aproximacion.aproximacion import aprox
from extras.generadores.generador_1 import generar_maestros
import time
import matplotlib.pyplot as plt
import numpy as np

def ejecutar_back(maestros, k, cant_iteraciones):
    if len(maestros) > 14:
        cant_iteraciones = 1  # reducir iteraciones para casos grandes
    tiempo = 0
    for _ in range(cant_iteraciones):
        inicio = time.time()
        tribu_del_agua_backtracking(k, maestros)
        fin = time.time()
        tiempo += fin - inicio
    return tiempo / cant_iteraciones

def ejecutar_pl(maestros, k, cant_iteraciones):
    tiempo = 0
    if len(maestros) > 14:
        cant_iteraciones = 1  # reducir iteraciones para casos grandes
    for _ in range(cant_iteraciones):
        inicio = time.time()
        # resolver_programacion_lineal(k, maestros)
        fin = time.time()
        tiempo += fin - inicio
    return tiempo / cant_iteraciones

def ejecutar_aprox(maestros, k, cant_iteraciones):
    tiempo = 0
    for _ in range(cant_iteraciones):
        inicio = time.time()
        aprox(k, maestros)
        fin = time.time()
        tiempo += fin - inicio
    return tiempo / cant_iteraciones

def tiempos_volumen_aprox(tamanios, cant_iteraciones):
    tiempos_aprox = []
    for n in tamanios:
        maestros = generar_maestros(n, (1, 100))
        tiempo_aprox = 0
        for i in range(1,6):
            k = i
            tiempo_aprox += ejecutar_aprox(maestros, k, cant_iteraciones)
            print(n, k, "aproximacion terminado")
        tiempos_aprox.append(tiempo_aprox / 5)
    return tiempos_aprox

def tiempos(tamanios, cant_iteraciones):
    tiempos_back = []
    tiempos_pl = []
    tiempos_aprox = []
    for n in tamanios:
        maestros = generar_maestros(n, (1, 100))
        tiempo_back = 0
        tiempo_pl = 0
        tiempo_aprox = 0
        for i in range(1,6):
            k = i
            tiempo_back += ejecutar_back(maestros, k, cant_iteraciones)
            print(n, k, "backtracking terminado")
            tiempo_pl += ejecutar_pl(maestros, k, cant_iteraciones)
            print(n, k, "programacion lineal terminado")
            tiempo_aprox += ejecutar_aprox(maestros, k, cant_iteraciones)
            print(n, k, "aproximacion terminado")
        tiempos_back.append(tiempo_back / 5)
        tiempos_pl.append(tiempo_pl / 5)
        tiempos_aprox.append(tiempo_aprox / 5)
    
    return tiempos_back, tiempos_pl, tiempos_aprox

def graficar(tamanios, tiempos_back, tiempos_pl, tiempos_aprox):
    plt.plot(tamanios, tiempos_back, label='Backtracking', marker='o')
    plt.plot(tamanios, tiempos_pl, label='Programacion Lineal', marker='o')
    plt.plot(tamanios, tiempos_aprox, label='Aproximacion', marker='o')
    plt.xlabel('Cantidad de Maestros')
    plt.ylabel('Tiempo Promedio (s)')
    plt.title('Comparacion de Tiempos de Algoritmos')
    plt.legend()
    plt.grid(True)
    plt.show()

def graficar_sin_aprox(tamanios, tiempos_back, tiempos_pl):
    plt.plot(tamanios, tiempos_back, label='Backtracking', marker='o')
    plt.plot(tamanios, tiempos_pl, label='Programacion Lineal', marker='o')
    plt.xlabel('Cantidad de Maestros')
    plt.ylabel('Tiempo Promedio (s)')
    plt.title('Comparacion de Tiempos de Algoritmos (sin Aproximacion)')
    plt.legend()
    plt.grid(True)
    plt.show()

def graficar_sin_pl(tamanios, tiempos_back, tiempos_aprox):
    plt.plot(tamanios, tiempos_back, label='Backtracking', marker='o')
    plt.plot(tamanios, tiempos_aprox, label='Aproximacion', marker='o')
    plt.xlabel('Cantidad de Maestros')
    plt.ylabel('Tiempo Promedio (s)')
    plt.title('Comparacion de Tiempos de Algoritmos (sin Programacion Lineal)')
    plt.legend()
    plt.grid(True)
    plt.show()

def graficar_solo_aprox(tamanios, tiempos_aprox):
    plt.plot(tamanios, tiempos_aprox, label='Aproximacion', marker='o')
    plt.xlabel('Cantidad de Maestros')
    plt.ylabel('Tiempo Promedio (s)')
    plt.title('Tiempos de Aproximacion')
    plt.legend()
    plt.grid(True)
    plt.show()

def graficar_prueba_cociente(tamaños, prueba_cociente):
    plt.figure(figsize=(20, 12))
    plt.plot(tamaños, prueba_cociente, '-o', label='Prueba del Cociente', color='purple')
    plt.legend()
    plt.xlabel("Tamaño de muestra (n)")
    plt.ylabel("Cociente")
    plt.title("Prueba del Cociente")
    plt.show()

def main():
    tamanios = [4,5,6,7,8,9,10,11,12,13,14,15,16,17,18]
    cant_iteraciones = 3
    # tiempos_back, tiempos_pl, tiempos_aprox = tiempos(tamanios, cant_iteraciones)
    # graficar(tamanios, tiempos_back, tiempos_pl, tiempos_aprox)
    # graficar_sin_aprox(tamanios, tiempos_back, tiempos_pl)
    # graficar_sin_pl(tamanios, tiempos_back, tiempos_aprox)
    tamanios = [t * 100000 for t in tamanios]
    tiempos_aprox = tiempos_volumen_aprox(tamanios, cant_iteraciones)
    graficar_solo_aprox(tamanios, tiempos_aprox)

main()