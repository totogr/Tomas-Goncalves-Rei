package integracion;

import dominio.Player;
import dominio.Jugada;

public class FuenteFijaDeJugada implements Player {
    private final Jugada jugada;

    public FuenteFijaDeJugada(Jugada jugada) { this.jugada = jugada; }
    @Override public Jugada getMove() { return jugada; }
}
