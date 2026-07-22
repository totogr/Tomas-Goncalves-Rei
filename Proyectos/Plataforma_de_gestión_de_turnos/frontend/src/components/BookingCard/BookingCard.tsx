import styles from "./BookingCard.module.css";
import { ClientBooking } from "@/models/Booking";

type Props = {
  booking: ClientBooking;
  onClick: () => void;
};

export const BookingCard = ({ booking, onClick }: Props) => {
  const dateObj = new Date(booking.date + "T00:00:00");
  const month = dateObj.toLocaleDateString("es-AR", { month: "short" });
  const day = dateObj.toLocaleDateString("es-AR", { day: "numeric" });

  return (
    <div className={styles.card}>
      <div className={styles.dateColumn}>
        <span className={styles.month}>{month}</span>
        <span className={styles.day}>{day}</span>
      </div>
      <div className={styles.infoColumn}>
        <h3 className={styles.serviceName}>{booking.service_name}</h3>
        <div className={styles.detailsInline}>
          <span className={styles.time}>{booking.time} hs</span>
          <span className={styles.detail}>{booking.professional_name}</span>
          {booking.location && <span className={styles.detail}>{booking.location}</span>}
        </div>
        {booking.status === "cancelled" && booking.cancelled_by && (
          <span className={styles.cancelledBy}>
            {booking.cancelled_by === "client" ? "Cancelado por vos" : "Cancelado por el profesional"}
          </span>
        )}
      </div>
      <div className={styles.actionColumn}>
        <button type="button" className={styles.detailButton} onClick={onClick}>
          Ver detalle
        </button>
      </div>
    </div>
  );
};