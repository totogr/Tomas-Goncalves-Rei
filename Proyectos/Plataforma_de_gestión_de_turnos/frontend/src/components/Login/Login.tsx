import { useAppForm } from "@/config/use-app-form";
import { LoginRequest, LoginRequestSchema } from "@/models/Login";

import styles from "./Login.module.css";



type Props = {
  onSubmit: (value: LoginRequest) => void;
  submitError: Error | null;
};

export function Login({ onSubmit, submitError }: Props) {
  const formData = useAppForm({
    defaultValues: {
      email: "",
      password: "",
    },
    validators: {
      onSubmit: LoginRequestSchema,
    },
    onSubmit: async ({ value }) => onSubmit(value),
  });

  return (
  <div className={styles.container}>
      <div className={styles.left}>
        <div className={styles.leftContent}>
          <h1 className={styles.logo}>
            Tur<span>nos</span>
          </h1>
 
            <ul className={styles.features}>
              {[
                "Reservas 24/7 sin llamadas",
                "Recordatorios automáticos",
                "Gestión de pagos y señas",
                "Estadísticas de tu negocio",
              ].map((text) => (
                <li key={text}>
                  <span className={styles.checkIcon}>
                    <svg viewBox="0 0 10 8">
                      <path d="M1 4l2.5 2.5L9 1" />
                    </svg>
                  </span>
                  {text}
                </li>
              ))}
            </ul>
        </div>
      </div>
 
      <div className={styles.right}>
        <div className={styles.formBox}>
          <h2 className={styles.title}>Bienvenido</h2>
          <p className={styles.subtitle}>Ingresá a tu cuenta para continuar</p>
 
          <formData.AppForm>
            <formData.FormContainer extraError={submitError} submitLabel="Iniciar sesión">
              <formData.AppField
                name="email"
                children={(field) => <field.TextField label="Email" />}
              />
              <formData.AppField
                name="password"
                children={(field) => (
                  <field.PasswordField label="Contraseña" forgotLink="/forgot-password" />
                )}
              />
            </formData.FormContainer>
          </formData.AppForm>
 
          <div className={styles.extraLinks}>
            <p>
              ¿No tenés cuenta? <a href="/">Registrate gratis</a>
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}
