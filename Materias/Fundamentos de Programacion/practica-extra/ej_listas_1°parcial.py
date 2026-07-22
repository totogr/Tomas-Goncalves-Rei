def matriz_cuadrada(lista):
    cuadrada = True
    filas = len(lista)
    for filas in lista:
        if len(filas) != filas:
            cuadrada = False
    return cuadrada

def main():
    lista = [
        [1,4,5],
        [0,5,1],
        [0,2,5]
    ]
    print(matriz_cuadrada(lista))
main()