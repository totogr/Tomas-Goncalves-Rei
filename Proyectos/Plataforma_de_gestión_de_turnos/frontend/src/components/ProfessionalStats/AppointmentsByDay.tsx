import { DayStat } from "@/models/Stats";
import styles from "./ProfessionalStats.module.css";

type Props = {
  data: DayStat[];
};

export const AppointmentsByDay = ({ data }: Props) => {
  const max = Math.max(...data.map((d) => d.count), 1);

  return (
    <div className={styles.card}>
      <h3 className={styles.cardTitle}>Turnos por día de la semana</h3>
      <p className={styles.cardSubtitle}>Promedio del período</p>
      <div className={styles.barChart}>
        {data.map((d) => (
          <div key={d.day} className={styles.barGroup}>
            <div className={styles.barTrack}>
              <div
                className={styles.bar}
                style={{ height: `${(d.count / max) * 100}%` }}
              />
            </div>
            <span className={styles.barLabel}>{d.day}</span>
          </div>
        ))}
      </div>
    </div>
  );
};