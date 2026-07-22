package ar.uba.fi.ingsoft1.turnos.appointment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public record BookingRequestDTO(
                @JsonProperty("professional_id") @NotNull Long professionalId,
                @JsonProperty("service_id") @NotNull Long serviceId,
                @JsonProperty("employee_id") Long employeeId,
                @NotNull LocalDate date,
                @JsonFormat(pattern = "HH:mm") @NotNull LocalTime time) {
}
