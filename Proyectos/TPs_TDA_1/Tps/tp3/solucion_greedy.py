from extras.recursos.grafo import Grafo
from extras.archivos import crear_grafo_desde_archivo
from distancias import obtener_distancias

#Llama al algoritmo Greedy
def minimizar_distancia_maxima_greedy(grafo:Grafo,k):
    distancias = obtener_distancias(grafo)
    return maxima_distancia(grafo,k,distancias)

#Algoritmo Greedy
def maxima_distancia(grafo:Grafo,k,distancias):
    vertices = grafo.obtener_vertices()
    cant_vertices = len(vertices)

    clusters = []
    dist_max = 0

    #si k>=|v| -> OPT
    if k>=cant_vertices:
        for i in range(k):
            if i<cant_vertices:
                clusters.append({vertices[i]})
            else:
                clusters.append(set())
        return clusters , dist_max 

    #Obtenemos los k vertices mas centrales del grafo 
    mas_centrales = obtener_vertices_centrales_y_distancias(grafo,distancias)

    centros=set()

    #Mas centrales uno en cada cluster
    for i in range(k):
        clusters.append(set())
        clusters[i].add(mas_centrales[i])
        centros.add(mas_centrales[i])

    #Ahora asignamos al resto por cercania
    for v in grafo.obtener_vertices():
        if v in centros : continue

        indice_mejor_cluster = obtener_cluster_mas_cercano(mas_centrales,distancias,k,v)
        clusters[indice_mejor_cluster].add(v)

    #Calculo la distancia maxima de cada cluster
    for i in range(k):
        cluster = clusters[i]
        for v in cluster:
            for w in cluster:
                dist_local = distancias[v][w]
                if dist_local>dist_max:
                    dist_max = dist_local

    return clusters , dist_max  


# Devuelve una lista ordenada de manera descendente basandose en la centralidad los vertices de un grafo
def obtener_vertices_centrales_y_distancias(grafo:Grafo,distancias):
    centralidad = {}
    for v in grafo:
        centralidad[v]=0

    for v in grafo:
        suma_centralidad = 0
        for w in grafo:
            if v != w:  
                suma_centralidad+= distancias[v][w]
        centralidad[v]=suma_centralidad
        
    ordenados = sorted(centralidad,key=lambda x: centralidad[x])

    return ordenados     

#Obtiene el cluster que minimiza la distancia de un vertice con el resto, basandose en una lista de vertices mas centrales
def obtener_cluster_mas_cercano(mas_centrales,distancias,k,v):
    min_dist = float("inf")
    indice_mejor_cluster = None
    for i in range(k):
        c = mas_centrales[i]
        dist = distancias[c][v]
        if dist<min_dist:
            min_dist = dist
            indice_mejor_cluster = i

    return indice_mejor_cluster 

#PARA PROBAR:

#g_prueba = crear_grafo_desde_archivo("extras/pruebas_catedra/45_3.txt")

#solucion , dist , _ = minimizar_distancia_maxima_greedy(g_prueba,7)

#print(f' Solucion : {solucion}\n Max distancia : {dist} ')