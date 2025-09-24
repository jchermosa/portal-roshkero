import { useEffect, useState } from "react";
import { getSolicitudById, aprobarSolicitud, rechazarSolicitud } from "../../services/RequestService";
import type { SolicitudItem } from "../../types";

export function useSolicitudDetail(token: string | null, id: number | null) {
  const [solicitud, setSolicitud] = useState<SolicitudItem | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!token || !id) {
      setSolicitud(null);
      setLoading(false);
      return;
    }

    const fetchSolicitud = async () => {
      setLoading(true);
      setError(null);

      try {
        const result = await getSolicitudById(token, id);
        setSolicitud(result);
      } catch (err) {
        setError(err instanceof Error ? err.message : "Error desconocido");
      } finally {
        setLoading(false);
      }
    };

    fetchSolicitud();
  }, [token, id]);

  const aprobar = async () => {
    if (!token || !id) return;
    await aprobarSolicitud(token, id);
  };

  const rechazar = async () => {
    if (!token || !id) return;
    await rechazarSolicitud(token, id);
  };

  return { solicitud, loading, error, aprobar, rechazar };
}