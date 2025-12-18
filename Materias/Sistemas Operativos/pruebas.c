#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>

// Función que verifica si un número es primo
int es_primo(int n) {
    if (n <= 1) return 0;  // 1 no es primo
    for (int i = 2; i * i <= n; i++) {
        if (n % i == 0) {
            return 0;  // No es primo
        }
    }
    return 1;  // Es primo
}

// Función recursiva que filtra los números primos
void filtrar_primos(int pipe_izq[2], int pipe_der[2]) {
    close(pipe_izq[1]);  // Cierra la escritura en el pipe izquierdo
    close(pipe_der[0]);  // Cierra la lectura en el pipe derecho

    int p;
    if (read(pipe_izq[0], &p, sizeof(p)) <= 0) {
        close(pipe_izq[0]);
        close(pipe_der[1]);
        exit(0);
    }

    // Si el número es primo, imprimirlo
    if (es_primo(p)) {
        printf("%d\n", p);  // Imprimir el número primo
    }

    // Crear nuevo pipe para la siguiente etapa
    int pipe_nuevo[2];
    if (pipe(pipe_nuevo) == -1) {
        perror("Error creando pipe");
        exit(1);
    }

    pid_t pid = fork();
    if (pid == -1) {
        perror("Error al hacer fork");
        exit(1);
    }

    if (pid == 0) {
        // Proceso hijo: continúa filtrando de forma recursiva
        filtrar_primos(pipe_nuevo, pipe_der);
    } else {
        // Proceso padre: filtra los números que no son múltiplos del primo
        int n;
        close(pipe_nuevo[1]);  // Cierra la escritura en el nuevo pipe

        while (read(pipe_izq[0], &n, sizeof(n)) > 0) {
            if (n % p != 0) {
                if (write(pipe_nuevo[1], &n, sizeof(n)) == -1) {
                    perror("Error escribiendo en pipe");
                    exit(1);
                }
            }
        }

        close(pipe_izq[0]);
        close(pipe_nuevo[1]);
        wait(NULL);  // Esperar a que termine el proceso hijo
        exit(0);
    }
}

int main(int argc, char *argv[]) {
    if (argc != 2) {
        fprintf(stderr, "Uso: %s <numero_maximo>\n", argv[0]);
        exit(1);
    }

    int max = atoi(argv[1]);

    // Crear el primer pipe
    int pipe_principal[2];
    if (pipe(pipe_principal) == -1) {
        perror("Error creando pipe");
        exit(1);
    }

    pid_t pid = fork();
    if (pid == -1) {
        perror("Error al hacer fork");
        exit(1);
    }

    if (pid == 0) {
        // Proceso hijo: inicia el filtrado de primos
        close(pipe_principal[1]);  // Cierra la escritura en el pipe principal
        int pipe_izq[2];
        pipe(pipe_izq);
        filtrar_primos(pipe_principal, pipe_izq);
    } else {
        // Proceso padre: llena el pipe con números hasta el valor de max
        close(pipe_principal[0]);  // Cierra la lectura en el pipe principal
        for (int i = 2; i <= max; i++) {
            if (write(pipe_principal[1], &i, sizeof(i)) == -1) {
                perror("Error escribiendo en pipe");
                exit(1);
            }
        }

        close(pipe_principal[1]);  // Cierra el pipe cuando ya no se va a escribir
        wait(NULL);  // Espera a que termine el proceso hijo
    }

    return 0;
}