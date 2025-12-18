.global _start
_start:
	
    .section .data               // Sección de datos
cant: .word 8                    // Número de elementos en el arreglo
arreglo: .word 1, 2, 3, 4, 5, 6, 7, 8  // Arreglo de 8 elementos
resultado: .word 0               // Variable para almacenar el resultado de la suma

    .section .text               // Sección de código
    .global _start               // Punto de entrada

_start:
    LDR R0, =cant                // Carga la dirección de 'cant' en R0
    LDR R1, [R0]                 // Carga el número de elementos en R1
    LDR R2, =arreglo             // Carga la dirección de 'arreglo' en R2
    MOV R3, #0                   // Inicializa el acumulador en R3 a 0

sum_loop:
    CMP R1, #0                   // Compara el contador con 0
    BEQ salir                    // Si el contador es 0, salta a 'salir'

    LDR R4, [R2], #4             // Carga el valor actual de 'arreglo' en R4 y avanza R2 al siguiente elemento
    ADD R3, R3, R4               // Suma el valor de R4 al acumulador R3
    SUB R1, R1, #1               // Decrementa el contador en 1
    B sum_loop                   // Repite el bucle

salir:
    LDR R0, =resultado           // Carga la dirección de 'resultado' en R0
    STR R3, [R0]                 // Almacena el acumulador (suma total) en 'resultado'
	
end:
    MOV R7, #1                   // Código de salida (1 para salir)
    SWI 0                        // Llamada al sistema para salir