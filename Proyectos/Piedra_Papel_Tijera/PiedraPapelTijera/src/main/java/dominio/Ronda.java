package dominio;

import java.util.Objects;

public final class Ronda {
    private final Jugada jugadaDelUsuario;
    private final Jugada jugadaDeLaComputadora;
    private final Resultado resultado;
    private final Jugada jugadaGanadora;

    public Ronda(Jugada jugadaDelUsuario, Jugada jugadaDeLaComputadora, Resultado resultado, Jugada jugadaGanadora) { this.jugadaDelUsuario = jugadaDelUsuario; this.jugadaDeLaComputadora = jugadaDeLaComputadora; this.resultado = resultado; this.jugadaGanadora = jugadaGanadora; }
    public Jugada jugadaDelUsuario() { return jugadaDelUsuario; }
    public Jugada jugadaDeLaComputadora() { return jugadaDeLaComputadora; }
    public Resultado resultado() { return resultado; }
    public Jugada jugadaGanadora() { return jugadaGanadora; }
    @Override public boolean equals(Object objeto) { return objeto instanceof Ronda ronda && Objects.equals(jugadaDelUsuario, ronda.jugadaDelUsuario) && Objects.equals(jugadaDeLaComputadora, ronda.jugadaDeLaComputadora) && Objects.equals(resultado, ronda.resultado) && Objects.equals(jugadaGanadora, ronda.jugadaGanadora); }
    @Override public int hashCode() { return Objects.hash(jugadaDelUsuario, jugadaDeLaComputadora, resultado, jugadaGanadora); }
    @Override public String toString() { return "Ronda[jugadaDelUsuario=%s, jugadaDeLaComputadora=%s, resultado=%s, jugadaGanadora=%s]".formatted(jugadaDelUsuario, jugadaDeLaComputadora, resultado, jugadaGanadora); }
}