import { useState } from "react";
import { useLocation } from "wouter";
import { CancelBookingModal } from "@/components/CancelBookingModal/CancelBookingModal";
import { RescheduleBookingModal } from "@/components/RescheduleBookingModal/RescheduleBookingModal";
import { BookingDetail as BookingDetailType } from "@/models/Booking";
import { useSubmitReview } from "@/services/BookingServices";
import styles from "./BookingDetail.module.css";

type Props = {
  booking: BookingDetailType | undefined;
  isLoading: boolean;
  isError: boolean;
  isCancelling: boolean;
  isRescheduling: boolean;
  onCancel: (id: string) => void;
  onReschedule: (id: string) => void;
};

const STATUS_LABELS: Record<string, string> = {
  confirmed: "Confirmado",
  completed: "Completado",
  cancelled: "Cancelado",
};

export const BookingDetail = ({
  booking,
  isLoading,
  isError,
  isCancelling,
  isRescheduling,
  onCancel,
  onReschedule,
}: Props) => {
  const [, navigate] = useLocation();
  const [showCancelModal, setShowCancelModal] = useState(false);
  const [showRescheduleModal, setShowRescheduleModal] = useState(false);
  const [hoveredStar, setHoveredStar] = useState<number | null>(null);
  const submitReview = useSubmitReview();

  if (isLoading) return <div className={styles.centerMsg}>Cargando detalle...</div>;
  if (isError || !booking) return <div className={styles.centerMsg}>Error al cargar el turno.</div>;

  const dateObj = new Date(booking.date + "T00:00:00");
  const fullDate = dateObj.toLocaleDateString("es-AR", {
    weekday: "long", day: "numeric", month: "long", year: "numeric",
  });
  const startTime = booking.start.split("T")[1]?.substring(0, 5) ?? "";
  const endTime = booking.end.split("T")[1]?.substring(0, 5) ?? "";
  const isFuture = new Date(booking.start) > new Date();

  const isCompleted = booking.status === "completed";
  const alreadyReviewed = booking.score != null;

  const handleStarClick = (score: number) => {
    if (alreadyReviewed || submitReview.isPending) return;
    submitReview.mutate({ appointmentId: booking.id, score });
  };

  return (
    <>
      <div className={styles.page}>
        <button type="button" className={styles.backButton} onClick={() => navigate("/bookings/me")}>
          ← Volver a Mis turnos
        </button>

        <div className={styles.card}>
          <div className={styles.cardHeader}>
            <h2 className={styles.cardTitle}>Detalle de tu reserva</h2>
            <span className={`${styles.badge} ${styles[booking.status]}`}>
              {STATUS_LABELS[booking.status] ?? booking.status}
            </span>
          </div>

          <div className={styles.divider} />

          {booking.status === "cancelled" && booking.cancelled_by && (
            <p className={styles.cancelledByNote}>
              {booking.cancelled_by === "client"
                ? "Cancelaste este turno vos mismo."
                : "Este turno fue cancelado por el profesional."}
            </p>
          )}

          <div className={styles.grid}>
            <div className={styles.infoSection}>
              <p className={styles.sectionLabel}>Servicio</p>
              <p className={styles.primaryText}>{booking.service.name}</p>
              <p className={styles.secondaryText}>Duración: {booking.service.duration_minutes} min</p>
              <p className={styles.price}>
                {new Intl.NumberFormat("es-AR", {
                  style: "currency", currency: "ARS", maximumFractionDigits: 0,
                }).format(booking.service.price)}
              </p>
            </div>

            <div className={styles.infoSection}>
              <p className={styles.sectionLabel}>Fecha y hora</p>
              <p className={styles.primaryText} style={{ textTransform: "capitalize" }}>{fullDate}</p>
              <p className={styles.secondaryText}>{startTime} a {endTime} hs</p>
            </div>

            <div className={styles.infoSection}>
              <p className={styles.sectionLabel}>Profesional</p>
              <p className={styles.primaryText}>
                {booking.professional.first_name} {booking.professional.last_name}
              </p>
            </div>
          </div>

          {/* ── calificación ── */}
          {isCompleted && (
            <>
              <div className={styles.divider} />
              <div className={styles.reviewSection}>
                <p className={styles.sectionLabel}>
                  {alreadyReviewed ? "Tu calificación" : "Calificá este turno"}
                </p>
                <div className={styles.stars}>
                  {[1, 2, 3, 4, 5].map((star) => {
                    const filled = alreadyReviewed
                      ? star <= (booking.score ?? 0)
                      : star <= (hoveredStar ?? 0);
                    return (
                      <button
                        key={star}
                        type="button"
                        className={`${styles.star} ${filled ? styles.starFilled : styles.starEmpty}`}
                        onClick={() => handleStarClick(star)}
                        onMouseEnter={() => !alreadyReviewed && setHoveredStar(star)}
                        onMouseLeave={() => !alreadyReviewed && setHoveredStar(null)}
                        disabled={alreadyReviewed || submitReview.isPending}
                        aria-label={`${star} estrellas`}
                      >
                        ★
                      </button>
                    );
                  })}
                </div>
                {submitReview.isError && (
                  <p className={styles.reviewError}>No se pudo enviar la calificación. Intentá de nuevo.</p>
                )}
              </div>
            </>
          )}

          {booking.status === "confirmed" && isFuture && (
            <div className={styles.actions}>
              <button
                type="button"
                className={styles.rescheduleButton}
                onClick={() => setShowRescheduleModal(true)}
              >
                Reprogramar turno
              </button>
              <button
                type="button"
                className={styles.cancelButton}
                onClick={() => setShowCancelModal(true)}
              >
                Cancelar turno
              </button>
            </div>
          )}
        </div>
      </div>

      <CancelBookingModal
        isOpen={showCancelModal}
        isCancelling={isCancelling}
        onConfirm={() => onCancel(booking.id)}
        onClose={() => setShowCancelModal(false)}
      />

      <RescheduleBookingModal
        isOpen={showRescheduleModal}
        isLoading={isRescheduling}
        serviceName={booking.service.name}
        onConfirm={() => onReschedule(booking.id)}
        onClose={() => setShowRescheduleModal(false)}
      />
    </>
  );
};