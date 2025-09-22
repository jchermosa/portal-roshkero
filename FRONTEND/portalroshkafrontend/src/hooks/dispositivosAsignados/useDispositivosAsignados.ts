// src/hooks/dispositivosAsignados/useDispositivosAsignados.ts
import { usePaginatedResource } from "../common/usePaginatedResource";
import { getDispositivosAsignados } from "../../services/DeviceAssignmentService";
import type { DispositivoAsignadoItem } from "../../types";

export function useDispositivosAsignados(
  token: string | null,
  filtros: Record<string, string | number | undefined> = {},
  page: number = 0,
  pageSize: number = 10
) {
  return usePaginatedResource<DispositivoAsignadoItem>(
    (params) => getDispositivosAsignados(token!, params),
    token,
    filtros,
    page,
    pageSize
  );
}
