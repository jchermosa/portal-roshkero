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
  estado: "ACTIVO" | "INACTIVO"; // enum real
  urlPerfil?: string;
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
  tipo_solicitud: "PERMISO" | "BENEFICIO";
  subtipo: { id: number; nombre: string };
  comentario?: string;
  estado: "P" | "A" | "R"; // Pendiente, Aprobada, Rechazada
}

// Solicitud de permisos
export interface SolicitudPermiso extends SolicitudBase {
  lider: { id: number; nombre: string; apellido: string };
  cantidad_dias: number;
  fecha_inicio: string;
  fecha_fin: string;
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
