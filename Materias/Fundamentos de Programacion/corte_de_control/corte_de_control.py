def leer_archivo(archivo):
    linea = archivo.readline()
    if linea:
        devolver = linea.rstrip("\n").split(",")
    else:
        devolver = "","","","",""
    return devolver

def informe():
    archivo = open('DatosCursos.csv', 'r')

    nro_inscripto, nivel_educativo, trabaja_actualmente, curso, comision = leer_archivo(archivo)

    total_inscriptos = 0

    while curso:
        total_curso = 0
        curso_anterior = curso
        print("Curso:", curso)

        while curso and curso_anterior == curso:
            total_comision = 0
            comision_anterior = comision

            while curso and curso_anterior == curso and comision_anterior == comision:
                total_comision += 1
                

                nro_inscripto, nivel_educativo, trabaja_actualmente, curso, comision = leer_archivo(archivo)

            print("Comision:", comision_anterior," - ", "total alumnos:", total_comision)

            total_curso += total_comision
            total_inscriptos += total_comision
        
        print("Cantidad alumnos en curso:", total_curso)
    
    print("La cantidad total de alumnos inscriptos es:", total_inscriptos)

    archivo.close()

informe()
