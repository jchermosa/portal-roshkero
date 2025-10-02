// src/hooks/roles/useRolForm.ts
import { useCallback, useEffect, useRef, useState } from "react";
import {
  getRolById,
  createRol,
  updateRol,
  deleteRol,
} from "../../services/RolesService";
import type { RolDetail, RolInsert, RolActionResponse } from "../../types";

type State = {
  item?: RolDetail;
  loading: boolean;   // loadById
  saving: boolean;    // create/update
  deleting: boolean;  // delete
  error?: string;     // <Alert kind="error">{error}</Alert>
  success?: string;   // <Alert kind="success">{success}</Alert>
};

type Options = {
  onSaved?: (res: RolActionResponse | RolDetail) => void;
  onDeleted?: (idRol: number) => void;
};

export function useRolForm(token: string | null, opts: Options = {}) {
  const [state, setState] = useState<State>({
    loading: false,
    saving: false,
    deleting: false,
    error: undefined,
    success: undefined,
  });

  const isMounted = useRef(true);
  useEffect(() => {
    return () => { isMounted.current = false; };
  }, []);

  const safeSet = useCallback((updater: (prev: State) => State) => {
    if (isMounted.current) setState(updater);
  }, []);

  /** Limpia mensajes (útil al cambiar de página/cerrar modal) */
  const clearMessages = useCallback(() => {
    safeSet((s) => ({ ...s, error: undefined, success: undefined }));
  }, [safeSet]);

  /** Cargar detalle por id */
  const loadById = useCallback(
    async (idRol: number) => {
      if (!token) {
        safeSet((s) => ({ ...s, error: "No hay token de autenticación.", success: undefined }));
        return;
      }
      safeSet((s) => ({ ...s, loading: true, error: undefined, success: undefined }));
      try {
        const item = await getRolById(token, idRol);
        safeSet((s) => ({ ...s, item, loading: false }));
        return item;
      } catch (e: any) {
        safeSet((s) => ({
          ...s,
          loading: false,
          error: e?.message || "Error al cargar el rol",
          success: undefined,
        }));
        return undefined;
      }
    },
    [token, safeSet]
  );

  /** Crear */
  const create = useCallback(
    async (payload: RolInsert) => {
      if (!token) {
        safeSet((s) => ({ ...s, error: "No hay token de autenticación.", success: undefined }));
        return undefined;
      }
      safeSet((s) => ({ ...s, saving: true, error: undefined, success: undefined }));
      try {
        const res = await createRol(token, payload);
        safeSet((s) => ({ ...s, saving: false, success: res?.message || "Rol creado correctamente." }));
        opts.onSaved?.(res);
        return res;
      } catch (e: any) {
        safeSet((s) => ({
          ...s,
          saving: false,
          error: e?.message || "Error al crear el rol",
          success: undefined,
        }));
        return undefined;
      }
    },
    [token, safeSet, opts]
  );

  /** Actualizar */
  const update = useCallback(
    async (idRol: number, payload: RolInsert) => {
      if (!token) {
        safeSet((s) => ({ ...s, error: "No hay token de autenticación.", success: undefined }));
        return undefined;
      }
      safeSet((s) => ({ ...s, saving: true, error: undefined, success: undefined }));
      try {
        const res = await updateRol(token, idRol, payload);
        safeSet((s) => ({ ...s, saving: false, success: res?.message || "Rol actualizado correctamente." }));
        opts.onSaved?.(res);
        return res;
      } catch (e: any) {
        safeSet((s) => ({
          ...s,
          saving: false,
          error: e?.message || "Error al actualizar el rol",
          success: undefined,
        }));
        return undefined;
      }
    },
    [token, safeSet, opts]
  );

  /** Eliminar */
  const remove = useCallback(
    async (idRol: number) => {
      if (!token) {
        safeSet((s) => ({ ...s, error: "No hay token de autenticación.", success: undefined }));
        return false;
      }
      safeSet((s) => ({ ...s, deleting: true, error: undefined, success: undefined }));
      try {
        const res = await deleteRol(token, idRol);
        safeSet((s) => ({ ...s, deleting: false, success: res?.message || "Rol eliminado correctamente." }));
        opts.onDeleted?.(idRol);
        return true;
      } catch (e: any) {
        safeSet((s) => ({
          ...s,
          deleting: false,
          error:
            e?.message ||
            "No se pudo eliminar el rol (verifica que no tenga usuarios asignados).",
          success: undefined,
        }));
        return false;
      }
    },
    [token, safeSet, opts]
  );

  return {
    ...state,        
    loadById,
    create,
    update,
    remove,
    clearMessages,
  };
}
