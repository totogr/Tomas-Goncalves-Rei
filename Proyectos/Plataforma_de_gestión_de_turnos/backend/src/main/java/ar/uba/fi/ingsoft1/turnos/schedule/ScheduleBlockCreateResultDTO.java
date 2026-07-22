package ar.uba.fi.ingsoft1.turnos.schedule;

public record ScheduleBlockCreateResultDTO(
        ScheduleBlockDTO block,
        int cancelledAppointments) {
}