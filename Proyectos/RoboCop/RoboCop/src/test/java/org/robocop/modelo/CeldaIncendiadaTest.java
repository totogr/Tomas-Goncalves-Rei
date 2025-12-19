package org.robocop.modelo;

import org.junit.jupiter.api.Test;
import org.robocop.modelo.Celda.Celda;
import org.robocop.modelo.Celda.CeldaIncendiada;
import org.robocop.modelo.Celda.EstadoDeCelda;

import static org.junit.jupiter.api.Assertions.*;

class CeldaIncendiadaTest {

    @Test
    void testEsTransitablePorJugador() {
        Celda celda = new CeldaIncendiada();
        assertTrue(celda.esTransitablePorJugador());
    }

    @Test
    void testEsTransitablePorRobot() {
        Celda celda = new CeldaIncendiada();
        assertTrue(celda.esTransitablePorRobot());
    }

    @Test
    void testGetEstado() {
        Celda celda = new CeldaIncendiada();
        assertEquals(EstadoDeCelda.INCENDIADA, celda.getEstado());
    }

    @Test
    void testCeldaLibreToStringCorrecto() {
        Celda celda = new CeldaIncendiada();
        System.out.println(celda);
        assertEquals("[INCENDIADA]", celda.toString());
    }
    @Test
    void testInteractuarConJugado() {
        Celda celda = new CeldaIncendiada();
        Jugador jugador = new Jugador(new Posicion(0, 0));

        System.out.println(jugador.toString());
        Celda resultado = celda.interactuarConJugador(jugador);
        System.out.println(jugador.toString());
        jugador.mover(1,1);
        System.out.println(jugador.toString());

        assertSame(celda, resultado);
        assertEquals(EstadoJugador.MUERTO, jugador.getEstado());
    }

}