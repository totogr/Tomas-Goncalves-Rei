package ar.uba.fi.ingsoft1.turnos.appointment;

import ar.uba.fi.ingsoft1.turnos.appointment.dto.BookingDetailResponseDTO;
import ar.uba.fi.ingsoft1.turnos.appointment.dto.BookingRequestDTO;
import ar.uba.fi.ingsoft1.turnos.appointment.dto.ClientBookingResponseDTO;
import ar.uba.fi.ingsoft1.turnos.appointment.dto.ProfessionalBookingResponseDTO;
import ar.uba.fi.ingsoft1.turnos.appointment.dto.RescheduleRequestDTO;
import ar.uba.fi.ingsoft1.turnos.blockedclient.BlockedClientService;
import ar.uba.fi.ingsoft1.turnos.client.Client;
import ar.uba.fi.ingsoft1.turnos.client.ClientRepository;
import ar.uba.fi.ingsoft1.turnos.config.email.BookingNotificationEmailService;
import ar.uba.fi.ingsoft1.turnos.professional.Professional;
import ar.uba.fi.ingsoft1.turnos.professional.ProfessionalRepository;
import ar.uba.fi.ingsoft1.turnos.review.ReviewRepository;
import ar.uba.fi.ingsoft1.turnos.schedule.ScheduleBlock;
import ar.uba.fi.ingsoft1.turnos.schedule.ScheduleBlockRepository;
import ar.uba.fi.ingsoft1.turnos.service.ServiceEntity;
import ar.uba.fi.ingsoft1.turnos.service.ServiceRepository;
import ar.uba.fi.ingsoft1.turnos.common.exception.BadRequestException;
import ar.uba.fi.ingsoft1.turnos.common.exception.ConflictException;
import ar.uba.fi.ingsoft1.turnos.common.exception.ForbiddenException;
import ar.uba.fi.ingsoft1.turnos.common.exception.ItemNotFoundException;
import ar.uba.fi.ingsoft1.turnos.common.exception.SlotTakenException;
import ar.uba.fi.ingsoft1.turnos.common.exception.UnprocessableEntityException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ar.uba.fi.ingsoft1.turnos.appointment.BookingBlockUtils.ZONE;
import static ar.uba.fi.ingsoft1.turnos.appointment.BookingBlockUtils.countBookingsInBlock;
import static ar.uba.fi.ingsoft1.turnos.appointment.BookingBlockUtils.countPromotionsInBlock;

@Service
public class BookingService {

    private final AppointmentRepository appointmentRepository;
    private final ServiceRepository serviceRepository;
    private final ProfessionalRepository professionalRepository;
    private final ClientRepository clientRepository;
    private final ReviewRepository reviewRepository;
    private final ScheduleBlockRepository scheduleBlockRepository;
    private final BlockedClientService blockedClientService;
    private final BookingNotificationEmailService notificationEmailService;
    private final ApplicationEventPublisher eventPublisher;
    private final WaitListPromotionRepository promotionRepository;

    public BookingService(
            AppointmentRepository appointmentRepository,
            ServiceRepository serviceRepository,
            ProfessionalRepository professionalRepository,
            ClientRepository clientRepository,
            ReviewRepository reviewRepository,
            ScheduleBlockRepository scheduleBlockRepository,
            BlockedClientService blockedClientService,
            BookingNotificationEmailService notificationEmailService,
            ApplicationEventPublisher eventPublisher,
            WaitListPromotionRepository promotionRepository) {
        this.appointmentRepository = appointmentRepository;
        this.serviceRepository = serviceRepository;
        this.professionalRepository = professionalRepository;
        this.clientRepository = clientRepository;
        this.reviewRepository = reviewRepository;
        this.scheduleBlockRepository = scheduleBlockRepository;
        this.blockedClientService = blockedClientService;
        this.notificationEmailService = notificationEmailService;
        this.eventPublisher = eventPublisher;
        this.promotionRepository = promotionRepository;
    }

    @Transactional
    public Appointment createBooking(BookingRequestDTO request, Long clientId) {
        if (blockedClientService.isBlocked(request.professionalId(), clientId)) {
            throw new ItemNotFoundException("Profesional no encontrado");
        }
        ServiceEntity service = serviceRepository.findById(request.serviceId())
                .orElseThrow(() -> new ItemNotFoundException("Servicio no encontrado"));
        if (!service.isActive()) {
            throw new BadRequestException("SERVICE_INACTIVE");
        }
        ZonedDateTime start = ZonedDateTime.of(request.date(), request.time(), ZONE);
        ZonedDateTime end = start.plusMinutes(service.getDuration());
        if (start.isBefore(ZonedDateTime.now(ZONE))) {
            throw new BadRequestException("SLOT_IN_PAST");
        }

        boolean alreadyBooked = appointmentRepository
                .existsByClientIdAndProfessionalIdAndServiceIdAndStartAndStatus(
                        clientId, request.professionalId(), request.serviceId(), start, "CONFIRMED");

        if (alreadyBooked) {
            throw new BadRequestException("Ya tenés una reserva confirmada para este turno.");
        }

        ZonedDateTime startOfDay = request.date().atStartOfDay(ZONE);
        ZonedDateTime endOfDay   = request.date().plusDays(1).atStartOfDay(ZONE);
        List<ScheduleBlock> dailyBlocks = scheduleBlockRepository
                .findByProfessionalIdAndBlockDateOrderByStartTimeAsc(request.professionalId(), request.date());
        if (isBlocked(request.time(), request.time().plusMinutes(service.getDuration()), dailyBlocks)) {
            throw new SlotTakenException();
        }
        List<Appointment> occupiedAppointments = appointmentRepository.findActiveAppointmentsForUpdate(
                request.professionalId(), request.serviceId(), startOfDay, endOfDay);

        List<WaitListPromotion> activePromotions = promotionRepository
                .findActivePromotionsForDay(request.professionalId(), request.serviceId(), startOfDay, endOfDay)
                .stream()
                .filter(p -> !(p.getClientId().equals(clientId) && p.getSlotStart().equals(start)))
                .toList();

        int requiredBlocks = (int) Math.ceil(service.getDuration() / 30.0);
        int maxCapacity = (service.getMaxCapacity() != null && service.getMaxCapacity() > 0)
                ? service.getMaxCapacity()
                : 1;
        LocalTime requestedTime = request.time();
        for (int i = 0; i < requiredBlocks; i++) {
            LocalTime blockTime = requestedTime.plusMinutes(i * 30L);
            long total = countBookingsInBlock(blockTime, occupiedAppointments)
                    + countPromotionsInBlock(blockTime, activePromotions, service.getDuration());
            if (total >= maxCapacity) {
                throw new SlotTakenException();
            }
        }
        Appointment appt = new Appointment();
        appt.setProfessionalId(request.professionalId());
        appt.setServiceId(request.serviceId());
        appt.setEmployeeId(request.employeeId());
        appt.setClientId(clientId);
        appt.setStart(start);
        appt.setEnd(end);
        appt.setCreatedDate(ZonedDateTime.now(ZONE));
        appt.setStatus("CONFIRMED");
        Appointment saved = appointmentRepository.save(appt);
        professionalRepository.findById(request.professionalId()).ifPresent(prof -> {
            clientRepository.findById(clientId).ifPresent(client -> {
                notificationEmailService.notifyProfessionalNewBooking(
                        prof.getEmail(),
                        prof.getFirstName() + " " + prof.getLastName(),
                        client.getFirstName() + " " + client.getLastName(),
                        client.getEmail(),
                        service.getName(),
                        saved.getStart().withZoneSameInstant(ZoneId.systemDefault()).toLocalDate(),
                        saved.getStart().withZoneSameInstant(ZoneId.systemDefault()).toLocalTime());
                notificationEmailService.notifyClientNewBooking(
                        client.getEmail(),
                        client.getFirstName() + " " + client.getLastName(),
                        service.getName(),
                        prof.getFirstName() + " " + prof.getLastName(),
                        saved.getStart().withZoneSameInstant(ZoneId.systemDefault()).toLocalDate(),
                        saved.getStart().withZoneSameInstant(ZoneId.systemDefault()).toLocalTime());
            });
        });
        return saved;
    }

    @Transactional
    public Appointment createBookingFromPromotion(BookingRequestDTO request, Long clientId) {
        if (blockedClientService.isBlocked(request.professionalId(), clientId)) {
            throw new ItemNotFoundException("Profesional no encontrado");
        }
        ServiceEntity service = serviceRepository.findById(request.serviceId())
                .orElseThrow(() -> new ItemNotFoundException("Servicio no encontrado"));
        if (!service.isActive()) {
            throw new BadRequestException("SERVICE_INACTIVE");
        }
        ZonedDateTime start = ZonedDateTime.of(request.date(), request.time(), ZONE);
        ZonedDateTime end = start.plusMinutes(service.getDuration());
        if (start.isBefore(ZonedDateTime.now(ZONE))) {
            throw new BadRequestException("SLOT_IN_PAST");
        }
        List<ScheduleBlock> dailyBlocks = scheduleBlockRepository
                .findByProfessionalIdAndBlockDateOrderByStartTimeAsc(request.professionalId(), request.date());
        if (isBlocked(request.time(), request.time().plusMinutes(service.getDuration()), dailyBlocks)) {
            throw new SlotTakenException();
        }

        Appointment appt = new Appointment();
        appt.setProfessionalId(request.professionalId());
        appt.setServiceId(request.serviceId());
        appt.setEmployeeId(request.employeeId());
        appt.setClientId(clientId);
        appt.setStart(start);
        appt.setEnd(end);
        appt.setCreatedDate(ZonedDateTime.now(ZONE));
        appt.setStatus("CONFIRMED");
        Appointment saved = appointmentRepository.save(appt);

        professionalRepository.findById(request.professionalId()).ifPresent(prof -> {
            clientRepository.findById(clientId).ifPresent(client -> {
                notificationEmailService.notifyProfessionalNewBooking(
                        prof.getEmail(),
                        prof.getFirstName() + " " + prof.getLastName(),
                        client.getFirstName() + " " + client.getLastName(),
                        client.getEmail(),
                        service.getName(),
                        saved.getStart().withZoneSameInstant(ZoneId.systemDefault()).toLocalDate(),
                        saved.getStart().withZoneSameInstant(ZoneId.systemDefault()).toLocalTime());
                notificationEmailService.notifyClientNewBooking(
                        client.getEmail(),
                        client.getFirstName() + " " + client.getLastName(),
                        service.getName(),
                        prof.getFirstName() + " " + prof.getLastName(),
                        saved.getStart().withZoneSameInstant(ZoneId.systemDefault()).toLocalDate(),
                        saved.getStart().withZoneSameInstant(ZoneId.systemDefault()).toLocalTime());
            });
        });
        return saved;
    }

    private boolean isBlocked(LocalTime startTime, LocalTime endTime, List<ScheduleBlock> blocks) {
        return blocks.stream().anyMatch(block -> startTime.isBefore(block.getEndTime())
                && endTime.isAfter(block.getStartTime()));
    }

    public List<ClientBookingResponseDTO> getClientBookings(Long clientId, String statusFilter) {
        List<Appointment> appointments = appointmentRepository.findByClientId(clientId);
        ZonedDateTime now = ZonedDateTime.now(ZONE);

        List<Appointment> filtered = appointments.stream()
                .filter(appt -> {
                    String status = appt.getStatus().toLowerCase();
                    boolean isBeforeNow = appt.getStart().isBefore(now);

                    switch (statusFilter.toLowerCase()) {
                        case "upcoming":
                            return !status.equals("cancelled") && !status.equals("completed") && !isBeforeNow;
                        case "past":
                            return status.equals("completed") || (!status.equals("cancelled") && isBeforeNow);
                        case "cancelled":
                            return status.equals("cancelled");
                        default:
                            return true;
                    }
                })
                .collect(Collectors.toList());

        Map<Long, ServiceEntity> servicesById = loadServicesById(filtered);
        Map<Long, Professional> professionalsById = loadProfessionalsById(filtered);

        return filtered.stream()
                .map(appt -> {
                    var service = servicesById.get(appt.getServiceId());
                    var prof = professionalsById.get(appt.getProfessionalId());

                    String serviceName = service != null ? service.getName() : "Servicio no especificado";
                    Integer duration = service != null ? service.getDuration() : 0;
                    String profName = prof != null ? (prof.getFirstName() + " " + prof.getLastName()) : "Profesional";

                    String location = "No especificada";
                    if (prof != null) {
                        String built = Stream.of(prof.getNeighborhood(), prof.getCity())
                                .filter(s -> s != null && !s.isBlank())
                                .collect(Collectors.joining(", "));
                        if (!built.isBlank()) location = built;
                    }

                    String formattedTime = appt.getStart().withZoneSameInstant(ZONE).toLocalTime()
                            .toString().substring(0, 5);

                    return new ClientBookingResponseDTO(
                            appt.getId(),
                            appt.getStatus().toUpperCase(),
                            appt.getCancelledBy(),
                            serviceName,
                            profName,
                            appt.getStart().toLocalDate(),
                            formattedTime,
                            duration,
                            location);
                })
                .collect(Collectors.toList());
    }

    public BookingDetailResponseDTO getBookingDetail(Long appointmentId, Long clientId) {
        Appointment appt = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ItemNotFoundException("Turno no encontrado"));

        if (!appt.getClientId().equals(clientId)) {
            throw new ForbiddenException("El turno no pertenece al cliente autenticado");
        }

        var service = serviceRepository.findById(appt.getServiceId()).orElse(null);
        var prof = professionalRepository.findById(appt.getProfessionalId()).orElse(null);

        String serviceName = service != null ? service.getName() : "Servicio no especificado";
        Integer duration = service != null ? service.getDuration() : 0;
        BigDecimal price = service != null ? service.getPrice() : BigDecimal.ZERO;

        String firstName = prof != null ? prof.getFirstName() : "Profesional";
        String lastName = prof != null ? prof.getLastName() : "";

        String start = appt.getStart().withZoneSameInstant(ZONE).toLocalDateTime().toString();
        String end = appt.getEnd().withZoneSameInstant(ZONE).toLocalDateTime().toString();

        Integer score = reviewRepository.findByAppointmentId(appointmentId)
                .map(r -> r.getScore())
                .orElse(null);

        return new BookingDetailResponseDTO(
                appt.getId(),
                appt.getStatus().toUpperCase(),
                appt.getCancelledBy(),
                new BookingDetailResponseDTO.ServiceDetailDTO(appt.getServiceId(), serviceName, duration, price),
                new BookingDetailResponseDTO.ProfessionalDetailDTO(appt.getProfessionalId(), firstName, lastName),
                appt.getStart().withZoneSameInstant(ZONE).toLocalDate(),
                start,
                end,
                score);
    }

    @Transactional
    public void cancelBooking(Long appointmentId, Long clientId) {
        Appointment appt = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ItemNotFoundException("Turno no encontrado"));

        if (!appt.getClientId().equals(clientId)) {
            throw new ForbiddenException("No tenés permiso para cancelar este turno");
        }
        if (appt.getStart().isBefore(ZonedDateTime.now(ZONE))) {
            throw new BadRequestException("No se puede cancelar un turno que ya pasó");
        }

        appt.setStatus("CANCELLED");
        appt.setCancelledBy("client");
        appt.setCancelledDate(ZonedDateTime.now(ZONE));
        appointmentRepository.save(appt);

        ServiceEntity service = serviceRepository.findById(appt.getServiceId())
                .orElseThrow(() -> new ItemNotFoundException("Servicio no encontrado"));

        int slotInterval = professionalRepository.findById(appt.getProfessionalId())
                .map(p -> p.getSlotIntervalMinutes() != null ? p.getSlotIntervalMinutes() : 30)
                .orElse(30);

        eventPublisher.publishEvent(new AppointmentCancelledEvent(
                appt.getProfessionalId(),
                appt.getServiceId(),
                appt.getStart(),
                service.getDuration(),
                slotInterval));

        professionalRepository.findById(appt.getProfessionalId()).ifPresent(prof -> {
            clientRepository.findById(clientId).ifPresent(client -> {
                serviceRepository.findById(appt.getServiceId()).ifPresent(svc -> {
                    notificationEmailService.notifyProfessionalCancellation(
                            prof.getEmail(),
                            prof.getFirstName() + " " + prof.getLastName(),
                            client.getFirstName() + " " + client.getLastName(),
                            svc.getName(),
                            appt.getStart().toLocalDate(),
                            appt.getStart().toLocalTime());
                });
            });
        });
    }


    public List<ProfessionalBookingResponseDTO> getProfessionalBookings(Long profId, String statusFilter) {
        List<Appointment> appointments = appointmentRepository.findByProfessionalId(profId);
        ZonedDateTime now = ZonedDateTime.now(ZONE);

        List<Appointment> filtered = appointments.stream()
                .filter(appt -> {
                    String status = appt.getStatus().toLowerCase();
                    boolean isBeforeNow = appt.getStart().isBefore(now);

                    return switch (statusFilter.toLowerCase()) {
                        case "upcoming" -> !status.equals("cancelled") && !status.equals("completed") && !isBeforeNow;
                        case "past" -> status.equals("completed") || (!status.equals("cancelled") && isBeforeNow);
                        case "cancelled" -> status.equals("cancelled");
                        default -> true;
                    };
                })
                .collect(Collectors.toList());

        Map<Long, ServiceEntity> servicesById = loadServicesById(filtered);
        Map<Long, Client> clientsById = clientRepository.findAllById(
                        filtered.stream().map(Appointment::getClientId).collect(Collectors.toSet()))
                .stream().collect(Collectors.toMap(Client::getId, Function.identity()));

        return filtered.stream()
                .map(appt -> {
                    var service = servicesById.get(appt.getServiceId());
                    var client = clientsById.get(appt.getClientId());

                    String serviceName = service != null ? service.getName() : "Servicio no especificado";
                    int duration = (int) java.time.Duration.between(appt.getStart(), appt.getEnd()).toMinutes();

                    String clientName = client != null
                            ? (client.getFirstName() + " " + client.getLastName())
                            : "Cliente";
                    String clientEmail = client != null ? client.getEmail() : "";

                    String formattedStart = appt.getStart().withZoneSameInstant(ZONE).toLocalTime()
                            .toString().substring(0, 5);
                    String formattedEnd = appt.getEnd().withZoneSameInstant(ZONE).toLocalTime()
                            .toString().substring(0, 5);

                    return new ProfessionalBookingResponseDTO(
                            appt.getId(),
                            appt.getStatus().toUpperCase(),
                            appt.getCancelledBy(),
                            serviceName,
                            clientName,
                            clientEmail,
                            appt.getStart().toLocalDate(),
                            formattedStart,
                            formattedEnd,
                            duration,
                            appt.getMarkedAbsentAt());
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void cancelBookingByProfessional(Long appointmentId, Long profId) {
        Appointment appt = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ItemNotFoundException("Turno no encontrado"));

        if (!appt.getProfessionalId().equals(profId)) {
            throw new ForbiddenException("El turno no pertenece a este profesional");
        }
        if (appt.getStart().isBefore(ZonedDateTime.now(ZONE))) {
            throw new BadRequestException("No se puede cancelar un turno que ya pasó");
        }

        appt.setStatus("CANCELLED");
        appt.setCancelledBy("professional");
        appt.setCancelledDate(ZonedDateTime.now(ZONE));
        appointmentRepository.save(appt);

        ServiceEntity service = serviceRepository.findById(appt.getServiceId())
                .orElseThrow(() -> new ItemNotFoundException("Servicio no encontrado"));

        int slotInterval = professionalRepository.findById(appt.getProfessionalId())
                .map(p -> p.getSlotIntervalMinutes() != null ? p.getSlotIntervalMinutes() : 30)
                .orElse(30);

        eventPublisher.publishEvent(new AppointmentCancelledEvent(
                appt.getProfessionalId(),
                appt.getServiceId(),
                appt.getStart(),
                service.getDuration(),
                slotInterval));

        professionalRepository.findById(profId).ifPresent(prof -> {
            clientRepository.findById(appt.getClientId()).ifPresent(client -> {
                serviceRepository.findById(appt.getServiceId()).ifPresent(svc -> {
                    notificationEmailService.notifyClientCancelledByProfessional(
                            client.getEmail(),
                            client.getFirstName() + " " + client.getLastName(),
                            svc.getName(),
                            prof.getFirstName() + " " + prof.getLastName(),
                            appt.getStart().withZoneSameInstant(ZoneId.systemDefault()).toLocalDate(),
                            appt.getStart().withZoneSameInstant(ZoneId.systemDefault()).toLocalTime());
                });
            });
        });
    }


    @Transactional
    public void markAbsent(Long appointmentId, Long profId) {
        Appointment appt = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ItemNotFoundException("Turno no encontrado"));

        if (!appt.getProfessionalId().equals(profId)) {
            throw new ForbiddenException("El turno no pertenece a este profesional");
        }

        if (appt.getStart().isAfter(ZonedDateTime.now(ZONE))) {
            throw new BadRequestException("No se puede registrar ausencia de un turno que aún no ocurrió");
        }

        if (appt.getMarkedAbsentAt() != null) {
            throw new BadRequestException("Este turno ya fue marcado como ausente");
        }

        appt.setMarkedAbsentAt(ZonedDateTime.now(ZONE));
        appointmentRepository.save(appt);

        Client client = clientRepository.findById(appt.getClientId())
                .orElseThrow(() -> new ItemNotFoundException("Cliente no encontrado"));

        client.setAbsenceCount(client.getAbsenceCount() + 1);
        clientRepository.save(client);
    }
    @Transactional
    public void rescheduleBooking(Long appointmentId, Long clientId, RescheduleRequestDTO request) {
        Appointment appt = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ItemNotFoundException("Turno no encontrado"));

        if (!appt.getClientId().equals(clientId)) {
            throw new ForbiddenException("No tenés permiso para reprogramar este turno");
        }
        if (appt.getStatus().equalsIgnoreCase("CANCELLED")) {
            throw new UnprocessableEntityException("No se puede reprogramar un turno cancelado");
        }
        if (appt.getStart().isBefore(ZonedDateTime.now(ZONE))) {
            throw new UnprocessableEntityException("No se puede reprogramar un turno que ya pasó");
        }

        ZonedDateTime newStart = ZonedDateTime.of(request.date(), request.time(), ZONE);

        if (newStart.isBefore(ZonedDateTime.now(ZONE))) {
            throw new UnprocessableEntityException("La nueva fecha debe ser futura");
        }

        ServiceEntity service = serviceRepository.findById(appt.getServiceId())
                .orElseThrow(() -> new ItemNotFoundException("Servicio no encontrado"));

        ZonedDateTime startOfDay = request.date().atStartOfDay(ZONE);
        ZonedDateTime endOfDay   = request.date().plusDays(1).atStartOfDay(ZONE);

        List<Appointment> occupiedAppointments = appointmentRepository
                .findActiveAppointmentsForUpdate(appt.getProfessionalId(), appt.getServiceId(), startOfDay, endOfDay)
                .stream()
                .filter(a -> !a.getId().equals(appointmentId))
                .collect(Collectors.toList());

        int requiredBlocks = (int) Math.ceil(service.getDuration() / 30.0);
        int maxCapacity = (service.getMaxCapacity() != null && service.getMaxCapacity() > 0)
                ? service.getMaxCapacity() : 1;

        for (int i = 0; i < requiredBlocks; i++) {
            LocalTime blockTime = request.time().plusMinutes(i * 30L);
            long bookingsInBlock = countBookingsInBlock(blockTime, occupiedAppointments);
            if (bookingsInBlock >= maxCapacity) {
                throw new ConflictException("SLOT_FULL");
            }
        }

        ZonedDateTime oldStart = appt.getStart();
        appt.setStart(newStart);
        appt.setEnd(newStart.plusMinutes(service.getDuration()));
        appointmentRepository.save(appt);

        professionalRepository.findById(appt.getProfessionalId()).ifPresent(prof -> {
            clientRepository.findById(clientId).ifPresent(client -> {
                notificationEmailService.notifyProfessionalReschedule(
                        prof.getEmail(),
                        prof.getFirstName() + " " + prof.getLastName(),
                        client.getFirstName() + " " + client.getLastName(),
                        service.getName(),
                        oldStart.withZoneSameInstant(ZoneId.systemDefault()).toLocalDate(),
                        oldStart.withZoneSameInstant(ZoneId.systemDefault()).toLocalTime(),
                        newStart.withZoneSameInstant(ZoneId.systemDefault()).toLocalDate(),
                        newStart.withZoneSameInstant(ZoneId.systemDefault()).toLocalTime());

                notificationEmailService.notifyClientReschedule(
                        client.getEmail(),
                        client.getFirstName() + " " + client.getLastName(),
                        service.getName(),
                        prof.getFirstName() + " " + prof.getLastName(),
                        newStart.withZoneSameInstant(ZoneId.systemDefault()).toLocalDate(),
                        newStart.withZoneSameInstant(ZoneId.systemDefault()).toLocalTime());
            });
        });
    }

    private Map<Long, ServiceEntity> loadServicesById(List<Appointment> appointments) {
        return serviceRepository.findAllById(
                        appointments.stream().map(Appointment::getServiceId).collect(Collectors.toSet()))
                .stream().collect(Collectors.toMap(ServiceEntity::getId, Function.identity()));
    }

    private Map<Long, Professional> loadProfessionalsById(List<Appointment> appointments) {
        return professionalRepository.findAllById(
                        appointments.stream().map(Appointment::getProfessionalId).collect(Collectors.toSet()))
                .stream().collect(Collectors.toMap(Professional::getId, Function.identity()));
    }
}
