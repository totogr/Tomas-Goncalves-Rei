package ar.uba.fi.ingsoft1.turnos.schedule;

import ar.uba.fi.ingsoft1.turnos.appointment.Appointment;
import ar.uba.fi.ingsoft1.turnos.appointment.AppointmentRepository;
import ar.uba.fi.ingsoft1.turnos.common.exception.BadRequestException;
import ar.uba.fi.ingsoft1.turnos.common.exception.ConflictException;
import ar.uba.fi.ingsoft1.turnos.common.exception.ForbiddenException;
import ar.uba.fi.ingsoft1.turnos.common.exception.ItemNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ScheduleBlockServiceTest {

    private static final ZoneId ZONE = ZoneId.of("America/Argentina/Buenos_Aires");

    private ScheduleBlockService scheduleBlockService;
    private ScheduleBlockRepository scheduleBlockRepository;
    private AppointmentRepository appointmentRepository;

    @BeforeEach
    void setUp() {
        scheduleBlockRepository = mock(ScheduleBlockRepository.class);
        appointmentRepository = mock(AppointmentRepository.class);
        scheduleBlockService = new ScheduleBlockService(scheduleBlockRepository, appointmentRepository);
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private ScheduleBlock buildBlock(Long id, Long professionalId, LocalDate blockDate,
                                     LocalTime startTime, LocalTime endTime) throws Exception {
        ScheduleBlock block = new ScheduleBlock();
        setField(block, "id", id);
        setField(block, "professionalId", professionalId);
        setField(block, "blockDate", blockDate);
        setField(block, "startTime", startTime);
        setField(block, "endTime", endTime);
        return block;
    }

    private Appointment buildAppointment(Long id, Long professionalId, LocalDate date,
                                         LocalTime start, LocalTime end, String status) throws Exception {
        Appointment appt = new Appointment();
        setField(appt, "id", id);
        setField(appt, "professionalId", professionalId);
        setField(appt, "start", ZonedDateTime.of(date, start, ZONE));
        setField(appt, "end", ZonedDateTime.of(date, end, ZONE));
        setField(appt, "status", status);
        return appt;
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    // ── getBlocks ────────────────────────────────────────────────────────────

    @Test
    void getBlocksReturnsDTOList() throws Exception {
        ScheduleBlock block = buildBlock(1L, 10L, LocalDate.of(2026, 6, 15),
                LocalTime.of(10, 0), LocalTime.of(12, 0));
        when(scheduleBlockRepository.findByProfessionalIdOrderByBlockDateAscStartTimeAsc(10L))
                .thenReturn(List.of(block));

        List<ScheduleBlockDTO> result = scheduleBlockService.getBlocks(10L);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).id());
        assertEquals(LocalDate.of(2026, 6, 15), result.get(0).blockDate());
        assertEquals(LocalTime.of(10, 0), result.get(0).startTime());
        assertEquals(LocalTime.of(12, 0), result.get(0).endTime());
    }

    @Test
    void getBlocksReturnsEmptyWhenNone() {
        when(scheduleBlockRepository.findByProfessionalIdOrderByBlockDateAscStartTimeAsc(10L))
                .thenReturn(List.of());

        List<ScheduleBlockDTO> result = scheduleBlockService.getBlocks(10L);

        assertTrue(result.isEmpty());
    }

    // ── createBlock — success ────────────────────────────────────────────────

    @Test
    void createBlockSavesAndReturnsResult() throws Exception {
        LocalDate date = LocalDate.of(2026, 7, 10);
        LocalTime start = LocalTime.of(9, 0);
        LocalTime end = LocalTime.of(11, 0);
        ScheduleBlockRequestDTO request = new ScheduleBlockRequestDTO(date, start, end);

        when(scheduleBlockRepository.findOverlappingBlocks(10L, date, start, end))
                .thenReturn(List.of());

        ScheduleBlock savedBlock = buildBlock(1L, 10L, date, start, end);
        when(scheduleBlockRepository.save(any())).thenReturn(savedBlock);

        when(appointmentRepository.findActiveByProfessionalIdAndRange(eq(10L), any(), any()))
                .thenReturn(List.of());

        ScheduleBlockCreateResultDTO result = scheduleBlockService.createBlock(10L, request);

        assertNotNull(result);
        assertEquals(1L, result.block().id());
        assertEquals(date, result.block().blockDate());
        assertEquals(start, result.block().startTime());
        assertEquals(end, result.block().endTime());
        assertEquals(0, result.cancelledAppointments());
        verify(scheduleBlockRepository).save(any());
    }

    @Test
    void createBlockCancelsOverlappingConfirmedAppointments() throws Exception {
        LocalDate date = LocalDate.of(2026, 7, 10);
        LocalTime blockStart = LocalTime.of(10, 0);
        LocalTime blockEnd = LocalTime.of(12, 0);
        ScheduleBlockRequestDTO request = new ScheduleBlockRequestDTO(date, blockStart, blockEnd);

        when(scheduleBlockRepository.findOverlappingBlocks(10L, date, blockStart, blockEnd))
                .thenReturn(List.of());

        ScheduleBlock savedBlock = buildBlock(1L, 10L, date, blockStart, blockEnd);
        when(scheduleBlockRepository.save(any())).thenReturn(savedBlock);

        // Two CONFIRMED appointments, one inside the block, one outside
        Appointment inside = buildAppointment(1L, 10L, date, LocalTime.of(10, 30), LocalTime.of(11, 0), "CONFIRMED");
        Appointment outside = buildAppointment(2L, 10L, date, LocalTime.of(12, 0), LocalTime.of(13, 0), "CONFIRMED");
        when(appointmentRepository.findActiveByProfessionalIdAndRange(eq(10L), any(), any()))
                .thenReturn(List.of(inside, outside));

        ScheduleBlockCreateResultDTO result = scheduleBlockService.createBlock(10L, request);

        assertEquals(1, result.cancelledAppointments());
        assertEquals("CANCELLED", inside.getStatus());
        assertEquals("professional", inside.getCancelledBy());
        assertNotNull(inside.getCancelledDate());
        // The outside appointment should NOT have been cancelled
        assertEquals("CONFIRMED", outside.getStatus());
    }

    @Test
    void createBlockSkipsPastAppointments() throws Exception {
        LocalDate date = LocalDate.of(2026, 6, 1);
        LocalTime blockStart = LocalTime.of(10, 0);
        LocalTime blockEnd = LocalTime.of(12, 0);
        ScheduleBlockRequestDTO request = new ScheduleBlockRequestDTO(date, blockStart, blockEnd);

        when(scheduleBlockRepository.findOverlappingBlocks(10L, date, blockStart, blockEnd))
                .thenReturn(List.of());

        ScheduleBlock savedBlock = buildBlock(1L, 10L, date, blockStart, blockEnd);
        when(scheduleBlockRepository.save(any())).thenReturn(savedBlock);

        // Appointment in the past (assuming current time is after this)
        Appointment pastAppointment = buildAppointment(1L, 10L, date, LocalTime.of(8, 0), LocalTime.of(9, 0), "CONFIRMED");
        when(appointmentRepository.findActiveByProfessionalIdAndRange(eq(10L), any(), any()))
                .thenReturn(List.of(pastAppointment));

        ScheduleBlockCreateResultDTO result = scheduleBlockService.createBlock(10L, request);

        assertEquals(0, result.cancelledAppointments());
        assertEquals("CONFIRMED", pastAppointment.getStatus());
    }

    @Test
    void createBlockSkipsNonConfirmedAppointments() throws Exception {
        LocalDate date = LocalDate.of(2026, 7, 10);
        LocalTime blockStart = LocalTime.of(10, 0);
        LocalTime blockEnd = LocalTime.of(12, 0);
        ScheduleBlockRequestDTO request = new ScheduleBlockRequestDTO(date, blockStart, blockEnd);

        when(scheduleBlockRepository.findOverlappingBlocks(10L, date, blockStart, blockEnd))
                .thenReturn(List.of());

        ScheduleBlock savedBlock = buildBlock(1L, 10L, date, blockStart, blockEnd);
        when(scheduleBlockRepository.save(any())).thenReturn(savedBlock);

        Appointment cancelled = buildAppointment(1L, 10L, date, LocalTime.of(10, 30), LocalTime.of(11, 0), "CANCELLED");
        Appointment completed = buildAppointment(2L, 10L, date, LocalTime.of(11, 0), LocalTime.of(11, 30), "COMPLETED");
        when(appointmentRepository.findActiveByProfessionalIdAndRange(eq(10L), any(), any()))
                .thenReturn(List.of(cancelled, completed));

        ScheduleBlockCreateResultDTO result = scheduleBlockService.createBlock(10L, request);

        assertEquals(0, result.cancelledAppointments());
        assertEquals("CANCELLED", cancelled.getStatus()); // unchanged
        assertEquals("COMPLETED", completed.getStatus()); // unchanged
    }

    @Test
    void createBlockSkipsNonOverlappingAppointments() throws Exception {
        LocalDate date = LocalDate.of(2026, 7, 10);
        LocalTime blockStart = LocalTime.of(10, 0);
        LocalTime blockEnd = LocalTime.of(12, 0);
        ScheduleBlockRequestDTO request = new ScheduleBlockRequestDTO(date, blockStart, blockEnd);

        when(scheduleBlockRepository.findOverlappingBlocks(10L, date, blockStart, blockEnd))
                .thenReturn(List.of());

        ScheduleBlock savedBlock = buildBlock(1L, 10L, date, blockStart, blockEnd);
        when(scheduleBlockRepository.save(any())).thenReturn(savedBlock);

        // Appointments that don't overlap with the block [10:00 - 12:00)
        Appointment beforeBlock = buildAppointment(1L, 10L, date, LocalTime.of(8, 0), LocalTime.of(9, 0), "CONFIRMED");
        Appointment afterBlock = buildAppointment(2L, 10L, date, LocalTime.of(12, 0), LocalTime.of(13, 0), "CONFIRMED");
        when(appointmentRepository.findActiveByProfessionalIdAndRange(eq(10L), any(), any()))
                .thenReturn(List.of(beforeBlock, afterBlock));

        ScheduleBlockCreateResultDTO result = scheduleBlockService.createBlock(10L, request);

        assertEquals(0, result.cancelledAppointments());
        assertEquals("CONFIRMED", beforeBlock.getStatus());
        assertEquals("CONFIRMED", afterBlock.getStatus());
    }

    // ── createBlock — errors ─────────────────────────────────────────────────

    @Test
    void createBlockThrowsWhenStartTimeNotBeforeEndTime() {
        ScheduleBlockRequestDTO invalidRequest = new ScheduleBlockRequestDTO(
                LocalDate.of(2026, 7, 10),
                LocalTime.of(14, 0),
                LocalTime.of(14, 0)  // same time = invalid
        );

        assertThrows(BadRequestException.class,
                () -> scheduleBlockService.createBlock(10L, invalidRequest));
    }

    @Test
    void createBlockThrowsWhenStartTimeAfterEndTime() {
        ScheduleBlockRequestDTO invalidRequest = new ScheduleBlockRequestDTO(
                LocalDate.of(2026, 7, 10),
                LocalTime.of(16, 0),
                LocalTime.of(15, 0)  // start > end = invalid
        );

        assertThrows(BadRequestException.class,
                () -> scheduleBlockService.createBlock(10L, invalidRequest));
    }

    @Test
    void createBlockThrowsWhenOverlappingBlockExists() throws Exception {
        LocalDate date = LocalDate.of(2026, 7, 10);
        LocalTime start = LocalTime.of(10, 0);
        LocalTime end = LocalTime.of(12, 0);
        ScheduleBlockRequestDTO request = new ScheduleBlockRequestDTO(date, start, end);

        ScheduleBlock existing = buildBlock(5L, 10L, date, LocalTime.of(11, 0), LocalTime.of(13, 0));
        when(scheduleBlockRepository.findOverlappingBlocks(10L, date, start, end))
                .thenReturn(List.of(existing));

        assertThrows(ConflictException.class,
                () -> scheduleBlockService.createBlock(10L, request));

        verify(scheduleBlockRepository, never()).save(any());
    }

    // ── deleteBlock ──────────────────────────────────────────────────────────

    @Test
    void deleteBlockDeletesWhenOwner() throws Exception {
        ScheduleBlock block = buildBlock(1L, 10L, LocalDate.of(2026, 7, 10),
                LocalTime.of(10, 0), LocalTime.of(12, 0));
        when(scheduleBlockRepository.findById(1L)).thenReturn(Optional.of(block));

        scheduleBlockService.deleteBlock(10L, 1L);

        verify(scheduleBlockRepository).delete(block);
    }

    @Test
    void deleteBlockThrowsWhenNotFound() {
        when(scheduleBlockRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class,
                () -> scheduleBlockService.deleteBlock(10L, 999L));

        verify(scheduleBlockRepository, never()).delete(any());
    }

    @Test
    void deleteBlockThrowsWhenNotOwner() throws Exception {
        ScheduleBlock block = buildBlock(1L, 99L, LocalDate.of(2026, 7, 10),
                LocalTime.of(10, 0), LocalTime.of(12, 0));
        when(scheduleBlockRepository.findById(1L)).thenReturn(Optional.of(block));

        assertThrows(ForbiddenException.class,
                () -> scheduleBlockService.deleteBlock(10L, 1L));

        verify(scheduleBlockRepository, never()).delete(any());
    }
}