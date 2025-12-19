#fuerzas es un diccionario tal que fuerzas[maestrox] = fuerza del maestro k 
def verificador(maestros,fuerzas,k,B,subgrupos): 
    #Verficar que se hizo una particion en k subgrupos 
    if len(subgrupos) != k : return False 
    
    #Verificar que no un maestro solo pertenece a un grupo 
    maestros_auxiliar = set() 
    for subgrupo in subgrupos: 
        for maestro in subgrupo: 
            if maestro in maestros_auxiliar: 
                return False 
            maestros_auxiliar.add(maestro) 
    
    #Verificar que la cantidad de maestros divididos en los subgrupos es igual al total de maestros 
    
    if len(maestros) != len(maestros_auxiliar): 
        return False 
    
    #Verificar que los maestros divididos en los subgrupos son validos 
    for maestro in maestros: 
        if maestro not in maestros_auxiliar: 
            return False 
    
    #Verificar que la la adición de los cuadrados de las sumas de las fuerzas de los grupos <= B 
    fuerza_grupal = 0 
    fuerza_total = 0 
    for subgrupo in subgrupos: 
        for maestro in subgrupo: 
            fuerza_grupal+=fuerzas[maestro] 
        fuerza_total+= (fuerza_grupal*fuerza_grupal) 
        fuerza_grupal = 0 
    if fuerza_total > B: 
        return False 
    return True