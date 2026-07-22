package ar.uba.fi.ingsoft1.turnos.common.exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        super("Token ausente o expirado.");
    }
}
