import { createContext } from "react";

export type Role = "CLIENT" | "PROFESSIONAL" | null;

export const RoleContext = createContext<[Role, (role: Role) => void]>([
  null,
  () => {},
]);