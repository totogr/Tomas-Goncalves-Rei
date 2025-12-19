from extras.recursos.grafo import Grafo
from distancias import obtener_distancias
from solucion_greedy import maxima_distancia
from extras.archivos import crear_grafo_desde_archivo

def minimizar_distancia_maxima(grafo:Grafo,k):
    distancias = obtener_distancias(grafo)

    sol_actual = [set() for _ in range(k)]
    dist_max_sol_actual =0
    
    mejor_sol , dist_mejor_sol = maxima_distancia(grafo,k,distancias)

    vertices = grafo.obtener_vertices()

    return minimizar_distancia_maxima_bt(grafo,k,distancias,vertices,0,sol_actual,dist_max_sol_actual,mejor_sol,dist_mejor_sol)


def minimizar_distancia_maxima_bt(grafo,k,distancias,vertices,indice,sol_actual,dist_max_sol_actual,mejor_sol,dist_mejor_sol):
    if indice == len(vertices):
        if dist_max_sol_actual<dist_mejor_sol:
            copia_sol_actual = []
            for cluster in sol_actual:
                copia_sol_actual.append(set(cluster))
            return copia_sol_actual, dist_max_sol_actual
        return mejor_sol , dist_mejor_sol
    
    v = vertices[indice]
    for i in range(k):  #Probamos agregar el vertice en el cluster i 
        cluster_actual = sol_actual[i]

        # Poda -> si ponemos en vacio evitamos repetir combinaciones
        if len(cluster_actual)==0:
            hay_cluster_vacio_menor= False
            for j in range(i):
                if len(sol_actual[j]) == 0:
                    hay_cluster_vacio_menor = True
                    break
            if hay_cluster_vacio_menor:
                continue

        condicion , maxima = validacion(cluster_actual,distancias,v,dist_mejor_sol)

        if condicion: #Poda -> si agregarlo a ese cluster empeora mi mejor solucion
            nueva_distancia_max = max(maxima,dist_max_sol_actual)

            if dist_max_sol_actual >= dist_mejor_sol:
                continue  

            sol_actual[i].add(v)

            mejor_sol , dist_mejor_sol = minimizar_distancia_maxima_bt(grafo,k,distancias,vertices,indice+1,sol_actual,nueva_distancia_max,mejor_sol,dist_mejor_sol)
        
            sol_actual[i].remove(v)

    return mejor_sol , dist_mejor_sol


#Si agregar un vertice hace que la distancia maxima de ese cluster sea mayor que la mejor hasta el momento retorna False
def validacion(cluster_actual,distancias,v,dist_mejor_sol):
    maxima = 0
    for w in cluster_actual:
        dist = distancias[v][w] 
        if dist>=dist_mejor_sol:
            return False , None
        if dist>maxima:
            maxima = dist
    return True , maxima

g_prueba = crear_grafo_desde_archivo("extras/pruebas_nuestras/v-55_k-5_c-10.txt")

solucion , dist = minimizar_distancia_maxima(g_prueba,5)

print(f' Solucion : {solucion}\n Max distancia : {dist} ')