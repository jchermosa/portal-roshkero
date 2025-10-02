import { useEffect, useState } from "react";
import { getDispositivoById } from "../../services/DeviceService";
type Option = { value: number; label: string };

// Ahora el segundo parámetro representa el id del dispositivo seleccionado (cuando se edita)
export function useAvailableDevicesOptions(
  token: string | null,
  selectedDeviceId?: number
) {
  const [options, setOptions] = useState<Option[]>([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (!token) return;
    setLoading(true);

    const params = new URLSearchParams();
    params.set("page", "0");
    params.set("size", "1000");
    params.set("sortBy", "default");

    fetch(`http://localhost:8080/api/v1/admin/sysadmin/devices/allDevicesWithoutOwner?${params.toString()}`, {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then(async (r) => {
        if (!r.ok) throw new Error(await r.text());
        return r.json();
      })
      .then(async (page) => {
        const content = Array.isArray(page?.content) ? page.content : [];
        const baseOptions: Option[] = content.map((d: any) => ({
          value: d.idDispositivo,
          label: `${d.nroSerie ?? d.idDispositivo} · ${d.modelo ?? ""}`,
        }));

        // Si estamos editando, incluir el dispositivo asignado actual aunque no esté en la lista
        if (selectedDeviceId) {
          try {
            const sel = await getDispositivoById(token, selectedDeviceId);
            const selOption: Option = {
              value: sel.idDispositivo ?? selectedDeviceId,
              label: `${sel.nroSerie ?? sel.idDispositivo} · ${sel.modelo ?? ""}`,
            };
            const exists = baseOptions.find((o) => o.value === selOption.value);
            if (!exists) baseOptions.unshift(selOption);
          } catch (e) {
            // ignore — si no se puede obtener el dispositivo no rompemos la carga
            // console.warn("No se pudo cargar dispositivo seleccionado:", e);
          }
        }

        setOptions(baseOptions);
      })
      .catch(() => {
        setOptions([]);
      })
      .finally(() => setLoading(false));
  }, [token, selectedDeviceId]);

  return { options, loading };
}
