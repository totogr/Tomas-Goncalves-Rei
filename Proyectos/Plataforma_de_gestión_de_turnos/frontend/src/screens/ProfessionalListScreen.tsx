import { useLocation } from "wouter";

import { ProfessionalList } from "@/components/ProfessionalList/ProfessionalList";
import { useProfessionals } from "@/services/ProfessionalListServices";

export const ProfessionalListScreen = () => {
  const { data: professionals, isLoading } = useProfessionals();
  const [, navigate] = useLocation();

  return (
    <ProfessionalList
      professionals={professionals}
      isLoading={isLoading}
      onSelect={(id) => navigate(`/professionals/${id}`)}
    />
  );
};