import { useEffect, useState } from "react";
import type { UbicacionItem, EstadoActivoInactivo } from "../../types";
import {
  getUbicacionById,
  createUbicacion,
  updateUbicacion,
  deleteUbicacion, // toggle A/I en tu backend
} from "../../services/UbicacionService";

export function useUbicacionForm(token: string | null, id?: number | string) {
  const numericId = typeof id === "string" ? Number(id) : id;
  const isEditing = Number.isFinite(numericId);

  const [data, setData] = useState<Partial<UbicacionItem>>({
    nombre: "",
    estado: "A", // ✅ default correcto según tu enum
  });
  const [loading, setLoading] = useState(false);
  const [saving, setSaving] = useState(false);
  const [toggling, setToggling] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Cargar detalle si es edición
  useEffect(() => {
    if (!token || !isEditing || !numericId) return;
    setLoading(true);
    setError(null);
    getUbicacionById(token, numericId)
      .then((res) => {
        // Asegurar shape y defaults
        setData({
          idUbicacion: res.idUbicacion,
          nombre: res.nombre ?? "",
          estado: (res.estado as EstadoActivoInactivo) ?? "A",
        });
      })
      .catch((err) => setError(err.message || "Error al cargar la ubicación"))
      .finally(() => setLoading(false));
  }, [token, numericId, isEditing]);

  // Crear o actualizar
  const handleSubmit = async (formData: Partial<UbicacionItem>) => {
    if (!token) return;
    setSaving(true);
    setError(null);
    try {
      const payload: Partial<UbicacionItem> = {
        nombre: (formData.nombre ?? "").toString().trim(),
        estado: (formData.estado as EstadoActivoInactivo) ?? "A",
      };

      if (isEditing && numericId) {
        await updateUbicacion(token, numericId, payload);
      } else {
        await createUbicacion(token, payload);
      }
    } catch (err: any) {
      setError(err.message || "Error al guardar ubicación");
      throw err;
    } finally {
      setSaving(false);
    }
  };

  // Toggle estado (DELETE en tu backend)
  const toggleEstado = async () => {
    if (!token || !numericId) return;
    setToggling(true);
    setError(null);
    try {
      await deleteUbicacion(token, numericId);
    } catch (err: any) {
      setError(err.message || "Error al cambiar estado");
      throw err;
    } finally {
      setToggling(false);
    }
  };

  return {
    data,
    setData,
    loading,
    saving,
    toggling,
    error,
    isEditing,
    handleSubmit,
    handleDelete: toggleEstado, 
    toggleEstado,
  };
}
