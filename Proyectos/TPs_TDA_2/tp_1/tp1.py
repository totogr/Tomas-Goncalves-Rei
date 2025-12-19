import sys
from archivos.parseo import parsear_archivo
# guerras = [(tiempo, peso)]

def algoritmo(guerras):
    guerras = sorted(guerras, key=lambda x: x[0]/x[1])
    F = 0
    orden = []
    suma = 0

    for tiempo, peso in guerras:
        orden.append((tiempo, peso))
        F += tiempo
        suma += F * peso
    
    return orden, suma


if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Uso: python3 tp1.py ruta/a/entrada.txt")
        sys.exit(1)

    archivo = sys.argv[1]
    batallas = parsear_archivo(archivo)

    orden, suma = algoritmo(batallas)

    print("Orden optimo de batallas (t, b):")
    for t,b in orden:
        print(f"t={t}, b={b}")
    print(f"Suma ponderada minima: {suma}")