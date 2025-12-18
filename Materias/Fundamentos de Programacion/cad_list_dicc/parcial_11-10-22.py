# Punto 1

def validar():
    cadena = "Algoritmos$7"
    aprobada = True
    tildes = "áéíóúÁÉÍÓÚ"
    cant_caracteres = range(8,13)
    simbolos = ["*","-","$","@"]
    mayus = False
    minus = False
    digito = False
    simbolo = False
    tilde = True
    i = 0
    if len(cadena) in cant_caracteres:
        while i < len(cadena) and aprobada:
            caracter = cadena[i]
            if caracter in tildes:
                tilde = False
            elif caracter.isupper():
                mayus = True
            elif caracter.islower():
                minus = True
            elif caracter.isdigit():
                digito = True
            elif caracter in simbolos:
                simbolo = True
            else:
                aprobada = False
            i += 1    

    return mayus and minus and digito and simbolo and tilde 

#Punto 3

def total_politicos(votacion):
    TOTAL_VOTOS = 1
    diccionario = {}
    for part, dip, sen in votacion:
        if part not in diccionario:
            diccionario[part] = (dip + sen) 
        else:
            diccionario[part] += (dip + sen)
    partidos = diccionario.items()
    dicc_ordenado = sorted(partidos, key=lambda x: x[TOTAL_VOTOS], reverse = True)
    for i in dicc_ordenado:
        print (i[0], "-", i[1])

def main():
    print(validar())
    votacion = [
        ["PP", 19, 35], 
        ["PSOE", 20, 30], 
        ["VOX", 15, 15], 
        ["PP", 0, 15]
    ]
    total_politicos(votacion)

main()
