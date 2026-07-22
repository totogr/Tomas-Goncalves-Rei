package ar.uba.fi.ingsoft1.turnos.schedule;

import ar.uba.fi.ingsoft1.turnos.config.security.JwtUserDetails;
import ar.uba.fi.ingsoft1.turnos.professional.ProfessionalRepository;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import ar.uba.fi.ingsoft1.turnos.common.exception.ForbiddenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schedule")
@Tag(name = "Schedule")
public class ScheduleRestController {

    private final ScheduleService scheduleService;
    private final ScheduleBlockService scheduleBlockService;
    private final ProfessionalRepository professionalRepository;

    public ScheduleRestController(ScheduleService scheduleService,
            ScheduleBlockService scheduleBlockService,
            ProfessionalRepository professionalRepository) {
        this.scheduleService = scheduleService;
        this.scheduleBlockService = scheduleBlockService;
        this.professionalRepository = professionalRepository;
    }

    private Long getProfessionalId(Authentication auth) {
        JwtUserDetails userDetails = (JwtUserDetails) auth.getPrincipal();
        return professionalRepository.findByEmail(userDetails.username())
                .orElseThrow(() -> new ForbiddenException("Solo los profesionales pueden acceder a esta sección"))
                .getId();
    }

    @GetMapping(produces = "application/json")
    @Operation(summary = "Get working hours for the logged-in professional")
    public ResponseEntity<WorkingHoursDTO> getSchedule(Authentication auth) {
        Long professionalId = getProfessionalId(auth);
        return ResponseEntity.ok(scheduleService.getSchedule(professionalId));
    }

    @PutMapping(consumes = "application/json", produces = "application/json")
    @Operation(summary = "Save working hours for the logged-in professional")
    public ResponseEntity<WorkingHoursDTO> saveSchedule(
            @RequestBody WorkingHoursDTO request,
            Authentication auth) {
        Long professionalId = getProfessionalId(auth);
        return ResponseEntity.ok(scheduleService.saveSchedule(professionalId, request));
    }

    @GetMapping(path = "/blocks", produces = "application/json")
    @Operation(summary = "Get blocked schedule ranges for the logged-in professional")
    public ResponseEntity<List<ScheduleBlockDTO>> getBlocks(Authentication auth) {
        Long professionalId = getProfessionalId(auth);
        return ResponseEntity.ok(scheduleBlockService.getBlocks(professionalId));
    }

    @PostMapping(path = "/blocks", consumes = "application/json", produces = "application/json")
    @Operation(summary = "Create a blocked schedule range for the logged-in professional")
    public ResponseEntity<ScheduleBlockCreateResultDTO> createBlock(@Valid @RequestBody ScheduleBlockRequestDTO request,
            Authentication auth) {
        Long professionalId = getProfessionalId(auth);
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleBlockService.createBlock(professionalId, request));
    }

    @DeleteMapping(path = "/blocks/{id}")
    @Operation(summary = "Delete a blocked schedule range for the logged-in professional")
    public ResponseEntity<Void> deleteBlock(@PathVariable("id") Long blockId, Authentication auth) {
        Long professionalId = getProfessionalId(auth);
        scheduleBlockService.deleteBlock(professionalId, blockId);
        return ResponseEntity.noContent().build();
    }
}