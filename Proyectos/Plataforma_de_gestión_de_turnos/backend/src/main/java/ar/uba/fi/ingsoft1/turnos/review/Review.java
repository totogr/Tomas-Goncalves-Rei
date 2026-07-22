package ar.uba.fi.ingsoft1.turnos.review;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "review")
@Getter
@Setter
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "professional_id", nullable = false)
    private Long professionalId;

    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @Column(name = "appointment_id", nullable = false)
    private Long appointmentId;

    @Column(nullable = false)
    private Integer score;
}