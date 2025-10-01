import { useCallback, useEffect, useMemo, useRef, useState } from "react";
import type { DispositivoItem } from "../../types";
import { EstadoInventarioEnum } from "../../types";
import {
  getDispositivoById,
  createDispositivo,
  updateDispositivo,
  deleteDispositivo,
} from "../../services/DeviceService";

type State = {
  data: Partial<DispositivoItem>;
  loading: boolean;   
  saving: boolean;    
  deleting: boolean; 
  error?: string;
  success?: string;
};

export function useDispositivoForm(token: string | null, id?: number | string) {
  const numericId = useMemo(() => {
    if (id === null || id === undefined) return undefined;
    const n = Number(id);
    return Number.isFinite(n) ? n : undefined;
  }, [id]);

  const isEditing = numericId !== undefined;

  const [state, setState] = useState<State>({
    data: {
      // Campos por defecto para crear (ajusta según tus DTOs)
      estado: EstadoInventarioEnum.D, // Disponible
    },
    loading: false,
    saving: false,
    deleting: false,
    error: undefined,
    success: undefined,
  });

  
  // Evita setState en componente desmontado
  const mounted = useRef(true);
  useEffect(() => () => { mounted.current = false; }, []);
  const safeSet = useCallback((updater: (prev: State) => State) => {
    if (mounted.current) setState(updater);
  }, []);

  // Helper para setear data (two-way binding con forms controlados)
  const setData = useCallback((updater: Partial<DispositivoItem> | ((prev: Partial<DispositivoItem>) => Partial<DispositivoItem>)) => {
    safeSet((s) => ({
      ...s,
      data: typeof updater === "function" ? (updater as any)(s.data) : { ...s.data, ...updater },
    }));
  }, [safeSet]);

  // Cargar detalle si es edición
  const loadById = useCallback(async () => {
    if (!token || !isEditing || numericId === undefined) return;
    safeSet((s) => ({ ...s, loading: true, error: undefined, success: undefined }));
    try {
      const item = await getDispositivoById(token, numericId);
      safeSet((s) => ({ ...s, data: item, loading: false }));
      return item;
    } catch (e: any) {
      safeSet((s) => ({ ...s, loading: false, error: e?.message || "Error al cargar el dispositivo" }));
      return undefined;
    }
  }, [token, isEditing, numericId, safeSet]);

  useEffect(() => { loadById(); }, [loadById]);

  // Guardar (crear/actualizar)
  const handleSubmit = useCallback(async (formData: Partial<DispositivoItem>) => {
    if (!token) return;
    safeSet((s) => ({ ...s, saving: true, error: undefined, success: undefined }));
    try {
      if (isEditing && numericId !== undefined) {
        const res = await updateDispositivo(token, numericId, formData);
        safeSet((s) => ({ ...s, saving: false, success: "Dispositivo actualizado correctamente." }));
        return res;
      } else {
        const res = await createDispositivo(token, formData);
        safeSet((s) => ({ ...s, saving: false, success: "Dispositivo creado correctamente." }));
        return res;
      }
    } catch (e: any) {
      safeSet((s) => ({ ...s, saving: false, error: e?.message || "Error al guardar el dispositivo" }));
      throw e;
    }
  }, [token, isEditing, numericId, safeSet]);

  // Eliminar
  const remove = useCallback(async () => {
    if (!token || numericId === undefined) return false;
    safeSet((s) => ({ ...s, deleting: true, error: undefined, success: undefined }));
    try {
      await deleteDispositivo(token, numericId);
      safeSet((s) => ({ ...s, deleting: false, success: "Dispositivo eliminado correctamente." }));
      return true;
    } catch (e: any) {
      safeSet((s) => ({ ...s, deleting: false, error: e?.message || "Error al eliminar el dispositivo" }));
      return false;
    }
  }, [token, numericId, safeSet]);

  const clearMessages = useCallback(() => {
    safeSet((s) => ({ ...s, error: undefined, success: undefined }));
  }, [safeSet]);

  return {
    data: state.data,
    setData,
    loading: state.loading,
    saving: state.saving,
    deleting: state.deleting,
    error: state.error,
    success: state.success,
    isEditing,
    loadById,
    handleSubmit,
    remove,
    clearMessages,
  };
}
