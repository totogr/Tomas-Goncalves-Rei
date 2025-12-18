#Seleccion (malo)

# def seleccion(lista):
#     contador = 0
#     for i in range(len(lista) - 1):
#         pos_min = i
#         for p in range(i + 1, len(lista)):
#             if lista[p] < lista[pos_min]:
#                 pos_min = p
#         intercambiar(lista, i, pos_min)
#     print("Contador:", contador)
#     return lista

#Burbujeo (malo)

# def burbujeo(lista):
#     contador = 0
#     for i in range(len(lista) - 1):
#         for p in range(len(lista) - i - 1):
#             if lista[p] > lista[p + 1]:
#                 intercambiar(lista, p ,p + 1)
#                 contador = contador + 1
#     print("Contador:", contador)
#     return lista

#Insercion (con muy muy mal orden, es malo. sirve si los datos estan mas o menos ordenados)

# def insercion(lista):
#     for i in range(1, len(lista)):
#         aux = lista[i]
#         p = i
#         while p > 0 and lista[p - 1] > aux:
#             intercambiar(lista, p, p - 1)
#             p = p - 1
#         lista[p] = aux
#     return lista

def intercambiar(lista, i, p):
    aux = lista[i]
    lista[i] = lista[p]
    lista[p] = aux

def main():
    lista = [1,5,9,6,44,3,8]
    print(insercion(lista))

main()