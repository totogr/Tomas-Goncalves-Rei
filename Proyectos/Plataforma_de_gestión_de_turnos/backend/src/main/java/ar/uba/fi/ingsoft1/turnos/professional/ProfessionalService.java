package ar.uba.fi.ingsoft1.turnos.professional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.uba.fi.ingsoft1.turnos.config.security.JwtService;
import ar.uba.fi.ingsoft1.turnos.config.security.JwtUserDetails;
import ar.uba.fi.ingsoft1.turnos.review.ReviewRepository;
import ar.uba.fi.ingsoft1.turnos.user.TokenDTO;
import ar.uba.fi.ingsoft1.turnos.user.UserLoginDTO;
import ar.uba.fi.ingsoft1.turnos.user.UserRole;
import ar.uba.fi.ingsoft1.turnos.user.refresh_token.RefreshToken;
import ar.uba.fi.ingsoft1.turnos.user.refresh_token.RefreshTokenService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProfessionalService {
    private final ProfessionalRepository professionalRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final ReviewRepository reviewRepository;

    @Autowired
    public ProfessionalService(
            ProfessionalRepository professionalRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            RefreshTokenService refreshTokenService,
            ReviewRepository reviewRepository) {
        this.professionalRepository = professionalRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.reviewRepository = reviewRepository;
    }

    public Optional<TokenDTO> register(ProfessionalCreateDTO data) {
        if (professionalRepository.findByEmail(data.email()).isPresent()) {
            return Optional.empty();
        }

        Professional professional = new Professional(
                data.email(),
                passwordEncoder.encode(data.password()),
                data.firstName(),
                data.lastName());

        professionalRepository.save(professional);
        return Optional.of(generateTokens(professional));
    }

    public Optional<TokenDTO> login(UserLoginDTO data) {
        Optional<Professional> maybeProf = professionalRepository.findByEmail(data.username());
        if (maybeProf.isEmpty()) {
            return Optional.empty();
        }
        Professional prof = maybeProf.get();

        if (!passwordEncoder.matches(data.password(), prof.getPassword())) {
            return Optional.empty();
        }

        return Optional.of(generateTokens(prof));
    }

    List<ProfessionalSummaryDTO> getAllProfessionals() {
        return getAllProfessionals(List.of());
    }

    List<ProfessionalSummaryDTO> getAllProfessionals(List<Long> blockedProfessionalIds) {
        Map<Long, Double> ratings = reviewRepository.findAllAverageScores()
                .stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> Math.round((Double) row[1] * 10.0) / 10.0));

        return professionalRepository.findAll()
                .stream()
                .filter(p -> !blockedProfessionalIds.contains(p.getId()))
                .map(p -> ProfessionalSummaryDTO.from(p, ratings.get(p.getId())))
                .toList();
    }

    boolean updateProfile(Long id, ProfessionalProfileDTO data) {
        return professionalRepository.findById(id).map(prof -> {
            prof.setSpecialty(data.specialty());
            prof.setAddress(data.address());
            prof.setNeighborhood(data.neighborhood());
            prof.setCity(data.city());
            professionalRepository.save(prof);
            return true;
        }).orElse(false);
    }

    private TokenDTO generateTokens(Professional prof) {
        String accessToken = jwtService.createToken(new JwtUserDetails(prof.getEmail(), UserRole.PROFESSIONAL));
        RefreshToken refreshToken = refreshTokenService.createFor(prof.getId(), "PROFESSIONAL");
        return new TokenDTO(accessToken, refreshToken.value(), UserRole.PROFESSIONAL, prof.getId(), prof.getFirstName(),
                prof.getLastName());
    }
}
