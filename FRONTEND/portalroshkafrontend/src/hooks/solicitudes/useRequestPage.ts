import { useEffect, useState } from "react";
import { useAuth } from "../../context/AuthContext";
import { type PaginatedResponse, getSolicitudesUsuario } from "../../services/RequestService";
import type { SolicitudItem } from "../../types";

interface UseSolicitudesParams {
  page: number;
  tipo?: "PERMISO" | "BENEFICIO" | "VACACIONES";
  subtipo?: string;
  estado?: string;
}

export function useSolicitudesUsuario({
  page,
  tipo,
  subtipo,
  estado,
}: UseSolicitudesParams) {
  const { token, user } = useAuth();
  
  const [data, setData] = useState<PaginatedResponse<SolicitudItem> | null>(null);
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
    
    if (tipo) params.tipo = tipo;
    if (subtipo) params.subtipo = subtipo;
    if (estado) params.estado = estado;

    getSolicitudesUsuario(token, user.id, params)
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
