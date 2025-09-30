import { useState, useEffect } from "react";
import { useAuth } from "../../context/AuthContext";
import { getSolicitudById, createSolicitud, updateSolicitud } from "../../services/RequestService";
import type { SolicitudItem } from "../../types";

export function useRequestForm(id?: string) {
  const { token, user } = useAuth();

  const [data, setData] = useState<SolicitudItem>({
    id: 0,
    id_usuario: user?.id || 0,
    nombre: user?.nombre || "",
    apellido: user?.apellido || "",
    tipo_solicitud: "PERMISO",
    id_subtipo: 0,
    comentario: "",
    fecha_inicio: "",
    cant_dias: 0,
    fecha_fin: "", // no editable, backend calcula
    estado: "P",
  });

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
      .then((res) => {
        if (res.tipo_solicitud === "PERMISO") {
          setData(res);
          setEditable(res.estado === "P"); // editable solo si pendiente
        }
      })
      .catch((err: any) => setError(err.message))
      .finally(() => setLoading(false));
  }, [id, token]);

  const handleSubmit = async (formData: SolicitudItem) => {
    if (!token) return;
    setLoading(true);
    try {
      if (isEditing && id) {
        await updateSolicitud(token, id, formData);
      } else {
        await createSolicitud(token, formData);
      }
    } catch (err: any) {
      setError(err.message);
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
