package dominio;

public class JuegoPiedraPapelTijera {
    private final Player jugadorDeLaComputadora;
    private final FabricaDeRondas fabricaDeRondas;
    private EstadoDelJuego estadoDelJuego;

    public JuegoPiedraPapelTijera(Player jugadorDeLaComputadora, FabricaDeRondas fabricaDeRondas) { this.jugadorDeLaComputadora = jugadorDeLaComputadora; this.fabricaDeRondas = fabricaDeRondas; this.estadoDelJuego = EstadoDelJuego.inicial(); }
    public EstadoDelJuego jugar(Jugada jugadaDelUsuario) { return actualizarEstadoCon(fabricaDeRondas.crearRonda(jugadaDelUsuario, jugadorDeLaComputadora.getMove())); }
    public EstadoDelJuego obtenerEstadoDelJuego() { return estadoDelJuego; }
    private EstadoDelJuego actualizarEstadoCon(Ronda ronda) { return estadoDelJuego = estadoDelJuego.actualizarCon(ronda); }
}