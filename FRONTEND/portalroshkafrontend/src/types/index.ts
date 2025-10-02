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
  nombreUsuario: string
  tipoSolicitud: "PERMISO" | "BENEFICIO" | "VACACIONES";
  nombreSubTipoSolicitud: string;
  subTipo: string;
  fechaInicio: string;
  cantDias: number | null;
  monto?: number;   
  fechaCreacion: string;
  nombreLider: string;
  estado: "P" | "A" | "R" | "RC";
}

export interface SolicitudFormData {
  idSubtipo?: number;
  fechaInicio: string;
  cantDias?: number;
  comentario: string;
  monto?: number;      
  fechaFin?: string;     
}

export type SolicitudPayload =
  | {
      id_tipo_permiso: number | undefined;
      fecha_inicio: string;
      cant_dias: number | null;
      comentario: string;
    }
  | {
      id_tipo_beneficio: number | undefined;
      fecha_inicio: string;
      cant_dias: number | null;
      comentario: string;
      monto: number | null;
    }
  | {
      fecha_inicio: string;
      fecha_fin: string;
    };

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
  idTipoPermiso: number;
  nombre: string;
  cantDias: number;
  observaciones: string;
  remunerado: boolean;
  fuerzaMenor: boolean;
}

export interface TipoBeneficioItem {
  idTipoBeneficio: number;
  nombre: string;
  descripcion: string;
  montoMaximo: number;
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