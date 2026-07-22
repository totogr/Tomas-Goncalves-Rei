import math
pi=float(math.pi)
#radio=float(input("Ingrese el radio de la circunferencia: "))
#valor_cent=float(input("Ingrese valor en centimetros:"))
#base=float(input("Ingrese ancho del rectangulo: "))
#altura=float(input("Ingrese altura del rectangulo: "))
#temp_fahrenheit=float(input("Ingrese temperatura en Fahrenheit: "))
#velocidad=float(input("Ingrese una velocidad en m/s: "))
#tiempo=float(input("Ingrese un tiempo en s: "))
#num1=float(input("Ingrese primer numero: "))
#num2=float(input("Ingrese segundo numero: "))
#num_entero=int(input("Escriba un valor entero: "))
#num_entero2=int(input("Escriba un segundo valor entero: "))
#gr_agua=float(input("Ingrese valor de gramos de agua: "))
#molec_agua=int(input("Ingrese cantidad de molecula de agua: "))


def longitud_circunferencia(radio):
    longitud = radio*2
    return longitud

def area(radio):
    area = pi * (radio**2)
    return area

def cent_a_pulg(valor_cent):
    pulgada=0.393701
    cent_a_pulg = pulgada*valor_cent
    return cent_a_pulg

def perio_superf_rect(base, altura):
    perimetro = 2*base + 2*altura
    superficie = base*altura
    return perimetro, superficie

def temp_celsius(temp_fahrenheit):
    fahr_a_cels = (temp_fahrenheit-32) * (5/9)
    return fahr_a_cels

def dist_recorrida(velocidad, tiempo):
    distancia = velocidad*tiempo
    return distancia

def equiv_segundos(tiempo):
    segundos = tiempo
    minutos = segundos/60
    horas = minutos/60
    dias = horas/24
    return segundos, minutos, horas, dias

def es_mayor(num1, num2):
    mayor = num1 > num2
    return mayor

def es_impar(num_entero):
    par = num_entero % 2 == 0
    return par

def es_multiplo_de(num_entero, num_entero2):
    multiplo = num_entero % num_entero2 == 0
    return multiplo

def moles(gr_agua):
    gr_a_mol = gr_agua / 18
    return gr_a_mol

def molec_a_atomos(molec_agua):
    atom_hidrog = molec_agua * 2
    atom_oxig = molec_agua
    return atom_hidrog, atom_oxig

#print("El diametro es", longitud_circunferencia(radio))
#print("El area es", area(radio))
#print("El valor en pulgadas es", cent_a_pulg(valor))
#print("El perimetro y la superficie son", perio_superf_rect(base, altura), "respectivamente")
#print("La temperatura es", temp_celsius(temp_fahrenheit), "Celsius")
#print("La distancia recorrida es", dist_recorrida(velocidad, tiempo), "metros")
#print("Los segundos, minutos, horas y dias son", equiv_segundos(tiempo), "respectivamente")
#print(es_mayor(num1, num2))
#print(es_impar(num_entero))
#print(es_multiplo_de(num_entero, num_entero2))
#print("El valor es", moles(gr_agua), "moles")
#print("La cantidad de atomos de hidrogeno y de oxigeno que tiene son", molec_a_atomos(molec_agua), "respectivamente")