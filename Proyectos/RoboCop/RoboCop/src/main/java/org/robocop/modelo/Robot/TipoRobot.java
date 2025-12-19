package org.robocop.modelo.Robot;

public enum TipoRobot {
    LENTO,
    RAPIDO;

    @Override
    public String toString() {
        return this.name();
    }
}

