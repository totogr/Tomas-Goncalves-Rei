package ar.uba.fi.ingsoft1.turnos.appointment;

import ar.uba.fi.ingsoft1.turnos.appointment.dto.BookingDetailResponseDTO;
import ar.uba.fi.ingsoft1.turnos.appointment.dto.BookingRequestDTO;
import ar.uba.fi.ingsoft1.turnos.appointment.dto.ClientBookingResponseDTO;
import ar.uba.fi.ingsoft1.turnos.config.security.CurrentClientId;
import ar.uba.fi.ingsoft1.turnos.config.security.CurrentProfessionalId;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import ar.uba.fi.ingsoft1.turnos.appointment.dto.ProfessionalBookingResponseDTO;
import ar.uba.fi.ingsoft1.turnos.appointment.dto.RescheduleRequestDTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Bookings")
public class BookingRestController {

        private final BookingService bookingService;

        public BookingRestController(BookingService bookingService) {
                this.bookingService = bookingService;
        }

        @GetMapping("/bookings/me")
        public ResponseEntity<List<ClientBookingResponseDTO>> getMyBookings(
                        @RequestParam(value = "status", defaultValue = "upcoming") String status,
                        @CurrentClientId Long clientId) {

                return ResponseEntity.ok(bookingService.getClientBookings(clientId, status));
        }

        @GetMapping("/bookings/{id}")
        public ResponseEntity<BookingDetailResponseDTO> getBookingDetail(
                        @PathVariable("id") Long id,
                        @CurrentClientId Long clientId) {

                return ResponseEntity.ok(bookingService.getBookingDetail(id, clientId));
        }

        @PatchMapping("/bookings/{id}/cancel")
        public ResponseEntity<java.util.Map<String, Object>> cancelBooking(
                        @PathVariable("id") Long id,
                        @CurrentClientId Long clientId) {

                bookingService.cancelBooking(id, clientId);

                return ResponseEntity.ok(java.util.Map.of(
                                "id", id,
                                "status", "CANCELLED",
                                "cancelled_by", "client"));
        }

        @PostMapping(value = "/appointments", consumes = "application/json", produces = "application/json")
        public ResponseEntity<AppointmentResponseDTO> createBooking(
                        @Valid @RequestBody BookingRequestDTO request,
                        @CurrentClientId Long clientId) {

                Appointment appointment = bookingService.createBooking(request, clientId);
                return ResponseEntity.status(HttpStatus.CREATED).body(AppointmentResponseDTO.from(appointment));
        }

        @GetMapping("/pro/bookings")
        public ResponseEntity<List<ProfessionalBookingResponseDTO>> getProfessionalBookings(
                        @RequestParam(value = "status", defaultValue = "all") String status,
                        @CurrentProfessionalId Long profId) {

                return ResponseEntity.ok(bookingService.getProfessionalBookings(profId, status));
        }

        @PatchMapping("/pro/bookings/{id}/cancel")
        public ResponseEntity<java.util.Map<String, Object>> cancelBookingByProfessional(
                        @PathVariable("id") Long id,
                        @CurrentProfessionalId Long profId) {

                bookingService.cancelBookingByProfessional(id, profId);

                return ResponseEntity.ok(java.util.Map.of(
                                "id", id,
                                "status", "CANCELLED",
                                "cancelled_by", "professional"));
        }

        @PatchMapping("/pro/bookings/{id}/absent")
        public ResponseEntity<java.util.Map<String, Object>> markAbsent(
                        @PathVariable("id") Long id,
                        @CurrentProfessionalId Long profId) {

                bookingService.markAbsent(id, profId);

                return ResponseEntity.ok(java.util.Map.of(
                                "id", id,
                                "markedAbsent", true));
        }

        @PatchMapping("/bookings/{id}/reschedule")
        public ResponseEntity<java.util.Map<String, Object>> rescheduleBooking(
                        @PathVariable("id") Long id,
                        @Valid @RequestBody RescheduleRequestDTO request,
                        @CurrentClientId Long clientId) {

                bookingService.rescheduleBooking(id, clientId, request);

                return ResponseEntity.ok(java.util.Map.of(
                                "id", id,
                                "status", "CONFIRMED",
                                "date", request.date().toString(),
                                "time", request.time().toString()));
        }

        public record AppointmentResponseDTO(
                        Long id,
                        String status,
                        @JsonProperty("start") String start,
                        @JsonProperty("end") String end) {
                static AppointmentResponseDTO from(Appointment a) {
                        return new AppointmentResponseDTO(
                                        a.getId(),
                                        a.getStatus(),
                                        a.getStart() != null ? a.getStart().toString() : null,
                                        a.getEnd() != null ? a.getEnd().toString() : null);
                }
        }
}
