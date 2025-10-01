// src/hooks/useUsuarioForm.ts
import { useEffect, useState } from "react";
import type { UsuarioItem } from "../../types";
import {
  getUsuarioById,
  createUsuario,
  updateUsuario,
} from "../../services/UserService";

export function useUsuarioForm(
  token: string | null,
  id?: string,
  cedulaParam?: string
) {
  const isEditing = !!id;

  // Estado inicial (creación)
  const [data, setData] = useState<Partial<UsuarioItem>>({
    nroCedula: cedulaParam || "",
    estado: true,
    requiereCambioContrasena: true,
    contrasena: "usuario123", // Contraseña por defecto
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Cargar usuario si es edición
  useEffect(() => {
    if (!token || !isEditing || !id) return;

    setLoading(true);
    getUsuarioById(token, id)
      .then((res) => {
        setData({
          ...res,
          estado: res.estado ?? true,
          requiereCambioContrasena: res.requiereCambioContrasena ?? false,
        });
      })
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, [token, id, isEditing]);

  // Guardar (crear o actualizar)
  const handleSubmit = async (formData: Partial<UsuarioItem>) => {
    if (!token) return;

    try {
      if (isEditing && id) {
        await updateUsuario(token, id, formData);
      } else {
        await createUsuario(token, formData);
      }
    } catch (err: any) {
      setError(err.message || "Error al guardar el usuario");
      throw err; // importante para que la Page pueda reaccionar
    }
  };

  return { data, setData, loading, error, handleSubmit, isEditing };
}
