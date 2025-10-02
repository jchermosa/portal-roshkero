import { useEffect, useState } from "react";
import { useAuth } from "../../context/AuthContext";
import { getSolicitudesVacaciones } from "../../services/RequestService";
import type { SolicitudItem } from "../../types";

export function useSolicitudesVacaciones(page: number, estado?: string, pageSize = 10) {
  const { token } = useAuth();
  const [solicitudes, setSolicitudes] = useState<SolicitudItem[]>([]);
  const [totalPages, setTotalPages] = useState(1);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!token) {
      setLoading(false);
      return;
    }

    setLoading(true);
    setError(null);

    const params: Record<string, string | number> = { 
      page,
      size: pageSize 
    };
    
    if (estado) {
      params.estado = estado;
    }

    getSolicitudesVacaciones(token, params)
      .then(res => {
        setSolicitudes(res.content);
        setTotalPages(res.totalPages);
      })
      .catch(err => {
        setError(err.message || "Error al cargar las solicitudes");
        setSolicitudes([]);
      })
      .finally(() => setLoading(false));
  }, [token, page, estado, pageSize]);

  return {
    solicitudes,
    totalPages,
    loading,
    error,
  };
}