import type { User } from "../context/AuthContext";
import { Roles, RolesMap } from "../types/roles";

export function tieneRol(user: User | null, ...roles: Roles[]): boolean {
  if (!user?.nombreRol) return false;
  const rol = RolesMap[user.nombreRol]; 
  return roles.includes(rol);
}
