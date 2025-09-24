import { useEffect, useState } from "react";
import type { SolicitudDispositivoItem } from "../../types";
import {
  getSolicitudDispositivoById,
  createSolicitudDispositivo,
  updateSolicitudDispositivo,
} from "../../services/DeviceRequestService";

export function useSolicitudDispositivoForm(
  token: string | null,
  userId?: number, // 👈 nuevo parámetro para inyectar el usuario loggeado
  id?: string
) {
  const isEditing = !!id;

  // Estado inicial (para creación)
  const [data, setData] = useState<Partial<SolicitudDispositivoItem>>({
    tipo_solicitud: "Dispositivo",   // ⚡ siempre forzado
    estado: "Pendiente",             // valor inicial
    fecha_inicio: new Date().toISOString().split("T")[0],
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Cargar solicitud si es edición
  useEffect(() => {
    if (!token || !isEditing || !id) return;

    setLoading(true);
    getSolicitudDispositivoById(token, id)
      .then((res) => {
        setData({
          ...res,
          tipo_solicitud: "Dispositivo", // aseguramos tipo
        });
      })
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, [token, id, isEditing]);

  // Guardar (crear o actualizar)
  const handleSubmit = async (formData: Partial<SolicitudDispositivoItem>) => {
    if (!token) return;

    const payload: Partial<SolicitudDispositivoItem> = {
      ...formData,
      id_usuario: userId, // siempre el usuario loggeado
      tipo_solicitud: "Dispositivo", // nunca se pierde
    };

    try {
      if (isEditing && id) {
        await updateSolicitudDispositivo(token, id, payload);
      } else {
        await createSolicitudDispositivo(token, payload);
      }
    } catch (err: any) {
      setError(err.message || "Error al guardar la solicitud de dispositivo");
      throw err;
    }
  };

  return { data, setData, loading, error, handleSubmit, isEditing };
}
