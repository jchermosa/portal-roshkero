import type { User } from "../context/AuthContext";
import { Roles, RolesMap } from "../types/roles";

export function tieneRol(user: User | null, ...roles: Roles[]): boolean {
  if (!user?.rol?.nombre) return false;
  const mapped = RolesMap[user.rol.nombre]; 
  return roles.includes(mapped);
}
