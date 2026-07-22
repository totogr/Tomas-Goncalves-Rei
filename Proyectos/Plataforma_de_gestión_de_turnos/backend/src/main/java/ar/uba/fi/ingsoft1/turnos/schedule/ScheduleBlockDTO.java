package ar.uba.fi.ingsoft1.turnos.schedule;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public record ScheduleBlockDTO(
        Long id,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate blockDate,
        @JsonFormat(pattern = "HH:mm") LocalTime startTime,
        @JsonFormat(pattern = "HH:mm") LocalTime endTime) {
}