import { usePaginatedResource } from "../common/usePaginatedResource";
import { getUsuarios } from "../../services/UserService";
import type { UsuarioItem } from "../../types";

export function useUsuarios(
  token: string | null,
  filtros: Record<string, string | number | undefined> = {},
  page: number = 0,
  pageSize: number = 10
) {
  return usePaginatedResource<UsuarioItem>(
    (params) => getUsuarios(token!, params),
    token,
    filtros,
    page,
    pageSize
  );
}
