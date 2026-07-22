package ar.uba.fi.ingsoft1.turnos.appointment;

import ar.uba.fi.ingsoft1.turnos.client.Client;
import ar.uba.fi.ingsoft1.turnos.client.ClientRepository;
import ar.uba.fi.ingsoft1.turnos.config.email.EmailService;
import ar.uba.fi.ingsoft1.turnos.professional.Professional;
import ar.uba.fi.ingsoft1.turnos.professional.ProfessionalRepository;
import ar.uba.fi.ingsoft1.turnos.service.ServiceEntity;
import ar.uba.fi.ingsoft1.turnos.service.ServiceRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import java.time.ZonedDateTime;
import java.util.List;

@Component
public class AppointmentReminderScheduler {

    private final AppointmentRepository appointmentRepository;
    private final ClientRepository clientRepository;
    private final ProfessionalRepository professionalRepository;
    private final ServiceRepository serviceRepository;
    private final EmailService emailService;

    public AppointmentReminderScheduler(AppointmentRepository appointmentRepository,
                                        ClientRepository clientRepository,
                                        ProfessionalRepository professionalRepository,
                                        ServiceRepository serviceRepository,
                                        EmailService emailService) {
        this.appointmentRepository = appointmentRepository;
        this.clientRepository = clientRepository;
        this.professionalRepository = professionalRepository;
        this.serviceRepository = serviceRepository;
        this.emailService = emailService;
    }

    @Scheduled(fixedRate = 900000)
    @Transactional
    public void processReminders() {
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tomorrow = now.plusHours(24);

        List<Appointment> upcomingAppointments = appointmentRepository.findAppointmentsForReminder(now, tomorrow);

        for (Appointment appt : upcomingAppointments) {
            Client client = clientRepository.findById(appt.getClientId()).orElse(null);

            if (client != null && client.isReceivesReminders()) {

                Professional professional = professionalRepository.findById(appt.getProfessionalId()).orElse(null);
                ServiceEntity service = serviceRepository.findById(appt.getServiceId()).orElse(null);

                String professionalName = (professional != null) ? professional.getFirstName() + " " + professional.getLastName() : "Tu Profesional";
                String serviceName = (service != null) ? service.getName() : "Servicio Reservado";

                ZonedDateTime timeArgentina = appt.getStart().withZoneSameInstant(ZoneId.of("America/Argentina/Buenos_Aires"));

                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

                String date = timeArgentina.format(dateFormatter);
                String time = timeArgentina.format(timeFormatter);

                emailService.sendAppointmentReminderEmail(
                        client.getEmail(),
                        client.getFirstName(),
                        serviceName,
                        professionalName,
                        date,
                        time
                );

                appt.setReminderSent(true);
                appointmentRepository.save(appt);
            }
        }
    }
}