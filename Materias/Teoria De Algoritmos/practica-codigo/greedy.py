from grafo import Grafo

# (★) Explicar por qué el Algoritmo de Kruskal (para obtener el MST 
# de un grafo no dirigido) es un Algoritmo Greedy.

# Idea del algoritmo:
# Kruskal construye el Árbol de Expansión Mínima (MST) agregando aristas de menor peso una a una, 
# siempre que no formen ciclos.

# Este algoritmo es Greedy ya que en cada paso elige la arista mas liviana sin considerar si en
# un futuro tendria que haberla utilizado o no. Una vez que eligio una arista, no deshace la eleccion
# buscando asi una solucion optima local para cada caso, buscando llegar a una solucion
# optima global.


# (★) Explicar por qué el Algoritmo de Prim (para obtener el MST 
# de un grafo no dirigido) es un Algoritmo Greedy.

# Idea del algoritmo:
# Prim también construye el MST, pero comienza en un vértice y va agregando el vértice más cercano al 
# árbol en expansión, considerando sólo las aristas que conectan el árbol con vértices aún no visitados.

# Este algoritmo es Greedy ya que en cada paso, elige la arista de menor peso que conecta el arbol 
# actual con un nuevo vertice. En cada decision se busca el optimo local sin retroceder ni analizar otra
# ruta. Se confia que con la construccion paso a paso se llegara a un optimo global.


# (★) Explicar por qué el Algoritmo de Dijkstra (para obtener caminos mínimos desde un vértice, 
# en un grafo con pesos positivos) es un Algoritmo Greedy.

# Idea del algoritmo:
# Dijkstra calcula el camino más corto desde un vértice fuente a todos los demás vértices en un grafo con 
# pesos positivos. Siempre elige el vértice más cercano que aún no haya sido visitado, y actualiza las distancias.

# Este algoritmo es Greedy a que en cada paso se elige el vertice con menor distancia estimada
# y lo considera resuelto, una vez que lo elige ya no se actualiza. Se basa en una decision local optima, osea la menor
# distancia actual, con la esperanza de llegar a caminos globalmente minimos.


# (★★) Dada un aula/sala donde se pueden dar charlas. Las charlas tienen horario de inicio y fin. Implementar 
# un algoritmo Greedy que reciba el arreglo de los horarios de las charlas, representando en tuplas los horarios de 
# inicios de las charlas, y sus horarios de fin, e indique cuáles son las charlas a dar para maximizar la cantidad total de 
# charlas. Indicar y justificar la complejidad del algoritmo implementado.

# Para este ejercicio de seleccion de charlas vamos a plantear el siguiente pensamiento Greedy:
# Ordenamos las charlas por su horario de finalizacion e iteramos sobre las mismas. Eligiremos las charlas que 
# su horario de inicio sea mayor o igual al horario de fin de la anterior seleccionada.

def seleccion_de_charlas(charlas):
    charlas_ordenadas = sorted(charlas, key=lambda x: x[1])
    seleccionadas = []
    fin_ultima = 0
    for inicio, fin in charlas_ordenadas:
        if inicio >= fin_ultima:
            seleccionadas.append((inicio,fin))
            fin_ultima = fin
    return seleccionadas

# La complejidad de este algoritmo es la siguiente:
# - Ordenar las charlas O(n log n)
# - Iterar sobre las charlas O(n)
# Complejidad total: O(n log n)

# Es Greedy ya que tomamos una decision local optima como agarrar la charla que termina antes y no volvemos a considerar 
# la eleccion. Con esto buscamos maximizar la cantidad de charlas y tener una solucion global optima.


# (★) Se tiene un sistema monetario (ejemplo, el nuestro). Se quiere dar “cambio” de una determinada cantidad de plata. 
# Implementar un algoritmo Greedy que devuelva el cambio pedido, usando la mínima cantidad de monedas/billetes.
# El algoritmo recibirá un arreglo de valores del sistema monetario, y la cantidad de cambio objetivo a dar, y debe 
# devolver qué monedas/billetes deben ser utilizados para minimizar la cantidad total utilizda. Indicar y justificar 
# la complejidad del algoritmo implementado. ¿El algoritmo implementado encuentra siempre la solución óptima? Justificar 
# si es óptimo, o dar un contraejemplo. ¿Por qué se trata de un algoritmo Greedy? Justificar

# Para este ejercicio del problema del cambio plantearemos el siguiente pensamiento Greedy:
# Ordenamos el arreglo de valores de mayor a menor e iteraremos sobre el. Si el valor de la moneda es menor o igual
# al valor del cambio a dar, lo seleccionamos sino continuamos con el otro. Si ya llegamos al cambio total a dar, cortamos.

def cambio(valores, monto):
    valores.sort(reverse=True)
    seleccionados = []
    for i in valores:
        while monto >= i:
            seleccionados.append(i)
            monto -= i
    return seleccionados

# la complejidad de este algoritmo es la siguiente:
# - Ordenar los valores es O(n log n) con n cantidad de valores
# - En el peor caso itera sobre todas las monedas por lo que es O(n)
# - En el loop interno del while, en el peor caso son todas monedas de valor 1 por lo que se itera m veces con m cantidad de monto
# Complejidad total: O(n log n + m)

# Este algoritmo es Greedy ya que en cada paso toma una decision optima local, usar la moneda mas grande 
# posible sin pasarse del monto y no considera modificar esto despues.

# Este algoritmo igualmente no siempre encuentra una solucion optima y lo demostraremos con un contraejemplo:
# valores = [1,3,4]
# monto = 6
# El algoritmo nos dara como resultado [4,1,1] cuando el optimo seria [3,3] por lo que el algoritmo no siempre es optimo.


# (★★) Tenemos unos productos dados por un arreglo R, donde R[i] nos dice el precio del producto. Cada día podemos y debemos 
# comprar uno (y sólo uno) de los productos, pero vivimos en una era de inflación y los precios aumentan todo el tiempo. El 
# precio del producto i el día j es R[i] j+1 (j comenzando en 0). Implementar un algoritmo greedy que nos indique el precio
# mínimo al que podemos comprar todos los productos. Indicar y justificar la complejidad del algoritmo implementado. 
# ¿El algoritmo implementado encuentra siempre la solución óptima? Justificar. ¿Por qué se trata de un algoritmo Greedy? 
# Justificar ¿Qué modificaciones se deben realizar para un estado de deflación, con productos que bajan de precio todo el tiempo?

# Para este ejercicio donde debemos asignar productos a dias de forma que minimizemos el costo total, por lo que plantearemos
# el siguiente pensamiento Greedy:
# Ordenamos el precio de los productos R[i] de mayor a menor, asi primero compramos los productos de mayor valor y luego los de menor
# valor. Como el paso de los dias hace que el precio aumente exponencialmente, para minimizar el costo total, los valores mas grandes
# deben tener un exponencial mas chico y los valores mas chicos puede tener un exponencial mas grande ya que su potencia crece menos.

def inflacion(R):
    R.sort(reverse=True)
    costo_total = 0
    j = 0
    for valor in R:
        costo_total += (valor ** (j + 1))
        j += 1
    return costo_total

# La complejidad de este algoritmo es la siguiente:
# - Ordenar precios es O(n log n)
# - Iterar sobre los valores es O(n)
# Complejidad total: O(n log n)

# Este algoritmo es Greedy ya que toma una decision local en cada paso, asigna dias temprano a productos mas caros sin reconsiderar
# otra decision y asume que con ella se llegara a una solucion optima global.
# Este algoritmo es optimo siempre ya que se compra primero los productos mas caros minimizando el efecto exponencial. Si se intercambia 
# un producto caro en un dia mas tarde y uno barato un dia mas temprano, el costo total aumentara, por lo que la solucion es optima.


# (★★) Tenemos una mochila con una capacidad W. Hay elementos a guardar, cada uno tiene un valor, y un peso que ocupa de la 
# capacidad total. Queremos maximizar el valor de lo que llevamos sin exceder la capacidad. Implementar un algoritmo Greedy que, 
# reciba dos arreglos de valores y pesos de los elementos, y devuelva qué elementos deben ser guardados para maximizar la ganancia 
# total. Indicar y justificar la complejidad del algoritmo implementado. ¿El algoritmo implementado encuentra siempre la solución óptima?
# Justificar. ¿Por qué se trata de un algoritmo Greedy? Justificar

# Para este problema de la Mochila plantearemos el siguiente pensamiento Greedy:
# Ordenaremos los elementos en base a la diferencia valor/peso, de mayor a menor. Con esto iteraremos sobre estos producto y los iremos 
# guardando en al mochila mientras que no excedan la capacidad total. Con esto llenaremos la mochila y maximizaremos el valor total.

def mochila(W, valores, pesos):
    productos = []
    for i in range(len(valores) - 1):
        productos.append((valores[i], pesos[i], valores[i]/pesos[i]))
    productos.sort(key=lambda x: x[2], reverse=True)
    seleccionados = []
    valor_total = 0
    for v, p, vp in productos:
        if W == 0:
            break
        if p <= W:
            W -= p
            valor_total += v
            seleccionados.append((v,p))
    return valor_total, seleccionados

# La complejidad de este algoritmo es la siguiente:
# - Recorrer los productos para juntarlos y calcular su valor/peso es O(n)
# - Ordenar los productos es O(n log n)
# - Iteramos nuevamente sobre los productos que es O(n)
# Complejidad total: O(n log n)

# Este algoritmo es Greedy ya que toma una decision local optima, agregar el objeto con mayor valor/peso a la mochila, y no lo actualiza
# sin importar si pudo haber convenido otro o no. Y espera que la suma de las elecciones de como resultado un optimo global.
# Este algoritmo igualmente no siempre encuentra una solucion optima y lo demostraremos con un contraejemplo:
# valores = [60, 100, 120]
# pesos = [10, 20, 30]
# W = 50
# En este caso se elegirian los productos ([60,10],[100,20]) dando como valor maximo 160. Cuando si eligieramos los productos 
# ([100,20],[120,30]) aumentariamos el valor maximo a 220, por lo que no siempre es optimo. Para optimizar esto podriamos 
# fraccionar los productos para solamente agregar una parte de ellos, en ese caso si llegariamos a un optimo ya que siempre completariamos
# la capacidad total de la mochila y tendriamos los productos con mayor diferencia valor/peso por lo que nos daria el valor maximo posible.


# (★★) Tenemos tareas con una duración y un deadline (fecha límite), pero pueden hacerse en cualquier momento, intentando que se hagan 
# antes del deadline. Una tarea puede completarse luego de su deadline, pero ello tendra una penalización de latencia. Para este problema, 
# buscamos minimizar la latencia máxima en el que las tareas se ejecuten. Es decir, dados los arreglos de: T tiempo de duraciones de las tareas y 
# D representando al deadline de cada tarea, si definimos que una tarea i empieza en Si, entonces termina en Fi = Si+Ti, y su latencia es Li = Fi−Di
# (si Fi > Di , sino 0). Nuestra latencia máxima será aquella i que maximice el valor Li. Implementar un algoritmo que defina en qué orden deben realizarse 
# las tareas, sabiendo que al terminar una tarea se puede empezar la siguiente. Indicar y justificar la complejidad del algoritmo implementado. 
# Devolver un arreglo de tuplas, una tupla por tarea, en el orden en que deben ser realizadas, y que cada tupla indique: (el tiempo Ti de la tarea i, 
# y la latencia resultante Li de esa tarea). ¿El algoritmo implementado encuentra siempre la solución óptima? Justificar. ¿Por qué se trata de un 
# algoritmo Greedy? Justificar

# Para este problema de minimizacion de latencia maxima plantearemos el siguiente pensamiento Greedy: 
# Ordenaremos las tareas de menor a mayor deadline, de tal modo que las que tienen menor deadline pueden ejecutarse primero y las que tardan mas despues,
# asi ejecutan primero las que vencen antes. Con esto iteraremos sobre las tareas y se devolvera el arreglo de tuplas en donde tendremos 
# el tiempo de la tarea i y la latencia calculada que tuvo.

def latencia(T, D):
    tareas = []
    for i in range(len(T)):
        tareas.append((T[i], D[i]))
    tareas.sort(key=lambda x: x[1])
    
    seleccionadas = []
    tiempo_actual = 0
    lat_maxima = 0
    for duracion, deadline in tareas:
        inicio = tiempo_actual
        fin = inicio + duracion
        latencia = max(0, fin - deadline)
        seleccionadas.append((duracion, latencia))
        tiempo_actual = fin
        lat_maxima = max(lat_maxima, latencia)
    return seleccionadas, lat_maxima

# La complejidad de este algoritmo es la siguiente:
# - Agregar la duracion y deadline al arreglo de "tareas" es O(n) con n cantidad de tareas
# - Ordenar las tareas por deadline es O(n log n)
# - Iteramos sobre tareas y realizamos operacion por lo que tambien es O(n)
# Complejidad total: O(n log n)

# Este algoritmo es Greedy ya que en cada paso agrega la tarea con el deadline mas bajo sin actualizarla o cambiarla despues, esperando que se
# llegue al optimo global donde se minimice la latencia maxima.
# Es optimo ya que al ordenar por deadline, lo que hacemos es ejecutar las tareas que son mas urgentes primero, ya que si las hacemos mas tarde
# acumularan mas latencia. Por lo que siempre que vayamos ejecutando las tareas de menor deadline, minimizaremos la latencia maxima.


# (★) Una ruta tiene un conjunto de bifurcaciones para acceder a diferentes pueblos. El listado (ordenado por nombre del pueblo) contiene 
# el número de kilómetro donde está ubicada cada una. Se desea ubicar la menor cantidad de patrullas policiales (en las bifurcaciones) de 
# tal forma que no haya bifurcaciones con vigilancia a más de 50 km. Justificar que la solución es óptima. Indicar y justificar la complejidad
# del algoritmo implementado. Ejemplo:
# Ciudad	    Bifurcación
# Castelli	    185
# Gral Guido	242
# Lezama	    156
# Maipú	        270
# Sevigne	    194

# Si pongo un patrullero en la bifurcación de Lezama, cubro Castelli y Sevigne. Pero no Gral Guido y Maipú. Necesitaría en ese caso, poner otro. 
# Agrego otro patrullero en Gral Guido. Con eso tengo 2 móviles policiales en bifurcaciones que cubren todas los accesos a todas las ciudades con 
# distancia menor a 50km.
# En un caso alternativo donde solamente se consideren las bifurcaciones de Castelli, Gral Guido y Sevigne, la
# única solución óptima sería colocar un móvil policial en Sevigne.

# Para este problema de cobertura minima de intervalos plantearemos el siguiente pensamiento Greedy:
# Ordenamos las bifurcaciones de menor a mayor para ir recorriendolas, luego iteramos las mismas, en cada paso colocamos un patrullero en la bifurcacion
# mas a la derecha posible asegurandonos que tambien cubra a la actual. Con esto podremos aprovechar al maximo el rango que cubre cada patrulla ya que
# cubriremos la bifurcacion actual y 50km mas adelante, osea la mayor cantidad de bifurcaciones futuras. Y luego seguimos iterando de la misma forma
# salteando todas las bifurcaciones cubiertas por ese patrullero colocado.

def cobertura_de_intervalos(bifurcaciones):
    bifurcaciones.sort()
    patrulleros = []
    n = len(bifurcaciones)
    i = 0

    while i < n:
        actual = bifurcaciones[i]
        j = i
        while j < n and bifurcaciones[j] <= actual + 50:
            j += 1
        patrullero = bifurcaciones[j - 1]
        patrulleros.append(patrullero)
        i = j
        while i < n and bifurcaciones[i] <= patrullero + 50:
            i += 1

    return patrulleros

# La complejidad de este algoritmo es la siguiente:
# - Ordenamos las bifurcaciones que es O(n log n)
# - Recorremos la lista de bifurcaciones que es O(n)
# Complejidad total: O(n log n)

# Este algoritmo es Greedy ya que en cada paso busca un optimo local al colocar el patrullero lo mas a la derecha posible, intentando contener la bifurcacion
# actual con la que estamos trabajando, con esto se aprovecha al maximo la distancia que cubre cada patrullero y se espera que se llegue a una 
# solucion optima global cubriendo todas las bifurcaciones con la cantidad minima de patrullas.
# Este algoritmo es optimo porque al elegir el punto mas a la derecha posible, sabemos que aun cubre la bifurcacion actual en la que estamos trabajando. 
# Con esto maximizamos la cantidad de bifurcaciones cubiertas por cada patrullero, ya que si lo colocabamos en alguna anterior, cubriamos menos kilometros
# hacia la derecha y si lo colocabamos mas adelante, no cubriria la actual. Con esto cumplimos con la decision de un optimo local para llegar a un 
# optimo global por lo que valida la optimalidad.


# (★★) Las bolsas de un supermercado se cobran por separado y soportan hasta un peso máximo P, por encima del cual se rompen. Implementar un algoritmo 
#  greedy que, teniendo una lista de pesos de n productos comprados, encuentre la mejor forma de distribuir los productos en la menor cantidad posible 
#  de bolsas. Realizar el seguimiento del algoritmo propuesto para bolsas con peso máximo 5 y para una lista con los pesos: [ 4, 2, 1, 3, 5 ]. 
#  ¿El algoritmo implementado encuentra siempre la solución óptima? Justificar. Indicar y justificar la complejidad del algoritmo implementado.

# Para este problema de Bin Packing plantearemos el siguiente pensamiento Greedy:
# Ordenaremos los productos por su peso de mayor a menor, e iremos guardando cada producto en una bolsa, si el producto no entra abriremos una nueva
# bolsa y seguiremos guardando hasta completar todos los productos.

def bin_packing(capacidad, pesos):
    pesos.sort(reverse=True)
    distribucion = []
    for p in pesos:
        colocado = False
        for bolsa in distribucion:
            if sum(bolsa) + p <= capacidad:
                bolsa.append(p)
                colocado = True
                break
        if not colocado:
            distribucion.append([p])
    return distribucion

# La complejidad de este algoritmo es la siguiente:
# - Ordenamos los pesos es O(n log n)
# - Iteramos sobre los pesos es O(n)
# - Luego tenemos una iteracion interior donde, en el peor caso, recorremos todas las bolsas, que es O(n)
# Complejidad total: O(n2) debido al doble loop

# Este algoritmo es Greedy ya que en cada paso se busca un optimo local donde o se guarda el objeto en la bolsa, o en caso de no entrar
# se abre otra para guardarlo, sin reconsiderar los elementos ya puestos. Estas decisiones tomadas buscan poder llegar al optimo global
# donde se utilice la minima contidad de bolsas.
# Este algoritmo igualmente no siempre encuentra una solucion optima y lo demostraremos con un contraejemplo:
# Tenemos los siguientes pesos: [44, 24, 24, 22, 21, 17, 8, 8, 6, 6] y la capacidad de cada bolsa es 60, por lo que tendremos:
# 44, 8, 8
# 24, 24, 6, 6
# 22, 21, 17
# Este resultado es optimo con 3 bolsas, pero si la capacidad fuera 61 pasaria lo siguiente:
# 44, 17
# 24, 24, 8
# 22, 21, 8, 6
# 6
# Con lo que ahora se modificaria el resultado utilizando 4 bolsas, lo cual no es lo optimo y pudimos comprobarlo.


# (★★) Trabajamos para el mafioso Arnook, que es quien tiene la máxima influencia y poder en la zona costera de Ciudad República. Allí reina
# el caos y la delincuencia, a tal punto que quien termina organizando las pequeñas mafias locales no es otro sino Arnook. En particular, 
# nos vamos a centrar en unos pedidos que recibe de parte de dichos grupos por el control de diferentes kilómetros de la ruta costera. Cada 
# pequeña mafia le pide a Arnook control sobre un rango de kilómetros (por ejemplo, la mafia nro 1 le pide del kilómetro 1 al 3.5, la mafia 
# 2 le pide del 3.3333 al 8, etc. . . ). Si hay una mafia tomando control de algún determinado kilómetro, no puede haber otra haciendo lo 
# mismo (es decir, no pueden solaparse). Cada mafia pide por un rango específico. Arnook no cobra por kilómetraje sino por “otorgar el permiso”,
# indistintamente de los kilómetros pedidos. Ahora bien, esto es una mafia, no una ONG, y no debe rendir cuentas con nadie, así que lo único 
# que es de interés es maximizar la cantidad de permisos otorgados (asegurándose de no otorgarle algún lugar a dos mafias diferentes). 
# Implementar un algoritmo Greedy que reciba los rangos de kilómetros pedidos por cada mafia, y determine a cuáles se les otorgará control, 
# de forma que no hayan dos mafias ocupando mismo territorio, y a su vez maximizando la cantidad de pedidos otorgados. Indicar y justificar
# la complejidad del algoritmo implementado. Justificar por qué el algoritmo planteado es Greedy. ¿El algoritmo da la solución óptima siempre?

# Este problema es una version clasica del problema de interval schudeling, donde tendremos una lista de pedidos con un inicio y fin, y queremos
# maximizar la cantidad de pedidos posibles sin que se solapen ninguno de ellos. La idea sera ordenar todos estos pedidos por tiempo de finalizacion,
# para luego iterar sobre los mismos, si no se solapa con el anterior elegido lo aceptamos y sino lo salteamos, hasta terminar la lista.

def mafias_optima(pedidos):
    pedidos_ordenados = sorted(pedidos, key=lambda x: x[1])
    seleccionados = []
    fin_ultima = 0
    for inicio, fin in pedidos_ordenados:
        if inicio >= fin_ultima:
            seleccionados.append((inicio, fin))
            fin_ultima = fin
    return seleccionados

# La complejidad de este algoritmo es la siguiente:
# - Ordenar los pedidos O(n log n)
# - Iterar sobre los pedidos O(n)
# Complejidad total: O(n log n) con n cantidad de pedidos

# Este algoritmo es Greedy ya que en cada paso verificamos que la charla actual sea optima y la agregamos, sin que en un futuro se pueda
# cambiar la eleccion de alguna forma, buscando que al hacer esto con todas los pedidos se llegue a la solucion optima global.
# Este mismo tambien es optimo, ya que nos basamos en el algoritmo de scheduling, donde al ordenar por fin de cada pedido y pidiendo 
# que no se solapen, maximiza la cantidad de pedidos seleccionados.


# (★★) Tenemos una ruta recta muy larga, de K kilómetros, sobre la cual hay casas dispersas. En dichas casas vive gente que usa mucho sus 
# celulares. El intendente a cargo la ruta debe renovar por completo el sistema de antenas, teniendo que construir sobre la ruta nuevas antenas. 
# Cada antena tiene un rango de cobertura de R kilómetros (valor constante conocido). Implementar un algoritmo Greedy que reciba las 
# ubicaciones de las casas, en número de kilómetro sobre esta ruta (números reales positivos) desordenadas, y devuelva los kilómetros sobre los 
# que debemos construir las antenas para que todas las casas tengan cobertura, y se construya para esto la menor cantidad de antenas posibles. 
# Indicar y justificar la complejidad del algoritmo implementado. Justificar por qué se trata de un algoritmo greedy. ¿El algoritmo da la solución 
# óptima siempre?

# Este es un problema clasico de cobertura de intervalos, donde tendremos una lista con los kilometros donde hay casas y un valor R que es la
# cantidad de kilometros que cubre cada antena. Iteraremos sobre las casas buscando la forma de colocar la menor cantidad posible de antenas
# pero que todas las casas queden cubiertas.

def ubicar_antenas(casas, R, K):
    casas.sort()
    n = len(casas)
    i = 0
    antenas = []
    while i < n:
        inicio = casas[i]
        pos_antena = min(inicio + R, K)
        antenas.append(pos_antena)
        i += 1
        while i < n and pos_antena + R >= casas[i]:
            i += 1
    return antenas

# La complejidad de este algoritmo es la siguiente:
# - Ordenar las casas O(n log n)
# - Recorremos las casas pasando solo una vez por cada una O(n)
# Complejidad total: O(n log n) con n cantidad de casas

# Este algoritmo es Greedy, ya que tomamos siempre la mejor solucion local posible, colocamos la antena en el km mas a la derecha posible
# tal que cubra la casa actual que estamos revisando, haciendo que cubra la cantidad maxima de km posibles, y nunca reconsideramos una eleccion
# o retrocedemos para cambiarla, buscando asi una solucion optima global en donde se cubran todas las casas minimizando la cantidad de antenas.
# Este mismo es optimo, ya que esta demostrado que para problemas de minima cobertura de puntos en una recta con intervalos de longitud fija,
# el algoritmo resuelve de una manera optima ya que aprovecha al maximo la cobertura de las antenas, utilizando la minima cantidad posible.


# (★★) Se tiene una matriz donde en cada celda hay submarinos, o no, y se quiere poner faros para iluminarlos a todos. Implementar un algoritmo 
# Greedy que dé la cantidad mínima de faros que se necesitan para que todos los submarinos queden iluminados, siendo que cada faro ilumina su 
# celda y además todas las adyacentes (incluyendo las diagonales), y las directamente adyacentes a estas (es decir, un “radio de 2 celdas”). 
# Indicar y justificar la complejidad del algoritmo implementado. ¿El algoritmo implementado da siempre la solución óptima? Justificar

# Para este ejercicio recorreremos la matriz buscando donde hay submarinos no cubiertos, cuando encontremos uno colocaremos un faro en la posicion
# que ilumine la mayor cantidad de submarinos posibles. Luego, marcaremos como cubiertas esas celdas iluminadas y lo repetiremos hasta cubrir 
# todos los submarinos. El pensamiento para colocar los faros sera, para cada submarino no cubierto se considerara colocar el faro en el lugar
# del submarino o en una celda dentro del rango 2 del mismo, evaluando cuantos submarinos no cubiertos se cubririan desde ahi, y por
# ultimo colocar el faro en la posicion que cubra mas submarinos no cubiertos.

def quedan_submarinos(matriz, cubierto):
    for i in range(len(matriz)):
        for j in range(len(matriz[0])):
            if matriz[i][j] == True and cubierto[i][j] == 0:
                return True
    return False

def submarinos_iluminados(matriz):
    if not matriz or not matriz[0]:
        return []
    filas = len(matriz)
    col = len(matriz[0])
    cubierto = []
    for x in range(filas):
        cubierto.append([0] * col)
    faros = []
    desplazamientos = []
    for dx in range(-2,3):
        for dy in range(-2,3):
            desplazamientos.append((dx, dy))
    while quedan_submarinos(matriz, cubierto):
        # Mejor pos para poner el faro
        mejor_x = -1
        mejor_y = -1
        mejor_cantidad_submarinos = 0
        # Recorremos la matriz
        for i in range(filas):
            for j in range(col):
                cantidad = 0
                for dx, dy in desplazamientos:
                    x = i + dx
                    y = j + dy
                    if 0 <= x < filas and 0 <= y < col:
                        if matriz[x][y] == True and cubierto[x][y] == 0:
                            cantidad += 1
                if cantidad > mejor_cantidad_submarinos:
                    mejor_cantidad_submarinos = cantidad
                    mejor_x = i
                    mejor_y = j
        if mejor_cantidad_submarinos == 0:
            break
        faros.append((mejor_x, mejor_y))
        for dx, dy in desplazamientos:
            x = mejor_x + dx
            y = mejor_y + dy
            if 0 <= x < filas and 0 <= y < col:
                cubierto[x][y] = 1
    return faros

# La complejidad de este algoritmo es la siguiente:
# - Por cada faro que se coloca, se recorre toda la matriz que es O(n * m)
# - Para cada celda se revisan otras 25 cercanas, lo cual es constante O(1)
# - Si hay k submarinos, en el peor caso se ponen k faros osea que es O(k * n * m)
# Complejidad total: O(k*n*m) con k cantidad de faros, n filas y m columnas.

# Este algoritmo es Greedy ya que se van tomando decisiones locales optimas al querer poner cada faro en la posicion que cubra la mayor
# cantidad de submarinos no cubiertos posibles, sin retroceder o cambiar esta eleccion y buscando minimizar la cantidad de faros puestos.
# Este algoritmo no siempre es optimo, y lo mostraremos con un contraejemplo:
# Hay veces que al colocar el faro donde cubra menos, puede permitirnos cubrir mas submarinos en total con menos cantidad de faros mas tarde.
# Por ejemplo en esta matriz:
# 1 0 0 0 1
# 0 0 0 0 0
# 0 0 1 0 0
# 0 0 0 0 0
# 1 0 0 0 1
# Si se coloca un faro en el medio de la matriz, ya cubririamos todos los submarinos con 1 solo faro. Pero el algoritmo empezara desde la esquina
# izquierda arriba a colocar faros, por lo que utilizara 3 o mas faros.


# (★★) Se tiene una colección de n libros con diferentes espesores, que pueden estar entre 1 y n (valores no necesariamente enteros). Tu objetivo 
#  es guardar esos libros en la menor cantidad de cajas. Todas las cajas disponibles son de la misma capacidad L (se asegura que L≥n). Obviamente, 
#  no podés partir un libro para que vaya en múltiples cajas, pero sí podés poner múltiples libros en una misma caja, siempre y cuando los 
#  espesores no superen esa capacidad L. Implementar un algoritmo Greedy que obtenga las cajas, tal que se minimicen la cantidad de cajas a 
#  utilizar. Indicar y justificar la complejidad del algoritmo implementado. Justificar por qué se trata de un algoritmo greedy. ¿El algoritmo 
#  propuesto encuentra siempre la solución óptima? Justificar. ¿Qué cambios aplicarías si supieras que los espesores sólo fueran números enteros 
#  de un rango acotado? Describir cómo afecta a la complejidad, y a su optimalidad.

# Para este problema conocido de Bin Packing, donde tendremos cajas con una capacidad L y libros con diferentes espesores los cuales seras su
# capacidad. Queremos guardar todos los libros en las cajas, buscando minimizar la cantidad de cajas utilizada, para esto ordenaremos los 
# libros a partir de sus espesores, del mas grande al mas chico, e iremos guardando en las cajas. Si el libro no entra en la caja, utilizamos otra
# para guardarlo, hasta tener todos los libros guardados.

def bin_packing_libros(capacidad, libros):
    libros.sort(reverse=True)
    cajas = []
    for n in libros:
        guardado = False
        for caja in cajas:
            if sum(caja) + n <= capacidad:
                caja.append(n)
                guardado = True
                break
        if not guardado:
            cajas.append([n])
    return cajas

# La complejidad de este algoritmo es la siguiente:
# - Ordenamos la capacidad de los libros es O(n log n)
# - Iteramos sobre los libros es O(n)
# - Luego tenemos una iteracion interior donde, en el peor caso, recorremos todas las cajas, que es O(n)
# Complejidad total: O(n2) debido al doble loop

# Este algoritmo es Greedy, ya que en cada paso se toma el libro mas grande posible y se lo coloca en la primera caja que entre, sin pensar
# si otra asignacion hubiese sido mejor o volviendolo a sacar.
# Este algoritmo igualmente no siempre encuentra una solucion optima y lo demostraremos con un contraejemplo:
# Tenemos los siguientes libros: [44, 24, 24, 22, 21, 17, 8, 8, 6, 6] y la capacidad de cada caja es 60, por lo que tendremos:
# 44, 8, 8
# 24, 24, 6, 6
# 22, 21, 17
# Este resultado es optimo con 3 cajas, pero si la capacidad fuera 61 pasaria lo siguiente:
# 44, 17
# 24, 24, 8
# 22, 21, 8, 6
# 6
# Con lo que ahora se modificaria el resultado utilizando 4 cajas, lo cual no es lo optimo y pudimos comprobarlo.

# Si los espesores fueran numeros enteros acotados por un rango, podemos pasar de un enfoque Greedy no optimo a uno exacto y optimo, pudiendo
# reducir la complejidad gracias a la restriccion de dominio del problema. Podremos probar muchas combinaciones ya que los valores se encuentran
# acotados o se puede precomputar agrupaciones optimas para rellenar las cajas.


# (★★★) El club de Amigos de Siempre prepara una cena en sus instalaciones en la que desea invitar a la máxima cantidad de sus n socios. Sin 
# embargo por protocolo cada persona invitada debe cumplir un requisito: Sólo puede ser invitada si conoce a al menos otras 4 personas invitadas.
# a. Nos solicitan seleccionar el mayor número posible de invitados. Proponer una estrategia greedy óptima para resolver el problema.
# b. Los organizadores desean que cada invitado pueda conocer nuevas personas. Por lo que nos solicitan que adicionemos una nueva restricción a 
# la invitación: Sólo puede asistir si NO conoce al menos otras 4 personas invitadas. Modifique su propuesta para satisfacer esta nueva solución.

# Para este ejercicio, utilizaremos su representacion en un grafo. La estrategia se basara en que cualquier persona con menos de 4 conocidos totales
# ya no puede cumplir nunca le requisito y, si al sacar a esa persona que no cumplia las condiciones, alguna otra tampoco las cumple, se saca tambien.

def obtener_invitados(conocidos):
    grafo = Grafo()
    for a, b in conocidos:
        if a not in grafo.obtener_vertices():
            grafo.agregar_vertice(a)
        if b not in grafo.obtener_vertices():
            grafo.agregar_vertice(b)
        grafo.agregar_arista(a,b)
    vertices = grafo.obtener_vertices()
    invitados = set()
    for v in vertices:
        invitados.add(v)
    eliminacion = True
    while eliminacion:
        eliminacion = False
        a_eliminar = set()
        for persona in invitados:
            vecinos = grafo.adyacentes(persona)
            conocidos_en_invitados = [v for v in vecinos if v in invitados]
            conocidos_fuera_invitados = [v for v in vecinos if v not in invitados]
            # Condición 1: conoce al menos 4 invitados
            if len(conocidos_en_invitados) < 4:
                a_eliminar.add(persona)
            # Condición 2: no conoce a más de 3 personas fuera invitados
            elif len(conocidos_fuera_invitados) < 4:
                a_eliminar.add(persona)
        if len(a_eliminar) > 0:
            eliminacion = True
            invitados -= a_eliminar
    return list(invitados)

# La complejidad de este algoritmo se puede explicar como:
# - O(m) para agregar vertice y aristas al grafo, con m cantidad de aristas
# - En el bucle de eliminacion, en el peor caso se hacen n iteraciones, una por cada persona eliminada y dentro se examinan sus vecinos,
# por lo que la complejidad es aprox O(n + m)
# Complejidad total: En el peor caso, cada iteracion elimina a una persona por lo que habra O(n) iteraciones, entonces la complejidad final
# sera O(n*(n+m))

# Este algoritmo es Greedy ya que se hacen elecciones locales en cada paso, se elimina a la persona que no cumple la regla, y garantiza un 
# resultado optimo ya que el grupo final, luego de las iteraciones, cumplen la restriccion y es el maximo.  


# (Examen) Dada una expresión representada por una cadena con aperturas y cierres de paréntesis, es sencillo implementar un
# algoritmo que, utilizando una pila, determine si la expresión se encuentra balanceada, o no (esto es un algoritmo sencillo
# de la materia anterior). Por ejemplo, la secuencia ()(), se encuentra balanceada, así como (()) también lo está, pero
# ((), no lo está, ni )()(. Implementar un algoritmo greedy que reciba una cadena y determine el largo del prefijo
# balanceado más largo (es decir, el largo de la subsecuencia balanceada más larga que sí o sí comienza en el inicio de
# la cadena). Indicar y justificar la complejidad del algoritmo. Indicar por qué se trata, en efecto, de un algoritmo greedy.
# El algoritmo, ¿es óptimo? si lo es, justificar brevemente, sino dar un contraejemplo.
# Ejemplo: para ()())(())()((), la respuesta es 4.

def parentesis(arr):
    total = 0
    abiertos = 0
    cerrados = 0

    for i, c in enumerate(arr):
        if c == "(":
            abiertos += 1
        elif c == ")":
            cerrados += 1

        if cerrados > abiertos:
            break

        if cerrados == abiertos:
            total = i + 1

    return total

arr = ["(","(",")","(",")",")","("]
print(parentesis(arr))

# Para este ejercicio planteamos el siguiente pensamiento Greedy, iteramos sobre los elementos del arreglo, si es un
# parentesis abierto sumamos 1 a los mismos y si es cerrado de la misma forma. Si en algun caso tenemos mas cerrados que abiertos
# el arreglo no estara balanceado y saldremos del ciclo. Si tenemos la misma cantidad significa que esta balanceado por lo que
# cargaremos en un valor la posicion donde se encontro el ultimo parentesis que balanceo el arreglo + 1. 
# En esto podemos ver como el algoritmo es Greedy ya que en cada paso se recorre el arreglo y sigue avanzando mientras sea valido, sino corta.
# Tampoco retrocede o busca soluciones anteriores y cada vez que encuentra un prefijo balanceado, actualiza su mejor solucion parcial.
# La complejidad de este algoritmo es lineal ya que en el peor caso se recorren todos los elementos del arreglo, osea que es O(n).
# Este algoritmo es optimo ya que desde el indice 0 se van tomando las cantidades de parentesis, si encuentra mas cerrados que abiertos 
# ya no puede estar mas balanceado por lo que corta y toma el ultimo valor actualizado de la solucion optima global, el cual se 
# va actualizando cuando encuentra un balanceo. Por lo cual es optimo.


# ejercicio: el explorador roberto se embarco en una mision para encontrar un tesoro. en el vasto desierto, hay oasis donde puede recoger 
# provisiones esenciales. cada oasis tiene recursos limitados. sin suficientes provisiones, roberto no podra cruzar el desierto. solo vamos 
# a considerar el hecho de tener k cantidad de provisiones. implementar un algoritmo greedy que permita a roberto llegar al templo con la 
# menor cantidad de paradas posibles en los oasis. datos que se reciben:
# - lista de n elementos que nos indica cantidad de provisiones se puede conseguir en cada n oasis.
# - lista con distancias del inicio de la travesia al primer oasis, del primero al segundo, del segundo al tercero y asi hasta el final de la 
#   travesia (la lista tiene n + 2 elementos)
# - la cantidad de provisiones iniciales de roberto
# - una constante KM_PROVISION que indica cuantas provisiones debe consumir apra caminar un KM

# Para este ejercicio plantearemos el siguiente pensamiento Greedy, roberto recorre los tramos uno a uno, en cada paso guarda las provisiones
# de los oasis anteriores disponibles. Si no puede avanzar, revisa entre esos oasis anteriores cual tenia mas provisiones, lo toma y lo
# suma a su inventario. Esto lo repite hasta que finalice el recorrido o no tenga mas provisiones.

def travesia_roberto(provisiones_oasis, distancia, provisiones_iniciales, KM_PROVISION):
    n = len(provisiones_oasis)
    provisiones = provisiones_iniciales
    paradas = 0
    oasis_disponible = []
    for i in range(n+2):
        consumo = distancia[i] * KM_PROVISION
        provisiones -= consumo
        while provisiones < 0 and oasis_disponible:
            mejor = max(oasis_disponible)
            oasis_disponible.remove(mejor)
            provisiones += mejor
            paradas += 1
        if provisiones < 0:
            return -1
        if i < n:
            oasis_disponible.append(provisiones_oasis[i])
    return paradas

# Complejidad:
# - Se recorren los oasis que es O(n) con n cantidad de oasis
# - Dentro, se recorren los oasis disponibles donde estan las provisiones y se busca el maximo y luego se elimina, lo cual es O(m) con m 
#   los oasis disponibles
# Por lo que la complejidad final sera O(n2)
    
# Este algoritmo es Greedy ya que siempre se elige la mejor opcion disponible en el momento que necesite provisiones. Cuando se queda sin
# provisiones elige el oasis pasado con mayor cantidad, sin reconsiderar otras combinaciones posibles, sino que tomando la decision local
# optima esperando que lo lleve a una solucion optima global.
# Este algoritmo es optimo, ya que solo para en oasis cuando ya no tiene provisiones, y entre todas las posibles paradas usamos la que nos
# de la maxima cantidad de provisiones para minimizar futuras paradas. Con esto hacemos que no se pare mas veces de la que se necesitan
# y que no se elija una parada que nos de pocas provisiones, siempre la que mas nos da.