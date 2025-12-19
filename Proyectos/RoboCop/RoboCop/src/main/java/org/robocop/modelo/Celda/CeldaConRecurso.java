package org.robocop.modelo.Celda;

import org.robocop.modelo.Jugador;
import org.robocop.modelo.Robot.Robot;

public class CeldaConRecurso implements Celda{

    public CeldaConRecurso() {

    }

    @Override
    public boolean esTransitablePorJugador() {
        return true;
    }

    @Override
    public boolean esTransitablePorRobot() {
        return false;
    }

    @Override
    public EstadoDeCelda getEstado() {
        return EstadoDeCelda.CON_RECURSO;
    }

    @Override
    public String toString() {
        return "[" + getEstado() + "]";
    }
    @Override
    public Celda interactuarConJugador(Jugador jugador){
        jugador.recolectarRecurso();
        return new CeldaLibre();
    }
    @Override
    public Celda interactuarConRobot(Robot robot) {
        return this;
    }
    @Override
    public boolean sePuedeUbicarRecurso(){return false;}
}
