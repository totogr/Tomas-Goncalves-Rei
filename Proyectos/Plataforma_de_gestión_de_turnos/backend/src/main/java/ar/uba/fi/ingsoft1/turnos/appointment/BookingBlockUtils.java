package ar.uba.fi.ingsoft1.turnos.appointment;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

final class BookingBlockUtils {

    static final ZoneId ZONE = ZoneId.of("America/Argentina/Buenos_Aires");

    private BookingBlockUtils() {}

    static long countBookingsInBlock(LocalTime blockTime, List<Appointment> appointments) {
        return appointments.stream()
                .filter(appt -> {
                    LocalTime apptStart = appt.getStart().withZoneSameInstant(ZONE).toLocalTime();
                    LocalTime apptEnd   = appt.getEnd().withZoneSameInstant(ZONE).toLocalTime();
                    return (blockTime.equals(apptStart) || blockTime.isAfter(apptStart))
                            && blockTime.isBefore(apptEnd);
                })
                .count();
    }

    static long countPromotionsInBlock(LocalTime blockTime, List<WaitListPromotion> promotions, int serviceDuration) {
        return promotions.stream()
                .filter(p -> {
                    LocalTime promoStart = p.getSlotStart().withZoneSameInstant(ZONE).toLocalTime();
                    LocalTime promoEnd   = promoStart.plusMinutes(serviceDuration);
                    return (blockTime.equals(promoStart) || blockTime.isAfter(promoStart))
                            && blockTime.isBefore(promoEnd);
                })
                .count();
    }
}