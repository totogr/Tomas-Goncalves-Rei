MAX_DIA = 70

def leer(archivo):
    linea = archivo.readline()
    if linea:
        devolver = linea.rstrip("\n").split(",")
    else:
        devolver = MAX_DIA,"","","",""
    return devolver

def leer_completo(archivo):
    linea = archivo.readline()
    if linea:
        devolver = linea.rstrip("\n").split(",")
    else:
        devolver = MAX_DIA,"","","","",""
    return devolver

def guardar_partido(archivo,dia,equipo_local,goles_local,equipo_visitante,goles_visitante,copa):
    archivo.write(str(dia) + "," + equipo_local + "," + goles_local + "," + equipo_visitante + "," + goles_visitante + "," + copa + '\n')

def juntar_mundiales(eurocopa, copaAmerica, unir):
    MAX_DIA = 70


    dia,equipo_local,goles_local,equipo_visitante,goles_visitante = leer(eurocopa)
    dia2,equipo_local2,goles_local2,equipo_visitante2,goles_visitante2 = leer(copaAmerica)

    while(int(dia) < MAX_DIA) or (int(dia2) <  MAX_DIA):
        minimo = str(min(int(dia), int(dia2)))
        while dia == minimo:
            guardar_partido(unir,dia,equipo_local,goles_local,equipo_visitante,goles_visitante,'europa')
            dia,equipo_local,goles_local,equipo_visitante,goles_visitante = leer(eurocopa)
        while dia2 == minimo:
            guardar_partido(unir,dia2,equipo_local2,goles_local2,equipo_visitante2,goles_visitante2,"america")
            dia2,equipo_local2,goles_local2,equipo_visitante2,goles_visitante2 = leer(copaAmerica)

def cant_part_ganados(completo_read):
    dicc = {}

    dia,equipo_local,goles_local,equipo_visitante,goles_visitante,copa = leer_completo(completo_read)
    
    while dia != MAX_DIA:
        if goles_local > goles_visitante:
            if equipo_local not in dicc:
                dicc[equipo_local] = [1,0,0]
            else:
                dicc[equipo_local][0] += 1
            if equipo_visitante not in dicc:
                dicc[equipo_visitante] = [0,0,1]
            else:
                dicc[equipo_visitante][2] += 1
        elif goles_visitante > goles_local:
            if equipo_local not in dicc:
                dicc[equipo_local] = [0,0,1]
            else:
                dicc[equipo_local][2] += 1
            if equipo_visitante not in dicc:
                dicc[equipo_visitante] = [1,0,0]
            else:
                dicc[equipo_visitante][0] += 1
        elif goles_visitante == goles_local:
            if equipo_local not in dicc:
                dicc[equipo_local] = [0,1,0]
            else:
                dicc[equipo_local][1] += 1
            if equipo_visitante not in dicc:
                dicc[equipo_visitante] = [0,1,0]
            else:
                dicc[equipo_visitante][1] += 1
        dia,equipo_local,goles_local,equipo_visitante,goles_visitante,copa = leer_completo(completo_read)        
    return dicc

def ord_ganados(dicc):
    dicc_items = dicc.items()
    ordenado = sorted(dicc_items, key=lambda x:x[1][0], reverse=True)
    i = 0
    while i < len(ordenado) and ordenado[i][1][0] != 0:
        print(ordenado[i][0], "-", ordenado[i][1][0])
        i += 1


def main():
    eurocopa = open('europa.csv','r')
    copaAmerica = open('america.csv','r')
    unir = open('resultados_completo.csv', 'w')
    
    juntar_mundiales(eurocopa, copaAmerica, unir)

    eurocopa.close()
    copaAmerica.close()
    unir.close()

    completo_read = open('resultados_completo.csv', 'r')
    dicc_ganados = cant_part_ganados(completo_read)
    completo_read.close()

    ord_ganados(dicc_ganados)


main()