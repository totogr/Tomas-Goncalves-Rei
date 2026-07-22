import { useEffect, useState } from "react";
import { DEFAULT_SERVICE, Service } from "@/models/Service";
import styles from "./ServicesConfigOnboarding.module.css";

type Props = {
    initialServices: Service[];
    onContinue: (services: Service[]) => void | Promise<void>;
    onBack?: () => void;
    isSaving: boolean;
    currentStep: number;
    totalSteps: number;
    professionalCategory?: string;
};

type TouchedSet = Set<string>;

export function ServicesConfigOnboarding({
    initialServices,
    onContinue,
    onBack,
    isSaving,
    currentStep,
    totalSteps,
    professionalCategory = "",
}: Props) {
    const [services, setServices] = useState<Service[]>(
        initialServices.length > 0
            ? initialServices
            : [{ ...DEFAULT_SERVICE(), category: professionalCategory }]
    );
    const [touchedMap, setTouchedMap] = useState<Record<number, TouchedSet>>({});
    const [attemptedSubmit, setAttemptedSubmit] = useState(false);

    const isTouched = (index: number, field: string) =>
        touchedMap[index]?.has(field) ?? false;

    const markTouched = (index: number, field: string) => {
        setTouchedMap(prev => ({
            ...prev,
            [index]: new Set([...(prev[index] ?? []), field]),
        }));
    };

    const showErr = (index: number, field: string, invalid: boolean) =>
        invalid && (attemptedSubmit || isTouched(index, field));

    const isServiceInvalid = (s: Service) =>
        s.name.trim() === "" ||
        s.duration_minutes < 5 ||
        !Number.isFinite(s.price) ||
        s.price < 1 ||
        s.max_capacity < 1 ||
        (s.category?.trim().length ?? 0) === 0;

    const hasErrors = services.some(isServiceInvalid);

    useEffect(() => {
        if (initialServices.length > 0) {
            setServices(initialServices);
            setTouchedMap({});
            return;
        }
        setServices([{ ...DEFAULT_SERVICE(), category: professionalCategory }]);
        setTouchedMap({});
    }, [initialServices, professionalCategory]);

    const updateServiceAt = <K extends keyof Service>(index: number, key: K, value: Service[K]) => {
        setServices((current) => {
            const next = [...current];
            next[index] = { ...next[index], [key]: value };
            return next;
        });
    };

    const addServiceDraft = () => {
        setServices((current) => [
            ...current,
            { ...DEFAULT_SERVICE(), category: professionalCategory },
        ]);
    };

    const removeServiceAt = (index: number) => {
        setServices((current) => current.filter((_, i) => i !== index));
        setTouchedMap({});
    };

    const handleContinue = () => {
        setAttemptedSubmit(true);
        if (!hasErrors) {
            void onContinue(services);
        }
    };

    return (
        <div>
            <div className={styles.cards}>
                {services.map((service, index) => (
                    <div key={index} className={styles.card}>
                        <div className={styles.cardHeader}>
                            <span className={styles.cardIndex}>Servicio {index + 1}</span>
                            {services.length > 1 && (
                                <button
                                    className={styles.removeBtn}
                                    onClick={() => removeServiceAt(index)}
                                    title="Eliminar servicio"
                                    type="button"
                                >
                                    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" aria-hidden="true"><line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/></svg>
                                </button>
                            )}
                        </div>

                        <div className={styles.cardBody}>
                            {/* Nombre */}
                            <div className={styles.field}>
                                <label className={styles.fieldLabel}>Nombre</label>
                                <input
                                    className={`${styles.nameInput} ${showErr(index, "name", service.name.trim() === "") ? styles.inputErr : ""}`}
                                    type="text"
                                    placeholder="Ej: Corte de cabello"
                                    value={service.name}
                                    onChange={(e) => updateServiceAt(index, "name", e.target.value)}
                                    onBlur={() => markTouched(index, "name")}
                                />
                                <span className={`${styles.errMsg} ${showErr(index, "name", service.name.trim() === "") ? "" : styles.errHidden}`}>
                                    El nombre es obligatorio
                                </span>
                            </div>

                            <div className={styles.grid2}>
                                {/* Duración */}
                                <div className={styles.field}>
                                    <label className={styles.fieldLabel}>Duración</label>
                                    <div className={`${styles.inputGroup} ${showErr(index, "duration_minutes", service.duration_minutes < 5) ? styles.inputGroupErr : ""}`}>
                                        <input
                                            className={styles.groupInput}
                                            type="number"
                                            min={5}
                                            step={5}
                                            value={service.duration_minutes}
                                            onChange={(e) => updateServiceAt(index, "duration_minutes", Number(e.target.value))}
                                            onBlur={() => markTouched(index, "duration_minutes")}
                                        />
                                        <span className={`${styles.adorn} ${styles.adornRight}`}>min</span>
                                    </div>
                                    <span className={`${styles.errMsg} ${showErr(index, "duration_minutes", service.duration_minutes < 5) ? "" : styles.errHidden}`}>
                                        Mínimo 5 minutos
                                    </span>
                                </div>

                                {/* Precio */}
                                <div className={styles.field}>
                                    <label className={styles.fieldLabel}>Precio</label>
                                    <div className={`${styles.inputGroup} ${showErr(index, "price", !Number.isFinite(service.price) || service.price < 1) ? styles.inputGroupErr : ""}`}>
                                        <span className={`${styles.adorn} ${styles.adornLeft}`}>$</span>
                                        <input
                                            className={styles.groupInput}
                                            type="number"
                                            min={1}
                                            placeholder="3500"
                                            value={Number.isFinite(service.price) && service.price !== 0 ? service.price : ""}
                                            onChange={(e) =>
                                                updateServiceAt(index, "price", e.target.value === "" ? Number.NaN : Number(e.target.value))
                                            }
                                            onBlur={() => markTouched(index, "price")}
                                        />
                                    </div>
                                    <span className={`${styles.errMsg} ${showErr(index, "price", !Number.isFinite(service.price) || service.price < 1) ? "" : styles.errHidden}`}>
                                        Debe ser mayor o igual a 1
                                    </span>
                                </div>
                            </div>

                            {/* Capacidad */}
                            <div className={styles.field} style={{ maxWidth: "50%" }}>
                                <label className={styles.fieldLabel}>Capacidad</label>
                                <div className={`${styles.inputGroup} ${showErr(index, "max_capacity", service.max_capacity < 1) ? styles.inputGroupErr : ""}`}>
                                    <input
                                        className={styles.groupInput}
                                        type="number"
                                        min={1}
                                        value={service.max_capacity}
                                        onChange={(e) => updateServiceAt(index, "max_capacity", Number(e.target.value))}
                                        onBlur={() => markTouched(index, "max_capacity")}
                                    />
                                    <span className={`${styles.adorn} ${styles.adornRight}`}>pers.</span>
                                </div>
                                <span className={`${styles.errMsg} ${showErr(index, "max_capacity", service.max_capacity < 1) ? "" : styles.errHidden}`}>
                                    Mínimo 1 persona
                                </span>
                            </div>
                        </div>

                        {/* Toggle activo */}
                        <div className={styles.toggleRow}>
                            <span className={styles.toggleLabel}>Activo</span>
                            <label className={styles.toggle}>
                                <input
                                    type="checkbox"
                                    checked={service.active}
                                    onChange={(e) => updateServiceAt(index, "active", e.target.checked)}
                                />
                                <span className={styles.toggleSlider} />
                            </label>
                            <span className={styles.toggleVal}>{service.active ? "Sí" : "No"}</span>
                        </div>
                    </div>
                ))}
            </div>

            <button className={styles.addBtn} onClick={addServiceDraft} type="button">
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" aria-hidden="true"><line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/></svg>
                Agregar servicio
            </button>

            {attemptedSubmit && hasErrors && (
                <p className={styles.globalErr}>
                    Completá al menos un servicio válido antes de continuar.
                </p>
            )}

            <hr className={styles.divider} />

            <div className={styles.footer}>
                {onBack ? (
                    <button className={styles.backBtn} onClick={onBack} type="button">← Atrás</button>
                ) : (
                    <span />
                )}
                <span className={styles.footerCenter}>{currentStep} de {totalSteps}</span>
                <button
                    className={styles.continueBtn}
                    onClick={handleContinue}
                    disabled={isSaving}
                    type="button"
                >
                    {isSaving ? "Guardando..." : "Continuar →"}
                </button>
            </div>
        </div>
    );
}
