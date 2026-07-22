package ar.uba.fi.ingsoft1.turnos.service;

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

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ServiceRestController.class)
@Import({ SecurityConfig.class, JwtService.class })
class ServiceRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @MockitoBean
    private ServiceService serviceService;

    @MockitoBean
    private ProfessionalRepository professionalRepository;

    private String token;

    @BeforeEach
    void setUp() {
        Professional prof = new Professional("prof@mail.com", "hashed", "Juan", "Perez");
        when(professionalRepository.findByEmail("prof@mail.com")).thenReturn(Optional.of(prof));
        token = "Bearer " + jwtService.createToken(new JwtUserDetails("prof@mail.com", UserRole.PROFESSIONAL));
    }

    @Test
    void getServicesReturns200WithList() throws Exception {
        ServiceEntity s = new ServiceEntity();
        s.setName("Corte");
        s.setDuration(30);
        s.setPrice(BigDecimal.valueOf(3500));
        s.setMaxCapacity(1);
        s.setActive(true);

        when(serviceService.getServices(any())).thenReturn(List.of(s));

        mockMvc.perform(get("/services")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Corte"));
    }

    @Test
    void getServicesReturns403WithoutAuth() throws Exception {
        mockMvc.perform(get("/services"))
                .andExpect(status().isForbidden());
    }

    @Test
    void createServiceReturns201() throws Exception {
        ServiceEntity saved = new ServiceEntity();
        saved.setName("Corte");
        saved.setDuration(30);
        saved.setPrice(BigDecimal.valueOf(3500));
        saved.setMaxCapacity(1);
        saved.setActive(true);

        when(serviceService.createService(any(), any())).thenReturn(saved);

        mockMvc.perform(post("/services")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Corte\",\"duration_minutes\":30,\"price\":3500,\"max_capacity\":1}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Corte"));
    }

    @Test
    void createServiceReturns403WithoutAuth() throws Exception {
        mockMvc.perform(post("/services")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Corte\",\"duration_minutes\":30,\"price\":3500,\"max_capacity\":1}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateServiceReturns200WhenFound() throws Exception {
        ServiceEntity updated = new ServiceEntity();
        updated.setName("Corte Premium");
        updated.setDuration(45);
        updated.setPrice(BigDecimal.valueOf(5000));
        updated.setMaxCapacity(1);
        updated.setActive(true);

        when(serviceService.updateService(any(), any(), any())).thenReturn(Optional.of(updated));

        mockMvc.perform(put("/services/1")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{\"name\":\"Corte Premium\",\"duration_minutes\":45,\"price\":5000,\"max_capacity\":1,\"active\":true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Corte Premium"));
    }

    @Test
    void updateServiceReturns404WhenNotFound() throws Exception {
        when(serviceService.updateService(any(), any(), any())).thenReturn(Optional.empty());

        mockMvc.perform(put("/services/99")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"X\",\"duration_minutes\":30,\"price\":1000,\"max_capacity\":1,\"active\":true}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateServiceReturns403WithoutAuth() throws Exception {
        mockMvc.perform(put("/services/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"X\",\"duration_minutes\":30,\"price\":1000,\"max_capacity\":1,\"active\":true}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void getServicesReturns403WhenProfessionalNotFound() throws Exception {
        String unknownToken = "Bearer " + jwtService.createToken(
                new JwtUserDetails("unknown@mail.com", UserRole.PROFESSIONAL));
        when(professionalRepository.findByEmail("unknown@mail.com")).thenReturn(Optional.empty());

        mockMvc.perform(get("/services")
                .header("Authorization", unknownToken))
                .andExpect(status().isForbidden());
    }
}
