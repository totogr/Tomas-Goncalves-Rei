package dominio;

import java.util.Objects;

public final class EstadoDelJuego {
    private final Marcador marcador;
    private final Ronda ultimaRonda;

    public EstadoDelJuego(Marcador marcador, Ronda ultimaRonda) { this.marcador = marcador; this.ultimaRonda = ultimaRonda; }
    public static EstadoDelJuego inicial() { return new EstadoDelJuego(Marcador.inicial(), null); }
    public Marcador marcador() { return marcador; }
    public Ronda ultimaRonda() { return ultimaRonda; }
    public EstadoDelJuego actualizarCon(Ronda ronda) { return new EstadoDelJuego(marcador.actualizarCon(ronda.resultado()), ronda); }
    @Override public boolean equals(Object objeto) { return objeto instanceof EstadoDelJuego estadoDelJuego && Objects.equals(marcador, estadoDelJuego.marcador) && Objects.equals(ultimaRonda, estadoDelJuego.ultimaRonda); }
    @Override public int hashCode() { return Objects.hash(marcador, ultimaRonda); }
    @Override public String toString() { return "EstadoDelJuego[marcador=%s, ultimaRonda=%s]".formatted(marcador, ultimaRonda); }
}
