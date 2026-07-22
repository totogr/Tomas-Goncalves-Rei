package interfaz;

import dominio.EstadoDelJuego;
import dominio.JuegoPiedraPapelTijera;
import dominio.Jugada;

public class ControladorDeJuego {
    private final JuegoPiedraPapelTijera juegoPiedraPapelTijera;

    public ControladorDeJuego(JuegoPiedraPapelTijera juegoPiedraPapelTijera) { this.juegoPiedraPapelTijera = juegoPiedraPapelTijera; }
    public EstadoDelJuego jugar(Jugada jugadaDelUsuario) { return juegoPiedraPapelTijera.jugar(jugadaDelUsuario); }
    public EstadoDelJuego obtenerEstadoInicial() { return juegoPiedraPapelTijera.obtenerEstadoDelJuego(); }
}
