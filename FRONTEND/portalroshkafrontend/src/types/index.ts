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



export interface SolicitudItem {
  idSolicitud: number;
  usuario: string;   
  tipoSolicitud: "PERMISO" | "BENEFICIO" | "VACACIONES";
  subtipo: string;
  fechaInicio: string;
  cantidadDias: number | null;
  fechaCreacion: string;
  estado: "P" | "A" | "R";
}

export interface EquipoItem {
  id: number;
  nombre: string;
  fechaInicio?: string | null;
  fechaLimite?: string | null;
  idCliente: number;
  fechaCreacion?: string;
  estado: "ACTIVO" | "INACTIVO"; 
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