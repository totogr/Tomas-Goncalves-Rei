.section .data               // Sección de datos
dividendo:   .word 20              // El número a dividir
divisor:     .word 3               // El número por el cual dividir
cociente:    .word 0               // Variable para almacenar el cociente
resto:       .word 0               // Variable para almacenar el resto

        .section .text
        .global _start

_start:
        // Cargar el dividendo y el divisor en los registros
        LDR R1, =dividendo         // Cargar la dirección de 'dividendo' en R1
        LDR R1, [R1]               // Cargar el valor de 'dividendo' en R1
        LDR R2, =divisor           // Cargar la dirección de 'divisor' en R2
        LDR R2, [R2]               // Cargar el valor de 'divisor' en R2

        // Inicializar el cociente a 0
        MOV R3, #0                 // R3 almacenará el cociente

loop_division:
        // Comparar el dividendo (R1) con el divisor (R2)
        CMP R1, R2
        BLT end_division           // Si el dividendo es menor que el divisor, salir del bucle

        // Restar el divisor del dividendo
        SUB R1, R1, R2             // R1 = R1 - R2

        // Incrementar el cociente
        ADD R3, R3, #1             // Incrementar el cociente

        // Volver a verificar si podemos seguir dividiendo
        B loop_division

end_division:
        // Almacenamos el cociente y el resto
        LDR R4, =cociente          // Cargar la dirección de 'cociente' en R4
        STR R3, [R4]               // Guardar el cociente en la memoria
        LDR R4, =resto             // Cargar la dirección de 'resto' en R4
        STR R1, [R4]               // Guardar el resto (R1) en la memoria

        // Salir del programa
        MOV R7, #1                 // Código de salida (1 para salida en Linux)
        SWI 0                      // Llamada al sistema para salir