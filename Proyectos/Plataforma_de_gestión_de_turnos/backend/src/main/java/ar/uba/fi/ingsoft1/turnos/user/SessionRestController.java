package ar.uba.fi.ingsoft1.turnos.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import ar.uba.fi.ingsoft1.turnos.common.exception.UnauthorizedException;
import ar.uba.fi.ingsoft1.turnos.config.email.EmailService;
import ar.uba.fi.ingsoft1.turnos.password_reset.ForgotPasswordDTO;
import ar.uba.fi.ingsoft1.turnos.password_reset.PasswordResetTokenService;
import ar.uba.fi.ingsoft1.turnos.password_reset.ResetPasswordDTO;

import ar.uba.fi.ingsoft1.turnos.common.exception.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sessions")
@Tag(name = "Sessions")
public class SessionRestController {

    private final AuthService authService;
    private final PasswordResetTokenService passwordResetTokenService;
    private final EmailService passwordResetEmailService;

    @Autowired
    SessionRestController(
            AuthService authService,
            PasswordResetTokenService passwordResetTokenService,
            EmailService passwordResetEmailService) {
        this.authService = authService;
        this.passwordResetTokenService = passwordResetTokenService;
        this.passwordResetEmailService = passwordResetEmailService;
    }

    @PostMapping(produces = "application/json")
    @Operation(summary = "Log in, creating a new session")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "401", description = "Invalid username or password supplied", content = @Content)
    public TokenDTO login(
            @Valid @NonNull @RequestBody UserLoginDTO data) throws MethodArgumentNotValidException {
        return authService
                .login(data)
                .orElseThrow(UnauthorizedException::new);
    }

    @PutMapping(produces = "application/json")
    @Operation(summary = "Refresh a session")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponse(responseCode = "401", description = "Invalid refresh token supplied", content = @Content)
    public TokenDTO refresh(
            @Valid @NonNull @RequestBody RefreshDTO data) throws MethodArgumentNotValidException {
        return authService
                .refresh(data)
                .orElseThrow(UnauthorizedException::new);
    }

    @PostMapping(value = "/forgot-password", produces = "application/json")
    @Operation(summary = "Request a password reset email")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponse(responseCode = "200", description = "Email sent if account exists", content = @Content)
    public void forgotPassword(
            @Valid @NonNull @RequestBody ForgotPasswordDTO data) {
        passwordResetTokenService.createFor(data.email())
                .ifPresent(token -> passwordResetEmailService.sendResetEmail(data.email(), token));
    }

    @PostMapping(value = "/reset-password", produces = "application/json")
    @Operation(summary = "Reset password using a valid token")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponse(responseCode = "400", description = "Token inválido o expirado", content = @Content)
    public void resetPassword(
            @Valid @NonNull @RequestBody ResetPasswordDTO data) {
        boolean success = passwordResetTokenService.resetPassword(data.token(), data.newPassword());
        if (!success) {
            throw new BadRequestException("Token invalido o expirado");
        }
    }
}
