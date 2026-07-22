package ar.uba.fi.ingsoft1.turnos.professional;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record ProfessionalCreateDTO(
                @NotBlank(message = "Email inválido") @Email(message = "Email inválido") String email,
                @NotBlank @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres") String password,
                @NotBlank String firstName,
                @NotBlank String lastName) {
}
