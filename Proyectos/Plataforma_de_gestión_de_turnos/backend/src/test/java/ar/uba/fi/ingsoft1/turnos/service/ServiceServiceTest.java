package ar.uba.fi.ingsoft1.turnos.service;

import ar.uba.fi.ingsoft1.turnos.common.exception.ForbiddenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ar.uba.fi.ingsoft1.turnos.appointment.AppointmentRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ServiceServiceTest {

    private ServiceService serviceService;
    private ServiceRepository serviceRepository;
    private AppointmentRepository appointmentRepository;

    @BeforeEach
    void setUp() {
        serviceRepository = mock(ServiceRepository.class);
        appointmentRepository = mock(AppointmentRepository.class);
        serviceService = new ServiceService(serviceRepository, appointmentRepository);
    }

    @Test
    void createServicePersistsAndReturnsEntity() {
        ServiceCreateDTO dto = new ServiceCreateDTO("Corte", 30, BigDecimal.valueOf(3500), 1);

        ServiceEntity saved = new ServiceEntity();
        saved.setName("Corte");
        saved.setDuration(30);
        saved.setPrice(BigDecimal.valueOf(3500));
        saved.setMaxCapacity(1);
        saved.setActive(true);

        when(serviceRepository.save(any())).thenReturn(saved);

        ServiceEntity result = serviceService.createService(dto, 1L);

        assertNotNull(result);
        assertEquals("Corte", result.getName());
        assertEquals(30, result.getDuration());
        assertTrue(result.isActive());
        verify(serviceRepository).save(any());
    }

    @Test
    void createServiceSetsSlugFromName() {
        ServiceCreateDTO dto = new ServiceCreateDTO("Corte de Cabello", 30, BigDecimal.valueOf(3500), 1);

        ServiceEntity saved = new ServiceEntity();
        saved.setSlug("corte de cabello");

        when(serviceRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ServiceEntity result = serviceService.createService(dto, 1L);

        assertEquals("corte-de-cabello", result.getSlug());
    }

    @Test
    void createServiceSetsActiveTrue() {
        ServiceCreateDTO dto = new ServiceCreateDTO("Lavado", 15, BigDecimal.valueOf(1000), 1);
        when(serviceRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ServiceEntity result = serviceService.createService(dto, 1L);

        assertTrue(result.isActive());
    }

    @Test
    void updateServiceReturnsUpdatedEntity() {
        ServiceEntity existing = new ServiceEntity();
        existing.setProfessionalId(1L);
        existing.setName("Corte");
        existing.setDuration(30);
        existing.setPrice(BigDecimal.valueOf(3500));
        existing.setMaxCapacity(1);
        existing.setActive(true);

        when(serviceRepository.findById(10L)).thenReturn(Optional.of(existing));
        when(serviceRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ServiceUpdateDTO update = new ServiceUpdateDTO("Corte Premium", 45, BigDecimal.valueOf(5000), 2, true);
        Optional<ServiceEntity> result = serviceService.updateService(10L, update, 1L);

        assertTrue(result.isPresent());
        assertEquals("Corte Premium", result.get().getName());
        assertEquals(45, result.get().getDuration());
        assertEquals(BigDecimal.valueOf(5000), result.get().getPrice());
        assertEquals(2, result.get().getMaxCapacity());
    }

    @Test
    void updateServiceReturnsEmptyWhenNotFound() {
        when(serviceRepository.findById(99L)).thenReturn(Optional.empty());

        ServiceUpdateDTO update = new ServiceUpdateDTO("X", 30, BigDecimal.ONE, 1, true);
        Optional<ServiceEntity> result = serviceService.updateService(99L, update, 1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void updateServiceThrowsForbiddenWhenWrongProfessional() {
        ServiceEntity existing = new ServiceEntity();
        existing.setProfessionalId(2L); // pertenece a otro profesional

        when(serviceRepository.findById(10L)).thenReturn(Optional.of(existing));

        ServiceUpdateDTO update = new ServiceUpdateDTO("X", 30, BigDecimal.ONE, 1, true);

        assertThrows(ForbiddenException.class,
                () -> serviceService.updateService(10L, update, 1L));
    }

    @Test
    void updateServiceUpdatesSlug() {
        ServiceEntity existing = new ServiceEntity();
        existing.setProfessionalId(1L);
        existing.setName("Viejo");

        when(serviceRepository.findById(10L)).thenReturn(Optional.of(existing));
        when(serviceRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ServiceUpdateDTO update = new ServiceUpdateDTO("Nuevo Servicio", 30, BigDecimal.ONE, 1, true);
        Optional<ServiceEntity> result = serviceService.updateService(10L, update, 1L);

        assertTrue(result.isPresent());
        assertEquals("nuevo-servicio", result.get().getSlug());
    }

    @Test
    void getServicesReturnsOnlyActiveForProfessional() {
        ServiceEntity s1 = new ServiceEntity();
        s1.setName("Activo");
        s1.setActive(true);

        when(serviceRepository.findByProfessionalId(1L)).thenReturn(List.of(s1));

        List<ServiceEntity> result = serviceService.getServices(1L);

        assertEquals(1, result.size());
        assertEquals("Activo", result.get(0).getName());
    }

    @Test
    void getServicesReturnsEmptyWhenNone() {
        when(serviceRepository.findByProfessionalIdAndActiveTrue(1L)).thenReturn(List.of());

        List<ServiceEntity> result = serviceService.getServices(1L);

        assertTrue(result.isEmpty());
    }
}