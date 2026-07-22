package dominio;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayerTest {
    @Test void deberiaPoderUsarseEnSistemasMasGrandes() { assertEquals(Jugada.PAPEL, crearJugadorDeLaComputadora().getMove().vs(crearJugadorHumano().getMove())); }

    private Player crearJugadorDeLaComputadora() { return () -> Jugada.PIEDRA; }
    private Player crearJugadorHumano() { return () -> Jugada.PAPEL; }
}