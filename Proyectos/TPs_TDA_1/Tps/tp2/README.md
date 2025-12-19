# Trabajo Practico 2 - Que parezca Programación Dinamica
El algoritmo para determinar posibles mensajes puede ser ejecutado de la siguiente manera:

```
python3 archivo_palabras.txt < posibles_textos/
```

Los textos los leera de entrada estandar. Se pueden escribir los textos manualmente si se ejecuta de la siguiente manera:

```
python3 archivo_palabras.txt
```

## Generación de pruebas
Cada generador incluido en la carpeta `extras/generadores` contiene un comentario al final del archivo explicando su uso. Igualmente, todos pueden ser ejecutados de la siguiente manera:

```
generador_x.py ruta_palabras.txt ruta_textos/
```

Todos ellos generaran textos con las palabras enviadas en `ruta_palabras.txt` y los guardaran en `ruta_textos/`

## Ejecución de pruebas
El archivo `pruebas.py` permite ejecutar pruebas individuales (un solo archivo de palabras) o multiples (multiples archivos de textos y palabras)

- Prueba individual:
```
pruebas.py ruta_archivo_palabras.txt < posibles_textos
```

- Pruebas multiples:
```
python3 pruebas.py -l carpeta_palabras/ carpeta_textos/ ruta_salida/resultados.txt
```
En la ruta de salida se debe especificar tambien el archivo de salida (si no existe se creara automaticamente).

## Mediciones
Con el archivo `medir_tiempos.py` se pueden realizar mediciones del rendimiento de nuestro algoritmo para diferentes tamaños de pruebas.
Para ejecutarlo:

```
python3 medir_tiempos.py carpeta_textos/ ruta_palabras.txt archivo.salida.txt
```

Los textos dentro de `carpeta_textos` deben ser del formato `n_nombre.txt`, ya que el tamaño de las palabras sera leído del nombre del archivo. Ejemplo: `1000_valido.txt`.

## Graficos
Una vez generadas las mediciones como se indico anteriormente, se pueden crear graficos comparativos entre los tiempos de nuestro algoritmo y los graficos de algunas complejidades conocidas. Para realizarlos se tiene el archivo `tiempos_y_complejidad.py`. Se puede ejecutar de esta manera:

```
python3 tiempos_y_complejidad.py resultados.txt
```

## Resultados con pruebas nuestras
En la carpeta `extras/resultados_io` se encuentra un archivo `resultados.txt` en el que se incluyen los resultados de ejecutar nuestro algoritmo con los sets de palabras y textos presentes en esa carpeta. Los textos fueron generados con nuestros propios generadores.