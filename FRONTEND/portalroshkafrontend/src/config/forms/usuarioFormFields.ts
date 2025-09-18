import type { FormSection } from "../../components/DynamicForm";
import type { EquipoItem, RolItem, CargoItem } from "../../types";

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
          name: "equipoId",
          label: "Equipo",
          type: "select",
          required: true,
          options: equipos.map((e) => ({ value: e.id, label: e.nombre })),
        },
        {
          name: "rolId",
          label: "Rol",
          type: "select",
          required: true,
          options: roles.map((r) => ({ value: r.id, label: r.nombre })),
        },
        {
          name: "cargoId",
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
        { name: "estado", label: "Usuario activo", type: "checkbox" },
        { name: "requiereCambioContrasena", label: "Requiere cambio de contraseña", type: "checkbox" },
        {
            name: "disponibilidad",
            label: "Disponibilidad",
            type: "slider",
            min: 0,
            max: 100,
            step: 5,
            required: true,
        }
      ],
    },
  ];
}
