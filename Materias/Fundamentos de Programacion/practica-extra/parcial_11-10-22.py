# Punto 1

# def minus(cadena):
#     for i in cadena:
#         if i.islower():
#             return True

# def mayus(cadena):
#     for i in cadena:
#         if i.isupper():
#             return True

# def num(cadena):
#     for i in cadena:
#         if i.isdigit():
#             return True

# def validar(cadena):
#     aprobada = False
#     rango_cad = len(cadena)
#     cantidad = range(8,13)
#     simbolos = ("*-$@")
#     if rango_cad in cantidad and mayus(cadena) and minus(cadena) and num(cadena):
#         for i in cadena:
#             if i in simbolos and not i.isspace():
#                 aprobada = True
#     return aprobada

# def main():
#     cadena = "Algoritmo$1"
#     print(validar(cadena))

# main()

#Punto 3

def total_politicos(votacion):
    diccionario = {}

    for part, dip, sen in votacion:
        if part not in diccionario:
            diccionario[part] = (dip + sen) 
        else:
            diccionario[part] += (dip + sen)
    return diccionario

def main():
    votacion = [
        ["PP", 19, 35], 
        ["PSOE", 20, 30], 
        ["VOX", 15, 15], 
        ["PP", 0, 15]
    ]
    print(total_politicos(votacion))

main()