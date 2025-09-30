import type { ReactNode } from "react";
import type { RolListItem } from "../../types";

export interface TableColumn<T> {
  key: string;
  label: string;
  render?: (row: T) => ReactNode;
}

export const rolesColumns: TableColumn<RolListItem>[] = [
  { key: "idRol", label: "ID" },
  { key: "nombre", label: "Nombre" },
];
