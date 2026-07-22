package infraestructura;

import dominio.Player;
import dominio.Jugada;

import java.util.List;
import java.util.Random;

public class FuenteAleatoriaDeJugada implements Player {
    private static final List<Jugada> JUGADAS = List.of(Jugada.PIEDRA, Jugada.PAPEL, Jugada.TIJERA);
    private final Random generador;

    public FuenteAleatoriaDeJugada(Random generador) { this.generador = generador; }
    @Override public Jugada getMove() { return JUGADAS.get(generador.nextInt(JUGADAS.size())); }
}
