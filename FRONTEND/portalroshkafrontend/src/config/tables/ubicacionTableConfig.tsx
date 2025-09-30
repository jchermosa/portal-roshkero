import type { UbicacionItem } from "../../types";
import type { ReactNode } from "react";

// Definición genérica de una columna
export interface TableColumn<T> {
  key: string;
  label: string;
  render?: (row: T) => ReactNode;
}

export const ubicacionColumns: TableColumn<UbicacionItem>[] = [
  { key: "id_ubicacion", label: "ID" },
  { key: "nombre", label: "Nombre" },
  {
    key: "estado",
    label: "Estado",
    render: (u: UbicacionItem) =>
      u.estado === "ACTIVO" ? (
        <span className="px-2 py-1 text-xs font-medium bg-green-100 text-green-700 rounded-full">
          Activo
        </span>
      ) : (
        <span className="px-2 py-1 text-xs font-medium bg-red-100 text-red-700 rounded-full">
          Inactivo
        </span>
      ),
  },
];
