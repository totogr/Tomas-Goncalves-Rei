@Escribir el número 2 en un display 7 segmentos, luego de un delay escribir el 3.

@ Simulador https://cpulator.01xz.net/?sys=arm-de1soc

.global _start


_start:
    LDR R0, =0xFF200020   // Cargar la dirección de memoria 0xFF200020 en R0
	
 	
	@ PRIMER NÚMERO
    MOV R1, #0x5B         // Cargar el valor 0x5B (1011011 en binario) en R1
    STR R1, [R0]          // Almacenar el valor de R1 en la dirección de memoria apuntada por R0
	
	@ DELAY1
	  MOV R1, #0xFFFFFF
inner_loop1:
    SUBS R1, R1, #1        // Resta 1 a R1 y actualiza las banderas
    BNE inner_loop1         // Si R1 no es cero, repetir el bucle interno

	@ SEGUNDO NÚMERO
	MOV R1, #0x4F         // Cargar el valor 0x5B (1011011 en binario) en R1
    STR R1, [R0]          // Almacenar el valor de R1 en la dirección de memoria apuntada por R0
	   
	@ Fin del programa
    SWI 0x11              // Llamada al sistema para salir




