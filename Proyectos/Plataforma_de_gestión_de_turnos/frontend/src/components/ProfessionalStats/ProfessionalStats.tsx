import { Stats, StatsPeriod, PERIOD_LABELS } from "@/models/Stats";
import { MetricCard } from "./MetricCard";
import { AppointmentsByDay } from "./AppointmentsByDay";
import { TopServices } from "./TopServices";
import { FrequentClients } from "./FrequentClients";
import styles from "./ProfessionalStats.module.css";

type Props = {
  stats: Stats | undefined;
  isLoading: boolean;
  period: StatsPeriod;
  onPeriodChange: (p: StatsPeriod) => void;
};

export const ProfessionalStats = ({ stats, isLoading, period, onPeriodChange }: Props) => (
  <div className={styles.page}>
    <div className={styles.header}>
      <div>
        <h1 className={styles.title}>Estadísticas</h1>
        <p className={styles.subtitle}>Rendimiento de tu negocio</p>
      </div>
      <select
        className={styles.periodSelect}
        value={period}
        onChange={(e) => onPeriodChange(e.target.value as StatsPeriod)}
      >
        {(Object.keys(PERIOD_LABELS) as StatsPeriod[]).map((p) => (
          <option key={p} value={p}>{PERIOD_LABELS[p]}</option>
        ))}
      </select>
    </div>

    {isLoading || !stats ? (
      <div className={styles.loading}>Cargando estadísticas...</div>
    ) : (
      <>
        <div className={styles.metricsGrid}>
          <MetricCard label="TURNOS TOTALES" value={String(stats.total_appointments)} />
          <MetricCard label="CALIFICACIÓN" value={stats.average_rating != null ? `★ ${stats.average_rating.toFixed(1)}` : "Sin calificaciones"} 
            highlightFirst={stats.average_rating != null} />
          <MetricCard label="CANCELACIONES" value={`${stats.cancellation_rate}%`} />
          <MetricCard
            label="INGRESOS EST."
            value={new Intl.NumberFormat("es-AR", {
              style: "currency", currency: "ARS", maximumFractionDigits: 0,
            }).format(stats.estimated_revenue)}
          />
        </div>

        <div className={styles.chartsGrid}>
          <AppointmentsByDay data={stats.appointments_by_day} />
          <TopServices data={stats.top_services} />
        </div>

        <FrequentClients data={stats.frequent_clients} />
      </>
    )}
  </div>
);