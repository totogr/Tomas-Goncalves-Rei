from archivos.archivos import leer_nombre, parsear_archivo
from detector_rata_greedy import verificar_sospechoso
from archivos.salida import imprimir_resultados

MOSTRAR_ASIGNACIONES = True

def ejecutar_pruebas_individuales():
    nombre_archivo = leer_nombre()
    intervalos, timestamps = parsear_archivo(nombre_archivo)

    es_rata, asignaciones = verificar_sospechoso(intervalos, timestamps)
    imprimir_resultados(es_rata, asignaciones,  mostrar_asignaciones=MOSTRAR_ASIGNACIONES)

#Condición para que solo haga este llamado, si comando pasado por terminal
if __name__ == "__main__":
    ejecutar_pruebas_individuales()

# Ejecutar con

# Uso: python3 pruebas_individuales.py <archivo>