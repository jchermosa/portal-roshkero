import type { TipoDispositivoItem } from "../../types";
import type { ReactNode } from "react";

export interface TableColumn<T> {
  key: string;
  label: string;
  render?: (row: T) => ReactNode;
}

export const tipoDispositivoColumns: TableColumn<TipoDispositivoItem>[] = [
  { key: "idTipoDispositivo", label: "ID" },
  { key: "nombre", label: "Nombre" },
  {
    key: "detalle",
    label: "Detalle",
    render: (d: TipoDispositivoItem) => d.detalle || "-",
  },
];
