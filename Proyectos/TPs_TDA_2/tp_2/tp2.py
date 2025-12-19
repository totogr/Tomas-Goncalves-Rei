import sys
import time
from archivos.parseo import parsear_archivo, leer_archivo
    
"""
Algoritmo de programacion dinamica.
Devuelve el arreglo eliminados.
"""
def algoritmo(n, x, f):
    # eliminados[i] = maxima cantidad de enemigos eliminados hasta el minuto i
    eliminados = [0] * (n + 1)
    for i in range(1, n + 1):
        eliminados_sin_atacar = eliminados[i-1]
        eliminados_atacando = 0
        for j in range(0,i):
            eliminados_atacando = max(eliminados_atacando, eliminados[j] + min(x[i], f[i - j]))
        eliminados[i] = max(eliminados_sin_atacar, eliminados_atacando)
    
    return eliminados
"""
Construye la estrategia paso a paso en base a los ataques encontrados.
"""
def construir_estrategia(n, eliminados,x,f):
    ataques = []

    i = n
    while i > 0:
        if eliminados[i] == eliminados[i-1]:
            i -= 1
        else:
            for j in range(0,i):
                if eliminados[i]==eliminados[j]+min(x[i],f[i-j]):
                    ataques.append(i)
                    i = j
                    break
    ataques.reverse()

    estrategia = []
    set_ataques = set(ataques)
    for i in range(1, n + 1):
        if i in set_ataques:
            estrategia.append("Atacar")
        else:
            estrategia.append("Cargar")
    return estrategia

def main():
    if len(sys.argv) < 2:
        print("Uso: python main.py archivo_instancia")
        sys.exit(1)

    archivo = leer_archivo()
    n, x, f = parsear_archivo(archivo)
    eliminados = algoritmo(n, x, f)
    estrategia = construir_estrategia(n, eliminados,x,f)

    print("Estrategia:", ", ".join(estrategia))
    print(f"Cantidad de tropas eliminadas: {eliminados[n]}")

if __name__ == "__main__":
    main()
