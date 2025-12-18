# include <stdio.h>

# define MF 10

typedef int vec[MF];

void valor_mas_alto(vec v, int ML){
    int i;
    int max_valor = v[0];
    for (i=1; i <= ML; i++)
        if (v[i] > max_valor)
            max_valor = v[i];
    printf ("%d \n", max_valor);
}

void main(){
    vec vec_1 = {8, -4, 7, 8, 0};
    vec vec_2 = {-4, -7, -10};
    vec vec_3 = {425, -70, 500, 2, 43, 12, -1};

    valor_mas_alto(vec_1, 5);
    valor_mas_alto(vec_2, 3);
    valor_mas_alto(vec_3, 7);

    return;
}