import type { SolicitudItem } from "../types";


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
  params: { tipoSolicitud?: "PERMISO" | "BENEFICIO" | "VACACIONES"; tipoId?: string; estado?: string } = {}
): Promise<PaginatedResponse<SolicitudItem>> {
  const query = new URLSearchParams();
  if (params.tipoId) query.append("tipoId", params.tipoId);
  if (params.estado) query.append("estado", params.estado);

  // Ajustar endpoint según tipoSolicitud
  let endpoint = "/api/v1/admin/th/users/requests/sortby";
  if (params.tipoSolicitud) endpoint += `?type=${params.tipoSolicitud.toLowerCase()}`;

  const res = await fetch(`${endpoint}&${query.toString()}`, {
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



// ================================
// Export selector entre MOCK y API
// ================================
export const getSolicitudes =  getSolicitudesApi;
export const getSolicitudById = getSolicitudByIdApi;
export const aprobarSolicitud =  aprobarSolicitudApi;
export const rechazarSolicitud = rechazarSolicitudApi;

