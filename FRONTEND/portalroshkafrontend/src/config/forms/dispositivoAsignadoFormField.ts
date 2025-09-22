// src/config/forms/dispositivoAsignadoFormFields.ts
import type { FormSection } from "../../components/DynamicForm";

export function buildDispositivoAsignadoSections(): FormSection[] {
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
          // ⚡ Los options se pueden inyectar dinámicamente desde el hook
          options: [],
        },
        {
          name: "id_solicitud",
          label: "Solicitud",
          type: "select",
          required: true,
          options: [],
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
            { value: "ASIGNADO", label: "Asignado" },
            { value: "DEVUELTO", label: "Devuelto" },
            { value: "EN_REPARACION", label: "En reparación" },
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
