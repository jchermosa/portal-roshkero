import type { RolInsert } from "../types";

export function mapFormToRolInsert(formData: any): RolInsert {
  return {
    nombre: (formData.nombre ?? "").toString().trim(),
  };
}
