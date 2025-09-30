// src/hooks/solicitudes/useSolicitudesTH.ts
import { useState, useEffect } from "react";
import { getSolicitudes } from "../../services/RequestTHService";
import type { SolicitudItem } from "../../types";

interface FiltrosTH {
  tipoSolicitud?: "PERMISO" | "BENEFICIO" | "VACACIONES";
  tipoId?: string;
  estado?: string;
  usuarioNombre?: string;
}

export function useSolicitudesTH(
  token: string | null,
  filtros: FiltrosTH,
  page: number,
  size: number = 10
) {
  const [data, setData] = useState<SolicitudItem[]>([]);
  const [totalPages, setTotalPages] = useState(1);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const modoDesarrollo = true; // cambiar a false en producción

  useEffect(() => {
    if (!token) {
      setData([]);
      setLoading(false);
      return;
    }

    setLoading(true);
    setError(null);

    // -----------------------
    // PRODUCCIÓN (API REAL)
    // -----------------------
    const fetchData = async () => {
      try {
        const params = {
          ...filtros,
          page,
          size,
        };

        const response = await getSolicitudes(token, params);
        
        const normalizedData: SolicitudItem[] = response.content.map((s: any) => ({
          idSolicitud: s.idSolicitud ?? s.id,
          usuario: s.usuario ?? `${s.nombre ?? ""} ${s.apellido ?? ""}`.trim(),
          tipoSolicitud: s.tipoSolicitud ?? s.tipo_solicitud,
          fechaInicio: s.fechaInicio ?? s.fecha_inicio,
          cantidadDias: s.cantidadDias ?? s.cantidad_dias ?? 0,
          fechaCreacion: s.fechaCreacion ?? s.fecha_creacion ?? new Date().toISOString(),
          estado: s.estado,
        }));

        setData(normalizedData);
        setTotalPages(response.totalPages);
      } catch (err) {
        setError(err instanceof Error ? err.message : "Error desconocido");
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [token, JSON.stringify(filtros), page, size, modoDesarrollo]);

  return { data, totalPages, loading, error };
}
