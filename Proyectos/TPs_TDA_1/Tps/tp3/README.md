# Trabajo Practico 3: Comunidades NP-Completas
A continuación se detalla como utilizar los diferentes algoritmos que fueron implementados

## Ejecución
Para ejecutar los diferentes algoritmos que permiten obtener los **k** clusters con menor distancia, se debe ejecutar el siguiente comando.
```
python3 main.py nombre_archivo.csv n
```
Al ejecutar este comando, se mostrara un menu en el que se debe seleccionar el algoritmo a utilizar. Una vez seleccionado, el algoritmo se ejecutara y se mostraran por terminar los **n** clusters obtenidos.

## Generación de sets de prueba
Tambien se incluye un algoritmo que permite generar archivos de prueba junto sus respectivos resultados esperados. El archivo esta en `extras/recursos/generador.py`. Se puede ejecutar de la siguiente manera:

```
python3 generador.py directorio_de_salida
```

El algoritmo creara los grafos y guardara cada uno en un archivo con el formato requerido para ser ejecutado por nuestros algoritmos. Ademas, tambien genera un archivo con los resultados optimos esperados.

## Errata
- **Sección 6.1.2 - Programación lineal:** La complejidad algoritmica que se menciona no es correcta. En realidad, el modelo inicialmente encontrara una solución usando el *Algoritmo Simplex*. La solución devuelta luego de aplicar este algoritmo no necesariamente sera entera, como lo requiere nuestro problema. Por ello, se debera aplicar *branch and bounds* hasta encontrar la solución optima. Este ultimo proceso provoca que la complejidad del algoritmo sea exponencial respecto de la cantidad de restricciones del modelo. En nuestra implementación, el modelo propuesto tiene *V + 2K\*V^2 + 2* restricciones. Por lo tanto, el tiempo de ejecución de nuestro algoritmo crecera exponencialmente respecto a esos valores.