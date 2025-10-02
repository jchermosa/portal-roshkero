import type { ReactNode } from "react";
import type { DispositivoItem } from "../../types";
import { EstadoInventarioLabels, CategoriaLabels } from "../../types";

export interface TableColumn<T> {
  key: string;
  label: string;
  render?: (row: T) => ReactNode;
}

export const dispositivosColumns: TableColumn<DispositivoItem>[] = [
  { key: "idDispositivo", label: "ID" },
  { key: "modelo", label: "Modelo" },
  { key: "nroSerie", label: "Nro. Serie" },
  {
    key: "categoria",
    label: "Categoría",
    render: (d: DispositivoItem) => CategoriaLabels[d.categoria],
  },
  {
    key: "encargado",
    label: "Encargado",
    render: (d: DispositivoItem) => d.nombreEncargado ?? "SYSADMIN",
  },
  {
    key: "estado",
    label: "Estado",
    render: (d: DispositivoItem) => {
      const label = EstadoInventarioLabels[d.estado];
      const color =
        d.estado === "D"
          ? "bg-green-100 text-green-700"
          : d.estado === "A"
          ? "bg-blue-100 text-blue-700"
          : d.estado === "R"
          ? "bg-yellow-100 text-yellow-700"
          : "bg-red-100 text-red-700";
      return (
        <span className={`px-2 py-1 text-xs font-medium rounded-full ${color}`}>
          {label}
        </span>
      );
    },
  },
  {
    key: "fechaFabricacion",
    label: "Fabricación",
    render: (d: DispositivoItem) =>
      d.fechaFabricacion
        ? new Date(d.fechaFabricacion).toLocaleDateString()
        : "-",
  },
];
