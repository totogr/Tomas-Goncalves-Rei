import styles from "./LeaveWaitListModal.module.css";

type Props = {
  isOpen: boolean;
  isLeaving: boolean;
  serviceName: string;
  professionalName: string;
  onConfirm: () => void;
  onClose: () => void;
};

export const LeaveWaitListModal = ({
  isOpen,
  isLeaving,
  serviceName,
  professionalName,
  onConfirm,
  onClose,
}: Props) => {
  if (!isOpen) return null;

  return (
    <div className={styles.overlay} onClick={onClose}>
      <div className={styles.modal} onClick={(e) => e.stopPropagation()}>
        <div className={styles.iconWrapper}>
          <i className="ti ti-clock-off" aria-hidden="true" />
        </div>
        <h3 className={styles.title}>Salir de la lista de espera</h3>
        <p className={styles.description}>
          ¿Querés salir de la lista de espera para{" "}
          <strong>{serviceName}</strong> con{" "}
          <strong>{professionalName}</strong>?
          Perdés tu lugar en la fila.
        </p>
        <div className={styles.actions}>
          <button
            type="button"
            className={styles.confirmButton}
            onClick={onConfirm}
            disabled={isLeaving}
          >
            {isLeaving ? "Saliendo..." : "Sí, salir de la lista"}
          </button>
          <button
            type="button"
            className={styles.cancelButton}
            onClick={onClose}
            disabled={isLeaving}
          >
            Cancelar
          </button>
        </div>
      </div>
    </div>
  );
};
