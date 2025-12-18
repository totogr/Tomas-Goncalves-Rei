matriz = [
    [5,0,0,0],
    [0,4,0,0],
    [0,0,5,1],
    [0,0,0,5]
]

def unos_diagonal(matriz):
    i = 0
    unos = True
    while i < len(matriz) and unos:
        if matriz[i][i] != matriz[0][0]:
                unos = False
        i += 1
    return unos

print(unos_diagonal(matriz))