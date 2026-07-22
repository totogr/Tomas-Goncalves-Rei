import { useState } from "react";
import { RoleContext, Role } from "./RoleContext";

export const RoleProvider = ({ children }: { children: React.ReactNode }) => {
  const [role, setRole] = useState<Role>(null);
  return (
    <RoleContext.Provider value={[role, setRole]}>
      {children}
    </RoleContext.Provider>
  );
};