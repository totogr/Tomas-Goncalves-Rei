package ar.uba.fi.ingsoft1.turnos.appointment;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.ZonedDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("SELECT a FROM Appointment a WHERE a.professionalId = :profId AND a.start >= :startOfDay AND a.start < :endOfDay AND a.status != 'CANCELLED'")
    List<Appointment> findActiveAppointments(Long profId, ZonedDateTime startOfDay, ZonedDateTime endOfDay);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Appointment a WHERE a.professionalId = :profId AND a.serviceId = :serviceId AND a.start >= :startOfDay AND a.start < :endOfDay AND a.status != 'CANCELLED'")
    List<Appointment> findActiveAppointmentsForUpdate(
            @Param("profId") Long profId,
            @Param("serviceId") Long serviceId,
            @Param("startOfDay") ZonedDateTime startOfDay,
            @Param("endOfDay") ZonedDateTime endOfDay);

    List<Appointment> findByClientId(Long clientId);

    @Query("SELECT a FROM Appointment a WHERE a.professionalId = :profId ORDER BY a.start DESC")
    List<Appointment> findByProfessionalId(Long profId);

    List<Appointment> findByProfessionalIdAndClientId(Long profId, Long clientId);

    @Query("SELECT a FROM Appointment a WHERE a.professionalId = :profId AND a.start >= :from AND a.start < :to AND a.status != 'CANCELLED' ORDER BY a.start ASC")
    List<Appointment> findActiveByProfessionalIdAndRange(Long profId, ZonedDateTime from, ZonedDateTime to);

    @Query("SELECT a FROM Appointment a WHERE a.professionalId = :profId AND ((a.status != 'CANCELLED' AND a.start >= :from AND a.start < :to) OR (a.status = 'CANCELLED' AND a.cancelledDate IS NOT NULL AND a.cancelledDate >= :from AND a.cancelledDate < :to)) ORDER BY a.start ASC")
    List<Appointment> findAllByProfessionalIdAndRange(Long profId, ZonedDateTime from, ZonedDateTime to);

    List<Appointment> findByStatusAndEndBefore(String status, ZonedDateTime end);

    @Query("SELECT a FROM Appointment a WHERE a.status = 'CONFIRMED' AND (a.reminderSent IS NULL OR a.reminderSent = false) AND a.start BETWEEN :now AND :tomorrow")
    List<Appointment> findAppointmentsForReminder(
            @org.springframework.data.repository.query.Param("now") java.time.ZonedDateTime now,
            @org.springframework.data.repository.query.Param("tomorrow") java.time.ZonedDateTime tomorrow
    );

    boolean existsByServiceId(Long serviceId);

    boolean existsByClientIdAndProfessionalIdAndServiceIdAndStartAndStatus(
            Long clientId, Long professionalId, Long serviceId, ZonedDateTime start, String status);

    @Query("SELECT a FROM Appointment a WHERE a.clientId = :clientId AND a.start >= :startOfDay AND a.start < :endOfDay AND a.status = 'CONFIRMED'")
    List<Appointment> findConfirmedByClientIdAndDay(
            @Param("clientId") Long clientId,
            @Param("startOfDay") ZonedDateTime startOfDay,
            @Param("endOfDay") ZonedDateTime endOfDay);

}