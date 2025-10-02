// src/config/tables/solicitudDispositivoTableConfig.ts
import type { ReactNode } from "react";
import type { SolicitudDispositivoUI } from "../../types";
import { EstadoSolicitudLabels } from "../../types";

export interface TableColumn<T> {
  key: string;
  label: string;
  render?: (row: T) => ReactNode;
}

export function buildSolicitudDispositivoColumns(
  isSysAdmin: boolean
): TableColumn<SolicitudDispositivoUI>[] {
  // columnas base (todos los usuarios)
  const base: TableColumn<SolicitudDispositivoUI>[] = [
    { key: "idSolicitud", label: "ID" },
    {
      key: "comentario",
      label: "Comentario",
      render: (s) => s.comentario || "-",
    },
    {
      key: "estado",
      label: "Estado",
      render: (s) => {
        const label = EstadoSolicitudLabels[s.estado];
        const color =
          s.estado === "P"
            ? "bg-yellow-100 text-yellow-700"
            : s.estado === "A"
            ? "bg-green-100 text-green-700"
            : s.estado === "R"
            ? "bg-red-100 text-red-700"
            : "bg-blue-100 text-blue-700";

        return (
          <span
            className={`px-2 py-1 text-xs font-medium rounded-full ${color}`}
          >
            {label}
          </span>
        );
      },
    },
    {
      key: "fechaInicio",
      label: "Fecha Inicio",
      render: (s) =>
        s.fechaInicio ? new Date(s.fechaInicio).toLocaleDateString() : "-",
    },
    {
      key: "fechaFin",
      label: "Fecha Fin",
      render: (s) =>
        s.fechaFin ? new Date(s.fechaFin).toLocaleDateString() : "-",
    },
  ];

  // columnas adicionales solo para SysAdmin
  if (isSysAdmin) {
    return [
      { key: "idSolicitud", label: "ID" },
      {
        key: "usuarioId",
        label: "Usuario",
        render: (s) => s.usuarioNombre ?? "-",
      },
      ...base.slice(1), // reuso todas excepto el ID (ya está arriba)
      {
        key: "fuente",
        label: "Origen",
        render: (s) =>
          s.fuente === "ADMINISTRADOR" ? (
            <span className="px-2 py-1 text-xs font-medium rounded-full bg-blue-100 text-blue-700">
              Admin
            </span>
          ) : (
            <span className="px-2 py-1 text-xs font-medium rounded-full bg-gray-100 text-gray-700">
              Usuario
            </span>
          ),
      },
    ];
  }

  return base;
}
