# (★) Implementar un algoritmo que, utilizando programación dinámica, obtenga el valor del n-ésimo número de fibonacci. 
# Indicar y justificar la complejidad del algoritmo implementado. Definición:
# n = 0 --> Debe devolver 1
# n = 1 --> Debe devolver 1
# n --> Debe devolver la suma entre los dos anteriores números de fibonacci (los fibonacci n-2 y n-1)

# Para calcular el fibonacci(n) necesitaremos calcular el fibonacci(n - 1) y fibonacci(n - 2) por lo que la ecuacion de
# recurrencia sera la siguiente:
# dp[n] = dp[n-1] + dp[n-2]
# con caso base dp[0] = 1 y dp[1] = 1

def fibonacci(n):
    if n == 1 or n == 0:
        return 1
    dp = [0] * (n+1)
    dp[0] = 1
    dp[1] = 1
    for i in range(2, n+1):
        dp[i] = dp[i-1] + dp[i-2]
    return dp[n]

# En este algoritmo se itera sobre todos los valores hasta llegar a n, por lo que la complejidad sera O(n)


# (★★★) Dada un aula/sala donde se pueden dar charlas. Las charlas tienen horario de inicio y fin. Además, cada 
# charla tiene asociado un valor de ganancia. Implementar un algoritmo que, utilizando programación dinámica, 
# reciba un arreglo que en cada posición tenga una charla representada por una tripla de inicio, fin y valor de cada 
# charla, e indique cuáles son las charlas a dar para maximizar la ganancia total obtenida. Indicar y justificar 
# la complejidad del algoritmo implementado.

# Para este problema de schudeling ponderado tendremos que elegir un conjunto de charlas no solapadas en donde
# la ganancia sea maxima. Para realizarlo tendremos dos opciones en base a cada charla, podemos incluir la charla al
# conjunto y sumar su ganancia con la mejor ganancia hasta la anterior charla compatible, o la omitimos y nos 
# quedamos con la solucion optima hasta la charla anterior.
# Para plantear la ecuacion de recurrencia, primero ordenamos las charlas por horario de fin y luego planteamos:
# dp[i] es la maxima ganancia considerando hasta la i charla con lo que nuestra ecuacion de recurrencia quedara como:
# dp[i] = max(dp[i-1], ganancia[i] + dp[p(i)]) donde p(i) es el indice anterior a i donde la charla no se solapa con la charla i
# con caso base dp[0] = 0

def calcular_p(charlas):
    n = len(charlas)
    p = [0] * n
    for i in range(n):
        inicio_i = charlas[i][0]
        izq = 0
        der = i - 1
        res = -1
        while izq <= der:
            medio = (izq + der) // 2
            if charlas[medio][1] <= inicio_i:
                res = medio
                izq = medio + 1
            else:
                der = medio - 1
        p[i] = res
    return p

def schudeling_ponderado(charlas):
    charlas_ordenadas = sorted(charlas, key=lambda x: x[1])
    p = calcular_p(charlas_ordenadas)
    n = len(charlas)
    dp = [0] * (n+1)
    for i in range(1, n+1):
        valor = charlas_ordenadas[i-1][2]
        dp[i] = max(dp[i-1], valor + dp[p[i-1]] + 1)
    return dp

def reconstruccion_schudeling_ponderado(charlas):
    charlas_ordenadas = sorted(charlas, key=lambda x: x[1])
    dp = schudeling_ponderado(charlas_ordenadas)
    p = calcular_p(charlas_ordenadas)
    i = len(charlas)
    seleccionadas = []
    while i > 0:
        if dp[i] == dp[i-1]:
            i -= 1
        else: 
            seleccionadas.append(charlas_ordenadas[i-1])
            i = p[i-1]
    seleccionadas.reverse()
    return seleccionadas, dp[len(charlas)]

charlas = [
    (1, 3, 5),
    (2, 5, 6),
    (4, 6, 5),
    (6, 7, 4),
    (5, 8, 11),
    (7, 9, 2)
]

seleccionadas, maxima_ganancia = reconstruccion_schudeling_ponderado(charlas)
print("Máxima ganancia:", maxima_ganancia)
print("Charlas seleccionadas:")
for charla in seleccionadas:
    print(charla)