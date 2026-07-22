package ar.uba.fi.ingsoft1.turnos.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ScheduleBlockRepository extends JpaRepository<ScheduleBlock, Long> {

    List<ScheduleBlock> findByProfessionalIdOrderByBlockDateAscStartTimeAsc(Long professionalId);

    List<ScheduleBlock> findByProfessionalIdAndBlockDateOrderByStartTimeAsc(Long professionalId, LocalDate blockDate);

    @Query("SELECT b FROM ScheduleBlock b WHERE b.professionalId = :professionalId AND b.blockDate = :blockDate "
            + "AND b.startTime < :endTime AND b.endTime > :startTime")
    List<ScheduleBlock> findOverlappingBlocks(@Param("professionalId") Long professionalId,
            @Param("blockDate") LocalDate blockDate,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);
}