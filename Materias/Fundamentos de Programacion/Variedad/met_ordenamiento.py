def intercambiar(l, i, j):
    aux = l[i]
    l[i] = l[j]
    l[j] = aux
    
def seleccion(lista):
    n = len(lista)
    for i in range(n - 1):
        pos_min = i
        for j in range(i + 1, n):
            if (lista[j] < lista[pos_min]):
                pos_min = j
	# intercambia una sola vez por cada ciclo externo
        intercambiar(lista, i, pos_min)


def burbujeo(lista):
    n = len(lista)
    for i in range(n - 1):
        for j in range(n - i - 1):
            if (lista[j] > lista[j + 1]):
		# intercambia siempre que los elementos no esten en orden
                intercambiar(lista, j, j + 1)


def insercion(lista):
    n = len(lista)
    for i in range(1, n):
        auxiliar = lista[i]
        j = i
        while ((j > 0) and (lista[j - 1] > auxiliar)):
	    # corre los elementos anteriores un lugar si fuera necesario
            intercambiar(lista, j, j-1)
            j = j - 1
        lista[j] = auxiliar



def main():
    lista = [5, 8, 3, 11, 7, 9]
    print("Antes de ordenar", lista)
    insercion(lista)
    print("Despues de ordenar", lista)
    
main()
