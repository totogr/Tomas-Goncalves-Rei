import { ClientStat } from "@/models/Stats";
import styles from "./ProfessionalStats.module.css";

type Props = {
  data: ClientStat[];
};

const getInitials = (name: string) =>
  name.split(" ").map((n) => n[0]).join("").toUpperCase().slice(0, 2);

const AVATAR_COLORS = ["#2d6a4f", "#1d3557", "#9b2226", "#6a4c93", "#b5838d"];

export const FrequentClients = ({ data }: Props) => (
  <div className={styles.card}>
    <h3 className={styles.cardTitle}>Clientes frecuentes</h3>
    <p className={styles.cardSubtitle}>Top clientes por visitas en el período</p>

    {data.length === 0 ? (
      <p className={styles.emptyMsg}>No hay datos para este período.</p>
    ) : (
      <table className={styles.clientTable}>
        <thead>
          <tr>
            <th>CLIENTE</th>
            <th>VISITAS</th>
            <th>ACTIVIDAD</th>
            <th>ESTADO</th>
          </tr>
        </thead>
        <tbody>
          {data.map((c, i) => (
            <tr key={c.name}>
              <td>
                <div className={styles.clientName}>
                  <span
                    className={styles.avatar}
                    style={{ background: AVATAR_COLORS[i % AVATAR_COLORS.length] }}
                  >
                    {getInitials(c.name)}
                  </span>
                  {c.name}
                </div>
              </td>
              <td className={styles.visits}>{c.visits} visitas</td>
              <td>
                <div className={styles.activityTrack}>
                  <div
                    className={styles.activityBar}
                    style={{ width: `${Math.min((c.visits / 10) * 100, 100)}%` }}
                  />
                </div>
              </td>
              <td>
                {c.cancellations > 0 ? (
                  <span className={styles.badgeCancellations}>
                    {c.cancellations} {c.cancellations === 1 ? "cancelación propia" : "cancelaciones propias"}
                  </span>
                ) : c.status === "frequent" ? (
                  <span className={styles.badgeFrequent}>⭐ Frecuente</span>
                ) : (
                  <span className={styles.badgeOccasional}>Ocasional</span>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    )}
  </div>
);