
; Escribir en la pantalla la palabra microprocesador y a continuación la
; misma palabra invertida de derecha a izquierda. 

org 100h
                    
    mov ax, 0B800h
    mov ds, ax   
    
    mov bx, 0000h
    
    mov cx, 0020h
    
    mov dx, 'M'     
    call f
    mov dx, 'i'
    call f    
    mov dx, 'c'
    call f
    mov dx, 'r'
    call f
    mov dx, 'o'
    call f
    mov dx, 'p'
    call f
    mov dx, 'r'
    call f
    mov dx, 'o'
    call f
    mov dx, 'c'
    call f
    mov dx, 'e'
    call f
    mov dx, 's'
    call f 
    mov dx, 'a'
    call f
    mov dx, 'd'
    call f
    mov dx, 'o'
    call f
    mov dx, 'r'
    call f  
    mov dx, ' '
    call f    
    mov dx, 'r'
    call f
    mov dx, 'o'
    call f          
    mov dx, 'd'
    call f 
    mov dx, 'a'
    call f
    mov dx, 's'
    call f
    mov dx, 'e'
    call f      
    mov dx, 'c'
    call f
    mov dx, 'o'
    call f
    mov dx, 'r'
    call f
    mov dx, 'p'
    call f
    mov dx, 'o'
    call f
    mov dx, 'r'
    call f
    mov dx, 'c'
    call f
    mov dx, 'i'
    call f
    mov dx, 'M'     
    call f         
ret

f:  mov [bx], dx
    inc bx
    mov [bx], 0Fh
    inc bx  
    ret        




