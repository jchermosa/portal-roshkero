import { useEffect, useState } from "react";
import type { SolicitudItem } from "../../types";
import { getSolicitudes } from "../../services/RequestTHService";

interface Filters {
  tipoSolicitud?: "PERMISO" | "BENEFICIO" | "VACACIONES";
  estado?: string;
  subTipo?: string;
}

export function useSolicitudesTH(
  token: string | null,
  filters: Filters,
  page: number,
  pageSize = 10
) {
  const [data, setData] = useState<SolicitudItem[]>([]);
  const [totalPages, setTotalPages] = useState(1);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const { tipoSolicitud, estado, subTipo } = filters;

  const fetchData = () => {
    if (!token) return;

    setLoading(true);
    setError(null);

    getSolicitudes(token, { tipoSolicitud, estado })
      .then((res) => {
        let solicitudes = res.content;

        if (subTipo) {
          solicitudes = solicitudes.filter((s: any) => s.subTipo === subTipo);
        }

        setTotalPages(Math.ceil(solicitudes.length / pageSize));
        setData(solicitudes.slice(page * pageSize, (page + 1) * pageSize));
      })
      .catch((err: any) => setError(err.message))
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    fetchData();
  }, [token, tipoSolicitud, estado, subTipo, page, pageSize]);

  return { data, totalPages, loading, error, refetch: fetchData };
}