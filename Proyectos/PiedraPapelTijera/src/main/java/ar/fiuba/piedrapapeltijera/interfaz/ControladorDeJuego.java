package ar.fiuba.piedrapapeltijera.interfaz;

import ar.fiuba.piedrapapeltijera.dominio.EstadoDelJuego;
import ar.fiuba.piedrapapeltijera.dominio.JuegoPiedraPapelTijera;
import ar.fiuba.piedrapapeltijera.dominio.Jugada;

public class ControladorDeJuego {
    private final JuegoPiedraPapelTijera juegoPiedraPapelTijera;

    public ControladorDeJuego(JuegoPiedraPapelTijera juegoPiedraPapelTijera) { this.juegoPiedraPapelTijera = juegoPiedraPapelTijera; }
    public EstadoDelJuego jugar(Jugada jugadaDelUsuario) { return juegoPiedraPapelTijera.jugar(jugadaDelUsuario); }
    public EstadoDelJuego obtenerEstadoInicial() { return juegoPiedraPapelTijera.obtenerEstadoDelJuego(); }
}
