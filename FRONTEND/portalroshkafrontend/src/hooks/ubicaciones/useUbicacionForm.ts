import { useEffect, useState } from "react";
import type { UbicacionItem } from "../../types";
import {
  getUbicacionById,
  createUbicacion,
  updateUbicacion,
  deleteUbicacion,
} from "../../services/UbicacionService";

export function useUbicacionForm(token: string | null, id?: number | string) {
  const numericId = typeof id === "string" ? Number(id) : id;
  const isEditing = !!numericId;

  const [data, setData] = useState<Partial<UbicacionItem>>({
    nombre: "",
    estado: "ACTIVO", // default
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // cargar si es edición
  useEffect(() => {
    if (!token || !isEditing || !numericId) return;

    setLoading(true);
    getUbicacionById(token, numericId)
      .then(setData)
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, [token, numericId, isEditing]);

  // crear o actualizar
  const handleSubmit = async (formData: Partial<UbicacionItem>) => {
    if (!token) return;

    try {
      if (isEditing && numericId) {
        await updateUbicacion(token, numericId, formData);
      } else {
        await createUbicacion(token, formData);
      }
    } catch (err: any) {
      setError(err.message || "Error al guardar ubicación");
      throw err;
    }
  };

  // eliminar
  const handleDelete = async () => {
    if (!token || !numericId) return;

    try {
      await deleteUbicacion(token, numericId);
    } catch (err: any) {
      setError(err.message || "Error al eliminar ubicación");
      throw err;
    }
  };

  return { data, setData, loading, error, handleSubmit, handleDelete, isEditing };
}
