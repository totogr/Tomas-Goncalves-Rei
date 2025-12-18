numero=int(input("Numero de mes: "))

def factorial(numero):
    if numero < 0:
        fact = 0
    else:
        fact = 1
        while numero > 1:
            fact *= numero
            numero -= 1
    return fact

print("El factorial de", numero, "es", factorial(numero))