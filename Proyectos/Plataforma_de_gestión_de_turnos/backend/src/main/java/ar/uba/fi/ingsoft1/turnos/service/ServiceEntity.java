package ar.uba.fi.ingsoft1.turnos.service;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "service")
@Getter
@Setter
@NoArgsConstructor
public class ServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "professional_id", nullable = false)
    private Long professionalId;

    @Column
    private String category;

    @Column
    private String slug;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @JsonProperty("duration_minutes")
    private Integer duration;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "max_capacity")
    @JsonProperty("max_capacity")
    private Integer maxCapacity;

    @Column(name = "is_active", nullable = false)
    @JsonProperty("active")
    private boolean active;
}