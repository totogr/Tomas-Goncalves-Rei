import styles from "./RescheduleBookingModal.module.css";

type Props = {
  isOpen: boolean;
  isLoading: boolean;
  serviceName: string;
  onConfirm: () => void;
  onClose: () => void;
};

export const RescheduleBookingModal = ({ isOpen, isLoading, serviceName, onConfirm, onClose }: Props) => {
  if (!isOpen) return null;

  return (
    <div className={styles.overlay} onClick={onClose}>
      <div className={styles.modal} onClick={(e) => e.stopPropagation()}>
        <div className={styles.iconWrapper}>
          <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
            <rect x="3" y="4" width="18" height="18" rx="2" />
            <path d="M16 2v4M8 2v4M3 10h18" />
            <path d="M17 14l-4 4-2-2" />
          </svg>
        </div>
        <h3 className={styles.title}>Reprogramar turno</h3>
        <p className={styles.description}>
          Tu turno actual de <strong>{serviceName}</strong> será cancelado y serás redirigido
          para elegir un nuevo horario. Esta acción no se puede deshacer.
        </p>
        <div className={styles.actions}>
          <button
            type="button"
            className={styles.confirmButton}
            onClick={onConfirm}
            disabled={isLoading}
          >
            {isLoading ? "Procesando..." : "Sí, reprogramar"}
          </button>
          <button
            type="button"
            className={styles.cancelButton}
            onClick={onClose}
            disabled={isLoading}
          >
            Mantener reserva
          </button>
        </div>
      </div>
    </div>
  );
};