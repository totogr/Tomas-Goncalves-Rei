import { useState } from "react";
import { AvailabilitySlot } from "@/models/AvailabilitySlot";
import { WaitListModal } from "./WaitListModal";
import styles from "./WaitListSlotsModal.module.css";

type Props = {
    date: string;
    fullSlots: AvailabilitySlot[];
    professionalId: number;
    serviceId: number;
    onClose: () => void;
};

export const WaitListSlotsModal = ({ date, fullSlots, professionalId, serviceId, onClose }: Props) => {
    const [selectedSlot, setSelectedSlot] = useState<AvailabilitySlot | null>(null);

    if (selectedSlot) {
        const slotStart = `${date}T${selectedSlot.time}:00-03:00`;
        return (
            <WaitListModal
                time={selectedSlot.time}
                slotStart={slotStart}
                professionalId={professionalId}
                serviceId={serviceId}
                onClose={onClose}
            />
        );
    }

    return (
        <div className={styles.overlay} onClick={onClose}>
            <div className={styles.modal} onClick={(e) => e.stopPropagation()}>
                <button className={styles.closeBtn} onClick={onClose} aria-label="Cerrar">×</button>
                <h2 className={styles.title}>Lista de espera</h2>
                <p className={styles.subtitle}>
                    Elegí el horario en el que te gustaría anotarte:
                </p>
                <div className={styles.slotsGrid}>
                    {fullSlots.map((slot) => (
                        <button
                            key={slot.time}
                            type="button"
                            className={styles.slotBtn}
                            onClick={() => setSelectedSlot(slot)}
                        >
                            {slot.time}
                        </button>
                    ))}
                </div>
            </div>
        </div>
    );
};