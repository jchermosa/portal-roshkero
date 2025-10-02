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
        console.log("Usuario cargado desde backend:", res);
        
        // Los datos ya vienen con idRol e idCargo correctos del backend
        const mappedData = {
          ...res,
          // El backend ya devuelve idRol e idCargo correctamente
          idRol: res.idRol,
          idCargo: res.idCargo,
          estado: res.estado, // Mantener el formato original del backend ("A"/"I")
          requiereCambioContrasena: res.requiereCambioContrasena ?? false,
        };
        
        console.log("Datos mapeados para el formulario:", mappedData);
        setData(mappedData);
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
      return true;
    } catch (err: unknown) {
      console.error("Error en handleSubmit:", err);
      setError(err instanceof Error ? err.message : "Error al guardar el usuario");
      return false;
    } finally {
      setLoading(false);
    }
  };

  return { data, setData, loading, error, handleSubmit, isEditing };
}
