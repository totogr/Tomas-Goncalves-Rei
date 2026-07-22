package ar.uba.fi.ingsoft1.turnos.appointment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class WaitListSchedulerTest {

    private static final ZoneId ZONE = ZoneId.of("America/Argentina/Buenos_Aires");

    private WaitListPromotionRepository promotionRepository;
    private WaitListService waitListService;
    private WaitListScheduler scheduler;

    @BeforeEach
    void setUp() {
        promotionRepository = mock(WaitListPromotionRepository.class);
        waitListService = mock(WaitListService.class);
        scheduler = new WaitListScheduler(promotionRepository, waitListService);
    }

    private WaitListPromotion promotion(Long id) {
        WaitListPromotion p = new WaitListPromotion();
        p.setId(id);
        p.setExpiresAt(ZonedDateTime.now(ZONE).minusMinutes(5));
        return p;
    }

    @Test
    void expiresEachDuePromotion() {
        when(promotionRepository.findByExpiredFalseAndConfirmedFalseAndExpiresAtBefore(any()))
                .thenReturn(List.of(promotion(1L), promotion(2L)));

        scheduler.expirePromotions();

        verify(waitListService).expireAndPromote(1L);
        verify(waitListService).expireAndPromote(2L);
    }

    @Test
    void doesNothingWhenNoPromotionsAreDue() {
        when(promotionRepository.findByExpiredFalseAndConfirmedFalseAndExpiresAtBefore(any()))
                .thenReturn(List.of());

        scheduler.expirePromotions();

        verify(waitListService, never()).expireAndPromote(any());
    }
}
