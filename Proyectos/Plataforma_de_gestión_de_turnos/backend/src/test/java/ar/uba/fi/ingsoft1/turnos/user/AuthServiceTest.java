package ar.uba.fi.ingsoft1.turnos.user;

import ar.uba.fi.ingsoft1.turnos.client.Client;
import ar.uba.fi.ingsoft1.turnos.client.ClientRepository;
import ar.uba.fi.ingsoft1.turnos.config.security.JwtService;
import ar.uba.fi.ingsoft1.turnos.professional.Professional;
import ar.uba.fi.ingsoft1.turnos.professional.ProfessionalRepository;
import ar.uba.fi.ingsoft1.turnos.user.refresh_token.RefreshToken;
import ar.uba.fi.ingsoft1.turnos.user.refresh_token.RefreshTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private AuthService authService;
    private ProfessionalRepository professionalRepository;
    private ClientRepository clientRepository;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;
    private RefreshTokenService refreshTokenService;

    @BeforeEach
    void setUp() {
        professionalRepository = mock(ProfessionalRepository.class);
        clientRepository = mock(ClientRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        jwtService = mock(JwtService.class);
        refreshTokenService = mock(RefreshTokenService.class);

        authService = new AuthService(
                professionalRepository, clientRepository,
                passwordEncoder, jwtService, refreshTokenService);

        when(jwtService.createToken(any())).thenReturn("access-token");
        when(refreshTokenService.createFor(any(), any()))
                .thenReturn(new RefreshToken("refresh-token", 1L, "PROFESSIONAL", Instant.now().plusSeconds(3600)));
    }

    @Test
    void loginReturnsProfessionalTokenWhenCredentialsMatch() {
        Professional prof = new Professional("prof@mail.com", "hashed", "Juan", "Perez");
        when(professionalRepository.findByEmail("prof@mail.com")).thenReturn(Optional.of(prof));
        when(passwordEncoder.matches("pass", "hashed")).thenReturn(true);

        Optional<TokenDTO> result = authService.login(new UserLoginDTO("prof@mail.com", "pass"));

        assertTrue(result.isPresent());
        assertEquals(UserRole.PROFESSIONAL, result.get().role());
        assertEquals("access-token", result.get().accessToken());
    }

    @Test
    void loginReturnsClientTokenWhenProfessionalNotFound() {
        when(professionalRepository.findByEmail("client@mail.com")).thenReturn(Optional.empty());

        Client client = new Client("client@mail.com", "hashed", "Ana", "Lopez");
        when(clientRepository.findByEmail("client@mail.com")).thenReturn(Optional.of(client));
        when(passwordEncoder.matches("pass", "hashed")).thenReturn(true);
        when(refreshTokenService.createFor(any(), any()))
                .thenReturn(new RefreshToken("refresh-token", 1L, "CLIENT", Instant.now().plusSeconds(3600)));

        Optional<TokenDTO> result = authService.login(new UserLoginDTO("client@mail.com", "pass"));

        assertTrue(result.isPresent());
        assertEquals(UserRole.CLIENT, result.get().role());
    }

    @Test
    void loginReturnsEmptyWhenNeitherFound() {
        when(professionalRepository.findByEmail("noone@mail.com")).thenReturn(Optional.empty());
        when(clientRepository.findByEmail("noone@mail.com")).thenReturn(Optional.empty());

        Optional<TokenDTO> result = authService.login(new UserLoginDTO("noone@mail.com", "pass"));

        assertTrue(result.isEmpty());
    }

    @Test
    void loginReturnsEmptyWhenProfessionalPasswordWrong() {
        Professional prof = new Professional("prof@mail.com", "hashed", "Juan", "Perez");
        when(professionalRepository.findByEmail("prof@mail.com")).thenReturn(Optional.of(prof));
        when(passwordEncoder.matches("wrongpass", "hashed")).thenReturn(false);
        when(clientRepository.findByEmail("prof@mail.com")).thenReturn(Optional.empty());

        Optional<TokenDTO> result = authService.login(new UserLoginDTO("prof@mail.com", "wrongpass"));

        assertTrue(result.isEmpty());
    }

    @Test
    void loginReturnsEmptyWhenClientPasswordWrong() {
        when(professionalRepository.findByEmail("client@mail.com")).thenReturn(Optional.empty());

        Client client = new Client("client@mail.com", "hashed", "Ana", "Lopez");
        when(clientRepository.findByEmail("client@mail.com")).thenReturn(Optional.of(client));
        when(passwordEncoder.matches("wrongpass", "hashed")).thenReturn(false);

        Optional<TokenDTO> result = authService.login(new UserLoginDTO("client@mail.com", "wrongpass"));

        assertTrue(result.isEmpty());
    }

    @Test
    void refreshReturnsEmptyWhenTokenInvalid() {
        when(refreshTokenService.verify("bad-token")).thenReturn(Optional.empty());

        Optional<TokenDTO> result = authService.refresh(new RefreshDTO("bad-token"));

        assertTrue(result.isEmpty());
    }

    @Test
    void refreshReturnsProfessionalTokenWhenValid() {
        RefreshToken token = new RefreshToken("good-token", 1L, "PROFESSIONAL", Instant.now().plusSeconds(3600));
        when(refreshTokenService.verify("good-token")).thenReturn(Optional.of(token));

        Professional prof = new Professional("prof@mail.com", "hashed", "Juan", "Perez");
        when(professionalRepository.findById(1L)).thenReturn(Optional.of(prof));
        when(refreshTokenService.createFor(any(), any()))
                .thenReturn(new RefreshToken("new-refresh", 1L, "PROFESSIONAL", Instant.now().plusSeconds(3600)));

        Optional<TokenDTO> result = authService.refresh(new RefreshDTO("good-token"));

        assertTrue(result.isPresent());
        assertEquals(UserRole.PROFESSIONAL, result.get().role());
    }

    @Test
    void refreshReturnsClientTokenWhenValid() {
        RefreshToken token = new RefreshToken("good-token", 2L, "CLIENT", Instant.now().plusSeconds(3600));
        when(refreshTokenService.verify("good-token")).thenReturn(Optional.of(token));

        Client client = new Client("client@mail.com", "hashed", "Ana", "Lopez");
        when(clientRepository.findById(2L)).thenReturn(Optional.of(client));
        when(refreshTokenService.createFor(any(), any()))
                .thenReturn(new RefreshToken("new-refresh", 2L, "CLIENT", Instant.now().plusSeconds(3600)));

        Optional<TokenDTO> result = authService.refresh(new RefreshDTO("good-token"));

        assertTrue(result.isPresent());
        assertEquals(UserRole.CLIENT, result.get().role());
    }
}