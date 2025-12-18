# def pedirFrutaYDarPrecio():
#     verduleria = {"Platano":1.35, "Manzana":0.80, "Pera":0.85, "Naranja":0.70}
#     for fruta in verduleria.keys():
#         fruta = input("Que fruta desea: ")
#         if fruta in verduleria:
#             kilos = int(input("Cuantos kilos: "))
#             total = kilos * verduleria[fruta]
#             print("Seria",total,"pesos")
#         else:
#             print("No se encentra en la verduleria")

def ingresar_valores():
    lista = []
    valor = float(input("Ingrese valor o 0 para terminar: "))
    while valor != 0:
        lista.append(valor)
        valor = float(input("Ingrese valor o 0 para terminar: "))
    print("La lista esta formada por los valores:",lista)

    contador = 0
    lista_3_par = []
    for i in lista:
        if i % 2 == 0 and contador < 3:
            contador += 1
            lista_3_par.append(i)
        elif contador < 3:
            lista_3_par.append(i)
    print("Los valores hasta el 3° valor par son",lista_3_par)

    lista_pos_pares = []
    for i in lista:
        if (lista.index(i) + 1) % 2 == 0:
            lista_pos_pares.append(i)
    print("Los valores en posiciones pares son:",lista_pos_pares)

    lista_no_repetida = []
    for i in lista:
        if i not in lista_no_repetida:
            lista_no_repetida.append(i)   
    lista_ordenada = sorted(lista_no_repetida)
    print("La lista ordenada sin repetir es:",lista_ordenada)

def main():
    ingresar_valores()

main()