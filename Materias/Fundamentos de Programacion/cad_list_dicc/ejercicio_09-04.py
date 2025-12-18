# 1)	Escribir una función elegir en Python para decidir asociarse o no a un club. Esta función debe devolver True si el club es aceptado, False de lo contrario.
# La función recibe dos listas y un entero: una de actividades (actividades que se realizan en el club), otra de actividades_deseadas (son las actividades que al usuario le gustaría realizar) y un valor de cuota.
# El club se aceptará si:
# -	Si realizan por lo menos dos actividades deseadas y el valor de la cuota es menor o igual a
# MAX_CUOTA = 6000
# Ejemplos:
# lista_1 = [“natación”, “gimnasio”, “voley”, “futbol”]
# lista_2 = [“natación”, “voley”, “gimnasio”]
# lista_3 = [“natación”, “futbol”, “karate”]

# elegir(lista_1, lista_2, 5000) --->  True 
# elegir(lista_1, lista_3, 5000) --->  True
# elegir(lista_2, lista_3, 100) --->  False

actividades = ["natación", "gimnasio", "voley", "futbol"]
actividades_deseadas = ["natación", "futbol", "karate"]
cuota = float(6000)
max_cuota = 6000

def elegir(actividades, actividades_deseadas, cuota):
    coincidencias = 0
    for actividad in actividades:
        if actividad in actividades_deseadas:
            coincidencias += 1
    if coincidencias >= 2 and cuota <= max_cuota:
        return True
    else:
        return False
        

print (elegir(actividades, actividades_deseadas, cuota))