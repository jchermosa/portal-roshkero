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
  params: { tipoSolicitud?: "PERMISO" | "BENEFICIO" | "VACACIONES"; estado?: string } = {}
): Promise<PaginatedResponse<SolicitudItem>> {
  let endpoint = "";

  if (params.tipoSolicitud === "VACACIONES") {
    endpoint = "/api/v1/admin/th/users/requests/vacations";
  } else {
    endpoint = "/api/v1/admin/th/users/requests/sortby";
    if (params.tipoSolicitud) {
      endpoint += `?type=${params.tipoSolicitud.toLowerCase()}`;
    }
  }

  const query = new URLSearchParams();
  if (params.estado) query.append("estado", params.estado);

  const res = await fetch(`${endpoint}${endpoint.includes("?") ? "&" : "?"}${query.toString()}`, {
    headers: { Authorization: `Bearer ${token}` },
  });

  if (!res.ok) throw new Error(await res.text());
  return res.json();
}


async function getSolicitudByIdApi(
  token: string,
  id: string
): Promise<SolicitudItem> {
  const res = await fetch(`/api/v1/admin/th/users/requests/${id}`, {
    headers: { Authorization: `Bearer ${token}` },
  });

  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

async function aprobarSolicitudApi(
  token: string,
  id: string
) {
  const res = await fetch(`/api/v1/admin/th/users/requests/${id}/accept`, {
    method: "POST",
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify({}), 
  });

  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

async function rechazarSolicitudApi(
  token: string,
  id: string
) {
  const res = await fetch(`/api/v1/admin/th/users/requests/${id}/reject`, {
    method: "POST",
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify({}), 
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

