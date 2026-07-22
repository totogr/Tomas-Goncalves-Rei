package ar.uba.fi.ingsoft1.turnos.appointment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.math.BigDecimal;

public record BookingDetailResponseDTO(
                Long id,
                String status,
                @JsonProperty("cancelled_by") String cancelledBy,
                ServiceDetailDTO service,
                ProfessionalDetailDTO professional,
                LocalDate date,
                String start,
                String end,
                Integer score) {

        public record ServiceDetailDTO(
                        Long id,
                        String name,
                        @JsonProperty("duration_minutes") Integer durationMinutes,
                        BigDecimal price) {
        }

        public record ProfessionalDetailDTO(
                        Long id,
                        @JsonProperty("first_name") String firstName,
                        @JsonProperty("last_name") String lastName) {
        }
}