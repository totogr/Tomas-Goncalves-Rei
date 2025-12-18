numero=int(input("Numero de mes: "))

def calendario(numero):
    mes = "Mes Invalido"
    if numero == 1:
        mes = "Enero"
    elif numero == 2:
        mes = "Febrero"
    elif numero == 3:
        mes = "Marzo"
    elif numero == 4:
        mes = "Abril"
    elif numero == 5:
        mes = "Mayo"
    elif numero == 6:
        mes = "Junio"
    elif numero == 7:
        mes = "Julio"
    elif numero == 8:
        mes = "Agosto"
    elif numero == 9:
        mes = "Septiembre"
    elif numero == 10:
        mes = "Octubre"
    elif numero == 11:
        mes = "Noviembre"
    elif numero == 12:
        mes="Diciembre"
    else:
        mes = "Mes Invalido"
    return mes

print (calendario(numero))