package ar.uba.fi.ingsoft1.turnos.blockedclient;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlockedClientRepository extends JpaRepository<BlockedClient, Long> {
    List<BlockedClient> findByProfessionalId(Long professionalId);
    List<BlockedClient> findByClientId(Long clientId);
    boolean existsByProfessionalIdAndClientId(Long professionalId, Long clientId);
    Optional<BlockedClient> findByProfessionalIdAndClientId(Long professionalId, Long clientId);
}