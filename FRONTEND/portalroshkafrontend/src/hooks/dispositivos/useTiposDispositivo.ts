// src/hooks/dispositivos/useTiposDispositivo.ts
import { useEffect, useState } from "react";
import { getTiposDispositivo } from "../../services/TipoDispositivoService";
import type { TipoDispositivoItem } from "../../types";

export function useTiposDispositivo(token: string | null) {
  const [data, setData] = useState<TipoDispositivoItem[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!token) return;
    setLoading(true);
    getTiposDispositivo(token)
      .then(setData)
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, [token]);

  return { data, loading, error };
}
