package ar.uba.fi.ingsoft1.turnos.appointment;

import ar.uba.fi.ingsoft1.turnos.client.ClientRepository;
import ar.uba.fi.ingsoft1.turnos.service.ServiceEntity;
import ar.uba.fi.ingsoft1.turnos.config.email.EmailService;
import ar.uba.fi.ingsoft1.turnos.appointment.dto.BookingRequestDTO;
import ar.uba.fi.ingsoft1.turnos.appointment.dto.WaitListEntryDTO;
import ar.uba.fi.ingsoft1.turnos.appointment.dto.ActivePromotionDTO;
import ar.uba.fi.ingsoft1.turnos.common.exception.BadRequestException;
import ar.uba.fi.ingsoft1.turnos.common.exception.ItemNotFoundException;
import ar.uba.fi.ingsoft1.turnos.service.ServiceRepository;
import ar.uba.fi.ingsoft1.turnos.professional.Professional;
import ar.uba.fi.ingsoft1.turnos.professional.ProfessionalRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ar.uba.fi.ingsoft1.turnos.appointment.BookingBlockUtils.ZONE;

@Service
public class WaitListService {

    private final WaitListRepository waitListRepository;
    private final BookingService bookingService;
    private final SlotAvailabilityService slotAvailabilityService;
    private final ServiceRepository serviceRepository;
    private final ProfessionalRepository professionalRepository;
    private final WaitListPromotionRepository promotionRepository;
    private final ClientRepository clientRepository;
    private final EmailService emailService;

    public WaitListService(
            WaitListRepository waitListRepository,
            BookingService bookingService,
            SlotAvailabilityService slotAvailabilityService,
            ServiceRepository serviceRepository,
            ProfessionalRepository professionalRepository,
            WaitListPromotionRepository promotionRepository,
            ClientRepository clientRepository,
            EmailService emailService) {

        this.waitListRepository = waitListRepository;
        this.bookingService = bookingService;
        this.slotAvailabilityService = slotAvailabilityService;
        this.serviceRepository = serviceRepository;
        this.professionalRepository = professionalRepository;
        this.promotionRepository = promotionRepository;
        this.clientRepository = clientRepository;
        this.emailService = emailService;
    }

    @Transactional
    public WaitListEntryDTO joinWaitList(Long professionalId, Long serviceId, ZonedDateTime slotStart, Long clientId) {

        ServiceEntity service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ItemNotFoundException("Servicio no encontrado"));

        if (slotAvailabilityService.hasClientOverlappingBooking(clientId, slotStart, service.getDuration())) {
            throw new BadRequestException("Ya tenés una reserva que se superpone con este horario.");
        }

        boolean slotFull = slotAvailabilityService.isSlotFullyBooked(professionalId, serviceId, slotStart);
        boolean hasOverlap = slotAvailabilityService.hasOverlappingAppointments(professionalId, serviceId, slotStart, service.getDuration());

        if (!slotFull && !hasOverlap) {
            throw new BadRequestException("El turno tiene lugares disponibles. Reserva directamente.");
        }

        if (waitListRepository.existsByClientIdAndProfessionalIdAndServiceIdAndSlotStart(clientId, professionalId, serviceId, slotStart)) {
            throw new BadRequestException("El cliente ya esta en la lista de espera para este turno.");
        }

        WaitListEntry entry = new WaitListEntry();
        entry.setClientId(clientId);
        entry.setProfessionalId(professionalId);
        entry.setServiceId(serviceId);
        entry.setSlotStart(slotStart);
        entry.setCreationTime(ZonedDateTime.now(ZONE));

        WaitListEntry saved = waitListRepository.save(entry);
        int position = getPositionInQueue(professionalId, serviceId, slotStart, saved.getId());
        return toDTO(saved, position);
    }
    @Transactional
    public void leaveWaitList(Long professionalId, Long serviceId, ZonedDateTime slotStart, Long clientId) {
        List<WaitListEntry> entries = waitListRepository
                .findByProfessionalIdAndServiceIdAndSlotStartOrderByCreationTimeAsc(professionalId, serviceId, slotStart);

        WaitListEntry entry = entries.stream()
                .filter(e -> e.getClientId().equals(clientId))
                .findFirst()
                .orElseThrow(() -> new ItemNotFoundException("El cliente no se encuentra en la lista de espera de este turno."));

        waitListRepository.delete(entry);
    }

    @Transactional(readOnly = true)
    public List<WaitListEntryDTO> getWaitList(Long professionalId, Long serviceId, ZonedDateTime slotStart) {
        List<WaitListEntry> entries = waitListRepository
                .findByProfessionalIdAndServiceIdAndSlotStartOrderByCreationTimeAsc(professionalId, serviceId, slotStart);

        int[] position = {1};
        return entries.stream()
                .map(e -> toDTO(e, position[0]++))
                .toList();
    }

    @Transactional(readOnly = true)
    public WaitListEntryDTO getClientWaitListEntry(Long professionalId, Long serviceId, ZonedDateTime slotStart, Long clientId) {
        List<WaitListEntry> entries = waitListRepository
                .findByProfessionalIdAndServiceIdAndSlotStartOrderByCreationTimeAsc(professionalId, serviceId, slotStart);

        int position = 1;
        for (WaitListEntry entry : entries) {
            if (entry.getClientId().equals(clientId)) {
                return toDTO(entry, position);
            }
            position++;
        }

        throw new ItemNotFoundException("El cliente no esta en la lista de espera para este turno.");
    }

    @Transactional(readOnly = true)
    public List<WaitListEntryDTO> getClientWaitListEntries(Long clientId) {
        List<WaitListEntry> entries = waitListRepository.findByClientId(clientId);

        Map<Long, ServiceEntity> servicesById = serviceRepository.findAllById(
                        entries.stream().map(WaitListEntry::getServiceId).collect(Collectors.toSet()))
                .stream().collect(Collectors.toMap(ServiceEntity::getId, Function.identity()));
        Map<Long, Professional> professionalsById = professionalRepository.findAllById(
                        entries.stream().map(WaitListEntry::getProfessionalId).collect(Collectors.toSet()))
                .stream().collect(Collectors.toMap(Professional::getId, Function.identity()));

        return entries.stream()
                .map(entry -> {
                    int position = getPositionInQueue(
                            entry.getProfessionalId(),
                            entry.getServiceId(),
                            entry.getSlotStart(),
                            entry.getId()
                    );
                    return buildDTO(entry, position,
                            servicesById.get(entry.getServiceId()),
                            professionalsById.get(entry.getProfessionalId()));
                })
                .toList();
    }

    @Transactional
    public void expireAndPromote(Long promotionId) {
        WaitListPromotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new ItemNotFoundException("Promoción no encontrada"));

        // limpiar cualquier otra fila vieja que ya tenga (confirmed=false, expired=true)
        promotionRepository.deleteByClientIdAndProfessionalIdAndServiceIdAndSlotStartAndConfirmedAndExpiredAndIdNot(
                promotion.getClientId(), promotion.getProfessionalId(), promotion.getServiceId(), promotion.getSlotStart(),
                false, true, promotion.getId());
        promotionRepository.flush();

        promotion.setExpired(true);
        promotionRepository.saveAndFlush(promotion);

        // el candidato pierde su lugar en la cola por no confirmar a tiempo
        waitListRepository.findById(promotion.getWaitListEntryId())
                .ifPresent(waitListRepository::delete);

        clientRepository.findById(promotion.getClientId()).ifPresent(client ->
                emailService.sendWaitListExpiredEmail(
                        client.getEmail(),
                        client.getFirstName(),
                        promotion.getProfessionalId(),
                        promotion.getServiceId(),
                        promotion.getSlotStart()
                )
        );


        promoteFirstInQueue(
                promotion.getProfessionalId(),
                promotion.getServiceId(),
                promotion.getSlotStart()
        );
    }

    @Transactional
    public void promoteFirstInQueue(Long professionalId, Long serviceId, ZonedDateTime slotStart) {
        List<WaitListEntry> queue = waitListRepository
                .findByProfessionalIdAndServiceIdAndSlotStartOrderByCreationTimeAsc(professionalId, serviceId, slotStart);

        for (WaitListEntry candidate : queue) {

            boolean alreadyOffered = promotionRepository
                    .findByClientIdAndProfessionalIdAndServiceIdAndSlotStartAndConfirmedFalseAndExpiredFalse(
                            candidate.getClientId(), professionalId, serviceId, slotStart)
                    .isPresent();
            if (alreadyOffered) continue;

            if (slotAvailabilityService.isClientBooked(candidate.getClientId(), professionalId, serviceId, slotStart)) {
                waitListRepository.delete(candidate);
                continue;
            }

            // limpiar promociones viejas (expiradas) para evitar choque con el constraint único
            promotionRepository.deleteByClientIdAndProfessionalIdAndServiceIdAndSlotStart(
                    candidate.getClientId(), professionalId, serviceId, slotStart);
            promotionRepository.flush();


            WaitListPromotion promotion = new WaitListPromotion();
            promotion.setWaitListEntryId(candidate.getId());
            promotion.setClientId(candidate.getClientId());
            promotion.setProfessionalId(professionalId);
            promotion.setServiceId(serviceId);
            promotion.setSlotStart(slotStart);
            promotion.setExpiresAt(ZonedDateTime.now(ZONE).plusMinutes(2)); //aca esta cuando expira, plusHours(2)
            promotionRepository.save(promotion);

            clientRepository.findById(candidate.getClientId()).ifPresent(client -> {
                emailService.sendWaitListPromotionEmail(
                        client.getEmail(),
                        client.getFirstName(),
                        professionalId,
                        serviceId,
                        slotStart,
                        promotion.getExpiresAt()
                );
            });

            break;
        }
    }
    @EventListener
    public void onAppointmentCancelled(AppointmentCancelledEvent event) {
        promoteAffectedSlots(
                event.professionalId(),
                event.serviceId(),
                event.slotStart(),
                event.serviceDurationMinutes(),
                event.slotInterval());
    }

    @Transactional
    public void promoteAffectedSlots(Long professionalId, Long serviceId,
                                     ZonedDateTime cancelledSlotStart,
                                     int serviceDurationMinutes,
                                     int slotInterval) {

        int blocks = (int) Math.ceil(serviceDurationMinutes / (double) slotInterval);

        for (int i = 0; i < blocks; i++) {
            ZonedDateTime candidateStart = cancelledSlotStart.minusMinutes((long) i * slotInterval);

            List<WaitListEntry> queue = waitListRepository
                    .findByProfessionalIdAndServiceIdAndSlotStartOrderByCreationTimeAsc(
                            professionalId, serviceId, candidateStart);

            if (queue.isEmpty()) continue;

            boolean windowFree = slotAvailabilityService.isWindowAvailable(
                    professionalId, serviceId, candidateStart, serviceDurationMinutes);

            boolean isExactSlot = candidateStart.equals(cancelledSlotStart);

            boolean noOverlap = isExactSlot || !slotAvailabilityService.hasOverlappingAppointments(
                    professionalId, serviceId, candidateStart, serviceDurationMinutes);

            if (windowFree && noOverlap) {
                promoteFirstInQueue(professionalId, serviceId, candidateStart);
            }
        }

        boolean cancelledSlotEmpty = slotAvailabilityService.isSlotCompletelyEmpty(
                professionalId, serviceId, cancelledSlotStart, serviceDurationMinutes);

        if (cancelledSlotEmpty) {
            ZonedDateTime cancelledEnd = cancelledSlotStart.plusMinutes(serviceDurationMinutes);
            ZonedDateTime windowFrom = cancelledSlotStart.minusMinutes(serviceDurationMinutes);

            List<WaitListEntry> overlapping = waitListRepository
                    .findByProfessionalIdAndServiceIdAndSlotStartBetween(
                            professionalId, serviceId, windowFrom, cancelledEnd)
                    .stream()
                    .filter(e -> !e.getSlotStart().equals(cancelledSlotStart))
                    .filter(e -> e.getSlotStart().isBefore(cancelledEnd) &&
                            e.getSlotStart().plusMinutes(serviceDurationMinutes).isAfter(cancelledSlotStart))
                    .toList();

            for (WaitListEntry entry : overlapping) {
                boolean overlapNowFree = slotAvailabilityService.isSlotCompletelyEmpty(
                        professionalId, serviceId, entry.getSlotStart(), serviceDurationMinutes);

                boolean noOverlap = !slotAvailabilityService.hasOverlappingAppointments(
                        professionalId, serviceId, entry.getSlotStart(), serviceDurationMinutes);

                if (overlapNowFree && noOverlap) {
                    promoteFirstInQueue(professionalId, serviceId, entry.getSlotStart());
                }
            }
        }
    }

    @Transactional
    public void confirmPromotion(Long professionalId, Long serviceId, ZonedDateTime slotStart, Long clientId) {

        WaitListPromotion promotion = promotionRepository
                .findByClientIdAndProfessionalIdAndServiceIdAndSlotStartAndConfirmedFalseAndExpiredFalse(
                        clientId, professionalId, serviceId, slotStart)
                .orElseThrow(() -> new ItemNotFoundException("No tenés una oferta de turno activa para este slot."));

        if (ZonedDateTime.now(ZONE).isAfter(promotion.getExpiresAt())) {
            promotion.setExpired(true);
            promotionRepository.save(promotion);
            throw new BadRequestException("El tiempo para confirmar expiró.");
        }

        BookingRequestDTO request = new BookingRequestDTO(
                professionalId,
                serviceId,
                null,
                slotStart.withZoneSameInstant(ZONE).toLocalDate(),
                slotStart.withZoneSameInstant(ZONE).toLocalTime()
        );

        bookingService.createBookingFromPromotion(request, clientId);

        promotion.setConfirmed(true);
        promotionRepository.save(promotion);

        waitListRepository
                .findByProfessionalIdAndServiceIdAndSlotStartOrderByCreationTimeAsc(professionalId, serviceId, slotStart)
                .stream()
                .filter(e -> e.getClientId().equals(clientId))
                .findFirst()
                .ifPresent(waitListRepository::delete);
    }

    @Transactional(readOnly = true)
    public List<ActivePromotionDTO> getActivePromotionsForProfessional(Long professionalId) {
        List<WaitListPromotion> promotions = promotionRepository
                .findByProfessionalIdAndConfirmedFalseAndExpiredFalse(professionalId);

        List<ActivePromotionDTO> result = new java.util.ArrayList<>();
        for (WaitListPromotion p : promotions) {
            String clientName = clientRepository.findById(p.getClientId())
                    .map(c -> c.getFirstName() + " " + c.getLastName())
                    .orElse("Cliente");
            String clientEmail = clientRepository.findById(p.getClientId())
                    .map(c -> c.getEmail())
                    .orElse("");
            String serviceName = serviceRepository.findById(p.getServiceId())
                    .map(s -> s.getName())
                    .orElse("Servicio");
            Integer duration = serviceRepository.findById(p.getServiceId())
                    .map(s -> s.getDuration())
                    .orElse(30);

            result.add(new ActivePromotionDTO(
                    p.getId(),
                    p.getClientId(),
                    clientName,
                    clientEmail,
                    p.getServiceId(),
                    serviceName,
                    p.getSlotStart(),
                    p.getExpiresAt(),
                    duration
            ));
        }
        return result;
    }

    private int getPositionInQueue(Long professionalId, Long serviceId, ZonedDateTime slotStart, Long entryId) {
        List<WaitListEntry> entries = waitListRepository
                .findByProfessionalIdAndServiceIdAndSlotStartOrderByCreationTimeAsc(professionalId, serviceId, slotStart);
        int pos = 1;
        for (WaitListEntry e : entries) {
            if (e.getId().equals(entryId)) return pos;
            pos++;
        }
        return pos;
    }

    private WaitListEntryDTO toDTO(WaitListEntry entry, int position) {
        var service = serviceRepository.findById(entry.getServiceId()).orElse(null);
        var prof = professionalRepository.findById(entry.getProfessionalId()).orElse(null);
        return buildDTO(entry, position, service, prof);
    }


    private WaitListEntryDTO buildDTO(WaitListEntry entry, int position,
                                      ServiceEntity service, Professional prof) {
        String serviceName = (service != null) ? service.getName() : "Servicio #" + entry.getServiceId();
        String professionalName = (prof != null) ? (prof.getFirstName() + " " + prof.getLastName()) : "Profesional #" + entry.getProfessionalId();

        return new WaitListEntryDTO(
                entry.getId(),
                entry.getProfessionalId(),
                professionalName,
                entry.getServiceId(),
                serviceName,
                entry.getSlotStart(),
                entry.getCreationTime(),
                position
        );
    }
}