import { useEffect, useState } from "react";
import { getSolicitudesDispositivo } from "../../services/DeviceRequestService";
import type { SolicitudDispositivoItem } from "../../types";

export function useSolicitudesDispositivo(token: string | null) {
  const [data, setData] = useState<SolicitudDispositivoItem[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!token) return;

    setLoading(true);
    getSolicitudesDispositivo(token)
      .then(setData)
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, [token]);

  return { data, loading, error };
}
