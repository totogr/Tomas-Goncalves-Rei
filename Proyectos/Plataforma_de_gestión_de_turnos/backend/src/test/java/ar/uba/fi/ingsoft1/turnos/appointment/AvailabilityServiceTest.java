package ar.uba.fi.ingsoft1.turnos.appointment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ar.uba.fi.ingsoft1.turnos.appointment.dto.AvailabilityResponseDTO;
import ar.uba.fi.ingsoft1.turnos.professional.ProfessionalRepository;
import ar.uba.fi.ingsoft1.turnos.schedule.Schedule;
import ar.uba.fi.ingsoft1.turnos.schedule.ScheduleBlock;
import ar.uba.fi.ingsoft1.turnos.schedule.ScheduleBlockRepository;
import ar.uba.fi.ingsoft1.turnos.schedule.ScheduleRepository;
import ar.uba.fi.ingsoft1.turnos.service.ServiceEntity;
import ar.uba.fi.ingsoft1.turnos.service.ServiceRepository;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AvailabilityServiceTest {

        private AvailabilityService availabilityService;
        private ScheduleRepository scheduleRepository;
        private ScheduleBlockRepository scheduleBlockRepository;
        private AppointmentRepository appointmentRepository;
        private ServiceRepository serviceRepository;
        private ProfessionalRepository professionalRepository;
        private WaitListPromotionRepository promotionRepository;

        @BeforeEach
        void setUp() {
                scheduleRepository = mock(ScheduleRepository.class);
                scheduleBlockRepository = mock(ScheduleBlockRepository.class);
                appointmentRepository = mock(AppointmentRepository.class);
                serviceRepository = mock(ServiceRepository.class);
                professionalRepository = mock(ProfessionalRepository.class);
                promotionRepository = mock(WaitListPromotionRepository.class);

                availabilityService = new AvailabilityService(
                        scheduleRepository,
                        scheduleBlockRepository,
                        appointmentRepository,
                        serviceRepository,
                        professionalRepository,
                        promotionRepository);

                when(scheduleBlockRepository.findByProfessionalIdAndBlockDateOrderByStartTimeAsc(any(), any()))
                        .thenReturn(List.of());
                when(promotionRepository.findActivePromotionsForDay(any(), any(), any(), any()))
                        .thenReturn(List.of());
        }
        private Appointment buildAppointment(LocalDate date, LocalTime start, LocalTime end, Long serviceId) throws Exception {
                Appointment appt = new Appointment();
                setField(appt, "serviceId", serviceId);
                setField(appt, "start", ZonedDateTime.of(date, start, ZoneId.systemDefault()));
                setField(appt, "end", ZonedDateTime.of(date, end, ZoneId.systemDefault()));
                return appt;
        }
        private void setField(Object target, String fieldName, Object value) throws Exception {
                Field field = target.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(target, value);
        }

        @Test
        void calculateAvailabilityFor60MinServiceWithOneObstacle() throws Exception {
                Long profId = 1L;
                Long serviceId = 100L;
                LocalDate testDate = LocalDate.now(ZoneId.systemDefault()).plusDays(7);

                Schedule mockSchedule = mock(Schedule.class);
                when(mockSchedule.getStart()).thenReturn(LocalTime.of(9, 0));
                when(mockSchedule.getEnd()).thenReturn(LocalTime.of(12, 0));
                when(scheduleRepository.findByProfessionalIdAndDayWeek(profId, testDate.getDayOfWeek().getValue()))
                                .thenReturn(List.of(mockSchedule));

                ServiceEntity mockService = mock(ServiceEntity.class);
                when(mockService.getDuration()).thenReturn(60);
                when(mockService.isActive()).thenReturn(true);
                when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(mockService));

                when(appointmentRepository.findActiveAppointments(eq(profId), any(), any()))
                        .thenReturn(List.of(
                                buildAppointment(testDate, LocalTime.of(10, 0), LocalTime.of(10, 30), serviceId)));

                AvailabilityResponseDTO response = availabilityService.getAvailability(profId, testDate, serviceId,
                                null);

                assertEquals(testDate, response.date());
                assertNotNull(response.slots());
                assertEquals(6, response.slots().size());

                assertTrue(response.slots().get(0).available(), "El slot de las 09:00 debería estar libre");
                assertFalse(response.slots().get(1).available(),
                                "El slot de las 09:30 debería estar ocupado por solapamiento");
                assertFalse(response.slots().get(2).available(), "El slot de las 10:00 está ocupado directamente");
                assertTrue(response.slots().get(3).available(), "El slot de las 10:30 debería estar libre");
                assertTrue(response.slots().get(4).available(), "El slot de las 11:00 debería estar libre");
                assertFalse(response.slots().get(5).available(),
                                "El slot de las 11:30 no tiene suficiente tiempo antes del fin de jornada");
        }

        @Test
        void returnsEmptySlotsWhenNoDaySchedule() {
                Long profId = 1L;
                Long serviceId = 100L;
                LocalDate testDate = LocalDate.now(ZoneId.systemDefault()).plusDays(7);

                when(scheduleRepository.findByProfessionalIdAndDayWeek(profId, testDate.getDayOfWeek().getValue()))
                                .thenReturn(List.of());

                AvailabilityResponseDTO response = availabilityService.getAvailability(profId, testDate, serviceId,
                                null);

                assertEquals(testDate, response.date());
                assertTrue(response.slots().isEmpty(), "Sin horario configurado no debe haber slots");
        }

        @Test
        void throwsWhenServiceIsInactive() {
                Long profId = 1L;
                Long serviceId = 100L;
                LocalDate testDate = LocalDate.now(ZoneId.systemDefault()).plusDays(7);

                Schedule mockSchedule = mock(Schedule.class);
                when(mockSchedule.getStart()).thenReturn(LocalTime.of(9, 0));
                when(mockSchedule.getEnd()).thenReturn(LocalTime.of(12, 0));
                when(scheduleRepository.findByProfessionalIdAndDayWeek(profId, testDate.getDayOfWeek().getValue()))
                                .thenReturn(List.of(mockSchedule));

                ServiceEntity mockService = mock(ServiceEntity.class);
                when(mockService.isActive()).thenReturn(false);
                when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(mockService));

                assertThrows(IllegalArgumentException.class,
                                () -> availabilityService.getAvailability(profId, testDate, serviceId, null),
                                "Debería lanzar excepción para un servicio inactivo");
        }

        @Test
        void calculateAvailabilityFor30MinService() {
                Long profId = 1L;
                Long serviceId = 100L;
                LocalDate testDate = LocalDate.now(ZoneId.systemDefault()).plusDays(7);

                Schedule mockSchedule = mock(Schedule.class);
                when(mockSchedule.getStart()).thenReturn(LocalTime.of(9, 0));
                when(mockSchedule.getEnd()).thenReturn(LocalTime.of(10, 0));
                when(scheduleRepository.findByProfessionalIdAndDayWeek(profId, testDate.getDayOfWeek().getValue()))
                                .thenReturn(List.of(mockSchedule));

                ServiceEntity mockService = mock(ServiceEntity.class);
                when(mockService.getDuration()).thenReturn(30);
                when(mockService.isActive()).thenReturn(true);
                when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(mockService));

                when(appointmentRepository.findActiveAppointments(eq(profId), any(), any()))
                                .thenReturn(List.of());

                AvailabilityResponseDTO response = availabilityService.getAvailability(profId, testDate, serviceId,
                                null);

                assertEquals(2, response.slots().size());
                assertTrue(response.slots().get(0).available(), "El slot de las 09:00 debería estar libre");
                assertTrue(response.slots().get(1).available(), "El slot de las 09:30 debería estar libre");
        }

        @Test
        void calculateAvailabilityMarksBlockedSlotsAsUnavailable() {
                Long profId = 1L;
                Long serviceId = 100L;
                LocalDate testDate = LocalDate.now(ZoneId.systemDefault()).plusDays(7);

                Schedule mockSchedule = mock(Schedule.class);
                when(mockSchedule.getStart()).thenReturn(LocalTime.of(9, 0));
                when(mockSchedule.getEnd()).thenReturn(LocalTime.of(12, 0));
                when(scheduleRepository.findByProfessionalIdAndDayWeek(profId, testDate.getDayOfWeek().getValue()))
                                .thenReturn(List.of(mockSchedule));

                ServiceEntity mockService = mock(ServiceEntity.class);
                when(mockService.getDuration()).thenReturn(60);
                when(mockService.isActive()).thenReturn(true);
                when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(mockService));

                ScheduleBlock block = new ScheduleBlock();
                block.setBlockDate(testDate);
                block.setStartTime(LocalTime.of(10, 0));
                block.setEndTime(LocalTime.of(16, 0));
                when(scheduleBlockRepository.findByProfessionalIdAndBlockDateOrderByStartTimeAsc(profId, testDate))
                                .thenReturn(List.of(block));

                when(appointmentRepository.findActiveAppointments(eq(profId), any(), any()))
                                .thenReturn(List.of());

                AvailabilityResponseDTO response = availabilityService.getAvailability(profId, testDate, serviceId,
                                null);

                assertEquals("blocked", response.slots().get(1).reason());
                assertFalse(response.slots().get(1).available());
        }
}