import type { SolicitudItem } from "../types";
import mockSolicitudes from "../data/mockSolicitudes.json";

const USE_MOCK = import.meta.env.VITE_USE_MOCK === "true";

export interface PaginatedResponse<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
}

// ================================
// Métodos API reales
// ================================
async function getSolicitudesApi(
  token: string,
  params: Record<string, string | number | undefined> = {}
): Promise<PaginatedResponse<SolicitudItem>> {
  const query = new URLSearchParams();
  Object.entries(params).forEach(([k, v]) => {
    if (v !== undefined && v !== "") query.append(k, String(v));
  });

  const res = await fetch(`/api/solicitudes/todas?${query.toString()}`, {
    headers: { Authorization: `Bearer ${token}` },
  });

  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

async function getSolicitudByIdApi(
  token: string,
  id: string
): Promise<SolicitudItem> {
  const res = await fetch(`/api/solicitudes/${id}`, {
    headers: { Authorization: `Bearer ${token}` },
  });

  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

async function aprobarSolicitudApi(
  token: string,
  id: string,
  comentario?: string
) {
  const res = await fetch(`/api/solicitudes/${id}/aprobar`, {
    method: "POST",
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ comentario }),
  });

  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

async function rechazarSolicitudApi(
  token: string,
  id: string,
  comentario: string
) {
  const res = await fetch(`/api/solicitudes/${id}/rechazar`, {
    method: "POST",
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ comentario }),
  });

  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

async function getEstadisticasSolicitudesApi(
  token: string
): Promise<{
  total: number;
  pendientes: number;
  aprobadas: number;
  rechazadas: number;
}> {
  const res = await fetch(`/api/solicitudes/estadisticas`, {
    headers: { Authorization: `Bearer ${token}` },
  });

  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

// ================================
// Métodos MOCK
// ================================
async function getSolicitudesMock(): Promise<PaginatedResponse<SolicitudItem>> {
  const data = [...(mockSolicitudes as SolicitudItem[])];

  return {
    content: data,
    totalPages: 1,
    totalElements: data.length,
    size: data.length,
    number: 0,
  };
}

async function getSolicitudByIdMock(
  _token: string,
  id: string
): Promise<SolicitudItem> {
  const data = [...(mockSolicitudes as SolicitudItem[])];
  const solicitud = data.find((s) => s.idSolicitud === Number(id));
  if (!solicitud) throw new Error("Solicitud no encontrada");
  return solicitud;
}

async function aprobarSolicitudMock(
  _token: string,
  id: string,
  comentario?: string
) {
  return { success: true, id, comentario };
}

async function rechazarSolicitudMock(
  _token: string,
  id: string,
  comentario: string
) {
  return { success: true, id, comentario };
}

async function getEstadisticasSolicitudesMock(): Promise<{
  total: number;
  pendientes: number;
  aprobadas: number;
  rechazadas: number;
}> {
  const data = [...(mockSolicitudes as SolicitudItem[])];
  const total = data.length;
  const pendientes = data.filter((s) => s.estado === "P").length;
  const aprobadas = data.filter((s) => s.estado === "A").length;
  const rechazadas = data.filter((s) => s.estado === "R").length;

  return { total, pendientes, aprobadas, rechazadas };
}

// ================================
// Export selector entre MOCK y API
// ================================
export const getSolicitudes = USE_MOCK ? getSolicitudesMock : getSolicitudesApi;
export const getSolicitudById = USE_MOCK
  ? getSolicitudByIdMock
  : getSolicitudByIdApi;
export const aprobarSolicitud = USE_MOCK
  ? aprobarSolicitudMock
  : aprobarSolicitudApi;
export const rechazarSolicitud = USE_MOCK
  ? rechazarSolicitudMock
  : rechazarSolicitudApi;
export const getEstadisticasSolicitudes = USE_MOCK
  ? getEstadisticasSolicitudesMock
  : getEstadisticasSolicitudesApi;
