
; Escribir “Organización del Computador” sin acentos en la memoria de
; video con letra roja sobre fondo amarillo

org 100h
                    
    mov ax, 0B800h
    mov ds, ax   
    
    mov bx, 0000h
         
    mov ah, 44h
    mov al, 0EEh 
    
    mov cx, 0020h
    
    mov dx, 'O'     
    call f
    mov dx, 'r'
    call f    
    mov dx, 'g'
    call f
    mov dx, 'a'
    call f
    mov dx, 'n'
    call f
    mov dx, 'i'
    call f
    mov dx, 'z'
    call f
    mov dx, 'a'
    call f
    mov dx, 'c'
    call f
    mov dx, 'i'
    call f
    mov dx, 'o'
    call f 
    mov dx, 'n'
    call f
    mov dx, ' '
    call f
    mov dx, 'd'
    call f
    mov dx, 'e'
    call f
    mov dx, 'l'
    call f
    mov dx, ' '
    call f
    mov dx, 'C'
    call f
    mov dx, 'o'
    call f
    mov dx, 'm'
    call f 
    mov dx, 'p'
    call f
    mov dx, 'u'
    call f
    mov dx, 't'
    call f
    mov dx, 'a'
    call f
    mov dx, 'd'
    call f
    mov dx, 'o'
    call f    
    mov dx, 'r'
    call f
    
ret

f:  mov [bx], dx
    inc bx
    mov [bx], 0E4h
    inc bx  
    ret        




