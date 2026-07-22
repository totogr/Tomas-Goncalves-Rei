# def contar(frase):
#     frase_normal = frase.lower()
#     letras = []
#     for i in frase_normal:
#         if i.isalpha() and i not in letras :
#             letras.append(i)
#     cant_letras = len(letras)
#     return (cant_letras)

# def elegir(atracciones, actividades_deseadas, costo):
#     visitar = False
#     contador = 0
#     max_costo = 5000
#     for i in actividades_deseadas:
#         if i in atracciones:
#             contador += 1
#     if contador >= 3 and costo <= max_costo:
#         visitar = True
#     return visitar

def concurso(lista):
    participantes = {}
    for nom,punt in lista:
        if nom not in participantes:
            participantes[nom] = [punt, 1,punt]
        else:
            participantes[nom][0] += punt
            participantes[nom][1] += 1
            participantes[nom][2] = participantes[nom][0]/participantes[nom][1]

    prom_particip = {}
    for nom in participantes.keys():
        prom_particip[nom] = participantes[nom][2]

    prom_ordenado = sorted(prom_particip.items(), key=lambda x: x[0], reverse=True)
    return prom_ordenado

def main():
    lista = [
        ["Luisa",4],
        ["Mariano",10],
        ["Luisa",5]
    ]
    print(concurso(lista))
    
    # print(contar("Algoritmos y Programación 1"))

    # lista_1 = ["museos", "senderismo", "bares", "montañismo"]
    # lista_2 = ["bares", "museos", "senderismo", "conciertos"]
    # lista_3 = ["bares", "conciertos", "museos"]
    # print(elegir(lista_1, lista_2, 5000))
    # print(elegir(lista_1, lista_3, 1000))



main()