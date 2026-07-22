package ar.uba.fi.ingsoft1.turnos.password_reset;

import ar.uba.fi.ingsoft1.turnos.client.Client;
import ar.uba.fi.ingsoft1.turnos.client.ClientRepository;
import ar.uba.fi.ingsoft1.turnos.professional.Professional;
import ar.uba.fi.ingsoft1.turnos.professional.ProfessionalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PasswordResetTokenServiceTest {

    private PasswordResetTokenRepository tokenRepository;
    private ClientRepository clientRepository;
    private ProfessionalRepository professionalRepository;
    private PasswordEncoder passwordEncoder;

    private PasswordResetTokenService service;

    @BeforeEach
    void setUp() {
        tokenRepository = mock(PasswordResetTokenRepository.class);
        clientRepository = mock(ClientRepository.class);
        professionalRepository = mock(ProfessionalRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);

        service = new PasswordResetTokenService(
                30, tokenRepository, clientRepository, professionalRepository, passwordEncoder);
    }

    private Professional professional(Long id) {
        Professional p = new Professional("pro@mail.com", "old", "Pro", "Fesional");
        p.setId(id);
        return p;
    }

    private Client client(Long id) {
        Client c = new Client("client@mail.com", "old", "Cli", "Ente");
        c.setId(id);
        return c;
    }

    // ── createFor ─────────────────────────────────────────────────────────────

    @Test
    void createForReturnsTokenForProfessional() {
        when(professionalRepository.findByEmail("pro@mail.com")).thenReturn(Optional.of(professional(1L)));

        Optional<String> result = service.createFor("pro@mail.com");

        assertTrue(result.isPresent());
        verify(tokenRepository).deleteByUserIdAndUserType(1L, "PROFESSIONAL");
        verify(tokenRepository).save(any(PasswordResetToken.class));
    }

    @Test
    void createForReturnsTokenForClientWhenNotProfessional() {
        when(professionalRepository.findByEmail("client@mail.com")).thenReturn(Optional.empty());
        when(clientRepository.findByEmail("client@mail.com")).thenReturn(Optional.of(client(2L)));

        Optional<String> result = service.createFor("client@mail.com");

        assertTrue(result.isPresent());
        verify(tokenRepository).deleteByUserIdAndUserType(2L, "CLIENT");
        verify(tokenRepository).save(any(PasswordResetToken.class));
    }

    @Test
    void createForReturnsEmptyWhenUserNotFound() {
        when(professionalRepository.findByEmail("ghost@mail.com")).thenReturn(Optional.empty());
        when(clientRepository.findByEmail("ghost@mail.com")).thenReturn(Optional.empty());

        assertTrue(service.createFor("ghost@mail.com").isEmpty());
        verify(tokenRepository, never()).save(any());
    }

    // ── resetPassword ─────────────────────────────────────────────────────────

    private PasswordResetToken token(String rawToken, String userType, Long userId,
                                     boolean used, Instant expiresAt) {
        return new PasswordResetToken(
                1L, service.sha256(rawToken), userId, userType, expiresAt, used, Instant.now());
    }

    @Test
    void resetPasswordSucceedsForClient() {
        PasswordResetToken token = token("raw-token", "CLIENT", 2L, false, Instant.now().plusSeconds(600));
        Client client = client(2L);
        when(tokenRepository.findByTokenHash(service.sha256("raw-token"))).thenReturn(Optional.of(token));
        when(clientRepository.findById(2L)).thenReturn(Optional.of(client));
        when(passwordEncoder.encode("new-pass")).thenReturn("ENCODED");

        boolean result = service.resetPassword("raw-token", "new-pass");

        assertTrue(result);
        assertEquals("ENCODED", client.getPassword());
        assertTrue(token.isUsed());
        verify(tokenRepository).save(token);
    }

    @Test
    void resetPasswordSucceedsForProfessional() {
        PasswordResetToken token = token("raw-token", "PROFESSIONAL", 1L, false, Instant.now().plusSeconds(600));
        Professional pro = professional(1L);
        when(tokenRepository.findByTokenHash(service.sha256("raw-token"))).thenReturn(Optional.of(token));
        when(professionalRepository.findById(1L)).thenReturn(Optional.of(pro));
        when(passwordEncoder.encode("new-pass")).thenReturn("ENCODED");

        assertTrue(service.resetPassword("raw-token", "new-pass"));
        assertEquals("ENCODED", pro.getPassword());
    }

    @Test
    void resetPasswordFailsWhenTokenNotFound() {
        when(tokenRepository.findByTokenHash(any())).thenReturn(Optional.empty());

        assertFalse(service.resetPassword("unknown", "new-pass"));
        verify(passwordEncoder, never()).encode(any());
    }

    @Test
    void resetPasswordFailsWhenTokenAlreadyUsed() {
        PasswordResetToken used = token("raw-token", "CLIENT", 2L, true, Instant.now().plusSeconds(600));
        when(tokenRepository.findByTokenHash(service.sha256("raw-token"))).thenReturn(Optional.of(used));

        assertFalse(service.resetPassword("raw-token", "new-pass"));
        verify(passwordEncoder, never()).encode(any());
    }

    @Test
    void resetPasswordFailsWhenTokenExpired() {
        PasswordResetToken expired = token("raw-token", "CLIENT", 2L, false, Instant.now().minusSeconds(1));
        when(tokenRepository.findByTokenHash(service.sha256("raw-token"))).thenReturn(Optional.of(expired));

        assertFalse(service.resetPassword("raw-token", "new-pass"));
        verify(passwordEncoder, never()).encode(any());
    }
}
