import type { SolicitudDispositivoItem, SolicitudUserItem } from "../types";
import type { SolicitudDispositivoUI } from "../types";
import type { UserSolDispositivoDto } from "../types";

/**
 * Convierte el form (camelCase, strings) al DTO que espera el backend (snake_case, number).
 */
export function mapFormToUserSolicitudDto(form: Record<string, any>): UserSolDispositivoDto {
  return {
    id_tipo_dispositivo: Number(
      form.idTipoDispositivo ?? form.id_tipo_dispositivo
    ),
    comentario: (form.comentario ?? "").toString().trim(),
  };
}

/**
 * Mapper de SysAdmin → UI
 */
export function mapAdminSolicitudToUI(
  item: SolicitudDispositivoItem
): SolicitudDispositivoUI {
  return {
    idSolicitud: item.idSolicitud,
    usuarioId: item.idUsuario,
    usuarioNombre: item.nombreUsuario ?? undefined, // Ahora coincide
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
