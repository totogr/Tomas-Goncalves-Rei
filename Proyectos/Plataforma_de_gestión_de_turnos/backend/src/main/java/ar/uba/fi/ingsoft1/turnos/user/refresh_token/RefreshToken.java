package ar.uba.fi.ingsoft1.turnos.user.refresh_token;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
    @Id
    @Column(name = "token_value")
    private String value;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_type")
    private String userType;

    @Column(nullable = false)
    private Instant expiresAt;

    public String value() {
        return this.value;
    }

    public Long userId() {
        return this.userId;
    }

    public String userType() {
        return this.userType;
    }

    public boolean isValid() {
        return expiresAt.isAfter(Instant.now());
    }
}
