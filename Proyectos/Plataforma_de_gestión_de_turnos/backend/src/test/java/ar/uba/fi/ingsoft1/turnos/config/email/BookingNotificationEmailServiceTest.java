package ar.uba.fi.ingsoft1.turnos.config.email;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * Verifica que las notificaciones construyen el HTML y no propagan excepciones
 * aunque el envío por Resend falle (el error se traga y se loguea).
 */
class BookingNotificationEmailServiceTest {

    private BookingNotificationEmailService service;

    private final LocalDate date = LocalDate.of(2030, 6, 1);
    private final LocalTime time = LocalTime.of(10, 0);

    @BeforeEach
    void setUp() {
        service = new BookingNotificationEmailService("key-invalida", "from@test.com");
    }

    @Test
    void notifyProfessionalNewBooking_noLanzaExcepcion() {
        assertDoesNotThrow(() -> service.notifyProfessionalNewBooking(
                "pro@test.com", "Juan Perez", "Ana Lopez", "ana@test.com", "Corte", date, time));
    }

    @Test
    void notifyProfessionalCancellation_noLanzaExcepcion() {
        assertDoesNotThrow(() -> service.notifyProfessionalCancellation(
                "pro@test.com", "Juan Perez", "Ana Lopez", "Corte", date, time));
    }

    @Test
    void notifyProfessionalReschedule_noLanzaExcepcion() {
        assertDoesNotThrow(() -> service.notifyProfessionalReschedule(
                "pro@test.com", "Juan Perez", "Ana Lopez", "Corte",
                date, time, date.plusDays(1), time.plusHours(1)));
    }

    @Test
    void notifyClientNewBooking_noLanzaExcepcion() {
        assertDoesNotThrow(() -> service.notifyClientNewBooking(
                "ana@test.com", "Ana Lopez", "Corte", "Juan Perez", date, time));
    }

    @Test
    void notifyClientCancelledByProfessional_noLanzaExcepcion() {
        assertDoesNotThrow(() -> service.notifyClientCancelledByProfessional(
                "ana@test.com", "Ana Lopez", "Corte", "Juan Perez", date, time));
    }

    @Test
    void notifyClientReschedule_noLanzaExcepcion() {
        assertDoesNotThrow(() -> service.notifyClientReschedule(
                "ana@test.com", "Ana Lopez", "Corte", "Juan Perez", date, time));
    }
}
