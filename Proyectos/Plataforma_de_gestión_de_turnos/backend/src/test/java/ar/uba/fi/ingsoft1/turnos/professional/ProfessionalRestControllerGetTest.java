package ar.uba.fi.ingsoft1.turnos.professional;

import ar.uba.fi.ingsoft1.turnos.appointment.AvailabilityService;
import ar.uba.fi.ingsoft1.turnos.appointment.dto.AvailabilityResponseDTO;
import ar.uba.fi.ingsoft1.turnos.appointment.dto.SlotDTO;
import ar.uba.fi.ingsoft1.turnos.blockedclient.BlockedClientService;
import ar.uba.fi.ingsoft1.turnos.client.Client;
import ar.uba.fi.ingsoft1.turnos.client.ClientRepository;
import ar.uba.fi.ingsoft1.turnos.config.security.JwtService;
import ar.uba.fi.ingsoft1.turnos.config.security.JwtUserDetails;
import ar.uba.fi.ingsoft1.turnos.config.security.SecurityConfig;
import ar.uba.fi.ingsoft1.turnos.review.ReviewRepository;
import ar.uba.fi.ingsoft1.turnos.service.ServiceEntity;
import ar.uba.fi.ingsoft1.turnos.service.ServiceRepository;
import ar.uba.fi.ingsoft1.turnos.user.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProfessionalRestController.class)
@Import({ SecurityConfig.class, JwtService.class })
class ProfessionalRestControllerGetTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @MockitoBean
    private ProfessionalService professionalService;
    @MockitoBean
    private AvailabilityService availabilityService;
    @MockitoBean
    private ProfessionalRepository professionalRepository;
    @MockitoBean
    private ServiceRepository serviceRepository;
    @MockitoBean
    private ReviewRepository reviewRepository;
    @MockitoBean
    private BlockedClientService blockedClientService;
    @MockitoBean
    private ClientRepository clientRepository;

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

    @Test
    void getAllProfessionalsReturns200AsClient() throws Exception {
        when(blockedClientService.getBlockedProfessionalIdsForClient(any())).thenReturn(List.of());
        when(professionalService.getAllProfessionals(List.of()))
                .thenReturn(List.of(new ProfessionalSummaryDTO(1L, "Juan", "Perez", "Kinesiólogo", 4.5)));

        mockMvc.perform(get("/professionals")
                .header("Authorization", clientToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].first_name").value("Juan"));
    }

    @Test
    void getAllProfessionalsFiltersBlockedForClient() throws Exception {
        Client clientObj = mock(Client.class);
        when(clientObj.getId()).thenReturn(1L);
        when(clientRepository.findByEmail("client@mail.com")).thenReturn(Optional.of(clientObj));
        when(blockedClientService.getBlockedProfessionalIdsForClient(1L)).thenReturn(List.of(2L));
        when(professionalService.getAllProfessionals(List.of(2L))).thenReturn(List.of());

        mockMvc.perform(get("/professionals")
                .header("Authorization", clientToken))
                .andExpect(status().isOk());
    }

    @Test
    void getAllProfessionalsDoesNotFilterForProfessionalRole() throws Exception {
        when(professionalService.getAllProfessionals(List.of())).thenReturn(List.of());

        mockMvc.perform(get("/professionals")
                .header("Authorization", profToken))
                .andExpect(status().isOk());
    }

    @Test
    void getProfessionalReturns200WhenFound() throws Exception {
        Professional prof = new Professional("p@mail.com", "x", "Juan", "Perez");
        prof.setId(1L);
        when(professionalRepository.findById(1L)).thenReturn(Optional.of(prof));
        when(serviceRepository.findByProfessionalId(1L)).thenReturn(List.of());
        when(reviewRepository.findAverageScoreByProfessionalId(1L)).thenReturn(Optional.of(4.5));
        when(reviewRepository.countByProfessionalId(1L)).thenReturn(10);
        when(blockedClientService.isBlocked(anyLong(), anyLong())).thenReturn(false);

        mockMvc.perform(get("/professionals/1")
                .header("Authorization", clientToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.first_name").value("Juan"))
                .andExpect(jsonPath("$.rating").value(4.5));
    }

    @Test
    void getProfessionalReturns200WithNullRatingWhenNoReviews() throws Exception {
        Professional prof = new Professional("p@mail.com", "x", "Ana", "Garcia");
        prof.setId(2L);
        when(professionalRepository.findById(2L)).thenReturn(Optional.of(prof));
        when(serviceRepository.findByProfessionalId(2L)).thenReturn(List.of());
        when(reviewRepository.findAverageScoreByProfessionalId(2L)).thenReturn(Optional.empty());
        when(reviewRepository.countByProfessionalId(2L)).thenReturn(0);
        when(blockedClientService.isBlocked(anyLong(), anyLong())).thenReturn(false);

        mockMvc.perform(get("/professionals/2")
                .header("Authorization", clientToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").doesNotExist());
    }

    @Test
    void getProfessionalReturns404WhenNotFound() throws Exception {
        when(professionalRepository.findById(99L)).thenReturn(Optional.empty());
        when(blockedClientService.isBlocked(anyLong(), anyLong())).thenReturn(false);

        mockMvc.perform(get("/professionals/99")
                .header("Authorization", clientToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void getProfessionalReturns404WhenClientIsBlocked() throws Exception {
        Client client = mock(Client.class);
        when(client.getId()).thenReturn(1L);
        when(clientRepository.findByEmail("client@mail.com")).thenReturn(Optional.of(client));
        when(blockedClientService.isBlocked(eq(1L), eq(1L))).thenReturn(true);

        mockMvc.perform(get("/professionals/1")
                .header("Authorization", clientToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void getProfessionalReturnsServicesWithDetails() throws Exception {
        Professional prof = new Professional("p@mail.com", "x", "Carlos", "Lopez");
        prof.setId(3L);
        when(professionalRepository.findById(3L)).thenReturn(Optional.of(prof));
        when(blockedClientService.isBlocked(anyLong(), anyLong())).thenReturn(false);

        ServiceEntity svc = new ServiceEntity();
        svc.setId(100L);
        svc.setName("Corte");
        svc.setDuration(30);
        svc.setPrice(BigDecimal.valueOf(1500));
        svc.setActive(true);
        when(serviceRepository.findByProfessionalId(3L)).thenReturn(List.of(svc));
        when(reviewRepository.findAverageScoreByProfessionalId(3L)).thenReturn(Optional.empty());
        when(reviewRepository.countByProfessionalId(3L)).thenReturn(0);

        mockMvc.perform(get("/professionals/3")
                .header("Authorization", clientToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.services[0].name").value("Corte"))
                .andExpect(jsonPath("$.services[0].duration_minutes").value(30));
    }

    @Test
    void getAvailabilityReturns200WithSlots() throws Exception {
        SlotDTO slot = new SlotDTO(LocalTime.of(10, 0), true, null, null);
        AvailabilityResponseDTO dto = new AvailabilityResponseDTO(
                LocalDate.of(2030, 6, 20), List.of(slot));
        when(availabilityService.getAvailability(eq(1L), any(), eq(100L), isNull()))
                .thenReturn(dto);

        mockMvc.perform(get("/professionals/1/services/100/availability")
                .header("Authorization", clientToken)
                .param("date", "2030-06-20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].available").value(true));
    }

    @Test
    void getAvailabilityReturnsEmptyListWhenNoSchedule() throws Exception {
        AvailabilityResponseDTO dto = new AvailabilityResponseDTO(
                LocalDate.of(2030, 6, 20), List.of());
        when(availabilityService.getAvailability(anyLong(), any(), anyLong(), isNull()))
                .thenReturn(dto);

        mockMvc.perform(get("/professionals/1/services/100/availability")
                .header("Authorization", clientToken)
                .param("date", "2030-06-20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}