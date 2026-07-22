package ar.uba.fi.ingsoft1.turnos.appointment;

import ar.uba.fi.ingsoft1.turnos.common.exception.ItemNotFoundException;
import ar.uba.fi.ingsoft1.turnos.service.ServiceEntity;
import ar.uba.fi.ingsoft1.turnos.service.ServiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.List;

import static ar.uba.fi.ingsoft1.turnos.appointment.BookingBlockUtils.ZONE;
import static ar.uba.fi.ingsoft1.turnos.appointment.BookingBlockUtils.countBookingsInBlock;
import static ar.uba.fi.ingsoft1.turnos.appointment.BookingBlockUtils.countPromotionsInBlock;

@Service
@Transactional(readOnly = true)
public class SlotAvailabilityService {

    private final AppointmentRepository appointmentRepository;
    private final ServiceRepository serviceRepository;
    private final WaitListPromotionRepository promotionRepository;

    public SlotAvailabilityService(AppointmentRepository appointmentRepository,
                                   ServiceRepository serviceRepository,
                                   WaitListPromotionRepository promotionRepository) {
        this.appointmentRepository = appointmentRepository;
        this.serviceRepository = serviceRepository;
        this.promotionRepository = promotionRepository;
    }

    public boolean isSlotFullyBooked(Long professionalId, Long serviceId, ZonedDateTime start) {
        ServiceEntity service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ItemNotFoundException("Servicio no encontrado"));

        int maxCapacity = (service.getMaxCapacity() != null && service.getMaxCapacity() > 0)
                ? service.getMaxCapacity()
                : 1;

        int requiredBlocks = (int) Math.ceil(service.getDuration() / 30.0);

        ZonedDateTime startOfDay = start.toLocalDate().atStartOfDay(ZONE);
        ZonedDateTime endOfDay = start.toLocalDate().plusDays(1).atStartOfDay(ZONE);

        List<Appointment> occupied = appointmentRepository.findActiveAppointmentsForUpdate(
                professionalId, serviceId, startOfDay, endOfDay);

        List<WaitListPromotion> activePromotions = promotionRepository
                .findActivePromotionsForDay(professionalId, serviceId, startOfDay, endOfDay);

        LocalTime requestedTime = start.withZoneSameInstant(ZONE).toLocalTime();

        for (int i = 0; i < requiredBlocks; i++) {
            LocalTime blockTime = requestedTime.plusMinutes(i * 30L);
            long total = countBookingsInBlock(blockTime, occupied)
                    + countPromotionsInBlock(blockTime, activePromotions, service.getDuration());
            if (total >= maxCapacity) {
                return true;
            }
        }

        return false;
    }

    public boolean isClientBooked(Long clientId, Long professionalId, Long serviceId, ZonedDateTime slotStart) {
        return appointmentRepository.existsByClientIdAndProfessionalIdAndServiceIdAndStartAndStatus(
                clientId, professionalId, serviceId, slotStart, "CONFIRMED");
    }

    public boolean isWindowAvailable(Long professionalId, Long serviceId,
                                     ZonedDateTime windowStart, int durationMinutes) {
        ZonedDateTime startOfDay = windowStart.toLocalDate().atStartOfDay(ZONE);
        ZonedDateTime endOfDay = windowStart.toLocalDate().plusDays(1).atStartOfDay(ZONE);

        List<Appointment> occupied = appointmentRepository.findActiveAppointmentsForUpdate(
                professionalId, serviceId, startOfDay, endOfDay);

        System.out.println("=== isWindowAvailable ===");
        System.out.println("windowStart: " + windowStart);
        System.out.println("durationMinutes: " + durationMinutes);
        System.out.println("occupied count: " + occupied.size());
        occupied.forEach(a -> System.out.println("  appt: " + a.getStart() + " -> " + a.getEnd()));


        List<WaitListPromotion> activePromotions = promotionRepository
                .findActivePromotionsForDay(professionalId, serviceId, startOfDay, endOfDay);

        ServiceEntity service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ItemNotFoundException("Servicio no encontrado"));

        int maxCapacity = (service.getMaxCapacity() != null && service.getMaxCapacity() > 0)
                ? service.getMaxCapacity() : 1;

        int requiredBlocks = (int) Math.ceil(durationMinutes / 30.0);
        LocalTime requestedTime = windowStart.withZoneSameInstant(ZONE).toLocalTime();

        for (int i = 0; i < requiredBlocks; i++) {
            LocalTime blockTime = requestedTime.plusMinutes(i * 30L);
            long total = countBookingsInBlock(blockTime, occupied)
                    + countPromotionsInBlock(blockTime, activePromotions, service.getDuration());
            if (total >= maxCapacity) {
                return false;
            }
        }
        return true;
    }

    public boolean hasClientOverlappingBooking(Long clientId, ZonedDateTime newStart, int newDurationMinutes) {
        ZonedDateTime newEnd = newStart.plusMinutes(newDurationMinutes);
        ZonedDateTime startOfDay = newStart.toLocalDate().atStartOfDay(ZONE);
        ZonedDateTime endOfDay = newStart.toLocalDate().plusDays(1).atStartOfDay(ZONE);

        return appointmentRepository.findConfirmedByClientIdAndDay(clientId, startOfDay, endOfDay)
                .stream()
                .anyMatch(a -> newStart.isBefore(a.getEnd()) && newEnd.isAfter(a.getStart()));
    }

    public boolean isSlotCompletelyEmpty(Long professionalId, Long serviceId,
                                         ZonedDateTime windowStart, int durationMinutes) {
        ZonedDateTime startOfDay = windowStart.toLocalDate().atStartOfDay(ZONE);
        ZonedDateTime endOfDay = windowStart.toLocalDate().plusDays(1).atStartOfDay(ZONE);

        List<Appointment> occupied = appointmentRepository.findActiveAppointmentsForUpdate(
                professionalId, serviceId, startOfDay, endOfDay);

        List<WaitListPromotion> activePromotions = promotionRepository
                .findActivePromotionsForDay(professionalId, serviceId, startOfDay, endOfDay);

        ServiceEntity service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ItemNotFoundException("Servicio no encontrado"));

        int requiredBlocks = (int) Math.ceil(durationMinutes / 30.0);
        LocalTime requestedTime = windowStart.withZoneSameInstant(ZONE).toLocalTime();

        for (int i = 0; i < requiredBlocks; i++) {
            LocalTime blockTime = requestedTime.plusMinutes(i * 30L);
            long total = countBookingsInBlock(blockTime, occupied)
                    + countPromotionsInBlock(blockTime, activePromotions, service.getDuration());
            if (total > 0) {
                return false;
            }
        }
        return true;
    }

    public boolean hasOverlappingAppointments(Long professionalId, Long serviceId,
                                              ZonedDateTime windowStart, int durationMinutes) {
        ZonedDateTime windowEnd = windowStart.plusMinutes(durationMinutes);
        ZonedDateTime startOfDay = windowStart.toLocalDate().atStartOfDay(ZONE);
        ZonedDateTime endOfDay = windowStart.toLocalDate().plusDays(1).atStartOfDay(ZONE);

        List<Appointment> occupied = appointmentRepository.findActiveAppointmentsForUpdate(
                professionalId, serviceId, startOfDay, endOfDay);

        return occupied.stream().anyMatch(appt ->
                appt.getStart().isBefore(windowEnd) && appt.getEnd().isAfter(windowStart)
        );
    }

}