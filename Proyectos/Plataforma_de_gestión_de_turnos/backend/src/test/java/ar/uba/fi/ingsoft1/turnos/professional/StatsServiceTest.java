package ar.uba.fi.ingsoft1.turnos.professional;

import ar.uba.fi.ingsoft1.turnos.appointment.Appointment;
import ar.uba.fi.ingsoft1.turnos.appointment.AppointmentRepository;
import ar.uba.fi.ingsoft1.turnos.client.Client;
import ar.uba.fi.ingsoft1.turnos.client.ClientRepository;
import ar.uba.fi.ingsoft1.turnos.review.ReviewRepository;
import ar.uba.fi.ingsoft1.turnos.service.ServiceEntity;
import ar.uba.fi.ingsoft1.turnos.service.ServiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class StatsServiceTest {

    private StatsService statsService;
    private AppointmentRepository appointmentRepository;
    private ServiceRepository serviceRepository;
    private ClientRepository clientRepository;
    private ReviewRepository reviewRepository;

    private Client buildClient(String firstName, String lastName, String email) throws Exception {
        Client c = new Client();
        setField(c, "firstName", firstName);
        setField(c, "lastName", lastName);
        setField(c, "email", email);
        return c;
    }

    @BeforeEach
    void setUp() {
        appointmentRepository = mock(AppointmentRepository.class);
        serviceRepository = mock(ServiceRepository.class);
        clientRepository = mock(ClientRepository.class);
        reviewRepository = mock(ReviewRepository.class);

        statsService = new StatsService(
                appointmentRepository, serviceRepository, clientRepository, reviewRepository);

        when(reviewRepository.findAverageScoreByProfessionalId(any())).thenReturn(Optional.empty());
    }

    private Appointment buildAppointment(Long id, Long clientId, Long serviceId,
            LocalDate date, LocalTime start, String status) throws Exception {
        Appointment appt = new Appointment();
        setField(appt, "id", id);
        setField(appt, "clientId", clientId);
        setField(appt, "serviceId", serviceId);
        setField(appt, "professionalId", 1L);
        setField(appt, "status", status);
        setField(appt, "start", ZonedDateTime.of(date, start, ZoneId.systemDefault()));
        setField(appt, "end", ZonedDateTime.of(date, start.plusHours(1), ZoneId.systemDefault()));
        if ("CANCELLED".equalsIgnoreCase(status)) {
            appt.setCancelledBy("client");
        }
        return appt;
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    private ServiceEntity buildService(String name, BigDecimal price) {
        ServiceEntity svc = new ServiceEntity();
        svc.setName(name);
        svc.setPrice(price);
        svc.setDuration(60);
        svc.setActive(true);
        return svc;
    }

    @Test
    void returnsZeroMetricsWhenNoAppointments() {
        when(appointmentRepository.findActiveByProfessionalIdAndRange(eq(1L), any(), any()))
                .thenReturn(List.of());
        when(appointmentRepository.findAllByProfessionalIdAndRange(eq(1L), any(), any()))
                .thenReturn(List.of());

        StatsResponseDTO result = statsService.getStats(1L, "30d");

        assertEquals(0, result.totalAppointments());
        assertEquals(0.0, result.cancellationRate());
        assertNull(result.averageRating());
        assertEquals(0.0, result.estimatedRevenue());
        assertEquals(7, result.appointmentsByDay().size());
        assertTrue(result.topServices().isEmpty());
        assertTrue(result.frequentClients().isEmpty());
    }

    @Test
    void calculatesCancellationRateCorrectly() throws Exception {
        LocalDate date = LocalDate.now(ZoneId.systemDefault()).minusDays(3);
        Appointment confirmed = buildAppointment(1L, 1L, 100L, date, LocalTime.of(9, 0), "CONFIRMED");
        Appointment cancelled = buildAppointment(2L, 2L, 100L, date, LocalTime.of(10, 0), "CANCELLED");

        when(appointmentRepository.findActiveByProfessionalIdAndRange(eq(1L), any(), any()))
                .thenReturn(List.of(confirmed));
        when(appointmentRepository.findAllByProfessionalIdAndRange(eq(1L), any(), any()))
                .thenReturn(List.of(confirmed, cancelled));
        when(serviceRepository.findById(100L)).thenReturn(Optional.of(buildService("Corte", BigDecimal.valueOf(1000))));
        when(clientRepository.findById(any())).thenReturn(Optional.empty());

        StatsResponseDTO result = statsService.getStats(1L, "30d");

        assertEquals(2, result.totalAppointments());
        assertEquals(50.0, result.cancellationRate());
    }

    @Test
    void calculatesRevenueCorrectly() throws Exception {
        LocalDate date = LocalDate.now(ZoneId.systemDefault()).minusDays(3);
        Appointment a1 = buildAppointment(1L, 1L, 100L, date, LocalTime.of(9, 0), "COMPLETED");
        Appointment a2 = buildAppointment(2L, 2L, 100L, date, LocalTime.of(10, 0), "COMPLETED");

        when(appointmentRepository.findActiveByProfessionalIdAndRange(eq(1L), any(), any()))
                .thenReturn(List.of(a1, a2));
        when(appointmentRepository.findAllByProfessionalIdAndRange(eq(1L), any(), any()))
                .thenReturn(List.of(a1, a2));
        when(serviceRepository.findById(100L)).thenReturn(Optional.of(buildService("Corte", BigDecimal.valueOf(1500))));
        when(clientRepository.findById(any())).thenReturn(Optional.empty());

        StatsResponseDTO result = statsService.getStats(1L, "30d");
        assertEquals(3000.0, result.estimatedRevenue());
    }

    @Test
    void classifiesFrequentAndOccasionalClients() throws Exception {
        LocalDate date = LocalDate.now(ZoneId.systemDefault()).minusDays(3);

        // Cliente 1: 2 visitas → frecuente
        Appointment a1 = buildAppointment(1L, 1L, 100L, date, LocalTime.of(9, 0), "COMPLETED");
        Appointment a2 = buildAppointment(2L, 1L, 100L, date, LocalTime.of(10, 0), "COMPLETED");
        // Cliente 2: 0 visitas, 1 cancelación → ocasional
        Appointment a3 = buildAppointment(3L, 2L, 100L, date, LocalTime.of(11, 0), "CANCELLED");

        when(appointmentRepository.findActiveByProfessionalIdAndRange(eq(1L), any(), any()))
                .thenReturn(List.of(a1, a2));
        when(appointmentRepository.findAllByProfessionalIdAndRange(eq(1L), any(), any()))
                .thenReturn(List.of(a1, a2, a3));
        when(serviceRepository.findById(100L)).thenReturn(Optional.of(buildService("Corte", BigDecimal.valueOf(1000))));

        Client c1 = buildClient("Juan", "Perez", "juan@mail.com");
        Client c2 = buildClient("Ana", "García", "ana@mail.com");

        when(clientRepository.findById(1L)).thenReturn(Optional.of(c1));
        when(clientRepository.findById(2L)).thenReturn(Optional.of(c2));

        StatsResponseDTO result = statsService.getStats(1L, "30d");

        assertEquals(2, result.frequentClients().size());
        StatsResponseDTO.ClientStatDTO frequent = result.frequentClients().get(0);
        assertEquals("frequent", frequent.status());
        assertEquals(2, frequent.visits());

        StatsResponseDTO.ClientStatDTO occasional = result.frequentClients().get(1);
        assertEquals("occasional", occasional.status());
        assertEquals(1, occasional.cancellations());
    }

    @Test
    void averageRatingIsIncludedWhenExists() throws Exception {
        LocalDate date = LocalDate.now(ZoneId.systemDefault()).minusDays(1);
        Appointment appt = buildAppointment(1L, 1L, 100L, date, LocalTime.of(9, 0), "COMPLETED");

        when(appointmentRepository.findActiveByProfessionalIdAndRange(eq(1L), any(), any()))
                .thenReturn(List.of(appt));
        when(appointmentRepository.findAllByProfessionalIdAndRange(eq(1L), any(), any()))
                .thenReturn(List.of(appt));
        when(serviceRepository.findById(100L)).thenReturn(Optional.of(buildService("Corte", BigDecimal.valueOf(1000))));
        when(clientRepository.findById(any())).thenReturn(Optional.empty());
        when(reviewRepository.findAverageScoreByProfessionalId(1L)).thenReturn(Optional.of(4.3));

        StatsResponseDTO result = statsService.getStats(1L, "30d");
        assertEquals(4.3, result.averageRating());
    }

    @Test
    void periodSevenDaysUsesCorrectRange() {
        when(appointmentRepository.findActiveByProfessionalIdAndRange(eq(1L), any(), any()))
                .thenReturn(List.of());
        when(appointmentRepository.findAllByProfessionalIdAndRange(eq(1L), any(), any()))
                .thenReturn(List.of());

        StatsResponseDTO result = statsService.getStats(1L, "7d");
        assertNotNull(result);
        assertEquals(0, result.totalAppointments());
    }

    @Test
    void periodThreeMonthsUsesCorrectRange() {
        when(appointmentRepository.findActiveByProfessionalIdAndRange(eq(1L), any(), any()))
                .thenReturn(List.of());
        when(appointmentRepository.findAllByProfessionalIdAndRange(eq(1L), any(), any()))
                .thenReturn(List.of());

        StatsResponseDTO result = statsService.getStats(1L, "3m");
        assertNotNull(result);
    }

    @Test
    void topServicesCalculatesPercentagesCorrectly() throws Exception {
        LocalDate date = LocalDate.now(ZoneId.systemDefault()).minusDays(3);

        Appointment a1 = buildAppointment(1L, 1L, 100L, date, LocalTime.of(9, 0), "COMPLETED");
        Appointment a2 = buildAppointment(2L, 1L, 100L, date, LocalTime.of(10, 0), "COMPLETED");
        Appointment a3 = buildAppointment(3L, 1L, 200L, date, LocalTime.of(11, 0), "COMPLETED");

        when(appointmentRepository.findActiveByProfessionalIdAndRange(eq(1L), any(), any()))
                .thenReturn(List.of(a1, a2, a3));
        when(appointmentRepository.findAllByProfessionalIdAndRange(eq(1L), any(), any()))
                .thenReturn(List.of(a1, a2, a3));
        when(serviceRepository.findById(100L)).thenReturn(Optional.of(buildService("Corte", BigDecimal.valueOf(1000))));
        when(serviceRepository.findById(200L))
                .thenReturn(Optional.of(buildService("Teñido", BigDecimal.valueOf(2000))));
        when(clientRepository.findById(any())).thenReturn(Optional.empty());

        StatsResponseDTO result = statsService.getStats(1L, "30d");

        assertEquals(2, result.topServices().size());
        assertEquals("Corte", result.topServices().get(0).name());
        assertEquals(67, result.topServices().get(0).percentage());
        assertEquals("Teñido", result.topServices().get(1).name());
        assertEquals(33, result.topServices().get(1).percentage());
    }

    @Test
    void appointmentsByDayCountsCorrectly() throws Exception {
        // Buscamos un lunes futuro para tener fecha conocida
        LocalDate monday = LocalDate.now(ZoneId.systemDefault())
                .minusDays(7)
                .with(java.time.DayOfWeek.MONDAY);

        Appointment a1 = buildAppointment(1L, 1L, 100L, monday, LocalTime.of(9, 0), "COMPLETED");
        Appointment a2 = buildAppointment(2L, 2L, 100L, monday, LocalTime.of(10, 0), "COMPLETED");

        when(appointmentRepository.findActiveByProfessionalIdAndRange(eq(1L), any(), any()))
                .thenReturn(List.of(a1, a2));
        when(appointmentRepository.findAllByProfessionalIdAndRange(eq(1L), any(), any()))
                .thenReturn(List.of(a1, a2));
        when(serviceRepository.findById(100L)).thenReturn(Optional.of(buildService("Corte", BigDecimal.valueOf(1000))));
        when(clientRepository.findById(any())).thenReturn(Optional.empty());

        StatsResponseDTO result = statsService.getStats(1L, "30d");

        assertEquals(7, result.appointmentsByDay().size());
        // Lunes es índice 0
        assertEquals("Lun", result.appointmentsByDay().get(0).day());
        assertEquals(2, result.appointmentsByDay().get(0).count());
        // El resto deben ser 0
        assertEquals(0, result.appointmentsByDay().get(1).count());
    }

    @Test
    void frequentClientsLimitedToFive() throws Exception {
        LocalDate date = LocalDate.now(ZoneId.systemDefault()).minusDays(3);
        List<Appointment> appts = new java.util.ArrayList<>();
        for (long i = 1; i <= 7; i++) {
            appts.add(buildAppointment(i, i, 100L, date, LocalTime.of((int) (8 + i), 0), "COMPLETED"));
            when(clientRepository.findById(i)).thenReturn(Optional.empty());
        }

        when(appointmentRepository.findActiveByProfessionalIdAndRange(eq(1L), any(), any()))
                .thenReturn(appts);
        when(appointmentRepository.findAllByProfessionalIdAndRange(eq(1L), any(), any()))
                .thenReturn(appts);
        when(serviceRepository.findById(100L)).thenReturn(Optional.of(buildService("Corte", BigDecimal.valueOf(1000))));

        StatsResponseDTO result = statsService.getStats(1L, "30d");
        assertTrue(result.frequentClients().size() <= 5);
    }

    @Test
    void clientWithCancellationsShowsCancellationCount() throws Exception {
        LocalDate date = LocalDate.now(ZoneId.systemDefault()).minusDays(3);
        Appointment visit = buildAppointment(1L, 1L, 100L, date, LocalTime.of(9, 0), "COMPLETED");
        Appointment c1 = buildAppointment(2L, 1L, 100L, date, LocalTime.of(10, 0), "CANCELLED");
        Appointment c2 = buildAppointment(3L, 1L, 100L, date, LocalTime.of(11, 0), "CANCELLED");

        when(appointmentRepository.findActiveByProfessionalIdAndRange(eq(1L), any(), any()))
                .thenReturn(List.of(visit));
        when(appointmentRepository.findAllByProfessionalIdAndRange(eq(1L), any(), any()))
                .thenReturn(List.of(visit, c1, c2));
        when(serviceRepository.findById(100L)).thenReturn(Optional.of(buildService("Corte", BigDecimal.valueOf(1000))));
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        StatsResponseDTO result = statsService.getStats(1L, "30d");
        assertEquals(1, result.frequentClients().size());
        assertEquals(2, result.frequentClients().get(0).cancellations());
    }

}