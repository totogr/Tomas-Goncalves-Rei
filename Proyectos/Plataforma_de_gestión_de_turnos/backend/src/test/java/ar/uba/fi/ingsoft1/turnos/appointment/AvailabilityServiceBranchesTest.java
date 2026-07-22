package ar.uba.fi.ingsoft1.turnos.appointment;

import ar.uba.fi.ingsoft1.turnos.appointment.dto.AvailabilityResponseDTO;
import ar.uba.fi.ingsoft1.turnos.professional.ProfessionalRepository;
import ar.uba.fi.ingsoft1.turnos.schedule.Schedule;
import ar.uba.fi.ingsoft1.turnos.schedule.ScheduleBlockRepository;
import ar.uba.fi.ingsoft1.turnos.schedule.ScheduleRepository;
import ar.uba.fi.ingsoft1.turnos.service.ServiceEntity;
import ar.uba.fi.ingsoft1.turnos.service.ServiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AvailabilityServiceBranchesTest {

    private static final ZoneId ZONE = ZoneId.of("America/Argentina/Buenos_Aires");

    private AvailabilityService availabilityService;
    private ScheduleRepository scheduleRepository;
        private ScheduleBlockRepository scheduleBlockRepository;
    private AppointmentRepository appointmentRepository;
    private ServiceRepository serviceRepository;
    private ProfessionalRepository professionalRepository;
    private WaitListPromotionRepository promotionRepository;

    @BeforeEach
    void setUp() {
        scheduleRepository = mock(ScheduleRepository.class);
        scheduleBlockRepository = mock(ScheduleBlockRepository.class);
        appointmentRepository = mock(AppointmentRepository.class);
        serviceRepository = mock(ServiceRepository.class);
        professionalRepository = mock(ProfessionalRepository.class);
        promotionRepository = mock(WaitListPromotionRepository.class);

        availabilityService = new AvailabilityService(
                scheduleRepository, scheduleBlockRepository, appointmentRepository, serviceRepository,
                professionalRepository, promotionRepository);

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

    // Usa new Schedule() en lugar de mock() para evitar UnfinishedStubbing
    // cuando se pasa dentro de thenReturn(List.of(...))
    private Schedule buildSchedule(LocalTime start, LocalTime end) {
        Schedule s = new Schedule();
        s.setStart(start);
        s.setEnd(end);
        return s;
    }

    private ServiceEntity buildService(int duration) {
        ServiceEntity svc = new ServiceEntity();
        svc.setDuration(duration);
        svc.setActive(true);
        svc.setMaxCapacity(1);
        return svc;
    }

    // ── Slots en el pasado (fecha de hoy) ─────────────────────────────────────

    @Test
    void slotsBeforeCurrentTimeMarkedAsPastWhenDateIsToday() {
        // Rango 00:00-01:00 de HOY — siempre en el pasado
        LocalDate today = LocalDate.now(ZONE);
        Long profId = 1L;
        Long serviceId = 100L;

        Schedule schedule = buildSchedule(LocalTime.of(0, 0), LocalTime.of(1, 0));

        when(scheduleRepository.findByProfessionalIdAndDayWeek(eq(profId), any()))
                .thenReturn(List.of(schedule));
        when(serviceRepository.findById(serviceId))
                .thenReturn(Optional.of(buildService(30)));
        when(appointmentRepository.findActiveAppointments(eq(profId), any(), any()))
                .thenReturn(List.of());

        AvailabilityResponseDTO response = availabilityService.getAvailability(profId, today, serviceId, null);

        assertFalse(response.slots().isEmpty());
        response.slots()
                .forEach(slot -> assertFalse(slot.available(), "Slot " + slot.time() + " debería estar en el pasado"));
    }

    // ── Múltiples rangos horarios en el mismo día ─────────────────────────────

    @Test
    void multipleScheduleRangesOnSameDayGenerateSlotsForEach() {
        LocalDate testDate = LocalDate.now(ZONE).plusDays(7);
        Long profId = 1L;
        Long serviceId = 100L;

        Schedule morning = buildSchedule(LocalTime.of(9, 0), LocalTime.of(10, 0));
        Schedule afternoon = buildSchedule(LocalTime.of(14, 0), LocalTime.of(15, 0));

        when(scheduleRepository.findByProfessionalIdAndDayWeek(eq(profId), any()))
                .thenReturn(List.of(morning, afternoon));
        when(serviceRepository.findById(serviceId))
                .thenReturn(Optional.of(buildService(30)));
        when(appointmentRepository.findActiveAppointments(eq(profId), any(), any()))
                .thenReturn(List.of());

        AvailabilityResponseDTO response = availabilityService.getAvailability(profId, testDate, serviceId, null);

        // 2 slots mañana (09:00, 09:30) + 2 slots tarde (14:00, 14:30) = 4
        assertEquals(4, response.slots().size());
        assertTrue(response.slots().stream().allMatch(s -> s.available()));
    }

    // ── Servicio no encontrado ────────────────────────────────────────────────

    @Test
    void throwsWhenServiceNotFound() {
        LocalDate testDate = LocalDate.now(ZONE).plusDays(7);
        Long profId = 1L;
        Long serviceId = 999L;

        Schedule schedule = buildSchedule(LocalTime.of(9, 0), LocalTime.of(10, 0));

        when(scheduleRepository.findByProfessionalIdAndDayWeek(eq(profId), any()))
                .thenReturn(List.of(schedule));
        when(serviceRepository.findById(serviceId)).thenReturn(Optional.empty());

        assertThrows(Exception.class,
                () -> availabilityService.getAvailability(profId, testDate, serviceId, null));
    }

    // ── Capacidad máxima > 1 (turnos grupales) ────────────────────────────────

    @Test
    void slotAvailableWhenBelowMaxCapacity() throws Exception {
        LocalDate testDate = LocalDate.now(ZONE).plusDays(7);
        Long profId = 1L;
        Long serviceId = 100L;

        ServiceEntity groupService = new ServiceEntity();
        groupService.setDuration(30);
        groupService.setActive(true);
        groupService.setMaxCapacity(2);

        // 1 booking existente en 09:00-09:30
        Appointment existing = new Appointment();
        setField(existing, "serviceId", serviceId);
        setField(existing, "start", ZonedDateTime.of(testDate, LocalTime.of(9, 0), ZONE));
        setField(existing, "end", ZonedDateTime.of(testDate, LocalTime.of(9, 30), ZONE));

        Schedule schedule = buildSchedule(LocalTime.of(9, 0), LocalTime.of(10, 0));

        when(scheduleRepository.findByProfessionalIdAndDayWeek(eq(profId), any()))
                .thenReturn(List.of(schedule));
        when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(groupService));
        when(appointmentRepository.findActiveAppointments(eq(profId), any(), any()))
                .thenReturn(List.of(existing));

        AvailabilityResponseDTO response = availabilityService.getAvailability(profId, testDate, serviceId, null);

        assertEquals(2, response.slots().size());
        assertTrue(response.slots().get(0).available(),
                "Slot 09:00 debería estar disponible (1 booking, capacidad 2)");
    }

    @Test
    void slotUnavailableWhenAtMaxCapacity() throws Exception {
        LocalDate testDate = LocalDate.now(ZONE).plusDays(7);
        Long profId = 1L;
        Long serviceId = 100L;

        ServiceEntity groupService = new ServiceEntity();
        groupService.setDuration(30);
        groupService.setActive(true);
        groupService.setMaxCapacity(2);

        // 2 bookings en 09:00-09:30 — capacidad llena
        Appointment b1 = new Appointment();
        setField(b1, "serviceId", serviceId);
        setField(b1, "start", ZonedDateTime.of(testDate, LocalTime.of(9, 0), ZONE));
        setField(b1, "end", ZonedDateTime.of(testDate, LocalTime.of(9, 30), ZONE));

        Appointment b2 = new Appointment();
        setField(b2, "serviceId", serviceId);
        setField(b2, "start", ZonedDateTime.of(testDate, LocalTime.of(9, 0), ZONE));
        setField(b2, "end", ZonedDateTime.of(testDate, LocalTime.of(9, 30), ZONE));

        Schedule schedule = buildSchedule(LocalTime.of(9, 0), LocalTime.of(10, 0));

        when(scheduleRepository.findByProfessionalIdAndDayWeek(eq(profId), any()))
                .thenReturn(List.of(schedule));
        when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(groupService));
        when(appointmentRepository.findActiveAppointments(eq(profId), any(), any()))
                .thenReturn(List.of(b1, b2));

        AvailabilityResponseDTO response = availabilityService.getAvailability(profId, testDate, serviceId, null);

        assertFalse(response.slots().get(0).available(),
                "Slot 09:00 debería estar lleno (2 bookings, capacidad 2)");
    }

    // ── employeeId se propaga en slots disponibles ────────────────────────────

    @Test
    void employeeIdIsIncludedInAvailableSlots() {
        LocalDate testDate = LocalDate.now(ZONE).plusDays(7);
        Long profId = 1L;
        Long serviceId = 100L;
        Long employeeId = 5L;

        Schedule schedule = buildSchedule(LocalTime.of(9, 0), LocalTime.of(10, 0));

        when(scheduleRepository.findByProfessionalIdAndDayWeek(eq(profId), any()))
                .thenReturn(List.of(schedule));
        when(serviceRepository.findById(serviceId))
                .thenReturn(Optional.of(buildService(30)));
        when(appointmentRepository.findActiveAppointments(eq(profId), any(), any()))
                .thenReturn(List.of());

        AvailabilityResponseDTO response = availabilityService.getAvailability(
                profId, testDate, serviceId, employeeId);

        assertTrue(response.slots().get(0).available());
        assertEquals(employeeId, response.slots().get(0).employeeId());
    }
}