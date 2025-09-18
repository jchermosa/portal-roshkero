// src/services/RequestTHService.ts
import type { SolicitudItem } from "../types";

// Respuesta pageable estándar de Spring
export interface PaginatedResponse<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number; // página actual
}

// ✅ Listado paginado de todas las solicitudes (para TH)
export async function getSolicitudesTH(
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
export async function getSolicitudById(
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
export async function aprobarSolicitud(
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
export async function rechazarSolicitud(
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
export async function getEstadisticasSolicitudes(
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