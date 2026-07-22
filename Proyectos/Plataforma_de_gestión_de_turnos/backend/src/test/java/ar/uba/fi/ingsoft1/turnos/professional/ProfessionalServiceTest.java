package ar.uba.fi.ingsoft1.turnos.professional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import ar.uba.fi.ingsoft1.turnos.config.security.JwtService;
import ar.uba.fi.ingsoft1.turnos.review.ReviewRepository;
import ar.uba.fi.ingsoft1.turnos.user.*;
import ar.uba.fi.ingsoft1.turnos.user.refresh_token.RefreshToken;
import ar.uba.fi.ingsoft1.turnos.user.refresh_token.RefreshTokenService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProfessionalServiceTest {

    private ProfessionalService professionalService;
    private ProfessionalRepository professionalRepository;
    private RefreshTokenService refreshTokenService;
    private ReviewRepository reviewRepository;

    private static final String EMAIL = "profesional@mail.com";
    private static final String PASSWORD = "password123";
    private static final String FIRST_NAME = "Juan";
    private static final String LAST_NAME = "Perez";

    @BeforeEach
    void setup() {
        var passwordEncoder = new BCryptPasswordEncoder();
        var passwordHash = passwordEncoder.encode(PASSWORD);

        professionalRepository = mock(ProfessionalRepository.class);
        when(professionalRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        Professional mockProf = new Professional(EMAIL, passwordHash, FIRST_NAME, LAST_NAME);
        mockProf.setId(1L);
        when(professionalRepository.findByEmail(EMAIL)).thenReturn(Optional.of(mockProf));

        var key = "0".repeat(64);
        var jwtService = new JwtService(key, 3600000L);

        refreshTokenService = mock(RefreshTokenService.class);
        when(refreshTokenService.createFor(any(), anyString()))
                .thenReturn(
                        new RefreshToken("mock-token", 1L, "PROFESSIONAL", java.time.Instant.now().plusSeconds(1000)));

        professionalService = new ProfessionalService(
                professionalRepository,
                passwordEncoder,
                jwtService,
                refreshTokenService,
                reviewRepository);
    }

    @Test
    void registerNewProfessional() {
        var response = professionalService.register(new ProfessionalCreateDTO(EMAIL + ".ar", PASSWORD, FIRST_NAME,
                LAST_NAME));
        assertNotNull(response.orElseThrow());
    }

    @Test
    void registerExistingEmail() {
        var response = professionalService.register(new ProfessionalCreateDTO(EMAIL, PASSWORD, FIRST_NAME, LAST_NAME));
        assertEquals(Optional.empty(), response);
    }

    @Test
    void loginProfessional() {
        UserLoginDTO userLoginDTOtest = new UserLoginDTO(EMAIL, PASSWORD);
        var response = professionalService.login(userLoginDTOtest);
        assertNotNull(response.orElseThrow());
        assertNotNull(response.orElseThrow().accessToken());
    }

    @Test
    void loginWrongPassword() {
        UserLoginDTO userLoginDTOtest = new UserLoginDTO(EMAIL, "claveIncorrecta");
        var response = professionalService.login(userLoginDTOtest);
        assertEquals(Optional.empty(), response);
    }

    @Test
    void loginNonExistingUser() {
        UserLoginDTO userLoginDTOtest = new UserLoginDTO("nadie@mail.com", PASSWORD);
        var response = professionalService.login(userLoginDTOtest);
        assertEquals(Optional.empty(), response);
    }
}