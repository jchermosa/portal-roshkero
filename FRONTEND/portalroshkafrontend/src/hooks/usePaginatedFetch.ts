// src/hooks/usePaginatedFetch.ts
import { useEffect, useState } from "react";

interface PaginatedResponse<T> {
  content?: T[];       // lo que devuelve Spring/Pageable
  totalPages?: number; // cantidad de páginas
  [key: string]: any;  // otros campos opcionales
}

export function usePaginatedFetch<T>(
  endpoint: string,                       // ej: "usuarios" o "solicitudes"
  token: string | null,                   // JWT del contexto
  params: Record<string, string | number | undefined> = {}, // filtros/paginación
  options?: {
    defaultPageSize?: number;             // tamaño de página por defecto
    transform?: (data: any) => T[];       // función opcional para transformar la respuesta
  }
) {
  const [data, setData] = useState<T[]>([]);
  const [totalPages, setTotalPages] = useState(1);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!token) return; // si no hay token, no hago nada

    setLoading(true);
    setError(null);

    // Construyo el querystring
    const query = new URLSearchParams();
    Object.entries({
        size: options?.defaultPageSize ?? 10,
        ...params,
        }).forEach(([k, v]) => {
        const value = String(v ?? "");
        if (value !== "") query.append(k, value);
        });


    // Hago la petición
    fetch(`/api/${endpoint}?${query.toString()}`, {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then(async (res) => {
        if (!res.ok) throw new Error(await res.text());
        return res.json();
      })
      .then((raw: PaginatedResponse<T>) => {
        // si quiero modificar la data, uso transform
        const items = options?.transform
          ? options.transform(raw)
          : raw.content ?? (Array.isArray(raw) ? raw : []);

        setData(items);
        setTotalPages(raw.totalPages ?? 1);
      })
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, [endpoint, token, JSON.stringify(params)]);

  return { data, totalPages, loading, error, setData };
}
