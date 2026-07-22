package ar.uba.fi.ingsoft1.turnos.professional;

import ar.uba.fi.ingsoft1.turnos.config.security.JwtUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import ar.uba.fi.ingsoft1.turnos.common.exception.BadRequestException;
import ar.uba.fi.ingsoft1.turnos.common.exception.ForbiddenException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/pro")
@Tag(name = "Stats")
public class StatsRestController {

    private final StatsService statsService;
    private final ProfessionalRepository professionalRepository;

    public StatsRestController(StatsService statsService,
            ProfessionalRepository professionalRepository) {
        this.statsService = statsService;
        this.professionalRepository = professionalRepository;
    }

    @GetMapping("/stats")
    @Operation(summary = "Get stats for the logged-in professional")
    public ResponseEntity<StatsResponseDTO> getStats(
            @RequestParam(value = "period", defaultValue = "30d") String period,
            Authentication authentication) {

        if (!List.of("7d", "30d", "3m").contains(period)) {
            throw new BadRequestException("Período inválido. Usar: 7d, 30d, 3m");
        }

        JwtUserDetails userDetails = (JwtUserDetails) authentication.getPrincipal();
        Long profId = professionalRepository.findByEmail(userDetails.username())
                .orElseThrow(() -> new ForbiddenException("Solo los profesionales pueden ver estadísticas"))
                .getId();

        return ResponseEntity.ok(statsService.getStats(profId, period));
    }
}