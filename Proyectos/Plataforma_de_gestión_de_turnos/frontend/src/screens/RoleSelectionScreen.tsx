import { useLocation } from "wouter";
import { RoleSelection } from "@/components/RoleSelection/RoleSelection";

export const RoleSelectionScreen = () => {
  const [, navigate] = useLocation();

  return (
    <RoleSelection
      onSelectClient={() => navigate("/signup/client")}
      onSelectProfessional={() => navigate("/signup/professional")}
    />
  );
};