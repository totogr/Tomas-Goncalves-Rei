package ar.uba.fi.ingsoft1.turnos.appointment;

import ar.uba.fi.ingsoft1.turnos.appointment.dto.WaitListEntryDTO;
import ar.uba.fi.ingsoft1.turnos.appointment.dto.ActivePromotionDTO;
import ar.uba.fi.ingsoft1.turnos.config.security.CurrentClientId;
import ar.uba.fi.ingsoft1.turnos.config.security.CurrentProfessionalId;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/waitlist")
@Tag(name = "Wait list")
@RequiredArgsConstructor
public class WaitListController {
    private final WaitListService waitListService;

    @PostMapping
    public ResponseEntity<WaitListEntryDTO> join(
            @RequestParam Long professionalId,
            @RequestParam Long serviceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime slotStart,
            @CurrentClientId Long clientId) {
        WaitListEntryDTO entry = waitListService.joinWaitList(professionalId, serviceId, slotStart, clientId);
        return ResponseEntity.status(HttpStatus.CREATED).body(entry);
    }

    @DeleteMapping
    public ResponseEntity<Void> leave(
            @RequestParam Long professionalId,
            @RequestParam Long serviceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime slotStart,
            @CurrentClientId Long clientId) {
        waitListService.leaveWaitList(professionalId, serviceId, slotStart, clientId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<WaitListEntryDTO> myPosition(
            @RequestParam Long professionalId,
            @RequestParam Long serviceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime slotStart,
            @CurrentClientId Long clientId) {
        return ResponseEntity.ok(
                waitListService.getClientWaitListEntry(professionalId, serviceId, slotStart, clientId));
    }

    @GetMapping
    public ResponseEntity<List<WaitListEntryDTO>> getWaitList(
            @RequestParam Long professionalId,
            @RequestParam Long serviceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime slotStart) {
        return ResponseEntity.ok(waitListService.getWaitList(professionalId, serviceId, slotStart));
    }

    @GetMapping("/me/all")
    public ResponseEntity<List<WaitListEntryDTO>> myEntries(@CurrentClientId Long clientId) {
        return ResponseEntity.ok(waitListService.getClientWaitListEntries(clientId));
    }

    @PostMapping("/confirm")
    public ResponseEntity<Void> confirmWaitListPromotion(
            @RequestParam Long professionalId,
            @RequestParam Long serviceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime slotStart,
            @CurrentClientId Long clientId) {
        waitListService.confirmPromotion(professionalId, serviceId, slotStart, clientId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/promotions/professional")
    public ResponseEntity<List<ActivePromotionDTO>> getActivePromotionsForProfessional(
            @CurrentProfessionalId Long profId) {
        return ResponseEntity.ok(waitListService.getActivePromotionsForProfessional(profId));
    }
}
