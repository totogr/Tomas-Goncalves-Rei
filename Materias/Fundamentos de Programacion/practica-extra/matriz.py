matriz = [
    [1,2,3],
    [2,1,4],
    [3,4,1]
]

i = 0
j = 1
simetrica = True
while i<len(matriz) and simetrica:
    while (i+j)<len(matriz) and simetrica:
        if matriz[i][i+j] != matriz[i+j][i]:
            simetrica = False
        j += 1
    j = 0
    i += 1

print(simetrica)