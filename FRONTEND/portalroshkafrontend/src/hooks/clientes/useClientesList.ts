import { useCallback, useEffect, useMemo, useState } from "react";
import { getClientes } from "../../services/ClientesService";
import type { ClientesPageResponse } from "../../types";

type Params = {
  page?: number;
  size?: number;
  sortBy?: string; 
  sort?: string;   
};

type State = {
  data?: ClientesPageResponse;
  loading: boolean;
  error?: string;
};

export function useClientesList(
  token: string | null,
  params: Params = { page: 0, size: 10, sortBy: "default" }
) {
  const [state, setState] = useState<State>({ loading: true });

  const stableParams = useMemo(
    () => ({
      page: params.page ?? 0,
      size: params.size ?? 10,
      sortBy: params.sortBy,
      sort: params.sort,
    }),
    [params.page, params.size, params.sortBy, params.sort]
  );

  const fetchData = useCallback(async () => {
    if (!token) return;
    setState((s) => ({ ...s, loading: true, error: undefined }));
    try {
      const data = await getClientes(token, stableParams);
      setState({ data, loading: false });
    } catch (e: any) {
      setState({ loading: false, error: e?.message || "Error al cargar clientes" });
    }
  }, [token, stableParams]);

  useEffect(() => {
    fetchData();
  }, [fetchData]);

  return {
    ...state,
    refresh: fetchData,
  };
}
