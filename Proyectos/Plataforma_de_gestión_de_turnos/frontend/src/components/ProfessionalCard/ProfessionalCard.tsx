import { ProfessionalSummary } from "@/models/Professional";
import styles from "./ProfessionalCard.module.css";

type Props = {
  professional: ProfessionalSummary;
  onClick: () => void;
};

export const ProfessionalCard = ({ professional, onClick }: Props) => {
  const initials = getInitials(professional.firstName, professional.lastName);

  return (
    <button type="button" className={styles.card} onClick={onClick}>
      <div className={styles.avatar}>{initials}</div>
      <div className={styles.info}>
        <span className={styles.name}>
          {professional.firstName} {professional.lastName}
        </span>
        {professional.specialty && (
          <span className={styles.specialty}>{professional.specialty}</span>
        )}
        {professional.rating !== null && professional.rating !== undefined && (
          <span className={styles.rating}>★ {professional.rating.toFixed(1)}</span>
        )}
      </div>
      <span className={styles.arrow}>›</span>
    </button>
  );
};

const getInitials = (firstName: string, lastName: string) =>
  `${firstName.charAt(0)}${lastName.charAt(0)}`.toUpperCase();
