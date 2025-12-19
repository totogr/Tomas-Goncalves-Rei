import numpy as np
import matplotlib.pyplot as plt
from scipy.optimize import curve_fit
import os
import sys

# Labels grafico
TITULO = "Complejidad algoritmo con set pruebas 150.000 intervalos"
YLABEL = "Tiempo de ejecución promedio (segundos)"
XLABEL = "Tamaño de entrada (valor)"
TIEMPOS_ALGORITMO = 'Tiempos medidos del algoritmo'
AJUSTE_1 = 'Ajuste O(n^2)'
AJUSTE_2 = 'Ajuste O(n log n)'

#Errores
ARGUMENTOS_INSUFICIENTES = "Se debe especificar la ruta de entrada de los resultados"
CARPETA_INEXISTENTE = "La carpeta especificada no existe"

# Mensajes para imprimir por stdout
ERROR_N2 = "ECM O(n^2): {error}"
ERROR_NLOGN = "ECM O(n log n): {error}"
MSJ_MEJOR_AJUSTE_N2 = "Mejor ajuste al algoritmo: O(n^2)"
MSJ_MEJOR_AJUSTE_NLOGN = "Mejor ajuste al algoritmo: O(n log n)"

def realizar_graficos():
    if len(sys.argv) != 2:
        raise Exception(ARGUMENTOS_INSUFICIENTES)
        
    ruta_archivo = sys.argv[1]
    if not os.path.exists(ruta_archivo):
        raise NotADirectoryError(CARPETA_INEXISTENTE)

    ruta = os.path.join(os.getcwd(), ruta_archivo)

    # Cargar datos medidos
    valores, tiempos = np.loadtxt(ruta, skiprows=2, unpack=True, usecols=(0, 1))

    # Funciones de ajuste
    def funcion_n2(n, a):
        return a * (n ** 2)

    def funcion_nlogn(n, a):
        return a * n * np.log(n)

    # Ajuste de curvas con minimos cuadrados
    parametros_n2, _ = curve_fit(funcion_n2, valores, tiempos)
    parametros_nlogn, _ = curve_fit(funcion_nlogn, valores, tiempos)

    # Cálculo de predicciones
    tiempos_estimados_n2 = funcion_n2(valores, *parametros_n2)
    tiempos_estimados_nlogn = funcion_nlogn(valores, *parametros_nlogn)

    # Cálculo del Error Cuadrático Medio
    ecm_n2 = np.mean((tiempos - tiempos_estimados_n2) ** 2)
    ecm_nlogn = np.mean((tiempos - tiempos_estimados_nlogn) ** 2)

    print(ERROR_N2.format(error=ecm_n2))
    print(ERROR_NLOGN.format(error=ecm_nlogn))

    # Comparación de errores con algoritmo
    if ecm_n2 < ecm_nlogn:
        print(MSJ_MEJOR_AJUSTE_N2)
    else:
        print(MSJ_MEJOR_AJUSTE_NLOGN)

    # Gráfico
    plt.plot(valores, tiempos, '-', label=TIEMPOS_ALGORITMO)
    plt.plot(valores, tiempos_estimados_n2, '--', label=AJUSTE_1)
    plt.plot(valores, tiempos_estimados_nlogn, '--', label=AJUSTE_2)
    plt.xlabel(XLABEL)
    plt.ylabel(YLABEL)
    plt.title(TITULO)
    plt.legend()
    plt.grid()
    plt.show()

realizar_graficos()

# Uso: python3 tiempos_y_complejidad.py <ruta_resultados>