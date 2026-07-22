package ar.uba.fi.ingsoft1.turnos.appointment;

import ar.uba.fi.ingsoft1.turnos.appointment.dto.*;
import ar.uba.fi.ingsoft1.turnos.client.Client;
import ar.uba.fi.ingsoft1.turnos.client.ClientRepository;
import ar.uba.fi.ingsoft1.turnos.config.security.JwtService;
import ar.uba.fi.ingsoft1.turnos.config.security.JwtUserDetails;
import ar.uba.fi.ingsoft1.turnos.config.security.SecurityConfig;
import ar.uba.fi.ingsoft1.turnos.professional.Professional;
import ar.uba.fi.ingsoft1.turnos.professional.ProfessionalRepository;
import ar.uba.fi.ingsoft1.turnos.user.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ar.uba.fi.ingsoft1.turnos.common.exception.BadRequestException;
import ar.uba.fi.ingsoft1.turnos.common.exception.ForbiddenException;
import ar.uba.fi.ingsoft1.turnos.common.exception.ItemNotFoundException;
import ar.uba.fi.ingsoft1.turnos.common.exception.SlotTakenException;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookingRestController.class)
@Import({ SecurityConfig.class, JwtService.class })
class BookingRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @MockitoBean
    private BookingService bookingService;

    @MockitoBean
    private ClientRepository clientRepository;

    @MockitoBean
    private ProfessionalRepository professionalRepository;

    private String clientToken;
    private String professionalToken;

    private static final ZoneId ZONE = ZoneId.of("America/Argentina/Buenos_Aires");

    @BeforeEach
    void setUp() {
        // Cliente mock con id=1
        Client client = mock(Client.class);
        when(client.getId()).thenReturn(1L);
        when(clientRepository.findByEmail("client@mail.com")).thenReturn(Optional.of(client));
        clientToken = "Bearer " + jwtService.createToken(
                new JwtUserDetails("client@mail.com", UserRole.CLIENT));

        // Profesional mock con id=1
        Professional prof = mock(Professional.class);
        when(prof.getId()).thenReturn(1L);
        when(professionalRepository.findByEmail("prof@mail.com")).thenReturn(Optional.of(prof));
        professionalToken = "Bearer " + jwtService.createToken(
                new JwtUserDetails("prof@mail.com", UserRole.PROFESSIONAL));
    }

    // ── POST /appointments ────────────────────────────────────────────────────

    @Test
    void createBookingReturns201WhenSuccessful() throws Exception {
        Appointment appt = new Appointment();
        appt.setStatus("CONFIRMED");
        appt.setProfessionalId(1L);
        appt.setServiceId(100L);
        appt.setClientId(1L);
        appt.setStart(ZonedDateTime.now(ZONE).plusDays(7));
        appt.setEnd(ZonedDateTime.now(ZONE).plusDays(7).plusHours(1));

        when(bookingService.createBooking(any(), eq(1L))).thenReturn(appt);

        mockMvc.perform(post("/appointments")
                .header("Authorization", clientToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"professional_id\":1,\"service_id\":100,\"date\":\"2026-06-10\",\"time\":\"10:00\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    void createBookingReturns409WhenSlotTaken() throws Exception {
        when(bookingService.createBooking(any(), eq(1L)))
                .thenThrow(new SlotTakenException());

        mockMvc.perform(post("/appointments")
                .header("Authorization", clientToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"professional_id\":1,\"service_id\":100,\"date\":\"2026-06-10\",\"time\":\"10:00\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    void createBookingReturns400WhenServiceInactive() throws Exception {
        when(bookingService.createBooking(any(), eq(1L)))
                .thenThrow(new BadRequestException("SERVICE_INACTIVE"));

        mockMvc.perform(post("/appointments")
                .header("Authorization", clientToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"professional_id\":1,\"service_id\":100,\"date\":\"2026-06-10\",\"time\":\"10:00\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createBookingReturns403WithoutAuth() throws Exception {
        mockMvc.perform(post("/appointments")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"professional_id\":1,\"service_id\":100,\"date\":\"2026-06-10\",\"time\":\"10:00\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void createBookingReturns403WhenClientNotFound() throws Exception {
        when(clientRepository.findByEmail("client@mail.com")).thenReturn(Optional.empty());

        mockMvc.perform(post("/appointments")
                .header("Authorization", clientToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"professional_id\":1,\"service_id\":100,\"date\":\"2026-06-10\",\"time\":\"10:00\"}"))
                .andExpect(status().isForbidden());
    }

    // ── GET /bookings/me ──────────────────────────────────────────────────────

    @Test
    void getMyBookingsReturns200WithList() throws Exception {
        LocalDate future = LocalDate.now(ZONE).plusDays(3);
        ClientBookingResponseDTO dto = new ClientBookingResponseDTO(
                1L, "CONFIRMED", null, "Corte", "Juan Perez",
                future, "10:00", 60, "Palermo, Buenos Aires");

        when(bookingService.getClientBookings(eq(1L), any())).thenReturn(List.of(dto));

        mockMvc.perform(get("/bookings/me")
                .header("Authorization", clientToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("CONFIRMED"))
                .andExpect(jsonPath("$[0].service_name").value("Corte"));
    }

    @Test
    void getMyBookingsWithUpcomingFilter() throws Exception {
        when(bookingService.getClientBookings(eq(1L), eq("upcoming"))).thenReturn(List.of());

        mockMvc.perform(get("/bookings/me?status=upcoming")
                .header("Authorization", clientToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getMyBookingsWithPastFilter() throws Exception {
        when(bookingService.getClientBookings(eq(1L), eq("past"))).thenReturn(List.of());

        mockMvc.perform(get("/bookings/me?status=past")
                .header("Authorization", clientToken))
                .andExpect(status().isOk());
    }

    @Test
    void getMyBookingsReturns403WithoutAuth() throws Exception {
        mockMvc.perform(get("/bookings/me"))
                .andExpect(status().isForbidden());
    }

    @Test
    void getMyBookingsReturns403WhenClientNotFound() throws Exception {
        when(clientRepository.findByEmail("client@mail.com")).thenReturn(Optional.empty());

        mockMvc.perform(get("/bookings/me")
                .header("Authorization", clientToken))
                .andExpect(status().isForbidden());
    }

    // ── GET /bookings/{id} ────────────────────────────────────────────────────

    @Test
    void getBookingDetailReturns200() throws Exception {
        LocalDate future = LocalDate.now(ZONE).plusDays(1);
        BookingDetailResponseDTO dto = new BookingDetailResponseDTO(
                1L, "CONFIRMED", null,
                new BookingDetailResponseDTO.ServiceDetailDTO(100L, "Corte", 60, BigDecimal.valueOf(1500)),
                new BookingDetailResponseDTO.ProfessionalDetailDTO(1L, "Juan", "Perez"),
                future, "2026-06-01T10:00", "2026-06-01T11:00", null);

        when(bookingService.getBookingDetail(eq(1L), eq(1L))).thenReturn(dto);

        mockMvc.perform(get("/bookings/1")
                .header("Authorization", clientToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    void getBookingDetailReturns403WhenWrongClient() throws Exception {
        when(bookingService.getBookingDetail(eq(1L), eq(1L)))
                .thenThrow(new ForbiddenException("El turno no pertenece al cliente autenticado"));

        mockMvc.perform(get("/bookings/1")
                .header("Authorization", clientToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void getBookingDetailReturns404WhenNotFound() throws Exception {
        when(bookingService.getBookingDetail(eq(99L), eq(1L)))
                .thenThrow(new ItemNotFoundException("Turno no encontrado"));

        mockMvc.perform(get("/bookings/99")
                .header("Authorization", clientToken))
                .andExpect(status().isNotFound());
    }

    // ── PATCH /bookings/{id}/cancel ───────────────────────────────────────────

    @Test
    void cancelBookingReturns200WhenSuccessful() throws Exception {
        mockMvc.perform(patch("/bookings/1/cancel")
                .header("Authorization", clientToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"))
                .andExpect(jsonPath("$.cancelled_by").value("client"));
    }

    @Test
    void cancelBookingReturns400WhenAlreadyPast() throws Exception {
        doThrow(new BadRequestException("No se puede cancelar un turno que ya pasó"))
                .when(bookingService).cancelBooking(eq(1L), eq(1L));

        mockMvc.perform(patch("/bookings/1/cancel")
                .header("Authorization", clientToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    void cancelBookingReturns403WhenWrongClient() throws Exception {
        doThrow(new ForbiddenException("No tenés permiso para cancelar este turno"))
                .when(bookingService).cancelBooking(eq(1L), eq(1L));

        mockMvc.perform(patch("/bookings/1/cancel")
                .header("Authorization", clientToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void cancelBookingReturns403WithoutAuth() throws Exception {
        mockMvc.perform(patch("/bookings/1/cancel"))
                .andExpect(status().isForbidden());
    }

    // ── GET /pro/bookings ─────────────────────────────────────────────────────

    @Test
    void getProfessionalBookingsReturns200WithList() throws Exception {
        LocalDate future = LocalDate.now(ZONE).plusDays(3);
        ProfessionalBookingResponseDTO dto = new ProfessionalBookingResponseDTO(
                1L,
                "CONFIRMED",
                null,
                "Corte",
                "Ana Lopez",
                "ana@mail.com",
                future,
                "10:00",
                "11:00",
                60,
                null
        );
        when(bookingService.getProfessionalBookings(eq(1L), any())).thenReturn(List.of(dto));
        mockMvc.perform(get("/pro/bookings")
                        .header("Authorization", professionalToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("CONFIRMED"))
                .andExpect(jsonPath("$[0].client_name").value("Ana Lopez"));
    }
    @Test
    void getProfessionalBookingsWithUpcomingFilter() throws Exception {
        when(bookingService.getProfessionalBookings(eq(1L), eq("upcoming"))).thenReturn(List.of());

        mockMvc.perform(get("/pro/bookings?status=upcoming")
                .header("Authorization", professionalToken))
                .andExpect(status().isOk());
    }

    @Test
    void getProfessionalBookingsReturns403WithoutAuth() throws Exception {
        mockMvc.perform(get("/pro/bookings"))
                .andExpect(status().isForbidden());
    }

    @Test
    void getProfessionalBookingsReturns403WhenProfessionalNotFound() throws Exception {
        when(professionalRepository.findByEmail("prof@mail.com")).thenReturn(Optional.empty());

        mockMvc.perform(get("/pro/bookings")
                .header("Authorization", professionalToken))
                .andExpect(status().isForbidden());
    }

    // ── PATCH /pro/bookings/{id}/cancel ───────────────────────────────────────

    @Test
    void cancelByProfessionalReturns200WhenSuccessful() throws Exception {
        mockMvc.perform(patch("/pro/bookings/1/cancel")
                .header("Authorization", professionalToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"))
                .andExpect(jsonPath("$.cancelled_by").value("professional"));
    }

    @Test
    void cancelByProfessionalReturns400WhenAlreadyPast() throws Exception {
        doThrow(new BadRequestException("No se puede cancelar un turno que ya pasó"))
                .when(bookingService).cancelBookingByProfessional(eq(1L), eq(1L));

        mockMvc.perform(patch("/pro/bookings/1/cancel")
                .header("Authorization", professionalToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    void cancelByProfessionalReturns403WhenWrongProfessional() throws Exception {
        doThrow(new ForbiddenException("El turno no pertenece a este profesional"))
                .when(bookingService).cancelBookingByProfessional(eq(1L), eq(1L));

        mockMvc.perform(patch("/pro/bookings/1/cancel")
                .header("Authorization", professionalToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void cancelByProfessionalReturns403WithoutAuth() throws Exception {
        mockMvc.perform(patch("/pro/bookings/1/cancel"))
                .andExpect(status().isForbidden());
    }
}