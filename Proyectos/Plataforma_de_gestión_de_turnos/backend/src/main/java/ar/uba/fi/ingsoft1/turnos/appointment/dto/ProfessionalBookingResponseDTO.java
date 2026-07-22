package ar.uba.fi.ingsoft1.turnos.appointment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.time.ZonedDateTime;

public record ProfessionalBookingResponseDTO(
        Long id,
        String status,
        @JsonProperty("cancelled_by") String cancelledBy,
        @JsonProperty("service_name") String serviceName,
        @JsonProperty("client_name") String clientName,
        @JsonProperty("client_email") String clientEmail,
        LocalDate date,
        String time,
        String end,
        @JsonProperty("duration_minutes") Integer durationMinutes,
        @JsonProperty("marked_absent_at") ZonedDateTime markedAbsentAt) {
}