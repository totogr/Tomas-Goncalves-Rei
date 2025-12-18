frase = "Ho á tíí pó alAÁ"
frase_minus = frase.lower()
palabras = []
tildes = "áéíóú"
i=0
for i in frase_minus:
    if i=="á" and "a" not in palabras:
        palabras.append("a")
    elif i=="é" and "e" not in palabras:
        palabras.append("e")
    elif i=="í" and "i" not in palabras:
        palabras.append("i") 
    elif i=="ó" and "o" not in palabras:
        palabras.append("o")    
    elif i=="ú" and "u" not in palabras:
        palabras.append("u")
    elif i.isalpha() and i not in palabras and i not in tildes :
        palabras.append(i)

print(len(palabras))
print(palabras)


