import { ClientLayout } from "@/components/ClientLayout/ClientLayout";
import { ProfessionalServiceList } from "@/components/ProfessionalServiceList/ProfessionalServiceList";
import { useProfessional } from "@/services/ProfessionalServices";

type Props = {
  id: string;
};

export const ProfessionalScreen = ({ id }: Props) => {
  const { data, isError } = useProfessional(id);

  return (
    <ClientLayout>
      {isError ? (
        <p>No se pudo cargar el profesional.</p>
      ) : data ? (
        <ProfessionalServiceList professional={data} />
      ) : null}
    </ClientLayout>
  );
};