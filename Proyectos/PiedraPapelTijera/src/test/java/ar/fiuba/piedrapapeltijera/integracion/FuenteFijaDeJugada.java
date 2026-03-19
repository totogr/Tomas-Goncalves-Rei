package ar.fiuba.piedrapapeltijera.integracion;

import ar.fiuba.piedrapapeltijera.dominio.FuenteDeJugadaDeLaComputadora;
import ar.fiuba.piedrapapeltijera.dominio.Jugada;

public class FuenteFijaDeJugada implements FuenteDeJugadaDeLaComputadora {
    private final Jugada jugada;

    public FuenteFijaDeJugada(Jugada jugada) { this.jugada = jugada; }
    @Override public Jugada obtenerJugada() { return jugada; }
}
