#!/bin/bash

carpeta="../llegadas_y_recargas/pruebas_catedra"

archivo="$carpeta/Resultados Esperados.txt"

programa="../tp2.py"

for test_file in "$carpeta"/*.txt; do
    filename=$(basename "$test_file")
    if [ "$filename" = "Resultados Esperados.txt" ]; then
        continue
    fi

    echo "Ejecutando test con archivo: $filename"

    output=$(python3 "$programa" "$test_file")

    estrategia_obtenida=$(echo "$output" | grep "Estrategia:" | sed 's/^Estrategia: //')
    cantidad_obtenida=$(echo "$output" | grep "Cantidad de tropas eliminadas:" | awk -F': ' '{print $2}' | awk -F';' '{print $1}' | tr -d ' ')

    estrategia_esperada=$(awk -v t="$filename" '
        $0 == t {getline; print substr($0, index($0,$2))}
    ' "$archivo" | sed 's/^Estrategia: //')

    cantidad_esperada=$(awk -v t="$filename" '
        $0 == t {getline; getline; print $5}
    ' "$archivo" | tr -d ' ')


    if [ "$cantidad_obtenida" = "$cantidad_esperada" ] && [ "$estrategia_obtenida" = "$estrategia_esperada" ]; then
        echo "✅ Test $filename PASÓ — Estrategia y cantidad correctas"
    else
        echo "❌❌ Test $filename FALLÓ"
        if [ "$cantidad_obtenida" != "$cantidad_esperada" ]; then
            echo "   ❌ Cantidad esperada: $cantidad_esperada — Obtenida: $cantidad_obtenida"
        fi
        if [ "$cantidad_obtenida" == "$cantidad_esperada" ]; then
            echo "   ✅ Cantidad esperada: $cantidad_esperada — Obtenida: $cantidad_obtenida"
        fi
        if [ "$estrategia_obtenida" != "$estrategia_esperada" ]; then
            echo "   ❌ Estrategia esperada ❌ Estrategia obtenida"
        fi
        if [ "$estrategia_obtenida" == "$estrategia_esperada" ]; then
            echo "   ✅Estrategia esperada ✅ Estrategia obtenida"
        fi
    fi

    echo "-----------------------------"
done