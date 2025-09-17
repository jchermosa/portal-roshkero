import { usePaginatedResource } from "../common/usePaginatedResource";
import type { CatalogItem } from "../../types";
import { getRoles, getCargos, getEquipos } from "../../services/CatalogService";

export function useCatalogos(token: string | null) {
  const { data: roles, loading: loadingRoles, error: errorRoles } =
    usePaginatedResource<CatalogItem>(
      (params) => getRoles(token!, params),
      token
    );

  const { data: cargos, loading: loadingCargos, error: errorCargos } =
    usePaginatedResource<CatalogItem>(
      (params) => getCargos(token!, params),
      token
    );

  const { data: equipos, loading: loadingEquipos, error: errorEquipos } =
    usePaginatedResource<CatalogItem>(
      (params) => getEquipos(token!, params),
      token
    );

  return {
    roles,
    cargos,
    equipos,
    loading: loadingRoles || loadingCargos || loadingEquipos,
    error: errorRoles || errorCargos || errorEquipos,
  };
}
