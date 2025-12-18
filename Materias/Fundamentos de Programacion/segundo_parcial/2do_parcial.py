def leer(archivo):
    linea = archivo.readline()
    if linea:
        devolver = linea.rstrip("\n").split(",")
    else:
        devolver = 70,"","","",""
    return devolver

def leer_completo(archivo):
    linea = archivo.readline()
    if linea:
        devolver = linea.rstrip("\n").split(",")
    else:
        devolver = "",70,"","","",""
    return devolver

def guardar_partido(año,dia,equipo_local,goles_local,equipo_visitante,goles_visitante, archivo):
    archivo.write(año + "," + str(dia) + "," + equipo_local + "," + goles_local + "," + equipo_visitante + "," + goles_visitante + '\n')

def juntar_mundiales(m2014, m2018, m2022, completo_edit):

    MAX_DIA = 70

    dia,equipo_local,goles_local,equipo_visitante,goles_visitante = leer(m2014)
    dia2,equipo_local2,goles_local2,equipo_visitante2,goles_visitante2 = leer(m2018)
    dia3,equipo_local3,goles_local3,equipo_visitante3,goles_visitante3 = leer(m2022)

    while(int(dia) < MAX_DIA) or (int(dia2) < MAX_DIA) or (int(dia3) < MAX_DIA):
        minimo = min(dia, dia2, dia3)
        while dia == minimo:
            guardar_partido("2014",dia,equipo_local,goles_local,equipo_visitante,goles_visitante, completo_edit)
            dia,equipo_local,goles_local,equipo_visitante,goles_visitante = leer(m2014)
        while dia2 == minimo:
            guardar_partido("2018",dia2,equipo_local2,goles_local2,equipo_visitante2,goles_visitante2, completo_edit)
            dia2,equipo_local2,goles_local2,equipo_visitante2,goles_visitante2 = leer(m2018)
        while dia3 == minimo:
            guardar_partido("2022",dia3,equipo_local3,goles_local3,equipo_visitante3,goles_visitante3, completo_edit)
            dia3,equipo_local3,goles_local3,equipo_visitante3,goles_visitante3 = leer(m2022)

def cant_part_ganados(completo_read):
    MAX_DIA = 70
    dicc = {}

    año,dia,equipo_local,goles_local,equipo_visitante,goles_visitante = leer_completo(completo_read)
    
    while int(dia) < MAX_DIA:
        if equipo_visitante not in dicc and goles_visitante > goles_local:
            dicc[equipo_visitante] = 1
        elif equipo_local not in dicc and goles_local > goles_visitante:
            dicc[equipo_local] = 1
        if equipo_visitante in dicc and goles_visitante > goles_local:
            dicc[equipo_visitante] += 1
        elif equipo_local in dicc and goles_local > goles_visitante:
            dicc[equipo_local] += 1
        año,dia,equipo_local,goles_local,equipo_visitante,goles_visitante = leer_completo(completo_read)


    print(dicc)




def main():
    m2014 = open('resultados_2014.csv','r')
    m2018 = open('resultados_2018.csv','r')
    m2022 = open('resultados_2022.csv','r')
    completo_edit = open('resultados_completo.csv', 'w')
    
    juntar_mundiales(m2014, m2018, m2022, completo_edit)

    m2014.close()
    m2018.close()
    m2022.close()
    completo_edit.close()

    completo_read = open('resultados_completo.csv', 'r')
    cant_part_ganados(completo_read)
    completo_read.close()


main()