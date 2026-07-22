package ar.uba.fi.ingsoft1.turnos.client;

import ar.uba.fi.ingsoft1.turnos.config.security.JwtService;
import ar.uba.fi.ingsoft1.turnos.user.refresh_token.RefreshTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ClientServiceTest {

    private ClientService clientService;
    private ClientRepository clientRepository;

    private static final String EMAIL = "cliente@example.com";
    private static final String PASSWORD = "password123";

    @BeforeEach
    void setUp() {
        var passwordEncoder = new BCryptPasswordEncoder();
        var passwordHash = passwordEncoder.encode(PASSWORD);

        clientRepository = mock(ClientRepository.class);
        when(clientRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        Client mockClient = new Client(EMAIL, passwordHash, "Juan", "Perez");
        when(clientRepository.findByEmail(EMAIL)).thenReturn(Optional.of(mockClient));
        when(clientRepository.findById(1L)).thenReturn(Optional.of(mockClient));

        var key = "0".repeat(64);
        clientService = new ClientService(
                clientRepository,
                new BCryptPasswordEncoder(),
                new JwtService(key, 1L),
                new RefreshTokenService(1L, 20, mock()));
    }

    @Test
    void registerNewClient() {
        var response = clientService.register(new ClientCreateDTO(EMAIL + "_new", PASSWORD, "Ana", "Lopez"));
        assertNotNull(response.orElseThrow());
    }

    @Test
    void registerExistingEmail() {
        var response = clientService.register(new ClientCreateDTO(EMAIL, PASSWORD, "Juan", "Perez"));
        assertEquals(Optional.empty(), response);
    }

    @Test
    void getAllClientsReturnsMappedList() {
        Client c1 = new Client("a@mail.com", "pass1", "Alice", "García");
        Client c2 = new Client("b@mail.com", "pass2", "Bob", "Pérez");
        when(clientRepository.findAll()).thenReturn(List.of(c1, c2));

        List<ClientSummaryDTO> result = clientService.getAllClients();

        assertEquals(2, result.size());
        assertEquals(c1.getId(), result.get(0).id());
        assertEquals("Alice", result.get(0).firstName());
        assertEquals("García", result.get(0).lastName());
        assertEquals("a@mail.com", result.get(0).email());
        assertEquals(c2.getId(), result.get(1).id());
        assertEquals("Bob", result.get(1).firstName());
        assertEquals("Pérez", result.get(1).lastName());
        assertEquals("b@mail.com", result.get(1).email());
        verify(clientRepository).findAll();
    }

    @Test
    void getAllClientsReturnsEmptyList() {
        when(clientRepository.findAll()).thenReturn(List.of());
        List<ClientSummaryDTO> result = clientService.getAllClients();
        assertTrue(result.isEmpty());
        verify(clientRepository).findAll();
    }

    @Test
    void updateClientPreferences_updatesValueSuccessfully() {
        boolean result = clientService.updateClientPreferences(1L, false);
        assertFalse(result);
    }
}