import { usePaginatedResource } from "../common/usePaginatedResource";
import { getDispositivos } from "../../services/DeviceService";
import type { DispositivoItem } from "../../types";

export function useDispositivos(
  token: string | null,
  filtros: Record<string, string | number | undefined> = {},
  page: number = 0,
  pageSize: number = 10
) {
  return usePaginatedResource<DispositivoItem>(
    (params) => getDispositivos(token!, params),
    token,
    filtros,
    page,
    pageSize
  );
}
