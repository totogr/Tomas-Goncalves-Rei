package dominio;

public enum Jugada {
    PIEDRA("Piedra") {
        @Override public Jugada vs(Jugada otraJugada) { return otraJugada.resultadoContraPiedra(); }
        @Override protected Jugada resultadoContraPiedra() { return PIEDRA; }
        @Override protected Jugada resultadoContraPapel() { return PAPEL; }
        @Override protected Jugada resultadoContraTijera() { return PIEDRA; }
        @Override public Resultado resultadoContra(Jugada otraJugada) { return otraJugada.resultadoDeEnfrentarPiedra(); }
        @Override protected Resultado resultadoDeEnfrentarPiedra() { return Resultado.EMPATE; }
        @Override protected Resultado resultadoDeEnfrentarPapel() { return Resultado.GANADA; }
        @Override protected Resultado resultadoDeEnfrentarTijera() { return Resultado.PERDIDA; }
    },
    PAPEL("Papel") {
        @Override public Jugada vs(Jugada otraJugada) { return otraJugada.resultadoContraPapel(); }
        @Override protected Jugada resultadoContraPiedra() { return PAPEL; }
        @Override protected Jugada resultadoContraPapel() { return PAPEL; }
        @Override protected Jugada resultadoContraTijera() { return TIJERA; }
        @Override public Resultado resultadoContra(Jugada otraJugada) { return otraJugada.resultadoDeEnfrentarPapel(); }
        @Override protected Resultado resultadoDeEnfrentarPiedra() { return Resultado.PERDIDA; }
        @Override protected Resultado resultadoDeEnfrentarPapel() { return Resultado.EMPATE; }
        @Override protected Resultado resultadoDeEnfrentarTijera() { return Resultado.GANADA; }
    },
    TIJERA("Tijera") {
        @Override public Jugada vs(Jugada otraJugada) { return otraJugada.resultadoContraTijera(); }
        @Override protected Jugada resultadoContraPiedra() { return PIEDRA; }
        @Override protected Jugada resultadoContraPapel() { return TIJERA; }
        @Override protected Jugada resultadoContraTijera() { return TIJERA; }
        @Override public Resultado resultadoContra(Jugada otraJugada) { return otraJugada.resultadoDeEnfrentarTijera(); }
        @Override protected Resultado resultadoDeEnfrentarPiedra() { return Resultado.GANADA; }
        @Override protected Resultado resultadoDeEnfrentarPapel() { return Resultado.PERDIDA; }
        @Override protected Resultado resultadoDeEnfrentarTijera() { return Resultado.EMPATE; }
    };

    private final String descripcion;

    Jugada(String descripcion) { this.descripcion = descripcion; }
    public String descripcion() { return descripcion; }
    public abstract Jugada vs(Jugada otraJugada);
    public abstract Resultado resultadoContra(Jugada otraJugada);
    protected abstract Jugada resultadoContraPiedra();
    protected abstract Jugada resultadoContraPapel();
    protected abstract Jugada resultadoContraTijera();
    protected abstract Resultado resultadoDeEnfrentarPiedra();
    protected abstract Resultado resultadoDeEnfrentarPapel();
    protected abstract Resultado resultadoDeEnfrentarTijera();
}
