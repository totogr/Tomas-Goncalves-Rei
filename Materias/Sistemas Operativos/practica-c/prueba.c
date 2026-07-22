#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>

void criba(int inicio, int fin) {
    int p;
    int pipe_izq[2], pipe_dch[2];

    // Crear el pipe de entrada (izquierda) y salida (derecha)
    if (pipe(pipe_izq) == -1 || pipe(pipe_dch) == -1) {
        perror("pipe");
        exit(1);
    }

    // Primer proceso (el que genera los números)
    if (fork() == 0) {
        // Cerrar el lado de lectura del pipe izquierdo en el proceso hijo
        close(pipe_izq[0]);

        // Enviar la secuencia de números del 2 hasta el número dado
        for (int i = 2; i <= fin; i++) {
            write(pipe_izq[1], &i, sizeof(i));
        }

        // Cerrar el pipe izquierdo después de escribir los números
        close(pipe_izq[1]);
        exit(0);  // Finalizar el primer proceso
    } else {
        // Cerrar el lado de escritura del pipe izquierdo en el proceso padre
        close(pipe_izq[1]);

        // Leer los números del pipe izquierdo
        while (read(pipe_izq[0], &p, sizeof(p)) > 0) {
            // Si el número es primo, lo imprimimos
            printf("primo %d\n", p);

            // Crear un nuevo pipe para filtrar los múltiplos del primo
            if (fork() == 0) {
                // Cerrar el lado de lectura y escritura del pipe izquierdo en el proceso hijo
                close(pipe_izq[0]);

                // Cerrar el pipe derecho que ya no necesitamos
                close(pipe_dch[0]);
                close(pipe_dch[1]);

                exit(0);  // Finalizar el proceso hijo
            }
        }

        close(pipe_izq[0]);  // Cerrar el pipe izquierdo en el proceso principal
    }
    wait(NULL);  // Esperar a que todos los procesos hijos terminen
}

int main(int argc, char *argv[]) {
    if (argc != 2) {
        fprintf(stderr, "Uso: %s <número>\n", argv[0]);
        return 1;
    }

    int n = atoi(argv[1]);

    // Comienza la criba de Eratóstenes
    criba(2, n);

    return 0;
}