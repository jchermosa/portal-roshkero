import { useEffect, useState } from "react";
import type { UsuarioItem } from "../../types";
import {
  getUsuarioById,
  createUsuario,
  updateUsuario,
} from "../../services/UserService";
import { EstadoActivoInactivo } from "../../types";

export function useUsuarioForm(
  token: string | null,
  id?: string, // viene de useParams
  cedulaParam?: number
) {
  const isEditing = !!id;

  const [data, setData] = useState<Partial<UsuarioItem>>({
    nroCedula: cedulaParam ? String(cedulaParam) : undefined,
    requiereCambioContrasena: true,
    estado: "A", // default = Activo
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Cargar usuario si estamos editando
  useEffect(() => {
    if (!token || !isEditing || !id) return;

    setLoading(true);
    getUsuarioById(token, Number(id))
      .then((res) => {
        setData({
          ...res,
          idRol: res.idRol,
          idCargo: res.idCargo,
          estado: res.estado ?? "A",
          requiereCambioContrasena: res.requiereCambioContrasena ?? false,
        });
      })
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, [token, id, isEditing]);

  // 🔄 Crear o actualizar
  const handleSubmit = async (
    formData: Partial<UsuarioItem>
  ): Promise<boolean> => {
    if (!token) return false;

    // Validaciones mínimas
    if (
      !formData.nroCedula?.trim() ||
      !formData.nombre?.trim() ||
      !formData.apellido?.trim() ||
      formData.idRol == null ||
      formData.idCargo == null
    ) {
      setError("Faltan datos obligatorios");
      return false;
    }

    setLoading(true);
    setError(null);

    try {
      if (isEditing && id) {
        await updateUsuario(token, Number(id), formData);
      } else {
        await createUsuario(token, formData);
      }
      return true;
    } catch (err: any) {
      console.error("Error en handleSubmit:", err);
      setError(err.message || "Error al guardar el usuario");
      return false;
    } finally {
      setLoading(false);
    }
  };

  return { data, setData, loading, error, handleSubmit, isEditing };
}
