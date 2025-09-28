import type { SolicitudDispositivoItem } from "../types";

// Listar todas las solicitudes de dispositivos
async function getSolicitudesDispositivo(
  token: string
): Promise<SolicitudDispositivoItem[]> {
  const res = await fetch(`/api/v1/admin/sysadmin/allRequests`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

// Aceptar solicitud de dispositivo
async function acceptSolicitudDispositivo(
  token: string,
  idSolicitud: number
): Promise<SolicitudDispositivoItem> {
  const res = await fetch(
    `/api/v1/admin/sysadmin/deviceRequest/${idSolicitud}/accept`,
    {
      method: "POST",
      headers: { Authorization: `Bearer ${token}` },
    }
  );
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

// Rechazar solicitud de dispositivo
async function rejectSolicitudDispositivo(
  token: string,
  idSolicitud: number
): Promise<SolicitudDispositivoItem> {
  const res = await fetch(
    `/api/v1/admin/sysadmin/deviceRequest/${idSolicitud}/reject`,
    {
      method: "POST",
      headers: { Authorization: `Bearer ${token}` },
    }
  );
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}


export {
  getSolicitudesDispositivo,
  acceptSolicitudDispositivo,
  rejectSolicitudDispositivo,
};
