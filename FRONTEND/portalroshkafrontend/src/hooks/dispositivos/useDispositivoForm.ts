// src/hooks/dispositivos/useDispositivoForm.ts
import { useEffect, useState } from "react";
import type { DispositivoItem } from "../../types";
import {
  getDispositivoById,
  createDispositivo,
  updateDispositivo,
} from "../../services/DeviceService";
import { EstadoInventarioEnum } from "../../types";
import { mapDeviceToForm } from "../../mappers/dispositivoMapper";

export function useDispositivoForm(token: string | null, id?: number) {
  const isEditing = !!id;

  // Estado inicial con defaults (igual que en user)
  const [data, setData] = useState<Partial<DispositivoItem>>({
    estado: EstadoInventarioEnum.D,
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // 📥 Cargar dispositivo si estamos editando
  useEffect(() => {
    if (!token || !isEditing || !id) return;

    setLoading(true);
    getDispositivoById(token, Number(id))
      .then((res) => {
        const mapped = mapDeviceToForm(res);
        console.log("✅ mapped device (toForm):", mapped);
        setData(mapped);
      })
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, [token, id, isEditing]);

  // 💾 Crear o actualizar
  const handleSubmit = async (
    formData: Partial<DispositivoItem>
  ): Promise<boolean> => {
    if (!token) return false;

    // Validaciones mínimas (ajustar según tu modelo)
    if (!formData.nroSerie?.trim() || !formData.modelo?.trim()) {
      setError("Faltan datos obligatorios");
      return false;
    }

    setLoading(true);
    setError(null);

    try {
      if (isEditing && id) {
        await updateDispositivo(token, Number(id), formData);
      } else {
        await createDispositivo(token, formData);
      }
      return true;
    } catch (err: any) {
      console.error("Error en handleSubmit:", err);
      setError(err.message || "Error al guardar el dispositivo");
      return false;
    } finally {
      setLoading(false);
    }
  };

  return { data, setData, loading, error, handleSubmit, isEditing };
}
