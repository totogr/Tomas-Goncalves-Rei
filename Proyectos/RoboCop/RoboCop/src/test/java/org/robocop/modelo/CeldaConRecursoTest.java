package org.robocop.modelo;

import org.junit.jupiter.api.Test;
import org.robocop.modelo.Celda.Celda;
import org.robocop.modelo.Celda.CeldaConRecurso;
import org.robocop.modelo.Celda.CeldaLibre;
import org.robocop.modelo.Celda.EstadoDeCelda;

import static org.junit.jupiter.api.Assertions.*;

class CeldaConRecursoTest {

    @Test
    void testEsTransitablePorJugador() {
        Celda celda = new CeldaConRecurso();
        assertTrue(celda.esTransitablePorJugador());
    }

    @Test
    void testEsTransitablePorRobot() {
        Celda celda = new CeldaConRecurso();
        assertFalse(celda.esTransitablePorRobot());
    }

    @Test
    void testGetEstado() {
        Celda celda = new CeldaConRecurso();
        assertEquals(EstadoDeCelda.CON_RECURSO, celda.getEstado());
    }

    @Test
    void testToString() {
        Celda celda = new CeldaConRecurso();
        System.out.println(celda);
        assertEquals("[CON_RECURSO]", celda.toString());
    }

    @Test
    void testInteractuarConJugador() {
        Celda celdaConRecurso = new CeldaConRecurso();
        Jugador jugador = new Jugador(new Posicion(0, 0));

        assertEquals(0, jugador.getRecursosRecolectados());
        System.out.println(jugador.toString());
        Celda resultado = celdaConRecurso.interactuarConJugador(jugador);
        System.out.println(jugador.toString());
        assertEquals(1, jugador.getRecursosRecolectados());
        assertTrue(resultado instanceof CeldaLibre);
        assertEquals(EstadoJugador.VIVO, jugador.getEstado());
    }

}