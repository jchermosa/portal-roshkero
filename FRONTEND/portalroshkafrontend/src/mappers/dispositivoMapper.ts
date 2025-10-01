import type { DispositivoItem } from "../types";

export function mapDeviceToForm(d?: Partial<DispositivoItem>) {
  if (!d) return {};
  return {
    nroSerie: d.nroSerie ?? "",
    modelo: d.modelo ?? "",
    detalle: d.detalle ?? "",
    fechaFabricacion: d.fechaFabricacion ? String(d.fechaFabricacion).slice(0, 10) : "",
    tipoDispositivo: d.tipoDispositivo != null ? Number(d.tipoDispositivo) : "",
    categoria: d.categoria ?? "",
    estado: d.estado ?? "",

    ubicacion: d.ubicacion != null ? Number(d.ubicacion) : "",
    encargado: d.encargado != null ? Number(d.encargado) : "",
  };
}
