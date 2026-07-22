package ar.uba.fi.ingsoft1.turnos.appointment;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface WaitListRepository extends JpaRepository<WaitListEntry, Long> {
    boolean existsByClientIdAndProfessionalIdAndServiceIdAndSlotStart(
            Long clientId, Long professionalId, Long serviceId, ZonedDateTime slotStart);

    List<WaitListEntry> findByProfessionalIdAndServiceIdAndSlotStartOrderByCreationTimeAsc(
            Long professionalId, Long serviceId, ZonedDateTime slotStart);

    Optional<WaitListEntry> findFirstByProfessionalIdAndServiceIdAndSlotStartOrderByCreationTimeAsc(
            Long professionalId, Long serviceId, ZonedDateTime slotStart);

    List<WaitListEntry> findByClientId(Long clientId);

    List<WaitListEntry> findByProfessionalIdAndServiceIdAndSlotStartBetween(
            Long professionalId, Long serviceId, ZonedDateTime from, ZonedDateTime to);
}