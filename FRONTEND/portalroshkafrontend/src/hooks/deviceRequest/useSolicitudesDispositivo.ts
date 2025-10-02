import { useCallback, useEffect, useState } from "react";
import {
  getSolicitudesDispositivoAdmin,
  getSolicitudesDispositivoUsuario,
} from "../../services/DeviceRequestService";
import {
  mapAdminSolicitudToUI,
  mapUserSolicitudToUI,
} from "../../mappers/solicitudDispositivoMapper";
import type { SolicitudDispositivoUI, PageResponse, SolicitudUserItem } from "../../types";

interface UseSolicitudesDispositivoResult {
  data: SolicitudDispositivoUI[];
  totalPages: number;
  loading: boolean;
  error: string | null;
  refresh: () => Promise<void>;
}

export function useSolicitudesDispositivo(
  token: string | null,
  page: number = 0,
  size: number = 10,
  isSysAdmin: boolean = false
): UseSolicitudesDispositivoResult {
  const [data, setData] = useState<SolicitudDispositivoUI[]>([]);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchData = useCallback(async () => {
    if (!token) {
      setData([]);
      setError("Token inválido");
      return;
    }

    setLoading(true);
    setError(null);

    try {
      if (isSysAdmin) {
        // ADMIN → paginado
        const res: PageResponse<any> = await getSolicitudesDispositivoAdmin(token, page, size);
        setData(res.content.map(mapAdminSolicitudToUI));
        setTotalPages(res.totalPages ?? 1);
      } else {
        // USUARIO → array simple (sin paginación)
        const res: SolicitudUserItem[] = await getSolicitudesDispositivoUsuario(token);
        const onlyDevices = res.filter((s) => s.tipoSolicitud === "DISPOSITIVO");
        setData(onlyDevices.map(mapUserSolicitudToUI));
        setTotalPages(1);
      }
    } catch (err: any) {
      const message =
        err instanceof Error ? err.message : "Error desconocido al cargar solicitudes";
      setError(message);
      setData([]);
      setTotalPages(0);
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
