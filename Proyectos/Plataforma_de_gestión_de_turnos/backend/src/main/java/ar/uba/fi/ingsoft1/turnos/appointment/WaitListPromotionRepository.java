package ar.uba.fi.ingsoft1.turnos.appointment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface WaitListPromotionRepository extends JpaRepository<WaitListPromotion, Long> {

    Optional<WaitListPromotion> findByClientIdAndProfessionalIdAndServiceIdAndSlotStartAndConfirmedFalseAndExpiredFalse(
            Long clientId, Long professionalId, Long serviceId, ZonedDateTime slotStart);

    List<WaitListPromotion> findByExpiredFalseAndConfirmedFalseAndExpiresAtBefore(ZonedDateTime now);

    void deleteByClientIdAndProfessionalIdAndServiceIdAndSlotStart(
            Long clientId, Long professionalId, Long serviceId, ZonedDateTime slotStart);

    void deleteByClientIdAndProfessionalIdAndServiceIdAndSlotStartAndConfirmedAndExpiredAndIdNot(
            Long clientId, Long professionalId, Long serviceId, ZonedDateTime slotStart,
            boolean confirmed, boolean expired, Long id);

    List<WaitListPromotion> findByProfessionalIdAndConfirmedFalseAndExpiredFalse(Long professionalId);

    @Query("SELECT p FROM WaitListPromotion p " +
            "WHERE p.professionalId = :professionalId " +
            "AND p.serviceId = :serviceId " +
            "AND p.slotStart >= :startOfDay AND p.slotStart < :endOfDay " +
            "AND p.confirmed = false AND p.expired = false")
    List<WaitListPromotion> findActivePromotionsForDay(
            @Param("professionalId") Long professionalId,
            @Param("serviceId") Long serviceId,
            @Param("startOfDay") ZonedDateTime startOfDay,
            @Param("endOfDay") ZonedDateTime endOfDay);
}