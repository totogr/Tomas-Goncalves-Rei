package ar.uba.fi.ingsoft1.turnos.schedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record ScheduleBlockRequestDTO(
        @NotNull(message = "La fecha de bloqueo es obligatoria")
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate blockDate,
        @NotNull(message = "La hora de inicio es obligatoria")
        @JsonFormat(pattern = "HH:mm") LocalTime startTime,
        @NotNull(message = "La hora de fin es obligatoria")
        @JsonFormat(pattern = "HH:mm") LocalTime endTime) {
}