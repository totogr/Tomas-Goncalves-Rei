package ar.uba.fi.ingsoft1.turnos.appointment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AppointmentStatusUpdaterTest {

    private static final ZoneId ZONE = ZoneId.of("America/Argentina/Buenos_Aires");

    private AppointmentRepository appointmentRepository;
    private AppointmentStatusUpdater updater;

    @BeforeEach
    void setUp() {
        appointmentRepository = mock(AppointmentRepository.class);
        updater = new AppointmentStatusUpdater(appointmentRepository);
    }

    private Appointment confirmedEndedAppointment() {
        Appointment appt = new Appointment();
        appt.setId(1L);
        appt.setStatus("CONFIRMED");
        appt.setStart(ZonedDateTime.now(ZONE).minusHours(2));
        appt.setEnd(ZonedDateTime.now(ZONE).minusHours(1));
        return appt;
    }

    @Test
    void marksConfirmedPastAppointmentsAsCompleted() {
        Appointment appt = confirmedEndedAppointment();
        when(appointmentRepository.findByStatusAndEndBefore(eq("CONFIRMED"), any()))
                .thenReturn(List.of(appt));

        updater.markCompletedAppointments();

        assertEquals("COMPLETED", appt.getStatus());
        verify(appointmentRepository).saveAll(List.of(appt));
    }

    @Test
    void doesNothingWhenNoAppointmentsToComplete() {
        when(appointmentRepository.findByStatusAndEndBefore(eq("CONFIRMED"), any()))
                .thenReturn(List.of());

        updater.markCompletedAppointments();

        verify(appointmentRepository).saveAll(List.of());
    }
}
