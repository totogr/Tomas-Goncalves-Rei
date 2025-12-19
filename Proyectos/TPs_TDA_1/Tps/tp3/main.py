from extras.archivos import (leer_entrada_usuario, 
                             crear_grafo_desde_archivo, 
                             leer_argumento,
                             )

from extras.archivos import TIPO_ARCHIVO, TIPO_NUMERO
from solucion_bt import minimizar_distancia_maxima as algo_backtracking
from solucion_greedy import minimizar_distancia_maxima_greedy as algo_greedy
from solucion_pl import clusterizar as algo_pl
from solucion_louvain import algoritmo_louvain_para_mediciones as algo_louvain

TEXTO_MENU = "Algoritmos disponibles:\n" \
"1. Algoritmo Backtracking\n" \
"2. Algoritmo Programación Lineal\n" \
"3. Algoritmo Greedy (aproximación)\n" \
"4. Algoritmo Louvain (aproximación)\n"

MENSAJE_INGRESE_ALGORITMO = "Algoritmo a utilizar (1,2,3 o 4):"
MENSAJE_ERROR_ALGORITMO = "Debe ingresar un numero de algoritmo valido (1,2,3 o 4)"
ENTRADAS_POSIBLES = ["1", "2", "3", "4"]

def main():
    # Abro el grafo que el usuario desea clusterizar
    ruta_archivo = leer_argumento(1, TIPO_ARCHIVO)
    print(ruta_archivo)
    grafo = crear_grafo_desde_archivo(ruta_archivo)

    # Leo la cantidad k de clusters deseados por el usuario
    n = leer_argumento(2, TIPO_NUMERO)
    
    # Muestro el menu
    print(TEXTO_MENU)

    # Obtener algoritmo deseado por el usuario
    numero_algoritmo = int(leer_entrada_usuario(MENSAJE_INGRESE_ALGORITMO, MENSAJE_ERROR_ALGORITMO, set(ENTRADAS_POSIBLES)))
    algoritmo = obtener_algoritmo(numero_algoritmo)
    
    # Devolver solucion
    solucion = algoritmo(grafo, n)

    print(solucion)

def obtener_algoritmo(numero):
    if numero == 1:
        return algo_backtracking
    elif numero == 2:
        return algo_pl
    elif numero == 3:
        return algo_greedy
    else:
        return algo_louvain
    
main()