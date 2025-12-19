package org.robocop.modelo;

public enum EstadoJugador {
    VIVO,
    MUERTO;

    @Override
    public String toString() {
        return this.name();
    }
}