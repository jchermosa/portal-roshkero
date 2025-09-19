import { useEffect, useState } from "react";
import { getRoles, getCargos, getEquipos, getTiposPermiso, getTiposBeneficio,} from "../../services/CatalogService";
import type { RolItem, CargoItem, EquipoItem, TipoBeneficioItem, TipoPermisoItem } from "../../types";

export function useCatalogos(token: string | null) {
  const [roles, setRoles] = useState<RolItem[]>([]);
  const [cargos, setCargos] = useState<CargoItem[]>([]);
  const [equipos, setEquipos] = useState<EquipoItem[]>([]);
  const [tiposPermiso, setTiposPermiso] = useState<TipoPermisoItem[]>([]);
  const [tiposBeneficio, setTiposBeneficio] = useState<TipoBeneficioItem[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!token) return;
    setLoading(true);

    Promise.all([getRoles(token), getCargos(token), getEquipos(token), getTiposPermiso(token),getTiposBeneficio(token),])
      .then(([rolesRes, cargosRes, equiposRes, permisosRes, beneficiosRes]) => {
        setRoles(rolesRes);
        setCargos(cargosRes);
        setEquipos(equiposRes);
        setTiposPermiso(permisosRes);
        setTiposBeneficio(beneficiosRes);
      })
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, [token]);

  return { roles, cargos, equipos, tiposPermiso, tiposBeneficio, loading, error };
}
