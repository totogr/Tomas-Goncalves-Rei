#!/bin/bash

# Ruta relativa del archivo donde se encuentran los valores a comparar 
archivo="../batallas/Resultados Esperados.txt"


test_files=$(grep -E '^[0-9]+\.txt$' "$archivo")

for test_file in $test_files; do
    echo "Ejecutando test con archivo de entrada: $test_file"

    output=$(python3 ../orden_batallas.py "../batallas/$test_file")

    coef=$(echo "$output" | grep "Coeficiente:" | awk '{print $2}')

    esperado=$(awk -v t="$test_file" '$0 == t { getline; print $4 }' "$archivo")

    if [ "$coef" = "$esperado" ]; then
        echo "✅ Test $test_file PASÓ, Dado:$coef Esperado: $esperado"
    else
        echo "❌ Test $test_file FALLÓ"
        echo "    Esperado: $esperado - Obtenido: $coef"
    fi

    echo "-----------------------------"
done
