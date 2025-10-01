import { useCallback, useRef, useState } from "react";
import {
  getTecnologiaById,
  createTecnologia,
  updateTecnologia,
  deleteTecnologia,
} from "../../services/TecnologiasService";
import type { TecnologiaRequest, TecnologiaResponse } from "../../types";

type State = {
  item?: TecnologiaResponse;
  loading: boolean;   // cargar por ID
  saving: boolean;    // crear/editar
  deleting: boolean;  // eliminar
  error?: string;
};

type Options = {
  onSaved?: (item: TecnologiaResponse) => void;
  onDeleted?: (id: number) => void;
};

export function useTecnologiasForm(token: string | null, opts: Options = {}) {
  const [state, setState] = useState<State>({
    loading: false,
    saving: false,
    deleting: false,
  });
  const isMounted = useRef(true);

  const safeSet = useCallback((updater: (prev: State) => State) => {
    if (isMounted.current) setState(updater);
  }, []);

  // Cargar por ID
  const loadById = useCallback(
    async (id: number) => {
      if (!token) {
        safeSet((s) => ({ ...s, error: "No hay token de autenticación." }));
        return;
      }
      safeSet((s) => ({ ...s, loading: true, error: undefined }));
      try {
        const item = await getTecnologiaById(token, id);
        safeSet((s) => ({ ...s, item, loading: false }));
        return item;
      } catch (e: any) {
        safeSet((s) => ({
          ...s,
          loading: false,
          error: e?.message || "Error al cargar la tecnología",
        }));
        return undefined;
      }
    },
    [token, safeSet]
  );

  // Crear
  const create = useCallback(
    async (payload: TecnologiaRequest) => {
      if (!token) {
        safeSet((s) => ({ ...s, error: "No hay token de autenticación." }));
        return;
      }
      safeSet((s) => ({ ...s, saving: true, error: undefined }));
      try {
        const saved = await createTecnologia(token, payload);
        safeSet((s) => ({ ...s, item: saved, saving: false }));
        opts.onSaved?.(saved);
        return saved;
      } catch (e: any) {
        safeSet((s) => ({
          ...s,
          saving: false,
          error: e?.message || "Error al crear la tecnología",
        }));
        return undefined;
      }
    },
    [token, safeSet, opts]
  );

  // Actualizar
  const update = useCallback(
    async (id: number, payload: TecnologiaRequest) => {
      if (!token) {
        safeSet((s) => ({ ...s, error: "No hay token de autenticación." }));
        return;
      }
      safeSet((s) => ({ ...s, saving: true, error: undefined }));
      try {
        const saved = await updateTecnologia(token, id, payload);
        safeSet((s) => ({ ...s, item: saved, saving: false }));
        opts.onSaved?.(saved);
        return saved;
      } catch (e: any) {
        safeSet((s) => ({
          ...s,
          saving: false,
          error: e?.message || "Error al actualizar la tecnología",
        }));
        return undefined;
      }
    },
    [token, safeSet, opts]
  );

  // Eliminar
  const remove = useCallback(
    async (id: number) => {
      if (!token) {
        safeSet((s) => ({ ...s, error: "No hay token de autenticación." }));
        return false;
      }
      safeSet((s) => ({ ...s, deleting: true, error: undefined }));
      try {
        await deleteTecnologia(token, id);
        safeSet((s) => ({ ...s, deleting: false }));
        opts.onDeleted?.(id);
        return true;
      } catch (e: any) {
        safeSet((s) => ({
          ...s,
          deleting: false,
          error: e?.message || "Error al eliminar la tecnología",
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
  };
}
