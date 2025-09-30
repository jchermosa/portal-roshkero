import type { UbicacionItem } from "../../types";
import type { ReactNode } from "react";
import { EstadoLabels } from "../../types";

export interface TableColumn<T> {
  key: string;
  label: string;
  render?: (row: T) => ReactNode;
}

export const ubicacionColumns: TableColumn<UbicacionItem>[] = [
  { key: "idUbicacion", label: "ID" },
  { key: "nombre", label: "Nombre" },
  {
    key: "estado",
    label: "Estado",
    render: (u) => {
      const label = EstadoLabels[u.estado];
      const color =
        u.estado === "A"
          ? "bg-green-100 text-green-700"
          : "bg-red-100 text-red-700";
      return (
        <span className={`px-2 py-1 text-xs font-medium rounded-full ${color}`}>
          {label}
        </span>
      );
    },
  },
  {
    key: "dispositivos",
    label: "Dispositivos",
    render: (u) => u.dispositivos?.length ?? 0,
  },
];
