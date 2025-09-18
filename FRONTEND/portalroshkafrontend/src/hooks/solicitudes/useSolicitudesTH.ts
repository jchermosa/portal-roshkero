// src/hooks/solicitudes/useSolicitudesTH.ts
import { useState, useEffect } from "react";
import { getSolicitudesTH } from "../../services/RequestTHService";
import type { SolicitudItem } from "../../types";

// Mock data import
import rawMockSolicitudes from "../../data/mockSolicitudes.json";

// Mock data con estructura correcta
const mockSolicitudes: SolicitudItem[] = rawMockSolicitudes.map((s) => ({
  ...s,
  estado: s.estado as "P" | "A" | "R",
  lideres: s.lideres || []
}));

export function useSolicitudesTH(
  token: string | null,
  filtros: Record<string, string | undefined>,
  page: number,
  size: number = 10
) {
  const [data, setData] = useState<SolicitudItem[]>([]);
  const [totalPages, setTotalPages] = useState(1);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // Modo desarrollo - cambiar a false cuando el backend esté listo
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
      // Usar datos mock
      setTimeout(() => {
        let solicitudesFiltradas = mockSolicitudes;

        // Aplicar filtros
        if (filtros.tipoId) {
          solicitudesFiltradas = solicitudesFiltradas.filter(
            (s) => s.tipo?.nombre === filtros.tipoId
          );
        }
        if (filtros.usuarioNombre) {
          solicitudesFiltradas = solicitudesFiltradas.filter((s) =>
            `${s.nombre || ''} ${s.apellido || ''}`
              .toLowerCase()
              .includes((filtros.usuarioNombre || '').toLowerCase())
          );
        }
        if (filtros.estado) {
          solicitudesFiltradas = solicitudesFiltradas.filter(
            (s) => s.estado === filtros.estado
          );
        }

        // Simular paginación
        const start = page * size;
        const paginatedData = solicitudesFiltradas.slice(start, start + size);
        
        setData(paginatedData);
        setTotalPages(Math.ceil(solicitudesFiltradas.length / size));
        setLoading(false);
      }, 500);
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
        
        setData(response.content);
        setTotalPages(response.totalPages);
      } catch (err) {
        setError(err instanceof Error ? err.message : "Error desconocido");
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [token, filtros, page, size, modoDesarrollo]);

  const refetch = () => {
    setLoading(true);
    setError(null);
    // Re-ejecutar el effect
  };

  return {
    data,
    totalPages,
    loading,
    error,
    refetch,
  };
}