import { useLocation } from "wouter";

import { formatHumanDate } from "@/components/BookingCalendar/calendarUtils";
import { Professional } from "@/models/Professional";
import { Service } from "@/models/Service";

import styles from "./BookingSuccess.module.css";

type Props = {
  professional: Professional;
  service: Service;
  date: string;
  time: string;
  onViewMore: () => void;
};

export const BookingSuccess = ({ professional, service, date, time, onViewMore }: Props) => {
  const [, navigate] = useLocation();

  return (
    <div className={styles.card}>
      <div className={styles.iconWrapper}>
        <svg width="36" height="36" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
          <path d="M20 6L9 17l-5-5" />
        </svg>
      </div>

      <h2 className={styles.title}>¡Reserva confirmada!</h2>
      <p className={styles.subtitle}>Tu turno fue agendado con éxito.</p>

      <dl className={styles.details}>
        <DetailRow label="Profesional" value={`${professional.firstName} ${professional.lastName}`} />
        <DetailRow label="Servicio" value={service.name} />
        <DetailRow label="Fecha" value={formatHumanDate(date)} />
        <DetailRow label="Hora" value={`${time} hs`} />
        <DetailRow label="Duración" value={`${service.duration_minutes} minutos`} />
      </dl>

      <div className={styles.actions}>
        <button
          type="button"
          className={styles.primaryButton}
          onClick={onViewMore}
        >
          Ver más servicios
        </button>
        <button
          type="button"
          className={styles.secondaryButton}
          onClick={() => navigate("/professionals")}
        >
          Ver otros profesionales
        </button>
      </div>
    </div>
  );
};

const DetailRow = ({ label, value }: { label: string; value: string }) => (
  <div className={styles.detailRow}>
    <dt className={styles.detailLabel}>{label}</dt>
    <dd className={styles.detailValue}>{value}</dd>
  </div>
);
