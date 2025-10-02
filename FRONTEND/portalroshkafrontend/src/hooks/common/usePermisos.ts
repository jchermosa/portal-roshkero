import { useAuth } from "../../context/AuthContext";
import { tieneRol } from "../../utils/permisos";
import { Roles } from "../../types/roles";

export function usePermisos(...rolesPermitidos: Roles[]) {
  const { user, token } = useAuth();

  // mientras no haya user cargado, consideramos que está cargando
  if (!user && token) {
    return { loading: true, allowed: false };
  }

  // si no hay user ni token, no hay permisos
  if (!user && !token) {
    return { loading: false, allowed: false };
  }

  // si hay user, chequeamos permisos
  const allowed = !!user && tieneRol(user, ...rolesPermitidos);

  return { loading: false, allowed };
}
