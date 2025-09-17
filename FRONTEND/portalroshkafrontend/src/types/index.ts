export interface CatalogItem {
  id: number;
  nombre: string;
}

export interface UsuarioItem {
  id: number;
  nombre: string;
  apellido: string;
  correo: string;
  nroCedula: string;
  contrasena?: string;
  telefono?: string;
  fechaIngreso?: string; 
  fechaNacimiento?: string; 
  estado: boolean;
  requiereCambioContrasena: boolean;
  rolId?: number;
  equipoId?: number;
  cargoId?: number;
  antiguedad?: string;
  antiguedadPretty?: string;
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


