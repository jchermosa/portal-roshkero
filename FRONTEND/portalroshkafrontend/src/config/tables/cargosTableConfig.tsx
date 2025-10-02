import type { ReactNode } from "react";
import type { CargoListItem } from "../../types";

export interface TableColumn<T> {
  key: string;
  label: string;
  render?: (row: T) => ReactNode;
}

export const cargosColumns: TableColumn<CargoListItem>[] = [
  { key: "idCargo", label: "ID" },
  { key: "nombre", label: "Nombre" },
];
