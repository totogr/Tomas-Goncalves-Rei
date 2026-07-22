package ar.uba.fi.ingsoft1.turnos.appointment;

import ar.uba.fi.ingsoft1.turnos.service.ServiceEntity;
import ar.uba.fi.ingsoft1.turnos.service.ServiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class SlotAvailabilityServiceTest {

    private static final ZoneId ZONE = ZoneId.of("America/Argentina/Buenos_Aires");

    private AppointmentRepository appointmentRepository;
    private ServiceRepository serviceRepository;
    private WaitListPromotionRepository promotionRepository;
    private SlotAvailabilityService service;

    private static final Long PROF = 1L;
    private static final Long SERVICE = 100L;
    private static final Long CLIENT = 7L;

    @BeforeEach
    void setUp() {
        appointmentRepository = mock(AppointmentRepository.class);
        serviceRepository = mock(ServiceRepository.class);
        promotionRepository = mock(WaitListPromotionRepository.class);
        service = new SlotAvailabilityService(appointmentRepository, serviceRepository, promotionRepository);

        when(promotionRepository.findActivePromotionsForDay(any(), any(), any(), any()))
                .thenReturn(List.of());
    }

    private ServiceEntity serviceEntity(int duration, Integer maxCapacity) {
        ServiceEntity s = new ServiceEntity();
        s.setDuration(duration);
        s.setActive(true);
        s.setMaxCapacity(maxCapacity);
        return s;
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

    // ── isSlotFullyBooked ─────────────────────────────────────────────────────

    @Test
    void isSlotFullyBookedTrueWhenCapacityReached() {
        LocalDate date = LocalDate.now(ZONE).plusDays(2);
        ZonedDateTime slot = ZonedDateTime.of(date, LocalTime.of(10, 0), ZONE);
        when(serviceRepository.findById(SERVICE)).thenReturn(Optional.of(serviceEntity(30, 1)));
        when(appointmentRepository.findActiveAppointmentsForUpdate(eq(PROF), eq(SERVICE), any(), any()))
                .thenReturn(List.of(occupied(date, LocalTime.of(10, 0), LocalTime.of(10, 30), 1L)));

        assertTrue(service.isSlotFullyBooked(PROF, SERVICE, slot));
    }

    @Test
    void isSlotFullyBookedFalseWhenRoomLeft() {
        LocalDate date = LocalDate.now(ZONE).plusDays(2);
        ZonedDateTime slot = ZonedDateTime.of(date, LocalTime.of(10, 0), ZONE);
        when(serviceRepository.findById(SERVICE)).thenReturn(Optional.of(serviceEntity(30, 2)));
        when(appointmentRepository.findActiveAppointmentsForUpdate(eq(PROF), eq(SERVICE), any(), any()))
                .thenReturn(List.of(occupied(date, LocalTime.of(10, 0), LocalTime.of(10, 30), 1L)));

        assertFalse(service.isSlotFullyBooked(PROF, SERVICE, slot));
    }

    // ── isWindowAvailable ─────────────────────────────────────────────────────

    @Test
    void isWindowAvailableTrueWhenNoOccupancy() {
        ZonedDateTime windowStart = ZonedDateTime.now(ZONE).plusDays(2).withHour(10).withMinute(0);
        when(serviceRepository.findById(SERVICE)).thenReturn(Optional.of(serviceEntity(60, 1)));
        when(appointmentRepository.findActiveAppointmentsForUpdate(eq(PROF), eq(SERVICE), any(), any()))
                .thenReturn(List.of());

        assertTrue(service.isWindowAvailable(PROF, SERVICE, windowStart, 60));
    }

    @Test
    void isWindowAvailableFalseWhenBlockFull() {
        LocalDate date = LocalDate.now(ZONE).plusDays(2);
        ZonedDateTime windowStart = ZonedDateTime.of(date, LocalTime.of(10, 0), ZONE);
        when(serviceRepository.findById(SERVICE)).thenReturn(Optional.of(serviceEntity(60, 1)));
        when(appointmentRepository.findActiveAppointmentsForUpdate(eq(PROF), eq(SERVICE), any(), any()))
                .thenReturn(List.of(occupied(date, LocalTime.of(10, 0), LocalTime.of(11, 0), 1L)));

        assertFalse(service.isWindowAvailable(PROF, SERVICE, windowStart, 60));
    }

    // ── isSlotCompletelyEmpty ─────────────────────────────────────────────────

    @Test
    void isSlotCompletelyEmptyReflectsOccupancy() {
        LocalDate date = LocalDate.now(ZONE).plusDays(2);
        ZonedDateTime windowStart = ZonedDateTime.of(date, LocalTime.of(10, 0), ZONE);
        when(serviceRepository.findById(SERVICE)).thenReturn(Optional.of(serviceEntity(60, 1)));
        when(appointmentRepository.findActiveAppointmentsForUpdate(eq(PROF), eq(SERVICE), any(), any()))
                .thenReturn(List.of());
        assertTrue(service.isSlotCompletelyEmpty(PROF, SERVICE, windowStart, 60));

        when(appointmentRepository.findActiveAppointmentsForUpdate(eq(PROF), eq(SERVICE), any(), any()))
                .thenReturn(List.of(occupied(date, LocalTime.of(10, 0), LocalTime.of(10, 30), 1L)));
        assertFalse(service.isSlotCompletelyEmpty(PROF, SERVICE, windowStart, 60));
    }

    // ── hasClientOverlappingBooking ───────────────────────────────────────────

    @Test
    void hasClientOverlappingBookingDetectsOverlap() {
        LocalDate date = LocalDate.now(ZONE).plusDays(2);
        ZonedDateTime newStart = ZonedDateTime.of(date, LocalTime.of(10, 30), ZONE);
        when(appointmentRepository.findConfirmedByClientIdAndDay(eq(CLIENT), any(), any()))
                .thenReturn(List.of(occupied(date, LocalTime.of(10, 0), LocalTime.of(11, 0), 1L)));

        assertTrue(service.hasClientOverlappingBooking(CLIENT, newStart, 60));
    }

    @Test
    void hasClientOverlappingBookingFalseWhenNoOverlap() {
        LocalDate date = LocalDate.now(ZONE).plusDays(2);
        ZonedDateTime newStart = ZonedDateTime.of(date, LocalTime.of(10, 0), ZONE);
        when(appointmentRepository.findConfirmedByClientIdAndDay(eq(CLIENT), any(), any()))
                .thenReturn(List.of(occupied(date, LocalTime.of(8, 0), LocalTime.of(9, 0), 1L)));

        assertFalse(service.hasClientOverlappingBooking(CLIENT, newStart, 60));
    }

    // ── isClientBooked ────────────────────────────────────────────────────────

    @Test
    void isClientBookedDelegatesToRepository() {
        ZonedDateTime slot = ZonedDateTime.now(ZONE).plusDays(2).withHour(10).withMinute(0);
        when(appointmentRepository.existsByClientIdAndProfessionalIdAndServiceIdAndStartAndStatus(
                CLIENT, PROF, SERVICE, slot, "CONFIRMED")).thenReturn(true);

        assertTrue(service.isClientBooked(CLIENT, PROF, SERVICE, slot));
    }
}