
; Escribir una fila con el abecedario

org 100h
                    
    mov ax, 0B800h
    mov ds, ax   
    
    mov bx, 0000h
     
    mov al, 'A'
    mov cx, 001ah
 f: mov [bx], al
    inc bx
    inc bx
    inc al
    loop f
              

ret




