# Escribir una función que reciba por parámetro un diccionario con el siguiente formato: { id_producto: [ stock_minimo, stock_actual ],..........}, donde el id_producto será la
# clave, de tipo cadena; y la lista asociada a cada clave id_producto, contendrá una dupla de valores, siendo el primero, el stock mínimo a mantener de dicho producto; y
# el segundo, el stock actual del producto; ambos de tipo entero positivo. La función debe imprimir un listado con los productos a reponer (cuyo stock_actual
# sea menor al stock_minimo), indicando el id_producto y la cantidad a reponer.

stock = {"Manzana": [5,10], "Pera":[5,2], "Banana":[5,7], "Kiwi":[5,0]}
def reponer(stock):
    STOCK_MINIMO = 0
    STOCK_ACTUAL = 1
    
    for producto in stock:
        if stock[producto][STOCK_MINIMO] > stock[producto][STOCK_ACTUAL]:
            cantidad_reponer = stock[producto][STOCK_MINIMO] - stock[producto][STOCK_ACTUAL]
            print("Reponer", cantidad_reponer , producto)

# Escribir una función que reciba por parámetro un texto, y devuelva un diccionario, el cual tendrá como claves, cada una de las palabras que hay en el texto, y como valor,
# la cantidad de ocurrencias de dicha palabra en el texto. No distinguir entre mayúsculas y minúsculas.
# Considerar que las palabras del texto estarán separadas por blancos.

texto = "Hola hola como te encuentras en el dia en el que como milanesa"
def ocurrencias(texto):
    dicc_ocurrencias={}
    texto_minus = texto.lower()
    for palabra in texto_minus.split():
        if palabra != " " and palabra not in dicc_ocurrencias:
            dicc_ocurrencias[palabra] = 1
        elif palabra in dicc_ocurrencias:
            dicc_ocurrencias[palabra] += 1
    print(dicc_ocurrencias)

# Dado el diccionario "dic_materias" que tiene cargados los nombre de las materias como clave, y como valor asociado, una lista con tres números enteros, que indican:
# la cantidad de alumnos anotados (como primer valor), cantidad de alumnos que rindieron el parcial (segundo valor), cantidad de alumnos que aprobaron el parcial
# (tercer valor). Se pide que escribas:
# a) Una función que reciba el diccionario y devuelva una lista con las materias cuyo índice de deserción sea mayor al 50% (esto se calcula teniendo en cuenta la cantidad
# de alumnos que rindieron el parcial sobre la cantidad de anotados).
# b) Una función que reciba el diccionario y que devuelva una lista de tuplas, formadas por pares (materia, porcentaje_aprobados), ordenada de mayor a menor por
# porcentaje de aprobados (en este caso, se calcula sobre la cantidad que rindieron).

dic_materias = {"Matematica": [30,30,20], "Lengua": [30,20,15], "Fisica": [30,10,10]}
def desercion(materias):
    CANT_ALUMNOS = 0
    CANT_RINDIERON = 1
    CANT_APROBADOS = 2
    indice_desercion = []

    for i in materias:
        if (materias[i][CANT_ALUMNOS] // 2) > materias[i][CANT_RINDIERON]:
            indice_desercion.append(i)

    print("Las materias con un indice de desercion mayor al 50% fueron:",indice_desercion)

    lista_tuplas = []
    for i in materias:
        porcentaje_aprobados = (materias[i][CANT_APROBADOS] * 100) // materias[i][CANT_ALUMNOS]
        lista_tuplas.append((i,porcentaje_aprobados))
        aprobados_ordenado = sorted(lista_tuplas, key=lambda x: x[1], reverse=True)

    print("Los porcentajes de aprobados de cada materia fueron:",aprobados_ordenado)

# Escribir un programa que solicite el ingreso de un texto y luego informe un ranking de las palabras que aparecen en el texto.
# El texto ingresado debe tener como mínimo 20 palabras. En caso de no tener las palabras suficientes, solicitar se ingrese más texto, que debe ser agregado al ya
# existente. Considere palabras que sólo estén formadas por letras.
# Tenga en cuenta que seguido a una palabra, pueden estar los siguientes signos de puntuación: “,;.:”, en este caso, se debe quitar el signo.
# No debe diferenciar entre mayúsculas y minúsculas, por ejemplo: si aparecen en el texto las siguientes palabras: “sol”, “Sol”, “SOL”; se deben contabilizar 3 ocurrencias
# de la palabra “sol” como clave. No utilice métodos tales como count, find, index.
# Imprima el ranking, ordenado descendentemente, por la cantidad de ocurrencias de la palabra.


def agregar_palabras(frase):
    simbolos = ",:;."
    palabras_texto = []
    frase_minus = frase.lower()
    for palabra in frase_minus.split():
        if palabra != " " and palabra not in simbolos:
            palabras_texto.append(palabra)
    return palabras_texto

def ocurrencias(ranking):
    diccionario = {}
    for palabra in ranking:
        if palabra in diccionario:
            diccionario[palabra] += 1
        else:
            diccionario[palabra] = 1
    
    cant_palabras = diccionario.items()
    ranking_ordenado = sorted(cant_palabras, key=lambda x: x[1], reverse=True)

    for i in ranking_ordenado:
        print(i[0],"-",i[1])

def ranking_palabras():
    texto = input("Ingrese texto de mas de 20 palabras: ")
    oracion = agregar_palabras(texto)
    while len(oracion) < 20:
        mas_texto = input("Ingrese mas texto: ")
        oracion += agregar_palabras(mas_texto)
    
    return ocurrencias(oracion)

# Escribir un programa que permita gestionar los datos de los alumnos de un curso. El programa deberá:
# a) Permitir la carga de un diccionario, que tendrá por clave un número de padrón, y por valores asociados a la clave, una lista compuesta por valores numéricos, que
# serán las notas obtenidas por un alumno. En cada ingreso, se deberá solicitar el padrón y la nota a cargar. Si el padrón es existente, se agregará la nota a la lista de
# notas; sino, se creará la clave padrón correspondiente con la nota asociada. Validar que el padrón sea un número entero entre 1 y 10000; y la nota entre 0 y 10.
# El ingreso finaliza cuando el padrón ingresado sea igual a cero.
# b) Informar mediante un listado, la nota promedio de cada alumno, ordenado por padrón.
# c) Informar que alumnos aprobaron la materia y que porcentaje representan. Para aprobar la materia, es necesario que en la lista de notas, al menos, haya 2 notas
# mayores o iguales a 4. Informar asociado a cada padrón, la nota promedio resultante de sumar sólo los valores mayores ó iguales a 4.
# d) En base al punto anterior, informar un ranking de notas, indicando la nota promedio de aprobación, y la cantidad de alumnos que la obtuvieron; ordenado por la
# cantidad de alumnos. 

alumnos = {}
padron = int(input("Ingrese padron o 0 para finalizar: "))
rango_padron = range(1,10001)
rango_nota = range(0,11)

while padron != 0:
    if padron in rango_padron:
        nota = int(input("Ingrese nota: "))
        if nota in rango_nota:
            if padron not in alumnos:
                alumnos[padron] = [nota]
            else:
                alumnos[padron].append(nota)
        else:
            print("Nota no existente")
    else:
        print("Padron no existente")
    padron = int(input("Ingrese padron o 0 para finalizar: "))
print(alumnos)

promedio_notas = {}
for padron in alumnos:
    promedio_notas[padron] = sum(alumnos[padron]) // len(alumnos[padron])

promedio_a_tupla = promedio_notas.items()
alumnos_ordenado = sorted(promedio_a_tupla, key=lambda x: x[0])

print(alumnos_ordenado)


