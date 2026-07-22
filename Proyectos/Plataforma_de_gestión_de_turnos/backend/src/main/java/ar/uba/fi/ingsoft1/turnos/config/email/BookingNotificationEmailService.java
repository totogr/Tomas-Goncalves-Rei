package ar.uba.fi.ingsoft1.turnos.config.email;

import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
public class BookingNotificationEmailService {

    private static final Logger log = LoggerFactory.getLogger(BookingNotificationEmailService.class);

    private final Resend resend;
    private final String fromAddress;

    public BookingNotificationEmailService(
            @Value("${resend.api-key}") String apiKey,
            @Value("${resend.from}") String fromAddress) {

        this.resend = new Resend(apiKey);
        this.fromAddress = fromAddress;
    }

    @Async
    public void notifyProfessionalNewBooking(
            String professionalEmail,
            String professionalName,
            String clientName,
            String clientEmail,
            String serviceName,
            LocalDate date,
            LocalTime time) {

        String subject = "Nueva reserva — " + serviceName;
        String html = buildProfessionalNewBookingHtml(
                professionalName, clientName, clientEmail, serviceName, date, time);
        send(professionalEmail, subject, html);
    }

    @Async
    public void notifyProfessionalCancellation(
            String professionalEmail,
            String professionalName,
            String clientName,
            String serviceName,
            LocalDate date,
            LocalTime time) {

        String subject = "Turno cancelado — " + serviceName;
        String html = buildProfessionalCancellationHtml(
                professionalName, clientName, serviceName, date, time);
        send(professionalEmail, subject, html);
    }

    @Async
    public void notifyProfessionalReschedule(
            String professionalEmail,
            String professionalName,
            String clientName,
            String serviceName,
            LocalDate oldDate,
            LocalTime oldTime,
            LocalDate newDate,
            LocalTime newTime) {

        String subject = "Turno reprogramado — " + serviceName;
        String html = buildProfessionalRescheduleHtml(
                professionalName, clientName, serviceName, oldDate, oldTime, newDate, newTime);
        send(professionalEmail, subject, html);
    }

    @Async
    public void notifyClientNewBooking(
            String clientEmail,
            String clientName,
            String serviceName,
            String professionalName,
            LocalDate date,
            LocalTime time) {

        String subject = "Reserva confirmada — " + serviceName;
        String html = buildClientNewBookingHtml(clientName, serviceName, professionalName, date, time);
        send(clientEmail, subject, html);
    }

    @Async
    public void notifyClientCancelledByProfessional(
            String clientEmail,
            String clientName,
            String serviceName,
            String professionalName,
            LocalDate date,
            LocalTime time) {

        String subject = "Tu turno fue cancelado — " + serviceName;
        String html = buildClientCancellationHtml(clientName, serviceName, professionalName, date, time);
        send(clientEmail, subject, html);
    }

    @Async
    public void notifyClientReschedule(
            String clientEmail,
            String clientName,
            String serviceName,
            String professionalName,
            LocalDate newDate,
            LocalTime newTime) {

        String subject = "Turno reprogramado — " + serviceName;
        String html = buildClientRescheduleHtml(clientName, serviceName, professionalName, newDate, newTime);
        send(clientEmail, subject, html);
    }    

    private void send(String to, String subject, String html) {
        CreateEmailOptions email = CreateEmailOptions.builder()
                .from(fromAddress)
                .to(to)
                .subject(subject)
                .html(html)
                .build();
        try {
            resend.emails().send(email);
        } catch (Exception e) {
            log.error("Error al enviar email a {}: {}", to, e.getMessage());
        }
    }

    private String buildProfessionalNewBookingHtml(
            String professionalName, String clientName, String clientEmail,
            String serviceName, LocalDate date, LocalTime time) {

        String formattedDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String formattedTime = time.format(DateTimeFormatter.ofPattern("HH:mm"));

        return baseTemplate("Nueva reserva recibida",
                """
                <p style="font-size:15px;color:#4a5248;line-height:1.6;margin:0 0 20px;">
                  Hola <strong style="color:#181c18;">%s</strong>, tenés una nueva reserva confirmada.
                </p>
                """.formatted(professionalName)
                + detailRow("Servicio", serviceName)
                + detailRow("Cliente", clientName + " (" + clientEmail + ")")
                + detailRow("Fecha", formattedDate)
                + detailRow("Hora", formattedTime));
    }

    private String buildProfessionalCancellationHtml(
            String professionalName, String clientName,
            String serviceName, LocalDate date, LocalTime time) {

        String formattedDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String formattedTime = time.format(DateTimeFormatter.ofPattern("HH:mm"));

        return baseTemplate("Turno cancelado por el cliente",
                """
                <p style="font-size:15px;color:#4a5248;line-height:1.6;margin:0 0 20px;">
                  Hola <strong style="color:#181c18;">%s</strong>, el cliente canceló un turno.
                </p>
                """.formatted(professionalName)
                + detailRow("Servicio", serviceName)
                + detailRow("Cliente", clientName)
                + detailRow("Fecha", formattedDate)
                + detailRow("Hora", formattedTime));
    }

    private String buildProfessionalRescheduleHtml(
            String professionalName, String clientName, String serviceName,
            LocalDate oldDate, LocalTime oldTime, LocalDate newDate, LocalTime newTime) {

        String fmtOldDate = oldDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String fmtOldTime = oldTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        String fmtNewDate = newDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String fmtNewTime = newTime.format(DateTimeFormatter.ofPattern("HH:mm"));

        return baseTemplate("Turno reprogramado",
                """
                <p style="font-size:15px;color:#4a5248;line-height:1.6;margin:0 0 20px;">
                  Hola <strong style="color:#181c18;">%s</strong>, el cliente reprogramó un turno.
                </p>
                """.formatted(professionalName)
                + detailRow("Servicio", serviceName)
                + detailRow("Cliente", clientName)
                + detailRow("Fecha anterior", fmtOldDate + " " + fmtOldTime)
                + detailRow("Nueva fecha", fmtNewDate + " " + fmtNewTime));
    }

    private String buildClientNewBookingHtml(
            String clientName, String serviceName,
            String professionalName, LocalDate date, LocalTime time) {

        String formattedDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String formattedTime = time.format(DateTimeFormatter.ofPattern("HH:mm"));

        return baseTemplate("Tu reserva fue confirmada",
                """
                <p style="font-size:15px;color:#4a5248;line-height:1.6;margin:0 0 20px;">
                  Hola <strong style="color:#181c18;">%s</strong>, tu turno quedó reservado.
                </p>
                """.formatted(clientName)
                + detailRow("Servicio", serviceName)
                + detailRow("Profesional", professionalName)
                + detailRow("Fecha", formattedDate)
                + detailRow("Hora", formattedTime));
    }

    private String buildClientCancellationHtml(
            String clientName, String serviceName,
            String professionalName, LocalDate date, LocalTime time) {

        String formattedDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String formattedTime = time.format(DateTimeFormatter.ofPattern("HH:mm"));

        return baseTemplate("Tu turno fue cancelado",
                """
                <p style="font-size:15px;color:#4a5248;line-height:1.6;margin:0 0 20px;">
                  Hola <strong style="color:#181c18;">%s</strong>, el profesional canceló tu turno.
                  Podés ingresar al sistema para reprogramarlo.
                </p>
                """.formatted(clientName)
                + detailRow("Servicio", serviceName)
                + detailRow("Profesional", professionalName)
                + detailRow("Fecha cancelada", formattedDate)
                + detailRow("Hora cancelada", formattedTime));
    }

    private String buildClientRescheduleHtml(
            String clientName, String serviceName,
            String professionalName, LocalDate newDate, LocalTime newTime) {

        String formattedDate = newDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String formattedTime = newTime.format(DateTimeFormatter.ofPattern("HH:mm"));

        return baseTemplate("Tu turno fue reprogramado",
                """
                <p style="font-size:15px;color:#4a5248;line-height:1.6;margin:0 0 20px;">
                  Hola <strong style="color:#181c18;">%s</strong>, tu turno fue reprogramado con éxito.
                </p>
                """.formatted(clientName)
                + detailRow("Servicio", serviceName)
                + detailRow("Profesional", professionalName)
                + detailRow("Nueva fecha", formattedDate)
                + detailRow("Nueva hora", formattedTime));
    }

    private String baseTemplate(String title, String body) {
        return """
            <div style="max-width:480px;margin:auto;background:#f5f3ee;border-radius:12px;overflow:hidden;font-family:'DM Sans',system-ui,sans-serif;">
              <div style="background:linear-gradient(160deg,#1a3320 0%%,#0f2218 60%%,#0d1f15 100%%);padding:32px 40px 28px;text-align:center;">
                <p style="font-family:Georgia,serif;font-size:32px;font-weight:500;color:#fff;margin:0;letter-spacing:-0.3px;">
                  Tur<span style="color:#6aaa7e;">nos</span>
                </p>
              </div>
              <div style="padding:36px 40px 36px;">
                <h2 style="font-family:Georgia,serif;font-size:24px;font-weight:500;color:#181c18;margin:0 0 20px;letter-spacing:-0.2px;">%s</h2>
                %s
              </div>
            </div>
            """.formatted(title, body);
    }

    private String detailRow(String label, String value) {
        return """
            <div style="background:#eceae4;border-radius:8px;padding:12px 18px;margin-bottom:10px;">
              <p style="font-size:13px;color:#7a8878;margin:0 0 2px;text-transform:uppercase;letter-spacing:0.6px;">%s</p>
              <p style="font-size:15px;color:#181c18;margin:0;font-weight:500;">%s</p>
            </div>
            """.formatted(label, value);
    }
}