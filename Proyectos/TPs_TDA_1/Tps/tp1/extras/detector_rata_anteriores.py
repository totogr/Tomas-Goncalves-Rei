""" 
Recibe:
-   Los intervalos de las transacciones en el formato [inicio, fin] 
-   Los timestamps del sospechoso (lista de enteros)

Devuelve:
-   Un booleano indicando si es el sospechoso
-   Una lista de cadenas con el formato 'timestamp --> tiempo +- error'
"""

def verificar_sospechoso_version_2(intervalos, timestamps_sospechoso):
    # Diccionario del tipo timestamps:cant_apariciones
    dicc_timestamps = obtener_dicc_timestamps(timestamps_sospechoso)
    intervalos = sorted(intervalos, key=lambda x: x[1]) # Ordeno por tiempo de finalizacion
    asignaciones={}

    for intervalo in intervalos:
        encontro = False
        for timestamp in dicc_timestamps:
            if en_rango(timestamp, intervalo):
                dicc_timestamps[timestamp] -= 1
                 
                # Si no quedan apariciones lo borramos del dicc para no iterar de mas
                if dicc_timestamps[timestamp]==0:
                    del dicc_timestamps[timestamp]
                
                encontro = True
                # Agrego la asignacion a la lista con el formato especificado
                asignaciones[intervalo] = timestamp
                break
        if not encontro:
            return False, asignaciones

    return True, asignaciones


"""
Recibe:
-   Un numero 
-   Un intervalo del formato [a, b]

Devuelve booleano indicando si el numero pertenece al intervalo
"""
def en_rango(timestamp, intervalo):
    return timestamp >= intervalo[0] and timestamp <= intervalo[1]

"""
Agrupa los timestamps en un diccionario del formato timestamp:cantidad_de_apariciones
Recibe:
-   Arreglo de timestamps
Devuelve:
-   Diccionario del formato clave=timestamp, valor=cantidad_de_apariciones_del_timestamp
"""
def obtener_dicc_timestamps(timestamps):
    dicc_timestamps = {}
    
    # Asignamos para cada timestamp -> clave: timestamp, valor: cantidad de apariciones
    for timestamp in timestamps:
        if timestamp not in dicc_timestamps:
            dicc_timestamps[timestamp] = 0
        
        dicc_timestamps[timestamp] += 1
    
    return dicc_timestamps

def verificar_sospechoso_version_1(intervalos, timestamps_sospechoso):
    intervalos = sorted(intervalos, key=lambda x: x[1])
    asignaciones={}

    for intervalo in intervalos:
        encontro = False
        
        for i in range(len(timestamps_sospechoso)):
            timestamp = timestamps_sospechoso[i]
            if timestamp is None:
                continue
            if timestamp >= intervalo[0] and timestamp <= intervalo[1]:
                asignaciones[intervalo] = timestamp
                timestamps_sospechoso[i] = None
                encontro = True
                break
        if not encontro:
            return False, asignaciones
        
    return True, asignaciones