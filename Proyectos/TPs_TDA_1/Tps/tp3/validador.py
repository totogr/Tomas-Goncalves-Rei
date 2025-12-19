from extras.recursos.grafo import Grafo
            
def validador_cd(grafo:Grafo, k, c, solucion,distancias):
    #se verifica que sean a lo sumo k clusters
    if len(solucion) > k: return False

    #se verifica que los vertices de la solucion sean correctos -> que esten todos los vertices y sin repetidos
    vertices = set(grafo.obtener_vertices())
    visitados = set()

    for cluster in solucion:
        for v in cluster:
            if v not in vertices or v in visitados : return False
            visitados.add(v)

    if visitados != vertices: return False

    #se verifica que en efecto se cumpla que no superan la distancia c
    for cluster in solucion:
        for v in cluster:
            for w in cluster:
                if v == w : continue 
                if distancias[v][w]>c : return False
    
    return True

