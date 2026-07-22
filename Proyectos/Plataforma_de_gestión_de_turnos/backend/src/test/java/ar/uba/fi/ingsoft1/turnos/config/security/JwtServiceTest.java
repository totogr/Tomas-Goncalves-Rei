package ar.uba.fi.ingsoft1.turnos.config.security;

import ar.uba.fi.ingsoft1.turnos.user.UserRole;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private static final String SECRET =
            "0000000000000000000000000000000000000000000000000000000000000000";
    private static final String OTHER_SECRET =
            "1111111111111111111111111111111111111111111111111111111111111111";
    private static final long ONE_HOUR = 3_600_000L;

    private JwtService jwtService(long expiration) {
        return new JwtService(SECRET, expiration);
    }

    private SecretKey keyFrom(String secret) {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    @Test
    void createAndExtractRoundTrip() {
        JwtService service = jwtService(ONE_HOUR);
        String token = service.createToken(new JwtUserDetails("user@mail.com", UserRole.CLIENT));

        Optional<JwtUserDetails> result = service.extractVerifiedUserDetails(token);

        assertTrue(result.isPresent());
        assertEquals("user@mail.com", result.get().username());
        assertEquals(UserRole.CLIENT, result.get().role());
    }

    @Test
    void expiredTokenThrowsUnauthorized() {
        JwtService service = jwtService(-ONE_HOUR); // expiración en el pasado
        String token = service.createToken(new JwtUserDetails("user@mail.com", UserRole.PROFESSIONAL));

        assertThrows(ResponseStatusException.class,
                () -> service.extractVerifiedUserDetails(token));
    }

    @Test
    void tamperedSignatureThrowsUnauthorized() {
        // token firmado con OTRA clave: la verificación con la clave real debe fallar
        String foreignToken = Jwts.builder()
                .subject("user@mail.com")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ONE_HOUR))
                .claim("role", "CLIENT")
                .signWith(keyFrom(OTHER_SECRET), Jwts.SIG.HS256)
                .compact();

        assertThrows(ResponseStatusException.class,
                () -> jwtService(ONE_HOUR).extractVerifiedUserDetails(foreignToken));
    }

    @Test
    void corruptRoleThrowsUnauthorized() {
        // firmado con la clave correcta pero con un rol inexistente en el enum
        String token = Jwts.builder()
                .subject("user@mail.com")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ONE_HOUR))
                .claim("role", "ADMIN")
                .signWith(keyFrom(SECRET), Jwts.SIG.HS256)
                .compact();

        assertThrows(ResponseStatusException.class,
                () -> jwtService(ONE_HOUR).extractVerifiedUserDetails(token));
    }

    @Test
    void tokenWithoutRoleClaimReturnsEmpty() {
        String token = Jwts.builder()
                .subject("user@mail.com")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ONE_HOUR))
                .signWith(keyFrom(SECRET), Jwts.SIG.HS256)
                .compact();

        assertTrue(jwtService(ONE_HOUR).extractVerifiedUserDetails(token).isEmpty());
    }
}
