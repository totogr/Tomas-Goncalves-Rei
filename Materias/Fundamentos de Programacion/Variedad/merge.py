MAX = "50,,"
MAX_DIA = 50

def leer(archivo):
    linea = archivo.readline()
    if (linea):
        linea = linea.rstrip()
    else:
        linea = MAX

    campos = linea.split(',')

    return (int(campos[0]), campos[1], campos[2])    

def guardar(fe, ve, mo, suc, archivo):
    archivo.write(str(fe) + "," + ve + "," + mo + "," + suc + "\n")

def mezcla (suc1, suc2, unificado):
	fecha1, vendedor1, monto1 = leer(suc1)
	fecha2, vendedor2, monto2 = leer(suc2)
	while (fecha1 < MAX_DIA) or (fecha2 < MAX_DIA):
		minimo = min(fecha1, fecha2)
		while (fecha1 == minimo):
			guardar(fecha1, vendedor1, monto1, "suc1", unificado)
			fecha1, vendedor1, monto1 = leer(suc1)
		while (fecha2 == minimo):
			guardar(fecha2, vendedor2, monto2, "suc2", unificado)
			fecha2, vendedor2, monto2 = leer(suc2)


def main():
    suc1 = open('suc1.txt', 'r')
    suc2 = open('suc2.txt', 'r')
    union = open('union.txt', 'w')
    mezcla(suc1, suc2, union)
    suc1.close()
    suc2.close()
    union.close()

main()
