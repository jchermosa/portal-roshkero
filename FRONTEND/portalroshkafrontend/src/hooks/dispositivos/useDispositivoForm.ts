import { useEffect, useState } from "react";
import type { DispositivoItem } from "../../types";
import {
  getDispositivoById,
  createDispositivo,
  updateDispositivo,
} from "../../services/DeviceService";

export function useDispositivoForm(
  token: string | null,
  id?: string
) {
  const isEditing = !!id;

  // Estado inicial (creación)
  const [data, setData] = useState<Partial<DispositivoItem>>({
    estado: "Activo",
    fecha_creacion: new Date().toISOString().split("T")[0], // default hoy
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Cargar dispositivo si es edición
  useEffect(() => {
    if (!token || !isEditing || !id) return;

    setLoading(true);
    getDispositivoById(token, id)
      .then((res) => {
        setData({
          ...res,
          estado: res.estado ?? "Activo",
        });
      })
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, [token, id, isEditing]);

  // Guardar (crear o actualizar)
  const handleSubmit = async (formData: Partial<DispositivoItem>) => {
    if (!token) return;

    try {
      if (isEditing && id) {
        await updateDispositivo(token, id, formData);
      } else {
        await createDispositivo(token, formData);
      }
    } catch (err: any) {
      setError(err.message || "Error al guardar el dispositivo");
      throw err; 
    }
  };

  return { data, setData, loading, error, handleSubmit, isEditing };
}
