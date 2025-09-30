import { useEffect, useState } from "react";
import { useAuth } from "../../context/AuthContext";
import { getSolicitudesPermisoUsuario, type PaginatedResponse } from "../../services/RequestService";
import type { SolicitudPermiso } from "../../types";

export function useSolicitudesPermiso(
  page: number,
  subtipo?: string
) {
  const { token, user } = useAuth();
  
  const [data, setData] = useState<PaginatedResponse<SolicitudPermiso> | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!token || !user?.id) {
      setLoading(false);
      return;
    }

    setLoading(true);
    setError(null);

    const params: Record<string, string | number | undefined> = {
      page,
      size: 10,
    };
    
    if (subtipo) {
      params.subtipo = subtipo;
    }

    getSolicitudesPermisoUsuario(token, user.id, params)
      .then((res) => setData(res))
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, [token, user?.id, page, subtipo]);

  return {
    solicitudes: data?.content ?? [],
    totalPages: data?.totalPages ?? 1,
    loading,
    error,
  };
}