// src/config/clientes/tableConfig.tsx
import type { ReactNode } from "react";
import type { ClienteResponse } from "../../types";

export interface TableColumn<T> {
  key: string;
  label: string;
  render?: (row: T) => ReactNode;
}

export const clientesColumns: TableColumn<ClienteResponse>[] = [
  { key: "idCliente", label: "ID" },
  { key: "nombre", label: "Nombre" },
  { key: "correo", label: "Correo" },
  { key: "nroTelefono", label: "Teléfono" },
  { key: "ruc", label: "RUC" },
];
