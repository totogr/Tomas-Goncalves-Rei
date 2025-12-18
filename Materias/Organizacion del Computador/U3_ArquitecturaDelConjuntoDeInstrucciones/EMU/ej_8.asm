
; Mostrar en la pantalla la suma de 10 datos 

org 100h


.DATA
 
 
DATOSSUMA DB 01h, 02h, 03h, 04h, 05h 
          DB 06h, 07h, 08h, 09h, 0Ah

.CODE
                    
    mov ax, @DATA          
    mov ds, ax 
    xor bx, bx
    xor dl, dl
    mov cx, 0Ah
    
ACUMULAR:  

    add dl, DATOSSUMA[bx]
    inc bx
    loop ACUMULAR


ret