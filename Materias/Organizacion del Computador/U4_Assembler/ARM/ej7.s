       .section .data
        .align 2                   // Alinear a 4 bytes (2^2 = 4)
texto:   .ascii "hola\n"            // Cadena de texto en minúsculas a convertir
        .align 2                   // Alinear a 4 bytes antes de 'len'
len:     .word 4                    // Longitud de la cadena (sin incluir el '\n')

        .section .text
        .global _start

_start:
        LDR R0, =texto             // Cargar la dirección de la cadena en R0
        LDR R1, =len               // Cargar la dirección de 'len' en R1
        LDR R1, [R1]               // Cargar la longitud en R1

loop:
        LDRB R2, [R0], #1          // Cargar el siguiente byte (carácter) en R2 y avanzar R0
        CMP R2, #'a'               // Comparar con el ASCII de 'a'
        BLT no_conversion          // Si el carácter es menor que 'a', no es minúscula
        CMP R2, #'z'               // Comparar con el ASCII de 'z'
        BGT no_conversion          // Si el carácter es mayor que 'z', no es minúscula

        // Convertir a mayúscula restando el valor ASCII de 'a' y sumando 'A'
        SUB R2, R2, #('a' - 'A')    // R2 = R2 - ('a' - 'A')

no_conversion:
        STRB R2, [R0, #-1]         // Guardar el carácter convertido o sin cambios

        // Disminuir la longitud y verificar si terminamos
        SUBS R1, R1, #1            // R1 = R1 - 1, actualizar las banderas
        BNE loop                   // Si R1 no es cero, repetir

        // Fin del programa
        MOV R7, #1                 // Código de salida (1 para salida en Linux)
        SWI 0                      // Llamada al sistema para salir
