import { useEffect, useState, useCallback } from "react";
import { getTiposDispositivo } from "../../services/TipoDispositivoService";
import type { TipoDispositivoItem } from "../../types";

export function useTiposDispositivo(
  token: string | null,
  page: number = 0,
  pageSize: number = 10
) {
  const [data, setData] = useState<TipoDispositivoItem[]>([]);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchData = useCallback(async () => {
    if (!token) return;
    setLoading(true);
    try {
      const all = await getTiposDispositivo(token);
      const start = page * pageSize;
      const end = start + pageSize;
      setData(all.slice(start, end));
      setTotalPages(Math.ceil(all.length / pageSize));
    } catch (err: any) {
      setError(err.message || "Error al cargar tipos de dispositivo");
    } finally {
      setLoading(false);
    }
  }, [token, page, pageSize]);

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
