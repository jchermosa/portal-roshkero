import type { FormSection } from "../../components/DynamicForm";

export function buildSolicitudDispositivoSections(
  tiposDispositivo: { value: number; label: string }[]
): FormSection[] {
  return [
    {
      title: "Datos de la solicitud",
      icon: "📝",
      fields: [
        
        {
          name: "id_tipo_dispositivo",
          label: "Tipo de dispositivo",
          type: "select",
          required: true,
          options: tiposDispositivo,
        },
        {
          name: "fecha_inicio",
          label: "Fecha de solicitud",
          type: "date",
          required: true,
        },
        // {
        //   name: "cant_dias",
        //   label: "Cantidad de días",
        //   type: "number",
        //   min: 1,
        // },
        // {
        //   name: "fecha_fin",
        //   label: "Fecha de fin",
        //   type: "date",
        // },
        {
          name: "comentario",
          label: "Comentario",
          type: "textarea",
        },
      ],
    },
  ];
}
