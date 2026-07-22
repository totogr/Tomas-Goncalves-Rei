package ar.uba.fi.ingsoft1.turnos.professional;

import jakarta.validation.constraints.NotBlank;

record ProfessionalProfileDTO(
                @NotBlank String specialty,
                @NotBlank String address,
                @NotBlank String neighborhood,
                @NotBlank String city) {
}