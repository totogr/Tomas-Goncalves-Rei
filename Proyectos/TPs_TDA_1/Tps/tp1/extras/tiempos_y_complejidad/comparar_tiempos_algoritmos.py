import numpy as np
import matplotlib.pyplot as plt
from scipy.optimize import curve_fit
import os
import sys

# Labels grafico
TITULO = "Comparación complejidad algoritmo según escenario"
YLABEL = "Tiempo de ejecución promedio (segundos)"
XLABEL = "Tamaño de entrada (valor)"
CANTIDAD_ALGORITMOS = 3  
LABEL1 = 'Tiempos medidos intervalos equilibrados'
LABEL2 = 'Tiempos medidos intervalos sin solapamientos'
LABEL3 = 'Tiempos medidos intervalos totalmente solapados'

# Errores
ARGUMENTOS_INSUFICIENTES = "Se debe especificar la ruta de entrada de los resultados"
CARPETA_INEXISTENTE = "La carpeta especificada no existe"


def comparar_algoritmos():
    print (len(sys.argv))
    if len(sys.argv) != CANTIDAD_ALGORITMOS+1: 
        raise Exception(ARGUMENTOS_INSUFICIENTES)

    rutas=[]
    for i in range(1,CANTIDAD_ALGORITMOS+1):
        rutas.append(obtener_ruta(i))

    # Cargar datos medidos
    valores1, tiempos1 = np.loadtxt(rutas[0], skiprows=2, unpack=True, usecols=(0, 1)) #primera entrada
    valores2, tiempos2 = np.loadtxt(rutas[1], skiprows=2, unpack=True, usecols=(0, 1)) #segunda entrada
    valores3, tiempos3 = np.loadtxt(rutas[2], skiprows=2, unpack=True, usecols=(0, 1)) # para usar si se quiere comparar con un tercer algoritmo

    # Gráfico
    plt.plot(valores1, tiempos1, '-', label=LABEL1) 
    plt.plot(valores2, tiempos2, '-', label=LABEL2)
    plt.plot(valores3, tiempos3, '-', label=LABEL3)
    # plt.plot(valores3, tiempos3, '-', label='Tiempos medidos ... ') -> para usar si se quiere comparar con un tercer algoritmo
    plt.xlabel(XLABEL)
    plt.ylabel(YLABEL)
    plt.title(TITULO)
    plt.legend()
    plt.grid()
    plt.show()

def obtener_ruta(numero_entrada):
    ruta_archivo = sys.argv[numero_entrada]
    if not os.path.exists(ruta_archivo):
        raise NotADirectoryError(CARPETA_INEXISTENTE)

    return os.path.join(os.getcwd(), ruta_archivo)

comparar_algoritmos()

# Uso: python3 comparar_tiempos_algoritmos.py <ruta_resultados> <ruta_resultados> <ruta_resultados>