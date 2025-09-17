import { useEffect, useState } from "react";

export interface PaginatedResponse<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number; // página actual
}

export function usePaginatedResource<T>(
  fetchFn: (params: Record<string, any>) => Promise<PaginatedResponse<T>>,
  token: string | null,
  filtros: Record<string, string | number | undefined> = {},
  page: number = 0,
  pageSize: number = 10
) {
  const [data, setData] = useState<T[]>([]);
  const [totalPages, setTotalPages] = useState(1);
  const [totalElements, setTotalElements] = useState(0);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!token) return;

    let isMounted = true;
    setLoading(true);
    setError(null);

    fetchFn({ ...filtros, page, size: pageSize })
      .then((res) => {
        if (!isMounted) return;
        setData(res.content ?? []);
        setTotalPages(res.totalPages ?? 1);
        setTotalElements(res.totalElements ?? 0);
      })
      .catch((err) => isMounted && setError(err.message))
      .finally(() => isMounted && setLoading(false));

    return () => {
      isMounted = false;
    };
  }, [token, page, pageSize, JSON.stringify(filtros)]);

  return { data, totalPages, totalElements, loading, error, setData };
}
