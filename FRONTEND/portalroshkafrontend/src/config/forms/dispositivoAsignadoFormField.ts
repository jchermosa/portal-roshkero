// src/config/forms/dispositivoAsignadoFormFields.ts
import type { FormSection } from "../../components/DynamicForm";

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
          name: "id_dispositivo",
          label: "Dispositivo",
          type: "select",
          required: true,
          options: dispositivos,
        },
        {
          name: "id_solicitud",
          label: "Solicitud",
          type: "select",
          required: true,
          options: solicitudes,
          disabled: !!solicitudPreasignada, // ⚡ si viene de aprobación, no se puede cambiar
        },
        {
          name: "fecha_entrega",
          label: "Fecha de entrega",
          type: "date",
          required: true,
        },
        {
          name: "fecha_devolucion",
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
          name: "estado_asignacion",
          label: "Estado",
          type: "select",
          required: true,
          options: [
            { value: "Activo", label: "Activo" },
            { value: "Devuelto", label: "Devuelto" },
            { value: "EnReparacion", label: "En reparación" },
          ],
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
