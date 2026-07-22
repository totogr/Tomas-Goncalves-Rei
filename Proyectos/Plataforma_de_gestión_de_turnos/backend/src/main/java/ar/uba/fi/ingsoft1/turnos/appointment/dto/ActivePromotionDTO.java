package ar.uba.fi.ingsoft1.turnos.appointment.dto;

import java.time.ZonedDateTime;

public record ActivePromotionDTO(
        Long id,
        Long clientId,
        String clientName,
        String clientEmail,
        Long serviceId,
        String serviceName,
        ZonedDateTime slotStart,
        ZonedDateTime expiresAt,
        Integer durationMinutes
) {}