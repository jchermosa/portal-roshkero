import type { ReactNode } from "react";

// Definición genérica de una columna
export interface TableColumn<T> {
  key: string;
  label: string;
  render?: (row: T) => ReactNode;
}

// Definición del tipo de dispositivo
export interface DispositivoItem {
  id_dispositivo: number;
  id_tipo_dispositivo: number;
  id_ubicacion: number;
  nro_serie: string;
  modelo: string;
  detalles?: string;
  fecha_fabricacion: string;
  estado: string;
  categoria: string;
  encargado: string;
  fecha_creacion: string;
}

export const dispositivosColumns: TableColumn<DispositivoItem>[] = [
  { key: "id_dispositivo", label: "ID" },
  { key: "modelo", label: "Modelo" },
  { key: "nro_serie", label: "Nro. Serie" },
  { key: "categoria", label: "Categoría" },
  { key: "encargado", label: "Encargado" },
  {
    key: "estado",
    label: "Estado",
    render: (d: DispositivoItem) =>
      d.estado === "Activo" ? (
        <span className="px-2 py-1 text-xs font-medium bg-green-100 text-green-700 rounded-full">
          Activo
        </span>
      ) : d.estado === "En reparación" ? (
        <span className="px-2 py-1 text-xs font-medium bg-yellow-100 text-yellow-700 rounded-full">
          En reparación
        </span>
      ) : (
        <span className="px-2 py-1 text-xs font-medium bg-red-100 text-red-700 rounded-full">
          {d.estado}
        </span>
      ),
  },
  {
    key: "fecha_fabricacion",
    label: "Fabricación",
    render: (d: DispositivoItem) =>
      new Date(d.fecha_fabricacion).toLocaleDateString(),
  },
  {
    key: "fecha_creacion",
    label: "Creación",
    render: (d: DispositivoItem) =>
      new Date(d.fecha_creacion).toLocaleDateString(),
  },
];
