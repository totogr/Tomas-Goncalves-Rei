package org.robocop.modelo.Robot;


public enum EstadoRobot {
    INTACTO,
    INCENDIADO;

    @Override
    public String toString() {
        return name();
    }
}

