import { useState } from "react";
import { ProfessionalProfileForm, ProfessionalProfileFormSchema } from "@/models/ProfessionalProfile";
import styles from "./ProfileConfig.module.css";

const SPECIALTY_CATEGORIES = [
    {
        label: "Belleza & estética",
        options: ["Peluquería", "Barbería", "Manicura & pedicura", "Maquillaje", "Depilación", "Pestañas & cejas"],
    },
    {
        label: "Salud & bienestar",
        options: ["Masajes", "Nutrición", "Psicología", "Kinesiología", "Odontología"],
    },
    {
        label: "Fitness",
        options: ["Personal trainer", "Yoga / Pilates", "Natación"],
    },
    {
        label: "Otros",
        options: ["Peluquería canina", "Tatuajes & piercings", "Fotografía", "Otro..."],
    },
];

const ALL_OPTIONS = SPECIALTY_CATEGORIES.flatMap((c) => c.options);

type Props = {
    initialValues?: Partial<ProfessionalProfileForm>;
    onContinue: (data: ProfessionalProfileForm) => void;
    isSaving: boolean;
    currentStep: number;
    totalSteps: number;
};

const EMPTY: ProfessionalProfileForm = {
    specialty: "",
    address: "",
    neighborhood: "",
    city: "",
};

type FieldErrors = Partial<Record<keyof ProfessionalProfileForm, string>>;

function resolveInitialSelected(specialty?: string): string {
    if (!specialty) return "";
    if (ALL_OPTIONS.includes(specialty)) return specialty;
    return "Otro...";
}

export function ProfileConfig({ initialValues, onContinue, isSaving, currentStep, totalSteps }: Props) {
    const [form, setForm] = useState<ProfessionalProfileForm>({ ...EMPTY, ...initialValues });
    const [errors, setErrors] = useState<FieldErrors>({});
    const [touched, setTouched] = useState<Partial<Record<keyof ProfessionalProfileForm, boolean>>>({});
    const [selectedSpecialty, setSelectedSpecialty] = useState<string>(
        () => resolveInitialSelected(initialValues?.specialty)
    );
    const [customSpecialty, setCustomSpecialty] = useState<string>(() => {
        if (!initialValues?.specialty) return "";
        return ALL_OPTIONS.includes(initialValues.specialty) ? "" : initialValues.specialty;
    });

    const update = (field: keyof ProfessionalProfileForm, value: string) => {
        setForm((prev) => ({ ...prev, [field]: value }));
        if (errors[field]) setErrors((prev) => ({ ...prev, [field]: undefined }));
    };

    const handleSpecialtySelect = (value: string) => {
        setSelectedSpecialty(value);
        setTouched((prev) => ({ ...prev, specialty: true }));
        if (value !== "Otro...") {
            setCustomSpecialty("");
            update("specialty", value);
        } else {
            update("specialty", "");
        }
    };

    const handleCustomSpecialty = (value: string) => {
        setCustomSpecialty(value);
        update("specialty", value);
    };

    const blur = (field: keyof ProfessionalProfileForm) => {
        setTouched((prev) => ({ ...prev, [field]: true }));
        const result = ProfessionalProfileFormSchema.shape[field].safeParse(form[field]);
        if (!result.success) {
            setErrors((prev) => ({ ...prev, [field]: result.error.issues[0].message }));
        }
    };

    const handleContinue = () => {
        const result = ProfessionalProfileFormSchema.safeParse(form);
        if (!result.success) {
            const newErrors: FieldErrors = {};
            const newTouched: typeof touched = {};
            result.error.issues.forEach((e) => {
                const field = e.path[0] as keyof ProfessionalProfileForm;
                newErrors[field] = e.message;
                newTouched[field] = true;
            });
            setErrors(newErrors);
            setTouched(newTouched);
            return;
        }
        onContinue(result.data);
    };

    const field = (name: keyof ProfessionalProfileForm, label: string, placeholder?: string) => (
        <div className={styles.fieldGroup}>
            <label className={styles.label}>{label}</label>
            <input
                className={`${styles.input} ${touched[name] && errors[name] ? styles.inputError : ""}`}
                type="text"
                placeholder={placeholder ?? label}
                value={form[name]}
                onChange={(e) => update(name, e.target.value)}
                onBlur={() => blur(name)}
            />
            {touched[name] && errors[name] && (
                <span className={styles.errorMsg}>{errors[name]}</span>
            )}
        </div>
    );

    return (
        <div className={styles.wrapper}>
            <h1 className={styles.heading}>Configurá tu perfil</h1>
            <p className={styles.description}>
                Completá los datos de tu negocio para que tus clientes puedan encontrarte.
            </p>

            <div className={styles.section}>
                <p className={styles.sectionTitle}>Datos personales</p>

                <div className={styles.fieldGroup}>

                    {SPECIALTY_CATEGORIES.map((category) => (
                        <div key={category.label} className={styles.categoryBlock}>
                            <span className={styles.categoryLabel}>{category.label}</span>
                            <div className={styles.pillRow}>
                                {category.options.map((option) => (
                                    <button
                                        key={option}
                                        type="button"
                                        className={`${styles.pill} ${selectedSpecialty === option ? styles.pillActive : ""}`}
                                        onClick={() => handleSpecialtySelect(option)}
                                    >
                                        {option}
                                    </button>
                                ))}
                            </div>
                        </div>
                    ))}

                    {selectedSpecialty === "Otro..." && (
                        <input
                            className={`${styles.input} ${touched.specialty && errors.specialty ? styles.inputError : ""}`}
                            type="text"
                            placeholder="Describí tu especialidad..."
                            value={customSpecialty}
                            onChange={(e) => handleCustomSpecialty(e.target.value)}
                            onBlur={() => blur("specialty")}
                            autoFocus
                        />
                    )}

                    {touched.specialty && errors.specialty && (
                        <span className={styles.errorMsg}>{errors.specialty}</span>
                    )}
                </div>
            </div>

            <div className={styles.section}>
                <p className={styles.sectionTitle}>Ubicación del negocio</p>
                <div className={`${styles.row} ${styles.single}`}>
                    {field("address", "Dirección", "Ej: Av. Corrientes 1234")}
                </div>
                <div className={styles.row}>
                    {field("neighborhood", "Barrio", "Ej: Palermo")}
                    {field("city", "Ciudad", "Ej: Buenos Aires")}
                </div>
            </div>

            <hr className={styles.divider} />

            <div className={styles.footer}>
                <span />
                <span className={styles.footerCenter}>{currentStep} de {totalSteps}</span>
                <button
                    className={styles.continueBtn}
                    onClick={handleContinue}
                    disabled={isSaving}
                >
                    {isSaving ? "Guardando..." : "Continuar →"}
                </button>
            </div>
        </div>
    );
}