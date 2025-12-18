@Escribir el número 2 en un display 7 segmentos
.global _start


_start:
    LDR R0, =0xFF200020   // Cargar la dirección de memoria 0xFF200020 en R0
    MOV R1, #0x5B         // Cargar el valor 0x5B (1011011 en binario) en R1
    STR R1, [R0]          // Almacenar el valor de R1 en la dirección de memoria apuntada por R0

    // Fin del programa
    SWI 0x11              // Llamada al sistema para salir




