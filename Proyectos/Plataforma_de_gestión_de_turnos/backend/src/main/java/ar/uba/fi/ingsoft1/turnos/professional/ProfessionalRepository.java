package ar.uba.fi.ingsoft1.turnos.professional;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProfessionalRepository extends JpaRepository<Professional, Long> {
    Optional<Professional> findByEmail(String email);
}
