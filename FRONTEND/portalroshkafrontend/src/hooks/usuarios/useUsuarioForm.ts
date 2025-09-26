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
  cedulaParam?: number
) {
  const isEditing = !!id;

  const [data, setData] = useState<Partial<UsuarioItem>>({
    nro_cedula: cedulaParam ? String(cedulaParam) : undefined,
    requiere_cambio_contrasena: true,
    contrasena: "usuario123", // Contraseña por defecto
    estado: "ACTIVO",
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!token || !isEditing || !id) return;

    setLoading(true);
    getUsuarioById(token, id)
      .then((res) => {
        setData({
          ...res,
          estado: res.estado ?? "ACTIVO",
          requiere_cambio_contrasena: res.requiere_cambio_contrasena ?? false,
        });
      })
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, [token, id, isEditing]);

  const handleSubmit = async (formData: Partial<UsuarioItem>) => {
    if (!token) return;

    if (
      !formData.nro_cedula ||
      !formData.nombre ||
      !formData.apellido ||
      !formData.id_rol ||
      !formData.id_cargo
    ) {
      setError("Faltan datos obligatorios");
      return;
    }

    try {
      if (isEditing && id) {
        await updateUsuario(token, id, formData);
      } else {
        await createUsuario(token, formData);
      }
    } catch (err: any) {
      setError(err.message || "Error al guardar el usuario");
      throw err;
    }
  };

  return { data, setData, loading, error, handleSubmit, isEditing };
}
