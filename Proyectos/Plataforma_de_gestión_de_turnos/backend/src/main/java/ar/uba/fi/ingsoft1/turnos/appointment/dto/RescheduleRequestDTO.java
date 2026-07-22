package ar.uba.fi.ingsoft1.turnos.appointment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public record RescheduleRequestDTO(
        @NotNull LocalDate date,
        @JsonFormat(pattern = "HH:mm") @NotNull LocalTime time) {
}
