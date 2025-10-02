export interface PageResponse<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
}

export interface CatalogItem {
  id: number;
  nombre: string;
}

export interface PaginatedResponse<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number; // página actual
}

export const EstadoActivoInactivo = {
  A: "A",
  I: "I",
} as const;
export type EstadoActivoInactivo = keyof typeof EstadoActivoInactivo; // "A" | "I"

// Para la UI
export const EstadoLabels: Record<EstadoActivoInactivo, string> = {
  A: "Activo",
  I: "Inactivo",
};
export const SeniorityEnum = {
  JUNIOR: "JUNIOR",
  BOOTCAMPER: "BOOTCAMPER",
  PLENO: "PLENO",
  SENIOR: "SENIOR",
} as const;
export type SeniorityEnum = typeof SeniorityEnum[keyof typeof SeniorityEnum];

export const SeniorityLabels: Record<SeniorityEnum, string> = {
  JUNIOR: "Junior",
  BOOTCAMPER: "Bootcamper",
  PLENO: "Pleno",
  SENIOR: "Senior",
};

export const FocoEnum = {
  FABRICA: "FABRICA",
  GAMES: "GAMES",
  MANTENIMIENTO: "MANTENIMIENTO",
  NINGUNO: "NINGUNO",
  PRODUCTO: "PRODUCTO",
  PROYECTO: "PROYECTO",
  STAFF: "STAFF",
  TERCERIZACION: "TERCERIZACION",
} as const;
export type FocoEnum = typeof FocoEnum[keyof typeof FocoEnum];

export const FocoLabels: Record<FocoEnum, string> = {
  FABRICA: "Fábrica",
  GAMES: "Games",
  MANTENIMIENTO: "Mantenimiento",
  NINGUNO: "Ninguno",
  PRODUCTO: "Producto",
  PROYECTO: "Proyecto",
  STAFF: "Staff",
  TERCERIZACION: "Tercerización",
};


export interface UsuarioItem {
  idUsuario: number;
  nombre: string;
  apellido: string;
  nombreApellido?: string; 
  nroCedula: string;
  correo: string;
  idRol: number;
  nombreRol?: string;
  rolNombre?: string;
  idCargo: number;
  cargoNombre?: string;
  fechaIngreso: string | null;
  fechaNacimiento?: string | null;
  telefono?: string;
  requiereCambioContrasena?: boolean;
  estado: EstadoActivoInactivo; 
  antiguedad?: string;
  diasVacaciones?: number;
  diasVacacionesRestante?: number;
  seniority?: SeniorityEnum;
  foco?: FocoEnum;
  disponibilidad?: number;
  urlPerfil?: string;
}


export type SortBy = "active" | "inactive" | "rol" | "cargo";

export interface FiltrosUsuarios {
  sortBy?: SortBy;
  idRol?: number;                
  idCargo?: number;            
  estado?: EstadoActivoInactivo; 
}

export interface RolItem {
  idRol: number;          
  nombre: string;
  fechaCreacion: string;
}

export interface CargoItem {
  idCargo: number;
  nombre: string;
  fechaCreacion: string;
}

export interface EquipoItem {
  equipoId: number;
  nombre: string;
  idLider: number;
  idCliente: number;
  fechaInicio: string;
  fechaLimite: string;
  fechaCreacion: string;
  estado: string;
}

export const EstadoAsignacionEnum = {
  U: "U",
  D: "D", 
} as const;

export type EstadoAsignacionEnum =
  typeof EstadoAsignacionEnum[keyof typeof EstadoAsignacionEnum];

export const EstadoAsignacionLabels: Record<EstadoAsignacionEnum, string> = {
  U: "En uso",
  D: "Devuelto",
};


export type SolicitudEstado = "P" | "A" | "R";

export interface LiderItem {
  id: number;
  nombre: string;
  aprobado: boolean;
}


export interface SolicitudBase {
  id: number;
  id_usuario: number;
  nombre: string;
  apellido: string;
  id_solicitud_tipo: number;
  tipo: { id: number; nombre: string };
  comentario?: string;
  estado: "P" | "A" | "R"; 
}

// Solicitud de permisos
export interface SolicitudPermiso extends SolicitudBase {
  cantidad_dias: number;
  fecha_inicio: string;
  fecha_fin: string;
  numero_aprobaciones: number;
}

// Solicitud de beneficios (solo tiene lo de base, sin extras)
export interface SolicitudBeneficio extends SolicitudBase {}

// Unión
export type SolicitudItem = SolicitudPermiso | SolicitudBeneficio;



export interface TipoPermisoItem {
  id: number;
  nombre: string;
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
}

export interface DispositivoAsignadoItem {
  idDispositivoAsignado: number;
  idDispositivo: number;
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
  idCliente: number;
  nombre: string;
  nroTelefono: string;
  correo: string;
  ruc: string;
  fechaCreacion: string; 
};

export type ClientesPageResponse = {
  content: ClienteResponse[];
  currentPage: number;
  totalItems: number;
  totalPages: number;
};

export interface PageSpring<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  number: number; 
  size: number;
  first?: boolean;
  last?: boolean;
  numberOfElements?: number;
  empty?: boolean;
}

// Cargos
export interface CargoListItem {
  idCargo: number;
  nombre: string;
}

/** Detalle de cargo (GET /th/cargos/{idCargo}) */
export interface CargoDetail {
  idCargo: number;
  nombre: string;
  empleadosAsignados: UsuarioSimple[]; 
}

/** Representación mínima del UsuarioSimpleDto del back */
export interface UsuarioSimple {
  idUsuario: number;
  nombre: string;
}

/** Insert/Update DTO (POST/PUT) */
export interface CargoInsert {
  nombre: string;
}

/** Respuesta por defecto */
export interface CargoActionResponse {
  idCargo: number;
  message: string;
}


//Roles
export interface RolListItem {
  idRol: number;
  nombre: string;
}

/** Detalle (GET /th/roles/{idRol}) */
export interface RolDetail {
  idRol: number;
  nombre: string;
  empleadosAsignados: UsuarioSimple[];
}


export interface UsuarioSimple {
  idUsuario: number;
  nombre: string;
}

/** Insert/Update DTO (POST/PUT) */
export interface RolInsert {
  nombre: string;
}

/** Respuesta del back en create/update/delete */
export interface RolActionResponse {
  idRol: number;
  message: string;
}


// Tecnologias
// Request DTO (crear/editar)
export interface TecnologiaRequest {
  nombre: string;
  descripcion: string;
}

// Response DTO (uno solo)
export interface TecnologiaResponse {
  idTecnologia: number;
  nombre: string;
  descripcion: string;
  fechaCreacion: string; // ISO (LocalDateTime)
}

// Respuesta paginada
export interface TecnologiasPageResponse {
  content: TecnologiaResponse[];
  currentPage: number;
  totalItems: number;
  totalPages: number;
}