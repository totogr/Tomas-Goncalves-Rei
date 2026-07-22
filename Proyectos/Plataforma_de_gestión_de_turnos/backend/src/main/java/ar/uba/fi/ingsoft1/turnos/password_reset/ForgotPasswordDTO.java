package ar.uba.fi.ingsoft1.turnos.password_reset;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordDTO(
                @NotBlank @Email String email) {
}