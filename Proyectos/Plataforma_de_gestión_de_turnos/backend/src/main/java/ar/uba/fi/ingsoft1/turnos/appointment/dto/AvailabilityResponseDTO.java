package ar.uba.fi.ingsoft1.turnos.appointment.dto;

import java.time.LocalDate;
import java.util.List;

public record AvailabilityResponseDTO(
                LocalDate date,
                List<SlotDTO> slots) {
}