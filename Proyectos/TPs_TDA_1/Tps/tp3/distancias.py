from extras.recursos.grafo import Grafo
from collections import deque

#Devuelve un diccionario de diccionarios que contiene todas las distancias entre los vertices
def obtener_distancias(grafo:Grafo):
    #dicc de distancias en el formato : {v:{w:dist,w2:dist},v2:{w:dist,w2:dist}}
    distancias = {} 
    
    for v in grafo.obtener_vertices(): 
        dist_v = bfs(grafo,v) 
        distancias[v] = dist_v

    return distancias 

def bfs(grafo:Grafo,vertice):
    visitados=set()
    cola = deque()
    dist={}

    cola.append(vertice)
    visitados.add(vertice)
    dist[vertice] = 0

    while len(cola) != 0:
        v = cola.popleft()
        for w in grafo.adyacentes(v):
            if w not in visitados:
                cola.append(w)
                visitados.add(w)
                dist[w]= 1 + dist[v]

    return dist 

    
