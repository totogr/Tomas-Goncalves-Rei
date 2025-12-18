#Escribir una función que reciba por parámetro un diccionario con el siguiente formato:
#{ id_producto: [ stock_minimo, stock_actual ],..........}, donde el id_producto será la
#clave, de tipo cadena; y la lista asociada a cada clave id_producto, contendrá una
#dupla de valores, siendo el primero, el stock mínimo a mantener de dicho producto; y
#el segundo, el stock actual del producto; ambos de tipo entero positivo.
#La función debe imprimir un listado con los productos a reponer (cuyo stock_actual
#sea menor al stock_minimo), indicando el id_producto y la cantidad a reponer.

diccionario={"Manzana": [10,15], "Pera": [10,5], "Melon": [10,9]}
def stock(diccionario):
    for productos in diccionario:
        if diccionario[productos][0] > diccionario[productos][1]:
           a_reponer= diccionario[productos][0] - diccionario[productos][1]
           print(productos, a_reponer)

#stock(diccionario)

def secuencia():
    valores= int(input ("Ingresa lo que quieras o finaliza con 0:"))
    secuencia_valores= []
    while valores != 0:
        secuencia_valores.append(valores)
        valores= int(input ("Ingresa lo que quieras o finaliza con 0:"))
    return secuencia_valores


print(secuencia())



