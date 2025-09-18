import { usePaginatedResource } from "../common/usePaginatedResource";
import type { CatalogItem } from "../../types";
import { getRoles, getCargos, getEquipos } from "../../services/CatalogService";

export function useCatalogos(token: string | null) {
  const { data: roles, loading: loadingRoles, error: errorRoles } =
    usePaginatedResource<CatalogItem>((_params) => getRoles(token!), token);

  const { data: cargos, loading: loadingCargos, error: errorCargos } =
    usePaginatedResource<CatalogItem>((_params) => getCargos(token!), token);

  const { data: equipos, loading: loadingEquipos, error: errorEquipos } =
    usePaginatedResource<CatalogItem>((_params) => getEquipos(token!), token);

  return {
    roles,
    cargos,
    equipos,
    loading: loadingRoles || loadingCargos || loadingEquipos,
    error: errorRoles || errorCargos || errorEquipos,
  };
}
