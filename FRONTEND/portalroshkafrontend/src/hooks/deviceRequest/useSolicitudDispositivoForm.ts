import { useState } from "react";
import {
  acceptSolicitudDispositivo,
  rejectSolicitudDispositivo,
  createSolicitudDispositivo,
} from "../../services/DeviceRequestService";
import type { SolicitudDispositivoUI, UserSolDispositivoDto } from "../../types";
import { mapAdminSolicitudToUI, mapUserSolicitudToUI } from "../../mappers/solicitudDispositivoMapper";

export function useSolicitudDispositivoForm(token: string | null, id?: number, isSysAdmin: boolean = false) {
  const isEditing = !!id;

  const [data, setData] = useState<SolicitudDispositivoUI | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Crear nueva solicitud (usuario normal)
  const create = async (formData: UserSolDispositivoDto) => {
     console.log("[Hook] create() called", { hasToken: !!token, formData }); 
    if (!token) return;
    console.warn("[Hook] sin token → no se hace fetch");   
    setLoading(true);
    try {
      const created = await createSolicitudDispositivo(token, formData);
      const mapped = mapUserSolicitudToUI(created);
      setData(mapped);
      return mapped;
    } catch (err: any) {
      setError(err.message || "Error al crear la solicitud");
      throw err;
    } finally {
      setLoading(false);
    }
  };

  // Aceptar solicitud (solo admin)
  const accept = async () => {
    if (!token || !id || !isSysAdmin) return;
    setLoading(true);
    try {
      const updated = await acceptSolicitudDispositivo(token, id);
      const mapped = mapAdminSolicitudToUI(updated);
      setData(mapped);
      return mapped;
    } catch (err: any) {
      setError(err.message || "Error al aceptar la solicitud");
      throw err;
    } finally {
      setLoading(false);
    }
  };

  // Rechazar solicitud (solo admin)
  const reject = async () => {
    if (!token || !id || !isSysAdmin) return;
    setLoading(true);
    try {
      const updated = await rejectSolicitudDispositivo(token, id);
      const mapped = mapAdminSolicitudToUI(updated);
      setData(mapped);
      return mapped;
    } catch (err: any) {
      setError(err.message || "Error al rechazar la solicitud");
      throw err;
    } finally {
      setLoading(false);
    }
  };

  return {
    data,
    setData,
    loading,
    error,
    isEditing,
    create,
    accept,
    reject,
  };
}
