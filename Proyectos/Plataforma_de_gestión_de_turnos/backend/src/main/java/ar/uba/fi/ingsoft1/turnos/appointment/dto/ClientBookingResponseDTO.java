package ar.uba.fi.ingsoft1.turnos.appointment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

public record ClientBookingResponseDTO(
                Long id,
                String status,
                @JsonProperty("cancelled_by") String cancelledBy,
                @JsonProperty("service_name") String serviceName,
                @JsonProperty("professional_name") String professionalName,
                LocalDate date,
                String time,
                @JsonProperty("duration_minutes") Integer durationMinutes,
                String location) {
}