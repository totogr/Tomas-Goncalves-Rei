package ar.uba.fi.ingsoft1.turnos.professional;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProfessionalSummaryDTO(
        Long id,
        @JsonProperty("first_name") String firstName,
        @JsonProperty("last_name") String lastName,
        String specialty,
        Double rating) {
    public static ProfessionalSummaryDTO from(Professional p, Double rating) {
        return new ProfessionalSummaryDTO(
                p.getId(),
                p.getFirstName(),
                p.getLastName(),
                p.getSpecialty(),
                rating);
    }
}
