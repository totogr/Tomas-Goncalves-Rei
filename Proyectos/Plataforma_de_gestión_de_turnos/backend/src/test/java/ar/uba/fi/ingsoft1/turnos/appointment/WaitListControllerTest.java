package ar.uba.fi.ingsoft1.turnos.appointment;

import ar.uba.fi.ingsoft1.turnos.appointment.dto.ActivePromotionDTO;
import ar.uba.fi.ingsoft1.turnos.appointment.dto.WaitListEntryDTO;
import ar.uba.fi.ingsoft1.turnos.client.Client;
import ar.uba.fi.ingsoft1.turnos.client.ClientRepository;
import ar.uba.fi.ingsoft1.turnos.common.exception.BadRequestException;
import ar.uba.fi.ingsoft1.turnos.common.exception.ItemNotFoundException;
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
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = WaitListController.class)
@Import({ SecurityConfig.class, JwtService.class })
class WaitListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @MockitoBean
    private WaitListService waitListService;

    @MockitoBean
    private ClientRepository clientRepository;

    @MockitoBean
    private ProfessionalRepository professionalRepository;

    private static final ZoneId ZONE = ZoneId.of("America/Argentina/Buenos_Aires");

    private String clientToken;
    private String profToken;

    @BeforeEach
    void setUp() {
        Client client = mock(Client.class);
        when(client.getId()).thenReturn(1L);
        when(clientRepository.findByEmail("client@mail.com")).thenReturn(Optional.of(client));
        clientToken = "Bearer " + jwtService.createToken(
                new JwtUserDetails("client@mail.com", UserRole.CLIENT));

        Professional prof = mock(Professional.class);
        when(prof.getId()).thenReturn(1L);
        when(professionalRepository.findByEmail("prof@mail.com")).thenReturn(Optional.of(prof));
        profToken = "Bearer " + jwtService.createToken(
                new JwtUserDetails("prof@mail.com", UserRole.PROFESSIONAL));
    }

    private WaitListEntryDTO sampleEntry() {
        return new WaitListEntryDTO(1L, 1L, "Juan Perez", 100L, "Corte",
                ZonedDateTime.of(2030, 1, 1, 10, 0, 0, 0, ZONE),
                ZonedDateTime.now(ZONE), 1);
    }

    private String slotParam() {
        return "2030-01-01T10:00:00-03:00";
    }

    @Test
    void joinReturns201WhenSuccessful() throws Exception {
        when(waitListService.joinWaitList(anyLong(), anyLong(), any(), anyLong()))
                .thenReturn(sampleEntry());

        mockMvc.perform(post("/waitlist")
                .header("Authorization", clientToken)
                .param("professionalId", "1")
                .param("serviceId", "100")
                .param("slotStart", slotParam()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.position").value(1));
    }

    @Test
    void joinReturns400WhenSlotAvailable() throws Exception {
        when(waitListService.joinWaitList(anyLong(), anyLong(), any(), anyLong()))
                .thenThrow(new BadRequestException("El turno tiene lugares disponibles. Reserva directamente."));

        mockMvc.perform(post("/waitlist")
                .header("Authorization", clientToken)
                .param("professionalId", "1")
                .param("serviceId", "100")
                .param("slotStart", slotParam()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void joinReturns403WithoutAuth() throws Exception {
        mockMvc.perform(post("/waitlist")
                .param("professionalId", "1")
                .param("serviceId", "100")
                .param("slotStart", slotParam()))
                .andExpect(status().isForbidden());
    }

    @Test
    void joinReturns400WhenAlreadyInWaitList() throws Exception {
        when(waitListService.joinWaitList(anyLong(), anyLong(), any(), anyLong()))
                .thenThrow(new BadRequestException("El cliente ya esta en la lista de espera para este turno."));

        mockMvc.perform(post("/waitlist")
                .header("Authorization", clientToken)
                .param("professionalId", "1")
                .param("serviceId", "100")
                .param("slotStart", slotParam()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void leaveReturns204WhenSuccessful() throws Exception {
        doNothing().when(waitListService).leaveWaitList(anyLong(), anyLong(), any(), anyLong());

        mockMvc.perform(delete("/waitlist")
                .header("Authorization", clientToken)
                .param("professionalId", "1")
                .param("serviceId", "100")
                .param("slotStart", slotParam()))
                .andExpect(status().isNoContent());
    }

    @Test
    void leaveReturns404WhenNotInQueue() throws Exception {
        doThrow(new ItemNotFoundException("El cliente no se encuentra en la lista de espera de este turno."))
                .when(waitListService).leaveWaitList(anyLong(), anyLong(), any(), anyLong());

        mockMvc.perform(delete("/waitlist")
                .header("Authorization", clientToken)
                .param("professionalId", "1")
                .param("serviceId", "100")
                .param("slotStart", slotParam()))
                .andExpect(status().isNotFound());
    }

    @Test
    void leaveReturns403WithoutAuth() throws Exception {
        mockMvc.perform(delete("/waitlist")
                .param("professionalId", "1")
                .param("serviceId", "100")
                .param("slotStart", slotParam()))
                .andExpect(status().isForbidden());
    }

    @Test
    void myPositionReturns200() throws Exception {
        when(waitListService.getClientWaitListEntry(anyLong(), anyLong(), any(), anyLong()))
                .thenReturn(sampleEntry());

        mockMvc.perform(get("/waitlist/me")
                .header("Authorization", clientToken)
                .param("professionalId", "1")
                .param("serviceId", "100")
                .param("slotStart", slotParam()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.position").value(1));
    }

    @Test
    void myPositionReturns404WhenNotInQueue() throws Exception {
        when(waitListService.getClientWaitListEntry(anyLong(), anyLong(), any(), anyLong()))
                .thenThrow(new ItemNotFoundException("El cliente no esta en la lista de espera para este turno."));

        mockMvc.perform(get("/waitlist/me")
                .header("Authorization", clientToken)
                .param("professionalId", "1")
                .param("serviceId", "100")
                .param("slotStart", slotParam()))
                .andExpect(status().isNotFound());
    }

    @Test
    void myPositionReturns403WithoutAuth() throws Exception {
        mockMvc.perform(get("/waitlist/me")
                .param("professionalId", "1")
                .param("serviceId", "100")
                .param("slotStart", slotParam()))
                .andExpect(status().isForbidden());
    }

    @Test
    void getWaitListReturns200WithEntries() throws Exception {
        when(waitListService.getWaitList(anyLong(), anyLong(), any()))
                .thenReturn(List.of(sampleEntry()));

        mockMvc.perform(get("/waitlist")
                .header("Authorization", clientToken)
                .param("professionalId", "1")
                .param("serviceId", "100")
                .param("slotStart", slotParam()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void getWaitListReturns200WhenEmpty() throws Exception {
        when(waitListService.getWaitList(anyLong(), anyLong(), any())).thenReturn(List.of());

        mockMvc.perform(get("/waitlist")
                .header("Authorization", clientToken)
                .param("professionalId", "1")
                .param("serviceId", "100")
                .param("slotStart", slotParam()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void myEntriesReturns200() throws Exception {
        when(waitListService.getClientWaitListEntries(1L)).thenReturn(List.of(sampleEntry()));

        mockMvc.perform(get("/waitlist/me/all")
                .header("Authorization", clientToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void myEntriesReturns403WithoutAuth() throws Exception {
        mockMvc.perform(get("/waitlist/me/all"))
                .andExpect(status().isForbidden());
    }

    @Test
    void confirmReturns200WhenSuccessful() throws Exception {
        doNothing().when(waitListService).confirmPromotion(anyLong(), anyLong(), any(), anyLong());

        mockMvc.perform(post("/waitlist/confirm")
                .header("Authorization", clientToken)
                .param("professionalId", "1")
                .param("serviceId", "100")
                .param("slotStart", slotParam()))
                .andExpect(status().isOk());
    }

    @Test
    void confirmReturns400WhenExpired() throws Exception {
        doThrow(new BadRequestException("El tiempo para confirmar expiró."))
                .when(waitListService).confirmPromotion(anyLong(), anyLong(), any(), anyLong());

        mockMvc.perform(post("/waitlist/confirm")
                .header("Authorization", clientToken)
                .param("professionalId", "1")
                .param("serviceId", "100")
                .param("slotStart", slotParam()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void confirmReturns404WhenNoActivePromotion() throws Exception {
        doThrow(new ItemNotFoundException("No tenés una oferta de turno activa para este slot."))
                .when(waitListService).confirmPromotion(anyLong(), anyLong(), any(), anyLong());

        mockMvc.perform(post("/waitlist/confirm")
                .header("Authorization", clientToken)
                .param("professionalId", "1")
                .param("serviceId", "100")
                .param("slotStart", slotParam()))
                .andExpect(status().isNotFound());
    }

    @Test
    void confirmReturns403WithoutAuth() throws Exception {
        mockMvc.perform(post("/waitlist/confirm")
                .param("professionalId", "1")
                .param("serviceId", "100")
                .param("slotStart", slotParam()))
                .andExpect(status().isForbidden());
    }

    @Test
    void getActivePromotionsReturns200() throws Exception {
        ZonedDateTime slot = ZonedDateTime.of(2030, 1, 1, 10, 0, 0, 0, ZONE);
        ActivePromotionDTO dto = new ActivePromotionDTO(
                10L, 1L, "Ana Lopez", "ana@mail.com", 100L, "Corte",
                slot, slot.plusHours(2), 60);
        when(waitListService.getActivePromotionsForProfessional(1L)).thenReturn(List.of(dto));

        mockMvc.perform(get("/waitlist/promotions/professional")
                .header("Authorization", profToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10));
    }

    @Test
    void getActivePromotionsReturns403WithoutAuth() throws Exception {
        mockMvc.perform(get("/waitlist/promotions/professional"))
                .andExpect(status().isForbidden());
    }

    @Test
    void getActivePromotionsReturnsEmptyList() throws Exception {
        when(waitListService.getActivePromotionsForProfessional(1L)).thenReturn(List.of());

        mockMvc.perform(get("/waitlist/promotions/professional")
                .header("Authorization", profToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}