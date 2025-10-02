import type { TecnologiaRequest } from "../types";

export function mapFormToTecnologiaRequest(formData: Record<string, any>): TecnologiaRequest {
  return {
    nombre: formData.nombre ?? "",
    descripcion: formData.descripcion ?? "",
  };
}
