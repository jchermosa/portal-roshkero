import { useEffect, useState } from "react";
import { getSolicitudById as getSolicitudUsuario } from "../../services/RequestService";
import type { SolicitudItem } from "../../types";

export function useRequestViewUsuario(token: string | null, id: string | null) {
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
        const result = await getSolicitudUsuario(token, id);
        setSolicitud({
          ...result,
            usuario: result.nombreUsuario,
            subtipo: result.nombreSubTipoSolicitud,
            nombreLider: result.nombreLider ?? "—",
        });
      } catch (err) {
        setError(err instanceof Error ? err.message : "Error desconocido");
      } finally {
        setLoading(false);
      }
    };

    fetchSolicitud();
  }, [token, id]);

  return { solicitud, loading, error };
}
