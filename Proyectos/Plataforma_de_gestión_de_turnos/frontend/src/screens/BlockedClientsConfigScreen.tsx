import { ProfessionalLayout } from "@/components/ProfessionalLayout/ProfessionalLayout";
import { ConfigLayout } from "@/components/ConfigLayout/ConfigLayout";
import { BlockedClientsConfig } from "@/components/BlockedClientsConfig/BlockedClientsConfig";

export const BlockedClientsConfigScreen = () => {
    return (
        <ProfessionalLayout>
            <ConfigLayout activeTab="bloqueo-clientes">
                <BlockedClientsConfig />
            </ConfigLayout>
        </ProfessionalLayout>
    );
};