
; Pintar la pantalla de la consola con los colores de tu equipo de futbol
; favorito. 

org 100h
                    
    mov ax, 0B800h
    mov ds, ax   
    
    mov bx, 0000h
         
    mov ah, 30
    mov al, 44h
    mov cx, 0020h
f:  inc bx
    mov [bx], ah
    inc bx
    loop f       
    mov cx, 0010h
q:  inc bx
    mov [bx], al
    inc bx
    loop q
    mov cx, 0020h
r:  inc bx
    mov [bx], ah
    inc bx
    loop r
              

ret




