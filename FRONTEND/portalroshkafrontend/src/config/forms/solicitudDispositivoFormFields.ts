// src/config/forms/solicitudDispositivoFormFields.ts
import type { FormSection } from "../../components/DynamicForm";

export function buildSolicitudDispositivoSections(
  tiposDispositivo: { value: number; label: string }[] = []
): FormSection[] {
  return [
    {
      title: "Datos de la solicitud",
      icon: "📝",
      fields: [
        {
          name: "idTipoDispositivo",
          label: "Tipo de dispositivo",
          type: "select",
          required: true,
          options: tiposDispositivo,
        },
        {
          name: "comentario",
          label: "Comentario",
          type: "textarea",
        },
      ],
    },
  ];
}
