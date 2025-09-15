import type { User } from "../context/AuthContext";
import { Roles } from "../types/roles";

export function tieneRol(user: User | null, ...roles: Roles[]): boolean {
  return !!user?.rol?.nombre && roles.includes(user.rol.nombre as Roles);
}
