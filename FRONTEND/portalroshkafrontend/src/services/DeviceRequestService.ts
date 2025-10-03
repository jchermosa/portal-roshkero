import type {
  SolicitudDispositivoItem,
  UserSolDispositivoDto,
  PageResponse,
  SolicitudUserItem,
} from "../types";
import { throwIfNotOk } from "../utils/http";
/**
 * Listar solicitudes de dispositivos (SysAdmin, paginado).
 */
async function getSolicitudesDispositivoAdmin(
  token: string,
  page: number = 0,
  size: number = 10
): Promise<PageResponse<SolicitudDispositivoItem>> {
  const url = `http://localhost:8080/api/v1/admin/sysadmin/allRequests?page=${page}&size=${size}`;

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
  const url = `http://localhost:8080/api/v1/usuarios/solicitudes`;

  const res = await fetch(url, {
    headers: { Authorization: `Bearer ${token}` },
  });

  await throwIfNotOk(res);
  return res.json();
}

/**
 * Aceptar solicitud de dispositivo (solo SysAdmin).
 */
async function acceptSolicitudDispositivo(
  token: string,
  idSolicitud: number
): Promise<void> {                         // 👈 ahora void
  const url = `http://localhost:8080/api/v1/admin/sysadmin/deviceRequest/${idSolicitud}/accept`;

  const res = await fetch(url, {
    method: "POST",
    headers: { Authorization: `Bearer ${token}` },
  });

  
    await throwIfNotOk(res); 
  
  
}

/**
 * Rechazar solicitud de dispositivo (solo SysAdmin).
 */
async function rejectSolicitudDispositivo(
  token: string,
  idSolicitud: number
): Promise<void> {
  const url = `http://localhost:8080/api/v1/admin/sysadmin/deviceRequest/${idSolicitud}/reject`;

  const res = await fetch(url, {
    method: "POST",
    headers: { Authorization: `Bearer ${token}` },
  });

  
    await throwIfNotOk(res); 
  
}

/**
 * Crear nueva solicitud de dispositivo (usuario autenticado).
 */
async function createSolicitudDispositivo(
  token: string,
  data: UserSolDispositivoDto
): Promise<SolicitudUserItem> {
  const url = `http://localhost:8080/api/v1/usuarios/pedirdispositivo`;
  const res = await fetch(url, {
    method: "POST",
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  });

  await throwIfNotOk(res);
  return res.json();
}

async function getSolicitudById(
  token: string,
  idSolicitud: number
): Promise<SolicitudUserItem> {
  const url = `http://localhost:8080/api/v1/admin/sysadmin/request/${idSolicitud}`;
  console.log("[Service] GET", url);
  const res = await fetch(url, {
    headers: { Authorization: `Bearer ${token}` },
  });
  await throwIfNotOk(res);
  const json = await res.json();
  console.log("[Service] detalle recibido:", json);
  return json;
}

export {
  getSolicitudesDispositivoAdmin,
  getSolicitudesDispositivoUsuario,
  acceptSolicitudDispositivo,
  rejectSolicitudDispositivo,
  createSolicitudDispositivo,
  getSolicitudById,
};
