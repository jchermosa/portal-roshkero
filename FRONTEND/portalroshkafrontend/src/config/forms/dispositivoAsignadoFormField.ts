// src/config/forms/dispositivoAsignadoFormFields.ts
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
        {
          name: "idSolicitud",
          label: "Solicitud",
          type: "select",
          required: true,
          options: solicitudes,
          disabled: !!solicitudPreasignada, 
        },
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
        },
      ],
    },
  ];
}
