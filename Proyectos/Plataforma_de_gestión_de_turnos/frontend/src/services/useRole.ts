import { useContext } from "react";
import { RoleContext } from "./RoleContext";

export const useRole = () => useContext(RoleContext);