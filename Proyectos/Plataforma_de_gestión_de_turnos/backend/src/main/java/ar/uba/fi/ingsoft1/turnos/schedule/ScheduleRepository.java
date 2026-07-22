package ar.uba.fi.ingsoft1.turnos.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByProfessionalId(Long professionalId);

    List<Schedule> findByProfessionalIdAndDayWeek(Long professionalId, Integer dayWeek);

    void deleteByProfessionalId(Long professionalId);
}