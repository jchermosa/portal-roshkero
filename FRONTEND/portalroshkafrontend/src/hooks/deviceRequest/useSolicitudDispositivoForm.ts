// src/hooks/deviceRequest/useSolicitudDispositivoForm.ts
import { useCallback, useState, useEffect } from "react";
import {
  acceptSolicitudDispositivo,
  rejectSolicitudDispositivo,
  createSolicitudDispositivo,
  getSolicitudById,
} from "../../services/DeviceRequestService";
import type { SolicitudDispositivoUI, UserSolDispositivoDto } from "../../types";
import { mapUserSolicitudToUI, mapAdminSolicitudToUI } from "../../mappers/solicitudDispositivoMapper";

export function useSolicitudDispositivoForm(
  token: string | null,
  id?: number,
  isSysAdmin: boolean = false
) {
  const isEditing = !!id;

  const [data, setData] = useState<SolicitudDispositivoUI | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  
  // 🔹 cargar detalle al editar
  useEffect(() => {
    if (!token || !isEditing || !id) return;
    setLoading(true);
    setError(null);
    console.log("[Hook] fetch detalle id:", id);

    getSolicitudById(token, id)
      .then((raw: any) => {
        // Usa el mapper que corresponda a tu DTO real
        const mapped = isSysAdmin ? mapAdminSolicitudToUI(raw) : mapUserSolicitudToUI(raw);
        console.log("[Hook] detalle mapeado:", mapped);
        setData(mapped);
      })
      .catch((e: any) => {
        console.error("[Hook] error detalle:", e);
        setError(e?.message || "Error al cargar la solicitud");
      })
      .finally(() => setLoading(false));
  }, [token, id, isEditing, isSysAdmin]);

  // Crear (usuario)
  const create = useCallback(async (formData: UserSolDispositivoDto) => {
    if (!token) {
      const msg = "No hay token de autenticación. Inicia sesión nuevamente.";
      setError(msg);
      throw new Error(msg);
    }
    setLoading(true);
    setError(null);
    try {
      const created = await createSolicitudDispositivo(token, formData);
      // opcional: map para feedback local; el refresh externo sigue siendo la fuente de verdad
      const mapped = mapUserSolicitudToUI({
        idSolicitud: (created as any).idSolicitud ?? 0,
        tipoSolicitud: "DISPOSITIVO",
        estado: (created as any).estado ?? "P",
        comentario: (created as any).comentario ?? "",
        fechaInicio: (created as any).fechaInicio ?? null,
        fechaFin: (created as any).fechaFin ?? null,
        cantDias: (created as any).cantDias ?? null,
        idTipoDispositivo:
          (created as any).idTipoDispositivo ??
          (formData as any).id_tipo_dispositivo ??
          null,
      });
      setData(mapped);
      return mapped;
    } finally {
      setLoading(false);
    }
  }, [token]);

  // Aceptar (admin)
  const accept = useCallback(async () => {
    if (!token) {
      const msg = "No hay token de autenticación.";
      setError(msg);
      throw new Error(msg);
    }
    if (!id || !isSysAdmin) {
      const msg = "Operación no permitida.";
      setError(msg);
      throw new Error(msg);
    }
    setLoading(true);
    setError(null);
    try {
      await acceptSolicitudDispositivo(token, id); // 👈 ignoramos body
      return true;                                 // 👈 el caller hará refresh()
    } catch (err: any) {
      setError(err?.message || "Error al aceptar la solicitud");
      throw err;
    } finally {
      setLoading(false);
    }
  }, [token, id, isSysAdmin]);

  // Rechazar (admin)
  const reject = useCallback(async () => {
    if (!token) {
      const msg = "No hay token de autenticación.";
      setError(msg);
      throw new Error(msg);
    }
    if (!id || !isSysAdmin) {
      const msg = "Operación no permitida.";
      setError(msg);
      throw new Error(msg);
    }
    setLoading(true);
    setError(null);
    try {
      await rejectSolicitudDispositivo(token, id); // 👈 ignoramos body
      return true;                                 // 👈 el caller hará refresh()
    } catch (err: any) {
      setError(err?.message || "Error al rechazar la solicitud");
      throw err;
    } finally {
      setLoading(false);
    }
  }, [token, id, isSysAdmin]);

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
