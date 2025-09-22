import { useEffect, useState } from "react";
import { getTiposPermiso, getTiposBeneficio } from "../../services/CatalogService";
import type { TipoBeneficioItem, TipoPermisoItem } from "../../types";

export function useCatalogosSolicitudes(token: string | null) {
  const [tiposPermiso, setTiposPermiso] = useState<TipoPermisoItem[]>([]);
  const [tiposBeneficio, setTiposBeneficio] = useState<TipoBeneficioItem[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!token) return;
    setLoading(true);

    Promise.all([getTiposPermiso(token), getTiposBeneficio(token)])
      .then(([permisosRes, beneficiosRes]) => {
        setTiposPermiso(permisosRes ?? []);
        setTiposBeneficio(beneficiosRes ?? []);
      })
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, [token]);

  return { tiposPermiso, tiposBeneficio, loading, error };
}
