package org.robocop.modelo.Celda;

import org.robocop.modelo.Jugador;
import org.robocop.modelo.Robot.Robot;

public class CeldaIncendiada implements Celda{

    public CeldaIncendiada() {

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
        return EstadoDeCelda.INCENDIADA;
    }

    @Override
    public String toString() {
        return "[" + getEstado() + "]";
    }

    @Override
    public Celda interactuarConJugador(Jugador jugador){
        jugador.morir();
        return this;
    }
    @Override
    public Celda interactuarConRobot(Robot robot) {
        robot.destruir();
        return this;
    }
    @Override
    public boolean sePuedeUbicarRecurso(){return false;}
}
