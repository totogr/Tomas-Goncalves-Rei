package ar.uba.fi.ingsoft1.turnos.review;

import ar.uba.fi.ingsoft1.turnos.appointment.Appointment;
import ar.uba.fi.ingsoft1.turnos.appointment.AppointmentRepository;
import ar.uba.fi.ingsoft1.turnos.common.exception.BadRequestException;
import ar.uba.fi.ingsoft1.turnos.common.exception.ConflictException;
import ar.uba.fi.ingsoft1.turnos.common.exception.ForbiddenException;
import ar.uba.fi.ingsoft1.turnos.common.exception.ItemNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReviewServiceTest {

    private ReviewService reviewService;
    private ReviewRepository reviewRepository;
    private AppointmentRepository appointmentRepository;

    @BeforeEach
    void setUp() {
        reviewRepository = mock(ReviewRepository.class);
        appointmentRepository = mock(AppointmentRepository.class);
        reviewService = new ReviewService(reviewRepository, appointmentRepository);
    }

    private Appointment buildAppointment(String status, Long clientId, Long profId) throws Exception {
        Appointment appt = new Appointment();
        setField(appt, "id", 1L);
        setField(appt, "status", status);
        setField(appt, "clientId", clientId);
        setField(appt, "professionalId", profId);
        setField(appt, "start", ZonedDateTime.of(
                LocalDate.now(ZoneId.systemDefault()).minusDays(1),
                LocalTime.of(10, 0), ZoneId.systemDefault()));
        setField(appt, "end", ZonedDateTime.of(
                LocalDate.now(ZoneId.systemDefault()).minusDays(1),
                LocalTime.of(11, 0), ZoneId.systemDefault()));
        return appt;
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void throwsWhenAppointmentNotFound() {
        when(appointmentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class,
                () -> reviewService.createReview(99L, 1L, new ReviewRequestDTO(5)));
    }

    @Test
    void throwsWhenAppointmentBelongsToDifferentClient() throws Exception {
        Appointment appt = buildAppointment("COMPLETED", 2L, 1L);
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appt));

        assertThrows(ForbiddenException.class,
                () -> reviewService.createReview(1L, 1L, new ReviewRequestDTO(5)));
    }

    @Test
    void throwsWhenAppointmentIsNotCompleted() throws Exception {
        Appointment appt = buildAppointment("CONFIRMED", 1L, 1L);
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appt));

        assertThrows(BadRequestException.class,
                () -> reviewService.createReview(1L, 1L, new ReviewRequestDTO(5)));
    }

    @Test
    void throwsWhenReviewAlreadyExists() throws Exception {
        Appointment appt = buildAppointment("COMPLETED", 1L, 1L);
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appt));
        when(reviewRepository.existsByAppointmentId(1L)).thenReturn(true);

        assertThrows(ConflictException.class,
                () -> reviewService.createReview(1L, 1L, new ReviewRequestDTO(5)));
    }

    @Test
    void createsReviewSuccessfully() throws Exception {
        Appointment appt = buildAppointment("COMPLETED", 1L, 1L);
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appt));
        when(reviewRepository.existsByAppointmentId(1L)).thenReturn(false);
        when(reviewRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Review result = reviewService.createReview(1L, 1L, new ReviewRequestDTO(4));

        assertEquals(4, result.getScore());
        assertEquals(1L, result.getClientId());
        assertEquals(1L, result.getProfessionalId());
        assertEquals(1L, result.getAppointmentId());
        verify(reviewRepository).save(any());
    }

    @Test
    void cancelledAppointmentCannotBeReviewed() throws Exception {
        Appointment appt = buildAppointment("CANCELLED", 1L, 1L);
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appt));

        assertThrows(BadRequestException.class,
                () -> reviewService.createReview(1L, 1L, new ReviewRequestDTO(5)));
    }

    @Test
    void scoreIsPersistedCorrectly() throws Exception {
        Appointment appt = buildAppointment("COMPLETED", 1L, 1L);
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appt));
        when(reviewRepository.existsByAppointmentId(1L)).thenReturn(false);
        when(reviewRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Review result = reviewService.createReview(1L, 1L, new ReviewRequestDTO(1));
        assertEquals(1, result.getScore());

        Review result5 = reviewService.createReview(1L, 1L, new ReviewRequestDTO(5));
        assertEquals(5, result5.getScore());
    }
}