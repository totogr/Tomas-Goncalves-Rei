package ar.uba.fi.ingsoft1.turnos.user;

import jakarta.validation.constraints.NotNull;

public record TokenDTO(
        @NotNull String accessToken,
        String refreshToken,
        UserRole role,
        Long id,
        String firstName,
        String lastName) {
}
