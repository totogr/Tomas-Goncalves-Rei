import { useAppForm } from "@/config/use-app-form";
import { ClientSignupFormSchema, ClientSignupRequest } from "@/models/Login";
import styles from "@/components/SignUp/SignUp.module.css";

type Props = {
    onSubmit: (value: ClientSignupRequest) => void;
    submitError: Error | null;
};

export function ClientSignup({ onSubmit, submitError }: Props) {
    const form = useAppForm({
        defaultValues: {
            email: "",
            password: "",
            confirmPassword: "",
            firstName: "",
            lastName: "",
        },
        validators: { onSubmit: ClientSignupFormSchema },
        onSubmit: async ({ value }) => {
            onSubmit({
                email: value.email,
                password: value.password,
                firstName: value.firstName,
                lastName: value.lastName,
            });
        },
    });

    return (
        <div className={styles.container}>
            <div className={styles.left}>
                <div className={styles.leftContent}>
                    <h1 className={styles.logo}>Tur<span>nos</span></h1>
                    <ul className={styles.features}>
                        {["Reservas 24/7 sin llamadas", "Recordatorios automáticos", "Gestión de pagos y señas", "Estadísticas de tu negocio"].map((text) => (
                            <li key={text}>
                                <span className={styles.checkIcon}>
                                    <svg viewBox="0 0 10 8" fill="none" stroke="currentColor" strokeWidth="1.8">
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
                    <h2 className={styles.title}>Crear cuenta</h2>
                    <p className={styles.subtitle}>Empezá a reservar tus turnos en minutos</p>

                    <div className={styles.roleTabs}>
                        <span className={`${styles.roleTab} ${styles.active}`}>Cliente</span>
                        <a href="/signup/professional" className={styles.roleTab}>Profesional</a>
                    </div>

                    <form.AppForm>
                        <form.FormContainer extraError={submitError}>
                            <div className={styles.fieldRow}>
                                <form.AppField name="firstName" children={(f) => <f.TextField label="Nombre" />} />
                                <form.AppField name="lastName" children={(f) => <f.TextField label="Apellido" />} />
                            </div>
                            <form.AppField name="email" children={(f) => <f.TextField label="Email" />} />
                            <form.AppField name="password" children={(f) => <f.PasswordField label="Contraseña" showStrength />} />
                            <form.AppField name="confirmPassword" children={(f) => <f.PasswordField label="Confirmá tu contraseña" />} />
                        </form.FormContainer>
                    </form.AppForm>

                    <div className={styles.extraLinks}>
                        <p>¿Ya tenés cuenta? <a href="/login">Iniciá sesión</a></p>
                    </div>
                </div>
            </div>
        </div>
    );
}