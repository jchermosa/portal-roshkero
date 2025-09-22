import type { DispositivoAsignadoItem } from "../../types";
import type { ReactNode } from "react";

// Definición genérica de una columna
export interface TableColumn<T> {
  key: string;
  label: string;
  render?: (row: T) => ReactNode;
}

export const dispositivosAsignadosColumns: TableColumn<DispositivoAsignadoItem>[] = [
  { key: "id_dispositivo_asignado", label: "ID" },
  { key: "id_dispositivo", label: "Dispositivo" },
  { key: "id_solicitud", label: "Solicitud" },
  {
    key: "fecha_entrega",
    label: "Fecha de Entrega",
    render: (d: DispositivoAsignadoItem) =>
      d.fecha_entrega ? new Date(d.fecha_entrega).toLocaleDateString() : "-",
  },
  {
    key: "fecha_devolucion",
    label: "Fecha de Devolución",
    render: (d: DispositivoAsignadoItem) =>
      d.fecha_devolucion ? new Date(d.fecha_devolucion).toLocaleDateString() : "-",
  },
  {
    key: "estado_asignacion",
    label: "Estado",
    render: (d: DispositivoAsignadoItem) =>
      d.estado_asignacion === "Activo" ? (
        <span className="px-2 py-1 text-xs font-medium bg-green-100 text-green-700 rounded-full">
          Activo
        </span>
      ) : (
        <span className="px-2 py-1 text-xs font-medium bg-yellow-100 text-yellow-700 rounded-full">
          {d.estado_asignacion}
        </span>
      ),
  },
  { key: "encargado", label: "Encargado" },
  { key: "observaciones", label: "Observaciones" },
];
