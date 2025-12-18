# Recorriendo una sola vez el archivo de resultados y sin cargarlo completamente en
# memoria, haga un corte por día, indicando: día, cantidad de partidos jugados, cantidad de
# set jugados.

MAX_DIA = 15

def leer_archivo(archivo):
    linea = archivo.readline()
    if linea:
        devolver = linea.rstrip("\n").split(",")
    else:
        devolver = MAX_DIA,"","","",""
    return devolver

def tenis(archivo):
    dia,jugador1,set1,jugador2,set2=leer_archivo(archivo)

    print("DIA  -  PARTIDOS  -  SET")
    while int(dia) < MAX_DIA:
        cant_partidos = 0
        cant_set = 0
        dia_anterior = dia

        while dia and dia_anterior == dia:
            cant_partidos += 1

            sets = set1.split("-")
            cant_set += len(sets)

            dia,jugador1,set1,jugador2,set2=leer_archivo(archivo)

        print(dia_anterior , "   -    " , cant_partidos , "     -  ", cant_set)


def dicc_set_ganados(archivo):
    dicc = {}
    dia,jugador1,set1,jugador2,set2=leer_archivo(archivo)

    while int(dia) < MAX_DIA:
        sets_j_1 = set1.split("-")
        sets_j_2 = set2.split("-")
        set_ganados_1 = 0
        set_ganados_2 = 0
        for juego in range(len(sets_j_1)):
            if sets_j_1[juego] > sets_j_2[juego]:
                set_ganados_1 += 1
            else:
                set_ganados_2 += 1
        if set_ganados_1 > set_ganados_2:
            if jugador1 not in dicc:
                dicc[jugador1] = 1
            else:
                dicc[jugador1] += 1
        else:
            if jugador2 not in dicc:
                dicc[jugador2] = 1
            else:
                dicc[jugador2] += 1

        dia,jugador1,set1,jugador2,set2=leer_archivo(archivo)

    dicc_items = dicc.items()
    dicc_ordenado = sorted(dicc_items, key=lambda x: x[1], reverse=True)
    for i in dicc_ordenado:
        print(i[0], " - ", i[1])

def main():
    puntos_tenis = open('resultados_tenis.csv', 'r')
    tenis(puntos_tenis)
    puntos_tenis.close()

    puntos_tenis = open('resultados_tenis.csv', 'r')
    dicc_set_ganados(puntos_tenis)
    puntos_tenis.close()


main()