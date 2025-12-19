package org.robocop.modelo;

import org.junit.jupiter.api.Test;
import org.robocop.modelo.Robot.EstadoRobot;
import org.robocop.modelo.Robot.Robot;
import org.robocop.modelo.Robot.RobotRapido;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class RobotRapidoTest {

    @Test
    void testGetPosicion() {
        Posicion inicial = new Posicion(5, 5);
        Robot robot = new RobotRapido(inicial);
        assertEquals(inicial, robot.getPosicion());
    }

    @Test
    void testGetEstado() {
        RobotRapido robot = new RobotRapido(new Posicion(1, 1));
        assertEquals(EstadoRobot.INTACTO, robot.getEstado());
    }

    @Test
    void testDestruir() {
        RobotRapido robot = new RobotRapido(new Posicion(1, 1));
        robot.destruir();
        assertEquals(EstadoRobot.INCENDIADO, robot.getEstado());
    }

    @Test
    void testCalcularTrayectoriaSinBloqueos() {
        RobotRapido robot = new RobotRapido(new Posicion(0, 0));
        Posicion objetivo = new Posicion(2, 2);

        List<Posicion> trayectoria = robot.calcularTrayectoria(objetivo, List.of());

        assertEquals(2, trayectoria.size());
        assertEquals(new Posicion(1, 1), trayectoria.get(0));
        assertEquals(new Posicion(2, 2), trayectoria.get(1));
    }


    @Test
    void testMoverA() {
        RobotRapido robot = new RobotRapido(new Posicion(0, 0));
        Posicion nueva = new Posicion(3, 3);

        robot.moverA(nueva);
        assertEquals(nueva, robot.getPosicion());
    }

    @Test
    void testToString() {
        Posicion inicial = new Posicion(1, 1);
        RobotRapido robot = new RobotRapido(inicial);

        String esperado = "RobotRapido{posicion=" + inicial + ", estado=INTACTO}";
        assertEquals(esperado, robot.toString());
    }

    @Test
    void testTrayectoriaConBloqueoExterno() {
        RobotRapido robot = new RobotRapido(new Posicion(0, 0));
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

        bloqueadas.add(new Posicion(2, 0)); // bloquea diagonal directa
        System.out.println("Objetivo:"+ objetivo.toString());
        System.out.println("Bloqueo: [2,0]");

        List<Posicion> trayectoria = robot.calcularTrayectoria(objetivo, bloqueadas);
        System.out.println(robot.toString());
        for (Posicion p : trayectoria) {
            System.out.print(p);
            robot.moverA(p);
        }
        System.out.println();
        System.out.println("Posición final: " + robot.getPosicion());

        trayectoria = robot.calcularTrayectoria(objetivo, bloqueadas);
        System.out.println(robot.toString());
        for (Posicion p : trayectoria) {
            System.out.print(p);
            robot.moverA(p);
        }
        System.out.println();
        System.out.println("Posición final: " + robot.getPosicion());

        trayectoria = robot.calcularTrayectoria(objetivo, bloqueadas);
        System.out.println(robot.toString());
        for (Posicion p : trayectoria) {
            System.out.print(p);
            robot.moverA(p);
        }
        System.out.println();
        System.out.println("Posición final: " + robot.getPosicion());

    }
}