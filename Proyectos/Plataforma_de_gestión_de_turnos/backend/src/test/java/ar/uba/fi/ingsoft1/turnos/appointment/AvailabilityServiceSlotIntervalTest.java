package ar.uba.fi.ingsoft1.turnos.appointment;

import ar.uba.fi.ingsoft1.turnos.appointment.dto.AvailabilityResponseDTO;
import ar.uba.fi.ingsoft1.turnos.professional.Professional;
import ar.uba.fi.ingsoft1.turnos.professional.ProfessionalRepository;
import ar.uba.fi.ingsoft1.turnos.schedule.Schedule;
import ar.uba.fi.ingsoft1.turnos.schedule.ScheduleBlockRepository;
import ar.uba.fi.ingsoft1.turnos.schedule.ScheduleRepository;
import ar.uba.fi.ingsoft1.turnos.service.ServiceEntity;
import ar.uba.fi.ingsoft1.turnos.service.ServiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AvailabilityServiceSlotIntervalTest {

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
                scheduleRepository, scheduleBlockRepository, appointmentRepository,
                serviceRepository, professionalRepository, promotionRepository);

        when(scheduleBlockRepository.findByProfessionalIdAndBlockDateOrderByStartTimeAsc(any(), any()))
                .thenReturn(List.of());
        when(promotionRepository.findActivePromotionsForDay(any(), any(), any(), any()))
                .thenReturn(List.of());
        when(appointmentRepository.findActiveAppointments(any(), any(), any()))
                .thenReturn(List.of());
    }

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

    @Test
    void customSlotIntervalOf60MinutesGeneratesFewerSlots() {
        LocalDate date = LocalDate.now(ZONE).plusDays(7);
        Long profId = 1L;
        Long serviceId = 100L;

        Professional prof = new Professional("p@mail.com", "x", "Juan", "Perez");
        prof.setSlotIntervalMinutes(60);
        when(professionalRepository.findById(profId)).thenReturn(Optional.of(prof));

        Schedule schedule = buildSchedule(LocalTime.of(9, 0), LocalTime.of(12, 0));
        when(scheduleRepository.findByProfessionalIdAndDayWeek(eq(profId), anyInt()))
                .thenReturn(List.of(schedule));
        when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(buildService(30)));

        AvailabilityResponseDTO response = availabilityService.getAvailability(profId, date, serviceId, null);

        assertEquals(3, response.slots().size());
        assertTrue(response.slots().stream().allMatch(s -> s.available()));
    }

    @Test
    void defaultSlotIntervalOf30MinutesIsUsedWhenProfessionalNotFound() {
        LocalDate date = LocalDate.now(ZONE).plusDays(7);
        Long profId = 1L;
        Long serviceId = 100L;

        when(professionalRepository.findById(profId)).thenReturn(Optional.empty());

        Schedule schedule = buildSchedule(LocalTime.of(9, 0), LocalTime.of(10, 0));
        when(scheduleRepository.findByProfessionalIdAndDayWeek(eq(profId), anyInt()))
                .thenReturn(List.of(schedule));
        when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(buildService(30)));

        AvailabilityResponseDTO response = availabilityService.getAvailability(profId, date, serviceId, null);

        assertEquals(2, response.slots().size());
    }

    @Test
    void slotIntervalNullFallsBackTo30Minutes() {
        LocalDate date = LocalDate.now(ZONE).plusDays(7);
        Long profId = 1L;
        Long serviceId = 100L;

        Professional prof = new Professional("p@mail.com", "x", "Juan", "Perez");
        prof.setSlotIntervalMinutes(null);
        when(professionalRepository.findById(profId)).thenReturn(Optional.of(prof));

        Schedule schedule = buildSchedule(LocalTime.of(9, 0), LocalTime.of(10, 0));
        when(scheduleRepository.findByProfessionalIdAndDayWeek(eq(profId), anyInt()))
                .thenReturn(List.of(schedule));
        when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(buildService(30)));

        AvailabilityResponseDTO response = availabilityService.getAvailability(profId, date, serviceId, null);

        assertEquals(2, response.slots().size());
    }

    @Test
    void inactiveServiceThrowsException() {
        LocalDate date = LocalDate.now(ZONE).plusDays(7);
        Long profId = 1L;
        Long serviceId = 100L;

        Schedule schedule = buildSchedule(LocalTime.of(9, 0), LocalTime.of(10, 0));
        when(scheduleRepository.findByProfessionalIdAndDayWeek(eq(profId), anyInt()))
                .thenReturn(List.of(schedule));

        ServiceEntity inactive = buildService(30);
        inactive.setActive(false);
        when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(inactive));

        assertThrows(IllegalArgumentException.class,
                () -> availabilityService.getAvailability(profId, date, serviceId, null));
    }

    @Test
    void slotIsMarkedFullWhenOverlapsOtherServiceAppointment() throws Exception {
        LocalDate date = LocalDate.now(ZONE).plusDays(7);
        Long profId = 1L;
        Long serviceId = 100L;
        Long otherServiceId = 200L;

        Schedule schedule = buildSchedule(LocalTime.of(10, 0), LocalTime.of(11, 0));
        when(scheduleRepository.findByProfessionalIdAndDayWeek(eq(profId), anyInt()))
                .thenReturn(List.of(schedule));
        when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(buildService(30)));

        Appointment otherAppt = new Appointment();
        java.lang.reflect.Field svcField = Appointment.class.getDeclaredField("serviceId");
        svcField.setAccessible(true);
        svcField.set(otherAppt, otherServiceId);
        java.lang.reflect.Field startField = Appointment.class.getDeclaredField("start");
        startField.setAccessible(true);
        startField.set(otherAppt, java.time.ZonedDateTime.of(date, LocalTime.of(10, 0), ZONE));
        java.lang.reflect.Field endField = Appointment.class.getDeclaredField("end");
        endField.setAccessible(true);
        endField.set(otherAppt, java.time.ZonedDateTime.of(date, LocalTime.of(10, 30), ZONE));

        when(appointmentRepository.findActiveAppointments(eq(profId), any(), any()))
                .thenReturn(List.of(otherAppt));

        AvailabilityResponseDTO response = availabilityService.getAvailability(profId, date, serviceId, null);

        assertFalse(response.slots().isEmpty());
        assertFalse(response.slots().get(0).available());
        assertEquals("full", response.slots().get(0).reason());
    }
}