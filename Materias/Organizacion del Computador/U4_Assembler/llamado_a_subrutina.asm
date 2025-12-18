
; Ejemplo de llamado a subrutina: Restar 3 -2 = 1

        .org 100h
        
        ;Carga de datos de prueba
        mov [200h], 3h
        mov [201h], 2h
        
        ;Busqueda de operandos
        mov AL, [200h]
        mov AH, [201h] 
        
        ;Llamado a subrutina
        call resta
        
        ;Almacenamiento de resultado
        mov [202h], AL 
        
        ;Salto a fin
        jmp fin

        
        ;Subrutina resta
        resta PROC          ;Inicio de la subrutina
        sub AL, AH          ;Resta: AL = AL - AH
        ret                 ;Retorno de la subrutina
        resta ENDP          ;Fin de la subrutina

        
        ;Fin del programa
fin:
        int 20h
        ret

              
              


