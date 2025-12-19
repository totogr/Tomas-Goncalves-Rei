import argparse
from extras.archivos import crear_grafo_desde_archivo
from extras.recursos.louvain import Louvain
from distancias import obtener_distancias
from extras.recursos.grafo import Grafo

# Agrupa nodos en comunidades con algoritmo de Louvain
def obtener_comunidades(grafo):
    louvain = Louvain(grafo)
    louvain.ejecutar()
    return louvain.comunidades.items()

# Calcula la distancia maxima dentro de los cluster seleccionados
def calcular_maxima_distancia(grafo, asignaciones):
    distancias = obtener_distancias(grafo)

    comunidades_dict = {}
    for nodo, comunidad in asignaciones:
        comunidades_dict.setdefault(comunidad, set()).add(nodo)

    max_distancias = []

    for nodos in comunidades_dict.values():
        max_dist = 0
        for u in nodos:
            if u not in distancias:
                continue
            for v in nodos:
                if v == u:
                    continue
                if v in distancias[u]:
                    max_dist = max(max_dist, distancias[u][v])
        max_distancias.append(max_dist)

    return max(max_distancias) if max_distancias else None

# Muestra por pantalla las comunidades
def imprimir_comunidades(asignaciones):
    comunidades = {}
    for nodo, comunidad in asignaciones:
        comunidades.setdefault(comunidad, []).append(nodo)

    print("Asignación:")
    for id_comunidad, nodos in comunidades.items():
        print(f"Cluster {id_comunidad} : {sorted(nodos)}")

# Creacion de subgrafo con mismo nodos y aristas
def crear_subgrafo(grafo: Grafo, nodos: list) -> Grafo:
    subgrafo = Grafo(es_dirigido=False)
    for v in nodos:
        subgrafo.agregar_vertice(v)
    
    for v in nodos:
        for w in grafo.adyacentes(v):
            if w in nodos:
                subgrafo.agregar_arista(v, w)
    
    return subgrafo

# Procesamiento principal del algoritmo
def procesar_grafo(nombre_archivo):
    grafo = crear_grafo_desde_archivo(nombre_archivo)
    comunidades = obtener_comunidades(grafo)
    imprimir_comunidades(comunidades)

    distancia_max = calcular_maxima_distancia(grafo, comunidades)
    print(f"Maxima distancia dentro del cluster: {distancia_max}")

# Algoritmos de louvain con procesamiento para realizar mediciones
def algoritmo_louvain_para_mediciones(grafo):
    comunidades = obtener_comunidades(grafo)
    distancia_max = calcular_maxima_distancia(grafo, comunidades)

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("archivo", type=str, help="Archivo de texto con lista de aristas")
    args = parser.parse_args()
    procesar_grafo(args.archivo)

if __name__ == "__main__":
    main()

# Ejecucion: $ python solucion_louvain.py <archivo.txt>