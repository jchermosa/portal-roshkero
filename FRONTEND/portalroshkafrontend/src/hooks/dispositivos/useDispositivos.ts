import { useState, useEffect } from "react";
import { getDispositivos } from "../../services/DeviceService";
import type { DispositivoItem } from "../../types";

export function useDispositivos(
  token: string | null,
  filtros: Record<string, string | number | undefined> = {},
  page: number = 0,
  pageSize: number = 10
) {
  const [data, setData] = useState<DispositivoItem[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [totalPages, setTotalPages] = useState(1);
  const [totalElements, setTotalElements] = useState(0);

  useEffect(() => {
    if (!token) {
      setData([]);
      setLoading(false);
      return;
    }

    let isMounted = true;
    setLoading(true);
    setError(null);

    getDispositivos(token, page, pageSize)
      .then((res) => {
        if (!isMounted) return;

        // El backend ya devuelve { content, totalPages, totalElements }
        let dispositivos = res.content ?? [];

        // Filtros locales (por si quieres mantenerlos en frontend)
        if (filtros.categoria) {
          dispositivos = dispositivos.filter(
            (d) => d.categoria === filtros.categoria
          );
        }
        if (filtros.estado) {
          dispositivos = dispositivos.filter(
            (d) => d.estado === filtros.estado
          );
        }
        if (filtros.encargado) {
          dispositivos = dispositivos.filter(
            (d) => d.encargado?.toString() === filtros.encargado
          );
        }

        setData(dispositivos);
        setTotalPages(res.totalPages ?? 1);
        setTotalElements(res.totalElements ?? dispositivos.length);
      })
      .catch((err) => {
        if (isMounted) {
          setError(err.message || "Error al cargar dispositivos");
        }
      })
      .finally(() => {
        if (isMounted) {
          setLoading(false);
        }
      });

    return () => {
      isMounted = false;
    };
  }, [token, page, pageSize, filtros.categoria, filtros.estado, filtros.encargado]);

  return {
    data,
    totalPages,
    totalElements,
    loading,
    error,
  };
}
