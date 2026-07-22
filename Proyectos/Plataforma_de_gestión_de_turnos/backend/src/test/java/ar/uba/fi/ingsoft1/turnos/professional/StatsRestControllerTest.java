package ar.uba.fi.ingsoft1.turnos.professional;

import ar.uba.fi.ingsoft1.turnos.config.security.JwtService;
import ar.uba.fi.ingsoft1.turnos.config.security.JwtUserDetails;
import ar.uba.fi.ingsoft1.turnos.config.security.SecurityConfig;
import ar.uba.fi.ingsoft1.turnos.user.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = StatsRestController.class)
@Import({ SecurityConfig.class, JwtService.class })
class StatsRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @MockitoBean
    private StatsService statsService;

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

    private StatsResponseDTO emptyStats() {
        return new StatsResponseDTO(0, 0.0, null, 0.0, List.of(), List.of(), List.of());
    }

    @Test
    void getStatsReturns200WithDefaultPeriod() throws Exception {
        when(statsService.getStats(eq(1L), eq("30d"))).thenReturn(emptyStats());

        mockMvc.perform(get("/pro/stats")
                .header("Authorization", profToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total_appointments").value(0));
    }

    @Test
    void getStatsReturns200WithPeriod7d() throws Exception {
        when(statsService.getStats(eq(1L), eq("7d"))).thenReturn(emptyStats());

        mockMvc.perform(get("/pro/stats?period=7d")
                .header("Authorization", profToken))
                .andExpect(status().isOk());
    }

    @Test
    void getStatsReturns200WithPeriod3m() throws Exception {
        when(statsService.getStats(eq(1L), eq("3m"))).thenReturn(emptyStats());

        mockMvc.perform(get("/pro/stats?period=3m")
                .header("Authorization", profToken))
                .andExpect(status().isOk());
    }

    @Test
    void getStatsReturns400WithInvalidPeriod() throws Exception {
        mockMvc.perform(get("/pro/stats?period=invalid")
                .header("Authorization", profToken))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getStatsReturns403WithoutAuth() throws Exception {
        mockMvc.perform(get("/pro/stats"))
                .andExpect(status().isForbidden());
    }

    @Test
    void getStatsReturns403WhenProfessionalNotFound() throws Exception {
        when(professionalRepository.findByEmail("prof@mail.com")).thenReturn(Optional.empty());

        mockMvc.perform(get("/pro/stats")
                .header("Authorization", profToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void getStatsReturnsFullStatsPayload() throws Exception {
        StatsResponseDTO stats = new StatsResponseDTO(
                10, 20.0, 4.5, 15000.0, List.of(), List.of(), List.of());
        when(statsService.getStats(eq(1L), eq("30d"))).thenReturn(stats);

        mockMvc.perform(get("/pro/stats")
                .header("Authorization", profToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total_appointments").value(10))
                .andExpect(jsonPath("$.cancellation_rate").value(20.0))
                .andExpect(jsonPath("$.average_rating").value(4.5))
                .andExpect(jsonPath("$.estimated_revenue").value(15000.0));
    }
}