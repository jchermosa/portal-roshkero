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
  id: number;
  id_usuario: number;
  nombre: string;
  apellido:string;
  id_solicitud_tipo: number;
  tipo: { id: number; nombre: string };
  cantidad_dias: number | null;
  fecha_inicio: string;
  fecha_fin: string;
  comentario: string;
  estado: "P" | "A" | "R";
  numero_aprobaciones: number;
  lideres: LiderItem[];
}


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
