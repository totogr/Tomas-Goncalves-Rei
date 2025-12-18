# include <stdio.h>

# define MF 10

typedef int legajo[MF];


void mostrar_datos(legajo v, int ultima_pos){
    int i;
    for(i=ultima_pos; i>=0; i--)
    printf("%i \n", v[i]);
}

void main(){
    legajo v_nros = {1,1,1,4,0,5};
    mostrar_datos(v_nros, 5);
    return;
}