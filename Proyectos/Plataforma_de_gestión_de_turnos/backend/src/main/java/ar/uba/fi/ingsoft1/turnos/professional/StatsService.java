package ar.uba.fi.ingsoft1.turnos.professional;

import ar.uba.fi.ingsoft1.turnos.appointment.Appointment;
import ar.uba.fi.ingsoft1.turnos.appointment.AppointmentRepository;
import ar.uba.fi.ingsoft1.turnos.client.ClientRepository;
import ar.uba.fi.ingsoft1.turnos.review.ReviewRepository;
import ar.uba.fi.ingsoft1.turnos.service.ServiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class StatsService {

    private static final List<String> DAY_NAMES = List.of("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom");

    private final AppointmentRepository appointmentRepository;
    private final ServiceRepository serviceRepository;
    private final ClientRepository clientRepository;
    private final ReviewRepository reviewRepository;

    public StatsService(AppointmentRepository appointmentRepository,
            ServiceRepository serviceRepository,
            ClientRepository clientRepository,
            ReviewRepository reviewRepository) {
        this.appointmentRepository = appointmentRepository;
        this.serviceRepository = serviceRepository;
        this.clientRepository = clientRepository;
        this.reviewRepository = reviewRepository;
    }

    public StatsResponseDTO getStats(Long profId, String period) {
        ZoneId zone = ZoneId.systemDefault();
        ZonedDateTime now = ZonedDateTime.now(zone);
        ZonedDateTime from = switch (period) {
            case "7d" -> now.minusDays(7);
            case "3m" -> now.minusMonths(3);
            default -> now.minusDays(30);
        };

        List<Appointment> all = appointmentRepository
                .findActiveByProfessionalIdAndRange(profId, from, now);

        List<Appointment> allIncludingCancelled = appointmentRepository
                .findAllByProfessionalIdAndRange(profId, from, now);

        int total = allIncludingCancelled.size();
        long cancelled = allIncludingCancelled.stream()
                .filter(a -> "CANCELLED".equalsIgnoreCase(a.getStatus()))
                .count();

        double cancellationRate = total == 0 ? 0 : (cancelled * 100.0) / total;

        Double averageRating = reviewRepository.findAverageScoreByProfessionalId(profId)
                .map(avg -> Math.round(avg * 10.0) / 10.0)
                .orElse(null);

        double revenue = all.stream()
                .mapToDouble(a -> {
                    var svc = serviceRepository.findById(a.getServiceId()).orElse(null);
                    return svc != null && svc.getPrice() != null
                            ? svc.getPrice().doubleValue()
                            : 0.0;
                })
                .sum();

        Map<Integer, Long> byDay = all.stream()
                .collect(Collectors.groupingBy(
                        a -> a.getStart().withZoneSameInstant(zone)
                                .getDayOfWeek().getValue(),
                        Collectors.counting()));

        List<StatsResponseDTO.DayStatDTO> appointmentsByDay = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            appointmentsByDay.add(new StatsResponseDTO.DayStatDTO(
                    DAY_NAMES.get(i - 1),
                    byDay.getOrDefault(i, 0L).intValue()));
        }

        Map<Long, Long> byService = all.stream()
                .collect(Collectors.groupingBy(Appointment::getServiceId, Collectors.counting()));

        int totalNonCancelled = all.size();
        List<StatsResponseDTO.ServiceStatDTO> topServices = byService.entrySet().stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                .limit(4)
                .map(e -> {
                    var svc = serviceRepository.findById(e.getKey()).orElse(null);
                    String name = svc != null ? svc.getName() : "Servicio";
                    int pct = totalNonCancelled == 0 ? 0
                            : (int) Math.round((e.getValue() * 100.0) / totalNonCancelled);
                    return new StatsResponseDTO.ServiceStatDTO(name, e.getValue().intValue(), pct);
                })
                .collect(Collectors.toList());

        int monthsInPeriod = switch (period) {
            case "7d" -> 1;
            case "3m" -> 3;
            default -> 1;
        };

        Map<Long, List<Appointment>> byClient = allIncludingCancelled.stream()
                .collect(Collectors.groupingBy(Appointment::getClientId));

        List<StatsResponseDTO.ClientStatDTO> frequentClients = byClient.entrySet().stream()
                .map(e -> {
                    Long clientId = e.getKey();
                    List<Appointment> clientAppts = e.getValue();

                    int visits = (int) clientAppts.stream()
                            .filter(a -> !"CANCELLED".equalsIgnoreCase(a.getStatus()))
                            .count();
                    int clientCancellations = (int) clientAppts.stream()
                            .filter(a -> "CANCELLED".equalsIgnoreCase(a.getStatus())
                                    && "client".equalsIgnoreCase(a.getCancelledBy()))
                            .count();

                    String status = visits >= monthsInPeriod ? "frequent" : "occasional";

                    var client = clientRepository.findById(clientId).orElse(null);
                    String name = client != null
                            ? (client.getFirstName() + " " + client.getLastName())
                            : "Cliente";

                    return new StatsResponseDTO.ClientStatDTO(name, visits, clientCancellations, status);
                })
                .sorted(Comparator.comparingInt(StatsResponseDTO.ClientStatDTO::visits).reversed())
                .limit(5)
                .collect(Collectors.toList());

        return new StatsResponseDTO(
                total,
                Math.round(cancellationRate * 10.0) / 10.0,
                averageRating,
                revenue,
                appointmentsByDay,
                topServices,
                frequentClients);
    }
}