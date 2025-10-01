import type { ReactNode } from "react";
import type { TecnologiaResponse } from "../../types";

export interface TableColumn<T> {
  key: string;
  label: string;
  render?: (row: T) => ReactNode;
}

export const tecnologiasColumns: TableColumn<TecnologiaResponse>[] = [
  { key: "idTecnologia", label: "ID" },
  { key: "nombre", label: "Nombre" },
  { key: "descripcion", label: "Descripción" },
  {
    key: "fechaCreacion",
    label: "Fecha de creación",
    render: (row) =>
      new Date(row.fechaCreacion).toLocaleDateString("es-PY", {
        day: "2-digit",
        month: "2-digit",
        year: "numeric",
      }),
  },
];
