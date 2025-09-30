import { useEffect, useState } from "react";
import type { TipoDispositivoItem } from "../../types";
import {
  getTipoDispositivoById,
  createTipoDispositivo,
  updateTipoDispositivo,
  deleteTipoDispositivo,
} from "../../services/TipoDispositivoService";

export function useTipoDispositivoForm(token: string | null, id?: number | string) {
  const numericId = typeof id === "string" ? Number(id) : id;
  const isEditing = !!numericId;

  const [data, setData] = useState<Partial<TipoDispositivoItem>>({
    nombre: "",
    detalle: "",
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // cargar si es edición
  useEffect(() => {
    if (!token || !isEditing || !numericId) return;

    setLoading(true);
    getTipoDispositivoById(token, numericId)
      .then(setData)
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, [token, numericId, isEditing]);

  // crear o actualizar
  const handleSubmit = async (formData: Partial<TipoDispositivoItem>) => {
    if (!token) return;

    try {
      if (isEditing && numericId) {
        await updateTipoDispositivo(token, numericId, formData);
      } else {
        await createTipoDispositivo(token, formData);
      }
    } catch (err: any) {
      setError(err.message || "Error al guardar tipo de dispositivo");
      throw err;
    }
  };

  // eliminar
  const handleDelete = async () => {
    if (!token || !numericId) return;

    try {
      await deleteTipoDispositivo(token, numericId);
    } catch (err: any) {
      setError(err.message || "Error al eliminar tipo de dispositivo");
      throw err;
    }
  };

  return { data, setData, loading, error, handleSubmit, handleDelete, isEditing };
}
