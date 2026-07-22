package ar.uba.fi.ingsoft1.turnos.client;

public record ClientSummaryDTO(
        Long id,
        String firstName,
        String lastName,
        String email) {
}