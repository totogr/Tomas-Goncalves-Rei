package ar.uba.fi.ingsoft1.turnos.schedule;

import java.util.List;

public record WorkingHoursDTO(List<WorkingHoursDayDTO> schedule, Integer slotIntervalMinutes) {
}