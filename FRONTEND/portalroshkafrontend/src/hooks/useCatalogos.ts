import { usePaginatedFetch } from "./usePaginatedFetch";
import type { CatalogItem } from "../types";

export function useCatalogos(token: string | null) {
  const { data: roles, loading: loadingRoles, error: errorRoles } =
    usePaginatedFetch<CatalogItem>("catalogos/roles", token);

  const { data: cargos, loading: loadingCargos, error: errorCargos } =
    usePaginatedFetch<CatalogItem>("catalogos/cargos", token);

  const { data: equipos, loading: loadingEquipos, error: errorEquipos } =
    usePaginatedFetch<CatalogItem>("catalogos/equipos", token);

  return {
    roles,
    cargos,
    equipos,
    loading: loadingRoles || loadingCargos || loadingEquipos,
    error: errorRoles || errorCargos || errorEquipos,
  };
}
