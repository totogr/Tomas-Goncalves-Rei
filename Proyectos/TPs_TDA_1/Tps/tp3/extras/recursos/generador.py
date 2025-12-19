from grafo import Grafo
import os
import sys

#Constantes para los generadores
#Formato SETS_PRUEBAS = (cantidad clusters,distancia maxima)
SETS_PRUEBAS = [(1,10),(2,5),(2,10),(3,1),(3,2),(3,8),(3,10),(4,2),(4,5),(4,10),(5,5),(5,10),(6,1),(6,2),(6,5),(6,10),(7,2)]
NOMBRE_ARCHIVO_ES = "v-{n}_k-{k}_c-{c}.txt"
NOMBRE_ARCHIVO_RESULTADOS_OPTIMOS = "Resultados_optimos.txt"

# Errores
FILE_NOT_FOUND = "ERROR: El archivo no existe"
ARGUMENTOS_INSUFICIENTES = "Se necesita un argumento {indice} en entrada estandar"
NOT_A_DIRECTORY = "La carpeta especificada no existe"

#Generador de grafos de k clusters y c como distancia maxima
def generador_grafo(k,c):
    #k: cantidad de clusters 
    #c: distancia maxima dentro de un cluster
    grafo = Grafo(es_dirigido=False)
    aristas = []    
    num_vertice = 0
    uniones = []

    for i in range(k):
        uniones.append(num_vertice)
        anterior = None

        for j in range(c+1):
             grafo.agregar_vertice(num_vertice)
             if anterior != None:
                  grafo.agregar_arista(anterior,num_vertice)
                  aristas.append((anterior,num_vertice))
             anterior = num_vertice
             num_vertice+=1

    for i in range(1,len(uniones)):
        grafo.agregar_arista(uniones[i-1],uniones[i])
        aristas.append((uniones[i-1],uniones[i]))

    return grafo, aristas

#Maneja creacion de archivos de pruebas
def crear_archivos_pruebas():
    ruta_resulados = generar_archivo_resultado()
    for cantidad_clusters,distancia_maxima in SETS_PRUEBAS:
        grafo , aristas = generador_grafo(cantidad_clusters,distancia_maxima)
        generar_archivo(aristas, len(grafo.obtener_vertices()),cantidad_clusters,distancia_maxima,ruta_resulados)

#Crea el archivo que contendra los datos y la distancia optima de todos los grafos creados
def generar_archivo_resultado():
     nombre_archivo= NOMBRE_ARCHIVO_RESULTADOS_OPTIMOS
     carpeta = leer_directorio(1)
     ruta = os.path.join(os.getcwd(), carpeta, nombre_archivo)

     with open(ruta, "w") as archivo:
          
        archivo.write(f"Resultados optimos de los archivos:\n")
        archivo.write(f"\n")

     return ruta

#Genera el archivo que contiene el formato del grafo y agrega informacion sobre este al archivo de resultados optimos
def generar_archivo(aritas, cantidad_vertices,cantidad_clusters,distancia_maxima,ruta_resulados):
    nombre_archivo = NOMBRE_ARCHIVO_ES.format(n=cantidad_vertices,k=cantidad_clusters,c=distancia_maxima)
    carpeta = leer_directorio(1)
    ruta = os.path.join(os.getcwd(), carpeta, nombre_archivo)

    with open(ruta, "w") as archivo:

        archivo.write(f"#sarasa\n")
        
        for arista in aritas:
            v,w = arista
            archivo.write(f"{v},{w}\n")

    with open(ruta_resulados, "a") as archivo:
         
         archivo.write(f"- Archivo: {nombre_archivo}\n")
         archivo.write(f"--- Cantidad de vertices: {cantidad_vertices}\n")
         archivo.write(f"--- Cantidad de clusters: {cantidad_clusters}\n")
         archivo.write(f"--- Distancia maxima esperada: {distancia_maxima}\n")
         archivo.write(f"\n")
         archivo.write(f"\n")
    

def leer_directorio(indice):
	"""
	Lee el argumento <indice> de la entrada estandar y comprueba que sea una carpeta.

	Si lo es, devuelve True.
	
	"""
	if indice >= len(sys.argv):
		raise Exception(ARGUMENTOS_INSUFICIENTES.format(indice=indice))
	
	carpeta = sys.argv[indice]
	if not os.path.exists(carpeta):
		raise NotADirectoryError(NOT_A_DIRECTORY)
	
	return carpeta
	
crear_archivos_pruebas()

#Utilizar con :
# python3  generador.py <direccion directorio>