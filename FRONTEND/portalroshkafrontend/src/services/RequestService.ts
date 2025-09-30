import type { SolicitudItem} from "../types";
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

  const res = await fetch(`/api/solicitudes/usuario?${query.toString()}`, {
    headers: { Authorization: `Bearer ${token}` },
  });

  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

async function getSolicitudesPermisoUsuarioApi(
  token: string,
  usuarioId: number,
  params: Record<string, string | number | undefined> = {}
): Promise<PaginatedResponse<SolicitudItem>> {
  const query = new URLSearchParams();
  query.append("usuarioId", usuarioId.toString());
  query.append("tipo", "PERMISO");
  
  Object.entries(params).forEach(([k, v]) => {
    if (v !== undefined && v !== "") query.append(k, String(v));
  });

  const res = await fetch(`/api/solicitudes/usuario?${query.toString()}`, {
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



// ================================
// Mock Methods
// ================================
async function getSolicitudesUsuarioMock(
  _token: string,
  usuarioId: number,
  params: Record<string, string | number | undefined> = {}
): Promise<PaginatedResponse<SolicitudItem>> {
  let filteredData = (mockSolicitudes as SolicitudItem[]).filter(
    s => s.id_usuario === usuarioId
  );

  if (params.tipo_solicitud) {
    filteredData = filteredData.filter(
      s => s.tipo_solicitud === params.tipo_solicitud
    );
  }

  if (params.id_subtipo) {
    filteredData = filteredData.filter(
      s => s.id_subtipo === Number(params.id_subtipo)
    );
  }

  const page = Number(params.page) || 0;
  const size = Number(params.size) || 10;
  const start = page * size;
  const end = start + size;

  return {
    content: filteredData.slice(start, end),
    totalPages: Math.ceil(filteredData.length / size),
    totalElements: filteredData.length,
    size,
    number: page,
  };
}

async function getSolicitudesPermisoUsuarioMock(
  token: string,
  usuarioId: number,
  params: Record<string, string | number | undefined> = {}
): Promise<PaginatedResponse<SolicitudItem>> {
  return getSolicitudesUsuarioMock(token, usuarioId, {
    ...params,
    tipo_solicitud: "PERMISO",
  });
}

async function getSolicitudByIdMock(
  _token: string,
  id: string
): Promise<SolicitudItem> {
  const solicitud = (mockSolicitudes as SolicitudItem[]).find(
    s => s.id === Number(id)
  );
  if (!solicitud) throw new Error("Solicitud no encontrada");
  return solicitud;
}


async function createSolicitudMock(
  _token: string,
  solicitud: Partial<SolicitudItem>
): Promise<SolicitudItem> {
  return {
    id: Date.now(),
    id_usuario: solicitud.id_usuario || 1,
    nombre: solicitud.nombre || "",
    apellido: solicitud.apellido || "",
    tipo_solicitud: solicitud.tipo_solicitud || "PERMISO",
    id_subtipo: solicitud.id_subtipo || 0,
    comentario: solicitud.comentario || "",
    cant_dias: solicitud.cant_dias || 0,
    fecha_inicio: solicitud.fecha_inicio || "",
    fecha_fin: solicitud.fecha_fin || "",
    estado: "P",
  };
}

async function updateSolicitudMock(
  _token: string,
  id: string,
  solicitud: Partial<SolicitudItem>
): Promise<SolicitudItem> {
  return { ...solicitud, id: Number(id) } as SolicitudItem;
}


// ================================
// Exports condicionales
// ================================
export const getSolicitudesUsuario = USE_MOCK
  ? getSolicitudesUsuarioMock
  : getSolicitudesUsuarioApi;

export const getSolicitudesPermisoUsuario = USE_MOCK
  ? getSolicitudesPermisoUsuarioMock
  : getSolicitudesPermisoUsuarioApi;

export const getSolicitudById = USE_MOCK
  ? getSolicitudByIdMock
  : getSolicitudByIdApi;

export const createSolicitud = USE_MOCK
  ? createSolicitudMock
  : createSolicitudApi;

export const updateSolicitud = USE_MOCK
  ? updateSolicitudMock
  : updateSolicitudApi;
