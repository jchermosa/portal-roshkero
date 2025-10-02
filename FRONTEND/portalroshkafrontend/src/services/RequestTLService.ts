import type { SolicitudItem} from "../types";

export interface PaginatedResponse<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
}

export async function getSolicitudesTL(
  token: string
): Promise<SolicitudItem[]> {
  const res = await fetch(`/api/v1/teamleader/users/requests/getall`, {
    headers: { Authorization: `Bearer ${token}` },
  });

  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

export async function getSolicitudByIdTL(
  token: string,
  id: string
): Promise<SolicitudItem> {
  const res = await fetch(`/api/v1/teamleader/users/requests/${id}`, {
    headers: { Authorization: `Bearer ${token}` },
  });

  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

export async function aprobarSolicitudTL(
  token: string,
  id: string
) {
  const res = await fetch(`/api/v1/teamleader/users/requests/${id}/accept`, {
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

export async function rechazarSolicitudTL(
  token: string,
  id: string
) {
  const res = await fetch(`/api/v1/teamleader/users/requests/${id}/reject`, {
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