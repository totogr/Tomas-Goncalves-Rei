package dominio;

import java.util.Objects;

public final class Marcador {
    private final int ganadas;
    private final int perdidas;
    private final int empates;

    public Marcador(int ganadas, int perdidas, int empates) { this.ganadas = ganadas; this.perdidas = perdidas; this.empates = empates; }
    public static Marcador inicial() { return new Marcador(0, 0, 0); }
    public int ganadas() { return ganadas; }
    public int perdidas() { return perdidas; }
    public int empates() { return empates; }
    public Marcador actualizarCon(Resultado resultado) { return resultado.actualizarMarcador(this); }
    public Marcador conUnaGanada() { return new Marcador(ganadas + 1, perdidas, empates); }
    public Marcador conUnaPerdida() { return new Marcador(ganadas, perdidas + 1, empates); }
    public Marcador conUnEmpate() { return new Marcador(ganadas, perdidas, empates + 1); }
    @Override public boolean equals(Object objeto) { return objeto instanceof Marcador marcador && ganadas == marcador.ganadas && perdidas == marcador.perdidas && empates == marcador.empates; }
    @Override public int hashCode() { return Objects.hash(ganadas, perdidas, empates); }
    @Override public String toString() { return "Marcador[ganadas=%d, perdidas=%d, empates=%d]".formatted(ganadas, perdidas, empates); }
}