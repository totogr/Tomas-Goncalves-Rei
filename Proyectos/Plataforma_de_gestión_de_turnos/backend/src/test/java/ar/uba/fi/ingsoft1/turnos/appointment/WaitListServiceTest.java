package ar.uba.fi.ingsoft1.turnos.appointment;

import ar.uba.fi.ingsoft1.turnos.appointment.dto.WaitListEntryDTO;
import ar.uba.fi.ingsoft1.turnos.client.Client;
import ar.uba.fi.ingsoft1.turnos.client.ClientRepository;
import ar.uba.fi.ingsoft1.turnos.common.exception.BadRequestException;
import ar.uba.fi.ingsoft1.turnos.common.exception.ItemNotFoundException;
import ar.uba.fi.ingsoft1.turnos.config.email.EmailService;
import ar.uba.fi.ingsoft1.turnos.professional.ProfessionalRepository;
import ar.uba.fi.ingsoft1.turnos.service.ServiceEntity;
import ar.uba.fi.ingsoft1.turnos.service.ServiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class WaitListServiceTest {

    private static final ZoneId ZONE = ZoneId.of("America/Argentina/Buenos_Aires");

    private WaitListRepository waitListRepository;
    private BookingService bookingService;
    private SlotAvailabilityService slotAvailabilityService;
    private ServiceRepository serviceRepository;
    private ProfessionalRepository professionalRepository;
    private WaitListPromotionRepository promotionRepository;
    private ClientRepository clientRepository;
    private EmailService emailService;

    private WaitListService waitListService;

    private static final Long PROF = 1L;
    private static final Long SERVICE = 100L;
    private static final Long CLIENT = 7L;
    private final ZonedDateTime slot = ZonedDateTime.of(2030, 1, 1, 10, 0, 0, 0, ZONE);

    @BeforeEach
    void setUp() {
        waitListRepository = mock(WaitListRepository.class);
        bookingService = mock(BookingService.class);
        slotAvailabilityService = mock(SlotAvailabilityService.class);
        serviceRepository = mock(ServiceRepository.class);
        professionalRepository = mock(ProfessionalRepository.class);
        promotionRepository = mock(WaitListPromotionRepository.class);
        clientRepository = mock(ClientRepository.class);
        emailService = mock(EmailService.class);

        waitListService = new WaitListService(
                waitListRepository, bookingService, slotAvailabilityService, serviceRepository,
                professionalRepository, promotionRepository, clientRepository, emailService);
    }

    private ServiceEntity service(int duration) {
        ServiceEntity s = new ServiceEntity();
        s.setDuration(duration);
        s.setActive(true);
        return s;
    }

    private WaitListEntry entry(Long id, Long clientId, ZonedDateTime created) {
        WaitListEntry e = new WaitListEntry();
        e.setId(id);
        e.setClientId(clientId);
        e.setProfessionalId(PROF);
        e.setServiceId(SERVICE);
        e.setSlotStart(slot);
        e.setCreationTime(created);
        return e;
    }

    // ── joinWaitList ──────────────────────────────────────────────────────────

    @Test
    void joinThrowsWhenServiceNotFound() {
        when(serviceRepository.findById(SERVICE)).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class,
                () -> waitListService.joinWaitList(PROF, SERVICE, slot, CLIENT));
    }

    @Test
    void joinThrowsWhenClientHasOverlappingBooking() {
        when(serviceRepository.findById(SERVICE)).thenReturn(Optional.of(service(60)));
        when(slotAvailabilityService.hasClientOverlappingBooking(CLIENT, slot, 60)).thenReturn(true);

        assertThrows(BadRequestException.class,
                () -> waitListService.joinWaitList(PROF, SERVICE, slot, CLIENT));
    }

    @Test
    void joinThrowsWhenSlotHasAvailability() {
        when(serviceRepository.findById(SERVICE)).thenReturn(Optional.of(service(60)));
        when(slotAvailabilityService.hasClientOverlappingBooking(CLIENT, slot, 60)).thenReturn(false);
        when(slotAvailabilityService.isSlotFullyBooked(PROF, SERVICE, slot)).thenReturn(false);

        assertThrows(BadRequestException.class,
                () -> waitListService.joinWaitList(PROF, SERVICE, slot, CLIENT));
    }

    @Test
    void joinThrowsWhenAlreadyInWaitList() {
        when(serviceRepository.findById(SERVICE)).thenReturn(Optional.of(service(60)));
        when(slotAvailabilityService.hasClientOverlappingBooking(CLIENT, slot, 60)).thenReturn(false);
        when(slotAvailabilityService.isSlotFullyBooked(PROF, SERVICE, slot)).thenReturn(true);
        when(waitListRepository.existsByClientIdAndProfessionalIdAndServiceIdAndSlotStart(
                CLIENT, PROF, SERVICE, slot)).thenReturn(true);

        assertThrows(BadRequestException.class,
                () -> waitListService.joinWaitList(PROF, SERVICE, slot, CLIENT));
    }

    @Test
    void joinSucceedsAndReturnsPosition() {
        when(serviceRepository.findById(SERVICE)).thenReturn(Optional.of(service(60)));
        when(slotAvailabilityService.hasClientOverlappingBooking(CLIENT, slot, 60)).thenReturn(false);
        when(slotAvailabilityService.isSlotFullyBooked(PROF, SERVICE, slot)).thenReturn(true);
        when(waitListRepository.existsByClientIdAndProfessionalIdAndServiceIdAndSlotStart(
                CLIENT, PROF, SERVICE, slot)).thenReturn(false);

        WaitListEntry saved = entry(55L, CLIENT, slot);
        when(waitListRepository.save(any())).thenReturn(saved);
        when(waitListRepository.findByProfessionalIdAndServiceIdAndSlotStartOrderByCreationTimeAsc(
                PROF, SERVICE, slot)).thenReturn(List.of(saved));
        when(serviceRepository.findById(SERVICE)).thenReturn(Optional.of(service(60)));
        when(professionalRepository.findById(PROF)).thenReturn(Optional.empty());

        WaitListEntryDTO dto = waitListService.joinWaitList(PROF, SERVICE, slot, CLIENT);

        assertEquals(55L, dto.id());
        assertEquals(1, dto.position());
        verify(waitListRepository).save(any());
    }

    // ── leaveWaitList ─────────────────────────────────────────────────────────

    @Test
    void leaveThrowsWhenClientNotInQueue() {
        when(waitListRepository.findByProfessionalIdAndServiceIdAndSlotStartOrderByCreationTimeAsc(
                PROF, SERVICE, slot)).thenReturn(List.of(entry(1L, 999L, slot)));

        assertThrows(ItemNotFoundException.class,
                () -> waitListService.leaveWaitList(PROF, SERVICE, slot, CLIENT));
    }

    @Test
    void leaveDeletesEntry() {
        WaitListEntry mine = entry(1L, CLIENT, slot);
        when(waitListRepository.findByProfessionalIdAndServiceIdAndSlotStartOrderByCreationTimeAsc(
                PROF, SERVICE, slot)).thenReturn(List.of(mine));

        waitListService.leaveWaitList(PROF, SERVICE, slot, CLIENT);

        verify(waitListRepository).delete(mine);
    }

    // ── getClientWaitListEntry (posición) ─────────────────────────────────────

    @Test
    void getClientEntryReturnsCorrectPosition() {
        WaitListEntry first = entry(1L, 999L, slot.minusMinutes(5));
        WaitListEntry mine = entry(2L, CLIENT, slot);
        when(waitListRepository.findByProfessionalIdAndServiceIdAndSlotStartOrderByCreationTimeAsc(
                PROF, SERVICE, slot)).thenReturn(List.of(first, mine));
        when(serviceRepository.findById(SERVICE)).thenReturn(Optional.of(service(60)));
        when(professionalRepository.findById(PROF)).thenReturn(Optional.empty());

        WaitListEntryDTO dto = waitListService.getClientWaitListEntry(PROF, SERVICE, slot, CLIENT);

        assertEquals(2, dto.position());
    }

    @Test
    void getClientEntryThrowsWhenNotInQueue() {
        when(waitListRepository.findByProfessionalIdAndServiceIdAndSlotStartOrderByCreationTimeAsc(
                PROF, SERVICE, slot)).thenReturn(List.of(entry(1L, 999L, slot)));

        assertThrows(ItemNotFoundException.class,
                () -> waitListService.getClientWaitListEntry(PROF, SERVICE, slot, CLIENT));
    }

    // ── confirmPromotion ──────────────────────────────────────────────────────

    private WaitListPromotion promotion(ZonedDateTime expiresAt) {
        WaitListPromotion p = new WaitListPromotion();
        p.setId(10L);
        p.setWaitListEntryId(2L);
        p.setClientId(CLIENT);
        p.setProfessionalId(PROF);
        p.setServiceId(SERVICE);
        p.setSlotStart(slot);
        p.setExpiresAt(expiresAt);
        return p;
    }

    @Test
    void confirmThrowsWhenNoActivePromotion() {
        when(promotionRepository
                .findByClientIdAndProfessionalIdAndServiceIdAndSlotStartAndConfirmedFalseAndExpiredFalse(
                        CLIENT, PROF, SERVICE, slot)).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class,
                () -> waitListService.confirmPromotion(PROF, SERVICE, slot, CLIENT));
    }

    @Test
    void confirmThrowsAndExpiresWhenPromotionExpired() {
        WaitListPromotion expired = promotion(ZonedDateTime.now(ZONE).minusMinutes(1));
        when(promotionRepository
                .findByClientIdAndProfessionalIdAndServiceIdAndSlotStartAndConfirmedFalseAndExpiredFalse(
                        CLIENT, PROF, SERVICE, slot)).thenReturn(Optional.of(expired));

        assertThrows(BadRequestException.class,
                () -> waitListService.confirmPromotion(PROF, SERVICE, slot, CLIENT));

        assertTrue(expired.isExpired());
        verify(bookingService, never()).createBooking(any(), anyLong());
    }

    @Test
    void confirmCreatesBookingAndCleansUp() {
        WaitListPromotion active = promotion(ZonedDateTime.now(ZONE).plusHours(1));
        when(promotionRepository
                .findByClientIdAndProfessionalIdAndServiceIdAndSlotStartAndConfirmedFalseAndExpiredFalse(
                        CLIENT, PROF, SERVICE, slot)).thenReturn(Optional.of(active));
        WaitListEntry mine = entry(2L, CLIENT, slot);
        when(waitListRepository.findByProfessionalIdAndServiceIdAndSlotStartOrderByCreationTimeAsc(
                PROF, SERVICE, slot)).thenReturn(List.of(mine));

        waitListService.confirmPromotion(PROF, SERVICE, slot, CLIENT);

        verify(bookingService).createBookingFromPromotion(any(), eq(CLIENT));
        assertTrue(active.isConfirmed());
        verify(waitListRepository).delete(mine);
    }

    // ── expireAndPromote ──────────────────────────────────────────────────────

    @Test
    void expireMarksPromotionAndRemovesCandidate() {
        WaitListPromotion p = promotion(ZonedDateTime.now(ZONE).minusMinutes(1));
        when(promotionRepository.findById(10L)).thenReturn(Optional.of(p));
        WaitListEntry candidate = entry(2L, CLIENT, slot);
        when(waitListRepository.findById(2L)).thenReturn(Optional.of(candidate));
        when(waitListRepository.findByProfessionalIdAndServiceIdAndSlotStartOrderByCreationTimeAsc(
                PROF, SERVICE, slot)).thenReturn(List.of());

        waitListService.expireAndPromote(10L);

        assertTrue(p.isExpired());
        verify(waitListRepository).delete(candidate);
        verify(promotionRepository).saveAndFlush(p);
    }

    // ── promoteFirstInQueue ───────────────────────────────────────────────────

    @Test
    void promoteFirstOffersToFirstEligibleAndSendsEmail() {
        WaitListEntry candidate = entry(2L, CLIENT, slot);
        when(waitListRepository.findByProfessionalIdAndServiceIdAndSlotStartOrderByCreationTimeAsc(
                PROF, SERVICE, slot)).thenReturn(List.of(candidate));
        when(promotionRepository
                .findByClientIdAndProfessionalIdAndServiceIdAndSlotStartAndConfirmedFalseAndExpiredFalse(
                        CLIENT, PROF, SERVICE, slot)).thenReturn(Optional.empty());
        when(slotAvailabilityService.isClientBooked(CLIENT, PROF, SERVICE, slot)).thenReturn(false);
        when(promotionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        Client client = new Client("c@mail.com", "x", "Ana", "Lopez");
        when(clientRepository.findById(CLIENT)).thenReturn(Optional.of(client));

        waitListService.promoteFirstInQueue(PROF, SERVICE, slot);

        verify(promotionRepository).save(any());
        verify(emailService).sendWaitListPromotionEmail(
                eq("c@mail.com"), eq("Ana"), eq(PROF), eq(SERVICE), eq(slot), any());
    }

    @Test
    void promoteFirstSkipsCandidateAlreadyOffered() {
        WaitListEntry candidate = entry(2L, CLIENT, slot);
        when(waitListRepository.findByProfessionalIdAndServiceIdAndSlotStartOrderByCreationTimeAsc(
                PROF, SERVICE, slot)).thenReturn(List.of(candidate));
        when(promotionRepository
                .findByClientIdAndProfessionalIdAndServiceIdAndSlotStartAndConfirmedFalseAndExpiredFalse(
                        CLIENT, PROF, SERVICE, slot)).thenReturn(Optional.of(promotion(slot)));

        waitListService.promoteFirstInQueue(PROF, SERVICE, slot);

        verify(promotionRepository, never()).save(any());
        verify(emailService, never()).sendWaitListPromotionEmail(any(), any(), any(), any(), any(), any());
    }

    @Test
    void promoteFirstRemovesCandidateAlreadyBooked() {
        WaitListEntry candidate = entry(2L, CLIENT, slot);
        when(waitListRepository.findByProfessionalIdAndServiceIdAndSlotStartOrderByCreationTimeAsc(
                PROF, SERVICE, slot)).thenReturn(List.of(candidate));
        when(promotionRepository
                .findByClientIdAndProfessionalIdAndServiceIdAndSlotStartAndConfirmedFalseAndExpiredFalse(
                        CLIENT, PROF, SERVICE, slot)).thenReturn(Optional.empty());
        when(slotAvailabilityService.isClientBooked(CLIENT, PROF, SERVICE, slot)).thenReturn(true);

        waitListService.promoteFirstInQueue(PROF, SERVICE, slot);

        verify(waitListRepository).delete(candidate);
        verify(promotionRepository, never()).save(any());
    }

    // ── promoteAffectedSlots (cascada en cancelación) ─────────────────────────

    @Test
    void promoteAffectedSlotsPromotesWhenWindowFreesUp() {
        WaitListEntry candidate = entry(2L, CLIENT, slot);
        // cola no vacía para el slot liberado
        when(waitListRepository.findByProfessionalIdAndServiceIdAndSlotStartOrderByCreationTimeAsc(
                PROF, SERVICE, slot)).thenReturn(List.of(candidate));
        when(slotAvailabilityService.isWindowAvailable(PROF, SERVICE, slot, 30)).thenReturn(true);
        // promoteFirstInQueue: candidato elegible
        when(promotionRepository
                .findByClientIdAndProfessionalIdAndServiceIdAndSlotStartAndConfirmedFalseAndExpiredFalse(
                        CLIENT, PROF, SERVICE, slot)).thenReturn(Optional.empty());
        when(slotAvailabilityService.isClientBooked(CLIENT, PROF, SERVICE, slot)).thenReturn(false);
        when(promotionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(clientRepository.findById(CLIENT)).thenReturn(Optional.of(new Client("c@mail.com", "x", "Ana", "Lopez")));
        // el slot cancelado no queda completamente vacío -> evita la rama de solapamientos
        when(slotAvailabilityService.isSlotCompletelyEmpty(PROF, SERVICE, slot, 30)).thenReturn(false);

        waitListService.promoteAffectedSlots(PROF, SERVICE, slot, 30, 30);

        verify(emailService).sendWaitListPromotionEmail(
                eq("c@mail.com"), eq("Ana"), eq(PROF), eq(SERVICE), eq(slot), any());
    }

    @Test
    void promoteAffectedSlotsDoesNothingWhenQueueEmpty() {
        when(waitListRepository.findByProfessionalIdAndServiceIdAndSlotStartOrderByCreationTimeAsc(
                PROF, SERVICE, slot)).thenReturn(List.of());
        when(slotAvailabilityService.isSlotCompletelyEmpty(PROF, SERVICE, slot, 30)).thenReturn(false);

        waitListService.promoteAffectedSlots(PROF, SERVICE, slot, 30, 30);

        verify(promotionRepository, never()).save(any());
        verify(emailService, never()).sendWaitListPromotionEmail(any(), any(), any(), any(), any(), any());
    }
}
