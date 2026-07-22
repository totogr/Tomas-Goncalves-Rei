package ar.uba.fi.ingsoft1.turnos.blockedclient;

import ar.uba.fi.ingsoft1.turnos.appointment.Appointment;
import ar.uba.fi.ingsoft1.turnos.appointment.AppointmentRepository;
import ar.uba.fi.ingsoft1.turnos.common.exception.ConflictException;
import ar.uba.fi.ingsoft1.turnos.common.exception.ItemNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BlockedClientServiceTest {

    private static final ZoneId ZONE = ZoneId.of("America/Argentina/Buenos_Aires");

    private BlockedClientService blockedClientService;
    private BlockedClientRepository blockedClientRepository;
    private AppointmentRepository appointmentRepository;

    @BeforeEach
    void setUp() {
        blockedClientRepository = mock(BlockedClientRepository.class);
        appointmentRepository = mock(AppointmentRepository.class);
        blockedClientService = new BlockedClientService(blockedClientRepository, appointmentRepository);
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private BlockedClient buildBlockedClient(Long professionalId, Long clientId) {
        return new BlockedClient(professionalId, clientId);
    }

    // ── blockClient ───────────────────────────────────────────────────────────

    @Test
    void blockClientCreatesAndReturnsBlockedClient() {
        when(blockedClientRepository.existsByProfessionalIdAndClientId(1L, 10L))
                .thenReturn(false);

        BlockedClient saved = buildBlockedClient(1L, 10L);
        when(blockedClientRepository.save(any())).thenReturn(saved);

        BlockedClient result = blockedClientService.blockClient(1L, 10L);

        assertNotNull(result);
        assertEquals(1L, result.getProfessionalId());
        assertEquals(10L, result.getClientId());
        assertNotNull(result.getBlockedAt());
        verify(blockedClientRepository).save(any());
    }

    @Test
    void blockClientThrowsWhenAlreadyBlocked() {
        when(blockedClientRepository.existsByProfessionalIdAndClientId(1L, 10L))
                .thenReturn(true);

        assertThrows(ConflictException.class,
                () -> blockedClientService.blockClient(1L, 10L));

        verify(blockedClientRepository, never()).save(any());
    }

    @Test
    void blockClientCancelsFutureConfirmedAppointments() {
        when(blockedClientRepository.existsByProfessionalIdAndClientId(1L, 10L))
                .thenReturn(false);

        BlockedClient saved = buildBlockedClient(1L, 10L);
        when(blockedClientRepository.save(any())).thenReturn(saved);

        ZonedDateTime future = ZonedDateTime.now(ZONE).plusDays(7);
        Appointment futureAppt = new Appointment();
        futureAppt.setId(100L);
        futureAppt.setProfessionalId(1L);
        futureAppt.setClientId(10L);
        futureAppt.setStart(future);
        futureAppt.setStatus("CONFIRMED");

        when(appointmentRepository.findByProfessionalIdAndClientId(1L, 10L))
                .thenReturn(List.of(futureAppt));

        BlockedClient result = blockedClientService.blockClient(1L, 10L);

        assertNotNull(result);
        verify(appointmentRepository).findByProfessionalIdAndClientId(1L, 10L);
        verify(appointmentRepository, times(1)).save(futureAppt);
        assertEquals("CANCELLED", futureAppt.getStatus());
        assertEquals("professional", futureAppt.getCancelledBy());
        assertNotNull(futureAppt.getCancelledDate());
    }

    @Test
    void blockClientDoesNotCancelPastAppointments() {
        when(blockedClientRepository.existsByProfessionalIdAndClientId(1L, 10L))
                .thenReturn(false);

        BlockedClient saved = buildBlockedClient(1L, 10L);
        when(blockedClientRepository.save(any())).thenReturn(saved);

        ZonedDateTime past = ZonedDateTime.now(ZONE).minusDays(7);
        Appointment pastAppt = new Appointment();
        pastAppt.setId(100L);
        pastAppt.setProfessionalId(1L);
        pastAppt.setClientId(10L);
        pastAppt.setStart(past);
        pastAppt.setStatus("CONFIRMED");

        when(appointmentRepository.findByProfessionalIdAndClientId(1L, 10L))
                .thenReturn(List.of(pastAppt));

        BlockedClient result = blockedClientService.blockClient(1L, 10L);

        assertNotNull(result);
        verify(appointmentRepository).findByProfessionalIdAndClientId(1L, 10L);
        verify(appointmentRepository, never()).save(pastAppt);
        assertEquals("CONFIRMED", pastAppt.getStatus());
        assertNull(pastAppt.getCancelledBy());
    }

    @Test
    void blockClientDoesNotCancelAlreadyCancelledAppointments() {
        when(blockedClientRepository.existsByProfessionalIdAndClientId(1L, 10L))
                .thenReturn(false);

        BlockedClient saved = buildBlockedClient(1L, 10L);
        when(blockedClientRepository.save(any())).thenReturn(saved);

        ZonedDateTime future = ZonedDateTime.now(ZONE).plusDays(7);
        Appointment cancelledAppt = new Appointment();
        cancelledAppt.setId(100L);
        cancelledAppt.setProfessionalId(1L);
        cancelledAppt.setClientId(10L);
        cancelledAppt.setStart(future);
        cancelledAppt.setStatus("CANCELLED");

        when(appointmentRepository.findByProfessionalIdAndClientId(1L, 10L))
                .thenReturn(List.of(cancelledAppt));

        BlockedClient result = blockedClientService.blockClient(1L, 10L);

        assertNotNull(result);
        verify(appointmentRepository).findByProfessionalIdAndClientId(1L, 10L);
        verify(appointmentRepository, never()).save(cancelledAppt);
        assertEquals("CANCELLED", cancelledAppt.getStatus());
    }

    // ── unblockClient ─────────────────────────────────────────────────────────

    @Test
    void unblockClientDeletesWhenExists() {
        BlockedClient existing = buildBlockedClient(1L, 10L);
        when(blockedClientRepository.findByProfessionalIdAndClientId(1L, 10L))
                .thenReturn(Optional.of(existing));

        blockedClientService.unblockClient(1L, 10L);

        verify(blockedClientRepository).delete(existing);
    }

    @Test
    void unblockClientThrowsWhenNotFound() {
        when(blockedClientRepository.findByProfessionalIdAndClientId(1L, 10L))
                .thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class,
                () -> blockedClientService.unblockClient(1L, 10L));

        verify(blockedClientRepository, never()).delete(any());
    }

    // ── getBlockedClientIds ───────────────────────────────────────────────────

    @Test
    void getBlockedClientIdsReturnsList() {
        BlockedClient b1 = buildBlockedClient(1L, 10L);
        BlockedClient b2 = buildBlockedClient(1L, 20L);
        BlockedClient b3 = buildBlockedClient(1L, 30L);
        when(blockedClientRepository.findByProfessionalId(1L))
                .thenReturn(List.of(b1, b2, b3));

        List<Long> result = blockedClientService.getBlockedClientIds(1L);

        assertEquals(3, result.size());
        assertTrue(result.containsAll(List.of(10L, 20L, 30L)));
    }

    @Test
    void getBlockedClientIdsReturnsEmptyWhenNone() {
        when(blockedClientRepository.findByProfessionalId(1L))
                .thenReturn(List.of());

        List<Long> result = blockedClientService.getBlockedClientIds(1L);

        assertTrue(result.isEmpty());
    }

    // ── getBlockedProfessionalIdsForClient ────────────────────────────────────

    @Test
    void getBlockedProfessionalIdsForClientReturnsList() {
        BlockedClient b1 = buildBlockedClient(1L, 10L);
        BlockedClient b2 = buildBlockedClient(2L, 10L);
        BlockedClient b3 = buildBlockedClient(3L, 10L);
        when(blockedClientRepository.findByClientId(10L))
                .thenReturn(List.of(b1, b2, b3));

        List<Long> result = blockedClientService.getBlockedProfessionalIdsForClient(10L);

        assertEquals(3, result.size());
        assertTrue(result.containsAll(List.of(1L, 2L, 3L)));
    }

    @Test
    void getBlockedProfessionalIdsForClientReturnsEmptyWhenNone() {
        when(blockedClientRepository.findByClientId(10L))
                .thenReturn(List.of());

        List<Long> result = blockedClientService.getBlockedProfessionalIdsForClient(10L);

        assertTrue(result.isEmpty());
    }

    // ── isBlocked ─────────────────────────────────────────────────────────────

    @Test
    void isBlockedReturnsTrueWhenBlocked() {
        when(blockedClientRepository.existsByProfessionalIdAndClientId(1L, 10L))
                .thenReturn(true);

        boolean result = blockedClientService.isBlocked(1L, 10L);

        assertTrue(result);
    }

    @Test
    void isBlockedReturnsFalseWhenNotBlocked() {
        when(blockedClientRepository.existsByProfessionalIdAndClientId(1L, 10L))
                .thenReturn(false);

        boolean result = blockedClientService.isBlocked(1L, 10L);

        assertFalse(result);
    }
}