def solicitar_dato(mensaje):
    try:
        valor = input(mensaje)
        if valor.isnumeric():
            valor = int(valor)
        else:
            valor = 1
    except ValueError as notnum:
        print("Solo se pueden dividir numeros. Error:", notnum)
    return valor
try:
    num1=solicitar_dato("Numero: ")
    num2=solicitar_dato("Numero2: ")
    cociente=float(num1/num2)
    print(cociente)
except ZeroDivisionError as cero:
    print("No se puede dividir por cero. Error:", cero)
except Exception as error:
    print("Error inesperado.", error)
finally:
    print("Fin del programa")




