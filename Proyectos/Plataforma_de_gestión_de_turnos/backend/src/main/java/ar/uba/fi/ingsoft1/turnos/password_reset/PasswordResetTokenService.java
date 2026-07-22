package ar.uba.fi.ingsoft1.turnos.password_reset;

import ar.uba.fi.ingsoft1.turnos.client.ClientRepository;
import ar.uba.fi.ingsoft1.turnos.professional.ProfessionalRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@Transactional
public class PasswordResetTokenService {

    private final long expirationMinutes;
    private final PasswordResetTokenRepository tokenRepository;
    private final ClientRepository clientRepository;
    private final ProfessionalRepository professionalRepository;
    private final PasswordEncoder passwordEncoder;

    public PasswordResetTokenService(
            @Value("${application.security.resetToken.expiration-minutes:30}") long expirationMinutes,
            PasswordResetTokenRepository tokenRepository,
            ClientRepository clientRepository,
            ProfessionalRepository professionalRepository,
            PasswordEncoder passwordEncoder) {
        this.expirationMinutes = expirationMinutes;
        this.tokenRepository = tokenRepository;
        this.clientRepository = clientRepository;
        this.professionalRepository = professionalRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<String> createFor(String email) {
        var prof = professionalRepository.findByEmail(email);
        if (prof.isPresent()) {
            return Optional.of(generateAndSave(prof.get().getId(), "PROFESSIONAL"));
        }

        var client = clientRepository.findByEmail(email);
        if (client.isPresent()) {
            return Optional.of(generateAndSave(client.get().getId(), "CLIENT"));
        }

        return Optional.empty();
    }

    public boolean resetPassword(String rawToken, String newPassword) {
        String hash = sha256(rawToken);

        return tokenRepository.findByTokenHash(hash)
                .filter(PasswordResetToken::isValid)
                .map(token -> {
                    String encoded = passwordEncoder.encode(newPassword);

                    if ("PROFESSIONAL".equals(token.getUserType())) {
                        professionalRepository.findById(token.getUserId()).ifPresent(p -> p.setPassword(encoded));
                    } else {
                        clientRepository.findById(token.getUserId()).ifPresent(c -> c.setPassword(encoded));
                    }

                    markAsUsed(token);
                    return true;
                })
                .orElse(false);
    }

    private String generateAndSave(Long userId, String userType) {
        tokenRepository.deleteByUserIdAndUserType(userId, userType);

        String rawToken = generateRawToken();
        Instant expiresAt = Instant.now().plus(expirationMinutes, ChronoUnit.MINUTES);
        tokenRepository.save(
                new PasswordResetToken(null, sha256(rawToken), userId, userType, expiresAt, false, Instant.now()));
        return rawToken;
    }

    private void markAsUsed(PasswordResetToken token) {
        token.setUsed(true);
        tokenRepository.save(token);
    }

    String generateRawToken() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return new BigInteger(1, bytes).toString(32);
    }

    String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return new BigInteger(1, hash).toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 no disponible", e);
        }
    }
}