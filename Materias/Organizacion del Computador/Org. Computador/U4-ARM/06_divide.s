@ Este programa reproduce el algoritmo de la división.
@ Ejemplo: 9/2 = 4 resto 1  r1/r2 = r4 resto r3
@ Cociente r4 = 0 inicialmente
@ Resto r3 = 0 inicialmente
@ Algoritmo:
@  r1 = 9 > r2 = 2 entoces r1 = r1-r2 = 7 y r4 = r4 + 1 = 1
@  r1 = 7 > r2 = 2 entoces r1 = r1-r2 = 5 y r4 = r4 + 1 = 2
@  r1 = 5 > r2 = 2 entoces r1 = r1-r2 = 3 y r4 = r4 + 1 = 3
@  r1 = 3 > r2 = 2 entoces r1 = r1-r2 = 1 y r4 = r4 + 1 = 4
@  r1 = 1 < r2 = 2 entoces r4 = 4 y r3 = r1 = 1
@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ 
  
			.data        
			divisor:		.word 2
			dividendo: 		.word 9
			cociente:		.word 0
			resto:			.word 0
			
			.text	
			.global main

			ldr r0, =dividendo
			ldr r1, [r0]
			
			ldr r0, =divisor
			ldr r2, [r0]
			
loop:		cmp r1, r2       @ si el dividendo r1 > divisor r2, cociente r4 +=1 y 
							 @ restarle el divisor al dividendo  r1-r2
			movlt r3,r1      @ si el dividendo r1 < divisor r2 entonces
							 @ resto r3 = dividendo r1 y r4 es el cociente 
			blt fin
			
			sub r1,r2
			add r4,#1
						
			b loop
			
fin:		ldr r0,=resto
			str r3,[r0]
			
			ldr r0,=cociente
			str r4,[r0]
			
			.end
			