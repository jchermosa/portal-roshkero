// src/config/forms/ubicacionFormFields.ts
import type { FormSection } from "../../components/DynamicForm";
import { EstadoActivoInactivo, EstadoLabels } from "../../types";

export function buildUbicacionSections(): FormSection[] {
  return [
    {
      title: "Ubicación",
      icon: "📍",
      fields: [
        {
          name: "nombre",
          label: "Nombre",
          type: "text",
          required: true,
          placeholder: "Ej. Oficina Central",
        },
        {
          name: "estado",
          label: "Estado",
          type: "select",
          required: true,
          options: Object.values(EstadoActivoInactivo).map((value) => ({
            value,
            label: EstadoLabels[value],
          })),
        },
      ],
    },
  ];
}
