package ar.uba.fi.ingsoft1.turnos.client;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClientCreateDTO(
                @NotBlank @Email(message = "Email inválido") String email,
                @NotBlank @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres") String password,
                @NotBlank String firstName,
                @NotBlank String lastName) {
}
