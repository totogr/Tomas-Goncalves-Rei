import { Link } from "wouter";
import styles from "./RoleSelection.module.css";

type Props = {
  onSelectClient: () => void;
  onSelectProfessional: () => void;
};

export const RoleSelection = ({ onSelectClient, onSelectProfessional }: Props) => {
  return (
    <div className={styles.page}>
      <div className={styles.content}>
        <span className={styles.badge}>GESTIÓN DE TURNOS ONLINE</span>

        <h1 className={styles.title}>
          Gestioná tu agenda{" "}
          <span className={styles.titleAccent}>en minutos</span>
        </h1>

        <p className={styles.subtitle}>
          Reservá turnos o administrá tu negocio.
          <br />
          Simple, rápido, profesional.
        </p>

        <div className={styles.cards}>
          <button className={styles.card} onClick={onSelectClient}>
            <div className={styles.iconWrapper}>
              <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8">
                <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2" />
                <circle cx="12" cy="7" r="4" />
              </svg>
            </div>
            <div className={styles.cardBody}>
              <div className={styles.cardHeader}>
                <span className={styles.cardTitle}>Soy cliente</span>
                <span className={styles.arrow}>→</span>
              </div>
              <p className={styles.cardDesc}>Buscá profesionales y reservá turnos fácilmente</p>
            </div>
          </button>

          <button className={styles.card} onClick={onSelectProfessional}>
            <div className={styles.iconWrapper}>
              <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8">
                <rect x="3" y="4" width="18" height="18" rx="2" ry="2" />
                <line x1="16" y1="2" x2="16" y2="6" />
                <line x1="8" y1="2" x2="8" y2="6" />
                <line x1="3" y1="10" x2="21" y2="10" />
              </svg>
            </div>
            <div className={styles.cardBody}>
              <div className={styles.cardHeader}>
                <span className={styles.cardTitle}>Soy profesional</span>
                <span className={styles.arrow}>→</span>
              </div>
              <p className={styles.cardDesc}>Gestioná tu agenda y crecé tu negocio</p>
            </div>
          </button>
        </div>

        <p className={styles.loginLink}>
          ¿Ya tenés cuenta?{" "}
          <Link href="/login" className={styles.link}>
            Iniciá sesión
          </Link>
        </p>
      </div>
    </div>
  );
};