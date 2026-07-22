import { ProfessionalLayout } from "@/components/ProfessionalLayout/ProfessionalLayout";
import { ConfigLayout } from "@/components/ConfigLayout/ConfigLayout";
import { BlockedHoursConfig } from "@/components/BlockedHoursConfig/BlockedHoursConfig";

export const BlockedHoursConfigScreen = () => {
    return (
        <ProfessionalLayout>
            <ConfigLayout activeTab="bloqueo-horarios">
                <BlockedHoursConfig />
            </ConfigLayout>
        </ProfessionalLayout>
    );
};