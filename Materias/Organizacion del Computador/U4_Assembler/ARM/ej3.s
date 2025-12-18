.global _start
_start:
	
    .section .data               // Sección de datos
num: .word 3                     // Número a elevar
resultado: .word 0               // Variable para almacenar el resultado de la suma

    .section .text               // Sección de código
    .global _start               // Punto de entrada del programa

_start:
    LDR R0, =num                 // Carga la dirección de 'num' en R0
    LDR R1, [R0]                 // Carga el valor de 'num' en R1 (número a elevar)
    MOV R3, #0                   // Inicializa el acumulador en R3 a 0

sum_loop:
    CMP R1, #0                   // Compara el contador R1 con 0
    BEQ salir                    // Si el contador es 0, salta a la etiqueta 'salir'

    LDR R4, [R0]                 // Carga el valor actual de 'num' en R4
    ADD R3, R3, R4               // Suma el valor de R4 al acumulador R3
    SUB R1, R1, #1               // Decrementa el contador R1 en 1
    B sum_loop                   // Salta a la etiqueta 'sum_loop' para repetir el bucle

salir:
    LDR R0, =resultado           // Carga la dirección de 'resultado' en R0
    STR R3, [R0]                 // Almacena el valor de R3 (suma total) en 'resultado'
	
end:
    MOV R7, #1                   // Código de salida (1 para invocar el sistema de salida)
    SWI 0                        // Llamada al sistema para salir del programa