import numpy as np
import matplotlib.pyplot as plt
import os
import sys

# Labels grafico
TITULO = "Comparación complejidad algoritmo según escenario"
YLABEL = "Tiempo de ejecución promedio (segundos)"
XLABEL = "Tamaño de entrada (valor)"

def comparar_algoritmos():
    if len(sys.argv) < 2:
        raise Exception("Se debe especificar al menos un archivo de resultados")

    rutas = sys.argv[1:]

    datos = []
    for ruta in rutas:
        if not os.path.exists(ruta):
            raise FileNotFoundError(f"El archivo '{ruta}' no existe")
        valores, tiempos = np.loadtxt(ruta, skiprows=2, unpack=True, usecols=(0, 1))
        datos.append((valores, tiempos))

    etiquetas = [os.path.splitext(os.path.basename(ruta))[0] for ruta in rutas]

    cmap = plt.get_cmap('tab10')

    for i, (valores, tiempos) in enumerate(datos):
        color = cmap(i % 10)
        plt.plot(valores, tiempos, '-', label=etiquetas[i], color=color)

    plt.xlabel(XLABEL)
    plt.ylabel(YLABEL)
    plt.title(TITULO)
    plt.legend()
    plt.grid()
    plt.show()

if __name__ == "__main__":
    comparar_algoritmos()

# Uso: python3 comparar_tiempos_algoritmos.py <ruta_resultados> <ruta_resultados> <ruta_resultados>