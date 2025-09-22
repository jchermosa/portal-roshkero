import { useEffect, useState } from "react";
import { getRoles, getCargos, getEquipos } from "../../services/CatalogService";
import type { RolItem, CargoItem, EquipoItem } from "../../types";

export function useCatalogosUsuarios(token: string | null) {
  const [roles, setRoles] = useState<RolItem[]>([]);
  const [cargos, setCargos] = useState<CargoItem[]>([]);
  const [equipos, setEquipos] = useState<EquipoItem[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!token) return;
    setLoading(true);

    Promise.all([getRoles(token), getCargos(token), getEquipos(token)])
      .then(([rolesRes, cargosRes, equiposRes]) => {
        setRoles(rolesRes ?? []);
        setCargos(cargosRes ?? []);
        setEquipos(equiposRes ?? []);
      })
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, [token]);

  return { roles, cargos, equipos, loading, error };
}
