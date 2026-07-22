package ar.uba.fi.ingsoft1.turnos.professional;

import ar.uba.fi.ingsoft1.turnos.config.security.JwtService;
import ar.uba.fi.ingsoft1.turnos.review.ReviewRepository;
import ar.uba.fi.ingsoft1.turnos.user.TokenDTO;
import ar.uba.fi.ingsoft1.turnos.user.UserLoginDTO;
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

class ProfessionalServiceLoginTest {

    private ProfessionalService professionalService;
    private ProfessionalRepository professionalRepository;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;
    private RefreshTokenService refreshTokenService;
    private ReviewRepository reviewRepository;

    @BeforeEach
    void setUp() {
        professionalRepository = mock(ProfessionalRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        jwtService = mock(JwtService.class);
        refreshTokenService = mock(RefreshTokenService.class);
        reviewRepository = mock(ReviewRepository.class);

        professionalService = new ProfessionalService(
                professionalRepository, passwordEncoder, jwtService, refreshTokenService, reviewRepository);

        when(jwtService.createToken(any())).thenReturn("access-token");
        when(refreshTokenService.createFor(any(), any()))
                .thenReturn(new RefreshToken("refresh-token", 1L, "PROFESSIONAL", Instant.now().plusSeconds(3600)));
    }

    @Test
    void loginReturnsEmptyWhenEmailNotFound() {
        when(professionalRepository.findByEmail("noexiste@mail.com")).thenReturn(Optional.empty());

        Optional<TokenDTO> result = professionalService.login(new UserLoginDTO("noexiste@mail.com", "pass"));

        assertTrue(result.isEmpty());
    }

    @Test
    void loginReturnsEmptyWhenPasswordDoesNotMatch() {
        Professional prof = new Professional("prof@mail.com", "hashedpass", "Juan", "Perez");
        when(professionalRepository.findByEmail("prof@mail.com")).thenReturn(Optional.of(prof));
        when(passwordEncoder.matches("wrongpass", "hashedpass")).thenReturn(false);

        Optional<TokenDTO> result = professionalService.login(new UserLoginDTO("prof@mail.com", "wrongpass"));

        assertTrue(result.isEmpty());
    }

    @Test
    void loginReturnsTokenWhenCredentialsAreValid() {
        Professional prof = new Professional("prof@mail.com", "hashedpass", "Juan", "Perez");
        when(professionalRepository.findByEmail("prof@mail.com")).thenReturn(Optional.of(prof));
        when(passwordEncoder.matches("correctpass", "hashedpass")).thenReturn(true);

        Optional<TokenDTO> result = professionalService.login(new UserLoginDTO("prof@mail.com", "correctpass"));

        assertTrue(result.isPresent());
        assertEquals("access-token", result.get().accessToken());
        assertEquals("refresh-token", result.get().refreshToken());
    }

    @Test
    void updateProfileReturnsFalseWhenProfessionalNotFound() {
        when(professionalRepository.findById(99L)).thenReturn(Optional.empty());

        ProfessionalProfileDTO dto = new ProfessionalProfileDTO("Estilista", "Av. Corrientes 123", "Microcentro",
                "Buenos Aires");
        boolean result = professionalService.updateProfile(99L, dto);

        assertFalse(result);
    }

    @Test
    void updateProfileReturnsTrueAndUpdatesFields() {
        Professional prof = new Professional("prof@mail.com", "hash", "Juan", "Perez");
        when(professionalRepository.findById(1L)).thenReturn(Optional.of(prof));
        when(professionalRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ProfessionalProfileDTO dto = new ProfessionalProfileDTO("Barbero", "Calle Falsa 123", "Palermo",
                "Buenos Aires");
        boolean result = professionalService.updateProfile(1L, dto);

        assertTrue(result);
        assertEquals("Barbero", prof.getSpecialty());
        assertEquals("Calle Falsa 123", prof.getAddress());
        assertEquals("Palermo", prof.getNeighborhood());
        assertEquals("Buenos Aires", prof.getCity());
        verify(professionalRepository).save(prof);
    }

    @Test
    void updateProfilePersistsAllFields() {
        Professional prof = new Professional("prof@mail.com", "hash", "Ana", "Garcia");
        when(professionalRepository.findById(2L)).thenReturn(Optional.of(prof));
        when(professionalRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ProfessionalProfileDTO dto = new ProfessionalProfileDTO("Colorista", "Rivadavia 500", "Caballito", "CABA");
        professionalService.updateProfile(2L, dto);

        assertEquals("Colorista", prof.getSpecialty());
        assertEquals("Rivadavia 500", prof.getAddress());
        assertEquals("Caballito", prof.getNeighborhood());
        assertEquals("CABA", prof.getCity());
    }
}