package ar.uba.fi.ingsoft1.turnos.review;

import ar.uba.fi.ingsoft1.turnos.appointment.Appointment;
import ar.uba.fi.ingsoft1.turnos.appointment.AppointmentRepository;
import ar.uba.fi.ingsoft1.turnos.common.exception.BadRequestException;
import ar.uba.fi.ingsoft1.turnos.common.exception.ConflictException;
import ar.uba.fi.ingsoft1.turnos.common.exception.ForbiddenException;
import ar.uba.fi.ingsoft1.turnos.common.exception.ItemNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final AppointmentRepository appointmentRepository;

    public ReviewService(ReviewRepository reviewRepository,
            AppointmentRepository appointmentRepository) {
        this.reviewRepository = reviewRepository;
        this.appointmentRepository = appointmentRepository;
    }

    public Review createReview(Long appointmentId, Long clientId, ReviewRequestDTO request) {
        Appointment appt = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ItemNotFoundException("Turno no encontrado"));

        if (!appt.getClientId().equals(clientId)) {
            throw new ForbiddenException("El turno no pertenece al cliente autenticado");
        }

        if (!"COMPLETED".equals(appt.getStatus())) {
            throw new BadRequestException("Solo se pueden calificar turnos completados");
        }

        if (reviewRepository.existsByAppointmentId(appointmentId)) {
            throw new ConflictException("Este turno ya fue calificado");
        }

        Review review = new Review();
        review.setAppointmentId(appointmentId);
        review.setClientId(clientId);
        review.setProfessionalId(appt.getProfessionalId());
        review.setScore(request.score());

        return reviewRepository.save(review);
    }
}