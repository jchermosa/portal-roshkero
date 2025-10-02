// src/config/forms/tipoDispositivoFormFields.ts
import type { FormSection } from "../../components/DynamicForm";

export function buildTipoDispositivoSections(): FormSection[] {
  return [
    {
      title: "Tipo de dispositivo",
      icon: "📦",
      fields: [
        {
          name: "nombre",
          label: "Nombre",
          type: "text",
          required: true,
          placeholder: "Ej. Notebook",
        },
        {
          name: "detalle",
          label: "Detalle",
          type: "textarea",
          required: true,
          placeholder: "Ej. Computadora portátil",
        },
      ],
    },
  ];
}
