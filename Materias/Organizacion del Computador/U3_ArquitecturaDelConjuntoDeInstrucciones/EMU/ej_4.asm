
; Escribir todo el abecedario con un atributo diferente para cada letra

org 100h
                    
    mov ax, 0B800h
    mov ds, ax   
    
    mov bx, 0000h
     
    mov al, 'A'    
    mov ah, 91
    mov cx, 001ah
 f: mov [bx], al
    inc bx
    mov [bx], ah
    inc bx
    inc ah
    inc al
    loop f
              

ret




