package ar.uba.fi.ingsoft1.turnos.schedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;

public record TimeRangeDTO(
        @JsonFormat(pattern = "HH:mm") LocalTime from,
        @JsonFormat(pattern = "HH:mm") LocalTime to) {
}