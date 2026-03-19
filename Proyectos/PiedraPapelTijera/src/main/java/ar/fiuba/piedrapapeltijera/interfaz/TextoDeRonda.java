package ar.fiuba.piedrapapeltijera.interfaz;

import ar.fiuba.piedrapapeltijera.dominio.Ronda;

import java.util.Map;
import java.util.function.Function;

public class TextoDeRonda {
    public static final Map<Boolean, Function<Ronda, String>> POR_PRESENCIA_DE_RONDA = Map.of(
            Boolean.TRUE, ronda -> "Vos jugaste %s, la computadora jugó %s y el resultado fue %s".formatted(ronda.jugadaDelUsuario().descripcion(), ronda.jugadaDeLaComputadora().descripcion(), ronda.resultado()),
            Boolean.FALSE, ronda -> "Elegí una opción para comenzar a jugar"
    );

    private TextoDeRonda() {
    }
}
