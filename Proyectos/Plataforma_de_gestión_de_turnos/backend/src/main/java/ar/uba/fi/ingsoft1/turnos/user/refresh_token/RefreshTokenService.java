package ar.uba.fi.ingsoft1.turnos.user.refresh_token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@Transactional
public class RefreshTokenService {

    private final Long expirationDays;
    private final Integer byteSize;
    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public RefreshTokenService(
            @Value("${application.security.refreshToken.expiration-days:7}") Long expirationDays,
            @Value("${jwt.refresh.bytes:32}") Integer byteSize,
            RefreshTokenRepository refreshTokenRepository) {
        this.expirationDays = expirationDays;
        this.byteSize = byteSize;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshToken createFor(Long userId, String userType) {
        refreshTokenRepository.deleteByUserIdAndUserType(userId, userType);
        String value = getRandomString();
        RefreshToken result = new RefreshToken(value, userId, userType, getExpirationFor(Instant.now()));
        refreshTokenRepository.save(result);
        return result;
    }

    public Optional<RefreshToken> verify(String tokenValue) {
        return refreshTokenRepository.findById(tokenValue)
                .filter(RefreshToken::isValid);
    }

    public Optional<RefreshToken> findByValue(String value) {
        Optional<RefreshToken> result = refreshTokenRepository.findById(value);
        result.ifPresent(refreshTokenRepository::delete);
        return result.filter(RefreshToken::isValid);
    }

    String getRandomString() {
        SecureRandom random = new SecureRandom();
        byte[] randomBytes = new byte[this.byteSize];
        random.nextBytes(randomBytes);
        return new BigInteger(1, randomBytes).toString(32);
    }

    Instant getExpirationFor(Instant reference) {
        return reference.plus(expirationDays, ChronoUnit.DAYS);
    }
}
