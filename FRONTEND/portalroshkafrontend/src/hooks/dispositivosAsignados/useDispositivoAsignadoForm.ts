import { useEffect, useState } from "react";
import type { DispositivoAsignadoItem } from "../../types";
import {
  getDispositivoAsignadoById,
  createDispositivoAsignado,
  updateDispositivoAsignado,
} from "../../services/DeviceAssignmentService";

export function useDispositivoAsignadoForm(
  token: string | null,
  id?: string,
  solicitudPreasignada?: number // ⚡ nueva prop
) {
  const isEditing = !!id;

  // Estado inicial
  const [data, setData] = useState<Partial<DispositivoAsignadoItem>>({
    estado_asignacion: "Activo", // por defecto
    fecha_entrega: new Date().toISOString().split("T")[0], // fecha actual
    ...(solicitudPreasignada ? { id_solicitud: solicitudPreasignada } : {}), // ⚡ si viene de aprobación, se guarda
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Cargar registro si es edición
  useEffect(() => {
    if (!token || !isEditing || !id) return;

    setLoading(true);
    getDispositivoAsignadoById(token, id)
      .then((res) => setData(res))
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, [token, id, isEditing]);

  // Guardar (crear o actualizar)
  const handleSubmit = async (formData: Partial<DispositivoAsignadoItem>) => {
    if (!token) return;

    try {
      if (isEditing && id) {
        await updateDispositivoAsignado(token, id, formData);
      } else {
        await createDispositivoAsignado(token, {
          ...formData,
          ...(solicitudPreasignada ? { id_solicitud: solicitudPreasignada } : {}),
        });
      }
    } catch (err: any) {
      setError(err.message || "Error al guardar el dispositivo asignado");
      throw err;
    }
  };

  return { data, setData, loading, error, handleSubmit, isEditing };
}
