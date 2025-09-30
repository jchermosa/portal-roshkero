export interface CatalogItem {
  id: number;
  nombre: string;
}


export const Estado = {
  ACTIVO: "ACTIVO",
  INACTIVO: "INACTIVO",
} as const;
export type Estado = typeof Estado[keyof typeof Estado];

export const Seniority = {
  JUNIOR: "JUNIOR",
  SEMI_SENIOR: "SEMI_SENIOR",
  SENIOR: "SENIOR",
} as const;
export type Seniority = typeof Seniority[keyof typeof Seniority];

export const Foco = {
  BACKEND: "BACKEND",
  FRONTEND: "FRONTEND",
  FULLSTACK: "FULLSTACK",
  OTRO: "OTRO",
} as const;
export type Foco = typeof Foco[keyof typeof Foco];

export interface UsuarioItem {
  id_usuario: number; 
  nombre: string;
  apellido: string;
  nro_cedula: string; 
  correo: string;
  id_rol: number;
  fecha_ingreso: string | null; 
  antiguedad?: string | null; 
  dias_vacaciones?: number | null;
  contrasena?: string;
  telefono?: string;
  id_cargo: number;
  fecha_nacimiento?: string | null; 
  dias_vacaciones_restante?: number | null;
  requiere_cambio_contrasena: boolean;
  fecha_creacion?: string; 
  seniority?: Seniority
  foco?: Foco
  estado: Estado;
  url_perfil?: string;
  disponibilidad?: number;
  antiguedadPretty?: string;
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

export interface EquipoItem {
  id: number;
  nombre: string;
  fechaInicio?: string | null;
  fechaLimite?: string | null;
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