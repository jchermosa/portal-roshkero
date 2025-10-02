import type { FormSection } from "../../components/DynamicForm";

export function buildTecnologiaSections(): FormSection[] {
  return [
    {
      title: "Datos de la tecnología",
      icon: "💻",
      fields: [
        {
          name: "nombre",
          label: "Nombre",
          type: "text",
          required: true,
          placeholder: "Ej: React, Spring Boot",
        },
        {
          name: "descripcion",
          label: "Descripción",
          type: "textarea",
          placeholder: "Breve descripción de la tecnología",
        },
      ],
    },
  ];
}
