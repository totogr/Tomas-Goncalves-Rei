#include <stdio.h>
#include <string.h>
#include <stdlib.h>

// Escribe un programa en C que realice operaciones básicas (suma, resta, multiplicación y división) entre dos números enteros introducidos por el usuario.

int calculadora (int a, int b, char cuenta){
    int resultado = 0;
    if (cuenta == '+'){
        resultado = a + b;
    }
    else if (cuenta == '-'){
       resultado = a - b;
    }
    else if (cuenta == '*'){
        resultado = a * b;
    }
    else if (cuenta == '/'){
        resultado = a / b;
    }
    return resultado;
}

// Escribe un programa en C que determine si un número entero introducido por el usuario es par o impar.

const char *par_o_impar(int a){
    const char *resultado;
    if ((a % 2 == 0)){
        resultado = "Par\n";
    }
    else{
        resultado = "Impar\n";
    }
    return resultado;
}

/*Escribe un programa en C que solicite al usuario la cantidad de elementos de un arreglo de enteros, 
luego reserve la memoria necesaria usando malloc, permita al usuario llenar el arreglo y 
finalmente imprima los elementos del arreglo. No olvides liberar la memoria al final.*/

# define MF 10

void mem_dinamica(){
    typedef int vec[MF];

    int cant_elementos = MF;

    int *arreglo = NULL;

    arreglo = malloc(cant_elementos * sizeof(int));

    if (arreglo != NULL){
        printf("Se asigno memoria correctamente\n");

        vec valores = {9,1,2,3,4,5,0};
        for (int i = 0; i < cant_elementos; i++){
            arreglo[i] = valores[i];
        }

        printf("Los elementos del arreglo son: \n");
        for (int i = 0; i < cant_elementos; i++){
            printf("%d\n", arreglo[i]);
        }
    }
    else{
        printf("No se asigno correctamente la memoria\n");
    }

    free(arreglo);

}

/*Escribe un programa en C que tenga un arreglo de enteros predefinido y lo ordene en orden ascendente usando el algoritmo de ordenamiento de tu elección 
(por ejemplo, burbuja, selección, inserción, etc.). */

void ordenar(){
    int ent[] = {1,5,9,7,3,4};
    int n = 6;

    for (int i = 0; i < n - 1; i++) {           //Recorro el arreglo
        for (int j = 0; j < n - i - 1; j++) {   //Recorro el arreglo y voy mandando al elemento mas grande a la ulltima posicion
            if (ent[j] > ent[j + 1]) {          //Compara el elemento actual con el siguiente
                // Intercambiar elementos
                int temp = ent[j];
                ent[j] = ent[j + 1];
                ent[j + 1] = temp;
            }
        }
    }
    printf("\nLos elementos ordenados son: \n");
    for (int i = 0; i < n; i++){
        printf("%d\n", ent[i]);
    }
}

/*Escribe un programa en C que tenga un arreglo de enteros predefinido y busque un número entero específico en el arreglo.
El programa debe indicar si el número está presente en el arreglo y, en caso afirmativo, en qué posición se encuentra.
*/

void buscar_num(){
    int arreglo[] = {5,7,6,1,3,4,8,2,1,9};
    int ML = 10;
    int n = 5;
    int i = 0;

    while (i < ML){
        if (arreglo[i] == n){
            printf("\nEl numero %d se encuentra en la posicion %d\n", n, i+1);
        }
        i++;
    }
}

/*Escribe un programa en C que tenga una cadena predefinida y luego imprima la longitud de la cadena.
*/

void largo_cadena(){
    char cad[] = "Hola Como va";
    int longitud = 0;

    while (cad[longitud] != '\0'){
        longitud++;
    }

    printf("La longitud de la cadena es: %d\n", longitud);

}

/* Escribe un programa en C que tenga una cadena predefinida y determine si es un palíndromo 
(es decir, si se lee igual de adelante hacia atrás y de atrás hacia adelante).
*/

void capicua(){
    char cad[] = "neuquen";
    int ML = 7;
    int i = 0;
    int palindromo = 1;

    while ((i < ML / 2) && palindromo == 1){
        if (cad[i] != cad[ML - i - 1]){
            palindromo = 0;
            printf("No es un palindromo");
        }
        i++;
    }
    if (palindromo){
        printf("Es un palindromo");
    }
}

/*Funcion que reciba vector de elementos reales, su ML, y devuelva el mismo vector pero con sus elementos invertidos.
Como maximo 15 elementos, sin vector auxiliar, se debe revertir sobre si mismo*/

#define MFIS 15

typedef int reales[MFIS];

void revertir(reales v, int ML){
    int posicion_max = ML - 1;
    for (int i = 0; i < posicion_max; i++){
        int valor_min = v[i];
        v[i] = v[posicion_max];
        v[posicion_max] = valor_min;
        posicion_max -= 1;
    }
}

//Funcion MAIN()

void main(){
    int cuenta = calculadora(20,10,'/');
    printf("El resultado es: %d\n", cuenta);

    const char *resultado = par_o_impar(8);
    printf("El numero es: %s", resultado);

    mem_dinamica();

    ordenar();
    
    buscar_num();

    largo_cadena();

    capicua();

    reales vector = {1,2,3,4,5,6,7,8,9};
    revertir(vector, 9);
    for (int i = 0; i < 9; i++){
        printf("\n%d", vector[i]);
    }

    return;
}