# Un comercio tiene 3 sucursales que trabajan de lunes a viernes. Los montos de las ventas se guardan en una matriz (lista de listas) de 3 x 5.
# Es decir que en cada sublista, tenemos las ventas correspondientes a cada una de las sucursales.
# Hacer un programa en Python que:
# a.      Calcule el total semanal de ventas.
# b.      Indique qué sucursal vendió menos.
# c.      Indique qué día se vendió más y de cuánto fue esa venta.
# El programa tiene que estar bien modularizado y contener una función main para las pruebas.

def ventas_semanal(sucursales):
    total = 0
    for ventas in sucursales:
        total += sum(ventas)
    return total

def menor_cant_ventas(sucursales):
    nom_sucursales = ["Sucursal 1", "Surcursal 2", "Sucursal 3"]
    ventas_diarias = []
    for ventas in sucursales:
        ventas_diarias.append(sum(ventas))
    sucursal_menos_ventas = ventas_diarias.index(min(ventas_diarias))
    return nom_sucursales[sucursal_menos_ventas]

def dia_mas_ventas(sucursales):
    dias = ["Lunes", "Martes", "Miercoles", "Jueves", "Viernes"]
    ventas_diarias = [0,0,0,0,0]
    for sucursales in sucursales:
        for i in range(5):
            ventas_diarias[i] += sucursales[i]
    dia_mayor_ventas = ventas_diarias.index(max(ventas_diarias))
    maxima_cant_ventas = ventas_diarias[dia_mayor_ventas]
    return dias[dia_mayor_ventas], maxima_cant_ventas


def main():
    sucursales =  [[1, 3, 5, 7, 9],
                [2, 4, 6, 8, 10],
                [11, 12, 13, 14, 15]]
    print ("La cantidad total de ventas en la semana fue de:", ventas_semanal(sucursales))
    print ("La sucursal que menos vendio fue:", menor_cant_ventas(sucursales))
    dia_de_mayor_cant_ventas, mayor_cant_vendida = dia_mas_ventas(sucursales)
    print ("El dia que mas se vendio fue el:", dia_de_mayor_cant_ventas, "con:", mayor_cant_vendida, "ventas")

main()