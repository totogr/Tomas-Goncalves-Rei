import { ProfessionalLayout } from "@/components/ProfessionalLayout/ProfessionalLayout";
import { ConfigLayout } from "@/components/ConfigLayout/ConfigLayout";
import { ProfessionalServicesConfig } from "@/components/ProfessionalServicesConfig/ProfessionalServicesConfig";

export const ServicesConfigScreen = () => {
    return (
        <ProfessionalLayout>
            <ConfigLayout activeTab="servicios">
                <ProfessionalServicesConfig />
            </ConfigLayout>
        </ProfessionalLayout>
    );
};