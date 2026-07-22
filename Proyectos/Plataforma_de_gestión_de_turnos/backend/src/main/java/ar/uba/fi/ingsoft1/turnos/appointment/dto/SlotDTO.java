package ar.uba.fi.ingsoft1.turnos.appointment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SlotDTO(
                @JsonFormat(pattern = "HH:mm") LocalTime time,
                boolean available,
                @JsonProperty("employee_id") Long employeeId,
                String reason) {
}