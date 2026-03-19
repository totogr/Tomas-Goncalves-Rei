package ar.fiuba.piedrapapeltijera.dominio;

public record Marcador(int ganadas, int perdidas, int empates) {
    public static Marcador inicial() { return new Marcador(0, 0, 0); }
    public Marcador actualizarCon(Resultado resultado) { return ActualizadorDeMarcador.POR_RESULTADO.get(resultado).apply(this); }
}
