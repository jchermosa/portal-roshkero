// src/hooks/cargos/useCargoForm.ts
import { useCallback, useEffect, useRef, useState } from "react";
import {
  getCargoById,
  createCargo,
  updateCargo,
  deleteCargo,
} from "../../services/CargosService";
import type {
  CargoDetail,
  CargoInsert,
  CargoActionResponse,
} from "../../types";

type State = {
  item?: CargoDetail;
  loading: boolean;  
  saving: boolean;    
  deleting: boolean; 
  error?: string;     
  success?: string;   
};

type Options = {
  onSaved?: (res: CargoActionResponse | CargoDetail) => void;
  onDeleted?: (idCargo: number) => void;
};

export function useCargoForm(token: string | null, opts: Options = {}) {
  const [state, setState] = useState<State>({
    loading: false,
    saving: false,
    deleting: false,
    error: undefined,
    success: undefined,
  });

  const isMounted = useRef(true);
  useEffect(() => {
    return () => {
      isMounted.current = false;
    };
  }, []);

  const safeSet = useCallback((updater: (prev: State) => State) => {
    if (isMounted.current) setState(updater);
  }, []);

  
  const clearMessages = useCallback(() => {
    safeSet((s) => ({ ...s, error: undefined, success: undefined }));
  }, [safeSet]);

  /** Cargar por ID (detalle) */
  const loadById = useCallback(
    async (idCargo: number) => {
      if (!token) {
        safeSet((s) => ({
          ...s,
          error: "No hay token de autenticación.",
          success: undefined,
        }));
        return;
      }
      safeSet((s) => ({
        ...s,
        loading: true,
        error: undefined,
        success: undefined,
      }));
      try {
        const item = await getCargoById(token, idCargo);
        safeSet((s) => ({ ...s, item, loading: false }));
        return item;
      } catch (e: any) {
        safeSet((s) => ({
          ...s,
          loading: false,
          error: e?.message || "Error al cargar el cargo",
          success: undefined,
        }));
        return undefined;
      }
    },
    [token, safeSet]
  );

  /** Crear */
  const create = useCallback(
    async (payload: CargoInsert) => {
      if (!token) {
        safeSet((s) => ({
          ...s,
          error: "No hay token de autenticación.",
          success: undefined,
        }));
        return undefined;
      }
      safeSet((s) => ({
        ...s,
        saving: true,
        error: undefined,
        success: undefined,
      }));
      try {
        const res = await createCargo(token, payload);
        safeSet((s) => ({
          ...s,
          saving: false,
          success: res?.message || "Cargo creado correctamente.",
        }));
        opts.onSaved?.(res);
        return res;
      } catch (e: any) {
        safeSet((s) => ({
          ...s,
          saving: false,
          error: e?.message || "Error al crear el cargo",
          success: undefined,
        }));
        return undefined;
      }
    },
    [token, safeSet, opts]
  );

  /** Actualizar */
  const update = useCallback(
    async (idCargo: number, payload: CargoInsert) => {
      if (!token) {
        safeSet((s) => ({
          ...s,
          error: "No hay token de autenticación.",
          success: undefined,
        }));
        return undefined;
      }
      safeSet((s) => ({
        ...s,
        saving: true,
        error: undefined,
        success: undefined,
      }));
      try {
        const res = await updateCargo(token, idCargo, payload);
        safeSet((s) => ({
          ...s,
          saving: false,
          success: res?.message || "Cargo actualizado correctamente.",
        }));
        opts.onSaved?.(res);
        return res;
      } catch (e: any) {
        safeSet((s) => ({
          ...s,
          saving: false,
          error: e?.message || "Error al actualizar el cargo",
          success: undefined,
        }));
        return undefined;
      }
    },
    [token, safeSet, opts]
  );

  /** Eliminar */
  const remove = useCallback(
    async (idCargo: number) => {
      if (!token) {
        safeSet((s) => ({
          ...s,
          error: "No hay token de autenticación.",
          success: undefined,
        }));
        return false;
      }
      safeSet((s) => ({
        ...s,
        deleting: true,
        error: undefined,
        success: undefined,
      }));
      try {
        const res = await deleteCargo(token, idCargo);
        safeSet((s) => ({
          ...s,
          deleting: false,
          success: res?.message || "Cargo eliminado correctamente.",
        }));
        opts.onDeleted?.(idCargo);
        return true;
      } catch (e: any) {
        safeSet((s) => ({
          ...s,
          deleting: false,
          error:
            e?.message ||
            "No se pudo eliminar el cargo (verifica que no tenga usuarios asignados).",
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
