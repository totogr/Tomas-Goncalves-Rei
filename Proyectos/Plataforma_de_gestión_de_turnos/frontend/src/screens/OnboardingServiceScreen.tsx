import { useState } from "react";
import { useLocation } from "wouter";
import { OnboardingLayout } from "@/components/OnboardingLayout/OnboardingLayout";
import { ServicesConfigOnboarding } from "@/components/ServicesConfigOnboarding/ServicesConfigOnboarding";
import { Service, ServiceSchema } from "@/models/Service";
import { useCreateService } from "@/services/ServiceServices";
import { useProfessional } from "@/services/ProfessionalServices";
import { useToken } from "@/services/TokenContext";

const getErrorMessage = (error: unknown) => {
    if (error instanceof Error) return error.message;
    if (typeof error === "string") return error;
    return "No se pudieron guardar los servicios";
};

export const OnboardingServiceScreen = () => {
    const [, navigate] = useLocation();
    const createServiceMutation = useCreateService();
    const [tokenState] = useToken();
    const professionalId = tokenState.state === "LOGGED_IN" ? String(tokenState.tokens.id) : "";
    const { data: professionalData } = useProfessional(professionalId);
    const [saveError, setSaveError] = useState<string | null>(null);
    const [isSaving, setIsSaving] = useState(false);
    const professionalCategory = professionalData?.specialty ?? "";

    const handleOnContinue = async (services: Service[]) => {
        setSaveError(null);
        setIsSaving(true);
        try {
            for (const service of services) {
                const parsed = ServiceSchema.parse(service);
                await createServiceMutation.mutateAsync(parsed);
            }
            navigate("/professional/onboarding/horarios");
        } catch (error) {
            setSaveError(getErrorMessage(error));
        } finally {
            setIsSaving(false);
        }
    };

    return (
        <OnboardingLayout currentStep="servicios">
            {saveError && (
                <div style={{ marginBottom: 16, padding: 12, borderRadius: 12, background: "#fff1f0", color: "#a61b1b", border: "1px solid #f5c2c7" }}>
                    {saveError}
                </div>
            )}
            <ServicesConfigOnboarding
                initialServices={[]}
                onBack={() => navigate("/professional/onboarding/perfil")}
                onContinue={(services) => { void handleOnContinue(services); }}
                isSaving={isSaving || createServiceMutation.isPending}
                currentStep={2}
                totalSteps={3}
                professionalCategory={professionalCategory}
            />
        </OnboardingLayout>
    );
};