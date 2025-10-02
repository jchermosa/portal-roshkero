import type { CargoInsert } from "../types";

export function mapFormToCargoInsert(formData: any): CargoInsert {
  return {
    nombre: (formData.nombre ?? "").toString().trim(),
  };
}
