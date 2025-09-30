// src/hooks/solicitudes/useSolicitudesTH.ts
import { useState, useEffect } from "react";
import { getSolicitudes } from "../../services/RequestTHService";
import mockSolicitudes from "../../data/mockSolicitudes.json";
import type { SolicitudItem } from "../../types";

interface FiltrosTH {
  tipoSolicitud?: "PERMISO" | "BENEFICIO";
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
    // MODO DESARROLLO (MOCKS)
    // -----------------------
    if (modoDesarrollo) {
      setTimeout(() => {
        try {
          let rawData: any[] = [...(mockSolicitudes as any[])];

          // Filtro por tipoSolicitud (PERMISO/BENEFICIO)
          if (filtros.tipoSolicitud) {
            rawData = rawData.filter(
              (s) => (s.tipoSolicitud ?? s.tipo_solicitud) === filtros.tipoSolicitud
            );
          }

          // Filtro por tipoId (subtipo)
          if (filtros.tipoId) {
            rawData = rawData.filter((s) => s.id_subtipo === Number(filtros.tipoId));
          }

          // Filtro por estado
          if (filtros.estado) {
            rawData = rawData.filter((s) => s.estado === filtros.estado);
          }

          // Filtro por nombre de usuario
          if (filtros.usuarioNombre) {
            const searchText = filtros.usuarioNombre.toLowerCase();
            rawData = rawData.filter(
              (s) =>
                `${s.nombre ?? ""} ${s.apellido ?? ""}`.toLowerCase().includes(searchText) ||
                (s.usuario ?? "").toLowerCase().includes(searchText)
            );
          }

          // Paginación
          const totalElements = rawData.length;
          const start = page * size;
          const paginatedData = rawData.slice(start, start + size);

          // Normalización a SolicitudItem
          const normalizedData: SolicitudItem[] = paginatedData.map((s) => ({
            idSolicitud: s.idSolicitud ?? s.id,
            usuario: s.usuario ?? `${s.nombre ?? ""} ${s.apellido ?? ""}`.trim(),
            tipoSolicitud: s.tipoSolicitud ?? s.tipo_solicitud,
            fechaInicio: s.fechaInicio ?? s.fecha_inicio,
            cantidadDias: s.cantidadDias ?? s.cantidad_dias ?? 0,
            fechaCreacion: s.fechaCreacion ?? s.fecha_creacion ?? new Date().toISOString(),
            estado: s.estado,
          }));

          setData(normalizedData);
          setTotalPages(Math.ceil(totalElements / size));
        } catch (err) {
          console.error("Error procesando mocks:", err);
          setError("Error al procesar datos mock");
        } finally {
          setLoading(false);
        }
      }, 300);

      return;
    }

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
