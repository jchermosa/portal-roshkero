import { useEffect, useState, useCallback } from "react";
import { getDeviceTypes } from "../../services/DeviceTypesService";
import type { TipoDispositivoItem } from "../../types";

export function useGetDeviceTypes(token: string | null) {
  const [data, setData] = useState<TipoDispositivoItem[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchData = useCallback(async () => {
    if (!token) {
      setData([]);
      setError(null);
      return;
    }

    setLoading(true);
    setError(null);

    try {
      const deviceTypes = await getDeviceTypes(token); 
      setData(deviceTypes);
    } catch (err) {
      const errorMessage =
        err instanceof Error ? err.message : "Error al cargar tipos de dispositivos";
      setError(errorMessage);
      setData([]);
    } finally {
      setLoading(false);
    }
  }, [token]);

  useEffect(() => {
    fetchData();
  }, [fetchData]);

  const refresh = useCallback(() => {
    fetchData();
  }, [fetchData]);

  return {
    data,
    loading,
    error,
    refresh,
  };
}
