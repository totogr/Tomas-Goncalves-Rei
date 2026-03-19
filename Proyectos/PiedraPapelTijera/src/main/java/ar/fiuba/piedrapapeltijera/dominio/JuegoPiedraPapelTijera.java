package ar.fiuba.piedrapapeltijera.dominio;

public class JuegoPiedraPapelTijera {
    private final FuenteDeJugadaDeLaComputadora fuenteDeJugadaDeLaComputadora;
    private final FabricaDeRondas fabricaDeRondas;
    private EstadoDelJuego estadoDelJuego;

    public JuegoPiedraPapelTijera(FuenteDeJugadaDeLaComputadora fuenteDeJugadaDeLaComputadora, FabricaDeRondas fabricaDeRondas) { this.fuenteDeJugadaDeLaComputadora = fuenteDeJugadaDeLaComputadora; this.fabricaDeRondas = fabricaDeRondas; this.estadoDelJuego = EstadoDelJuego.inicial(); }
    public EstadoDelJuego jugar(Jugada jugadaDelUsuario) { return actualizarEstadoCon(fabricaDeRondas.crearRonda(jugadaDelUsuario, fuenteDeJugadaDeLaComputadora.obtenerJugada())); }
    public EstadoDelJuego obtenerEstadoDelJuego() { return estadoDelJuego; }
    private EstadoDelJuego actualizarEstadoCon(Ronda ronda) { return estadoDelJuego = estadoDelJuego.actualizarCon(ronda); }
}
