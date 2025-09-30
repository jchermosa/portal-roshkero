import { useEffect, useState } from "react";
import {
  getSolicitudById,
  aprobarSolicitud,
  rechazarSolicitud,
} from "../../services/RequestTHService";
import type { SolicitudItem } from "../../types";

export function useRequestView(token: string | null, id: string | null) {
  const [solicitud, setSolicitud] = useState<SolicitudItem | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [procesando, setProcesando] = useState(false);

  useEffect(() => {
    if (!token || !id) {
      setSolicitud(null);
      setLoading(false);
      return;
    }

    const fetchSolicitud = async () => {
      setLoading(true);
      setError(null);

      try {
        const result = await getSolicitudById(token, id);
        setSolicitud(result);
      } catch (err) {
        setError(err instanceof Error ? err.message : "Error desconocido");
      } finally {
        setLoading(false);
      }
    };

    fetchSolicitud();
  }, [token, id]);

  const aprobar = async (): Promise<boolean> => {
  if (!token || !id || !solicitud) {
    setError("Datos insuficientes para aprobar la solicitud");
    return false;
  }

  setProcesando(true);
  setError(null);

  try {
    await aprobarSolicitud(token, id);   
    setSolicitud((prev) => (prev ? { ...prev, estado: "A" } : null));
    return true;
  } catch (err) {
    setError(err instanceof Error ? err.message : "Error al aprobar la solicitud");
    return false;
  } finally {
    setProcesando(false);
  }
};


  const rechazar = async (motivo?: string): Promise<boolean> => {
    if (!token || !id || !solicitud) {
      setError("Datos insuficientes para rechazar la solicitud");
      return false;
    }

    setProcesando(true);
    setError(null);

    try {
      await rechazarSolicitud(token, id, motivo ?? ""); 
      setSolicitud((prev) => (prev ? { ...prev, estado: "R" } : null));
      return true;
    } catch (err) {
      setError(err instanceof Error ? err.message : "Error al rechazar la solicitud");
      return false;
    } finally {
      setProcesando(false);
    }
  };

  return {
    solicitud,
    loading,
    error,
    procesando,
    aprobar,
    rechazar,
    puedeEvaluar: solicitud?.estado === "P",
    esPermiso: solicitud?.tipoSolicitud === "PERMISO",
    esBeneficio: solicitud?.tipoSolicitud === "BENEFICIO",
    esVacaciones: solicitud?.tipoSolicitud === "VACACIONES"
  };
}
