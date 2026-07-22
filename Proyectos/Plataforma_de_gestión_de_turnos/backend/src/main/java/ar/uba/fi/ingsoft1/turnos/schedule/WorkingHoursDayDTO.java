package ar.uba.fi.ingsoft1.turnos.schedule;

import java.util.List;

public record WorkingHoursDayDTO(
        String day,
        boolean enabled,
        List<TimeRangeDTO> ranges) {
}