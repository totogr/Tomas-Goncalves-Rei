package ar.uba.fi.ingsoft1.turnos.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

record ServiceCreateDTO(
        @NotBlank(message = "El nombre es obligatorio") String name,

        @NotNull(message = "La duración es obligatoria") @Positive(message = "La duración debe ser mayor a 0") @JsonProperty("duration_minutes") Integer duration,

        @NotNull(message = "El precio es obligatorio") @Positive(message = "El precio debe ser mayor a 0") BigDecimal price,

        @NotNull(message = "La capacidad máxima es obligatoria") @Positive(message = "La capacidad máxima debe ser mayor a 0") @JsonProperty("max_capacity") Integer maxCapacity) {
}