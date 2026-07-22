package ar.uba.fi.ingsoft1.turnos.client;

import ar.uba.fi.ingsoft1.turnos.config.security.JwtService;
import ar.uba.fi.ingsoft1.turnos.config.security.JwtUserDetails;
import ar.uba.fi.ingsoft1.turnos.config.security.SecurityConfig;
import ar.uba.fi.ingsoft1.turnos.user.TokenDTO;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ClientRestController.class)
@Import({ SecurityConfig.class, JwtService.class})
class ClientRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @MockitoBean
    private ClientService clientService;

    @MockitoBean
    private ClientRepository clientRepository;

    private String clientToken;
    private String profToken;

    @BeforeEach
    void setUp() {
        clientToken = "Bearer " + jwtService.createToken(
                new JwtUserDetails("client@mail.com", UserRole.CLIENT));
        profToken = "Bearer " + jwtService.createToken(
                new JwtUserDetails("prof@mail.com", UserRole.PROFESSIONAL));
    }

    @Test
    void registerNewClient() throws Exception {
        var tokens = new TokenDTO("access-token", "refresh-token", UserRole.CLIENT, 1L, "Ana", "Lopez");
        when(clientService.register(any())).thenReturn(Optional.of(tokens));

        var jsonRequest = """
                {
                "email": "ana@example.com",
                "password": "password123",
                "firstName": "Ana",
                "lastName": "Lopez"
                }
                """;

        var request = post("/clients/signup")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpectAll(
                status().isCreated(),
                jsonPath("$.accessToken").value(tokens.accessToken()),
                jsonPath("$.refreshToken").value(tokens.refreshToken()));
    }

    @Test
    void registerExistingEmail() throws Exception {
        when(clientService.register(any())).thenReturn(Optional.empty());

        var jsonRequest = """
                {
                "email": "ana@example.com",
                "password": "password123",
                "firstName": "Ana",
                "lastName": "Lopez"
                }
                """;

        var request = post("/clients/signup")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpectAll(
                status().isConflict());
    }

    @Test
    void registerWithMissingLastName() throws Exception {
        var jsonRequest = """
                {
                "email": "ana@example.com",
                "password": "password123",
                "firstName": "Ana"
                }
                """;

        var request = post("/clients/signup")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpectAll(
                status().isBadRequest());
    }

    @Test
    void registerWithMalformedJson() throws Exception {
        var jsonRequest = "{";
        var request = post("/clients/signup")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpectAll(
                status().isBadRequest());
    }

    @Test
    void getAllClientsReturns200WithListForProfessional() throws Exception {
        var c1 = new ClientSummaryDTO(1L, "Ana", "Lopez", "ana@mail.com");
        var c2 = new ClientSummaryDTO(2L, "Bob", "Perez", "bob@mail.com");
        when(clientService.getAllClients()).thenReturn(List.of(c1, c2));

        mockMvc.perform(get("/clients")
                        .header("Authorization", profToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("Ana"))
                .andExpect(jsonPath("$[0].lastName").value("Lopez"))
                .andExpect(jsonPath("$[0].email").value("ana@mail.com"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].firstName").value("Bob"))
                .andExpect(jsonPath("$[1].lastName").value("Perez"))
                .andExpect(jsonPath("$[1].email").value("bob@mail.com"));
    }

    @Test
    void getAllClientsReturns403ForClientRole() throws Exception {
        mockMvc.perform(get("/clients")
                        .header("Authorization", clientToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllClientsReturns403WithoutAuth() throws Exception {
        mockMvc.perform(get("/clients"))
                .andExpect(status().isForbidden());
    }
}