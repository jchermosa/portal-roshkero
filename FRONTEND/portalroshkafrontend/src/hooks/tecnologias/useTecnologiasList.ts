import { useCallback, useEffect, useMemo, useState } from "react";
import { getTecnologias } from "../../services/TecnologiasService";
import type { TecnologiasPageResponse } from "../../types";

type Params = {
  page?: number;
  size?: number;
  sort?: string;
};

type State = {
  data?: TecnologiasPageResponse;
  loading: boolean;
  error?: string;
};

export function useTecnologiasList(
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
      const data = await getTecnologias(token, stableParams);
      setState({ data, loading: false });
    } catch (e: any) {
      setState({
        loading: false,
        error: e?.message || "Error al cargar tecnologías",
      });
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
