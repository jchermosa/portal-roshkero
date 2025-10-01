import type { DispositivoItem } from "../types";
import { EstadoInventarioEnum } from "../types";


export function mapDeviceToForm(res: any): Partial<DispositivoItem> {
  if (!res) {
    return {
      estado: EstadoInventarioEnum.D, // valor por defecto
    };
  }

  return {
    idDispositivo: res.idDispositivo ?? undefined,
    nroSerie: res.nroSerie ?? "",
    modelo: res.modelo ?? "",
    detalle: res.detalle ?? "",

    // aseguramos que fecha sea yyyy-mm-dd o undefined
    fechaFabricacion: res.fechaFabricacion
      ? String(res.fechaFabricacion).slice(0, 10)
      : undefined,

    // normalizamos IDs como number o undefined
    tipoDispositivo:
      res.tipoDispositivo != null
        ? Number(res.tipoDispositivo)
        : res.tipoDispositivo?.idTipoDispositivo
        ? Number(res.tipoDispositivo.idTipoDispositivo)
        : undefined,

    categoria: res.categoria ?? undefined,

    estado: (res.estado as EstadoInventarioEnum) ?? EstadoInventarioEnum.D,

    ubicacion:
      res.ubicacion != null
        ? Number(res.ubicacion)
        : res.ubicacion?.idUbicacion
        ? Number(res.ubicacion.idUbicacion)
        : undefined,

    encargado:
      res.encargado != null
        ? Number(res.encargado)
        : res.encargado?.idUsuario
        ? Number(res.encargado.idUsuario)
        : undefined,
  };
}


export function mapDeviceResponseToItem(res: any): DispositivoItem {
  return {
    idDispositivo: res.idDispositivo,
    tipoDispositivo: res.tipoDispositivo,
    ubicacion: res.ubicacion,
    nroSerie: res.nroSerie ?? "",
    modelo: res.modelo ?? "",
    detalle: res.detalle ?? "",
    fechaFabricacion: res.fechaFabricacion ?? "",
    estado: res.estado,
    categoria: res.categoria,
    encargado: res.encargado,
  };
}