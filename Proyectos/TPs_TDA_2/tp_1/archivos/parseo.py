import os
import sys

# Constantes
SEPARADOR_TI_BI = ","
ENCABEZADO_ESPERADO = "T_i,B_i"

# Errores
FILE_NOT_FOUND = "ERROR: El archivo no existe"
ARGUMENTOS_INSUFICIENTES = "Se necesita un argumento {indice} en entrada estandar"
NOT_A_DIRECTORY = "La carpeta especificada no existe"
ENCABEZADO_INCORRECTO = "Formato incorrecto: se esperaba encabezado 'T_i,B_i'"

# Archivo de resultados (para mediciones de tiempo)
HEADER_RESULTADOS = "Tamano de entrada -- Tiempo de ejecucion promedio\n\n"
SALIDA_MEDICION = "        {valor}              {tiempo} seg \n"

def parseo(ruta, dicc=True):	
    """
    Parsea todos los archivos de un directorio.
    
    Si dicc=True devuelve un diccionario:
        { tamano: (batallas, nombre_archivo) }
    Si dicc=False devuelve:
        lista de listas de batallas, lista de nombres de archivos
    """

    if not os.path.exists(ruta):
        raise NotADirectoryError(f"Directorio no encontrado: {ruta}")

    nombres_archivos = []
    batallas_totales = []
    
    for nombre_archivo in os.listdir(ruta):
        ruta_archivo = os.path.join(ruta, nombre_archivo)
        if not os.path.isfile(ruta_archivo):
            continue
        try:
            batallas = parsear_archivo(ruta_archivo)
            nombres_archivos.append(nombre_archivo)
            batallas_totales.append(batallas)
        except Exception:
            continue
    
    if not dicc:
        return batallas_totales, nombres_archivos
    
    dicc_pruebas = {}
    for i in range(len(batallas_totales)):
        tam = len(batallas_totales[i])
        dicc_pruebas[tam] = (batallas_totales[i], nombres_archivos[i])
    return dicc_pruebas

def parsear_archivo(ruta):
    """
    Parsea un archivo de batallas.
    Formato esperado:
        T_i,B_i
        53,100
        61,100
        ...
    Devuelve lista de tuplas (tiempo, peso).
    """
    if not os.path.exists(ruta):
        raise FileNotFoundError(FILE_NOT_FOUND)

    batallas = []
    with open(ruta, "r") as f:
        encabezado = f.readline().strip()
        if encabezado != ENCABEZADO_ESPERADO:
            raise ValueError(f"Formato incorrecto, se esperaba '{ENCABEZADO_ESPERADO}'")
        for linea in f:
            linea = linea.strip()
            if not linea:
                continue
            t, b = map(int, linea.split(SEPARADOR_TI_BI))
            batallas.append((t, b))
    return batallas

def leer_archivo():
    """
    Lee de entrada estandar el nombre del archivo a probar
    """
    if len(sys.argv) < 2:
        raise FileNotFoundError(FILE_NOT_FOUND)

    nombre = sys.argv[1]
    if not os.path.exists(nombre):
        raise FileNotFoundError(FILE_NOT_FOUND)

    return nombre

def leer_directorio(indice):
    """
    Lee el argumento <indice> de la entrada estandar y comprueba que sea una carpeta.
    """
    if indice >= len(sys.argv):
        raise Exception(ARGUMENTOS_INSUFICIENTES.format(indice=indice))

    carpeta = sys.argv[indice]
    if not os.path.exists(carpeta):
        raise NotADirectoryError(NOT_A_DIRECTORY)

    return carpeta

def crear_archivo_resultados(resultados, ruta):
    """
    Recibe los resultados de una prueba y los escribe en la ruta especificada.
    """
    with open(ruta, "w") as archivo:
        archivo.write(HEADER_RESULTADOS)
        for valor, tiempo in resultados:
            archivo.write(SALIDA_MEDICION.format(valor=valor, tiempo=tiempo))