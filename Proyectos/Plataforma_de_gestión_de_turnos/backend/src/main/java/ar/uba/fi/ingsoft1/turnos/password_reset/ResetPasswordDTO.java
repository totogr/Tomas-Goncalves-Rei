package ar.uba.fi.ingsoft1.turnos.password_reset;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordDTO(
                @NotBlank String token,
                @NotBlank @Size(min = 8) String newPassword) {
}