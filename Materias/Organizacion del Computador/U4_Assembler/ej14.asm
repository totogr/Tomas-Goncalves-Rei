;14. Sumar el conjunto de datos de 16 bits que comienzan en la dirección 203
;y tiene una longitud dada por el dato almacenado en la dirección 202.
;Elegir los datos para que su suma sea menor a 65.536. Almacenar el
;resultado a partir de la dirección 200.

org 100h

            MOV CH, 0
            MOV CL, [202h]
            MOV BX, 203h   ;dirección de inicio
            MOV AX, 0      ;acumulador en 0
itera:    
            MOV DX, [BX]   ;observar en DX el nuevo dato 
            ADC AX, [BX]   ;acumula un dato
            INC BX         ;incremento 2 veces porque son 2 bytes por dato
            INC BX
            LOOP itera
            MOV [200h], AX
            ret




