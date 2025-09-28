import { useState } from "react";
import type { DispositivoAsignadoItem } from "../../types";
import {
  createDispositivoAsignado,
  updateDispositivoAsignado,
} from "../../services/DeviceAssignmentService";
import { EstadoAsignacionEnum } from "../../types";

export function useDispositivoAsignadoForm(
  token: string | null,
  id?: number,
  solicitudPreasignada?: number
) {
  const isEditing = !!id;

  // Estado inicial
  const [data, setData] = useState<Partial<DispositivoAsignadoItem>>({
    estadoAsignacion: EstadoAsignacionEnum.U, // por defecto "En uso"
    fechaEntrega: new Date().toISOString().split("T")[0], // default hoy
    ...(solicitudPreasignada ? { idSolicitud: solicitudPreasignada } : {}),
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Guardar (crear o actualizar)
  const handleSubmit = async (formData: Partial<DispositivoAsignadoItem>) => {
    if (!token) return;
    setLoading(true);

    try {
      if (isEditing && id) {
        await updateDispositivoAsignado(token, id, formData);
      } else {
        await createDispositivoAsignado(token, {
          ...formData,
          ...(solicitudPreasignada ? { idSolicitud: solicitudPreasignada } : {}),
        });
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
