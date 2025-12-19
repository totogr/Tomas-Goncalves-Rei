package org.robocop.modelo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PosicionTest {

    @Test
    void testGetFila() {
        Posicion pos = new Posicion(3, 5);
        assertEquals(3, pos.getFila());
    }

    @Test
    void testGetColumna() {
        Posicion pos = new Posicion(3, 5);
        assertEquals(5, pos.getColumna());
    }

    @Test
    void testSetFila() {
        Posicion pos = new Posicion(0, 0);
        pos.setFila(7);
        assertEquals(7, pos.getFila());
    }

    @Test
    void testSetColumna() {
        Posicion pos = new Posicion(0, 0);
        pos.setColumna(4);
        assertEquals(4, pos.getColumna());
    }

    @Test
    void testEquals() {
        Posicion p1 = new Posicion(2, 3);
        Posicion p2 = new Posicion(2, 3);
        Posicion p3 = new Posicion(3, 4);

        assertEquals(p1, p2);
        assertNotEquals(p1, p3);
    }

    @Test
    void testToString() {
        Posicion pos = new Posicion(1, 2);
        assertEquals("[1, 2]", pos.toString());
    }

    @Test
    void testDistancia() {
        Posicion p1 = new Posicion(0, 0);
        Posicion p2 = new Posicion(3, 4);
        assertEquals(5.0, p1.distancia(p2), 0.001);
    }

    @Test
    void testMover() {
        Posicion pos = new Posicion(2, 3);
        Posicion nuevaPos = pos.mover(1, -1);
        assertEquals(new Posicion(3, 2), nuevaPos);
    }
}