package ar.uba.fi.ingsoft1.turnos.appointment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;

public record WaitListEntryDTO(
        Long id,
        @JsonProperty("professional_id")   Long professionalId,
        @JsonProperty("professional_name") String professionalName,
        @JsonProperty("service_id")        Long serviceId,
        @JsonProperty("service_name")      String serviceName,
        @JsonProperty("slot_start")        ZonedDateTime slotStart,
        @JsonProperty("creation_time")     ZonedDateTime creationTime,
        int position
) {}