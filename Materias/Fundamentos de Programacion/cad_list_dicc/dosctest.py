cadena = str(input("Escriba una palabra: "))
vocales = "aeiouAEIOU"
i=0
def obtener_vocales(cadena):
    """Devuelve  una cadena, formada por las vocales que se encuentran en la cadena recibida.
    >>> obtener_vocales("campana")
    a
    a
    a
    >>> obtener_vocales("pueblo")
    u
    e
    o
    >>> obtener_vocales("cg9")
   
    >>> obtener_vocales("palabra")
    a
    a
    a
    >>> obtener_vocales("lgkjhlkfgj")
   
    >>> obtener_vocales("gatito")
    a
    i
    o
    """
    for i in cadena:
        if i in vocales:
            print (i)

obtener_vocales(cadena)

import doctest
print (doctest.testmod())