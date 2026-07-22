package ar.uba.fi.ingsoft1.turnos.schedule;

import ar.uba.fi.ingsoft1.turnos.appointment.Appointment;
import ar.uba.fi.ingsoft1.turnos.appointment.AppointmentRepository;
import ar.uba.fi.ingsoft1.turnos.common.exception.BadRequestException;
import ar.uba.fi.ingsoft1.turnos.common.exception.ConflictException;
import ar.uba.fi.ingsoft1.turnos.common.exception.ForbiddenException;
import ar.uba.fi.ingsoft1.turnos.common.exception.ItemNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@Transactional
public class ScheduleBlockService {

    private static final ZoneId ZONE = ZoneId.of("America/Argentina/Buenos_Aires");

    private final ScheduleBlockRepository scheduleBlockRepository;
    private final AppointmentRepository appointmentRepository;

    public ScheduleBlockService(ScheduleBlockRepository scheduleBlockRepository,
            AppointmentRepository appointmentRepository) {
        this.scheduleBlockRepository = scheduleBlockRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Transactional(readOnly = true)
    public List<ScheduleBlockDTO> getBlocks(Long professionalId) {
        return scheduleBlockRepository.findByProfessionalIdOrderByBlockDateAscStartTimeAsc(professionalId).stream()
                .map(this::toDTO)
                .toList();
    }

    public ScheduleBlockCreateResultDTO createBlock(Long professionalId, ScheduleBlockRequestDTO request) {
        validateRange(request.startTime(), request.endTime());

        List<ScheduleBlock> overlappingBlocks = scheduleBlockRepository.findOverlappingBlocks(
                professionalId, request.blockDate(), request.startTime(), request.endTime());
        if (!overlappingBlocks.isEmpty()) {
            throw new ConflictException("Ya existe un bloqueo que se superpone con ese horario");
        }

        ScheduleBlock block = new ScheduleBlock();
        block.setProfessionalId(professionalId);
        block.setBlockDate(request.blockDate());
        block.setStartTime(request.startTime());
        block.setEndTime(request.endTime());

        ScheduleBlock savedBlock = scheduleBlockRepository.save(block);
        int cancelledAppointments = cancelAppointmentsCoveredByBlock(professionalId, savedBlock);

        return new ScheduleBlockCreateResultDTO(toDTO(savedBlock), cancelledAppointments);
    }

    public void deleteBlock(Long professionalId, Long blockId) {
        ScheduleBlock block = scheduleBlockRepository.findById(blockId)
                .orElseThrow(() -> new ItemNotFoundException("Bloqueo de horario", blockId));

        if (!block.getProfessionalId().equals(professionalId)) {
            throw new ForbiddenException("No podés eliminar el bloqueo de otro profesional");
        }

        scheduleBlockRepository.delete(block);
    }

    private void validateRange(LocalTime startTime, LocalTime endTime) {
        if (!startTime.isBefore(endTime)) {
            throw new BadRequestException("La hora de fin debe ser posterior a la hora de inicio");
        }
    }

    private ScheduleBlockDTO toDTO(ScheduleBlock block) {
        return new ScheduleBlockDTO(block.getId(), block.getBlockDate(), block.getStartTime(), block.getEndTime());
    }

    private int cancelAppointmentsCoveredByBlock(Long professionalId, ScheduleBlock block) {
        ZonedDateTime from = block.getBlockDate().atStartOfDay(ZONE);
        ZonedDateTime to = block.getBlockDate().plusDays(1).atStartOfDay(ZONE);
        ZonedDateTime now = ZonedDateTime.now(ZONE);

        LocalDateTime blockStart = block.getBlockDate().atTime(block.getStartTime());
        LocalDateTime blockEnd = block.getBlockDate().atTime(block.getEndTime());

        List<Appointment> dayAppointments = appointmentRepository.findActiveByProfessionalIdAndRange(professionalId, from, to);

        int cancelledCount = 0;
        for (Appointment appointment : dayAppointments) {
            if (!"CONFIRMED".equalsIgnoreCase(appointment.getStatus())) {
                continue;
            }

            if (!appointment.getStart().isAfter(now)) {
                continue;
            }

            LocalDateTime appointmentStart = appointment.getStart().withZoneSameInstant(ZONE).toLocalDateTime();
            LocalDateTime appointmentEnd = appointment.getEnd().withZoneSameInstant(ZONE).toLocalDateTime();

            boolean overlaps = appointmentStart.isBefore(blockEnd) && appointmentEnd.isAfter(blockStart);
            if (!overlaps) {
                continue;
            }

            appointment.setStatus("CANCELLED");
            appointment.setCancelledBy("professional");
            appointment.setCancelledDate(now);
            cancelledCount++;
        }

        return cancelledCount;
    }
}