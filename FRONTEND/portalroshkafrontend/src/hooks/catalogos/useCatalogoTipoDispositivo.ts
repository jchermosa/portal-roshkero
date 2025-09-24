import { useEffect, useState } from "react";
import { getTiposDispositivo, type TipoDispositivoItem } from "../../services/TipoDispositivoService";

export function useCatalogoTipoDispositivo(token: string | null) {
  const [tipos, setTipos] = useState<TipoDispositivoItem[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!token) return;
    setLoading(true);

    getTiposDispositivo(token)
      .then((res) => setTipos(res))
      .catch((err) => setError(err.message || "Error al cargar tipos de dispositivo"))
      .finally(() => setLoading(false));
  }, [token]);

  return { tipos, loading, error };
}
