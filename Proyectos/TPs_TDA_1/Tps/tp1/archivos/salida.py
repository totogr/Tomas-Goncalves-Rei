MENSAJE_ES_RATA = "El sospechoso es la rata!"
MENSAJE_NO_ES_RATA = "El sospechoso NO es la rata!"
SALIDA_ASIGNACION = "{timestamp} --> {mitad} +- {rango}"

def imprimir_resultados(es_rata, asignaciones, mostrar_asignaciones=True): 
    """
    Imprime los resultados de llamar a verificar_sospechoso en el formato pedido por la catedra
    Recibe:
    -   Un booleano indicando si es la rata
    -   Un diccionario con las asignaciones en el formato {intervalo:timestamp}
    """   
    if es_rata:
        print(MENSAJE_ES_RATA)
        
        if mostrar_asignaciones:
            # Imprimir asignaciones timestamp --> intervalo
            for intervalos, timestamp in asignaciones.items():  
                imprimir_asignacion(intervalos, timestamp)
    else:
        print(MENSAJE_NO_ES_RATA)

def imprimir_asignacion(intervalo,timestamp):
    """ 
    Recibe un intervalo y el timestamp que le fue asignado
    Los imprime en el formato pedido por la catedra
    """
    desde= intervalo[0]
    hasta= intervalo[1]
    mitad = (desde + hasta) // 2
    rango = mitad - desde
    print(SALIDA_ASIGNACION.format(timestamp=timestamp, mitad=mitad, rango=rango))