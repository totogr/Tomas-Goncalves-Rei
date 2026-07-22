package dominio;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReglasDeJugadaTest {
    private final Jugada piedra = Jugada.PIEDRA;
    private final Jugada papel = Jugada.PAPEL;
    private final Jugada tijera = Jugada.TIJERA;

    @Test void piedraLeGanaATijera() { assertEquals(piedra, piedra.vs(tijera)); }
    @Test void piedraPierdeContraPapel() { assertEquals(papel, piedra.vs(papel)); }
    @Test void papelLeGanaAPiedra() { assertEquals(papel, papel.vs(piedra)); }
    @Test void empateDevuelveLaMismaJugada() { assertEquals(tijera, tijera.vs(tijera)); }
}