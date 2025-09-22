// src/config/forms/dispositivoFormFields.ts
import type { FormSection } from "../../components/DynamicForm";

export function buildDispositivoSections(): FormSection[] {
  return [
    {
      title: "Información básica",
      icon: "💻",
      fields: [
        { name: "nroSerie", label: "Número de serie", type: "text", required: true },
        { name: "modelo", label: "Modelo", type: "text", required: true },
        { name: "detalles", label: "Detalles", type: "textarea" },
        { name: "fechaFabricacion", label: "Fecha de fabricación", type: "date" },
        { name: "fechaCreacion", label: "Fecha de creación", type: "date" },
      ],
    },
    {
      title: "Clasificación",
      icon: "📦",
      fields: [
        {
          name: "idTipoDispositivo",
          label: "Tipo de dispositivo",
          type: "select",
          required: true,
          // Opciones vendrán desde catálogos en el futuro
          options: [],
        },
        {
          name: "categoria",
          label: "Categoría",
          type: "select",
          required: true,
          options: [
            { value: "Laptop", label: "Laptop" },
            { value: "Impresora", label: "Impresora" },
            { value: "Servidor", label: "Servidor" },
            { value: "Otro", label: "Otro" },
          ],
        },
        {
          name: "estado",
          label: "Estado",
          type: "select",
          required: true,
          options: [
            { value: "Activo", label: "Activo" },
            { value: "En reparación", label: "En reparación" },
            { value: "Inactivo", label: "Inactivo" },
          ],
        },
      ],
    },
    {
      title: "Asignación",
      icon: "👤",
      fields: [
        {
          name: "idUbicacion",
          label: "Ubicación",
          type: "select",
          required: true,
          options: [], // se llena dinámicamente desde catálogos si existiera
        },
        { name: "encargado", label: "Encargado", type: "text" },
      ],
    },
  ];
}
