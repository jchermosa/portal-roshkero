import { useEffect, useState } from "react";
import { getRoles, getCargos } from "../../services/CatalogService";
import type { RolItem, CargoItem } from "../../types";

export function useCatalogosUsuarios(token: string | null) {
  const [roles, setRoles] = useState<RolItem[]>([]);
  const [cargos, setCargos] = useState<CargoItem[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!token) return;
    setLoading(true);

    Promise.all([getRoles(token), getCargos(token)])
      .then(([rolesRes, cargosRes]) => {
        setRoles(rolesRes ?? []);
        setCargos(cargosRes ?? []);
      })
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, [token]);

  return { roles, cargos, loading, error };
}
