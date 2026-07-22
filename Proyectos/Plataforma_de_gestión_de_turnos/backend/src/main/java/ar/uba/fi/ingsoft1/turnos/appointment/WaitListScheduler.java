package ar.uba.fi.ingsoft1.turnos.appointment;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.ZonedDateTime;
import java.util.List;
import static ar.uba.fi.ingsoft1.turnos.appointment.BookingBlockUtils.ZONE;

@Component
public class WaitListScheduler {

    private final WaitListPromotionRepository promotionRepository;
    private final WaitListService waitListService;

    public WaitListScheduler(
            WaitListPromotionRepository promotionRepository,
            WaitListService waitListService) {
        this.promotionRepository = promotionRepository;
        this.waitListService = waitListService;
    }

    @Scheduled(fixedRate = 100000)
    public void expirePromotions() {
        List<WaitListPromotion> toExpire = promotionRepository
                .findByExpiredFalseAndConfirmedFalseAndExpiresAtBefore(ZonedDateTime.now(ZONE));

        for (WaitListPromotion promotion : toExpire) {
            waitListService.expireAndPromote(promotion.getId());
        }
    }
}