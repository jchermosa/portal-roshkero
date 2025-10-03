import type { SolicitudItem} from "../types";
import type { SolicitudPayload } from "../types";

export interface PaginatedResponse<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
}

export async function getSolicitudesPermisoBeneficio(
  token: string
): Promise<SolicitudItem[]> {
  const res = await fetch(`http://localhost:8080/api/v1/usuarios/solicitudes`, {
    headers: { Authorization: `Bearer ${token}` },
  });

  if (!res.ok) throw new Error(await res.text());
  return res.json();
}



export async function getSolicitudesVacaciones(
  token: string,
  params: Record<string, string | number | undefined> = {}
): Promise<PaginatedResponse<SolicitudItem>> {
  const query = new URLSearchParams();
  
  // IMPORTANTE: Agregar el filtro de tipo VACACIONES
  query.append("tipoSolicitud", "VACACIONES");
  
  Object.entries(params).forEach(([k, v]) => {
    if (v !== undefined && v !== "") query.append(k, String(v));
  });

  const res = await fetch(`http://localhost:8080/api/v1/usuarios/solicitudes?${query.toString()}`, {
    headers: { Authorization: `Bearer ${token}` },
  });

  if (!res.ok) throw new Error(await res.text());
  return res.json();
}



async function getSolicitudByIdApi(
  token: string,
  id: string
): Promise<SolicitudItem> {
  const res = await fetch(`http://localhost:8080/api/v1/usuarios/solicitud/${id}`, {
    headers: { Authorization: `Bearer ${token}` },
  });

  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

export async function createSolicitudPermiso(
  token: string,
  solicitud: SolicitudPayload
): Promise<SolicitudItem> {
  const res = await fetch(`http://localhost:8080/api/v1/usuarios/crearpermiso`, {
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

export async function createSolicitudBeneficio(
  token: string,
  solicitud: SolicitudPayload
): Promise<SolicitudItem> {
  const res = await fetch(`http://localhost:8080/api/v1/usuarios/crearbeneficio`, {
    method: "POST",
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify(solicitud),
  });

  const data = await res.json();

  if (!res.ok) {
    throw new Error(data.message || data.error || "Error al crear beneficio");
  }

  if (data.success === false || data.error) {
    throw new Error(data.message || data.error || "No se pudo crear la solicitud");
  }

  return data as SolicitudItem;
}

export async function createSolicitudVacaciones(
  token: string,
  solicitud: SolicitudPayload
): Promise<SolicitudItem> {
  const res = await fetch(`http://localhost:8080/api/v1/usuarios/crearvacacion`, {
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

export async function updateSolicitud(
  token: string,
  id: string,
  solicitud: Partial<SolicitudItem>
): Promise<SolicitudItem> {
  const res = await fetch(`http://localhost:8080/api/solicitudes/${id}`, {
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

export const getSolicitudesUsuario = getSolicitudesPermisoBeneficio;

export const getSolicitudById = getSolicitudByIdApi;


