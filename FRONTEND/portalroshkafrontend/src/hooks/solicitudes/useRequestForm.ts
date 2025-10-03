import { useState, useEffect } from "react";
import { useAuth } from "../../context/AuthContext";
import { 
  getSolicitudById, 
  createSolicitudPermiso,
  createSolicitudBeneficio,
  createSolicitudVacaciones
} from "../../services/RequestService";
import type { SolicitudItem, SolicitudFormData, SolicitudPayload } from "../../types";

export function useRequestForm(
  tipo: "PERMISO" | "BENEFICIO" | "VACACIONES",
  id?: string
) {
  const { token } = useAuth();

  const getInitialData = (): SolicitudFormData => ({
    idSubtipo: undefined,
    fechaInicio: "",
    cantDias: tipo === "VACACIONES" ? 0 : undefined,
    comentario: "",
    monto: undefined,
    fechaFin: "" // solo se usa en vacaciones
  });

  const [data, setData] = useState<SolicitudFormData>(getInitialData());
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [isEditing, setIsEditing] = useState(false);
  const [editable, setEditable] = useState(true);

  // Cargar datos si hay ID (modo edición)
  useEffect(() => {
    if (!id || !token) return;

    setLoading(true);
    setIsEditing(true);

    getSolicitudById(token, id)
      .then((res: SolicitudItem) => {
        if (res.tipoSolicitud === tipo) {
          setData({
            idSubtipo: undefined, 
            fechaInicio: res.fechaInicio,
            cantDias: res.cantDias ?? undefined,
            comentario: res.subTipo ?? "", 
            monto: undefined,
            fechaFin: "" 
          });
          setEditable(res.estado === "P");
        } else {
          setError("Tipo de solicitud no coincide");
        }
      })
      .catch((err: any) => setError(err.message))
      .finally(() => setLoading(false));
  }, [id, token, tipo]);

  const buildPayload = (tipo: "PERMISO" | "BENEFICIO" | "VACACIONES", data: SolicitudFormData): SolicitudPayload => {
    if (tipo === "PERMISO") {
      return {
        id_tipo_permiso: data.idSubtipo,
        fecha_inicio: data.fechaInicio,
        cant_dias: data.cantDias ?? null,
        comentario: data.comentario || "",
      };
    }

    if (tipo === "BENEFICIO") {
      return {
        id_tipo_beneficio: data.idSubtipo,
        fecha_inicio: data.fechaInicio,
        cant_dias: data.cantDias ?? null,
        comentario: data.comentario || "",
        monto: data.monto ?? 0,
      };
    }

    if (tipo === "VACACIONES") {
      return {
        fecha_inicio: data.fechaInicio,
        fecha_fin: data.fechaFin || "",
      };
    }

    throw new Error("Tipo de solicitud desconocido");
  };

  const handleSubmit = async (formData: SolicitudFormData): Promise<boolean> => {
    if (!token) {
      setError("No hay token de autenticación");
      return false;
    }

    setLoading(true);
    setError(null);

    try {
      const payload = buildPayload(tipo, formData);

      if (tipo === "PERMISO") {
        await createSolicitudPermiso(token, payload);
      } else if (tipo === "BENEFICIO") {
        await createSolicitudBeneficio(token, payload);
      } else {
        await createSolicitudVacaciones(token, payload);
      }
      return true;
    } catch (err: any) {
      setError(err.message || "Error al guardar la solicitud");
      return false;
    } finally {
      setLoading(false);
    }
  };

  return {
    data,
    setData,
    loading,
    error,
    handleSubmit,
    isEditing,
    editable,
  };
}
