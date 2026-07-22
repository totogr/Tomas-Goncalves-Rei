package ar.uba.fi.ingsoft1.turnos.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record UserLoginDTO(
                @NotBlank @JsonProperty("email") String username,
                @NotBlank String password) implements UserCredentials {
}
