package ar.fiuba.piedrapapeltijera.dominio;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TablaDeResultadosTest {
    private final TablaDeResultados tablaDeResultados = new TablaDeResultados();

    @Test void deberiaIndicarQuePiedraLeGanaATijera() { assertEquals(Resultado.GANADA, tablaDeResultados.obtenerResultado(Jugada.PIEDRA, Jugada.TIJERA)); }
    @Test void deberiaIndicarQuePapelPierdeContraTijera() { assertEquals(Resultado.PERDIDA, tablaDeResultados.obtenerResultado(Jugada.PAPEL, Jugada.TIJERA)); }
    @Test void deberiaIndicarEmpateCuandoLasJugadasSonIguales() { assertEquals(Resultado.EMPATE, tablaDeResultados.obtenerResultado(Jugada.TIJERA, Jugada.TIJERA)); }
}
