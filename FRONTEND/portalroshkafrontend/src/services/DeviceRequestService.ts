import type {
  SolicitudDispositivoItem,
  UserSolDispositivoDto,
  PageResponse,
  SolicitudUserItem,
} from "../types";

/**
 * Listar solicitudes de dispositivos (SysAdmin, paginado).
 */
async function getSolicitudesDispositivoAdmin(
  token: string,
  page: number = 0,
  size: number = 10
): Promise<PageResponse<SolicitudDispositivoItem>> {
  const url = `/api/v1/admin/sysadmin/allRequests?page=${page}&size=${size}`;

  const res = await fetch(url, {
    headers: { Authorization: `Bearer ${token}` },
  });

  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

/**
 * Listar solicitudes del usuario autenticado (no paginado).
 */
async function getSolicitudesDispositivoUsuario(
  token: string
): Promise<SolicitudUserItem[]> {
  const url = `/api/v1/usuarios/solicitudes`;

  const res = await fetch(url, {
    headers: { Authorization: `Bearer ${token}` },
  });

  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

/**
 * Aceptar solicitud de dispositivo (solo SysAdmin).
 */
async function acceptSolicitudDispositivo(
  token: string,
  idSolicitud: number
): Promise<SolicitudDispositivoItem> {
  const url = `/api/v1/admin/sysadmin/deviceRequest/${idSolicitud}/accept`;

  const res = await fetch(url, {
    method: "POST",
    headers: { Authorization: `Bearer ${token}` },
  });

  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

/**
 * Rechazar solicitud de dispositivo (solo SysAdmin).
 */
async function rejectSolicitudDispositivo(
  token: string,
  idSolicitud: number
): Promise<SolicitudDispositivoItem> {
  const url = `/api/v1/admin/sysadmin/deviceRequest/${idSolicitud}/reject`;

  const res = await fetch(url, {
    method: "POST",
    headers: { Authorization: `Bearer ${token}` },
  });

  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

/**
 * Crear nueva solicitud de dispositivo (usuario autenticado).
 */
async function createSolicitudDispositivo(
  token: string,
  data: UserSolDispositivoDto
): Promise<SolicitudUserItem> {
  const url = `/api/v1/usuarios/pedir_dispositivo`;
  console.log("[Service] POST /api/v1/usuarios/pedir_dispositivo", { data });
  const res = await fetch(url, {
    method: "POST",
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  });

  if (!res.ok) {
    const txt = await res.text();
    console.error("Error crear solicitud dispositivo:", res.status, txt);
    throw new Error(txt || `HTTP ${res.status}`);
  }
  return res.json();
}

export {
  getSolicitudesDispositivoAdmin,
  getSolicitudesDispositivoUsuario,
  acceptSolicitudDispositivo,
  rejectSolicitudDispositivo,
  createSolicitudDispositivo,
};
