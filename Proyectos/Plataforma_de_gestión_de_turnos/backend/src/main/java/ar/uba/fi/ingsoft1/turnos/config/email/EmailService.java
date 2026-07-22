package ar.uba.fi.ingsoft1.turnos.config.email;

import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

  private static final Logger log = LoggerFactory.getLogger(EmailService.class);

  private final Resend resend;
  private final String fromAddress;
  private final String frontendUrl;

  public EmailService(
          @Value("${resend.api-key}") String apiKey,
          @Value("${resend.from}") String fromAddress,
          @Value("${application.frontend.url}") String frontendUrl) {

    if (apiKey == null || apiKey.isBlank()) {
      throw new IllegalStateException("resend.api-key no está configurado");
    }
    this.resend = new Resend(apiKey);
    this.fromAddress = fromAddress;
    this.frontendUrl = frontendUrl;
  }

  private void sendEmail(String to, String subject, String htmlContent) {
    CreateEmailOptions email = CreateEmailOptions.builder()
            .from(fromAddress)
            .to(to)
            .subject(subject)
            .html(htmlContent)
            .build();

    try {
      resend.emails().send(email);
    } catch (Exception e) {
      log.error("No se pudo enviar email a {}: {}", to, e.getMessage());
    }
  }

  @Async
  public void sendResetEmail(String to, String rawToken) {
    String resetLink = frontendUrl + "/reset-password?token=" + rawToken;
    String html = buildPasswordResetHtml(resetLink);
    sendEmail(to, "Restablecer contraseña — Turnos", html);
  }

  @Async
  public void sendAppointmentReminderEmail(String to, String clientFirstName, String serviceName, String professionalName, String date, String time) {
    String html = buildAppointmentReminderHtml(clientFirstName, serviceName, professionalName, date, time);
    sendEmail(to, "Recordatorio de tu turno de mañana — Turnos", html);
  }

  private String buildPasswordResetHtml(String resetLink) {
    return """
            <div style="max-width:480px;margin:auto;background:#f5f3ee;border-radius:12px;overflow:hidden;font-family:'DM Sans',system-ui,sans-serif;">
              <div style="background:linear-gradient(160deg,#1a3320 0%%,#0f2218 60%%,#0d1f15 100%%);padding:32px 40px 28px;text-align:center;">
                <p style="font-family:Georgia,serif;font-size:32px;font-weight:500;color:#fff;margin:0;letter-spacing:-0.3px;">
                  Tur<span style="color:#6aaa7e;">nos</span>
                </p>
              </div>
              <div style="padding:36px 40px 36px;">
                <p style="font-size:13px;color:#7a8878;margin:0 0 20px;text-transform:uppercase;letter-spacing:0.8px;">Seguridad de cuenta</p>
                <h2 style="font-family:Georgia,serif;font-size:26px;font-weight:500;color:#181c18;margin:0 0 12px;letter-spacing:-0.2px;">Restablecer tu contraseña</h2>
                <p style="font-size:15px;color:#4a5248;line-height:1.6;margin:0 0 28px;">
                  Recibimos una solicitud para restablecer la contraseña de tu cuenta en <strong style="color:#181c18;">Turnos</strong>. Si fuiste vos, hacé clic en el botón para continuar.
                </p>
                <div style="text-align:center;margin:0 0 28px;">
                  <a href="%s" style="display:inline-block;background:#1e5c32;color:#fff;font-size:15px;font-weight:500;padding:13px 32px;border-radius:8px;text-decoration:none;letter-spacing:0.1px;">
                    Restablecer contraseña
                  </a>
                </div>
                <div style="background:#eceae4;border-radius:8px;padding:14px 18px;">
                  <p style="font-size:13px;color:#5a6358;margin:0;line-height:1.5;">
                    <strong style="color:#2d3b2c;">&#9201; Este enlace expira en 30 minutos.</strong><br>
                    Si no solicitaste esto, podés ignorar este email. Tu contraseña no será modificada.
                  </p>
                </div>
              </div>
            </div>
            """.formatted(resetLink);
  }

  private String buildAppointmentReminderHtml(String clientFirstName, String serviceName, String professionalName, String date, String time) {
    return """
            <div style="max-width:480px;margin:auto;background:#f5f3ee;border-radius:12px;overflow:hidden;font-family:'DM Sans',system-ui,sans-serif;">
              <div style="background:linear-gradient(160deg,#1a3320 0%%,#0f2218 60%%,#0d1f15 100%%);padding:32px 40px 28px;text-align:center;">
                <p style="font-family:Georgia,serif;font-size:32px;font-weight:500;color:#fff;margin:0;letter-spacing:-0.3px;">
                  Tur<span style="color:#6aaa7e;">nos</span>
                </p>
              </div>
              <div style="padding:36px 40px 36px;">
                <p style="font-size:13px;color:#7a8878;margin:0 0 20px;text-transform:uppercase;letter-spacing:0.8px;">Recordatorio de Turno</p>
                <h2 style="font-family:Georgia,serif;font-size:26px;font-weight:500;color:#181c18;margin:0 0 12px;letter-spacing:-0.2px;">¡Hola, %s!</h2>
                <p style="font-size:15px;color:#4a5248;line-height:1.6;margin:0 0 28px;">
                  Te escribimos para recordarte que tenés un turno programado para mañana. Acá te dejamos los detalles:
                </p>
                
                <div style="background:#fff;border:1px solid #dcd7cb;border-radius:8px;padding:20px;margin:0 0 28px;">
                  <p style="margin:0 0 10px;font-size:15px;color:#2d3b2c;"><strong>Servicio:</strong> %s</p>
                  <p style="margin:0 0 10px;font-size:15px;color:#2d3b2c;"><strong>Profesional:</strong> %s</p>
                  <p style="margin:0 0 10px;font-size:15px;color:#2d3b2c;"><strong>Fecha:</strong> %s</p>
                  <p style="margin:0;font-size:15px;color:#2d3b2c;"><strong>Hora:</strong> %s hs</p>
                </div>

                <div style="text-align:center;margin:0 0 28px;">
                  <a href="%s/bookings/me" style="display:inline-block;background:#1e5c32;color:#fff;font-size:15px;font-weight:500;padding:13px 32px;border-radius:8px;text-decoration:none;letter-spacing:0.1px;">
                    Ver mis turnos
                  </a>
                </div>

                <div style="background:#eceae4;border-radius:8px;padding:14px 18px;">
                  <p style="font-size:13px;color:#5a6358;margin:0;line-height:1.5;">
                    Si no podés asistir, por favor recordá cancelar el turno desde la plataforma para liberar el espacio.<br><br>
                    <em>Podés desactivar estos recordatorios desde tu perfil de usuario.</em>
                  </p>
                </div>
              </div>
            </div>
            """.formatted(clientFirstName, serviceName, professionalName, date, time, frontendUrl);
  }

  @Async
  public void sendWaitListPromotionEmail(String to, String firstName,
                                         Long professionalId, Long serviceId,
                                         ZonedDateTime slotStart, ZonedDateTime expiresAt) {
    ZonedDateTime slotArg = slotStart.withZoneSameInstant(ZoneId.of("America/Argentina/Buenos_Aires"));
    ZonedDateTime expArg  = expiresAt.withZoneSameInstant(ZoneId.of("America/Argentina/Buenos_Aires"));

    String date    = slotArg.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    String time    = slotArg.format(DateTimeFormatter.ofPattern("HH:mm"));
    String dateHuman = slotArg.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    String expTime = expArg.format(DateTimeFormatter.ofPattern("HH:mm 'del' dd/MM/yyyy"));

    String confirmUrl = frontendUrl + "/professionals/" + professionalId
            + "?serviceId=" + serviceId
            + "&date=" + date
            + "&slotTime=" + time
            + "&waitlistConfirm=true";

    String html = buildWaitListPromotionHtml(firstName, dateHuman, time, expTime, confirmUrl);
    sendEmail(to, "¡Hay un lugar disponible! Confirmá tu turno — Turnos", html);
  }

  private String buildWaitListPromotionHtml(String firstName, String date, String time,
                                            String expTime, String confirmUrl) {
    return """
        <div style="max-width:480px;margin:auto;background:#f5f3ee;border-radius:12px;overflow:hidden;font-family:'DM Sans',system-ui,sans-serif;">
          <div style="background:linear-gradient(160deg,#1a3320 0%%,#0f2218 60%%,#0d1f15 100%%);padding:32px 40px 28px;text-align:center;">
            <p style="font-family:Georgia,serif;font-size:32px;font-weight:500;color:#fff;margin:0;letter-spacing:-0.3px;">
              Tur<span style="color:#6aaa7e;">nos</span>
            </p>
          </div>
          <div style="padding:36px 40px 36px;">
            <p style="font-size:13px;color:#7a8878;margin:0 0 20px;text-transform:uppercase;letter-spacing:0.8px;">Lista de espera</p>
            <h2 style="font-family:Georgia,serif;font-size:26px;font-weight:500;color:#181c18;margin:0 0 12px;letter-spacing:-0.2px;">¡Hola, %s!</h2>
            <p style="font-size:15px;color:#4a5248;line-height:1.6;margin:0 0 24px;">
              Se liberó un lugar para tu turno. Tenés tiempo hasta las <strong style="color:#181c18;">%s</strong> para confirmarlo, después el lugar pasará al siguiente en la lista.
            </p>
            <div style="background:#eceae4;border-radius:8px;padding:12px 18px;margin-bottom:10px;">
              <p style="font-size:13px;color:#7a8878;margin:0 0 2px;text-transform:uppercase;letter-spacing:0.6px;">Fecha</p>
              <p style="font-size:15px;color:#181c18;margin:0;font-weight:500;">%s</p>
            </div>
            <div style="background:#eceae4;border-radius:8px;padding:12px 18px;margin-bottom:24px;">
              <p style="font-size:13px;color:#7a8878;margin:0 0 2px;text-transform:uppercase;letter-spacing:0.6px;">Hora</p>
              <p style="font-size:15px;color:#181c18;margin:0;font-weight:500;">%s hs</p>
            </div>
            <div style="text-align:center;">
              <a href="%s" style="display:inline-block;background:#1e5c32;color:#fff;font-size:15px;font-weight:500;padding:13px 32px;border-radius:8px;text-decoration:none;letter-spacing:0.1px;">
                Confirmar turno
              </a>
            </div>
          </div>
        </div>
        """.formatted(firstName, expTime, date, time, confirmUrl);
  }

  @Async
  public void sendWaitListExpiredEmail(String to, String firstName,
                                       Long professionalId, Long serviceId,
                                       ZonedDateTime slotStart) {
    ZonedDateTime slotArg = slotStart.withZoneSameInstant(ZoneId.of("America/Argentina/Buenos_Aires"));
    String dateHuman = slotArg.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    String time = slotArg.format(DateTimeFormatter.ofPattern("HH:mm"));

    String browseUrl = frontendUrl + "/professionals/" + professionalId + "?serviceId=" + serviceId;

    String html = buildWaitListExpiredHtml(firstName, dateHuman, time, browseUrl);
    sendEmail(to, "Se venció tu oferta de turno — Turnos", html);
  }

  private String buildWaitListExpiredHtml(String firstName, String date, String time, String browseUrl) {
    return """
        <div style="max-width:480px;margin:auto;background:#f5f3ee;border-radius:12px;overflow:hidden;font-family:'DM Sans',system-ui,sans-serif;">
          <div style="background:linear-gradient(160deg,#1a3320 0%%,#0f2218 60%%,#0d1f15 100%%);padding:32px 40px 28px;text-align:center;">
            <p style="font-family:Georgia,serif;font-size:32px;font-weight:500;color:#fff;margin:0;letter-spacing:-0.3px;">
              Tur<span style="color:#6aaa7e;">nos</span>
            </p>
          </div>
          <div style="padding:36px 40px 36px;">
            <p style="font-size:13px;color:#7a8878;margin:0 0 20px;text-transform:uppercase;letter-spacing:0.8px;">Lista de espera</p>
            <h2 style="font-family:Georgia,serif;font-size:26px;font-weight:500;color:#181c18;margin:0 0 12px;letter-spacing:-0.2px;">Hola, %s</h2>
            <p style="font-size:15px;color:#4a5248;line-height:1.6;margin:0 0 24px;">
              El tiempo para confirmar tu turno del <strong style="color:#181c18;">%s a las %s hs</strong> venció, así que el lugar pasó al siguiente cliente en la lista de espera. El enlace de confirmación que recibiste anteriormente ya no es válido.
            </p>
            <div style="background:#eceae4;border-radius:8px;padding:14px 18px;margin-bottom:24px;">
              <p style="font-size:13px;color:#5a6358;margin:0;line-height:1.5;">
                Si seguís interesado, podés anotarte nuevamente en la lista de espera o buscar otro horario disponible.
              </p>
            </div>
            <div style="text-align:center;">
              <a href="%s" style="display:inline-block;background:#1e5c32;color:#fff;font-size:15px;font-weight:500;padding:13px 32px;border-radius:8px;text-decoration:none;letter-spacing:0.1px;">
                Ver disponibilidad
              </a>
            </div>
          </div>
        </div>
        """.formatted(firstName, date, time, browseUrl);
  }
}