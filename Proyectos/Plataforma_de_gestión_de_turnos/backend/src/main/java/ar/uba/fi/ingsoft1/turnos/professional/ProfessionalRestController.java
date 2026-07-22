package ar.uba.fi.ingsoft1.turnos.professional;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import ar.uba.fi.ingsoft1.turnos.appointment.AvailabilityService;
import ar.uba.fi.ingsoft1.turnos.blockedclient.BlockedClientService;
import ar.uba.fi.ingsoft1.turnos.client.ClientRepository;
import ar.uba.fi.ingsoft1.turnos.common.exception.ConflictException;
import ar.uba.fi.ingsoft1.turnos.common.exception.ForbiddenException;
import ar.uba.fi.ingsoft1.turnos.appointment.dto.SlotDTO;
import ar.uba.fi.ingsoft1.turnos.config.security.JwtUserDetails;
import ar.uba.fi.ingsoft1.turnos.review.ReviewRepository;
import ar.uba.fi.ingsoft1.turnos.service.ServiceEntity;
import ar.uba.fi.ingsoft1.turnos.service.ServiceRepository;
import ar.uba.fi.ingsoft1.turnos.user.TokenDTO;
import ar.uba.fi.ingsoft1.turnos.user.UserRole;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/professionals")
@Tag(name = "Professionals")
public class ProfessionalRestController {

        private final ProfessionalService professionalService;
        private final AvailabilityService availabilityService;
        private final ProfessionalRepository professionalRepository;
        private final ServiceRepository serviceRepository;
        private final ReviewRepository reviewRepository;
        private final BlockedClientService blockedClientService;
        private final ClientRepository clientRepository;

        @Autowired
        ProfessionalRestController(ProfessionalService professionalService,
                        AvailabilityService availabilityService,
                        ProfessionalRepository professionalRepository,
                        ServiceRepository serviceRepository,
                        ReviewRepository reviewRepository,
                        BlockedClientService blockedClientService,
                        ClientRepository clientRepository) {
                this.professionalService = professionalService;
                this.availabilityService = availabilityService;
                this.professionalRepository = professionalRepository;
                this.serviceRepository = serviceRepository;
                this.reviewRepository = reviewRepository;
                this.blockedClientService = blockedClientService;
                this.clientRepository = clientRepository;
        }

        @GetMapping(produces = "application/json")
        @Operation(summary = "Get all professionals")
        @ApiResponse(responseCode = "200", description = "List of professionals")
        public ResponseEntity<List<ProfessionalSummaryDTO>> getAllProfessionals(Authentication authentication) {
                List<Long> blockedProfIds = getBlockedProfIdsForClient(authentication);
                return ResponseEntity.ok(professionalService.getAllProfessionals(blockedProfIds));
        }

        @PostMapping(path = "/signup", produces = "application/json")
        @Operation(summary = "Register a new professional")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Professional created", content = {
                                        @Content(mediaType = "application/json", schema = @Schema(implementation = TokenDTO.class)) }),
                        @ApiResponse(responseCode = "409", description = "Email already taken", content = @Content)
        })
        public ResponseEntity<TokenDTO> register(@Valid @NonNull @RequestBody ProfessionalCreateDTO data) {
                TokenDTO tokens = professionalService.register(data)
                                .orElseThrow(() -> new ConflictException("Este email ya tiene una cuenta asociada"));
        return ResponseEntity.status(HttpStatus.CREATED).body(tokens);
        }

        @GetMapping(path = "/{id}", produces = "application/json")
        @Operation(summary = "Get professional profile with services")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Professional found"),
                        @ApiResponse(responseCode = "404", description = "Professional not found", content = @Content)
        })
        public ResponseEntity<ProfessionalDTO> getProfessional(@PathVariable("id") Long id,
                        Authentication authentication) {
                Long clientId = getClientIdIfClient(authentication);
                if (clientId != null && blockedClientService.isBlocked(id, clientId)) {
                        return ResponseEntity.notFound().build();
                }
                return professionalRepository.findById(id)
                                .map(prof -> {
                                        List<ServiceEntity> services = serviceRepository.findByProfessionalId(id);
                                        Double rating = reviewRepository.findAverageScoreByProfessionalId(id)
                                                        .map(avg -> Math.round(avg * 10.0) / 10.0)
                                                        .orElse(null);
                                        Integer reviewCount = reviewRepository.countByProfessionalId(id);
                                        return ResponseEntity
                                                        .ok(ProfessionalDTO.from(prof, services, rating, reviewCount));
                                })
                                .orElse(ResponseEntity.notFound().build());
        }

        @GetMapping(path = "/{id}/services/{serviceId}/availability", produces = "application/json")
        @Operation(summary = "Get available slots for a professional's service")
        public ResponseEntity<List<SlotDTO>> getAvailability(
                        @PathVariable("id") Long professionalId,
                        @PathVariable("serviceId") Long serviceId,
                        @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
                List<SlotDTO> slots = availabilityService
                                .getAvailability(professionalId, date, serviceId, null)
                                .slots();
                return ResponseEntity.ok(slots);
        }

        @PatchMapping(path = "/{id}/profile", produces = "application/json")
        @Operation(summary = "Complete professional profile after signup")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Profile updated"),
                        @ApiResponse(responseCode = "403", description = "Not owner of this profile", content = @Content),
                        @ApiResponse(responseCode = "404", description = "Professional not found", content = @Content)
        })
        public ResponseEntity<Void> updateProfile(
                        @PathVariable("id") Long id,
                        @Valid @NonNull @RequestBody ProfessionalProfileDTO data,
                        Authentication authentication) {

                JwtUserDetails userDetails = (JwtUserDetails) authentication.getPrincipal();
                Long authProfId = professionalRepository.findByEmail(userDetails.username())
                                .orElseThrow(() -> new ForbiddenException("Acceso denegado"))
                                .getId();

                if (!authProfId.equals(id)) {
                        throw new ForbiddenException("No podés modificar el perfil de otro profesional");
                }

                return professionalService.updateProfile(id, data)
                                ? ResponseEntity.noContent().build()
                                : ResponseEntity.notFound().build();
        }

        private List<Long> getBlockedProfIdsForClient(Authentication authentication) {
                if (authentication == null) {
                        return List.of();
                }
                JwtUserDetails userDetails = (JwtUserDetails) authentication.getPrincipal();
                if (userDetails.role() != UserRole.CLIENT) {
                        return List.of();
                }
                Long clientId = clientRepository.findByEmail(userDetails.username())
                                .map(c -> c.getId())
                                .orElse(null);
                if (clientId == null) {
                        return List.of();
                }
                return blockedClientService.getBlockedProfessionalIdsForClient(clientId);
        }

        private Long getClientIdIfClient(Authentication authentication) {
                if (authentication == null) {
                        return null;
                }
                JwtUserDetails userDetails = (JwtUserDetails) authentication.getPrincipal();
                if (userDetails.role() != UserRole.CLIENT) {
                        return null;
                }
                return clientRepository.findByEmail(userDetails.username())
                                .map(c -> c.getId())
                                .orElse(null);
        }

}
