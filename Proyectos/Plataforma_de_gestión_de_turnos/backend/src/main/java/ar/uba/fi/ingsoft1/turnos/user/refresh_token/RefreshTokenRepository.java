package ar.uba.fi.ingsoft1.turnos.user.refresh_token;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    void deleteByUserIdAndUserType(Long userId, String userType);
}
