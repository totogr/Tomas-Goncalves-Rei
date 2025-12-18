import pandas as pd

datos = pd.read_csv("sueldos.csv")
# print(datos)
# print(datos.describe())
# limpio = datos.dropna()
# limpio = datos.dropna()
# limpio = datos.fillna(4)
# print(limpio)
# print(datos[["nombre", "sueldo"]])
# print(datos.loc[[3], ["nombre"]])
# print(datos[(datos["sueldo"] >= 80) & (datos["legajo"] >= 1)])
# print(datos[(datos["sueldo"] >= 80) | (datos["legajo"] >= 6)])

def sueldo_por_semana(valor):
    return valor * 5

datos["sueldo"] = datos["sueldo"].apply(sueldo_por_semana)
print(datos)
