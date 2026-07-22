package ar.uba.fi.ingsoft1.turnos.schedule;

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

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ScheduleRestController.class)
@Import({ SecurityConfig.class, JwtService.class })
class ScheduleRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @MockitoBean
    private ScheduleService scheduleService;

        @MockitoBean
        private ScheduleBlockService scheduleBlockService;

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
    void getScheduleReturns200WithSchedule() throws Exception {
        WorkingHoursDTO dto = new WorkingHoursDTO(List.of(
                new WorkingHoursDayDTO("monday", true,
                        List.of(new TimeRangeDTO(LocalTime.of(9, 0), LocalTime.of(17, 0))))), 30);

        when(scheduleService.getSchedule(any())).thenReturn(dto);

        mockMvc.perform(get("/schedule")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.schedule[0].day").value("monday"))
                .andExpect(jsonPath("$.schedule[0].enabled").value(true));
    }

    @Test
    void getScheduleReturns403WithoutAuth() throws Exception {
        mockMvc.perform(get("/schedule"))
                .andExpect(status().isForbidden());
    }

    @Test
    void saveScheduleReturns200WhenValid() throws Exception {
        WorkingHoursDTO dto = new WorkingHoursDTO(List.of(
                new WorkingHoursDayDTO("monday", true,
                        List.of(new TimeRangeDTO(LocalTime.of(9, 0), LocalTime.of(17, 0))))), 30);

        when(scheduleService.saveSchedule(any(), any())).thenReturn(dto);

        mockMvc.perform(put("/schedule")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{\"schedule\":[{\"day\":\"monday\",\"enabled\":true,\"ranges\":[{\"from\":\"09:00\",\"to\":\"17:00\"}]}]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.schedule[0].day").value("monday"));
    }

    @Test
    void saveScheduleReturns403WithoutAuth() throws Exception {
        mockMvc.perform(put("/schedule")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"schedule\":[]}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void getScheduleReturns403WhenProfessionalNotFound() throws Exception {
        String unknownToken = "Bearer " + jwtService.createToken(
                new JwtUserDetails("unknown@mail.com", UserRole.PROFESSIONAL));
        when(professionalRepository.findByEmail("unknown@mail.com")).thenReturn(Optional.empty());

        mockMvc.perform(get("/schedule")
                .header("Authorization", unknownToken))
                .andExpect(status().isForbidden());
    }
}