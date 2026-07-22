package ar.uba.fi.ingsoft1.turnos.service;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {
    List<ServiceEntity> findByProfessionalId(Long professionalId);

    List<ServiceEntity> findByProfessionalIdAndActiveTrue(Long professionalId);
}