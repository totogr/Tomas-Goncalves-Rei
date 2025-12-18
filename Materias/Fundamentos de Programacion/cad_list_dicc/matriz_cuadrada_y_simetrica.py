matriz = [
    [4,4,7],
    [4,9,8],
    [7,8,0]
]

def cuadrada(matriz):
    i = 0
    cuadrada = True
    while i < len(matriz) and cuadrada:
        if len(matriz) != len(matriz[i]):
            cuadrada = False
        i += 1
    return cuadrada

print(cuadrada(matriz))

def simetrica(matriz):
    i = 0
    j = 1
    simetrica = True
    while i < len(matriz) and simetrica:
        while j < len(matriz[i]) and simetrica:
            if matriz[i][j] != matriz[j][i]:
                simetrica = False
            j += 1
        j = 1
        i += 1
    return simetrica

print(simetrica(matriz))
