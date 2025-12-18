  .equ SWI_Exit, 0x11
    .equ hexbase, 0xFF200020

    .text
    .global _start

_start:
    ldr r0, =hexbase             


    mov r3, #0b1011011          
    lsl r3, #8                    
    add r3, r3, #0b0111111       
    lsl r3, #8                    
    add r3, r3, #0b1011011      
    lsl r3, #8                    
    add r3, r3, #0b1100110
    
    str r3, [r0]                  

salir:
    swi SWI_Exit                