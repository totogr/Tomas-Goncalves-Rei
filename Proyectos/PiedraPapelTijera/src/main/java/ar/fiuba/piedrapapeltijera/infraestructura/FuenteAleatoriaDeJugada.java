package ar.fiuba.piedrapapeltijera.infraestructura;

import ar.fiuba.piedrapapeltijera.dominio.FuenteDeJugadaDeLaComputadora;
import ar.fiuba.piedrapapeltijera.dominio.Jugada;

import java.util.List;
import java.util.Random;

public class FuenteAleatoriaDeJugada implements FuenteDeJugadaDeLaComputadora {
    private static final List<Jugada> JUGADAS = List.of(Jugada.PIEDRA, Jugada.PAPEL, Jugada.TIJERA);
    private final Random generador;

    public FuenteAleatoriaDeJugada(Random generador) { this.generador = generador; }
    @Override public Jugada obtenerJugada() { return JUGADAS.get(generador.nextInt(JUGADAS.size())); }
}
