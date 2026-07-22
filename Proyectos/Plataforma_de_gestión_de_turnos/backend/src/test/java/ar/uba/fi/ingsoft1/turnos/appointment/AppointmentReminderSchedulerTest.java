package ar.uba.fi.ingsoft1.turnos.appointment;

import ar.uba.fi.ingsoft1.turnos.client.Client;
import ar.uba.fi.ingsoft1.turnos.client.ClientRepository;
import ar.uba.fi.ingsoft1.turnos.config.email.EmailService;
import ar.uba.fi.ingsoft1.turnos.professional.Professional;
import ar.uba.fi.ingsoft1.turnos.professional.ProfessionalRepository;
import ar.uba.fi.ingsoft1.turnos.service.ServiceEntity;
import ar.uba.fi.ingsoft1.turnos.service.ServiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AppointmentReminderSchedulerTest {

    private static final ZoneId ZONE = ZoneId.of("America/Argentina/Buenos_Aires");

    private AppointmentRepository appointmentRepository;
    private ClientRepository clientRepository;
    private ProfessionalRepository professionalRepository;
    private ServiceRepository serviceRepository;
    private EmailService emailService;

    private AppointmentReminderScheduler scheduler;

    @BeforeEach
    void setUp() {
        appointmentRepository = mock(AppointmentRepository.class);
        clientRepository = mock(ClientRepository.class);
        professionalRepository = mock(ProfessionalRepository.class);
        serviceRepository = mock(ServiceRepository.class);
        emailService = mock(EmailService.class);

        scheduler = new AppointmentReminderScheduler(
                appointmentRepository, clientRepository, professionalRepository,
                serviceRepository, emailService);
    }

    private Appointment appointment() {
        Appointment appt = new Appointment();
        appt.setId(1L);
        appt.setClientId(2L);
        appt.setProfessionalId(3L);
        appt.setServiceId(4L);
        appt.setStatus("CONFIRMED");
        appt.setStart(ZonedDateTime.now(ZONE).plusHours(12));
        appt.setEnd(ZonedDateTime.now(ZONE).plusHours(13));
        return appt;
    }

    private Client clientWithReminders(boolean receives) {
        Client c = new Client("c@mail.com", "x", "Ana", "Lopez");
        c.setId(2L);
        c.setReceivesReminders(receives);
        return c;
    }

    @Test
    void sendsReminderAndMarksItAsSent() {
        Appointment appt = appointment();
        when(appointmentRepository.findAppointmentsForReminder(any(), any())).thenReturn(List.of(appt));
        when(clientRepository.findById(2L)).thenReturn(Optional.of(clientWithReminders(true)));
        Professional pro = new Professional("p@mail.com", "x", "Juan", "Perez");
        when(professionalRepository.findById(3L)).thenReturn(Optional.of(pro));
        ServiceEntity service = new ServiceEntity();
        service.setName("Corte");
        when(serviceRepository.findById(4L)).thenReturn(Optional.of(service));

        scheduler.processReminders();

        verify(emailService).sendAppointmentReminderEmail(
                eq("c@mail.com"), eq("Ana"), eq("Corte"), eq("Juan Perez"), anyString(), anyString());
        assertTrue(appt.getReminderSent());
        verify(appointmentRepository).save(appt);
    }

    @Test
    void skipsWhenClientOptedOutOfReminders() {
        Appointment appt = appointment();
        when(appointmentRepository.findAppointmentsForReminder(any(), any())).thenReturn(List.of(appt));
        when(clientRepository.findById(2L)).thenReturn(Optional.of(clientWithReminders(false)));

        scheduler.processReminders();

        verify(emailService, never()).sendAppointmentReminderEmail(any(), any(), any(), any(), any(), any());
        verify(appointmentRepository, never()).save(any());
    }

    @Test
    void skipsWhenClientNotFound() {
        Appointment appt = appointment();
        when(appointmentRepository.findAppointmentsForReminder(any(), any())).thenReturn(List.of(appt));
        when(clientRepository.findById(2L)).thenReturn(Optional.empty());

        scheduler.processReminders();

        verify(emailService, never()).sendAppointmentReminderEmail(any(), any(), any(), any(), any(), any());
    }
}
