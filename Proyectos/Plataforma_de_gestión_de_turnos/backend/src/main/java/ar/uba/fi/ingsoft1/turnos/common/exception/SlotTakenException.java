package ar.uba.fi.ingsoft1.turnos.common.exception;

public class SlotTakenException extends RuntimeException {
    public SlotTakenException() {
        super("El horario elegido ya no está disponible.");
    }
}
