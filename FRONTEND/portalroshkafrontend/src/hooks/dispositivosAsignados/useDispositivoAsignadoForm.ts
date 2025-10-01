import { useEffect, useState } from "react";
import type { DispositivoAsignadoItem } from "../../types";
import {
  createDispositivoAsignado,
  updateDispositivoAsignado,
  // getDispositivoAsignadoById  // ← TODO si luego expones este endpoint
} from "../../services/DeviceAssignmentService";
import { EstadoAsignacionEnum } from "../../types";

export function useDispositivoAsignadoForm(
  token: string | null,
  id?: number,
  solicitudPreasignada?: number
) {
  const isEditing = !!id;

  const [data, setData] = useState<Partial<DispositivoAsignadoItem>>({
    estadoAsignacion: EstadoAsignacionEnum.U,      // "En uso" por defecto
    fechaEntrega: new Date().toISOString().split("T")[0], // YYYY-MM-DD
    ...(solicitudPreasignada ? { idSolicitud: solicitudPreasignada } : {}),
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // TODO: si luego tienes endpoint, carga aquí los datos al editar
  // useEffect(() => {
  //   if (!token || !isEditing || !id) return;
  //   setLoading(true);
  //   getDispositivoAsignadoById(token, id)
  //     .then((res) => setData(res))
  //     .catch((e: any) => setError(e.message || "Error al cargar la asignación"))
  //     .finally(() => setLoading(false));
  // }, [token, isEditing, id]);

  const handleSubmit = async (formData: Partial<DispositivoAsignadoItem>) => {
    if (!token) return;

    // Normalizar tipos (selects llegan como string)
    const idDispositivo =
      formData.idDispositivo != null ? Number(formData.idDispositivo) : undefined;

    // Priorizar la solicitud preasignada si existe
    const idSolicitud =
      solicitudPreasignada != null
        ? solicitudPreasignada
        : formData.idSolicitud != null
        ? Number(formData.idSolicitud)
        : undefined;

    const payload: Partial<DispositivoAsignadoItem> = {
      ...formData,
      idDispositivo,
      idSolicitud,
      // mantener fechaEntrega/fechaDevolucion tal como vienen (YYYY-MM-DD)
      // estadoAsignacion ya es string literal del enum ("U" | "D")
    };

    // Validaciones rápidas
    if (!payload.idDispositivo) {
      setError("Debe seleccionar un dispositivo.");
      throw new Error("Debe seleccionar un dispositivo.");
    }
    if (!payload.idSolicitud) {
      setError("Debe indicar la solicitud asociada.");
      throw new Error("Debe indicar la solicitud asociada.");
    }
    if (!payload.fechaEntrega) {
      setError("Debe indicar la fecha de entrega.");
      throw new Error("Debe indicar la fecha de entrega.");
    }

    setLoading(true);
    setError(null);
    try {
      if (isEditing && id) {
        await updateDispositivoAsignado(token, id, payload);
      } else {
        await createDispositivoAsignado(token, payload);
      }
    } catch (err: any) {
      setError(err.message || "Error al guardar la asignación de dispositivo");
      throw err;
    } finally {
      setLoading(false);
    }
  };

  return { data, setData, loading, error, handleSubmit, isEditing };
}
