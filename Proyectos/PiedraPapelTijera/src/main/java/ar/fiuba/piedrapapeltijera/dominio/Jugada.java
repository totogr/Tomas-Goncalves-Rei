package ar.fiuba.piedrapapeltijera.dominio;

public enum Jugada {
    PIEDRA("Piedra"),
    PAPEL("Papel"),
    TIJERA("Tijera");

    private final String descripcion;

    Jugada(String descripcion) { this.descripcion = descripcion; }
    public String descripcion() { return descripcion; }
}
