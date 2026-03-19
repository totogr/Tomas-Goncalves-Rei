package ar.fiuba.piedrapapeltijera.dominio;

import java.util.Map;

public class TablaDeResultados {
    private static final Map<ClaveDeResultado, Resultado> RESULTADOS = Map.ofEntries(
            Map.entry(new ClaveDeResultado(Jugada.PIEDRA, Jugada.PIEDRA), Resultado.EMPATE),
            Map.entry(new ClaveDeResultado(Jugada.PIEDRA, Jugada.PAPEL), Resultado.PERDIDA),
            Map.entry(new ClaveDeResultado(Jugada.PIEDRA, Jugada.TIJERA), Resultado.GANADA),
            Map.entry(new ClaveDeResultado(Jugada.PAPEL, Jugada.PIEDRA), Resultado.GANADA),
            Map.entry(new ClaveDeResultado(Jugada.PAPEL, Jugada.PAPEL), Resultado.EMPATE),
            Map.entry(new ClaveDeResultado(Jugada.PAPEL, Jugada.TIJERA), Resultado.PERDIDA),
            Map.entry(new ClaveDeResultado(Jugada.TIJERA, Jugada.PIEDRA), Resultado.PERDIDA),
            Map.entry(new ClaveDeResultado(Jugada.TIJERA, Jugada.PAPEL), Resultado.GANADA),
            Map.entry(new ClaveDeResultado(Jugada.TIJERA, Jugada.TIJERA), Resultado.EMPATE)
    );

    public Resultado obtenerResultado(Jugada jugadaDelUsuario, Jugada jugadaDeLaComputadora) { return RESULTADOS.get(new ClaveDeResultado(jugadaDelUsuario, jugadaDeLaComputadora)); }
}
