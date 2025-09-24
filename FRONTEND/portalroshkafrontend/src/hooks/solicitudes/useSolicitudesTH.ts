// src/hooks/solicitudes/useSolicitudesTH.ts
import { useState, useEffect } from "react";
import { getSolicitudesTH } from "../../services/RequestService";

// Importar los mocks actualizados
import mockPermisos from "../../data/mockSolicitudPermiso.json";
import mockBeneficios from "../../data/mockSolicitudBeneficios.json";

// Tipo normalizado que usará la tabla
export interface SolicitudData {
  id: number;
  nombre: string;
  apellido: string;
  tipoNombre: string;
  cantidad_dias?: number | null;
  fecha_inicio?: string | null;
  fecha_fin?: string | null;
  estado: "P" | "A" | "R";
  comentario?: string;
}

export function useSolicitudesTH(
  token: string | null,
  filtros: Record<string, string | undefined>,
  page: number,
  size: number = 10
) {
  const [data, setData] = useState<SolicitudData[]>([]);
  const [totalPages, setTotalPages] = useState(1);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // Cambiar a false en producción
  const modoDesarrollo = true;

  useEffect(() => {
    if (!token) {
      setData([]);
      setLoading(false);
      return;
    }

    setLoading(true);
    setError(null);

    if (modoDesarrollo) {
      setTimeout(() => {
        try {
          let rawData: any[] = [];

          // Seleccionar dataset según filtro
          if (filtros.tipoSolicitud === "PERMISO") {
            rawData = [...(mockPermisos as any[])];
          } else if (filtros.tipoSolicitud === "BENEFICIO") {
            rawData = [...(mockBeneficios as any[])];
          } else {
            rawData = [
              ...(mockPermisos as any[]),
              ...(mockBeneficios as any[]),
            ];
          }

          // Filtros adicionales
          if (filtros.tipoId) {
            rawData = rawData.filter(
              (s) => s.subtipo?.id === Number(filtros.tipoId)
            );
          }

          if (filtros.usuarioNombre) {
            const searchText = filtros.usuarioNombre.toLowerCase();
            rawData = rawData.filter(
              (s) =>
                s.nombre?.toLowerCase().includes(searchText) ||
                s.apellido?.toLowerCase().includes(searchText)
            );
          }

          if (filtros.estado) {
            rawData = rawData.filter((s) => s.estado === filtros.estado);
          }

          // Paginación
          const totalElements = rawData.length;
          const start = page * size;
          const paginatedData = rawData.slice(start, start + size);

          // Normalizar al formato de tabla
          const normalizedData: SolicitudData[] = paginatedData.map((s) => ({
            id: s.id,
            nombre: s.nombre,
            apellido: s.apellido,
            tipoNombre: s.subtipo?.nombre ?? s.tipo_solicitud,
            cantidad_dias: s.cantidad_dias ?? null,
            fecha_inicio: s.fecha_inicio ?? null,
            fecha_fin: s.fecha_fin ?? null,
            estado: s.estado,
            comentario: s.comentario ?? "",
          }));

          setData(normalizedData);
          setTotalPages(Math.ceil(totalElements / size));
        } catch (err) {
          console.error("Error procesando mock:", err);
          setError("Error al procesar datos mock");
        } finally {
          setLoading(false);
        }
      }, 300);

      return;
    }

    // Fetch real del backend
    const fetchData = async () => {
      try {
        const params = {
          ...filtros,
          page,
          size,
        };

        const response = await getSolicitudesTH(token, params);

        const normalizedData: SolicitudData[] = response.content.map((s: any) => ({
          id: s.id,
          nombre: s.nombre,
          apellido: s.apellido,
          tipoNombre: s.subtipo?.nombre ?? s.tipo_solicitud,
          cantidad_dias: s.cantidad_dias ?? null,
          fecha_inicio: s.fecha_inicio ?? null,
          fecha_fin: s.fecha_fin ?? null,
          estado: s.estado,
          comentario: s.comentario ?? "",
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

  return {
    data,
    totalPages,
    loading,
    error,
  };
}
