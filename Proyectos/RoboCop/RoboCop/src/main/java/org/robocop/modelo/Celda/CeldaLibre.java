package org.robocop.modelo.Celda;

import org.robocop.modelo.Jugador;
import org.robocop.modelo.Robot.Robot;

public class CeldaLibre implements Celda{

    public CeldaLibre() {

    }

    @Override
    public boolean esTransitablePorJugador() {
        return true;
    }

    @Override
    public boolean esTransitablePorRobot() {
        return true;
    }

    @Override
    public EstadoDeCelda getEstado() {
        return EstadoDeCelda.LIBRE;
    }

    @Override
    public String toString() {
        return "[" + getEstado() + "]";
    }

    @Override
    public Celda interactuarConJugador(Jugador jugador) {
        return this;
    }

    @Override
    public Celda interactuarConRobot(Robot robot) {
        return this;
    }
    @Override
    public boolean sePuedeUbicarRecurso(){return true;}
}
