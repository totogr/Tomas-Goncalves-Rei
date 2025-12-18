# Hacer una función que reciba dos enteros y devuelva el mcd de los dos
a=0
b=0

def mcd(a, b):
    temporal = 0
    while b != 0:
        temporal = b
        b = a % b
        a = temporal
    return a

def main():  
    print(mcd(30,25))

main()