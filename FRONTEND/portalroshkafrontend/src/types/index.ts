export interface CatalogItem {
  id: number;
  nombre: string;
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
  estado: "A" | "I"; 
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
}


export interface RolItem {
  idCargo: number;          
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
  estado: "P" | "A" | "R"; // Pendiente, Aprobada, Rechazada
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
  cantidadDias: number | null; // null si es ajustable
}

export interface TipoBeneficioItem {
  id: number;
  nombre: string;
  descripcion: string;
  estado: "A" | "I";
  requiereAprobacionLider: boolean;
}


export interface DispositivoItem {
  id_dispositivo: number;
  id_tipo_dispositivo: number;
  id_ubicacion: number;
  nro_serie: string;
  modelo: string;
  detalles: string;
  fecha_fabricacion: string; // YYYY-MM-DD
  estado: string;
  categoria: string;
  encargado: string;
  fecha_creacion: string; // YYYY-MM-DD
}

export interface DispositivoAsignadoItem {
  id_dispositivo_asignado: number;
  id_dispositivo: number;
  id_solicitud: number;
  fecha_entrega: string;
  fecha_devolucion?: string | null;
  estado_asignacion: string;
  observaciones?: string | null;
}

export interface SolicitudDispositivoItem {
  id_solicitud: number;
  id_usuario: number;
  id_documento_adjunto?: number | null;
  tipo_solicitud: "Dispositivo"; // restringido
  comentario?: string;
  estado: string; // enum estado_solicitud_enum
  fecha_inicio: string;
  cant_dias?: number | null;
  fecha_fin?: string | null;
  fecha_creacion: string;
}


export interface TipoDispositivoItem {
  id_tipo_dispositivo: number;
  nombre: string;
  detalle: string;
  fecha_creacion: string;
}

export interface UbicacionItem {
  id_ubicacion: number;
  nombre: string;
  estado: string; // viene de enum estado_ac_enum
}