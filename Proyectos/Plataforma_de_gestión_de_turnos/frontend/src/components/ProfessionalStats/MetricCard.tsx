import styles from "./ProfessionalStats.module.css";

type Props = {
  label: string;
  value: string;
  highlightFirst?: boolean;
};

export const MetricCard = ({ label, value, highlightFirst }: Props) => {
  const parts = highlightFirst ? value.split(/(?<=^\S+)\s/) : null;

  return (
    <div className={styles.metricCard}>
      <p className={styles.metricLabel}>{label}</p>
      <p className={styles.metricValue}>
        {parts ? (
          <>
            <span className={styles.metricHighlight}>{parts[0]}</span>
            {parts[1] ? ` ${parts[1]}` : ""}
          </>
        ) : (
          value
        )}
      </p>
    </div>
  );
};