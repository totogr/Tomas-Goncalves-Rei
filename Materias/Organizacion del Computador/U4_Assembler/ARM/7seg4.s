@Conteo regresivo la cantidad de veces que indica la variable veces

@ Simulador https://cpulator.01xz.net/?sys=arm-de1soc

@0  0111111 0x3F  63
@1  0000110 0x06   6
@2  1011011 0x5B   91
@3  1001111 0x4F  79
@4  1100110 0x66  102
@5  1101101 0X6D  109
@6  1111101 0x7D  125
@7  1000111 0x47  71
@8  1111111 0x7F  127
@9  1101111 0x6F  111

.equ DISPLAY, 0xFF200020	 @ la dirección está en la ventana del Sisplay en el simulador
.data					@ variables      
		veces:		.word 10
		@datos:		.word 0x3F, 0x06, 0x5B, 0x4F, 0x66, 0X6D, 0x7D, 0x47, 0x7F, 0x6F
		datos:		.word 111, 127, 71, 125, 109, 102, 79, 91, 6, 63

.global _start
_start:
		ldr r0, =veces       	@ r0 = dirección de la variable
		ldr r2, [r0]			@ r2 = cantidad de veces 
		ldr r0, =datos
		ldr r4, =DISPLAY

    
loop:   cmp r2, #0
		beq salir
		
		ldr r1, [r0]              @ lee el dato
		STR r1, [R4]			@ sale a display

        add r0,r0, #4			@ siguiente dato	
		sub r2,#1 				@ decrementa la cantidad de veces
		
		B loop
salir:
	.end
		
