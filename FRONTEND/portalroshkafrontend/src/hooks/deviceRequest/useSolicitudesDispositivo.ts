import { usePaginatedResource } from "../common/usePaginatedResource";
import { getSolicitudesDispositivo } from "../../services/DeviceRequestService";
import type { SolicitudDispositivoItem } from "../../types";

export function useSolicitudesDispositivo(
  token: string | null,
  filtros: Record<string, string | number | undefined> = {},
  page: number = 0,
  pageSize: number = 10
) {
  return usePaginatedResource<SolicitudDispositivoItem>(
    (params) => getSolicitudesDispositivo(token!, params),
    token,
    filtros,
    page,
    pageSize
  );
}
