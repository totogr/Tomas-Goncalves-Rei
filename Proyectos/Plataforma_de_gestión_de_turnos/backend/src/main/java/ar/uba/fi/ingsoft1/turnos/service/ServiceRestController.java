package ar.uba.fi.ingsoft1.turnos.service;

import ar.uba.fi.ingsoft1.turnos.config.security.JwtUserDetails;
import ar.uba.fi.ingsoft1.turnos.professional.ProfessionalRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.NonNull;

import java.util.List;

import ar.uba.fi.ingsoft1.turnos.common.exception.ForbiddenException;
import ar.uba.fi.ingsoft1.turnos.common.exception.ItemNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/services")
@Tag(name = "Services")
class ServiceRestController {

    private final ServiceService serviceService;
    private final ProfessionalRepository professionalRepository;

    @Autowired
    ServiceRestController(ServiceService serviceService, ProfessionalRepository professionalRepository) {
        this.serviceService = serviceService;
        this.professionalRepository = professionalRepository;
    }

    private Long getProfessionalId(Authentication auth) {
        JwtUserDetails userDetails = (JwtUserDetails) auth.getPrincipal();
        return professionalRepository.findByEmail(userDetails.username())
                .orElseThrow(() -> new ForbiddenException("Solo los profesionales pueden gestionar servicios"))
                .getId();
    }

    @PostMapping(produces = "application/json")
    @Operation(summary = "Create a new service")
    public ResponseEntity<ServiceEntity> create(
            @Valid @NonNull @RequestBody ServiceCreateDTO data,
            Authentication authentication) {
        Long professionalId = getProfessionalId(authentication);
        ServiceEntity savedService = serviceService.createService(data, professionalId);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedService);
    }

    @GetMapping(produces = "application/json")
    @Operation(summary = "Get services for the logged-in professional")
    public ResponseEntity<List<ServiceEntity>> getServices(Authentication authentication) {
        Long professionalId = getProfessionalId(authentication);
        List<ServiceEntity> services = serviceService.getServices(professionalId);
        return ResponseEntity.ok(services);
    }

    @PutMapping(path = "/{id}", produces = "application/json")
    @Operation(summary = "Update an existing service")
    public ResponseEntity<ServiceEntity> update(
            @PathVariable("id") Long id,
            @Valid @NonNull @RequestBody ServiceUpdateDTO data,
            Authentication authentication) {
        Long professionalId = getProfessionalId(authentication);
        ServiceEntity updated = serviceService.updateService(id, data, professionalId)
                .orElseThrow(() -> new ItemNotFoundException("Servicio no encontrado"));
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(
            @PathVariable Long id,
            Authentication authentication) {
        Long profId = getProfessionalId(authentication);
        serviceService.deleteService(id, profId);
        return ResponseEntity.noContent().build();
    }

}