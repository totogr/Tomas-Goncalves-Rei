package ar.uba.fi.ingsoft1.turnos.appointment;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.ZonedDateTime;

@Entity
@Table(name = "appointment")
@Getter
@Setter
@NoArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "professional_id", nullable = false)
    private Long professionalId;

    @Column(name = "employee_id")
    private Long employeeId;

    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "service_id", nullable = false)
    private Long serviceId;

    @Column(name = "start_time", nullable = false)
    private ZonedDateTime start;

    @Column(name = "end_time", nullable = false)
    private ZonedDateTime end;

    @Column(length = 50)
    private String status;

    @Column(name = "cancelled_by", length = 20)
    private String cancelledBy;
    @Column(name = "cancelled_date")
    private ZonedDateTime cancelledDate;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "marked_absent_at")
    private ZonedDateTime markedAbsentAt;

    @Column(name = "reminder_sent")
    private Boolean reminderSent;

}