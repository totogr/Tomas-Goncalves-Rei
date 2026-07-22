package ar.uba.fi.ingsoft1.turnos.appointment;

import ar.uba.fi.ingsoft1.turnos.appointment.dto.BookingDetailResponseDTO;
import ar.uba.fi.ingsoft1.turnos.appointment.dto.BookingRequestDTO;
import ar.uba.fi.ingsoft1.turnos.appointment.dto.ClientBookingResponseDTO;
import ar.uba.fi.ingsoft1.turnos.appointment.dto.ProfessionalBookingResponseDTO;
import ar.uba.fi.ingsoft1.turnos.blockedclient.BlockedClientService;
import ar.uba.fi.ingsoft1.turnos.client.ClientRepository;
import ar.uba.fi.ingsoft1.turnos.config.email.BookingNotificationEmailService;
import ar.uba.fi.ingsoft1.turnos.professional.ProfessionalRepository;
import ar.uba.fi.ingsoft1.turnos.review.ReviewRepository;
import ar.uba.fi.ingsoft1.turnos.schedule.ScheduleBlockRepository;
import ar.uba.fi.ingsoft1.turnos.service.ServiceEntity;
import ar.uba.fi.ingsoft1.turnos.service.ServiceRepository;
import ar.uba.fi.ingsoft1.turnos.common.exception.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingServiceBranchesTest {

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
                appointmentRepository, serviceRepository,
                professionalRepository, clientRepository, reviewRepository,
                scheduleBlockRepository, blockedClientService,
                notificationEmailService, eventPublisher, promotionRepository);

        when(blockedClientService.isBlocked(anyLong(), anyLong())).thenReturn(false);
        when(scheduleBlockRepository.findByProfessionalIdAndBlockDateOrderByStartTimeAsc(any(), any()))
                .thenReturn(List.of());
        when(promotionRepository.findActivePromotionsForDay(any(), any(), any(), any()))
                .thenReturn(List.of());
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    private Appointment buildAppointment(LocalDate date, LocalTime start, LocalTime end,
                                         String status) throws Exception {
        Appointment appt = new Appointment();
        setField(appt, "start", ZonedDateTime.of(date, start, ZONE));
        setField(appt, "end", ZonedDateTime.of(date, end, ZONE));
        setField(appt, "status", status);
        setField(appt, "id", 1L);
        setField(appt, "clientId", 1L);
        setField(appt, "professionalId", 1L);
        setField(appt, "serviceId", 100L);
        return appt;
    }

    private ServiceEntity mockActiveService(int duration) {
        ServiceEntity service = new ServiceEntity();
        service.setDuration(duration);
        service.setActive(true);
        service.setPrice(BigDecimal.valueOf(1000));
        return service;
    }

    // ── createBooking: slot en el pasado ──────────────────────────────────────

    @Test
    void throwsWhenSlotIsInThePast() {
        LocalDate past = LocalDate.now(ZONE).minusDays(1);
        when(serviceRepository.findById(100L)).thenReturn(Optional.of(mockActiveService(60)));

        assertThrows(BadRequestException.class,
                () -> bookingService.createBooking(
                        new BookingRequestDTO(1L, 100L, null, past, LocalTime.of(10, 0)), 1L));
    }

    // ── getClientBookings: rama "past" ────────────────────────────────────────

    @Test
    void getClientBookingsPastIncludesConfirmedPastAppointments() throws Exception {
        LocalDate past = LocalDate.now(ZONE).minusDays(2);
        Appointment appt = buildAppointment(past, LocalTime.of(10, 0), LocalTime.of(11, 0), "CONFIRMED");

        when(appointmentRepository.findByClientId(1L)).thenReturn(List.of(appt));
        when(serviceRepository.findById(100L)).thenReturn(Optional.of(mockActiveService(60)));
        when(professionalRepository.findById(1L)).thenReturn(Optional.empty());

        List<ClientBookingResponseDTO> result = bookingService.getClientBookings(1L, "past");
        assertEquals(1, result.size());
    }

    @Test
    void getClientBookingsPastExcludesCancelledEvenIfPast() throws Exception {
        LocalDate past = LocalDate.now(ZONE).minusDays(2);
        Appointment appt = buildAppointment(past, LocalTime.of(10, 0), LocalTime.of(11, 0), "CANCELLED");

        when(appointmentRepository.findByClientId(1L)).thenReturn(List.of(appt));
        when(serviceRepository.findById(100L)).thenReturn(Optional.of(mockActiveService(60)));
        when(professionalRepository.findById(1L)).thenReturn(Optional.empty());

        List<ClientBookingResponseDTO> result = bookingService.getClientBookings(1L, "past");
        assertTrue(result.isEmpty());
    }

    @Test
    void getClientBookingsUpcomingIncludesPendingFutureAppointments() throws Exception {
        LocalDate future = LocalDate.now(ZONE).plusDays(3);
        Appointment appt = buildAppointment(future, LocalTime.of(10, 0), LocalTime.of(11, 0), "PENDING");

        when(appointmentRepository.findByClientId(1L)).thenReturn(List.of(appt));
        when(serviceRepository.findById(100L)).thenReturn(Optional.of(mockActiveService(60)));
        when(professionalRepository.findById(1L)).thenReturn(Optional.empty());

        List<ClientBookingResponseDTO> result = bookingService.getClientBookings(1L, "upcoming");
        assertEquals(1, result.size());
    }

    // ── getProfessionalBookings: ramas de filtro ──────────────────────────────

    @Test
    void getProfessionalBookingsPastIncludesConfirmedPastAppointments() throws Exception {
        LocalDate past = LocalDate.now(ZONE).minusDays(2);
        Appointment appt = buildAppointment(past, LocalTime.of(10, 0), LocalTime.of(11, 0), "CONFIRMED");

        when(appointmentRepository.findByProfessionalId(1L)).thenReturn(List.of(appt));
        when(serviceRepository.findById(100L)).thenReturn(Optional.of(mockActiveService(60)));
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        List<ProfessionalBookingResponseDTO> result = bookingService.getProfessionalBookings(1L, "past");
        assertEquals(1, result.size());
    }

    @Test
    void getProfessionalBookingsPastExcludesCancelled() throws Exception {
        LocalDate past = LocalDate.now(ZONE).minusDays(2);
        Appointment appt = buildAppointment(past, LocalTime.of(10, 0), LocalTime.of(11, 0), "CANCELLED");

        when(appointmentRepository.findByProfessionalId(1L)).thenReturn(List.of(appt));
        when(serviceRepository.findById(100L)).thenReturn(Optional.of(mockActiveService(60)));
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        List<ProfessionalBookingResponseDTO> result = bookingService.getProfessionalBookings(1L, "past");
        assertTrue(result.isEmpty());
    }

    @Test
    void getProfessionalBookingsUpcomingExcludesPastConfirmed() throws Exception {
        LocalDate past = LocalDate.now(ZONE).minusDays(2);
        Appointment appt = buildAppointment(past, LocalTime.of(10, 0), LocalTime.of(11, 0), "CONFIRMED");

        when(appointmentRepository.findByProfessionalId(1L)).thenReturn(List.of(appt));
        when(serviceRepository.findById(100L)).thenReturn(Optional.of(mockActiveService(60)));
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        List<ProfessionalBookingResponseDTO> result = bookingService.getProfessionalBookings(1L, "upcoming");
        assertTrue(result.isEmpty());
    }

    // ── getBookingDetail: cancelledBy ─────────────────────────────────────────

    @Test
    void getBookingDetailCancelledByIsNullWhenConfirmed() throws Exception {
        LocalDate future = LocalDate.now(ZONE).plusDays(1);
        Appointment appt = buildAppointment(future, LocalTime.of(10, 0), LocalTime.of(11, 0), "CONFIRMED");

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appt));
        when(serviceRepository.findById(100L)).thenReturn(Optional.of(mockActiveService(60)));
        when(professionalRepository.findById(1L)).thenReturn(Optional.empty());
        when(reviewRepository.findByAppointmentId(1L)).thenReturn(Optional.empty());

        BookingDetailResponseDTO result = bookingService.getBookingDetail(1L, 1L);
        assertNull(result.cancelledBy());
    }
}