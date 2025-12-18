cadena = str(input("Introduzca su nombre: "))
vocales = "aeiou"
i=0

def vocal(cadena):
    cad_vocales = []
    for i in cadena:
        if i in vocales:
            cad_vocales += i
    return cad_vocales
        
print (vocal(cadena))
