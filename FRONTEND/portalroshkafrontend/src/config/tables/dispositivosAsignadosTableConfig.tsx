import type { DispositivoAsignadoItem } from "../../types";
import type { ReactNode } from "react";
import { EstadoAsignacionLabels } from "../../types";

// Definición genérica de una columna
export interface TableColumn<T> {
  key: string;
  label: string;
  render?: (row: T) => ReactNode;
}

export const dispositivosAsignadosColumns: TableColumn<DispositivoAsignadoItem>[] = [
  { key: "idDispositivoAsignado", label: "ID" },
  { key: "nombreDispositivo", label: "Dispositivo" },
  { key: "idSolicitud", label: "Solicitud" },
  {
    key: "fechaEntrega",
    label: "Fecha de Entrega",
    render: (d: DispositivoAsignadoItem) =>
      d.fechaEntrega ? new Date(d.fechaEntrega).toLocaleDateString() : "-",
  },
  {
    key: "fechaDevolucion",
    label: "Fecha de Devolución",
    render: (d: DispositivoAsignadoItem) =>
      d.fechaDevolucion ? new Date(d.fechaDevolucion).toLocaleDateString() : "-",
  },
  {
    key: "estadoAsignacion",
    label: "Estado",
    render: (d: DispositivoAsignadoItem) => (
      <span
        className={`px-2 py-1 text-xs font-medium rounded-full ${
          d.estadoAsignacion === "U"
            ? "bg-green-100 text-green-700"
            : "bg-red-100 text-red-700"
        }`}
      >
        {EstadoAsignacionLabels[d.estadoAsignacion]}
      </span>
    ),
  },
  { key: "observaciones", label: "Observaciones" },
];
