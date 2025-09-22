import type { UsuarioItem } from "../../types";
import type { ReactNode } from "react";

// Definición genérica de una columna
export interface TableColumn<T> {
  key: string;
  label: string;
  render?: (row: T) => ReactNode;
}

export const usuariosColumns: TableColumn<UsuarioItem>[] = [
  { key: "id", label: "ID" },
  {
    key: "nombre",
    label: "Nombre",
    render: (u: UsuarioItem) => `${u.nombre} ${u.apellido}`,
  },
  { key: "correo", label: "Correo" },
  {
    key: "antiguedadPretty",
    label: "Antigüedad",
    render: (u: UsuarioItem) => u.antiguedadPretty ?? "-",
  },
  {
    key: "estado",
    label: "Estado",
    render: (u: UsuarioItem) => (
      u.estado ? (
        <span className="px-2 py-1 text-xs font-medium bg-green-100 text-green-700 rounded-full">
          Activo
        </span>
      ) : (
        <span className="px-2 py-1 text-xs font-medium bg-red-100 text-red-700 rounded-full">
          Inactivo
        </span>
      )
    )
  },
];
