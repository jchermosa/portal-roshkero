import { useState } from "react";
import type { DispositivoItem } from "../../types";
import {
  createDispositivo,
  updateDispositivo,
} from "../../services/DeviceService";
import { EstadoInventarioEnum } from "../../types";

export function useDispositivoForm(token: string | null, id?: number) {
  const isEditing = !!id;

  const [data, setData] = useState<Partial<DispositivoItem>>({
    estado: EstadoInventarioEnum.D, 
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Guardar (crear o actualizar)
  const handleSubmit = async (formData: Partial<DispositivoItem>) => {
    if (!token) return;
    setLoading(true);

    try {
      if (isEditing && id) {
        await updateDispositivo(token, id, formData);
      } else {
        await createDispositivo(token, formData);
      }
    } catch (err: any) {
      setError(err.message || "Error al guardar el dispositivo");
      throw err;
    } finally {
      setLoading(false);
    }
  };

  return { data, setData, loading, error, handleSubmit, isEditing };
}
