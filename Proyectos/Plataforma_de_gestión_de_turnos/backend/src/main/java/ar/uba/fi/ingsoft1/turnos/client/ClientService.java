package ar.uba.fi.ingsoft1.turnos.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import ar.uba.fi.ingsoft1.turnos.config.security.JwtService;
import ar.uba.fi.ingsoft1.turnos.config.security.JwtUserDetails;
import ar.uba.fi.ingsoft1.turnos.user.TokenDTO;
import ar.uba.fi.ingsoft1.turnos.user.UserRole;
import ar.uba.fi.ingsoft1.turnos.user.refresh_token.RefreshToken;
import ar.uba.fi.ingsoft1.turnos.user.refresh_token.RefreshTokenService;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClientService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Autowired
    ClientService(
            ClientRepository clientRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            RefreshTokenService refreshTokenService) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    public Optional<TokenDTO> register(ClientCreateDTO data) {
        if (clientRepository.findByEmail(data.email()).isPresent()) {
            return Optional.empty();
        }

        Client client = new Client(
                data.email(),
                passwordEncoder.encode(data.password()),
                data.firstName(),
                data.lastName());

        clientRepository.save(client);
        return Optional.of(generateTokens(client));
    }

    public List<ClientSummaryDTO> getAllClients() {
        return clientRepository.findAll()
                .stream()
                .map(c -> new ClientSummaryDTO(c.getId(), c.getFirstName(), c.getLastName(), c.getEmail()))
                .toList();
    }

    private TokenDTO generateTokens(Client client) {
        String accessToken = jwtService.createToken(new JwtUserDetails(client.getEmail(), UserRole.CLIENT));
        RefreshToken refreshToken = refreshTokenService.createFor(client.getId(), "CLIENT");
        return new TokenDTO(accessToken, refreshToken.value(), UserRole.CLIENT, client.getId(), client.getFirstName(),
                client.getLastName());
    }

    @Transactional
    public boolean updateClientPreferences(Long clientId, Boolean receivesReminders) {
        var client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));

        if (receivesReminders != null) {
            client.setReceivesReminders(receivesReminders);
            clientRepository.save(client);
        }

        return client.isReceivesReminders();
    }
}
