package ar.fiuba.piedrapapeltijera.dominio;

import java.util.Map;
import java.util.function.Function;

public class ActualizadorDeMarcador {
    public static final Map<Resultado, Function<Marcador, Marcador>> POR_RESULTADO = Map.of(
            Resultado.GANADA, marcador -> new Marcador(marcador.ganadas() + 1, marcador.perdidas(), marcador.empates()),
            Resultado.PERDIDA, marcador -> new Marcador(marcador.ganadas(), marcador.perdidas() + 1, marcador.empates()),
            Resultado.EMPATE, marcador -> new Marcador(marcador.ganadas(), marcador.perdidas(), marcador.empates() + 1)
    );

    private ActualizadorDeMarcador() {
    }
}
