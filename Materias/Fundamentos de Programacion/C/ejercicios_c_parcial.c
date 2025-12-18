// Escribir una función en C que reciba una cadena a través de un puntero a char (char *) y devuelva en un vector de 3 enteros (int *) la cantidad de letras minúsculas (primera posición), 
//letras mayúsculas (segunda posición) y dígitos (tercera posición). No contar caracteres con tildes ni símbolos como espacios, etc.  Nota, la función devuelve void, tanto el char *,
// como el int * se reciben por parámetro.
//Escribir una función recursiva en C que reciba un vector de enteros por parámetro y devuelva la cantidad de elementos pares. 

# include <stdio.h>
# include <ctype.h>


# define MF 3

typedef int vec[MF];

void contador_cadena(vec v, char *ptr){
    int i;
    for (i=0; ptr[i] != '\0'; i++)
        if (islower(ptr[i]))
            v[0] += 1;
        else if (isupper(ptr[i]))
            v[1] += 1;
        else if (isdigit(ptr[i]))
            v[2] += 1;

    printf ("Minusculas: %d\n", v[0]);
    printf ("Mayusculas: %d\n", v[1]);
    printf ("Digitos: %d\n", v[2]);

}

int pares(vec v, int ML){
    if (ML==0){
        return 0; 
    }

    int par = 0;
    if (v[ML-1] % 2 == 0){
        par = 1;
    }
    return par + pares(v, ML - 1);
}

void main(){
    vec contador = {0,0,0};
    char *cadena = "Hola mi DNI es 44824323";

    contador_cadena(contador, cadena);

    vec num = {3,2,9};
    int num_pares = pares(num, MF);
    printf("La cantidad de numero pares es: %d", num_pares);
    return;
}
