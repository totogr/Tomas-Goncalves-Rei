package ar.uba.fi.ingsoft1.turnos.appointment;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Component
public class AppointmentStatusUpdater {

    private final AppointmentRepository appointmentRepository;

    public AppointmentStatusUpdater(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void markCompletedAppointments() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
        List<Appointment> toComplete = appointmentRepository
                .findByStatusAndEndBefore("CONFIRMED", now);

        for (Appointment appt : toComplete) {
            appt.setStatus("COMPLETED");
        }
        appointmentRepository.saveAll(toComplete);
    }
}