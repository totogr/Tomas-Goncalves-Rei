import { useLocation } from "wouter";
import { OnboardingLayout } from "@/components/OnboardingLayout/OnboardingLayout";
import { ProfileConfig } from "@/components/ProfileConfig/ProfileConfig";
import { useSaveProfile } from "@/services/ProfileServices";

export const OnboardingPerfilScreen = () => {
    const { mutate, isPending } = useSaveProfile();
    const [, navigate] = useLocation();

    return (
        <OnboardingLayout currentStep="perfil">
            <ProfileConfig
                onContinue={(data) =>
                    mutate(data, {
                        onSuccess: () => navigate("/professional/onboarding/servicios"),
                    })
                }
                isSaving={isPending}
                currentStep={1}
                totalSteps={3}
            />
        </OnboardingLayout>
    );
};