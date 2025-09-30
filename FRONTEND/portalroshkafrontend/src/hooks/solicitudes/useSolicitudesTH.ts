// src/hooks/solicitudes/useSolicitudesTH.ts
import { useState, useEffect } from "react";
import { getSolicitudesTH } from "../../services/RequestTHService";

// Importar los mocks directamente
import mockSolicitudes from "../../data/mockSolicitudes.json";
import mockBeneficios from "../../data/mockBeneficios.json";

// Tipo normalizado que usará la tabla
export interface SolicitudData {
  id: number;
  nombre: string;
  apellido: string;
  tipoNombre: string;
  cantidad_dias?: number | null;
  fecha_inicio?: string | null;
  lideres?: any[];
  numero_aprobaciones?: number;
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

  // Modo desarrollo
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
      // Simular delay
      setTimeout(() => {
        try {
          let rawData: any[] = [];

          // Combinar ambos mocks o filtrar por tipo
          if (filtros.tipoSolicitud === "PERMISO") {
            rawData = [...mockSolicitudes];
          } else if (filtros.tipoSolicitud === "BENEFICIO") {
            rawData = [...mockBeneficios];
          } else {
            rawData = [...mockSolicitudes, ...mockBeneficios];
          }

          console.log("Raw data:", rawData); // Debug

          // Aplicar filtros
          if (filtros.tipoId) {
            rawData = rawData.filter((s) => {
              // Para permisos: comparar con id_solicitud_tipo
              if (filtros.tipoSolicitud === "PERMISO") {
                return s.id_solicitud_tipo === Number(filtros.tipoId);
              }
              // Para beneficios: comparar con tipo.id
              if (filtros.tipoSolicitud === "BENEFICIO") {
                return s.tipo?.id === Number(filtros.tipoId);
              }
              return false;
            });
          }

          if (filtros.usuarioNombre) {
            const searchText = filtros.usuarioNombre.toLowerCase();
            rawData = rawData.filter((s) =>
              s.nombre?.toLowerCase().includes(searchText) ||
              s.apellido?.toLowerCase().includes(searchText)
            );
          }

          if (filtros.estado) {
            rawData = rawData.filter((s) => s.estado === filtros.estado);
          }

          console.log("Filtered data:", rawData); // Debug

          // Paginación
          const totalElements = rawData.length;
          const start = page * size;
          const paginatedData = rawData.slice(start, start + size);

          // Normalizar datos para la tabla
          const normalizedData: SolicitudData[] = paginatedData.map((s) => ({
            id: s.id,
            nombre: s.nombre || "",
            apellido: s.apellido || "",
            tipoNombre: s.tipo?.nombre || s.tipo || "",
            cantidad_dias: s.cantidad_dias ?? null,
            fecha_inicio: s.fecha_inicio ?? null,
            lideres: s.lideres ?? [],
            numero_aprobaciones: s.numero_aprobaciones ?? 0,
            estado: s.estado,
            comentario: s.comentario || "",
          }));

          console.log("Normalized data:", normalizedData); // Debug

          setData(normalizedData);
          setTotalPages(Math.ceil(totalElements / size));
          setLoading(false);
        } catch (err) {
          console.error("Error processing mock data:", err);
          setError("Error al procesar datos mock");
          setLoading(false);
        }
      }, 300); // Reducido el delay

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
        
        // Normalizar datos del backend
        const normalizedData: SolicitudData[] = response.content.map((s: any) => ({
          id: s.id,
          nombre: s.nombre || "",
          apellido: s.apellido || "",
          tipoNombre: s.tipo?.nombre || "",
          cantidad_dias: s.cantidad_dias ?? null,
          fecha_inicio: s.fecha_inicio ?? null,
          lideres: s.lideres ?? [],
          numero_aprobaciones: s.numero_aprobaciones ?? 0,
          estado: s.estado,
          comentario: s.comentario || "",
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