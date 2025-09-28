import { useEffect, useState } from "react";
import { getDispositivosAsignados } from "../../services/DeviceAssignmentService";
import type { DispositivoAsignadoItem, PaginatedResponse } from "../../types";

export function useDispositivosAsignados(
  token: string | null,
  page: number = 0,
  pageSize: number = 10
) {
  const [data, setData] = useState<DispositivoAsignadoItem[]>([]);
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const refresh = () => {
    if (!token) return;
    setLoading(true);
    getDispositivosAsignados(token, page, pageSize)
      .then((res: PaginatedResponse<DispositivoAsignadoItem>) => {
        setData(res.content);
        setTotalPages(res.totalPages);
      })
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    refresh();
  }, [token, page, pageSize]);

  return { data, totalPages, loading, error, refresh };
}
