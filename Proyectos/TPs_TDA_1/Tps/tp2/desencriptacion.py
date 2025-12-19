def cadena_es_mensaje(lista_palabras,mensaje):
    """
    Recibe:
    palabras : listado de palabras posibles
    mensaje  : cadena que se evaluara

    Devuelve:
    - Un booleano indicando si el mensaje era valido dadas las palabras recibidas
    - El texto con los espacios correspondientes, o None si el texto no fuera valido
    """

    if len(mensaje)==0 : return True , None
    if len(lista_palabras)==0 : return False , None

    #Se pasa lista de palabras a set y se guarda la longitud de la mas larga
    palabras = set()
    longitud_max=0
    for p in lista_palabras:
        palabras.add(p)
        longitud_p =len(p) 
        if longitud_p > longitud_max:
            longitud_max = longitud_p

    #Creo el arreglo que se utilizara para la "memoizacion"
    M = [False] * (len(mensaje)+1)

    #Caso base -> cadena vacia
    M[0] = True
    
    # palabrapalabra

    #Recorremos cadena
    for indice in range(1,len(mensaje)+1):
        es_palabra = False

        indice_anterior_desde = max(0, indice-longitud_max)

        for indice_anterior in range(indice_anterior_desde, indice):
            palabra = []

            for i in range(indice_anterior, indice):
                palabra.append(mensaje[i])
            palabra = "".join(palabra)
            
            if palabra in palabras:
                anterior_es_mensaje= M[indice_anterior]
                if anterior_es_mensaje:
                    es_palabra = True
                    break
        M[indice] = es_palabra
    
    if M[len(mensaje)] :
        return True , reconstruccion_mensaje(mensaje,M,palabras)
    
    return False , None 


def reconstruccion_mensaje(mensaje,M,conjunto_palabras):
    """
    Recibe:
    mensaje : cadena que fue evaluada en la función que llama
    M : arreglo de memoizacion
    conjunto_palabras : set con las posibles palabras

    Devuelve:
    El mensaje correspondiente con los espacios en donde es debido.
    """
    palabras = []
    indice = len(M)-1

    palabraActual = []
    while indice != 0:
        palabraActual.append(mensaje[indice-1])  
        indice = hallar_inicio(M,indice,palabraActual,mensaje)
        
        palabraActual.reverse()
        palabra = "".join(palabraActual)

        if palabra in conjunto_palabras:
            palabras.append(palabra)
            palabraActual = []
        else:
            palabraActual.reverse()

    palabras.reverse()
    mensaje = " ".join(palabras)
    return mensaje

def hallar_inicio(M,finActual,palabraActual,mensaje):
    for indice in range(finActual-1,-1,-1):
        if M[indice] == True : 
            return indice
        palabraActual.append(mensaje[indice-1])
