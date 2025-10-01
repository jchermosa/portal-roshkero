import { useEffect, useState } from "react";
type Option = { value: number; label: string };

export function useAvailableDevicesOptions(
  token: string | null,
  tipoDispositivoId?: number
) {
  const [options, setOptions] = useState<Option[]>([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (!token) return;
    setLoading(true);

    const params = new URLSearchParams();
    params.set("page", "0");
    params.set("size", "1000");
    if (tipoDispositivoId) {
      params.set("sortBy", "tipoDispositivo");
      params.set("filterValue", String(tipoDispositivoId));
    } else {
      params.set("sortBy", "default");
    }

    console.log("PARAMS ",params.toString());

    fetch(`/api/v1/admin/sysadmin/devices/allDevicesWithoutOwner?${params.toString()}`, {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then(async (r) => {
        if (!r.ok) throw new Error(await r.text());
        return r.json();
      })
      .then((page) => {
        const content = Array.isArray(page?.content) ? page.content : [];
        setOptions(
          content.map((d: any) => ({
            value: d.idDispositivo,
            label: `${d.nroSerie ?? d.idDispositivo} · ${d.modelo ?? ""}`,
          }))
        );
      })
      .finally(() => setLoading(false));
  }, [token, tipoDispositivoId]);

  return { options, loading };
}
