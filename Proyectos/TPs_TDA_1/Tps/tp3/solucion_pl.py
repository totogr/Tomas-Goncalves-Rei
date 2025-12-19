import pulp as pl
from pulp import lpSum as Sumatoria
from extras.recursos.grafo import Grafo
from distancias import obtener_distancias
from extras.archivos import crear_grafo_desde_archivo

def clusterizar(grafo:Grafo, cantidad_clusters):
    # Constantes del modelo
    distancias = obtener_distancias(grafo)
    vertices = grafo.obtener_vertices()

    # Creo el modelo
    prob = pl.LpProblem("Separar en k clusters de un grafo minimizando las distancias maximas", pl.LpMinimize)

    # VARIABLES
    variables_x = {} #clave: nombre, valor: variable
    variables_d = {} #lo mismo
    variables_x_clusters = [] #En cada posicion k, guarda todas las variables asociadas al cluster k
    variables_x_vertices = [] #En cada posición, guarda las variables asociadas al vertice i

    # Por cada cluster
    for k in range(cantidad_clusters):
        # Creo variable de dist maxima en el cluster
        nombre_variable_d = f"D_{k}"
        variable_dist = pl.LpVariable(f"D_{k}", cat=pl.LpInteger)
        variables_d[nombre_variable_d] = variable_dist

        # Inicializo la lista de variables de este cluster
        variables_x_clusters.append([])
        
        # Creo variable de vertices para este cluster
        for i in range(len(vertices)):
            # Si todavia no inicialize la lista de variables de este vertice,la inicializo
            if i <= len(variables_x_vertices):
                variables_x_vertices.append([])

            nombre_variable = f"x_{i}_{k}"
            variable = pl.LpVariable(nombre_variable, cat=pl.LpBinary)
            variables_x[nombre_variable] = variable
            variables_x_clusters[k].append(variable)
            variables_x_vertices[i].append(variable)

    # Creo variables y
    variables_y = {}
    for i in range(len(vertices)):
        for j in range(i+1, len(vertices)):
            nombre_variable = f"Y_{i}_{j}"
            variable = pl.LpVariable(nombre_variable, cat=pl.LpBinary)
            variables_y[nombre_variable] = variable

    # Creo variable con distancia maxima
    variable_dist_maxima = pl.LpVariable("D_MAX", cat=pl.LpInteger)

    # Agrego funcion objetivo a minimizar
    prob += variable_dist_maxima

    # RESTRICCIONES
    # 1. Cada vertice pertenece como maximo a un cluster
    for variables_vertice in variables_x_vertices:
        prob += Sumatoria(variables_vertice) <= 1.0

    # 2. La suma de todas las variables de todos los vertices debe ser al menos V
    prob += Sumatoria(variables_x.values()) >= len(vertices)

    # 3. DMAX es mayor o igual a la distancia maxima dentro de un cluster
    for d in variables_d.values():
        prob += variable_dist_maxima >= d
    
    # 4 y 5
    for k in range(cantidad_clusters):
        nombre_variable_cluster = f"D_{k}"
        variable_dist_cluster = variables_d[nombre_variable_cluster]

        for i in range(len(vertices)):
            for j in range(i+1, len(vertices)):
                # Obtengo la variable Y
                nombre_y = f"Y_{i}_{j}"
                variable_y_i_j = variables_y[nombre_y]

                # Obtengo distancia entre los vertices
                vertice_i = vertices[i]
                vertice_j = vertices[j]
                dist_ij = distancias[vertice_i][vertice_j]

                # Obtengo variables asociadas a los vertices
                variable_x_i = variables_x_clusters[k][i]
                variable_x_j = variables_x_clusters[k][j]

                # 4. La variable Yi,j vale 1 si ambos vertices estan en el mismo cluster
                prob += (variable_y_i_j >= variable_x_i + variable_x_j - 1)

                # 5. La distancia maxima del cluster es mayor o igual a la distancia 
                # entre estos vertices, siempre y cuando esten en este cluster
                prob += variable_dist_cluster >= (dist_ij*variable_y_i_j)

    # Resuelvo el problema
    prob.solve()
    
    resultado = []
    for cluster in variables_x_clusters:
        variables_cluster = []
        
        for variable in cluster:
            if pl.value(variable) == 0:
                continue
            nombre_variable = str(variable)
            _, indice_vertice, _ = nombre_variable.split("_")
            indice_vertice = int(indice_vertice)
            variables_cluster.append(vertices[indice_vertice])

        resultado.append(variables_cluster)

    return resultado

if __name__ == "__main__":
    g_prueba = crear_grafo_desde_archivo("extras/pruebas_catedra/22_3.txt")
    print(clusterizar(g_prueba,4))