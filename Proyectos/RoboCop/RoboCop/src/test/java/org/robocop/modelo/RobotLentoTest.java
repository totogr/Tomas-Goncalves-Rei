package org.robocop.modelo;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import org.robocop.modelo.Robot.EstadoRobot;
import org.robocop.modelo.Robot.Robot;
import org.robocop.modelo.Robot.RobotLento;

import static org.junit.jupiter.api.Assertions.*;

class RobotLentoTest {

    @Test
    void testGetPosicion() {
        Posicion inicial = new Posicion(5, 5);
        Robot robot = new RobotLento(inicial);
        assertEquals(inicial, robot.getPosicion());
    }

    @Test
    void testGetEstado() {
        RobotLento robot = new RobotLento(new Posicion(1, 1));
        assertEquals(EstadoRobot.INTACTO, robot.getEstado());

    }

    @Test
    void testDestruir() {
        RobotLento robot = new RobotLento(new Posicion(1, 1));
        robot.destruir();
        assertEquals(EstadoRobot.INCENDIADO, robot.getEstado());
    }

    @Test
    void testCalcularTrayectoria() {
        RobotLento robot = new RobotLento(new Posicion(0, 0));
        Posicion objetivo = new Posicion(1, 1);
        List<Posicion> trayectoria = robot.calcularTrayectoria(objetivo, List.of());

        assertEquals(1, trayectoria.size());
        assertEquals(new Posicion(1, 1), trayectoria.get(0));
    }

    @Test
    void testMoverA() {
        RobotLento robot = new RobotLento(new Posicion(0, 0));
        Posicion nueva = new Posicion(2, 2);

        robot.moverA(nueva);

        assertEquals(nueva, robot.getPosicion());
    }

    @Test
    void testToString() {
        Posicion posicionInicial = new Posicion(2, 3);
        RobotLento robot = new RobotLento(posicionInicial);

        String esperado = "RobotLento{posicion=" + posicionInicial + ", estado=INTACTO}";
        assertEquals(esperado, robot.toString());
    }

    @Test
    void testCalcularTrayectoria2(){
        RobotLento robot = new RobotLento(new Posicion(0, 0));
        Posicion objetivo = new Posicion(2, 2);
        List<Posicion> bloqueadas = List.of(new Posicion(1, 1));
        System.out.println(robot.toString());
        System.out.println("Objetivo "+ objetivo.toString());
        System.out.println("Bloqueo "+bloqueadas.get(0).toString());
        Posicion trayectoria = robot.calcularTrayectoria(objetivo, bloqueadas).get(0);
        System.out.println("Trayectoria "+ trayectoria.toString());
        robot.moverA(trayectoria);
        System.out.println(robot);
        trayectoria = robot.calcularTrayectoria(objetivo, bloqueadas).get(0);
        System.out.println("Trayectoria "+ trayectoria.toString());
        robot.moverA(trayectoria);
        System.out.println(robot);
        trayectoria = robot.calcularTrayectoria(objetivo, bloqueadas).get(0);
        System.out.println("Trayectoria "+ trayectoria.toString());
        robot.moverA(trayectoria);
        System.out.println(robot);
        System.out.println("Trayectoria "+ trayectoria.toString());
        robot.moverA(trayectoria);
        System.out.println(robot);

    }

    @Test
    void testCalcularTrayectoria3(){
        RobotLento robot = new RobotLento(new Posicion(0, 0));
        Posicion objetivo = new Posicion(2, 2);
        List<Posicion> bloqueadas = List.of(new Posicion(1, 1));
        System.out.println(robot.toString());
        System.out.println("Objetivo "+ objetivo.toString());
        System.out.println("Bloqueo "+bloqueadas.get(0).toString());
        Posicion trayectoria = robot.calcularTrayectoria(objetivo, bloqueadas).get(0);
        System.out.println("Trayectoria "+ trayectoria.toString());
        robot.moverA(trayectoria);
        System.out.println(robot);
        trayectoria = robot.calcularTrayectoria(objetivo, bloqueadas).get(0);
        System.out.println("Trayectoria "+ trayectoria.toString());
        robot.moverA(trayectoria);
        System.out.println(robot);
        objetivo=objetivo.mover(1,1);
        System.out.println("Nueva Posicion Objetivo "+ objetivo);
        trayectoria = robot.calcularTrayectoria(objetivo, bloqueadas).get(0);
        System.out.println("Trayectoria "+ trayectoria.toString());
        robot.moverA(trayectoria);
        System.out.println(robot);
        trayectoria = robot.calcularTrayectoria(objetivo, bloqueadas).get(0);
        System.out.println("Trayectoria "+ trayectoria.toString());
        robot.moverA(trayectoria);
        System.out.println(robot);
    }
    @Test
    void testCalcularTrayectoria4(){
        RobotLento robot = new RobotLento(new Posicion(0, 0));
        Posicion objetivo = new Posicion(5, 0);
        List<Posicion> bloqueadas = new ArrayList<>();

        for (int col = -1; col <= 5; col++) {
            bloqueadas.add(new Posicion(-1, col));
            bloqueadas.add(new Posicion(7, col));
        }

        for (int fila = 0; fila <= 6; fila++) {
            bloqueadas.add(new Posicion(fila, -1));
            bloqueadas.add(new Posicion(fila, 5));
        }
        bloqueadas.add(new Posicion(2,0));
        System.out.println(robot.toString());

        System.out.println("Objetivo "+ objetivo.toString());
        //System.out.println("Bloqueo "+bloqueadas.get(0).toString());
        Posicion trayectoria = robot.calcularTrayectoria(objetivo, bloqueadas).get(0);
        System.out.println("Trayectoria "+ trayectoria.toString());
        robot.moverA(trayectoria);
        System.out.println(robot);
        trayectoria = robot.calcularTrayectoria(objetivo, bloqueadas).get(0);
        System.out.println("Trayectoria "+ trayectoria.toString());
        robot.moverA(trayectoria);
        System.out.println(robot);
        trayectoria = robot.calcularTrayectoria(objetivo, bloqueadas).get(0);
        System.out.println("Trayectoria "+ trayectoria.toString());
        robot.moverA(trayectoria);
        System.out.println(robot);
        trayectoria = robot.calcularTrayectoria(objetivo, bloqueadas).get(0);
        System.out.println("Trayectoria "+ trayectoria.toString());
        robot.moverA(trayectoria);
        System.out.println(robot);
        trayectoria = robot.calcularTrayectoria(objetivo, bloqueadas).get(0);
        System.out.println("Trayectoria "+ trayectoria.toString());
        robot.moverA(trayectoria);
        System.out.println(robot);
        trayectoria = robot.calcularTrayectoria(objetivo, bloqueadas).get(0);
        System.out.println("Trayectoria "+ trayectoria.toString());
        robot.moverA(trayectoria);
        System.out.println(robot);
    }
    @Test
    void testCalcularTrayectoria5(){
        RobotLento robot = new RobotLento(new Posicion(0, 4));
        Posicion objetivo = new Posicion(5, 4);
        List<Posicion> bloqueadas = new ArrayList<>();

        for (int col = -1; col <= 5; col++) {
            bloqueadas.add(new Posicion(-1, col));
            bloqueadas.add(new Posicion(7, col));
        }

        for (int fila = 0; fila <= 6; fila++) {
            bloqueadas.add(new Posicion(fila, -1));
            bloqueadas.add(new Posicion(fila, 5));
        }
        bloqueadas.add(new Posicion(2,4));
        System.out.println(robot.toString());

        System.out.println("Objetivo "+ objetivo.toString());
        //System.out.println("Bloqueo "+bloqueadas.get(0).toString());
        Posicion trayectoria = robot.calcularTrayectoria(objetivo, bloqueadas).get(0);
        System.out.println("Trayectoria "+ trayectoria.toString());
        robot.moverA(trayectoria);
        System.out.println(robot);
        trayectoria = robot.calcularTrayectoria(objetivo, bloqueadas).get(0);
        System.out.println("Trayectoria "+ trayectoria.toString());
        robot.moverA(trayectoria);
        System.out.println(robot);
        trayectoria = robot.calcularTrayectoria(objetivo, bloqueadas).get(0);
        System.out.println("Trayectoria "+ trayectoria.toString());
        robot.moverA(trayectoria);
        System.out.println(robot);
        trayectoria = robot.calcularTrayectoria(objetivo, bloqueadas).get(0);
        System.out.println("Trayectoria "+ trayectoria.toString());
        robot.moverA(trayectoria);
        System.out.println(robot);
        trayectoria = robot.calcularTrayectoria(objetivo, bloqueadas).get(0);
        System.out.println("Trayectoria "+ trayectoria.toString());
        robot.moverA(trayectoria);
        System.out.println(robot);
        trayectoria = robot.calcularTrayectoria(objetivo, bloqueadas).get(0);
        System.out.println("Trayectoria "+ trayectoria.toString());
        robot.moverA(trayectoria);
        System.out.println(robot);
    }
}