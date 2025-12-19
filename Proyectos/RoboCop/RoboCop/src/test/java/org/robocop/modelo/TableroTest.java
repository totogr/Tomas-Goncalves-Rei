package org.robocop.modelo;

import org.junit.jupiter.api.Test;
import org.robocop.modelo.Celda.*;
import org.robocop.modelo.Robot.Robot;
import org.robocop.modelo.Robot.RobotLento;
import org.robocop.modelo.Robot.RobotRapido;

import java.util.*;


import static org.junit.jupiter.api.Assertions.*;

class TableroTest {

    @Test
    void testToString() {
        int filas = 6;
        int columnas = 4;
        Tablero tablero = new Tablero(filas, columnas);
        System.out.println(tablero.toString());
    }

    @Test
    void testGetCeldaValida() {
        Tablero tablero = new Tablero(4, 4);
        Posicion posicion = new Posicion(2, 2);
        System.out.println(tablero.toString());

        Celda celda = tablero.getCelda(posicion);
        assertEquals(EstadoDeCelda.LIBRE, celda.getEstado());
    }

    @Test
    void testSetCeldaConRecurso() {
        Tablero tablero = new Tablero(4, 4);
        Posicion posicion = new Posicion(1, 1);

        tablero.setCelda(posicion, new CeldaConRecurso());

        System.out.println(tablero.toString());
        Celda celda = tablero.getCelda(posicion);
        assertEquals(EstadoDeCelda.CON_RECURSO, celda.getEstado());
    }

    @Test
    void testSetCeldaIncendiada() {
        Tablero tablero = new Tablero(4, 4);
        Posicion posicion = new Posicion(1, 1);

        tablero.setCelda(posicion, new CeldaIncendiada());

        System.out.println(tablero.toString());
        Celda celda = tablero.getCelda(posicion);
        assertEquals(EstadoDeCelda.INCENDIADA, celda.getEstado());
    }
    @Test
    void testSetCeldaEnPosicionInvalida() {
        Tablero tablero = new Tablero(4, 4);
        Posicion posicionInvalida = new Posicion(5, 0);

        assertThrows(IllegalArgumentException.class, () -> {
            tablero.setCelda(posicionInvalida, new CeldaConRecurso());
        });
    }

    @Test
    void testMoverJugadorRecolectaRecurso() {
        Tablero tablero = new Tablero(3, 3);
        Posicion posicionInicial = new Posicion(0, 0);
        Jugador jugador = new Jugador(posicionInicial);

        Posicion posicionRecurso = new Posicion(1, 1);
        tablero.setCelda(posicionRecurso, new CeldaConRecurso());

        assertEquals(0, jugador.getRecursosRecolectados());
        assertTrue(tablero.getCelda(posicionRecurso) instanceof CeldaConRecurso);

        tablero.moverJugador(jugador, 1, 1);

        assertEquals(new Posicion(1, 1), jugador.getPosicion());
        assertEquals(1, jugador.getRecursosRecolectados());
        assertTrue(tablero.getCelda(posicionRecurso) instanceof CeldaLibre);
        assertEquals(EstadoJugador.VIVO, jugador.getEstado());
    }

    @Test
    void testMoverJugadorRecolectaRecurso2() {
        Tablero tablero = new Tablero(3, 3);
        Posicion posicionInicial = new Posicion(0, 0);
        Jugador jugador = new Jugador(posicionInicial);
        tablero.setCelda(new Posicion(1, 1), new CeldaConRecurso());
        tablero.setCelda(new Posicion(2, 2), new CeldaConRecurso());

        System.out.println(jugador);
        System.out.println(tablero);
        tablero.moverJugador(jugador,1,1);
        System.out.println(jugador);
        System.out.println(tablero);
        tablero.moverJugador(jugador,1,0);
        System.out.println(jugador);
        System.out.println(tablero);
        tablero.moverJugador(jugador,1,0);
        System.out.println(jugador);
        System.out.println(tablero);
        tablero.moverJugador(jugador,0,1);
        System.out.println(jugador);
        System.out.println(tablero);
    }

    @Test
    void testMoverJugadorCeldaIncendiada() {
        Tablero tablero = new Tablero(4, 4);
        Posicion posicionInicial = new Posicion(0, 0);
        Jugador jugador = new Jugador(posicionInicial);
        tablero.setCelda(new Posicion(1, 1), new CeldaConRecurso());
        tablero.setCelda(new Posicion(2, 2), new CeldaIncendiada());

        System.out.println(jugador);
        System.out.println(tablero);
        tablero.moverJugador(jugador,1,1);
        System.out.println(jugador);
        System.out.println(tablero);
        tablero.moverJugador(jugador,1,1);
        System.out.println(jugador);
        System.out.println(tablero);
        tablero.moverJugador(jugador,1,1);
        System.out.println(jugador);
        System.out.println(tablero);
    }

    @Test
    void testTeletransportarSeguroJugadorARecolectarRecurso() {
        Tablero tablero = new Tablero(4, 4);
        tablero.setCelda(new Posicion(1, 1), new CeldaConRecurso());
        tablero.setCelda(new Posicion(2, 2), new CeldaConRecurso());
        Jugador jugador = new Jugador(new Posicion(0, 0));

        System.out.println(jugador);
        System.out.println(tablero);
        jugador.agregarTeletransportesSeguro(3);
        jugador.agregarTeletransportesSeguro(3);
        System.out.println(jugador);

        tablero.teletransportarSeguroJugador(jugador,new Posicion(1, 1));
        System.out.println(jugador);
        System.out.println(tablero);

        tablero.teletransportarSeguroJugador(jugador,new Posicion(1, 2));
        System.out.println(jugador);
        System.out.println(tablero);

        tablero.teletransportarSeguroJugador(jugador,new Posicion(2, 2));
        System.out.println(jugador);
        System.out.println(tablero);
    }

    @Test
    void testGetPosicionesBloqueadas() {
        Tablero tablero = new Tablero(7, 5);

        tablero.setCelda(new Posicion(1, 1), new CeldaConRecurso());
        tablero.setCelda(new Posicion(2, 3), new CeldaConRecurso());

        List<Posicion> bloqueadas = tablero.getPosicionesBloqueadas();

        for (Posicion pos : bloqueadas) {
            System.out.println(pos);
        }
    }

    /*@Test
    void testMoverRobots(){
        Tablero tablero = new Tablero(7, 7);
        Jugador jugador = new Jugador(new Posicion(4, 6));
        RobotLento lento = new RobotLento(new Posicion(0, 0));
        RobotRapido rapido = new RobotRapido(new Posicion(0, 4));
        tablero.setCelda(new Posicion(1, 1), new CeldaConRecurso());
        tablero.setCelda(new Posicion(1, 5), new CeldaIncendiada());
        tablero.setCelda(new Posicion(2, 1), new CeldaIncendiada());

        List<Robot> robots = List.of(lento, rapido);
        System.out.println(jugador);
        System.out.println(lento);
        System.out.println(rapido);
        System.out.println(tablero);
        tablero.moverRobots(robots, jugador);
        System.out.println(jugador);
        System.out.println(lento);
        System.out.println(rapido);
        System.out.println(tablero);
        tablero.moverRobots(robots, jugador);
        System.out.println(jugador);
        System.out.println(lento);
        System.out.println(rapido);
        System.out.println(tablero);

    }*/

    @Test
    void testMoverRobots2(){
        Tablero tablero = new Tablero(7, 7);
        Jugador jugador = new Jugador(new Posicion(4, 1));
        RobotLento lento1 = new RobotLento(new Posicion(0, 0));
        RobotLento lento2 = new RobotLento(new Posicion(0, 2));
        tablero.setCelda(new Posicion(1, 1), new CeldaConRecurso());
        tablero.setCelda(new Posicion(3, 0), new CeldaConRecurso());
        tablero.setCelda(new Posicion(3, 2), new CeldaConRecurso());


        List<Posicion> bloqueadas = tablero.getPosicionesBloqueadas();

        Posicion trayectoria1 = lento1.calcularTrayectoria(jugador.getPosicion(), bloqueadas).get(0);
        System.out.println(trayectoria1);
        lento1.moverA(trayectoria1);
        Posicion trayectoria2 = lento2.calcularTrayectoria(jugador.getPosicion(), bloqueadas).get(0);
        System.out.println(trayectoria2);
        lento2.moverA(trayectoria2);
        trayectoria1 = lento1.calcularTrayectoria(jugador.getPosicion(), bloqueadas).get(0);
        System.out.println(trayectoria1);
        lento1.moverA(trayectoria1);
        trayectoria2 = lento2.calcularTrayectoria(jugador.getPosicion(), bloqueadas).get(0);
        System.out.println(trayectoria2);
        lento2.moverA(trayectoria2);
        trayectoria1 = lento1.calcularTrayectoria(jugador.getPosicion(), bloqueadas).get(0);
        System.out.println(trayectoria1);
        lento1.moverA(trayectoria1);
        trayectoria2 = lento2.calcularTrayectoria(jugador.getPosicion(), bloqueadas).get(0);
        System.out.println(trayectoria2);
        lento2.moverA(trayectoria2);
        trayectoria1 = lento1.calcularTrayectoria(jugador.getPosicion(), bloqueadas).get(0);
        System.out.println(trayectoria1);
        lento1.moverA(trayectoria1);
        trayectoria2 = lento2.calcularTrayectoria(jugador.getPosicion(), bloqueadas).get(0);
        System.out.println(trayectoria2);

        System.out.println(tablero);

    }

    @Test
    void testMoverRobots3(){
        Tablero tablero = new Tablero(7, 7);
        Jugador jugador = new Jugador(new Posicion(4, 1));
        RobotLento lento1 = new RobotLento(new Posicion(0, 0));
        RobotLento lento2 = new RobotLento(new Posicion(0, 2));
        tablero.setCelda(new Posicion(1, 1), new CeldaConRecurso());
        tablero.setCelda(new Posicion(3, 0), new CeldaConRecurso());
        tablero.setCelda(new Posicion(3, 2), new CeldaConRecurso());

        System.out.println(jugador);
        System.out.println(lento1);
        System.out.println(lento2);
        tablero.moverRobot(lento1,jugador);
        tablero.moverRobot(lento2,jugador);
        System.out.println(jugador);
        System.out.println(lento1);
        System.out.println(lento2);
        tablero.moverRobot(lento1,jugador);
        tablero.moverRobot(lento2,jugador);
        System.out.println(jugador);
        System.out.println(lento1);
        System.out.println(lento2);
        tablero.moverRobot(lento1,jugador);
        tablero.moverRobot(lento2,jugador);
        System.out.println(jugador);
        System.out.println(lento1);
        System.out.println(lento2);


        System.out.println(tablero);

    }

    @Test
    void testMoverRobots4(){
        Tablero tablero = new Tablero(7, 7);
        Jugador jugador = new Jugador(new Posicion(4, 1));
        Robot r1 = new RobotLento(new Posicion(0, 0));
        Robot r2 = new RobotLento(new Posicion(0, 2));
        tablero.setCelda(new Posicion(1, 1), new CeldaConRecurso());
        tablero.setCelda(new Posicion(3, 0), new CeldaConRecurso());
        tablero.setCelda(new Posicion(3, 2), new CeldaConRecurso());
        List<Robot> robots = new ArrayList<>(List.of(r1, r2));


        System.out.println(jugador);
        System.out.println(r1);
        System.out.println(r2);
        System.out.println(tablero);
        tablero.moverRobots(robots,jugador);
        System.out.println(jugador);
        System.out.println(r1);
        System.out.println(r2);
        System.out.println(tablero);
        tablero.moverRobots(robots,jugador);
        System.out.println(jugador);
        System.out.println(r1);
        System.out.println(r2);
        System.out.println(tablero);

    }
    @Test
    void testMoverRobots5(){
        Tablero tablero = new Tablero(7, 7);
        Jugador jugador = new Jugador(new Posicion(4, 1));
        Robot r1 = new RobotLento(new Posicion(0, 0));
        Robot r2 = new RobotLento(new Posicion(0, 2));
        Robot r3 = new RobotLento(new Posicion(4, 3));
        tablero.setCelda(new Posicion(1, 1), new CeldaConRecurso());
        tablero.setCelda(new Posicion(3, 0), new CeldaConRecurso());
        tablero.setCelda(new Posicion(3, 2), new CeldaConRecurso());
        List<Robot> robots = new ArrayList<>(List.of(r1, r2, r3));


        System.out.println(jugador);
        System.out.println(r1);
        System.out.println(r2);
        System.out.println(r3);
        System.out.println(tablero);
        tablero.moverRobots(robots,jugador);
        System.out.println(jugador);
        System.out.println(r1);
        System.out.println(r2);
        System.out.println(r3);
        System.out.println(tablero);
        tablero.moverRobots(robots,jugador);
        System.out.println(jugador);
        System.out.println(r1);
        System.out.println(r2);
        System.out.println(r3);
        System.out.println(tablero);

    }
    @Test
    void testMoverRobots6(){
        Tablero tablero = new Tablero(7, 7);
        Jugador jugador = new Jugador(new Posicion(4, 1));
        Robot r1 = new RobotLento(new Posicion(0, 0));
        Robot r2 = new RobotLento(new Posicion(0, 2));
        Robot r3 = new RobotLento(new Posicion(4, 3));
        tablero.setCelda(new Posicion(1, 1), new CeldaConRecurso());
        tablero.setCelda(new Posicion(3, 0), new CeldaConRecurso());
        tablero.setCelda(new Posicion(3, 2), new CeldaConRecurso());
        tablero.setCelda(new Posicion(4, 2), new CeldaIncendiada());

        List<Robot> robots = new ArrayList<>(List.of(r1, r2, r3));


        System.out.println(jugador);
        System.out.println(r1);
        System.out.println(r2);
        System.out.println(r3);
        System.out.println(tablero);
        tablero.moverRobots(robots,jugador);
        System.out.println(jugador);
        System.out.println(r1);
        System.out.println(r2);
        System.out.println(r3);
        System.out.println(tablero);
        tablero.moverRobots(robots,jugador);
        System.out.println(jugador);
        System.out.println(r1);
        System.out.println(r2);
        System.out.println(r3);
        System.out.println(tablero);

    }
    @Test
    void testMoverRobots7(){
        Tablero tablero = new Tablero(7, 7);
        Jugador jugador = new Jugador(new Posicion(4, 6));
        Robot r1 = new RobotLento(new Posicion(0, 0));
        Robot r2 = new RobotLento(new Posicion(0, 6));
        tablero.setCelda(new Posicion(1, 1), new CeldaConRecurso());
        tablero.setCelda(new Posicion(3, 6), new CeldaConRecurso());
        tablero.setCelda(new Posicion(3, 2), new CeldaConRecurso());
        tablero.setCelda(new Posicion(2, 3), new CeldaIncendiada());

        List<Robot> robots = new ArrayList<>(List.of(r1, r2));


        System.out.println(jugador);
        System.out.println(r1);
        System.out.println(r2);
        System.out.println(tablero);
        tablero.moverRobots(robots,jugador);
        System.out.println(jugador);
        System.out.println(r1);
        System.out.println(r2);
        System.out.println(tablero);
        tablero.moverRobots(robots,jugador);
        System.out.println(jugador);
        System.out.println(r1);
        System.out.println(r2);
        System.out.println(tablero);
        tablero.moverRobots(robots,jugador);
        System.out.println(jugador);
        System.out.println(r1);
        System.out.println(r2);
        System.out.println(tablero);
        tablero.moverRobots(robots,jugador);
        System.out.println(jugador);
        System.out.println(r1);
        System.out.println(r2);
        System.out.println(tablero);

    }

    @Test
    void testMoverRobots8(){
        Tablero tablero = new Tablero(7, 7);
        Jugador jugador = new Jugador(new Posicion(4, 1));
        Robot r1 = new RobotRapido(new Posicion(0, 0));
        Robot r2 = new RobotRapido(new Posicion(0, 2));
        tablero.setCelda(new Posicion(1, 1), new CeldaConRecurso());
        tablero.setCelda(new Posicion(3, 0), new CeldaConRecurso());
        tablero.setCelda(new Posicion(3, 2), new CeldaConRecurso());
        tablero.setCelda(new Posicion(4, 2), new CeldaIncendiada());

        List<Robot> robots = new ArrayList<>(List.of(r1, r2));


        System.out.println(jugador);
        System.out.println(r1);
        System.out.println(r2);
        System.out.println(tablero);
        tablero.moverRobots(robots,jugador);
        System.out.println(jugador);
        System.out.println(r1);
        System.out.println(r2);
        System.out.println(tablero);
        tablero.moverRobots(robots,jugador);
        System.out.println(jugador);
        System.out.println(r1);
        System.out.println(r2);
        System.out.println(tablero);
        tablero.moverRobots(robots,jugador);
        System.out.println(jugador);
        System.out.println(r1);
        System.out.println(r2);
        System.out.println(tablero);
        tablero.moverRobots(robots,jugador);
        System.out.println(jugador);
        System.out.println(r1);
        System.out.println(r2);
        System.out.println(tablero);

    }
    @Test
    void testMoverRobots9(){
        Tablero tablero = new Tablero(7, 7);
        Jugador jugador = new Jugador(new Posicion(4, 1));
        Robot r1 = new RobotRapido(new Posicion(0, 0));
        Robot r2 = new RobotRapido(new Posicion(0, 2));
        tablero.setCelda(new Posicion(1, 1), new CeldaConRecurso());
        tablero.setCelda(new Posicion(3, 0), new CeldaConRecurso());
        tablero.setCelda(new Posicion(3, 2), new CeldaConRecurso());
        tablero.setCelda(new Posicion(4, 2), new CeldaIncendiada());

        List<Robot> robots = new ArrayList<>(List.of(r1, r2));


        System.out.println(jugador);
        System.out.println(r1);
        System.out.println(r2);
        System.out.println(tablero);
        tablero.moverJugador(jugador,1,0);
        tablero.moverRobots(robots,jugador);
        System.out.println(jugador);
        System.out.println(r1);
        System.out.println(r2);
        System.out.println(tablero);

    }
    @Test
    void testMoverRobots10(){
        Tablero tablero = new Tablero(7, 7);
        Jugador jugador = new Jugador(new Posicion(4, 1));
        Robot r1 = new RobotRapido(new Posicion(0, 0));
        Robot r2 = new RobotRapido(new Posicion(0, 2));
        tablero.setCelda(new Posicion(1, 1), new CeldaConRecurso());
        tablero.setCelda(new Posicion(3, 0), new CeldaConRecurso());
        tablero.setCelda(new Posicion(3, 2), new CeldaConRecurso());
        tablero.setCelda(new Posicion(4, 2), new CeldaIncendiada());

        List<Robot> robots = new ArrayList<>(List.of(r1, r2));


        System.out.println(jugador);
        System.out.println(r1);
        System.out.println(r2);
        System.out.println(tablero);
        tablero.moverJugador(jugador,0,1);
        tablero.moverRobots(robots,jugador);
        System.out.println(jugador);
        System.out.println(r1);
        System.out.println(r2);
        System.out.println(tablero);
        tablero.moverRobots(robots,jugador);
        System.out.println(jugador);
        System.out.println(r1);
        System.out.println(r2);
        System.out.println(tablero);

    }
    @Test
    void testMoverRobots11(){
        Tablero tablero = new Tablero(7, 7);
        Jugador jugador = new Jugador(new Posicion(4, 1));
        Robot r1 = new RobotLento(new Posicion(1, 0));
        Robot r2 = new RobotRapido(new Posicion(0, 2));
        tablero.setCelda(new Posicion(1, 1), new CeldaConRecurso());
        tablero.setCelda(new Posicion(3, 0), new CeldaConRecurso());
        tablero.setCelda(new Posicion(3, 2), new CeldaConRecurso());
        tablero.setCelda(new Posicion(5, 1), new CeldaConRecurso());


        List<Robot> robots = new ArrayList<>(List.of(r1, r2));


        System.out.println(jugador);
        System.out.println(r1);
        System.out.println(r2);
        System.out.println(tablero);
        tablero.moverJugador(jugador,1,0);
        tablero.moverRobots(robots,jugador);
        System.out.println(jugador);
        System.out.println(r1);
        System.out.println(r2);
        System.out.println(tablero);
        tablero.moverRobots(robots,jugador);
        System.out.println(jugador);
        System.out.println(r1);
        System.out.println(r2);
        System.out.println(tablero);

    }
    @Test
    void testUbicarRecursos(){
        Tablero tablero = new Tablero(10, 10);

        System.out.println(tablero);
        tablero.ubicarRecursos(5);
        System.out.println(tablero);

    }
}