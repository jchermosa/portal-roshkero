import { useEffect, useState } from "react";
import { useAuth } from "../../context/AuthContext";
import { getSolicitudesPermisoBeneficio } from "../../services/RequestService";
import type { SolicitudItem } from "../../types";

export function useSolicitudesUsuario(
  page: number,
  subTipo?: string,
  size: number = 10
) {
  const { token } = useAuth();

  const [allSolicitudes, setAllSolicitudes] = useState<SolicitudItem[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!token) {
      setLoading(false);
      return;
    }

    setLoading(true);
    setError(null);

    getSolicitudesPermisoBeneficio(token)
      .then((res) => {
        const filtradas = subTipo
          ? res.filter((s) => s.subTipo === subTipo)
          : res;
        setAllSolicitudes(filtradas);
      })
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, [token, subTipo]);

  // --- paginación local ---
  const totalPages = Math.max(1, Math.ceil(allSolicitudes.length / size));
  const start = page * size;
  const paginated = allSolicitudes.slice(start, start + size);

  return {
    solicitudes: paginated,
    totalPages,
    loading,
    error,
  };
}
