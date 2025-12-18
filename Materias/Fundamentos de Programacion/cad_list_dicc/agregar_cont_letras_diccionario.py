frase = input("Ingrese una frase: ")
frase = frase.lower()
palabras = {}
acentos = "áéíóú"
i=0
for i in frase:
    if i not in acentos:
        if i in palabras:
            palabras[i] += 1
        else:
            palabras[i] = 1      
print(palabras)

