import type { FormSection } from "../../components/DynamicForm";

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
          options: [
            { value: "ACTIVO", label: "Activo" },
            { value: "INACTIVO", label: "Inactivo" },
          ],
        },
      ],
    },
  ];
}
