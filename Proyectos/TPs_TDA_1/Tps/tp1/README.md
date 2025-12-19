# Trabajo practico 1: La mafia de los algoritmos Greedy
Para ejecutar nuestro algoritmo con un archivo de prueba especifico (formato especificado por la catedra)

```
python3 tp1.py <archivo_entrada>
```

## Instalaciones necesarias:
Las siguientes librerias de python deben estar instaladas previamente:
- Python
- Numpy
- Scipy
- matplotlib

## Pruebas
El archivo `pruebas.py` permite probar nuestro algoritmo con un set de pruebas. Uso:

```
python3 pruebas.py <carpeta_set_pruebas>
```

Por defecto, por cada detección de *la rata* se imprimen todas las asignaciones realizadas por el algoritmo. Esto puede cambiarse con la constante `MOSTRAR_ASIGNACIONES`que esta presente en el archivo.

## Generación de pruebas
Para este trabajo se realizaron un conjunto de generadores de pruebas con diferente funcionamiento. Todos se encuentran en la carpeta `extras/generadores`. Se pueden usar de la siguiente manera:


```
python3 <archivo_generador>.py <carpeta_salida>
```

## Mediciones
Se pueden hacer mediciones del funcionamiento de nuestro algoritmo de la siguiente manera:

1. Primero, se deben medir los tiempos de ejecución con un set de pruebas especifico:

```
python3 medir_tiempos.py <carpeta_set_pruebas> <carpeta_salida>
# Los resultados se guardan en <carpeta_salida>
```

2. Luego, se deben realizar los graficos usando el archivo `tiempos_y_complejidad.py` 
```
python3 tiempos_y_complejidad.py <archivo_resultados>
```
Este paso se debe hacer usando el archivo resultante de hacer las mediciones en el paso anterior. Luego de ejecutar el comando recien mencionado, se deberia mostrar el grafico por pantalla.
