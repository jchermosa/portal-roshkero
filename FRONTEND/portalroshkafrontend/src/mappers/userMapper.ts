import type { UsuarioItem, EstadoActivoInactivo, SeniorityEnum, FocoEnum } from "../types";
//  Normaliza un objeto recibido desde el backend (UserResponseDto / UserByIdResponseDto / UserDto) a un UsuarioItem compatible con el frontend.
export function mapUserResponseToUsuarioItem(res: any): UsuarioItem {
  return {
    idUsuario: res.idUsuario,
    nombre: res.nombre,
    apellido: res.apellido,
     nombreApellido: res.nombreApellido ?? `${res.nombre ?? ""} ${res.apellido ?? ""}`.trim(),

    nroCedula: String(res.nroCedula ?? ""),
    correo: res.correo ?? "",

    idRol: res.idRol ?? res.rolId ?? res.roles?.idRol ?? 0,
    nombreRol: res.nombreRol ?? res.rolNombre ?? res.roles?.nombre,
    rolNombre: res.rolNombre ?? res.nombreRol ?? res.roles?.nombre,

    idCargo: res.idCargo ?? res.cargoId ?? res.cargos?.idCargo ?? 0,
    cargoNombre: res.cargoNombre ?? res.cargos?.nombre,

    fechaIngreso: res.fechaIngreso ?? null,
    fechaNacimiento: res.fechaNacimiento ?? null,
    telefono: res.telefono ?? "",

    requiereCambioContrasena: Boolean(res.requiereCambioContrasena),
    estado: (res.estado as EstadoActivoInactivo) ?? "A",

    antiguedad: res.antiguedad,
    diasVacaciones: res.diasVacaciones,
    diasVacacionesRestante: res.diasVacacionesRestante,

    seniority: res.seniority as SeniorityEnum,
    foco: res.foco as FocoEnum,

    disponibilidad: res.disponibilidad ?? 0,
    urlPerfil: res.urlPerfil ?? res.url ?? null,
  };
}
