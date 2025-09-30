import type { TipoDispositivoItem } from "../../types";
import type { ReactNode } from "react";

// Definición genérica de una columna
export interface TableColumn<T> {
  key: string;
  label: string;
  render?: (row: T) => ReactNode;
}

export const tipoDispositivoColumns: TableColumn<TipoDispositivoItem>[] = [
  { key: "id_tipo_dispositivo", label: "ID" },
  { key: "nombre", label: "Nombre" },
  {
    key: "detalle",
    label: "Detalle",
    render: (d: TipoDispositivoItem) => d.detalle ?? "-",
  },
  {
    key: "fecha_creacion",
    label: "Creado",
    render: (d: TipoDispositivoItem) =>
      new Date(d.fecha_creacion).toLocaleDateString("es-ES"),
  },
];
