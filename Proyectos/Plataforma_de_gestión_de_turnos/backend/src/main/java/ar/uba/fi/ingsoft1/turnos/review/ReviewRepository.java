package ar.uba.fi.ingsoft1.turnos.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByAppointmentId(Long appointmentId);

    Optional<Review> findByAppointmentId(Long appointmentId);

    @Query("SELECT AVG(r.score) FROM Review r WHERE r.professionalId = :professionalId")
    Optional<Double> findAverageScoreByProfessionalId(Long professionalId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.professionalId = :professionalId")
    Integer countByProfessionalId(Long professionalId);

    @Query("SELECT r.professionalId, AVG(r.score) FROM Review r GROUP BY r.professionalId")
    List<Object[]> findAllAverageScores();
}