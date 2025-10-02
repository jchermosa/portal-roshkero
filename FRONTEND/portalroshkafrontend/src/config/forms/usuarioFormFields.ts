import type { FormSection } from "../../components/DynamicForm";
import type { RolItem, CargoItem } from "../../types";
import {
  EstadoLabels,
  SeniorityLabels,
  FocoLabels,
} from "../../types";

export function buildUsuarioSections(
  roles: RolItem[],
  cargos: CargoItem[]
): FormSection[] {
  return [
    {
      title: "Información básica",
      icon: "👤",
      fields: [
        { name: "nombre", label: "Nombre", type: "text", required: true },
        { name: "apellido", label: "Apellido", type: "text", required: true },
        { name: "nroCedula", label: "Número de cédula", type: "text", required: true },
        { name: "correo", label: "Correo electrónico", type: "email", required: true },
        { name: "telefono", label: "Teléfono", type: "text" },
        { name: "fechaIngreso", label: "Fecha de ingreso", type: "date" },
        { name: "fechaNacimiento", label: "Fecha de nacimiento", type: "date" },
      ],
    },
    {
      title: "Rol y asignación",
      icon: "🛠️",
      fields: [
        {
          name: "idRol",
          label: "Rol",
          type: "select",
          required: true,
          options: roles.map((r) => ({ value: r.idRol, label: r.nombre })),
        },
        {
          name: "idCargo",
          label: "Cargo",
          type: "select",
          required: true,
          options: cargos.map((c) => ({ value: c.idCargo, label: c.nombre })),
        },
      ],
    },
    {
      title: "Configuración avanzada",
      icon: "⚙️",
      fields: [
        {
          name: "estado",
          label: "Estado",
          type: "select",
          required: true,
          options: Object.entries(EstadoLabels).map(([value, label]) => ({
            value,
            label,
          })),
        },
        {
          name: "seniority",
          label: "Seniority",
          type: "select",
          options: Object.entries(SeniorityLabels).map(([value, label]) => ({
            value,
            label,
          })),
        },
        {
          name: "foco",
          label: "Foco principal",
          type: "select",
          options: Object.entries(FocoLabels).map(([value, label]) => ({
            value,
            label,
          })),
        },
        {
          name: "requiereCambioContrasena",
          label: "Requiere cambio de contraseña",
          type: "select",
          options: [
            { value: "true", label: "Sí" },
            { value: "false", label: "No" },
          ],
        },
        {
          name: "disponibilidad",
          label: "Disponibilidad (%)",
          type: "slider",
          min: 0,
          max: 100,
          step: 5,
          required: true,
        },
        // {
        //   name: "urlPerfil",
        //   label: "URL Perfil",
        //   type: "text",
        //   placeholder: "https://...",
        // },
      ],
    },
  ];
}
