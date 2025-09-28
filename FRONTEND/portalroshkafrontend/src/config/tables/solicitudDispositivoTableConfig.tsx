import type { ReactNode } from "react";
import type { SolicitudDispositivoItem } from "../../types";
import { EstadoSolicitudLabels } from "../../types";

// Definición genérica de una columna
export interface TableColumn<T> {
  key: string;
  label: string;
  render?: (row: T) => ReactNode;
}

export const solicitudDispositivoColumns: TableColumn<SolicitudDispositivoItem>[] = [
  { key: "idSolicitud", label: "ID" },
  {
    key: "idUsuario",
    label: "Usuario",
    render: (s: SolicitudDispositivoItem) => `${s.idUsuario}`,
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
      const label = EstadoSolicitudLabels[s.estado];
      const color =
        s.estado === "P"
          ? "bg-yellow-100 text-yellow-700"
          : s.estado === "A"
          ? "bg-green-100 text-green-700"
          : s.estado === "R"
          ? "bg-red-100 text-red-700"
          : "bg-blue-100 text-blue-700"; 
      return (
        <span className={`px-2 py-1 text-xs font-medium rounded-full ${color}`}>
          {label}
        </span>
      );
    },
  },
  {
    key: "fechaInicio",
    label: "Fecha Inicio",
    render: (s: SolicitudDispositivoItem) =>
      s.fechaInicio ? new Date(s.fechaInicio).toLocaleDateString() : "-",
  },
  {
    key: "fechaFin",
    label: "Fecha Fin",
    render: (s: SolicitudDispositivoItem) =>
      s.fechaFin ? new Date(s.fechaFin).toLocaleDateString() : "-",
  },
];
