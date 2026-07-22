package ar.uba.fi.ingsoft1.turnos.blockedclient;

import ar.uba.fi.ingsoft1.turnos.config.security.JwtUserDetails;
import ar.uba.fi.ingsoft1.turnos.common.exception.ForbiddenException;
import ar.uba.fi.ingsoft1.turnos.professional.ProfessionalRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/professionals/{professionalId}/blocked-clients")
@Tag(name = "Blocked Clients")
public class BlockedClientRestController {

    private final BlockedClientService blockedClientService;
    private final ProfessionalRepository professionalRepository;

    public BlockedClientRestController(BlockedClientService blockedClientService,
            ProfessionalRepository professionalRepository) {
        this.blockedClientService = blockedClientService;
        this.professionalRepository = professionalRepository;
    }

    private Long getProfessionalId(Authentication auth) {
        JwtUserDetails userDetails = (JwtUserDetails) auth.getPrincipal();
        return professionalRepository.findByEmail(userDetails.username())
                .orElseThrow(() -> new ForbiddenException("Solo los profesionales pueden acceder a esta sección"))
                .getId();
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    @Operation(summary = "Block a client from the logged-in professional's list")
    public ResponseEntity<BlockedClient> blockClient(
            @PathVariable Long professionalId,
            @RequestBody BlockClientRequest request,
            Authentication auth) {
        Long authProfId = getProfessionalId(auth);
        if (!authProfId.equals(professionalId)) {
            throw new ForbiddenException("No podés bloquear clientes para otro profesional");
        }
        BlockedClient result = blockedClientService.blockClient(professionalId, request.clientId());
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @DeleteMapping(path = "/{clientId}")
    @Operation(summary = "Unblock a client from the logged-in professional")
    public ResponseEntity<Void> unblockClient(
            @PathVariable("professionalId") Long professionalId,
            @PathVariable("clientId") Long clientId,
            Authentication auth) {
        Long authProfId = getProfessionalId(auth);
        if (!authProfId.equals(professionalId)) {
            throw new ForbiddenException("No podés desbloquear clientes para otro profesional");
        }
        blockedClientService.unblockClient(professionalId, clientId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(produces = "application/json")
    @Operation(summary = "Get blocked client IDs for the logged-in professional")
    public ResponseEntity<List<Long>> getBlockedClients(
            @PathVariable Long professionalId,
            Authentication auth) {
        Long authProfId = getProfessionalId(auth);
        if (!authProfId.equals(professionalId)) {
            throw new ForbiddenException("Solo podés ver tus propios clientes bloqueados");
        }
        return ResponseEntity.ok(blockedClientService.getBlockedClientIds(professionalId));
    }
}