# Funcion para buscar un numero en la lista pero recorriendo toda la lista
# def buscar(lista, valor):
#     contador = 0
#     encontrado = -1
#     while contador < len(lista) and encontrado == -1:
#         if lista[contador] == valor:
#             encontrado = contador
#         else:
#             contador = contador + 1
#     return encontrado

#Funcion para buscar un numero en la lista pero primero la ordeno, lo busco y 
#si encuentro un numero mas grande dejo de buscar
# def buscar(lista,valor):
#     lista.sort()
#     contador = 0
#     encontrado = -1
#     while contador < len(lista) and encontrado == -1 and lista[contador] <= valor:
#         if lista[contador] == valor:
#             encontrado = contador
#         else:
#             contador = contador + 1
#     print (contador)
#     return encontrado

#Funcion para buscar un numero en la lista pero primero la ordeno y despues
#voy dividiendo la lista buscando entre sus valores mas grandes y chicos
# def buscar(lista,valor):
#     lista.sort()
#     inferior = 0
#     superior = len(lista) - 1
#     index = -1
#     contador = 0
#     while index == -1 and inferior <= superior:
#         contador = contador + 1
#         mitad = (inferior + superior) // 2
#         if lista[mitad] == valor:
#             index = mitad
#         elif lista[mitad] > valor:
#             superior = mitad -1
#         else:
#             inferior = mitad + 1
    
#     print (contador)
#     return index

def main():
    lista = [5,8,3,11,7,9,1]
    print(buscar(lista, 11))
main()