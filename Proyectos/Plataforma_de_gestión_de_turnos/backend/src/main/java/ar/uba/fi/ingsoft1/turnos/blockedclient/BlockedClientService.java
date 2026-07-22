package ar.uba.fi.ingsoft1.turnos.blockedclient;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.uba.fi.ingsoft1.turnos.appointment.Appointment;
import ar.uba.fi.ingsoft1.turnos.appointment.AppointmentRepository;
import ar.uba.fi.ingsoft1.turnos.common.exception.ConflictException;
import ar.uba.fi.ingsoft1.turnos.common.exception.ItemNotFoundException;

@Service
@Transactional
public class BlockedClientService {

    private static final ZoneId ZONE = ZoneId.of("America/Argentina/Buenos_Aires");

    private final BlockedClientRepository blockedClientRepository;
    private final AppointmentRepository appointmentRepository;

    public BlockedClientService(BlockedClientRepository blockedClientRepository,
                                AppointmentRepository appointmentRepository) {
        this.blockedClientRepository = blockedClientRepository;
        this.appointmentRepository = appointmentRepository;
    }

    public BlockedClient blockClient(Long professionalId, Long clientId) {
        if (blockedClientRepository.existsByProfessionalIdAndClientId(professionalId, clientId)) {
            throw new ConflictException("El cliente ya está bloqueado");
        }
        BlockedClient blocked = new BlockedClient(professionalId, clientId);
        blockedClientRepository.save(blocked);

        // Cancel all future appointments with this client
        ZonedDateTime now = ZonedDateTime.now(ZONE);
        List<Appointment> futureAppointments = appointmentRepository.findByProfessionalIdAndClientId(professionalId, clientId)
                .stream()
                .filter(a -> "CONFIRMED".equals(a.getStatus()) && a.getStart().isAfter(now))
                .toList();

        for (Appointment appt : futureAppointments) {
            appt.setStatus("CANCELLED");
            appt.setCancelledBy("professional");
            appt.setCancelledDate(ZonedDateTime.now(ZONE));
            appointmentRepository.save(appt);
        }

        return blocked;
    }

    public void unblockClient(Long professionalId, Long clientId) {
        BlockedClient blocked = blockedClientRepository
                .findByProfessionalIdAndClientId(professionalId, clientId)
                .orElseThrow(() -> new ItemNotFoundException("Bloqueo no encontrado"));
        blockedClientRepository.delete(blocked);
    }

    public List<Long> getBlockedClientIds(Long professionalId) {
        return blockedClientRepository.findByProfessionalId(professionalId)
                .stream()
                .map(BlockedClient::getClientId)
                .toList();
    }

    public List<Long> getBlockedProfessionalIdsForClient(Long clientId) {
        return blockedClientRepository.findByClientId(clientId)
                .stream()
                .map(BlockedClient::getProfessionalId)
                .toList();
    }

    public boolean isBlocked(Long professionalId, Long clientId) {
        return blockedClientRepository.existsByProfessionalIdAndClientId(professionalId, clientId);
    }
}