package ar.uba.fi.ingsoft1.turnos.common.exception;

public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(String entity, Long id) {
        super(String.format("No se encontró %s con id %d", entity, id));
    }

    public ItemNotFoundException(String message) {
        super(message);
    }
}
