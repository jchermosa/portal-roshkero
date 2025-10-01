import { useCallback, useRef, useState } from "react";
import {
  getClienteById,
  createCliente,
  updateCliente,
  deleteCliente,
} from "../../services/ClientesService";
import type { ClienteRequest, ClienteResponse } from "../../types";

type State = {
  item?: ClienteResponse;
  loading: boolean;   
  saving: boolean;    
  deleting: boolean;  
  error?: string;
};

type Options = {
  onSaved?: (item: ClienteResponse) => void;
  onDeleted?: (id: number) => void;
};

export function useClientesForm(token: string | null, opts: Options = {}) {
  const [state, setState] = useState<State>({
    loading: false,
    saving: false,
    deleting: false,
  });
  const isMounted = useRef(true);

  const safeSet = useCallback((updater: (prev: State) => State) => {
    if (isMounted.current) setState(updater);
  }, []);

  // Cargar por ID (para edición/vista)
  const loadById = useCallback(
    async (id: number) => {
      if (!token) {
        safeSet((s) => ({ ...s, error: "No hay token de autenticación." }));
        return;
      }
      safeSet((s) => ({ ...s, loading: true, error: undefined }));
      try {
        const item = await getClienteById(token, id);
        safeSet((s) => ({ ...s, item, loading: false }));
        return item;
      } catch (e: any) {
        safeSet((s) => ({ ...s, loading: false, error: e?.message || "Error al cargar el cliente" }));
        return undefined;
      }
    },
    [token, safeSet]
  );

  // Crear
  const create = useCallback(
    async (payload: ClienteRequest) => {
      if (!token) {
        safeSet((s) => ({ ...s, error: "No hay token de autenticación." }));
        return;
      }
      safeSet((s) => ({ ...s, saving: true, error: undefined }));
      try {
        const saved = await createCliente(token, payload);
        safeSet((s) => ({ ...s, item: saved, saving: false }));
        opts.onSaved?.(saved);
        return saved;
      } catch (e: any) {
        safeSet((s) => ({ ...s, saving: false, error: e?.message || "Error al crear el cliente" }));
        return undefined;
      }
    },
    [token, safeSet, opts]
  );

  // Actualizar
  const update = useCallback(
    async (id: number, payload: ClienteRequest) => {
      if (!token) {
        safeSet((s) => ({ ...s, error: "No hay token de autenticación." }));
        return;
      }
      safeSet((s) => ({ ...s, saving: true, error: undefined }));
      try {
        const saved = await updateCliente(token, id, payload);
        safeSet((s) => ({ ...s, item: saved, saving: false }));
        opts.onSaved?.(saved);
        return saved;
      } catch (e: any) {
        safeSet((s) => ({ ...s, saving: false, error: e?.message || "Error al actualizar el cliente" }));
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
        await deleteCliente(token, id);
        safeSet((s) => ({ ...s, deleting: false }));
        opts.onDeleted?.(id);
        return true;
      } catch (e: any) {
        safeSet((s) => ({ ...s, deleting: false, error: e?.message || "Error al eliminar el cliente" }));
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
