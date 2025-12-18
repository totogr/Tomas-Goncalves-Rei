
; Llenar una fila de la pantalla de la consola con la letra a. 

org 100h
                    
    mov ax, 0B800h
    mov ds, ax   
    
    mov bx, 0000h
     
    mov al, 'A'
    mov ah, 92
    mov cx, 0010h
 f: mov [bx], AL
    inc bx
    mov [bx], AH
    inc bx
    inc AH
    loop f
              

ret




