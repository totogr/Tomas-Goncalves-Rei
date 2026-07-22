import { useLocation } from "wouter";
import { OnboardingLayout } from "@/components/OnboardingLayout/OnboardingLayout";
import { WorkingHoursConfig } from "@/components/WorkingHoursConfig/WorkingHoursConfig";
import { DEFAULT_WORKING_HOURS } from "@/models/WorkingHours";
import { useWorkingHours, useSaveWorkingHours } from "@/services/WorkingHoursServices";
import styles from "@/components/OnboardingLayout/OnboardingLayout.module.css";

export const OnboardingHorariosScreen = () => {
    const { data, isLoading } = useWorkingHours();
    const { mutate, isPending, isSuccess } = useSaveWorkingHours();
    const [, navigate] = useLocation();

    if (isLoading) return null;

    return (
        <OnboardingLayout currentStep="horarios">
            <h1 className={styles.stepHeading}>Horarios</h1>
            <p className={styles.stepDescription}>Configurá los horarios disponibles de tu negocio</p>
            <WorkingHoursConfig
                initialWorkingHours={data ?? DEFAULT_WORKING_HOURS}
                onSave={(wh) => mutate(wh, { onSuccess: () => navigate("/professional/agenda") })}
                isSaving={isPending}
                saved={isSuccess}
            />
        </OnboardingLayout>
    );
};