package ar.uba.fi.ingsoft1.turnos.service;

import org.springframework.beans.factory.annotation.Autowired;
import ar.uba.fi.ingsoft1.turnos.common.exception.ConflictException;
import ar.uba.fi.ingsoft1.turnos.common.exception.ForbiddenException;
import ar.uba.fi.ingsoft1.turnos.common.exception.ItemNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.uba.fi.ingsoft1.turnos.appointment.AppointmentRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
class ServiceService {

    private final ServiceRepository serviceRepository;
    private final AppointmentRepository appointmentRepository;

    @Autowired
    ServiceService(ServiceRepository serviceRepository, AppointmentRepository appointmentRepository) {
        this.serviceRepository = serviceRepository;
        this.appointmentRepository = appointmentRepository;
    }

    ServiceEntity createService(ServiceCreateDTO data, Long professionalId) {
        ServiceEntity entity = new ServiceEntity();

        entity.setProfessionalId(professionalId);
        entity.setName(data.name());
        entity.setDuration(data.duration());
        entity.setPrice(data.price());
        entity.setMaxCapacity(data.maxCapacity());
        entity.setActive(true);

        if (data.name() != null) {
            entity.setSlug(data.name().toLowerCase().replace(" ", "-"));
        }

        return serviceRepository.save(entity);
    }

    Optional<ServiceEntity> updateService(Long id, ServiceUpdateDTO data, Long professionalId) {
        return serviceRepository.findById(id).map(entity -> {

            if (!entity.getProfessionalId().equals(professionalId)) {
                throw new ForbiddenException("No podés modificar servicios de otro profesional");
            }

            entity.setName(data.name());
            entity.setDuration(data.duration());
            entity.setPrice(data.price());
            entity.setMaxCapacity(data.maxCapacity());
            entity.setActive(data.active());

            if (data.name() != null) {
                entity.setSlug(data.name().toLowerCase().replace(" ", "-"));
            }

            return serviceRepository.save(entity);
        });
    }

    List<ServiceEntity> getServices(Long professionalId) {
        return serviceRepository.findByProfessionalId(professionalId);
    }

    @Transactional
    public void deleteService(Long serviceId, Long profId) {
        ServiceEntity service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ItemNotFoundException("Servicio no encontrado"));

        if (!service.getProfessionalId().equals(profId)) {
            throw new ForbiddenException("El servicio no pertenece a este profesional");
        }

        boolean hasAppointments = appointmentRepository.existsByServiceId(serviceId);
        if (hasAppointments) {
            throw new ConflictException("No se puede eliminar un servicio que tiene turnos asociados. Desactivalo en su lugar.");
        }

        serviceRepository.delete(service);
    }

}