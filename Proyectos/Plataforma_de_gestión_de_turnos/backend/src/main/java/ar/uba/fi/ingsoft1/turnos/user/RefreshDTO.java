package ar.uba.fi.ingsoft1.turnos.user;

import jakarta.validation.constraints.NotBlank;

public record RefreshDTO(
                @NotBlank String refreshToken) {
}
