export interface CatalogItem {
  id: number;
  nombre: string;
}

export interface UsuarioItem {
  id: number;
  nombre: string;
  apellido: string;
  nroCedula: number;
  correo: string;
  idRol: number;
  fechaIngreso: string | null;
  antiguedad?: string | null;
  diasVacaciones?: number | null;
  contrasena?: string;
  telefono?: string;
  idCargo: number;
  fechaNacimiento?: string | null;
  diasVacacionesRestante?: number | null;
  requiereCambioContrasena: boolean;
  fechaCreacion?: string; // timestamp
  estado: "ACTIVO" | "INACTIVO" | "VACACIONES"; // enum real
  urlPerfil?: string;
  disponibilidad?: number;
}

export type SolicitudEstado = "P" | "A" | "R";

export interface LiderItem {
  id: number;
  nombre: string;
  aprobado: boolean;
}

export interface SolicitudItem {
  id_solicitud: number;
  tipo: CatalogItem;
  comentario: string;
  estado: SolicitudEstado;
}


export interface EquipoItem {
  id: number;
  nombre: string;
<<<<<<< HEAD
  fechaInicio?: string | null;
  fechaLimite?: string | null;
=======
  cantidadDias: number | null; 
}

export interface TipoBeneficioItem {
  id: number;
  nombre: string;
  descripcion: string;
  estado: "A" | "I";
  requiereAprobacionLider: boolean;
}


export interface DispositivoItem {
  idDispositivo?: number; 
  tipoDispositivo: number;
  ubicacion: number;
  nroSerie: string;
  modelo: string;
  detalle: string;
  fechaFabricacion: string; 
  estado: EstadoInventarioEnum;
  categoria: CategoriaEnum;
  encargado: number; 
  nombreEncargado?: string;
}

export interface DispositivoAsignadoItem {
  idDispositivoAsignado: number;
  idDispositivo: number;
  nombreDispositivo?: string;
  idSolicitud: number;
  fechaEntrega: string;
  fechaDevolucion?: string | null;
  estadoAsignacion: EstadoAsignacionEnum;
  observaciones?: string | null;
}

export interface SolicitudDispositivoItem {
  idSolicitud: number;
  idUsuario: number;
  nombreUsuario?: string;
  idDocumentoAdjunto?: number | null;
  idLider?: number | null;
  tipoSolicitud: "DISPOSITIVO";
  comentario?: string;
  estado: EstadoSolicitudEnum;
  fechaInicio: string;
  cantDias?: number | null;
  fechaFin?: string | null;
  idTipoDispositivo?: number | null;
}

export interface UserSolDispositivoDto {
  id_tipo_dispositivo: number; 
  comentario?: string;         
}

export interface TipoDispositivoItem {
  idTipoDispositivo: number;
  nombre: string;
  detalle: string;
}

export interface UbicacionItem {
  idUbicacion: number;
  nombre: string;
  dispositivos?: DispositivoItem[];
  estado: EstadoActivoInactivo;
}

export const EstadoInventarioEnum = {
  D: "D",   
  A: "A",   
  ND: "ND", 
  R: "R",   
} as const;

export type EstadoInventarioEnum =
  typeof EstadoInventarioEnum[keyof typeof EstadoInventarioEnum];

export const EstadoInventarioLabels: Record<EstadoInventarioEnum, string> = {
  D: "Disponible",
  A: "Asignado",
  ND: "No disponible",
  R: "En reparación",
};

export const EstadoSolicitudEnum = {
  A: "A",   
  R: "R",   
  P: "P",   
  RC: "RC",
} as const;

export type EstadoSolicitudEnum =
  typeof EstadoSolicitudEnum[keyof typeof EstadoSolicitudEnum];

export const EstadoSolicitudLabels: Record<EstadoSolicitudEnum, string> = {
  A: "Aprobado",
  R: "Rechazado",
  P: "Pendiente",
  RC: "Recalendarizado",
};

export const SolicitudesEnum = {
  PERMISO: "Permiso",
  BENEFICIO: "Beneficio",
  DISPOSITIVO: "Dispositivo",
  VACACIONES: "Vacaciones",
} as const;

export type SolicitudesEnum =
  typeof SolicitudesEnum[keyof typeof SolicitudesEnum];

export const SolicitudesLabels: Record<SolicitudesEnum, string> = {
  Permiso: "Permiso",
  Beneficio: "Beneficio",
  Dispositivo: "Dispositivo",
  Vacaciones: "Vacaciones",
};


export const CategoriaEnum = {
  CONCEDIDO: "CONCEDIDO",
  OFICINA: "OFICINA",
} as const;

export type CategoriaEnum =
  typeof CategoriaEnum[keyof typeof CategoriaEnum];

export const CategoriaLabels: Record<CategoriaEnum, string> = {
  CONCEDIDO: "Concedido",
  OFICINA: "Oficina",
};

export interface SolicitudUserItem {
  idSolicitud: number;
  tipoSolicitud: "PERMISO" | "VACACIONES" | "BENEFICIO" | "DISPOSITIVO";
  estado: EstadoSolicitudEnum;
  comentario?: string;
  fechaInicio?: string | null;
  fechaFin?: string | null;
  cantDias?: number | null;
  idTipoDispositivo?: number | null;
}

export interface SolicitudDispositivoUI {
  idSolicitud: number;
  usuarioId?: number;        
  usuarioNombre?: string;
  tipoSolicitud: "DISPOSITIVO";
  comentario?: string;
  estado: EstadoSolicitudEnum;
  fechaInicio?: string | null;
  fechaFin?: string | null;
  cantDias?: number | null;
  idTipoDispositivo?: number | null;
  fuente: "ADMINISTRADOR" | "USUARIO"; 
}

/** 
* Clientes
*/
export type ClienteRequest = {
  nombre: string;
  nroTelefono: string;
  correo: string;
  ruc: string;
};

export type ClienteResponse = {
>>>>>>> origin/merge-estable
  idCliente: number;
  fechaCreacion?: string;
  estado: "ACTIVO" | "INACTIVO"; // enum real
}

export interface RolItem {
  id: number;
  nombre: string;
  fechaCreacion?: string;
}

export interface CargoItem {
  id: number;
  nombre: string;
  fechaCreacion?: string;
}
