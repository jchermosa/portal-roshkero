import { useEffect, useState, useCallback } from "react";
import { getUbicaciones } from "../../services/UbicacionService";
import type { UbicacionItem, PageResponse } from "../../types";

export function useUbicaciones(
  token: string | null,
  page: number = 0,
  pageSize: number = 10
) {
  const [data, setData] = useState<UbicacionItem[]>([]);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchData = useCallback(async () => {
    if (!token) return;
    setLoading(true);
    setError(null);
    try {
      const pageRes: PageResponse<UbicacionItem> = await getUbicaciones(token, page, pageSize);
      setData(pageRes.content ?? []);
      setTotalPages(pageRes.totalPages ?? 0);
      setTotalElements(pageRes.totalElements ?? 0);
    } catch (err: any) {
      setError(err.message || "Error al cargar ubicaciones");
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
    totalElements,
    loading,
    error,
    refresh: fetchData,
  };
}
