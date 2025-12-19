package org.robocop.modelo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JugadorTest {

    @Test
    void testGetPosicion() {
        Posicion inicial = new Posicion(5, 5);
        Jugador jugador = new Jugador(inicial);
        assertEquals(inicial, jugador.getPosicion());
    }

    @Test
    void testMover() {
        Posicion inicial = new Posicion(5, 5);
        Jugador jugador = new Jugador(inicial);
        jugador.mover(1, -1);  // Debería quedar en (6, 4)
        assertEquals(new Posicion(6, 4), jugador.getPosicion());
    }

    @Test
    void testTeletransportar() {
        Posicion inicial = new Posicion(5, 5);
        Jugador jugador = new Jugador(inicial);
        Posicion nueva = new Posicion(2, 2);
        jugador.teletransportar(nueva);
        assertEquals(nueva, jugador.getPosicion());
    }

    @Test
    void testAgregarTeletransporteSeguro() {
        Posicion inicial = new Posicion(0, 0);
        Jugador jugador = new Jugador(inicial);
        assertFalse(jugador.puedeTeletransportarse());
        jugador.agregarTeletransporteSeguro();
        assertTrue(jugador.puedeTeletransportarse());
    }

    @Test
    void testPuedeTeletransportarse() {
        Posicion inicial = new Posicion(0, 0);
        Jugador jugador = new Jugador(inicial);
        assertFalse(jugador.puedeTeletransportarse());
        jugador.agregarTeletransporteSeguro();
        assertTrue(jugador.puedeTeletransportarse());
    }

    @Test
    void testUsarTeletransporteSeguro() {
        Posicion inicial = new Posicion(0, 0);
        Jugador jugador = new Jugador(inicial);
        jugador.agregarTeletransporteSeguro();
        assertTrue(jugador.puedeTeletransportarse());

        jugador.usarTeletransporteSeguro(new Posicion(1, 1));
        assertFalse(jugador.puedeTeletransportarse());
    }

    @Test
    void testEstadoInicialVivo() {
        Posicion inicial = new Posicion(0, 0);
        Jugador jugador = new Jugador(inicial);
        assertEquals(EstadoJugador.VIVO, jugador.getEstado());
        assertTrue(jugador.estaVivo());
    }

    @Test
    void testRecolectarRecurso() {
        Posicion inicial = new Posicion(0, 0);
        Jugador jugador = new Jugador(inicial);
        assertEquals(0, jugador.getRecursosRecolectados());

        jugador.recolectarRecurso();
        assertEquals(1, jugador.getRecursosRecolectados());

        jugador.recolectarRecurso();
        assertEquals(2, jugador.getRecursosRecolectados());
    }

    @Test
    void testToString() {
        Posicion inicial = new Posicion(0, 0);
        Jugador jugador = new Jugador(inicial);
        String resultado = jugador.toString();
        assertTrue(resultado.contains("posicion="));
        assertTrue(resultado.contains("estado=VIVO"));
        assertTrue(resultado.contains("teletransportesSeguros=0"));
    }
    @Test
    void testToString2() {
        Posicion inicial = new Posicion(9, 1);
        Jugador jugador = new Jugador(inicial);
        System.out.println(jugador.toString());
    }

    @Test
    void testJugadorMorir(){
        Posicion inicial = new Posicion(9, 1);
        Jugador jugador = new Jugador(inicial);
        System.out.println(jugador.toString());
        jugador.morir();
        System.out.println(jugador.toString());
    }

}