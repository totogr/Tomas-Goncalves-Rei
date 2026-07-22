package ar.uba.fi.ingsoft1.turnos.appointment;

import ar.uba.fi.ingsoft1.turnos.appointment.dto.BookingRequestDTO;
import ar.uba.fi.ingsoft1.turnos.appointment.dto.RescheduleRequestDTO;
import ar.uba.fi.ingsoft1.turnos.blockedclient.BlockedClientService;
import ar.uba.fi.ingsoft1.turnos.client.Client;
import ar.uba.fi.ingsoft1.turnos.client.ClientRepository;
import ar.uba.fi.ingsoft1.turnos.common.exception.BadRequestException;
import ar.uba.fi.ingsoft1.turnos.common.exception.ConflictException;
import ar.uba.fi.ingsoft1.turnos.common.exception.ForbiddenException;
import ar.uba.fi.ingsoft1.turnos.common.exception.ItemNotFoundException;
import ar.uba.fi.ingsoft1.turnos.common.exception.SlotTakenException;
import ar.uba.fi.ingsoft1.turnos.common.exception.UnprocessableEntityException;
import ar.uba.fi.ingsoft1.turnos.config.email.BookingNotificationEmailService;
import ar.uba.fi.ingsoft1.turnos.professional.Professional;
import ar.uba.fi.ingsoft1.turnos.professional.ProfessionalRepository;
import ar.uba.fi.ingsoft1.turnos.review.ReviewRepository;
import ar.uba.fi.ingsoft1.turnos.schedule.ScheduleBlockRepository;
import ar.uba.fi.ingsoft1.turnos.service.ServiceEntity;
import ar.uba.fi.ingsoft1.turnos.service.ServiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class BookingServiceTest {

    private static final ZoneId ZONE = ZoneId.of("America/Argentina/Buenos_Aires");

    private BookingService bookingService;
    private AppointmentRepository appointmentRepository;
    private ServiceRepository serviceRepository;
    private ProfessionalRepository professionalRepository;
    private ClientRepository clientRepository;
    private ReviewRepository reviewRepository;
    private ScheduleBlockRepository scheduleBlockRepository;
    private BlockedClientService blockedClientService;
    private BookingNotificationEmailService notificationEmailService;
    private ApplicationEventPublisher eventPublisher;
    private WaitListPromotionRepository promotionRepository;

    private static final Long PROF = 1L;
    private static final Long SERVICE = 100L;
    private static final Long CLIENT = 7L;

    @BeforeEach
    void setUp() {
        appointmentRepository = mock(AppointmentRepository.class);
        serviceRepository = mock(ServiceRepository.class);
        professionalRepository = mock(ProfessionalRepository.class);
        clientRepository = mock(ClientRepository.class);
        reviewRepository = mock(ReviewRepository.class);
        scheduleBlockRepository = mock(ScheduleBlockRepository.class);
        blockedClientService = mock(BlockedClientService.class);
        notificationEmailService = mock(BookingNotificationEmailService.class);
        eventPublisher = mock(ApplicationEventPublisher.class);
        promotionRepository = mock(WaitListPromotionRepository.class);

        bookingService = new BookingService(
                appointmentRepository, serviceRepository, professionalRepository,
                clientRepository, reviewRepository, scheduleBlockRepository,
                blockedClientService, notificationEmailService, eventPublisher,
                promotionRepository);

        when(blockedClientService.isBlocked(anyLong(), anyLong())).thenReturn(false);
        when(scheduleBlockRepository.findByProfessionalIdAndBlockDateOrderByStartTimeAsc(any(), any()))
                .thenReturn(List.of());
        when(promotionRepository.findActivePromotionsForDay(any(), any(), any(), any()))
                .thenReturn(List.of());
    }

    private ServiceEntity service(int duration, Integer maxCapacity) {
        ServiceEntity s = new ServiceEntity();
        s.setDuration(duration);
        s.setActive(true);
        s.setMaxCapacity(maxCapacity);
        return s;
    }

    private Professional professional() {
        Professional p = new Professional("pro@mail.com", "x", "Juan", "Perez");
        p.setId(PROF);
        return p;
    }

    private Client client() {
        Client c = new Client("cli@mail.com", "x", "Ana", "Lopez");
        c.setId(CLIENT);
        return c;
    }

    private Appointment occupied(LocalDate date, LocalTime start, LocalTime end, Long id) {
        Appointment a = new Appointment();
        a.setId(id);
        a.setProfessionalId(PROF);
        a.setServiceId(SERVICE);
        a.setStatus("CONFIRMED");
        a.setStart(ZonedDateTime.of(date, start, ZONE));
        a.setEnd(ZonedDateTime.of(date, end, ZONE));
        return a;
    }

    // ── createBooking ─────────────────────────────────────────────────────────

    @Test
    void createBookingHappyPathSavesConfirmedAndNotifies() {
        LocalDate date = LocalDate.now(ZONE).plusDays(2);
        when(serviceRepository.findById(SERVICE)).thenReturn(Optional.of(service(60, null)));
        when(appointmentRepository.findActiveAppointmentsForUpdate(eq(PROF), eq(SERVICE), any(), any()))
                .thenReturn(List.of());
        when(appointmentRepository.save(any())).thenAnswer(inv -> {
            Appointment a = inv.getArgument(0);
            a.setId(500L);
            return a;
        });
        when(professionalRepository.findById(PROF)).thenReturn(Optional.of(professional()));
        when(clientRepository.findById(CLIENT)).thenReturn(Optional.of(client()));

        Appointment saved = bookingService.createBooking(
                new BookingRequestDTO(PROF, SERVICE, null, date, LocalTime.of(10, 0)), CLIENT);

        assertEquals("CONFIRMED", saved.getStatus());
        assertEquals(CLIENT, saved.getClientId());
        verify(appointmentRepository).save(any());
        verify(notificationEmailService).notifyProfessionalNewBooking(
                eq("pro@mail.com"), any(), any(), eq("cli@mail.com"), any(), any(), any());
        verify(notificationEmailService).notifyClientNewBooking(
                eq("cli@mail.com"), any(), any(), any(), any(), any());
    }

    @Test
    void createBookingThrowsWhenServiceInactive() {
        LocalDate date = LocalDate.now(ZONE).plusDays(2);
        ServiceEntity inactive = service(60, null);
        inactive.setActive(false);
        when(serviceRepository.findById(SERVICE)).thenReturn(Optional.of(inactive));

        assertThrows(BadRequestException.class, () -> bookingService.createBooking(
                new BookingRequestDTO(PROF, SERVICE, null, date, LocalTime.of(10, 0)), CLIENT));
    }

    @Test
    void createBookingThrowsWhenClientBlocked() {
        when(blockedClientService.isBlocked(PROF, CLIENT)).thenReturn(true);
        LocalDate date = LocalDate.now(ZONE).plusDays(2);

        assertThrows(ItemNotFoundException.class, () -> bookingService.createBooking(
                new BookingRequestDTO(PROF, SERVICE, null, date, LocalTime.of(10, 0)), CLIENT));
    }

    @Test
    void createBookingThrowsSlotTakenWhenGroupCapacityFull() {
        LocalDate date = LocalDate.now(ZONE).plusDays(2);
        when(serviceRepository.findById(SERVICE)).thenReturn(Optional.of(service(30, 2)));
        // capacidad 2 y ya hay 2 reservas en el bloque 10:00-10:30
        when(appointmentRepository.findActiveAppointmentsForUpdate(eq(PROF), eq(SERVICE), any(), any()))
                .thenReturn(List.of(
                        occupied(date, LocalTime.of(10, 0), LocalTime.of(10, 30), 1L),
                        occupied(date, LocalTime.of(10, 0), LocalTime.of(10, 30), 2L)));

        assertThrows(SlotTakenException.class, () -> bookingService.createBooking(
                new BookingRequestDTO(PROF, SERVICE, null, date, LocalTime.of(10, 0)), CLIENT));
    }

    @Test
    void createBookingSucceedsWhenGroupCapacityHasRoom() {
        LocalDate date = LocalDate.now(ZONE).plusDays(2);
        when(serviceRepository.findById(SERVICE)).thenReturn(Optional.of(service(30, 2)));
        // capacidad 2, solo 1 reserva existente -> hay lugar
        when(appointmentRepository.findActiveAppointmentsForUpdate(eq(PROF), eq(SERVICE), any(), any()))
                .thenReturn(List.of(occupied(date, LocalTime.of(10, 0), LocalTime.of(10, 30), 1L)));
        when(appointmentRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(professionalRepository.findById(PROF)).thenReturn(Optional.empty());

        Appointment saved = bookingService.createBooking(
                new BookingRequestDTO(PROF, SERVICE, null, date, LocalTime.of(10, 0)), CLIENT);

        assertEquals("CONFIRMED", saved.getStatus());
    }

    // ── rescheduleBooking ─────────────────────────────────────────────────────

    private Appointment ownedFutureAppointment() {
        Appointment appt = new Appointment();
        appt.setId(9L);
        appt.setClientId(CLIENT);
        appt.setProfessionalId(PROF);
        appt.setServiceId(SERVICE);
        appt.setStatus("CONFIRMED");
        appt.setStart(ZonedDateTime.now(ZONE).plusDays(3).withHour(10).withMinute(0));
        appt.setEnd(appt.getStart().plusMinutes(60));
        return appt;
    }

    @Test
    void rescheduleThrowsWhenNotOwner() {
        Appointment appt = ownedFutureAppointment();
        appt.setClientId(999L);
        when(appointmentRepository.findById(9L)).thenReturn(Optional.of(appt));

        LocalDate date = LocalDate.now(ZONE).plusDays(4);
        assertThrows(ForbiddenException.class, () -> bookingService.rescheduleBooking(
                9L, CLIENT, new RescheduleRequestDTO(date, LocalTime.of(11, 0))));
    }

    @Test
    void rescheduleThrowsWhenAppointmentCancelled() {
        Appointment appt = ownedFutureAppointment();
        appt.setStatus("CANCELLED");
        when(appointmentRepository.findById(9L)).thenReturn(Optional.of(appt));

        LocalDate date = LocalDate.now(ZONE).plusDays(4);
        assertThrows(UnprocessableEntityException.class, () -> bookingService.rescheduleBooking(
                9L, CLIENT, new RescheduleRequestDTO(date, LocalTime.of(11, 0))));
    }

    @Test
    void rescheduleThrowsConflictWhenTargetSlotFull() {
        Appointment appt = ownedFutureAppointment();
        when(appointmentRepository.findById(9L)).thenReturn(Optional.of(appt));
        when(serviceRepository.findById(SERVICE)).thenReturn(Optional.of(service(30, 1)));

        LocalDate date = LocalDate.now(ZONE).plusDays(4);
        when(appointmentRepository.findActiveAppointmentsForUpdate(eq(PROF), eq(SERVICE), any(), any()))
                .thenReturn(List.of(occupied(date, LocalTime.of(11, 0), LocalTime.of(11, 30), 2L)));

        assertThrows(ConflictException.class, () -> bookingService.rescheduleBooking(
                9L, CLIENT, new RescheduleRequestDTO(date, LocalTime.of(11, 0))));
    }

    @Test
    void rescheduleHappyPathUpdatesStartAndNotifies() {
        Appointment appt = ownedFutureAppointment();
        when(appointmentRepository.findById(9L)).thenReturn(Optional.of(appt));
        when(serviceRepository.findById(SERVICE)).thenReturn(Optional.of(service(60, 1)));
        when(appointmentRepository.findActiveAppointmentsForUpdate(eq(PROF), eq(SERVICE), any(), any()))
                .thenReturn(List.of());
        when(appointmentRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(professionalRepository.findById(PROF)).thenReturn(Optional.of(professional()));
        when(clientRepository.findById(CLIENT)).thenReturn(Optional.of(client()));

        LocalDate date = LocalDate.now(ZONE).plusDays(4);
        bookingService.rescheduleBooking(9L, CLIENT, new RescheduleRequestDTO(date, LocalTime.of(15, 0)));

        ZonedDateTime expectedStart = ZonedDateTime.of(date, LocalTime.of(15, 0), ZONE);
        assertEquals(expectedStart, appt.getStart());
        verify(appointmentRepository).save(appt);
        verify(notificationEmailService).notifyClientReschedule(any(), any(), any(), any(), any(), any());
    }

    // ── cancelBooking (cliente) ───────────────────────────────────────────────

    @Test
    void cancelBookingMarksCancelledAndPromotesWaitlist() {
        Appointment appt = ownedFutureAppointment();
        when(appointmentRepository.findById(9L)).thenReturn(Optional.of(appt));
        when(serviceRepository.findById(SERVICE)).thenReturn(Optional.of(service(60, null)));
        when(professionalRepository.findById(PROF)).thenReturn(Optional.of(professional()));
        when(clientRepository.findById(CLIENT)).thenReturn(Optional.of(client()));

        bookingService.cancelBooking(9L, CLIENT);

        assertEquals("CANCELLED", appt.getStatus());
        assertEquals("client", appt.getCancelledBy());
        assertNotNull(appt.getCancelledDate());
        verify(eventPublisher).publishEvent(any(AppointmentCancelledEvent.class));
        verify(notificationEmailService).notifyProfessionalCancellation(
                eq("pro@mail.com"), any(), any(), any(), any(), any());
    }

    @Test
    void cancelBookingThrowsWhenNotOwner() {
        Appointment appt = ownedFutureAppointment();
        appt.setClientId(999L);
        when(appointmentRepository.findById(9L)).thenReturn(Optional.of(appt));

        assertThrows(ForbiddenException.class, () -> bookingService.cancelBooking(9L, CLIENT));
    }

    @Test
    void cancelBookingThrowsWhenAlreadyPast() {
        Appointment appt = ownedFutureAppointment();
        appt.setStart(ZonedDateTime.now(ZONE).minusHours(2));
        when(appointmentRepository.findById(9L)).thenReturn(Optional.of(appt));

        assertThrows(BadRequestException.class, () -> bookingService.cancelBooking(9L, CLIENT));
    }

    @Test
    void cancelBookingThrowsWhenNotFound() {
        when(appointmentRepository.findById(9L)).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () -> bookingService.cancelBooking(9L, CLIENT));
    }

    // ── cancelBookingByProfessional ───────────────────────────────────────────

    @Test
    void cancelByProfessionalMarksCancelledAndNotifiesClient() {
        Appointment appt = ownedFutureAppointment();
        when(appointmentRepository.findById(9L)).thenReturn(Optional.of(appt));
        when(serviceRepository.findById(SERVICE)).thenReturn(Optional.of(service(60, null)));
        when(professionalRepository.findById(PROF)).thenReturn(Optional.of(professional()));
        when(clientRepository.findById(CLIENT)).thenReturn(Optional.of(client()));

        bookingService.cancelBookingByProfessional(9L, PROF);

        assertEquals("CANCELLED", appt.getStatus());
        assertEquals("professional", appt.getCancelledBy());
        verify(notificationEmailService).notifyClientCancelledByProfessional(
                eq("cli@mail.com"), any(), any(), any(), any(), any());
    }

    @Test
    void cancelByProfessionalThrowsWhenNotOwner() {
        Appointment appt = ownedFutureAppointment();
        appt.setProfessionalId(999L);
        when(appointmentRepository.findById(9L)).thenReturn(Optional.of(appt));

        assertThrows(ForbiddenException.class, () -> bookingService.cancelBookingByProfessional(9L, PROF));
    }

    // ── markAbsent ────────────────────────────────────────────────────────────

    @Test
    void markAbsentIncrementsClientAbsenceCount() {
        Appointment appt = ownedFutureAppointment();
        appt.setStart(ZonedDateTime.now(ZONE).minusHours(2));
        appt.setEnd(ZonedDateTime.now(ZONE).minusHours(1));
        when(appointmentRepository.findById(9L)).thenReturn(Optional.of(appt));
        Client client = client();
        when(clientRepository.findById(CLIENT)).thenReturn(Optional.of(client));

        bookingService.markAbsent(9L, PROF);

        assertNotNull(appt.getMarkedAbsentAt());
        assertEquals(1, client.getAbsenceCount());
        verify(clientRepository).save(client);
    }

    @Test
    void markAbsentThrowsWhenAppointmentInFuture() {
        Appointment appt = ownedFutureAppointment();
        when(appointmentRepository.findById(9L)).thenReturn(Optional.of(appt));

        assertThrows(BadRequestException.class, () -> bookingService.markAbsent(9L, PROF));
    }

    @Test
    void markAbsentThrowsWhenAlreadyMarked() {
        Appointment appt = ownedFutureAppointment();
        appt.setStart(ZonedDateTime.now(ZONE).minusHours(2));
        appt.setMarkedAbsentAt(ZonedDateTime.now(ZONE).minusHours(1));
        when(appointmentRepository.findById(9L)).thenReturn(Optional.of(appt));

        assertThrows(BadRequestException.class, () -> bookingService.markAbsent(9L, PROF));
    }

    @Test
    void markAbsentThrowsWhenNotOwner() {
        Appointment appt = ownedFutureAppointment();
        appt.setProfessionalId(999L);
        when(appointmentRepository.findById(9L)).thenReturn(Optional.of(appt));

        assertThrows(ForbiddenException.class, () -> bookingService.markAbsent(9L, PROF));
    }
}
