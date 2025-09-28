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
          name: "fechaInicio",
          label: "Fecha de inicio",
          type: "date",
          required: true,
        },
        {
          name: "cantDias",
          label: "Cantidad de días",
          type: "number",
          required: true,
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
