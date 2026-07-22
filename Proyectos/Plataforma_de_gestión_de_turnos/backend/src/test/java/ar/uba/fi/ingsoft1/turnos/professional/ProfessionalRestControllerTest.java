package ar.uba.fi.ingsoft1.turnos.professional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import ar.uba.fi.ingsoft1.turnos.appointment.AvailabilityService;
import ar.uba.fi.ingsoft1.turnos.blockedclient.BlockedClientService;
import ar.uba.fi.ingsoft1.turnos.client.ClientRepository;
import ar.uba.fi.ingsoft1.turnos.config.security.JwtService;
import ar.uba.fi.ingsoft1.turnos.config.security.JwtUserDetails;
import ar.uba.fi.ingsoft1.turnos.config.security.SecurityConfig;
import ar.uba.fi.ingsoft1.turnos.review.ReviewRepository;
import ar.uba.fi.ingsoft1.turnos.service.ServiceRepository;
import ar.uba.fi.ingsoft1.turnos.user.TokenDTO;
import ar.uba.fi.ingsoft1.turnos.user.UserRole;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProfessionalRestController.class)
@Import({ SecurityConfig.class, JwtService.class })
class ProfessionalRestControllerTest {

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

        private String profToken;

        @BeforeEach
        void setUp() {
                Professional prof = mock(Professional.class);
                when(prof.getId()).thenReturn(1L);
                when(professionalRepository.findByEmail("prof@mail.com")).thenReturn(Optional.of(prof));
                profToken = "Bearer " + jwtService.createToken(
                                new JwtUserDetails("prof@mail.com", UserRole.PROFESSIONAL));
        }

        @Test
        void registerNewProfessional() throws Exception {
                var tokens = new TokenDTO("access-token", "refresh-token", UserRole.PROFESSIONAL, 1L, "Juan", "Perez");
                when(professionalService.register(any())).thenReturn(Optional.of(tokens));

                var request = post("/professionals/signup")
                                .content("{\"email\":\"juanperez@mail.com\",\"password\":\"secreto123\",\"firstName\"" +
                                                ":\"Juan\",\"lastName\":\"Perez\",\"specialty\":\"Kinesiólogo\",\"address\":\""
                                                +
                                                "Narnia\"}")
                                .contentType(MediaType.APPLICATION_JSON);
                mockMvc.perform(request).andExpectAll(
                                status().isCreated(),
                                jsonPath("$.accessToken").value(tokens.accessToken()),
                                jsonPath("$.refreshToken").value(tokens.refreshToken()));
        }

        @Test
        void registerExistingEmail() throws Exception {
                when(professionalService.register(any())).thenReturn(Optional.empty());

                var request = post("/professionals/signup")
                                .content("{\"email\":\"juanperez@mail.com\",\"password\":\"secreto123\",\"firstName\":\"Juan\",\"lastName\":\"Perez\",\"specialty\":\"Kinesiólogo\",\"address\":\"Bernal, Quilmes\"}")
                                .contentType(MediaType.APPLICATION_JSON);
                mockMvc.perform(request).andExpectAll(
                                status().isConflict());
        }

        @Test
        void registerWithMissingPassword() throws Exception {
                var request = post("/professionals/signup")
                                .content("{\"email\":\"juanperez@gmil.com\",\"specialty\":\"Kinesiólogo\"}")
                                .contentType(MediaType.APPLICATION_JSON);
                mockMvc.perform(request).andExpectAll(
                                status().isBadRequest());
        }

        @Test
        void registerWithMissingSpecialty() throws Exception {
                var request = post("/professionals/signup")
                                .content("{\"email\":\"juanperez@gmil.com\",\"password\":\"secreto123\"}")
                                .contentType(MediaType.APPLICATION_JSON);
                mockMvc.perform(request).andExpectAll(
                                status().isBadRequest());
        }

        @Test
        void registerWithMalformedJson() throws Exception {
                var request = post("/professionals/signup")
                                .content("{")
                                .contentType(MediaType.APPLICATION_JSON);
                mockMvc.perform(request).andExpectAll(
                                status().isBadRequest());
        }

        // ── PATCH /professionals/{id}/profile ─────────────────────────────────

        @Test
        void updateProfileReturns403WithoutAuth() throws Exception {
                mockMvc.perform(patch("/professionals/1/profile")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"specialty\":\"Kinesiólogo\",\"address\":\"Av. Corrientes 1234\","
                                                + "\"neighborhood\":\"Centro\",\"city\":\"Buenos Aires\"}"))
                                .andExpect(status().isForbidden());
        }

        @Test
        void updateProfileReturns403WhenProfessionalIdMismatch() throws Exception {
                // profToken pertenece al profesional con id=1; intenta patchear id=2
                mockMvc.perform(patch("/professionals/2/profile")
                                .header("Authorization", profToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"specialty\":\"Kinesiólogo\",\"address\":\"Av. Corrientes 1234\","
                                                + "\"neighborhood\":\"Centro\",\"city\":\"Buenos Aires\"}"))
                                .andExpect(status().isForbidden());
        }

        @Test
        void updateProfileReturns204WhenOwner() throws Exception {
                when(professionalService.updateProfile(any(), any())).thenReturn(true);

                mockMvc.perform(patch("/professionals/1/profile")
                                .header("Authorization", profToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"specialty\":\"Kinesiólogo\",\"address\":\"Av. Corrientes 1234\","
                                                + "\"neighborhood\":\"Centro\",\"city\":\"Buenos Aires\"}"))
                                .andExpect(status().isNoContent());
        }
}
