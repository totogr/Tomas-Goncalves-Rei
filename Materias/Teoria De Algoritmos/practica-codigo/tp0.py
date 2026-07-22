
class varible_tipo_alumno:
    def __init__(self, nombre, altura):
        self.nombre = nombre
        self.altura = altura
        
alumnos = [
    varible_tipo_alumno("A", 1.2),
    varible_tipo_alumno("B", 1.15),
    varible_tipo_alumno("C", 1.14),
    varible_tipo_alumno("D", 1.12),
    varible_tipo_alumno("E", 1.02),
    varible_tipo_alumno("F", 0.98),  # Más bajo (esperado índice 5)
    varible_tipo_alumno("G", 1.18),
    varible_tipo_alumno("H", 1.23)
]

def indice_mas_bajo(alumnos):
    bajo = 0
    alto = len(alumnos) - 1
    while bajo < alto:
        medio = (bajo + alto) // 2

        if alumnos[medio].altura < alumnos[medio - 1].altura and alumnos[medio].altura < alumnos[medio + 1].altura:
            return medio
        elif alumnos[medio].altura > alumnos[medio + 1].altura:
            bajo = medio + 1
        else:
            alto = medio

def validar_mas_bajo(alumnos, indice):
    if indice <= 0 or indice >= len(alumnos) - 1:
        return False

    return (alumnos[indice].altura < alumnos[indice - 1].altura and alumnos[indice].altura < alumnos[indice + 1].altura)


def elemento_desordenado(arr, inicio, fin):
        if inicio >= fin:
            return None
        
        mitad = (inicio + fin) // 2

        if mitad < len(arr) - 1 and arr[mitad] > arr[mitad + 1]:  
            return arr[mitad + 1]
        if mitad > 0 and arr[mitad] < arr[mitad - 1]:  
            return arr[mitad]

        izquierda = elemento_desordenado(arr, inicio, mitad - 1)
        derecha = elemento_desordenado(arr, mitad + 1, fin)

        if izquierda is None:
            return derecha
        else:
            return izquierda
        
arr = [1,2,1,3,4,5,6,8,9,10]

print(elemento_desordenado(arr, 0, len(arr)-1))

