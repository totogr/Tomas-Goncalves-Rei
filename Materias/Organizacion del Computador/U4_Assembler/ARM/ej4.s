.global _start
_start:
	
    .section .data               // Sección de datos
cant: .word 3                    // Número de elementos en el arreglo
arreglo: .word 1, 2, 3  // Arreglo de 8 elementos
resultado: .word 0               // Variable para almacenar el resultado de la suma

    .section .text               // Sección de código
    .global _start               // Punto de entrada

_start:
    LDR R0, =cant                // Carga la dirección de 'cant' en R0
    LDR R1, [R0]                 // Carga el número de elementos en R1
    LDR R2, =arreglo             // Carga la dirección de 'arreglo' en R2
    MOV R3, #0                   // Inicializa el acumulador en R3 a 0

suma_arreglo:
    CMP R1, #0                   // Compara el contador con 0
    BEQ salir                 // Si el contador es 0, salta al final

    LDR R4, [R2], #4             // Carga el valor actual de 'arreglo' en R4 y avanza R2 al siguiente elemento
    MOV R5, R4                   // Copia el valor de R4 en R5 para usarlo como contador de la multiplicación
    MOV R6, #0                   // Inicializa el acumulador temporal en R6 (acumulará el cuadrado de R4)

elevar:
    CMP R5, #0                   // Compara el contador R5 con 0
    BEQ agregar_la_suma               // Si el contador es 0, salta para sumar a la suma total
    ADD R6, R6, R4               // Suma el valor de R4 a R6 (acumulador del cuadrado)
    SUB R5, R5, #1               // Decrementa el contador de multiplicación en 1
    B elevar                // Repite el bucle de multiplicación

agregar_la_suma:
    ADD R3, R3, R6               // Suma el cuadrado calculado en R6 al acumulador total R3
    SUB R1, R1, #1               // Decrementa el contador de elementos en 1
    B suma_arreglo             // Repite el bucle para el siguiente elemento

salir:
    LDR R0, =resultado           // Carga la dirección de 'resultado' en R0
    STR R3, [R0]                 // Almacena el acumulador (suma total) en 'resultado'
	
end:
    MOV R7, #1                   // Código de salida (1 para salir)
    SWI 0                        // Llamada al sistema para salir