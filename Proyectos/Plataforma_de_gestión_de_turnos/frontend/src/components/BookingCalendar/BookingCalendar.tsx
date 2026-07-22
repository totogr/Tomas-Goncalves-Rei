import { Service } from "@/models/Service";
import { useAvailability } from "@/services/AvailabilityServices";

import { Calendar } from "./Calendar";
import { TimeSlots } from "./TimeSlots";
import styles from "./BookingCalendar.module.css";

type Props = {
  professionalId: number;
  service: Service;
  selectedDate: string | null;
  selectedSlot: string | null;
  onSelectDate: (date: string) => void;
  onSelectSlot: (slot: string) => void;
};

export const BookingCalendar = ({
  professionalId,
  service,
  selectedDate,
  selectedSlot,
  onSelectDate,
  onSelectSlot,
}: Props) => {
  const availabilityQuery = useAvailability(professionalId, service.id, selectedDate);

  return (
    <section className={styles.section}>
      <h2 className={styles.sectionTitle}>Elegí fecha y horario</h2>

      <div className={styles.calendarLayout}>
        <Calendar selectedDate={selectedDate} onSelectDate={onSelectDate} />

        <div className={styles.slotsContainer}>
          <TimeSlots
            slots={availabilityQuery.data ?? null}
            selectedSlot={selectedSlot}
            onSelectSlot={onSelectSlot}
            loading={availabilityQuery.isLoading}
            date={selectedDate}
            professionalId={professionalId}
            serviceId={Number(service.id ?? 0)}
            serviceDuration={service.duration_minutes}
          />
        </div>
      </div>
    </section>
  );
};
