package ar.uba.fi.ingsoft1.turnos.schedule;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalTime;

@Entity
@Data
@Table(name = "schedule")
@Getter
@Setter
@NoArgsConstructor
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "professional_id", nullable = false)
    private Long professionalId;

    @Column(name = "day_week", nullable = false)
    private Integer dayWeek;

    @Column(nullable = false)
    private LocalTime start;

    @Column(name = "\"end\"", nullable = false)
    private LocalTime end;
}