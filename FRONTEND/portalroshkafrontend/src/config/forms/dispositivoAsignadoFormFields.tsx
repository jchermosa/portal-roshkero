// src/config/forms/dispositivoAsignadoFormFields.tsx
import * as React from "react";
import type { FormSection } from "../../components/DynamicForm";
import { EstadoAsignacionEnum, EstadoAsignacionLabels } from "../../types";

export function buildDispositivoAsignadoSections(
  dispositivos: { value: number; label: string }[] = [],
  solicitudes: { value: number; label: string }[] = [],
  solicitudPreasignada?: number
): FormSection[] {
  return [
    {
      title: "Asignación de dispositivo",
      icon: "💻",
      fields: [
        {
          name: "idDispositivo",
          label: "Dispositivo",
          type: "select",
          required: true,
          options: dispositivos,
          placeholder: "Seleccionar dispositivo...",
        },
        ...(solicitudPreasignada
          ? ([
              {
                name: "idSolicitud",
                label: "Solicitud",
                type: "custom" as const,
                fullWidth: true,
                render: () => (
                  <div className="space-y-2">
                    <label className="text-sm font-semibold text-gray-700 dark:text-gray-300">
                      Solicitud
                    </label>

                    {/* Chip read-only */}
                    <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-blue-50 text-blue-700 dark:bg-blue-900/30 dark:text-blue-300 text-sm">
                      <span className="font-medium">ID</span>
                      <span className="font-semibold">#{solicitudPreasignada}</span>
                    </div>

                    {/* Hidden para que el valor viaje en el submit */}
                    <input type="hidden" name="idSolicitud" value={solicitudPreasignada} readOnly />
                  </div>
                ),
              },
            ] as const)
          : ([
              {
                name: "idSolicitud",
                label: "Solicitud",
                type: "select" as const,
                required: true,
                options: solicitudes,
                placeholder: "Seleccionar solicitud...",
              },
            ] as const)),
        {
          name: "fechaEntrega",
          label: "Fecha de entrega",
          type: "date",
          required: true,
        },
        {
          name: "fechaDevolucion",
          label: "Fecha de devolución",
          type: "date",
        },
      ],
    },
    {
      title: "Detalles de asignación",
      icon: "📋",
      fields: [
        {
          name: "estadoAsignacion",
          label: "Estado",
          type: "select",
          required: true,
          options: Object.values(EstadoAsignacionEnum).map((value) => ({
            value,
            label: EstadoAsignacionLabels[value],
          })),
        },
        {
          name: "observaciones",
          label: "Observaciones",
          type: "textarea",
          placeholder: "Detalle adicional (opcional)",
        },
      ],
    },
  ];
}
