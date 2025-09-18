import type { SolicitudItem } from "../types";
import mockPermisos from "../data/mockSolicitudes.json";
import mockBeneficios from "../data/mockBeneficios.json";

const USE_MOCK = import.meta.env.VITE_USE_MOCK === "true";

// ================================
// Respuesta pageable estándar de Spring
// ================================
export interface PaginatedResponse<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number; // página actual
}

// ================================
// Métodos reales (API)
// ================================

// ✅ Listado paginado de todas las solicitudes (para TH)
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

// ✅ Obtener una solicitud por ID
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

// ✅ Aprobar solicitud
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

// ✅ Rechazar solicitud
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

// ✅ Obtener estadísticas de solicitudes
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
  const data = [...(mockPermisos as SolicitudItem[]), ...(mockBeneficios as SolicitudItem[])];

  return {
    content: data,
    totalPages: 1,
    totalElements: data.length,
    size: data.length,
    number: 0,
  };
}

async function getSolicitudByIdMock(_token: string, id: string): Promise<SolicitudItem> {
  const data = [...(mockPermisos as SolicitudItem[]), ...(mockBeneficios as SolicitudItem[])];
  const solicitud = data.find((s) => s.id === Number(id));
  if (!solicitud) throw new Error("Solicitud no encontrada");
  return solicitud;
}

async function aprobarSolicitudMock(_token: string, id: string, comentario?: string) {
  return { success: true, id, comentario };
}

async function rechazarSolicitudMock(_token: string, id: string, comentario: string) {
  return { success: true, id, comentario };
}

async function getEstadisticasSolicitudesMock(): Promise<{
  total: number;
  pendientes: number;
  aprobadas: number;
  rechazadas: number;
}> {
  const data = [...(mockPermisos as SolicitudItem[]), ...(mockBeneficios as SolicitudItem[])];
  return {
    total: data.length,
    pendientes: data.filter((s) => s.estado === "P").length,
    aprobadas: data.filter((s) => s.estado === "A").length,
    rechazadas: data.filter((s) => s.estado === "R").length,
  };
}

// ================================
// Export condicional
// ================================
export const getSolicitudesTH = USE_MOCK ? getSolicitudesMock : getSolicitudesApi;
export const getSolicitudById = USE_MOCK ? getSolicitudByIdMock : getSolicitudByIdApi;
export const aprobarSolicitud = USE_MOCK ? aprobarSolicitudMock : aprobarSolicitudApi;
export const rechazarSolicitud = USE_MOCK ? rechazarSolicitudMock : rechazarSolicitudApi;
export const getEstadisticasSolicitudes = USE_MOCK
  ? getEstadisticasSolicitudesMock
  : getEstadisticasSolicitudesApi;
