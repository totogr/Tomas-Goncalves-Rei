.global _start
_start:
	
	.section .data               // Sección de datos
num1:   .word 4                   // Primer número (4)
num2:   .word 3                   // Segundo número (3)
resultado: .word 0                // Variable para almacenar el resultado

    .section .text                // Sección de código
    .global _start                // Punto de entrada

_start:
    LDR R0, =num1                 // Carga la dirección de num1 en R0
    LDR R1, [R0]                  // Carga el valor de num1 en R1
    LDR R0, =num2                 // Carga la dirección de num2 en R0
    LDR R2, [R0]                  // Carga el valor de num2 en R2

    ADD R3, R1, R2                // Suma los valores de R1 y R2, almacena en R3
	
	LDR R0, =resultado            // Carga la dirección de 'resultado' en R0
    STR R3, [R0]                  // Almacena el valor de R3 en la variable 'resultado'
	
	end:
    MOV R7, #1                    // Código de salida (1 para salida)
    SWI 0                         // Llamada al sistema para salir