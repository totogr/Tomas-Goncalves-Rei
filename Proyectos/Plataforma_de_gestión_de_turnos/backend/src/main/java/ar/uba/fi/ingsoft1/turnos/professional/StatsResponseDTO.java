package ar.uba.fi.ingsoft1.turnos.professional;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record StatsResponseDTO(
        @JsonProperty("total_appointments") int totalAppointments,
        @JsonProperty("cancellation_rate") double cancellationRate,
        @JsonProperty("average_rating") Double averageRating,
        @JsonProperty("estimated_revenue") double estimatedRevenue,
        @JsonProperty("appointments_by_day") List<DayStatDTO> appointmentsByDay,
        @JsonProperty("top_services") List<ServiceStatDTO> topServices,
        @JsonProperty("frequent_clients") List<ClientStatDTO> frequentClients) {

    public record DayStatDTO(String day, int count) {
    }

    public record ServiceStatDTO(String name, int count, int percentage) {
    }

    public record ClientStatDTO(String name, int visits, int cancellations, String status) {
    }
}