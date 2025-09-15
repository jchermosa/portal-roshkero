export interface CatalogItem {
  id: number;
  nombre: string;
}

export interface UsuarioItem {
  id: number;
  nombre: string;
  apellido: string;
  correo: string;
  rolId?: number;
  equipoId?: number;
  cargoId?: number;
  estado?: boolean;
  antiguedad?: string;
  antiguedadPretty?: string;
}

export type SolicitudEstado = "P" | "A" | "R";

export interface SolicitudItem {
  id_solicitud: number;
  tipo: CatalogItem;
  comentario: string;
  estado: SolicitudEstado;
}
