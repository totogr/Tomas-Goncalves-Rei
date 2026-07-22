import { useState } from "react";
import { AvailabilitySlot } from "@/models/AvailabilitySlot";
import { SlotButton } from "../WaitList/SlotButton";
import { WaitListSlotsModal } from "../WaitList/WaitListSlotsModal";
import { formatHumanDate } from "./calendarUtils";
import styles from "./BookingCalendar.module.css";

type Props = {
    slots: AvailabilitySlot[] | null;
    selectedSlot: string | null;
    onSelectSlot: (time: string) => void;
    loading: boolean;
    date: string | null;
    professionalId: number;
    serviceId: number;
    serviceDuration: number;
};

export const TimeSlots = ({
                              slots,
                              selectedSlot,
                              onSelectSlot,
                              loading,
                              date,
                              professionalId,
                              serviceId,
                              serviceDuration,
                          }: Props) => {
    const [showWaitListModal, setShowWaitListModal] = useState(false);

    if (!date) {
        return <div className={styles.slotsEmpty}>Elegí una fecha para ver los horarios.</div>;
    }
    if (loading) {
        return <div className={styles.slotsEmpty}>Cargando horarios...</div>;
    }

    const allSlots = slots ?? [];
    const now = new Date();

    const futureSlots = allSlots.filter((slot) => {
        const slotStart = `${date}T${slot.time}:00-03:00`;
        return new Date(slotStart) >= now && slot.reason !== "blocked";
    });

    const slotTimes = futureSlots.map((slot) => {
        const [h, m] = slot.time.split(":").map(Number);
        return h * 60 + m;
    });

    const granularity = slotTimes.length >= 2 ? slotTimes[1] - slotTimes[0] : 15;
    const lastSlotStart = slotTimes.length > 0 ? slotTimes[slotTimes.length - 1] : 0;

    const fitsWithinDay = (_slot: AvailabilitySlot, index: number) => {
        return slotTimes[index] + serviceDuration <= lastSlotStart + granularity;
    };

    const availableSlots = futureSlots.filter((slot, i) => slot.available && fitsWithinDay(slot, i));
    const fullSlots = futureSlots.filter((slot, i) => !slot.available && slot.reason === "full" && fitsWithinDay(slot, i));

    if (futureSlots.length === 0) {
        return <div className={styles.slotsEmpty}>No hay horarios disponibles para esa fecha.</div>;
    }

    return (
        <div>
            <div className={styles.slotsHeader}>
                Horarios disponibles – {formatHumanDate(date)}
            </div>
            {availableSlots.length === 0 ? (
                <div className={styles.slotsEmpty}>No quedan horarios libres para esta fecha.</div>
            ) : (
                <div className={styles.slotsGrid}>
                    {availableSlots.map((slot) => (
                        <SlotButton
                            key={slot.time}
                            time={slot.time}
                            isSelected={slot.time === selectedSlot}
                            onSelect={() => onSelectSlot(slot.time)}
                        />
                    ))}
                </div>
            )}
            {fullSlots.length > 0 && (
                <div className={styles.waitListSection}>
                    <button
                        type="button"
                        className={styles.waitListBtn}
                        onClick={() => setShowWaitListModal(true)}
                    >
                        Anotarme en lista de espera
                    </button>
                </div>
            )}
            {showWaitListModal && (
                <WaitListSlotsModal
                    date={date}
                    fullSlots={fullSlots}
                    professionalId={professionalId}
                    serviceId={serviceId}
                    onClose={() => setShowWaitListModal(false)}
                />
            )}
        </div>
    );
};