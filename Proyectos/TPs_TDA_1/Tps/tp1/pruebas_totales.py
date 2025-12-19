from detector_rata_greedy import verificar_sospechoso
from archivos.archivos import parseo, leer_directorio
from archivos.salida import imprimir_resultados
import os

MOSTRAR_ASIGNACIONES = False

def ejecutar_pruebas_totales():
    """
    Lee y parsea los archivos del directorio de pruebas pasado por parametro.
    Las rutas y constantes se encuentran en el archivo archivos.py
    """
    carpeta = leer_directorio(1)
    ruta = os.path.join(os.getcwd(), carpeta)
    intervalos_totales, timestamps_totales, nombres_archivos = parseo(ruta, dicc=False)

    for i in range(len(intervalos_totales)):
        # Por cada conjunto de timestamps e intervalos, llamo a verificar sospechoso
        print(f"Probando archivo {nombres_archivos[i]}:")
        es_rata, asignaciones = verificar_sospechoso(intervalos_totales[i], timestamps_totales[i])
        imprimir_resultados(es_rata, asignaciones, mostrar_asignaciones=MOSTRAR_ASIGNACIONES)

ejecutar_pruebas_totales()

# Ejecutar con

# Uso: python3 pruebas_totales.py <ruta_set_pruebas>