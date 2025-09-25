import type { ReactNode } from "react";
import type { SolicitudDispositivoItem } from "../../types";

// Definición genérica de una columna
export interface TableColumn<T> {
  key: string;
  label: string;
  render?: (row: T) => ReactNode;
}

export const solicitudDispositivoColumns: TableColumn<SolicitudDispositivoItem>[] = [
  { key: "id_solicitud", label: "ID" },
  {
    key: "id_usuario",
    label: "Usuario",
    render: (s: SolicitudDispositivoItem) => `${s.id_usuario}`,
  },
  {
    key: "comentario",
    label: "Comentario",
    render: (s: SolicitudDispositivoItem) => s.comentario || "-",
  },
  {
    key: "estado",
    label: "Estado",
    render: (s: SolicitudDispositivoItem) => {
      switch (s.estado) {
        case "Pendiente":
          return (
            <span className="px-2 py-1 text-xs font-medium bg-yellow-100 text-yellow-700 rounded-full">
              Pendiente
            </span>
          );
        case "Aprobada":
          return (
            <span className="px-2 py-1 text-xs font-medium bg-green-100 text-green-700 rounded-full">
              Aprobada
            </span>
          );
        case "Rechazada":
          return (
            <span className="px-2 py-1 text-xs font-medium bg-red-100 text-red-700 rounded-full">
              Rechazada
            </span>
          );
        default:
          return s.estado;
      }
    },
  },
  {
    key: "fecha_inicio",
    label: "Fecha Inicio",
    render: (s: SolicitudDispositivoItem) =>
      s.fecha_inicio ? new Date(s.fecha_inicio).toLocaleDateString() : "-",
  },
  {
    key: "fecha_fin",
    label: "Fecha Fin",
    render: (s: SolicitudDispositivoItem) =>
      s.fecha_fin ? new Date(s.fecha_fin).toLocaleDateString() : "-",
  },
];
