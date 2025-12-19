[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/UoY0NL5F)

# Trabajo practico 2 - Sistemas-L

## Introduccion

En el presente trabajo practico se busca crear un programa a traves de la *programacion funcional* y *sistemas generativos*, en el cual se generen imagenes fractales mediante la implementacion de **sistemas-L** y **graficos tortuga**, exportando el resultado en formato **SVG**. 

## Implementacion

Este trabajo consiste en desarrollar en Clojure un programa que realice lo siguiente:
- Leer un archivo que contenga los parametro para un sistema-L, el cual guardara un angulo, un axioma y un conjunto de reglas de produccion.
- Aplique esas reglas durante una cierta cantidad de iteraciones para generar una cadena de simbolos.
- Interprete dicha cadena como instrucciones graficas usando el paradigma de graficos tortuga.
- Exporte el dibujo final en un archivo SVG, para poder visualizarlo en cualquier navegador.

## Graficos Tortuga

Los gráficos tortuga permiten generar imágenes a partir de instrucciones simples dadas a una "tortuga virtual", que se mueve sobre un plano siguiendo comandos como "avanzar", "girar", o "levantar/bajar la pluma". Estos gráficos permiten traducir las cadenas generadas por el sistema-L en dibujos geométricos.

## Sistemas-L

Los sistemas de Lindenmayer son modelos formales que producen cadenas de símbolos a partir de un conjunto de reglas de reemplazo. Al iterar estas reglas a partir de un axioma inicial, se genera una secuencia cada vez más compleja.

## Integracion de conceptos

Cada sambolo del sistema-L se interpreta como un comando grafico para la tortuga. Los caracteres *F* y *G* indican avanzar y dibujar, los simbolos *+* y *-* indican rotaciones, y *[* y *]* manejan el apilado y desapilado del estado de la tortuga. Esta interpretacion permite traducir directamente las producciones del sistema-L en dibujos fractales.

## Archivo de entrada

El archivo que describe al sistema-L contiene:
- Un angulo en grados
- El axioma inicial
- Una o mas reglas de produccion con el predecesor y el sucesor

## Formato de salida

El resultado se exporta en formato SVG, un estándar XML para representar imágenes vectoriales. Esto permite que el dibujo generado pueda visualizarse fácilmente en navegadores.

## Parametros para ejecutar el programa

El programa debe ejecutarse desde la linea de comandos recibiendo tres argumentos:
- El nombre del archivo del sistema-L.
- La cantidad de iteraciones a aplicar.
- El nombre del archivo SVG de salida.

Ejemplo:

```
lein run <archivo_sistema_L> <cantidad_de_iteraciones> <archivo_SVG>
```

## Integrantes del Grupo

| Alumno | Padrón |
| -- | -- |
| David Leonardo Batallan| 97529 |
| Tomas Goncalves Rei | 111405 |


