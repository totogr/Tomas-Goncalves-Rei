package ar.uba.fi.ingsoft1.turnos.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.uba.fi.ingsoft1.turnos.client.Client;
import ar.uba.fi.ingsoft1.turnos.client.ClientRepository;
import ar.uba.fi.ingsoft1.turnos.common.exception.ItemNotFoundException;
import ar.uba.fi.ingsoft1.turnos.config.security.JwtService;
import ar.uba.fi.ingsoft1.turnos.config.security.JwtUserDetails;
import ar.uba.fi.ingsoft1.turnos.professional.Professional;
import ar.uba.fi.ingsoft1.turnos.professional.ProfessionalRepository;
import ar.uba.fi.ingsoft1.turnos.user.refresh_token.RefreshToken;
import ar.uba.fi.ingsoft1.turnos.user.refresh_token.RefreshTokenService;

import java.util.Optional;

@Service
@Transactional
public class AuthService {

    private final ProfessionalRepository professionalRepository;
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthService(ProfessionalRepository professionalRepository,
            ClientRepository clientRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            RefreshTokenService refreshTokenService) {
        this.professionalRepository = professionalRepository;
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    public Optional<TokenDTO> login(UserLoginDTO data) {
        Optional<Professional> prof = professionalRepository.findByEmail(data.username());
        if (prof.isPresent() && passwordEncoder.matches(data.password(), prof.get().getPassword())) {
            return Optional.of(generateTokens(prof.get().getId(), prof.get().getEmail(), UserRole.PROFESSIONAL,
                    prof.get().getFirstName(), prof.get().getLastName()));
        }

        Optional<Client> client = clientRepository.findByEmail(data.username());
        if (client.isPresent() && passwordEncoder.matches(data.password(), client.get().getPassword())) {
            return Optional.of(generateTokens(client.get().getId(), client.get().getEmail(), UserRole.CLIENT,
                    client.get().getFirstName(), client.get().getLastName()));
        }

        return Optional.empty();
    }

    public Optional<TokenDTO> refresh(RefreshDTO data) {
        return refreshTokenService.verify(data.refreshToken())
                .map(token -> {
                    UserRole role = UserRole.valueOf(token.userType());

                    if (role == UserRole.PROFESSIONAL) {
                        Professional prof = professionalRepository.findById(token.userId())
                                .orElseThrow(() -> new ItemNotFoundException("profesional", token.userId()));
                        return generateTokens(token.userId(), prof.getEmail(), role,
                                prof.getFirstName(), prof.getLastName());
                    } else {
                        Client client = clientRepository.findById(token.userId())
                                .orElseThrow(() -> new ItemNotFoundException("cliente", token.userId()));
                        return generateTokens(token.userId(), client.getEmail(), role,
                                client.getFirstName(), client.getLastName());
                    }
                });
    }

    private TokenDTO generateTokens(Long id, String email, UserRole role, String firstName, String lastName) {
        String accessToken = jwtService.createToken(new JwtUserDetails(email, role));
        RefreshToken refreshToken = refreshTokenService.createFor(id, role.name());
        return new TokenDTO(accessToken, refreshToken.value(), role, id, firstName, lastName);
    }
}
