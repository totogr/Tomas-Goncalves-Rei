package ar.uba.fi.ingsoft1.turnos.appointment;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.ZonedDateTime;
@Entity
@Table(name = "wait_list_promotions")
@Getter
@Setter

public class WaitListPromotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long waitListEntryId;
    private Long clientId;
    private Long professionalId;
    private Long serviceId;
    private ZonedDateTime slotStart;
    private ZonedDateTime expiresAt;
    private boolean confirmed = false;
    private boolean expired = false;

}