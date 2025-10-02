import { useEffect, useState, useCallback } from "react";
import { getUsuarios } from "../../services/UserService";
import type { UsuarioItem, FiltrosUsuarios } from "../../types";

export function useUsuarios(
  token: string | null,
  filtros: FiltrosUsuarios,
  page: number,
  size: number
) {
  const [data, setData] = useState<UsuarioItem[]>([]);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const fetchUsuarios = useCallback(async () => {
    if (!token) return;

    setLoading(true);
    setError(null);

    try {
      const response = await getUsuarios(token, filtros, page, size);
      setData(response.content);
      setTotalPages(response.totalPages);
      setTotalElements(response.totalElements);
    } catch (err: any) {
      console.error(err);
      setError(err.message || "Error al cargar usuarios");
    } finally {
      setLoading(false);
    }
  }, [token, filtros, page, size]);

  useEffect(() => {
    fetchUsuarios();
  }, [fetchUsuarios]);

  return {
    data,
    totalPages,
    totalElements,
    loading,
    error,
    refresh: fetchUsuarios, // 👈 ahora lo exponemos
  };
}
