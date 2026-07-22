package ar.uba.fi.ingsoft1.turnos.professional;

import com.fasterxml.jackson.annotation.JsonProperty;

import ar.uba.fi.ingsoft1.turnos.service.ServiceEntity;

import java.math.BigDecimal;
import java.util.List;

public record ProfessionalDTO(
        Long id,
        @JsonProperty("first_name") String firstName,
        @JsonProperty("last_name") String lastName,
        String specialty,
        LocationDTO location,
        Double rating,
        @JsonProperty("review_count") Integer reviewCount,
        List<ServiceDTO> services) {
    public record LocationDTO(String neighborhood, String city, String address) {
    }

    public record ServiceDTO(
            Long id,
            String name,
            @JsonProperty("duration_minutes") Integer durationMinutes,
            BigDecimal price,
            @JsonProperty("max_capacity") Integer maxCapacity,
            Boolean active) {
        public static ServiceDTO from(ServiceEntity s) {
            return new ServiceDTO(s.getId(), s.getName(), s.getDuration(),
                    s.getPrice(), s.getMaxCapacity(), s.isActive());
        }
    }

    public static ProfessionalDTO from(Professional p, List<ServiceEntity> services,
            Double rating, Integer reviewCount) {
        return new ProfessionalDTO(
                p.getId(),
                p.getFirstName(),
                p.getLastName(),
                p.getSpecialty(),
                new LocationDTO(p.getNeighborhood(), p.getCity(), p.getAddress()),
                rating,
                reviewCount,
                services.stream().map(ServiceDTO::from).toList());
    }
}
