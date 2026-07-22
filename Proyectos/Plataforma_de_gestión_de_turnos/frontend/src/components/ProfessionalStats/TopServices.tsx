import { ServiceStat } from "@/models/Stats";
import styles from "./ProfessionalStats.module.css";

type Props = {
  data: ServiceStat[];
};

const COLORS = ["#2d6a4f", "#40916c", "#74c69d", "#d8f3dc"];

export const TopServices = ({ data }: Props) => {
  const total = data.reduce((sum, s) => sum + s.count, 0);
  let cumulative = 0;
  const segments = data.map((s, i) => {
    const start = cumulative;
    cumulative += (s.count / (total || 1)) * 360;
    return { ...s, startAngle: start, endAngle: cumulative, color: COLORS[i] ?? "#e5e7eb" };
  });

  const polarToCartesian = (cx: number, cy: number, r: number, angle: number) => {
    const rad = ((angle - 90) * Math.PI) / 180;
    return { x: cx + r * Math.cos(rad), y: cy + r * Math.sin(rad) };
  };

  const describeArc = (cx: number, cy: number, r: number, start: number, end: number) => {
    if (end - start >= 360) end = 359.99;
    const s = polarToCartesian(cx, cy, r, start);
    const e = polarToCartesian(cx, cy, r, end);
    const large = end - start > 180 ? 1 : 0;
    return `M ${s.x} ${s.y} A ${r} ${r} 0 ${large} 1 ${e.x} ${e.y}`;
  };

  return (
    <div className={styles.card}>
      <h3 className={styles.cardTitle}>Servicios más solicitados</h3>
      <p className={styles.cardSubtitle}>Distribución del período</p>
      <div className={styles.servicesLayout}>
        <svg viewBox="0 0 100 100" className={styles.donut}>
          {segments.map((s, i) =>
            s.count > 0 ? (
              <path
                key={i}
                d={describeArc(50, 50, 35, s.startAngle, s.endAngle)}
                fill="none"
                stroke={s.color}
                strokeWidth="18"
              />
            ) : null
          )}
          {total === 0 && (
            <circle cx="50" cy="50" r="35" fill="none" stroke="#e5e7eb" strokeWidth="18" />
          )}
        </svg>
        <ul className={styles.servicesList}>
          {data.map((s, i) => (
            <li key={s.name} className={styles.servicesItem}>
              <span className={styles.serviceDot} style={{ background: COLORS[i] }} />
              <span className={styles.serviceName}>{s.name}</span>
              <span className={styles.servicePct}>{s.percentage}%</span>
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
};