package org.robocop.modelo.Celda;

import org.robocop.modelo.Jugador;
import org.robocop.modelo.Robot.Robot;

public interface Celda {
    boolean esTransitablePorJugador();
    boolean esTransitablePorRobot();
    EstadoDeCelda getEstado();
    Celda interactuarConJugador(Jugador jugador);
    Celda interactuarConRobot(Robot robot);
    boolean sePuedeUbicarRecurso();
}

