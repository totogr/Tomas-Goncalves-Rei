package ar.uba.fi.ingsoft1.turnos.appointment;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookingBlockUtilsTest {

    private static final ZoneId ZONE = ZoneId.of("America/Argentina/Buenos_Aires");
    private static final LocalDate DATE = LocalDate.of(2030, 6, 15);

    private Appointment appt(LocalTime start, LocalTime end) {
        Appointment a = new Appointment();
        a.setStart(ZonedDateTime.of(DATE, start, ZONE));
        a.setEnd(ZonedDateTime.of(DATE, end, ZONE));
        a.setStatus("CONFIRMED");
        return a;
    }

    @Test
    void countBookingsInBlock_countsAppointmentStartingExactlyAtBlockTime() {
        Appointment a = appt(LocalTime.of(10, 0), LocalTime.of(10, 30));
        long count = BookingBlockUtils.countBookingsInBlock(LocalTime.of(10, 0), List.of(a));
        assertEquals(1, count);
    }

    @Test
    void countBookingsInBlock_countsAppointmentThatCoversBlockTime() {
        Appointment a = appt(LocalTime.of(9, 30), LocalTime.of(10, 30));
        long count = BookingBlockUtils.countBookingsInBlock(LocalTime.of(10, 0), List.of(a));
        assertEquals(1, count);
    }

    @Test
    void countBookingsInBlock_doesNotCountAppointmentEndingAtBlockTime() {
        Appointment a = appt(LocalTime.of(9, 0), LocalTime.of(10, 0));
        long count = BookingBlockUtils.countBookingsInBlock(LocalTime.of(10, 0), List.of(a));
        assertEquals(0, count);
    }

    @Test
    void countBookingsInBlock_doesNotCountAppointmentAfterBlockTime() {
        Appointment a = appt(LocalTime.of(11, 0), LocalTime.of(11, 30));
        long count = BookingBlockUtils.countBookingsInBlock(LocalTime.of(10, 0), List.of(a));
        assertEquals(0, count);
    }

    @Test
    void countBookingsInBlock_countsMultipleOverlappingAppointments() {
        Appointment a1 = appt(LocalTime.of(10, 0), LocalTime.of(10, 30));
        Appointment a2 = appt(LocalTime.of(9, 45), LocalTime.of(10, 15));
        long count = BookingBlockUtils.countBookingsInBlock(LocalTime.of(10, 0), List.of(a1, a2));
        assertEquals(2, count);
    }

    @Test
    void countBookingsInBlock_returnsZeroForEmptyList() {
        assertEquals(0, BookingBlockUtils.countBookingsInBlock(LocalTime.of(10, 0), List.of()));
    }

    private WaitListPromotion promotion(LocalTime start) {
        WaitListPromotion p = new WaitListPromotion();
        p.setSlotStart(ZonedDateTime.of(DATE, start, ZONE));
        p.setClientId(1L);
        p.setProfessionalId(1L);
        p.setServiceId(100L);
        return p;
    }

    @Test
    void countPromotionsInBlock_countsPromotionStartingAtBlockTime() {
        WaitListPromotion p = promotion(LocalTime.of(10, 0));
        long count = BookingBlockUtils.countPromotionsInBlock(LocalTime.of(10, 0), List.of(p), 30);
        assertEquals(1, count);
    }

    @Test
    void countPromotionsInBlock_countsPromotionCoveringBlockTime() {
        WaitListPromotion p = promotion(LocalTime.of(9, 30));
        long count = BookingBlockUtils.countPromotionsInBlock(LocalTime.of(10, 0), List.of(p), 60);
        assertEquals(1, count);
    }

    @Test
    void countPromotionsInBlock_doesNotCountPromotionEndingAtBlockTime() {
        WaitListPromotion p = promotion(LocalTime.of(9, 0));
        long count = BookingBlockUtils.countPromotionsInBlock(LocalTime.of(10, 0), List.of(p), 60);
        assertEquals(0, count);
    }

    @Test
    void countPromotionsInBlock_returnsZeroForEmptyList() {
        assertEquals(0, BookingBlockUtils.countPromotionsInBlock(LocalTime.of(10, 0), List.of(), 30));
    }

    @Test
    void countPromotionsInBlock_countsMultiple() {
        WaitListPromotion p1 = promotion(LocalTime.of(10, 0));
        WaitListPromotion p2 = promotion(LocalTime.of(9, 30));
        long count = BookingBlockUtils.countPromotionsInBlock(LocalTime.of(10, 0), List.of(p1, p2), 60);
        assertEquals(2, count);
    }
}