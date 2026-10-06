## Clase 10 - Machine Learning I

- Predecir un evento del futuro, que aún no sucedió. 
- Al mismo tiempo medimos el error que cometemos al predecir ese evento.

### Aprendizaje supervisado

- Se parte de un set de datos con *n* filas y *m* columnas.
- Queremos predecir una **nueva columna**, usando las anteriores.
- **Entrenamiento**: le paso el set de datos al modelo, y el modelo debe aprender de esos datos para ser más preciso.
- **Estimación**: obtenemos una nueva observación, y se la pasamos al modelo ya entrenado, para que estime un resultado.

#### Pasos

0) **Obtener el set de datos** (paso preliminar)
1) **División del set de datos**
	- Set de entrenamiento (**train set**): entrenar los diferentes modelos (~80%)
	- Set de validación (**validation set**): para medir la performance de los modelos entrenados (~10%)
	- Set de testeo (**test set**): para medir la performance del modelo final (10%)
2) **Preprocesamiento del set de datos**
	- Eliminación de filas duplicadas
	- Imputación de nulos
	- Balanceo del sets de datos
	- Creación de nuevas variables o eliminación de otras
	- Transformación de variables
	- Encoding de variables categóricas
3) **Búsqueda de hiperparámetros**
	- Se busca la mejor combinación de los hiperparámetros de un modelo.
	- Se define un conjunto de valores a probar por cada hiperparámetro.
	- 2 enfoques:
		- **Grid Search**: probar cada combinación posible y elegir la de mejor métrica. Obtenemos siempre la mejor, pero es muy lenta (fuerza bruta).
		- **Random Search**: probar `k` combinaciones aleatorias y elegir la de mejor métrica. Optimizamos tiempo, pero no es totalmente óptima.
	- **K-Fold Cross Validation**: 
		- Lo hacemos para cada hiperparámetro y sus posibles valores.
		- Dividir el set de datos de entrenamiento en `k` partes. 
		- Se entrenará un modelo usando `k-1` partes, evaluándolo sobre el restante (el `Fold`). 
		- Eso lo hacemos `k` veces (una vez por cada parte).
4) **Entrenamiento del modelo**
5) **Evaluación del modelo**

### Regresión Lineal

- Variable `Y` que se quiere predecir.
- Serie de otras `p` variables (`x1`, `x2`, ..., `xp`)
- Encontrar una relación entre la variable dependiente `Y` y las otras `p` variables.
- Expresar a la variable dependiente `Y` en función del resto de las variables.

```
Tomando 'n' observaciones:

y_i = β_0 + β_1*x_i1 + ... + β_p*x_ip + ϵ_i
∀i = 1, ..., n
``` 

- Se tiene un error aleatorio (`ϵ_i`) en la predicción.

```
Matricialmente:
Y = X β + ϵ
```

Obtenemos los `β` con **mínimos cuadrados**.

### KNN

- Al tener una nueva observación, se encuentran los `k` puntos del set de entrenamiento **más cercanos** y se los usa para predecir.

#### Algoritmo

1. Calcular la distancia de la nueva observación a cada punto del *training set* (se obtiene un vector de distancias).
2. Ordenar el vector de menor a mayor.
3. Seleccionar los primeros `k` puntos de ese vector ordenado.
4. Usar los `k` puntos para predecir:
	- **Clasificación**: clasificar por voto de mayoría
	- **Regresión**: obtener el promedio

#### Hiperparámetros a optimizar

- `k`: cantidad de vecinos a utilizar
- Métrica de distancia (euclídea, Manhattan, etc)
- Algoritmo de pesos

### Regresión logística

- Algoritmo de **clasificación**.
- Usar regresión para modelar la **probabilidad** de que una observación pertenezca a una categoría particular.
- Los valores predichos no están necesariamente entre 0 y 1: se usa la **función logística**.
- La regresión se inserta dentro de la función logística, en el exponente de `e`.
- Para estimar los parámetros se usa el **método de máxima verosimilitud**, a ser resuelto numéricamente.

### 5) Evaluación de Modelo

#### Error de Regresión

- Error absoluto medio (MAE)
- Error cuadrático medio (MSE)

#### Error de Clasificación

- Matriz de confusión

|              |    Valor Predicho 0     |    Valor Predicho 1     |
| :----------: | :---------------------: | :---------------------: |
| **Valor Real 0** | Verdadero Negativo (VN) |   Falso Positivo (FP)   |
| **Valor Real 1** |   Falso Negativo (FN)   | Verdadero Positivo (VP) |
- **Accuracy** = `(VN + VP) / n`
- **Sensibilidad (Recall)** = `VP / (VP + FN)`
- **Especificidad** = `VN / (VN + FP)`
- **Precisión** = `VP / (VP + FP)`

**CURVAS ROC**

- **Eje vertical**: `sensibilidad`
- **Eje horizontal**: `1 - especificidad`
- Puntos clave:
	- `(0, 1)`: FN=0 y FP=0 (mejor posible)
	- `(1, 0)`: VN=0 y VP=0 (todo incorrecto)
	- `(0, 0)`: VP=0 y FP=0 (siempre predice negativo)
	- `(1, 1)`: FN=0 y VP=0 (siempre predice positivo)

### Underfitting

- Modelo no es lo suficientemente complejo para capturar patrones presentes en el *training set*,
- Síntomas: 
	- Error de entrenamiento alto
	- Error de validación alto
- Se soluciona **cambiando a un modelo más complejo**.

### Overfitting

- El modelo se ajusta demasiado a los datos y **no generaliza bien a nuevos datos**.
- Síntomas: 
	- Error de entrenamiento bajo
	- Error de validación alto
- Se soluciona **cambiando a un modelo más simple** o **consiguiendo más datos**.