package ar.fiuba.piedrapapeltijera.dominio;

public record EstadoDelJuego(Marcador marcador, Ronda ultimaRonda) {
    public static EstadoDelJuego inicial() { return new EstadoDelJuego(Marcador.inicial(), null); }
    public EstadoDelJuego actualizarCon(Ronda ronda) { return new EstadoDelJuego(marcador.actualizarCon(ronda.resultado()), ronda); }
}
