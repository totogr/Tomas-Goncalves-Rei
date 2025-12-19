import numpy as np
import matplotlib.pyplot as plt
from scipy.optimize import curve_fit
import os
import sys
import re

# Labels grafico
TITULO = "Complejidad algoritmo con set de 150.000 palabras"
YLABEL = "Tiempo de ejecución promedio (segundos)"
XLABEL = "Tamaño de entrada (valor)"
TIEMPOS_ALGORITMO = 'Tiempos medidos del algoritmo'
AJUSTE_N = 'Ajuste O(n)'
AJUSTE_1 = 'Ajuste O(n^2)'
AJUSTE_2 = 'Ajuste O(n log n)'
AJUSTE_3 = 'Ajuste O(n^3)'


#Errores
ARGUMENTOS_INSUFICIENTES = "Se debe especificar la ruta de entrada de los resultados"
CARPETA_INEXISTENTE = "La carpeta especificada no existe"

# Mensajes para imprimir por stdout
ERROR_ON = "ECM O(n): {error}"
ERROR_N2 = "ECM O(n^2): {error}"
ERROR_NLOGN = "ECM O(n log n): {error}"
ERROR_N3 = "ECM O(n^3): {error}"
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
    def funcion_n3(n, a): return a * (n ** 3)

    # Ajuste de curvas con minimos cuadrados
    parametros_n, _ = curve_fit(funcion_n, valores, tiempos)
    parametros_n2, _ = curve_fit(funcion_n2, valores, tiempos)
    parametros_nlogn, _ = curve_fit(funcion_nlogn, valores, tiempos)
    parametros_n3, _ = curve_fit(funcion_n3, valores, tiempos)


    # Cálculo de predicciones
    tiempos_n = funcion_n(valores, *parametros_n)
    tiempos_estimados_n2 = funcion_n2(valores, *parametros_n2)
    tiempos_estimados_nlogn = funcion_nlogn(valores, *parametros_nlogn)
    tiempos_n3 = funcion_n3(valores, *parametros_n3)

    # Cálculo del Error Cuadrático Medio
    ecm_n = np.mean((tiempos - tiempos_n) ** 2)
    ecm_n2 = np.mean((tiempos - tiempos_estimados_n2) ** 2)
    ecm_nlogn = np.mean((tiempos - tiempos_estimados_nlogn) ** 2)
    ecm_n3 = np.mean((tiempos - tiempos_n3) ** 2)

    print(ERROR_ON.format(error=ecm_n))
    print(ERROR_N2.format(error=ecm_n2))
    print(ERROR_NLOGN.format(error=ecm_nlogn))
    print(ERROR_N3.format(error=ecm_n3))

    # Comparación de errores con algoritmo
    errores = {
        "O(n)": ecm_n,
        "O(n^2)": ecm_n2,
        "O(n log n)": ecm_nlogn,
        "O(n^3)": ecm_n3
    }
    mejor_ajuste = min(errores, key=errores.get)
    print(MSJ_MEJOR_AJUSTE.format(ajuste=mejor_ajuste))

    # Gráfico
    plt.plot(valores, tiempos, '-', label=TIEMPOS_ALGORITMO)
    plt.plot(valores, tiempos_n, '--', label=AJUSTE_N)
    plt.plot(valores, tiempos_estimados_n2, '--', label=AJUSTE_1)
    plt.plot(valores, tiempos_estimados_nlogn, '--', label=AJUSTE_2)
    plt.plot(valores, tiempos_n3, '--', label=AJUSTE_3)
    plt.xlabel(XLABEL)
    plt.ylabel(YLABEL)
    plt.title(TITULO)
    plt.legend()
    plt.grid()
    plt.show()

realizar_graficos()

# Uso: python3 tiempos_y_complejidad.py <ruta_resultados>