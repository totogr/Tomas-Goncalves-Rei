@ Este programa setea switches y enciende el display 7 seg con el número correspondiente.
@ Por ejemplo el switch en 000000011 muestra el 3
@ Al comenzar no debe haber un número mayor que 9 en los switches.
@ Para salir poner cualquier número mayor que 9.
@ No es necesario ejecutarlo paso a paso.
@ Simulador https://cpulator.01xz.net/?sys=arm-de1soc

@0  0111111 0x3F
@1  0000110 0x06
@2  1011011 0x5B
@3  1001111 0x4F
@4  1100110 0x66
@5  1101101 0X6D
@6  1111101 0x7D
@7  1000111 0x47
@8  1111111 0x7F
@9  1101111 0x6F



.equ SWITCH, 0xFF200040  @ la dirección está en la ventana del switch en el simulador
.equ DISPLAY, 0xFF200020	 @ la dirección está en la ventana del Sisplay en el simulador
.global _start
_start:
		ldr R4,=DISPLAY

		ldr R3,=SWITCH
		ldr R0,[R3]      @ lectura del dispositivo de entrada almacenada en r0

    CMP R0, #0             // Check if R0 equals 1
    BEQ case_0
	
    CMP R0, #1             // Check if R0 equals 1
    BEQ case_1             // If equal, branch to case_1

    CMP R0, #2             // Check if R0 equals 2
    BEQ case_2             // If equal, branch to case_2

    CMP R0, #3             // Check if R0 equals 3
    BEQ case_3             // If equal, branch to case_3

    CMP R0, #4             // Check if R0 equals 1
    BEQ case_4
	
	CMP R0, #5             // Check if R0 equals 1
    BEQ case_5
	
	CMP R0, #6             // Check if R0 equals 1
	BEQ case_6
		
	CMP R0, #7             // Check if R0 equals 1
    BEQ case_7


    CMP R0, #8             // Check if R0 equals 8
    BEQ case_8             // If equal, branch to case_8

    CMP R0, #9             // Check if R0 equals 1
    BEQ case_9

	B default_case         // If no case matched, branch to default_case

case_0:
    // Code for case 1
    MOV R1, #0x3F            // Example operation
    B muestra                  // Go to muestra


case_1:
    // Code for case 1
    MOV R1, #0x06            // Example operation
    B muestra                  // Go to muestra

case_2:
    // Code for case 2
    MOV R1, #0x5B            // Example operation
    B muestra                  // Go to muestra

case_3:
    // Code for case 3
    MOV R1, #0x4F            // Example operation
    B muestra                  // Go to muestra

case_4:
    // Code for case 8
    MOV R1, #0x66            // Example operation
    B muestra                  // Go to muestra
case_5:
    // Code for case 8
    MOV R1, #0X6D            // Example operation
    B muestra  	// Go to muestra


case_6:
    // Code for case 8
    MOV R1, #0x7D            // Example operation
    B muestra                  // Go to muestra

case_7:
    // Code for case 8
    MOV R1, #0x47            // Example operation
    B muestra                  // Go to muestra


case_8:
    // Code for case 8
    MOV R1, #0x7F            // Example operation
    B muestra                  // Go to muestra

case_9:
    // Code for case 9
    MOV R1, #0x6F            // Example operation
    B muestra                  // Go to muestra

default_case:
    // Code for default case
    B end          // Go to end

muestra:  
	STR R1, [R4]
	B _start
		

		.end