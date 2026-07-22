import { useLocation } from "wouter";
import { ProfessionalLayout } from "@/components/ProfessionalLayout/ProfessionalLayout";
import { ConfigLayout } from "@/components/ConfigLayout/ConfigLayout";
import { WorkingHoursConfig } from "@/components/WorkingHoursConfig/WorkingHoursConfig";
import { DEFAULT_WORKING_HOURS } from "@/models/WorkingHours";
import { useWorkingHours, useSaveWorkingHours } from "@/services/WorkingHoursServices";

export const WorkingHoursConfigScreen = () => {
    const { data, isLoading } = useWorkingHours();
    const { mutate, isPending, isSuccess } = useSaveWorkingHours();
    const [, navigate] = useLocation();

    if (isLoading) return null;

    return (
        <ProfessionalLayout>
            <ConfigLayout activeTab="horarios">
                <WorkingHoursConfig
                    initialWorkingHours={data ?? DEFAULT_WORKING_HOURS}
                    onSave={(wh) => mutate(wh, { onSuccess: () => navigate("/professional/agenda") })}
                    isSaving={isPending}
                    saved={isSuccess}
                />
            </ConfigLayout>
        </ProfessionalLayout>
    );
};