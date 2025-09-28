import { useState } from "react";
import type { SolicitudDispositivoItem } from "../../types";
import {
  acceptSolicitudDispositivo,
  rejectSolicitudDispositivo,
} from "../../services/DeviceRequestService";
import { EstadoSolicitudEnum } from "../../types";

export function useSolicitudDispositivoForm(
  token: string | null,
  id?: number
) {
  const isEditing = !!id;

  // Estado inicial (para visualización, no creación)
  const [data, setData] = useState<Partial<SolicitudDispositivoItem>>({
    tipoSolicitud: "Dispositivo",
    estado: EstadoSolicitudEnum.P, // Pendiente por defecto
    fechaInicio: new Date().toISOString().split("T")[0],
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Guardar (solo aceptar o rechazar)
  const handleAccept = async () => {
    if (!token || !id) return;
    setLoading(true);

    try {
      const updated = await acceptSolicitudDispositivo(token, id);
      setData(updated);
    } catch (err: any) {
      setError(err.message || "Error al aceptar la solicitud");
      throw err;
    } finally {
      setLoading(false);
    }
  };

  const handleReject = async () => {
    if (!token || !id) return;
    setLoading(true);

    try {
      const updated = await rejectSolicitudDispositivo(token, id);
      setData(updated);
    } catch (err: any) {
      setError(err.message || "Error al rechazar la solicitud");
      throw err;
    } finally {
      setLoading(false);
    }
  };

  return { data, setData, loading, error, handleAccept, handleReject, isEditing };
}
