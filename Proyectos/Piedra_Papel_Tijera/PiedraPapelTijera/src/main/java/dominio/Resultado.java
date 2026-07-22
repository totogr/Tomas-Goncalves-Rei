package dominio;

public enum Resultado {
    GANADA("Ganaste") { @Override public Marcador actualizarMarcador(Marcador marcador) { return marcador.conUnaGanada(); } },
    PERDIDA("Perdiste") { @Override public Marcador actualizarMarcador(Marcador marcador) { return marcador.conUnaPerdida(); } },
    EMPATE("Empataron") { @Override public Marcador actualizarMarcador(Marcador marcador) { return marcador.conUnEmpate(); } };

    private final String descripcion;

    Resultado(String descripcion) { this.descripcion = descripcion; }
    public String descripcion() { return descripcion; }
    public abstract Marcador actualizarMarcador(Marcador marcador);
}
