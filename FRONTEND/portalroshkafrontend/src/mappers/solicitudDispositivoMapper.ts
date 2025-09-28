import type { SolicitudDispositivoItem, SolicitudUserItem } from "../types";
import type { SolicitudDispositivoUI } from "../types";

/**
 * Mapper de SysAdmin → UI
 */
export function mapAdminSolicitudToUI(
  item: SolicitudDispositivoItem
): SolicitudDispositivoUI {
  return {
    idSolicitud: item.idSolicitud,
    usuarioId: item.idUsuario,
    tipoSolicitud: "DISPOSITIVO",
    comentario: item.comentario,
    estado: item.estado,
    fechaInicio: item.fechaInicio,
    fechaFin: item.fechaFin ?? null,
    cantDias: item.cantDias ?? null,
    idTipoDispositivo: item.idTipoDispositivo ?? null,
    fuente: "ADMINISTRADOR",
  };
}

/**
 * Mapper de Usuario → UI
 */
export function mapUserSolicitudToUI(
  item: SolicitudUserItem
): SolicitudDispositivoUI {
  return {
    idSolicitud: item.idSolicitud,
    usuarioId: undefined, 
    tipoSolicitud: "DISPOSITIVO",
    comentario: item.comentario,
    estado: item.estado,
    fechaInicio: item.fechaInicio ?? null,
    fechaFin: item.fechaFin ?? null,
    cantDias: item.cantDias ?? null,
    idTipoDispositivo: item.idTipoDispositivo ?? null,
    fuente: "USUARIO",
  };
}
