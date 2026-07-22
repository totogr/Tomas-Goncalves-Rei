import { useId } from "react";
import { ErrorContainer } from "@/components/form-components/ErrorContainer/ErrorContainer";
import { useFieldContext } from "@/config/form-context";
import styles from "./InputFields.module.css";

export const TextField = ({ label }: { label: string }) => {
  return <FieldWithType type="text" label={label} />;
};

export const PasswordField = ({
  label,
  showStrength = false,
  forgotLink,
}: {
  label: string;
  showStrength?: boolean;
  forgotLink?: string;
}) => {
  return (
    <FieldWithType
      type="password"
      label={label}
      showStrength={showStrength}
      forgotLink={forgotLink}
    />
  );
};

function getStrength(password: string): {
  score: number;
  label: string;
  color: string;
} {
  let score = 0;
  if (password.length >= 8) score++;
  if (/[A-Z]/.test(password)) score++;
  if (/[a-z]/.test(password)) score++;
  if (/[0-9]/.test(password)) score++;
  if (/[^A-Za-z0-9]/.test(password)) score++;

  if (score <= 1) return { score, label: "Muy débil", color: "#c0392b" };
  if (score === 2) return { score, label: "Débil", color: "#e67e22" };
  if (score === 3) return { score, label: "Regular", color: "#f1c40f" };
  if (score === 4) return { score, label: "Fuerte", color: "#2ecc71" };
  return { score, label: "Muy fuerte", color: "#27ae60" };
}

const FieldWithType = ({
  label,
  type,
  showStrength = false,
  forgotLink,
}: {
  label: string;
  type: string;
  showStrength?: boolean;
  forgotLink?: string;
}) => {
  const id = useId();
  const field = useFieldContext<string>();
  const value = field.state.value;
  const strength = showStrength && value ? getStrength(value) : null;

  return (
    <div className={styles.fieldGroup}>
      <div className={styles.labelRow}>
        <label htmlFor={id} className={styles.label}>
          {label}
        </label>
        {forgotLink && (
          <a href={forgotLink} className={styles.forgotLink}>
            ¿Olvidaste tu contraseña?
          </a>
        )}
      </div>
      <div className={styles.dataContainer}>
        <input
          id={id}
          name={field.name}
          value={value}
          className={styles.input}
          type={type}
          onBlur={field.handleBlur}
          onChange={(e) => field.handleChange(e.target.value)}
        />
        {strength && (
          <div className={styles.strengthBar}>
            <div className={styles.strengthSegments}>
              {[1, 2, 3, 4, 5].map((i) => (
                <div
                  key={i}
                  className={styles.strengthSegment}
                  style={{
                    background:
                      i <= strength.score ? strength.color : "#e0ddd6",
                  }}
                />
              ))}
            </div>
            <span
              className={styles.strengthLabel}
              style={{ color: strength.color }}
            >
              {strength.label}
            </span>
          </div>
        )}
        <ErrorContainer errors={field.state.meta.errors} />
      </div>
    </div>
  );
};