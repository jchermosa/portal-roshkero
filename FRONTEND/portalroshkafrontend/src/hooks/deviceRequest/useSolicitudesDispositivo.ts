import { useCallback, useEffect, useState } from "react";
import {
  getSolicitudesDispositivoAdmin,
  getSolicitudesDispositivoUsuario,
} from "../../services/DeviceRequestService";
import {
  mapAdminSolicitudToUI,
  mapUserSolicitudToUI,
} from "../../mappers/solicitudDispositivoMapper";
import type { SolicitudDispositivoUI } from "../../types";

export function useSolicitudesDispositivo(
  token: string | null,
  page: number = 0,
  size: number = 10,
  isSysAdmin: boolean = false
) {
  const [data, setData] = useState<SolicitudDispositivoUI[]>([]);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchData = useCallback(async () => {
    if (!token) return;
    setLoading(true);
    try {
      if (isSysAdmin) {
        // ADMIN → paginado
        const res = await getSolicitudesDispositivoAdmin(token, page, size);
        setData(res.content.map(mapAdminSolicitudToUI));
        setTotalPages(res.totalPages);
      } else {
        // USUARIO → array simple
        const res = await getSolicitudesDispositivoUsuario(token);
        setData(res.map(mapUserSolicitudToUI));
        setTotalPages(1); // no hay paginación
      }
    } catch (err: any) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }, [token, page, size, isSysAdmin]);

  useEffect(() => {
    fetchData();
  }, [fetchData]);

  return {
    data,
    totalPages,
    loading,
    error,
    refresh: fetchData,
  };
}
