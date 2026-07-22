import styles from "./CancelBookingModal.module.css";

type Props = {
  isOpen: boolean;
  isCancelling: boolean;
  onConfirm: () => void;
  onClose: () => void;
};

export const CancelBookingModal = ({ isOpen, isCancelling, onConfirm, onClose }: Props) => {
  if (!isOpen) return null;

  return (
    <div className={styles.overlay} onClick={onClose}>
      <div className={styles.modal} onClick={(e) => e.stopPropagation()}>
        <div className={styles.iconWrapper}>
          <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
            <path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z"/>
            <line x1="12" y1="9" x2="12" y2="13"/>
            <line x1="12" y1="17" x2="12.01" y2="17"/>
          </svg>
        </div>
        <h3 className={styles.title}>Cancelar turno</h3>
        <p className={styles.description}>
          ¿Estás seguro que querés cancelar este turno? Esta acción no se puede deshacer.
        </p>
        <div className={styles.actions}>
          <button type="button" className={styles.cancelButton} onClick={onConfirm} disabled={isCancelling}>
            {isCancelling ? "Cancelando..." : "Sí, cancelar turno"}
          </button>
          <button type="button" className={styles.keepButton} onClick={onClose} disabled={isCancelling}>
            Mantener reserva
          </button>
        </div>
      </div>
    </div>
  );
};