import numpy as np
import matplotlib.pyplot as plt
from scipy.optimize import curve_fit
import os
import sys
import re

# Labels grafico
TITULO = "Complejidad de algoritmo"
YLABEL = "Tiempo de ejecución promedio (segundos)"
XLABEL = "Tamaño de entrada (valor)"
TIEMPOS_ALGORITMO = 'Tiempos medidos del algoritmo'
AJUSTE_N = 'Ajuste O(n)'
AJUSTE_1 = 'Ajuste O(n^2)'
AJUSTE_2 = 'Ajuste O(n log n)'
AJUSTE_EXP = 'Ajuste O(c^n)'

#Errores
ARGUMENTOS_INSUFICIENTES = "Se debe especificar la ruta de entrada de los resultados"
CARPETA_INEXISTENTE = "La carpeta especificada no existe"

# Mensajes para imprimir por stdout
ERROR_ON = "ECM O(n): {error}"
ERROR_N2 = "ECM O(n^2): {error}"
ERROR_NLOGN = "ECM O(n log n): {error}"
ERROR_EXP = "ECM O(c^n): {error}"
MSJ_MEJOR_AJUSTE = "Mejor ajuste al algoritmo: {ajuste}"

def realizar_graficos():
    if len(sys.argv) != 2:
        raise Exception(ARGUMENTOS_INSUFICIENTES)
        
    ruta_archivo = sys.argv[1]
    if not os.path.exists(ruta_archivo):
        raise NotADirectoryError(CARPETA_INEXISTENTE)

    ruta = os.path.join(os.getcwd(), ruta_archivo)

    # Cargar datos medidos
    valores = []
    tiempos = []
    with open(ruta, encoding='utf-8') as f:
        for linea in f:
            if linea.strip() == "" or "Tamanio" in linea:
                continue
            partes = linea.strip().split()
            try:
                match = re.search(r"(\d+)\s+([0-9.]+)", linea)
                if match:
                    valor = int(match.group(1))
                    tiempo = float(match.group(2))
                    valores.append(valor)
                    tiempos.append(tiempo)
            except Exception:
                continue
    valores = np.array(valores)
    tiempos = np.array(tiempos)

    # Funciones de ajuste
    def funcion_n(n, a): return a * n
    def funcion_n2(n, a): return a * (n ** 2)
    def funcion_nlogn(n, a): return a * n * np.log(n)
    def funcion_exponencial(n, a, b): return a * (b ** n)

    # Ajuste de curvas con minimos cuadrados
    parametros_n, _ = curve_fit(funcion_n, valores, tiempos)
    parametros_n2, _ = curve_fit(funcion_n2, valores, tiempos)
    parametros_nlogn, _ = curve_fit(funcion_nlogn, valores, tiempos)

    try:
        parametros_exp, _ = curve_fit(funcion_exponencial, valores, tiempos, p0=[1e-9, 1.1], maxfev=10000)
        tiempos_exp = funcion_exponencial(valores, *parametros_exp)
        ecm_exp = np.mean((tiempos - tiempos_exp) ** 2)
    except RuntimeError:
        tiempos_exp = None
        ecm_exp = float('inf')

    # Cálculo de predicciones
    tiempos_n = funcion_n(valores, *parametros_n)
    tiempos_estimados_n2 = funcion_n2(valores, *parametros_n2)
    tiempos_estimados_nlogn = funcion_nlogn(valores, *parametros_nlogn)

    # Cálculo del Error Cuadrático Medio
    ecm_n = np.mean((tiempos - tiempos_n) ** 2)
    ecm_n2 = np.mean((tiempos - tiempos_estimados_n2) ** 2)
    ecm_nlogn = np.mean((tiempos - tiempos_estimados_nlogn) ** 2)

    print(ERROR_ON.format(error=ecm_n))
    print(ERROR_N2.format(error=ecm_n2))
    print(ERROR_NLOGN.format(error=ecm_nlogn))
    print(ERROR_EXP.format(error=ecm_exp))

    # Comparación de errores con algoritmo
    errores = {
        "O(n)": ecm_n,
        "O(n^2)": ecm_n2,
        "O(n log n)": ecm_nlogn,
        "O(c^n)": ecm_exp
    }
    mejor_ajuste = min(errores, key=errores.get)
    print(MSJ_MEJOR_AJUSTE.format(ajuste=mejor_ajuste))

    # Gráfico
    plt.plot(valores, tiempos, '-', label=TIEMPOS_ALGORITMO)
    plt.plot(valores, tiempos_n, '--', label=AJUSTE_N)
    plt.plot(valores, tiempos_estimados_n2, '--', label=AJUSTE_1)
    plt.plot(valores, tiempos_estimados_nlogn, '--', label=AJUSTE_2)
    if tiempos_exp is not None:
        plt.plot(valores, tiempos_exp, '--', label=AJUSTE_EXP)
    plt.xlabel(XLABEL)
    plt.ylabel(YLABEL)
    plt.title(TITULO)
    plt.legend()
    plt.grid()
    plt.show()

realizar_graficos()

# Uso: python3 tiempos_y_complejidad.py <ruta_resultados>