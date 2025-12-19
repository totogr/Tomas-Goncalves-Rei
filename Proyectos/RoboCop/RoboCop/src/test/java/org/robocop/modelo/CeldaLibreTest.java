package org.robocop.modelo;

import org.junit.jupiter.api.Test;
import org.robocop.modelo.Celda.Celda;
import org.robocop.modelo.Celda.CeldaLibre;
import org.robocop.modelo.Celda.EstadoDeCelda;

import static org.junit.jupiter.api.Assertions.*;

class CeldaLibreTest {

    @Test
    void testEsTransitablePorJugador() {
        Celda celda = new CeldaLibre();
        assertTrue(celda.esTransitablePorJugador());
    }

    @Test
    void testEsTransitablePorRobot() {
        Celda celda = new CeldaLibre();
        assertTrue(celda.esTransitablePorRobot());
    }

    @Test
    void testGetEstado() {
        Celda celda = new CeldaLibre();
        assertEquals(EstadoDeCelda.LIBRE, celda.getEstado());
    }

    @Test
    void testCeldaLibreToStringCorrecto() {
        Celda celda = new CeldaLibre();
        System.out.println(celda);
        assertEquals("[LIBRE]", celda.toString());
    }
    @Test
    void testInteractuarConJugado() {
        Celda celda = new CeldaLibre();
        Jugador jugador = new Jugador(new Posicion(0, 0));

        System.out.println(jugador.toString());
        Celda resultado = celda.interactuarConJugador(jugador);
        System.out.println(jugador.toString());

        assertSame(celda, resultado);
        assertEquals(EstadoJugador.VIVO, jugador.getEstado());
    }
}