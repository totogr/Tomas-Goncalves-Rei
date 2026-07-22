import styles from "./WaitListModal.module.css";
import { useJoinWaitList, useMyWaitListPosition } from "@/services/WaitListService";

type Props = {
    time: string;
    slotStart: string;
    professionalId: number;
    serviceId: number;
    onClose: () => void;
};

export const WaitListModal = ({ time, slotStart, professionalId, serviceId, onClose }: Props) => {
    const params = { professionalId, serviceId, slotStart };
    const { data: myEntry, isLoading } = useMyWaitListPosition(params);
    const { mutate: joinWaitList, isPending: joining, isError: joinError, error: joinErrorObj } = useJoinWaitList();

    const alreadyInQueue = myEntry != null;

    const errorMessage = joinErrorObj instanceof Error
        ? joinErrorObj.message
        : "Ocurrió un error. Intentá de nuevo.";

    return (
        <div className={styles.overlay} onClick={onClose}>
            <div className={styles.modal} onClick={(e) => e.stopPropagation()}>
                <button className={styles.closeBtn} onClick={onClose} aria-label="Cerrar">×</button>
                <div className={styles.iconWrapper}>⏳</div>
                <h2 className={styles.title}>Cupo lleno</h2>
                <p className={styles.subtitle}>
                    El turno de las <strong>{time} hs</strong> no tiene lugares disponibles.
                </p>

                {isLoading && (
                    <p className={styles.checking}>Verificando...</p>
                )}

                {!isLoading && alreadyInQueue && (
                    <div className={styles.infoBox}>
                        <p>
                            Ya estás anotado en la lista de espera.{" "}
                            <span className={styles.position}>Posición #{myEntry.position}</span>
                        </p>
                    </div>
                )}

                {!isLoading && !alreadyInQueue && (
                    <>
                        <p className={styles.description}>
                            Anotate en la lista de espera y te notificamos automáticamente si se libera un lugar.
                        </p>
                        {joinError && (
                            <p className={styles.errorMsg}>{errorMessage}</p>
                        )}
                        {!joinError && (
                            <button
                                className={styles.joinBtn}
                                onClick={() => joinWaitList(params)}
                                disabled={joining}
                            >
                                {joining ? "Anotándote..." : "Unirse a lista de espera"}
                            </button>
                        )}
                    </>
                )}
            </div>
        </div>
    );
};