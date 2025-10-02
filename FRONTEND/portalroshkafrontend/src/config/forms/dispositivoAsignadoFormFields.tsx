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
        },
        solicitudPreasignada
          ? {
              name: "solicitud",
              label: "Solicitud",
              type: "custom" as const,
              fullWidth: true,
              render: () => (
                <div className="space-y-2">
                  <label className="text-sm font-semibold text-gray-700 dark:text-gray-300">
                    Solicitud
                  </label>
                  <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-blue-50 text-blue-700 dark:bg-blue-900/30 dark:text-blue-200">
                    <span className="font-medium">ID</span>
                    <span className="font-semibold">#{solicitudPreasignada}</span>
                  </div>
                  <input type="hidden" name="idSolicitud" value={solicitudPreasignada} />
                </div>
              ),
            }
          : {
              name: "idSolicitud",
              label: "Solicitud",
              type: "select" as const,
              required: true,
              options: solicitudes,
            },
        { name: "fechaEntrega", label: "Fecha de entrega", type: "date", required: true },
        { name: "fechaDevolucion", label: "Fecha de devolución", type: "date" },
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
          options: Object.values(EstadoAsignacionEnum).map((v) => ({
            value: v,
            label: EstadoAsignacionLabels[v],
          })),
        },
        { name: "observaciones", label: "Observaciones", type: "text" },
      ],
    },
  ];
}
