import { useCallback, useEffect, useMemo, useState } from "react";
import { getCargos } from "../../services/CargosService";
import type { PageSpring, CargoListItem } from "../../types";

type Params = {
  page?: number;   
  size?: number;   
  sort?: string;   
};

type State = {
  data?: PageSpring<CargoListItem>;
  loading: boolean;
  error?: string;
};

export function useCargosList(
  token: string | null,
  params: Params = { page: 0, size: 10 }
) {
  const [state, setState] = useState<State>({ loading: true });

  const stableParams = useMemo(
    () => ({
      page: params.page ?? 0,
      size: params.size ?? 10,
      sort: params.sort,
    }),
    [params.page, params.size, params.sort]
  );

  const fetchData = useCallback(async () => {
    if (!token) return;
    setState((s) => ({ ...s, loading: true, error: undefined }));
    try {
      const data = await getCargos(token, stableParams);
      setState({ data, loading: false });
    } catch (e: any) {
      setState({ loading: false, error: e?.message || "Error al cargar cargos" });
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
