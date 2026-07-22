import React from "react";
import styles from "./OnboardingLayout.module.css";

const CHECK_ICON = (
    <svg width="14" height="14" viewBox="0 0 14 14" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
        <path d="M2 7l3.5 3.5L12 3" />
    </svg>
);

const STEPS = [
    { key: "perfil",    label: "Perfil",    subtitle: "Datos del negocio"  },
    { key: "servicios", label: "Servicios", subtitle: "Qué ofrecés"        },
    { key: "horarios",  label: "Horarios",  subtitle: "Disponibilidad"     },
];

type StepKey = "perfil" | "servicios" | "horarios";

const STEP_INDEX: Record<StepKey, number> = {
    perfil: 0,
    servicios: 1,
    horarios: 2,
};


type Props = React.PropsWithChildren<{
    currentStep: StepKey;
}>;

export const OnboardingLayout = ({ children, currentStep }: Props) => {
    const currentIndex = STEP_INDEX[currentStep];

    const totalSteps = STEPS.length;

    return (
        <div className={styles.root}>
            <aside className={styles.sidebar}>
                <span className={styles.logo}>Tur<span>nos</span></span>

                <div className={styles.steps}>
                    {STEPS.map((step, i) => {
                        const isDone   = i < currentIndex;
                        const isActive = i === currentIndex;

                        return (
                            <div key={step.key} className={styles.step}>
                                <div className={`${styles.stepIndicator} ${isDone ? styles.done : ""} ${isActive ? styles.active : ""}`}>
                                    {isDone ? CHECK_ICON : i + 1}
                                </div>
                                <div className={styles.stepText}>
                  <span className={`${styles.stepTitle} ${!isDone && !isActive ? styles.inactive : ""}`}>
                    {step.label}
                  </span>
                                    <span className={styles.stepSubtitle}>{step.subtitle}</span>
                                </div>
                            </div>
                        );
                    })}
                </div>
            </aside>

            <main className={styles.content}>
                <div className={styles.inner}>
                    <p className={styles.stepMeta}>PASO {currentIndex + 1} DE {totalSteps}</p>
                    {children}
                </div>
            </main>
        </div>
    );
};