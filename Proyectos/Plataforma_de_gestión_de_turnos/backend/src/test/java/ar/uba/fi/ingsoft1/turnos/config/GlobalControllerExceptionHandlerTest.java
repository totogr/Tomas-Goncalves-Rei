package ar.uba.fi.ingsoft1.turnos.config;

import ar.uba.fi.ingsoft1.turnos.common.exception.BadRequestException;
import ar.uba.fi.ingsoft1.turnos.common.exception.ConflictException;
import ar.uba.fi.ingsoft1.turnos.common.exception.ForbiddenException;
import ar.uba.fi.ingsoft1.turnos.common.exception.ItemNotFoundException;
import ar.uba.fi.ingsoft1.turnos.common.exception.SlotTakenException;
import ar.uba.fi.ingsoft1.turnos.common.exception.UnauthorizedException;
import ar.uba.fi.ingsoft1.turnos.common.exception.UnprocessableEntityException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

import static org.junit.jupiter.api.Assertions.*;

class GlobalControllerExceptionHandlerTest {

    private final GlobalControllerExceptionHandler handler = new GlobalControllerExceptionHandler();

    @Test
    void badRequestMapsTo400() {
        var response = handler.handleBadRequest(new BadRequestException("SERVICE_INACTIVE"));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("BAD_REQUEST", response.getBody().code());
        assertEquals("SERVICE_INACTIVE", response.getBody().message());
    }

    @Test
    void notFoundMapsTo404() {
        var response = handler.handleNotFound(new ItemNotFoundException("Turno no encontrado"));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("NOT_FOUND", response.getBody().code());
    }

    @Test
    void slotTakenMapsTo409() {
        var response = handler.handleSlotTaken(new SlotTakenException());
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("SLOT_TAKEN", response.getBody().code());
    }

    @Test
    void conflictMapsTo409() {
        var response = handler.handleConflict(new ConflictException("SLOT_FULL"));
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("CONFLICT", response.getBody().code());
    }

    @Test
    void forbiddenMapsTo403() {
        var response = handler.handleForbidden(new ForbiddenException("sin permiso"));
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("FORBIDDEN", response.getBody().code());
    }

    @Test
    void unauthorizedMapsTo401() {
        var response = handler.handleUnauthorized(new UnauthorizedException());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("UNAUTHORIZED", response.getBody().code());
    }

    @Test
    void accessDeniedMapsTo403() {
        var response = handler.handleAccessDenied(new AccessDeniedException("denegado"));
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("FORBIDDEN", response.getBody().code());
    }

    @Test
    void unprocessableEntityMapsTo422() {
        var response = handler.handleUnprocessableEntity(
                new UnprocessableEntityException("No se puede reprogramar un turno cancelado"));
        assertEquals(HttpStatus.UNPROCESSABLE_CONTENT, response.getStatusCode());
        assertEquals("UNPROCESSABLE_ENTITY", response.getBody().code());
    }

    @Test
    void unexpectedExceptionMapsTo500WithoutLeakingDetails() {
        ResponseEntity<GlobalControllerExceptionHandler.ErrorResponse> response =
                handler.handleUnexpected(new NullPointerException("npe interno sensible"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("INTERNAL_ERROR", response.getBody().code());
        // el mensaje interno no debe filtrarse al cliente
        assertFalse(response.getBody().message().contains("npe interno sensible"));
    }
}
