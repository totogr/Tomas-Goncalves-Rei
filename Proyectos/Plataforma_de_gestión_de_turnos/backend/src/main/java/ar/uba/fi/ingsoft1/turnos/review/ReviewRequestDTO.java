package ar.uba.fi.ingsoft1.turnos.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ReviewRequestDTO(
        @NotNull @Min(1) @Max(5) Integer score) {
}