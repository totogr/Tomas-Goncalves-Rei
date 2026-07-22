package ar.uba.fi.ingsoft1.turnos.client;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import ar.uba.fi.ingsoft1.turnos.common.exception.ConflictException;
import ar.uba.fi.ingsoft1.turnos.common.exception.ForbiddenException;
import ar.uba.fi.ingsoft1.turnos.config.security.JwtUserDetails;
import ar.uba.fi.ingsoft1.turnos.user.TokenDTO;
import ar.uba.fi.ingsoft1.turnos.user.UserRole;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/clients")
@Tag(name = "Clients", description = "Gestión de clientes")
class ClientRestController {

    private final ClientService clientService;
    private final ClientRepository clientRepository;

    @Autowired
    ClientRestController(ClientService clientService,
                         ClientRepository clientRepository) {
        this.clientService = clientService;
        this.clientRepository = clientRepository;
    }

    @PostMapping(path = "/signup", produces = "application/json")
    @Operation(summary = "Register a new client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Client created", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TokenDTO.class)) }),
            @ApiResponse(responseCode = "409", description = "Email already taken", content = @Content)
    })
    public ResponseEntity<TokenDTO> register(@Valid @NonNull @RequestBody ClientCreateDTO data) {
        TokenDTO tokens = clientService.register(data)
                .orElseThrow(() -> new ConflictException("Este email ya tiene una cuenta asociada"));
        return ResponseEntity.status(HttpStatus.CREATED).body(tokens);
    }

    @GetMapping(produces = "application/json")
    @Operation(summary = "Get all clients (professionals only)")
    @ApiResponse(responseCode = "200", description = "List of all clients")
    public ResponseEntity<List<ClientSummaryDTO>> getAllClients(Authentication auth) {
        JwtUserDetails userDetails = (JwtUserDetails) auth.getPrincipal();
        if (userDetails.role() != UserRole.PROFESSIONAL) {
            throw new ForbiddenException("Solo los profesionales pueden ver la lista de clientes");
        }
        return ResponseEntity.ok(clientService.getAllClients());
    }

    @Operation(
            summary = "Actualizar preferencias de notificaciones del cliente",
            description = "Permite al cliente logueado activar o desactivar el envío de correos recordatorios."
    )
    @PatchMapping("/me/preferences")
    public ResponseEntity<Map<String, Object>> updateClientPreferences(
            @RequestBody Map<String, Boolean> request,
            Authentication authentication) {

        JwtUserDetails userDetails = (JwtUserDetails) authentication.getPrincipal();
        Long clientId = clientRepository.findByEmail(userDetails.username())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"))
                .getId();

        Boolean receivesReminders = request.get("receives_reminders");
        boolean updatedValue = clientService.updateClientPreferences(clientId, receivesReminders);

        return ResponseEntity.ok(Map.of(
                "receives_reminders", updatedValue
        ));
    }
}