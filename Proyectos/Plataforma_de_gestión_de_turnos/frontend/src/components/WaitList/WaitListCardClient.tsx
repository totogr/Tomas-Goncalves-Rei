import { WaitListEntry } from "@/models/WaitList";
import { formatHumanDate } from "../BookingCalendar/calendarUtils";
import styles from "./WaitListCard.module.css";

type WaitListCardProps = {
    entry: WaitListEntry;
    onLeave: (entry: WaitListEntry) => void;
};

export const WaitListCard = ({ entry, onLeave }: WaitListCardProps) => {
    const date = new Date(entry.slotStart);
    const dateStr = date.toLocaleDateString("es-AR", { timeZone: "America/Argentina/Buenos_Aires", year: "numeric", month: "2-digit", day: "2-digit" }).split("/").reverse().join("-");
    const timeStr = date.toLocaleTimeString("es-AR", { timeZone: "America/Argentina/Buenos_Aires", hour: "2-digit", minute: "2-digit", hour12: false });

    return (
        <div className={styles.card}>
            <div className={styles.info}>
                <span className={styles.badge}>En Lista de Espera #{entry.position}</span>
                <h3 className={styles.serviceName}>{entry.serviceName}</h3>
                <p className={styles.professional}>Con {entry.professionalName}</p>
                <p className={styles.dateTime}>
                    {formatHumanDate(dateStr)} a las {timeStr} hs
                </p>
            </div>
            <div className={styles.actions}>
                <button
                    type="button"
                    className={styles.leaveButton}
                    onClick={() => onLeave(entry)}
                >
                    Dejar de esperar
                </button>
            </div>
        </div>
    );
};