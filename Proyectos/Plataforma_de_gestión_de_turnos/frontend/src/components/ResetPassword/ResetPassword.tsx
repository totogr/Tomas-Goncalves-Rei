import { useState } from "react";
import { useResetPassword } from "@/services/UserServices";
import styles from "./ResetPassword.module.css";

export function ResetPassword() {
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [done, setDone] = useState(false);
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirm, setShowConfirm] = useState(false);
  const { mutate, isPending, error } = useResetPassword();

  const token = new URLSearchParams(window.location.search).get("token") ?? "";
  const passwordMismatch = confirmPassword.length > 0 && newPassword !== confirmPassword;
  const tooShort = newPassword.length > 0 && newPassword.length < 8;

  if (!token) {
    return (
      <div className={styles.container}>
        <div className={styles.left}>
          <div className={styles.leftContent}>
            <h1 className={styles.logo}>Tur<span>nos</span></h1>
          </div>
        </div>
        <div className={styles.right}>
          <div className={styles.formBox}>
            <div className={styles.invalidBox}>
              <div className={styles.invalidIcon}>
                <svg viewBox="0 0 24 24"><path d="M18 6L6 18M6 6l12 12" /></svg>
              </div>
              <h2 className={styles.successTitle}>Enlace inválido</h2>
              <p className={styles.successText}>
                Este enlace de recuperación es inválido o ya expiró.
              </p>
              <div className={styles.extraLinks}>
                <a href="/forgot-password">Solicitá uno nuevo</a>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }

  if (done) {
    return (
      <div className={styles.container}>
        <div className={styles.left}>
          <div className={styles.leftContent}>
            <h1 className={styles.logo}>Tur<span>nos</span></h1>
          </div>
        </div>
        <div className={styles.right}>
          <div className={styles.formBox}>
            <div className={styles.successBox}>
              <div className={styles.successIcon}>
                <svg viewBox="0 0 24 24"><path d="M20 6L9 17l-5-5" /></svg>
              </div>
              <h2 className={styles.successTitle}>¡Contraseña actualizada!</h2>
              <p className={styles.successText}>
                Tu contraseña fue restablecida correctamente. Ya podés iniciar sesión.
              </p>
              <div className={styles.extraLinks}>
                <a href="/login">Ir al inicio de sesión</a>
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
            Elegí una contraseña segura con al menos 8 caracteres.
          </p>
        </div>
      </div>
      <div className={styles.right}>
        <div className={styles.formBox}>
          <h2 className={styles.title}>Nueva contraseña</h2>
          <p className={styles.subtitle}>
            Ingresá y confirmá tu nueva contraseña para recuperar el acceso a tu cuenta.
          </p>

          <div className={styles.inputGroup}>
            <label className={styles.label}>Nueva contraseña</label>
            <div className={styles.inputWrapper}>
              <input
                className={styles.input}
                type={showPassword ? "text" : "password"}
                placeholder="Mínimo 8 caracteres"
                value={newPassword}
                onChange={(e: React.ChangeEvent<HTMLInputElement>) => setNewPassword(e.target.value)}
              />
              <button
                type="button"
                className={styles.eyeButton}
                onClick={() => setShowPassword(!showPassword)}
              >
                {showPassword ? (
                  <svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="#7a8878" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                    <path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94" />
                    <path d="M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19" />
                    <line x1="1" y1="1" x2="23" y2="23" />
                  </svg>
                ) : (
                  <svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="#7a8878" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                    <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z" />
                    <circle cx="12" cy="12" r="3" />
                  </svg>
                )}
              </button>
            </div>
            {tooShort && <span className={styles.validationHint}>Mínimo 8 caracteres</span>}
          </div>

          <div className={styles.inputGroup}>
            <label className={styles.label}>Confirmá tu contraseña</label>
            <div className={styles.inputWrapper}>
              <input
                className={styles.input}
                type={showConfirm ? "text" : "password"}
                placeholder="Repetí tu contraseña"
                value={confirmPassword}
                onChange={(e: React.ChangeEvent<HTMLInputElement>) => setConfirmPassword(e.target.value)}
              />
              <button
                type="button"
                className={styles.eyeButton}
                onClick={() => setShowConfirm(!showConfirm)}
              >
                {showConfirm ? (
                  <svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="#7a8878" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                    <path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94" />
                    <path d="M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19" />
                    <line x1="1" y1="1" x2="23" y2="23" />
                  </svg>
                ) : (
                  <svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="#7a8878" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                    <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z" />
                    <circle cx="12" cy="12" r="3" />
                  </svg>
                )}
              </button>
            </div>
            {passwordMismatch && <span className={styles.validationHint}>Las contraseñas no coinciden</span>}
          </div>

          <button
            className={styles.submitButton}
            onClick={() => mutate({ token, newPassword }, { onSuccess: () => setDone(true) })}
            disabled={isPending || !newPassword || newPassword !== confirmPassword || newPassword.length < 8}
          >
            {isPending ? "Guardando..." : "Guardar contraseña"}
          </button>

          {error && (
            <p className={styles.errorMessage}>
              El enlace expiró o ya fue usado.{" "}
              <a href="/forgot-password">Solicitá uno nuevo</a>.
            </p>
          )}
        </div>
      </div>
    </div>
  );
}