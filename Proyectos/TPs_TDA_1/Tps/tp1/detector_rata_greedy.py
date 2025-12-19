def verificar_sospechoso(intervalos, timestamps_sospechoso):
    """
    Teniendo los intervalos y timestamps, verifica si el sospechoso es la rata y devuelve las asignaciones
    PRE:
    -    El primer argumento es una lista de intervalos (como tuplas) del formato (ti-ei, ti+ei) 
    -    El segundo argumento es una lista de timestamps (numeros)
    POST:
    -    Se devuelve una tupla con dos valores: un booleano (indicando si es el sospechoso) y una diccionario con asignaciones
         del formato clave=intervalo, valor=timestamp
    """
    dicc_timestamps , valores_timestamps = obtener_dicc_timestamps(timestamps_sospechoso)
    intervalos = sorted(intervalos, key=lambda x: x[1]) # Ordeno por tiempo de finalizacion
    asignaciones={}
 
    for intervalo in intervalos:
        if len(valores_timestamps)==0:
            return False , asignaciones
        elem = busqueda_binaria(valores_timestamps,intervalo[0],intervalo[1])
        if elem == -1:
            return False , asignaciones
        dicc_timestamps[elem] -= 1
    
        if dicc_timestamps[elem]==0: # Si no quedan apariciones lo borramos del dicc para no iterar de mas
            del dicc_timestamps[elem]
            valores_timestamps.remove(elem)
        
        asignaciones[intervalo] = elem # Agrego la asignacion a la lista con el formato especificado

    return True, asignaciones

#Algoritmo que encuentra mas chiquito dentro del rango desdeHasta -> del intervalo
def busqueda_binaria(lista,desde,hasta):
    """
    Busca el primer numero perteneciente al intervalo
    Recibe:
    -   Arreglo de numeros ordenados
    -   Numero desde
    -   Numero hasta

    Devuelve el menor numero del arreglo que pertenece al intervalo [desde:hasta] 
    """
    return _busqueda_binaria(lista,0,len(lista)-1,desde,hasta)
    
def _busqueda_binaria(lista,inicio,fin,desde,hasta):
    if inicio == fin:
        if lista[inicio]>=desde and lista[inicio]<=hasta :
            return lista[inicio]
        else:
            return -1
    mitad=(inicio+fin)//2

    if lista[mitad]==desde:
        return lista[mitad]
    elif (mitad==0 and lista[mitad]>desde) or (mitad>0 and lista[mitad]>desde and lista[mitad-1]<desde):
        return lista[mitad]
    elif lista[mitad]>desde:
        return _busqueda_binaria(lista,inicio,mitad,desde,hasta)
    else:
        return _busqueda_binaria(lista,mitad+1,fin,desde,hasta)
    

def obtener_dicc_timestamps(timestamps):
    """
    Recibe:
    -   Arreglo de timestamps
    
    Devuelve una tupla con:
    -   Un diccionario del formato timestamp:cant_apariciones
    -   Un arreglo con los timestamps sin repetidos   
    """
    dicc_timestamps = {}
    valores = []
    
    # Asignamos para cada timestamp -> clave: timestamp, valor: cantidad de apariciones
    for timestamp in timestamps:
        if timestamp not in dicc_timestamps:
            dicc_timestamps[timestamp] = 0
            valores.append(timestamp)
        
        dicc_timestamps[timestamp] += 1
    
    return dicc_timestamps , valores
