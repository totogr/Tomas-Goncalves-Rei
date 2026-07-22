package ar.uba.fi.ingsoft1.turnos.review;

import ar.uba.fi.ingsoft1.turnos.client.ClientRepository;
import ar.uba.fi.ingsoft1.turnos.config.security.JwtUserDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import ar.uba.fi.ingsoft1.turnos.common.exception.ForbiddenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Tag(name = "Reviews")
public class ReviewRestController {

    private final ReviewService reviewService;
    private final ClientRepository clientRepository;

    public ReviewRestController(ReviewService reviewService,
            ClientRepository clientRepository) {
        this.reviewService = reviewService;
        this.clientRepository = clientRepository;
    }

    @PostMapping("/bookings/{id}/review")
    public ResponseEntity<Map<String, Object>> createReview(
            @PathVariable("id") Long appointmentId,
            @Valid @RequestBody ReviewRequestDTO request,
            Authentication authentication) {

        JwtUserDetails userDetails = (JwtUserDetails) authentication.getPrincipal();
        Long clientId = clientRepository.findByEmail(userDetails.username())
                .orElseThrow(() -> new ForbiddenException("Solo los clientes pueden calificar turnos"))
                .getId();

        Review review = reviewService.createReview(appointmentId, clientId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "id", review.getId(),
                "appointment_id", review.getAppointmentId(),
                "score", review.getScore()));
    }
}