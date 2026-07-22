package ar.uba.fi.ingsoft1.turnos.config.email;

import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

class EmailServiceTest {

    @Test
    void instanciacion_noLanzaExcepcionConDatosValidos() {
        assertDoesNotThrow(() -> new EmailService("key", "from@test.com", "http://localhost:5173"));
    }

    @Test
    void sendResetEmail_noLanzaExcepcionSiResendFalla() {
        EmailService servicioConKeyInvalida = new EmailService("key-invalida",
                "from@test.com", "http://localhost:5173");
        assertDoesNotThrow(() -> servicioConKeyInvalida.sendResetEmail("dest@test.com", "token123"));
    }

    @Test
    void sendAppointmentReminderEmail_noLanzaExcepcionSiResendFalla() {
        EmailService servicioConKeyInvalida = new EmailService("key-invalida",
                "from@test.com", "http://localhost:5173");
        assertDoesNotThrow(() ->
                servicioConKeyInvalida.sendAppointmentReminderEmail(
                        "dest@test.com",
                        "Nicolás",
                        "Consulta General",
                        "Dr. López",
                        "04-06-2026",
                        "15:00"
                )
        );
    }

    @Test
    void sendWaitListPromotionEmail_noLanzaExcepcionSiResendFalla() {
        EmailService servicioConKeyInvalida = new EmailService("key-invalida",
                "from@test.com", "http://localhost:5173");
        ZoneId zone = ZoneId.of("America/Argentina/Buenos_Aires");
        ZonedDateTime slot = ZonedDateTime.of(2030, 6, 1, 10, 0, 0, 0, zone);
        assertDoesNotThrow(() ->
                servicioConKeyInvalida.sendWaitListPromotionEmail(
                        "dest@test.com", "Nicolás", 1L, 2L, slot, slot.plusHours(2)));
    }
}