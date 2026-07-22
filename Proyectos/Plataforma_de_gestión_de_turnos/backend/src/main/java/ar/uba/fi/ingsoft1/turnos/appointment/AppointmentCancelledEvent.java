package ar.uba.fi.ingsoft1.turnos.appointment;

import java.time.ZonedDateTime;

/**
 * Se publica cuando un turno se cancela, para que la lista de espera promueva los
 * slots afectados sin que {@link BookingService} dependa directamente de
 * {@code WaitListService} (evita el ciclo de beans).
 */
public record AppointmentCancelledEvent(
        Long professionalId,
        Long serviceId,
        ZonedDateTime slotStart,
        int serviceDurationMinutes,
        int slotInterval) {
}
