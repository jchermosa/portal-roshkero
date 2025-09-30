import type { SolicitudItem} from "../types";



export interface PaginatedResponse<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
}

// ================================
// API Methods
// ================================
async function getSolicitudesUsuarioApi(
  token: string,
  usuarioId: number,
  params: Record<string, string | number | undefined> = {}
): Promise<PaginatedResponse<SolicitudItem>> {
  const query = new URLSearchParams();
  query.append("usuarioId", usuarioId.toString());
  
  Object.entries(params).forEach(([k, v]) => {
    if (v !== undefined && v !== "") query.append(k, String(v));
  });

  const res = await fetch(`/api/v1/usuarios/solicitudes?${query.toString()}`, {
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

async function createSolicitudApi(
  token: string,
  solicitud: Partial<SolicitudItem>
): Promise<SolicitudItem> {
  const res = await fetch(`/api/solicitudes`, {
    method: "POST",
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify(solicitud),
  });

  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

async function updateSolicitudApi(
  token: string,
  id: string,
  solicitud: Partial<SolicitudItem>
): Promise<SolicitudItem> {
  const res = await fetch(`/api/solicitudes/${id}`, {
    method: "PUT",
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify(solicitud),
  });

  if (!res.ok) throw new Error(await res.text());
  return res.json();
}




// Exports 

export const getSolicitudesUsuario = getSolicitudesUsuarioApi;

export const getSolicitudById = getSolicitudByIdApi;

export const createSolicitud = createSolicitudApi;

export const updateSolicitud = updateSolicitudApi;