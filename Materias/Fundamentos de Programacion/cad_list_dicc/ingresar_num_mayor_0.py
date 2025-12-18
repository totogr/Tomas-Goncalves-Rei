numero=int(input("Numero: "))

def positivo(numero):
    while numero <= 0:
        print ("ERROR: El valor a ingresar debe ser mayor a 0")
        numero=int(input("Ingrese un nuevo valor: "))
    return numero

print ("Su valor:",positivo(numero), ",es mayor a 0")
