import { useEffect, useState } from "react";
import { getDispositivosAsignados } from "../../services/DeviceAssignmentService";
import type { DispositivoAsignadoItem } from "../../types";

export function useDispositivosAsignados(token: string | null) {
  const [data, setData] = useState<DispositivoAsignadoItem[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!token) return;

    setLoading(true);
    getDispositivosAsignados(token)
      .then(setData)
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, [token]);

  return { data, loading, error };
}
