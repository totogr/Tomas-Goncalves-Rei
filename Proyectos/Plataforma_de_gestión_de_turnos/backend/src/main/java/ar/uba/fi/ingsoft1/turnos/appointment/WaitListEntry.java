package ar.uba.fi.ingsoft1.turnos.appointment;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.ZonedDateTime;
@Entity
@Table(
        name = "wait_list",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {
                        "client_id",
                        "professional_id",
                        "service_id",
                        "slot_start"
                }
        )
)
@Getter
@Setter
@NoArgsConstructor
public class WaitListEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "client_id", nullable = false)
    private Long clientId;
    @Column(name = "professional_id", nullable = false)
    private Long professionalId;
    @Column(name = "service_id", nullable = false)
    private Long serviceId;
    @Column(name = "slot_start", nullable = false)
    private ZonedDateTime slotStart;
    @Column(name = "creation_time", nullable = false)
    private ZonedDateTime creationTime;
}