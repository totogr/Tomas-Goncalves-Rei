package ar.uba.fi.ingsoft1.turnos.appointment;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ar.uba.fi.ingsoft1.turnos.appointment.dto.AvailabilityResponseDTO;
import ar.uba.fi.ingsoft1.turnos.appointment.dto.SlotDTO;
import static ar.uba.fi.ingsoft1.turnos.appointment.BookingBlockUtils.countPromotionsInBlock;
import ar.uba.fi.ingsoft1.turnos.common.exception.ItemNotFoundException;
import ar.uba.fi.ingsoft1.turnos.professional.ProfessionalRepository;
import ar.uba.fi.ingsoft1.turnos.schedule.Schedule;
import ar.uba.fi.ingsoft1.turnos.schedule.ScheduleRepository;
import ar.uba.fi.ingsoft1.turnos.schedule.ScheduleBlock;
import ar.uba.fi.ingsoft1.turnos.schedule.ScheduleBlockRepository;
import ar.uba.fi.ingsoft1.turnos.service.ServiceEntity;
import ar.uba.fi.ingsoft1.turnos.service.ServiceRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import static ar.uba.fi.ingsoft1.turnos.appointment.BookingBlockUtils.ZONE;
import static ar.uba.fi.ingsoft1.turnos.appointment.BookingBlockUtils.countBookingsInBlock;

@Service
@Transactional(readOnly = true)
public class AvailabilityService {

    private final ScheduleRepository scheduleRepository;
    private final ScheduleBlockRepository scheduleBlockRepository;
    private final AppointmentRepository appointmentRepository;
    private final ServiceRepository serviceRepository;
    private final ProfessionalRepository professionalRepository;
    private final WaitListPromotionRepository promotionRepository;

    public AvailabilityService(ScheduleRepository scheduleRepository,
                               ScheduleBlockRepository scheduleBlockRepository,
                               AppointmentRepository appointmentRepository,
                               ServiceRepository serviceRepository,
                               ProfessionalRepository professionalRepository,
                               WaitListPromotionRepository promotionRepository) {
        this.scheduleRepository = scheduleRepository;
        this.scheduleBlockRepository = scheduleBlockRepository;
        this.appointmentRepository = appointmentRepository;
        this.serviceRepository = serviceRepository;
        this.professionalRepository = professionalRepository;
        this.promotionRepository = promotionRepository;
    }
    public AvailabilityResponseDTO getAvailability(Long profId, LocalDate date, Long serviceId, Long employeeId) {
        int dayOfWeek = date.getDayOfWeek().getValue();
        List<Schedule> dailySchedules = scheduleRepository.findByProfessionalIdAndDayWeek(profId, dayOfWeek);
        List<ScheduleBlock> dailyBlocks = scheduleBlockRepository.findByProfessionalIdAndBlockDateOrderByStartTimeAsc(profId, date);
        List<SlotDTO> slots = new ArrayList<>();

        if (dailySchedules.isEmpty()) {
            return new AvailabilityResponseDTO(date, slots);
        }

        ServiceEntity service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ItemNotFoundException("servicio", serviceId));

        if (!service.isActive()) {
            throw new IllegalArgumentException("El servicio no está disponible");
        }

        int requiredBlocks = (int) Math.ceil(service.getDuration() / 30.0);
        int maxCapacity = (service.getMaxCapacity() != null && service.getMaxCapacity() > 0)
                ? service.getMaxCapacity()
                : 1;

        int slotInterval = professionalRepository.findById(profId)
                .map(p -> p.getSlotIntervalMinutes() != null ? p.getSlotIntervalMinutes() : 30)
                .orElse(30);

        ZonedDateTime startOfDay = date.atStartOfDay(ZONE);
        ZonedDateTime endOfDay = date.plusDays(1).atStartOfDay(ZONE);
        List<Appointment> occupiedAppointments = appointmentRepository.findActiveAppointments(profId, startOfDay, endOfDay);

        List<WaitListPromotion> activePromotions = promotionRepository
                .findActivePromotionsForDay(profId, serviceId, startOfDay, endOfDay);

        final List<LocalTime> groupStarts;
        if (maxCapacity > 1) {
            groupStarts = occupiedAppointments.stream()
                    .filter(a -> a.getServiceId().equals(serviceId))
                    .map(a -> a.getStart().withZoneSameInstant(ZONE).toLocalTime())
                    .distinct()
                    .toList();
        } else {
            groupStarts = List.of();
        }

        LocalDate today = LocalDate.now(ZONE);
        LocalTime nowTime = LocalTime.now(ZONE);

        for (Schedule schedule : dailySchedules) {
            LocalTime currentTime = schedule.getStart();
            LocalTime endTime = schedule.getEnd();

            while (currentTime.isBefore(endTime)) {
                if (date.equals(today) && !currentTime.isAfter(nowTime)) {
                    slots.add(new SlotDTO(currentTime, false, null, "past"));
                    currentTime = currentTime.plusMinutes(slotInterval);
                    continue;
                }

                if (isBlocked(currentTime, currentTime.plusMinutes(service.getDuration()), dailyBlocks)) {
                    slots.add(new SlotDTO(currentTime, false, null, "blocked"));
                    currentTime = currentTime.plusMinutes(slotInterval);
                    continue;
                }

                final LocalTime capturedTime = currentTime;
                final LocalTime slotEnd = currentTime.plusMinutes(service.getDuration());

                // bloquear solapamiento con otros servicios (aplica siempre)
                boolean overlapsOtherService = occupiedAppointments.stream()
                        .filter(a -> !a.getServiceId().equals(serviceId))
                        .anyMatch(a -> {
                            LocalTime aStart = a.getStart().withZoneSameInstant(ZONE).toLocalTime();
                            LocalTime aEnd = a.getEnd().withZoneSameInstant(ZONE).toLocalTime();
                            return capturedTime.isBefore(aEnd) && slotEnd.isAfter(aStart);
                        });

                if (overlapsOtherService) {
                    slots.add(new SlotDTO(currentTime, false, null, "full"));
                    currentTime = currentTime.plusMinutes(slotInterval);
                    continue;
                }

                // para grupales: bloquear solapamiento con distinto slot del mismo servicio
                if (!groupStarts.isEmpty()) {
                    boolean exactMatch = groupStarts.contains(currentTime);
                    boolean overlapsSameService = occupiedAppointments.stream()
                            .filter(a -> a.getServiceId().equals(serviceId))
                            .anyMatch(a -> {
                                LocalTime aStart = a.getStart().withZoneSameInstant(ZONE).toLocalTime();
                                LocalTime aEnd = a.getEnd().withZoneSameInstant(ZONE).toLocalTime();
                                return capturedTime.isBefore(aEnd) && slotEnd.isAfter(aStart);
                            });

                    if (overlapsSameService && !exactMatch) {
                        slots.add(new SlotDTO(currentTime, false, null, "full"));
                        currentTime = currentTime.plusMinutes(slotInterval);
                        continue;
                    }
                }

                boolean hasEnoughSpace = true;
                LocalTime checkTime = currentTime;

                for (int i = 0; i < requiredBlocks; i++) {
                    if (checkTime.equals(endTime) || checkTime.isAfter(endTime)) {
                        hasEnoughSpace = false;
                        break;
                    }
                    long bookingsInBlock = countBookingsInBlock(checkTime, occupiedAppointments)
                            + countPromotionsInBlock(checkTime, activePromotions, service.getDuration());
                    if (bookingsInBlock >= maxCapacity) {
                        hasEnoughSpace = false;
                        break;
                    }
                    checkTime = checkTime.plusMinutes(30);
                }

                if (hasEnoughSpace) {
                    slots.add(new SlotDTO(currentTime, true, employeeId, null));
                } else {
                    slots.add(new SlotDTO(currentTime, false, null, "full"));
                }

                currentTime = currentTime.plusMinutes(slotInterval);
            }
        }

        return new AvailabilityResponseDTO(date, slots);
    }

    private boolean isBlocked(LocalTime startTime, LocalTime endTime, List<ScheduleBlock> blocks) {
        return blocks.stream().anyMatch(block -> startTime.isBefore(block.getEndTime())
                && endTime.isAfter(block.getStartTime()));
    }
}