import { FocoLabels, SeniorityLabels, type UsuarioItem } from "../../types";
import type { ReactNode } from "react";

// Definición genérica de una columna
export interface TableColumn<T> {
  key: string;
  label: string;
  render?: (row: T) => ReactNode;
}

export const usuariosColumns: TableColumn<UsuarioItem>[] = [
  { key: "idUsuario", label: "ID" },
  {
    key: "nombreApellido",
    label: "Nombre",
    render: (u) => u.nombreApellido ?? `${u.nombre} ${u.apellido}`,
  },
  { key: "correo", label: "Correo" },
  {
    key: "rolNombre",
    label: "Rol",
    render: (u) => u.rolNombre ?? u.nombreRol ?? "-",
  },
  {
    key: "cargoNombre",
    label: "Cargo",
    render: (u) => u.cargoNombre ?? "-",
  },
  {
    key: "seniority",
    label: "Seniority",
    render: (u) => (u.seniority ? SeniorityLabels[u.seniority] : "-"),
  },
  {
    key: "foco",
    label: "Foco",
    render: (u) => (u.foco ? FocoLabels[u.foco] : "-"),
  },
  {
    key: "antiguedad",
    label: "Antigüedad",
    render: (u) => u.antiguedad ?? "-",
  },
  {
    key: "estado",
    label: "Estado",
    render: (u) => (
      <span
        className={`px-2 py-1 text-xs font-medium rounded-full ${
          u.estado === "A"
            ? "bg-green-100 text-green-700"
            : "bg-red-100 text-red-700"
        }`}
      >
        {u.estado === "A" ? "Activo" : "Inactivo"}
      </span>
    ),
  },
];
