package ar.uba.fi.ingsoft1.turnos.blockedclient;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Entity
@Table(name = "blocked_client")
@Getter
@NoArgsConstructor
public class BlockedClient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "professional_id", nullable = false)
    private Long professionalId;

    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @Column(name = "blocked_at", nullable = false)
    private ZonedDateTime blockedAt;

    public BlockedClient(Long professionalId, Long clientId) {
        this.professionalId = professionalId;
        this.clientId = clientId;
        this.blockedAt = ZonedDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"));
    }
}