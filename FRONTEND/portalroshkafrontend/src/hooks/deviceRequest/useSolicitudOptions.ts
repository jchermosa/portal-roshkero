import { useEffect, useState } from "react";
type Option = { value: number; label: string };

export function useSolicitudOptions(
  token: string | null,
  solicitudPreasignada?: number
) {
  const [options, setOptions] = useState<Option[]>(
    solicitudPreasignada
      ? [{ value: solicitudPreasignada, label: `Solicitud #${solicitudPreasignada}` }]
      : []
  );
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (!token || solicitudPreasignada) return;
    setLoading(true);

    fetch("/api/v1/admin/sysadmin/allRequests?type=dispositivo&estado=A", {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then(async (r) => {
        if (!r.ok) throw new Error(await r.text());
        return r.json();
      })
      .then((page) => {
        const content = Array.isArray(page?.content) ? page.content : [];
        setOptions(
          content.map((s: any) => ({
            value: s.idSolicitud ?? s.id ?? 0,
            label: `#${s.idSolicitud ?? s.id} · ${s.comentario ?? ""}`,
          }))
        );
      })
      .finally(() => setLoading(false));
  }, [token, solicitudPreasignada]);

  return { options, loading };
}
