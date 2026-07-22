import { useState } from "react";
import { useForgotPassword } from "@/services/UserServices";
import styles from "./ForgotPassword.module.css";

export function ForgotPassword() {
  const [email, setEmail] = useState("");
  const [sent, setSent] = useState(false);
  const { mutate, isPending, error } = useForgotPassword();

  if (sent) {
    return (
      <div className={styles.container}>
        <div className={styles.left}>
          <div className={styles.leftContent}>
            <h1 className={styles.logo}>Tur<span>nos</span></h1>
            <p className={styles.leftDescription}>
              Revisá tu bandeja de entrada y seguí las instrucciones para recuperar tu acceso.
            </p>
          </div>
        </div>
        <div className={styles.right}>
          <div className={styles.formBox}>
            <div className={styles.successBox}>
              <div className={styles.successIcon}>
                <svg viewBox="0 0 24 24"><path d="M20 6L9 17l-5-5" /></svg>
              </div>
              <h2 className={styles.successTitle}>¡Email enviado!</h2>
              <p className={styles.successText}>
                Si tu cuenta existe, vas a recibir un enlace para restablecer tu contraseña en los próximos minutos.
              </p>
              <div className={styles.extraLinks}>
                <a href="/login">Volver al inicio de sesión</a>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className={styles.container}>
      <div className={styles.left}>
        <div className={styles.leftContent}>
          <h1 className={styles.logo}>Tur<span>nos</span></h1>
          <p className={styles.leftDescription}>
            Ingresá tu email y te mandamos un enlace para que puedas crear una nueva contraseña.
          </p>
        </div>
      </div>
      <div className={styles.right}>
        <div className={styles.formBox}>
          <h2 className={styles.title}>Recuperar contraseña</h2>
          <p className={styles.subtitle}>
            Ingresá el email con el que te registraste y te enviamos un enlace de restablecimiento.
          </p>
          <div className={styles.inputGroup}>
            <label className={styles.label}>Email</label>
            <input
              className={styles.input}
              type="email"
              placeholder="tu@email.com"
              value={email}
              onChange={(e: React.ChangeEvent<HTMLInputElement>) => setEmail(e.target.value)}
            />
          </div>
          <button
            className={styles.submitButton}
            onClick={() => mutate({ email }, { onSuccess: () => setSent(true) })}
            disabled={isPending || !email}
          >
            {isPending ? "Enviando..." : "Enviar enlace"}
          </button>
          {error && <p className={styles.errorMessage}>Ocurrió un error. Intentá de nuevo.</p>}
          <div className={styles.extraLinks}>
            <p>¿Recordaste tu contraseña? <a href="/login">Iniciá sesión</a></p>
          </div>
        </div>
      </div>
    </div>
  );
}