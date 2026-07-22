package ar.uba.fi.ingsoft1.turnos.blockedclient;

import ar.uba.fi.ingsoft1.turnos.common.exception.ConflictException;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BlockedClientRestController.class)
@Import({ SecurityConfig.class, JwtService.class })
class BlockedClientRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @MockitoBean
    private BlockedClientService blockedClientService;

    @MockitoBean
    private ProfessionalRepository professionalRepository;

    private String profToken;

    @BeforeEach
    void setUp() {
        Professional prof = mock(Professional.class);
        when(prof.getId()).thenReturn(1L);
        when(professionalRepository.findByEmail("prof@mail.com")).thenReturn(Optional.of(prof));
        profToken = "Bearer " + jwtService.createToken(
                new JwtUserDetails("prof@mail.com", UserRole.PROFESSIONAL));
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private BlockedClient buildBlockedClient(Long professionalId, Long clientId) {
        return new BlockedClient(professionalId, clientId);
    }

    // ── POST /professionals/{professionalId}/blocked-clients ──────────────────

    @Test
    void blockClientReturns201WhenSuccessful() throws Exception {
        BlockedClient result = buildBlockedClient(1L, 10L);
        when(blockedClientService.blockClient(eq(1L), eq(10L))).thenReturn(result);

        mockMvc.perform(post("/professionals/1/blocked-clients")
                .header("Authorization", profToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"clientId\":10}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.professionalId").value(1))
                .andExpect(jsonPath("$.clientId").value(10));
    }

    @Test
    void blockClientReturns409WhenAlreadyBlocked() throws Exception {
        when(blockedClientService.blockClient(eq(1L), eq(10L)))
                .thenThrow(new ConflictException("El cliente ya está bloqueado"));

        mockMvc.perform(post("/professionals/1/blocked-clients")
                .header("Authorization", profToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"clientId\":10}"))
                .andExpect(status().isConflict());
    }

    @Test
    void blockClientReturns403WhenProfessionalIdMismatch() throws Exception {
        mockMvc.perform(post("/professionals/2/blocked-clients")
                .header("Authorization", profToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"clientId\":10}"))
                .andExpect(status().isForbidden());

        verify(blockedClientService, never()).blockClient(anyLong(), anyLong());
    }

    @Test
    void blockClientReturns403WithoutAuth() throws Exception {
        mockMvc.perform(post("/professionals/1/blocked-clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"clientId\":10}"))
                .andExpect(status().isForbidden());
    }

    // ── DELETE /professionals/{professionalId}/blocked-clients/{clientId} ─────

    @Test
    void unblockClientReturns204WhenSuccessful() throws Exception {
        mockMvc.perform(delete("/professionals/1/blocked-clients/10")
                .header("Authorization", profToken))
                .andExpect(status().isNoContent());

        verify(blockedClientService).unblockClient(1L, 10L);
    }

    @Test
    void unblockClientReturns404WhenNotFound() throws Exception {
        doThrow(new ItemNotFoundException("Bloqueo no encontrado"))
                .when(blockedClientService).unblockClient(eq(1L), eq(99L));

        mockMvc.perform(delete("/professionals/1/blocked-clients/99")
                .header("Authorization", profToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void unblockClientReturns403WhenProfessionalIdMismatch() throws Exception {
        mockMvc.perform(delete("/professionals/2/blocked-clients/10")
                .header("Authorization", profToken))
                .andExpect(status().isForbidden());

        verify(blockedClientService, never()).unblockClient(anyLong(), anyLong());
    }

    @Test
    void unblockClientReturns403WithoutAuth() throws Exception {
        mockMvc.perform(delete("/professionals/1/blocked-clients/10"))
                .andExpect(status().isForbidden());
    }

    // ── GET /professionals/{professionalId}/blocked-clients ───────────────────

    @Test
    void getBlockedClientsReturns200WithList() throws Exception {
        when(blockedClientService.getBlockedClientIds(1L))
                .thenReturn(List.of(10L, 20L, 30L));

        mockMvc.perform(get("/professionals/1/blocked-clients")
                .header("Authorization", profToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(10))
                .andExpect(jsonPath("$[1]").value(20))
                .andExpect(jsonPath("$[2]").value(30));
    }

    @Test
    void getBlockedClientsReturns200WithEmptyList() throws Exception {
        when(blockedClientService.getBlockedClientIds(1L))
                .thenReturn(List.of());

        mockMvc.perform(get("/professionals/1/blocked-clients")
                .header("Authorization", profToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getBlockedClientsReturns403WhenProfessionalIdMismatch() throws Exception {
        mockMvc.perform(get("/professionals/2/blocked-clients")
                .header("Authorization", profToken))
                .andExpect(status().isForbidden());

        verify(blockedClientService, never()).getBlockedClientIds(anyLong());
    }

    @Test
    void getBlockedClientsReturns403WithoutAuth() throws Exception {
        mockMvc.perform(get("/professionals/1/blocked-clients"))
                .andExpect(status().isForbidden());
    }
}