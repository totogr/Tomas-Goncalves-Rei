package org.robocop.modelo.Celda;

public enum EstadoDeCelda {
    LIBRE,
    CON_RECURSO,
    INCENDIADA;

    @Override
    public String toString() {
        return switch (this) {
            case LIBRE -> "LIB";
            case CON_RECURSO -> "REC";
            case INCENDIADA -> "INC";
        };
    }
}