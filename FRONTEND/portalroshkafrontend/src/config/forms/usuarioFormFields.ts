// src/config/forms/usuarioFormFields.ts
import type { FormSection } from "../../components/DynamicForm";
import type { EquipoItem, RolItem, CargoItem } from "../../types";
import { Estado, Seniority, Foco } from "../../types"; // 👈 Importamos los objetos, no los types

export function buildUsuarioSections(
  equipos: EquipoItem[],
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
        { name: "nro_cedula", label: "Número de cédula", type: "text", required: true },
        { name: "correo", label: "Correo electrónico", type: "email", required: true },
        { name: "telefono", label: "Teléfono", type: "text" },
        { name: "fecha_ingreso", label: "Fecha de ingreso", type: "date" },
        { name: "fecha_nacimiento", label: "Fecha de nacimiento", type: "date" },
      ],
    },
    {
      title: "Rol y asignación",
      icon: "🛠️",
      fields: [
        {
          name: "id_equipo",
          label: "Equipo",
          type: "select",
          required: true,
          options: equipos.map((e) => ({ value: e.id, label: e.nombre })),
        },
        {
          name: "id_rol",
          label: "Rol",
          type: "select",
          required: true,
          options: roles.map((r) => ({ value: r.id, label: r.nombre })),
        },
        {
          name: "id_cargo",
          label: "Cargo",
          type: "select",
          required: true,
          options: cargos.map((c) => ({ value: c.id, label: c.nombre })),
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
          options: Object.values(Estado).map((v) => ({ value: v, label: v })),
        },
        {
          name: "seniority",
          label: "Seniority",
          type: "select",
          options: Object.values(Seniority).map((v) => ({ value: v, label: v })),
        },
        {
          name: "foco",
          label: "Foco principal",
          type: "select",
          options: Object.values(Foco).map((v) => ({ value: v, label: v })),
        },
        {
          name: "requiere_cambio_contrasena",
          label: "Requiere cambio de contraseña",
          type: "checkbox",
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
      ],
    },
  ];
}
